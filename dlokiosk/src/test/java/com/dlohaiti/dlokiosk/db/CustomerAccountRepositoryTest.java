package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.dlohaiti.dlokiosk.domain.SalesChannel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.dlohaiti.dlokiosk.db.KioskDatabase.CustomerAccountsTable;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class CustomerAccountRepositoryTest {

    KioskDatabase db = new KioskDatabase(Robolectric.application.getApplicationContext());
    CustomerAccountRepository repository;
    SalesChannelRepository salesChannelRepository;
    private SponsorRepository sponsorRepository;

    @Before
    public void setUp() {
        salesChannelRepository = new SalesChannelRepository(db);
         sponsorRepository = new SponsorRepository(db);
        repository = new CustomerAccountRepository(db, salesChannelRepository,sponsorRepository);
    }

    @Test
    public void shouldReturnEmptySetWhenNoCustomerAccounts() {
        SortedSet<CustomerAccount> list = repository.findAll();
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldReturnAllCustomerAccountsInSetInAlphabeticalOrder() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        saveSalesChannel();
        List<CustomerAccount> customerAccounts = asList(
                new CustomerAccount("1", "Name 1", "Contact Name 1", "School", "Address 1", "Phone 1", (long) 11, 0, true).withChannelIds(asList(1L)),
                new CustomerAccount("2", "Name 2", "Contact Name 2", "School", "Address 2", "Phone 2", (long) 22, 1, true).withChannelIds(asList(2L)));
        saveCustomerAccounts(wdb, customerAccounts);

        SortedSet<CustomerAccount> list = repository.findAll();
        assertThat(list.size(), is(2));
        assertThat(list,
                is(sortedSet(
                        new CustomerAccount("1", "Name 1", "Contact Name 1", "School", "Address 1", "Phone 1", (long) 11, 1, true).withChannelIds(asList(1L)),
                        new CustomerAccount("1", "Name 2", "Contact Name 2", "School", "Address 2", "Phone 2", (long) 22, 0, true).withChannelIds(asList(2L)))));
        assertThat(asList(new SalesChannel(1L, "Name 1", "Desc 1", false)), is(list.first().getChannels()));
        assertThat(asList(new SalesChannel(2L, "Name 2", "Desc 2", false)), is(list.last().getChannels()));
    }

    @Test
    public void shouldReplaceAll() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        saveSalesChannel();
        saveCustomerAccounts(wdb, asList(
                new CustomerAccount("1", "Name 1", "Contact Name 1", "School", "Address 1", "Phone 1", (long) 11, 1, true).withChannelIds(asList(1L)),
                new CustomerAccount("2", "Name 2", "Contact Name 2", "School", "Address 2", "Phone 2", (long) 22, 0, true).withChannelIds(asList(2L))));

        SortedSet<CustomerAccount> initialList = repository.findAll();
        assertThat(initialList,
                is(sortedSet(new CustomerAccount("1", "Name 1", "Contact Name 1", "School", "Address 1", "Phone 1", (long) 11, 0, true).withChannelIds(asList(1L)),
                        new CustomerAccount("2", "Name 2", "Contact Name 2", "School", "Address 2", "Phone 2", (long) 22, 0, true).withChannelIds(asList(2L)))));

        assertThat(asList(new SalesChannel(1L, "Name 1", "Desc 1", false)), is(initialList.first().getChannels()));
        assertThat(asList(new SalesChannel(2L, "Name 2", "Desc 2", false)), is(initialList.last().getChannels()));

        boolean success = repository.replaceAll(asList(
                new CustomerAccount("1", "Name 3", "Contact Name 3", "School", "Address 3", "Phone 3", (long) 33, 0, true).withChannelIds(asList(3L)).withSponsorIds(asList(1L)),
                new CustomerAccount("2", "Name 4", "Contact Name 4", "School", "Address 4", "Phone 4", (long) 44, 0, true).withChannelIds(asList(4L)).withSponsorIds(asList(1L))));

        assertThat(success, is(true));
        SortedSet<CustomerAccount> updatedList = repository.findAll();
        assertThat(updatedList,
                is(sortedSet(new CustomerAccount("1", "Name 3", "Contact Name 3", "School", "Address 3", "Phone 3", (long) 33, 0, true).withChannelIds(asList(3L)),
                        new CustomerAccount("2", "Name 4", "Contact Name 4", "School", "Address 4", "Phone 4", (long) 44, 0, true).withChannelIds(asList(4L)))));
        assertThat(asList(new SalesChannel(3L, "Name 3", "Desc 3", false)), is(updatedList.first().getChannels()));
        assertThat(asList(new SalesChannel(4L, "Name 4", "Desc 4", false)), is(updatedList.last().getChannels()));
    }

    private void saveCustomerAccounts(SQLiteDatabase wdb, List<CustomerAccount> customerAccounts) {
        for (CustomerAccount account : customerAccounts) {
            ContentValues values = new ContentValues();
            values.put(CustomerAccountsTable.ID, account.getId());
            values.put(CustomerAccountsTable.NAME, account.getName());
            values.put(CustomerAccountsTable.CONTACT_NAME, account.getContactName());
            values.put(CustomerAccountsTable.ADDRESS, account.getAddress());
            values.put(CustomerAccountsTable.PHONE_NUMBER, account.getPhoneNumber());
            values.put(CustomerAccountsTable.KIOSK_ID, account.kioskId());
            wdb.insert(CustomerAccountsTable.TABLE_NAME, null, values);

            ContentValues mapTableValues = new ContentValues();
            mapTableValues.put(KioskDatabase.SalesChannelCustomerAccountsTable.CUSTOMER_ACCOUNT_ID, account.getId());
            mapTableValues.put(KioskDatabase.SalesChannelCustomerAccountsTable.SALES_CHANNEL_ID, account.channelIds().get(0));
            wdb.insert(KioskDatabase.SalesChannelCustomerAccountsTable.TABLE_NAME, null, mapTableValues);
        }
    }

    private void saveSalesChannel() {
        salesChannelRepository.replaceAll(
                asList(
                        new SalesChannel(1L, "Name 1", "Desc 1", false),
                        new SalesChannel(2L, "Name 2", "Desc 2", false),
                        new SalesChannel(3L, "Name 3", "Desc 3", false),
                        new SalesChannel(4L, "Name 4", "Desc 4", false)));
    }

    public static <T> SortedSet<T> sortedSet(T... rest) {
        SortedSet<T> set = new TreeSet<T>();
        Collections.addAll(set, rest);
        return set;
    }
}
