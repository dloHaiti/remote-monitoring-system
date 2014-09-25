package com.dlohaiti.dlokiosk.domain;

public class ProductMrp {
    private long productId;
    private long channelId;
    private Money price;

    public ProductMrp(long productId, long channelId, Money price) {
        this.productId = productId;
        this.channelId = channelId;
        this.price = price;
    }

    public long productId() {
        return productId;
    }

    public long channelId() {
        return channelId;
    }

    public Money price() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductMrp that = (ProductMrp) o;

        if (channelId != that.channelId) return false;
        if (productId != that.productId) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (productId ^ (productId >>> 32));
        result = 31 * result + (int) (channelId ^ (channelId >>> 32));
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
