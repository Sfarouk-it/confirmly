FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Copy pom.xml first to cache dependencies
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Get the built jar file name and copy it
RUN mv target/*.jar target/app.jar

# Run the application
ENTRYPOINT ["java","-jar","target/app.jar"]