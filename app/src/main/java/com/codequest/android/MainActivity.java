package com.codequest.android;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
public class MainActivity extends Activity {
  private WebView view;
  @Override public void onCreate(Bundle state) {
    super.onCreate(state);
    view = new WebView(this);
    WebSettings settings = view.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setAllowFileAccess(true);
    view.setWebViewClient(new WebViewClient());
    view.setWebChromeClient(new WebChromeClient());
    setContentView(view);
    view.loadUrl("file:///android_asset/web/index.html");
  }
  @Override public void onBackPressed() {
    if (view != null && view.canGoBack()) view.goBack(); else super.onBackPressed();
  }
}