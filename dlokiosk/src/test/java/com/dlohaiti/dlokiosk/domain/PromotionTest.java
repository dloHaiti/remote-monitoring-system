package com.dlohaiti.dlokiosk.domain;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.dlohaiti.dlokiosk.domain.PromotionApplicationType.BASKET;
import static com.dlohaiti.dlokiosk.domain.PromotionApplicationType.SKU;
import static com.dlohaiti.dlokiosk.domain.PromotionBuilder.promotionBuilder;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PromotionTest {

    @Test
    public void shouldAlwaysApplyBasketPromotions() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(true));
    }

    @Test
    public void shouldOnlyApplySkuPromotionToMatchingSku() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(true));
    }

    @Test
    public void shouldNotApplySkuPromotionToMismatchedSku() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("XYZ").build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldNotApplyToBasketIfBeforeStartDate() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).withStartDate(tomorrow()).build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldNotApplyToSkuIfBeforeStartDate() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withStartDate(tomorrow()).build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldNotApplyToBasketIfAfterEndDate() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = promotionBuilder().thatAppliesTo(BASKET).withEndDate(yesterday()).build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldNotApplyToSkuIfAfterEndDate() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = promotionBuilder().thatAppliesTo(SKU).withSku("ABC").withEndDate(yesterday()).build();
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldKnowAmountAndTypeOfDiscount() {
        Promotion tenPercent = promotionBuilder().withAmount("10").withPromotionType(PromotionType.PERCENT).build();

        assertThat(tenPercent.getAmount(), is(BigDecimal.TEN));
        assertThat(tenPercent.getType(), is(PromotionType.PERCENT));
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
