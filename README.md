# GSOI Automotive OS

Sistema operativo embedded per l'automobile, costruito con **Yocto** e pensato
per **Raspberry Pi 5**. È **offline-first**: al boot mostra un cockpit a schermo
intero (Qt/QML su Wayland) e ospita **a bordo** l'assistente GSOI, con un
modello LLM che gira **localmente** — nessuna dipendenza da un server per
"pensare".

> **Filosofia:** l'auto deve funzionare anche completamente senza rete. Il
> cervello (LLM) è sul dispositivo; la connettività, quando c'è, serve solo
> alle funzioni che la richiedono (traffico, meteo, aggiornamenti OTA).

---

## Indice

- [Cos'è e com'è fatto](#cosè-e-comè-fatto)
- [L'ecosistema GSOI](#lecosistema-gsoi)
- [Architettura al boot](#architettura-al-boot)
- [Componenti del layer `meta-gsoi`](#componenti-del-layer-meta-gsoi)
- [Build](#build)
  - [Test rapido in QEMU](#test-rapido-in-qemu)
  - [Immagine per Raspberry Pi 5](#immagine-per-raspberry-pi-5)
- [Il cervello locale (LLM)](#il-cervello-locale-llm)
- [Aggiornare il modello](#aggiornare-il-modello)
- [Schermi e quadro strumenti](#schermi-e-quadro-strumenti)
- [Hardware](#hardware)
- [Sicurezza](#sicurezza)
- [Struttura della repo](#struttura-della-repo)

---

## Cos'è e com'è fatto

- **Base:** immagine Yocto (`gsoi-automotive-image`), distro `gsoi-automotive`,
  init **systemd**, grafica **Wayland/Weston**.
- **Target:** `raspberrypi5` (produzione) e `qemux86-64` / `qemuarm64` (test).
- **Interfaccia:** cockpit **gsoi-cockpit** (Qt6/QML) avviato a schermo intero
  da Weston in modalità kiosk.
- **Assistente:** **gsoi-jarvis-mini** (Car Agent) come servizio systemd, che
  interroga un **modello LLM locale** servito da **llama.cpp** su `127.0.0.1`.

I layer upstream (OE-Core, meta-raspberrypi, meta-qt6, meta-openembedded) **non**
sono versionati qui: li clona `scripts/setup-env.sh` ai commit esatti indicati
in `UPSTREAM_VERSIONS.txt`. Questa repo contiene solo la parte "nostra": il
layer **`meta-gsoi`** e gli script di setup.

## L'ecosistema GSOI

| Repo | Ruolo |
| --- | --- |
| **gsoi-automotive-os** (questa) | L'OS: distro, immagine, ricette dei componenti |
| [**gsoi-jarvis-mini**](https://github.com/antoniogisondi/gsoi-jarvis-mini) | Il Car Agent (voce, tool, sicurezza) + pipeline di fine-tuning |
| [**gsoi-cockpit**](https://github.com/antoniogisondi/gsoi-cockpit) | L'interfaccia grafica Qt/QML |
| [**gsoi-llm**](https://github.com/antoniogisondi/gsoi-llm) | Ricerca/heritage: LLM italiano addestrato da zero (pre-pivot) |

Il modello di produzione è **Qwen3-4B-Instruct** fine-tunato (QLoRA) e
quantizzato in GGUF, servito da llama.cpp. Il modello si trova su HuggingFace
([`gsoi/gsoi-qwen3-4b-gguf`](https://huggingface.co/gsoi/gsoi-qwen3-4b-gguf)) e
viene scaricato in fase di build.

## Architettura al boot

```
POWER ON
   │
   ▼
 splash GSOI (psplash)  ── niente log kernel a schermo
   │
   ▼
 systemd (multi-user.target)
   ├─ gsoi-model.service     → llama-server (llama.cpp) sul .gguf, su 127.0.0.1:8091
   ├─ jarvis-mini.service    → agente; JARVIS_AI=local → interroga :8091
   ├─ gsoi-reverse.service   → retromarcia + sensori (GPIO/CAN) su :8092    [no-AI]
   ├─ gsoi-vehicled.service  → dati veicolo (CAN sola lettura) su :8093     [no-AI]
   └─ weston (kiosk)         → 2 schermi HDMI, un'app ciascuno (per app_id)
        ├─ HDMI-A-1  gsoi-cockpit  → infotainment (/state da jarvis-mini)
        │                            + overlay retrocamera su retromarcia (:8092)
        └─ HDMI-A-2  gsoi-cluster  → quadro strumenti digitale (:8093)
```

- **Funzioni di sicurezza indipendenti dall'AI:** la retrocamera/sensori
  (`gsoi-reverse`) e il quadro strumenti (`gsoi-vehicled` + `gsoi-cluster`) non
  passano da Jarvis Mini né dal modello. Leggono il veicolo in **sola lettura** e
  restano operative anche con l'assistente spento. Il quadro **si affianca** a
  quello OEM (odometro legale, spie omologate, immobilizer restano nell'OEM).
- **Il cervello non esce dalla macchina:** jarvis-mini parla con gsoi-model su
  `localhost`, senza rete.
- Se il modello non è ancora pronto (carica ~2,5 GB all'avvio) o assente,
  jarvis-mini **ripiega sul mock** e riprende col modello appena disponibile —
  nessun boot bloccato.

## Componenti del layer `meta-gsoi`

| Ricetta / file | Cosa fa |
| --- | --- |
| `recipes-core/images/gsoi-automotive-image.bb` | L'immagine dell'OS (IMAGE_INSTALL, splash, kiosk) |
| `conf/distro/gsoi-automotive.conf` | La distro (offline-first, systemd, Wayland) |
| `recipes-gsoi/jarvis-mini/` | Il Car Agent come servizio systemd (pin per SRCREV) |
| `recipes-gsoi/gsoi-model/` | Servizio `gsoi-model` + launcher `llama-server` + drop-in |
| `recipes-gsoi/cockpit/` | Il cockpit Qt/QML **e** il quadro strumenti (2 eseguibili: `gsoi-cockpit` + `gsoi-cluster`); include la retrocamera + sensori su retromarcia |
| `recipes-gsoi/gsoi-reverse/` | Servizio `gsoi-reverse`: legge la retromarcia (GPIO) + sensori (PDC) e li serve su `127.0.0.1:8092` — **indipendente dall'AI** |
| `recipes-gsoi/gsoi-vehicled/` | Servizio `gsoi-vehicled`: legge i dati di guida dal **CAN (sola lettura)** e li serve su `127.0.0.1:8093` per il quadro strumenti — **indipendente dall'AI** |
| `recipes-support/llama-cpp/` | Compila `llama-server` (motore di inferenza) |
| `recipes-support/gsoi-model-weights/` | Scarica il `.gguf` del modello da HuggingFace |
| `recipes-graphics/` | Splash di boot, Weston kiosk (2 schermi HDMI, un'app per app_id), font |

## Build

Prerequisito: una macchina Linux con gli strumenti di build Yocto. Lo script di
setup clona i layer upstream e genera la configurazione:

```bash
./scripts/setup-env.sh
source layers/openembedded-core/oe-init-build-env build
```

### Test rapido in QEMU

Per **testare anche il modello** conviene `qemux86-64` con **KVM** (gira sulla
CPU dell'host, a velocità reale; l'emulazione `aarch64` sarebbe troppo lenta per
un LLM). In `build/conf/local.conf`:

```bitbake
MACHINE = "qemux86-64"
IMAGE_ROOTFS_EXTRA_SPACE = "4194304"   # spazio per il modello (~2,5 GB)
```

Poi:

```bash
bitbake gsoi-automotive-image
runqemu qemux86-64 kvm slirp snapshot qemuparams="-m 8192 -smp 4"
```

Verifica (via `ssh -p 2222 root@127.0.0.1`, password vuota):

```bash
systemctl is-active gsoi-model     # active
systemctl is-active gsoi-reverse   # active (retrocamera/sensori)
systemctl is-active gsoi-vehicled  # active (dati quadro strumenti)
curl -s 127.0.0.1:8093/vehicle     # JSON animato (mock): velocità, giri, spie…
jarvis-mini cli                    # "Chi sei?" -> risponde GSOI (modello locale)
```

Nella finestra grafica di QEMU: premi **`R`** per simulare la **retromarcia**
(compare la retrocamera + sensori a tutto schermo).

**Provare il quadro strumenti in QEMU** (una sola uscita → di default parte solo
il cockpit): forza l'app cluster con il flag e riavvia Weston:

```bash
mkdir -p /etc/gsoi && echo cluster > /etc/gsoi/ui-mode
systemctl restart weston            # ora lo schermo mostra il quadro strumenti
echo auto > /etc/gsoi/ui-mode && systemctl restart weston   # torna al cockpit
```

Sul **Raspberry Pi 5** con due HDMI collegati non serve il flag: `auto` mette il
cockpit su `HDMI-A-1` e il quadro su `HDMI-A-2`.

Per un'immagine **leggera** senza modello (solo boot + cockpit + agente mock),
togli `gsoi-model-weights` da `IMAGE_INSTALL`.

### Immagine per Raspberry Pi 5

```bitbake
# build/conf/local.conf
MACHINE = "raspberrypi5"
```

```bash
bitbake gsoi-automotive-image
# immagine .wic in:  build/tmp/deploy/images/raspberrypi5/
```

La `.wic` si flasha sulla microSD. Nota: la prima build per aarch64 ricompila
`llama.cpp` (lungo, una volta sola) e usa la build **CPU** (il Pi 5 non ha GPU
per llama).

## Il cervello locale (LLM)

- **gsoi-model** avvia `llama-server` sul primo `.gguf` in `/var/lib/gsoi-model/`
  (cartella mutabile: aggiornabile via OTA senza ricostruire l'immagine), su
  `127.0.0.1:8091`, endpoint OpenAI-compatible.
- **jarvis-mini** con `JARVIS_AI=local` lo interroga: i comandi semplici
  restano intent/tool **deterministici**, il resto va al modello locale.
- Contesto limitato a `-c 4096` per stare nel budget di RAM (8 GB su Pi/Jetson).

Il server e i pesi **non** vengono forniti insieme al codice dell'agente: sono
pacchetti separati (`llama-cpp`, `gsoi-model-weights`) così l'agente resta
leggero e il modello si aggiorna in autonomia.

## Aggiornare il modello

Nuovo dataset → nuovo fine-tuning → nuovo `.gguf`. Ciclo (dalla repo
`gsoi-jarvis-mini`, cartella `finetune/`):

```bash
python train.py --config config.yaml        # addestra (QLoRA, Unsloth)
./publish-model.sh                           # upload HF + stampa 2 righe pronte
```

Poi incolla le due righe stampate in
`layers/meta-gsoi/recipes-support/gsoi-model-weights/gsoi-model-weights.bb`
(URL pinnato alla revisione del commit + `sha256sum`) e ricompila
`bitbake gsoi-automotive-image`.

*Aggiornamento veloce sul dispositivo:* copia il nuovo `.gguf` in
`/var/lib/gsoi-model/` e `systemctl restart gsoi-model` — senza rebuild.

## Schermi e quadro strumenti

Il Raspberry Pi 5 ha **due uscite micro-HDMI**: l'OS pilota **due schermi**.

| Schermo | app_id | App | Sorgente dati |
| --- | --- | --- | --- |
| Centrale (plancia) | `org.gsoi.cockpit` | `gsoi-cockpit` (infotainment) | jarvis-mini `/state` (:8090) |
| Dietro al volante | `org.gsoi.cluster` | `gsoi-cluster` (quadro strumenti) | `gsoi-vehicled` (:8093, CAN sola lettura) |

- **Weston kiosk** assegna ogni app alla sua uscita in base all'app_id
  (`recipes-graphics/weston-init`). Il launcher `gsoi-ui-launch` avvia sempre il
  cockpit e, se rileva una **2ª uscita HDMI collegata**, anche il quadro. Override
  manuale con `/etc/gsoi/ui-mode` (`auto` | `cockpit` | `cluster` | `both`).
- **Il quadro si affianca a quello OEM** (scelta di sicurezza): l'odometro
  legale, le spie omologate e l'immobilizer restano nel quadro originale, che
  rimane nodo del CAN. GSOI **legge** il bus e mostra un quadro digitale
  aggiuntivo — reversibile, senza toccare la rete del veicolo.
- **`gsoi-vehicled`** legge velocità/giri/temperatura/carburante/spie dal CAN in
  **sola lettura**. La mappa degli ID del veicolo (Renault Clio IV 1.5 dCi) è un
  hook da completare sull'hardware (`decode_frame`); finché non c'è CAN
  configurato gira in **mock animato**, così il quadro è visibile in QEMU.

## Hardware

La lista completa dei componenti per l'installazione in auto (schermo, audio,
OBD in sola lettura, alimentazione, microfono), con schema di cablaggio, è in
[`docs/hardware.md`](docs/hardware.md).

## Sicurezza

- **OBD/CAN in sola lettura.** Nessuna scrittura sui bus dei sistemi di
  sicurezza (sterzo, freni, ABS, airbag, ADAS).
- I **comandi veicolo** passano da codice deterministico, **mai** da un LLM che
  potrebbe "inventare" un'azione.
- Sul dispositivo va previsto lo **spegnimento pulito** (per non corrompere il
  filesystem) e un fusibile in linea sull'alimentazione 12 V.

## Struttura della repo

```
gsoi-automotive-os/
├── README.md                  # questo file
├── UPSTREAM_VERSIONS.txt      # commit pinnati dei layer upstream
├── scripts/
│   ├── setup-env.sh           # clona i layer e genera build/conf
│   └── README.md              # dettagli sul setup
├── docs/
│   └── hardware.md            # lista hardware + cablaggio
└── layers/meta-gsoi/          # il layer GSOI (distro, immagine, ricette)
```

I layer upstream e la cartella `build/` sono esclusi da git (vedi `.gitignore`):
si rigenerano con `scripts/setup-env.sh`.
