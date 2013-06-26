Server Information
==================
The choice was made to put the database and application on the same server for cost reasons. It is understood this is
not the most robust solution, but it was deemed worth a try during the ramp up because of relatively low traffic.

Scaling in the future would most likely look like:
1 database server
multiple application servers, each with their own FTP server for receiving data from the sensors

Backup
------
There is no automated backup solution in place, and mysql should be backed up regularly.


Security
--------
Endpoints are protected with HTTP Basic authentication. A sensible next step would be HTTPS, which requires a SSL
certificate. It should be a relatively high priority.


FTP
---
username: autosensor

The FTP server dumps files to /tmp/incoming on the server. From there, the application moves successfully imported files
to /tmp/processed and failed files to /tmp/failed. The application logs to /var/log/tomcat7/catalina.out


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

base64encoded_image must be a 144x144 PNG. Images can be converted with many free online tools.
Products must be active to end up on the kiosk.
maximum and minimum quantities are not necessary. Specifying them will pop up the quantity spinner in the kiosk.
    ensure maximum is a number greater than minimum


Add a Promotion
---------------
1. connect to the database with mysql workbench or similar
2. insert into the PROMOTION table

amount
    must be positive
type
    must either be 'AMOUNT' or 'PERCENT'
applies_to
    must either be 'SKU' or 'BASKET'
sku
    the sku of the promotion
    appears as a line item on receipts
product_sku
    the sku of the product the discount applies to, if applicable
    can be null
    must match exactly existing product
    if basket contains sku that promotion applies to, promotion is available to add to cart on kiosk
start_date and end_date
    determine whether or not a promotion will be shown on the kiosk


Add a Kiosk
-----------
1. connect to the database with mysql workbench or similar
2. insert into the KIOSK table

name
    the kioskId
api_key
    the password on the kiosk


Add a Parameter
---------------
1. connect to the database with mysql workbench or similar
2. insert into the PARAMETER table

active
    must be true to be shown in reports
is_ok_not_ok
    'true' makes this OK/NOT OK radio buttons on kiosk
    'false' makes this a decimal field on kiosk
is_used_in_totalizer
    used in determining the totalizer line on the volumeByDay report
    only 'Gallons distributed' are used in the totalizer
manual
    true to show up on kiosk
    false for parameters measured by things like solar sensors
minimum and maximum
    can be null
    validate the input result on the kiosk
    displayed as the range of valid values on the kiosk
name
    displayed on the kiosk
unit
    shown on manual reading entry screen

Add a SamplingSiteParameter
---------------------------
1. connect to the database with mysql workbench or similar
2. insert into the SAMPLING_SITES_PARAMETER table

every kiosk gets the same list of these
just a join table between sampling_site_id and parameter_id
controls the parameters that appear when a sampling site is selected on the kiosk


Add a Sampling Site
-------------------
1. connect to the database with mysql workbench or similar
2. insert into the SamplingSite table

without a relationship to at least one parameter, this won't show up on any kiosk

name
    name of the sampling site
is_used_for_totalizer
    many sampling sites have 'Gallons distributed', but only sites with 'true' are used in calculation
followup_to_site_id
    can be null
    reference to another sampling_site_id
    totalizer calculation requires us to subtract late day numbers from early day numbers.

    example: 'borehole - late day' would reference the id of 'borehole - early day'


Add a Delivery Agent
--------------------
1. connect to the database with mysql workbench or similar
2. insert into the DELIVERY_AGENT table
3. agents must be active to show up for a kiosk
4. agents must belong to the appropriate kiosk

name
    name of agent
kiosk_id
    id of kiosk this agent belongs to
active
    must be true to show up on kiosk


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
