<%@ page contentType="text/html;charset=UTF-8" %>
<script language="javascript" type="text/javascript">

    window.onload = function(){
        var filter = getParameterByName('filterType');
        var filterParam = getParameterByName('filterParam')
        var timeLine = getParameterByName('timeLine')

    if (!filter)
           enableCorrespondingFilterType("kiosk");
        if (filter)
            enableCorrespondingFilterType(filter)
        if (filterParam)
            enableCorrespondingFilterParam(filterParam)
        if (timeLine)
            enableCorrespondingTimeline(timeLine)
    };

    var enableCorrespondingFilterType = function (filter) {
        var kioskOption = document.getElementById('kiosk-filter');
        var regionOption = document.getElementById('region-filter');
        if (filter === 'kiosk') {
            kioskOption.className += ' btn-primary active';
            regionOption.className += ' btn-default';
        } else {
            regionOption.className += ' btn-primary active';
            kioskOption.className += ' btn-default';
        }
    };

    var pageUrl = function() {
        var urlComponents = window.location.href.split('?');
        var url = urlComponents[0];
        return url;
    };

    var reloadWithParams = function(key, value) {
        var queryStringMap = queryStringAsMap();
        queryStringMap[key] = value;

        var destinationUrl = urlWithQueryParams(pageUrl(), queryStringMap);
        location.href = destinationUrl;
    };

    var queryStringAsMap = function() {
        var pairs = location.search.slice(1).split('&');
        var result = {};
        pairs.forEach(function(pair) {
            pair = pair.split('=');
            result[pair[0]] = decodeURIComponent(pair[1] || '');
        });
        return JSON.parse(JSON.stringify(result));
    };

    var urlWithQueryParams = function(url, map) {
        str = '';
        for(var key in map) {
            str += key + '=' + map[key] + '&';
        }
        return url + '?' + str.slice(0, str.length - 1);

    };

    var getParameterByName = function(name) {
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
                results = regex.exec(location.search);
        return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    };
</script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<div style="width: 100%">
    <div class="btn-group" data-toggle="buttons" style="float: right; margin-right: 20; margin-top: 10; z-index: 200">
        <label class="btn" id="kiosk-filter">
            <input type="radio" name="options"  onclick="reloadWithParams('filterType', 'kiosk')">
                    <g:message code="message.kiosk" />
        </label>
        <label class="btn" id="region-filter">
            <input type="radio" name="options"  onclick="reloadWithParams('filterType', 'region')">
                    <g:message code="message.region" />
        </label>
    </div>
</div>