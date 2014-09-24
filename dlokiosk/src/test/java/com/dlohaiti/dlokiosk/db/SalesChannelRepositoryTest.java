package com.dlohaiti.dlokiosk.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class SalesChannelRepositoryTest {

    KioskDatabase db = new KioskDatabase(Robolectric.application.getApplicationContext());
    SalesChannelRepository repository;

    @Before
    public void setUp() {
        repository = new SalesChannelRepository(db);
    }

    @Test
    public void shouldReturnEmptySetWhenNoSalesChannels() {
        SortedSet<SalesChannel> list = repository.findAll();
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldReturnAllSalesChannelsInSetInAlphabeticalOrder() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        List<SalesChannel> salesChannels = asList(new SalesChannel(1L, "Name 1", "Desc 1", false), new SalesChannel(2L, "Name 2", "Desc 2", false));
        saveSalesChannels(wdb, salesChannels);

        SortedSet<SalesChannel> list = repository.findAll();

        assertThat(list.size(), is(2));
        assertThat(list, is(sortedSet(new SalesChannel(1L, "Name 1", "Desc 1", false), new SalesChannel(2L, "Name 2", "Desc 2", false))));
    }

    @Test
    public void shouldReplaceAll() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        saveSalesChannels(wdb, asList(new SalesChannel(1L, "Over the Counter", "Over the Counter", false), new SalesChannel(2L, "Door delivery", "Door delivery", false)));
        assertThat(repository.findAll(),
                is(sortedSet(new SalesChannel(1L, "Over the Counter", "Over the Counter", false), new SalesChannel(2L, "Door delivery", "Door delivery", false))));

        boolean success = repository.replaceAll(asList(new SalesChannel(2L, "Name 2", "Desc 2", false), new SalesChannel(3L, "Name 3", "Name 4", false)));

        assertThat(success, is(true));
        assertThat(repository.findAll(),
                is(sortedSet(new SalesChannel(2L, "Name 2", "Desc 2", false), new SalesChannel(3L, "Name 3", "Name 4", false))));
    }

    private void saveSalesChannels(SQLiteDatabase wdb, List<SalesChannel> salesChannels) {
        for (SalesChannel channel : salesChannels) {
            ContentValues values = new ContentValues();
            values.put(KioskDatabase.SalesChannelsTable.ID, channel.getId());
            values.put(KioskDatabase.SalesChannelsTable.NAME, channel.name());
            values.put(KioskDatabase.SalesChannelsTable.DESCRIPTION, channel.description());
            values.put(KioskDatabase.SalesChannelsTable.DELAYED_DELIVERY, channel.delayedDelivery());
            wdb.insert(KioskDatabase.SalesChannelsTable.TABLE_NAME, null, values);
        }
    }

    public static <T> SortedSet<T> sortedSet(T... rest) {
        SortedSet<T> set = new TreeSet<T>();
        Collections.addAll(set, rest);
        return set;
    }
}
