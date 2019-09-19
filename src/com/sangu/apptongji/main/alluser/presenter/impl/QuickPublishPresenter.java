package com.sangu.apptongji.main.alluser.presenter.impl;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.activity.PaidanListActivity;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.IQuickPublishModel;
import com.sangu.apptongji.main.alluser.model.impl.QuickPublishModel;
import com.sangu.apptongji.main.alluser.presenter.IQuickPublishPresenter;
import com.sangu.apptongji.main.moments.QuickPublishActivity;

/**
 * Created by Administrator on 2018-01-16.
 */

public class QuickPublishPresenter implements IQuickPublishPresenter {
    private IQuickPublishModel priceModel;
    private Context context;

    public QuickPublishPresenter(Context context) {
        this.context = context;
        this.priceModel = new QuickPublishModel(context);
    }

    @Override
    public void quickPublish(String userid,String title,String msg,String lng,String lat) {
        priceModel.quickSend(userid,title,msg,lng,lat, new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object success) {
                showDialogWithMsg("发送成功", 1,PaidanListActivity.class);
                Log.d("chen", "发送成功" + success);
            }

            @Override
            public void onError(Object error) {
                showDialogWithMsg("发送失败", 1,null);
                Log.d("chen", "发送失败" + error);
            }
        });
    }

    private void showDialogWithMsg(String s, final int i, final Class clazz) {
        LayoutInflater inflater2 = LayoutInflater.from(context);
        RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_paidan, null);
        final Dialog dialog2 = new AlertDialog.Builder(context, R.style.Dialog).create();
        dialog2.show();
        dialog2.getWindow().setContentView(layout2);
        dialog2.setCanceledOnTouchOutside(true);
        dialog2.setCancelable(true);
        TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
        final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
        TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
        title2.setText("提示:");
        btnOK2.setText("确定");
        title_tv2.setText(s);
        if (clazz == null) {
            btnOK2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog2.dismiss();
                    Intent intent=new Intent();
                    intent.setAction("com.broadcast.reflash");
                    context.sendBroadcast(intent);
                    ((QuickPublishActivity)context).finish();
                }
            });

        } else {
            btnOK2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog2.dismiss();
                    context.startActivity(new Intent(context, clazz).putExtra("type",i).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    ((QuickPublishActivity)context).finish();
                }
            });

        }
    }

    @Override
    public void quickUpdate(String userid,String sid,String title,String display) {
        priceModel.quickUpdate(userid,sid,title,display, new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object success) {
                showDialogWithMsg("修改成功", 1,null);
            }

            @Override
            public void onError(Object error) {
                showDialogWithMsg("修改成功", 1,null);
                Log.d("chen", "发送失败" + error);
            }
        });
    }

    @Override
    public void quickVoicePublish(String uId, String content, String file, String lng, String lat) {
        priceModel.quickVoicePublish(uId,content,file,lng,lat,new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object success) {
                showDialogWithMsg("发送成功",1,PaidanListActivity.class);
            }

            @Override
            public void onError(Object error) {
                showDialogWithMsg("发送失败", 1,null);
                Log.d("chen", "发送失败" + error);
            }
        });
    }

    @Override
    public void quickVoiceUpdate(String uId, String sid,String content, String file, String display) {
        priceModel.quickVoiceUpdate(uId,content,file,sid,display,new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object success) {
                showDialogWithMsg("修改成功", 1,null);
            }

            @Override
            public void onError(Object error) {
                showDialogWithMsg("发送失败", 1,null);
                Log.d("chen", "发送失败" + error);
            }
        });
    }
}
