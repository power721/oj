#! /bin/bash

read -p "Do you want to update from git?[Y/n]" PULL
[ "$PULL" == 'Y' -o "$PULL" == 'y' ] && git pull

grep 'devMode' WebRoot/WEB-INF/oj.properties
read -p "Do you want to continue with this mode?[Y/n]" cont
[ "$cont" == 'n' -o "$cont" == 'N' ] && exit

read -p "Input the mysql username, press enter to use default value:" USERNAME
read -p "Input the mysql password, press enter to use default value:" PASSWORD
[ -n "$USERNAME" ] && sed -i "s/dev.user=.*/dev.user=$USERNAME/" WebRoot/WEB-INF/oj.properties
[ -n "$PASSWORD" ] && sed -i "s/dev.password=.*/dev.password=$PASSWORD/" WebRoot/WEB-INF/oj.properties

TOMCAT=/usr/share/tomcat7/webapps
if [ ! -d $TOMCAT ]; then
    TOMCAT=/usr/share/tomcat8/webapps
    if [ ! -d $TOMCAT ]; then
        read -p "Please input the tomcat home path:" TOMCAT
        TOMCAT=$TOMCAT/webapps
        if [ ! -d $TOMCAT ]; then
            echo "Cannot find Tomcat!"
            exit
        fi
    fi
fi
echo "Use tomcat webapps: $TOMCAT"

# find -type d -name assets -exec sudo cp -r {} /var/www/ \;
sudo cp -r WebRoot/assets/ /var/www/

gradle build
sudo cp build/libs/oj.war $TOMCAT

echo "waiting war deploy..."
sleep 10

sudo mv $TOMCAT/oj/upload/ /var/www/ 2>&1 >/dev/null
sudo mv $TOMCAT/oj/download/ /var/www/ 2>&1 >/dev/null

sudo ln -sf /var/www/upload $TOMCAT/oj/upload
sudo ln -sf /var/www/download $TOMCAT/oj/download

echo "OJ deploy completed."
