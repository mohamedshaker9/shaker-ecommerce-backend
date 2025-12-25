FROM eclipse-temurin:17-jdk-focal AS build
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src
RUN mkdir "product_images"
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-focal
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]