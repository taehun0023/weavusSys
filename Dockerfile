# Build the application
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

# Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/weavusys-backend.jar weavusys-backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/weavusys-backend.jar"]
