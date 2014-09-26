<%@ page contentType="text/html;charset=UTF-8" %>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<script type="text/javascript">
    var skuFilter = function() {
        document.getElementById('sales-channel-filter').className = "btn btn-default";
        document.getElementById('sku-filter').className = "btn btn-primary active"
    }

    var salesChannelFilter = function() {
        document.getElementById('sku-filter').className = "btn btn-default"
        document.getElementById('sales-channel-filter').className = "btn btn-primary active"
    }
</script>
<div>
    <div class="btn-group" data-toggle="buttons" style="float: left; margin-right: 20; margin-top: 10; z-index: 200">
        <label class="btn btn-primary active" id="sku-filter" >
            <input type="radio" name="options" onclick="skuFilter()"> SKU
        </label>
        <label class="btn btn-default" id="sales-channel-filter">
            <input type="radio" name="options" onclick="salesChannelFilter()"> Sales Channel
        </label>
    </div>
</div>