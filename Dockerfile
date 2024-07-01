FROM openjdk:17-alpine
COPY ./build/libs/news-service-1.0.jar news-service-1.0.jar
ENTRYPOINT ["java","-jar","news-service-1.0.jar"]