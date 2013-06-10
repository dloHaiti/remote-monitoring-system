package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ReceiptsRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.dlohaiti.dlokiosk.domain.ProductBuilder.productBuilder;
import static com.dlohaiti.dlokiosk.domain.PromotionApplicationType.BASKET;
import static com.dlohaiti.dlokiosk.domain.PromotionApplicationType.SKU;
import static com.dlohaiti.dlokiosk.domain.PromotionBuilder.promotionBuilder;
import static com.dlohaiti.dlokiosk.domain.PromotionType.PERCENT;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class RegisterTest {
    Product tenDollarTenGallonABC;
    Promotion tenPercentOffABC = promotionBuilder()
            .thatAppliesTo(PromotionApplicationType.SKU)
            .withProductSku("ABC")
            .withAmount("10")
            .withPromotionType(PERCENT).build();
    Promotion tenPercentOffBasket = promotionBuilder()
            .thatAppliesTo(BASKET)
            .withAmount("10")
            .withPromotionType(PERCENT).build();
    ReceiptsRepository repository = mock(ReceiptsRepository.class);
    Register register = new Register(mock(Clock.class), repository);
    ShoppingCart cart;

    @Before
    public void setUp() {
        cart = new ShoppingCart(register);
        tenDollarTenGallonABC = productBuilder()
                .withSku("ABC")
                .withPrice(10d)
                .withGallons(10).build();
    }

    @Test
    public void shouldApplySkuPromotionToLineItemsIndividually() {
        cart.addProduct(tenDollarTenGallonABC);
        cart.addPromotion(tenPercentOffABC);
        Receipt receipt = register.checkout(cart);

        assertThat(receipt.getProductLineItemsCount(), is(1));
        assertThat(receipt.getTotal().getAmount().compareTo(new BigDecimal(9)), is(0));
        assertThat(receipt.getProductLineItems().get(0).getPrice().getAmount().compareTo(new BigDecimal(9)), is(0));
    }

    @Test
    public void shouldApplySkuPromotionOnlyOnce() {
        cart.addProduct(tenDollarTenGallonABC);
        cart.addProduct(tenDollarTenGallonABC);
        cart.addPromotion(tenPercentOffABC);
        Receipt receipt = register.checkout(cart);

        List<Money> lineItemPrices = new ArrayList<Money>();
        for (LineItem item : receipt.getProductLineItems()) {
            lineItemPrices.add(item.getPrice());
        }

        assertThat(receipt.getTotal(), is(new Money(new BigDecimal("19.00"))));
        assertThat(receipt.getProductLineItemsCount(), is(2));

        // should have one line item with 10 and one with 9
        assertThat(lineItemPrices.contains(new Money(BigDecimal.TEN)), is(true));
        assertThat(lineItemPrices.contains(new Money(new BigDecimal("9.00"))), is(true));
    }

    @Test
    public void shouldApplyBasketPromotionEquallyToAllItems() {
        cart.addProduct(tenDollarTenGallonABC);
        cart.addProduct(tenDollarTenGallonABC);
        cart.addProduct(tenDollarTenGallonABC);
        cart.addPromotion(tenPercentOffBasket);
        Receipt receipt = register.checkout(cart);

        BigDecimal lineItemTotal = BigDecimal.ZERO;
        for(LineItem item : receipt.getProductLineItems()) {
            lineItemTotal = lineItemTotal.add(item.getPrice().getAmount());
        }

        assertThat(receipt.getTotal(), is(new Money(new BigDecimal("27.00"))));
        assertThat(lineItemTotal, is(new BigDecimal("27.00")));
        assertThat(receipt.getProductLineItemsCount(), is(3));
    }

    @Test
    public void shouldRoundCorrectly() {
        cart.addProduct(productBuilder().withPrice(7d).withSku("ZZZ").build());
        cart.addPromotion(promotionBuilder().withProductSku("ZZZ").withAmount("23").withPromotionType(PERCENT).thatAppliesTo(SKU).build());
        Receipt receipt = register.checkout(cart);
        assertThat(receipt.getProductLineItems().get(0).getPrice(), is(new Money(new BigDecimal("5.39"))));
    }

    @Test
    public void shouldTotalGallons() {
        cart.addProduct(tenDollarTenGallonABC);
        cart.addProduct(tenDollarTenGallonABC);
        cart.addProduct(tenDollarTenGallonABC);
        Receipt receipt = register.checkout(cart);

        assertThat(receipt.getTotalGallons(), is(30));
    }

    @Test
    public void shouldAddToRepositoryOnCheckout() {
        cart.addProduct(tenDollarTenGallonABC);
        Receipt receipt = register.checkout(cart);
        verify(repository, times(1)).add(receipt);
    }

    @Test
    public void shouldSubtotalCart() {
        cart.addProduct(tenDollarTenGallonABC);
        cart.addProduct(tenDollarTenGallonABC);
        cart.addProduct(productBuilder().withPrice(17.35).build());
        cart.addPromotion(tenPercentOffBasket);
        Money subtotal = register.subtotal(cart);

        assertThat(subtotal, is(new Money(new BigDecimal("37.35"))));
    }

    @Test
    public void shouldTotalCart() {
        cart.addProduct(tenDollarTenGallonABC);
        cart.addProduct(tenDollarTenGallonABC);
        cart.addProduct(productBuilder().withPrice(17.3567).build());
        cart.addPromotion(tenPercentOffBasket);
        Money total = register.total(cart);

        assertThat(total, is(new Money(new BigDecimal("33.61"))));
    }
}
