<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>DLO Haiti: Available Reports</title>
</head>
<body>
    <h1>DLO Haiti Kiosk Reports</h1>
    <h2 style="text-align: center;"><g:link action="volumeByDay" params="[kioskName: kioskName]">Volume par jour</g:link></h2>
    <h2 style="text-align: center;"><g:link action="salesByDay" params="[kioskName: kioskName]">Ventes par jour</g:link></h2>
    <h2 style="text-align: center;"><g:link action="readings" params="[kioskName: kioskName]">Mesures eau</g:link></h2>
</body>
</html>