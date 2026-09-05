# GSOI: configura Weston in modalita' "kiosk" e avvia il cockpit a schermo
# intero come client Wayland al boot.

do_install:append() {
    install -d ${D}${sysconfdir}/xdg/weston
    cat > ${D}${sysconfdir}/xdg/weston/weston.ini <<'EOF'
# Generato da meta-gsoi (weston-init bbappend)
[core]
# kiosk-shell: una sola app a tutto schermo, niente desktop.
shell=kiosk-shell.so
# Parte anche senza dispositivi di input (utile in QEMU e al primo boot).
require-input=false
xwayland=false

[autolaunch]
# Avvia il cockpit GSOI (via launcher che forza la piattaforma Wayland).
path=/usr/bin/gsoi-cockpit-launch
watch=true
EOF
}
