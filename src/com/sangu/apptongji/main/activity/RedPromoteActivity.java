package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuMLocationActivity;
import com.sangu.apptongji.main.alluser.presenter.IFindPresenter;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.FindPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IFindView;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;
import com.sangu.apptongji.widget.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2018-09-27.
 */

public class RedPromoteActivity extends BaseActivity implements IFindView,IUAZView, View.OnClickListener{

    TextView tv_authTitle,tv_redBalance,tv_withDrawal,tv_redCount,tv_promote,tv_promoteTitle,
    tv_commit,tv_lastTitle,tv_remark;
    RelativeLayout r1,r2;
    EditText et_phone;
    String redPromoteBalance="0";
    int redPromoteCount=0;
    private Dialog mWeiboDialog;
    String promoteType = "0";

    private RelativeLayout rl_commentStore,rl_recommend,rl_shareLocation;
    private IFindPresenter findPresenter=null;
    private List<UserAll> userArr = new ArrayList<>();

    private IUAZPresenter uazPresenter;

    //派单人用户信息相关
    private CircleImageView iv_head;
    private TextView tv_name;
    private TextView tv_titl;
    private TextView tv_nianling;
    private TextView tv_company_count;
    private TextView tv_company;
    private TextView tv_distance;
    private TextView tv_project_one;
    private TextView tv_project_two;
    private TextView tv_project_three;
    private TextView tv_project_four;
    private TextView tv_zy1_bao;
    private TextView tv_zy2_bao;
    private TextView tv_zy3_bao;
    private TextView tv_zy4_bao;
    private TextView tv_qianming;
    private TextView iv_zy1_tupian;
    private TextView iv_zy2_tupian;
    private TextView iv_zy3_tupian;
    private TextView iv_zy4_tupian;
    private ImageView iv_sex;
    LinearLayout ll_one,ll_two,ll_three,ll_four;

    private TextView tv_pushstore,tv_pushDownLoad,tv_wechatclick;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_redpromote);
        WeakReference<RedPromoteActivity> reference =  new WeakReference<RedPromoteActivity>(RedPromoteActivity.this);
        findPresenter = new FindPresenter(RedPromoteActivity.this,reference.get());
        String lat = DemoApplication.getInstance().getCurrentLat();
        String lng = DemoApplication.getInstance().getCurrentLng();
        findPresenter.loadUserList(DemoHelper.getInstance().getCurrentUsernName(), "1", "1", lng + "", lat + "", null, null, null, null, null, null, null, false, false, false);

        uazPresenter = new UAZPresenter(this,this);
        uazPresenter.loadThisDetail(DemoHelper.getInstance().getCurrentUsernName());

        initView();

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(RedPromoteActivity.this, "加载中...");

        GetUserRedPromoteRecordInfo();
        SelectNotice();

    }

    @Override
    protected void onResume() {
        super.onResume();

        GetUserRedPromoteInfo();

    }

    private void initView(){

        tv_authTitle = (TextView) findViewById(R.id.tv_authTitle);
        tv_redBalance = (TextView) findViewById(R.id.red_balance);
       // tv_withDrawal = (TextView) findViewById(R.id.tv_withdrawal);
       // tv_redCount = (TextView) findViewById(R.id.red_count);
       // tv_promote = (TextView) findViewById(R.id.tv_promote);
        tv_promoteTitle = (TextView) findViewById(R.id.tv_promoteTitle);
       // tv_commit = (TextView) findViewById(R.id.tv_commit);
       // tv_lastTitle = (TextView) findViewById(R.id.tv_lastTitle);
      //  r1 = (RelativeLayout) findViewById(R.id.rl_editPhone);
      //  r2 = (RelativeLayout) findViewById(R.id.rl_commit);
      //  et_phone = (EditText) findViewById(R.id.et_phone);
     //   tv_remark = (TextView) findViewById(R.id.tv_remark);


        rl_commentStore = (RelativeLayout) findViewById(R.id.rl_commentStore);
        rl_recommend = (RelativeLayout) findViewById(R.id.rl_recommend);
        rl_shareLocation = (RelativeLayout) findViewById(R.id.rl_shareLocation);


        tv_pushstore = (TextView) findViewById(R.id.tv_pushstore);
        tv_pushDownLoad = (TextView) findViewById(R.id.tv_pushDownLoad);
        tv_wechatclick = (TextView) findViewById(R.id.tv_wechatclick);

        tv_pushstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RedPromoteActivity.this,ZBAuthTaskActivity.class);
                intent.putExtra("type","0");//点击的评论任务
                startActivityForResult(intent,0);
            }
        });

        tv_pushDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RedPromoteActivity.this,ZBAuthTaskActivity.class);
                intent.putExtra("type","1");//点击的下载任务
                startActivityForResult(intent,0);
            }
        });

        tv_wechatclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RedPromoteActivity.this,ZBAuthTaskActivity.class);
                intent.putExtra("type","2");//点击的点赞任务
                startActivityForResult(intent,0);
            }
        });

      //  tv_withDrawal.setOnClickListener(this);
      //  tv_promote.setOnClickListener(this);
        tv_redBalance.setOnClickListener(this);
        rl_recommend.setOnClickListener(this);
        rl_commentStore.setOnClickListener(this);
        rl_shareLocation.setOnClickListener(this);
    }

    //查询分享图文还是链接
    private void SelectNotice(){

        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");

                promoteType = object1.getString("promoteType");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("deviceType","android0");
                return param;
            }
        };
        MySingleton.getInstance(RedPromoteActivity.this).addToRequestQueue(request);
    }

    //查询推荐补贴信息（查询merAccount表就是）
    private void GetUserRedPromoteInfo(){

        String url = FXConstant.URL_Query_YuE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    WeiboDialogUtils.closeDialog(mWeiboDialog);

                    JSONObject object1 = new JSONObject(s);
                    JSONObject object = object1.getJSONObject("merAccount");

                    redPromoteBalance = object.getString("redPromoteBalance");
                    redPromoteCount = object.getInt("redPromoteCount");

                    if (redPromoteBalance.equals("0")){

                    }else{

                        tv_authTitle.setText("恭喜您的账号获得了推荐补贴");
                        tv_redBalance.setText(redPromoteBalance+" 元");

                        if (redPromoteCount == 0){
                            tv_promoteTitle.setText("您已满足提取条件");
                        }else {
                            tv_promoteTitle.setText("再推荐"+redPromoteCount+"名好友就可以获得推荐补贴");
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(RedPromoteActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(RedPromoteActivity.this).addToRequestQueue(request);

    }

    //获取用户推荐记录
    private void GetUserRedPromoteRecordInfo(){

        String url = FXConstant.URL_SELECTREDPROMOTE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    JSONObject object1 = new JSONObject(s);
                    JSONArray list = object1.getJSONArray("list"); //我推荐的
                    JSONArray list2 = object1.getJSONArray("list2"); //推荐我的

//                    if (list != null){
//
//                        tv_redCount.setText(list.length()+" 人");
//
//                    }

                    if (list2 != null){

                        if (list2.length() > 0){

//                            r1.setVisibility(View.GONE);
//                            r2.setVisibility(View.GONE);
//                            tv_lastTitle.setVisibility(View.GONE);
//                            tv_remark.setVisibility(View.VISIBLE);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(RedPromoteActivity.this).addToRequestQueue(request);

    }

    private void SelectIsInUserinfo(){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(RedPromoteActivity.this, "请稍后...");

        String url = FXConstant.URL_Get_UserInfo+et_phone.getText().toString().trim();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");

                if (code.equals("SUCCESS")){

                    InsertRedPromoteRecord();

                }else if (code.equals("用户名为空")){

                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    Toast.makeText(RedPromoteActivity.this,"请输入已注册为平台用户的手机号",Toast.LENGTH_SHORT).show();

                }else {

                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    Toast.makeText(RedPromoteActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(RedPromoteActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(RedPromoteActivity.this).addToRequestQueue(request);

    }

    private void InsertRedPromoteRecord(){

        String url = FXConstant.URL_INSERTREDPROMOTE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    JSONObject object1 = new JSONObject(s);

                    String code = object1.getString("code");
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    if (code.equals("SUCCESS")){

                        r1.setVisibility(View.GONE);
                        r2.setVisibility(View.GONE);
                        tv_lastTitle.setVisibility(View.GONE);
                        tv_remark.setVisibility(View.VISIBLE);
                        Toast.makeText(RedPromoteActivity.this,"提交信息成功",Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(RedPromoteActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    Toast.makeText(RedPromoteActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(RedPromoteActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id", et_phone.getText().toString().trim());
                param.put("promoteId", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };

        MySingleton.getInstance(RedPromoteActivity.this).addToRequestQueue(request);

    }

    //提取推荐补贴到余额
    private void WithDrawalRequest(){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(RedPromoteActivity.this, "请稍后...");

        String url = FXConstant.URL_WITHDRAWALPROMOTE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    JSONObject object1 = new JSONObject(s);

                    String code = object1.getString("code");
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    if (code.equals("success")){

                        Toast.makeText(RedPromoteActivity.this,"提取成功",Toast.LENGTH_SHORT).show();
                        finish();

                    }else {

                        Toast.makeText(RedPromoteActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    Toast.makeText(RedPromoteActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(RedPromoteActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("redPromoteBalance", redPromoteBalance);
                return param;
            }
        };

        MySingleton.getInstance(RedPromoteActivity.this).addToRequestQueue(request);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.red_balance:

                if (redPromoteBalance.equals("0")){

                    Toast.makeText(RedPromoteActivity.this,"您暂未获得推荐补贴",Toast.LENGTH_SHORT).show();

                }else {

                    if (redPromoteCount == 0){

                        LayoutInflater inflater1 = LayoutInflater.from(getApplicationContext());
                        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog1 = new AlertDialog.Builder(RedPromoteActivity.this,R.style.Dialog).create();
                        dialog1.show();
                        dialog1.getWindow().setContentView(layout1);
                        dialog1.setCanceledOnTouchOutside(true);
                        dialog1.setCancelable(true);
                        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                        title.setText("提示");
                        btnOK1.setText("确定");
                        btnCancel1.setText("取消");
                        title_tv1.setText("是否确定提取补贴");
                        btnCancel1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                        btnOK1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                WithDrawalRequest();
                                dialog1.dismiss();
                            }
                        });

                    }else {

                        Toast.makeText(RedPromoteActivity.this,"再推荐"+redPromoteCount+"名好友即可提现",Toast.LENGTH_SHORT).show();

                    }
                }

                break;

            case R.id.rl_recommend:

                ShareClick();

                break;

            case R.id.rl_commentStore:

                try {
                    Uri uri = Uri.parse("market://details?id="
                            + RedPromoteActivity.this.getPackageName());//需要评分的APP包名
                    Intent intent5 = new Intent(Intent.ACTION_VIEW, uri);
                    intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent5);

                } catch (Exception e) {
                    Toast.makeText(RedPromoteActivity.this, "跳转失败", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.rl_shareLocation:

                if (userArr.size() > 0) {

                    final List<String> strLat = new ArrayList<>();
                    final List<String> strLong = new ArrayList<>();
                    final List<String> strLoginId = new ArrayList<>();
                    final List<String> strName = new ArrayList<>();
                    final List<String> strSex = new ArrayList<>();
                    for (int i = 0; i < userArr.size(); i++) {
                        String lng = userArr.get(i).getResv1();
                        String lat = userArr.get(i).getResv2();
                        String loginId = userArr.get(i).getuLoginId();
                        String name = userArr.get(i).getuName();
                        String sex = TextUtils.isEmpty(userArr.get(i).getuSex())?"01":userArr.get(i).getuSex();
                        strLat.add(lat);
                        strLong.add(lng);
                        strLoginId.add(loginId);
                        strName.add(name);
                        strSex.add(sex);
                    }

                    Intent intent = new Intent(RedPromoteActivity.this, BaiDuMLocationActivity.class);
                    intent.putExtra("lat", (Serializable) strLat);
                    intent.putExtra("lng", (Serializable) strLong);
                    intent.putExtra("loginId", (Serializable) strLoginId);
                    intent.putExtra("name", (Serializable) strName);
                    intent.putExtra("sex", (Serializable) strSex);
                    intent.putExtra("mlat", DemoApplication.getInstance().getCurrentLat());
                    intent.putExtra("mlon", DemoApplication.getInstance().getCurrentLng());
                    intent.putExtra("redPromote", "yes");
                    startActivityForResult(intent, 1);

                }else {

                    Toast.makeText(getApplicationContext(), "数据获取错误，首页地图也可分享接单坐标", Toast.LENGTH_SHORT).show();

                }

                break;

//            case R.id.tv_commit:
//
//                String userId = et_phone.getText().toString().trim();
//                if (TextUtils.isEmpty(userId)||userId.length()!=11) {
//                    Toast.makeText(getApplicationContext(), "请输入正确格式的电话号码!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (userId.equals(DemoHelper.getInstance().getCurrentUsernName())){
//                    Toast.makeText(getApplicationContext(), "请填写除自己以外的推荐人", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                LayoutInflater inflater1 = LayoutInflater.from(getApplicationContext());
//                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
//                final Dialog dialog1 = new AlertDialog.Builder(RedPromoteActivity.this,R.style.Dialog).create();
//                dialog1.show();
//                dialog1.getWindow().setContentView(layout1);
//                dialog1.setCanceledOnTouchOutside(true);
//                dialog1.setCancelable(true);
//                TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
//                Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
//                final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
//                TextView title = (TextView) layout1.findViewById(R.id.tv_title);
//                title.setText("提示");
//                btnOK1.setText("确定");
//                btnCancel1.setText("取消");
//                title_tv1.setText("是否确定提交推荐人信息");
//                btnCancel1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog1.dismiss();
//                    }
//                });
//                btnOK1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SelectIsInUserinfo();
//                        dialog1.dismiss();
//                    }
//                });
//
//                break;

        }

    }


    private void ShareClick(){

//        uazPresenter = new UAZPresenter(this,this);
//        uazPresenter.loadThisDetail(DemoHelper.getInstance().getCurrentUsernName());

        LayoutInflater inflater5 = LayoutInflater.from(RedPromoteActivity.this);
        final RelativeLayout layout5 = (RelativeLayout) inflater5.inflate(R.layout.dialog_fenxiang, null);
        final Dialog dialog = new AlertDialog.Builder(RedPromoteActivity.this,R.style.Dialog).create();
        dialog.show();
        Window window = dialog.getWindow();
        dialog.show();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        //这句就是设置dialog横向满屏了。
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.getWindow().setContentView(layout5);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        TextView tv_title = (TextView) layout5.findViewById(R.id.tv_title);
        RelativeLayout rl1 = (RelativeLayout) layout5.findViewById(R.id.rl1);
        RelativeLayout rl2 = (RelativeLayout) layout5.findViewById(R.id.rl2);
        RelativeLayout rl3 = (RelativeLayout) layout5.findViewById(R.id.rl3);
        RelativeLayout rl4 = (RelativeLayout) layout5.findViewById(R.id.rl4);
        RelativeLayout rl5 = (RelativeLayout) layout5.findViewById(R.id.rl5);
        RelativeLayout rl6 = (RelativeLayout) layout5.findViewById(R.id.rl6);
        rl6.setVisibility(View.INVISIBLE);

        tv_title.setText("分享至");

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fenxiangtoqq();
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fenxiangtowx();
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fenxiangtowb();
            }
        });
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fenxiangtoqqf();
            }
        });
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fenxiangtowxf();
            }
        });

    }

    private void fenxiangtoqq(){

        final QZone.ShareParams sp = new QZone.ShareParams();

        if (promoteType.equals("0")){

            ScreenshotUtil.saveDrawableById(RedPromoteActivity.this,R.drawable.promoteshare);

            //图文
            sp.setText(null);
            sp.setTitle(null);
            sp.setTitleUrl(null);
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");

        }else {
            //链接

            ScreenshotUtil.saveDrawableById(RedPromoteActivity.this,R.drawable.app_logo);
            sp.setSite("分享链接");
            sp.setTitle("正事多-接单派单工具");
            sp.setTitleUrl("http://www.fulu86.com/download.html");
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
            sp.setText("帮我提高个人指数、多接单,为我的工作帮个忙,感谢!");
            sp.setSiteUrl("http://www.fulu86.com/download.html");

        }

        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qzone.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                InsertShareType();
                Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();

            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        qzone.share(sp);

    }

    private void fenxiangtoqqf(){

        final QQ.ShareParams sp = new QQ.ShareParams();

        if (promoteType.equals("0")){
            //图文
            ScreenshotUtil.saveDrawableById(RedPromoteActivity.this,R.drawable.promoteshare);
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
            sp.setTitle(null);
            sp.setText(null);

        }else {
            //链接
            ScreenshotUtil.saveDrawableById(RedPromoteActivity.this,R.drawable.app_logo);
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
            sp.setTitleUrl("http://www.fulu86.com/download.html");
            sp.setSite("分享链接");
            sp.setTitle("正事多-接单派单工具");
            sp.setText("帮我提高个人指数、多接单,为我的工作帮个忙,感谢!");
            sp.setSiteUrl("http://www.fulu86.com/download.html");
        }

        Platform qq = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qq.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                InsertShareType();
                Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();

            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        qq.share(sp);

    }

    private void fenxiangtowx(){

        final WechatMoments.ShareParams sp = new WechatMoments.ShareParams();

        if (promoteType.equals("0")){
            //图文
            ScreenshotUtil.saveDrawableById(RedPromoteActivity.this,R.drawable.promoteshare);
            sp.setShareType(Platform.SHARE_IMAGE);
            sp.setTitle("帮我提高个人指数、多接单,为我的工作帮个忙,感谢!");
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");

        }else {
            //链接
            ScreenshotUtil.saveDrawableById(RedPromoteActivity.this,R.drawable.app_logo);
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
            sp.setTitle("帮我提高个人指数、多接单,为我的工作帮个忙,感谢!");
            sp.setUrl("http://www.fulu86.com/download.html");

        }

        Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                InsertShareType();
                Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();

            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wx.share(sp);

    }

    private void fenxiangtowxf(){

        final Wechat.ShareParams sp = new Wechat.ShareParams();

        if (promoteType.equals("0")){
            //图文
            ScreenshotUtil.saveDrawableById(RedPromoteActivity.this,R.drawable.promoteshare);
            sp.setShareType(Platform.SHARE_IMAGE);
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
            sp.setTitle("帮我提高个人指数、多接单,为我的工作帮个忙,感谢!");

        }else {

            //链接
            ScreenshotUtil.saveDrawableById(RedPromoteActivity.this,R.drawable.app_logo);
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
            sp.setTitle("正事多-接单派单工具");
            sp.setText("帮我提高个人指数、多接单,为我的工作帮个忙,感谢!");
            sp.setUrl("http://www.fulu86.com/download.html");

        }

        Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                InsertShareType();
                Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();
            }

            public void onCancel(Platform arg0, int arg1) {

                Toast.makeText(getApplicationContext(), "取消分享到微信好友！", Toast.LENGTH_SHORT).show();

            }

        });

        // 执行图文分享
        wx.share(sp);

    }

    private void fenxiangtowb(){

        final SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();

        if (promoteType.equals("0")){
            //图文
            ScreenshotUtil.saveDrawableById(RedPromoteActivity.this,R.drawable.promoteshare);
            sp.setShareType(Platform.SHARE_IMAGE);
            sp.setTitle("帮我提高个人指数、多接单,为我的工作帮个忙,感谢!");
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");

        }else {
            //链接
            ScreenshotUtil.saveDrawableById(RedPromoteActivity.this,R.drawable.app_logo);
            sp.setShareType(Platform.SHARE_WEBPAGE);
            String url = "http://www.fulu86.com/download.html";
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
            sp.setText("帮我提高个人指数、多接单,为我的工作帮个忙,感谢!"+url);
            sp.setTitle("正事多-接单派单工具");

        }

        Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wb.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();
                InsertShareType();
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wb.share(sp);

    }

    private void InsertShareType(){

        String url = FXConstant.URL_INSERTCOUNTDOWN;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("type","推荐补贴android");
                param.put("uId",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(RedPromoteActivity.this).addToRequestQueue(request);
    }


    @Override
    public void updateUserList(List<UserAll> users, boolean hasMore) {

        userArr = users;

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(String mag) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void updateThisUser(Userful allUser) {

        String wechatAuth = allUser.getWechatAuth();

        if (wechatAuth.equals("1")){

            tv_wechatclick.setText("领取奖励");

        }else if (wechatAuth.equals("2"))
        {
            tv_wechatclick.setText("已完成");
        }

    }

    @Override
    public void showproLoading() {

    }

    @Override
    public void showproError() {

    }

    @Override
    public void hideproLoading() {

    }
}
