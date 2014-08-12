package com.dlohaiti.dlokiosk.domain;

public class CustomerAccount {
    private long id;
    private String name;
    private String address;
    private String phoneNumber;
    private long kiosk_id;

    public CustomerAccount(long id, String name, String address, String phoneNumber, long kiosk_id) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.kiosk_id = kiosk_id;
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
}
