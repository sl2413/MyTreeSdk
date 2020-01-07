package com.shenl.mytree.Utils;

import android.app.Activity;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.trade.biz.applink.adapter.AlibcFailModeType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;

import java.util.HashMap;
import java.util.Map;

public class TaobaoUtils {

    public static String AppKey;

    public void setAppKey(String appKey) {
        this.AppKey = appKey;
    }

    /**
     * TODO 功能：判断是否已经设置过appkey
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/1/8
     */
    private static boolean isAppKey() {
        boolean b = false;
        if (!TextUtils.isEmpty(AppKey)){
            b = true;
        }else{
            System.out.println("appkey还未设置,设置方法TaobaoUtils.setAppKey(appKey) 或者 TaobaoUtils.AppKey=appKey");
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
        if (!isAppKey()){
            return;
        }
        HashMap<String, String> map = new HashMap<>();
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
        if (!isAppKey()){
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

        AlibcTaokeParams taokeParams = new AlibcTaokeParams("", "", "");
        taokeParams.setPid("mm_112883640_11584347_72287650277");
        taokeParams.setAdzoneid(itemId);
        //adzoneid是需要taokeAppkey参数才可以转链成功&店铺页面需要卖家id（sellerId），具体设置方式如下：
        taokeParams.extraParams.put("taokeAppkey",AppKey);
        //taokeParams.extraParams.put("sellerId", "xxxxx");

        AlibcTrade.openByBizCode(activity, page, null, new WebViewClient(),
                new WebChromeClient(), "detail", showParams, taokeParams,
                trackParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                        // 交易成功回调（其他情形不回调）
                        openPageCallBack.success(tradeResult);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // 失败回调信息
                        System.out.println("唤起失败:code=" + code + ", msg=" + msg);
                    }
                });
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
    interface OpenPageCallBack {
        void success(AlibcTradeResult tradeResult);
    }
}
