SUMMARY = "GSOI Automotive OS"
DESCRIPTION = "GSOI Automotive OS development image for Raspberry Pi 5"

LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL:append = " \
    bash \
    python3 \
    jarvis-mini \
"

IMAGE_FEATURES += "ssh-server-openssh"
