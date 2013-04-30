package com.dlohaiti.dlokiosk;

import android.os.AsyncTask;
import android.util.Log;
import com.dlohaiti.dlokiosk.client.HealthcheckClient;
import com.dlohaiti.dlokiosk.client.RestClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class CheckServerStatusTask extends
        AsyncTask<Void, Void, Boolean> {

    private HealthcheckClient healthcheckClient;
    private StatusView statusView;

    public CheckServerStatusTask(StatusView statusView, String baseUrl) {
        this.statusView = statusView;
        healthcheckClient = new HealthcheckClient(new RestClient(baseUrl));
    }

    @Override
    protected void onPreExecute() {
        statusView.showProgressBar();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            return healthcheckClient.getServerStatus();
        } catch (Exception e) {
            logException(e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        statusView.dismissProgressBar();
        statusView.refreshStatus(result);
    }

    private void logException(Exception e) {
        Log.e("CheckServerStatusTask", e.getMessage(), e);
        Writer result = new StringWriter();
        e.printStackTrace(new PrintWriter(result));
    }

}

