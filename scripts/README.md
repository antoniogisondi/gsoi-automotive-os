# scripts/

Script di supporto per GSOI Automotive OS.

## `setup-env.sh`

Ricostruisce l'ambiente di build Yocto su una macchina nuova (es. il PC di
build Kali). Clona i layer upstream **ai commit esatti** pinnati in
[`../UPSTREAM_VERSIONS.txt`](../UPSTREAM_VERSIONS.txt) e genera una
configurazione di build coerente col progetto
(`MACHINE=raspberrypi5`, `DISTRO=gsoi-automotive`).

I layer upstream non sono versionati in questa repo (vedi `.gitignore`):
vengono scaricati sotto `layers/`.

### Uso

Dalla root della repo:

```bash
# 1. Scarica/pinna i layer upstream e genera build/conf/
./scripts/setup-env.sh

# 2. Inizializza l'ambiente di build (trova bitbake in layers/bitbake)
source layers/openembedded-core/oe-init-build-env build

# 3. Costruisci l'immagine minimale v0.1
bitbake gsoi-automotive-image
```

L'immagine finale (`.wic` / `.wic.bz2`) verrà prodotta in:

```
build/tmp/deploy/images/raspberrypi5/
```

### Note

- Lo script è **idempotente**: se un layer esiste già, esegue solo
  `fetch` + `checkout` del commit pinnato.
- Se il PC di build ha poca RAM, in `build/conf/local.conf` sono presenti
  (commentate) le variabili `BB_NUMBER_THREADS` e `PARALLEL_MAKE` da
  abbassare per evitare l'OOM killer.
- `DL_DIR` e `SSTATE_DIR` puntano a cartelle a livello di repo, così le
  build successive riutilizzano download e cache.
