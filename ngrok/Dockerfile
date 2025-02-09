# Utiliser l'image officielle Ngrok
FROM ngrok/ngrok:latest

# Étape 1: Définir le répertoire de travail
WORKDIR /ngrok

# Étape 2: Copier le script ngrok-proxy.sh dans le conteneur
COPY ngrok-proxy.sh /ngrok/ngrok-proxy.sh

# Étape 3: Donner les permissions d'exécution au script
RUN chmod +x /ngrok/ngrok-proxy.sh

# Étape 4: Exposer les ports nécessaires
EXPOSE 4040 4041

# Étape 5: Exécuter le script au démarrage du conteneur
CMD ["bash", "/ngrok/ngrok-proxy.sh"]
