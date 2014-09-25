package com.dlohaiti.dlokiosk.client;

public class ProductMrpJson {
    private long kiosk_id;
    private long product_id;
    private long channel_id;
    private PriceJson price;

    public long getKiosk_id() {
        return kiosk_id;
    }

    public void setKiosk_id(long kiosk_id) {
        this.kiosk_id = kiosk_id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public long getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(long channel_id) {
        this.channel_id = channel_id;
    }

    public PriceJson getPrice() {
        return price;
    }

    public void setPrice(PriceJson price) {
        this.price = price;
    }
}
