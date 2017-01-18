#!/usr/bin/env bash

CONF=src/main/resources/oj.properties
ASSETS=src/main/webapp/assets
ARTIFACT=target/oj.war

function parse() {
  while [ $# -gt 0 ]; do
    case $1 in
      "-q")
      QUICK=true
      shift
      ;;
      *)
      TOMCAT=$1
      shift
      ;;
    esac
  done
}

parse "$@"

if [ "${QUICK}" ]; then
  echo "use current git reversion"
else
  read -p "Do you want to update from git?[Y/n]" PULL
  [ "$PULL" = 'Y' -o "$PULL" = 'y' ] && git pull
fi

grep '^\s*devMode=' ${CONF}
if [ -z "${QUICK}" ]; then
  read -p "Do you want to continue with this mode?[Y/n]" cont
  [ "$cont" = 'n' -o "$cont" = 'N' ] && exit
fi
grep 'judge.version' ${CONF}

if [ "${QUICK}" ]; then
  echo "use user/password from configuration file"
else
  read -p "Input the mysql username, press enter to use default value: " USERNAME
  read -p "Input the mysql password, press enter to use default value: " PASSWORD
  [ -n "$USERNAME" ] && sed -i "s/dev.user=.*/dev.user=$USERNAME/" ${CONF}
  [ -n "$PASSWORD" ] && sed -i "s/dev.password=.*/dev.password=$PASSWORD/" ${CONF}
fi

git rev-parse --short HEAD >src/main/webapp/WEB-INF/view/common/version.ftl

COMPILERS=src/main/webapp/WEB-INF/view/common/compilers.ftl
echo "<li>GCC/G++ $(g++ --version | head -n1 | awk '{print $4}')</li>" >${COMPILERS}
echo "<li>$(fpc -v | head -n1)</li>" >>${COMPILERS}
echo "<li>$(javac -version 2>&1 | head -n1) & $(java -version 2>&1 | head -n1)</li>" >>${COMPILERS}
echo "<li>$(python --version 2>&1 | head -n1)</li>" >>${COMPILERS}

if [ ! -d "${TOMCAT}/webapps" ]; then
  TOMCAT=${CATALINA_HOME}/webapps
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
        USER=tomcat8
        GROUP=tomcat8
        TOMCAT=/var/lib/tomcat8/webapps
    fi

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

if [ -d /var/www/upload/ ]; then
  sudo mkdir -p ~/oj_backup/upload/
  echo "backup /var/www/upload/ to ~/oj_backup/upload/"
  sudo rsync -r /var/www/upload/ ~/oj_backup/upload/
  #sudo rm -rf ${TOMCAT}/oj/upload
  #sudo rm -rf ${TOMCAT}/oj/download
else
  mkdir -p /var/www/upload/
  mkdir -p /var/www/download/
  mkdir -p /var/www/assets/
fi

echo "copy ${ARTIFACT} to $TOMCAT/"
sudo cp ${ARTIFACT} ${TOMCAT}/

echo "waiting war deploy..."
sleep 15
CNT=0

# FOLDER=${TOMCAT}/oj/assets/MathJax/jax/
# while [ ! -d "${FOLDER}" ] || [ ${FOLDER} -ot ${TOMCAT}/oj.war ]; do
#     echo "Please start the tomcat service!"
#     let CNT+=1
#     sleep 5
#     if [ ${CNT} -eq 5 ]; then
#         sudo touch ${TOMCAT}/oj.war
#         CNT=0
#     fi
# done

if grep -q '^\s*devMode=true' ${CONF}; then
  echo "use log4j-dev.xml"
  sudo cp src/main/resources/log4j-dev.xml $(find ${TOMCAT}/oj/ -type f -name log4j.xml)
fi

if [ -d /var/log/nginx/ ]; then
    USER=`stat -c '%U' /var/log/nginx/`
    GROUP=`stat -c '%G' /var/log/nginx/`
fi
[ ! -d /var/www/assets ] && mkdir -p /var/www/assets
[ ! -d /var/www/upload ] && mkdir -p /var/www/upload
[ ! -d /var/www/download ] && mkdir -p /var/www/download
echo "change owner to $USER:$GROUP"
sudo chown -R ${USER}:${GROUP} /var/www/assets
sudo chown -R ${USER}:${GROUP} /var/www/upload
sudo chown -R ${USER}:${GROUP} /var/www/download

sudo chmod -R 775 /var/www/assets
sudo chmod -R 775 /var/www/upload
sudo chmod -R 775 /var/www/download
echo "/var/www/"
ls -l --color=auto /var/www/

sudo rm -rf ${TOMCAT}/oj/upload
sudo rm -rf ${TOMCAT}/oj/download
sudo rm -rf ${TOMCAT}/oj/assets

echo "$TOMCAT/oj/"
sudo ls -l --color=auto ${TOMCAT}/oj/
sudo find ${TOMCAT}/oj/WEB-INF/ -type f -exec chmod 600 {} \;

echo "OJ deploy completed."
