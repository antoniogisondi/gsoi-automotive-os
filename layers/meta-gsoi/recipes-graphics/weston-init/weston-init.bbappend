# GSOI: configura Weston in modalita' "kiosk" e avvia le UI GSOI a schermo
# intero come client Wayland al boot.
#
# Due schermi sul Raspberry Pi 5 (2x micro-HDMI):
#   HDMI-A-1  -> cockpit/infotainment   (app_id org.gsoi.cockpit)
#   HDMI-A-2  -> quadro strumenti        (app_id org.gsoi.cluster)
# kiosk-shell assegna ciascuna app alla sua uscita in base all'app_id.
# In QEMU (una sola uscita) parte solo il cockpit: nessuna regressione.

# Spedisci il link di autostart e il launcher aggiunti in do_install.
FILES:${PN} += "${systemd_system_unitdir}/multi-user.target.wants"
FILES:${PN} += "${bindir}/gsoi-ui-launch"

# I launcher gsoi-cockpit-launch / gsoi-cluster-launch arrivano da gsoi-cockpit.
RDEPENDS:${PN} += "gsoi-cockpit"

do_install:append() {
    install -d ${D}${sysconfdir}/xdg/weston
    cat > ${D}${sysconfdir}/xdg/weston/weston.ini <<'EOF'
# Generato da meta-gsoi (weston-init bbappend)
[core]
# kiosk-shell: app a tutto schermo, niente desktop.
shell=kiosk-shell.so
# Parte anche senza dispositivi di input (utile in QEMU e al primo boot).
require-input=false
xwayland=false

# Assegnazione app -> uscita HDMI (kiosk-shell). Su hardware senza queste
# uscite (es. QEMU) le voci vengono ignorate e l'app va sull'unico schermo.
[output]
name=HDMI-A-1
app-ids=org.gsoi.cockpit

[output]
name=HDMI-A-2
app-ids=org.gsoi.cluster

[autolaunch]
# Avvia le UI GSOI (cockpit sempre; cluster sul 2o schermo se collegato).
path=/usr/bin/gsoi-ui-launch
watch=true
EOF

    # Launcher che decide quali UI avviare in base agli schermi presenti.
    install -d ${D}${bindir}
    cat > ${D}${bindir}/gsoi-ui-launch <<'EOF'
#!/bin/sh
# Avvio delle interfacce grafiche GSOI sotto Weston (kiosk).
#   cockpit -> schermo centrale (org.gsoi.cockpit)
#   cluster -> quadro strumenti, 2a uscita HDMI (org.gsoi.cluster)
# In QEMU/single-output parte solo il cockpit.
# Override manuale: /etc/gsoi/ui-mode = auto | cockpit | cluster | both
#   (utile per PROVARE il quadro in QEMU: echo cluster > /etc/gsoi/ui-mode
#    e riavvia weston).
mode=auto
if [ -r /etc/gsoi/ui-mode ]; then
    mode=$(cat /etc/gsoi/ui-mode 2>/dev/null | tr -d ' \t\r\n')
fi

# Numero di schermi: preferisci le capacità rilevate da gsoi-detect, con
# fallback a una scansione diretta dei connettori HDMI.
hdmi_connected() {
    n=0
    for s in /sys/class/drm/card*-HDMI-*/status; do
        [ -f "$s" ] || continue
        if [ "$(cat "$s" 2>/dev/null)" = connected ]; then
            n=$((n + 1))
        fi
    done
    echo "$n"
}

displays=""
if [ -r /run/gsoi/capabilities.env ]; then
    . /run/gsoi/capabilities.env 2>/dev/null
    displays="$GSOI_DISPLAYS"
fi
[ -n "$displays" ] || displays=$(hdmi_connected)

case "$mode" in
    cluster)
        exec /usr/bin/gsoi-cluster-launch
        ;;
    cockpit)
        exec /usr/bin/gsoi-cockpit-launch
        ;;
    both)
        /usr/bin/gsoi-cluster-launch &
        exec /usr/bin/gsoi-cockpit-launch
        ;;
    *)  # auto: quadro solo se c'e' un 2o schermo collegato
        if [ "$displays" -ge 2 ] 2>/dev/null; then
            /usr/bin/gsoi-cluster-launch &
        fi
        exec /usr/bin/gsoi-cockpit-launch
        ;;
esac
EOF
    chmod 0755 ${D}${bindir}/gsoi-ui-launch

    # Forza l'avvio di Weston al boot (kiosk): di default weston.service e'
    # solo socket-activated, quindi senza questo link non parte da solo.
    install -d ${D}${systemd_system_unitdir}/multi-user.target.wants
    ln -sf ../weston.service \
        ${D}${systemd_system_unitdir}/multi-user.target.wants/weston.service
}
