#!/bin/bash

# Update /etc/hosts
#echo "172.17.0.1 host.docker.internal" >> /etc/hosts

# Start the Java application
java -Xmx256m -jar /root/app.jar
