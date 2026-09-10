SUMMARY = "GSOI Automotive OS"
DESCRIPTION = "GSOI Automotive OS development image for Raspberry Pi 5"

LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL:append = " \
    bash \
    python3 \
    jarvis-mini \
    weston \
    weston-init \
    gsoi-cockpit \
    qtwayland \
    ttf-dejavu-sans \
    source-serif-4 \
"

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
