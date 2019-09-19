package com.sangu.apptongji.main.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018-08-17.
 */

public class MessageOrderIntroduceActivity extends BaseActivity {

    private TextView remainText;
    private TextView allCountText;
    private TextView sendOrderText;
    private TextView tv_identify;
    private Button midTopBtn,orderSetBtn;
    private ImageView image_advert;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_messageorder_introduce);

        initView();

        GetNotice();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //通过查询用户信息获取用户当前短信派单量
        GetUserInfo();

    }


    private void GetUserInfo(){

        String url = FXConstant.URL_Get_UserInfo + DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");

                JSONObject userInfo = object.getJSONObject("userInfo");

                String allCount = userInfo.getString("messageOrderAll");
                String remainCount = userInfo.getString("messageOrderCount");

                allCountText.setText(allCount);
                remainText.setText(remainCount);

                Integer send = Integer.valueOf(allCount) - Integer.valueOf(remainCount);

                sendOrderText.setText(send.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MySingleton.getInstance(MessageOrderIntroduceActivity.this).addToRequestQueue(request);

    }

    private void initView(){

        remainText = (TextView) findViewById(R.id.tv_remain);
        allCountText = (TextView) findViewById(R.id.tv_allCount);
        sendOrderText = (TextView) findViewById(R.id.tv_sendOrder);
        tv_identify = (TextView) findViewById(R.id.tv_identify);

        image_advert = (ImageView) findViewById(R.id.image_advert);

        String string = "客户姓名+电话+/需要专业描述/距离范围。<br /><font color='red'>对比：</font>做过竞价排名的应该知道，对比点击—转化咨询—转化成交率，仅仅点击就需要支付多大的成本！<br /><font color='red'>优势：</font>直接派单，没有点击成本，不需要优化技术，只需要做一点就是订单成交率。<br /><font color='red'>操作：</font>只需要设置接单专业，和接单价格就够 不需要SEO优化、不需要SEM推广操作。<br /><font color='red'>规则：</font>充值派单短信费不支持退款，不过期。";
        tv_identify.setText(Html.fromHtml(string));

        midTopBtn = (Button) findViewById(R.id.btn_topupBtn);

        midTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MessageOrderIntroduceActivity.this, MessageOrderTopActivity.class);

                startActivityForResult(intent, 0);

            }
        });

        orderSetBtn = (Button) findViewById(R.id.btn_orderSet);

        orderSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转接单设置
                startActivity(new Intent(MessageOrderIntroduceActivity.this, JieDanSettingActivity.class));

            }
        });
    }


    //查询广告
    private void GetNotice(){

        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");
                SharedPreferences sp = getSharedPreferences("sangu_gonggao_info", Context.MODE_PRIVATE);

                String type = object1.getString("type2");

                if (type.equals("1")){

                    String url = FXConstant.URL_ADVERTURL+object1.getString("image2");

                    ImageLoader.getInstance().displayImage(url,image_advert);

                    image_advert.setVisibility(View.VISIBLE);

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
                param.put("deviceType","payadvert");
                return param;
            }
        };

        MySingleton.getInstance(MessageOrderIntroduceActivity.this).addToRequestQueue(request);

    }

}
