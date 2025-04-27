FROM amazoncorretto:17-alpine AS build
WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Copy the parent POM first and install it
COPY pom.xml .
RUN mvn install -N

# Copy Common module and install it
COPY Common Common/
RUN cd Common && mvn clean install -DskipTests

# Copy and build SchoolService
COPY SchoolService SchoolService/
RUN cd SchoolService && mvn clean package -DskipTests

# Create runtime image
FROM amazoncorretto:17-alpine
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/SchoolService/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]