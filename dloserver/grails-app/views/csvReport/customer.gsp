<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Customer Report</title>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"> </script>
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
            <h2>Customer Report</h2>

            <g:form action="csvCustomerReport" >
                <g:hiddenField name="kioskName" value="${kioskName}" />
                    <div class="form-group">
                        <label for="fromDate" class="col-sm-2 control-label">Start Date</label>
                        <div class="col-sm-10">
                            <input type="date" class="form-control" id="fromDate" name="fromDate">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="toDate" class="col-sm-2 control-label">End Date</label>
                        <div class="col-sm-10">
                            <input type="date" class="form-control" id="toDate" name="toDate">
                        </div>
                    </div>
                <g:submitButton class="btn btn-default" name="submit" > Download Customer Data</g:submitButton>
            </g:form>
        </div>
    </body>
</html>