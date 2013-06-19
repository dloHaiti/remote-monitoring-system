Server Information
==================
The choice was made to put the database and application on the same server for cost reasons. It is understood this is
not the most robust solution, but it was deemed worth a try during the ramp up because of relatively low traffic.

Scaling in the future would most likely look like:
1 database server
multiple application servers, each with their own FTP server for receiving data from the sensors

Backup
------
There is no automated backup solution in place, and mysql should be backed up regularly at an interval that makes sense.


Security
--------
Endpoints are protected with HTTP Basic authentication. A sensible next step would be HTTPS, which requires a SSL
certificate. It should be a relatively high priority.


Kiosk Information
=================
* Once per day while on the home screen, if the network connection is active, kiosks check for new configuration.
* Configuration can be manually updated from the Configuration screen
* Orders, Readings, and Deliveries can be taken while the network is down
* Orders, Readings, and Deliveries are all sent to the server when the network is available
* Reports do not work while offline as they contact the server


Data Management
===============
As much data as possible is managed through the database. Here's how to do some common scenarios.


Add a Product
-------------
1. connect to the database with mysql workbench or similar
2. insert into the PRODUCT table


Add a Promotion
---------------
1. connect to the database with mysql workbench or similar
2. insert into the PROMOTION table


Add a Kiosk
-----------
1. connect to the database with mysql workbench or similar
2. insert into the Kiosk table


Add a Parameter
---------------
1. connect to the database with mysql workbench or similar
2. insert into the PARAMETER table


Add a SamplingSiteParameter
---------------------------
1. connect to the database with mysql workbench or similar
2. insert into the SAMPLING_SITES_PARAMETER table
* every kiosk gets the same list of these


Add a Sampling Site
-------------------
1. connect to the database with mysql workbench or similar
2. insert into the SamplingSite table
* without a relationship to at least one parameter, this won't show up on any kiosk


Add a Delivery Agent
--------------------
1. connect to the database with mysql workbench or similar
2. insert into the DELIVERY_AGENT table
3. agents must be active to show up for a kiosk
4. agents must belong to the appropriate kiosk


Changing Delivery Information
-----------------------------
There is a DELIVERY_CONFIGURATION table that holds the following information:
1. gallons in a delivery
2. price of a delivery
3. minimum of a delivery
4. maximum of a delivery
5. default that kiosk shows on the delivery tracking screen

Only the first row is paid attention to. Changes must be made to the row with ID=1.


Deleting data
-------------
In general, the data is not designed to be deleted. Instead it should be marked inactive where possible.
