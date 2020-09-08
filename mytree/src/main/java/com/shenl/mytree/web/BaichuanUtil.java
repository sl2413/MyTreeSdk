package com.shenl.mytree.web;

import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.trade.biz.applink.adapter.AlibcFailModeType;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;

import java.util.Map;

import static com.shenl.mytree.web.BaichuanConstants.Auto_OpenType;
import static com.shenl.mytree.web.BaichuanConstants.JumpDownloadPage_FailMode;
import static com.shenl.mytree.web.BaichuanConstants.JumpH5_FailMode;
import static com.shenl.mytree.web.BaichuanConstants.Tmall_ClientType;

/**
 * @Author karedem
 * @Date 2019/9/10 11:42
 * @Description 映射返回对应值
**/
public class BaichuanUtil {

    public static OpenType getOpenType(String open){
        if (Auto_OpenType.equals(open)){
            return OpenType.Auto;
        }else {
           return OpenType.Native;
        }
    }

    public static String getClientType(String client){
        if (client.equals(Tmall_ClientType)){
            return "tmall";
        }else {
            return "taobao";
        }
    }

    public static AlibcFailModeType getFailModeType(String mode){
        if (JumpH5_FailMode.equals(mode)){
            return AlibcFailModeType.AlibcNativeFailModeJumpH5;
        }else if (JumpDownloadPage_FailMode.equals(mode)){
            return AlibcFailModeType.AlibcNativeFailModeJumpDOWNLOAD;
        }else {
            return AlibcFailModeType.AlibcNativeFailModeNONE;
        }
    }

    public static AlibcTaokeParams getTaokeParams(Map<String, Object> taokePar){
        String pid = (String) taokePar.get("pid");
        AlibcTaokeParams taokeParams = new AlibcTaokeParams("", "", "");
        if (pid != null){
            taokeParams.setPid(pid);
        }
        Object extParams = taokePar.get("extParams");
        //TODO 其他参数待添加
        return taokeParams;
    }

}
