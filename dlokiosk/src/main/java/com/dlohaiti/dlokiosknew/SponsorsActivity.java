package com.dlohaiti.dlokiosknew;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.dlohaiti.dlokiosknew.adapter.SponsorsArrayAdapter;
import com.dlohaiti.dlokiosknew.db.CustomerAccountRepository;
import com.dlohaiti.dlokiosknew.db.SponsorRepository;
import com.dlohaiti.dlokiosknew.domain.Sponsor;
import com.dlohaiti.dlokiosknew.domain.Sponsors;
import com.google.inject.Inject;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SponsorsActivity extends RoboActivity {
    @InjectView(R.id.sponsors_list)
    private ListView sponsorsListView;

    @Inject
    private SponsorRepository sponsorRepository;
    @Inject
    private CustomerAccountRepository customerAccountRepository;


    private Sponsors allSponsors;
    private SponsorsArrayAdapter sponsorsAdapter;
    private SearchView searchView;
    private Sponsors filteredSponsors =new Sponsors();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sponsors);
        initialiseSponsorsList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SponsorsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        initialiseSponsorsList();
    }

    private void initialiseSponsorsList() {
        clearSponsorSearch();
        allSponsors = sponsorRepository.findAll();
        fillAccounts();
        sponsorsAdapter = new SponsorsArrayAdapter(getApplicationContext(), allSponsors);
        sponsorsListView.setAdapter(sponsorsAdapter);
    }

    private void fillAccounts() {
        for(Sponsor s: allSponsors){
            s.withAccounts(customerAccountRepository.findBySponsorId(s.getId()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sponsors_activity, menu);

        searchView = ((SearchView) menu.findItem(R.id.search_sponsors).getActionView());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                filteredSponsors.clear();
                List<Sponsor> filteredList = allSponsors.filterBySponsorName(text);
                filteredSponsors.addAll(filteredList);
                sponsorsAdapter = new SponsorsArrayAdapter(SponsorsActivity.this, filteredSponsors);
                sponsorsListView.setAdapter(sponsorsAdapter);
                return true;
            }
        });
        return true;
    }

    private void clearSponsorSearch() {
        if(searchView ==null){
            return;
        }
        if (isNotBlank(searchView.getQuery())) {
            searchView.setQuery("", true);
        }
        searchView.setIconified(true);
    }

    public void onCancel(View view) {
        Intent intent = new Intent(SponsorsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onBack(View view) {
        finish();
    }

    public void onAddSponsor(MenuItem item) {
        Intent intent = new Intent(SponsorsActivity.this, SponsorFormActivity.class);
        startActivity(intent);
    }
}