package com.dlohaiti.dlokiosknew.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.dlohaiti.dlokiosknew.db.KioskDatabase.SponsorsTable;
import com.dlohaiti.dlokiosknew.domain.Sponsor;
import com.dlohaiti.dlokiosknew.domain.Sponsors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class SponsorRepositoryTest {

    KioskDatabase db = new KioskDatabase(Robolectric.application.getApplicationContext());
    SponsorRepository repository;

    @Before
    public void setUp() {
        repository = new SponsorRepository(db);
    }

    @Test
    public void shouldReturnEmptySetWhenNoSponsors() {
        Sponsors list = repository.findAll();
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldReturnAllSponsorsInSetInAlphabeticalOrder() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        List<Sponsor> sponsors = asList(new Sponsor("1", "Name 1", "Contact Name 1", "Desc 1",true),
                new Sponsor("2", "Name 2", "Contact Name 2", "Desc 2",true));
        saveSponsors(wdb, sponsors);

        Sponsors list = repository.findAll();

        assertThat(list.size(), is(2));
        assertThat(list, is(asList(new Sponsor("1", "Name 1", "Contact Name 1", "Desc 1",true),
                new Sponsor("2", "Name 2", "Contact Name 2", "Desc 2",true))));
    }

    @Test
    public void shouldReplaceAll() {
        SQLiteDatabase wdb = db.getWritableDatabase();
        saveSponsors(wdb, asList(new Sponsor("1", "Name 1", "Contact Name 1", "Desc 1",true),
                new Sponsor("2", "Name 2", "Contact Name 2", "Desc 2",true)));
        assertThat(repository.findAll(),
                is(asList(new Sponsor("1", "Name 1", "Contact Name 1", "Desc 1",true),
                        new Sponsor("2", "Name 2", "Contact Name 2", "Desc 2",true))));

        boolean success = repository.replaceAll(asList(new Sponsor("3", "Name 3", "Contact Name 3", "Desc 3",true),
                new Sponsor("4", "Name 4", "Contact Name 4", "Desc 3",true)));

        assertThat(success, is(true));
        assertThat(repository.findAll(),
                is(asList(new Sponsor("3", "Name 3", "Contact Name 3", "Desc 3",true),
                        new Sponsor("4", "Name 4", "Contact Name 4", "Desc 3",true))));
    }

    private void saveSponsors(SQLiteDatabase wdb, List<Sponsor> sponsors) {
        for (Sponsor sponsor : sponsors) {
            ContentValues values = new ContentValues();
            values.put(SponsorsTable.ID, sponsor.getId());
            values.put(SponsorsTable.NAME, sponsor.getName());
            values.put(SponsorsTable.CONTACT_NAME, sponsor.getContactName());
            values.put(SponsorsTable.PHONE_NUMBER, sponsor.getPhoneNumber());
            wdb.insert(SponsorsTable.TABLE_NAME, null, values);
        }
    }
}
