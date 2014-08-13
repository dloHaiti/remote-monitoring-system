package com.dlohaiti.dlokiosk.domain;

public class CustomerAccountSalesChannelMap {
    private long customerAccountId;
    private long salesChannelId;

    public CustomerAccountSalesChannelMap(long customerAccountId, long salesChannelId) {
        this.customerAccountId = customerAccountId;
        this.salesChannelId = salesChannelId;
    }

    public long customerAccountId() {
        return customerAccountId;
    }

    public long salesChannelId() {
        return salesChannelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerAccountSalesChannelMap that = (CustomerAccountSalesChannelMap) o;

        if (customerAccountId != that.customerAccountId) return false;
        if (salesChannelId != that.salesChannelId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
