package com.shenl.mytree.Login;

import android.content.Context;
import android.content.Intent;
import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.shenl.mytree.Utils.TaobaoUtils;
import com.shenl.mytree.web.WebViewActivity;
import com.tencent.mm.opensdk.utils.Log;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

public class TaoBaoLogin {
    /**
     * TODO 功能：淘宝登录
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2019/12/31
     */
    public static void Login(final TaoBaoLoginCallBack taoBaoLoginCallBack){
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i, String s, String s1) {
                taoBaoLoginCallBack.onSuccess(i,s,s1);
            }

            @Override
            public void onFailure(int code, String msg) {
                System.out.println("***淘宝登录失败:"+code+"***{"+msg+"}");
            }
        });
    }
    /**
     * TODO 功能：淘客登录
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2019/12/31
     */
    public static void tk_Login(Context context, final TkLoginCallBack tkLoginCallBack){
        String url = "https://oauth.taobao.com/authorize?response_type=code&client_id="+ TaobaoUtils.AppKey +"&redirect_uri="+TaobaoUtils.CallBackUrl+"&view=wap";
        Log.e("shenl",url);
        WebViewActivity.setCallBack(new WebViewActivity.CallBack() {
            @Override
            public void success(String accessToken) {
                tkLoginCallBack.onSuccess(accessToken);
            }

            @Override
            public void failed(String errorMsg) {
                tkLoginCallBack.onError(errorMsg);
            }
        });
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("arguments", new HashMap<String,Object>());
        context.startActivity(intent);
    }

    /**
     * TODO 功能：淘宝登出
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2019/12/31
     */
    public static void logout(){
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.logout(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int loginResult, String openId, String userNick) {
                // 参数说明：
                // loginResult(3--登出成功)
                // openId：用户id
                // userNick: 用户昵称
                if (loginResult == 3){
                    System.out.println("==========="+userNick+"退出成功===========");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                // code：错误码  msg： 错误信息
                System.out.println("***淘宝退出失败:"+code+"***{"+msg+"}");
            }
        });
    }

    /**
     * TODO 功能：获取淘宝用户信息
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2019/12/31
     */
    public static Map<String, String> getTaoBaoUserInfo(){
        Map<String, String> map = new HashMap<>();
        Session session = AlibcLogin.getInstance().getSession();
        map.put("nick",session.nick);//用户昵称
        map.put("avatarUrl",session.avatarUrl);//用户头像
        map.put("topAccessToken",session.topAccessToken);//登录Token
        map.put("openId",session.openId);
        map.put("openSid",session.openSid);
        map.put("topAuthCode",session.topAuthCode);
        return map;
    }

    /**
     * TODO 功能：淘宝登录回调
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2019/12/31
     */
    public abstract static class TaoBaoLoginCallBack{
        //"获取淘宝用户信息: " + AlibcLogin.getInstance().getSession()
        public abstract void onSuccess(int code, String openId, String nickName);
    }

    /**
     * TODO 功能：淘宝登录回调
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2019/12/31
     */
    public abstract static class TkLoginCallBack{
        public abstract void onSuccess(String s);
        public abstract void onError(String s);
    }
}
