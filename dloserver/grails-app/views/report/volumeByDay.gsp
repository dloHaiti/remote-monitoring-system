<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>A Report!</title>
</head>

<body>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
    google.load("visualization", "1", {packages: ["corechart"]});
    google.setOnLoadCallback(drawChart);
    function drawChart() {
        var data = google.visualization.arrayToDataTable(<%= rows as JSON %>);

        var options = {
            title: 'Volume by Day',
            vAxis: {title: 'Gallons', minValue: 0},
            isStacked: true
        };

        var chart = new google.visualization.ColumnChart(document.getElementById('chart'));
        chart.draw(data, options);
    }
</script>

<div id="chart" style="width:900px;height:500px"></div>

<table>
    <thead>
    <tr>
        <g:each in="${days}">
            <th><g:formatDate format="MM/dd/yy" date="${it}"/></th>
        </g:each>
    </tr>
    </thead>
    <tbody>
    <g:each var="row" in="${rows}">
        <tr>
            <g:each in="${0..(row.size() - 1)}">
                <td>${row[it]}</td>
            </g:each>
        </tr>
    </g:each>
    </tbody>
</table>
</body>
</html>