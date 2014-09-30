package com.dlohaiti.dlokiosk.domain;

import java.util.List;

public class CustomerAccountBuilder {
    private String id = "1";
    private String name = "Name 1";
    private String contactName = "Contact Name 1";
    private String address = "Address 1";
    private String phoneNumber = "Phone 1";
    private long kioskId = 1;
    private List<SalesChannel> salesChannels;
    private boolean selected = false;
    private double dueAmount = 0;

    public CustomerAccountBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public CustomerAccountBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CustomerAccountBuilder withContactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public CustomerAccountBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public CustomerAccountBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public CustomerAccountBuilder withKioskId(long kioskId) {
        this.kioskId = kioskId;
        return this;
    }

    public CustomerAccountBuilder withChannels(List<SalesChannel> salesChannels) {
        this.salesChannels = salesChannels;
        return this;
    }

    public CustomerAccountBuilder select() {
        this.selected = true;
        return this;
    }

    public CustomerAccount build() {
        CustomerAccount account = new CustomerAccount(id, name, contactName, "School", address, phoneNumber, kioskId,
                dueAmount, true)
                .withChannels(salesChannels);
        if (selected) {
            account.select();
        }
        return account;
    }

    public CustomerAccountBuilder withDueAmount(double dueAmount) {
        this.dueAmount = dueAmount;
        return this;
    }
}
