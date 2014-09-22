package com.dlohaiti.dlokiosk.domain;

import android.text.TextUtils;

import com.dlohaiti.dlokiosk.SelectableListItem;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
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
    private String customerTypeId;
    private List<Long> sponsorIds;
    private Sponsors sponsors;
    private String gpsCoordinates;


    public CustomerAccount(long id, String name, String contactName, String customerTypeId, String address, String phoneNumber, long kiosk_id, int amount, boolean synced) {
        super(id, name);
        this.id = id;
        this.name = name;
        this.contactName = contactName;
        this.customerTypeId = customerTypeId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.kiosk_id = kiosk_id;
        this.dueAmount = amount;
        this.synced = synced;
    }

    public String getGpsCoordinates() {
        return gpsCoordinates;
    }

    public String formatedGPS() {
        if (TextUtils.isEmpty(gpsCoordinates))
            return "";
        String[] split = gpsCoordinates.split(",");
        String[] latitude = split[0].split(":");
        String[] longitude = split[1].split(":");
        return format("%s° %s′ %s″,%s° %s′ %s″", latitude[0], latitude[1], latitude[2], longitude[0], longitude[1], longitude[2]);
    }

    public CustomerAccount setGpsCoordinates(String gpsCoordinates) {
        this.gpsCoordinates = gpsCoordinates;
        return this;
    }

    public List<Long> getSponsorIds() {
        try {
            if (sponsorIds != null) {
                return sponsorIds;
            } else {
                List<Long> ids = new ArrayList<Long>();
                for (Sponsor s : sponsors) {
                    ids.add(s.id());
                }
                return ids;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setCustomerTypeId(String customerTypeId) {
        this.customerTypeId = customerTypeId;
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

    public List<SalesChannel> getChannels() {
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
            if (salesChannel.getId() == channelId) {
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
        if (contactName != null ? !contactName.equals(that.contactName) : that.contactName != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null)
            return false;

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

    public String getCustomerTypeId() {
        return customerTypeId;
    }

    public CustomerAccount withSponsorIds(List<Long> sponsorIds) {
        this.sponsorIds = sponsorIds;
        return this;
    }

    public void withSponsors(Sponsors sponsors) {
        this.sponsors = sponsors;
    }

    public Sponsors sponsors() {
        return sponsors;
    }
}
