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
import static org.mockito.Mockito.mock;

public class ShoppingCartNewTest {
    private ShoppingCartNew cart;
    private RegisterNew register;

    @Before
    public void setUp() {
        register = new RegisterNew(mock(Clock.class), mock(ReceiptsRepository.class));
        cart = new ShoppingCartNew(register);
    }

    @Test
    public void shouldClearOnCheckout() {
        cart.addOrUpdateProduct(productBuilder().build());
        cart.addPromotion(promotionBuilder().build());
        cart.setSalesChannel(new SalesChannelBuilder().build());
        cart.setCustomerAccount(new CustomerAccountBuilder().build());

        cart.checkout();

        assertThat(cart.isEmpty(), is(true));
    }

    @Test
    public void shouldNotBeClearWithAProduct() {
        cart.addOrUpdateProduct(productBuilder().build());

        assertThat(cart.isEmpty(), is(false));
    }

    @Test
    public void shouldBeClearAfterRemovingOnlyProduct() {
        Product product = productBuilder().build();

        cart.addOrUpdateProduct(product);
        cart.removeProduct(product);

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
    public void shouldClearPromotions() {
        cart.addPromotion(promotionBuilder().build());

        cart.clearPromotions();

        assertThat(cart.isEmpty(), is(true));
    }

    @Test
    public void shouldKnowSubtotalForSingleQuantity() {
        Product product1 = productBuilder().withId(1l).withPrice(10d).build();
        Product product2 = productBuilder().withId(2l).withPrice(15d).build();
        cart.addOrUpdateProduct(product1);
        cart.addOrUpdateProduct(product2);

        Money subtotal = cart.getActualTotal();

        assertThat(subtotal.getAmount().compareTo(new BigDecimal("25")), is(0));
    }

    @Test
    public void shouldKnowSubtotalForMultipleQuantities() {
        Product product = productBuilder().withPrice(5d).withQuantity(5).build();
        cart.addOrUpdateProduct(product);

        Money subtotal = cart.getActualTotal();

        assertThat(subtotal.getAmount().compareTo(new BigDecimal("25")), is(0));
    }

    @Test
    public void shouldHaveOneCurrencyForShoppingCart() {
        Product product1 = productBuilder().withPrice(10d).build();
        Product product2 = productBuilder().withPrice(15d).build();
        cart.addOrUpdateProduct(product1);
        cart.addOrUpdateProduct(product2);

        assertThat(cart.getCurrencyCode(), is("HTG"));
    }

    @Test
    public void shouldNotHaveCurrencyForEmptyShoppingCart() {
        assertThat(cart.getCurrencyCode(), is(""));
    }

    @Test
    public void shouldTotalCart_happy_basketPercent() {
        Product product = productBuilder().withPrice(10d).build();
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).withAmount("10").withPromotionType(PERCENT).build();
        cart.addOrUpdateProduct(product);
        cart.addPromotion(promo);

        assertThat(cart.getDiscountedTotal().compareTo(new Money(new BigDecimal("9.00"))), is(0));
    }

    @Test
    public void shouldTotalCart_happy_basketPercentMultiple() {
        Product product = productBuilder().withPrice(10d).build();
        Promotion promo1 = promotionBuilder().thatAppliesTo(BASKET).withAmount("10").withPromotionType(PERCENT).build();
        Promotion promo2 = promotionBuilder().thatAppliesTo(BASKET).withAmount("10").withPromotionType(PERCENT).build();
        cart.addOrUpdateProduct(product);
        cart.addPromotion(promo1);
        cart.addPromotion(promo2);

        Money total = cart.getDiscountedTotal();
        assertThat(total.compareTo(new Money(new BigDecimal("8.10"))), is(0));
    }

    @Test
    public void shouldTotalCart_happy_basketAmount() {
        Product product = productBuilder().withPrice(10d).build();
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).withAmount("10").withPromotionType(AMOUNT).build();
        cart.addOrUpdateProduct(product);
        cart.addPromotion(promo);

        assertThat(cart.getDiscountedTotal().compareTo(Money.ZERO), is(0));
    }

    @Test
    public void shouldTotalCart_happy_basketAmountMultiple() {
        Product product = productBuilder().withPrice(10d).build();
        Promotion promo1 = promotionBuilder().thatAppliesTo(BASKET).withAmount("5").withPromotionType(AMOUNT).build();
        Promotion promo2 = promotionBuilder().thatAppliesTo(BASKET).withAmount("1").withPromotionType(AMOUNT).build();
        cart.addOrUpdateProduct(product);
        cart.addPromotion(promo1);
        cart.addPromotion(promo2);

        assertThat(cart.getDiscountedTotal().compareTo(new Money(new BigDecimal("4"))), is(0));
    }

    @Test
    public void shouldTotalCart_sad_basketPercentOver100() {
        Product product = productBuilder().withPrice(10d).build();
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).withAmount("120").withPromotionType(PERCENT).build();
        cart.addOrUpdateProduct(product);
        cart.addPromotion(promo);

        assertThat(cart.getDiscountedTotal().compareTo(Money.ZERO), is(0));
    }

    @Test
    public void shouldTotalCart_sad_basketAmountUnderPromoAmount() {
        Product product = productBuilder().withPrice(10d).build();
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).withAmount("10.50").withPromotionType(AMOUNT).build();
        cart.addOrUpdateProduct(product);
        cart.addPromotion(promo);

        assertThat(cart.getDiscountedTotal().compareTo(Money.ZERO), is(0));
    }

    @Test
    public void shouldTotalCart_happy_skuPercentage() {
        Product product = productBuilder().withSku("ABC").withPrice(10d).build();
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withAmount("20").withPromotionType(PERCENT).build();
        cart.addOrUpdateProduct(product);
        cart.addPromotion(promo);

        assertThat(cart.getDiscountedTotal().compareTo(new Money(new BigDecimal("8"))), is(0));
    }

    @Test
    public void shouldTotalCart_happy_skuAmount() {
        Product product = productBuilder().withSku("ABC").withPrice(10d).build();
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withAmount("5.5").withPromotionType(AMOUNT).build();

        cart.addOrUpdateProduct(product);
        cart.addPromotion(promo);

        assertThat(cart.getDiscountedTotal().compareTo(new Money(new BigDecimal("4.5"))), is(0));
    }

    @Test
    public void shouldTotalCart_happy_skuPercentageAsManyTimesAsProductQuantity() {
        Product product1 = productBuilder().withSku("ABC").withPrice(10d).withQuantity(2).build();
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withAmount("10").withPromotionType(PERCENT).build();
        cart.addOrUpdateProduct(product1);
        cart.addPromotion(promo);

        Money total = cart.getDiscountedTotal();
        assertThat(total, is(new Money(new BigDecimal("18.00"))));
    }

    @Test
    public void shouldTotalCart_happy_skuAmountAsManyTimesAsProductQuantity() {
        Product product1 = productBuilder().withSku("ABC").withPrice(10d).withQuantity(2).build();
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withAmount("4").withPromotionType(AMOUNT).build();
        cart.addOrUpdateProduct(product1);
        cart.addPromotion(promo);

        Money total = cart.getDiscountedTotal();

        assertThat(total, is(new Money(new BigDecimal("12.00"))));
    }

    @Test
    public void shouldGetSameResultEveryTime() {
        Product product1 = productBuilder().withSku("ABC").withPrice(10d).withQuantity(2).build();
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withAmount("4").withPromotionType(AMOUNT).build();

        cart.addOrUpdateProduct(product1);
        cart.addPromotion(promo);

        assertThat(cart.getDiscountedTotal(), is(new Money(new BigDecimal("12.00"))));
        assertThat(cart.getDiscountedTotal(), is(new Money(new BigDecimal("12.00"))));
    }

    @Test
    public void shouldDiscountPercentagesBeforeAmounts() {
        Product product1 = productBuilder().withId(1l).withSku("ABC").withPrice(10d).build();
        Product product2 = productBuilder().withId(2l).withSku("DEF").withPrice(5d).build();
        Product product3 = productBuilder().withId(3l).withSku("GHI").withPrice(15d).build();
        Promotion promo1 = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withAmount("2").withPromotionType(AMOUNT).build(); // $2 off ABC
        Promotion promo2 = promotionBuilder().thatAppliesTo(BASKET).withAmount("5").withPromotionType(AMOUNT).build();             // $5 off cart
        Promotion promo3 = promotionBuilder().thatAppliesTo(BASKET).withAmount("10").withPromotionType(PERCENT).build();           // 10% off cart

        cart.addOrUpdateProduct(product1);
        cart.addOrUpdateProduct(product2);
        cart.addOrUpdateProduct(product3);
        cart.addPromotion(promo1);
        cart.addPromotion(promo2);
        cart.addPromotion(promo3);

        Money total = cart.getDiscountedTotal();
        assertThat(total.compareTo(new Money(new BigDecimal("19.99"))), is(0));
    }

}
