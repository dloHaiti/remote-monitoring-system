package com.dlohaiti.dlokiosknew;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import roboguice.activity.RoboActivity;

public class SelectDiagnostic extends RoboActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_select_diagnostic);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SelectDiagnostic.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void cancelClick(View view) {
        super.onBackPressed();
    }

    public void moveToFlowMeterScreen(View view) {
        startActivity(new Intent(this, FlowMeterReadingActivity.class));
    }

    public void moveToWaterQualityScreen(View view) {
        startActivity(new Intent(this,WaterQualityActivity.class));
    }
}