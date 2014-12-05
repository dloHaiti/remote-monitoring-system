package com.dlohaiti.dlokiosknew;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dlohaiti.dlokiosknew.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosknew.db.SponsorRepository;
import com.dlohaiti.dlokiosknew.domain.CustomerAccounts;
import com.dlohaiti.dlokiosknew.domain.Sponsor;
import com.google.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class SponsorFormActivity extends RoboActivity {
    @InjectView(R.id.accounts)
    protected MultiSelectSpinner accounts;

    @InjectView(R.id.sponsor_name)
    protected EditText sponsorName;

    @InjectView(R.id.sponsor_contact_name)
    protected EditText contactName;

    @InjectView(R.id.customer_phone)
    protected EditText phoneNumber;

    @InjectResource(R.string.mandatory_field)
    private String mandatoryFieldMessage;

    @Inject
    CustomerAccountRepository customerAccountRepository;
    @Inject
    SponsorRepository sponsorRepository;

    @InjectResource(R.string.saved_message)
    private String savedMessage;
    @InjectResource(R.string.error_not_saved_message)
    private String errorNotSavedMessage;

    private CustomerAccounts customerAccounts;
    private Sponsor sponsor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sponsor_form);
        loadCustomers();
        fillSponsorDetails();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SponsorFormActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fillSponsorDetails() {
        String sponsorId = getIntent().getStringExtra("sponsor_id");
        if (StringUtils.isEmpty(sponsorId)) {
            sponsorId = null;
            return;
        }
        sponsor = sponsorRepository.findById(sponsorId);
        if (sponsor == null) {
            return;
        }
        sponsorName.setText(sponsor.getName());
        contactName.setText(sponsor.getContactName());
        phoneNumber.setText(sponsor.getPhoneNumber());
        CustomerAccounts sponsorsAccounts = new CustomerAccounts(customerAccountRepository.findBySponsorId(sponsor.getId()));
        accounts.setSelection(sponsorsAccounts.getContactNames());

    }

    private void loadCustomers() {
        customerAccounts = new CustomerAccounts(customerAccountRepository.findAll());
        accounts.setItems(customerAccounts.getContactNames());
    }

    public void onCancel(View view) {
        Intent intent = new Intent(SponsorFormActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onSave(View view) {
        if (!validateSponsorForm()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Save")
                    .setMessage("There are validation errors")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }

                    })
                    .show();
            return;
        }

        boolean successful = (sponsor == null) ? createNewSponsor() : updateSponsor(sponsor);
        if (successful) {
            Toast.makeText(this, savedMessage, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, errorNotSavedMessage, Toast.LENGTH_LONG).show();
        }
    }

    private boolean updateSponsor(Sponsor sponsor) {
        sponsor.setName(sponsorName.getText().toString());
        sponsor.setContactName(contactName.getText().toString());
        sponsor.setPhoneNumber(phoneNumber.getText().toString());
        sponsor.withAccounts(customerAccounts.getAccountsFromNames(accounts.getSelectedStrings()));
        return sponsorRepository.save(sponsor);
    }

    private boolean createNewSponsor() {
        Sponsor s=new Sponsor();
        return updateSponsor(s);
    }

    private boolean validateSponsorForm() {
        boolean result = true;
        if (sponsorName.getText().toString().isEmpty()) {
            sponsorName.setError(mandatoryFieldMessage);
            result = false;
        } else {
            sponsorName.setError(null);
            Sponsor existingSponsor = sponsorRepository.findByName(sponsorName.getText().toString());
            if (existingSponsor != null) {
                if (sponsor == null || !sponsor.getId().equalsIgnoreCase(existingSponsor.getId())) {
                    sponsorName.setError("Already exist");
                    result = false;
                }
            }
        }
        return result;
    }

    public void onBack(View view) {
        this.onBackPressed();
    }
}