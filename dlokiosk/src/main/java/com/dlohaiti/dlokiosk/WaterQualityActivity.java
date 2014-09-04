package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class WaterQualityActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waterquality);
    }

    public void onBack(View view) {
        finish();
    }

    public void onCancel(View view) {
    }

    public void onSave(View view) {
    }
}