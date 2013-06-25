#!/bin/bash
set -e

# get the latest security patches
sudo apt-get update
sudo apt-get upgrade -y


# get the basics
sudo apt-get install -y tomcat7 mysql-server


# create the database
mysql -u root -e 'CREATE DATABASE dlo;'


# create the app user
mysql -u root -e 'CREATE USER app;'
mysql -u root -e "GRANT ALL PRIVILEGES ON *.* TO 'app'@'localhost' IDENTIFIED BY 'password123';"


# serve on the default HTTP port
iptables -A INPUT -i eth0 -p tcp --dport 80 -j ACCEPT
iptables -A INPUT -i eth0 -p tcp --dport 8080 -j ACCEPT
iptables -A PREROUTING -t nat -i eth0 -p tcp --dport 80 -j REDIRECT --to-port 8080


# save the iptables rules
sudo apt-get install -y iptables-persistent
