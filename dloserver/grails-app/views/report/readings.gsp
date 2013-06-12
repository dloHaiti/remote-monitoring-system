<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Readings Report</title>
</head>
<body>
<g:each in="${parameters}">
    <h1>${it.name}</h1>
    <table>
        <thead>
        <tr>
            <th>Date</th>
        <g:each var="site" in="${it.samplingSites}">
            <th>${site.name}</th>
        </g:each>
        </tr>
        </thead>
        <tbody>
        <g:each var="d" in="${lastWeek}">
            <tr>
                <td><g:formatDate date="${d}" format="MM/dd/yy"/></td>
                <g:each var="site2" in="${it.samplingSites}">
                    <td>${readings.averageFor(site2, it, d)}</td>
                </g:each>
            </tr>
        </g:each>
        </tbody>
    </table>
</g:each>
</body>
</html>