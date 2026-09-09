SUMMARY = "Source Serif 4 — famiglia serif di Adobe (OFL)"
DESCRIPTION = "Il font serif usato dal cockpit GSOI (stile editoriale). \
Imbarcato nell'immagine cosi' la UI si vede col carattere giusto sul target."
HOMEPAGE = "https://github.com/adobe-fonts/source-serif"

LICENSE = "OFL-1.1"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=32f66d18777ee4f28b4ef87d628b9f4d"

SRC_URI = "git://github.com/adobe-fonts/source-serif.git;protocol=https;branch=release"
SRCREV = "5f220b17d27ed64873f22cde0dd593685387bd19"

PV = "4.0.5+git"

inherit allarch fontcache

do_install() {
    install -d ${D}${datadir}/fonts/truetype/source-serif-4
    install -m 0644 ${S}/TTF/*.ttf \
        ${D}${datadir}/fonts/truetype/source-serif-4/
}

FILES:${PN} = "${datadir}/fonts"
