package com.sangu.apptongji.main.qiye;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.pay.demo.PayResult;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.EaseConstant;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.HbHuoQuActivity;
import com.sangu.apptongji.main.activity.LoginActivity;
import com.sangu.apptongji.main.activity.ZYDetailActivity;
import com.sangu.apptongji.main.address.AddressListActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFiveActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFourActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailThreeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailTwoActivity;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.moments.BigImageActivity;
import com.sangu.apptongji.main.moments.MomentsPublishActivity;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.LocalUserUtil;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.main.utils.SoundPlayUtils;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.ChatActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * Created by Administrator on 2016-12-30.
 */

public class QiYeDetailsActivity extends BaseActivity implements IQiYeDetailView,IPriceView{
    private static final int SDK_PAY_FLAG = 1;
    private String qiyeId=null,name=null,faren=null,sign=null,CompanyAddress=null,imageStr=null,ZY1=null,ZY2=null,ZY3=null,ZY4=null,
            currentQiye=null,userName=null,com_id,fxUpName;
    private String companyName=null,comSignature=null,comAddress=null,farenName=null;
    private String wodezhanghao=null,upId1=null,upId2=null,upId3=null,upId4=null,resv31=null,resv32=null,resv33=null,
            resv34=null,dType=null,typeDetail=null;
    private TextView tvSign=null,tv_qiye_phone=null,all_jiedancishu=null,all_xiaofeicishu=null,qiye_faren=null,tv_company_name=null;
    private TextView tvCompanyAddress=null,tv_my_paidan=null,tvqiye_name=null,tvqiye_renshu=null;
    private TextView tvZY1=null,tvZY2=null,tvZY3=null,tvZY4=null;
    private TextView zy1_bao=null,zy2_bao=null,zy3_bao=null,zy4_bao=null,tv_chuangjian1=null,tv_chuangjian2=null,tv_chuangjian3=null,
            tv_chuangjian4=null,tv_liulan=null;
    private ImageView iv_hb=null;
    private TextView zy1_jiedancishu=null,zy2_jiedancishu=null,zy3_jiedancishu=null,zy4_jiedancishu=null;
    private Button btn_fenxiang=null,btn_msg=null,btn_dianhua=null;
    private Button btnAdd=null;
    private Button btn_comit1=null,btn_comit2=null,btn_comit3=null,btn_comit4=null;
    private ImageView ivAvatar=null;
    private ImageView ivAvatar2=null;
    private ImageView ivAvatar3=null;
    private ImageView ivAvatar4=null;
    private ImageView ivAvatar5=null;
    private ImageView ivAvatar6=null;
    private ImageView ivAvatar7=null;
    private ImageView ivAvatar8=null;
    private IQiYeInfoPresenter presenter=null;
    private IPricePresenter pricePresenter=null;
    private String pass="",joinMoney=null,recruitMoney=null,resv5;
    private String shareType=null,fxPingTai = null,fxLeiXing = null;
    private RelativeLayout rl1=null,rl2=null,rl3=null,rl4=null;
    private String recruitImages=null,joinImages=null,recruitBody=null,joinBody=null,shareRed=null,singleShare=null,friendsNumber=null;
    private double yuE;
    private String onedown = null,hbYaoqiu,exShareRed;
    private String decribe1=null,decribe2=null,decribe3=null,decribe4=null,margen1=null,margen2=null,margen3=null,margen4=null,comTell=null,managerId=null,isQunzhu="00";
    private String remark1=null,remark2=null,remark3=null,remark4=null,image1=null,image2=null,image3=null,image4=null,create1=null,create2=null,create3=null,create4=null;
    private String liulancishu1=null,liulancishu2=null,liulancishu3=null,liulancishu4=null,biaoshi=null,personalDtails=null,redInterval;
    private IWXAPI api=null;
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
                        Toast.makeText(QiYeDetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        addFriendInDetail(qiyeId);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(QiYeDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    //    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        String zhifuZhuangtai = intent.getStringExtra("zhifu");
//        if (zhifuZhuangtai!=null&&"支付成功".equals(zhifuZhuangtai)){
//            Toast.makeText(QiYeDetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//            addFriendInDetail(qiyeId);
//        }
//    }
    @Override
    protected void onResume() {
        super.onResume();
        yuE = DemoApplication.getInstance().getCurrenPrice();
        SharedPreferences sp = getSharedPreferences("sangu_chongzhi_zhuangtai", Context.MODE_PRIVATE);
        String zhuangtai = sp.getString("zhuangtai","失败");
        if ("成功".equals(zhuangtai)){
            Toast.makeText(QiYeDetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            if (sp!=null) {
                SharedPreferences.Editor editor = sp.edit();
                if (editor!=null) {
                    editor.clear();
                    editor.commit();
                }
            }
            addFriendInDetail(qiyeId);
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fx_activity_qiyedetail);
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        qiyeId = this.getIntent().getStringExtra("qiyeId");
        com_id = this.getIntent().getStringExtra("com_id");
        biaoshi = this.getIntent().hasExtra("biaoshi")?this.getIntent().getStringExtra("biaoshi"):"01";
        if (qiyeId!=null) {
            Log.e("qiyeDetail,", qiyeId);
        }
        userName = this.getIntent().hasExtra("name")?this.getIntent().getStringExtra("name"):"";
        currentQiye = DemoApplication.getInstance().getCurrentQiYeId();
        initView();
        presenter = new QiYeInfoPresenter(this,this);
        pricePresenter = new PricePresenter(this,this);
        if (DemoHelper.getInstance().isLoggedIn(QiYeDetailsActivity.this)) {
            setListener();
            pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
        }else {
            btn_comit1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(QiYeDetailsActivity.this, LoginActivity.class));
                }
            });
            btn_comit2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(QiYeDetailsActivity.this, LoginActivity.class));
                }
            });
            btn_comit3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(QiYeDetailsActivity.this, LoginActivity.class));
                }
            });
            btn_comit4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(QiYeDetailsActivity.this, LoginActivity.class));
                }
            });
            btn_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(QiYeDetailsActivity.this, LoginActivity.class));
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(QiYeDetailsActivity.this, LoginActivity.class));
                }
            });
            btn_dianhua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(QiYeDetailsActivity.this, LoginActivity.class));
                }
            });
            btn_fenxiang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(QiYeDetailsActivity.this, LoginActivity.class));
                }
            });
        }
        if (DemoHelper.getInstance().isLoggedIn(QiYeDetailsActivity.this)){
            upDateLiulan();
        }
        presenter.loadQiYeInfo(qiyeId);
    }

    private void upDateLiulan() {
        String url = FXConstant.URL_ADD_COMCISHU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Userdetailac,add","增加浏览次数成功"+s);
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
                param.put("companyId",qiyeId);
                param.put("times","1");
                if ("11".equals(biaoshi)&&com_id!=null&&!com_id.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                    param.put("com_id", com_id);
                }
                param.put("v_id",DemoHelper.getInstance().getCurrentUsernName());
                Log.e("Userdetailac,addp",param.toString());
                return param;
            }
        };
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    private void showDialog1() {
        LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        RelativeLayout re_item4 = (RelativeLayout) dialog.findViewById(R.id.re_item4);
        RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
        re_item4.setVisibility(View.VISIBLE);
        re_item5.setVisibility(View.VISIBLE);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        TextView tv_item4 = (TextView) dialog.findViewById(R.id.tv_item4);
        TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
        tv_item1.setText("我的派单");
        tv_item2.setText("我的合同");
        tv_item5.setText("工作报表");
        tv_item4.setText("请假条");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(QiYeDetailsActivity.this,QiyePaidanListActivity.class).putExtra("biaoshi","00").putExtra("isQunzhu",isQunzhu));
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(QiYeDetailsActivity.this,HeTongListActivity.class).putExtra("biaoshi","yonghu").putExtra("image1",recruitImages).putExtra("image2",joinImages).putExtra("body1",recruitBody)
                        .putExtra("body2",joinBody).putExtra("feiyong1",recruitMoney).putExtra("feiyong2",joinMoney).putExtra("companyName",name));
            }
        });
        re_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showDialog3();
            }
        });
        re_item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(QiYeDetailsActivity.this,QingjiaListActivity.class).putExtra("biaoshi","00"));
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private void showDialog3() {
        LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
        re_item5.setVisibility(View.VISIBLE);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
        tv_item1.setText("手动编辑");
        tv_item2.setText("上传文件");
        tv_item5.setText("我的报表");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(QiYeDetailsActivity.this,WorkstatementActivity.class).putExtra("biaoshi2","yonghu")
                        .putExtra("qiyeId",qiyeId).putExtra("userName",userName).putExtra("managerId",managerId));
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(QiYeDetailsActivity.this,WorkstatementTwoActivity.class).putExtra("biaoshi2","yonghu").putExtra("userName",userName)
                        .putExtra("qiyeId",qiyeId).putExtra("managerId",managerId));
            }
        });
        re_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(QiYeDetailsActivity.this,BaobiaoListActivity.class).putExtra("tiaojian2","01").putExtra("biaoshi","00"));
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
    public void back(View view) {
        super.back(view);
    }

    private void initView() {
        tvqiye_name = (TextView) findViewById(R.id.tvqiye_name);
        tvqiye_renshu = (TextView) findViewById(R.id.tvqiye_renshu);
        tv_my_paidan = (TextView) findViewById(R.id.tv_my_paidan);
        tv_chuangjian1 = (TextView) findViewById(R.id.zy1_chjshj);
        tv_chuangjian2 = (TextView) findViewById(R.id.zy2_chjshj);
        tv_chuangjian3 = (TextView) findViewById(R.id.zy3_chjshj);
        tv_chuangjian4 = (TextView) findViewById(R.id.zy4_chjshj);
        tv_liulan = (TextView) findViewById(R.id.tv_liulan);
        btn_fenxiang = (Button) this.findViewById(R.id.btn_fenxiang);
        btn_dianhua = (Button) this.findViewById(R.id.btn_dianhua);
        btn_msg = (Button) this.findViewById(R.id.btn_msg);
        btnAdd = (Button) this.findViewById(R.id.btn_add);
        zy1_bao = (TextView) findViewById(R.id.zy1_bao);
        zy2_bao = (TextView) findViewById(R.id.zy2_bao);
        zy3_bao = (TextView) findViewById(R.id.zy3_bao);
        zy4_bao = (TextView) findViewById(R.id.zy4_bao);
        zy1_jiedancishu = (TextView) findViewById(R.id.zy1_jiedancishu);
        zy2_jiedancishu = (TextView) findViewById(R.id.zy2_jiedancishu);
        zy3_jiedancishu = (TextView) findViewById(R.id.zy3_jiedancishu);
        zy4_jiedancishu = (TextView) findViewById(R.id.zy4_jiedancishu);
        rl1 = (RelativeLayout) this.findViewById(R.id.rl1);
        rl2 = (RelativeLayout) this.findViewById(R.id.rl2);
        rl3 = (RelativeLayout) this.findViewById(R.id.rl3);
        rl4 = (RelativeLayout) this.findViewById(R.id.rl4);
        iv_hb = (ImageView) this.findViewById(R.id.iv_hb);
        ivAvatar = (ImageView) this.findViewById(R.id.iv1);
        ivAvatar2 = (ImageView) this.findViewById(R.id.iv2);
        ivAvatar3 = (ImageView) this.findViewById(R.id.iv3);
        ivAvatar4 = (ImageView) this.findViewById(R.id.iv4);
        ivAvatar5 = (ImageView) this.findViewById(R.id.iv5);
        ivAvatar6 = (ImageView) this.findViewById(R.id.iv6);
        ivAvatar7 = (ImageView) this.findViewById(R.id.iv7);
        ivAvatar8 = (ImageView) this.findViewById(R.id.iv8);
        btn_comit1 = (Button) this.findViewById(R.id.btn_comit1);
        btn_comit2 = (Button) this.findViewById(R.id.btn_comit2);
        btn_comit3 = (Button) this.findViewById(R.id.btn_comit3);
        btn_comit4 = (Button) this.findViewById(R.id.btn_comit4);
        tvSign = (TextView) findViewById(R.id.tv_qianming);
        tv_qiye_phone = (TextView) findViewById(R.id.tv_qiye_phone);
        all_jiedancishu = (TextView) findViewById(R.id.all_jiedancishu);
        all_xiaofeicishu = (TextView) findViewById(R.id.all_xiaofeicishu);
        qiye_faren = (TextView) findViewById(R.id.qiye_faren);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);
        tvCompanyAddress = (TextView) findViewById(R.id.tv_company_address);
        tvZY1 = (TextView) findViewById(R.id.tv_zy1);
        tvZY2 = (TextView) findViewById(R.id.tv_zy2);
        tvZY3 = (TextView) findViewById(R.id.tv_zy3);
        tvZY4 = (TextView) findViewById(R.id.tv_zy4);
    }

    private void addFriendInDetail(final String hxid1) {
        final ProgressDialog dialog = new ProgressDialog(QiYeDetailsActivity.this);
        dialog.setMessage("正在发送请求...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        String url = FXConstant.URL_ADDTO_QIYE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if(code.equals("SUCCESS")){
                        EMClient.getInstance().groupManager().asyncApplyJoinToGroup(qiyeId, "请求加入企业", new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                dialog.dismiss();
                                sendPushMessage(managerId);
                                updateBmob(managerId);
                            }
                            @Override
                            public void onError(int i, String s) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        Toast.makeText(QiYeDetailsActivity.this, "发送请求失败,请勿重复发送", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            @Override
                            public void onProgress(int i, String s) {
                            }
                        });
                    }else{
                        dialog.dismiss();
                        Toast.makeText(QiYeDetailsActivity.this, "请勿重复添加", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                Toast.makeText(QiYeDetailsActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("companyId",hxid1);
                param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }
    private void sendPushMessage(final String hxid1) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String name = DemoApplication.getInstance().getCurrentUser().getName();
        if (name==null||"".equals(name)){
            name = DemoHelper.getInstance().getCurrentUsernName();
        }
        final String finalName = name;
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
                param.put("u_id",hxid1);
                param.put("body","企业消息");
                param.put("type","03");
                param.put("userId",myId);
                param.put("companyId",qiyeId);
                param.put("companyName",companyName);
                param.put("companyAdress",comAddress);
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    private void updateBmob(final String hxid1) {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                duanxintongzhi(null,null,null,null,0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
                duanxintongzhi(null,null,null,null,0);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("joinCompanyCount","1");
                param.put("userId",hxid1);
                return param;
            }
        };
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    private void duanxintongzhi(final String shareRedAmount, final String broAdd, final String newShareTimes, final String newBrowse, final int type) {
        final String id = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (type==0) {
                    Toast.makeText(QiYeDetailsActivity.this, "发送成功！请等待企业审核通过!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                if (type==1){
                    param.put("message","【正事多】 通知：您的企业红包已被分享传播完毕,一共被"+shareRedAmount+"人转发传播!");
                }else if (type==0){
                    param.put("message", "【正事多】 通知:用户" + id + "在正事多平台申请加入您的企业,希望您及时处理!");
                }else {
                    param.put("message","【正事多】您的名片红包结束报告：共获得用户的"+newShareTimes+"次分享（增长"+shareRedAmount+"%），"+newBrowse+"次浏览量（增长"+broAdd+"%）");
                }
                param.put("telNum",managerId);
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    public void doClick(View view){
        switch (view.getId()){
            case R.id.btn_msg:
                if (qiyeId.equals(currentQiye)) {
                    String imgUrl=null;
                    if (imageStr!=null&&!"".equals(imageStr)){
                        imgUrl = imageStr.split("\\|")[0];
                    }
                    Intent intent = new Intent(QiYeDetailsActivity.this, ChatActivity.class);
                    // it is group chat
                    intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                    intent.putExtra("userId", qiyeId);
                    intent.putExtra(EaseConstant.EXTRA_USER_TYPE, "企业");
                    intent.putExtra(EaseConstant.EXTRA_USER_IMG,imgUrl);
                    intent.putExtra(EaseConstant.EXTRA_USER_NAME,companyName);
                    intent.putExtra(EaseConstant.EXTRA_USER_SHARERED,exShareRed);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(QiYeDetailsActivity.this, ChatActivity.class);
                    // it is group chat
                    intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                    intent.putExtra("userId", managerId);
                    intent.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                    startActivity(intent);
                }
                break;
            case R.id.btn_add:
                showDialog2();
                break;
            case R.id.btn_comit1:
                if (qiyeId.equals(currentQiye)){
                    Toast.makeText(QiYeDetailsActivity.this,"不能给自己的企业下单！",Toast.LENGTH_SHORT).show();
                }else {
                    dType = qiyeId + "1";
                    showDialog();
                }
                break;
            case R.id.btn_comit2:
                if (qiyeId.equals(currentQiye)){
                    Toast.makeText(QiYeDetailsActivity.this,"不能给自己的企业下单！",Toast.LENGTH_SHORT).show();
                }else {
                    dType = qiyeId + "2";
                    showDialog();
                }
                break;
            case R.id.btn_comit3:
                if (qiyeId.equals(currentQiye)){
                    Toast.makeText(QiYeDetailsActivity.this,"不能给自己的企业下单！",Toast.LENGTH_SHORT).show();
                }else {
                    dType = qiyeId + "3";
                    showDialog();
                }
                break;
            case R.id.btn_comit4:
                if (qiyeId.equals(currentQiye)){
                    Toast.makeText(QiYeDetailsActivity.this,"不能给自己的企业下单！",Toast.LENGTH_SHORT).show();
                }else {
                    dType = qiyeId + "4";
                    showDialog();
                }
                break;
            case R.id.btn_dianhua:
                if (comTell.contains("20000000")) {
                    comTell = "4000010084";
                }
                LayoutInflater inflater = LayoutInflater.from(QiYeDetailsActivity.this);
                RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_alert, null);
                final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
                dialog.show();
                dialog.getWindow().setContentView(layout);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                btnOK.setText("确定");
                btnCancel.setText("取消");
                title_tv.setText("确认拨打电话么？");
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isAllowPhone()) {
                            PermissionUtil permissionUtil = new PermissionUtil(QiYeDetailsActivity.this);
                            permissionUtil.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                    new PermissionListener() {
                                        @Override
                                        public void onGranted() {
                                            dialog.dismiss();
                                            UserPermissionUtil.getUserPermission(QiYeDetailsActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "2", new UserPermissionUtil.UserPermissionListener() {
                                                @Override
                                                public void onAllow() {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Intent.ACTION_CALL);
                                                    //url:统一资源定位符
                                                    //uri:统一资源标示符（更广）
                                                    intent.setData(Uri.parse("tel:" + comTell));
                                                    //开启系统拨号器
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onBan() {
                                                    ToastUtils.showNOrmalToast(QiYeDetailsActivity.this.getApplicationContext(), "您的账户已被禁止打聊天");

                                                }
                                            });

                                        }

                                        @Override
                                        public void onDenied(List<String> deniedPermission) {
                                            dialog.dismiss();
                                            //Toast第一个被拒绝的权限
                                            Toast.makeText(QiYeDetailsActivity.this.getApplicationContext(), "您拒绝了拨打电话的权限！", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onShouldShowRationale(List<String> deniedPermission) {
                                            dialog.dismiss();
                                            //Toast第一个勾选不在提示的权限
                                            Toast.makeText(QiYeDetailsActivity.this.getApplicationContext(), "您拒绝了拨打电话的权限,请前往设置手动打开！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {

                            LayoutInflater inflater2 = LayoutInflater.from(QiYeDetailsActivity.this);
                            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog2 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
                            dialog2.show();
                            dialog2.getWindow().setContentView(layout2);
                            dialog2.setCanceledOnTouchOutside(true);
                            dialog2.setCancelable(true);
                            TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                            Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                            final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                            TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                            title2.setText("温馨提示");
                            btnOK2.setText("确定");
                            btnCancel2.setText("取消");
                            title_tv2.setText("主动找不到合适的" +
                                    "\n" +
                                    " 那就发个需求吧" +
                                    "\n" +
                                    " 让人主动联系您");
                            btnCancel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                }
                            });
                            btnOK2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent2 = new Intent(QiYeDetailsActivity.this, MomentsPublishActivity.class);
                                    intent2.putExtra("biaoshi", "xuqiu");
                                    startActivity(intent2);
                                    dialog2.dismiss();
                                }
                            });
                        }

                    }
                });
                break;
            case R.id.btn_fenxiang:
                if (!DemoHelper.getInstance().isLoggedIn(QiYeDetailsActivity.this)){
                    Toast.makeText(getApplicationContext(),"登陆后才能分享哦",Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflater3 = LayoutInflater.from(QiYeDetailsActivity.this);
                RelativeLayout layout3 = (RelativeLayout) inflater3.inflate(R.layout.dialog_alert, null);
                final Dialog dialog3 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
                dialog3.show();
                dialog3.getWindow().setContentView(layout3);
                dialog3.setCanceledOnTouchOutside(true);
                dialog3.setCancelable(true);
                TextView title_tv3 = (TextView) layout3.findViewById(R.id.title_tv);
                Button btnCancel3 = (Button) layout3.findViewById(R.id.btn_cancel);
                final Button btnOK3 = (Button) layout3.findViewById(R.id.btn_ok);
                btnOK3.setText("确定");
                btnCancel3.setText("取消");
                title_tv3.setText("确认分享么？");
                btnCancel3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog3.dismiss();
                    }
                });
                btnOK3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog3.dismiss();
                        saveCurrentImage();
                    }
                });
                break;
            case R.id.iv_hb:
                if (!DemoHelper.getInstance().isLoggedIn(QiYeDetailsActivity.this)){
                    Toast.makeText(getApplicationContext(),"登陆后才能分享，并获得红包哦",Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = FXConstant.URL_SEARCH_REDAUTH;

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {

                            JSONObject object = new JSONObject(s);
                            String code = object.getString("code");
                            if (code==null||"".equals(code)||code.equalsIgnoreCase("null")){

                                    UserPermissionUtil.getUserPermission(QiYeDetailsActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "8", new UserPermissionUtil.UserPermissionListener() {
                                    @Override
                                    public void onAllow() {
                                        PermissionUtil permissionUtil = new PermissionUtil(QiYeDetailsActivity.this);
                                        permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                                                        , Manifest.permission.READ_PHONE_STATE
                                                        , Manifest.permission.ACCESS_WIFI_STATE},
                                                new PermissionListener() {
                                                    @Override
                                                    public void onGranted() {
                                                        queryhbzgCount();
                                                    }

                                                    @Override
                                                    public void onDenied(List<String> deniedPermission) {
                                                        Toast.makeText(QiYeDetailsActivity.this.getApplicationContext(), "您拒绝了获取定位的权限！无法赚取红包 请手动开启权限", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onShouldShowRationale(List<String> deniedPermission) {
                                                        Toast.makeText(QiYeDetailsActivity.this.getApplicationContext(), "您拒绝了获取定位的权限！无法赚取红包 请手动开启权限", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                                        startActivity(intent);
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onBan() {
                                        ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止转发红包");

                                    }
                                });


                            }else
                            {
                                final LayoutInflater inflater1 = LayoutInflater.from(QiYeDetailsActivity.this);
                                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                                final Dialog dialog1 = new AlertDialog.Builder(QiYeDetailsActivity.this, R.style.Dialog).create();
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
                                title_tv1.setText("您需要邀请20个用户注册才可以继续赚取红包");
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

                                        //跳转到通讯录界面邀请好友注册  标识是红包

                                        if (ContextCompat.checkSelfPermission(QiYeDetailsActivity.this, Manifest.permission.READ_CONTACTS)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(QiYeDetailsActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
                                        }else {
                                            Intent intent = new Intent(QiYeDetailsActivity.this,AddressListActivity.class);

                                            intent.putExtra("redAuth","yes");

                                            startActivityForResult(intent,0);
                                        }
                                    }
                                });
                            }

                        }catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        if (volleyError!=null) {
                            Log.e("hongbao", volleyError.getMessage());
                            Log.d("chen", "hongbao" + volleyError.getMessage());
                        }
                        Toast.makeText(QiYeDetailsActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> param = new HashMap<>();

                        param.put("rid",DemoHelper.getInstance().getCurrentUsernName());

                        return param;
                    }
                };

                MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限通过
                Intent intent = new Intent(QiYeDetailsActivity.this,AddressListActivity.class);
                intent.putExtra("redAuth","yes");
                startActivityForResult(intent,0);

            } else {  //权限拒绝

                Toast.makeText(QiYeDetailsActivity.this,"您拒绝了访问通讯录权限，请前往设置手动打开权限！",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void queryhbzgCount() {
        String url = FXConstant.URL_Get_UserInfo+DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject userInfo = object.getJSONObject("userInfo");
                String homePageTimes = userInfo.getString("homePageTimes");
                final String score = userInfo.getString("score");
                final String withdrawals = DemoApplication.getInstance().getCurrentWithdrawals();
                if (homePageTimes==null||"".equals(homePageTimes)||Double.parseDouble(homePageTimes)==0){
                    LayoutInflater inflater2 = LayoutInflater.from(QiYeDetailsActivity.this);
                    RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog2 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout2);
                    dialog2.setCanceledOnTouchOutside(true);
                    dialog2.setCancelable(true);
                    TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                    Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                    final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                    TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                    if ("提现".equals(withdrawals)&&"否".equals(score)) {
                        title2.setText("温馨提示");
                        btnOK2.setText("前去评分");
                        btnCancel2.setText("下次再说");
                        title_tv2.setText("您的红包次数已用完,前去应用市场为软件评分,即可永久获得一次分享次数！");
                    }else {
                        title2.setText("温馨提示");
                        btnOK2.setText("增加次数");
                        btnCancel2.setText("取消操作");
                        title_tv2.setText("您的红包次数已用完！");
                    }
                    btnCancel2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    btnOK2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            if ("提现".equals(withdrawals)&&"否".equals(score)) {
                                try {
                                    Uri uri = Uri.parse("market://details?id="
                                            + getPackageName());//需要评分的APP包名
                                    Intent intent5 = new Intent(Intent.ACTION_VIEW, uri);
                                    intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivityForResult(intent5,1);
                                } catch (Exception e) {
                                    Toast.makeText(QiYeDetailsActivity.this, "跳转失败,请下载应用宝之后评分", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                startActivity(new Intent(QiYeDetailsActivity.this, HbHuoQuActivity.class));
                            }
                        }
                    });
                }else {
                    if ("no".equals(redInterval)) {
                        queryHBZhuFCShu();
                    } else {
                        showhbClick();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络连接中断",Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    private void updateScore() {
        String url = FXConstant.URL_UPDATE_TIME;
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
                Map<String,String> params = new HashMap<String, String>();
                params.put("uLoginId",DemoHelper.getInstance().getCurrentUsernName());
                params.put("score","是");
                return params;
            }
        };
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    private void updateHbTimes() {
        String url = FXConstant.URL_XIUGAI_HBTIMES;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getApplicationContext(),"红包次数增加成功！",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("shareTimes", "1");
                param.put("homePageTimes", "1");
                param.put("dynamicTimes", "1");
                return param;
            }
        };
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                updateHbTimes();
                updateScore();
                break;
        }
    }

    private void queryHBZhuFCShu() {
        String url = FXConstant.URL_QUERY_ZHFHBCSHU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("0".equals(code)){
                    showhbClick();
                }else {
                    LayoutInflater inflater2 = LayoutInflater.from(QiYeDetailsActivity.this);
                    RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog2 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout2);
                    dialog2.setCanceledOnTouchOutside(true);
                    dialog2.setCancelable(true);
                    TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                    Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                    final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                    TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                    title2.setText("温馨提示");
                    btnOK2.setText("确定");
                    btnCancel2.setText("取消");
                    title_tv2.setText("一人只能领一次红包，本次无奖励，是否继续？");
                    btnCancel2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    btnOK2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            showhbClick();
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showhbClick();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("mer_id", DemoHelper.getInstance().getCurrentUsernName());
                param.put("user_id",qiyeId);
                return param;
            }
        };
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    private void showhbClick() {
        final int i1 = DemoHelper.getInstance().getContactList().size();
        LayoutInflater inflater5 = LayoutInflater.from(QiYeDetailsActivity.this);
        RelativeLayout layout5 = (RelativeLayout) inflater5.inflate(R.layout.dialog_red, null);
        final Dialog dialog5 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
        dialog5.show();
        dialog5.getWindow().setContentView(layout5);
        dialog5.setCanceledOnTouchOutside(true);
        dialog5.setCancelable(true);
        TextView tv_title  = (TextView) layout5.findViewById(R.id.tv_title);
        TextView tv_title1 = (TextView) layout5.findViewById(R.id.tv_title1);
        TextView tv_title2 = (TextView) layout5.findViewById(R.id.tv_title2);
        RelativeLayout rl_qq = (RelativeLayout) layout5.findViewById(R.id.rl_qq);
        RelativeLayout rl_wx = (RelativeLayout) layout5.findViewById(R.id.rl_wx);
        RelativeLayout rl_wb = (RelativeLayout) layout5.findViewById(R.id.rl_wb);
        ImageView iv_qq = (ImageView) layout5.findViewById(R.id.iv_qq);
        ImageView iv_wx = (ImageView) layout5.findViewById(R.id.iv_wx);
        ImageView iv_wb = (ImageView) layout5.findViewById(R.id.iv_wb);
        if (shareType==null||"".equals(shareType)||(fxPingTai!=null&&fxPingTai.contains("不限"))){
            iv_qq.setVisibility(View.VISIBLE);
            iv_wx.setVisibility(View.VISIBLE);
            iv_wb.setVisibility(View.VISIBLE);
        }else {
            if (fxPingTai != null && fxPingTai.contains("空间")) {
                iv_qq.setVisibility(View.VISIBLE);
            }
            if (fxPingTai != null && fxPingTai.contains("微信")) {
                iv_wx.setVisibility(View.VISIBLE);
            }
            if (fxPingTai != null && fxPingTai.contains("微博")) {
                iv_wb.setVisibility(View.VISIBLE);
            }
        }
        String [] num;
        if (friendsNumber != null&&!"".equals(friendsNumber)&&!friendsNumber.equalsIgnoreCase("null")&&!"0".equals(friendsNumber)) {
            num = friendsNumber.split("\\|");
            onedown = num[0];
            if (shareRed!=null&& !"".equals(shareRed)&&!shareRed.equalsIgnoreCase("null")&&Double.parseDouble(shareRed)>0&&Double.parseDouble(onedown)>0){
                int i2;
                if ((Double.parseDouble(shareRed) * 100) % (Double.parseDouble(onedown) * 100) != 0) {
                    i2 = (int) ((Double.parseDouble(shareRed) * 100) / (Double.parseDouble(onedown) * 100)) + 1;
                } else {
                    i2 = (int) ((Double.parseDouble(shareRed) * 100) / (Double.parseDouble(onedown) * 100));
                }
                tv_title2.setText(String.valueOf(i2) + "个");
            }
            singleShare = onedown;
        }
        double bktx = Double.parseDouble(onedown);
        String prices1 = String.format("%.2f", bktx);
        tv_title1.setText(prices1);
        String Jine = null;
        if (shareRed != null && singleShare != null&&!"".equals(shareRed)&&!"".equals(singleShare)&&!shareRed.equalsIgnoreCase("null")) {
            if (Double.parseDouble(shareRed) > (Double.parseDouble(singleShare))) {
                Jine = singleShare;
            }else {
                Jine = shareRed;
            }
        }
        final String fenxiangJine = Jine;
        rl_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog5.dismiss();
                if (hbYaoqiu!=null&&!"".equals(hbYaoqiu)&&!hbYaoqiu.equalsIgnoreCase("null")&&Double.parseDouble(hbYaoqiu)>i1) {
                    LayoutInflater inflater1 = LayoutInflater.from(QiYeDetailsActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
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
                    title_tv1.setText("不符合红包要求,用户设置好友达到"+hbYaoqiu+"人以上才能获得红包,是否继续分享？");
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
                            saveCurrentImage();
                        }
                    });
                }else {
                    if (fenxiangJine!=null&&Double.parseDouble(fenxiangJine)>0) {
                        if (Double.parseDouble(fenxiangJine)!=Double.parseDouble(singleShare)) {
                            LayoutInflater inflater2 = LayoutInflater.from(QiYeDetailsActivity.this);
                            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog2 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
                            dialog2.show();
                            dialog2.getWindow().setContentView(layout2);
                            dialog2.setCanceledOnTouchOutside(true);
                            dialog2.setCancelable(true);
                            TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                            Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                            final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                            TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                            title2.setText("温馨提示");
                            btnOK2.setText("确定");
                            btnCancel2.setText("取消");
                            title_tv2.setText("红包余额不足圈子奖励,此次分享只能获得"+fenxiangJine+"元,是否继续分享?");
                            btnCancel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                }
                            });
                            btnOK2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                    queryfxhbCount(fenxiangJine,0);
                                }
                            });
                        }else {
                            queryfxhbCount(fenxiangJine,0);
                        }
                    }else {
                        saveCurrentImage();
                    }
                }
            }
        });
        rl_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog5.dismiss();//WechatMoments.NAME
                if (hbYaoqiu!=null&&!"".equals(hbYaoqiu)&&!hbYaoqiu.equalsIgnoreCase("null")&&Double.parseDouble(hbYaoqiu)>i1) {
                    LayoutInflater inflater1 = LayoutInflater.from(QiYeDetailsActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
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
                    title_tv1.setText("不符合红包要求,用户设置好友达到"+hbYaoqiu+"人以上才能获得红包,是否继续分享？");
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
                            saveCurrentImage();
                        }
                    });
                }else {
                    if (fenxiangJine!=null&&Double.parseDouble(fenxiangJine)>0) {
                        if (Double.parseDouble(fenxiangJine)!=Double.parseDouble(singleShare)) {
                            LayoutInflater inflater2 = LayoutInflater.from(QiYeDetailsActivity.this);
                            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog2 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
                            dialog2.show();
                            dialog2.getWindow().setContentView(layout2);
                            dialog2.setCanceledOnTouchOutside(true);
                            dialog2.setCancelable(true);
                            TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                            Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                            final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                            TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                            title2.setText("温馨提示");
                            btnOK2.setText("确定");
                            btnCancel2.setText("取消");
                            title_tv2.setText("红包余额不足圈子奖励,此次分享只能获得"+fenxiangJine+"元,是否继续分享?");
                            btnCancel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                }
                            });
                            btnOK2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                    queryfxhbCount(fenxiangJine,1);
                                }
                            });
                        }else {
                            queryfxhbCount(fenxiangJine,1);
                        }
                    }else {
                        saveCurrentImage();
                    }
                }
            }
        });
        rl_wb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog5.dismiss();//SinaWeibo.NAME
                if (hbYaoqiu!=null&&!"".equals(hbYaoqiu)&&!hbYaoqiu.equalsIgnoreCase("null")&&Double.parseDouble(hbYaoqiu)>i1) {
                    LayoutInflater inflater1 = LayoutInflater.from(QiYeDetailsActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
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
                    title_tv1.setText("不符合红包要求,用户设置好友达到"+hbYaoqiu+"人以上才能获得红包,是否继续分享？");
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
                            saveCurrentImage();
                        }
                    });
                }else {
                    if (fenxiangJine!=null&&Double.parseDouble(fenxiangJine)>0) {
                        if (Double.parseDouble(fenxiangJine)!=Double.parseDouble(singleShare)) {
                            LayoutInflater inflater2 = LayoutInflater.from(QiYeDetailsActivity.this);
                            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog2 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
                            dialog2.show();
                            dialog2.getWindow().setContentView(layout2);
                            dialog2.setCanceledOnTouchOutside(true);
                            dialog2.setCancelable(true);
                            TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                            Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                            final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                            TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                            title2.setText("温馨提示");
                            btnOK2.setText("确定");
                            btnCancel2.setText("取消");
                            title_tv2.setText("红包余额不足圈子奖励,此次分享只能获得"+fenxiangJine+"元,是否继续分享?");
                            btnCancel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                }
                            });
                            btnOK2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                    queryfxhbCount(fenxiangJine,2);
                                }
                            });
                        }else {
                            queryfxhbCount(fenxiangJine,2);
                        }
                    }else {
                        saveCurrentImage();
                    }
                }
            }
        });
    }

    private void queryfxhbCount(final String fenxiangJine, final int type) {
        String url = FXConstant.URL_QUERY_HBCOUNT+DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                int sum = object.getIntValue("sum");
                sum = sum+1;
                if (type==0) {
                    fenxiangtoqq(fenxiangJine,sum);
                }else if (type==1){
                    fenxiangtowx(fenxiangJine,sum);
                }else if (type==2){
                    fenxiangtowb(fenxiangJine,sum);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (type==0) {
                    fenxiangtoqq(fenxiangJine,-1);
                }else if (type==1){
                    fenxiangtowx(fenxiangJine,-1);
                }else if (type==2){
                    fenxiangtowb(fenxiangJine,-1);
                }
            }
        });
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    private void fenxiangtoqq(final String fenxiangJine, int sum) {
        ScreenshotUtil.getBitmapByView(QiYeDetailsActivity.this, findViewById(R.id.ll2), "分享企业红包", fenxiangJine,sum,true,0,0);
        String filep = Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png";
        if (!new File(filep).exists()){
            ScreenshotUtil.saveDrawableById(QiYeDetailsActivity.this,R.drawable.share_mingpian);
        }
        final QZone.ShareParams sp = new QZone.ShareParams();
        if (fxLeiXing!=null&&fxLeiXing.contains("图文")){
            sp.setText(null);
            sp.setTitle(null);
            sp.setTitleUrl(null);
            sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
            showqqfxType(sp,fenxiangJine);
        }else if (fxLeiXing!=null&&fxLeiXing.contains("链接")){
            sp.setTitleUrl("http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId);
            sp.setSite("分享链接");
            sp.setTitle("正事多-接单派单工具");
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
            sp.setText("我转发他的主页名片，获得了一个红包，"+fxUpName+"不限行业接派单，全民分享赚红包");
            sp.setSiteUrl("http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId);
            showqqfxType(sp,fenxiangJine);
        }else {
            LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
            TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
            RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
            RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
            RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
            tv_item1.setText("分享图文");
            tv_item2.setText("分享链接");
            re_item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    sp.setText(null);
                    sp.setTitle(null);
                    sp.setTitleUrl(null);
                    sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                    showqqfxType(sp,fenxiangJine);
                }
            });
            re_item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    sp.setTitleUrl("http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId);
                    sp.setSite("分享链接");
                    sp.setTitle("正事多-接单派单工具");
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                    sp.setText("我转发他的主页名片，获得了一个红包，"+fxUpName+"不限行业接派单，全民分享赚红包");
                    sp.setSiteUrl("http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId);
                    showqqfxType(sp,fenxiangJine);
                }
            });
            re_item3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

    }

    private void showqqfxType(QZone.ShareParams sp,final String fenxiangJine) {
        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qzone.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                if (fxPingTai!=null&&fxPingTai.contains("空间")){
                    queryHByuE(fenxiangJine);
                }else if (fxPingTai==null||"".equals(fxPingTai)){
                    queryHByuE(fenxiangJine);
                }
            }
            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        qzone.share(sp);
    }

    private void fenxiangtowx(final String fenxiangJine, int sum) {
        ScreenshotUtil.getBitmapByView(QiYeDetailsActivity.this, findViewById(R.id.ll2), "分享企业红包", fenxiangJine,sum,true,0,0);
        String filep = Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png";
        if (!new File(filep).exists()){
            ScreenshotUtil.saveDrawableById(QiYeDetailsActivity.this,R.drawable.share_mingpian);
        }
        final WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        if (fxLeiXing!=null&&fxLeiXing.contains("图文")){
            sp.setShareType(Platform.SHARE_IMAGE);
            sp.setTitle("正事多app");
            sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
            showwxfxType(sp,fenxiangJine);
        }else if (fxLeiXing!=null&&fxLeiXing.contains("链接")){
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
            sp.setTitle("我转发他的主页名片，获得了一个红包，"+fxUpName+"不限行业接派单，全民分享赚红包");
            sp.setUrl("http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId);
            showwxfxType(sp,fenxiangJine);
        }else {
            LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
            TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
            RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
            RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
            RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
            tv_item1.setText("分享图文");
            tv_item2.setText("分享链接");
            re_item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    sp.setShareType(Platform.SHARE_IMAGE);
                    sp.setTitle("正事多app");
                    sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                    showwxfxType(sp,fenxiangJine);
                }
            });
            re_item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    sp.setShareType(Platform.SHARE_WEBPAGE);
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                    sp.setTitle("我转发他的主页名片，获得了一个红包，"+fxUpName+"不限行业接派单，全民分享赚红包");
                    sp.setUrl("http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId);
                    showwxfxType(sp,fenxiangJine);
                }
            });
            re_item3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void showwxfxType(WechatMoments.ShareParams sp, final String fenxiangJine) {
        Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getApplicationContext(), "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                if (fxPingTai!=null&&fxPingTai.contains("微信")){
                    queryHByuE(fenxiangJine);
                }else if (fxPingTai==null||"".equals(fxPingTai)){
                    queryHByuE(fenxiangJine);
                }
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wx.share(sp);
    }

    private void fenxiangtowb(final String fenxiangJine, int sum) {
        ScreenshotUtil.getBitmapByView(QiYeDetailsActivity.this, findViewById(R.id.ll2), "分享企业红包", fenxiangJine,sum,true,0,0);
        String filep = Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png";
        if (!new File(filep).exists()){
            ScreenshotUtil.saveDrawableById(QiYeDetailsActivity.this,R.drawable.share_mingpian);
        }
        final SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        if (fxLeiXing!=null&&fxLeiXing.contains("图文")){
            sp.setShareType(Platform.SHARE_IMAGE);
            sp.setTitle("正事多app");
            sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
            showwbfxType(sp,fenxiangJine);
        }else if (fxLeiXing!=null&&fxLeiXing.contains("链接")){
            String url = "http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId;
            sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut2.png");
            sp.setText("我转发他的主页名片，获得了一个红包，"+fxUpName+"不限行业接派单，全民分享赚红包"+url);
            sp.setTitle("正事多-接单派单工具");
            showwbfxType(sp,fenxiangJine);
        }else {
            LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
            TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
            RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
            RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
            RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
            tv_item1.setText("分享图文");
            tv_item2.setText("分享链接");
            re_item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    sp.setShareType(Platform.SHARE_IMAGE);
                    sp.setTitle("正事多app");
                    sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                    showwbfxType(sp,fenxiangJine);
                }
            });
            re_item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    String url = "http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId;
                    sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut2.png");
                    sp.setText("我转发他的主页名片，获得了一个红包，"+fxUpName+"不限行业接派单，全民分享赚红包"+url);
                    sp.setTitle("正事多-接单派单工具");
                    showwbfxType(sp,fenxiangJine);
                }
            });
            re_item3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void showwbfxType(SinaWeibo.ShareParams sp, final String fenxiangJine) {
        Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wb.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getApplicationContext(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
                if (fxPingTai!=null&&fxPingTai.contains("微博")){
                    queryHByuE(fenxiangJine);
                }else if (fxPingTai==null||"".equals(fxPingTai)){
                    queryHByuE(fenxiangJine);
                }
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wb.share(sp);
    }

    private void queryHByuE(final String fenxiangJine) {
        String url = FXConstant.URL_Get_QiyeInfo+qiyeId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject object = jsonObject.getJSONObject("companyInfo");
                    shareRed = object.isNull("shareRed")?"0":object.getString("shareRed");
                    friendsNumber = object.isNull("friendsNumber")?"0|0|0":object.getString("friendsNumber");
                    int i1 = DemoHelper.getInstance().getContactList().size();
                    String[] num;
                    if (friendsNumber != null) {
                        num = friendsNumber.split("\\|");
                        onedown = num[0];
                        singleShare = onedown;
                        String Jine = null;
                        if (shareRed != null && singleShare != null) {
                            if (Double.parseDouble(shareRed) > (Double.parseDouble(singleShare))) {
                                Jine = singleShare;
                            }else {
                                Jine = shareRed;
                            }
                        }
                        if (!fenxiangJine.equals(singleShare)){
                            iv_hb.setVisibility(View.INVISIBLE);
                            btn_fenxiang.setVisibility(View.VISIBLE);
                        }
                        if (fenxiangJine.equals(Jine)){
                            if (managerId!=null) {
                                queryHBCount();
                            }
                            addhongbao(Jine);
                            dongtai();
                        }else if (Jine!=null&&Double.parseDouble(Jine)>0){
                            Toast.makeText(getApplicationContext(),"手慢了，红包仅剩"+Jine+"元",Toast.LENGTH_LONG).show();
                            iv_hb.setVisibility(View.INVISIBLE);
                            btn_fenxiang.setVisibility(View.VISIBLE);
                            if (managerId!=null) {
                                queryHBCount();
                            }
                            addhongbao(Jine);
                            dongtai();
                        }else {
                            LayoutInflater inflater1 = LayoutInflater.from(QiYeDetailsActivity.this);
                            RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog1 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
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
                            title_tv1.setText("手慢了，红包已经被抢完了！");
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
        });
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    private void queryHBCount() {
        String url = FXConstant.URL_MINGZI_DAN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String shareRedAmount = object.getString("shareRedAmount");
                    int shareCount;
                    if (shareRedAmount==null||"".equals(shareRedAmount)||"0".equals(shareRedAmount)){
                        shareCount = 1;
                    }else {
                        shareCount = Integer.parseInt(shareRedAmount);
                        shareCount += 1;
                    }
//                    duanxintongzhi(shareCount+"",null,null,null,1);
                    if (shareRed!=null&&singleShare!=null){
                        if (Double.parseDouble(shareRed)<=Double.parseDouble(singleShare)){
                            queryZengZhang(shareCount+"");
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
                Map<String, String> params = new HashMap<>();
                params.put("merId",qiyeId);
                params.put("currentPage","3");
                params.put("transactionType","红包");
                params.put("accType","支出");
                Log.e("userdeac,pa",params.toString());
                return params;
            }
        };
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    private void queryZengZhang(final String shareRedAmount) {
        String url = FXConstant.URL_QUERY_SHARECORD;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject shareRedRecord = object.optJSONObject("shareRedRecord");
                    String newShareTimes = shareRedAmount;
                    if (shareRedAmount==null||"".equals(shareRedAmount)){
                        newShareTimes = "0";
                    }
                    int newBrowse = 0;
                    if (personalDtails!=null&&!"".equals(personalDtails)){
                        newBrowse = Integer.parseInt(personalDtails);
                    }
                    if (shareRedRecord==null||"".equals(shareRedRecord)){
                        insertZengZhang(newShareTimes,newBrowse+"","0","0");
                    }else {
                        String oldShareTimes = shareRedRecord.getString("shareTimes");
                        String oldBrowse = shareRedRecord.getString("browse");
                        insertZengZhang(newShareTimes,newBrowse+"",oldShareTimes,oldBrowse);
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
                Map<String, String> params = new HashMap<>();
                params.put("u_id",qiyeId);
                return params;
            }
        };
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    private void insertZengZhang(final String newShareTimes, final String newBrowse,final String oldShareTimes,final String oldBrowse) {
        String url = FXConstant.URL_INSERT_SHARECORD;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (oldShareTimes==null||"".equals(oldShareTimes)||"0".equals(oldShareTimes)){
                    duanxintongzhi("100","100",newShareTimes,newBrowse,2);
                }else {
                    double newTime = Double.parseDouble(newShareTimes);
                    double newBrow = Double.parseDouble(newBrowse);
                    double oldTime = Double.parseDouble(oldShareTimes);
                    double oldBrow = Double.parseDouble(oldBrowse);
                    double shareAdd = (newTime - oldTime)*100/oldTime;
                    double browdd = (newBrow - oldBrow)*100/oldBrow;
                    duanxintongzhi(shareAdd+"",browdd+"",newShareTimes,newBrowse,2);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("u_id",qiyeId);
                params.put("browse",newBrowse);
                params.put("shareTimes",newShareTimes);
                return params;
            }
        };
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    private void addhongbao(final String fenxiangJine) {
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        Log.e("qiyedetail,add","开始加红包");
        String url = FXConstant.URL_HUOQU_QIYEFXHONGBAO;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s==null||"".equals(s)||"{}".equals(s)){
                    LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                    final Dialog dialog2 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.setCancelable(false);
                    TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                    Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                    if ("no".equals(redInterval)){
                        tv_title.setText("一人只能领一次红包");
                    }else {
                        int time;
                        if (redInterval.equalsIgnoreCase("null")||redInterval==null||"".equals(redInterval)||"0".equals(redInterval)){
                            time = 1;
                        }else {
                            time = Integer.valueOf(redInterval);
                        }
                        tv_title.setText("请"+String.valueOf(time)+"天后再来吧~");
                    }
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                }
                Log.e("qiyedetail,s",s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if ("success".equals(code)) {
                        SoundPlayUtils.play(2);
                        LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_hongbao, null);
                        final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        TextView tv_title1 = (TextView) layout.findViewById(R.id.tv_title1);
                        TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
                        TextView tv_yue = (TextView) layout.findViewById(R.id.tv_yue);
                        TextPaint tp = tv_title.getPaint();
                        tp.setFakeBoldText(true);
                        tv_title.setText(fenxiangJine + "元");
                        tv_title1.setText(companyName);
                        tv_yue.setText("正事多 企业红包");
                        layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
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
                Map<String ,String> param = new HashMap<>();
                param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                param.put("singleShare",fenxiangJine);
                param.put("merId",qiyeId);
                if ("0".equals(redInterval)||"".equals(redInterval)){
                    param.put("redInterval","24");
                }else if ("no".equals(redInterval)){
                    param.put("redInterval","no");
                }else {
                    param.put("redInterval",String.valueOf(Integer.valueOf(redInterval)*24));
                }
                return param;
            }
        };
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);

        } else {
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }

    private void dongtai() {
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "4", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                ArrayList<String> imagePaths1 = new ArrayList<>();
                imagePaths1.add(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                List<Param> params=new ArrayList<>();
                params.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
                params.add(new Param("content","我得到了一个企业分享红包了!"));
                params.add(new Param("authType","01"));
                params.add(new Param("shareType","yes"));
                params.add(new Param("shareUserId",qiyeId));
                params.add(new Param("dType","01"));
                OkHttpManager.getInstance().postMoment(params, imagePaths1, FXConstant.URL_PUBLISH, new OkHttpManager.HttpCallBack() {
                    @Override
                    public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                        String code = jsonObject.getString("code");
                        if (code.equals("SUCCESS")) {
                            updateBmob1();
                        }
                    }
                    @Override
                    public void onFailure(String errorMsg) {
                    }
                });
            }

            @Override
            public void onBan() {
                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送动态");

            }
        });

    }
    private void updateBmob1() {
        String url = FXConstant.URL_UPDATE_DYNATIME;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                setResult(RESULT_OK);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setResult(RESULT_OK);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("type5",getNowTime());
                return param;
            }
        };
        MySingleton.getInstance(QiYeDetailsActivity.this).addToRequestQueue(request);
    }

    private void saveCurrentImage() {
        ScreenshotUtil.getBitmapByView(QiYeDetailsActivity.this, findViewById(R.id.ll2), "分享名片红包",null,6,false,0,0);
        String filep = Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png";
        if (!new File(filep).exists()){
            ScreenshotUtil.saveDrawableById(QiYeDetailsActivity.this,R.drawable.share_mingpian);
        }
        LayoutInflater inflater5 = LayoutInflater.from(QiYeDetailsActivity.this);
        final RelativeLayout layout5 = (RelativeLayout) inflater5.inflate(R.layout.dialog_fenxiang, null);
        final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
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
        TextView tv4 = (TextView) layout5.findViewById(R.id.tv4);
        TextView tv5 = (TextView) layout5.findViewById(R.id.tv5);
        RelativeLayout rl1 = (RelativeLayout) layout5.findViewById(R.id.rl1);
        RelativeLayout rl2 = (RelativeLayout) layout5.findViewById(R.id.rl2);
        RelativeLayout rl3 = (RelativeLayout) layout5.findViewById(R.id.rl3);
        RelativeLayout rl4 = (RelativeLayout) layout5.findViewById(R.id.rl4);
        RelativeLayout rl5 = (RelativeLayout) layout5.findViewById(R.id.rl5);
        RelativeLayout rl6 = (RelativeLayout) layout5.findViewById(R.id.rl6);
        rl6.setVisibility(View.INVISIBLE);
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtoqqz();
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtowxm();
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtowb();
            }
        });
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtoqqf();
            }
        });
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtowxf();
            }
        });
    }

    private void fxtoqqf() {
        final QQ.ShareParams sp = new QQ.ShareParams();
        LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle(null);
                sp.setText(null);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qq.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qq.share(sp);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitleUrl("http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId);
                sp.setSite("分享链接");
                sp.setTitle("正事多-接单派单工具");
                sp.setText("我转发他的主页名片，"+fxUpName+"不限行业接派单，全民分享赚红包");
                sp.setSiteUrl("http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qq.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qq.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void fxtoqqz() {
        final QZone.ShareParams sp = new QZone.ShareParams();
        LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle(null);
                sp.setText(null);
                Platform qq = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qq.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qq.share(sp);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitleUrl("http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId);
                sp.setSite("分享链接");
                sp.setTitle("正事多-接单派单工具");
                sp.setText("我转发他的主页名片，"+fxUpName+"不限行业接派单，全民分享赚红包");
                sp.setSiteUrl("http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId);
                Platform qq = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qq.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qq.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void fxtowxm() {
        final WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("【正事多】里面有接单派单名片红包");
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
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
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setShareType(Platform.SHARE_WEBPAGE);
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitle("我转发他的主页名片，"+fxUpName+"不限行业接派单，全民分享赚红包");
                sp.setUrl("http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId);
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
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

    private void fxtowxf() {
        final Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("【正事多】里面有接单派单名片红包");
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setShareType(Platform.SHARE_WEBPAGE);
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitle("我转发他的主页名片，"+fxUpName+"不限行业接派单，全民分享赚红包");
                sp.setText("我转发他的主页名片，"+fxUpName+"不限行业接派单，全民分享赚红包");
                sp.setUrl("http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId);
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
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

    private void fxtowb() {
        final SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("正事多-接单派单工具");
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wb.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wb.share(sp);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String url = "http://www.fulu86.com/Details_qiye.html?companyId="+qiyeId;
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut2.png");
                sp.setText("我转发他的主页名片，"+fxUpName+"不限行业接派单，全民分享赚红包"+url);
                sp.setTitle("正事多-接单派单工具");
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wb.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wb.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showDialog2() {
        LayoutInflater inflater2 = LayoutInflater.from(QiYeDetailsActivity.this);
        RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
        final Dialog dialog2 = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
        dialog2.show();
        dialog2.getWindow().setContentView(layout2);
        dialog2.setCanceledOnTouchOutside(true);
        dialog2.setCancelable(true);
        TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
        Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
        final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
        TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
        title2.setText("温馨提示");
        btnOK2.setText("现在申请");
        btnCancel2.setText("稍后申请");
        title_tv2.setText("因牵涉到企业管理模式、资源分配、管理费用、加盟费用等，建议提前跟企业相关人员提前沟通后再选择申请加入企业");
        btnCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        btnOK2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                showdialog3();
            }
        });
    }

    private void showdialog3() {
        LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        tv_item1.setText("应聘企业");
        tv_item2.setText("加盟企业");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (recruitImages==null||"".equals(recruitImages)){
                    if (recruitMoney==null||"".equals(recruitMoney)||Double.parseDouble(recruitMoney)<=0){
                        resv5="0";
                        addFriendInDetail(qiyeId);
                    }else {
                        resv5 = recruitMoney;
                        String fufei=null;
                        fufei = "付费";
                        startActivity(new Intent(QiYeDetailsActivity.this,QianDingHetongActivity.class).putExtra("biaoshi","yingpin").putExtra("image",recruitImages).putExtra("feiyong",recruitMoney).putExtra("body",recruitBody)
                                .putExtra("qiyeId",qiyeId).putExtra("biaoshi2","yonghu").putExtra("managerId",managerId).putExtra("fufei",fufei).putExtra("companyName",companyName).putExtra("comAddress",comAddress));
                    }
                }else {
                    String fufei=null;
                    if (recruitMoney==null||"".equals(recruitMoney)||Double.parseDouble(recruitMoney)==0){
                        fufei = "无付费";
                    }else {
                        fufei = "付费";
                    }
                    startActivity(new Intent(QiYeDetailsActivity.this,QianDingHetongActivity.class).putExtra("biaoshi","yingpin").putExtra("image",recruitImages).putExtra("feiyong",recruitMoney).putExtra("body",recruitBody)
                            .putExtra("qiyeId",qiyeId).putExtra("biaoshi2","yonghu").putExtra("managerId",managerId).putExtra("fufei",fufei).putExtra("companyName",companyName).putExtra("comAddress",comAddress));
                }
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (joinImages==null||"".equals(joinImages)){
                    if (joinMoney==null||"".equals(joinMoney)||Double.parseDouble(joinMoney)<=0){
                        resv5="0";
                        addFriendInDetail(qiyeId);
                    }else {
                        resv5 = joinMoney;
                        String fufei=null;
                        fufei = "付费";
                        startActivity(new Intent(QiYeDetailsActivity.this,QianDingHetongActivity.class).putExtra("biaoshi","jiameng").putExtra("image",joinImages).putExtra("feiyong",joinMoney).putExtra("body",joinBody)
                                .putExtra("qiyeId",qiyeId).putExtra("biaoshi2","yonghu").putExtra("managerId",managerId).putExtra("fufei",fufei).putExtra("companyName",companyName).putExtra("comAddress",comAddress));
                    }
                }else {
                    String fufei=null;
                    if (joinMoney==null||"".equals(joinMoney)||Double.parseDouble(joinMoney)==0){
                        fufei = "无付费";
                    }else {
                        fufei = "付费";
                    }
                    startActivity(new Intent(QiYeDetailsActivity.this,QianDingHetongActivity.class).putExtra("biaoshi","jiameng").putExtra("image",joinImages).putExtra("feiyong",joinMoney).putExtra("body",joinBody)
                            .putExtra("qiyeId",qiyeId).putExtra("biaoshi2","yonghu").putExtra("managerId",managerId).putExtra("fufei",fufei).putExtra("companyName",companyName).putExtra("comAddress",comAddress));
                }
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showDialog() {
        LayoutInflater inflaterDl = LayoutInflater.from(QiYeDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(QiYeDetailsActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        View v2 = dialog.findViewById(R.id.v2);
        v2.setVisibility(View.GONE);
        re_item2.setVisibility(View.GONE);
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDetail = "01";
                addToDingdan(wodezhanghao,qiyeId,dType,typeDetail);
                dialog.dismiss();
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDetail = "02";
                addToDingdan(wodezhanghao,qiyeId,dType,typeDetail);
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

    private void addToDingdan(final String wodezhanghao, final String hxid, final String zy1 ,final String typeDetail) {
        String zyType,orderBody;
        if (hxid.equals(wodezhanghao)) {
            Toast.makeText(QiYeDetailsActivity.this, "不能给自己企业下单！", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((hxid+"1").equals(zy1)) {
            zyType = resv31;
            orderBody = ZY1;
        }else if ((hxid+"2").equals(zy1)){
            zyType = resv32;
            orderBody = ZY2;
        }else if ((hxid+"3").equals(zy1)){
            zyType = resv33;
            orderBody = ZY3;
        }else {
            zyType = resv34;
            orderBody = ZY4;
        }
        if ("01".equals(zyType)) {
            Intent intent = new Intent(QiYeDetailsActivity.this, UOrderDetailActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("hxid", hxid);
            intent.putExtra("companyName", companyName);
            intent.putExtra("companyAdress", comAddress);
            intent.putExtra("managerId", managerId);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", orderBody);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "02");
            startActivity(intent);
        }else if ("02".equals(zyType)){
            Intent intent = new Intent(QiYeDetailsActivity.this, UOrderDetailTwoActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("distance", "");
            intent.putExtra("hxid", hxid);
            intent.putExtra("companyName", companyName);
            intent.putExtra("companyAdress", comAddress);
            intent.putExtra("managerId", managerId);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", orderBody);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "02");
            startActivity(intent);
        }else if ("03".equals(zyType)){
            Intent intent = new Intent(QiYeDetailsActivity.this, UOrderDetailThreeActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("hxid", hxid);
            intent.putExtra("companyName", companyName);
            intent.putExtra("companyAdress", comAddress);
            intent.putExtra("managerId", managerId);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", orderBody);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "02");
            startActivity(intent);
        }else if ("04".equals(zyType)){
            Intent intent = new Intent(QiYeDetailsActivity.this, UOrderDetailFourActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("distance", "");
            intent.putExtra("hxid", hxid);
            intent.putExtra("companyName", companyName);
            intent.putExtra("companyAdress", comAddress);
            intent.putExtra("managerId", managerId);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", orderBody);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "02");
            startActivity(intent);
        }else if ("05".equals(zyType)){
            Intent intent = new Intent(QiYeDetailsActivity.this, UOrderDetailFiveActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("distance", "");
            intent.putExtra("hxid", hxid);
            intent.putExtra("companyName", companyName);
            intent.putExtra("companyAdress", comAddress);
            intent.putExtra("managerId", managerId);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", orderBody);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "02");
            startActivity(intent);
        }
    }

    private void setListener() {
        //资料是自己
        if (currentQiye!=null&&!"".equals(currentQiye)){
            btnAdd.setEnabled(false);
            btnAdd.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }else {
            btnAdd.setVisibility(View.VISIBLE);
        }
    }

    public void doCl(View v) {
        String zyType;
        switch (v.getId()){
            case R.id.rl1:
                zyType = resv31;
                Intent intent = new Intent(QiYeDetailsActivity.this,ZYDetailActivity.class);
                intent.putExtra("distance","001");
                intent.putExtra("zhuanye","01");
                intent.putExtra("zyType",zyType);
                intent.putExtra("comName",name);
                intent.putExtra("fxUpName", fxUpName);
                intent.putExtra("decribe",decribe1);
                intent.putExtra("remark",remark1);
                intent.putExtra("image",image1);
                intent.putExtra("create",create1);
                intent.putExtra("body",ZY1);
                intent.putExtra("margen",margen1);
                intent.putExtra("pass",pass);
                intent.putExtra("hxid",qiyeId);
                intent.putExtra("upId",upId1);
                intent.putExtra("liulancishu",liulancishu1);
                startActivity(intent);
                break;
            case R.id.rl2:
                zyType = resv32;
                Intent intent2 = new Intent(QiYeDetailsActivity.this,ZYDetailActivity.class);
                intent2.putExtra("distance","001");
                intent2.putExtra("zhuanye","02");
                intent2.putExtra("zyType",zyType);
                intent2.putExtra("comName",name);
                intent2.putExtra("fxUpName", fxUpName);
                intent2.putExtra("decribe",decribe2);
                intent2.putExtra("remark",remark2);
                intent2.putExtra("image",image2);
                intent2.putExtra("create",create2);
                intent2.putExtra("body",ZY2);
                intent2.putExtra("margen",margen2);
                intent2.putExtra("pass",pass);
                intent2.putExtra("hxid",qiyeId);
                intent2.putExtra("upId",upId2);
                intent2.putExtra("liulancishu",liulancishu2);
                startActivity(intent2);
                break;
            case R.id.rl3:
                zyType = resv33;
                Intent intent3 = new Intent(QiYeDetailsActivity.this,ZYDetailActivity.class);
                intent3.putExtra("distance","001");
                intent3.putExtra("zhuanye","03");
                intent3.putExtra("zyType",zyType);
                intent3.putExtra("comName",name);
                intent3.putExtra("fxUpName", fxUpName);
                intent3.putExtra("decribe",decribe3);
                intent3.putExtra("remark",remark3);
                intent3.putExtra("image",image3);
                intent3.putExtra("create",create3);
                intent3.putExtra("body",ZY3);
                intent3.putExtra("margen",margen3);
                intent3.putExtra("pass",pass);
                intent3.putExtra("hxid",qiyeId);
                intent3.putExtra("upId",upId3);
                intent3.putExtra("liulancishu",liulancishu3);
                startActivity(intent3);
                break;
            case R.id.rl4:
                zyType = resv34;
                Intent intent4 = new Intent(QiYeDetailsActivity.this,ZYDetailActivity.class);
                intent4.putExtra("distance","001");
                intent4.putExtra("zhuanye","04");
                intent4.putExtra("zyType",zyType);
                intent4.putExtra("comName",name);
                intent4.putExtra("fxUpName", fxUpName);
                intent4.putExtra("decribe",decribe4);
                intent4.putExtra("remark",remark4);
                intent4.putExtra("image",image4);
                intent4.putExtra("create",create4);
                intent4.putExtra("body",ZY4);
                intent4.putExtra("margen",margen4);
                intent4.putExtra("pass",pass);
                intent4.putExtra("hxid",qiyeId);
                intent4.putExtra("upId",upId4);
                intent4.putExtra("liulancishu",liulancishu4);
                startActivity(intent4);
                break;
        }
    }

    @Override
    public void updateCurrentPrice(Object success) {
        yuE = DemoApplication.getInstance().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
    }
    class ImageListener implements View.OnClickListener {
        String[] images;
        int page;

        public ImageListener(String[] images, int page) {
            this.images = images;
            this.page = page;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), BigImageActivity.class);
            intent.putExtra("images", images);
            intent.putExtra("page", page);
            intent.putExtra("biaoshi","14");
            startActivity(intent);
        }
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }
    private String getNowTime2() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    @Override
    public void updateQiyeInfo(QiYeInfo user2) {
        shareType = user2.getShareType();
        hbYaoqiu = user2.getSingleShare();
        redInterval = user2.getRedInterval();
        personalDtails = user2.getPersonalDtails();
        friendsNumber = user2.getFriendsNumber();
        shareRed = user2.getShareRed();
        comTell = user2.getManagerId();
        managerId = user2.getManagerId();
        String mem = user2.getMemberNum();
        resv31 = TextUtils.isEmpty(user2.getZy1resv3())?"01":user2.getZy1resv3();
        resv32 = TextUtils.isEmpty(user2.getZy2resv3())?"01":user2.getZy2resv3();
        resv33 = TextUtils.isEmpty(user2.getZy3resv3())?"01":user2.getZy3resv3();
        resv34 = TextUtils.isEmpty(user2.getZy4resv3())?"01":user2.getZy4resv3();
        decribe1 = TextUtils.isEmpty(user2.getUpDescribe1())?"":user2.getUpDescribe1();
        decribe2 = TextUtils.isEmpty(user2.getUpDescribe2())?"":user2.getUpDescribe2();
        decribe3 = TextUtils.isEmpty(user2.getUpDescribe3())?"":user2.getUpDescribe3();
        decribe4 = TextUtils.isEmpty(user2.getUpDescribe4())?"":user2.getUpDescribe4();
        margen1 = TextUtils.isEmpty(user2.getMargin1())?"0":user2.getMargin1();
        margen2 = TextUtils.isEmpty(user2.getMargin2())?"0":user2.getMargin2();
        margen3 = TextUtils.isEmpty(user2.getMargin3())?"0":user2.getMargin3();
        margen4 = TextUtils.isEmpty(user2.getMargin4())?"0":user2.getMargin4();
        liulancishu1 = TextUtils.isEmpty(user2.getZy1resv1())?"0":user2.getZy1resv1();
        liulancishu2 = TextUtils.isEmpty(user2.getZy2resv1())?"0":user2.getZy2resv1();
        liulancishu3 = TextUtils.isEmpty(user2.getZy3resv1())?"0":user2.getZy3resv1();
        liulancishu4 = TextUtils.isEmpty(user2.getZy4resv1())?"0":user2.getZy4resv1();
        remark1 = TextUtils.isEmpty(user2.getRemark1())?"":user2.getRemark1();
        remark2 = TextUtils.isEmpty(user2.getRemark2())?"":user2.getRemark2();
        remark3 = TextUtils.isEmpty(user2.getRemark3())?"":user2.getRemark3();
        remark4 = TextUtils.isEmpty(user2.getRemark4())?"":user2.getRemark4();
        image1 = TextUtils.isEmpty(user2.getZyImage1())?"":user2.getZyImage1();
        image2 = TextUtils.isEmpty(user2.getZyImage2())?"":user2.getZyImage2();
        image3 = TextUtils.isEmpty(user2.getZyImage3())?"":user2.getZyImage3();
        image4 = TextUtils.isEmpty(user2.getZyImage4())?"":user2.getZyImage4();
        create1 = TextUtils.isEmpty(user2.getCreateTime1())?"":user2.getCreateTime1();
        create2 = TextUtils.isEmpty(user2.getCreateTime2())?"":user2.getCreateTime2();
        create3 = TextUtils.isEmpty(user2.getCreateTime3())?"":user2.getCreateTime3();
        create4 = TextUtils.isEmpty(user2.getCreateTime4())?"":user2.getCreateTime4();
        wodezhanghao=DemoHelper.getInstance().getCurrentUsernName();
        joinMoney = TextUtils.isEmpty(user2.getJoinMoney())?"":user2.getJoinMoney();
        joinImages = TextUtils.isEmpty(user2.getJoinImages())?"":user2.getJoinImages();
        joinBody = TextUtils.isEmpty(user2.getJoinBody())?"":user2.getJoinBody();
        recruitMoney = TextUtils.isEmpty(user2.getRecruitMoney())?"":user2.getRecruitMoney();
        recruitImages = TextUtils.isEmpty(user2.getRecruitImages())?"":user2.getRecruitImages();
        recruitBody = TextUtils.isEmpty(user2.getRecruitBody())?"":user2.getRecruitBody();
        upId1 = user2.getUpId1();
        upId2 = user2.getUpId2();
        upId3 = user2.getUpId3();
        upId4 = user2.getUpId4();
        name = user2.getCompanyName();
        faren = user2.getResv3();
        sign = user2.getComSignature();
        CompanyAddress = user2.getComAddress();
        String comTell = user2.getManagerId();
        imageStr = TextUtils.isEmpty(user2.getComImage())?"":user2.getComImage();
        ZY1= TextUtils.isEmpty(user2.getUpName1())?"未编辑专业":user2.getUpName1();
        ZY2= TextUtils.isEmpty(user2.getUpName2())?"未编辑专业":user2.getUpName2();
        ZY3= TextUtils.isEmpty(user2.getUpName3())?"未编辑专业":user2.getUpName3();
        ZY4= TextUtils.isEmpty(user2.getUpName4())?"未编辑专业":user2.getUpName4();
        if (shareType!=null&&!"".equals(shareType)){
            String [] count;
            count = shareType.split("\\|");
            if (count.length>0) {
                fxPingTai = count[0];
            }
            if (count.length>1){
                fxLeiXing = count[1];
            }
        }
        if (managerId.equals(DemoHelper.getInstance().getCurrentUsernName())){
            isQunzhu = "01";
        }
        Log.e("qiyeDetail,ma",managerId);
        try {
            companyName = URLDecoder.decode(name, "UTF-8");
            comSignature = URLDecoder.decode(sign, "UTF-8");
            comAddress = URLDecoder.decode(CompanyAddress, "UTF-8");
            farenName = URLDecoder.decode(faren, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (personalDtails==null||"".equals(personalDtails)||"NULL".equals(personalDtails)){
            personalDtails = "0";
        }
        tv_liulan.setText(personalDtails+"次");
        if ("".equals(mem)||mem==null){
            mem = "1";
        }
        String [] num;
        if (friendsNumber!=null&&!"0".equals(friendsNumber)) {
            num = friendsNumber.split("\\|");
            String onedown = num[0];
            if (shareRed!=null&&!"".equals(shareRed)&&!shareRed.equalsIgnoreCase("null")&&Double.parseDouble(shareRed)>0&&Double.parseDouble(onedown)>0){
                exShareRed="有";
                iv_hb.setVisibility(View.VISIBLE);
                btn_fenxiang.setVisibility(View.INVISIBLE);
            }else {
                exShareRed="无";
                iv_hb.setVisibility(View.INVISIBLE);
                btn_fenxiang.setVisibility(View.VISIBLE);
            }
        }else {
            exShareRed="无";
            iv_hb.setVisibility(View.INVISIBLE);
            btn_fenxiang.setVisibility(View.VISIBLE);
        }
        if ("00".equals(biaoshi)){
            btn_fenxiang.setVisibility(View.INVISIBLE);
            iv_hb.setVisibility(View.INVISIBLE);
            tv_my_paidan.setVisibility(View.VISIBLE);
            tv_my_paidan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog1();
                }
            });
        }
        tvqiye_name.setText(companyName);
        tvqiye_renshu.setText("("+mem+")");
        qiye_faren.setText(farenName);
        tvSign.setText("企业简介："+comSignature);
        tv_qiye_phone.setText(comTell);
        tv_company_name.setText(companyName);
        tvCompanyAddress.setText(comAddress);
        String cuTime = getNowTime2();
        if (!create1.equals("")&&create1.length()>8){
            String create = create1.substring(0,4)+"-"+create1.substring(4,6)+"-"+create1.substring(6,8);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date_start = null;
            Date date_end = null;
            try {
                date_end = sdf.parse(cuTime);
                date_start = sdf.parse(create);
                int day1 = getGapCount(date_start,date_end);
                tv_chuangjian1.setText(day1+"天");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!create2.equals("")&&create2.length()>8){
            String create = create2.substring(0,4)+"-"+create2.substring(4,6)+"-"+create2.substring(6,8);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date_start = null;
            Date date_end = null;
            try {
                date_start = sdf.parse(create);
                date_end = sdf.parse(cuTime);
                int day1 = getGapCount(date_start,date_end);
                tv_chuangjian2.setText(day1+"天");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!create3.equals("")&&create3.length()>8){
            String create = create3.substring(0,4)+"-"+create3.substring(4,6)+"-"+create3.substring(6,8);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date_start = null;
            Date date_end = null;
            try {
                date_start = sdf.parse(create);
                date_end = sdf.parse(cuTime);
                int day1 = getGapCount(date_start,date_end);
                tv_chuangjian3.setText(day1+"天");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!create4.equals("")&&create4.length()>8){
            String create = create4.substring(0,4)+"-"+create4.substring(4,6)+"-"+create4.substring(6,8);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date_start = null;
            Date date_end = null;
            try {
                date_start = sdf.parse(create);
                date_end = sdf.parse(cuTime);
                int day1 = getGapCount(date_start,date_end);
                tv_chuangjian4.setText(day1+"天");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!imageStr.equals("")) {
            String[] images = imageStr.split("\\|");
            int imNumb = images.length;
            ivAvatar.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[0],ivAvatar, DemoApplication.mOptions2);
            ivAvatar.setOnClickListener(new ImageListener(images, 0));
            // 六张图的时间情况比较特殊
            if (imNumb == 6) {
                ivAvatar2.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[1],ivAvatar2, DemoApplication.mOptions2);
                ivAvatar2.setOnClickListener(new ImageListener(images,1));
                ivAvatar3.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[2],ivAvatar3, DemoApplication.mOptions2);
                ivAvatar3.setOnClickListener(new ImageListener(images,2));
                ivAvatar4.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[3],ivAvatar4, DemoApplication.mOptions2);
                ivAvatar4.setOnClickListener(new ImageListener(images,3));
                ivAvatar6.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[4],ivAvatar6, DemoApplication.mOptions2);
                ivAvatar6.setOnClickListener(new ImageListener(images,4));
                ivAvatar7.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[5],ivAvatar7, DemoApplication.mOptions2);
                ivAvatar7.setOnClickListener(new ImageListener(images,5));
            } else {
                if (imNumb > 1) {
                    ivAvatar2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[1],ivAvatar2, DemoApplication.mOptions2);
                    ivAvatar2.setOnClickListener(new ImageListener(images, 1));
                    if (imNumb > 2) {
                        ivAvatar3.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[2],ivAvatar3, DemoApplication.mOptions2);
                        ivAvatar3.setOnClickListener(new ImageListener(images, 2));
                        if (imNumb > 3) {
                            ivAvatar4.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[3],ivAvatar4, DemoApplication.mOptions2);
                            ivAvatar4.setOnClickListener(new ImageListener(images, 3));
                            if (imNumb > 4) {
                                ivAvatar5.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[4],ivAvatar5, DemoApplication.mOptions2);
                                ivAvatar5.setOnClickListener(new ImageListener(images, 4));
                                if (imNumb > 5) {
                                    ivAvatar6.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[5],ivAvatar6, DemoApplication.mOptions2);
                                    ivAvatar6.setOnClickListener(new ImageListener(images, 5));
                                    if (imNumb > 6) {
                                        ivAvatar7.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[6],ivAvatar7, DemoApplication.mOptions2);
                                        ivAvatar7.setOnClickListener(new ImageListener(images, 6));
                                        if (imNumb > 7) {
                                            ivAvatar8.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[7],ivAvatar8, DemoApplication.mOptions2);
                                            ivAvatar8.setOnClickListener(new ImageListener(images, 7));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        String strremark1 = "0";
        String strremark2 = "0";
        String strremark3 = "0";
        String strremark4 = "0";
        if (!remark1.equals("")&&remark1!=null) {
            String[] remark = remark1.split(",");
            strremark1 = remark[0];
        }
        if (!remark2.equals("")&&remark2!=null) {
            String[] remark = remark2.split(",");
            strremark2 = remark[0];
        }
        if (!remark3.equals("")&&remark3!=null) {
            String[] remark = remark3.split(",");
            strremark3 = remark[0];
        }
        if (!remark4.equals("")&&remark4!=null) {
            String[] remark = remark4.split(",");
            strremark4 = remark[0];
        }
        zy1_bao.setText(margen1+"元");
        zy2_bao.setText(margen2+"元");
        zy3_bao.setText(margen3+"元");
        zy4_bao.setText(margen4+"元");
        zy1_jiedancishu.setText(strremark1+"次");
        zy2_jiedancishu.setText(strremark2+"次");
        zy3_jiedancishu.setText(strremark3+"次");
        zy4_jiedancishu.setText(strremark4+"次");
        tvZY1.setText(ZY1);
        tvZY2.setText(ZY2);
        tvZY3.setText(ZY3);
        tvZY4.setText(ZY4);
        if ("未编辑专业".equals(ZY1)){
            rl1.setEnabled(false);
            btn_comit1.setEnabled(false);
            btn_comit1.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }else {
            if (fxUpName==null){
                fxUpName = ZY1+"，";
            }
        }
        if ("未编辑专业".equals(ZY2)){
            rl2.setEnabled(false);
            btn_comit2.setEnabled(false);
            btn_comit2.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }else {
            if (fxUpName==null){
                fxUpName = ZY2+"，";
            }else {
                fxUpName += ZY2+"，";
            }
        }
        if ("未编辑专业".equals(ZY3)){
            rl3.setEnabled(false);
            btn_comit3.setEnabled(false);
            btn_comit3.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }else {
            if (fxUpName==null){
                fxUpName = ZY3+"，";
            }else {
                fxUpName += ZY3+"，";
            }
        }
        if ("未编辑专业".equals(ZY4)){
            rl4.setEnabled(false);
            btn_comit4.setEnabled(false);
            btn_comit4.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }else {
            if (fxUpName==null){
                fxUpName = ZY4+"，";
            }else {
                fxUpName += ZY4+"，";
            }
        }
    }
    private int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    private boolean isAllowPhone() {
        LocalUserUtil localUserUtil = LocalUserUtil.getInstance();
        if (TextUtils.isEmpty(localUserUtil.getString("phone_count"))) {
            localUserUtil.setString("phone_count","1");
            String time = String.valueOf(System.currentTimeMillis());
            localUserUtil.setString("phone_time",time);
            return true;
        } else {
            int count = Integer.valueOf(localUserUtil.getString("phone_count"));
            if (System.currentTimeMillis() - Long.valueOf(localUserUtil.getString("phone_time")) < 1000 * 60 * 5) {
                if (count >= 10) {
                    return false;
                } else {
                    localUserUtil.setString("phone_count", String.valueOf(count+1));
                    return true;
                }
            } else {
                if (System.currentTimeMillis() - Long.valueOf(localUserUtil.getString("phone_time")) > 1000 * 60 * 30) {
                    //大于半小时
                    localUserUtil.setString("phone_count", "1");
                    localUserUtil.setString("phone_time", String.valueOf(System.currentTimeMillis()));
                    return true;
                } else {
                    if (count >= 10) {
                        return false;
                    } else {
                        localUserUtil.setString("phone_count", String.valueOf(1));
                        localUserUtil.setString("phone_time", String.valueOf(System.currentTimeMillis()));
                        return true;
                    }
                }

            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showLoading() {
    }
    @Override
    public void hideLoading() {
    }
    @Override
    public void showError() {
    }
}
