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
    public void shouldReturnAllAgentsInSetInAlphabeticalOrder() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        for (SalesChannel channel : asList(new SalesChannel(1L, "Name 1", "Desc 1"), new SalesChannel(2L, "Name 2", "Desc 2"))) {
            ContentValues values = new ContentValues();
            values.put(KioskDatabase.SalesChannelTable.ID, channel.id());
            values.put(KioskDatabase.SalesChannelTable.NAME, channel.name());
            values.put(KioskDatabase.SalesChannelTable.DESCRIPTION, channel.description());
            wdb.insert(KioskDatabase.SalesChannelTable.TABLE_NAME, null, values);
        }

        SortedSet<SalesChannel> list = repository.findAll();
        assertThat(list.size(), is(2));
        assertThat(list, is(sortedSet(new SalesChannel(1L, "Name 1", "Desc 1"), new SalesChannel(2L, "Name 2", "Desc 2"))));
    }

    @Test
    public void shouldReplaceAll() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        for (SalesChannel channel : asList(new SalesChannel(1L, "Over the Counter", "Over the Counter"), new SalesChannel(2L, "Door delivery", "Door delivery"))) {
            ContentValues values = new ContentValues();
            values.put(KioskDatabase.SalesChannelTable.ID, channel.id());
            values.put(KioskDatabase.SalesChannelTable.NAME, channel.name());
            values.put(KioskDatabase.SalesChannelTable.DESCRIPTION, channel.description());
            wdb.insert(KioskDatabase.SalesChannelTable.TABLE_NAME, null, values);
        }
        assertThat(repository.findAll(),
                is(sortedSet(new SalesChannel(1L, "Over the Counter", "Over the Counter"), new SalesChannel(2L, "Door delivery", "Door delivery"))));

        boolean success = repository.replaceAll(asList(new SalesChannel(2L, "Name 2", "Desc 2"), new SalesChannel(3L, "Name 3", "Name 4")));

        assertThat(success, is(true));
        assertThat(repository.findAll(),
                is(sortedSet(new SalesChannel(2L, "Name 2", "Desc 2"), new SalesChannel(3L, "Name 3", "Name 4"))));
    }

    public static <T> SortedSet<T> sortedSet(T... rest) {
        SortedSet<T> set = new TreeSet<T>();
        Collections.addAll(set, rest);
        return set;
    }
}
