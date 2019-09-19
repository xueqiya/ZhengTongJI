package com.sangu.apptongji.main.alluser.order.avtivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.fanxin.easeui.EaseConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.IProfilePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.ProfilePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.alluser.view.IProfileView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.widget.FXPopWindow;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.ChatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-02.
 */

public class SelfYuEActivity extends BaseActivity implements View.OnClickListener,IPriceView,IProfileView{
    private TextView tv_yuE=null,tv_bktx_yuE;
    private TextView tv_name=null;
    private TextView tv_mingxi=null;
    private TextView tv_loginId=null;
    private TextView tvTitleA=null,tv_kefu;
    private ImageView ivHead=null;
    private RelativeLayout re_my_xinxi=null;
    private RelativeLayout re_my_yue=null;
    private RelativeLayout re_my_chongzhi=null;
    private RelativeLayout re_my_tixian=null;
    private RelativeLayout re_my_zhuanzhang=null;
    private RelativeLayout re_zhifu_set=null;
    private RelativeLayout re_zhifu_xiugai=null;
    private RelativeLayout re_zhifu_wangji=null;
    private RelativeLayout re_buke_tixian=null;
    private String pass,avatar1,name,biaoshi,id,hasbzj;
    double yuE;
    FXPopWindow fxPopWindow;
    private IPricePresenter pricePresenter;
    private IProfilePresenter profilePresenter;
    String url=null,data;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_yue_self);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        pricePresenter = new PricePresenter(this,this);
        profilePresenter = new ProfilePresenter(this,this);
        initView();
        hasbzj = this.getIntent().getStringExtra("baozhengjin");
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        id = DemoHelper.getInstance().getCurrentUsernName();
        profilePresenter.updateData();
        queryData();
        fxPopWindow=new FXPopWindow(this,R.layout.layout_buttom_dialog,new FXPopWindow.OnItemClickListener(){
            @Override
            public void onClick(int position) {
                switch (position){
                    //转账给好友
                    case 0:
                        Intent intent3 = new Intent(SelfYuEActivity.this,FriendActivity.class);
                        intent3.putExtra("payPass",pass);
                        intent3.putExtra("biaoshi","000");
                        startActivity(intent3);
                        break;
                    //转给用户
                    case 1:
                        Intent intent = new Intent(SelfYuEActivity.this,ZhangHuActivity.class);
                        intent.putExtra("payPass",pass);
                        intent.putExtra("biaoshi","000");
                        startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(SelfYuEActivity.this,"该二维码接口即将上线",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        fxPopWindow.dismiss();
                        break;
                }
            }
        });
        fxPopWindow.setOutsideTouchable(true);
        if ("000".equals(biaoshi)){
            deletePush();
        }
    }

    private void deletePush() {
        String url = FXConstant.URL_DELETE_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                param.put("type","08");
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void queryData() {
        String url = FXConstant.URL_Query_BKTXYuE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                data = s;
                JSONObject object = JSON.parseObject(s);
                String total = object.getString("total");
                if (total!=null){
                    double bktx = Double.parseDouble(total);
                    String prices1 = String.format("%.2f", bktx);
                    tv_bktx_yuE.setText(prices1+"元");
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
                param.put("mer_id",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(SelfYuEActivity.this).addToRequestQueue(request);
    }

    private void initView() {
        tv_yuE = (TextView) findViewById(R.id.tv_yuE);
        tv_bktx_yuE = (TextView) findViewById(R.id.tv_bktx_yuE);
        tv_kefu = (TextView) findViewById(R.id.tv_kefu);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_mingxi = (TextView) findViewById(R.id.tv_mingxi);
        tv_loginId = (TextView) findViewById(R.id.tv_loginId);
        tvTitleA = (TextView) findViewById(R.id.tv_titl);
        ivHead = (ImageView) findViewById(R.id.iv_avatar);
        re_my_yue = (RelativeLayout) findViewById(R.id.re_my_yue);
        re_my_xinxi = (RelativeLayout) findViewById(R.id.re_my_xinxi);
        re_my_chongzhi = (RelativeLayout) findViewById(R.id.re_my_chongzhi);
        re_my_tixian = (RelativeLayout) findViewById(R.id.re_my_tixian);
        re_buke_tixian = (RelativeLayout) findViewById(R.id.re_buke_tixian);
        re_my_zhuanzhang = (RelativeLayout) findViewById(R.id.re_my_zhuanzhang);
        re_zhifu_set = (RelativeLayout) findViewById(R.id.re_zhifu_set);
        re_zhifu_xiugai = (RelativeLayout) findViewById(R.id.re_zhifu_xiugai);
        re_zhifu_wangji = (RelativeLayout) findViewById(R.id.re_zhifu_wangji);
        tv_kefu.setOnClickListener(this);
        tv_mingxi.setOnClickListener(this);
        re_buke_tixian.setOnClickListener(this);
        re_my_chongzhi.setOnClickListener(this);
        re_my_tixian.setOnClickListener(this);
        re_my_zhuanzhang.setOnClickListener(this);
        re_zhifu_set.setOnClickListener(this);
        re_zhifu_xiugai.setOnClickListener(this);
        re_zhifu_wangji.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_OK);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pricePresenter.updatePriceData(id);
    }

    @Override
    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.re_buke_tixian:
                startActivity(new Intent(SelfYuEActivity.this,BukeTXActivity.class).putExtra("data",data).putExtra("biaoshi","000"));
                break;
            case R.id.tv_mingxi:
                startActivity(new Intent(SelfYuEActivity.this,MerDetailListActivity.class).putExtra("biaoshi","000"));
                break;
            case R.id.re_my_zhuanzhang:
                fxPopWindow.showPopupWindow(re_my_zhuanzhang);
                break;
            case R.id.re_my_chongzhi:
                Intent intent2 = new Intent(SelfYuEActivity.this, ChongZhiActivity.class);
                intent2.putExtra("papass",pass);
                intent2.putExtra("price",yuE);
                intent2.putExtra("biaoshi","00");
                startActivityForResult(intent2,1);
                break;
            case R.id.re_my_tixian:
                Intent intent3 = new Intent(SelfYuEActivity.this, TiXianActivity.class);
                intent3.putExtra("baozhengjin",hasbzj);
                intent3.putExtra("biaoshi","00");
                intent3.putExtra("papass",pass);
                intent3.putExtra("price",yuE);
                startActivityForResult(intent3,3);
                break;
            case R.id.re_zhifu_xiugai:
                Intent intent = new Intent(SelfYuEActivity.this,XiuGaiZFActivity.class);
                intent.putExtra("zhifupass",pass);
                intent.putExtra("biaoshi","000");
                startActivity(intent);
                break;
            case R.id.re_zhifu_set:
                if (pass!=null&&!"".equals(pass)&&!pass.equalsIgnoreCase("null")){
                    Toast.makeText(SelfYuEActivity.this, "已存在支付密码，不能重复设置！", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent1 = new Intent(SelfYuEActivity.this, ZhiFuSettingActivity.class);
                    intent1.putExtra("payPass", pass);
                    intent1.putExtra("biaoshi", "000");
                    startActivity(intent1);
                }
                break;
            case R.id.re_zhifu_wangji:
                startActivity(new Intent(SelfYuEActivity.this, WJPaActivity.class).putExtra("biaoshi","000"));
                break;
            case R.id.tv_kefu:
                Intent intent11 = new Intent(SelfYuEActivity.this, ChatActivity.class);
                intent11.putExtra("userId", "18337101357");
                intent11.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                intent11.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                intent11.putExtra(EaseConstant.EXTRA_USER_IMG,"zhengshiduo.png");
                intent11.putExtra(EaseConstant.EXTRA_USER_NAME,"正事多客服");
                intent11.putExtra(EaseConstant.EXTRA_USER_SHARERED,"无");
                startActivity(intent11);
                break;
        }
    }

    @Override
    public void updateCurrentPrice(Object success) {
        yuE = DemoApplication.getInstance().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
        String prices1 = String.format("%.2f", yuE);
        tv_yuE.setText(prices1+"元");
    }

    @Override
    public void updateUserInfo(Userful user) {
        name = user.getName();
        avatar1 = user.getImage();
        if (avatar1!=null&&!"".equals(avatar1)){
            avatar1 = avatar1.split("\\|")[0];
        }
        url = FXConstant.URL_AVATAR + avatar1;
        if (name!=null) {
            try {
                name = URLDecoder.decode(name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String number = null,tell=null;
        tell = DemoHelper.getInstance().getCurrentUsernName();
        if (tell.length()>10) {
            number = tell.substring(0, 3) + "****" + tell.substring(7, 11);
        }
        tv_loginId.setText(number);
        tv_name.setText(name);
        if (avatar1!=null&&!"".equals(avatar1)) {
            ImageLoader.getInstance().displayImage(url,ivHead,DemoApplication.mOptions);
        }else {
            ivHead.setVisibility(View.INVISIBLE);
            tvTitleA.setVisibility(View.VISIBLE);
            tvTitleA.setText(name);
        }
    }

    @Override
    public void showproLoading() {
    }
    @Override
    public void hideproLoading() {
    }
    @Override
    public void showproError() {

    }
}
