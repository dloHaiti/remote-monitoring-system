package com.dlohaiti.dlokiosk;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InternalWebViewClient extends WebViewClient {
    @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
