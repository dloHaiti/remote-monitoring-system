package com.dlohaiti.dlokiosk.domain;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Sponsor implements Comparable<Sponsor> {
    private String id;
    private String name;
    private String contactName;
    private String phoneNumber;
    private List<CustomerAccount> customerAccounts;
    private boolean synced;

    public Sponsor(String id, String name, String contactName, String phoneNumber,boolean synced) {
        this.id = id;
        this.name = name;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.synced = synced;
    }

    public Sponsor() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContactName() {
        return contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sponsor sponsor = (Sponsor) o;

        if (!id.equalsIgnoreCase(sponsor.id)) return false;
        if (contactName != null ? !contactName.equals(sponsor.contactName) : sponsor.contactName != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(sponsor.phoneNumber) : sponsor.phoneNumber != null) return false;
        if (name != null ? !name.equals(sponsor.name) : sponsor.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (id != null ? id.hashCode() : 0);
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
    public List<String> getCustomerAccountIds(){
        List<String> ids=new ArrayList<String>();
        for(CustomerAccount account: customerAccounts){
            ids.add(account.getId());
        }
        return ids;
    }
    public List<CustomerAccount> customerAccounts() {
        return customerAccounts;
    }
}
