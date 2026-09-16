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

# Niente prompt di login sulla console grafica (tty1): sullo schermo
# dell'auto non si deve vedere il getty. La console seriale (debug/ssh)
# resta disponibile.
ROOTFS_POSTPROCESS_COMMAND += "gsoi_mask_tty1_getty;"
gsoi_mask_tty1_getty() {
    ln -sf /dev/null ${IMAGE_ROOTFS}${sysconfdir}/systemd/system/getty@tty1.service
}
