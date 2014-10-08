package com.dlohaiti.dlokiosk;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.dlohaiti.dlokiosk.domain.ShoppingCartNew;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public abstract class SaleActivity extends RoboActivity {
    @InjectView(R.id.continue_button)
    protected ImageButton continueButton;

    @Inject
    protected ConfigurationRepository configurationRepository;

    @Inject
    protected ShoppingCartNew cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.cancel_sale_confirm_dialog_message)
                        .setTitle(R.string.cancel_sale_confirm_dialog_title)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes_button_label,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        cart.clear();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton(R.string.no_button_label,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                    }
                                })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected String currency() {
        return configurationRepository.get(ConfigurationKey.CURRENCY);
    }

    protected abstract Class<? extends Activity> nextActivity();

    protected void showNoConfigurationAlert() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.no_configuration_error_message)
                .setTitle(R.string.no_configuration_error_title)
                .setCancelable(false)
                .setNeutralButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                finish();
                            }
                        })
                .show();
    }

    public void onCancel(View view) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.cancel_sale_confirm_dialog_message)
                .setTitle(R.string.cancel_sale_confirm_dialog_title)
                .setCancelable(false)
                .setPositiveButton(R.string.yes_button_label,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                cart.clear();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton(R.string.no_button_label,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .show();
    }

    public void onBack(View view) {
        finish();
    }

    public void onContinue(View view) {
        navigateToNextActivity();
    }

    private void navigateToNextActivity() {
        if (nextActivity() == null) {
            return;
        }
        Intent intent = new Intent(getApplicationContext(), nextActivity());
        startActivity(intent);
    }

    protected void continueWhenCartIsNotEmpty() {
        if (cart.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.no_products_in_cart_error_message)
                    .setTitle(R.string.no_products_in_cart_error_title)
                    .setCancelable(false)
                    .setNeutralButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                }
                            })
                    .show();
        } else {
            navigateToNextActivity();
        }
    }
}
