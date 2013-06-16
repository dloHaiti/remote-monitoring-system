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
        viewReportsWebView.loadUrl(config.get(ConfigurationKey.SERVER_URL) + "/report");
    }

    @Override public void onBackPressed() {
        if(viewReportsWebView.canGoBack()) {
            viewReportsWebView.goBack();
        } else {
            finish();
        }
    }
}
