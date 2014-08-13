package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.widgets.SelectableListItem;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.upperCase;

public class CustomerAccount extends SelectableListItem implements Comparable<CustomerAccount> {
    private long id;
    private String name;
    private String address;
    private String phoneNumber;
    private long kiosk_id;
    private List<Long> channelIds;

    private List<SalesChannel> channels;

    public CustomerAccount(long id, String name, String address, String phoneNumber, long kiosk_id) {
        super(id, name);
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.kiosk_id = kiosk_id;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String address() {
        return address;
    }

    public String phoneNumber() {
        return phoneNumber;
    }

    public long kioskId() {
        return kiosk_id;
    }

    public List<Long> channelIds() {
        return channelIds;
    }

    public List<SalesChannel> channels() {
        return channels;
    }

    public CustomerAccount withChannelIds(List<Long> channelIds) {
        this.channelIds = channelIds;
        return this;
    }

    public CustomerAccount withChannels(ArrayList<SalesChannel> channels) {
        this.channels = channels;
        return this;
    }

    @Override
    public int compareTo(CustomerAccount another) {
        return upperCase(defaultString(name)).compareTo(upperCase(defaultString(another.name)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerAccount that = (CustomerAccount) o;

        if (id != that.id) return false;
        if (kiosk_id != that.kiosk_id) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (int) (kiosk_id ^ (kiosk_id >>> 32));
        return result;
    }

    public boolean canBeServedByChannel(String channel) {
        for (SalesChannel salesChannel : channels) {
            if (salesChannel.name().equalsIgnoreCase(channel)) {
                return true;
            }
        }
        return false;
    }
}
