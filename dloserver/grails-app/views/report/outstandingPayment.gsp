<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>
        <g:message code="message.outstandingPayment" />
    </title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>

    <script type="text/javascript">
        google.load("visualization", "1", {packages: ["table"]});
        google.setOnLoadCallback(drawChart);
        function drawChart() {
            var tableData = google.visualization.arrayToDataTable(<%= tableData as JSON %>);
            new google.visualization.Table(document.getElementById('table')).draw(tableData, {});
        }
    </script>
</head>

<body>
    <div>
        <g:render template="menu"></g:render>
    </div>
    <div id="content">
        <h2>
            <g:message code="message.outstandingPayment" />
        </h2>
        <div id="table" style="width: 80%;">
        </div>
    </div>
</body>
</html>