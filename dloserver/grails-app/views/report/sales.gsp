<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>Sales Report</title>
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
            title: 'SALES',
            vAxis: {title: 'Money', minValue: 0},
            isStacked: true
        };

        new google.visualization.ColumnChart(document.getElementById('chart')).draw(chartData, options);
        new google.visualization.Table(document.getElementById('table')).draw(tableData, options);
    }
</script>

<div>
    <g:render template="menu"></g:render>
</div>



<div id="content">
    <div>
        <g:render template="filter"></g:render>
    </div>
    <div style="margin: 0 auto;">
        <div style="margin-left:auto;margin-right:auto;padding-bottom: 30px ">
            <g:render template="filter-for-sales"></g:render>
        </div>

        <div id="chart" style="width:800px;height:400px;margin-left:auto;margin-right:auto;"></div>

        <div align="center" style="margin-top:-40px;margin-bottom:-60px;padding-bottom:80px; margin-left:auto;margin-right:auto;">
            <g:render template="timeLine" />
        </div>

        <div id="table" style="width:800px;margin-left:auto;margin-right:auto;"></div>
    </div>
</div>
</body>
</html>