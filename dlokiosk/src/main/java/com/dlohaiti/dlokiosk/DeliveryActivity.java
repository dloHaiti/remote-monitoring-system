package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.dlohaiti.dlokiosk.db.DeliveryAgentRepository;
import com.dlohaiti.dlokiosk.db.DeliveryRepository;
import com.dlohaiti.dlokiosk.domain.Delivery;
import com.dlohaiti.dlokiosk.domain.DeliveryAgent;
import com.dlohaiti.dlokiosk.domain.DeliveryFactory;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class DeliveryActivity extends RoboActivity {

    @InjectView(R.id.delivery_tracking) private NumberPicker deliveryPicker;
    @InjectView(R.id.agent_spinner) private Spinner agentSpinner;
    @Inject private DeliveryFactory factory;
    @Inject private DeliveryRepository repository;
    @Inject private DeliveryAgentRepository deliveryAgentRepository;
    @Inject private ConfigurationRepository config;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_tracking);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for(DeliveryAgent agent : deliveryAgentRepository.list()) {
            adapter.add(agent.getName());
        }
        agentSpinner.setAdapter(adapter);
        deliveryPicker.setMinValue(config.getInt(ConfigurationKey.DELIVERY_TRACKING_MIN));
        deliveryPicker.setMaxValue(config.getInt(ConfigurationKey.DELIVERY_TRACKING_MAX));
        deliveryPicker.setValue(config.getInt(ConfigurationKey.DELIVERY_TRACKING_DEFAULT));
        deliveryPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    public void outForDelivery(View v) {
        createRecord(DeliveryType.OUT_FOR_DELIVERY);
    }

    public void returned(View v) {
        createRecord(DeliveryType.RETURNED);
    }

    private void createRecord(DeliveryType type) {
        Delivery delivery = factory.makeDelivery(deliveryPicker.getValue(), type, (String)agentSpinner.getSelectedItem());
        boolean isSuccessful = repository.save(delivery);

        ResultDialog resultDialog = new ResultDialog();
        Bundle args = new Bundle();
        args.putBoolean("isSuccessful", isSuccessful);
        resultDialog.setArguments(args);
        resultDialog.show(getFragmentManager(), "deliveryResultDialog");
    }
}
