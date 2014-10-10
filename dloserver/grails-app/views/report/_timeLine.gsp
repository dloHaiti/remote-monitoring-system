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
</script>
<div>
<div class="btn-group" data-toggle="buttons" style=" margin-right: 20; margin-top: 10; z-index: 200">

    <label class="btn btn-default" id="lastWeek-filter">
        <input type="radio" name="options" onclick="reloadWithParams('timeLine','lastWeek')"> Last Week
    </label>
    <label class="btn btn-primary active" id="currentWeek-filter" >
        <input type="radio" name="options" onclick="reloadWithParams('timeLine','currentWeek')"> Current Week
    </label>
</div>
</div>