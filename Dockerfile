FROM oraclelinux:9-slim
EXPOSE 8080
ADD target/book-management.jar book-management.jar
ENTRYPOINT ["java", "-jar", "/book-management.jar"]