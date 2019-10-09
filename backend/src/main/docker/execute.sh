#!/bin/bash

# Start application
java -Xmx512m \
-Dserver.port=$PORT \
-jar /opt/ces/backend.jar
