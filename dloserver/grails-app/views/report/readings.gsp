<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Readings Report</title>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
        google.load("visualization", "1", {packages: ["corechart", "table"]});
        google.setOnLoadCallback(drawChart);
        function drawChart() {
            <g:each var="parameter" in="${paramMap.keySet()}">
            var tableData = google.visualization.arrayToDataTable(<%= paramMap[parameter] as JSON %>);
            new google.visualization.Table(document.getElementById('${parameter}')).draw(tableData);
            new google.visualization.LineChart(document.getElementById('${parameter}-chart')).draw(tableData, {});
            </g:each>
        }
    </script>
</head>

<body>
<g:each in="${paramMap.keySet()}">
    <h1>${it}</h1>
    <div id="${it}-chart" style="width:800px;height:400px;margin-right: auto;margin-left: auto"></div>
    <div id="${it}" style="width: 800px;height:400px;margin-right: auto;margin-left: auto"></div>
</g:each>
</body>
</html>