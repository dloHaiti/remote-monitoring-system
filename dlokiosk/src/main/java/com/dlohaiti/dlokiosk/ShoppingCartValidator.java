package com.dlohaiti.dlokiosk;

import com.dlohaiti.dlokiosk.domain.Money;
import com.dlohaiti.dlokiosk.domain.ShoppingCartNew;

public class ShoppingCartValidator {
    public ValidationResult validate(ShoppingCartNew cart) {
        if (cart.isSponsorSelected() && !Money.hasValue(cart.sponsorAmount())) {
            return new ValidationResult(true, R.string.error_enter_sponsor_amount);
        } else if (PaymentActivity.PAYMENT_TYPE_POST_PAY.equalsIgnoreCase(cart.paymentType()) && !Money.hasValue(cart.customerAmount())) {
            return new ValidationResult(true, R.string.error_enter_customer_amount);
        } else if (cart.sponsorAmount().plus(cart.customerAmount()).isGreaterThan(cart.getDiscountedTotal())) {
            return new ValidationResult(true, R.string.error_sponsor_customer_amount_greater_than_total);
        }
        return new ValidationResult(false);
    }
}
