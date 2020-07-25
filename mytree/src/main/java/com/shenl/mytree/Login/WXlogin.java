package com.shenl.mytree.Login;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.shenl.mytree.Utils.HttpUtils;
import com.shenl.mytree.Utils.JsonUtils;
import com.shenl.mytree.Utils.WxUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;

public class WXlogin {

    private static IWXAPI api;

    /**
     * TODO 功能：微信授权登录
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/25
     */
    public static void Login(Context context) {
        if (api == null) {
            api = WxUtils.RegToWx(context);
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo,snsapi_friend,snsapi_message,snsapi_contact";
        req.state = "none";
        api.sendReq(req);
    }

    /**
     * TODO 功能：微信授权注销
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/25
     */
    public static void Logout(Context context) {
        if (api == null) {
            api = WxUtils.RegToWx(context);
        }
        api.unregisterApp();
        WxUtils.access_token = "";
        WxUtils.openid = "";
    }

    /**
     * TODO 功能：获取登录Token
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/26
     */
    public static void getToken(final Activity activity, SendAuth.Resp resp, final TokenListener tokenListener) {
        //0:同意   -2:用户取消  -4: 用户拒绝
        if (resp.errCode == 0) {
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WxUtils.APP_ID + "&secret=" + WxUtils.APP_SECRET + "&code=" + resp.code + "&grant_type=authorization_code";
            HttpUtils.get(url, new HttpUtils.HttpCallBack() {
                @Override
                public void Success(String s) {
                    Log.e("shenl", "用户同意授权获得:" + s);
                    WxUtils.access_token = JsonUtils.getFieldValue(s, "access_token");
                    WxUtils.expires_in = JsonUtils.getFieldValue(s, "expires_in");
                    WxUtils.refresh_token = JsonUtils.getFieldValue(s, "refresh_token");
                    WxUtils.openid = JsonUtils.getFieldValue(s, "openid");
                    WxUtils.scope = JsonUtils.getFieldValue(s, "scope");
                    WxUtils.unionid = JsonUtils.getFieldValue(s, "unionid");
                    tokenListener.success();
                }

                @Override
                public void Fail(int ResponseCode) {
                    Log.e("shenl", "网络错误:" + ResponseCode);
                    tokenListener.Fail();
                }
            });
        }
    }

    /**
     * TODO 功能：刷新微信登录Token
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/26
     */
    public static void getRefreshToken(final TokenListener tokenListener) {
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + WxUtils.APP_ID + "&grant_type=refresh_token&refresh_token=" + WxUtils.refresh_token;
        HttpUtils.get(url, new HttpUtils.HttpCallBack() {
            @Override
            public void Success(String s) {
                Log.e("shenl", "用户刷新token获得:" + s);
                WxUtils.access_token = JsonUtils.getFieldValue(s, "access_token");
                WxUtils.expires_in = JsonUtils.getFieldValue(s, "expires_in");
                WxUtils.refresh_token = JsonUtils.getFieldValue(s, "refresh_token");
                WxUtils.openid = JsonUtils.getFieldValue(s, "openid");
                WxUtils.scope = JsonUtils.getFieldValue(s, "scope");
                tokenListener.success();
            }

            @Override
            public void Fail(int ResponseCode) {
                Log.e("shenl", "网络错误:" + ResponseCode);
                tokenListener.Fail();
            }
        });
    }

    /**
     * TODO 功能：验证Token是否有效
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/26
     */
    public static void isValid(Context context, HttpUtils.HttpCallBack httpCallBack) {
        if (!TextUtils.isEmpty(WxUtils.access_token) && !TextUtils.isEmpty(WxUtils.openid)) {
            String url = "https://api.weixin.qq.com/sns/auth?access_token=" + WxUtils.access_token + "&openid=" + WxUtils.openid;
            HttpUtils.get(url, httpCallBack);
        } else {
            Login(context);
        }
    }

    /**
     * TODO 功能：获取用户信息
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/26
     */
    public static void getUserInfo(final Context context, final HttpUtils.HttpCallBack httpCallBack) {
        isValid(context, new HttpUtils.HttpCallBack() {
            @Override
            public void Success(String s) {
                String errcode = JsonUtils.getFieldValue(s, "errcode");
                if (!"0".equals(errcode)) {
                    Toast.makeText(context,"请先微信授权登录",Toast.LENGTH_SHORT).show();
                } else {
                    String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + WxUtils.access_token + "&openid=" + WxUtils.openid;
                    HttpUtils.get(url, httpCallBack);
                }
            }

            @Override
            public void Fail(int ResponseCode) {

            }
        });
    }

    /**
     * TODO 功能：token获取完成监听
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/4/18
     */
    public interface TokenListener{
        void success();
        void Fail();
    }
}
