package com.dlohaiti.dlokiosk;

import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;

public class InternalWebViewClient extends WebViewClient {
    @Inject private ConfigurationRepository config;

    @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        handler.proceed(config.get(ConfigurationKey.KIOSK_ID), config.get(ConfigurationKey.KIOSK_PASSWORD));
    }
}
