package com.dlohaiti.dlokiosknew.domain;

import com.dlohaiti.dlokiosknew.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosknew.db.PaymentHistoryRepository;
import com.dlohaiti.dlokiosknew.db.ReceiptLineItemType;
import com.dlohaiti.dlokiosknew.db.ReceiptsRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;

import static com.dlohaiti.dlokiosknew.domain.ProductBuilder.productBuilder;
import static com.dlohaiti.dlokiosknew.domain.PromotionApplicationType.BASKET;
import static com.dlohaiti.dlokiosknew.domain.PromotionApplicationType.SKU;
import static com.dlohaiti.dlokiosknew.domain.PromotionBuilder.promotionBuilder;
import static com.dlohaiti.dlokiosknew.domain.PromotionType.PERCENT;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RegisterNewTest {
    Product tenDollarTenGallonABC;
    Product fiveDollarFiveGallonXYZ;
    Promotion tenPercentOffABC;
    Promotion tenPercentOffBasket;
    @Mock
    ReceiptsRepository repository;
    @Mock
    CustomerAccountRepository customerAccountRepository;

    @Mock
    PaymentHistoryRepository paymentHistoryRepository;
    RegisterNew register;
    ShoppingCartNew cart;

    @Before
    public void setUp() {
        initMocks(this);
        tenPercentOffABC = promotionBuilder()
                .thatAppliesTo(PromotionApplicationType.SKU)
                .withProductSku("ABC")
                .withAmount("10")
                .withPromotionType(PERCENT).build();
        tenPercentOffBasket = promotionBuilder()
                .thatAppliesTo(BASKET)
                .withAmount("10")
                .withPromotionType(PERCENT).build();
        register = new RegisterNew(mock(Clock.class), repository, customerAccountRepository,paymentHistoryRepository);
        cart = new ShoppingCartNew(register);
        cart.setSalesChannel(new SalesChannelBuilder().build());
        cart.setCustomerAccount(new CustomerAccountBuilder().build());
        tenDollarTenGallonABC = productBuilder()
                .withId(1l)
                .withSku("ABC")
                .withPrice(10d)
                .withGallons(10.0)
                .withQuantity(1)
                .build();
        fiveDollarFiveGallonXYZ = productBuilder()
                .withId(2l)
                .withSku("XYZ")
                .withPrice(5d)
                .withQuantity(1)
                .withGallons(5.0)
                .build();
    }

    @Test
    public void shouldApplySkuPromotionToLineItemsIndividually() {
        cart.addOrUpdateProduct(tenDollarTenGallonABC);
        cart.addPromotion(tenPercentOffABC);
        Receipt receipt = register.checkout(cart);

        assertThat(receipt.getProductLineItemsCount(), is(1));
        assertThat(receipt.getTotal().getAmount().compareTo(new BigDecimal(9)), is(0));
        assertThat(receipt.getProductLineItems().get(0).getPrice().getAmount().compareTo(new BigDecimal(9)), is(0));
    }

    @Test
    public void shouldApplySkuPromotionAsManyTimesAsTheQuantityOfProducts() {
        cart.addOrUpdateProduct(tenDollarTenGallonABC.withQuantity(2));
        cart.addPromotion(tenPercentOffABC);

        Receipt receipt = register.checkout(cart);

        assertThat(receipt.getTotal(), is(new Money(new BigDecimal("18.00"))));
        assertThat(receipt.getProductLineItemsCount(), is(2));
        assertThat(receipt.getLineItemsCount(), is(3));
        assertThat(receipt.getProductLineItems().contains(new LineItem(tenDollarTenGallonABC.getSku(),
                2,
                new Money(new BigDecimal("18.00"), "HTG"),
                ReceiptLineItemType.PRODUCT)), is(true));
    }

    @Test
    public void shouldApplyBasketPromotionEquallyToAllItems() {
        cart.addOrUpdateProduct(tenDollarTenGallonABC);
        cart.addOrUpdateProduct(fiveDollarFiveGallonXYZ);
        cart.addPromotion(tenPercentOffBasket);

        Receipt receipt = register.checkout(cart);

        BigDecimal lineItemTotal = BigDecimal.ZERO;
        for (LineItem item : receipt.getProductLineItems()) {
            lineItemTotal = lineItemTotal.add(item.getPrice().getAmount());
        }

        assertThat(receipt.getTotal(), is(new Money(new BigDecimal("13.50"))));
        assertThat(receipt.getProductLineItems().contains(
                new LineItem(tenDollarTenGallonABC.getSku(),
                        1,
                        new Money(new BigDecimal("9.25"), "HTG"),
                        ReceiptLineItemType.PRODUCT)), is(true));
        assertThat(receipt.getProductLineItems().contains(new LineItem(fiveDollarFiveGallonXYZ.getSku(),
                1,
                new Money(new BigDecimal("4.25"), "HTG"),
                ReceiptLineItemType.PRODUCT)), is(true));
    }

    @Test
    public void shouldRoundCorrectly() {
        cart.addOrUpdateProduct(productBuilder().withPrice(7d).withSku("ZZZ").build());
        cart.addPromotion(promotionBuilder().withProductSku("ZZZ").withAmount("23").withPromotionType(PERCENT).thatAppliesTo(SKU).build());

        Receipt receipt = register.checkout(cart);

        assertThat(receipt.getProductLineItems().get(0).getPrice(), is(new Money(new BigDecimal("5.39"))));
    }

    @Test
    public void shouldTotalGallons() {
        cart.addOrUpdateProduct(tenDollarTenGallonABC.withQuantity(3));

        Receipt receipt = register.checkout(cart);

        assertThat(receipt.getTotalGallons(), is(30.0));
    }

    @Test
    public void shouldAddToRepositoryOnCheckout() {
        cart.addOrUpdateProduct(tenDollarTenGallonABC);
        Receipt receipt = register.checkout(cart);
        verify(repository, times(1)).add(receipt);
    }

    @Test
    public void shouldComputeActualTotalOfCart() {
        cart.addOrUpdateProduct(tenDollarTenGallonABC.withQuantity(2));
        cart.addOrUpdateProduct(fiveDollarFiveGallonXYZ);
        cart.addPromotion(tenPercentOffBasket);

        Money actualTotal = register.actualTotal(cart);

        assertThat(actualTotal, is(new Money(new BigDecimal("25.00"))));
    }

    @Test
    public void shouldComputeDiscountedTotalCart() {
        cart.addOrUpdateProduct(tenDollarTenGallonABC.withQuantity(2));
        cart.addOrUpdateProduct(fiveDollarFiveGallonXYZ);
        cart.addPromotion(tenPercentOffBasket);

        Money total = register.discountedTotal(cart);

        assertThat(total, is(new Money(new BigDecimal("22.50"))));
    }

    @Test
    public void shouldSaveDueAmountWhenTotalAmountIsNotPaid() {
        cart.addOrUpdateProduct(tenDollarTenGallonABC);
        cart.setCustomerAmount(new Money("4.0", "HTG"));
        cart.setSponsorAmount(new Money("4.0", "HTG"));
        cart.setCustomerAccount(new CustomerAccountBuilder().withDueAmount(0).withIsSynced(true).build());

        register.checkout(cart);

        verify(customerAccountRepository).save(new CustomerAccountBuilder().withDueAmount(2).withIsSynced(false).build());
    }

    @Test
    public void shouldAddSaleDueAmountToCustomerDueAmountWhenTotalAmountIsNotPaid() {
        cart.addOrUpdateProduct(tenDollarTenGallonABC);
        cart.setCustomerAmount(new Money("4.0", "HTG"));
        cart.setSponsorAmount(new Money("4.0", "HTG"));
        CustomerAccount account = new CustomerAccountBuilder().withDueAmount(5.0).withIsSynced(true).build();
        cart.setCustomerAccount(account);

        register.checkout(cart);

        verify(customerAccountRepository).save(new CustomerAccountBuilder().withDueAmount(7.0).withIsSynced(false).build());
    }

    @Test
    public void shouldNotSaveDueAmountWhenTotalAmountIsPaid() {
        cart.addOrUpdateProduct(tenDollarTenGallonABC);
        cart.setCustomerAmount(new Money("5.0", "HTG"));
        cart.setSponsorAmount(new Money("5.0", "HTG"));
        CustomerAccount account = new CustomerAccountBuilder().withDueAmount(5.0).withIsSynced(false).build();
        cart.setCustomerAccount(account);

        register.checkout(cart);

        verifyZeroInteractions(customerAccountRepository);
    }
}