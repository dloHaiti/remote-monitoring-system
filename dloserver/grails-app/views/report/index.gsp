<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>DLO Haiti: Available Reports</title>
</head>
<body>
    <h1>DLO Haiti Kiosk Reports</h1>
    <ul>
    <g:each in="${reports}">
      <g:link controller="report" action="show" params="[id: it.id]">
          <li>${it.name}</li>
      </g:link>
    </g:each>
    </ul>
</body>
</html>