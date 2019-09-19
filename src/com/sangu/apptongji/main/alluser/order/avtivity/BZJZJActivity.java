package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.RedPromoteActivity;
import com.sangu.apptongji.main.alluser.order.entity.DIDAList;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.ISearchPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.SearchPresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.alluser.view.ISearchView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.SoundPlayUtils;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.OnPasswordInputFinish;
import com.sangu.apptongji.main.widget.PasswordView;
import com.sangu.apptongji.ui.BaseActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-09-26.
 */

public class BZJZJActivity extends BaseActivity implements ISearchView,IPriceView{
    private static final int SDK_PAY_FLAG = 1;
    private Button btn_zhr_bao=null,btn_tx_bao=null;
    private TextView tv_xiangmu_mingcheng=null,tv_baozheng_jine=null;
    private EditText et_baozheng_jine=null;
    private String yue=null,jine=null,upId=null,maj=null,biaoshi=null,paypass=null,fukuanId=null,createTime,orderTime,
            margin_time,managerId;
    private ISearchPresenter presenter=null;
    private IPricePresenter pricePresenter;
    private int a,errorTime=3;
    private IWXAPI api=null;
    private ImageView image_advert;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Toast.makeText(BZJZJActivity.this, "支付成功,质保增加成功", Toast.LENGTH_SHORT).show();
//                        setResult(RESULT_OK);
//                        finish();

                        UpdateMeraccountSubsidy();

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(BZJZJActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };
    private String currentTime;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baozhengjin_zengjia);
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        presenter = new SearchPresenter(this,this);
        pricePresenter = new PricePresenter(this,this);
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        createTime = this.getIntent().getStringExtra("createTime");
        margin_time = this.getIntent().getStringExtra("margin_time");

        managerId = this.getIntent().getStringExtra("managerId");
        jine = TextUtils.isEmpty(this.getIntent().getStringExtra("JINE"))?"0.00":this.getIntent().getStringExtra("JINE");
        upId = this.getIntent().getStringExtra("upId");
        maj = this.getIntent().getStringExtra("maj");

        image_advert = (ImageView) findViewById(R.id.image_advert);

        if ("00".equals(biaoshi)){

            fukuanId = DemoHelper.getInstance().getCurrentUsernName();
            paypass = DemoApplication.getInstance().getCurrentPayPass();
            yue = TextUtils.isEmpty(DemoApplication.getInstance().getCurrenPrice()+"")?"0.00":DemoApplication.getInstance().getCurrenPrice()+"";

        }else {
            fukuanId = DemoApplication.getInstance().getCurrentQiYeId();
            paypass = DemoApplication.getInstance().getCurrentQiyePayPass();
            yue = TextUtils.isEmpty(DemoApplication.getInstance().getCurrenQiyePrice()+"")?"0.00":DemoApplication.getInstance().getCurrenQiyePrice()+"";
        }

        pricePresenter.updatePriceData(fukuanId);
        presenter.loadOrderDetail(upId);
        tv_xiangmu_mingcheng = (TextView) findViewById(R.id.tv_xiangmu_mingcheng);
        tv_baozheng_jine = (TextView) findViewById(R.id.tv_baozheng_jine);
        btn_tx_bao = (Button) findViewById(R.id.btn_tx_bao);
        btn_zhr_bao = (Button) findViewById(R.id.btn_zhr_bao);
        et_baozheng_jine = (EditText) findViewById(R.id.et_baozheng_jine);
        et_baozheng_jine.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tv_xiangmu_mingcheng.setText(maj);
        tv_baozheng_jine.setText(jine+"元");
        setlistener();

        GetNotice();

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

                    String url = FXConstant.URL_ADVERTURL+object1.getString("image3");

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

        MySingleton.getInstance(BZJZJActivity.this).addToRequestQueue(request);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String zhifuZhuangtai = intent.getStringExtra("zhifu");
        if (zhifuZhuangtai!=null&&"增加成功".equals(zhifuZhuangtai)){
//            Toast.makeText(BZJZJActivity.this, "支付成功,质保增加成功", Toast.LENGTH_SHORT).show();
////            setResult(RESULT_OK);
////            finish();

            UpdateMeraccountSubsidy();

        }
    }

    public void back(View v){
        finish();
    }
    private void setlistener() {
        btn_zhr_bao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String baozhengjin = et_baozheng_jine.getText().toString().trim();
                if (baozhengjin!=null&&!"".equals(baozhengjin)&&Double.valueOf(baozhengjin)>=100) {

                    LayoutInflater inflaterDl = LayoutInflater.from(BZJZJActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                    final Dialog dialog = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                    RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                    RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                    RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
                    re_item5.setVisibility(View.VISIBLE);
                    TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                    TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                    TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                    TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
                    tv_title.setText("请选择支付方式");
                    tv_item1.setText("余 额 充 值");
                    tv_item2.setText("微 信 充 值");
                    tv_item5.setText("支付宝充值");
                    re_item1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            pricePresenter.updatePriceData(fukuanId);
                            zhifu(baozhengjin);
                        }
                    });
                    re_item2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            rechargefromWx(baozhengjin);
                        }
                    });
                    re_item5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            rechargefromZhFb(baozhengjin);
                        }
                    });
                    re_item3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }else {
                    Toast.makeText(getApplicationContext(),"质保每次增加不得低于100元！",Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_tx_bao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                //20180219201232
                Log.d("chen", String.valueOf(Long.valueOf(currentTime) - Long.valueOf(margin_time)));
                try {

                    int c = 10;
                    BigInteger longa =BigInteger.valueOf(3600*24*30);
                    if (margin_time!=null&&!"".equals(margin_time)){

                        Long a = Long.valueOf(margin_time);
                        Long b = Long.valueOf("20180925000000");

                        if (a>b){

                            //新版本30天
                            c = 30;

                            if (margin_time!=null&&!"".equals(margin_time)&& BigInteger.valueOf((simpleDateFormat.parse(currentTime).getTime() - simpleDateFormat.parse(margin_time).getTime())/1000).compareTo(longa) < 0){

                                LayoutInflater inflaterDl = LayoutInflater.from(BZJZJActivity.this);
                                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                                final Dialog dialog2 = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
                                dialog2.show();
                                dialog2.getWindow().setContentView(layout);
                                dialog2.setCanceledOnTouchOutside(false);
                                dialog2.setCancelable(false);
                                TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                                Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                                tv_title.setText("质保操作"+c+"天之后才能提取！");
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                    }
                                });

                                return;
                            }

                        }else {
                            c = 10;

                            if (margin_time!=null&&!"".equals(margin_time)&&(simpleDateFormat.parse(currentTime).getTime() - simpleDateFormat.parse(margin_time).getTime()) < 1000*3600*24*10){
                                LayoutInflater inflaterDl = LayoutInflater.from(BZJZJActivity.this);
                                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                                final Dialog dialog2 = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
                                dialog2.show();
                                dialog2.getWindow().setContentView(layout);
                                dialog2.setCanceledOnTouchOutside(false);
                                dialog2.setCancelable(false);
                                TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                                Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                                tv_title.setText("质保操作"+c+"天之后才能提取！");
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                    }
                                });

                                return;
                            }

                        }

                    }

                if (createTime!=null&&!"".equals(createTime) &&  BigInteger.valueOf((simpleDateFormat.parse(currentTime).getTime() - simpleDateFormat.parse(createTime).getTime())/1000).compareTo(longa) < 0){
                    LayoutInflater inflaterDl = LayoutInflater.from(BZJZJActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                    final Dialog dialog2 = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.setCancelable(false);
                    TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                    Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                    tv_title.setText("专业修改30天之后才能提取质保！");
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });

                    return;
                }

                //20180202144533
                if (a == 0) {

                    if (orderTime!=null&&!"".equals(orderTime)&&BigInteger.valueOf((simpleDateFormat.parse(currentTime).getTime()-simpleDateFormat.parse(orderTime).getTime())/1000).compareTo(longa)<0){
                        LayoutInflater inflaterDl = LayoutInflater.from(BZJZJActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                        final Dialog dialog2 = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
                        dialog2.show();
                        dialog2.getWindow().setContentView(layout);
                        dialog2.setCanceledOnTouchOutside(false);
                        dialog2.setCancelable(false);
                        TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                        Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                        tv_title.setText("订单完成三十天之后才能提取质保！");
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                            }
                        });
                        return;
                    }

                    LayoutInflater inflaterD5 = LayoutInflater.from(BZJZJActivity.this);
                    LinearLayout layout5 = (LinearLayout) inflaterD5.inflate(R.layout.dialog_withdrawmargin, null);
                    final Dialog dialog5 = new AlertDialog.Builder(BZJZJActivity.this, R.style.Dialog).create();
                    dialog5.show();
                    dialog5.getWindow().setContentView(layout5);
                    WindowManager.LayoutParams params = dialog5.getWindow().getAttributes() ;
                    Display display = BZJZJActivity.this.getWindowManager().getDefaultDisplay();
                    params.width =(int) (display.getWidth()*0.75); //使用这种方式更改了dialog的框宽
                    dialog5.getWindow().setAttributes(params);
                    dialog5.setCancelable(true);
                    dialog5.setCanceledOnTouchOutside(true);

                    TextView tv_agreen = (TextView)layout5.findViewById(R.id.tv_agreen);
                    tv_agreen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog5.dismiss();

                            String url = FXConstant.URL_TQBAOZHJ;
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    try {
                                        JSONObject obj = new JSONObject(s);
                                        String code = obj.getString("code");
                                        if (code.equals("success")) {
                                            Toast.makeText(BZJZJActivity.this, "提取成功！", Toast.LENGTH_SHORT).show();
                                            setResult(RESULT_OK);
                                            finish();
                                        } else {
                                            Toast.makeText(BZJZJActivity.this, "提取失败！"+code, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                    params.put("margin_thaw", jine);
                                    params.put("upId", upId);
                                    return params;
                                }
                            };
                            MySingleton.getInstance(BZJZJActivity.this).addToRequestQueue(request);

                        }

                    });

                }else {
                    LayoutInflater inflaterDl = LayoutInflater.from(BZJZJActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                    final Dialog dialog2 = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.setCancelable(false);
                    TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                    Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                    tv_title.setText("您在该专业存在未完成的订单，无法提取！");
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    return;
                }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private long parseTime(String d) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
          date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    private void showErrorLing() {
        LayoutInflater inflaterDl = LayoutInflater.from(BZJZJActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
        final Dialog dialog2 = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
        dialog2.show();
        dialog2.getWindow().setContentView(layout);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
        Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
        tv_title.setText("支付密码错误次数达到上限,请次日重试或联系客服");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
    }

    private void showErrorTishi(String s) {
        LayoutInflater inflater1 = LayoutInflater.from(BZJZJActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
        dialog1.show();
        dialog1.getWindow().setContentView(layout1);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        title.setText("温馨提示");
        btnOK1.setText("忘记密码");
        btnCancel1.setText("重新输入");
        title_tv1.setText("支付密码错误，您还可以输入"+s+"次");
        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        btnOK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                String bs;
                if ("00".equals(biaoshi)) {
                    bs = "000";
                }else {
                    bs = "001";
                }
                startActivity(new Intent(BZJZJActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
            }
        });
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if(!isNum.matches() ){
            return false;
        }
        return true;
    }

    private void showErrorMiMaXG() {
        LayoutInflater inflater1 = LayoutInflater.from(BZJZJActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
        dialog1.show();
        dialog1.getWindow().setContentView(layout1);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        title.setText("温馨提示");
        btnOK1.setText("前去设置");
        btnCancel1.setText("以后再说");
        title_tv1.setText("支付系统更新,请先修改您的支付密码为6为纯数字组合");
        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        btnOK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                String bs;
                bs = "000";
                startActivity(new Intent(BZJZJActivity.this, XiuGaiZFActivity.class).putExtra("biaoshi",bs).putExtra("zhifupass",paypass));
            }
        });
    }

    private void showErrorMiMaSHZH() {
        LayoutInflater inflater1 = LayoutInflater.from(BZJZJActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
        dialog1.show();
        dialog1.getWindow().setContentView(layout1);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        title.setText("温馨提示");
        btnOK1.setText("前去设置");
        btnCancel1.setText("以后再说");
        if ("00".equals(biaoshi)) {
            title_tv1.setText("您还没有设置支付密码!");
        } else {
            title_tv1.setText("您还没有设置企业的支付密码!");
        }
        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        btnOK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                String bs;
                if ("00".equals(biaoshi)) {
                    bs = "000";
                } else {
                    bs = "001";
                }
                startActivity(new Intent(BZJZJActivity.this,ZhiFuSettingActivity.class).putExtra("biaoshi",bs).putExtra("zhifupass",paypass));
            }
        });
    }

    private void reduceShRZFCount(final String times) {
        String url = FXConstant.URL_UPDATEZHHU;
        StringRequest request3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if (code.equals("SUCCESS")){
                    if (errorTime>0) {
                        errorTime--;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<>();
                params1.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                params1.put("enterErrorTimes", times+"");
                return params1;
            }
        };
        MySingleton.getInstance(BZJZJActivity.this).addToRequestQueue(request3);
    }
    
    private void zhifu(final String baozhengjin) {
        if (Double.valueOf(baozhengjin)<100){
            Toast.makeText(BZJZJActivity.this, "质保单次最少充值100！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (paypass==null||"".equals(paypass)){
            showErrorMiMaSHZH();
            return;
        }
        if (paypass.length()!=6||!isNumeric(paypass)){
            showErrorMiMaXG();
            return;
        }
        if (errorTime<=0){
            showErrorLing();
            return;
        }
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        if (Double.valueOf(yue)>=Double.valueOf(baozhengjin)){
            LayoutInflater inflaterDl = LayoutInflater.from(BZJZJActivity.this);
            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            final Dialog dialog = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
            final PasswordView pwdView = (PasswordView)layout.findViewById(R.id.pwd_view);
            pwdView.setOnFinishInput(new OnPasswordInputFinish() {
                @Override
                public void inputFinish() {
                    final ProgressDialog pd = new ProgressDialog(BZJZJActivity.this);
                    pd.setMessage("正在支付...");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    if (pwdView.getStrPassword().equals(paypass)) {
                        dialog.dismiss();
                        String url = FXConstant.URL_ZJBAOZHJ;
                        final String finalBaozhengjin = baozhengjin;
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                dialog.dismiss();

                                if (pd!=null&&pd.isShowing()){
                                    pd.dismiss();
                                }

                                try {
                                    JSONObject obj = new JSONObject(s);
                                    String code = obj.getString("code");
                                    if (code.equals("success")){
//                                        Toast.makeText(BZJZJActivity.this,"增加成功！",Toast.LENGTH_SHORT).show();
//                                        setResult(RESULT_OK);
//                                        finish();

                                        UpdateMeraccountSubsidy();

                                    }else {
                                        Toast.makeText(BZJZJActivity.this,"增加失败！",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                dialog.dismiss();
                                Toast.makeText(BZJZJActivity.this, "网络连接错误！", Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("userId", fukuanId);
                                params.put("margin", finalBaozhengjin);
                                params.put("upId",upId);
                                return params;
                            }
                        };
                        MySingleton.getInstance(BZJZJActivity.this).addToRequestQueue(request);
                    } else {
                        pd.dismiss();
                        int times;
                        if (errorTime>0){
                            times = errorTime-1;
                        }else {
                            times = 0;
                        }
                        reduceShRZFCount(times+"");
                        if (times==0) {
                            showErrorLing();
                        }else {
                            showErrorTishi(times + "");
                        }
                        dialog.dismiss();
                    }
                }
            });
            /**
             *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供响应
             *  如果写了，会覆盖我们在自定义控件中提供的响应
             *  可以看到这里toast显示 "Biu Biu Biu"而不是"Cancel"*/
            pwdView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            pwdView.getForgetTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bs;
                    if ("00".equals(biaoshi)) {
                        bs = "000";
                    }else {
                        bs = "001";
                    }
                    startActivity(new Intent(BZJZJActivity.this, WJPaActivity.class).putExtra("biaoshi",bs));
                }
            });
        }else {
            LayoutInflater inflaterDl = LayoutInflater.from(BZJZJActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
            RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
            RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
            RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
            re_item1.setVisibility(View.GONE);
            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
            TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
            TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
            TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
            tv_title.setText("钱包余额不足(剩余"+yue+"元),请您先充值或者选择其他支付方式");
            tv_item2.setText("微信支付");
            tv_item5.setText("支付宝支付");
            re_item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            re_item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    rechargefromWx(baozhengjin);

                }
            });
            re_item5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    rechargefromZhFb(baozhengjin);
                }
            });
            re_item3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        } else {
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }


    private void UpdateMeraccountSubsidy(){

        String url = FXConstant.URL_UPDATEZHHU;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);

                if (object.getString("code").equals("SUCCESS")){

                    SoundPlayUtils.play(2);
                    LayoutInflater inflaterDl = LayoutInflater.from(BZJZJActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_hongbao, null);
                    final Dialog dialog = new AlertDialog.Builder(BZJZJActivity.this,R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    TextView tv_title1 = (TextView) layout.findViewById(R.id.tv_title1);
                    TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
                    TextView tv_yue = (TextView) layout.findViewById(R.id.tv_yue);
                    TextView tv_pushclick = (TextView) layout.findViewById(R.id.tv_pushclick);
                    tv_pushclick.setVisibility(View.VISIBLE);
                    TextPaint tp = tv_title.getPaint();
                    tp.setFakeBoldText(true);
                    tv_title.setText("1元");
                    tv_title1.setText("充值成功");
                    tv_yue.setText("正事多 补贴红包");

                    tv_pushclick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();

                            Intent intent = new Intent(BZJZJActivity.this,RedPromoteActivity.class);

                            startActivityForResult(intent,0);

                        }
                    });

                }else {

                    Toast.makeText(BZJZJActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    finish();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(BZJZJActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                finish();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();

                param.put("merId",DemoHelper.getInstance().getCurrentUsernName());
                param.put("redPromoteBalance","1");
                param.put("redPromoteCount","0");
                param.put("redPromoteType","1");

                return param;
            }

        };

        MySingleton.getInstance(BZJZJActivity.this).addToRequestQueue(request);

    }

    private void rechargefromWx(String balance1) {
        if (Double.valueOf(balance1)<100){
            Toast.makeText(BZJZJActivity.this, "质保单次最少充值100！", Toast.LENGTH_SHORT).show();
            return;
        }
        final String mubiaoId = fukuanId+"_"+"3"+"_"+fukuanId+"_"+upId;
        String balance = (int)(Double.parseDouble(balance1)*100)+"";
        Toast.makeText(BZJZJActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
        final String finalBalance = balance;
        StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("activity","BZJZJActivity");
                    editor.commit();
                    JSONObject object = new JSONObject(s);
                    Log.e("chongzhiac,s", s);
                    String appid="",mch_id="",nonce_str="",sign="",prepayId="",timestamp="";
                    appid = object.getString("appid");
                    nonce_str = object.getString("noncestr");
                    mch_id = object.getString("partnerid");
                    prepayId = object.getString("prepayid");
                    timestamp = object.getString("timestamp");
                    sign = object.getString("sign");
                    PayReq req = new PayReq();
                    req.appId = appid;
                    req.partnerId = mch_id;
                    req.prepayId = prepayId;
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = nonce_str;
                    //这是得到一个时间戳(除以1000转化成秒数)
                    req.timeStamp = timestamp;
                    //调用获得签名的方法,这里直接把服务器返回来的sign给覆盖了,所以我不是很明白服务器为什么返回这个sign值,然后调起支付,基本上就可以了(我的反正是可以了....)
//                sign = OrderInfoUtil2_0.createSign(parameters);
                    Log.e("TAG", "timestamp=====" + req.timeStamp);
                    req.sign = sign;
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(BZJZJActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("body","正事多-质保支付");
                param.put("detail","正事多-质保支付");
                param.put("out_trade_no",getNowTime());
                param.put("total_fee", finalBalance);
                param.put("spbill_create_ip",getHostIP());
                param.put("attach",mubiaoId);
                return param;
            }
        };
        MySingleton.getInstance(BZJZJActivity.this).addToRequestQueue(request);
    }

    private String getNowTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

    private String getHostIP() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;
    }

    private void rechargefromZhFb(final String balance){
        if (Double.valueOf(balance)<100){
            Toast.makeText(BZJZJActivity.this, "质保单次最少充值100！", Toast.LENGTH_SHORT).show();
            return;
        }
        String mubiaoId = fukuanId + "_" + upId;
        String chongzhiId=null;
        chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
        try {
            chongzhiId = URLEncoder.encode(chongzhiId,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (balance!=null&&Double.parseDouble(balance)>0) {
            String url = FXConstant.URL_ZhiFu;
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap2(Constant.APPID_WX,balance,chongzhiId,"3",mubiaoId,null,"正事多-质保支付");
            final String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
            final String orderinfo = OrderInfoUtil2_0.getSign(params);
            StringRequest request = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String sign = object.getString("sign");
                        sign = URLEncoder.encode(sign, "UTF-8");
                        final String orderInfo = orderParam + "&" + "sign=" + sign;
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(BZJZJActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(BZJZJActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(BZJZJActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(BZJZJActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateThisDiDa(List<DIDAList> didaLists, String time) {
        if (!TextUtils.isEmpty(time)) {
            currentTime = time;
        }
        a = 0;
        for (int i = 0;i<didaLists.size();i++) {
            String orderState = didaLists.get(i).getOrderState();
            if (orderState.equals("03") || orderState.equals("04")) {
                a = a + 1;
            }
        }
        if (didaLists!=null&&didaLists.size()>0) {
            orderTime = didaLists.get(0).getOrderTime();
        }
    }

    @Override
    public void updateCurrentPrice(Object success) {
        if ("00".equals(biaoshi)) {
            yue = TextUtils.isEmpty(DemoApplication.getInstance().getCurrenPrice()+"") ? "0.00" : DemoApplication.getInstance().getCurrenPrice()+"";
            paypass = DemoApplication.getInstance().getCurrentPayPass();
        }else {
            yue = TextUtils.isEmpty(DemoApplication.getInstance().getCurrenQiyePrice()+"") ? "0.00" : DemoApplication.getInstance().getCurrenQiyePrice()+"";
            paypass = DemoApplication.getInstance().getCurrentQiyePayPass();
        }
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
        errorTime = object.getInteger("enterErrorTimes");
    }
}
