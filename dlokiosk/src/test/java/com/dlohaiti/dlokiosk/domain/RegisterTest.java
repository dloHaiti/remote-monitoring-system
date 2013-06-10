package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ReceiptsRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dlohaiti.dlokiosk.domain.ProductBuilder.productBuilder;
import static com.dlohaiti.dlokiosk.domain.PromotionApplicationType.BASKET;
import static com.dlohaiti.dlokiosk.domain.PromotionBuilder.promotionBuilder;
import static com.dlohaiti.dlokiosk.domain.PromotionType.PERCENT;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class RegisterTest {
    Promotion tenPercentOffABC = promotionBuilder().thatAppliesTo(PromotionApplicationType.SKU).withProductSku("ABC").withAmount("10").withPromotionType(PERCENT).build();
    Product tenDollarABC = productBuilder().withSku("ABC").withPrice(10d).build();
    Register register = new Register(mock(Clock.class));
    Promotion tenPercentOffBasket = promotionBuilder().thatAppliesTo(BASKET).withAmount("10").withPromotionType(PERCENT).build();
    ShoppingCart cart;

    @Before
    public void setUp() {
        cart = new ShoppingCart(mock(ReceiptsRepository.class));
    }

    @Test
    public void shouldApplySkuPromotionToLineItemsIndividually() {
        cart.addProduct(tenDollarABC);
        cart.addPromotion(tenPercentOffABC);
        Receipt receipt = register.checkout(cart);

        assertThat(receipt.getProductLineItemsCount(), is(1));
        assertThat(receipt.getTotal(), is(new Money(new BigDecimal(9))));
        assertThat(receipt.getProductLineItems().get(0).getPrice(), is(new Money(new BigDecimal(9))));
    }

    @Test
    public void shouldApplySkuPromotionOnlyOnce() {
        cart.addProduct(tenDollarABC);
        cart.addProduct(tenDollarABC);
        cart.addPromotion(tenPercentOffABC);
        Receipt receipt = register.checkout(cart);

        List<Money> lineItemPrices = new ArrayList<Money>();
        for (LineItem item : receipt.getProductLineItems()) {
            lineItemPrices.add(item.getPrice());
        }

        assertThat(receipt.getTotal(), is(new Money(new BigDecimal(19))));
        assertThat(receipt.getProductLineItemsCount(), is(2));

        // should have one line item with 10 and one with 9
        assertThat(lineItemPrices.contains(new Money(BigDecimal.TEN)), is(true));
        assertThat(lineItemPrices.contains(new Money(new BigDecimal(9))), is(true));
    }

    @Test
    public void shouldApplyBasketPromotionEquallyToAllItems() {
        cart.addProduct(tenDollarABC);
        cart.addProduct(tenDollarABC);
        cart.addProduct(tenDollarABC);
        cart.addPromotion(tenPercentOffBasket);
        Receipt receipt = register.checkout(cart);

        List<Money> lineItemPrices = new ArrayList<Money>();
        for(LineItem item : receipt.getProductLineItems()) {
            lineItemPrices.add(item.getPrice());
        }

        assertThat(receipt.getTotal(), is(new Money(new BigDecimal(27))));
        assertThat(receipt.getProductLineItemsCount(), is(3));

        //since all are the same price, they should all reflect a 10% discount
        for(Money price : lineItemPrices) {
            assertThat(price, is(new Money(new BigDecimal(9))));
        }
    }
}
