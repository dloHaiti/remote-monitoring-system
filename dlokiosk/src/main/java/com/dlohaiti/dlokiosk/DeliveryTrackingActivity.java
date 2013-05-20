package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import com.dlohaiti.dlokiosk.db.DeliveryTrackingRepository;
import com.dlohaiti.dlokiosk.domain.Delivery;
import com.dlohaiti.dlokiosk.domain.DeliveryFactory;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class DeliveryTrackingActivity extends RoboActivity {

    @InjectView(R.id.delivery_tracking) private NumberPicker deliveryTrackingPicker;
    @Inject private DeliveryFactory factory;
    @Inject private DeliveryTrackingRepository repository;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_tracking);
        //TODO: pull these from configuration repository
        deliveryTrackingPicker.setMinValue(0);
        deliveryTrackingPicker.setMaxValue(24);
        deliveryTrackingPicker.setValue(24);
        deliveryTrackingPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    public void outForDelivery(View v) {
        createRecord(DeliveryTrackingType.OUT_FOR_DELIVERY);
    }

    public void returned(View v) {
        createRecord(DeliveryTrackingType.RETURNED);
    }

    private void createRecord(DeliveryTrackingType type) {
        Delivery delivery = factory.makeDelivery(deliveryTrackingPicker.getValue(), type);
        boolean isSuccessful = repository.save(delivery);

        ResultDialog resultDialog = new ResultDialog();
        Bundle args = new Bundle();
        args.putBoolean("isSuccessful", isSuccessful);
        resultDialog.setArguments(args);
        resultDialog.show(getFragmentManager(), "deliveryResultDialog");
    }
}
