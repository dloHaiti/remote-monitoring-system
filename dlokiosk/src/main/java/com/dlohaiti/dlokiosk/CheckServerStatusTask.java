package com.dlohaiti.dlokiosk;

import android.content.Context;
import android.util.Log;
import com.dlohaiti.dlokiosk.client.HealthcheckClient;
import com.dlohaiti.dlokiosk.client.RestClient;
import roboguice.util.RoboAsyncTask;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class CheckServerStatusTask extends RoboAsyncTask<Boolean> {

    private HealthcheckClient healthcheckClient;
    private StatusView statusView;

    public CheckServerStatusTask(Context context, StatusView statusView, String baseUrl) {
        super(context);
        this.statusView = statusView;
        healthcheckClient = new HealthcheckClient(new RestClient(baseUrl));
    }

    @Override
    protected void onPreExecute() {
        statusView.showProgressBar();
    }

    @Override
    public Boolean call() throws Exception {
        try {
            return healthcheckClient.getServerStatus();
        } catch (Exception e) {
            logException(e);
            return false;
        }
    }

    @Override
    protected void onSuccess(Boolean result) {
        statusView.dismissProgressBar();
        statusView.refreshStatus(result);
    }

    private void logException(Exception e) {
        Log.e("CheckServerStatusTask", e.getMessage(), e);
        Writer result = new StringWriter();
        e.printStackTrace(new PrintWriter(result));
    }

}

