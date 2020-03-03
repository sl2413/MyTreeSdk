package com.shenl.mytree.Utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    /**
     * TODO 功能：get网络请求
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/25
     */
    public static void get(final String path, final HttpCallBack httpCallBack){
        new Thread(){
            public void run() {
                try {
                    URL url=new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(3000);
                    //判断服务器发来的响应码
                    if (conn.getResponseCode()==200) {
                        InputStream is = conn.getInputStream();
                        //使用工具类得到流里的文本
                        String text = getTextFromStream(is);
                        httpCallBack.Success(text);
                    }else{
                        httpCallBack.Fail(conn.getResponseCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * TODO 功能：网络流转文本
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/25
     */
    public static String getTextFromStream(InputStream is){
        byte[] b = new byte[1024];
        int len;
        //这是一个比较特殊的流叫做字节数据流，它可以直接把内容写到内存中，在内存中进行操作
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while((len = is.read(b)) != -1){
                bos.write(b, 0, len);
            }
            //把字节数组输出流转换成字节数组，然后用字节数组构造一个字符串
            String text = new String(bos.toByteArray());
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //网络请求回调方法
    public interface HttpCallBack{
        void Success(String s);
        void Fail(int ResponseCode);
    }
}
