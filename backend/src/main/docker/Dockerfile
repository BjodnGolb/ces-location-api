FROM openjdk:8-jre

#
# Set timezone
#
USER root
ENV TZ=Europe/Oslo
RUN chmod 777 /etc/localtime
RUN chmod 777 /usr/share/zoneinfo/$TZ
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime

#
# Setup catalog
#
RUN mkdir /opt/ces && chmod a+rw /opt/ces
ADD ./backend.jar /opt/ces/
ADD execute.sh /opt/ces/
RUN chmod +x /opt/ces/execute.sh

#
# Start application
#
CMD ["bash","/opt/ces/execute.sh"]
