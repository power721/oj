#! /bin/bash

read -p "Do you want to update from git?[Y/n]" PULL
[ "$PULL" == 'Y' -o "$PULL" == 'y' ] && git pull

grep 'devMode' WebRoot/WEB-INF/oj.properties
read -p "Do you want to continue with this mode?[Y/n]" cont
[ "$cont" == 'n' -o "$cont" == 'N' ] && exit

read -p "Input the mysql username, press enter to use default value: " USERNAME
read -p "Input the mysql password, press enter to use default value: " PASSWORD
[ -n "$USERNAME" ] && sed -i "s/dev.user=.*/dev.user=$USERNAME/" WebRoot/WEB-INF/oj.properties
[ -n "$PASSWORD" ] && sed -i "s/dev.password=.*/dev.password=$PASSWORD/" WebRoot/WEB-INF/oj.properties

if [ $# -gt 0 ]; then
    TOMCAT=$1
    USER=`stat -c '%U' $TOMCAT/`
    GROUP=`stat -c '%G' $TOMCAT/`
else
    USER=tomcat7
    GROUP=tomcat7
    TOMCAT=/usr/share/tomcat7/webapps
fi

if [ ! -d $TOMCAT ]; then
    USER=tomcat8
    GROUP=tomcat8
    TOMCAT=/usr/share/tomcat8/webapps
    if [ ! -d $TOMCAT ]; then
        read -p "Please input the tomcat home path: " TOMCAT
        TOMCAT=$TOMCAT/webapps
        if [ ! -d $TOMCAT ]; then
            echo "Cannot find Tomcat!"
            exit
        fi
        USER=`stat -c '%U' $TOMCAT/`
        GROUP=`stat -c '%G' $TOMCAT/`
    fi
fi
echo "Use tomcat webapps: $TOMCAT"

# find -type d -name assets -exec sudo cp -r {} /var/www/ \;

sudo cp -r WebRoot/assets/ /var/www/

gradle build
[ $? -ne 0 ] && exit 1

sudo rm -rf $TOMCAT/oj/upload
sudo rm -rf $TOMCAT/oj/download

sudo cp build/libs/oj.war $TOMCAT

echo "waiting war deploy..."
sleep 10
CNT=0
while [ ! -e $TOMCAT/oj/upload/ ]; do
    echo "Please start the tomcat service!"
    ((CNT++))
    sleep 5
    if [ $CNT -eq 5 ]; then
        sudo touch $TOMCAT/oj.war
        CNT=0
    fi
done

sudo rm -rf $TOMCAT/oj/assets/
sudo cp -r $TOMCAT/oj/upload/ /var/www/ 2>&1 >/dev/null
sudo rm -rf $TOMCAT/oj/upload/
sudo cp -r $TOMCAT/oj/download/ /var/www/ 2>&1 >/dev/null
sudo rm -rf $TOMCAT/oj/download/

sudo chown -R $USER:$GROUP /var/www/assets
sudo chown -R $USER:$GROUP /var/www/upload
sudo chown -R $USER:$GROUP /var/www/download

sudo ln -sf /var/www/assets $TOMCAT/oj/assets
sudo ln -sf /var/www/upload $TOMCAT/oj/upload
sudo ln -sf /var/www/download $TOMCAT/oj/download

echo "OJ deploy completed."
