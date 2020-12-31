package com.shenl.mytree.Share;

import android.content.Context;

import com.shenl.mytree.Utils.WxUtils;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;

public class WXjump {
    private static IWXAPI api;

    /**
     * TODO 功能：拉起微信小程序
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/12/31
     */
    public static void LaunchMiniProgram(Context context,String MiniProgramId,String path){
        if (api == null) {
            api = WxUtils.RegToWx(context);
        }
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = MiniProgramId; // 填小程序原始id
        req.path = path;             //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }
}
