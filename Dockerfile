FROM maven:3.9.11-eclipse-temurin-17-alpine
LABEL authors="N1Fex"

COPY pom.xml /app/
COPY .env /app/
WORKDIR /app/
COPY src /app/src/
RUN mvn clean install -Dmaven.test.skip=true

CMD ["mvn", "spring-boot:run", "-Dmaven.test.skip=true"]

#FROM maven:3.9.11-amazoncorretto-17
#WORKDIR /build/
#COPY pom.xml ./
#COPY .env ./
#RUN mvn dependency:go-offline
#COPY src ./src/
#RUN mvn package -DskipTests
#RUN mkdir -p /opt/app/
#RUN cp $(find ./target/ -name '*.jar') /opt/app/app.jar
#ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]