This project was designed to utilize the database as much as possible,
on the client and the server

Effort has been made to stick the conventions of the frameworks and
platforms where available. For example, most of the Android code for
database access follows the online documentation. Hopefully this makes
things easier for the next person.

dloserver
=========
* grails 2.2.1
* mysql
* vsftpd

To get a production-like server up and running, create a vm with 2GB RAM and
install Ubuntu 12.04 32-bit. `scp` a copy of `server-setup.sh` over to the vm
and run it. Fill in the prompts as they appear. Note the script assumes you'll
set a mysql root password and prompts for it 3 times in a row.

```shell
$ scp dloserver/scripts/server-setup.sh username@address:/tmp/
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
