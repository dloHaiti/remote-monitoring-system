package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.webkit.WebView;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class ViewReportsActivity extends RoboActivity {

    @InjectView(R.id.view_reports_webview) WebView viewReportsWebView;
    @Inject private ConfigurationRepository config;
    @Inject private InternalWebViewClient webViewClient;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);
        viewReportsWebView.setWebViewClient(webViewClient);
        viewReportsWebView.getSettings().setJavaScriptEnabled(true);
//        viewReportsWebView.setHttpAuthUsernamePassword("http://10.0.2.2:8080", "DLO Server", config.get(ConfigurationKey.KIOSK_ID), config.get(ConfigurationKey.KIOSK_PASSWORD));
        viewReportsWebView.loadUrl(config.get(ConfigurationKey.REPORTS_HOME_URL));
    }
}
