FROM openjdk:17-alpine
COPY ./build/libs/news-service-1.0.jar news-service-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","news-service-1.0.jar"]