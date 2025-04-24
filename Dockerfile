# Etapa de construcción
FROM eclipse-temurin:21-jdk as build
WORKDIR /app

# Copia todo el código fuente
COPY . .

# Da permisos de ejecución al wrapper de Maven
RUN chmod +x ./mvnw

# Construye el .jar 
RUN ./mvnw clean package

# Etapa de ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia el .jar desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto por defecto de Spring Boot
EXPOSE 5433

# Comando para ejecutar el .jar
ENTRYPOINT ["java", "-jar", "app.jar"]
