package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AccountsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
    }

    public void onCancel(View view) {
        finish();
    }

    public void moveToCustomerAccountScreen(View view) {
        startActivity(new Intent(this, CustomerAccountsActivity.class));
    }

    public void moveToSponsorsScreen(View view) {
        startActivity(new Intent(this, SponsorsActivity.class));
    }
}