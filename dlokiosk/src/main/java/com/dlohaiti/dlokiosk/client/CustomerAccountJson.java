package com.dlohaiti.dlokiosk.client;

import java.util.ArrayList;
import java.util.List;

public class CustomerAccountJson {
    private long id;
    private String name;
    private String address;
    private String phoneNumber;
    private long kiosk_id;
    private List<SalesChannelIdJson> channels;
    private String contactName;
    private int dueAmount;

    public int getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(int dueAmount) {
        this.dueAmount = dueAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getKiosk_id() {
        return kiosk_id;
    }

    public void setKiosk_id(long kiosk_id) {
        this.kiosk_id = kiosk_id;
    }

    public List<SalesChannelIdJson> getChannels() {
        return channels;
    }

    public void setChannels(List<SalesChannelIdJson> channels) {
        this.channels = channels;
    }

    public List<Long> channelIds() {
        ArrayList<Long> ids = new ArrayList<Long>();
        for (SalesChannelIdJson channel : channels) {
            ids.add(channel.getId());
        }
        return ids;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
