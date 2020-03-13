package com.shenl.mytreesdk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;


public class WebViewActivity extends Activity {

    private BridgeWebView webView;

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
        webView.setDefaultHandler(new DefaultHandler());
        webView.setWebChromeClient(new WebChromeClient());
//      webView.loadUrl("file:///android_asset/index.html");
        webView.loadUrl("https://oauth.taobao.com/authorize?response_type=code&client_id=28294826&redirect_uri=http://m.qianrong.vip:8040/qr-consumer-user/userserver/user/getTaobaoAccessToken&view=wap&state=1");
//      注册监听方法当js中调用callHandler方法时会调用此方法（handlerName必须和js中相同）
        webView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.e("shenl", "js返回：" + data);
                //显示js传递给Android的消息
                Toast.makeText(WebViewActivity.this, "js返回:" + data, Toast.LENGTH_LONG).show();
                //Android返回给JS的消息
                function.onCallBack("我是js调用Android返回数据：22222");
                finish();
            }
        });
    }

    public void initData() {
    }

    public void initEvent() {

    }

}
