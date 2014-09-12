package com.dlohaiti.dlokiosk;

import android.os.Bundle;

public class PaymentActivity extends SaleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }

    @Override
    protected Class<? extends SaleActivity> nextActivity() {
        return null;
    }
}
