package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.dlohaiti.dlokiosk.db.DeliveryRepository;
import com.dlohaiti.dlokiosk.domain.Delivery;
import com.dlohaiti.dlokiosk.domain.DeliveryFactory;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class DeliveryActivity extends RoboActivity {

    @InjectView(R.id.delivery_tracking) private NumberPicker deliveryPicker;
    @Inject private DeliveryFactory factory;
    @Inject private DeliveryRepository repository;
    @Inject private ConfigurationRepository config;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_tracking);
        //TODO: guard against integer failing to parse
        deliveryPicker.setMinValue(Integer.valueOf(config.get(ConfigurationKey.DELIVERY_TRACKING_MIN)));
        deliveryPicker.setMaxValue(Integer.valueOf(config.get(ConfigurationKey.DELIVERY_TRACKING_MAX)));
        deliveryPicker.setValue(Integer.valueOf(config.get(ConfigurationKey.DELIVERY_TRACKING_DEFAULT)));
        deliveryPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    public void outForDelivery(View v) {
        createRecord(DeliveryType.OUT_FOR_DELIVERY);
    }

    public void returned(View v) {
        createRecord(DeliveryType.RETURNED);
    }

    private void createRecord(DeliveryType type) {
        Delivery delivery = factory.makeDelivery(deliveryPicker.getValue(), type);
        boolean isSuccessful = repository.save(delivery);

        ResultDialog resultDialog = new ResultDialog();
        Bundle args = new Bundle();
        args.putBoolean("isSuccessful", isSuccessful);
        resultDialog.setArguments(args);
        resultDialog.show(getFragmentManager(), "deliveryResultDialog");
    }
}
