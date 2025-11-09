# # File: `Dockerfile`
# # Build stage: uses Maven with Java 21 to compile and package the app
# FROM maven:3.9.9-eclipse-temurin-21 AS build
# WORKDIR /app

# # Cache dependencies first
# COPY pom.xml .
# RUN mvn -q -DskipTests dependency:go-offline

# # Build the application
# COPY src ./src
# RUN mvn -B -DskipTests package

# # Runtime stage: minimal JRE image
# FROM eclipse-temurin:21-jre
# WORKDIR /app

# # Run as non-root user
# RUN useradd -r -u 10001 spring
# USER 10001

# # Copy the fat JAR from the build stage
# COPY --from=build /app/target/*.jar app.jar

# # Configure runtime
# EXPOSE 8080
# ENV SPRING_PROFILES_ACTIVE=default
# ENV JAVA_OPTS=""

# # Start the app
# ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
