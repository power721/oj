#!/usr/bin/env bash

CATALINA_HOME=/usr/share/tomcat8
TOMCAT8_USER=tomcat8
TOMCAT8_GROUP=tomcat8

if [ `id -u` -ne 0 ]; then
	echo "You need root privileges to run this script"
	exit 1
fi

#if [ -d "${CATALINA_HOME}" ]; then
#    echo "Tomcat is exist in ${CATALINA_HOME}"
#    exit 1
#fi

. default.tomcat8

if ! getent group "$TOMCAT8_GROUP" > /dev/null 2>&1 ; then
    addgroup --system "$TOMCAT8_GROUP" --quiet
fi

if ! id ${TOMCAT8_USER} > /dev/null 2>&1 ; then
    adduser --system --home ${CATALINA_HOME} --no-create-home \
        --ingroup "$TOMCAT8_GROUP" --disabled-password --shell /bin/false \
        "$TOMCAT8_USER"
fi

ucf --debconf-ok default.tomcat8 /etc/default/tomcat8
ucf --debconf-ok logrotate.d.tomcat8 /etc/logrotate.d/tomcat8
install -o root -g root -m 755 cron.daily.tomcat8 /etc/cron.daily/tomcat8
install -o root -g root -m 755 init.d.tomcat8 /etc/init.d/tomcat8

chown -R ${TOMCAT8_USER}:${TOMCAT8_GROUP} ${CATALINA_HOME}
