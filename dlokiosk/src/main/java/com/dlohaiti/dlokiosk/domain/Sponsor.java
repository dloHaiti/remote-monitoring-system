package com.dlohaiti.dlokiosk.domain;


import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Sponsor implements Comparable<Sponsor> {
    private long id;
    private String name;
    private String contactName;
    private String phoneNumber;
    private List<CustomerAccount> customerAccounts;

    public Sponsor(long id, String name, String contactName, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String contactName() {
        return contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sponsor sponsor = (Sponsor) o;

        if (id != sponsor.id) return false;
        if (contactName != null ? !contactName.equals(sponsor.contactName) : sponsor.contactName != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(sponsor.phoneNumber) : sponsor.phoneNumber != null) return false;
        if (name != null ? !name.equals(sponsor.name) : sponsor.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (contactName != null ? contactName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return StringUtils.capitalize(name);
    }

    @Override
    public int compareTo(Sponsor anotherSponsor) {
        return name.compareToIgnoreCase(anotherSponsor.name);
    }

    public void withAccounts(List<CustomerAccount> customerAccounts) {
        this.customerAccounts=customerAccounts;
    }

    public List<CustomerAccount> getCustomerAccounts() {
        return customerAccounts;
    }
}
