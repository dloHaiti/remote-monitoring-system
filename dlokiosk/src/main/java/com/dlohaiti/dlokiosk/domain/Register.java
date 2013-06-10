package com.dlohaiti.dlokiosk.domain;

import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.dlohaiti.dlokiosk.db.ReceiptLineItemType.PRODUCT;
import static com.dlohaiti.dlokiosk.db.ReceiptLineItemType.PROMOTION;

public class Register {

    private final Clock clock;

    @Inject
    public Register(Clock clock) {
        this.clock = clock;
    }

    public Receipt checkout(ShoppingCart cart) {
        List<LineItem> lineItems = new ArrayList<LineItem>();
        for(Promotion promotion : cart.getPromotions()) {
            lineItems.add(new LineItem(promotion.getSku(), promotion.getQuantity(), new Money(BigDecimal.ZERO), PROMOTION));
        }
        List<Promotion> promotionsCopy = new ArrayList<Promotion>(cart.getPromotions());
        for(Product product : cart.getProducts()) {
            Money actualPrice = product.getPrice().times(product.getQuantity());
            for (Iterator<Promotion> it = promotionsCopy.iterator(); it.hasNext(); ) {
                Promotion promo = it.next();
                if (promo.isFor(product)) {
                    actualPrice = actualPrice.minus(promo.discountFor(product));
                    it.remove();
                    break;
                }
                if(promo.appliesToBasket()) {
                    BigDecimal totalCartDiscount = promo.discountCart(cart.getSubtotal().getAmount());
                    BigDecimal discountPerProduct = totalCartDiscount.divide(new BigDecimal(cart.getProducts().size()));
                    actualPrice = actualPrice.minus(discountPerProduct);
                    break;
                }
            }

            lineItems.add(new LineItem(product.getSku(), product.getQuantity(), actualPrice, PRODUCT));
        }
        return new Receipt(lineItems, "", clock.now(), 100, new Money(cart.getTotal()));
    }
}
