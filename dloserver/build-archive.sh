#!/bin/bash
set -e

grails test-app
grails prod war

echo "*****************************************************"
echo "ready to scp to server with: scp target/dloserver-version.war user@address:/tmp/dloserver.war"
echo "$ ssh user@address"
echo "$ sudo service tomcat7 stop"
echo "$ sudo rm -rf /var/lib/tomcat7/webapps/dloserver*"
echo "$ sudo mv /tmp/dloserver.war /var/lib/tomcat7/webapps/"
echo "$ sudo service tomcat7 start"
echo "*****************************************************"