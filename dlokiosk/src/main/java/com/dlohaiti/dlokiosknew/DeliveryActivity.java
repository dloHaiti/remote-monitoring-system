package com.dlohaiti.dlokiosknew;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import com.dlohaiti.dlokiosknew.db.ConfigurationKey;
import com.dlohaiti.dlokiosknew.db.ConfigurationRepository;
import com.dlohaiti.dlokiosknew.db.DeliveryAgentRepository;
import com.dlohaiti.dlokiosknew.db.DeliveryRepository;
import com.dlohaiti.dlokiosknew.domain.Delivery;
import com.dlohaiti.dlokiosknew.domain.DeliveryAgent;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.Date;

public class DeliveryActivity extends RoboActivity {

    @InjectView(R.id.delivery_tracking)
    private NumberPicker deliveryPicker;
    @InjectView(R.id.agent_spinner)
    private Spinner agentSpinner;
    @Inject
    private DeliveryRepository repository;
    @Inject
    private DeliveryAgentRepository deliveryAgentRepository;
    @Inject
    private ConfigurationRepository config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_tracking);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (DeliveryAgent agent : deliveryAgentRepository.findAll()) {
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
        Delivery delivery = new Delivery(deliveryPicker.getValue(), type, new Date(), (String) agentSpinner.getSelectedItem());
        boolean isSuccessful = repository.save(delivery);

        ResultDialog resultDialog = new ResultDialog();
        Bundle args = new Bundle();
        args.putBoolean("isSuccessful", isSuccessful);
        resultDialog.setArguments(args);
        resultDialog.show(getFragmentManager(), "deliveryResultDialog");
    }
}
