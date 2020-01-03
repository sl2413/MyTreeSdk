package com.shenl.mytree.Login;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;

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
    public static String getTaoBaoUserInfo(){
        return AlibcLogin.getInstance().getSession()+"";
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
}
