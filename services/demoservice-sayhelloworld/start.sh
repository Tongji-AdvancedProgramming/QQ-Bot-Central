#!/bin/bash

# Update /etc/hosts
#echo "172.17.0.1 host.docker.internal" >> /etc/hosts

# Start the Java application
java -Xmx128m -Djasypt.encryptor.password=rmbrus -Dbot-config.file=/root/config.ini -jar /root/app.jar
