SUMMARY = "GSOI Model — server locale del modello LLM (cervello a bordo)"
DESCRIPTION = "Servizio systemd che serve il modello LLM locale (Qwen3-4B) su \
127.0.0.1 con endpoint OpenAI-compatible. E' il 'cervello' interrogato da \
gsoi-jarvis-mini senza uscire dalla rete. Servizio SEPARATO da jarvis-mini: \
questo ospita solo il modello, l'agente resta a parte."
HOMEPAGE = "https://github.com/antoniogisondi/gsoi-jarvis-mini"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://gsoi-model.service \
    file://gsoi-model-serve \
    file://jarvis-mini-local.conf \
"

# Solo script + unit systemd: nessun binario compilato qui.
inherit allarch systemd

# Dipendenze runtime:
#  - jarvis-mini: il drop-in attiva il cervello locale nell'agente;
#  - llama-cpp:   fornisce 'llama-server', il motore che il launcher avvia.
RDEPENDS:${PN} += "jarvis-mini llama-cpp"

# NOTA: il SERVER (llama-server) ora e' fornito dalla ricetta llama-cpp ed e'
# tirato dentro l'immagine da questo RDEPENDS. Resta a carico del runtime solo
# il file .gguf del modello: va messo in /var/lib/gsoi-model (via OTA o
# provisioning). Finche' il modello manca, il servizio resta inattivo e
# jarvis-mini usa il mock.

SYSTEMD_SERVICE:${PN} = "gsoi-model.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
    # Launcher del server.
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/gsoi-model-serve ${D}${bindir}/gsoi-model-serve

    # Unit del servizio.
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/gsoi-model.service \
        ${D}${systemd_system_unitdir}/gsoi-model.service

    # Drop-in che attiva JARVIS_AI=local in jarvis-mini quando gsoi-model c'e'.
    install -d ${D}${systemd_system_unitdir}/jarvis-mini.service.d
    install -m 0644 ${WORKDIR}/jarvis-mini-local.conf \
        ${D}${systemd_system_unitdir}/jarvis-mini.service.d/10-gsoi-model.conf

    # Cartella mutabile del modello (popolata dopo: build/OTA/provisioning).
    install -d ${D}${localstatedir}/lib/gsoi-model
}

FILES:${PN} += " \
    ${systemd_system_unitdir}/jarvis-mini.service.d/10-gsoi-model.conf \
    ${localstatedir}/lib/gsoi-model \
"
