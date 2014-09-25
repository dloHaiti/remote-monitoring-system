package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.exception.NoCustomerAccountWithGivenIdException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

public class CustomerAccounts extends ArrayList<CustomerAccount> {
    public CustomerAccounts(Collection<CustomerAccount> accounts) {
        super(accounts);
    }

    public CustomerAccounts() {
    }

    public void unSelectAll() {
        for (CustomerAccount account : this) {
            account.unSelect();
        }
    }

    public CustomerAccounts findAccountsThatCanBeServedByChannel(long channelId) {
        CustomerAccounts accounts = new CustomerAccounts();
        for (CustomerAccount account : this) {
            if (account.canBeServedByChannel(channelId)) {
                accounts.add(account);
            }
        }
        return accounts;
    }

    public CustomerAccounts filterAccountsBy(String text, SalesChannel selectedSalesChannel) {
        CustomerAccounts newFilteredCustomerList = new CustomerAccounts();

        for (CustomerAccount account : this) {
            if ((containsIgnoreCase(account.getName(), text)
                    || containsIgnoreCase(account.getContactName(), text))
                    && (selectedSalesChannel == null || account.canBeServedByChannel(selectedSalesChannel.getId()))) {
                newFilteredCustomerList.add(account);
            }
        }
        return newFilteredCustomerList;
    }

    public CustomerAccounts filterAccountsBy(String text) {
        CustomerAccounts newFilteredCustomerList = new CustomerAccounts();

        for (CustomerAccount account : this) {
            if (containsIgnoreCase(account.getName(), text)
                    || containsIgnoreCase(account.getContactName(), text)) {
                newFilteredCustomerList.add(account);
            }
        }
        return newFilteredCustomerList;
    }

    public CustomerAccount findById(String id) {
        for (CustomerAccount account : this) {
            if (account.getId().equalsIgnoreCase(id)) {
                return account;
            }
        }
        throw new NoCustomerAccountWithGivenIdException(id);
    }

    public List<CustomerAccount> filterAccountsByContactName(String contactName, SalesChannel selectedSalesChannel) {
        List<CustomerAccount> newFilteredCustomerList = new ArrayList<CustomerAccount>();

        for (CustomerAccount account : this) {
            if (containsIgnoreCase(account.getContactName(), contactName) && (selectedSalesChannel == null || account.canBeServedByChannel(selectedSalesChannel.getId()))) {
                newFilteredCustomerList.add(account);
            }
        }
        return newFilteredCustomerList;

    }

    public List<String> getContactNames() {
        List<String> names=new ArrayList<String>();
        for (CustomerAccount account : this) {
            names.add(account.getContactName());
        }
        return names;
    }

    public List<CustomerAccount> getAccountsFromNames(List<String> names) {
        List<CustomerAccount> accounts = new ArrayList<CustomerAccount>();
        for (String name : names) {
            CustomerAccount account = this.findAccountByContactName(name);
            if(account!=null){
                accounts.add(account);
            }
        }
        return accounts;
    }

    private CustomerAccount findAccountByContactName(String name) {
        for(CustomerAccount ca:this){
            if(ca.getContactName().equalsIgnoreCase(name)){
                return ca;
            }
        }
        return null;
    }
}
