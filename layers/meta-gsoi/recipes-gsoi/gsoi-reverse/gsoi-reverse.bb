SUMMARY = "GSOI Reverse — retrocamera/sensori su retromarcia"
DESCRIPTION = "Servizio systemd, INDIPENDENTE dall'AI, che legge la retromarcia \
dal filo luce-retromarcia (GPIO) e i sensori di parcheggio (PDC), ed espone lo \
stato in JSON su 127.0.0.1:8092 per il cockpit. Funzione di sicurezza \
deterministica: nessun passaggio da Jarvis Mini o dal modello LLM."
HOMEPAGE = "https://github.com/antoniogisondi/gsoi-automotive-os"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://gsoi-reverse \
    file://gsoi-reverse.service \
"

# Solo script Python + unit systemd.
inherit allarch systemd

# Runtime: solo libreria standard Python (http.server, json, threading).
# NOTA hardware: per leggere il GPIO reale aggiungi qui il binding python di
# libgpiod (es. python3-libgpiod) e imposta GSOI_REVERSE_GPIOLINE nel service.
RDEPENDS:${PN} += "python3-core python3-netclient"

SYSTEMD_SERVICE:${PN} = "gsoi-reverse.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/gsoi-reverse ${D}${bindir}/gsoi-reverse

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/gsoi-reverse.service \
        ${D}${systemd_system_unitdir}/gsoi-reverse.service
}
