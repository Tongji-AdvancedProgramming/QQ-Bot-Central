FROM amazoncorretto:8

COPY target/backstage-service-1.0.0-SNAPSHOT.jar /root/app.jar
ENTRYPOINT java -Xmx1g -jar /root/app.jar
EXPOSE 9905
