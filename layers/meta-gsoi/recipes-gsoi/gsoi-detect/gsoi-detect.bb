SUMMARY = "GSOI — rilevamento capacità hardware"
DESCRIPTION = "Servizio oneshot che al boot sonda l'hardware realmente presente \
(schermi DRM, bus CAN, telecamere V4L2, audio/mic ALSA, GPIO, tipo di ambiente) \
e scrive le capacità in /run/gsoi/capabilities.env (per i servizi) e .json (per \
le UI). Permette a GSOI di girare su QUALSIASI auto: ogni componente si attiva \
solo dove l'hardware c'è (es. niente 2o schermo -> niente quadro; niente CAN -> \
nessun dato veicolo finto, mock solo in emulazione)."
HOMEPAGE = "https://github.com/antoniogisondi/gsoi-automotive-os"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://gsoi-detect \
    file://gsoi-detect.service \
"

# Solo script POSIX sh + unit systemd.
inherit allarch systemd

# Runtime: shell + coreutils/busybox + systemd (per systemd-detect-virt).
RDEPENDS:${PN} += "base-files"

SYSTEMD_SERVICE:${PN} = "gsoi-detect.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/gsoi-detect ${D}${bindir}/gsoi-detect

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/gsoi-detect.service \
        ${D}${systemd_system_unitdir}/gsoi-detect.service
}
