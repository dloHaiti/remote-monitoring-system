package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.app.ProgressDialog;
import com.dlohaiti.dlokiosk.client.ReadingClient;
import com.dlohaiti.dlokiosk.db.MeasurementRepository;
import com.google.inject.Inject;
import roboguice.util.RoboAsyncTask;

public class ManualSyncReadingsTask extends RoboAsyncTask<Void> {

    private Activity activity;
    @Inject
    private ProgressDialog progressDialog;
    @Inject
    private MeasurementRepository repository;
    @Inject
    private ReadingClient readingClient;


    public ManualSyncReadingsTask(Activity activity) {
        super(activity.getApplicationContext());
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
    public Void call() throws Exception {
        return null;  //TODO
    }

    @Override
    protected void onSuccess(Void aVoid) {
        // TODO
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
