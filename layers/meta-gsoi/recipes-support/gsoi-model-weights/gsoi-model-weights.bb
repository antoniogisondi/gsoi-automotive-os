SUMMARY = "Pesi GSOI — Qwen3-4B fine-tunato (GGUF) per gsoi-model"
DESCRIPTION = "Il modello LLM di GSOI: Qwen3-4B-Instruct fine-tunato (QLoRA) e \
quantizzato in GGUF (q4_k_m). Viene installato in /var/lib/gsoi-model, dove il \
launcher di gsoi-model lo trova e lo serve con llama-server su 127.0.0.1. \
Scaricato da HuggingFace al momento del build (repo pubblico)."
HOMEPAGE = "https://huggingface.co/gsoi/gsoi-qwen3-4b-gguf"

# Base Qwen3 Apache-2.0; il fine-tune ne eredita i termini.
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# Fetch del .gguf da HuggingFace (repo pubblico -> nessun token richiesto).
# URL pinnato alla revisione del commit (non a 'main') per build riproducibili.
# Per aggiornare il modello: nuovo upload su HF, poi aggiorna la revisione
# nell'URL e lo SHA256 qui sotto.
SRC_URI = "https://huggingface.co/gsoi/gsoi-qwen3-4b-gguf/resolve/40fef16d12bb1f5af41957cb699da8c6c76602ca/qwen3-4b-instruct-2507.Q4_K_M.gguf;downloadfilename=gsoi-qwen3-4b-q4_k_m.gguf"
SRC_URI[sha256sum] = "c487a5af3efb32f16833d56af4f8d3608846e68022ae36786b228392022f4b55"


PV = "0.3"

# Solo dati (nessuna compilazione, nessuna dipendenza d'architettura).
inherit allarch

do_install() {
    install -d ${D}${localstatedir}/lib/gsoi-model
    install -m 0644 ${UNPACKDIR}/gsoi-qwen3-4b-q4_k_m.gguf \
        ${D}${localstatedir}/lib/gsoi-model/gsoi-qwen3-4b-q4_k_m.gguf
}

# Possiede solo il file: la cartella e' condivisa con la ricetta gsoi-model.
FILES:${PN} = "${localstatedir}/lib/gsoi-model/gsoi-qwen3-4b-q4_k_m.gguf"
