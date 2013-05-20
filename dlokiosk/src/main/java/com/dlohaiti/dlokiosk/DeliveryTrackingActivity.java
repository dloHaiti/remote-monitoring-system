package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.dlohaiti.dlokiosk.db.DeliveryTrackingRepository;
import com.dlohaiti.dlokiosk.domain.Delivery;
import com.dlohaiti.dlokiosk.domain.DeliveryFactory;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class DeliveryTrackingActivity extends RoboActivity {

    @InjectView(R.id.delivery_tracking) private EditText deliveryTrackingTextBox;
    @Inject private DeliveryFactory factory;
    @Inject private DeliveryTrackingRepository repository;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_tracking);
    }

    public void outForDelivery(View v) {
        createRecord(DeliveryTrackingType.OUT_FOR_DELIVERY);
    }

    public void returned(View v) {
        createRecord(DeliveryTrackingType.RETURNED);
    }

    private void createRecord(DeliveryTrackingType outForDelivery) {
        Integer unitsDelivered = Integer.valueOf(deliveryTrackingTextBox.getText().toString());
        Delivery delivery = factory.makeDelivery(unitsDelivered, outForDelivery);
        repository.save(delivery);
        new DataSavedDialog().show(getFragmentManager(), "dataSavedDialog");
    }
}
