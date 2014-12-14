git pull
find -type f -name oj.properties -exec grep 'devMode' {} \;
read -p "Do you want to continue?"
find -type d -name assets -exec sudo cp -r {} /var/www/ \;
gradle build
sudo cp build/libs/oj.war /usr/share/tomcat7/webapps/
