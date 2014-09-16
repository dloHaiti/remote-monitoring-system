package com.dlohaiti.dlokiosk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import roboguice.inject.InjectView;

import static java.util.Arrays.asList;

public class PaymentActivity extends SaleActivity {

    @InjectView(R.id.payment_type)
    private Spinner paymentType;

    @InjectView(R.id.select_sponsor)
    private Spinner selectSponsor;

    @InjectView(R.id.payment_mode)
    private Spinner paymentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,
                asList("", "Sponsor 1", "Sponsor 2"));
        selectSponsor.setAdapter(adapter);

        ArrayAdapter<String> paymentTypeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,
                getResources().getStringArray(R.array.payment_types));
        paymentType.setAdapter(paymentTypeAdapter);

        ArrayAdapter<String> paymentModeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,
                getResources().getStringArray(R.array.payment_modes));
        paymentMode.setAdapter(paymentModeAdapter);
    }

    @Override
    public void onContinue(final View view) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.sale_confirm_dialog_message)
                .setCancelable(false)
                .setNeutralButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                callBaseOnContinue(view);
                            }
                        })
                .show();

        super.onContinue(view);
    }

    private void callBaseOnContinue(View view) {
        super.onContinue(view);
    }

    @Override
    protected Class<? extends SaleActivity> nextActivity() {
        return null;
    }
}
