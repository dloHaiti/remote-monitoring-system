package com.dlohaiti.dlokiosk;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.dlohaiti.dlokiosk.client.HealthcheckClient;
import com.dlohaiti.dlokiosk.client.RestClient;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class MainActivity extends RoboActivity {
    private static final String TAG = "MainActivity";

    @InjectView(R.id.serverStatusProgressBar)
    ProgressBar serverStatusProgressBar;
    @InjectView(R.id.statusImage)
    ImageView statusImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        statusImage.setVisibility(View.INVISIBLE);
    }

    private void dismissProgressBar() {
        serverStatusProgressBar.setVisibility(View.INVISIBLE);
    }

    private void refreshStatus(Boolean result) {
        int imageResource;
        if (result) {
            imageResource = R.drawable.green_checkmark;
        } else {
            imageResource = R.drawable.red_x;
        }
        statusImage.setImageResource(imageResource);
        statusImage.setVisibility(View.VISIBLE);
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
