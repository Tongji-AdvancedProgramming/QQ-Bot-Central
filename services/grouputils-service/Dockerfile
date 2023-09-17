FROM amazoncorretto:8

COPY target/gateway-service-1.0.0-SNAPSHOT.jar /root/app.jar
COPY start.sh /root/start.sh

RUN chmod +x /root/start.sh

ENTRYPOINT ["/root/start.sh"]
EXPOSE 9906
