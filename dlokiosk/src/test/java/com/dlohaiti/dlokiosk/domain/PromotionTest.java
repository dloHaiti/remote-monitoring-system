package com.dlohaiti.dlokiosk.domain;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PromotionTest {

    @Test
    public void shouldAlwaysApplyBasketPromotions() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = new Promotion(PromotionApplicationType.BASKET, "", new Date(), tomorrow(), "10.0", PromotionType.PERCENT);
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(true));
    }

    @Test
    public void shouldOnlyApplySkuPromotionToMatchingSku() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = new Promotion(PromotionApplicationType.SKU, "ABC", new Date(), tomorrow(), "10.0", PromotionType.PERCENT);
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(true));
    }


    @Test
    public void shouldNotApplySkuPromotionToMismatchedSku() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = new Promotion(PromotionApplicationType.SKU, "XYZ", new Date(), tomorrow(), "10.0", PromotionType.PERCENT);
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldNotApplyToBasketIfBeforeStartDate() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = new Promotion(PromotionApplicationType.BASKET, "", tomorrow(), nextWeek(), "10.0", PromotionType.PERCENT);
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldNotApplyToSkuIfBeforeStartDate() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = new Promotion(PromotionApplicationType.SKU, "ABC", tomorrow(), nextWeek(), "10.0", PromotionType.PERCENT);
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldNotApplyToBasketIfAfterEndDate() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = new Promotion(PromotionApplicationType.BASKET, "", new Date(100), new Date(101), "10.0", PromotionType.PERCENT);
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldNotApplyToSkuIfAfterEndDate() {
        List<OrderedProduct> shoppingCart = asList(new OrderedProduct("ABC", 2));
        Promotion promo = new Promotion(PromotionApplicationType.SKU, "ABC", new Date(100), new Date(101), "10.0", PromotionType.PERCENT);
        boolean applicable = promo.appliesTo(shoppingCart);
        assertThat(applicable, is(false));
    }

    @Test
    public void shouldKnowAmountAndTypeOfDiscount() {
        Promotion tenPercent = new Promotion(PromotionApplicationType.SKU, "ABC", new Date(100), new Date(101), "10", PromotionType.PERCENT);

        assertThat(tenPercent.getAmount(), is(BigDecimal.TEN));
        assertThat(tenPercent.getType(), is(PromotionType.PERCENT));
    }

    private Date nextWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        return calendar.getTime();
    }

    private Date tomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
