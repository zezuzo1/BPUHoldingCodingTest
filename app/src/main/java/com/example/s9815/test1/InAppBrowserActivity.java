package com.example.s9815.test1;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;

import com.example.s9815.test1.R;

import org.apache.http.util.EncodingUtils;

public class InAppBrowserActivity extends AppCompatActivity {

    public static String KEY_URL = "IN_APP_BROWSER_URL";
    private final long FINSH_INTERVAL_TIME = 3000;

    private WebView mWebView = null;
    private long backPressedTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inapp_browser);

        View.OnClickListener inAppBrowserClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.browser_prev:
                        clickPrev();
                        break;
                    case R.id.browser_next:
                        clickNext();
                        break;
                    case R.id.browser_reload:
                        clickReload();
                        break;
                    case R.id.browser_close:
                        clickClose();
                        break;
                    default:
                        break;
                }
            }
        };

        findViewById(R.id.browser_prev).setOnClickListener(inAppBrowserClickListener);
        findViewById(R.id.browser_next).setOnClickListener(inAppBrowserClickListener);
        findViewById(R.id.browser_reload).setOnClickListener(inAppBrowserClickListener);
        findViewById(R.id.browser_close).setOnClickListener(inAppBrowserClickListener);

        findViewById(R.id.browser_prev).setEnabled(false);
        findViewById(R.id.browser_next).setEnabled(false);

        mWebView = createWebView(R.id.browser_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(InAppBrowserActivity.this)
                        .setTitle("Alert title")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }
        });

        String toUrl = getIntent().getStringExtra(KEY_URL);
        if( toUrl == null || toUrl.length() <= 0 )
            toUrl = "";
        mWebView.loadUrl(toUrl);

        setTitle(toUrl);
    }

    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //showDialog(0);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //dismissDialog(0);
        }
    };

    private WebView createWebView(int webviewId) {
        final WebView webView = (WebView) findViewById(webviewId);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(webViewClient);
        return webView;
    }

    public void clickPrev() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    public void clickNext() {
        if (mWebView.canGoForward()) {
            mWebView.goForward();
        }
    }

    public void clickReload() {
        Log.d("oz","clickReload");
        mWebView.reload();
    }

    public void clickClose() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        } else {
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;
            if (0 == intervalTime || FINSH_INTERVAL_TIME < intervalTime) {
                backPressedTime = tempTime;
                return;
            }
        }
        super.onBackPressed();
    }
}
