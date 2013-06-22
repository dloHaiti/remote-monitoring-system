package com.dlohaiti.dlokiosk.db;

import com.dlohaiti.dlokiosk.DeliveryType;
import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.domain.Delivery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;
import java.util.List;

import static com.dlohaiti.dlokiosk.DeliveryType.OUT_FOR_DELIVERY;
import static com.dlohaiti.dlokiosk.DeliveryType.RETURNED;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class DeliveryRepositoryTest {
    KioskDatabase db = new KioskDatabase(Robolectric.application.getApplicationContext());
    DeliveryRepository repository;

    @Before
    public void setUp() {
        repository = new DeliveryRepository(db, new KioskDate());
    }

    @Test
    public void shouldReturnEmptyListWhenNoDeliveries() {
        List<Delivery> deliveries = repository.list();
        assertThat(deliveries.size(), is(0));
    }

    @Test
    public void shouldReturnAllSavedDeliveries() {
        assertThat(repository.list().isEmpty(), is(true));
        Delivery one = new Delivery(1, 10, OUT_FOR_DELIVERY, new Date(0), "agent 1");
        Delivery two = new Delivery(2, 20, OUT_FOR_DELIVERY, new Date(1000), "agent 1");
        Delivery three = new Delivery(3, 5, OUT_FOR_DELIVERY, new Date(2000), "agent 1");
        Delivery four = new Delivery(4, 1, DeliveryType.RETURNED, new Date(3000), "agent 1");
        boolean saveSuccessful = repository.save(one) && repository.save(two) && repository.save(three) && repository.save(four);
        assertThat(saveSuccessful, is(true));

        List<Delivery> deliveries = repository.list();

        assertThat(deliveries, is(asList(one, two, three, four)));
    }

    @Test
    public void shouldRemoveExistingDeliveryFromDatabase() {
        Delivery one = new Delivery(1, 10, OUT_FOR_DELIVERY, new Date(0), "agent 1");
        repository.save(one);
        assertThat(repository.list().size(), is(1));

        boolean successfulRemove = repository.remove(one);

        assertThat(successfulRemove, is(true));
        assertThat(repository.list().size(), is(0));
    }

    @Test
    public void shouldNotFailWhenRemovingDeliveryThatDoesNotExist() {
        repository.save(new Delivery(1, 10, OUT_FOR_DELIVERY, new Date(0), "agent 1"));
        assertThat(repository.list().size(), is(1));

        boolean successfulRemove = repository.remove(new Delivery(100, 100, RETURNED, new Date(9000), "agent 6"));

        assertThat(successfulRemove, is(true));
        assertThat(repository.list().size(), is(1));
    }

    @Test
    public void shouldKnowIfTableHasEntries() {
        assertThat(repository.list().size(), is(0));
        assertThat(repository.isNotEmpty(), is(false));

        repository.save(new Delivery(1, 10, OUT_FOR_DELIVERY, new Date(0), "agent 1"));

        assertThat(repository.list().size(), is(1));
        assertThat(repository.isNotEmpty(), is(true));
    }
}
