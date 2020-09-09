package com.shenl.mytree.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.trade.biz.applink.adapter.AlibcFailModeType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.shenl.mytree.R;
import com.shenl.mytree.Utils.JsonUtils;
import com.shenl.mytree.Utils.TaobaoUtils;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import static com.shenl.mytree.web.BaichuanUtil.getClientType;
import static com.shenl.mytree.web.BaichuanUtil.getFailModeType;
import static com.shenl.mytree.web.BaichuanUtil.getOpenType;
import static com.shenl.mytree.web.BaichuanUtil.getTaokeParams;

public class WebViewActivity extends Activity {

    //这个回调最好是非静态的  多线程下会有问题  但是这样比较快
    private static CallBack callBack;

    public static void setCallBack(CallBack callBack){
        WebViewActivity.callBack = callBack;
    }

    @JavascriptInterface
    public void show(String txt) {
        callBack.success(txt);
        int start = txt.indexOf("{");
        int end = txt.indexOf("}")+1;
        String s = txt.substring(start,end);
        String code = JsonUtils.getFieldValue(s, "code");
        if (!"0".equals(code)){
            Toast.makeText(WebViewActivity.this,JsonUtils.getFieldValue(s,"msg"),Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        makeStatusBarTransparent(this);
        Intent intent = getIntent();
        if (intent != null) {
            final String url = intent.getStringExtra("url");
            HashMap<String, Object> arguments = (HashMap<String, Object>) intent.getSerializableExtra("arguments");
            WebView webView = findViewById(R.id.webview);
            //启用支持JavaScript
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(this, "handler");
            //启用支持DOM Storage
            webView.getSettings().setDomStorageEnabled(true);
            openByUrl(url, webView, arguments);
        }
    }

    private String getAccessToken(String url) {
        try {
            int startIndex = url.indexOf("access_token");
            String subStr = url.substring(startIndex);
            String tempUrl = URLDecoder.decode(subStr, "UTF-8");
            int endIndex = tempUrl.indexOf("&");
            subStr = tempUrl.substring(0, endIndex);
            startIndex = subStr.indexOf("=");
            subStr = subStr.substring(startIndex+1);
            return subStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void openByUrl(String url, WebView webView, HashMap argument) {
        AlibcShowParams showParams = new AlibcShowParams();
        AlibcTaokeParams taokeParams = new AlibcTaokeParams("", "", "");
        showParams.setBackUrl(String.valueOf(argument.get(BaichuanConstants.key_BackUrl)));

        if (argument.get(BaichuanConstants.key_OpenType) != null){
            showParams.setOpenType(getOpenType(String.valueOf(argument.get(BaichuanConstants.key_OpenType))));
        }

        if (argument.get(BaichuanConstants.key_ClientType) != null){
            showParams.setClientType(getClientType(String.valueOf(argument.get(BaichuanConstants.key_ClientType))));
        }
        if (argument.get("taokeParams") != null){
            taokeParams  = getTaokeParams((HashMap<String, Object>)argument.get("taokeParams"));
        }
        if ("false".equals(argument.get("isNeedCustomNativeFailMode"))){
            showParams.setNativeOpenFailedMode(AlibcFailModeType.AlibcNativeFailModeNONE);
        }else if (argument.get(BaichuanConstants.key_NativeFailMode) != null){
            showParams.setNativeOpenFailedMode(getFailModeType(String.valueOf(argument.get(BaichuanConstants.key_NativeFailMode))));
        }

        Map<String, String> trackParams = new HashMap<>();

        WebViewClient client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                int i = TaobaoUtils.CallBackUrl.indexOf("?");
                int i1 = url.indexOf("?");
                if (url.substring(0,i1).equals(TaobaoUtils.CallBackUrl.substring(0,i))){
                    setVisible(false);
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                int i = TaobaoUtils.CallBackUrl.indexOf("?");
                int i1 = url.indexOf("?");
                if (url.substring(0,i1).equals(TaobaoUtils.CallBackUrl.substring(0,i))){
                    view.loadUrl("javascript:window.handler.show(document.body.innerHTML);");
                }
            }
        };

        AlibcTrade.openByUrl(WebViewActivity.this, "", url, webView,
                client, new WebChromeClient(),
                showParams, taokeParams, trackParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {}//不会回调
                    @Override
                    public void onFailure(int code, String msg) {}//不会回调
                });
    }

    @Override
    protected void onDestroy() {
//        if (callBack != null){
//            callBack.failed("授权失败");
//        }
        super.onDestroy();
    }



    public interface CallBack{
        void success(String accessToken);
        void failed(String errorMsg);
    }

    public void Back(View v){
        finish();
    }

    public static void makeStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
}
