
# Usa una imagen base de Java (OpenJDK 17 por ejemplo)
#FROM eclipse-temurin:17-jdk-alpine
FROM eclipse-temurin:21-jdk-alpine

# Crea un directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR desde tu sistema local al contenedor
COPY /target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que tu app se ejecuta (ajusta si es diferente)
EXPOSE 8080

# Comando para ejecutar el JAR
ENTRYPOINT ["java", "-jar", "app.jar"]