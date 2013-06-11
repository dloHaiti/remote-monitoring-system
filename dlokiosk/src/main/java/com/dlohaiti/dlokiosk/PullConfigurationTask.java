package com.dlohaiti.dlokiosk;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.dlohaiti.dlokiosk.client.Configuration;
import com.dlohaiti.dlokiosk.client.ConfigurationClient;
import com.google.inject.Inject;
import roboguice.inject.InjectResource;
import roboguice.util.RoboAsyncTask;

public class PullConfigurationTask extends RoboAsyncTask<Configuration> {
    private static final String TAG = PullConfigurationTask.class.getSimpleName();
    private ProgressDialog dialog;
    @Inject private ConfigurationClient client;
    @InjectResource(R.string.fetch_configuration_failed) private String fetchConfigurationFailedMessage;
    @InjectResource(R.string.fetch_configuration_succeeded) private String fetchConfigurationSucceededMessage;
    private Context context;

    public PullConfigurationTask(Context context) {
        super(context);
        this.context = context;
        this.dialog = new ProgressDialog(context);
    }

    @Override protected void onPreExecute() throws Exception {
        dialog.setMessage("Loading Configuration From Server...");
        dialog.show();
    }

    @Override public Configuration call() throws Exception {
        return client.fetch();
    }

    @Override protected void onSuccess(Configuration s) throws Exception {
        Toast.makeText(context, fetchConfigurationSucceededMessage, Toast.LENGTH_LONG).show();
    }

    @Override protected void onException(Exception e) throws RuntimeException {
        Log.e(TAG, "Error fetching configuration from server", e);
        Toast.makeText(context, fetchConfigurationFailedMessage, Toast.LENGTH_LONG).show();
    }

    @Override protected void onFinally() throws RuntimeException {
        if(dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
