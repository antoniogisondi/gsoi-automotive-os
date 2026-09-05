SUMMARY = "GSOI Jarvis Mini (Car Agent)"
DESCRIPTION = "Agente locale offline-first dell'automobile per GSOI Automotive OS. \
Riconosce i comandi automotive di base in locale (senza LLM), instrada le \
richieste complesse al server GSOI quando raggiungibile e resta sempre attivo \
anche completamente offline. Installato come servizio systemd."
HOMEPAGE = "https://github.com/antoniogisondi/gsoi-jarvis-mini"

LICENSE = "MIT"
# Il sorgente non contiene ancora un file LICENSE: usiamo il testo MIT
# standard fornito da OE-Core. Quando la repo avra' un file LICENSE
# conviene puntare a quello (file://LICENSE;md5=...).
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# --- Sorgente: la repo gsoi-jarvis-mini, pinnata a un commit preciso -------
# Build riproducibili: si aggiorna il progetto cambiando SRCREV (e branch,
# una volta unito il lavoro su main).
SRC_URI = "git://github.com/antoniogisondi/gsoi-jarvis-mini.git;protocol=https;branch=${GSOI_JARVIS_MINI_BRANCH}"

GSOI_JARVIS_MINI_BRANCH ?= "claude/jarvis-mini-v0.1"
SRCREV = "1e5a6fb15745e3de1ebd7beb52a899d225eac04c"

PV = "0.1.0+git"
S = "${WORKDIR}/git"

# Pacchetto Python (pyproject.toml, backend setuptools) + integrazione systemd.
inherit python_setuptools_build_meta systemd

# --- Dipendenze runtime -----------------------------------------------------
# La v0.1 usa solo la libreria standard Python. Moduli usati: socket, urllib,
# argparse, unicodedata, dataclasses, enum, abc, random, time.
# argparse e unicodedata sono in python3-core in questa release (non piu'
# pacchetti separati). urllib e' in python3-netclient.
# NOTA: se al primo boot un "import" fallisse, aggiungere qui il python3-*
# che fornisce quel modulo (lo split di python3 in OE-Core e' a grana fine).
RDEPENDS:${PN} += " \
    python3-core \
    python3-netclient \
"

# --- Servizio systemd -------------------------------------------------------
SYSTEMD_SERVICE:${PN} = "jarvis-mini.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_system_unitdir}
        install -m 0644 ${S}/systemd/jarvis-mini.service \
            ${D}${systemd_system_unitdir}/jarvis-mini.service
    fi
}

FILES:${PN} += "${systemd_system_unitdir}/jarvis-mini.service"
