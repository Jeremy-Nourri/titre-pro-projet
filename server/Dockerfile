# Étape 1 : Construction de l'application
FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app

# Copier le fichier de configuration Maven et installer les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le code source et construire le JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copier le fichier JAR depuis l'étape de construction
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port 8080
EXPOSE 8080

# Commande pour exécuter l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
