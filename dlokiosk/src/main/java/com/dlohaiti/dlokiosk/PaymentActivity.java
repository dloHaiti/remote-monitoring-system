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
import com.dlohaiti.dlokiosk.domain.PaymentModes;
import com.dlohaiti.dlokiosk.domain.PaymentTypes;
import com.dlohaiti.dlokiosk.domain.Sponsor;
import com.dlohaiti.dlokiosk.domain.Sponsors;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import roboguice.inject.InjectView;

import java.math.BigDecimal;
import java.util.List;

public class PaymentActivity extends SaleActivity {

    public static final String PAYMENT_TYPE_POST_PAY = "Post-Pay";
    public static final String PAYMENT_TYPE_NOW = "Now";

    @Inject
    private SponsorRepository sponsorRepository;

    @InjectView(R.id.payment_mode)
    private Spinner paymentModeView;

    @InjectView(R.id.select_payee)
    private RadioGroup selectPayeeView;

    @InjectView(R.id.select_customer)
    private RadioButton selectCustomerView;

    @InjectView(R.id.select_sponsor)
    private RadioButton selectSponsorView;

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

    @InjectView(R.id.payment_type)
    private Spinner paymentTypeView;

    @InjectView(R.id.customer_amount_row)
    private LinearLayout customerAmountRowView;

    @InjectView(R.id.customer_amount)
    private EditText customerAmountView;

    @InjectView(R.id.total_price_value)
    private TextView totalPriceView;

    @InjectView(R.id.customer_payment_summary)
    private TextView customerPaymentSummaryView;

    @InjectView(R.id.customer_payment_currency)
    private TextView customerPaymentCurrencyView;

    @InjectView(R.id.sponsor_payment_summary)
    private TextView sponsorPaymentSummaryView;

    @InjectView(R.id.sponsor_payment_currency)
    private TextView sponsorPaymentCurrencyView;

    @InjectView(R.id.total_price_currency)
    private TextView totalPriceCurrencyView;

    @InjectView(R.id.amount_due_summary_row)
    private LinearLayout amountDueSummaryRowView;

    @InjectView(R.id.amount_due_summary)
    private TextView amountDueSummaryView;

    @InjectView(R.id.amount_due_currency)
    private TextView amountDueCurrencyView;

    private Sponsors sponsors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initialiseSponsorList();
        initialiseCustomerOnlyOrWithSponsorPaymentOptions();
        initialisePaymentModeList();
        initialisePaymentTypeList();
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
                    sponsorAmountView.setText("");
                    cart.setSponsorAmount(Money.ZERO);
                    updatePriceSummaryViews();
                }
            }
        });
        selectCustomerView.setChecked(!cart.isSponsorSelected);
        selectSponsorView.setChecked(cart.isSponsorSelected);
    }

    private void hideAllSponsorViews() {
        sponsorDetailsDividerView.setVisibility(View.GONE);
        selectPayeeRowView.setVisibility(View.GONE);
        sponsorRowView.setVisibility(View.GONE);
        sponsorAmountRowView.setVisibility(View.GONE);
    }

    private void initialisePriceViews() {
        initialiseSponsorAmountView();
        initialiseCustomerAmountView();
        updatePriceSummaryViews();
        initialiseCurrencyViews();
    }

    private void initialiseCurrencyViews() {
        customerPaymentCurrencyView.setText(currency());
        sponsorPaymentCurrencyView.setText(currency());
        totalPriceCurrencyView.setText(currency());
        amountDueCurrencyView.setText(currency());
    }

    private void updatePriceSummaryViews() {
        totalPriceView.setText(cart.getTotal().amountAsString());
        customerPaymentSummaryView.setText(cart.customerAmount().amountAsString());
        sponsorPaymentSummaryView.setText(cart.sponsorAmount().amountAsString());
        amountDueSummaryView.setText(cart.dueAmount().amountAsString());

        if (PAYMENT_TYPE_POST_PAY.equalsIgnoreCase(cart.paymentType())) {
            amountDueSummaryRowView.setVisibility(View.VISIBLE);
        } else if (PAYMENT_TYPE_NOW.equalsIgnoreCase(cart.paymentType())) {
            amountDueSummaryRowView.setVisibility(View.GONE);
        }
    }

    private void initialiseCustomerAmountView() {
        if (cart.customerAmount() != Money.ZERO) {
            customerAmountView.setText(cart.customerAmount().amountAsString());
        }
        customerAmountView.setFilters(new InputFilter[]{new RangeFilter(Money.ZERO, cart.getTotal())});
        customerAmountView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Money customerAmount = StringUtils.isBlank(editable)
                        ? new Money(BigDecimal.ZERO)
                        : new Money(new BigDecimal(editable.toString()));
                cart.setCustomerAmount(customerAmount);
                updatePriceSummaryViews();
            }
        });
    }

    private void initialiseSponsorAmountView() {
        if (cart.sponsorAmount() != Money.ZERO) {
            sponsorAmountView.setText(cart.sponsorAmount().amountAsString());
        }
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
                if (PAYMENT_TYPE_NOW.equalsIgnoreCase(cart.paymentType())) {
                    cart.updateCustomerAmountWithTheBalanceAmount();
                }
                updatePriceSummaryViews();
            }
        });
    }

    private void initialisePaymentModeList() {
        PaymentModes paymentModes = new PaymentModes(configurationRepository.get(ConfigurationKey.PAYMENT_MODE));
        ArrayAdapter<String> paymentTypeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,
                paymentModes);
        paymentModeView.setAdapter(paymentTypeAdapter);
        paymentModeView.setSelection(paymentModes.indexOf(cart.paymentMode()));
        paymentModeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cart.setPaymentMode((String) parent.getItemAtPosition(position));
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
        sponsorView.setSelection(sponsors.indexOf(cart.sponsor()));
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

    private void initialisePaymentTypeList() {
        List<String> paymentTypes = new PaymentTypes(configurationRepository.get(ConfigurationKey.PAYMENT_TYPE));
        ArrayAdapter<String> paymentTypeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,
                paymentTypes);
        paymentTypeView.setAdapter(paymentTypeAdapter);
        paymentTypeView.setSelection(paymentTypes.indexOf(cart.paymentType()));
        paymentTypeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String paymentType = (String) parent.getItemAtPosition(position);
                if (PAYMENT_TYPE_POST_PAY.equalsIgnoreCase(paymentType)) {
                    customerAmountRowView.setVisibility(View.VISIBLE);
                    cart.setCustomerAmount(Money.ZERO);
                } else if (PAYMENT_TYPE_NOW.equalsIgnoreCase(paymentType)) {
                    customerAmountRowView.setVisibility(View.GONE);
                    customerAmountView.setText("");
                    cart.updateCustomerAmountWithTheBalanceAmount();
                }
                cart.setPaymentType(paymentType);
                updatePriceSummaryViews();
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
