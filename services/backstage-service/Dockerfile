FROM amazoncorretto:8

COPY target/backstage-service-1.0.0-SNAPSHOT.jar /root/app.jar

ENV Bot_AdminPass admin
ENV Bot_JwtKey rememberUs

CMD ip -4 route list match 0/0 | awk '{print $3 " host.docker.internal"}' >> /etc/hosts

ENTRYPOINT java -Xmx1g -Djasypt.encryptor.password=rmbrus -jar /root/app.jar
EXPOSE 9905
