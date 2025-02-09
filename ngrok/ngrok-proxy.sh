#!/bin/bash

# Assurer que les variables d'environnement sont passées correctement
if [ -z "$NGROK_AUTHTOKEN" ]; then
    echo "L'auth token Ngrok est manquant"
    exit 1
fi

if [ -z "$Webapi_URL" ]; then
    echo "L'URL WebAPI est manquante"
    exit 1
fi

if [ -z "$Cryptoserveur_URL" ]; then
    echo "L'URL Cryptoserveur est manquante"
    exit 1
fi

# Connecter Ngrok avec l'authtoken
echo "Authentification avec Ngrok..."
ngrok authtoken $NGROK_AUTHTOKEN

# Démarrer les tunnels Ngrok pour les deux services
# Tunnel pour WebAPI (Spring Boot sur le port 8080)
echo "Démarrage du tunnel pour WebAPI..."
ngrok http $Webapi_URL --region=ap &

# Tunnel pour Cryptoserveur (Spring Boot sur le port 8081 par exemple)
echo "Démarrage du tunnel pour Cryptoserveur..."
ngrok http $Cryptoserveur_URL --region=ap &

# Attendre que les tunnels soient actifs et récupérer les URL
sleep 5

# Récupérer les URL publiques des tunnels via l'API de Ngrok
WEBAPI_PUBLIC_URL=$(curl -s http://127.0.0.1:4040/api/tunnels | jq -r '.tunnels[0].public_url')
CRYPTOSERVEUR_PUBLIC_URL=$(curl -s http://127.0.0.1:4041/api/tunnels | jq -r '.tunnels[0].public_url')

echo "Tunnel WebAPI accessible à : $WEBAPI_PUBLIC_URL"
echo "Tunnel Cryptoserveur accessible à : $CRYPTOSERVEUR_PUBLIC_URL"

# # Optionnel: Tu peux ici envoyer ces URLs à Firebase ou à tout autre service nécessaire.
# # Par exemple, une API pour envoyer les URL à Firebase :
# curl -X POST -H "Content-Type: application/json" \
#     -d '{"webapi_url": "'$WEBAPI_PUBLIC_URL'", "cryptoserveur_url": "'$CRYPTOSERVEUR_PUBLIC_URL'"}' \
#     https://YOUR_FIREBASE_FUNCTION_URL/endpoint
