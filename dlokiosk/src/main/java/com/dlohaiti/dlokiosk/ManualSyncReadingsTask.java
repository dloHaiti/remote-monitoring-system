package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import com.dlohaiti.dlokiosk.client.ReadingClient;
import com.dlohaiti.dlokiosk.client.SalesClient;
import com.dlohaiti.dlokiosk.db.MeasurementRepository;
import com.dlohaiti.dlokiosk.domain.Reading;
import com.dlohaiti.dlokiosk.domain.Receipt;
import com.google.inject.Inject;
import roboguice.util.RoboAsyncTask;

import java.util.Collection;

public class ManualSyncReadingsTask extends RoboAsyncTask<String> {

    @Inject private MeasurementRepository measurementRepository;
    @Inject private ReadingClient readingClient;
    @Inject private SalesClient salesClient;
    @Inject private ReceiptsRepository receiptsRepository;
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
        Collection<Reading> readings = measurementRepository.getReadings();
        Collection<Receipt> receipts = receiptsRepository.list();
        if (readings.isEmpty() && receipts.isEmpty()) {
            return activity.getString(R.string.no_readings_msg);
        }
        boolean atLeastOneReadingFailed = false;
        boolean atLeastOneSaleFailed = false;
        for (Reading reading: readings) {
            if (!readingClient.send(reading)) {
                atLeastOneReadingFailed = true;
            }
        }
        for (Receipt receipt : receipts) {
            if(!salesClient.send(receipt)) {
                atLeastOneSaleFailed = true;
            }
        }
        if (atLeastOneReadingFailed || atLeastOneSaleFailed) {
            return activity.getString(R.string.send_error_msg);
        }
        return activity.getString(R.string.send_success_msg, readings.size(), receipts.size());
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
        super.onException(e);  //TODO
    }

    @Override
    protected void onFinally() throws RuntimeException {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }
}
