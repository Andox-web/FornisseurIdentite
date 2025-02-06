# Utilise l'image officielle Node.js pour la construction de l'application Vue.js
FROM node:16 AS build

# Répertoire de travail dans le container
WORKDIR /app

# Copie tous les fichiers du projet Vue.js dans le container
COPY . .

# Installe les dépendances
# RUN npm install

# Génère le build de l'application Vue.js
RUN npm run build

# Utilise une image Nginx pour servir les fichiers statiques
FROM nginx:alpine

# Copie le contenu généré par Vue.js dans le répertoire public de Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Expose le port 80 pour le frontend
EXPOSE 80

# Démarre Nginx
CMD ["nginx", "-g", "daemon off;"]
