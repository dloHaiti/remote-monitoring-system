package com.dlohaiti.dlokiosknew.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosknew.domain.DeliveryAgent;
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
public class DeliveryAgentRepositoryTest {

    KioskDatabase db = new KioskDatabase(Robolectric.application.getApplicationContext());
    DeliveryAgentRepository repository;

    @Before
    public void setUp() {
        repository = new DeliveryAgentRepository(db);
    }

    @Test
    public void shouldReturnEmptySetWhenNoDeliveryAgents() {
        SortedSet<DeliveryAgent> list = repository.findAll();
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldReturnAllAgentsInSetInAlphabeticalOrder() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        for (String name : asList("agent 1", "agent 3", "agent 2")) {
            ContentValues values = new ContentValues();
            values.put(KioskDatabase.DeliveryAgentsTable.NAME, name);
            wdb.insert(KioskDatabase.DeliveryAgentsTable.TABLE_NAME, null, values);
        }

        SortedSet<DeliveryAgent> list = repository.findAll();
        assertThat(list.size(), is(3));
        assertThat(list, is(sortedSet(new DeliveryAgent("agent 1"), new DeliveryAgent("agent 2"), new DeliveryAgent("agent 3"))));
    }

    @Test
    public void shouldReplaceAll() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        for (String name : asList("agent 1", "agent 3", "agent 2")) {
            ContentValues values = new ContentValues();
            values.put(KioskDatabase.DeliveryAgentsTable.NAME, name);
            wdb.insert(KioskDatabase.DeliveryAgentsTable.TABLE_NAME, null, values);
        }
        assertThat(repository.findAll(), is(sortedSet(new DeliveryAgent("agent 1"), new DeliveryAgent("agent 2"), new DeliveryAgent("agent 3"))));

        boolean success = repository.replaceAll(asList(new DeliveryAgent("a4"), new DeliveryAgent("a7"), new DeliveryAgent("a5"), new DeliveryAgent("a6")));

        assertThat(success, is(true));
        assertThat(repository.findAll(), is(sortedSet(new DeliveryAgent("a4"), new DeliveryAgent("a5"), new DeliveryAgent("a6"), new DeliveryAgent("a7"))));
    }

    public static <T> SortedSet<T> sortedSet(T... rest) {
        SortedSet<T> set = new TreeSet<T>();
        Collections.addAll(set, rest);
        return set;
    }
}
