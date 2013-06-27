This project was designed to utilize the database as much as possible,
on the client and the server

Effort has been made to stick the conventions of the frameworks and
platforms where available. For example, most of the Android code for
database access follows the online documentation. Hopefully this makes
things easier for the next person.

dloserver
=========
* grails 2.2.1 ([documentation][docs])
* mysql
* vsftpd


development
-----------
install `gvm` from gvmtool.net by opening up a Terminal (Mac) and typing

```shell
$ curl -s get.gvmtool.net | bash
```

next, we'll need to install grails 2.2.1

```shell
$ gvm install grails 2.2.1
```

get the source code, run the server

```shell
$ git clone https://github.com/dloHaiti/remote-monitoring-system.git
$ cd remote-monitoring-system
$ grails run-app
```

the server should now be running @ http://localhost:8080/dloserver with an in-memory database.
to access the database, head to http://localhost:8080/dloserver/dbconsole

changes can be made in any text editor, preferably with tests.
changes to the structure of the database will require a [database migration][dbm]

Once you're confident in the changes, it's time to push to production.

deployment
----------
1. run all the automated tests
```shell
$ grails test-app
```

2. package the application
```shell
$ grails prod war
```

3. push to server
```shell
$ scp target/dloserver-<version>.war user@address:/tmp/dloserver.war
```

4. restart the application on the server
```shell
$ ssh user@address
$ sudo service tomcat7 stop
$ sudo rm -rf /var/lib/tomcat7/webapps/dloserver*
$ sudo mv /tmp/dloserver.war /var/lib/tomcat7/webapps
$ sudo service tomcat7 start
```

creating a production-like environment
--------------------------------------
To get a production-like server up and running, create a vm with 2GB RAM and
install Ubuntu 12.04 32-bit. `scp` a copy of `server-setup.sh` over to the vm
and run it. Fill in the prompts as they appear. Note the script assumes you'll
set a mysql root password and prompts for it 3 times in a row.

```shell
$ scp dloserver/server-setup.sh username@address:/tmp/
The authenticity of host 'address' can't be established.
ECDSA key fingerprint is _fingerprint_.

$ Are you sure you want to continue connecting (yes/no)? yes
user@address's password:
server-setup.sh                                                                       100% 1032     1.0KB/s   00:00

$ ssh user@address
user@address's password:
Welcome to Ubuntu 12.04 LTS (GNU/Linux 3.2.0-24-virtual i686)

 * Documentation:  https://help.ubuntu.com/
Last login: Fri May  3 18:28:34 2013

$ /etc/server-setup.sh
```

The kiosk object is attached through the request by the `KioskFilters` class.

testing
-------
Integration tests are meant to cover accepted formats and responses.


dlokiosk
========
* roboguice


[docs]: http://grails.org/documentation
[dbm]: http://grails-plugins.github.io/grails-database-migration/docs/manual/index.html