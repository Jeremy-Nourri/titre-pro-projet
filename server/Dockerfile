# Étape 1 : Construire l'application avec Maven
FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app

# Copier le fichier de configuration Maven
COPY pom.xml .

# Installer les dépendances Maven
RUN mvn dependency:go-offline

# Copier le code source de l'application
COPY src ./src

# Construire le JAR de l'application
RUN mvn clean package -DskipTests

# Étape 2 : Exécuter l'application
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copier le fichier JAR généré depuis l'étape précédente
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port 8080 pour l'API
EXPOSE 8080

# Commande pour exécuter l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
