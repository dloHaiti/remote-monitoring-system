package com.dlohaiti.dlokiosk;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dlohaiti.dlokiosk.client.HealthcheckClient;
import com.dlohaiti.dlokiosk.client.RestClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends RoboActivity {
    private static final String TAG = "MainActivity";

    @InjectView(R.id.serverStatusTextView)
    TextView serverStatusTextView;
    @InjectView(R.id.serverStatusProgressBar)
    ProgressBar serverStatusProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dismissProgressBar();

        new CheckServerStatusTask(getString(R.string.dlo_server_url)).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void moveToNewReadingScreen(View view) {
        Intent intent = new Intent(this, EnterReadingActivity.class);
        startActivity(intent);
    }

    private void showProgressBar() {
        serverStatusProgressBar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressBar() {
        serverStatusProgressBar.setVisibility(View.INVISIBLE);
    }

    private void refreshStatus(Boolean result) {
        String statusText;
        if (result) {
            statusText = getString(R.string.server_status_online_label);
        } else {
            statusText = getString(R.string.server_status_offline_label);
        }
        serverStatusTextView.setText(statusText);
    }

    private void logException(Exception e) {
        Log.e(TAG, e.getMessage(), e);
        Writer result = new StringWriter();
        e.printStackTrace(new PrintWriter(result));
    }

    private class CheckServerStatusTask extends
            AsyncTask<Void, Void, Boolean> {

        private HealthcheckClient healthcheckClient;

        public CheckServerStatusTask(String baseUrl) {
            healthcheckClient = new HealthcheckClient(new RestClient(baseUrl));
        }

        @Override
        protected void onPreExecute() {
            showProgressBar();
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
            dismissProgressBar();

            refreshStatus(result);
        }
    }

}
