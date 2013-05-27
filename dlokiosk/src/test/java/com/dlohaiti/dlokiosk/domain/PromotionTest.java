package com.dlohaiti.dlokiosk.domain;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.dlohaiti.dlokiosk.domain.ProductBuilder.productBuilder;
import static com.dlohaiti.dlokiosk.domain.PromotionApplicationType.BASKET;
import static com.dlohaiti.dlokiosk.domain.PromotionApplicationType.SKU;
import static com.dlohaiti.dlokiosk.domain.PromotionBuilder.promotionBuilder;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PromotionTest {

    @Test
    public void shouldAlwaysApplyBasketPromotions() {
        List<Product> shoppingCart = asList(productBuilder().build());
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(true));
    }

    @Test
    public void shouldOnlyApplySkuPromotionToMatchingSku() {
        List<Product> shoppingCart = asList(productBuilder().withSku("ABC").build());
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withProductSku("ABC").build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(true));
    }

    @Test
    public void shouldNotApplySkuPromotionToMismatchedSku() {
        List<Product> shoppingCart = asList(productBuilder().withSku("ABC").build());
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withProductSku("XYZ").build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldNotApplyToBasketIfBeforeStartDate() {
        List<Product> shoppingCart = asList(productBuilder().withSku("ABC").build());
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).withStartDate(tomorrow()).build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldNotApplyToSkuIfBeforeStartDate() {
        List<Product> shoppingCart = asList(productBuilder().withSku("ABC").build());
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withProductSku("ABC").withStartDate(tomorrow()).build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldNotApplyToBasketIfAfterEndDate() {
        List<Product> shoppingCart = asList(productBuilder().withSku("ABC").build());
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).withEndDate(yesterday()).build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldNotApplyToSkuIfAfterEndDate() {
        List<Product> shoppingCart = asList(productBuilder().withSku("ABC").build());
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withProductSku("ABC").withEndDate(yesterday()).build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldKnowAmountAndTypeOfDiscount() {
        Promotion tenPercent = promotionBuilder().withAmount("10").withPromotionType(PromotionType.PERCENT).build();

        assertThat(tenPercent.getAmount(), is(BigDecimal.TEN));
        assertThat(tenPercent.getType(), is(PromotionType.PERCENT));
    }

    @Test
    public void shouldOrderPercentagePromotionsBeforeAmountPromotions() {
        Promotion percent = promotionBuilder().withPromotionType(PromotionType.PERCENT).build();
        Promotion amount = promotionBuilder().withPromotionType(PromotionType.AMOUNT).build();

        assertThat(percent.compareTo(amount), is(-1));
    }

    @Test
    public void shouldOrderPercentagePromotionsWithHigherValueBeforeLowerValuePercentage() {
        Promotion lowPercent = promotionBuilder().withAmount("10").withPromotionType(PromotionType.PERCENT).build();
        Promotion highPercent = promotionBuilder().withAmount("30").withPromotionType(PromotionType.PERCENT).build();

        assertThat(lowPercent.compareTo(highPercent), is(1));
    }

    private Date yesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    private Date tomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
