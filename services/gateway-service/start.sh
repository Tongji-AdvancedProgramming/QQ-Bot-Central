#!/bin/bash

# Update /etc/hosts
#echo "172.17.0.1 host.docker.internal" >> /etc/hosts

# Start the Java application
java -Xmx256m -Djasypt.encryptor.password=$bot_je_password -Dbot-config.file=/root/config.ini -jar /root/app.jar
