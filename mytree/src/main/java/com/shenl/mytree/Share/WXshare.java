package com.shenl.mytree.Share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;
import com.shenl.mytree.Utils.WxUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class WXshare {

    private static IWXAPI api;
    private static final int THUMB_SIZE = 150;

    /**
     * TODO 功能：微信文本分享
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/24
     */
    public static void ShareText(Context context, int scene, String text) {
        if (api == null) {
            api = WxUtils.RegToWx(context);
        }
        //初始化一个 WXTextObject 对象，填写分享的文本内容
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = scene;
        //调用api接口，发送数据到微信
        api.sendReq(req);
    }

    /**
     * TODO 功能：微信图片分享(位图)
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/25
     */
    public static void ShareImage(Context context, int scene, Bitmap bmp) {
        if (api == null) {
            api = WxUtils.RegToWx(context);
        }
            //初始化 WXImageObject 和 WXMediaMessage 对象
            WXImageObject imgObj = new WXImageObject(bmp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;
            //设置缩略图
//            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//            bmp.recycle();
            msg.thumbData = bmpToByteArray(bmp, true);
            //构造一个Req
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = scene;
            //调用api接口，发送数据到微信
            api.sendReq(req);
    }

    /**
     * TODO 功能：微信图片分享(路径)
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/25
     */
    public static void ShareImage(Context context, int scene, String path) {
        if (api == null) {
            api = WxUtils.RegToWx(context);
        }
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(context, "文件不存在", Toast.LENGTH_LONG).show();
            return;
        }
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }

    /**
     * TODO 功能：微信音频分享
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/25
     */
    public static void ShareMusic(Context context, int scene, String url, String title, String desc, Bitmap ScaledBitmap) {
        if (api == null) {
            api = WxUtils.RegToWx(context);
        }
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = url;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = desc;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(ScaledBitmap, THUMB_SIZE, THUMB_SIZE, true);
        ScaledBitmap.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }

    /**
     * TODO 功能：微信视频分享
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/25
     */
    public static void ShareVideo(Context context, int scene, String url, String title, String desc, Bitmap ScaledBitmap) {
        if (api == null) {
            api = WxUtils.RegToWx(context);
        }
        //初始化一个WXVideoObject，填写url
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = url;

        //用 WXVideoObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = title;
        msg.description = desc;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(ScaledBitmap, THUMB_SIZE, THUMB_SIZE, true);
        ScaledBitmap.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = scene;

        //调用api接口，发送数据到微信
        api.sendReq(req);
    }

    /**
     * TODO 功能：微信链接分享
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/25
     */
    public static void ShareUrl(Context context, int scene, String url, String title, String desc, Bitmap ScaledBitmap) {
        if (api == null) {
            api = WxUtils.RegToWx(context);
        }
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl =url;

        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title =title;
        msg.description =desc;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(ScaledBitmap, THUMB_SIZE, THUMB_SIZE, true);
        ScaledBitmap.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message =msg;
        req.scene =scene;

        //调用api接口，发送数据到微信
        api.sendReq(req);
    }

    /**
     * TODO 功能：生成通讯ID
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/24
     */
    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * TODO 功能：位图转二进制
     * <p>
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/2/25
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
