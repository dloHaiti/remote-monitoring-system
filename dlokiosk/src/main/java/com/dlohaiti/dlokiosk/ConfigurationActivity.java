package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class ConfigurationActivity extends RoboActivity {
    @InjectView(R.id.kiosk_id) private EditText kioskIdTextBox;
    @Inject private ConfigurationRepository configurationRepository;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        kioskIdTextBox.setText(configurationRepository.getKioskId());
    }

    public void save(View v) {
        String kioskId = kioskIdTextBox.getText().toString();
        configurationRepository.saveKioskId(kioskId);
        finish();
    }
}
