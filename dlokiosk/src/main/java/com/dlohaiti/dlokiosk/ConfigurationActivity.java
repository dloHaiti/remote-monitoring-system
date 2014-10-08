package com.dlohaiti.dlokiosk;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dlohaiti.dlokiosk.client.ConfigurationClient;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.SponsorRepository;
import com.google.inject.Inject;

import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class ConfigurationActivity extends RoboActivity {
    @InjectView(R.id.kiosk_id)
    private EditText kioskIdTextBox;
    @InjectView(R.id.kiosk_password)
    private EditText kioskPasswordTextBox;
    @InjectView(R.id.server_url)
    private EditText serverUrl;
    @InjectView(R.id.last_updated)
    private TextView lastUpdated;
    @Inject
    private ConfigurationRepository config;
    @Inject
    private ConfigurationClient client;

    @Inject
    private CustomerAccountRepository customerAccountRepository;
    @Inject
    private SponsorRepository sponsorRepository;

    @InjectResource(R.string.do_manual_sync_msg)
    private String do_manual_sync_msg;

    @InjectResource(R.string.saved_message)
    private String saved_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_configuration);
        kioskIdTextBox.setText(config.get(ConfigurationKey.KIOSK_ID));
        kioskPasswordTextBox.setText(config.get(ConfigurationKey.KIOSK_PASSWORD));
        serverUrl.setText(config.get(ConfigurationKey.SERVER_URL));
        lastUpdated.setText(ISODateTimeFormat.basicDate().parseLocalDate(config.get(ConfigurationKey.LAST_UPDATE)).toString("dd-MMM-yy"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void save(View v) {
        String kioskId = kioskIdTextBox.getText().toString();
        String kioskPassword = kioskPasswordTextBox.getText().toString();
        String serverHome = serverUrl.getText().toString();
        config.save(ConfigurationKey.KIOSK_ID, kioskId);
        config.save(ConfigurationKey.KIOSK_PASSWORD, kioskPassword);
        config.save(ConfigurationKey.SERVER_URL, serverHome);
//        finish();
        Toast.makeText(this, saved_message, Toast.LENGTH_LONG).show();
    }

    public void updateConfiguration(View v) {
        if (customerAccountRepository.getNonSyncAccounts().size() == 0 || !sponsorRepository.isNotEmpty()) {
            new PullConfigurationTask(this).execute();
            config.save(ConfigurationKey.LAST_UPDATE, new LocalDate().toString(ISODateTimeFormat.basicDate()));
        } else {
            Toast.makeText(this, do_manual_sync_msg, Toast.LENGTH_LONG).show();
        }
    }

    public void doManualSync(View view) {
        new ManualSyncReadingsTask(this).execute();
    }
}
