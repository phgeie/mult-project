# Schritt 1: Build-Image mit Gradle und JDK 21
FROM gradle:8.4-jdk21 AS build

# Arbeitsverzeichnis festlegen
WORKDIR /app

# Kopiere die Gradle-Konfigurationsdateien und den Quellcode ins Docker-Image
COPY build.gradle settings.gradle /app/
COPY web/build.gradle /app/web/
COPY fx/build.gradle /app/fx/
COPY web/src /app/web/src
COPY fx/src /app/fx/src

# Baue nur das web-Modul
RUN gradle :web:clean :web:build -x test

# Schritt 2: Laufzeit-Image mit schlankem JDK 21
FROM openjdk:21-jdk-slim

# Arbeitsverzeichnis festlegen
WORKDIR /app

# Kopiere das JAR-File des web-Moduls vom Build-Image
COPY --from=build /app/web/build/libs/*.jar app.jar

# Exponiere den Port (Standard: 8080)
EXPOSE 8080

# Start der Anwendung
ENTRYPOINT ["java", "-jar", "app.jar"]
