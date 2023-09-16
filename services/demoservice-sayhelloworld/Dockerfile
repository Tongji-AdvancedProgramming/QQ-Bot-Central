FROM amazoncorretto:8

COPY target/demoservice-sayhelloworld-1.0.0-SNAPSHOT.jar /root/app.jar

CMD echo 172.17.0.1 host.docker.internal >> /etc/hosts

ENTRYPOINT java -Xmx400m -jar /root/app.jar
EXPOSE 9901
