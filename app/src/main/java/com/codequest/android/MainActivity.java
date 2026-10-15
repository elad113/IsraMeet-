package com.codequest.android;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.net.Uri;\nimport android.content.Intent;\nimport android.webkit.ValueCallback;\nimport android.webkit.JavascriptInterface;\nimport java.io.BufferedReader;\nimport java.io.InputStream;\nimport java.io.InputStreamReader;\nimport java.io.OutputStream;\nimport java.net.HttpURLConnection;\nimport java.net.URL;\nimport org.json.JSONObject;
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
  private WebView view;\n  private ValueCallback<Uri[]> filePathCallback;\n  private static final int FILE_CHOOSER_REQUEST = 4312;
  private WebViewAssetLoader assetLoader;
  @Override public void onCreate(Bundle state) {
    super.onCreate(state);
    assetLoader = new WebViewAssetLoader.Builder().addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this)).build();
    view = new WebView(this);
    WebSettings settings = view.getSettings();
    settings.setJavaScriptEnabled(true);\n    view.addJavascriptInterface(new AiBridge(), "CodeQuestNative");
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
      @Override public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> callback, FileChooserParams params) {\n        if (filePathCallback != null) filePathCallback.onReceiveValue(null);\n        filePathCallback = callback;\n        Intent intent = params.createIntent();\n        try { startActivityForResult(intent, FILE_CHOOSER_REQUEST); return true; } catch (Exception e) { filePathCallback = null; return false; }\n      }\n      @Override public boolean onConsoleMessage(ConsoleMessage message) {
        android.util.Log.e("CodeQuestWeb", message.message() + " at " + message.sourceId() + ":" + message.lineNumber());
        return true;
      }
    });
    setContentView(view);
    view.loadUrl("https://appassets.androidplatform.net/assets/web/index.html");
  }
  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {\n    super.onActivityResult(requestCode, resultCode, data);\n    if (requestCode == FILE_CHOOSER_REQUEST && filePathCallback != null) {\n      Uri[] results = WebChromeClient.FileChooserParams.parseResult(resultCode, data);\n      filePathCallback.onReceiveValue(results); filePathCallback = null;\n    }\n  }\n  private class AiBridge {\n    @JavascriptInterface public String postJson(String endpoint, String body) {\n      HttpURLConnection connection = null;\n      try {\n        Uri uri = Uri.parse(endpoint);\n        if (!"https".equals(uri.getScheme()) || !"eureka-blocks-android.vercel.app".equals(uri.getHost()) || !"/api/ai".equals(uri.getPath())) return new JSONObject().put("ok", false).put("error", "AI endpoint not allowed").toString();\n        connection = (HttpURLConnection) new URL(endpoint).openConnection(); connection.setRequestMethod("POST"); connection.setConnectTimeout(20000); connection.setReadTimeout(60000); connection.setDoOutput(true); connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");\n        try (OutputStream os = connection.getOutputStream()) { os.write(body.getBytes(java.nio.charset.StandardCharsets.UTF_8)); }\n        int status = connection.getResponseCode(); InputStream stream = status >= 200 && status < 400 ? connection.getInputStream() : connection.getErrorStream();\n        StringBuilder result = new StringBuilder(); try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, java.nio.charset.StandardCharsets.UTF_8))) { String line; while ((line = reader.readLine()) != null) result.append(line); }\n        JSONObject output = new JSONObject(); output.put("ok", status >= 200 && status < 300); output.put("payload", new JSONObject(result.toString())); if (status < 200 || status >= 300) output.put("error", "AI server returned HTTP " + status); return output.toString();\n      } catch (Exception e) { try { return new JSONObject().put("ok", false).put("error", "AI network error: " + e.getMessage()).toString(); } catch (Exception ignored) { return "{\\\"ok\\\":false}"; } } finally { if (connection != null) connection.disconnect(); }\n    }\n  }\n  private void showError(String title, String detail) {
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
