package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.widget.Toast;
import com.dlohaiti.dlokiosk.client.DeliveriesClient;
import com.dlohaiti.dlokiosk.client.PostResponse;
import com.dlohaiti.dlokiosk.client.ReadingsClient;
import com.dlohaiti.dlokiosk.client.ReceiptsClient;
import com.dlohaiti.dlokiosk.db.DeliveryRepository;
import com.dlohaiti.dlokiosk.db.ReadingsRepository;
import com.dlohaiti.dlokiosk.db.ReceiptsRepository;
import com.dlohaiti.dlokiosk.domain.Delivery;
import com.dlohaiti.dlokiosk.domain.Reading;
import com.dlohaiti.dlokiosk.domain.Receipt;
import com.google.inject.Inject;
import roboguice.util.RoboAsyncTask;

import java.util.Collection;

public class ManualSyncReadingsTask extends RoboAsyncTask<String> {

    @Inject
    private ReceiptsClient receiptsClient;
    @Inject
    private DeliveriesClient deliveriesClient;
    @Inject
    private ReadingsClient readingsClient;
    @Inject
    private ReceiptsRepository receiptsRepository;
    @Inject
    private DeliveryRepository deliveriesRepository;
    @Inject
    private ReadingsRepository readingsRepository;

    private Activity activity;
    private ProgressDialog progressDialog;

    public ManualSyncReadingsTask(Activity activity) {
        super(activity.getApplicationContext());
        this.progressDialog = new ProgressDialog(activity);
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(activity);
            this.progressDialog.setIndeterminate(true);
        }

        this.progressDialog.setMessage(activity.getString(R.string.sending_readings_message));
        this.progressDialog.show();
    }

    @Override
    public String call() throws Exception {
        Collection<Receipt> receipts = receiptsRepository.list();
        Collection<Delivery> deliveries = deliveriesRepository.list();
        Collection<Reading> readings = readingsRepository.list();

        if (receipts.isEmpty() && deliveries.isEmpty() && readings.isEmpty()) {
            return activity.getString(R.string.no_readings_msg);
        }

        Failures failures = new Failures();

        for (Reading reading : readings) {
            PostResponse response = readingsClient.send(reading);
            if (response.isSuccess()) {
                readingsRepository.remove(reading);
            } else {
                failures.add(new Failure(FailureKind.READING, response.getErrors()));
            }
        }

        for (Receipt receipt : receipts) {
            PostResponse response = receiptsClient.send(receipt);
            if (response.isSuccess()) {
                receiptsRepository.remove(receipt);
            } else {
                failures.add(new Failure(FailureKind.RECEIPT, response.getErrors()));
            }
        }

        for (Delivery delivery : deliveries) {
            PostResponse response = deliveriesClient.send(delivery);
            if (response.isSuccess()) {
                deliveriesRepository.remove(delivery);
            } else {
                failures.add(new Failure(FailureKind.DELIVERY, response.getErrors()));
            }
        }

        if (failures.isNotEmpty()) {
            Integer readingCount = failures.countFor(FailureKind.READING);
            Integer receiptCount = failures.countFor(FailureKind.RECEIPT);
            Integer deliveryCount = failures.countFor(FailureKind.DELIVERY);
            return activity.getString(R.string.send_error_msg, readingCount, receiptCount, deliveryCount);
        }
        return activity.getString(R.string.send_success_msg, readings.size(), receipts.size(), deliveries.size());
    }

    private void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onSuccess(String message) {
        showMessage(message);
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        Toast.makeText(getContext(), "PROBLEM: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onFinally() throws RuntimeException {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

}
