package com.shenl.mytreesdk;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.shenl.mytree.Login.TaoBaoLogin;
import com.shenl.mytree.Utils.TaobaoUtils;
import com.shenl.mytree.Utils.TaobaoUtils.OpenPageCallBack;
import com.shenl.mytree.initTree.TaoBaoInit;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void initTaobao(View v){
        TaoBaoInit.Init(getApplication());
    }

    public void loginTaobao(View v){
        TaoBaoLogin.Login(new TaoBaoLogin.TaoBaoLoginCallBack() {
            @Override
            public void onSuccess(int code, String openId, String nickName) {
                //nick = 小豆鸭88,
                // ava = https://wwc.alicdn.com/avatar/getAvatar.do?userIdStrV2=IqOgPXM9jGUslehRPBVLugTT&type=taobao
                // openId=AAEt0k_fAJjdhBxR6ZuhYB9H
                // openSid=41245f9ffe097e46f5cc12533dc64c0a21593072570fe9b3f93dccf68cc2c5b6a8dde8b82d516224b9dce6a005c31e3a
                // topAccessToken=6301b02b6d4d92be66d2baf322f64a77e763802b04a0572272937162
                // topAuthCode=IU0PgUg25cEEHaaeln3qozgW3853010
                // topExpireTime=7776000
                Log.e("shenl","获取淘宝用户信息: " + TaoBaoLogin.getTaoBaoUserInfo());
            }
        });

        TaobaoUtils.openDetails(MainActivity.this, "", new OpenPageCallBack() {
            @Override
            public void success(List<String> paySuccessOrders, List<String> payFailedOrders) {

            }
        });
    }
}
