FROM amazoncorretto:8

COPY target/client-1.0.0-SNAPSHOT.jar /root/app.jar
ENTRYPOINT java -Xmx512m -jar /root/app.jar
EXPOSE 9900
