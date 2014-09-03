package com.dlohaiti.dlokiosk.db;

import android.content.Context;
import com.dlohaiti.dlokiosk.Base64ImageConverter;
import com.dlohaiti.dlokiosk.domain.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ProductRepositoryTest {
    KioskDatabase db = new KioskDatabase(Robolectric.application.getApplicationContext());
    ProductRepository repository;

    @Before
    public void setUp() {
        Context applicationContext = Robolectric.application.getApplicationContext();
        repository = new ProductRepository(db, new Base64ImageConverter(applicationContext));
    }

    @Test
    public void shouldReturnEmptyListWhenNoProducts() {
        List<Product> products = repository.list();
        assertThat(products.size(), is(0));
    }
}
