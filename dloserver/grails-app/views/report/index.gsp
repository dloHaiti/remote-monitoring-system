<%--
  Created by IntelliJ IDEA.
  User: Thoughtworker
  Date: 5/10/13
  Time: 4:09 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body>
    <g:each in="${measurements}">
        <p>Parameter: ${it.parameter}</p>
        <p>TimeStamp: ${it.timestamp}</p>
        <p>Value: ${it.value}</p>
    </g:each>
</body>
</html>