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
            if ((containsIgnoreCase(account.name(), text)
                    || containsIgnoreCase(account.contactName(), text))
                    && (selectedSalesChannel == null || account.canBeServedByChannel(selectedSalesChannel.id()))) {
                newFilteredCustomerList.add(account);
            }
        }
        return newFilteredCustomerList;
    }

    public CustomerAccounts filterAccountsBy(String text) {
        CustomerAccounts newFilteredCustomerList = new CustomerAccounts();

        for (CustomerAccount account : this) {
            if (containsIgnoreCase(account.name(), text)
                    || containsIgnoreCase(account.contactName(), text)) {
                newFilteredCustomerList.add(account);
            }
        }
        return newFilteredCustomerList;
    }

    public CustomerAccount findById(long id) {
        for (CustomerAccount account : this) {
            if (account.id() == id) {
                return account;
            }
        }
        throw new NoCustomerAccountWithGivenIdException(id);
    }

    public List<CustomerAccount> filterAccountsByContactName(String contactName, SalesChannel selectedSalesChannel) {
        List<CustomerAccount> newFilteredCustomerList = new ArrayList<CustomerAccount>();

        for (CustomerAccount account : this) {
            if (containsIgnoreCase(account.contactName(), contactName) && (selectedSalesChannel == null || account.canBeServedByChannel(selectedSalesChannel.id()))) {
                newFilteredCustomerList.add(account);
            }
        }
        return newFilteredCustomerList;

    }
}
