package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import roboguice.activity.RoboActivity;

public class SelectDiagnostic extends RoboActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_diagnostic);
    }

    public void cancelClick(View view) {
        super.onBackPressed();
    }

    public void moveToFlowMeterScreen(View view) {
        startActivity(new Intent(this, FlowMeterReadingActivity.class));
    }
}