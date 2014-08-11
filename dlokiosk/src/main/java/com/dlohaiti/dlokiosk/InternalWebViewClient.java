package com.dlohaiti.dlokiosk;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;
import roboguice.inject.InjectView;

public class InternalWebViewClient extends WebViewClient {
    @InjectView(R.id.view_reports_progress)
    private ProgressBar progress;
    @Inject
    private ConfigurationRepository config;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        handler.proceed(config.get(ConfigurationKey.KIOSK_ID), config.get(ConfigurationKey.KIOSK_PASSWORD));
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        progress.setVisibility(View.VISIBLE);
        view.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        progress.setVisibility(View.INVISIBLE);
        view.setVisibility(View.VISIBLE);
    }
}
