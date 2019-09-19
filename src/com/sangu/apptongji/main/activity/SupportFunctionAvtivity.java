package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.ui.BaseActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.moments.WechatMoments;

public class SupportFunctionAvtivity extends BaseActivity implements View.OnClickListener {

    private TextView type1 = null, type2 = null, type3 = null, type4 = null, type5 = null, type6 = null, type7 = null, type8 = null, type9 = null;
    private Button supportBtn1 = null, supportBtn2 = null, supportBtn3 = null, supportBtn4 = null, supportBtn5 = null, supportBtn6 = null, supportBtn7 = null, supportBtn8 = null, supportBtn9 = null;
    private Button shareBtn1 = null, shareBtn2 = null, shareBtn3 = null, shareBtn4 = null, shareBtn5 = null, shareBtn6 = null, shareBtn7 = null, shareBtn8 = null, shareBtn9 = null;
    private String clickType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_function);

        //初始化控件赋值
        initView();

    }

    private void initView() {

        type1 = (TextView) findViewById(R.id.type1);
        type2 = (TextView) findViewById(R.id.type2);
        type3 = (TextView) findViewById(R.id.type3);
        type4 = (TextView) findViewById(R.id.type4);
        type5 = (TextView) findViewById(R.id.type5);
        type6 = (TextView) findViewById(R.id.type6);
        type7 = (TextView) findViewById(R.id.type7);
        type8 = (TextView) findViewById(R.id.type8);
        type9 = (TextView) findViewById(R.id.type9);

        supportBtn1 = (Button) findViewById(R.id.supportBtn1);
        supportBtn2 = (Button) findViewById(R.id.supportBtn2);
        supportBtn3 = (Button) findViewById(R.id.supportBtn3);
        supportBtn4 = (Button) findViewById(R.id.supportBtn4);
        supportBtn5 = (Button) findViewById(R.id.supportBtn5);
        supportBtn6 = (Button) findViewById(R.id.supportBtn6);
        supportBtn7 = (Button) findViewById(R.id.supportBtn7);
        supportBtn8 = (Button) findViewById(R.id.supportBtn8);
        supportBtn9 = (Button) findViewById(R.id.supportBtn9);

        shareBtn1 = (Button) findViewById(R.id.shareBtn1);
        shareBtn2 = (Button) findViewById(R.id.shareBtn2);
        shareBtn3 = (Button) findViewById(R.id.shareBtn3);
        shareBtn4 = (Button) findViewById(R.id.shareBtn4);
        shareBtn5 = (Button) findViewById(R.id.shareBtn5);
        shareBtn6 = (Button) findViewById(R.id.shareBtn6);
        shareBtn7 = (Button) findViewById(R.id.shareBtn7);
        shareBtn8 = (Button) findViewById(R.id.shareBtn8);
        shareBtn9 = (Button) findViewById(R.id.shareBtn9);

        supportBtn1.setOnClickListener(this);
        supportBtn2.setOnClickListener(this);
        supportBtn3.setOnClickListener(this);
        supportBtn4.setOnClickListener(this);
        supportBtn5.setOnClickListener(this);
        supportBtn6.setOnClickListener(this);
        supportBtn7.setOnClickListener(this);
        supportBtn8.setOnClickListener(this);
        supportBtn9.setOnClickListener(this);

        shareBtn1.setOnClickListener(this);
        shareBtn2.setOnClickListener(this);
        shareBtn3.setOnClickListener(this);
        shareBtn4.setOnClickListener(this);
        shareBtn5.setOnClickListener(this);
        shareBtn6.setOnClickListener(this);
        shareBtn7.setOnClickListener(this);
        shareBtn8.setOnClickListener(this);
        shareBtn9.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.supportBtn1:
                clickType = "01";
                SupportClick();
                break;
            case R.id.supportBtn2:
                clickType = "02";
                SupportClick();
                break;
            case R.id.supportBtn3:
                clickType = "03";
                SupportClick();
                break;
            case R.id.supportBtn4:
                clickType = "04";
                SupportClick();
                break;
            case R.id.supportBtn5:
                clickType = "05";
                SupportClick();
                break;
            case R.id.supportBtn6:
                clickType = "06";
                SupportClick();
                break;
            case R.id.supportBtn7:
                clickType = "07";
                SupportClick();
                break;
            case R.id.supportBtn8:
                clickType = "08";
                SupportClick();
                break;
            case R.id.supportBtn9:
                clickType = "09";
                SupportClick();
                break;
            case R.id.shareBtn1:
                clickType = "01";
                ShareClick();
                break;
            case R.id.shareBtn2:
                clickType = "02";
                ShareClick();
                break;
            case R.id.shareBtn3:
                clickType = "03";
                ShareClick();
                break;
            case R.id.shareBtn4:
                clickType = "04";
                ShareClick();
                break;
            case R.id.shareBtn5:
                clickType = "05";
                ShareClick();
                break;
            case R.id.shareBtn6:
                clickType = "06";
                ShareClick();
                break;
            case R.id.shareBtn7:
                clickType = "07";
                ShareClick();
                break;
            case R.id.shareBtn8:
                clickType = "08";
                ShareClick();
                break;
            case R.id.shareBtn9:
                clickType = "09";
                ShareClick();
                break;
            default:
                break;
        }

    }

    //点击支持的操作
    private void SupportClick() {

        Intent intent = new Intent(SupportFunctionAvtivity.this, SupportTopActivity.class);

        intent.putExtra("clickType",clickType);

        startActivityForResult(intent, 0);

    }

    //点击分享的操作
    private void ShareClick() {

        LayoutInflater inflaterDl = LayoutInflater.from(SupportFunctionAvtivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(SupportFunctionAvtivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("QQ空间");
        tv_item2.setText("朋友圈");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                QZone.ShareParams sp = new QZone.ShareParams();

                String filep = Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/supportshare.png";
                if (!new File(filep).exists()){
                    ScreenshotUtil.saveDrawableById(SupportFunctionAvtivity.this,R.drawable.supportshare);
                }

                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitle(null);
                sp.setText(null);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//                        updateLiulancishu();
//                        updateTJzhuanfa(hxid,0);
                        Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qzone.share(sp);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                WechatMoments.ShareParams sp = new WechatMoments.ShareParams();

                String filep = Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/supportshare.png";
                if (!new File(filep).exists()){
                    ScreenshotUtil.saveDrawableById(SupportFunctionAvtivity.this,R.drawable.supportshare);
                }

                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");

                sp.setTitle("");
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);

// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//                        updateLiulancishu();
//                        updateTJzhuanfa(hxid,1);
                        Toast.makeText(getApplicationContext(), "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);

            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        //每次进入刷新次数
        querySupportCount();

    }


    //查询当前平台所有支持量
    private void querySupportCount() {

        String url = FXConstant.URL_QUERY_SUPPORTCOUNT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);

                type1.setText("1、信息奖赏"+object.getString("type1")+"次");
                type2.setText("2、交易奖赏"+object.getString("type2")+"次");
                type3.setText("3、销售奖赏"+object.getString("type3")+"次");
                type4.setText("4、电子单据"+object.getString("type4")+"次");
                type5.setText("5、接单奖赏"+object.getString("type5")+"次");
                type6.setText("6、名片红包"+object.getString("type6")+"次");
                type7.setText("7、管理考勤"+object.getString("type7")+"次");
                type8.setText("8、聊天定位"+object.getString("type8")+"次");
                type9.setText("9、定制开发"+object.getString("type9")+"次");

                //   finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("support,e",volleyError.toString());
                //finish();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        MySingleton.getInstance(SupportFunctionAvtivity.this).addToRequestQueue(request);

    }

}
