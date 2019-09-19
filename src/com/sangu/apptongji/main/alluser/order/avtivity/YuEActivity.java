package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.ChatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-02.
 */

public class YuEActivity extends BaseActivity implements View.OnClickListener,IPriceView{
    private TextView tv_yuE=null,tv_bktx_yuE;
    private TextView tv_name=null;
    private TextView tv_mingxi=null,tv_tishi;
    private TextView tv_loginId=null;
    private TextView tvTitleA=null,tv_kefu;
    private ImageView ivHead=null,iv5,iv6,iv7;
    private RelativeLayout re_my_xinxi=null;
    private RelativeLayout re_my_yue=null;
    private RelativeLayout re_my_chongzhi=null;
    private RelativeLayout re_my_tixian=null;
    private RelativeLayout re_my_zhuanzhang=null;
    private RelativeLayout re_zhifu_set=null;
    private RelativeLayout re_zhifu_xiugai=null;
    private RelativeLayout re_zhifu_wangji=null;
    private RelativeLayout re_buke_tixian=null;
    private String pass,avatar1,name,biaoshi,id,hasbzj,managerId;
    double yuE;
    private IPricePresenter pricePresenter;
    String url=null,data;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_yue_self);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        pricePresenter = new PricePresenter(this,this);
        initView();
        managerId = this.getIntent().getStringExtra("managerId");
        avatar1 = this.getIntent().getStringExtra("image");
        name = this.getIntent().getStringExtra("name");
        id = DemoApplication.getInstance().getCurrentQiYeId();
        url = FXConstant.URL_QIYE_TOUXIANG + avatar1;
        if (name!=null) {
            try {
                name = URLDecoder.decode(name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (avatar1!=null&&!"".equals(avatar1)) {
            ImageLoader.getInstance().displayImage(url,ivHead, DemoApplication.mOptions);
        }else {
            ivHead.setVisibility(View.INVISIBLE);
            tvTitleA.setVisibility(View.VISIBLE);
            tvTitleA.setText("企");
        }
        tv_name.setText(name);
        String number = null;
        if (id.length()>12) {
            number = id.substring(0, 3) + "****" + id.substring(id.length()-4, id.length());
        }
        tv_loginId.setText(number);
        queryData();
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
                param.put("mer_id", id);
                return param;
            }
        };
        MySingleton.getInstance(YuEActivity.this).addToRequestQueue(request);
    }

    private void initView() {
        tv_yuE = (TextView) findViewById(R.id.tv_yuE);
        tv_bktx_yuE = (TextView) findViewById(R.id.tv_bktx_yuE);
        tv_kefu = (TextView) findViewById(R.id.tv_kefu);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_mingxi = (TextView) findViewById(R.id.tv_mingxi);
        tv_tishi = (TextView) findViewById(R.id.tv_tishi);
        tv_loginId = (TextView) findViewById(R.id.tv_loginId);
        tvTitleA = (TextView) findViewById(R.id.tv_titl);
        ivHead = (ImageView) findViewById(R.id.iv_avatar);
        iv5 = (ImageView) findViewById(R.id.iv5);
        iv6 = (ImageView) findViewById(R.id.iv6);
        iv7 = (ImageView) findViewById(R.id.iv7);
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
        iv5.setImageResource(R.drawable.bianhao_five);
        iv6.setImageResource(R.drawable.bianhao_sex);
        iv7.setImageResource(R.drawable.bianhao_seven);
        re_my_zhuanzhang.setVisibility(View.GONE);
        tv_tishi.setVisibility(View.GONE);
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
                startActivity(new Intent(YuEActivity.this,BukeTXActivity.class).putExtra("data",data).putExtra("biaoshi","001"));
                break;
            case R.id.tv_mingxi:
                startActivity(new Intent(YuEActivity.this,MerDetailListActivity.class).putExtra("biaoshi","001"));
                break;
            case R.id.re_my_chongzhi:
                String item1= "钱包余额充值";
                String item2= "直 接 充 值";
                showDialog(item1,item2);
                break;
            case R.id.re_my_tixian:
                String item4= "提现到支付宝";
                String item5= "提 现 到 微 信";
                String item6= "提现到账户余额";
                showDialog2(item4,item5,item6);
                break;
            case R.id.re_zhifu_xiugai:
                Intent intent = new Intent(YuEActivity.this,XiuGaiZFActivity.class);
                intent.putExtra("zhifupass",pass);
                intent.putExtra("managerId",managerId);
                intent.putExtra("biaoshi","001");
                startActivity(intent);
                break;
            case R.id.re_zhifu_set:
                if (pass!=null&&!"".equals(pass)&&!pass.equalsIgnoreCase("null")){
                    Toast.makeText(YuEActivity.this, "已存在支付密码，不能重复设置！", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent3 = new Intent(YuEActivity.this,ZhiFuSettingActivity.class);
                    intent3.putExtra("payPass",pass);
                    intent3.putExtra("biaoshi","001");
                    startActivity(intent3);
                }
                break;
            case R.id.re_zhifu_wangji:
                startActivity(new Intent(YuEActivity.this, WJPaActivity.class).putExtra("biaoshi","001").putExtra("managerId",managerId));
                break;
            case R.id.tv_kefu:
                Intent intent11 = new Intent(YuEActivity.this, ChatActivity.class);
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

    private void showDialog2(String item4,String item5,String item6) {
        final String[] biaoshi2 = new String[1];
        LayoutInflater inflaterDl = LayoutInflater.from(YuEActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(YuEActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
        re_item1.setVisibility(View.GONE);
        re_item5.setVisibility(View.VISIBLE);
        tv_item1.setText(item4);
        tv_item2.setText(item5);
        tv_item5.setText(item6);
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biaoshi2[0] = "00";
                Intent intent2 = new Intent(YuEActivity.this, TiXianActivity.class);
                intent2.putExtra("papass",pass);
                intent2.putExtra("price",yuE);
                intent2.putExtra("managerId",managerId);
                intent2.putExtra("biaoshi","01");
                intent2.putExtra("biaoshi2", biaoshi2[0]);
                startActivityForResult(intent2,1);
                dialog.dismiss();
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biaoshi2[0] = "00";
                Intent intent2 = new Intent(YuEActivity.this, TiXianActivity.class);
                intent2.putExtra("papass",pass);
                intent2.putExtra("price",yuE);
                intent2.putExtra("managerId",managerId);
                intent2.putExtra("biaoshi","01");
                intent2.putExtra("biaoshi2", biaoshi2[0]);
                startActivityForResult(intent2,1);
                dialog.dismiss();
            }
        });
        re_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biaoshi2[0] = "01";
                Intent intent2 = new Intent(YuEActivity.this, TiXianActivity.class);
                intent2.putExtra("papass",pass);
                intent2.putExtra("price",yuE);
                intent2.putExtra("managerId",managerId);
                intent2.putExtra("biaoshi","01");
                intent2.putExtra("biaoshi2", biaoshi2[0]);
                startActivityForResult(intent2,1);
                dialog.dismiss();
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showDialog(String item1,String item2) {
        final String[] biaoshi2 = new String[1];
        LayoutInflater inflaterDl = LayoutInflater.from(YuEActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(YuEActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        RelativeLayout re_item4 = (RelativeLayout) dialog.findViewById(R.id.re_item4);
        re_item4.setVisibility(View.GONE);
        tv_item1.setText(item1);
        tv_item2.setText(item2);
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biaoshi2[0] = "00";
                Intent intent2 = new Intent(YuEActivity.this, ChongZhiActivity.class);
                intent2.putExtra("papass",pass);
                intent2.putExtra("price",yuE);
                intent2.putExtra("biaoshi","01");
                intent2.putExtra("biaoshi2", biaoshi2[0]);
                startActivityForResult(intent2,1);
                dialog.dismiss();
            }
        });
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biaoshi2[0] = "01";
                Intent intent2 = new Intent(YuEActivity.this, ChongZhiActivity.class);
                intent2.putExtra("papass",pass);
                intent2.putExtra("price",yuE);
                intent2.putExtra("biaoshi","01");
                intent2.putExtra("biaoshi2", biaoshi2[0]);
                startActivityForResult(intent2,1);
                dialog.dismiss();
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
    public void updateCurrentPrice(Object success) {
        yuE = DemoApplication.getInstance().getCurrenQiyePrice();
        pass = DemoApplication.getInstance().getCurrentQiyePayPass();
        String prices1 = String.format("%.2f", yuE);
        tv_yuE.setText(prices1+"元");
    }

}
