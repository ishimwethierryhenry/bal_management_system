#FROM ubuntu:latest
#LABEL authors="USER"
#
#ENTRYPOINT ["top", "-b"]

#FROM maven:3.8.5-openjdk-17 AS build
#WORKDIR /portfolio/portfolio
#COPY . /portfolio
#RUN mkdir -p target
#RUN mvn clean package -DskipTests
#
#FROM openjdk:17.0.1-jdk-slim
#COPY --from=build /target/portfolio-0.0.1-SNAPSHOT.jar portfolio.jar
#EXPOSE 3000
#ENTRYPOINT ["java","-jar","demo.jar"]


#FROM maven:3.8.5-openjdk-17 AS build
#WORKDIR /portfolio/portfolio
#COPY . .
#RUN mvn clean package -DskipTests
#
#FROM openjdk:17.0.1-jdk-slim
#COPY --from=build /portfolio/target/portfolio-0.0.1-SNAPSHOT.jar portfolio.jar
#EXPOSE 3000
#ENTRYPOINT ["java","-jar","portfolio.jar"]


FROM maven:3.8.5-openjdk-17 AS build
# Set working directory to the repository root
WORKDIR /bal_management_system
# Copy everything to the container
COPY . .
# Run Maven from the portfolio subdirectory where pom.xml is located
RUN cd bal_management_system && mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
# Copy the JAR file from where Maven built it
COPY --from=build /bal_management_system/bal_management_system/target/bal_management_system-0.0.1-SNAPSHOT.jar bal_management.jar
EXPOSE 3000
ENTRYPOINT ["java","-jar","bal_management.jar"]