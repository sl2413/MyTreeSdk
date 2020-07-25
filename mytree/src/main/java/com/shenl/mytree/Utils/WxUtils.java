package com.shenl.mytree.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WxUtils {
    // APP_ID 替换为你的应用从官方网站申请到的合法appID
    public static String APP_ID = "";
    public static String APP_SECRET = "";

    //登录后获得
    public static String access_token = "";
    public static String expires_in = "";
    public static String refresh_token = "";
    public static String openid = "";
    public static String scope = "";
    public static String unionid = "";


    //分享到对话
    public static final int SESSION = SendMessageToWX.Req.WXSceneSession;
    //分享到朋友圈
    public static final int TIMELINE = SendMessageToWX.Req.WXSceneTimeline;
    //分享到收藏
    public static final int FAVORITE = SendMessageToWX.Req.WXSceneFavorite;



    /**
     * TODO 功能：将自己的app注册到微信
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/24
     */
    public static IWXAPI RegToWx(Context context) {
        if (TextUtils.isEmpty(APP_ID)){
            Toast.makeText(context,"注册微信appId不能为空",Toast.LENGTH_SHORT).show();
            Log.e("shenl","要注册微信请先调用WxUtils.APP_ID进行赋值操作...");
            return null;
        }
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        // IWXAPI 是第三方app和微信通信的openApi接口
        final IWXAPI api = WXAPIFactory.createWXAPI(context, APP_ID, false);
        // 将应用的appId注册到微信
        //api.registerApp(APP_ID);

        //建议动态监听微信启动广播进行注册到微信
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 将该app注册到微信
                api.registerApp(APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

        return api;
    }
}
