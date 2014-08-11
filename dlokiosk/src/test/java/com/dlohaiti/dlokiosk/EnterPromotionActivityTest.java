package com.dlohaiti.dlokiosk;

import com.dlohaiti.dlokiosk.domain.ShoppingCart;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class EnterPromotionActivityTest {

    private EnterPromotionActivity activity;
    private ShoppingCart mockCart;

    @Before
    public void setUp() {
        activity = new EnterPromotionActivity();
        mockCart = mock(ShoppingCart.class);
    }

    @Test
    public void shouldClearPromotionsOnBackPressed() {
        activity.shoppingCart = mockCart;

        activity.onBackPressed();

        verify(mockCart, times(1)).clearPromotions();
    }
}
