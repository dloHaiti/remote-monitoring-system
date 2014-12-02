package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.dlohaiti.dlokiosk.client.CustomerAccountClient;
import com.dlohaiti.dlokiosk.client.DeliveriesClient;
import com.dlohaiti.dlokiosk.client.PaymentHistoryClient;
import com.dlohaiti.dlokiosk.client.PostResponse;
import com.dlohaiti.dlokiosk.client.ReadingsClient;
import com.dlohaiti.dlokiosk.client.ReceiptsClient;
import com.dlohaiti.dlokiosk.client.SponsorClient;
import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.DeliveryRepository;
import com.dlohaiti.dlokiosk.db.PaymentHistoryRepository;
import com.dlohaiti.dlokiosk.db.ReadingsRepository;
import com.dlohaiti.dlokiosk.db.ReceiptsRepository;
import com.dlohaiti.dlokiosk.db.SponsorRepository;
import com.dlohaiti.dlokiosk.domain.CustomerAccount;
import com.dlohaiti.dlokiosk.domain.PaymentHistory;
import com.dlohaiti.dlokiosk.domain.Reading;
import com.dlohaiti.dlokiosk.domain.Receipt;
import com.dlohaiti.dlokiosk.domain.Sponsor;
import com.google.inject.Inject;

import java.util.Collection;
import java.util.List;

import roboguice.util.RoboAsyncTask;

public class ManualSyncReadingsTask extends RoboAsyncTask<String> {

    @Inject
    private ReceiptsClient receiptsClient;
    @Inject
    private PaymentHistoryClient paymentHistoryClient;
    @Inject
    private DeliveriesClient deliveriesClient;
    @Inject
    private ReadingsClient readingsClient;
    @Inject
    private SponsorClient sponsorClient;
    @Inject
    private CustomerAccountClient accountClient;
    @Inject
    private ReceiptsRepository receiptsRepository;
    @Inject
    private DeliveryRepository deliveriesRepository;
    @Inject
    private ReadingsRepository readingsRepository;
    @Inject
    private CustomerAccountRepository customerAccountRepository;
    @Inject
    private SponsorRepository sponsorRepository;
    @Inject
    private PaymentHistoryRepository paymentHistoryRepository;

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
        Collection<Reading> readings = readingsRepository.list();
        List<CustomerAccount> accounts = customerAccountRepository.getNonSyncAccounts();
        List<Sponsor> sponsors = sponsorRepository.getNonSyncSponsors();
        List<PaymentHistory> paymentHistories = paymentHistoryRepository.list();

        if (sponsors.isEmpty() && accounts.isEmpty() && receipts.isEmpty() && readings.isEmpty() && paymentHistories.isEmpty()) {
            return activity.getString(R.string.no_readings_msg);
        }

        Failures failures = new Failures();

        for (Sponsor sponsor : sponsors) {
            sponsor.withAccounts(customerAccountRepository.findBySponsorId(sponsor.getId()));
            PostResponse response = sponsorClient.send(sponsor);
            if (response.isSuccess()) {
                sponsorRepository.synced(sponsor);
            } else {
                failures.add(new Failure(FailureKind.SPONSOR, response.getErrors()));
            }
        }

        for (CustomerAccount account : accounts) {
            PostResponse response = accountClient.send(account);
            if (response.isSuccess()) {
                customerAccountRepository.synced(account);
            } else {
                failures.add(new Failure(FailureKind.ACCOUNT, response.getErrors()));
            }
        }

        for (Reading reading : readings) {
            PostResponse response = readingsClient.send(reading);
            if (response.isSuccess()) {
                readingsRepository.remove(reading);
            } else {
                failures.add(new Failure(FailureKind.READING, response.getErrors()));
            }
        }

        for (Receipt receipt : receipts) {
            PaymentHistory paymentHistory=getPaymentHistoryForReceipt(paymentHistories,receipt);
            receipt.setPaymentHistory(paymentHistory);
            PostResponse response = receiptsClient.send(receipt);
            if (response.isSuccess()) {
                receiptsRepository.remove(receipt);
                if (paymentHistory!=null){
                    paymentHistoryRepository.remove(paymentHistory);
                }
            } else {
                failures.add(new Failure(FailureKind.RECEIPT, response.getErrors()));
            }
        }
        paymentHistories = paymentHistoryRepository.list();
        for(PaymentHistory history: paymentHistories){
            PostResponse response = paymentHistoryClient.send(history);
            if (response.isSuccess()) {
                paymentHistoryRepository.remove(history);
            } else {
                failures.add(new Failure(FailureKind.PAYMENT_HISTORY, response.getErrors()));
            }
        }
        if (failures.isNotEmpty()) {
            Integer readingCount = failures.countFor(FailureKind.READING);
            Integer receiptCount = failures.countFor(FailureKind.RECEIPT);
            Integer accountCount = failures.countFor(FailureKind.ACCOUNT);
            Integer sponsorCount = failures.countFor(FailureKind.SPONSOR);
            Integer paymentCount = failures.countFor(FailureKind.PAYMENT_HISTORY);
            return activity.getString(R.string.send_error_msg, readingCount, receiptCount,accountCount,sponsorCount,paymentCount);
        }
        return activity.getString(R.string.send_success_msg, readings.size(), receipts.size(), accounts.size(),sponsors.size(),paymentHistories.size());
    }

    private PaymentHistory getPaymentHistoryForReceipt(List<PaymentHistory> paymentHistories, Receipt receipt) {
        for(PaymentHistory history: paymentHistories){
            if(history.getReceiptID().equals(receipt.getId())){
                return history;
            }
        }
        return null;
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
