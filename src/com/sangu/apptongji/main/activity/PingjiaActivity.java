package com.sangu.apptongji.main.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-30.
 */

public class PingjiaActivity extends BaseActivity implements View.OnClickListener {
    EditText et_pingjia=null;
    TextView tv_pinglin_zhuangtai=null;
    ImageView iv1=null;
    ImageView iv2=null;
    ImageView iv3=null;
    ImageView iv4=null;
    ImageView iv5=null;
    Button btn_commit = null;
    String userId=null;
    boolean is1add = false;
    boolean is2add = false;
    boolean is3add = false;
    boolean is4add = false;
    boolean is5add = false;
    String type="2";
    int iv1click = 2;
    int iv2click = 2;
    int iv3click = 2;
    int iv4click = 2;
    int iv5click = 2;
    int allClick = 10;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_pingjia);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        userId = this.getIntent().getStringExtra("userId");
        et_pingjia = (EditText) findViewById(R.id.et_pingjia);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        tv_pinglin_zhuangtai = (TextView) findViewById(R.id.tv_pinglin_zhuangtai);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        iv5 = (ImageView) findViewById(R.id.iv5);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv4.setOnClickListener(this);
        iv5.setOnClickListener(this);
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_pingjia.getText().toString().trim())){
                    Toast.makeText(PingjiaActivity.this,"请先输入评价内容！",Toast.LENGTH_SHORT).show();
                    return;
                }
                updatePingjia();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv1:
                iv2click=0;
                iv3click=0;
                iv4click=0;
                iv5click=0;
                iv2.setImageResource(R.drawable.goods_none);
                iv3.setImageResource(R.drawable.goods_none);
                iv4.setImageResource(R.drawable.goods_none);
                iv5.setImageResource(R.drawable.goods_none);
                if (iv1click<2&&is1add||iv1click==0) {
                    iv1click++;
                    if (iv1click==2){
                        is1add=false;
                    }
                    if (iv1click==0){
                        iv1.setImageResource(R.drawable.goods_none);
                    }
                    if (iv1click==1){
                        iv1.setImageResource(R.drawable.goods_half);
                    }
                    if (iv1click==2){
                        iv1.setImageResource(R.drawable.goods_full);
                    }
                }else if (!is1add||iv1click==2){
                    iv1click--;
                    if (iv1click==0){
                        is1add=true;
                    }
                    if (iv1click==0){
                        iv1.setImageResource(R.drawable.goods_none);
                    }
                    if (iv1click==1){
                        iv1.setImageResource(R.drawable.goods_half);
                    }
                    if (iv1click==2){
                        iv1.setImageResource(R.drawable.goods_full);
                    }
                }
                allClick = iv1click+iv2click+iv3click+iv4click+iv5click;
                type = "0";
                tv_pinglin_zhuangtai.setText("差评");
                break;
            case R.id.iv2:
                iv1click=2;
                iv3click=0;
                iv4click=0;
                iv5click=0;
                iv1.setImageResource(R.drawable.goods_full);
                iv3.setImageResource(R.drawable.goods_none);
                iv4.setImageResource(R.drawable.goods_none);
                iv5.setImageResource(R.drawable.goods_none);
                Log.e("iv2click,1",iv2click+""+is2add);
                if (iv2click<2&&is2add||iv2click==0) {
                    iv2click++;
                    if (iv2click==2){
                        is2add=false;
                    }
                    Log.e("iv2click,2",iv2click+"");
                    if (iv2click==0){
                        iv2.setImageResource(R.drawable.goods_none);
                    }
                    if (iv2click==1){
                        iv2.setImageResource(R.drawable.goods_half);
                    }
                    if (iv2click==2){
                        iv2.setImageResource(R.drawable.goods_full);
                    }
                }else if(!is2add||iv2click==2){
                    iv2click--;
                    if (iv2click==0){
                        is2add=true;
                    }
                    Log.e("iv2click,2",iv2click+"");
                    if (iv2click==0){
                        iv2.setImageResource(R.drawable.goods_none);
                    }
                    if (iv2click==1){
                        iv2.setImageResource(R.drawable.goods_half);
                    }
                    if (iv2click==2){
                        iv2.setImageResource(R.drawable.goods_full);
                    }
                }
                allClick = iv1click+iv2click+iv3click+iv4click+iv5click;
                if (allClick==3) {
                    type = "0";
                    tv_pinglin_zhuangtai.setText("差评");
                }else if (allClick==4){
                    type = "1";
                    tv_pinglin_zhuangtai.setText("中评");
                }
                break;
            case R.id.iv3:
                iv1click=2;
                iv2click=2;
                iv4click=0;
                iv5click=0;
                iv1.setImageResource(R.drawable.goods_full);
                iv2.setImageResource(R.drawable.goods_full);
                iv4.setImageResource(R.drawable.goods_none);
                iv5.setImageResource(R.drawable.goods_none);
                if (iv3click<2&&is3add||iv3click==0) {
                    iv3click++;
                    if (iv3click==2){
                        is3add=false;
                    }
                }else if (!is3add||iv3click==2){
                    iv3click--;
                    if (iv3click==0){
                        is3add=true;
                    }
                }
                if (iv3click==0){
                    iv3.setImageResource(R.drawable.goods_none);
                }
                if (iv3click==1){
                    iv3.setImageResource(R.drawable.goods_half);
                }
                if (iv3click==2){
                    iv3.setImageResource(R.drawable.goods_full);
                }
                allClick = iv1click+iv2click+iv3click+iv4click+iv5click;
                type = "1";
                tv_pinglin_zhuangtai.setText("中评");
                break;
            case R.id.iv4:
                iv1click=2;
                iv2click=2;
                iv3click=2;
                iv5click=0;
                iv1.setImageResource(R.drawable.goods_full);
                iv2.setImageResource(R.drawable.goods_full);
                iv3.setImageResource(R.drawable.goods_full);
                iv5.setImageResource(R.drawable.goods_none);
                if (iv4click<2&&is4add||iv4click==0) {
                    iv4click++;
                    if (iv4click==2){
                        is4add=false;
                    }
                }else if (!is4add||iv4click==2){
                    iv4click--;
                    if (iv4click==0){
                        is4add=true;
                    }
                }
                if (iv4click==0){
                    iv4.setImageResource(R.drawable.goods_none);
                }
                if (iv4click==1){
                    iv4.setImageResource(R.drawable.goods_half);
                }
                if (iv4click==2){
                    iv4.setImageResource(R.drawable.goods_full);
                }
                allClick = iv1click+iv2click+iv3click+iv4click+iv5click;
                if (allClick==7) {
                    type = "1";
                    tv_pinglin_zhuangtai.setText("中评");
                }else if (allClick==8){
                    type = "2";
                    tv_pinglin_zhuangtai.setText("好评");
                }
                break;
            case R.id.iv5:
                iv1click=2;
                iv2click=2;
                iv3click=2;
                iv4click=2;
                iv1.setImageResource(R.drawable.goods_full);
                iv2.setImageResource(R.drawable.goods_full);
                iv3.setImageResource(R.drawable.goods_full);
                iv4.setImageResource(R.drawable.goods_full);
                if (iv5click<2&&is5add||iv5click==0) {
                    iv5click++;
                    if (iv5click==2){
                        is5add=false;
                    }
                }else if (!is5add||iv5click==2){
                    iv5click--;
                    if (iv5click==0){
                        is5add=true;
                    }
                }
                if (iv5click==0){
                    iv5.setImageResource(R.drawable.goods_none);
                }
                if (iv5click==1){
                    iv5.setImageResource(R.drawable.goods_half);
                }
                if (iv5click==2){
                    iv5.setImageResource(R.drawable.goods_full);
                }
                type = "2";
                allClick = iv1click+iv2click+iv3click+iv4click+iv5click;
                tv_pinglin_zhuangtai.setText("好评");
                break;
        }
    }

    private void updatePingjia() {
        final String content = et_pingjia.getText().toString().trim();
        Userful user = DemoApplication.getInstance().getCurrentUser();
        final String name = user.getName();
        final String id = user.getLoginId();
        String url = FXConstant.URL_PINJIA;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("pingjiaac,s",s);
                JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                String commentTimes = object.getString("commentTimes");
                if ("SUCCESS".equals(code)){
                    if ("2".equals(type)&&"0".equals(commentTimes)) {
                        Log.e("pingjiaac,s","增加次数");
                        updateHbTimes(userId);
                    }else {
                        Log.e("pingjiaac,s","不增加次数");
                        Toast.makeText(getApplicationContext(),"评价成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"评价失败！",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络连接中断",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("userId",userId);
                param.put("commentId",id);
                param.put("commentName",name);
                param.put("commentType",type);
                param.put("content",content);
                param.put("starCount",allClick+"");
                return param;
            }
        };
        MySingleton.getInstance(PingjiaActivity.this).addToRequestQueue(request);
    }

    private void updateHbTimes(final String uId) {
        String url = FXConstant.URL_XIUGAI_HBTIMES;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getApplicationContext(),"评价成功！",Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("uLoginId", uId);
                param.put("shareTimes", "1");
                param.put("homePageTimes", "1");
                param.put("dynamicTimes", "1");
                return param;
            }
        };
        MySingleton.getInstance(PingjiaActivity.this).addToRequestQueue(request);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

}
