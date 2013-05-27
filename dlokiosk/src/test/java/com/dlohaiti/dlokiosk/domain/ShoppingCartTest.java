package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ReceiptsRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.dlohaiti.dlokiosk.domain.ProductBuilder.productBuilder;
import static com.dlohaiti.dlokiosk.domain.PromotionApplicationType.BASKET;
import static com.dlohaiti.dlokiosk.domain.PromotionApplicationType.SKU;
import static com.dlohaiti.dlokiosk.domain.PromotionBuilder.promotionBuilder;
import static com.dlohaiti.dlokiosk.domain.PromotionType.AMOUNT;
import static com.dlohaiti.dlokiosk.domain.PromotionType.PERCENT;
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

    @Test
    public void shouldKnowSubtotal() {
        Product product1 = productBuilder().withPrice(10d, "HTG").build();
        Product product2 = productBuilder().withPrice(15d, "HTG").build();
        cart.addProduct(product1);
        cart.addProduct(product2);
        Money subtotal = cart.getSubtotal();

        assertThat(subtotal.getAmount().compareTo(new BigDecimal("25")), is(0));
    }

    @Test
    public void shouldHaveOneCurrencyForShoppingCart() {
        Product product1 = productBuilder().withPrice(10d, "HTG").build();
        Product product2 = productBuilder().withPrice(15d, "HTG").build();
        cart.addProduct(product1);
        cart.addProduct(product2);

        assertThat(cart.getCurrencyCode(), is("HTG"));
    }

    @Test
    public void shouldNotHaveCurrencyForEmptyShoppingCart() {
        assertThat(cart.getCurrencyCode(), is(""));
    }

    @Test
    public void shouldReturnMISMATCHED_CURRENCIESForMultipleCurrenciesInCart() {
        Product product1 = productBuilder().withPrice(10d, "HTG").build();
        Product product2 = productBuilder().withPrice(15d, "USD").build();
        cart.addProduct(product1);
        cart.addProduct(product2);

        assertThat(cart.getCurrencyCode(), is("MISMATCHED CURRENCIES"));
    }

    @Test
    public void shouldTotalCart_happy_basketPercent() {
        Product product = productBuilder().withPrice(10d, "HTG").build();
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).withAmount("10").withPromotionType(PERCENT).build();
        cart.addProduct(product);
        cart.addPromotion(promo);

        assertThat(cart.getTotal().compareTo(new BigDecimal("9.00")), is(0));
    }

    @Test
    public void shouldTotalCart_happy_basketPercentMultiple() {
        Product product = productBuilder().withPrice(10d, "HTG").build();
        Promotion promo1 = promotionBuilder().thatAppliesTo(BASKET).withAmount("10").withPromotionType(PERCENT).build();
        Promotion promo2 = promotionBuilder().thatAppliesTo(BASKET).withAmount("10").withPromotionType(PERCENT).build();
        cart.addProduct(product);
        cart.addPromotion(promo1);
        cart.addPromotion(promo2);

        assertThat(cart.getTotal().compareTo(new BigDecimal("8.10")), is(0));
    }

    @Test
    public void shouldTotalCart_happy_basketAmount() {
        Product product = productBuilder().withPrice(10d, "HTG").build();
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).withAmount("10").withPromotionType(AMOUNT).build();
        cart.addProduct(product);
        cart.addPromotion(promo);

        assertThat(cart.getTotal().compareTo(BigDecimal.ZERO), is(0));
    }

    @Test
    public void shouldTotalCart_happy_basketAmountMultiple() {
        Product product = productBuilder().withPrice(10d, "HTG").build();
        Promotion promo1 = promotionBuilder().thatAppliesTo(BASKET).withAmount("5").withPromotionType(AMOUNT).build();
        Promotion promo2 = promotionBuilder().thatAppliesTo(BASKET).withAmount("1").withPromotionType(AMOUNT).build();
        cart.addProduct(product);
        cart.addPromotion(promo1);
        cart.addPromotion(promo2);

        assertThat(cart.getTotal().compareTo(new BigDecimal("4")), is(0));
    }

    @Test
    public void shouldTotalCart_sad_basketPercentOver100() {
        Product product = productBuilder().withPrice(10d, "HTG").build();
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).withAmount("120").withPromotionType(PERCENT).build();
        cart.addProduct(product);
        cart.addPromotion(promo);

        assertThat(cart.getTotal().compareTo(BigDecimal.ZERO), is(0));
    }

    @Test
    public void shouldTotalCart_sad_basketAmountUnderPromoAmount() {
        Product product = productBuilder().withPrice(10d, "HTG").build();
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).withAmount("10.50").withPromotionType(AMOUNT).build();
        cart.addProduct(product);
        cart.addPromotion(promo);

        assertThat(cart.getTotal().compareTo(BigDecimal.ZERO), is(0));
    }

    @Test
    public void shouldTotalCart_happy_skuPercentage() {
        Product product = productBuilder().withSku("ABC").withPrice(10d, "HTG").build();
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withAmount("20").withPromotionType(PERCENT).build();
        cart.addProduct(product);
        cart.addPromotion(promo);

        assertThat(cart.getTotal().compareTo(new BigDecimal("8")), is(0));
    }

    @Test
    public void shouldTotalCart_happy_skuAmount() {
        Product product = productBuilder().withSku("ABC").withPrice(10d, "HTG").build();
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withAmount("5.5").withPromotionType(AMOUNT).build();

        cart.addProduct(product);
        cart.addPromotion(promo);

        assertThat(cart.getTotal().compareTo(new BigDecimal("4.5")), is(0));
    }

    @Test
    public void shouldTotalCart_happy_skuPercentageJustOnce() {
        Product product1 = productBuilder().withSku("ABC").withPrice(10d, "HTG").build();
        Product product2 = productBuilder().withSku("ABC").withPrice(10d, "HTG").build();
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withAmount("10").withPromotionType(PERCENT).build();

        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addPromotion(promo);

        BigDecimal total = cart.getTotal();
        assertThat(total.compareTo(new BigDecimal("19")), is(0));
    }

    @Test
    public void shouldTotalCart_happy_skuAmountJustOnce() {
        Product product1 = productBuilder().withSku("ABC").withPrice(10d, "HTG").build();
        Product product2 = productBuilder().withSku("ABC").withPrice(10d, "HTG").build();
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withAmount("4").withPromotionType(AMOUNT).build();

        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addPromotion(promo);

        BigDecimal total = cart.getTotal();
        assertThat(total.compareTo(new BigDecimal("16")), is(0));
    }

    @Test
    public void shouldDiscountPercentagesBeforeAmounts() {
        Product product1 = productBuilder().withSku("ABC").withPrice(10d, "HTG").build();
        Product product2 = productBuilder().withSku("DEF").withPrice(5d, "HTG").build();
        Product product3 = productBuilder().withSku("GHI").withPrice(15d, "HTG").build();
        Promotion promo1 = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withAmount("2").withPromotionType(AMOUNT).build(); // $2 off ABC
        Promotion promo2 = promotionBuilder().thatAppliesTo(BASKET).withAmount("5").withPromotionType(AMOUNT).build();             // $5 off cart
        Promotion promo3 = promotionBuilder().thatAppliesTo(BASKET).withAmount("10").withPromotionType(PERCENT).build();           // 10% off cart

        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addProduct(product3);
        cart.addPromotion(promo1);
        cart.addPromotion(promo2);
        cart.addPromotion(promo3);

        assertThat(cart.getTotal().compareTo(new BigDecimal("20")), is(0));
    }

}
