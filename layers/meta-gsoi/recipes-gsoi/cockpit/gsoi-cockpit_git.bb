SUMMARY = "GSOI Cockpit (Qt Quick UI)"
DESCRIPTION = "Interfaccia cockpit/infotainment di GSOI Automotive OS, scritta \
in Qt6/QML, avviata a schermo intero come client Wayland."
HOMEPAGE = "https://github.com/antoniogisondi/gsoi-cockpit"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Sorgente pinnato a un commit preciso (build riproducibili).
SRC_URI = "git://github.com/antoniogisondi/gsoi-cockpit.git;protocol=https;branch=${GSOI_COCKPIT_BRANCH}"

GSOI_COCKPIT_BRANCH ?= "main"
SRCREV = "0800e8bc09f6e60cc41726841108d6139f2281e6"

PV = "0.1.0+git"

# Dipendenze di build: Qt base + Qt Declarative (QML) e i loro tool nativi.
DEPENDS = "qtbase qtdeclarative qtbase-native qtdeclarative-native"

inherit qt6-cmake

# Piccolo launcher: forza la piattaforma Wayland per l'app Qt.
do_install:append() {
    install -d ${D}${bindir}
    cat > ${D}${bindir}/gsoi-cockpit-launch <<'EOF'
#!/bin/sh
export QT_QPA_PLATFORM=wayland
exec /usr/bin/gsoi-cockpit "$@"
EOF
    chmod 0755 ${D}${bindir}/gsoi-cockpit-launch
}

FILES:${PN} += "${bindir}/gsoi-cockpit-launch"

# Runtime: motore QML + moduli QtQuick + plugin piattaforma Wayland.
RDEPENDS:${PN} += " \
    qtbase \
    qtdeclarative \
    qtdeclarative-qmlplugins \
    qtwayland \
"
