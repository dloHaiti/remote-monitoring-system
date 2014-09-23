package com.dlohaiti.dlokiosk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.SponsorRepository;
import com.dlohaiti.dlokiosk.domain.Money;
import com.dlohaiti.dlokiosk.domain.PaymentTypes;
import com.dlohaiti.dlokiosk.domain.Sponsor;
import com.dlohaiti.dlokiosk.domain.Sponsors;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import roboguice.inject.InjectView;

import java.math.BigDecimal;

public class PaymentActivity extends SaleActivity {

    public static final String POST_PAY_STRING = "Post-Pay";
    @Inject
    private SponsorRepository sponsorRepository;

    @InjectView(R.id.payment_type)
    private Spinner paymentTypeView;

    @InjectView(R.id.select_payee)
    private RadioGroup selectPayeeView;

    @InjectView(R.id.select_customer)
    private RadioButton selectCustomerView;

    @InjectView(R.id.sponsor_details_divider)
    private ImageView sponsorDetailsDividerView;

    @InjectView(R.id.select_payee_row)
    private LinearLayout selectPayeeRowView;

    @InjectView(R.id.sponsor_row)
    private LinearLayout sponsorRowView;

    @InjectView(R.id.sponsor_amount_row)
    private LinearLayout sponsorAmountRowView;

    @InjectView(R.id.sponsor)
    private Spinner sponsorView;

    @InjectView(R.id.sponsor_amount)
    private EditText sponsorAmountView;

    @InjectView(R.id.payment_mode)
    private Spinner paymentModeView;

    @InjectView(R.id.amount_due_row)
    private LinearLayout amountDueRowView;

    @InjectView(R.id.amount_due)
    private EditText amountDueView;

    @InjectView(R.id.total_price_value)
    private TextView totalPriceView;

    @InjectView(R.id.customer_payment_value)
    private TextView customerPaymentValueView;

    @InjectView(R.id.customer_payment_currency)
    private TextView customerPaymentCurrencyView;

    @InjectView(R.id.sponsor_payment_value)
    private TextView sponsorPaymentValueView;

    @InjectView(R.id.sponsor_payment_currency)
    private TextView sponsorPaymentCurrencyView;

    @InjectView(R.id.total_price_currency)
    private TextView totalPriceCurrencyView;

    @InjectView(R.id.amount_due_value)
    private TextView amountDueValueView;

    @InjectView(R.id.amount_due_currency)
    private TextView amountDueCurrencyView;

    private Sponsors sponsors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initialiseSponsorList();
        initialiseCustomerOnlyOrWithSponsorPaymentOptions();
        initialisePaymentTypeList();
        initialisePaymentModeList();
        initialisePriceViews();
    }

    private void initialiseCustomerOnlyOrWithSponsorPaymentOptions() {
        if (sponsors.isEmpty()) {
            hideAllSponsorViews();
            return;
        }
        selectPayeeView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int selectedRadioButtonId) {
                View selectedRadioButton = radioGroup.findViewById(selectedRadioButtonId);
                if (selectedRadioButton.getId() == R.id.select_sponsor) {
                    cart.isSponsorSelected = true;
                    sponsorRowView.setVisibility(View.VISIBLE);
                    sponsorAmountRowView.setVisibility(View.VISIBLE);
                } else {
                    cart.isSponsorSelected = false;
                    cart.setSponsor(null);
                    sponsorRowView.setVisibility(View.GONE);
                    sponsorAmountRowView.setVisibility(View.GONE);
                }
            }
        });
        selectCustomerView.setChecked(true);
    }

    private void hideAllSponsorViews() {
        sponsorDetailsDividerView.setVisibility(View.GONE);
        selectPayeeRowView.setVisibility(View.GONE);
        sponsorRowView.setVisibility(View.GONE);
        sponsorAmountRowView.setVisibility(View.GONE);
    }

    private void initialisePriceViews() {
        totalPriceView.setText(String.valueOf(cart.getTotal().getAmount()));
        customerPaymentValueView.setText(String.valueOf(cart.getTotal().getAmount()));
        cart.setCustomerAmount(cart.getTotal());
        initialiseSponsorAmountView();
        initialiseAmountDueView();

        customerPaymentCurrencyView.setText(currency());
        sponsorPaymentCurrencyView.setText(currency());
        totalPriceCurrencyView.setText(currency());
        amountDueCurrencyView.setText(currency());
    }

    private void initialiseAmountDueView() {
        amountDueView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Money dueAmount = StringUtils.isBlank(editable)
                        ? new Money(BigDecimal.ZERO)
                        : new Money(new BigDecimal(editable.toString()));
                cart.setDueAmount(dueAmount);
                amountDueValueView.setText(String.valueOf(cart.dueAmount().getAmount()));
            }
        });
        amountDueView.setFilters(new InputFilter[]{new RangeFilter(Money.ZERO, cart.getTotal())});
    }

    private void initialiseSponsorAmountView() {
        sponsorAmountView.setFilters(new InputFilter[]{new RangeFilter(Money.ZERO, cart.getTotal())});
        sponsorAmountView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Money sponsorAmount = StringUtils.isBlank(editable)
                        ? new Money(BigDecimal.ZERO)
                        : new Money(new BigDecimal(editable.toString()));
                cart.setSponsorAmount(sponsorAmount);
                sponsorPaymentValueView.setText(String.valueOf(cart.sponsorAmount().getAmount()));
                customerPaymentValueView.setText(String.valueOf(cart.customerAmount().getAmount()));
            }
        });
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
        sponsors = sponsorRepository.findByCustomerId(cart.customerAccount().getId());
        ArrayAdapter<Sponsor> adapter = new ArrayAdapter<Sponsor>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,
                sponsors);
        sponsorView.setAdapter(adapter);
        sponsorView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cart.setSponsor((Sponsor) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initialisePaymentModeList() {
        ArrayAdapter<String> paymentModeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,
                getResources().getStringArray(R.array.payment_modes));
        paymentModeView.setAdapter(paymentModeAdapter);
        paymentModeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String paymentMode = (String) parent.getItemAtPosition(position);
                amountDueRowView.setVisibility(POST_PAY_STRING.equalsIgnoreCase(paymentMode) ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
