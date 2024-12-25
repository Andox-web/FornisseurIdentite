# Utilisation de l'image de base pour .NET SDK (pour la phase de build)
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers .csproj et restaurer les dépendances
COPY . .
RUN dotnet restore

# Construire le projet
RUN dotnet publish -c Release -o out

# Utilisation d'une image plus légère pour exécuter l'application
FROM mcr.microsoft.com/dotnet/aspnet:8.0

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers publiés depuis la phase de build
COPY --from=build /app/out .

# Définir la commande par défaut pour exécuter l'application
ENTRYPOINT ["dotnet", "FournisseurIdentite.dll"]

# Exposer le port sur lequel l'API écoutera
EXPOSE 8080
