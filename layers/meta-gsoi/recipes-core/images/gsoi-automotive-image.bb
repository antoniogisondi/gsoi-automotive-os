SUMMARY = "GSOI Automotive OS"
DESCRIPTION = "GSOI Automotive OS development image for Raspberry Pi 5"

LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL:append = " \
    bash \
    python3 \
    jarvis-mini \
    gsoi-model \
    gsoi-model-weights \
    weston \
    weston-init \
    gsoi-cockpit \
    gsoi-reverse \
    qtwayland \
    ttf-dejavu-sans \
    source-serif-4 \
"

# gsoi-model-weights scarica il .gguf del modello (~2,5 GB) da HuggingFace e lo
# mette in /var/lib/gsoi-model: cosi' il cervello e' gia' dentro l'immagine.
# Per una build piu' leggera (senza modello), rimuovi 'gsoi-model-weights' qui
# sopra: gsoi-model resta inattivo e jarvis-mini usa il mock.

IMAGE_FEATURES += "ssh-server-openssh"

# Splash di boot (psplash con logo GSOI) al posto del testo del kernel.
IMAGE_FEATURES += "splash"

# Kiosk: nessun getty sulle VT grafiche (Weston possiede lo schermo).
# NB: NON si maschera getty@tty1 con /dev/null: 'systemctl preset-all'
# (systemd recente) fallisce sulle unit mascherate e rompe do_rootfs.
# Si disattivano gli autovt via logind. La console seriale resta attiva.
ROOTFS_POSTPROCESS_COMMAND += "gsoi_kiosk_no_vt_getty;"
gsoi_kiosk_no_vt_getty() {
    install -d ${IMAGE_ROOTFS}${sysconfdir}/systemd/logind.conf.d
    printf '[Login]\nNAutoVTs=0\nReserveVT=0\n' > \
        ${IMAGE_ROOTFS}${sysconfdir}/systemd/logind.conf.d/00-gsoi-kiosk.conf
}
