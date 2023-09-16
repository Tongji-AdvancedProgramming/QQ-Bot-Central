FROM amazoncorretto:8

COPY target/backstage-service-1.0.0-SNAPSHOT.jar /root/app.jar

ENV Bot_AdminPass admin
ENV Bot_JwtKey rememberUs

ENTRYPOINT java -Xmx1g -jar /root/app.jar -Djasypt.encryptor.password=rmbrus
EXPOSE 9905
