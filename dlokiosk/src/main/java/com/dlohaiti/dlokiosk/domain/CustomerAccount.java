package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.SelectableListItem;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.upperCase;

public class CustomerAccount extends SelectableListItem implements Comparable<CustomerAccount> {
    private double dueAmount;
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private long kiosk_id;
    private List<Long> channelIds;
    private String contactName;
    private boolean synced;
    private List<SalesChannel> channels;
    private String customerType;

    public CustomerAccount(long id, String name, String contactName, String customerType, String address, String phoneNumber, long kiosk_id,int amount,boolean synced) {
        super(id, name);
        this.id = id;
        this.name = name;
        this.contactName = contactName;
        this.customerType=customerType;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.kiosk_id = kiosk_id;
        this.dueAmount=amount;
        this.synced=synced;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
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

    public String getContactName() {
        return contactName;
    }

    public CustomerAccount withChannelIds(List<Long> channelIds) {
        this.channelIds = channelIds;
        return this;
    }

    public CustomerAccount withChannels(List<SalesChannel> channels) {
        this.channels = channels;
        return this;
    }

    @Override
    public int compareTo(CustomerAccount another) {
        return upperCase(defaultString(name)).compareTo(upperCase(defaultString(another.name)));
    }

    public boolean canBeServedByChannel(long channelId) {
        if (channels == null || channels.isEmpty()) {
            return false;
        }
        for (SalesChannel salesChannel : channels) {
            if (salesChannel.id() == channelId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerAccount that = (CustomerAccount) o;

        if (id != that.id) return false;
        if (kiosk_id != that.kiosk_id) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (contactName != null ? !contactName.equals(that.contactName) : that.contactName != null) return false;
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
        result = 31 * result + (contactName != null ? contactName.hashCode() : 0);
        return result;
    }

    public double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getCustomerType() {
        return customerType;
    }
}
