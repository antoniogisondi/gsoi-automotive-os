SUMMARY = "GSOI Vehicle Data — dati veicolo per il quadro strumenti"
DESCRIPTION = "Servizio systemd, INDIPENDENTE dall'AI, che legge dal bus CAN in \
SOLA LETTURA i dati di guida (velocità, giri, temperatura, carburante, spie, \
frecce) e li espone in JSON su 127.0.0.1:8093 per l'app gsoi-cluster (quadro \
strumenti digitale). Strumentazione deterministica: nessun passaggio da Jarvis \
Mini o dal modello LLM. Senza CAN configurato gira in mock (per QEMU/banco)."
HOMEPAGE = "https://github.com/antoniogisondi/gsoi-automotive-os"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://gsoi-vehicled \
    file://gsoi-vehicled.service \
"

# Solo script Python + unit systemd.
inherit allarch systemd

# Runtime: solo libreria standard Python (http.server, socket AF_CAN, struct).
# La lettura CAN usa SocketCAN via socket stdlib: nessuna dipendenza esterna.
RDEPENDS:${PN} += "python3-core python3-netclient"

SYSTEMD_SERVICE:${PN} = "gsoi-vehicled.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/gsoi-vehicled ${D}${bindir}/gsoi-vehicled

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/gsoi-vehicled.service \
        ${D}${systemd_system_unitdir}/gsoi-vehicled.service
}
