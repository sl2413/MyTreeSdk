package com.shenl.mytree.Utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.viewpager.widget.ViewPager;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.trade.biz.alipay.AliPayResult;
import com.alibaba.baichuan.trade.biz.applink.adapter.AlibcFailModeType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaobaoUtils {

    public static String Pid;
    public static String Adzoneid;
    public static String AppKey;

    /**
     * TODO 功能：活动任务适配器
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/1/10
     *
     * @param Pid
     * @param Adzoneid
     * @param AppKey
     */
    public void setParams(String Pid, String Adzoneid, String AppKey) {
        this.Pid = Pid;
        this.Adzoneid = Adzoneid;
        this.AppKey = AppKey;
    }

    /**
     * TODO 功能：判断是否已经设置过appkey
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/1/8
     */
    private static boolean isAppKey() {
        boolean b = false;
        if (!TextUtils.isEmpty(AppKey) && !TextUtils.isEmpty(Pid) && !TextUtils.isEmpty(Adzoneid)) {
            b = true;
        } else {
            System.out.println("还未设置参数,请在初始化之后调用方法TaobaoUtils.setParams()");
        }
        return b;
    }


    /**
     * TODO 功能：打开单品详情页面
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/1/7
     *
     * @param itemId 产品详情ID
     */
    public static void openDetails(Activity activity, String itemId, OpenPageCallBack openPageCallBack) {
        if (!isAppKey()) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        openDetails(activity, itemId, map, openPageCallBack);
    }


    /**
     * TODO 功能：打开单品详情页面
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/1/7
     *
     * @param itemId      产品详情ID
     * @param trackParams 自定义参数 Map<String, String>
     */
    public static void openDetails(final Activity activity, String itemId, Map<String, String> trackParams, final OpenPageCallBack openPageCallBack) {
        if (!isAppKey()) {
            return;
        }
        AlibcBasePage page = new AlibcDetailPage(itemId);
        AlibcShowParams showParams = new AlibcShowParams();
        //OpenType.Auto 不做任何设置自动选择打开方式
        // OpenType.Native 唤起客户端
        showParams.setOpenType(OpenType.Auto);
        //taobao---唤起淘宝客户端；tmall---唤起天猫客户端
        showParams.setClientType("taobao");
        //唤起端返回的url 以小把手的形式在唤起端展示
        //showParams.setBackUrl(BACK_URL);
        //AlibcNativeFailModeNONE：不做处理；
        //AlibcNativeFailModeJumpBROWER：跳转浏览器；
        //AlibcNativeFailModeJumpDOWNLOAD：跳转下载页；
        //AlibcNativeFailModeJumpH5：应用内webview打开
        showParams.setNativeOpenFailedMode(AlibcFailModeType.AlibcNativeFailModeJumpH5);

        AlibcTaokeParams taokeParams = new AlibcTaokeParams(Pid, "", "");
//        taokeParams.setPid(Pid);
//        taokeParams.setAdzoneid(Adzoneid);
        //adzoneid是需要taokeAppkey参数才可以转链成功&店铺页面需要卖家id（sellerId），具体设置方式如下：
//        taokeParams.extraParams.put("taokeAppkey",AppKey);
        //taokeParams.extraParams.put("sellerId", "xxxxx");

        /*AlibcTrade.openByUrl(activity, "", "", new WebView(activity), new WebViewClient(), new WebChromeClient(), showParams, taokeParams, trackParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                Log.e("shenl","成功回调");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });*/

        AlibcTrade.openByBizCode(activity, page, null, new WebViewClient(),
                new WebChromeClient(), "detail", showParams, taokeParams,
                trackParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                        // 交易成功回调（其他情形不回调）
                        AliPayResult payResult = tradeResult.payResult;
                        List<String> payFailedOrders = payResult.payFailedOrders;
                        List<String> paySuccessOrders = payResult.paySuccessOrders;
                        openPageCallBack.success(paySuccessOrders, payFailedOrders);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // 失败回调信息
                        System.out.println("唤起失败:code=" + code + ", msg=" + msg);
                        Log.e("shenl", "唤起失败:code=" + code + ", msg=" + msg);
                    }
                });
    }

    /**
     * TODO 功能：打开详情的url
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/1/14
     */
    public static void openDetailsByUrl(Activity activity, String url, Map<String, String> trackParams, final OpenPageCallBack openPageCallBack) {
        AlibcShowParams showParams = new AlibcShowParams();
        //OpenType.Auto 不做任何设置自动选择打开方式
        // OpenType.Native 唤起客户端
        showParams.setOpenType(OpenType.Auto);
        //taobao---唤起淘宝客户端；tmall---唤起天猫客户端
        showParams.setClientType("taobao");
        //唤起端返回的url 以小把手的形式在唤起端展示
        //showParams.setBackUrl(BACK_URL);
        //AlibcNativeFailModeNONE：不做处理；
        //AlibcNativeFailModeJumpBROWER：跳转浏览器；
        //AlibcNativeFailModeJumpDOWNLOAD：跳转下载页；
        //AlibcNativeFailModeJumpH5：应用内webview打开
        showParams.setNativeOpenFailedMode(AlibcFailModeType.AlibcNativeFailModeJumpH5);
        AlibcTaokeParams taokeParams = new AlibcTaokeParams(Pid, "", "");
        // 以显示传入url的方式打开页面（第二个参数是套件名称）
        //webView.loadUrl("file:///android_asset/index.html");

        WebView webView = new WebView(activity);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("shenl",url);
            }
        });

        AlibcTrade.openByUrl(activity, "", url, webView,
                new MyWebViewClient(), new WebChromeClient(), showParams,
                taokeParams, trackParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                        Log.e("shenl", "成功回调");
                        // 交易成功回调（其他情形不回调）
                        AliPayResult payResult = tradeResult.payResult;
                        List<String> payFailedOrders = payResult.payFailedOrders;
                        List<String> paySuccessOrders = payResult.paySuccessOrders;
                        openPageCallBack.success(paySuccessOrders, payFailedOrders);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // 失败回调信息
                        System.out.println("唤起失败:code=" + code + ", msg=" + msg);
                        Log.e("shenl", "唤起失败:code=" + code + ", msg=" + msg);
                    }
                });
    }

    private static class MyWebViewClient extends WebViewClient {
        @Override
        // 在WebView中而不是默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("shenl",url);
            view.loadUrl(url);
            return true;
        }
    }

    /**
     * TODO 功能：打开商铺页面
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/1/7
     */
    public static void openShop() {

    }

    /**
     * TODO 功能：唤起或打开页面回调
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/1/7
     */
    public interface OpenPageCallBack {
        void success(List<String> paySuccessOrders, List<String> payFailedOrders);
    }
}
