package com.dlohaiti.dlokiosk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.domain.PaymentTypes;
import roboguice.inject.InjectView;

import static java.util.Arrays.asList;

public class PaymentActivity extends SaleActivity {

    @InjectView(R.id.payment_type)
    private Spinner paymentTypeView;

    @InjectView(R.id.select_payee)
    private RadioGroup selectPayeeView;

    @InjectView(R.id.select_sponsor)
    private RadioButton selectSponsorView;

    @InjectView(R.id.select_customer)
    private RadioButton selectCustomerView;

    @InjectView(R.id.sponsor_row)
    private LinearLayout sponsorRowView;

    @InjectView(R.id.sponsor_amount_row)
    private LinearLayout sponsorAmountRowView;

    @InjectView(R.id.sponsor)
    private Spinner sponsorView;

    @InjectView(R.id.payment_mode)
    private Spinner paymentModeView;

    @InjectView(R.id.total_price_value)
    private TextView totalPriceView;

    @InjectView(R.id.customer_payment_currency)
    private TextView customerPaymentCurrencyView;

    @InjectView(R.id.sponsor_payment_currency)
    private TextView sponsorPaymentCurrencyView;

    @InjectView(R.id.total_price_currency)
    private TextView totalPriceCurrencyView;

    @InjectView(R.id.amount_due_currency)
    private TextView amountDueCurrencyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initialiseCustomerOnlyOrWithSponsorPaymentOptions();
        initialiseSponsorList();
        initialisePaymentTypeList();
        initialisePaymentModeList();
        initialisePriceViews();
    }

    private void initialiseCustomerOnlyOrWithSponsorPaymentOptions() {
        selectPayeeView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int selectedRadioButtonId) {
                View selectedRadioButton = radioGroup.findViewById(selectedRadioButtonId);
                cart.isSponsorSelected = selectedRadioButton.getId() == R.id.select_sponsor;
                int sponsorInformationVisibility = selectedRadioButton.getId() == R.id.select_sponsor
                        ? View.VISIBLE : View.GONE;
                sponsorRowView.setVisibility(sponsorInformationVisibility);
                sponsorAmountRowView.setVisibility(sponsorInformationVisibility);
            }
        });
        selectCustomerView.setChecked(true);
    }

    private void initialisePriceViews() {
        totalPriceView.setText(String.valueOf(cart.getTotal()));

        customerPaymentCurrencyView.setText(currency());
        sponsorPaymentCurrencyView.setText(currency());
        totalPriceCurrencyView.setText(currency());
        amountDueCurrencyView.setText(currency());
    }

    private void initialisePaymentTypeList() {
        ArrayAdapter<String> paymentTypeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,
                new PaymentTypes(configurationRepository.get(ConfigurationKey.PAYMENT_TYPE)));
        paymentTypeView.setAdapter(paymentTypeAdapter);
        paymentTypeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cart.setPaymentType((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initialiseSponsorList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,
                asList("", "Sponsor 1", "Sponsor 2"));
        sponsorView.setAdapter(adapter);
    }

    private void initialisePaymentModeList() {
        ArrayAdapter<String> paymentModeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,
                getResources().getStringArray(R.array.payment_modes));
        paymentModeView.setAdapter(paymentModeAdapter);
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
