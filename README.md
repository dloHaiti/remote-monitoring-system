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

The kiosk object is attached through the request by the `KioskFilters` class

testing
-------
Integration tests are meant to cover accepted formats and responses.


dlokiosk
========
* roboguice
