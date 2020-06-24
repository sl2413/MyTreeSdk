package com.shenl.mytree.Share;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenl.mytree.R;
import com.shenl.mytree.Utils.ScreenSizeUtils;
import com.shenl.mytree.Utils.WxUtils;
import java.util.ArrayList;
import java.util.List;

public class ShareUtils {

    //分享平台
    public static final String WEIXIN = "WeiXin";

    private List<ShareBean> list;
    /**
     * TODO 功能：构造方法
     *
     * 参数说明:
     * pro ==> 分享的平台数组
     * 作    者:   沈  亮
     * 创建时间:   2020/6/23
     */
    public ShareUtils(String[] pro){
        if (pro.length != 0){
            list = new ArrayList<>();
            for (int i=0;i<pro.length;i++){
                //添加微信平台
                if (pro[i].equals(WEIXIN)){
                    ShareBean bean1 = new ShareBean();
                    bean1.icon = R.drawable.session;
                    bean1.text = "微信好友";
                    bean1.type = WxUtils.SESSION;
                    bean1.proName = WEIXIN;
                    ShareBean bean2 = new ShareBean();
                    bean2.icon = R.drawable.timeline;
                    bean2.text = "朋友圈";
                    bean2.type = WxUtils.TIMELINE;
                    bean2.proName = WEIXIN;
                    list.add(bean1);
                    list.add(bean2);
                }
            }
        }else{
            System.out.println("必须设置要分享的平台");
        }
    }

    /**
     * TODO 功能：显示分享对话框
     *
     * 参数说明:
     * 作    者:   沈  亮
     * 创建时间:   2020/6/23
     */
    public void ShowShare(Context context, final onItemClickListener ItemClickListener){
        final Dialog dialog = new Dialog(context, R.style.NormalDialogStyle);
        View view = View.inflate(context, R.layout.share_dialog, null);
        LinearLayout ll_pro = view.findViewById(R.id.ll_pro);
        for (int i = 0; i<list.size(); i++){
            final ShareBean shareBean = list.get(i);
            View itemView = View.inflate(context, R.layout.item_pro, null);
            ImageView iv_ProIcon = itemView.findViewById(R.id.iv_ProIcon);
            TextView tv_ProName = itemView.findViewById(R.id.tv_ProName);
            iv_ProIcon.setImageResource(shareBean.icon);
            tv_ProName.setText(shareBean.text);
            itemView.setPadding(0,0,50,0);
            ll_pro.addView(itemView);
            //子条目点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    ItemClickListener.onItemClick(shareBean);
                }
            });
        }
        dialog.setContentView(view);

        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(context).getScreenWidth() * 1f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    //分享平台列表Bean类
    public class ShareBean{
        public int icon;
        public int type;
        public String text;
        public String proName;
    }

    //分享平台点击事件
    public interface onItemClickListener{
        void onItemClick(ShareBean bean);
    }
}
