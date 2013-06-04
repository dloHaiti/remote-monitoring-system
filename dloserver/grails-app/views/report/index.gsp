<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body>
    <g:each in="${measurements}">
        <p>Parameter: ${it.parameter}</p>
        <p>Value: ${it.value}</p>
    </g:each>
</body>
</html>