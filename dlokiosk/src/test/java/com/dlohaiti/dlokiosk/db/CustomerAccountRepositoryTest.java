package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class CustomerAccountRepositoryTest {

    KioskDatabase db = new KioskDatabase(Robolectric.application.getApplicationContext());
    CustomerAccountRepository repository;

    @Before
    public void setUp() {
        repository = new CustomerAccountRepository(db);
    }

    @Test
    public void shouldReturnEmptySetWhenNoCustomerAccounts() {
        SortedSet<CustomerAccount> list = repository.findAll();
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldReturnAllCustomerAccountsInSetInAlphabeticalOrder() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        List<CustomerAccount> customerAccounts = asList(
                new CustomerAccount(1L, "Name 1", "Address 1", "Phone 1", 11),
                new CustomerAccount(2L, "Name 2", "Address 2", "Phone 2", 22));
        for (CustomerAccount account : customerAccounts) {
            ContentValues values = new ContentValues();
            values.put(KioskDatabase.CustomerAccountsTable.ID, account.id());
            values.put(KioskDatabase.CustomerAccountsTable.NAME, account.name());
            values.put(KioskDatabase.CustomerAccountsTable.ADDRESS, account.address());
            values.put(KioskDatabase.CustomerAccountsTable.PHONE_NUMBER, account.phoneNumber());
            values.put(KioskDatabase.CustomerAccountsTable.KIOSK_ID, account.kioskId());
            wdb.insert(KioskDatabase.CustomerAccountsTable.TABLE_NAME, null, values);
        }

        SortedSet<CustomerAccount> list = repository.findAll();
        assertThat(list.size(), is(2));
        assertThat(list,
                is(sortedSet(
                        new CustomerAccount(1L, "Name 1", "Address 1", "Phone 1", 11),
                        new CustomerAccount(2L, "Name 2", "Address 2", "Phone 2", 22))));
    }

    @Test
    public void shouldReplaceAll() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        for (CustomerAccount account : asList(
                new CustomerAccount(1L, "Name 1", "Address 1", "Phone 1", 11),
                new CustomerAccount(2L, "Name 2", "Address 2", "Phone 2", 22))) {
            ContentValues values = new ContentValues();
            values.put(KioskDatabase.CustomerAccountsTable.ID, account.id());
            values.put(KioskDatabase.CustomerAccountsTable.NAME, account.name());
            values.put(KioskDatabase.CustomerAccountsTable.ADDRESS, account.address());
            values.put(KioskDatabase.CustomerAccountsTable.PHONE_NUMBER, account.phoneNumber());
            values.put(KioskDatabase.CustomerAccountsTable.KIOSK_ID, account.kioskId());
            wdb.insert(KioskDatabase.CustomerAccountsTable.TABLE_NAME, null, values);
        }
        assertThat(repository.findAll(),
                is(sortedSet(new CustomerAccount(1L, "Name 1", "Address 1", "Phone 1", 11),
                        new CustomerAccount(2L, "Name 2", "Address 2", "Phone 2", 22))));

        boolean success = repository.replaceAll(asList(
                new CustomerAccount(1L, "Name 3", "Address 3", "Phone 3", 33),
                new CustomerAccount(2L, "Name 4", "Address 4", "Phone 4", 44)));

        assertThat(success, is(true));
        assertThat(repository.findAll(),
                is(sortedSet(new CustomerAccount(1L, "Name 3", "Address 3", "Phone 3", 33),
                        new CustomerAccount(2L, "Name 4", "Address 4", "Phone 4", 44))));
    }

    public static <T> SortedSet<T> sortedSet(T... rest) {
        SortedSet<T> set = new TreeSet<T>();
        Collections.addAll(set, rest);
        return set;
    }
}
