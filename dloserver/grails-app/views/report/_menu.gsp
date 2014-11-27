<%@ page contentType="text/html;charset=UTF-8" %>
    <style type="text/css">
    #sidemenu {
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

    #sidemenu ul {
        clear: left;
        float: left;
        width: 100%;
        list-style: none;
    }

    #sidemenu ul li {
        margin: 15px 0 10px;
        display: block;
        height: 30px;
        width: 70%;
        //font-size: 15px;
    }
    </style>

<div id="sidemenu">
    <ul>
        <li><h4> <g:message code="message.reports" /> </h4></li>
    </ul>
    <ul>
        <li>
            <g:link action="volume" params="[kioskName: kioskName, filterType: 'kiosk', filterParam: 'sku','timeLine':'currentWeek']"  class="smoothScroll">
                   <g:message code="message.volume" />
            </g:link>
        </li>
        <li>
            <g:link action="sales" params="[kioskName: kioskName, filterType: 'kiosk', filterParam: 'sku','timeLine':'currentWeek']" class="smoothScroll">
                    <g:message code="message.sales" />
            </g:link>
        </li>
        <li>
            <g:link action="readings" params="[kioskName: kioskName,'timeLine':'currentWeek']" class="smoothScroll">
                    <g:message code="message.measures" />
            </g:link>
        </li>
        <li>
            <g:link action="outstandingPayment" params="[kioskName: kioskName]" class="smoothScroll">
                    <g:message code="message.outstandingPayment" />
            </g:link>
        </li>
        <li>
            <g:link action="customer" params="[kioskName: kioskName]" class="smoothScroll">
                    <g:message code="message.customerData" />
            </g:link>
        </li>
    </ul>
    <br class="clearLeft"/>
</div>
</div>
