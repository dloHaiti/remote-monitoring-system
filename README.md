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
install `git`. GitHub has provided an [excellent guide][installing_git].

install `gvm` from [gvmtool.net][gvm] by opening up a Terminal (Mac) and typing

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
$ cd remote-monitoring-system/dloserver
$ grails run-app
```

the server should now be running @ http://localhost:8080/dloserver with an in-memory database.
to access the database, head to http://localhost:8080/dloserver/dbconsole

changes can be made in any text editor, preferably with tests.
changes to the structure of the database will require a [database migration][dbm]

Once you're confident in the changes, commit them.

```shell
$ git add -A
$ git commit -m "message about something specific I changed"
$ git push origin master
```

After the changes are committed, we can push to production.

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
Uses the [RoboGuice][roboguice] framework. Requires Android 4.0.+


development
-----------
1. Install [Apache Maven][mvn]. 
   This is installed when running `mvn --version` from the terminal prints the version information
2. Install [Android Development Tools][adt]
3. Set up the ADT Bundle by referring to [Google's guide][setup_adt].
4. Download the [recommended][recommended] packages, platforms, and tools with the SDK manager. 
   This steps takes a considerable amount of time.
5. Create an emulator based on the Nexus 7 named 'dloKioski' and start it

Tests can be run with `mvn clean test`
Deploy the code to the emulator with `mvn clean package android:deploy`


device deployment
-----------------
1. Create an archive for deployment to devices with `mvn clean package`
2. Host the resulting archive (named 'DLO Kiosk.apk') on dropbox or similar
3. Ensure all data is off the existing kiosks by hitting the 'Manual Sync' button in 'Configuration' on each kiosk
4. Remove the application from the kiosks
5. Download the archive from the dropbox (or similar) link
6. Install the archive and ensure it is pointing to the correct server with username and password


license
=======
Apache 2.0


[installing_git]: https://help.github.com/articles/set-up-git
[gvm]: http://gvmtool.net/
[docs]: http://grails.org/documentation
[dbm]: http://grails-plugins.github.io/grails-database-migration/docs/manual/index.html
[roboguice]: https://github.com/roboguice/roboguice
[mvn]: https://maven.apache.org/
[adt]: https://developer.android.com/sdk/installing/bundle.html
[setup_adt]: https://developer.android.com/sdk/installing/bundle.html
[recommended]: https://developer.android.com/tools/help/sdk-manager.html
