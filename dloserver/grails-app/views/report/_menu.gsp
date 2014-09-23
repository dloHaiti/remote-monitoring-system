<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>DLO Haiti: Available Reports</title>
    <style type="text/css">
    #menu {
        position: fixed;
        top: 0;
        left: 0;
        float: left;
        width: 18%;
        min-height: 100%;
        background: silver;
    }

    #content {
        float: right;
        top: 0;
        right: 0;
        margin-bottom: 20px;
        width: 80%;
        min-height: 100%;
    }

    #menu ul {
        clear: left;
        float: left;
        width: 100%;
        list-style: none;
    }

    #menu ul li {
        margin: 15px 0 10px;
        display: block;
        height: 30px;
        width: 70%;
    }
    </style>
</head>

<body>
<div id="menu">
    <div id="toggle">
    </div>
    <ul>
        <li><g:link action="volumeByDay" params="[kioskName: kioskName]"
                    class="smoothScroll">Volume par jour</g:link></li>
        <li><g:link action="salesByDay" params="[kioskName: kioskName]"
                    class="smoothScroll">Ventes par jour</g:link></li>
        <li><g:link action="readings" params="[kioskName: kioskName]" class="smoothScroll">Mesures eau</g:link></li>
    </ul>
    <br class="clearLeft"/>
</div>
</div>
</body>
</html>