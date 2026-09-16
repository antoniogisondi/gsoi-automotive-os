# GSOI: in questa immagine il cervello locale e' presente, quindi anche le
# shell di login (es. 'jarvis-mini cli' di test via ssh/seriale) usano il
# modello locale invece del mock. Il servizio systemd ha gia' queste
# variabili tramite il drop-in 10-gsoi-model.conf.
export JARVIS_AI=local
export JARVIS_MODEL_URL=http://127.0.0.1:8091/v1
export JARVIS_MODEL_NAME=qwen3-4b-instruct
