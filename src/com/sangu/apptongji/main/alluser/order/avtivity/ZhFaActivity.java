package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.SoundPlayUtils;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-18.
 */

public class ZhFaActivity extends BaseActivity {
    private EditText et_content=null;
    private Button btn_send=null;
    private ImageView sdv_first=null;
    private TextView tv_titl=null;
    private TextView tv_first_name=null;
    private TextView tv_first_content=null;
    private String dType=null,firstUId=null,fromUId=null,dynamicSeq=null,jinE=null,createTime=null;
    private String responseTime;
    private String resv1;
    private String resv2;
    private String firstDistance;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_zhuanfa_moments);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        dType = this.getIntent().getStringExtra("dType");
        jinE = this.getIntent().getStringExtra("jinE");
        firstUId = this.getIntent().getStringExtra("firstUId");
        fromUId = this.getIntent().getStringExtra("fromUId");
        dynamicSeq = this.getIntent().getStringExtra("dynamicSeq");
        createTime = this.getIntent().getStringExtra("createTime");
        responseTime = this.getIntent().getStringExtra("responseTime");
        firstDistance = this.getIntent().getStringExtra("firstDistance");
        resv1 = this.getIntent().getStringExtra("resv1");
        resv2 = this.getIntent().getStringExtra("resv2");
        String firstImage1 = this.getIntent().getStringExtra("firstImage");
        String firstImage = TextUtils.isEmpty(firstImage1)?"":firstImage1;
        String firstName = this.getIntent().getStringExtra("firstName");
        String firstContent = this.getIntent().getStringExtra("firstContent");
        et_content = (EditText) this.findViewById(R.id.et_content);
        tv_titl = (TextView) this.findViewById(R.id.tv_titl);
        btn_send = (Button) this.findViewById(R.id.btn_send);
        sdv_first = (ImageView) this.findViewById(R.id.sdv_first);
        tv_first_name = (TextView) this.findViewById(R.id.tv_first_name);
        tv_first_content = (TextView) this.findViewById(R.id.tv_first_content);
        //intent.putExtra("firstImage", finalFirstImage);
        //intent.putExtra("firstName",firstName);
        //intent.putExtra("firstContent",content);
        tv_first_name.setText(firstName);
        tv_first_content.setText(firstContent);
        if (!firstImage.equals("")){
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+firstImage,sdv_first, DemoApplication.mOptions);
            tv_titl.setVisibility(View.INVISIBLE);
        }else {
            sdv_first.setVisibility(View.INVISIBLE);
            tv_titl.setVisibility(View.VISIBLE);
            tv_titl.setText(firstName);
        }
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("zhuanfaac,jinE",jinE+"元");
                if (firstUId.equals(DemoHelper.getInstance().getCurrentUsernName())){
                    Toast.makeText(ZhFaActivity.this,"不能转发自己的动态！",Toast.LENGTH_SHORT).show();
                    return;
                }
                final String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)){
                    Toast.makeText(ZhFaActivity.this,"请先输入想说的话！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = FXConstant.URL_PUBLISH;
                final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            if (code.equals("SUCCESS")){
                                Toast.makeText(ZhFaActivity.this,"转发成功！",Toast.LENGTH_SHORT).show();
                                String timel1 = getNowTime();
                                SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mSharedPreferences.edit();
                                if (dType.equals("01")) {
                                    editor.putString("time1", timel1);
                                    editor.putString("time5", timel1);
                                }else if (dType.equals("02")){
                                    editor.putString("time2", timel1);
                                    editor.putString("time6", timel1);
                                }else if (dType.equals("03")){
                                    editor.putString("time3", timel1);
                                    editor.putString("time7", timel1);
                                }else {
                                    editor.putString("time4", timel1);
                                    editor.putString("time8", timel1);
                                }
                                editor.commit();
                                updateTJLiulancishu("转发");
                                if (!dType.equals("05")) {
                                    updateTJzhuanfa(firstUId);
                                }else {
                                    updateBmob();
                                }
                            }else {
                                Toast.makeText(ZhFaActivity.this,"转发失败！",Toast.LENGTH_SHORT).show();
                            }
                            Log.e("zhuanfaac,message",message);
                            if (jinE!=null&&!"".equals(jinE)&&Double.parseDouble(jinE)>0) {
                                if ("已经转发过次动态".equals(message)) {
                                    LayoutInflater inflater1 = LayoutInflater.from(ZhFaActivity.this);
                                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                                    final Dialog dialog1 = new AlertDialog.Builder(ZhFaActivity.this,R.style.Dialog).create();
                                    dialog1.show();
                                    dialog1.getWindow().setContentView(layout1);
                                    dialog1.setCanceledOnTouchOutside(true);
                                    dialog1.setCancelable(true);
                                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                                    TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                                    title.setText("温馨提示");
                                    btnOK1.setText("确定");
                                    btnCancel1.setText("取消");
                                    title_tv1.setText("您已经转发过此条动态,本次转发无红包奖励");
                                    btnCancel1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog1.dismiss();
                                            finish();
                                        }
                                    });
                                    btnOK1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog1.dismiss();
                                            finish();
                                        }
                                    });
                                } else {
                                    SoundPlayUtils.play(2);
                                    LayoutInflater inflaterDl = LayoutInflater.from(getApplicationContext());
                                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_hongbao, null);
                                    final Dialog dialog = new AlertDialog.Builder(ZhFaActivity.this,R.style.Dialog).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.setCancelable(true);
                                    TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
                                    TextView tv_yue = (TextView) layout.findViewById(R.id.tv_yue);
                                    tv_title.setText(jinE+"");
                                    tv_yue.setText("正事多 动态红包");
                                    layout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
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
                        Map<String,String> param = new HashMap<String, String>();
                        param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                        param.put("dType",dType);
                        param.put("firstUId",firstUId);
                        param.put("fromUId",fromUId);
                        param.put("dynamicSeq",dynamicSeq);
                        param.put("content",content);
                        return param;
                    }
                };

                UserPermissionUtil.getUserPermission(ZhFaActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "4", new UserPermissionUtil.UserPermissionListener() {
                    @Override
                    public void onAllow() {
                        MySingleton.getInstance(ZhFaActivity.this).addToRequestQueue(request);
                    }

                    @Override
                    public void onBan() {
                        ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送动态");

                    }
                });

            }
        });
    }

    private void updateTJLiulancishu(final String type) {
        String url = FXConstant.URL_JILU_LIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("chen","成功,updateTJLiulancishu="+s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("chen","失败,updateTJLiulancishu="+volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("create_time",createTime);
                param.put("dynamic_seq",dynamicSeq);
                param.put("timestamp",getNowTime());
                param.put("type",type);
                param.put("v_id",DemoHelper.getInstance().getCurrentUsernName());
                if (TextUtils.isEmpty(responseTime) || responseTime.equalsIgnoreCase("0")) {
                    String time = getMin(createTime);
                    Log.d("chen", "增加浏览time"+ time);
                    if (!TextUtils.isEmpty(time)  ) {
                        param.put("responseTime",time);
                    }
                }
                if (TextUtils.isEmpty(firstDistance) || firstDistance.equalsIgnoreCase("0")) {
                    String distance = getDistance(resv2, resv1);
                    Log.d("chen", "增加浏览distance"+ distance);
                    if (!TextUtils.isEmpty(distance)) {
                        param.put("distance",distance);
                    }
                }
                return param;
            }
        };
        if (!TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) && !TextUtils.isEmpty(resv2)) {
            MySingleton.getInstance(ZhFaActivity.this).addToRequestQueue(request);
        }
    }

    private String getDistance(String latS, String lngS) {
        String paidanLat = latS;
        String paidanLng = lngS;
        //4.9E-324  考虑到百度定位出错时 就不显示
        if (!(TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) || TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) || TextUtils.isEmpty(paidanLat) || TextUtils.isEmpty(paidanLng) ||
                paidanLat.equalsIgnoreCase("4.9E-324") || paidanLng.equalsIgnoreCase("4.9E-324"))) {
            double latitude1 = Double.valueOf(DemoApplication.getInstance().getCurrentLat());
            double longitude1 = Double.valueOf(DemoApplication.getInstance().getCurrentLng());
            final LatLng ll1 = new LatLng(Double.parseDouble(paidanLat), Double.parseDouble(paidanLng));
            LatLng ll = new LatLng(latitude1, longitude1);
            double distance = DistanceUtil.getDistance(ll, ll1);
            double dou = distance / 1000;
            String str = String.format("%.1f", dou);//format 返回的是字符串
            return str;
        } else {
            return null;
        }
    }



    private String getMin(String s) {
        if (s == null || TextUtils.isEmpty(s)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        long spaceTime = 0;
        try {
            Date date = format.parse(s);
            spaceTime = System.currentTimeMillis() - date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        double dou = (spaceTime / 1000.00) / 60;
        String str = String.format("%.1f", dou);//format 返回的是字符串
        return str;

    }

    private void updateTJzhuanfa(final String loginId) {
        String url = FXConstant.URL_TONGJI_ZHUANFA;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Userdetailac,add","增加浏览次数成功"+s);
                updateBmob();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null) {
                    Log.e("Userdetailac,add", "增加浏览次数错误");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uLoginId",loginId);
                if ("01".equals(dType) || "05".equals(dType)) {
                    param.put("lifeDynamics", "1");
                    param.put("type", "fwLifeDynamic");
                }else if ("02".equals(dType)){
                    param.put("locationDynamics", "1");
                    param.put("type", "fwLocationDynamic");
                }else if ("03".equals(dType)){
                    param.put("businessDynamics", "1");
                    param.put("type", "fwBusinessDynamic");
                }
                param.put("f_id",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(ZhFaActivity.this).addToRequestQueue(request);
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }
    private void updateBmob() {
        String url = FXConstant.URL_UPDATE_DYNATIME;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                setResult(RESULT_OK);
                if (jinE!=null&&!"".equals(jinE)&&Double.parseDouble(jinE)>0) {
                }else {
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setResult(RESULT_OK);
                if (jinE!=null&&!"".equals(jinE)&&Double.parseDouble(jinE)>0) {
                }else {
                    finish();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                if (dType.equals("01")){
                    param.put("type5",getNowTime());
                }else if (dType.equals("02")){
                    param.put("type6",getNowTime());
                }else if (dType.equals("03")){
                    param.put("type7",getNowTime());
                }else if (dType.equals("05")){
                    param.put("type8",getNowTime());
                }
                return param;
            }
        };
        MySingleton.getInstance(ZhFaActivity.this).addToRequestQueue(request);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
}
