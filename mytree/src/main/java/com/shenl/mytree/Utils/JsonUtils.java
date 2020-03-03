package com.shenl.mytree.Utils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
    /**
     * TODO 功能：获取json串中某个字段的值，注意，只能获取同一层级的value
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/26
     */
    public static String getFieldValue(String json, String key) {
        if (TextUtils.isEmpty(json))
            return null;
        if (!json.contains(key))
            return "";
        JSONObject jsonObject = null;
        String value = null;
        try {
            jsonObject = new JSONObject(json);
            value = jsonObject.getString(key);

        } catch (JSONException e) {
            System.out.print("报错的key:"+key);
            e.printStackTrace();
        }
        return value;
    }
}
