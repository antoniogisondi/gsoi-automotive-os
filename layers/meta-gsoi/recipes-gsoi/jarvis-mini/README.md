# recipe: jarvis-mini

Ricetta che integra **GSOI Jarvis Mini** (il Car Agent) dentro l'immagine di
GSOI Automotive OS.

- Sorgente: la repo separata
  [`gsoi-jarvis-mini`](https://github.com/antoniogisondi/gsoi-jarvis-mini),
  **non** versionata qui — viene scaricata da BitBake a un commit pinnato
  (`SRCREV`), esattamente come i layer upstream.
- Installa il pacchetto Python e l'eseguibile `/usr/bin/jarvis-mini`.
- Registra e abilita il servizio systemd `jarvis-mini.service`, così l'agente
  parte automaticamente al boot.

## Aggiungere Jarvis Mini all'immagine

La ricetta da sola non finisce nell'immagine: va richiesta esplicitamente.
In `recipes-core/images/gsoi-automotive-image.bb`:

```
IMAGE_INSTALL:append = " jarvis-mini"
```

(È la Fase 4 della roadmap: farlo solo quando si vuole Jarvis Mini a bordo.)

## Aggiornare la versione installata

Quando `gsoi-jarvis-mini` avanza:

1. aggiorna `SRCREV` con il nuovo commit;
2. se cambia il branch (es. una volta unito su `main`), aggiorna
   `GSOI_JARVIS_MINI_BRANCH` (o rimuovi l'override per usare il default).

## Verifiche al primo build

- Se un `import` della stdlib fallisce a runtime, aggiungi il pacchetto
  `python3-*` mancante in `RDEPENDS:${PN}` nella ricetta.
- Controlla il servizio sul target: `systemctl status jarvis-mini` e
  `journalctl -u jarvis-mini`.
