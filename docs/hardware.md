# GSOI Automotive OS — Hardware da acquistare

Lista completa dei componenti per installare GSOI su una **Renault Clio IV
(2012, 1.5 dCi, con Media Nav)**, riutilizzando la cornice originale e
sostituendo il modulo Media Nav con uno schermo collegato al Raspberry Pi
(**Opzione A**: una sola interfaccia integrata).

> **Filosofia:** GSOI resta agnostico all'hardware. Lo schermo si collega in
> **HDMI + USB touch**, il cockpit si adatta da solo a qualsiasi risoluzione,
> l'audio esce dal Pi verso un amplificatore. OBD/CAN è sempre **sola lettura**.

---

## 0. Legenda

| Simbolo | Significato |
|---|---|
| 🔴 | **Obbligatorio** — senza non funziona |
| 🟡 | **Consigliato** — serve per l'uso reale in auto |
| 🟢 | **Opzionale** — funzione extra / comfort |
| 🧪 | **Solo sviluppo** — utile ora, non va in auto |

Prezzi indicativi in EUR (settembre 2026), variabili per rivenditore.

---

## 1. Cervello — Raspberry Pi 5

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🔴 | **Raspberry Pi 5 (8 GB)** | 8 GB per avere margine su UI + voce | ~80–90 € |
| 🔴 | **Dissipatore attivo** (Active Cooler ufficiale) | in auto fa caldo, indispensabile | ~5–8 € |
| 🔴 | **microSD A2 32/64 GB** *oppure* **SSD NVMe + HAT** | SD per iniziare; NVMe per boot veloce e affidabile | SD ~10 € / NVMe+HAT ~35 € |
| 🟢 | Case/telaio di montaggio dietro il vano | anche stampato 3D | ~10 € |

---

## 2. Schermo (nel vano Media Nav)

Requisiti **inderogabili**: ingresso **HDMI**, touch **USB capacitivo**,
formato **open-frame** (pannello nudo, per incassarlo dietro la tua cornice).

### 2a. Per l'auto (leggibile al sole, 12 V) 🚗

| # | Modello | Caratteristiche | Prezzo |
|---|---|---|---|
| 🟡 | **Faytech 7" IP65 High Brightness HDMI** | HDMI + USB touch, alim. 8–36 V, robusto | ~200–350 € |
| 🟡 | **Xenarc 702CSH 7"** | 1024×600, **1000 nit**, touch USB, HDMI/DVI, 12 V | ~350–450 € |
| 🟡 | **Mimo UM-760CH-OF 7" open-frame** | HDMI, capacitivo, 400 cd/m², pannello nudo | ~150–220 € |

> Scegline **uno**. Preferisci **open-frame** per riutilizzare la cornice OEM.

### 2b. Solo per sviluppo/test a banco 🧪

| # | Modello | Note | Prezzo |
|---|---|---|---|
| 🧪 | **Waveshare 7" 1024×600 IPS capacitivo** | HDMI + USB, driver-free | ~40 € |
| 🧪 | **SunFounder 7" IPS 1024×600** | HDMI + USB, testato su Pi 5 | ~45 € |

> Comodo per provare il cockpit su hardware vero **prima** di montare in auto.
> Luminosità bassa: **non** adatto all'uso diurno in macchina.

---

## 3. Montaggio nel vano (riuso cornice OEM)

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🔴 | **Cavo HDMI ad angolo 90°** (corto) | guadagna profondità dietro il vano | ~6 € |
| 🔴 | **Cavo USB ad angolo** (per il touch) | idem | ~4 € |
| 🟡 | **Staffa/adattatore** dietro la cornice | piastra tagliata o **stampata 3D** su misura | 0–15 € |
| 🟢 | *(Alternativa)* **Mascherina aftermarket Clio 4** | se non riusi la cornice OEM (Sound Way / Ultrasuono, formato DIN) | ~15–30 € |

> **Prima di comprare lo schermo:** misura sulla **tua** cornice l'area visibile
> del foro (L×H in mm) e la profondità disponibile dietro il vano.

---

## 4. Audio (togliendo la Media Nav perdi l'amplificatore)

Il Pi esce a **livello linea**, non pilota gli altoparlanti dell'auto.

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🟡 | **Amplificatore 4 canali 12 V classe D** (mini-amp) | pilota gli altoparlanti Clio | ~30–60 € |
| 🟡 | **DAC USB** *oppure* **HAT audio I2S** | qualità audio migliore del jack Pi | ~15–30 € |
| 🟢 | Cablaggio/adattatore altoparlanti Clio (connettore ISO) | collega l'amp ai cavi originali | ~8 € |

> Alternativa "tutto in uno": una **testata aftermarket con ingresso HDMI/AUX**
> che fa da schermo + amplificatore (ma duplica in parte GSOI).

---

## 5. Voce (microfono per GSOI Jarvis Mini)

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🟡 | **Microfono USB con cancellazione rumore** *o* **ReSpeaker USB Mic Array** | il ReSpeaker è ottimo in auto (rumore) | ~15–60 € |
| 🟢 | Posizionamento a padiglione/plafoniera | mic vicino al guidatore | — |

---

## 6. Dati auto — OBD-II (SOLA LETTURA)

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🟡 | **Adattatore OBD-II USB** (chip ELM327, cavo USB) | preferibile USB al Bluetooth per stabilità | ~15–25 € |
| 🟢 | Prolunga OBD-II | per portare la presa vicino al Pi | ~8 € |

> ⚠️ **Solo lettura.** Mai scrivere sul bus CAN dei sistemi di sicurezza
> (sterzo, freni, ABS, airbag, ADAS).

---

## 7. Retrocamera e sensori di parcheggio (retromarcia)

Come la Media Nav di serie: innestando la retromarcia compare a tutto schermo il
video della telecamera posteriore (con linee guida) e i sensori di parcheggio.
Nell'OS è una funzione **indipendente dall'AI**, gestita dal **cockpit**.

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🟡 | **Telecamera posteriore** (riusa quella di serie, o aftermarket) | uscita **video composito** analogico | 0 (riuso) / ~15–30 € |
| 🟡 | **Dongle USB di acquisizione video (UVC)** composito→USB | porta il video nel Pi (V4L2, plug-and-play) | ~15–25 € |
| 🔴 | **Optoisolatore / convertitore di livello** per il filo luce-retromarcia | isola i **+12 V** del filo retro dai **3,3 V** del GPIO | ~3–8 € |
| 🟢 | **Sensori di parcheggio (PDC)** | dal **CAN** (sola lettura) se l'auto li trasmette, oppure **kit ultrasuoni** dedicato | 0 / ~20–40 € |

> **Come funziona:** il **filo della luce di retromarcia** (+12 V con la R
> innestata) va, tramite **optoisolatore**, a un **GPIO** del Pi → il cockpit
> rileva la retromarcia e mostra video + sensori. Il video entra dal **dongle di
> acquisizione** (device V4L2). Nessun passaggio dall'AI.
>
> Per la **telecamera reale** serve `qtmultimedia` nell'immagine; il cockpit ha
> già la schermata (con segnaposto in mock, tasto `R` per il test in QEMU).

---

## 8. Quadro strumenti digitale (2° schermo, dietro al volante)

Un **secondo schermo** dietro al volante mostra il quadro strumenti GSOI
(`gsoi-cluster`), che **si affianca** a quello OEM: l'originale resta al suo
posto (odometro legale, spie omologate, immobilizer, nodo CAN) e GSOI **legge**
il CAN in sola lettura per aggiungere un quadro digitale. Il Pi 5 pilota i due
schermi dalle sue **due uscite micro-HDMI**.

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🟡 | **Display "bar/stretched" da cruscotto** (8–12", es. 1920×720 o 1280×480) HDMI | formato largo da quadro; in alternativa un 7" IPS se lo spazio è ridotto | ~70–200 € |
| 🔴 | **2° cavo micro-HDMI** (angolo) dal Pi 5 | usa la 2ª uscita HDMI del Pi 5 | ~6 € |
| 🔴 | **Interfaccia CAN per il Pi** (MCP2515 SPI *oppure* CANable/USB-CAN) | legge il bus veicolo in **sola lettura** per velocità/giri/temp/spie | ~10–30 € |
| 🟢 | Staffa/telaio dietro al volante (stampa 3D) | fissaggio del display nel cruscotto | 0–15 € |

> **Come funziona:** il servizio **`gsoi-vehicled`** legge il **CAN in sola
> lettura** (interfaccia MCP2515/USB-CAN) e pubblica i dati su `127.0.0.1:8093`;
> l'app **`gsoi-cluster`** li mostra sul 2° schermo (Weston assegna le uscite per
> app_id). La mappa degli ID CAN del **Clio IV 1.5 dCi** va rilevata sul veicolo
> (hook `decode_frame` in `gsoi-vehicled`); finché non c'è, il quadro gira in
> **mock animato** (visibile anche in QEMU).
>
> ⚠️ **Il quadro OEM non si rimuove.** Su Renault contiene l'odometro legale ed è
> legato a immobilizer/gateway: toglierlo può rompere la rete CAN e ha
> implicazioni legali (tachimetro omologato). GSOI si **affianca**, non sostituisce.
>
> *Nota CAN:* l'ELM327 USB della sezione 6 (per la telemetria di Jarvis Mini) è
> a interrogazione ed è lento per un quadro in tempo reale; per il cluster serve
> un'interfaccia che **ascolti** il bus (MCP2515/USB-CAN).

---

## 9. Radio (opzionale — la Media Nav aveva DAB/FM)

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🟢 | **Dongle USB DAB+/FM** *oppure* **RTL-SDR** | radio locale senza rete | ~20–40 € |
| 🟢 | Antenna DAB/FM (adattatore Fakra Renault) | riusa l'antenna auto | ~10 € |

> In alternativa: radio in **streaming** quando c'è connettività.

---

## 10. Alimentazione (rete auto 12 V → 5 V)

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🔴 | **Convertitore DC-DC 12 V → 5 V / ≥5 A** (buck automotive) | il Pi 5 vuole 5 V/5 A; deve reggere i picchi | ~15–25 € |
| 🟡 | **Circuito di spegnimento ritardato / ignition sense** | spegne GSOI dopo lo switch-off senza corrompere la SD | ~15–30 € |
| 🟢 | Fusibile in linea + cablaggio | sicurezza elettrica | ~5 € |

> ⚠️ **Non** alimentare il Pi direttamente dai 12 V: serve il convertitore.
> Lo **spegnimento pulito** evita corruzioni del filesystem quando spegni l'auto.

---

## 11. Connettività (opzionale)

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🟢 | **Dongle 4G/LTE USB** + SIM dati | per le funzioni **online** (traffico, meteo, aggiornamenti OTA) | ~25–40 € |
| 🟢 | Antenna GPS USB | posizione precisa per navigazione | ~12 € |

> GSOI è **offline-first**: il modello LLM gira **a bordo**, senza server. Senza
> rete l'assistente funziona lo stesso; la connettività serve **solo** alle
> funzioni che la richiedono (traffico, meteo, OTA).

---

## Schema di collegamento

```
                         ┌─────────────────────────────┐
   12V auto ──[fusibile]─┤ DC-DC 12V→5V/5A + ign. sense ├─► Raspberry Pi 5
                         └─────────────────────────────┘        │
                                                                │
   Schermo 7" open-frame (nel vano) ◄────── HDMI-1 (angolo) ────┤
                        │  └──────────────── USB (touch) ────────┤
                        │                                        │
   Quadro strumenti (dietro al volante) ◄── HDMI-2 (angolo) ────┤
                                                                 │
   Interfaccia CAN (MCP2515/USB-CAN, SOLA LETTURA) ─ SPI/USB ───┤
                                                                 │
   Microfono USB ───────────────────────────── USB ─────────────┤
                                                                 │
   DAC/HAT audio ─► Amplificatore 4ch 12V ─► altoparlanti Clio   │
        └───────────────────────────────────── USB/I2S ──────────┤
                                                                 │
   OBD-II ELM327 (SOLA LETTURA) ─────────────── USB ─────────────┤
                                                                 │
   Telecamera post. ─► Dongle acquisizione video ─ USB ─────────┤
   Filo luce-retro (+12V) ─► optoisolatore ─────► GPIO ─────────┤
   Sensori PDC (CAN sola lettura / kit) ────────────────────────┤
                                                                 │
   [opz.] Dongle DAB/FM ─────────────────────── USB ─────────────┤
   [opz.] Dongle 4G/LTE ─────────────────────── USB ─────────────┘

        cornice OEM Clio (riusata)
                │
          [ adattatore/staffa ]   ← 3D-print o piastra su misura
                │
        [ schermo open-frame ]
                │
          [ Raspberry Pi 5 ]      ← dietro, nel vano
```

---

## Riepilogo spesa (configurazione consigliata in auto)

| Voce | Scelta consigliata | Prezzo |
|---|---|---|
| Raspberry Pi 5 8 GB + cooler | ufficiale | ~90 € |
| Storage | NVMe + HAT | ~35 € |
| Schermo | Mimo/Faytech 7" open-frame HDMI+USB | ~150–250 € |
| Montaggio | cavi angolati + staffa | ~15 € |
| Audio | mini-amp 4ch + DAC USB | ~60 € |
| Microfono | ReSpeaker / USB noise-cancel | ~30 € |
| OBD-II | ELM327 USB | ~20 € |
| Retrocamera | telecamera (riuso) + acquisizione USB + optoisolatore | ~25–40 € |
| Quadro strumenti | 2° display cruscotto HDMI + cavo + interfaccia CAN | ~85–235 € |
| Alimentazione | DC-DC 5 V/5 A + ignition sense | ~40 € |
| **Totale indicativo** | | **~550–815 €** |

> **Extra opzionali:** radio DAB/FM (~30 €), 4G/LTE (~35 €), GPS (~12 €).
> **Solo sviluppo:** schermo Pi da banco ~40 € (non va in auto).

---

## Ordine di acquisto suggerito

1. **Ora (sviluppo):** Raspberry Pi 5 + cooler + storage + schermo da banco 🧪
   → testi GSOI su hardware vero uscendo da QEMU.
2. **Poi (auto):** schermo open-frame automotive + montaggio + audio + OBD +
   alimentazione + microfono → installazione nella Clio.
3. **Quadro strumenti:** 2° display + interfaccia CAN dietro al volante
   (si affianca all'OEM) → rilievo della mappa CAN del Clio IV.
4. **Infine (comfort):** radio, 4G/LTE, GPS.

---

## Note di sicurezza (vincoli di progetto)

- **OBD/CAN in sola lettura.** Nessuna scrittura sui bus dei sistemi di
  sicurezza (sterzo, freni, ABS, airbag, ADAS).
- **Spegnimento pulito** obbligatorio (ignition sense) per non corrompere il
  filesystem.
- **Fusibile** sempre in linea sull'alimentazione 12 V.
- Fissaggio meccanico saldo: nulla deve staccarsi con vibrazioni/urti.
