package com.shenl.mytree.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.shenl.mytree.R;
import com.shenl.mytree.Utils.WxUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IWXAPI api = WxUtils.RegToWx(WXEntryActivity.this);
        api.handleIntent(getIntent(),new MyEventHandler());
    }

    /**
     * TODO 功能：接收微信返回的信息
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/24
     */
    class MyEventHandler implements IWXAPIEventHandler{

        @Override
        public void onReq(BaseReq baseReq) {
            //微信发送的请求将回调
        }

        @Override
        public void onResp(BaseResp baseResp) {
            //发送到微信请求的响应结果回调
            SendAuth.Resp resp = (SendAuth.Resp)baseResp;
            Log.e("shenl","errCode="+resp.errCode);
            Log.e("shenl","code="+resp.code);
        }
    }
}
