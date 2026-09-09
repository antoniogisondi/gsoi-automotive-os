# GSOI: configura Weston in modalita' "kiosk" e avvia il cockpit a schermo
# intero come client Wayland al boot.

# Spedisci il link di autostart aggiunto in do_install (altrimenti QA fallisce).
FILES:${PN} += "${systemd_system_unitdir}/multi-user.target.wants"

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

    # Forza l'avvio di Weston al boot (kiosk): di default weston.service e'
    # solo socket-activated, quindi senza questo link non parte da solo.
    install -d ${D}${systemd_system_unitdir}/multi-user.target.wants
    ln -sf ../weston.service \
        ${D}${systemd_system_unitdir}/multi-user.target.wants/weston.service
}
