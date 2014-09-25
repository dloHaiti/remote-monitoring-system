package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import roboguice.activity.RoboActivity;

public class SponsorFormActivity extends RoboActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_form);
    }

    public void onCancel(View view) {
        Intent intent = new Intent(SponsorFormActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onBack(View view) {
       this.onBackPressed();
    }
}