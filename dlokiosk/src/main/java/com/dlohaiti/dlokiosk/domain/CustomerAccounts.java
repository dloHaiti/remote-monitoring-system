package com.dlohaiti.dlokiosk.domain;

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

    public ArrayList<CustomerAccount> findAccountsThatCanBeServedByChannel(long channelId) {
        ArrayList<CustomerAccount> accounts = new ArrayList<CustomerAccount>();
        for (CustomerAccount account : this) {
            if (account.canBeServedByChannel(channelId)) {
                accounts.add(account);
            }
        }
        return accounts;
    }

    public List<CustomerAccount> filterAccountsBy(String text, SalesChannel selectedSalesChannel) {
        List<CustomerAccount> newFilteredCustomerList = new ArrayList<CustomerAccount>();

        for (CustomerAccount account : this) {
            if ((containsIgnoreCase(account.name(), text)
                    || containsIgnoreCase(account.contactName(), text)) && (selectedSalesChannel == null || account.canBeServedByChannel(selectedSalesChannel.id()))) {
                newFilteredCustomerList.add(account);
            }
        }
        return newFilteredCustomerList;
    }

    public List<CustomerAccount> filterAccountsBy(String text) {
        List<CustomerAccount> newFilteredCustomerList = new ArrayList<CustomerAccount>();

        for (CustomerAccount account : this) {
            if (containsIgnoreCase(account.name(), text)
                    || containsIgnoreCase(account.contactName(), text)) {
                newFilteredCustomerList.add(account);
            }
        }
        return newFilteredCustomerList;
    }
}
