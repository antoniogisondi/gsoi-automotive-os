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

## 7. Radio (opzionale — la Media Nav aveva DAB/FM)

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🟢 | **Dongle USB DAB+/FM** *oppure* **RTL-SDR** | radio locale senza rete | ~20–40 € |
| 🟢 | Antenna DAB/FM (adattatore Fakra Renault) | riusa l'antenna auto | ~10 € |

> In alternativa: radio in **streaming** quando c'è connettività.

---

## 8. Alimentazione (rete auto 12 V → 5 V)

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🔴 | **Convertitore DC-DC 12 V → 5 V / ≥5 A** (buck automotive) | il Pi 5 vuole 5 V/5 A; deve reggere i picchi | ~15–25 € |
| 🟡 | **Circuito di spegnimento ritardato / ignition sense** | spegne GSOI dopo lo switch-off senza corrompere la SD | ~15–30 € |
| 🟢 | Fusibile in linea + cablaggio | sicurezza elettrica | ~5 € |

> ⚠️ **Non** alimentare il Pi direttamente dai 12 V: serve il convertitore.
> Lo **spegnimento pulito** evita corruzioni del filesystem quando spegni l'auto.

---

## 9. Connettività (opzionale)

| # | Componente | Note | Prezzo |
|---|---|---|---|
| 🟢 | **Dongle 4G/LTE USB** + SIM dati | ponte verso il server GSOI (Jarvis) fuori casa | ~25–40 € |
| 🟢 | Antenna GPS USB | posizione precisa per navigazione | ~12 € |

> GSOI è **offline-first**: senza rete Jarvis Mini funziona lo stesso; la rete
> serve solo per instradare le richieste complesse al server GSOI-LLM.

---

## Schema di collegamento

```
                         ┌─────────────────────────────┐
   12V auto ──[fusibile]─┤ DC-DC 12V→5V/5A + ign. sense ├─► Raspberry Pi 5
                         └─────────────────────────────┘        │
                                                                │
   Schermo 7" open-frame (nel vano) ◄────── HDMI (angolo 90°) ──┤
                        │  └──────────────── USB (touch) ────────┤
                        │                                        │
   Microfono USB ───────────────────────────── USB ─────────────┤
                                                                 │
   DAC/HAT audio ─► Amplificatore 4ch 12V ─► altoparlanti Clio   │
        └───────────────────────────────────── USB/I2S ──────────┤
                                                                 │
   OBD-II ELM327 (SOLA LETTURA) ─────────────── USB ─────────────┤
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
| Alimentazione | DC-DC 5 V/5 A + ignition sense | ~40 € |
| **Totale indicativo** | | **~440–540 €** |

> **Extra opzionali:** radio DAB/FM (~30 €), 4G/LTE (~35 €), GPS (~12 €).
> **Solo sviluppo:** schermo Pi da banco ~40 € (non va in auto).

---

## Ordine di acquisto suggerito

1. **Ora (sviluppo):** Raspberry Pi 5 + cooler + storage + schermo da banco 🧪
   → testi GSOI su hardware vero uscendo da QEMU.
2. **Poi (auto):** schermo open-frame automotive + montaggio + audio + OBD +
   alimentazione + microfono → installazione nella Clio.
3. **Infine (comfort):** radio, 4G/LTE, GPS.

---

## Note di sicurezza (vincoli di progetto)

- **OBD/CAN in sola lettura.** Nessuna scrittura sui bus dei sistemi di
  sicurezza (sterzo, freni, ABS, airbag, ADAS).
- **Spegnimento pulito** obbligatorio (ignition sense) per non corrompere il
  filesystem.
- **Fusibile** sempre in linea sull'alimentazione 12 V.
- Fissaggio meccanico saldo: nulla deve staccarsi con vibrazioni/urti.
