#!/bin/bash
set -e

# require the app password to be set
if [ $# -ne 1 ]; then
  echo "Usage: $0 MYSQL_APP_PASSWORD"
  exit 1;
fi

# get the latest security patches
sudo apt-get update
sudo apt-get upgrade -y


# get the basics
sudo apt-get install -y tomcat7 mysql-server python-software-properties


# create the database
mysql -u root -p -e 'CREATE DATABASE IF NOT EXISTS dlo;'


# create the app user
mysql -u root -p -e 'CREATE USER app;'
mysql -u root -p -e "GRANT ALL PRIVILEGES ON *.* TO 'app'@'localhost' IDENTIFIED BY '$1';"


# serve on the default HTTP port
sudo iptables -A INPUT -i eth0 -p tcp --dport 80 -j ACCEPT
sudo iptables -A INPUT -i eth0 -p tcp --dport 8080 -j ACCEPT
sudo iptables -A PREROUTING -t nat -i eth0 -p tcp --dport 80 -j REDIRECT --to-port 8080
# open the ftp ports
sudo iptables -A INPUT -i eth0 -p tcp --dport 20 -j ACCEPT
sudo iptables -A INPUT -i eth0 -p tcp --dport 21 -j ACCEPT


# save the iptables rules
sudo apt-get install -y iptables-persistent


# create the ftp folders
mkdir /tmp/{incoming,processed,failed}
chmod a+w /tmp/incoming


# install ftp server
# https://www.digitalocean.com/community/articles/how-to-set-up-vsftpd-on-ubuntu-12-04
# http://stackoverflow.com/q/16102996
sudo add-apt-repository ppa:thefrontiergroup/vsftpd
sudo apt-get update -y
sudo apt-get install -y vsftpd

# create user for sensors
sudo useradd autosensor
sudo passwd autosensor
sudo mkdir /home/autosensor


# set the timezone to eastern
sudo dpkg-reconfigure tzdata


# app configuration
sudo mkdir /etc/appconfig

echo "************************************************************************************"
echo "don't forget to configure vsftpd as per"
echo "https://www.digitalocean.com/community/articles/how-to-set-up-vsftpd-on-ubuntu-12-04"
echo "and add local_root=/tmp/incoming along with allow_writeable_chroot=YES"
echo "then restart with `service vsftpd restart`"
echo "************************************************************************************"

echo "************************************************************************************"
echo "don't forget to set the environment variable APP_DB_PW for database access"
echo "and add the production datasource to /etc/appconfig/Config.groovy"
echo "************************************************************************************"
