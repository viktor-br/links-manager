FROM openjdk:8
EXPOSE 8080
CMD java -jar /data/links-manager-0.1.0.jar
ADD target/links-manager-0.1.0.jar /data/links-manager-0.1.0.jar
