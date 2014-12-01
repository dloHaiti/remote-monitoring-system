<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>
        <g:message code="message.customerReport" />
    </title>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"> </script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
</head>

    <body>
        <div>
            <g:render template="menu"></g:render>
        </div>

        <div id="content">
            <h2>
                <g:message code="message.customerReport" />
            </h2>
            <div class="row">
                <div class="col-md-10 col-md-offset-1">
                    <g:form action="csvCustomerReport" method="get">
                        <g:hiddenField name="kioskName" value="${kioskName}" />
                        <div class="form-group">
                            <label for="fromDate" class="col-sm-2 control-label">
                                <g:message code="message.selectMonthYear" />
                            </label>
                            <div class="col-sm-10">
                                <g:datePicker name="fromDate" value="${new Date()}" precision="month" years="${2013..2030}" size="5"/>
                            </div>
                        </div>
                        <br><br>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="submit" class="btn btn-default">
                                    <g:message code="message.download" />
                                </button>
                            </div>
                        </div>
                    </g:form>
                </div>
            </div>
        </div>
</body>
</html>