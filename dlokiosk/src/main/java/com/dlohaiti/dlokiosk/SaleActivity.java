package com.dlohaiti.dlokiosk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public abstract class SaleActivity extends RoboActivity {
    @InjectView(R.id.continue_button)
    protected ImageButton continueButton;

    protected abstract Class<? extends SaleActivity> nextActivity();

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
                                finish();
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
        if (nextActivity() == null) {
            return;
        }

        Intent intent = new Intent(getApplicationContext(), nextActivity());
        startActivity(intent);
    }
}
