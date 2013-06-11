<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>A Report!</title>
</head>

<body>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
    google.load("visualization", "1", {packages: ["corechart", "table"]});
    google.setOnLoadCallback(drawChart);
    function drawChart() {
        var chartData = google.visualization.arrayToDataTable(<%= chartData as JSON %>);
        var tableData = google.visualization.arrayToDataTable(<%= tableData as JSON %>);

        var options = {
            title: 'Sales by Day',
            vAxis: {title: 'Gourdes', minValue: 0},
            isStacked: true
        };

        new google.visualization.ColumnChart(document.getElementById('chart')).draw(chartData, options);
        new google.visualization.Table(document.getElementById('table')).draw(tableData, options);
    }
</script>

<div id="chart" style="width:800px;height:400px;margin-left:auto;margin-right:auto;"></div>
<div id="table" style="width:800px;margin-left:auto;margin-right:auto;"></div>
</body>
</html>