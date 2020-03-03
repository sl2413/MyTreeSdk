package com.shenl.mytreesdk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;



public class WebViewActivity extends Activity {

    private WebView webView;
    private String url;
    private LinearLayout ll_btns;
    private String aaa = "https://oauth.taobao.com/authorize?response_type=code&client_id=28294826&redirect_uri=http://m.qianrong.vip:8040/qr-consumer-user/userserver/user/getTaobaoAccessToken&view=wap&state=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView();
        initData();
        initEvent();
    }

    public void initView() {
        webView = findViewById(R.id.webView);
        ll_btns = findViewById(R.id.ll_btns);
    }

    public void initData() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.supportMultipleWindows();
        settings.setAllowFileAccess(true);
        settings.setBuiltInZoomControls(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLoadsImagesAutomatically(true);
        //settings.setSupportZoom(true);
        //调整图片至适合webview的大小
        settings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        settings.setLoadWithOverviewMode(true);
        settings.setLoadsImagesAutomatically(true);
        //设置默认编码
        settings.setDefaultTextEncodingName("utf-8");
        //设置自动加载图片
        settings.setLoadsImagesAutomatically(true);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        //webView.setWebViewClient(new WebViewClient());
        //webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                //监听跳转的url
                return false;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                //监听网络请求的过程
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //监听网页错误
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //监听网页开始加载
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //监听网页加载完成
                if (!url.equals(aaa+1)){
                    //finish();
                }
                super.onPageFinished(view, url);
            }
        });

        webView.loadUrl(aaa+1);
    }

    public void initEvent() {

    }

    public void toPay(View v){
        String url = webView.getUrl();
        String originalUrl = webView.getOriginalUrl();
        Log.e("shenl",url);
        Log.e("shenl",originalUrl);
    }

}
