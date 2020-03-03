package com.shenl.mytree.initTree;

import android.app.Application;
import android.util.Log;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;

public class TaoBaoInit {
    /**
     * TODO 功能：初始化淘宝api
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2019/12/31
     */
    public static void Init(Application application){
        AlibcTradeSDK.asyncInit(application, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                System.out.println("===========淘宝api初始化成功===========");
                Log.e("shenl","===========淘宝api初始化成功===========");
            }

            @Override
            public void onFailure(int code, String msg) {
                System.out.println("***淘宝api初始化失败:"+code+"***{"+msg+"}");
                Log.e("shenl","***淘宝api初始化失败:"+code+"***{"+msg+"}");
            }
        });
    }
}
