package com.dlohaiti.dlokiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActivity implements StatusView {
    @InjectView(R.id.serverStatusProgressBar) ProgressBar serverStatusProgressBar;
    @InjectView(R.id.statusImage) ImageView statusImage;
    @Inject private ConfigurationRepository config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            new CheckServerStatusTask(this.getApplicationContext(), this, config.get(ConfigurationKey.SERVER_URL)).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void moveToNewSaleScreen(View view) {
        startActivity(new Intent(this, EnterSaleActivity.class));
    }

    public void doManualSync(View view) {
        new ManualSyncReadingsTask(this).execute();
    }

    public void moveToDeliveryTrackingScreen(View v) {
        startActivity(new Intent(this, DeliveryActivity.class));
    }

    public void moveToSelectSamplingSite(View v) {
        startActivity(new Intent(this, SelectSamplingSiteActivity.class));
    }

    public void moveToViewReports(View v) {
        startActivity(new Intent(this, ViewReportsActivity.class));
    }

    @Override
    public void showProgressBar() {
        statusImage.setVisibility(View.INVISIBLE);
        serverStatusProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressBar() {
        serverStatusProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void refreshStatus(Boolean result) {
        int imageResource;
        if (result) {
            imageResource = R.drawable.green_checkmark;
        } else {
            imageResource = R.drawable.red_x;
        }
        statusImage.setImageResource(imageResource);
        statusImage.setVisibility(View.VISIBLE);
    }

    @Override public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_configuration) {
            startActivity(new Intent(MainActivity.this, ConfigurationActivity.class));
        }
        return super.onMenuItemSelected(featureId, item);
    }

}
