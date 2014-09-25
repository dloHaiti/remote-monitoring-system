<%@ page contentType="text/html;charset=UTF-8" %>
<script language="javascript" type="text/javascript">

    window.onload = function(){
        var filter = getParameterByName('filterType');
        var kioskOption = document.getElementById('kiosk-filter');
        var regionOption = document.getElementById('region-filter');
        if (filter === 'kiosk') {
            kioskOption.className += ' active'
        } else {
            regionOption.className += ' active'
        }
    };

    function baseUrl() {
        var urlComponents = window.location.href.split('?');
        var url = urlComponents[0];
        var parameters = {};
        var queryStringPairs = urlComponents[1].split('&');
        for (var i = 0; i < queryStringPairs.length; i++) {
            var array = queryStringPairs[i].split('=');
            parameters[array[0]] = array[1]
        }
        var destinationUrl = url + '?kioskName=' + parameters['kioskName'];
        return destinationUrl;
    }

    var kioskFilter = function () {
        var destinationUrl = baseUrl() + '&filterType=kiosk';
        location.href = destinationUrl
    }

    var regionFilter = function () {
        destinationUrl = baseUrl() + '&filterType=region';
        location.href = destinationUrl
    }

    var getParameterByName = function(name) {
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
                results = regex.exec(location.search);
        return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }
</script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<div style="width: 100%">
    <div class="btn-group" data-toggle="buttons" style="float: right; margin-right: 20; margin-top: 10; z-index: 200">
        <label class="btn btn-primary" id="kiosk-filter">
            <input type="radio" name="options"  onclick="kioskFilter()"> Kiosk
        </label>
        <label class="btn btn-primary" id="region-filter">
            <input type="radio" name="options"  onclick="regionFilter()"> Region
        </label>
    </div>
</div>