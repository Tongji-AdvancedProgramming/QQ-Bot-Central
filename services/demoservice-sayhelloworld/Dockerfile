FROM amazoncorretto:8

COPY target/demoservice-sayhelloworld-1.0.0-SNAPSHOT.jar /root/app.jar

CMD ip -4 route list match 0/0 | awk '{print $3 " host.docker.internal"}' >> /etc/hosts

ENTRYPOINT java -Xmx400m -jar /root/app.jar
EXPOSE 9901
