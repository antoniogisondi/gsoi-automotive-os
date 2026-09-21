# GSOI: configura Weston in modalita' "kiosk" e avvia le UI GSOI a schermo
# intero come client Wayland al boot.
#
# Due schermi sul Raspberry Pi 5 (2x micro-HDMI):
#   HDMI-A-1  -> cockpit/infotainment   (app_id org.gsoi.cockpit)
#   HDMI-A-2  -> quadro strumenti        (app_id org.gsoi.cluster)
# kiosk-shell assegna ciascuna app alla sua uscita in base all'app_id.
# In QEMU (una sola uscita) parte solo il cockpit: nessuna regressione.

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Il launcher e' un file a parte (NON un heredoc: BitBake non capisce gli
# heredoc e la '}' di una funzione shell interna chiuderebbe do_install).
SRC_URI += "file://gsoi-ui-launch"

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

# NB: l'assegnazione app->uscita HDMI (kiosk-shell [output] app-ids) e' un
# concetto SOLO per il Pi con due HDMI. In QEMU l'unica uscita e' "Virtual-1":
# mappare le app a HDMI-A-1/2 le lascerebbe senza schermo. Si riattiva sul Pi.

[autolaunch]
# Avvia le UI GSOI (cockpit sempre; cluster sul 2o schermo se collegato).
path=/usr/bin/gsoi-ui-launch
watch=true
EOF

    # Launcher che decide quali UI avviare in base agli schermi presenti.
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/gsoi-ui-launch ${D}${bindir}/gsoi-ui-launch

    # Forza l'avvio di Weston al boot (kiosk): di default weston.service e'
    # solo socket-activated, quindi senza questo link non parte da solo.
    install -d ${D}${systemd_system_unitdir}/multi-user.target.wants
    ln -sf ../weston.service \
        ${D}${systemd_system_unitdir}/multi-user.target.wants/weston.service
}
