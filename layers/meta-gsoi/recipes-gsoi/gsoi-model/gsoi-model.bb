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

# Il drop-in attiva il cervello locale in jarvis-mini: ha senso solo se
# l'agente e' presente nell'immagine.
RDEPENDS:${PN} += "jarvis-mini"

# NOTA: il server vero (llama.cpp / ollama) e il file .gguf del modello NON
# sono forniti da questa ricetta. Il launcher li cerca a runtime:
#   - server: un binario 'llama-server' o 'ollama' nel PATH (da un layer che
#     lo fornisce, es. una ricetta llama.cpp — su Jetson: TensorRT-LLM);
#   - modello: un file *.gguf in /var/lib/gsoi-model (installato via OTA o
#     copiato in fase di provisioning).
# Finche' mancano, il servizio resta inattivo e jarvis-mini usa il mock.

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
