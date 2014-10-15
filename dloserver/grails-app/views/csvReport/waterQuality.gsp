<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Water Quality Report</title>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>

    <link type="text/css" href="${resource(dir: 'css', file: 'datepicker.css')}" />
    <script type="text/javascript" src="${resource(dir: 'js', file: 'bootstrap-datepicker.js')}"></script>
</head>

<body>
<div>
    <g:render template="menu"></g:render>
</div>
<div id="content">
    <h2>Water Quality Report</h2>

    <div class="row">
    <div class="col-md-10 col-md-offset-1">
    <form class="form-horizontal" role="form" method="post" action="csvWaterQuality">
        <g:hiddenField name="kioskName" value="${kioskName}" />
        <div class="form-group">
            <label for="fromDate" class="col-sm-2 control-label">Start Date</label>
            <div class="col-sm-10">
                <input class="form-control" id="fromDate" name="fromDate">
            </div>
        </div>
        <div class="form-group">
            <label for="toDate" class="col-sm-2 control-label">End Date</label>
            <div class="col-sm-10">
                <input class="form-control" id="toDate" name="toDate">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Download</button>
            </div>
        </div>
    </form>
    </div>
    </div>
</div>
<script type="text/javascript">
    $('#fromDate').datepicker({
            format: "yyyy-mm-dd",
        todayBtn: true,
        autoclose: true,
        todayHighlight: true
    })
    $('#toDate').datepicker({
         format: "yyyy-mm-dd",
        todayBtn: true,
        autoclose: true,
        todayHighlight: true
    })
</script>
</body>
</html>