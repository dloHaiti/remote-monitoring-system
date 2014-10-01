package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.ReceiptsRepository;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.ReceiptLineItemType.PRODUCT;
import static com.dlohaiti.dlokiosk.db.ReceiptLineItemType.PROMOTION;

public class RegisterNew {

    private final Clock clock;
    private final ReceiptsRepository repository;
    private CustomerAccountRepository customerAccountRepository;

    @Inject
    public RegisterNew(Clock clock, ReceiptsRepository repository, CustomerAccountRepository customerAccountRepository) {
        this.clock = clock;
        this.repository = repository;
        this.customerAccountRepository = customerAccountRepository;
    }

    public Receipt checkout(ShoppingCartNew cart) {
        List<LineItem> lineItems = buildLineItemsFrom(cart);
        int totalGallons = 0;
        for (Product product : cart.getProducts()) {
            totalGallons += product.getGallons() * product.getQuantity();
        }
        String sponsorId = cart.isSponsorSelected() ? cart.sponsor().getId() : null;
        Receipt receipt = new Receipt(lineItems, clock.now(), totalGallons,
                cart.getDiscountedTotal(), cart.salesChannel().getId(), cart.customerAccount().getId(),
                cart.paymentMode(), cart.isSponsorSelected(), sponsorId,
                cart.sponsorAmount(), cart.customerAmount(), cart.paymentType(), cart.deliveryTime());
        repository.add(receipt);
        updateDueAmountOfCustomerIfTotalAmountIsNotPaid(cart);
        return receipt;
    }

    private void updateDueAmountOfCustomerIfTotalAmountIsNotPaid(ShoppingCartNew cart) {
        if (cart.dueAmount().isGreaterThan(Money.ZERO)) {
            cart.customerAccount()
                    .setDueAmount(cart.customerAccount().getDueAmount() + cart.dueAmount().amountAsDouble())
                    .setIsSynced(false);
            customerAccountRepository.save(cart.customerAccount());
        }
    }

    private List<LineItem> buildLineItemsFrom(ShoppingCartNew cart) {
        List<LineItem> lineItems = new ArrayList<LineItem>();
        for (Promotion promotion : cart.getPromotions()) {
            lineItems.add(new LineItem(promotion.getSku(), promotion.getQuantity(), new Money(BigDecimal.ZERO), PROMOTION));
        }
        List<Promotion> promotionsCopy = new ArrayList<Promotion>(cart.getPromotions());
        Collections.sort(promotionsCopy); // percentages and large amounts first
        BigDecimal actualTotal = cart.getActualTotal().getAmount();
        List<Product> productsCopy = new ArrayList<Product>(cart.getProducts());

        // deduct everything at the basket-level first
        List<Discount> discounts = new ArrayList<Discount>();
        for (Iterator<Promotion> it = promotionsCopy.iterator(); it.hasNext(); ) {
            Promotion promo = it.next();
            if (promo.appliesToBasket()) {
                BigDecimal discount = promo.discountCart(actualTotal);
                actualTotal = actualTotal.subtract(discount);
                BigDecimal discountPerItem = discount.divide(new BigDecimal(cart.getProducts().size()), 4, RoundingMode.HALF_UP);
                for (Product p : productsCopy) {
                    discounts.add(new Discount(p.getSku(), new Money(discountPerItem)));
                }
                it.remove();
            }
        }

        for (Product product : productsCopy) {
            for (Promotion promo : promotionsCopy) {
                if (promo.isFor(product)) {
                    discounts.add(new Discount(product.getSku(), new Money(promo.discountFor(product))));
                }
            }
        }

        for (Product product : productsCopy) {
            Money actualPrice = retailPriceFor(product);
            for (Discount discount : discounts) {
                if (discount.isFor(product)) {
                    actualPrice = actualPrice.minus(discount.amountMoney());
                }
            }
            lineItems.add(new LineItem(product.getSku(), product.getQuantity(), actualPrice, PRODUCT));
        }

        return lineItems;
    }

    private Money retailPriceFor(Product product) {
        return product.getPrice().times(product.getQuantity());
    }

    public Money actualTotal(ShoppingCartNew cart) {
        BigDecimal actualTotal = BigDecimal.ZERO;
        for (Product product : cart.getProducts()) {
            actualTotal = actualTotal.add(retailPriceFor(product).getAmount());
        }
        return new Money(actualTotal);
    }

    public Money discountedTotal(ShoppingCartNew cart) {
        List<LineItem> lineItems = buildLineItemsFrom(cart);
        BigDecimal total = BigDecimal.ZERO;
        for (LineItem item : lineItems) {
            total = total.add(item.getPrice().getAmount());
        }
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            return new Money(BigDecimal.ZERO);
        }
        return new Money(total);
    }
}
