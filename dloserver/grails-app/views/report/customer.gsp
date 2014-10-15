<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<html>
<head>
</head>
<title>Customer Report</title>

    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js">
    </script>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

    <script type="text/javascript" src="https://www.google.com/jsapi">
    </script>

    <body>
        <div>
            <g:render template="menu"></g:render>
        </div>

        <div id="content">
            <h2>Customer Report</h2>

            <form role="form" method="post" action="csvCustomerReport">
                <input type="checkbox" name="customerId" value="customerId"> Customer Id </input>
                </br>
                <input type="checkbox" name="customerName" value="customerName"> Customer Name </input>
                </br>
                <input type="checkbox" name="phoneNumber" value="phoneNumber"> Customer Phone Number </input>
                </br>
                <input type="checkbox" name="address" value="address"> Customer Address </input>
                </br>
                <input type="checkbox" name="dueAmount" value="dueAmount"> Due Amount </input>
                </br>
                </br>
                <button type="submit" class="btn btn-default" value="submit"> Download Customer Data</button>
            </form>
        </div>
    </body>
</html>