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
//        TaobaoUtils.Pid = "mm_43873942_452300281_109725600386";
        TaobaoUtils.Pid = "mm_43873942_1234500410_109945250290";
        TaobaoUtils.Adzoneid = "109725600386";
        TaobaoUtils.AppKey = "27996984";
        TaoBaoLogin.Login(new TaoBaoLogin.TaoBaoLoginCallBack() {
            @Override
            public void onSuccess(int code, String openId, String nickName) {

            }
        });
    }

    public void openTaobao(View v){
        TaobaoUtils.openDetails(MainActivity.this, "535615570326", new OpenPageCallBack() {
            @Override
            public void success(List<String> paySuccessOrders, List<String> payFailedOrders) {
                Log.e("shenl","回调成功…………");
            }
        });
    }

    public void openTaobaoByUrl(){
        String url = "";
        TaobaoUtils.openDetailsByUrl(MainActivity.this, url, new OpenPageCallBack() {
            @Override
            public void success(List<String> paySuccessOrders, List<String> payFailedOrders) {

            }
        });
    }

    public void logoutTaobao(View v){
        TaoBaoLogin.logout();
    }
}
