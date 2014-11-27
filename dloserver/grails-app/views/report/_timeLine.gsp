<%@ page contentType="text/html;charset=UTF-8" %>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<script type="text/javascript">

var enableCurrentWeekFilter = function() {
    document.getElementById('lastWeek-filter').className = "btn btn-default";
    document.getElementById('currentWeek-filter').className = "btn btn-primary active"
};

var enableLastWeekFilter = function() {
    document.getElementById('currentWeek-filter').className = "btn btn-default"
    document.getElementById('lastWeek-filter').className = "btn btn-primary active"
};

var enableCorrespondingTimeline= function (filterParam) {
    if (filterParam === 'lastWeek')
        enableLastWeekFilter();
    else
        enableCurrentWeekFilter();
};


var pageUrl = function() {
    var urlComponents = window.location.href.split('?');
    var url = urlComponents[0];
    return url;
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

var reloadWithParams = function(key, value) {
    var queryStringMap = queryStringAsMap();
    queryStringMap[key] = value;

    var destinationUrl = urlWithQueryParams(pageUrl(), queryStringMap);
    location.href = destinationUrl;
};

</script>

<div>
<div class="btn-group" data-toggle="buttons" style=" margin-right: 20; margin-top: 10; z-index: 200">
    <label class="btn btn-default" id="lastWeek-filter">
        <input type="radio" name="options" onclick="reloadWithParams('timeLine','lastWeek')">
            <g:message code="message.lastWeek" />
    </label>
    <label class="btn btn-primary active" id="currentWeek-filter" >
        <input type="radio" name="options" onclick="reloadWithParams('timeLine','currentWeek')">
            <g:message code="message.currentWeek" />
    </label>
</div>
</div>