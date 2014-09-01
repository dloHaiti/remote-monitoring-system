package com.dlohaiti.dlokiosk;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.dlohaiti.dlokiosk.db.*;
import com.google.inject.Inject;
import org.joda.time.LocalDate;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import static org.joda.time.format.ISODateTimeFormat.basicDate;

public class MainActivity extends RoboActivity implements StatusView {
    @InjectView(R.id.serverStatusProgressBar)
    ProgressBar serverStatusProgressBar;
    @InjectView(R.id.statusImage)
    ImageView statusImage;
    @Inject
    private ConfigurationRepository config;
    @Inject
    private ReadingsRepository readingsRepository;
    @Inject
    private ReceiptsRepository receiptsRepository;
    @Inject
    private DeliveryRepository deliveryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isConnected()) {
            String text = config.get(ConfigurationKey.LAST_UPDATE);
            LocalDate lastUpdate = basicDate().parseLocalDate(text);
            if (lastUpdate.isBefore(new LocalDate())) {
                new PullConfigurationTask(this).execute();
                config.save(ConfigurationKey.LAST_UPDATE, new LocalDate().toString(basicDate()));
            }
            if (hasUnsentData()) {
                doManualSync(null);
            }
        }
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

    public void moveToSelectSalesChannelScreen(View view) {
        startActivity(new Intent(this, SelectSalesChannelAndCustomerActivity.class));
    }

    public void doManualSync(View view) {
        new ManualSyncReadingsTask(this).execute();
    }

    public void moveToDeliveryTrackingScreen(View v) {
        startActivity(new Intent(this, DeliveryActivity.class));
    }

    public void moveToSelectSamplingSite(View v) {
//        startActivity(new Intent(this, SelectSamplingSiteActivity.class));
        startActivity(new Intent(this, SelectDiagnostic.class));
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

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_configuration) {
            startActivity(new Intent(MainActivity.this, ConfigurationActivity.class));
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean hasUnsentData() {
        return readingsRepository.isNotEmpty() ||
                receiptsRepository.isNotEmpty() ||
                deliveryRepository.isNotEmpty();
    }
}
