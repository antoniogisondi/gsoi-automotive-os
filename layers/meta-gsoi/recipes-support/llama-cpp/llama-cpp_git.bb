SUMMARY = "llama.cpp — motore di inferenza LLM (server OpenAI-compatible)"
DESCRIPTION = "Motore di inferenza llama.cpp. Fornisce 'llama-server', l'endpoint \
OpenAI-compatible usato dal servizio gsoi-model per servire il modello locale \
(Qwen3-4B) su 127.0.0.1. Include anche llama-cli e llama-quantize."
HOMEPAGE = "https://github.com/ggml-org/llama.cpp"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=223b26b3c1143120c87e2b13111d3e99"

# Sorgente pinnato a un commit preciso (build riproducibili). Per aggiornare
# llama.cpp si cambia SRCREV (verifica anche il md5 della LICENSE se cambia).
SRC_URI = "git://github.com/ggml-org/llama.cpp.git;protocol=https;branch=master"
SRCREV = "7ceed8737fdb4eb09b4760e77bd12d38012de5a8"

PV = "0.0+git"

inherit cmake

# Build CPU di base (Raspberry Pi 5, aarch64/NEON attivo in automatico).
#  - GGML_NATIVE=OFF   : niente -march=native (cross-compilazione corretta)
#  - LLAMA_CURL=OFF    : nessuna dipendenza da libcurl (il .gguf lo fornisce gsoi-model)
#  - BUILD_SHARED_LIBS=OFF : binari autoconsistenti, packaging semplice
#  - LLAMA_BUILD_TESTS=OFF : build piu' snella
#
# NOTA per il Jetson: per l'accelerazione GPU si aggiunge -DGGML_CUDA=ON, che
# richiede il toolchain CUDA (layer meta-tegra). Su RPi5 si resta su CPU.
EXTRA_OECMAKE = " \
    -DGGML_NATIVE=OFF \
    -DLLAMA_CURL=OFF \
    -DBUILD_SHARED_LIBS=OFF \
    -DLLAMA_BUILD_TESTS=OFF \
    -DCMAKE_BUILD_TYPE=Release \
"

do_install:append() {
    # Garantisce i binari chiave in ${bindir}, anche se l'install cmake non li copre.
    install -d ${D}${bindir}
    for b in llama-server llama-cli llama-quantize; do
        if [ -x "${B}/bin/${b}" ]; then
            install -m 0755 "${B}/bin/${b}" "${D}${bindir}/${b}"
        fi
    done
}

FILES:${PN} += " \
    ${bindir}/llama-server \
    ${bindir}/llama-cli \
    ${bindir}/llama-quantize \
"
