package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.client.ConfigurationClient;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;
import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class ConfigurationActivity extends RoboActivity {
    @InjectView(R.id.kiosk_id) private EditText kioskIdTextBox;
    @InjectView(R.id.kiosk_password) private EditText kioskPasswordTextBox;
    @InjectView(R.id.server_url) private EditText serverUrl;
    @InjectView(R.id.last_updated) private TextView lastUpdated;
    @Inject private ConfigurationRepository config;
    @Inject private ConfigurationClient client;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        kioskIdTextBox.setText(config.get(ConfigurationKey.KIOSK_ID));
        kioskPasswordTextBox.setText(config.get(ConfigurationKey.KIOSK_PASSWORD));
        serverUrl.setText(config.get(ConfigurationKey.SERVER_URL));
        lastUpdated.setText(ISODateTimeFormat.basicDate().parseLocalDate(config.get(ConfigurationKey.LAST_UPDATE)).toString("dd-MMM-yy"));
    }

    public void save(View v) {
        String kioskId = kioskIdTextBox.getText().toString();
        String kioskPassword = kioskPasswordTextBox.getText().toString();
        String serverHome = serverUrl.getText().toString();
        config.save(ConfigurationKey.KIOSK_ID, kioskId);
        config.save(ConfigurationKey.KIOSK_PASSWORD, kioskPassword);
        config.save(ConfigurationKey.SERVER_URL, serverHome);
        finish();
    }

    public void updateConfiguration(View v) {
        new PullConfigurationTask(this).execute();
        config.save(ConfigurationKey.LAST_UPDATE, new LocalDate().toString(ISODateTimeFormat.basicDate()));
    }
}
