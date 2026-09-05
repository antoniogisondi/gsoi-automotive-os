#!/usr/bin/env bash
#
# GSOI Automotive OS - setup ambiente Yocto
# ==========================================
#
# Ricostruisce l'ambiente di build Yocto clonando i layer upstream
# ai commit ESATTI indicati in UPSTREAM_VERSIONS.txt e genera una
# configurazione di build coerente (MACHINE=raspberrypi5,
# DISTRO=gsoi-automotive).
#
# I layer upstream NON sono versionati in questa repo (vedi .gitignore):
# questo script li scarica separatamente sotto layers/.
#
# Uso tipico (da eseguire sul PC di build, es. Kali):
#
#     ./scripts/setup-env.sh
#     source layers/openembedded-core/oe-init-build-env build
#     bitbake gsoi-automotive-image
#
# Lo script e' idempotente: se un layer esiste gia', si limita a fare
# fetch e checkout del commit pinnato.

set -euo pipefail

# --- Percorsi ---------------------------------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

LAYERS_DIR="$REPO_ROOT/layers"
BUILD_DIR="$REPO_ROOT/build"
UPSTREAM_FILE="$REPO_ROOT/UPSTREAM_VERSIONS.txt"

# Nota: NON si clona per nome di branch. I repo upstream non espongono
# tutti lo stesso nome di branch e comunque servono commit precisi: si
# clona il repo e si fa checkout del commit pinnato in UPSTREAM_VERSIONS.txt.

# --- Layer upstream: nome -> URL -------------------------------------------
# Layout "standalone" (OE-Core) come previsto dal .gitignore della repo.

declare -A REPOS=(
    [bitbake]="https://git.openembedded.org/bitbake"
    [openembedded-core]="https://git.openembedded.org/openembedded-core"
    [meta-yocto]="https://git.yoctoproject.org/meta-yocto"
    [meta-raspberrypi]="https://git.yoctoproject.org/meta-raspberrypi"
    [meta-openembedded]="https://git.openembedded.org/meta-openembedded"
    [meta-qt6]="https://code.qt.io/yocto/meta-qt6.git"
)

# --- Funzioni ---------------------------------------------------------------

log() {
    printf '\n\033[1;36m==> %s\033[0m\n' "$*"
}

commit_for() {
    # Estrae il commit pinnato da UPSTREAM_VERSIONS.txt per il layer $1.
    # Formato atteso della riga: "nome: <hash>"
    local name="$1"
    local commit
    commit="$(grep -E "^${name}:" "$UPSTREAM_FILE" | head -n1 | awk '{print $2}')"

    if [ -z "$commit" ]; then
        echo "ERRORE: nessun commit per '$name' in $UPSTREAM_FILE" >&2
        exit 1
    fi

    printf '%s' "$commit"
}

clone_or_update() {
    local name="$1"
    local url="${REPOS[$name]}"
    local commit
    commit="$(commit_for "$name")"

    local dir="$LAYERS_DIR/$name"

    if [ -d "$dir/.git" ]; then
        log "Aggiorno $name (gia' presente)"
        git -C "$dir" fetch --tags origin
    else
        # Rimuove eventuali cloni parziali lasciati da un tentativo fallito.
        rm -rf "$dir"
        log "Clono $name ($url)"
        git clone "$url" "$dir"
    fi

    # Forza il commit pinnato (HEAD staccato: va bene per un layer di build).
    log "Checkout $name -> $commit"
    git -C "$dir" checkout --quiet "$commit"
}

# --- Prerequisiti ------------------------------------------------------------

if [ ! -f "$UPSTREAM_FILE" ]; then
    echo "ERRORE: $UPSTREAM_FILE non trovato." >&2
    exit 1
fi

for tool in git awk grep; do
    command -v "$tool" >/dev/null 2>&1 || {
        echo "ERRORE: '$tool' non trovato nel PATH." >&2
        exit 1
    }
done

mkdir -p "$LAYERS_DIR"

# --- 1. Layer upstream -------------------------------------------------------

log "GSOI Automotive OS - setup layer Yocto"
echo "Repo root : $REPO_ROOT"
echo "Layers    : $LAYERS_DIR"
echo "Build dir : $BUILD_DIR"

for name in bitbake openembedded-core meta-yocto meta-raspberrypi \
            meta-openembedded meta-qt6; do
    clone_or_update "$name"
done

# --- 2. Configurazione di build ---------------------------------------------
# oe-init-build-env crea questi file solo se assenti: pre-generandoli
# manteniamo una configurazione deterministica e coerente col progetto.

mkdir -p "$BUILD_DIR/conf"

if [ ! -f "$BUILD_DIR/conf/bblayers.conf" ]; then
    log "Genero build/conf/bblayers.conf"
    cat > "$BUILD_DIR/conf/bblayers.conf" <<'EOF'
# Generato da scripts/setup-env.sh - GSOI Automotive OS
POKY_BBLAYERS_CONF_VERSION = "2"

BBPATH = "${TOPDIR}"
BBFILES ?= ""

# build/ si trova nella root della repo, quindi ../layers punta ai layer.
BBLAYERS ?= " \
    ${TOPDIR}/../layers/openembedded-core/meta \
    ${TOPDIR}/../layers/meta-yocto/meta-poky \
    ${TOPDIR}/../layers/meta-raspberrypi \
    ${TOPDIR}/../layers/meta-openembedded/meta-oe \
    ${TOPDIR}/../layers/meta-openembedded/meta-python \
    ${TOPDIR}/../layers/meta-qt6 \
    ${TOPDIR}/../layers/meta-gsoi \
"
EOF
else
    echo "build/conf/bblayers.conf gia' presente, non lo tocco."
fi

if [ ! -f "$BUILD_DIR/conf/local.conf" ]; then
    log "Genero build/conf/local.conf"
    cat > "$BUILD_DIR/conf/local.conf" <<'EOF'
# Generato da scripts/setup-env.sh - GSOI Automotive OS
CONF_VERSION = "2"

# Target hardware e distro del progetto.
MACHINE ??= "raspberrypi5"
DISTRO ?= "gsoi-automotive"

# Grafica: OpenGL/GLES + Wayland (per Weston e il cockpit Qt/QML).
DISTRO_FEATURES:append = " opengl wayland"

# Abilita la seriale UART (utile per debug boot su RPi5).
ENABLE_UART = "1"

# Accetta il license flag del firmware WiFi/BT del Raspberry
# (chip Synaptics/Cypress), altrimenti packagegroup-base-extended
# non e' costruibile e la build si ferma.
LICENSE_FLAGS_ACCEPTED = "synaptics-killswitch"

PACKAGE_CLASSES ?= "package_rpm"

# Directory condivise a livello di repo (escluse da git, riutilizzabili
# tra build diverse per non riscaricare/ricompilare tutto).
DL_DIR ?= "${TOPDIR}/../downloads"
SSTATE_DIR ?= "${TOPDIR}/../sstate-cache"

# Monitor spazio disco: interrompe la build se lo spazio scende troppo.
BB_DISKMON_DIRS ??= "\
    STOPTASKS,${TMPDIR},1G,100K \
    STOPTASKS,${DL_DIR},1G,100K \
    STOPTASKS,${SSTATE_DIR},1G,100K \
    HALT,${TMPDIR},100M,1K \
    HALT,${DL_DIR},100M,1K \
    HALT,${SSTATE_DIR},100M,1K"

# --- Risorse di build ---
# La prima build su un PC con poca RAM aveva attivato l'OOM killer.
# Se hai poca RAM (<=8-16 GB) decommenta e abbassa questi valori per
# ridurre il carico (build piu' lenta ma piu' stabile):
# BB_NUMBER_THREADS = "4"
# PARALLEL_MAKE = "-j 4"
EOF
else
    echo "build/conf/local.conf gia' presente, non lo tocco."
fi

# --- Fine --------------------------------------------------------------------

log "Setup completato."
cat <<EOF

Prossimi passi (dalla root della repo):

  1. Inizializza l'ambiente di build:
       source layers/openembedded-core/oe-init-build-env build

  2. Avvia la build dell'immagine minimale:
       bitbake gsoi-automotive-image

  3. L'immagine .wic verra' prodotta in:
       build/tmp/deploy/images/raspberrypi5/

Nota: openembedded-core/oe-init-build-env trova bitbake nella cartella
sorella layers/bitbake automaticamente.
EOF
