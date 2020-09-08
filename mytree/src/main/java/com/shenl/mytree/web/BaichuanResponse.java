package com.shenl.mytree.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author karedem
 * @Date 2019/9/7 19:55
 * @Description 插件 响应返回实体类
**/
public class BaichuanResponse implements Serializable {

    private String errorCode;
    private String errorMessage;
    private Object data;

    public static BaichuanResponse success(Object obj){
        return new BaichuanResponse("0", "成功", obj);
    }

    public BaichuanResponse(String errorCode, String errorMessage, Object data) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("errorCode", errorCode);
        map.put("errorMessage", errorMessage);
        map.put("data", data);
        return map;
    }
}
