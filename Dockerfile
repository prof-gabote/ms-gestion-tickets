# Etapa 1: Construcción. Instala Maven y compila el proyecto
# Usa una imagen de Maven con JDK 21 (puedes cambiarla según tu versión de Java)
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Directorio de trabajo
WORKDIR /app

# Copiamos los archivos de Maven para aprovechar el cache de dependencias
COPY pom.xml .
COPY src ./src
COPY wallet ./wallet

# Compilamos el proyecto
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jdk

# Directorio de trabajo
WORKDIR /app

# Copiamos el jar desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/wallet ./wallet

# Exponemos el puerto
EXPOSE 8081

# Comando para correr la app
ENTRYPOINT ["java", "-jar", "app.jar"]

# Comandos
# docker build -t ms-gestion-tickets .
# docker run -p 8081:8081 ms-gestion-tickets
# git clone --branch nombre-de-la-rama --single-branch url-del-repo

