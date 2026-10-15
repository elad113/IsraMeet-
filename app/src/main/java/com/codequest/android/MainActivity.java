package com.codequest.android;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.net.Uri;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceError;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.webkit.WebViewAssetLoader;

public class MainActivity extends Activity {
  private WebView view;
  private WebViewAssetLoader assetLoader;
  @Override public void onCreate(Bundle state) {
    super.onCreate(state);
    assetLoader = new WebViewAssetLoader.Builder().addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this)).build();
    view = new WebView(this);
    WebSettings settings = view.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setAllowFileAccess(false);
    settings.setAllowContentAccess(true);
    settings.setSupportZoom(false);
    settings.setDefaultTextEncodingName("UTF-8");
    view.setBackgroundColor(Color.rgb(22,13,49));
    view.setWebViewClient(new WebViewClient() {
      @Override public android.webkit.WebResourceResponse shouldInterceptRequest(WebView v, WebResourceRequest request) {
        return assetLoader.shouldInterceptRequest(request.getUrl());
      }
      @Override public android.webkit.WebResourceResponse shouldInterceptRequest(WebView v, String url) {
        return assetLoader.shouldInterceptRequest(Uri.parse(url));
      }
      @Override public void onReceivedError(WebView v, WebResourceRequest request, WebResourceError error) {
        if (request.isForMainFrame()) showError("Could not open CodeQuest", error.getDescription().toString());
      }
    });
    view.setWebChromeClient(new WebChromeClient() {
      @Override public boolean onConsoleMessage(ConsoleMessage message) {
        android.util.Log.e("CodeQuestWeb", message.message() + " at " + message.sourceId() + ":" + message.lineNumber());
        return true;
      }
    });
    setContentView(view);
    view.loadUrl("https://appassets.androidplatform.net/assets/web/index.html");
  }
  private void showError(String title, String detail) {
    runOnUiThread(() -> {
      LinearLayout panel = new LinearLayout(this);
      panel.setOrientation(LinearLayout.VERTICAL);
      panel.setPadding(40,60,40,40);
      panel.setBackgroundColor(Color.rgb(22,13,49));
      TextView heading = new TextView(this);
      heading.setText(title); heading.setTextColor(Color.WHITE); heading.setTextSize(24);
      TextView body = new TextView(this);
      body.setText(detail); body.setTextColor(Color.LTGRAY); body.setTextSize(15);
      panel.addView(heading); panel.addView(body); setContentView(panel);
    });
  }
  @Override public void onBackPressed() {
    if (view != null && view.canGoBack()) view.goBack(); else super.onBackPressed();
  }
}
