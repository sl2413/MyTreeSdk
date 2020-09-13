package com.shenl.mytreesdk;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.shenl.mytree.Login.TaoBaoLogin;
import com.shenl.mytree.Login.WXlogin;
import com.shenl.mytree.Share.ShareUtils;
import com.shenl.mytree.Share.WXshare;
import com.shenl.mytree.Utils.HttpUtils;
import com.shenl.mytree.Utils.JsonUtils;
import com.shenl.mytree.Utils.TaobaoUtils;
import com.shenl.mytree.Utils.TaobaoUtils.OpenPageCallBack;
import com.shenl.mytree.Utils.WxUtils;
import com.shenl.mytree.initTree.TaoBaoInit;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WxUtils.APP_ID = "wx39685299f84af107";
        WxUtils.APP_SECRET = "324f9258a1c93b93af961254c4a2b522";

    }

    public void initTaobao(View v) {
        TaoBaoInit.Init(getApplication());
    }

    public void loginTaobao(View v) {
//        TaobaoUtils.Pid = "mm_43873942_452300281_109725600386";
        TaobaoUtils.AppKey = "27996984";
        TaobaoUtils.CallBackUrl = "http://m.qianrong.vip:8040/qr-consumer-user/userserver/user/getTaobaoAccessToken?view=wap&state="+"12235";
        TaoBaoLogin.Login(new TaoBaoLogin.TaoBaoLoginCallBack() {
            @Override
            public void onSuccess(int code, String openId, String nickName) {
                TaoBaoLogin.tk_Login(MainActivity.this, new TaoBaoLogin.TkLoginCallBack() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e("shenl",s);
                    }

                    @Override
                    public void onError(String s) {
                        Log.e("shenl",s);
                    }
                });
            }
        });
    }

    public void openTaobaoByUrl(View v) {
        String url = "";
        TaobaoUtils.openDetailsByUrl(MainActivity.this, "", url, "", new HashMap<String, String>(), new OpenPageCallBack() {
            @Override
            public void success(List<String> paySuccessOrders, List<String> payFailedOrders) {
                Log.e("shenl", "授权完成");
            }
        });
    }

    public void logoutTaobao(View v) {
        TaoBaoLogin.logout();
    }


    public void Share(View v) {
        ShareUtils shareUtils = new ShareUtils(new String[]{ShareUtils.WEIXIN});
        shareUtils.ShowShare(MainActivity.this, new ShareUtils.onItemClickListener() {
            @Override
            public void onItemClick(ShareUtils.ShareBean bean) {
                Log.e("shenl", bean.proName);
                Log.e("shenl", bean.text);
            }
        });
    }

    public void ShareText(View v) {
        WXshare.ShareText(MainActivity.this, WxUtils.TIMELINE, "微信分享文字");
    }

    public void ShareImg(View v) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.shareimg);
        WXshare.ShareImage(MainActivity.this, WxUtils.TIMELINE, bmp);
    }

    public void ShareMusic(View v) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.shareimg);
        String url = "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3";
        WXshare.ShareMusic(MainActivity.this, WxUtils.TIMELINE, url, "音频分享", "一段好听的音乐", bmp);
    }

    public void ShareVideo(View v) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.shareimg);
        String url = "https://v.youku.com/v_show/id_XNDQxMDk1Mzg1Mg==.html";
        WXshare.ShareVideo(MainActivity.this, WxUtils.TIMELINE, url, "视频分享", "一个好看的视频", bmp);
    }

    public void ShareUrl(View v) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.shareimg);
        String url = "http://www.baidu.com";
        WXshare.ShareUrl(MainActivity.this, WxUtils.TIMELINE, url, "网页分享", "一个搜索的网站", bmp);
    }

    public void WXlogin(View v) {
        WXlogin.isValid(MainActivity.this, new HttpUtils.HttpCallBack() {
            @Override
            public void Success(String s) {
                String errcode = JsonUtils.getFieldValue(s, "errcode");
                if (!"0".equals(errcode)) {
                    WXlogin.getRefreshToken(new WXlogin.TokenListener() {
                        @Override
                        public void success() {
                            WXlogin.getUserInfo(MainActivity.this, new HttpUtils.HttpCallBack() {
                                @Override
                                public void Success(String s) {
                                    Log.e("shenl", s);
                                }

                                @Override
                                public void Fail(int ResponseCode) {
                                    Log.e("shenl", "网络错误:" + ResponseCode);
                                }
                            });
                        }

                        @Override
                        public void Fail() {

                        }
                    });
                }
            }

            @Override
            public void Fail(int ResponseCode) {
                Log.e("shenl", "网络错误:" + ResponseCode);
            }
        });
    }

    public void getInfo(View v) {
        WXlogin.getUserInfo(MainActivity.this, new HttpUtils.HttpCallBack() {
            @Override
            public void Success(String s) {
                Log.e("shenl", s);
            }

            @Override
            public void Fail(int ResponseCode) {
                Log.e("shenl", "网络错误:" + ResponseCode);
            }
        });
    }

    public void WXlogout(View v) {
        WXlogin.Logout(MainActivity.this);
    }
}
