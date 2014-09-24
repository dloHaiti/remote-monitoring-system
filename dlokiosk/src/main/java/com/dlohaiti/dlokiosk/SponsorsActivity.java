package com.dlohaiti.dlokiosk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.dlohaiti.dlokiosk.adapter.SponsorsArrayAdapter;
import com.dlohaiti.dlokiosk.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosk.db.SponsorRepository;
import com.dlohaiti.dlokiosk.domain.Sponsor;
import com.dlohaiti.dlokiosk.domain.Sponsors;
import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class SponsorsActivity extends RoboActivity {
    @InjectView(R.id.sponsors_list)
    private ListView sponsorsListView;

    @Inject
    private SponsorRepository sponsorRepository;
    @Inject
    private CustomerAccountRepository customerAccountRepository;

    private Sponsors sponsors;
    private SponsorsArrayAdapter sponsorsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsors);
        initialiseSponsorsList();
    }

    private void initialiseSponsorsList() {
        sponsors = sponsorRepository.findAll();
        fillAccounts();
        sponsorsAdapter = new SponsorsArrayAdapter(getApplicationContext(), sponsors);
        sponsorsListView.setAdapter(sponsorsAdapter);
    }

    private void fillAccounts() {
        for(Sponsor s:sponsors){
            s.withAccounts(customerAccountRepository.findBySponsorId(s.id()));
        }
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