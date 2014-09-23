package com.dlohaiti.dlokiosk.domain;

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

    @Inject
    public RegisterNew(Clock clock, ReceiptsRepository repository) {
        this.clock = clock;
        this.repository = repository;
    }

    public Receipt checkout(ShoppingCartNew cart) {
        List<LineItem> lineItems = buildLineItemsFrom(cart);
        int totalGallons = 0;
        for (Product product : cart.getProducts()) {
            totalGallons += product.getGallons() * product.getQuantity();
        }
        Receipt receipt = new Receipt(lineItems, clock.now(), totalGallons, cart.getTotal());
        repository.add(receipt);
        return receipt;
    }

    private List<LineItem> buildLineItemsFrom(ShoppingCartNew cart) {
        List<LineItem> lineItems = new ArrayList<LineItem>();
        for (Promotion promotion : cart.getPromotions()) {
            lineItems.add(new LineItem(promotion.getSku(), promotion.getQuantity(), new Money(BigDecimal.ZERO), PROMOTION));
        }
        List<Promotion> promotionsCopy = new ArrayList<Promotion>(cart.getPromotions());
        Collections.sort(promotionsCopy); // percentages and large amounts first
        BigDecimal subtotal = cart.getSubtotal().getAmount();
        List<Product> productsCopy = new ArrayList<Product>(cart.getProducts());

        // deduct everything at the basket-level first
        List<Discount> discounts = new ArrayList<Discount>();
        for (Iterator<Promotion> it = promotionsCopy.iterator(); it.hasNext(); ) {
            Promotion promo = it.next();
            if (promo.appliesToBasket()) {
                BigDecimal discount = promo.discountCart(subtotal);
                subtotal = subtotal.subtract(discount);
                BigDecimal discountPerItem = discount.divide(new BigDecimal(cart.getProducts().size()), 4, RoundingMode.HALF_UP);
                for (Product p : productsCopy) {
                    discounts.add(new Discount(p.getSku(), new Money(discountPerItem)));
                }
                it.remove();
            }
        }

        for (Product product : productsCopy) {
            for (Iterator<Promotion> it = promotionsCopy.iterator(); it.hasNext(); ) {
                Promotion promo = it.next();
                if (promo.isFor(product)) {
                    discounts.add(new Discount(product.getSku(), new Money(promo.discountFor(product))));
                    it.remove();
                }
            }
        }

        for (Product product : productsCopy) {
            Money actualPrice = retailPriceFor(product);
            for (Iterator<Discount> it = discounts.iterator(); it.hasNext(); ) {
                Discount discount = it.next();
                if (discount.isFor(product)) {
                    actualPrice = actualPrice.minus(discount.amountMoney());
                    it.remove();
                }
            }
            lineItems.add(new LineItem(product.getSku(), product.getQuantity(), actualPrice, PRODUCT));
        }

        return lineItems;
    }

    private Money retailPriceFor(Product product) {
        return product.getPrice().times(product.getQuantity());
    }

    public Money subtotal(ShoppingCartNew cart) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Product product : cart.getProducts()) {
            subtotal = subtotal.add(retailPriceFor(product).getAmount());
        }
        return new Money(subtotal);
    }

    public Money total(ShoppingCartNew cart) {
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
