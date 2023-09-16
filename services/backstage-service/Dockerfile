FROM amazoncorretto:8

COPY target/backstage-service-1.0.0-SNAPSHOT.jar /root/app.jar
COPY start.sh /root/start.sh

ENV Bot_AdminPass admin
ENV Bot_JwtKey rememberUs

RUN chmod +x /root/start.sh

ENTRYPOINT ["/root/start.sh"]
EXPOSE 9905
