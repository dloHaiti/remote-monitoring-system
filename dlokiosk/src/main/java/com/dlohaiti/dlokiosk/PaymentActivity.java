package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.SponsorRepository;
import com.dlohaiti.dlokiosk.domain.*;
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

    @InjectView(R.id.sales_channel)
    private TextView salesChannelView;

    @InjectView(R.id.customer_account)
    private TextView customerAccountView;

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

    @InjectView(R.id.delivery_time_row)
    private LinearLayout deliveryTimeRowView;

    @InjectView(R.id.delivery_time)
    private Spinner deliveryTimeView;

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

    @InjectView(R.id.sponsor_payment_label)
    private TextView sponsorPaymentLabel;

    @InjectView(R.id.sponsor_payment_amount)
    private LinearLayout sponsorPaymentAmount;

    private Sponsors sponsors;

    @Inject
    private ShoppingCartValidator cartValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initialiseOrderSummary();
        initialiseSponsorList();
        initialiseCustomerOnlyOrWithSponsorPaymentOptions();
        initialisePaymentModeList();
        initialisePaymentTypeList();
        initialiseDeliveryTimeList();
        initialisePriceViews();
    }

    private void initialiseOrderSummary() {
        salesChannelView.setText(cart.salesChannel().name());
        customerAccountView.setText(cart.customerAccount().getContactName());
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
                    cart.setIsSponsorSelected(true);
                    sponsorRowView.setVisibility(View.VISIBLE);
                    sponsorAmountRowView.setVisibility(View.VISIBLE);
                } else {
                    cart.setIsSponsorSelected(false);
                    cart.setSponsor(null);
                    sponsorRowView.setVisibility(View.GONE);
                    sponsorAmountRowView.setVisibility(View.GONE);
                    sponsorAmountView.setText("");
                    cart.setSponsorAmount(Money.ZERO);
                    updatePriceSummaryViews();
                }
            }
        });
        selectCustomerView.setChecked(!cart.isSponsorSelected());
        selectSponsorView.setChecked(cart.isSponsorSelected());
    }

    private void hideAllSponsorViews() {
        sponsorDetailsDividerView.setVisibility(View.GONE);
        selectPayeeRowView.setVisibility(View.GONE);
        sponsorRowView.setVisibility(View.GONE);
        sponsorAmountRowView.setVisibility(View.GONE);
        sponsorPaymentLabel.setVisibility(View.GONE);
        sponsorPaymentAmount.setVisibility(View.GONE);
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
        totalPriceView.setText(cart.getDiscountedTotal().amountAsString());
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
        customerAmountView.setFilters(new InputFilter[]{new RangeFilter(Money.ZERO, cart.getDiscountedTotal())});
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
        sponsorAmountView.setFilters(new InputFilter[]{new RangeFilter(Money.ZERO, cart.getDiscountedTotal())});
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
                    customerAmountView.setText("0");
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

    private void initialiseDeliveryTimeList() {
        if (!cart.salesChannel().delayedDelivery()) {
            deliveryTimeRowView.setVisibility(View.GONE);
            return;
        }

        List<String> deliveryTimes = new DeliveryTimes(configurationRepository.get(ConfigurationKey.DELIVERY_TIME));
        ArrayAdapter<String> deliveryTimeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.layout_spinner_dropdown_item,
                deliveryTimes);
        deliveryTimeView.setAdapter(deliveryTimeAdapter);
        deliveryTimeView.setSelection(deliveryTimes.indexOf(cart.deliveryTime()));
        deliveryTimeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String deliveryTime = (String) parent.getItemAtPosition(position);
                cart.setDeliveryTime(deliveryTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onContinue(final View view) {
        ValidationResult validateResult = cartValidator.validate(cart);
        if (validateResult.hasError()) {
            new AlertDialog.Builder(this)
                    .setMessage(validateResult.message())
                    .setCancelable(true)
                    .setNeutralButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                }
                            })
                    .show();
            return;
        }
        new AlertDialog.Builder(this)
                .setMessage(R.string.sale_confirm_dialog_message)
                .setCancelable(false)
                .setNeutralButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                cart.checkout();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                .show();
    }

    private void callSuperOnContinue(View view) {
        super.onContinue(view);
    }

    @Override
    protected Class<? extends Activity> nextActivity() {
        return MainActivity.class;
    }
}
