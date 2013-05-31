package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import com.dlohaiti.dlokiosk.client.DeliveriesClient;
import com.dlohaiti.dlokiosk.client.ReceiptsClient;
import com.dlohaiti.dlokiosk.db.DeliveryRepository;
import com.dlohaiti.dlokiosk.db.ReceiptsRepository;
import com.dlohaiti.dlokiosk.domain.Delivery;
import com.dlohaiti.dlokiosk.domain.Receipt;
import com.google.inject.Inject;
import roboguice.util.RoboAsyncTask;

import java.util.Collection;

public class ManualSyncReadingsTask extends RoboAsyncTask<String> {

    @Inject private ReceiptsClient receiptsClient;
    @Inject private DeliveriesClient deliveriesClient;
    @Inject private ReceiptsRepository receiptsRepository;
    @Inject private DeliveryRepository deliveriesRepository;

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

        if (receipts.isEmpty() && deliveries.isEmpty()) {
            return activity.getString(R.string.no_readings_msg);
        }

        boolean atLeastOneReadingFailed = false;
        boolean atLeastOneSaleFailed = false;
        boolean atLeastOneDeliveryFailed = false;

        for (Receipt receipt : receipts) {
            boolean sendOk = receiptsClient.send(receipt);
            if(sendOk) {
                receiptsRepository.remove(receipt);
            } else {
                atLeastOneSaleFailed = true;
            }
        }

        for(Delivery delivery: deliveries) {
            boolean sendOk = deliveriesClient.send(delivery);
            if(sendOk) {
                deliveriesRepository.remove(delivery);
            } else {
                atLeastOneDeliveryFailed = true;
            }
        }

        if (atLeastOneReadingFailed || atLeastOneSaleFailed || atLeastOneDeliveryFailed) {
            return activity.getString(R.string.send_error_msg);
        }
        return activity.getString(R.string.send_success_msg, 0, receipts.size(), deliveries.size());
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
