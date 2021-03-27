package com.shenl.mytree.Pay;

import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.shenl.mytree.Utils.MD5Util;
import com.shenl.mytree.Utils.WxUtils;
import com.shenl.mytree.bean.UnifiedorderBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.w3c.dom.Document;

import java.io.StringWriter;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class WXpay {

    private static IWXAPI api;
    private static String stringA;

    /**
     * TODO 功能：统一下单方法
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/26
     */
    public static void unifiedorder(UnifiedorderBean bean) {
        Map<String, String> orderMap = new TreeMap<>();
        orderMap.put("appid", WxUtils.APP_ID);//应用ID
        orderMap.put("body", bean.getBody());//商品描述
        orderMap.put("device_info", bean.getDevice_info());//设备号
        orderMap.put("nonce_str", getNonceStr());//随机字符串
        orderMap.put("sign_type", "MD5");
        orderMap.put("mch_id", bean.getMch_id());//商户号
        orderMap.put("detail", bean.getDetail());//商品详情
        orderMap.put("attach", bean.getAttach());//附加数据
        orderMap.put("out_trade_no", bean.getOut_trade_no());//商户订单号
        orderMap.put("fee_type", "CNY");//货币类型
        orderMap.put("total_fee", Math.round(bean.getTotal_fee() * 100) + "");//订单金额
        orderMap.put("spbill_create_ip", bean.getSpbill_create_ip());//服务器IP
        orderMap.put("time_start", bean.getTime_start());//交易起始时间
        orderMap.put("time_expire", bean.getTime_expire());//交易结束时间
        orderMap.put("goods_tag", bean.getGoods_tag());//订单优惠标记
        orderMap.put("notify_url", bean.getNotify_url());//回调地址
        orderMap.put("trade_type", "APP");//交易类型
        orderMap.put("limit_pay", bean.getLimit_pay());//指定支付方式
        orderMap.put("receipt", bean.getReceipt());//开发票入口开放标识
        String str = getStr(orderMap) + "&key=" + bean.getKey();
        orderMap.put("sign", MD5Util.md5(str).toUpperCase());

        String xml = "";
        try {
            //xml = mapToXml(orderMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO 功能：获取所有参与签名字段拼接的字符串
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/26
     */
    public static String getStr(Map<String, String> map) {
        stringA = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!TextUtils.isEmpty(entry.getValue())) {
                stringA += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        return stringA.substring(0, stringA.length() - 1);
    }

    /**
     * TODO 功能：随机生成32位随机字符串
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/26
     */
    public static String getNonceStr() {
        String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new SecureRandom();
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * TODO 功能：app调用微信支付功能
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2021/3/27
     */
    public static void pay(Context context, Map<String,Object> map){
        if (api == null) {
            api = WxUtils.RegToWx(context);
        }
        PayReq request = new PayReq();
        request.appId = (String) map.get("appId");
        request.partnerId = (String) map.get("partnerId");
        request.prepayId= (String) map.get("prepayId");
        request.packageValue = (String) map.get("packageValue");
        request.nonceStr= (String) map.get("nonceStr");
        request.timeStamp= (String) map.get("timeStamp");
        request.sign= (String) map.get("sign");
        api.sendReq(request);
    }
}
