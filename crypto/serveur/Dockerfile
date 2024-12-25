# Utilise l'image officielle de Java pour la construction de l'application
FROM openjdk:17-jdk-slim AS build

# Installez Maven
RUN apt-get update && apt-get install -y maven

# Répertoire de travail dans le container
WORKDIR /app

# Copie les fichiers nécessaires pour construire l'application
COPY pom.xml .
COPY src ./src

# Construire l'application avec Maven 
RUN mvn clean package -DskipTests

# Utilisation d'une image plus légère pour l'exécution de l'application
FROM openjdk:17-jdk-slim

# Répertoire de travail pour l'exécution
WORKDIR /app

# Copie le jar généré à partir du build
COPY --from=build /app/target/*.jar app.jar

# Expose le port 8080
EXPOSE 8080

# Commande pour exécuter l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
