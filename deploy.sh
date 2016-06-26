#!/usr/bin/env bash

CONF=src/main/resources/oj.properties
ASSETS=src/main/webapp/assets
ARTIFACT=target/oj.war

read -p "Do you want to update from git?[Y/n]" PULL
[ "$PULL" = 'Y' -o "$PULL" = 'y' ] && git pull

grep 'devMode' ${CONF}
read -p "Do you want to continue with this mode?[Y/n]" cont
[ "$cont" = 'n' -o "$cont" = 'N' ] && exit

read -p "Input the mysql username, press enter to use default value: " USERNAME
read -p "Input the mysql password, press enter to use default value: " PASSWORD
[ -n "$USERNAME" ] && sed -i "s/dev.user=.*/dev.user=$USERNAME/" ${CONF}
[ -n "$PASSWORD" ] && sed -i "s/dev.password=.*/dev.password=$PASSWORD/" ${CONF}

if [ $# -gt 0 ]; then
    TOMCAT=$1/webapps
    USER=`stat -c '%U' ${TOMCAT}/`
    GROUP=`stat -c '%G' ${TOMCAT}/`
fi

if [ ! -d "${TOMCAT}" ]; then
    USER=tomcat7
    GROUP=tomcat7
    TOMCAT=/usr/share/tomcat7/webapps
fi

if [ ! -d "${TOMCAT}" ]; then
    USER=tomcat8
    GROUP=tomcat8
    TOMCAT=/usr/share/tomcat8/webapps
    if [ ! -d "${TOMCAT}" ]; then
        read -p "Please input the tomcat home path: " TOMCAT
        TOMCAT=${TOMCAT}/webapps
        if [ ! -d "${TOMCAT}" ]; then
            echo "Cannot find Tomcat!"
            exit
        fi
        USER=`stat -c '%U' ${TOMCAT}/`
        GROUP=`stat -c '%G' ${TOMCAT}/`
    fi
fi
echo "Use tomcat webapps: $TOMCAT"

# find -type d -name assets -exec sudo cp -r {} /var/www/ \;

[ -L ${TOMCAT}/oj/assets ] && sudo unlink ${TOMCAT}/oj/assets
[ -L ${TOMCAT}/oj/upload ] && sudo unlink ${TOMCAT}/oj/upload
[ -L ${TOMCAT}/oj/download ] && sudo unlink ${TOMCAT}/oj/download
sudo rsync -r ${ASSETS}/ /var/www/assets/

#export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64

#gradle build
#[ $? -ne 0 ] && exit 1

mvn clean package || exit 1

sudo mkdir -p ~/oj_backup/upload/image/
echo "backup /var/www/upload/image/ to ~/oj_backup/upload/image/"
sudo rsync -r /var/www/upload/image/ ~/oj_backup/upload/image/
sudo rm -rf ${TOMCAT}/oj/upload
sudo rm -rf ${TOMCAT}/oj/download

echo "copy ${ARTIFACT} to $TOMCAT/"
sudo cp ${ARTIFACT} ${TOMCAT}/

echo "waiting war deploy..."
sleep 10
CNT=0
while [ ! -d "${TOMCAT}/oj/WEB-INF/" ] || [ ${TOMCAT}/oj/WEB-INF/ -ot ${TOMCAT}/oj.war ]; do
    echo "Please start the tomcat service!"
    let CNT+=1
    sleep 5
    if [ ${CNT} -eq 5 ]; then
        sudo touch ${TOMCAT}/oj.war
        CNT=0
    fi
done

echo "remove $TOMCAT/oj/assets/"
if [ ! -L ${TOMCAT}/oj/assets ]; then
    echo "delete director ${TOMCAT}/oj/assets"
    sudo rm -rf ${TOMCAT}/oj/assets
fi

#sudo cp -r ${TOMCAT}/oj/upload/ /var/www/ 2>&1 >/dev/null
#sudo rm -rf ${TOMCAT}/oj/upload/
#sudo cp -r ${TOMCAT}/oj/download/ /var/www/ 2>&1 >/dev/null
#sudo rm -rf ${TOMCAT}/oj/download/

if [ -d /var/log/nginx/ ]; then
    USER=`stat -c '%U' /var/log/nginx/`
    GROUP=`stat -c '%G' /var/log/nginx/`
fi
echo "change owner to $USER:$GROUP"
sudo chown -R ${USER}:${GROUP} /var/www/assets
sudo chown -R ${USER}:${GROUP} /var/www/upload
sudo chown -R ${USER}:${GROUP} /var/www/download

#sudo chmod -R 664 /var/www/assets
#sudo chmod -R 664 /var/www/upload
#sudo chmod -R 664 /var/www/download
echo "/var/www/"
ls -l --color=auto /var/www/

echo "make soft link"
### Issue: when delete oj.war from webapps, the directory /var/www/upload will be deleted by Tomcat  ###
sudo ln -sf /var/www/assets ${TOMCAT}/oj/
sudo ln -sf /var/www/upload ${TOMCAT}/oj/
sudo ln -sf /var/www/download ${TOMCAT}/oj/
echo "$TOMCAT/oj/"
ls -l --color=auto ${TOMCAT}/oj/

echo "OJ deploy completed."
