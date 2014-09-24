package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.dlohaiti.dlokiosk.adapter.SponsorsArrayAdapter;
import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.SponsorRepository;
import com.dlohaiti.dlokiosk.domain.Sponsors;
import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class SponsorsActivity extends RoboActivity {
    @InjectView(R.id.sponsors_list)
    private ListView sponsorsListView;

    @Inject
    private SponsorRepository sponsorRepository;
    private Sponsors sponsors;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsors);
        initialiseSponsorsList();
    }

    private void initialiseSponsorsList() {
        sponsors = sponsorRepository.findAll();
        new SponsorsArrayAdapter(getApplicationContext(),sponsors);
    }


    public void onCancel(View view) {
        Intent intent = new Intent(SponsorsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void onBack(View view) {
        finish();
    }
}