package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ReceiptsRepository;
import org.junit.Before;
import org.junit.Test;

import static com.dlohaiti.dlokiosk.domain.ProductBuilder.productBuilder;
import static com.dlohaiti.dlokiosk.domain.PromotionBuilder.promotionBuilder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ShoppingCartTest {
    private ReceiptsRepository receiptsRepository;
    private ShoppingCart cart;

    @Before
    public void setUp() {
        receiptsRepository = mock(ReceiptsRepository.class);
        cart = new ShoppingCart(receiptsRepository);
    }

    @Test
    public void shouldSaveToReceiptsRepositoryOnCheckout() {
        cart.addProduct(productBuilder().build());
        cart.checkout();
        verify(receiptsRepository, only()).add(anyListOf(Product.class), anyListOf(Promotion.class));
    }

    @Test
    public void shouldClearOnCheckout() {
        cart.addProduct(productBuilder().build());
        cart.addPromotion(promotionBuilder().build());
        cart.checkout();
        assertThat(cart.isEmpty(), is(true));
    }

    @Test
    public void shouldNotBeClearWithAProduct() {
        cart.addProduct(productBuilder().build());
        assertThat(cart.isEmpty(), is(false));
    }

    @Test
    public void shouldNotBeClearWithAPromotion() {
        cart.addPromotion(promotionBuilder().build());
        assertThat(cart.isEmpty(), is(false));
    }

    @Test
    public void shouldBeClearAfterRemovingOnlyProduct() {
        Product product = productBuilder().build();
        cart.addProduct(product);
        cart.remove(0);
        assertThat(cart.isEmpty(), is(true));
    }

    @Test
    public void shouldBeClearAfterRemovingOnlyPromotion() {
        Promotion promotion = promotionBuilder().build();
        cart.addPromotion(promotion);
        cart.removePromotion(promotion);
        assertThat(cart.isEmpty(), is(true));
    }
}
