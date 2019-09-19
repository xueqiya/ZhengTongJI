package com.sangu.apptongji.main.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.fanxin.easeui.EaseConstant;
import com.fanxin.easeui.domain.EaseUser;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.address.AddressListActivity;
import com.sangu.apptongji.main.alluser.entity.LiuLanDetail;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.entity.ZhuFaDetail;
import com.sangu.apptongji.main.alluser.order.avtivity.CurrentDynamicActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.FriendActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFiveActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFourActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailThreeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailTwoActivity;
import com.sangu.apptongji.main.alluser.presenter.ILiulanListPresenter;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.IZhuFaListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.LiulanListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.ZhuFaListPresenter;
import com.sangu.apptongji.main.alluser.view.ILiulanListView;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.alluser.view.IZhuanFaListView;
import com.sangu.apptongji.main.moments.BigImageActivity;
import com.sangu.apptongji.main.moments.MomentsPublishActivity;
import com.sangu.apptongji.main.qiye.OfflineOrderActivity;
import com.sangu.apptongji.main.qiye.QiYeDetailsActivity;
import com.sangu.apptongji.main.service.ContactsService;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
 * Created by lishao on 2016/7/6.
 * QQ:84543217
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class UserDetailsActivity extends BaseActivity implements IPriceView, IUAZView, ILiulanListView, IZhuanFaListView {
    /**
     * 用户详情页接收两种传值
     * 1：用户完整资料的JSON字符串-userInfo-这种情况如果是好友进行刷新
     * 2：只传用户的hxid，这种情况直接从网络取数据显示-如果是好友，刷新资料
     * 3：有分享名片红包的加载网络数据
     */
    private String hxid, hxidx, hxidx2, sign, resv1, resv2, sex, Company, CompanyAddress, home, imageStr, Zhiye, School, ZY1, ZY2, ZY3, ZY4, GZ1, GZ2, GZ3, GZ4, GZ5, GZ6, fxUpName;
    private String wodezhanghao, upId1, upId2, upId3, upId4, resv31, resv32, resv33, resv34, dType, typeDetail, distance, biaoshi;
    private TextView tvCompany = null;
    private TextView tvSign = null;
    private TextView tvJuli = null;
    private TextView tvCompanyAddress = null;
    private TextView tvhome = null;
    private TextView tvZhiye = null, tv1, tv2, tv3, tv4;
    private TextView tvSchool = null, tvTitleA = null;
    private TextView tvZY1 = null, tvZY2 = null, tvZY3 = null, tvZY4 = null, tv_name = null, tv_zy1_tupian, tv_zy2_tupian, tv_zy3_tupian, tv_zy4_tupian;
    private TextView tvGZ1 = null, tvGZ2 = null, tvGZ3 = null, tvGZ4 = null, tvGZ5 = null, tvGZ6 = null, tv_qiye;
    private TextView zy1_bao, zy2_bao = null, zy3_bao, zy4_bao = null, tv_chuangjian1 = null, tv_chuangjian2 = null, tv_chuangjian3 = null, tv_chuangjian4 = null;
    private TextView zy1_jiedancishu = null, zy2_jiedancishu = null, zy3_jiedancishu = null, zy4_jiedancishu = null, tv_liulan_cishu = null;
    private Button btnMsg = null, btn_fenxiang = null, btn_paidan = null;
    private Button btnAdd = null;
    private Button btn_comit1 = null, btn_comit2 = null, btn_comit3 = null, btn_comit4 = null, btn_duanxin = null, btn_dianhua = null;
    private ImageView ivsex = null;
    private ImageView iv_hb = null;
    private ImageView ivAvatar = null, iv_v1;
    private ImageView ivAvatar2 = null, iv_v2;
    private ImageView ivAvatar3 = null, iv_v3;
    private ImageView ivAvatar4 = null, iv_v4;
    private ImageView ivAvatar5 = null, iv_v5;
    private ImageView ivAvatar6 = null, iv_v6;
    private ImageView ivAvatar7 = null, iv_v7;
    private ImageView ivAvatar8 = null, iv_v8;
    private String filesUrl, file1Url, file2Url, file3Url, file4Url, file5Url, file6Url, file7Url, file8Url;
    private IPricePresenter pricePresenter = null;
    private IUAZPresenter uazPresenter = null;
    private ILiulanListPresenter liuLanPresenter = null;
    private IZhuFaListPresenter zhuFaPresenter = null;
    private String pass, resv5, name, personalDtails, exShareRed;
    private RelativeLayout rl1 = null, rl2 = null, rl3 = null, rl4 = null, rl_v8, rl_v7, rl_v6, rl_v5, rl_v4, rl_v3, rl_v2, rl_v1, re_my_ziliao;
    private LinearLayout ll;
    private String decribe1, decribe2, decribe3, decribe4, margen1, margen2, margen3, margen4;
    private String remark1, remark2, remark3, remark4, image1, image2, image3, image4, create1, create2, create3, create4, redInterval;
    private String liulancishu1, liulancishu2, liulancishu3, liulancishu4, xiaoliang, createTime, dynamicSeq, shareRed, singleShare, friendsNumber, hbYaoqiu;
    private String onedown = "0";
    private boolean isManager;
    private boolean isLiulanFinish = false;
    private boolean isZhuFaFinish = false;
    private boolean isBeBlacked = false;
    private String shareType = null, fxPingTai = null, fxLeiXing = null;
    private int qqHomePage = 0, weixinHomePage = 0, weiboHomePage = 0, homePage = 0, qqAll = 0, weixinAll = 0, weiboAll = 0;
    private int liuLanSize = 0;
    private int llhomePage = 0;
    private int zhuFaSize = 0;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isLiulanFinish && isZhuFaFinish) {
                tv_liulan_cishu.setText(String.valueOf(liuLanSize + zhuFaSize) + "次");
            }
        }
    };
    static final String EXTRA_STARTING_ALBUM_POSITION = "extra_starting_item_position";
    static final String EXTRA_CURRENT_ALBUM_POSITION = "extra_current_item_position";
    private Bundle mTmpReenterState;

    //"我转发他的主页名片，获得了一个红包，"+fxUpName+"不限行业接派单，全民分享赚红包"
    //"我转发他的主页名片，"+fxUpName+"不限行业接派单，全民分享赚红包"
    @Override
    public void onActivityReenter(int requestCode, Intent data) {
        super.onActivityReenter(requestCode, data);
        mTmpReenterState = new Bundle(data.getExtras());
        postponeEnterTransition();
        startPostponedEnterTransition();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fx_activity_userinfo);
//        setExitSharedElementCallback(mCallback);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        hxid = this.getIntent().getStringExtra(FXConstant.JSON_KEY_HXID);
        biaoshi = this.getIntent().hasExtra("biaoshi") ? getIntent().getStringExtra("biaoshi") : null;
        xiaoliang = this.getIntent().hasExtra("xiaoliang") ? "01" : "00";
        createTime = this.getIntent().hasExtra("createTime") ? this.getIntent().getStringExtra("createTime") : "";
        dynamicSeq = this.getIntent().hasExtra("dynamicSeq") ? this.getIntent().getStringExtra("dynamicSeq") : "";
        Log.e("userdetail", hxid);
        initView();
        isManager = false;
        String resv6 = DemoApplication.getInstance().getCurrentResv6();
        String uNation = DemoApplication.getInstance().getCurrentQiYeRemark();
        if ("1".equals(uNation) && "00".equals(resv6)) {
            isManager = true;
        }
        if (!hxid.equals(DemoHelper.getInstance().getCurrentUsernName())) {
            updateLiulancishu();
            gethxidPBList();
        }
        uazPresenter = new UAZPresenter(this, this);
        liuLanPresenter = new LiulanListPresenter(this, this);
        zhuFaPresenter = new ZhuFaListPresenter(this, this);
        uazPresenter.loadThisDetail(hxid);
        pricePresenter = new PricePresenter(this, this);
        pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
        liuLanPresenter.loadLiuLanList("1", hxid, "homePage");
        zhuFaPresenter.loadZhuFaList("1", hxid, "homePage");
        if (DemoHelper.getInstance().isLoggedIn(this)) {
            setListener();
        } else {
            btn_comit1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserPermissionUtil.getUserPermission(UserDetailsActivity.this, hxid, "8", new UserPermissionUtil.UserPermissionListener() {
                        @Override
                        public void onAllow() {
                            startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
                        }

                        @Override
                        public void onBan() {
                            Toast.makeText(UserDetailsActivity.this, "该账户已被禁止接单", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
            btn_comit2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserPermissionUtil.getUserPermission(UserDetailsActivity.this, hxid, "8", new UserPermissionUtil.UserPermissionListener() {
                        @Override
                        public void onAllow() {
                            startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
                        }

                        @Override
                        public void onBan() {
                            Toast.makeText(UserDetailsActivity.this, "该账户已被禁止接单", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
            btn_comit3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserPermissionUtil.getUserPermission(UserDetailsActivity.this, hxid, "8", new UserPermissionUtil.UserPermissionListener() {
                        @Override
                        public void onAllow() {
                            startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
                        }

                        @Override
                        public void onBan() {
                            Toast.makeText(UserDetailsActivity.this, "该账户已被禁止接单", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
            btn_comit4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserPermissionUtil.getUserPermission(UserDetailsActivity.this, hxid, "8", new UserPermissionUtil.UserPermissionListener() {
                        @Override
                        public void onAllow() {
                            startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
                        }

                        @Override
                        public void onBan() {
                            Toast.makeText(UserDetailsActivity.this, "该账户已被禁止接单", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
            btnMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
                }
            });
            btn_duanxin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
                }
            });
            btn_dianhua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
                }
            });
            btn_fenxiang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
                }
            });
            btn_paidan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
                }
            });
            iv_hb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserDetailsActivity.this, LoginActivity.class));
                }
            });
        }


    }

    private void gethxidPBList() {
        String url = FXConstant.URL_BLACKLIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonArray = object.getJSONArray("list");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String shield_id = jsonObject.getString("shield_id");
                            if (shield_id.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                                isBeBlacked = true;
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("-----失败----", "" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", hxid);
                return param;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void updateLiulancishu() {
        String url = FXConstant.URL_ADD_USERCISHU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Userdetailac,add", "增加浏览次数成功" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError != null) {
                    Log.e("Userdetailac,add", "增加浏览次数错误");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("uLoginId", hxid);
                param.put("homePage", "1");
                param.put("v_id", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void initView() {
        ll = (LinearLayout) findViewById(R.id.ll);
        tv_liulan_cishu = (TextView) findViewById(R.id.tv_liulan_cishu);
        tv_chuangjian1 = (TextView) findViewById(R.id.zy1_chjshj);
        tv_chuangjian2 = (TextView) findViewById(R.id.zy2_chjshj);
        tv_chuangjian3 = (TextView) findViewById(R.id.zy3_chjshj);
        tv_chuangjian4 = (TextView) findViewById(R.id.zy4_chjshj);
        tvTitleA = (TextView) findViewById(R.id.tv_titl);
        btnMsg = (Button) this.findViewById(R.id.btn_msg);
        btn_paidan = (Button) this.findViewById(R.id.btn_paidan);
        btn_fenxiang = (Button) this.findViewById(R.id.btn_fenxiang);
        btnAdd = (Button) this.findViewById(R.id.btn_add);
        btn_duanxin = (Button) this.findViewById(R.id.btn_duanxin);
        btn_dianhua = (Button) this.findViewById(R.id.btn_dianhua);
        zy1_bao = (TextView) findViewById(R.id.zy1_bao);
        zy2_bao = (TextView) findViewById(R.id.zy2_bao);
        zy3_bao = (TextView) findViewById(R.id.zy3_bao);
        zy4_bao = (TextView) findViewById(R.id.zy4_bao);
        zy1_jiedancishu = (TextView) findViewById(R.id.zy1_jiedancishu);
        zy2_jiedancishu = (TextView) findViewById(R.id.zy2_jiedancishu);
        zy3_jiedancishu = (TextView) findViewById(R.id.zy3_jiedancishu);
        zy4_jiedancishu = (TextView) findViewById(R.id.zy4_jiedancishu);
        rl_v1 = (RelativeLayout) this.findViewById(R.id.rl_v1);
        rl_v2 = (RelativeLayout) this.findViewById(R.id.rl_v2);
        rl_v3 = (RelativeLayout) this.findViewById(R.id.rl_v3);
        rl_v4 = (RelativeLayout) this.findViewById(R.id.rl_v4);
        rl_v5 = (RelativeLayout) this.findViewById(R.id.rl_v5);
        rl_v6 = (RelativeLayout) this.findViewById(R.id.rl_v6);
        rl_v7 = (RelativeLayout) this.findViewById(R.id.rl_v7);
        rl_v8 = (RelativeLayout) this.findViewById(R.id.rl_v8);
        re_my_ziliao = (RelativeLayout) this.findViewById(R.id.re_my_ziliao);
        rl1 = (RelativeLayout) this.findViewById(R.id.rl1);
        rl2 = (RelativeLayout) this.findViewById(R.id.rl2);
        rl3 = (RelativeLayout) this.findViewById(R.id.rl3);
        rl4 = (RelativeLayout) this.findViewById(R.id.rl4);
        iv_v1 = (ImageView) this.findViewById(R.id.iv_v1);
        iv_v2 = (ImageView) this.findViewById(R.id.iv_v2);
        iv_v3 = (ImageView) this.findViewById(R.id.iv_v3);
        iv_v4 = (ImageView) this.findViewById(R.id.iv_v4);
        iv_v5 = (ImageView) this.findViewById(R.id.iv_v5);
        iv_v6 = (ImageView) this.findViewById(R.id.iv_v6);
        iv_v7 = (ImageView) this.findViewById(R.id.iv_v7);
        iv_v8 = (ImageView) this.findViewById(R.id.iv_v8);
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
        ivsex = (ImageView) this.findViewById(R.id.iv_sex);
        iv_hb = (ImageView) this.findViewById(R.id.iv_hb);
        tvCompany = (TextView) findViewById(R.id.tv_company);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tvSign = (TextView) findViewById(R.id.tv_qianming);
        tvJuli = (TextView) findViewById(R.id.tv_juli);
        tvCompanyAddress = (TextView) findViewById(R.id.tv_company_address);
        tvhome = (TextView) findViewById(R.id.tv_hometown);
        tvZhiye = (TextView) findViewById(R.id.tv_zhiye);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tvSchool = (TextView) findViewById(R.id.tv_xuexiao);
        tv_qiye = (TextView) findViewById(R.id.tv_qiye);
        tv_zy1_tupian = (TextView) findViewById(R.id.tv_zy1_tupian);
        tv_zy2_tupian = (TextView) findViewById(R.id.tv_zy2_tupian);
        tv_zy3_tupian = (TextView) findViewById(R.id.tv_zy3_tupian);
        tv_zy4_tupian = (TextView) findViewById(R.id.tv_zy4_tupian);
        tvZY1 = (TextView) findViewById(R.id.tv_zy1);
        tvZY2 = (TextView) findViewById(R.id.tv_zy2);
        tvZY3 = (TextView) findViewById(R.id.tv_zy3);
        tvZY4 = (TextView) findViewById(R.id.tv_zy4);
        tvGZ1 = (TextView) findViewById(R.id.tv_gz1);
        tvGZ2 = (TextView) findViewById(R.id.tv_gz2);
        tvGZ3 = (TextView) findViewById(R.id.tv_gz3);
        tvGZ4 = (TextView) findViewById(R.id.tv_gz4);
        tvGZ5 = (TextView) findViewById(R.id.tv_gz5);
        tvGZ6 = (TextView) findViewById(R.id.tv_gz6);
    }

    private void addFriendInDetail(final String hxid1) {
        final ProgressDialog dialog = new ProgressDialog(UserDetailsActivity.this);
        dialog.setMessage("正在发送请求...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        String url = FXConstant.URL_ADD_FRIEND;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("SUCCESS")) {
                        sendPushMessage(hxid1, 0, null, null, null, null, null);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "发送成功！", Toast.LENGTH_LONG).show();
                    } else if (code.equals("1012")) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "该好友不存在", Toast.LENGTH_SHORT).show();
                    } else if (code.equals("1013")) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "该帐号已经是您的好友！", Toast.LENGTH_SHORT).show();
                    } else if (code.equals("1014")) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "请勿重复发送请求！", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("f_word", hxid1);
                param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void sendPushMessage(final String hxid1,
                                 final int type,
                                 final String weiboAdd,
                                 final String weixinAdd,
                                 final String qqAdd,
                                 final String hxid2,
                                 final String broAddPer) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_SENDPUSHMSG;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (type == 0) {
                    updateBmob(hxid1);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (type == 0) {
                    updateBmob(hxid1);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("u_id", hxid1);
                if (type == 0) {
                    param.put("body", "好友消息");
                    param.put("type", "02");
                    param.put("companyName", "0");
                } else {
                    param.put("body", "名片传播报告");
                    param.put("type", "15");
                    param.put("companyName", "总转发" + zhuFaSize + "次,微博" + weiboAdd + "次,朋友圈" + weixinAdd + "次," +
                            "空间" + qqAdd + "次(增长" + hxid2 + "%),总浏览" + liuLanSize + "次(增长" + broAddPer + "%)");
                }
                param.put("userId", myId);
                param.put("companyId", "0");
                param.put("companyAdress", "0");
                param.put("dynamicSeq", "0");
                param.put("createTime", "0");
                param.put("fristId", "0");
                param.put("dType", "0");
                return param;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void updateBmob(final String hxid1) {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    Log.e("addfriend,s", s);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError != null) {
                    Log.e("addfriend,e", volleyError.toString());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("addFriendCount", "1");
                param.put("userId", hxid1);
                return param;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                showDialog2();
                break;
            case R.id.btn_msg:
                String imgUrl = null;
                if (imageStr != null && !"".equals(imageStr)) {
                    imgUrl = imageStr.split("\\|")[0];
                }
                sendContactTrack("聊天");
                final Intent intent = new Intent(UserDetailsActivity.this, ChatActivity.class);
                intent.putExtra("userId", hxid);
                intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                intent.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                intent.putExtra(EaseConstant.EXTRA_USER_IMG, imgUrl);
                intent.putExtra(EaseConstant.EXTRA_USER_NAME, name);
                intent.putExtra(EaseConstant.EXTRA_USER_SHARERED, exShareRed);
                startActivity(intent);
                startActivity(new Intent(UserDetailsActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, hxid));

                break;
            case R.id.btn_duanxin:


                Intent intent1 = new Intent(UserDetailsActivity.this,SingleSendMessageActivity.class);

                intent1.putExtra("userId",hxid);

                startActivity(intent1);

//                SharedPreferences sp1 = getSharedPreferences("sangu_bddh_time", Context.MODE_PRIVATE);
//                final String time = sp1.getString(hxid, null);
//                LayoutInflater inflater1 = LayoutInflater.from(UserDetailsActivity.this);
//                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
//                final Dialog dialog1 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
//                dialog1.show();
//                dialog1.getWindow().setContentView(layout1);
//                dialog1.setCanceledOnTouchOutside(true);
//                dialog1.setCancelable(true);
//                TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
//                Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
//                final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
//                TextView title = (TextView) layout1.findViewById(R.id.tv_title);
//                title.setText("温馨提示");
//                btnOK1.setText("确定");
//                btnCancel1.setText("取消");
//                if (time == null || Long.parseLong(getNowTime()) - Long.parseLong(time) > 500) {
//                    title_tv1.setText("本次操作使用的是平台短信,不收取任何费用,确认发送么?");
//                } else {
//                    title_tv1.setText("您的操作过于频繁,歇五分钟再发吧！");
//                }
//                btnCancel1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog1.dismiss();
//                    }
//                });
//                btnOK1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog1.dismiss();
//                        if (time == null || Long.parseLong(getNowTime()) - Long.parseLong(time) > 500) {
//                            SharedPreferences mSharedPreferences = getSharedPreferences("sangu_bddh_time", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = mSharedPreferences.edit();
//                            editor.putString(hxid, getNowTime());
//                            editor.commit();
//                            if (!"1000000".equals(hxidx2) && !"2000000".equals(hxidx2) && !"3000000".equals(hxidx2)) {
//                                duanxintongzhi(hxid, "", null, null, null, "", "", "", 0);
//                            } else {
//                                duanxintongzhi("13513895563", hxid, null, null, null, "", "", "", 0);
//                                // TODO: 2017-11-27 检查错误
//                            }
//                            sendContactTrack("短信");
//                        }
//                    }
//                });
                break;
            case R.id.btn_dianhua:

                final LayoutInflater inflater2 = LayoutInflater.from(UserDetailsActivity.this);
                RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                final Dialog dialog2 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                title_tv2.setText("确认拨打电话吗？");
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

                        if (isAllowPhone()) {
                            PermissionUtil permissionUtil = new PermissionUtil(UserDetailsActivity.this);
                            permissionUtil.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                    new PermissionListener() {
                                        @Override
                                        public void onGranted() {
                                            UserPermissionUtil.getUserPermission(UserDetailsActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "2", new UserPermissionUtil.UserPermissionListener() {
                                                @Override
                                                public void onAllow() {
                                                    //发送通话记录
                                                    sendContactTrack("电话");
                                                    //所有权限都已经授权
                                                    if (!"1833710135".equals(hxidx) && !"1000000".equals(hxidx2) && !"2000000".equals(hxidx2) && !"3000000".equals(hxidx2)) {
                                                        Intent intent = new Intent();
                                                        intent.setAction(Intent.ACTION_CALL);
                                                        //url:统一资源定位符
                                                        //uri:统一资源标示符（更广）
                                                        intent.setData(Uri.parse("tel:" + hxid));
                                                        //开启系统拨号器
                                                        startActivity(intent);
                                                    } else {
                                                        Intent intent = new Intent();
                                                        intent.setAction(Intent.ACTION_CALL);
                                                        //url:统一资源定位符
                                                        //uri:统一资源标示符（更广）
                                                        intent.setData(Uri.parse("tel:" + "13513895563"));
                                                        //开启系统拨号器
                                                        startActivity(intent);
                                                    }
                                                }

                                                @Override
                                                public void onBan() {
                                                    ToastUtils.showNOrmalToast(UserDetailsActivity.this.getApplicationContext(), "您的账户已被禁止打电话");

                                                }
                                            });

                                        }

                                        @Override
                                        public void onDenied(List<String> deniedPermission) {
                                            //Toast第一个被拒绝的权限
                                            Toast.makeText(getApplicationContext(), "您拒绝了拨打电话的权限！", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onShouldShowRationale(List<String> deniedPermission) {
                                            //Toast第一个勾选不在提示的权限
                                            Toast.makeText(getApplicationContext(), "您拒绝了拨打电话的权限,请前往设置手动打开！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            LayoutInflater inflater2 = LayoutInflater.from(UserDetailsActivity.this);
                            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog2 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                                    Intent intent2 = new Intent(UserDetailsActivity.this, MomentsPublishActivity.class);
                                    intent2.putExtra("biaoshi", "xuqiu");
                                    startActivity(intent2);
                                    dialog2.dismiss();
                                }
                            });
                        }


                    }
                });
                break;
            case R.id.btn_comit1:
                dType = hxid + "1";
                showDialog();
                break;
            case R.id.btn_comit2:
                dType = hxid + "2";
                showDialog();
                break;
            case R.id.btn_comit3:
                dType = hxid + "3";
                showDialog();
                break;
            case R.id.btn_comit4:
                dType = hxid + "4";
                showDialog();
                break;
            case R.id.btn_fenxiang:
                LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
                dialog.show();
                dialog.getWindow().setContentView(layout);
                dialog.setCanceledOnTouchOutside(true);
                RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                tv_item1.setText("设置VIP");
                tv_item2.setText("发送短信");
                re_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        Intent intent2 = new Intent(UserDetailsActivity.this,SetVipToUserActivity.class);

                        intent2.putExtra("userId",hxid);

                        startActivity(intent2);


//                        ScreenshotUtil.getBitmapByView(UserDetailsActivity.this, findViewById(R.id.ll1), "分享名片红包", null, 6, false,llhomePage,homePage);
//                        startActivity(new Intent(UserDetailsActivity.this, FriendActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid)
//                                .putExtra("biaoshi", "02").putExtra("filePath", Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png"));
                    }
                });
                re_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        Intent intent3 = new Intent(UserDetailsActivity.this,SendMessageActivity.class);

                        intent3.putExtra("userId",hxid);

                        startActivity(intent3);

                     //   saveCurrentImage();
                    }
                });
                re_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.iv_hb:
                if (DemoHelper.getInstance().getCurrentUsernName().equals(hxid)) {
                    Toast.makeText(UserDetailsActivity.this, "不能分享自己的红包！", Toast.LENGTH_SHORT).show();
                    return;
                }

                //查询一下是否被设置了禁止转发红包，必须邀请好友才能解开

                String url = FXConstant.URL_SEARCH_REDAUTH;

                StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.d("chen", "onResponse" + s);

                        try {
//                            org.json.JSONObject object = new org.json.JSONObject(s);
//
//                            String code = object.getString("code");

                            JSONObject object = new JSONObject(s);
                            String code = object.getString("code");

                            if (code==null||"".equals(code)||code.equalsIgnoreCase("null")){
                                //处理null

                                //没有限制  随便转
                                UserPermissionUtil.getUserPermission(UserDetailsActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "8", new UserPermissionUtil.UserPermissionListener() {
                                    @Override
                                    public void onAllow() {
                                        PermissionUtil permissionUtil = new PermissionUtil(UserDetailsActivity.this);
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
                                                        Toast.makeText(UserDetailsActivity.this.getApplicationContext(), "您拒绝了获取定位的权限！无法赚取红包 请手动开启权限", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onShouldShowRationale(List<String> deniedPermission) {
                                                        Toast.makeText(UserDetailsActivity.this.getApplicationContext(), "您拒绝了获取定位的权限！无法赚取红包 请手动开启权限", Toast.LENGTH_SHORT).show();
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

                            }else {
                                //禁止转了  要求发短信才可以

                                final LayoutInflater inflater1 = LayoutInflater.from(UserDetailsActivity.this);
                                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                                final Dialog dialog1 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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

                                        if (ContextCompat.checkSelfPermission(UserDetailsActivity.this, Manifest.permission.READ_CONTACTS)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(UserDetailsActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
                                        }else {
                                            Intent intent = new Intent(UserDetailsActivity.this,AddressListActivity.class);

                                            intent.putExtra("redAuth","yes");

                                            startActivityForResult(intent,0);
                                        }
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

                        if (volleyError!=null) {
                            Log.e("hongbao", volleyError.getMessage());
                            Log.d("chen", "hongbao" + volleyError.getMessage());
                        }
                        Toast.makeText(UserDetailsActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> param = new HashMap<>();

                        param.put("rid",DemoHelper.getInstance().getCurrentUsernName());

                        return param;
                    }
                };
                MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);

                break;
            case R.id.btn_paidan:
                if (isManager) {
                    startActivity(new Intent(UserDetailsActivity.this, OfflineOrderActivity.class)
                            .putExtra("biaoshi", "00").putExtra("biaoshi2", "0").putExtra("paidanId", hxid).putExtra("paidanName", name).putExtra("companyId", resv5));
                } else {
                    LayoutInflater inflater4 = LayoutInflater.from(UserDetailsActivity.this);
                    RelativeLayout layout4 = (RelativeLayout) inflater4.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog4 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
                    dialog4.show();
                    dialog4.getWindow().setContentView(layout4);
                    dialog4.setCanceledOnTouchOutside(true);
                    dialog4.setCancelable(true);
                    TextView title_tv4 = (TextView) layout4.findViewById(R.id.title_tv);
                    Button btnCancel4 = (Button) layout4.findViewById(R.id.btn_cancel);
                    final Button btnOK4 = (Button) layout4.findViewById(R.id.btn_ok);
                    btnOK4.setText("确定");
                    btnCancel4.setText("取消");
                    title_tv4.setText("只有企业创始人才有权限派单哦！");
                    btnCancel4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog4.dismiss();
                        }
                    });
                    btnOK4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog4.dismiss();
                        }
                    });
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限通过
                Intent intent = new Intent(UserDetailsActivity.this,AddressListActivity.class);
                intent.putExtra("redAuth","yes");
                startActivityForResult(intent,0);

            } else {  //权限拒绝

                Toast.makeText(UserDetailsActivity.this,"您拒绝了访问通讯录权限，请前往设置手动打开权限！",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isAllowPhone() {
        LocalUserUtil localUserUtil = LocalUserUtil.getInstance();
        if (TextUtils.isEmpty(localUserUtil.getString("phone_count"))) {
            localUserUtil.setString("phone_count", "1");
            String time = String.valueOf(System.currentTimeMillis());
            localUserUtil.setString("phone_time", time);
            return true;
        } else {
            int count = Integer.valueOf(localUserUtil.getString("phone_count"));
            if (System.currentTimeMillis() - Long.valueOf(localUserUtil.getString("phone_time")) < 1000 * 60 * 5) {
                if (count >= 10) {
                    return false;
                } else {
                    localUserUtil.setString("phone_count", String.valueOf(count + 1));
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

    private void sendContactTrack(String msg) {
        uazPresenter.sendContactTrack(DemoHelper.getInstance().getCurrentUsernName(), hxid, msg);
        if (msg.equalsIgnoreCase("电话") || msg.equalsIgnoreCase("聊天") ) {
            if (TextUtils.isEmpty(dynamicSeq) || TextUtils.isEmpty(createTime) || TextUtils.isEmpty(hxid)) {
                return;
            }
            String url = FXConstant.URL_UPDATE_INSERT_DYNAMIC_CONTACT;
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.d("chen", "insertDynamicContact onResponse" + s);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("chen", "insertDynamicContact onErrorResponse" + volleyError.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //uId=&dynamicSeq=&createTime=&dis=&responsetime=&ordernum=&status=
                    Map<String, String> param = new HashMap<>();
                    param.put("dynamicSeq", dynamicSeq);
                    param.put("createTime", createTime);
                    param.put("uId", DemoHelper.getInstance().getCurrentUsernName());
                    param.put("contactId", hxid);
                    param.put("type", "00");
                    return param;
                }
            };
            MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
        }
    }

    private void queryhbzgCount() {
        String url = FXConstant.URL_Get_UserInfo + DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject userInfo = object.getJSONObject("userInfo");
                String homePageTimes = userInfo.getString("homePageTimes");
                final String score = userInfo.getString("score");
                final String withdrawals = DemoApplication.getInstance().getCurrentWithdrawals();
                if (homePageTimes == null || "".equals(homePageTimes) || Double.parseDouble(homePageTimes) == 0) {
                    LayoutInflater inflater2 = LayoutInflater.from(UserDetailsActivity.this);
                    RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog2 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout2);
                    dialog2.setCanceledOnTouchOutside(true);
                    dialog2.setCancelable(true);
                    TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                    Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                    final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                    TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                    if ("提现".equals(withdrawals) && "否".equals(score)) {
                        title2.setText("温馨提示");
                        btnOK2.setText("前去评分");
                        btnCancel2.setText("下次再说");
                        title_tv2.setText("您的红包次数已用完,前去应用市场为软件评分,即可永久获得一次分享次数！");
                    } else {
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
                            if ("提现".equals(withdrawals) && "否".equals(score)) {
                                try {
                                    Uri uri = Uri.parse("market://details?id="
                                            + getPackageName());//需要评分的APP包名
                                    Intent intent5 = new Intent(Intent.ACTION_VIEW, uri);
                                    intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivityForResult(intent5, 1);
                                } catch (Exception e) {
                                    Toast.makeText(UserDetailsActivity.this, "跳转失败,请下载应用宝之后评分", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                startActivity(new Intent(UserDetailsActivity.this, HbHuoQuActivity.class));
                            }
                        }
                    });
                } else {
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
                Toast.makeText(getApplicationContext(), "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void showhbClick() {
        LayoutInflater inflater5 = LayoutInflater.from(UserDetailsActivity.this);
        RelativeLayout layout5 = (RelativeLayout) inflater5.inflate(R.layout.dialog_red, null);
        final Dialog dialog5 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
        try {
            if (!UserDetailsActivity.this.isFinishing() && !UserDetailsActivity.this.isDestroyed()) {
                dialog5.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog5.getWindow().setContentView(layout5);
        dialog5.setCanceledOnTouchOutside(true);
        dialog5.setCancelable(true);
        TextView tv_title = (TextView) layout5.findViewById(R.id.tv_title);
        TextView tv_title1 = (TextView) layout5.findViewById(R.id.tv_title1);
        TextView tv_title2 = (TextView) layout5.findViewById(R.id.tv_title2);
        ImageView iv_qq = (ImageView) layout5.findViewById(R.id.iv_qq);
        ImageView iv_wx = (ImageView) layout5.findViewById(R.id.iv_wx);
        ImageView iv_wb = (ImageView) layout5.findViewById(R.id.iv_wb);
        RelativeLayout rl_qq = (RelativeLayout) layout5.findViewById(R.id.rl_qq);
        RelativeLayout rl_wx = (RelativeLayout) layout5.findViewById(R.id.rl_wx);
        RelativeLayout rl_wb = (RelativeLayout) layout5.findViewById(R.id.rl_wb);
        if (shareType == null || "".equals(shareType) || (fxPingTai != null && fxPingTai.contains("不限"))) {
            iv_qq.setVisibility(View.VISIBLE);
            iv_wx.setVisibility(View.VISIBLE);
            iv_wb.setVisibility(View.VISIBLE);
        } else {
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
        final int i1 = DemoHelper.getInstance().getContactList().size();
        String[] num;
        if (friendsNumber != null && !"".equals(friendsNumber) && !friendsNumber.equalsIgnoreCase("null") && !"0".equals(friendsNumber)) {
            num = friendsNumber.split("\\|");
            if (num.length > 0) {
                onedown = num[0];
            }
            if (shareRed != null && !"".equals(shareRed) && !shareRed.equalsIgnoreCase("null") && Double.parseDouble(shareRed) > 0 && Double.parseDouble(onedown) > 0) {
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
        if (shareRed != null && singleShare != null && !"".equals(shareRed) && !"".equals(singleShare) && !shareRed.equalsIgnoreCase("null")) {
            if (Double.parseDouble(shareRed) > (Double.parseDouble(singleShare))) {
                Jine = singleShare;
            } else {
                Jine = shareRed;
            }
        }
        final String fenxiangJine = Jine;
        rl_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog5.dismiss();
                if (hbYaoqiu != null && !"".equals(hbYaoqiu) && !hbYaoqiu.equalsIgnoreCase("null") && Double.parseDouble(hbYaoqiu) > i1) {
                    LayoutInflater inflater1 = LayoutInflater.from(UserDetailsActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                    title_tv1.setText("不符合红包要求,用户设置好友达到" + hbYaoqiu + "人以上才能获得红包,是否继续分享？");
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
                } else {
                    if (fenxiangJine != null && Double.parseDouble(fenxiangJine) > 0) {
                        if (Double.parseDouble(fenxiangJine) != Double.parseDouble(singleShare)) {
                            LayoutInflater inflater2 = LayoutInflater.from(UserDetailsActivity.this);
                            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog2 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                            title_tv2.setText("红包余额不足奖励,此次分享只能获得" + fenxiangJine + "元,是否继续分享?");
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
                                    queryfxhbCount(fenxiangJine, 0);
                                }
                            });
                        } else {
                            queryfxhbCount(fenxiangJine, 0);
                        }
                    } else {
                        saveCurrentImage();
                    }
                }
            }
        });
        rl_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog5.dismiss();//WechatMoments.NAME
                if (hbYaoqiu != null && !"".equals(hbYaoqiu) && !hbYaoqiu.equalsIgnoreCase("null") && Double.parseDouble(hbYaoqiu) > i1) {
                    LayoutInflater inflater1 = LayoutInflater.from(UserDetailsActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                    title_tv1.setText("不符合红包要求,用户设置好友达到" + hbYaoqiu + "人以上才能获得红包,是否继续分享？");
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
                } else {
                    if (fenxiangJine != null && Double.parseDouble(fenxiangJine) > 0) {
                        if (Double.parseDouble(fenxiangJine) != Double.parseDouble(singleShare)) {
                            LayoutInflater inflater2 = LayoutInflater.from(UserDetailsActivity.this);
                            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog2 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                            title_tv2.setText("红包余额不足圈子奖励,此次分享只能获得" + fenxiangJine + "元,是否继续分享?");
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
                                    queryfxhbCount(fenxiangJine, 1);
                                }
                            });
                        } else {
                            queryfxhbCount(fenxiangJine, 1);
                        }
                    } else {
                        saveCurrentImage();
                    }
                }
            }
        });
        rl_wb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog5.dismiss();
                if (hbYaoqiu != null && !"".equals(hbYaoqiu) && !hbYaoqiu.equalsIgnoreCase("null") && Double.parseDouble(hbYaoqiu) > i1) {
                    LayoutInflater inflater1 = LayoutInflater.from(UserDetailsActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                    title_tv1.setText("不符合红包要求,用户设置好友达到" + hbYaoqiu + "人以上才能获得红包,是否继续分享？");
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
                } else {
                    if (fenxiangJine != null && Double.parseDouble(fenxiangJine) > 0) {
                        if (Double.parseDouble(fenxiangJine) != Double.parseDouble(singleShare)) {
                            LayoutInflater inflater2 = LayoutInflater.from(UserDetailsActivity.this);
                            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog2 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                            title_tv2.setText("红包余额不足圈子奖励,此次分享只能获得" + fenxiangJine + "元,是否继续分享?");
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
                                    queryfxhbCount(fenxiangJine, 2);
                                }
                            });
                        } else {
                            queryfxhbCount(fenxiangJine, 2);
                        }
                    } else {
                        saveCurrentImage();
                    }
                }
            }
        });
    }

    private void queryHBZhuFCShu() {
        String url = FXConstant.URL_QUERY_ZHFHBCSHU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("0".equals(code)) {
                    showhbClick();
                } else {
                    LayoutInflater inflater2 = LayoutInflater.from(UserDetailsActivity.this);
                    RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog2 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("mer_id", DemoHelper.getInstance().getCurrentUsernName());
                param.put("user_id", hxid);
                return param;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void fenxiangtowb(final String fenxiangJine, int sum) {
        ScreenshotUtil.getBitmapByView(UserDetailsActivity.this, findViewById(R.id.ll1), "分享名片红包", fenxiangJine, sum, true,0,0);
        String filep = Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png";
        if (!new File(filep).exists()) {
            ScreenshotUtil.saveDrawableById(UserDetailsActivity.this, R.drawable.share_mingpian);
        }
        final SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        if (fxLeiXing != null && fxLeiXing.contains("图文")) {
            sp.setShareType(Platform.SHARE_IMAGE);
            sp.setTitle("正事多app");
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
            showwbfxType(sp, fenxiangJine);
        } else if (fxLeiXing != null && fxLeiXing.contains("链接")) {
            String url = "http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=weiboHomePage";
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
            sp.setText("我转发他的主页名片，获得了一个红包，" + fxUpName + "不限行业接派单，全民分享赚红包" + url);
            sp.setTitle("正事多-接单派单工具");
            showwbfxType(sp, fenxiangJine);
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                    showwbfxType(sp, fenxiangJine);
                }
            });
            re_item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    String url = "http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=weiboHomePage";
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                    sp.setText("我转发他的主页名片，获得了一个红包，" + fxUpName + "不限行业接派单，全民分享赚红包" + url);
                    sp.setTitle("正事多-接单派单工具");
                    showwbfxType(sp, fenxiangJine);
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
                updateTJzhuanfa(hxid, 2);
                weiboHomePage++;
                homePage++;
                weiboAll++;
                if (fxPingTai != null && (fxPingTai.contains("微博") || fxPingTai.contains("不限"))) {
                    queryHByuE(fenxiangJine);
                } else if (fxPingTai == null || "".equals(fxPingTai)) {
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

    private void fenxiangtowx(final String fenxiangJine, int sum) {
        ScreenshotUtil.getBitmapByView(UserDetailsActivity.this, findViewById(R.id.ll1), "分享名片红包", fenxiangJine, sum, true,0,0);
        String filep = Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png";
        if (!new File(filep).exists()) {
            ScreenshotUtil.saveDrawableById(UserDetailsActivity.this, R.drawable.share_mingpian);
        }
        final WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        if (fxLeiXing != null && fxLeiXing.contains("图文")) {
            sp.setShareType(Platform.SHARE_IMAGE);
            sp.setTitle("正事多app");
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
            showwxfxType(sp, fenxiangJine);
        } else if (fxLeiXing != null && fxLeiXing.contains("链接")) {
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
            sp.setTitle("我转发他的主页名片，获得了一个红包，" + fxUpName + "不限行业接派单，全民分享赚红包");
            sp.setUrl("http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=weixinHomePage");
            showwxfxType(sp, fenxiangJine);
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                    showwxfxType(sp, fenxiangJine);
                }
            });
            re_item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    sp.setShareType(Platform.SHARE_WEBPAGE);
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                    sp.setTitle("我转发他的主页名片，获得了一个红包，" + fxUpName + "不限行业接派单，全民分享赚红包");
                    sp.setUrl("http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=weixinHomePage");
                    showwxfxType(sp, fenxiangJine);
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
                updateTJzhuanfa(hxid, 1);
                weixinHomePage++;
                homePage++;
                weixinAll++;
                if (fxPingTai != null && (fxPingTai.contains("微信") || fxPingTai.contains("不限"))) {
                    queryHByuE(fenxiangJine);
                } else if (fxPingTai == null || "".equals(fxPingTai)) {
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

    private void queryfxhbCount(final String fenxiangJine, final int type) {
        String url = FXConstant.URL_QUERY_HBCOUNT + DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                int sum = object.getIntValue("sum");
                sum = sum + 1;
                if (type == 0) {
                    fenxiangtoqq(fenxiangJine, sum);
                } else if (type == 1) {
                    fenxiangtowx(fenxiangJine, sum);
                } else if (type == 2) {
                    fenxiangtowb(fenxiangJine, sum);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (type == 0) {
                    fenxiangtoqq(fenxiangJine, -1);
                } else if (type == 1) {
                    fenxiangtowx(fenxiangJine, -1);
                } else if (type == 2) {
                    fenxiangtowb(fenxiangJine, -1);
                }
            }
        });
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void fenxiangtoqq(final String fenxiangJine, int sum) {
        ScreenshotUtil.getBitmapByView(UserDetailsActivity.this, findViewById(R.id.ll1), "分享名片红包", fenxiangJine, sum, true,0,0);
        String filep = Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png";
        if (!new File(filep).exists()) {
            ScreenshotUtil.saveDrawableById(UserDetailsActivity.this, R.drawable.share_mingpian);
        }
        final QZone.ShareParams sp = new QZone.ShareParams();
        if (fxLeiXing != null && fxLeiXing.contains("图文")) {
            sp.setText(null);
            sp.setTitle(null);
            sp.setTitleUrl(null);
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
            showqqfxType(sp, fenxiangJine);
        } else if (fxLeiXing != null && fxLeiXing.contains("链接")) {
            sp.setTitleUrl("http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=qqHomePage");
            sp.setSite("分享链接");
            sp.setTitle("正事多-接单派单工具");
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
            sp.setText("我转发他的主页名片，获得了一个红包，" + fxUpName + "不限行业接派单，全民分享赚红包");
            sp.setSiteUrl("http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=qqHomePage");
            showqqfxType(sp, fenxiangJine);
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                    showqqfxType(sp, fenxiangJine);
                }
            });
            re_item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    sp.setTitleUrl("http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=qqHomePage");
                    sp.setSite("分享链接");
                    sp.setTitle("正事多-接单派单工具");
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                    sp.setText("我转发他的主页名片，获得了一个红包，" + fxUpName + "不限行业接派单，全民分享赚红包");
                    sp.setSiteUrl("http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=qqHomePage");
                    showqqfxType(sp, fenxiangJine);
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

    private void showqqfxType(QZone.ShareParams sp, final String fenxiangJine) {
        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qzone.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                updateTJzhuanfa(hxid, 0);
                qqHomePage++;
                homePage++;
                qqAll++;
                if (fxPingTai != null && (fxPingTai.contains("空间") || fxPingTai.contains("不限"))) {
                    queryHByuE(fenxiangJine);
                } else if (fxPingTai == null || "".equals(fxPingTai)) {
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


    private void queryHByuE(final String fenxiangJine) {
        String url = FXConstant.URL_Get_UserInfo + hxid;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (!UserDetailsActivity.this.isFinishing()) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject object = jsonObject.getJSONObject("userInfo");
                        shareRed = object.isNull("shareRed") ? "0" : object.getString("shareRed");
                        friendsNumber = object.isNull("friendsNumber") ? "0|0|0" : object.getString("friendsNumber");
                        String[] num;
                        if (friendsNumber != null) {
                            num = friendsNumber.split("\\|");
                            if (num.length > 0) {
                                onedown = num[0];
                            }
                            singleShare = onedown;
                            String Jine = null;
                            if (shareRed != null && singleShare != null) {
                                if (Double.parseDouble(shareRed) > (Double.parseDouble(singleShare))) {
                                    Jine = singleShare;
                                } else {
                                    Jine = shareRed;
                                }
                            }
                            if (!fenxiangJine.equals(singleShare)) {
                                iv_hb.setVisibility(View.INVISIBLE);
                                btn_fenxiang.setVisibility(View.VISIBLE);
                            }
                            if (fenxiangJine.equals(Jine)) {
                                if (shareRed != null && singleShare != null) {
                                    if (Double.parseDouble(shareRed) <= Double.parseDouble(singleShare)) {
                                        queryZengZhang();
                                    }
                                }
                                addhongbao(Jine);
                                dongtai(1);
                            } else if (Jine != null && Double.parseDouble(Jine) > 0) {
                                Toast.makeText(getApplicationContext(), "手慢了，红包仅剩" + Jine + "元", Toast.LENGTH_LONG).show();
                                iv_hb.setVisibility(View.INVISIBLE);
                                btn_fenxiang.setVisibility(View.VISIBLE);
                                if (shareRed != null && singleShare != null) {
                                    if (Double.parseDouble(shareRed) <= Double.parseDouble(singleShare)) {
                                        queryZengZhang();
                                    }
                                }
                                addhongbao(Jine);
                                dongtai(1);
                            } else {
                                LayoutInflater inflater1 = LayoutInflater.from(UserDetailsActivity.this);
                                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                                final Dialog dialog1 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "网路连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void queryZengZhang() {
        String url = FXConstant.URL_QUERY_SHARECORD;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject shareRedRecord = object.optJSONObject("shareRedRecord");
                    String newShareTimes = String.valueOf(homePage);
                    String newBrowse = String.valueOf(llhomePage);
                    if (shareRedRecord == null || "".equals(shareRedRecord)) {
                        insertZengZhang(newShareTimes, newBrowse, "0", "0", "0", "0", "0");
                    } else {
                        String oldShareTimes = shareRedRecord.getString("shareTimes");
                        String oldBrowse = shareRedRecord.getString("browse");
                        String oldQq = shareRedRecord.getString("qq");
                        String oldWeixin = shareRedRecord.getString("weixin");
                        String oldWeibo = shareRedRecord.getString("weibo");
                        insertZengZhang(newShareTimes, newBrowse, oldShareTimes, oldBrowse, oldQq, oldWeixin, oldWeibo);
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
                Map<String, String> params = new HashMap<>();
                params.put("u_id", hxid);
                return params;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void insertZengZhang(final String newShareTimes, final String newBrowse, final String oldShareTimes, final String oldBrowse, final String oldQq, final String oldWeixin, final String oldWeibo) {
        String url = FXConstant.URL_INSERT_SHARECORD;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (oldShareTimes == null || "".equals(oldShareTimes) || "0".equals(oldShareTimes)) {
                    duanxintongzhi(hxid, "100", "100", newShareTimes, newBrowse, qqAll + "", weixinAll + "", weiboAll + "", 2);
                } else {
                    double newTime = Double.parseDouble(newShareTimes);
                    double newBrow = Double.parseDouble(newBrowse);
                    double oldTime = Double.parseDouble(oldShareTimes);
                    double oldBrow = Double.parseDouble(oldBrowse);
                    int shareAdd = (int) (newTime - oldTime);
                    int browAdd = (int) (newBrow - oldBrow);
                    int shareAddPer = (int) ((newTime - oldTime) * 100 / oldTime);
                    int browddPer = (int) ((newBrow - oldBrow) * 100 / oldBrow);
                    duanxintongzhi(hxid, shareAddPer + "", browddPer + "", shareAdd + "", browAdd + "", qqAll + "", weixinAll + "", weiboAll + "", 2);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                duanxintongzhi(hxid, null, null, newShareTimes, newBrowse, qqAll + "", weixinAll + "", weiboAll + "", 2);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("u_id", hxid);
                params.put("browse", newBrowse);
                params.put("qq", String.valueOf(qqHomePage));
                params.put("weixin", String.valueOf(weixinHomePage));
                params.put("weibo", String.valueOf(weiboHomePage));
                params.put("shareTimes", newShareTimes);
                return params;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void dongtai(int type) {
        final ArrayList<String> imagePaths1 = new ArrayList<>();
        imagePaths1.add(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
        final List<Param> params = new ArrayList<>();
        if (type == 1) {
            params.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
            params.add(new Param("content", "我得到了一个主页分享红包了!"));
            params.add(new Param("authType", "01"));
            params.add(new Param("shareType", "yes"));
            params.add(new Param("shareUserId", hxid));
            params.add(new Param("dType", "01"));
        } else {
            final String content;
            if ("00".equals(biaoshi)) {
                content = "我在正事多平台下线休息！";
            } else {
                content = "我在正事多平台上线接单！";
            }
            params.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
            params.add(new Param("content", content));
            params.add(new Param("authType", "01"));
            params.add(new Param("shareType", "yes"));
            params.add(new Param("shareUserId", hxid));
            params.add(new Param("dType", "01"));
        }

        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "4", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                OkHttpManager.getInstance().postMoment(params, imagePaths1, FXConstant.URL_PUBLISH, new OkHttpManager.HttpCallBack() {
                    @Override
                    public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                        String code = jsonObject.getString("code");
                        if (code.equals("SUCCESS")) {
                            updateBmob1();
                        }
                        if ("00".equals(biaoshi)) {
                            shangxiaban("下线");
                        } else if ("01".equals(biaoshi)) {
                            shangxiaban("上线");
                        }
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        if ("00".equals(biaoshi)) {
                            shangxiaban("下线");
                        } else if ("01".equals(biaoshi)) {
                            shangxiaban("上线");
                        }
                    }
                });
            }

            @Override
            public void onBan() {
                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送动态");

            }
        });

    }

    private void shangxiaban(final String shangbanzt) {
        String url = FXConstant.URL_UPDATE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("userdetailac,s", s);
                String locationState = null;
                if ("上线".equals(shangbanzt)) {
                    locationState = "01";
                } else {
                    locationState = "00";
                }
                SharedPreferences mSharedPreferences = getSharedPreferences("sangu_shxb_zhuangtai", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("zhuangtai", locationState);
                editor.commit();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                if ("上线".equals(shangbanzt)) {
                    param.put("workState", "01");
                    param.put("locationState", "01");
                } else {
                    param.put("workState", "00");
                    param.put("locationState", "00");
                }
                Log.e("param", param.toString());
                return param;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("type5", getNowTime());
                return param;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    private void duanxintongzhi(final String hxid1, final String hxid2, final String broAddPer,
                                final String shareAdd, final String browAdd, final String qqAdd, final String weixinAdd, final String weiboAdd, final int type) {
        String id = DemoHelper.getInstance().getCurrentUsernName();
        final String message;
        if (type == 0) {
            if ("".equals(hxid2)) {
                //+ id +
                message = "【正事多】 通知：用户在正事多平台联系您,希望得到您的回复!";
            } else {
                message = "【正事多】 通知：用户"+ "在正事多平台联系" + hxid2 + ",希望得到您的回复!";
            }
        } else if (type == 1) {
            message = "【正事多】 通知：您的主页红包已被分享传播完毕,一共被" + hxid2 + "人转发传播!";
        } else {
            if (broAddPer == null) {
                message = "【正事多】您的红包名片传播报告：总转发" + zhuFaSize + "次，微博" + weiboAdd + "次，朋友圈" + weixinAdd + "次，空间" + qqAdd + "次" +
                        "，浏览" + liuLanSize + "次，详情查看正事多个人统计";
            } else {
                message = "【正事多】您的红包名片传播报告：总转发" + zhuFaSize + "次，微博" + weiboAdd + "次，朋友圈" + weixinAdd + "次，空间" + qqAdd + "次" +
                        "（增长" + hxid2 + "％），浏览" + liuLanSize + "次（增长" + broAddPer + "％）详情查看正事多个人统计";
            }
        }
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if (type == 0) {
                        if ("SUCCESS".equals(code)) {
                            Toast.makeText(getApplicationContext(), "发送成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "发送失败！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        sendPushMessage(hxid1, 1, weiboAdd, weixinAdd, qqAdd, hxid2, broAddPer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError != null && volleyError.getMessage() != null) {
                    Log.e("volleyError", volleyError.getMessage());
                }
                if (type == 0) {
                    Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("message", message);
                param.put("telNum", hxid1);
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void saveCurrentImage() {
        String filep = Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png";
        ScreenshotUtil.getBitmapByView(UserDetailsActivity.this, findViewById(R.id.ll1), "分享名片红包", null, 6, false,llhomePage,homePage);
        if (!new File(filep).exists()) {
            ScreenshotUtil.saveDrawableById(UserDetailsActivity.this, R.drawable.share_mingpian);
        }
        LayoutInflater inflater5 = LayoutInflater.from(UserDetailsActivity.this);
        final RelativeLayout layout5 = (RelativeLayout) inflater5.inflate(R.layout.dialog_fenxiang, null);
        final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
        Window window = null;
        try {
            dialog.show();
            window = dialog.getWindow();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle(null);
                sp.setText(null);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qq.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        updateTJzhuanfa(hxid, 0);
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
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitleUrl("http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=qqHomePage");
                sp.setSite("分享链接");
                sp.setTitle("【正事多】里面有接单派单名片红包");
                sp.setText("我转发他的主页名片，" + fxUpName + "不限行业接派单，全民分享赚红包");
                sp.setSiteUrl("http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=qqHomePage");
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qq.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        updateTJzhuanfa(hxid, 0);
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
        LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle(null);
                sp.setText(null);
                Platform qq = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qq.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        updateTJzhuanfa(hxid, 0);
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
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitleUrl("http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=qqHomePage");
                sp.setSite("分享链接");
                sp.setTitle("正事多-接单派单工具");
                sp.setText("我转发他的主页名片，" + fxUpName + "不限行业接派单，全民分享赚红包");
                sp.setSiteUrl("http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=qqHomePage");
                Platform qq = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qq.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        updateTJzhuanfa(hxid, 0);
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
        LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("【正事多】里面有接单派单名片红包");
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        updateTJzhuanfa(hxid, 1);
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
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitle("我转发他的主页名片，" + fxUpName + "不限行业接派单，全民分享赚红包");
                sp.setUrl("http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=weixinHomePage");
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        updateTJzhuanfa(hxid, 1);
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
        LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("【正事多】里面有接单派单名片红包");
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        updateTJzhuanfa(hxid, 1);
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
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitle("我转发他的主页名片，" + fxUpName + "不限行业接派单，全民分享赚红包");
                sp.setText("我转发他的主页名片，" + fxUpName + "不限行业接派单，全民分享赚红包");
                sp.setUrl("http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=weixinHomePage");
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        updateTJzhuanfa(hxid, 1);
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
        LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("正事多-接单派单工具");
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wb.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        updateTJzhuanfa(hxid, 2);
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
                String url = "http://www.fulu86.com/Details_peo.html?u_id=" + hxid + "&type=weiboHomePage";
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                sp.setText("我转发他的主页名片，" + fxUpName + "不限行业接派单，全民分享赚红包" + url);
                sp.setTitle("正事多-接单派单工具");
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wb.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        updateTJzhuanfa(hxid, 2);
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

    private void updateTJzhuanfa(final String loginId, final int type) {
        String url = FXConstant.URL_TONGJI_ZHUANFA;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Userdetailac,add", "增加浏览次数成功" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError != null) {
                    Log.e("Userdetailac,add", "增加浏览次数错误");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("uLoginId", loginId);
                param.put("homePage", "1");
                if (type == 0) {
                    param.put("type", "qqHomePage");
                } else if (type == 1) {
                    param.put("type", "weixinHomePage");
                } else if (type == 2) {
                    param.put("type", "weiboHomePage");
                }
                param.put("f_id", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void addhongbao(final String fenxiangJine) {
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
            String url = FXConstant.URL_HUOQU_FXHONGBAO;
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (!UserDetailsActivity.this.isFinishing()) {
                        if (s == null || "".equals(s) || "{}".equals(s)) {
                            LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                            final Dialog dialog2 = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
                            dialog2.show();
                            dialog2.getWindow().setContentView(layout);
                            dialog2.setCanceledOnTouchOutside(false);
                            dialog2.setCancelable(false);
                            TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                            Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                            if ("no".equals(redInterval)) {
                                tv_title.setText("一人只能领一次红包");
                            } else {
                                int time;
                                if (redInterval.equalsIgnoreCase("null") || redInterval == null || "".equals(redInterval) || "0".equals(redInterval)) {
                                    time = 1;
                                } else {
                                    time = Integer.valueOf(redInterval);
                                }
                                tv_title.setText("请" + String.valueOf(time) + "天后再来吧~");
                            }
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                }
                            });
                        } else {
                            Log.e("Userdetail,s", s);
                            try {
                                JSONObject object = new JSONObject(s);
                                String code = object.getString("code");
                                if ("success".equals(code)) {
                                    SoundPlayUtils.play(2);
                                    LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
                                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_hongbao, null);
                                    final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                                    tv_title1.setText(name);
                                    tv_yue.setText("正事多 名片红包");
                                    layout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "网络连接错误...", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), "网络连接中断", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                    param.put("singleShare", fenxiangJine);
                    param.put("merId", hxid);
                    if ("0".equals(redInterval) || "".equals(redInterval)) {
                        param.put("redInterval", "24");
                    } else if ("no".equals(redInterval)) {
                        param.put("redInterval", "no");
                    } else {
                        param.put("redInterval", String.valueOf(Integer.valueOf(redInterval) * 24));
                    }
                    return param;
                }
            };
            MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
        } else {
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }

    private void showDialog2() {
        LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialoga = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
        dialoga.show();
        dialoga.getWindow().setContentView(layout);
        dialoga.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialoga.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialoga.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialoga.findViewById(R.id.re_item3);
        RelativeLayout re_item5 = (RelativeLayout) dialoga.findViewById(R.id.re_item5);
        RelativeLayout re_item4 = (RelativeLayout) dialoga.findViewById(R.id.re_item4);
        RelativeLayout re_item6 = (RelativeLayout) dialoga.findViewById(R.id.re_item6);
        TextView tv_item1 = (TextView) dialoga.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialoga.findViewById(R.id.tv_item2);
        TextView tv_item5 = (TextView) dialoga.findViewById(R.id.tv_item5);
        TextView tv_item4 = (TextView) dialoga.findViewById(R.id.tv_item4);
        TextView tv_item6 = (TextView) dialoga.findViewById(R.id.tv_item6);
        re_item4.setVisibility(View.VISIBLE);
        re_item5.setVisibility(View.VISIBLE);
        re_item6.setVisibility(View.VISIBLE);
        if (DemoHelper.getInstance().getContactList().containsKey(hxid)) {
            re_item1.setVisibility(View.GONE);
        } else {
            re_item1.setVisibility(View.VISIBLE);
        }
        tv_item1.setText("加好友");
        if (DemoHelper.getInstance().getBlackList().containsKey(hxid)) {
            tv_item2.setText("取消屏蔽");
        } else {
            tv_item2.setText("屏蔽该用户");
        }
        if (isBeBlacked) {
            tv_item6.setText("取消黑名单");
        } else {
            tv_item6.setText("加入黑名单");
        }
        tv_item5.setText("投诉/举报");
        tv_item4.setText("发送名片给好友");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoga.dismiss();
                LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
                dialog.show();
                dialog.getWindow().setContentView(layout);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                btnOK.setText("确定");
                btnCancel.setText("取消");
                title_tv.setText("确认添加好友么？");
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        addFriendInDetail(hxid);
                    }
                });
            }
        });
        re_item6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoga.dismiss();
                if (isBeBlacked) {
                    quxiaoPingbi(2);
                } else {
                    pingbi(2);
                }
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoga.dismiss();
                if (DemoHelper.getInstance().getBlackList().containsKey(hxid)) {
                    quxiaoPingbi(1);
                } else {
                    pingbi(1);
                }
            }
        });
        re_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoga.dismiss();

                IsRealNameAuth();

            }
        });
        re_item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoga.dismiss();
                ScreenshotUtil.getBitmapByView(UserDetailsActivity.this, findViewById(R.id.ll1), "分享名片红包", null, 6, false,llhomePage,homePage);
                startActivity(new Intent(UserDetailsActivity.this, FriendActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid)
                        .putExtra("biaoshi", "02").putExtra("filePath", Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png"));
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoga.dismiss();
            }
        });
    }


    private void IsRealNameAuth()
    {

        String url = FXConstant.URL_CHAXUN_RENZHENG;

        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                Log.d("chen", "onResponse" + s);

                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("SUCCESS")){
                        org.json.JSONObject object1 = object.getJSONObject("list");
                        if (object1!=null&&!"".equals(object1)){

                            String examine = object1.getString("examine");

                            if ("审核通过".equals(examine)) {

                                startActivity(new Intent(UserDetailsActivity.this, TousuActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid));

                            }else if ("正在审核".equals(examine))
                            {
                                //正在审核提示等待
                                Toast.makeText(UserDetailsActivity.this, "请等待实名认证审核通过后再投诉", Toast.LENGTH_SHORT).show();

                            }else {
                                //未实名 直接跳转实名提示框

                                LayoutInflater inflater2 = LayoutInflater.from(UserDetailsActivity.this);
                                RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                                final Dialog dialog2 = new AlertDialog.Builder(UserDetailsActivity.this).create();
                                dialog2.show();
                                dialog2.getWindow().setContentView(layout2);
                                dialog2.setCanceledOnTouchOutside(true);
                                dialog2.setCancelable(true);
                                TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                                Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                                final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                                btnOK2.setText("确定");
                                btnCancel2.setText("取消");
                                title_tv2.setText("请您实名认证后再进行投诉");
                                btnCancel2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                    }
                                });
                                btnOK2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(UserDetailsActivity.this, CertificationActivity.class));
                                        dialog2.dismiss();
                                    }
                                });

                            }
                        }else {

                            Toast.makeText(UserDetailsActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

                        }

                    }else {

                        Toast.makeText(UserDetailsActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if (volleyError!=null) {
                    Log.e("tixian", volleyError.getMessage());
                    Log.d("chen", "tixian" + volleyError.getMessage());
                }
                Toast.makeText(UserDetailsActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();

                param.put("uId",DemoHelper.getInstance().getCurrentUsernName());

                return param;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }


    private void quxiaoPingbi(final int type) {
        String url = FXConstant.URL_DELETE_BLACKLIST;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if ("success".equals(code)) {
                        DemoHelper.getInstance().deleteBlack(hxid);
                        startService(new Intent(UserDetailsActivity.this, ContactsService.class));
                        Toast.makeText(getApplicationContext(), "取消成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                if (type == 1) {
                    param.put("user_id", DemoHelper.getInstance().getCurrentUsernName());
                    param.put("shield_id", hxid);
                    param.put("shield_state", "01");
                } else {
                    param.put("user_id", hxid);
                    param.put("shield_id", DemoHelper.getInstance().getCurrentUsernName());
                    param.put("shield_state", "02");
                }
                return param;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void pingbi(final int type) {
        String url = FXConstant.URL_ADDTO_BLACKLIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if ("success".equals(code)) {
                        EaseUser user = new EaseUser(hxid);
                        DemoHelper.getInstance().saveBlack(user);
                        Toast.makeText(getApplicationContext(), "屏蔽成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                if (type == 1) {
                    param.put("user_id", DemoHelper.getInstance().getCurrentUsernName());
                    param.put("shield_id", hxid);
                    param.put("shield_state", "01");
                } else {
                    param.put("user_id", hxid);
                    param.put("shield_id", DemoHelper.getInstance().getCurrentUsernName());
                    param.put("shield_state", "02");
                }
                return param;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void showDialog() {
        LayoutInflater inflaterDl = LayoutInflater.from(UserDetailsActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(UserDetailsActivity.this, R.style.Dialog).create();
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
                addToDingdan(wodezhanghao, hxid, dType, typeDetail);
                dialog.dismiss();
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDetail = "02";
                addToDingdan(wodezhanghao, hxid, dType, typeDetail);
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

    private void addToDingdan(final String wodezhanghao, final String hxid, final String zy1, final String typeDetail) {
        String zyType, orderBody, biaoshi;
        if (hxid.equals(wodezhanghao)) {
            biaoshi = "08";
        } else {
            biaoshi = "00";
        }
        if ((hxid + "1").equals(zy1)) {
            zyType = resv31;
            orderBody = ZY1;
        } else if ((hxid + "2").equals(zy1)) {
            zyType = resv32;
            orderBody = ZY2;
        } else if ((hxid + "3").equals(zy1)) {
            zyType = resv33;
            orderBody = ZY3;
        } else {
            zyType = resv34;
            orderBody = ZY4;
        }
        if (zyType == null || "".equals(zyType) || zyType.equalsIgnoreCase("null")) {
            Intent intent = new Intent(UserDetailsActivity.this, UOrderDetailActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("pypass", pass);
            intent.putExtra("orderBody", orderBody);
            intent.putExtra("createTime", createTime);
            intent.putExtra("dynamicSeq", dynamicSeq);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", biaoshi);
        } else {
            if ("01".equals(zyType)) {
                Intent intent = new Intent(UserDetailsActivity.this, UOrderDetailActivity.class);
                intent.putExtra("wodezhanghao", wodezhanghao);
                intent.putExtra("hxid", hxid);
                intent.putExtra("zy1", zy1);
                intent.putExtra("pypass", pass);
                intent.putExtra("orderBody", orderBody);
                intent.putExtra("createTime", createTime);
                intent.putExtra("dynamicSeq", dynamicSeq);
                intent.putExtra("typeDetail", typeDetail);
                intent.putExtra("biaoshi", biaoshi);
                startActivity(intent);
            } else if ("02".equals(zyType)) {
                Intent intent = new Intent(UserDetailsActivity.this, UOrderDetailTwoActivity.class);
                intent.putExtra("wodezhanghao", wodezhanghao);
                intent.putExtra("distance", distance);
                intent.putExtra("hxid", hxid);
                intent.putExtra("zy1", zy1);
                intent.putExtra("pypass", pass);
                intent.putExtra("orderBody", orderBody);
                intent.putExtra("createTime", createTime);
                intent.putExtra("dynamicSeq", dynamicSeq);
                intent.putExtra("typeDetail", typeDetail);
                intent.putExtra("biaoshi", biaoshi);
                startActivity(intent);
            } else if ("03".equals(zyType)) {
                Intent intent = new Intent(UserDetailsActivity.this, UOrderDetailThreeActivity.class);
                intent.putExtra("wodezhanghao", wodezhanghao);
                intent.putExtra("hxid", hxid);
                intent.putExtra("zy1", zy1);
                intent.putExtra("pypass", pass);
                intent.putExtra("orderBody", orderBody);
                intent.putExtra("createTime", createTime);
                intent.putExtra("dynamicSeq", dynamicSeq);
                intent.putExtra("typeDetail", typeDetail);
                intent.putExtra("biaoshi", biaoshi);
                startActivity(intent);
            } else if ("04".equals(zyType)) {
                Intent intent = new Intent(UserDetailsActivity.this, UOrderDetailFourActivity.class);
                intent.putExtra("wodezhanghao", wodezhanghao);
                intent.putExtra("hxid", hxid);
                intent.putExtra("zy1", zy1);
                intent.putExtra("pypass", pass);
                intent.putExtra("orderBody", orderBody);
                intent.putExtra("createTime", createTime);
                intent.putExtra("dynamicSeq", dynamicSeq);
                intent.putExtra("typeDetail", typeDetail);
                intent.putExtra("biaoshi", biaoshi);
                startActivity(intent);
            } else if ("05".equals(zyType)) {
                Intent intent = new Intent(UserDetailsActivity.this, UOrderDetailFiveActivity.class);
                intent.putExtra("wodezhanghao", wodezhanghao);
                intent.putExtra("hxid", hxid);
                intent.putExtra("zy1", zy1);
                intent.putExtra("pypass", pass);
                intent.putExtra("orderBody", orderBody);
                intent.putExtra("createTime", createTime);
                intent.putExtra("dynamicSeq", dynamicSeq);
                intent.putExtra("typeDetail", typeDetail);
                intent.putExtra("biaoshi", biaoshi);
                startActivity(intent);
            }
        }
    }

    private void setListener() {
        //资料是自己
        if (DemoHelper.getInstance().getCurrentUsernName().equals(hxid)) {
            btnMsg.setEnabled(false);
            btnAdd.setEnabled(false);
            btn_dianhua.setEnabled(false);
            btn_duanxin.setEnabled(false);
            btnMsg.setBackgroundColor(Color.rgb(170, 170, 170));
            btnAdd.setBackgroundColor(Color.rgb(170, 170, 170));
            btn_dianhua.setBackgroundColor(Color.rgb(170, 170, 170));
            btn_duanxin.setBackgroundColor(Color.rgb(170, 170, 170));
            ll.setVisibility(View.GONE);
        }
    }

    public void doCl(View v) {
        String zyType;
        switch (v.getId()) {
            case R.id.re_my_ziliao:
                startActivity(new Intent(UserDetailsActivity.this, TongjiDetailActivity.class).putExtra("biaoshi", "01").putExtra("qiyeId", resv5).putExtra(FXConstant.JSON_KEY_HXID, hxid));
                break;
            case R.id.rl1:
                zyType = resv31;
                Intent intent = new Intent(UserDetailsActivity.this, ZYDetailActivity.class);
                intent.putExtra("distance", distance);
                intent.putExtra("zyType", zyType);
                intent.putExtra("name", name);
                intent.putExtra("fxUpName", fxUpName);
                intent.putExtra("zhuanye", "01");
                intent.putExtra("decribe", decribe1);
                intent.putExtra("remark", remark1);
                intent.putExtra("image", image1);
                intent.putExtra("create", create1);
                intent.putExtra("body", ZY1);
                intent.putExtra("margen", margen1);
                intent.putExtra("pass", pass);
                intent.putExtra("hxid", hxid);
                intent.putExtra("upId", upId1);
                intent.putExtra("liulancishu", liulancishu1);
                startActivityForResult(intent, 0);
                break;
            case R.id.rl2:
                zyType = resv32;
                Intent intent2 = new Intent(UserDetailsActivity.this, ZYDetailActivity.class);
                intent2.putExtra("distance", distance);
                intent2.putExtra("zyType", zyType);
                intent2.putExtra("name", name);
                intent2.putExtra("fxUpName", fxUpName);
                intent2.putExtra("zhuanye", "02");
                intent2.putExtra("decribe", decribe2);
                intent2.putExtra("remark", remark2);
                intent2.putExtra("image", image2);
                intent2.putExtra("create", create2);
                intent2.putExtra("body", ZY2);
                intent2.putExtra("margen", margen2);
                intent2.putExtra("pass", pass);
                intent2.putExtra("hxid", hxid);
                intent2.putExtra("upId", upId2);
                intent2.putExtra("liulancishu", liulancishu2);
                startActivityForResult(intent2, 0);
                break;
            case R.id.rl3:
                zyType = resv33;
                Intent intent3 = new Intent(UserDetailsActivity.this, ZYDetailActivity.class);
                intent3.putExtra("distance", distance);
                intent3.putExtra("zyType", zyType);
                intent3.putExtra("name", name);
                intent3.putExtra("fxUpName", fxUpName);
                intent3.putExtra("zhuanye", "03");
                intent3.putExtra("decribe", decribe3);
                intent3.putExtra("remark", remark3);
                intent3.putExtra("image", image3);
                intent3.putExtra("create", create3);
                intent3.putExtra("body", ZY3);
                intent3.putExtra("margen", margen3);
                intent3.putExtra("pass", pass);
                intent3.putExtra("hxid", hxid);
                intent3.putExtra("upId", upId3);
                intent3.putExtra("liulancishu", liulancishu3);
                startActivityForResult(intent3, 0);
                break;
            case R.id.rl4:
                zyType = resv34;
                Intent intent4 = new Intent(UserDetailsActivity.this, ZYDetailActivity.class);
                intent4.putExtra("distance", distance);
                intent4.putExtra("zyType", zyType);
                intent4.putExtra("name", name);
                intent4.putExtra("fxUpName", fxUpName);
                intent4.putExtra("zhuanye", "04");
                intent4.putExtra("decribe", decribe4);
                intent4.putExtra("remark", remark4);
                intent4.putExtra("image", image4);
                intent4.putExtra("create", create4);
                intent4.putExtra("body", ZY4);
                intent4.putExtra("margen", margen4);
                intent4.putExtra("pass", pass);
                intent4.putExtra("hxid", hxid);
                intent4.putExtra("upId", upId4);
                intent4.putExtra("liulancishu", liulancishu4);
                startActivityForResult(intent4, 0);
                break;
            case R.id.rl_current_dynamic:
                String biaoshi = "";
                if (hxid.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                    biaoshi = "00";
                } else {
                    biaoshi = "01";
                }
                Intent intent5 = new Intent(UserDetailsActivity.this, CurrentDynamicActivity.class);
                intent5.putExtra("userId", hxid);
                intent5.putExtra("biaoshi", biaoshi);
                startActivity(intent5);
                break;
            case R.id.rl_his_qiye:
                if (!"".equals(resv5)) {
                    startActivity(new Intent(UserDetailsActivity.this, QiYeDetailsActivity.class).putExtra("qiyeId", resv5).putExtra("biaoshi", "11").putExtra("com_id", hxid));
                } else {
                    Toast.makeText(getApplicationContext(), "该用户没有加入企业哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_his_pingjia:
                startActivity(new Intent(UserDetailsActivity.this, PingJiaListActivity.class).putExtra("userId", hxid));
                break;
        }
    }

    @Override
    public void updateLiuLanList(List<LiuLanDetail> liuLanDetails, JSONObject object, String size, boolean hasMore) {
        isLiulanFinish = true;
        if (object != null) {
            try {
                llhomePage = object.getInt("homePage");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (size != null && !"".equals(size) && !size.equalsIgnoreCase("null")) {
            liuLanSize = Integer.parseInt(size);
        }
        myHandler.sendEmptyMessage(0);
    }

    @Override
    public void updateZhuFaList(List<ZhuFaDetail> zhuFaDetails, JSONObject object, String size, boolean hasMore) {
        int lifeDynamics = 0;
        int locationDynamics = 0;
        int businessDynamics = 0;
        if (object != null) {
            try {
                homePage = object.getInt("homePage");
                qqHomePage = object.getInt("qqHomePage");
                weixinHomePage = object.getInt("weixinHomePage");
                weiboHomePage = object.getInt("weiboHomePage");
                int qqLifeDynamic = object.getInt("qqLifeDynamic");
                int qqLocationDynamic = object.getInt("qqLocationDynamic");
                int qqBusinessDynamic = object.getInt("qqBusinessDynamic");
                int weixinLifeDynamic = object.getInt("weixinLifeDynamic");
                int weixinLocationDynamic = object.getInt("weixinLocationDynamic");
                int weixinBusinessDynamic = object.getInt("weixinBusinessDynamic");
                int weiboLifeDynamic = object.getInt("weiboLifeDynamic");
                int weiboLocationDynamic = object.getInt("weiboLocationDynamic");
                int weiboBusinessDynamic = object.getInt("weiboBusinessDynamic");
                qqAll = qqHomePage + qqLifeDynamic + qqLocationDynamic + qqBusinessDynamic;
                weixinAll = weixinHomePage + weixinLifeDynamic + weixinLocationDynamic + weixinBusinessDynamic;
                weiboAll = weiboHomePage + weiboLifeDynamic + weiboLocationDynamic + weiboBusinessDynamic;
                lifeDynamics = object.getInt("lifeDynamics");
                locationDynamics = object.getInt("locationDynamics");
                businessDynamics = object.getInt("businessDynamics");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        isZhuFaFinish = true;
        if (size != null && !"".equals(size) && !size.equalsIgnoreCase("null")) {
            zhuFaSize = homePage + lifeDynamics + locationDynamics + businessDynamics;
        }
        myHandler.sendEmptyMessage(0);
    }

    class ImageListener implements View.OnClickListener {
        String[] images;
        int page;

        public ImageListener(String[] images, int page) {
            this.images = images;
            this.page = page;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if (page == 0) {
                if (file1Url != null && !"0".equals(file1Url)) {
                    startActivity(new Intent(UserDetailsActivity.this, VideoPlayActivity.class)
                            .putExtra("videoUrl", FXConstant.URL_AVATAR + file1Url).putExtra("imageUrl", FXConstant.URL_AVATAR + images[0])
                            .putExtra("name", name));
//                    startActivity(new Intent(UserDetailsActivity.this, VideoPlayActivity.class)
//                            .putExtra("videoUrl", FXConstant.URL_AVATAR + file1Url).putExtra("imageUrl", FXConstant.URL_AVATAR + images[0])
//                            .putExtra("name", name), ActivityOptions.makeSceneTransitionAnimation(UserDetailsActivity.this, v, "share1").toBundle());
                } else {
                    Intent intent = new Intent(getApplicationContext(), BigImageActivity.class);
                    intent.putExtra("images", images);
                    intent.putExtra("page", page);
                    intent.putExtra("biaoshi", "12");
                    startActivity(intent);
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(UserDetailsActivity.this, v, "share1").toBundle());
                }
            } else if (page == 1) {
                if (file2Url != null && !"0".equals(file2Url)) {
                    startActivity(new Intent(UserDetailsActivity.this, VideoPlayActivity.class)
                            .putExtra("videoUrl", FXConstant.URL_AVATAR + file2Url).putExtra("imageUrl", FXConstant.URL_AVATAR + images[1])
                            .putExtra("name", name));
//                    startActivity(new Intent(UserDetailsActivity.this, VideoPlayActivity.class)
//                            .putExtra("videoUrl", FXConstant.URL_AVATAR + file2Url).putExtra("imageUrl", FXConstant.URL_AVATAR + images[1])
//                            .putExtra("name", name), ActivityOptions.makeSceneTransitionAnimation(UserDetailsActivity.this, v, "share2").toBundle());
                } else {
                    Intent intent = new Intent(getApplicationContext(), BigImageActivity.class);
                    intent.putExtra("images", images);
                    intent.putExtra("page", page);
                    intent.putExtra("biaoshi", "12");
                    startActivity(intent);
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(UserDetailsActivity.this, v, "share2").toBundle());
                }
            } else if (page == 2) {
                if (file3Url != null && !"0".equals(file3Url)) {
                    startActivity(new Intent(UserDetailsActivity.this, VideoPlayActivity.class)
                            .putExtra("videoUrl", FXConstant.URL_AVATAR + file3Url).putExtra("imageUrl", FXConstant.URL_AVATAR + images[2])
                            .putExtra("name", name));
//                    startActivity(new Intent(UserDetailsActivity.this, VideoPlayActivity.class)
//                            .putExtra("videoUrl", FXConstant.URL_AVATAR + file3Url).putExtra("imageUrl", FXConstant.URL_AVATAR + images[2])
//                            .putExtra("name", name), ActivityOptions.makeSceneTransitionAnimation(UserDetailsActivity.this, v, "share3").toBundle());
                } else {
                    Intent intent = new Intent(getApplicationContext(), BigImageActivity.class);
                    intent.putExtra("images", images);
                    intent.putExtra("page", page);
                    intent.putExtra("biaoshi", "12");
                    startActivity(intent);
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(UserDetailsActivity.this, v, "share3").toBundle());
                }
            } else if (page == 3) {
                if (file4Url != null && !"0".equals(file4Url)) {
                    startActivity(new Intent(UserDetailsActivity.this, VideoPlayActivity.class)
                            .putExtra("videoUrl", FXConstant.URL_AVATAR + file4Url).putExtra("imageUrl", FXConstant.URL_AVATAR + images[3])
                            .putExtra("name", name));
                } else {
                    Intent intent = new Intent(getApplicationContext(), BigImageActivity.class);
                    intent.putExtra("images", images);
                    intent.putExtra("page", page);
                    intent.putExtra("biaoshi", "12");
                    startActivity(intent);
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(UserDetailsActivity.this, v, "share4").toBundle());
                }
            } else if (page == 4) {
                if (file5Url != null && !"0".equals(file5Url)) {
                    startActivity(new Intent(UserDetailsActivity.this, VideoPlayActivity.class)
                            .putExtra("videoUrl", FXConstant.URL_AVATAR + file5Url).putExtra("imageUrl", FXConstant.URL_AVATAR + images[4])
                            .putExtra("name", name));
                } else {
                    Intent intent = new Intent(getApplicationContext(), BigImageActivity.class);
                    intent.putExtra("images", images);
                    intent.putExtra("page", page);
                    intent.putExtra("biaoshi", "12");
                    startActivity(intent);
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(UserDetailsActivity.this, v, "share5").toBundle());
                }
            } else if (page == 5) {
                if (file6Url != null && !"0".equals(file6Url)) {
                    startActivity(new Intent(UserDetailsActivity.this, VideoPlayActivity.class)
                            .putExtra("videoUrl", FXConstant.URL_AVATAR + file6Url).putExtra("imageUrl", FXConstant.URL_AVATAR + images[5])
                            .putExtra("name", name));
                } else {
                    Intent intent = new Intent(getApplicationContext(), BigImageActivity.class);
                    intent.putExtra("images", images);
                    intent.putExtra("page", page);
                    intent.putExtra("biaoshi", "12");
                    startActivity(intent);
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(UserDetailsActivity.this, v, "share6").toBundle());
                }
            } else if (page == 6) {
                if (file7Url != null && !"0".equals(file7Url)) {
                    startActivity(new Intent(UserDetailsActivity.this, VideoPlayActivity.class)
                            .putExtra("videoUrl", FXConstant.URL_AVATAR + file7Url).putExtra("imageUrl", FXConstant.URL_AVATAR + images[6])
                            .putExtra("name", name));
                } else {
                    Intent intent = new Intent(getApplicationContext(), BigImageActivity.class);
                    intent.putExtra("images", images);
                    intent.putExtra("page", page);
                    intent.putExtra("biaoshi", "12");
                    startActivity(intent);
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(UserDetailsActivity.this, v, "share7").toBundle());
                }
            } else if (page == 7) {
                if (file8Url != null && !"0".equals(file8Url)) {
                    startActivity(new Intent(UserDetailsActivity.this, VideoPlayActivity.class)
                            .putExtra("videoUrl", FXConstant.URL_AVATAR + file8Url).putExtra("imageUrl", FXConstant.URL_AVATAR + images[7])
                            .putExtra("name", name));
                } else {
                    Intent intent = new Intent(getApplicationContext(), BigImageActivity.class);
                    intent.putExtra("images", images);
                    intent.putExtra("page", page);
                    intent.putExtra("biaoshi", "12");
                    startActivity(intent);
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(UserDetailsActivity.this, v, "share8").toBundle());
                }
            }
        }
    }

    private String getNowTime2() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        return dateFormat.format(date);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("score", "是");
                return params;
            }
        };
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    private void updateHbTimes() {
        String url = FXConstant.URL_XIUGAI_HBTIMES;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getApplicationContext(), "红包次数增加成功！", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
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
        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                uazPresenter.loadThisDetail(hxid);
                break;
            case 1:
                updateHbTimes();
                updateScore();
                break;
        }
    }

    @Override
    public void updateThisUser(Userful user2) {
        shareType = user2.getShareType();
        hbYaoqiu = user2.getSingleShare();
        redInterval = user2.getRedInterval();
        filesUrl = user2.getuFiles();
        name = TextUtils.isEmpty(user2.getName()) ? user2.getLoginId() : user2.getName();
        personalDtails = TextUtils.isEmpty(user2.getPersonalDtails()) ? "0" : user2.getPersonalDtails();
        resv5 = TextUtils.isEmpty(user2.getResv5()) ? "" : user2.getResv5();
        resv31 = TextUtils.isEmpty(user2.getZy1resv3()) ? "01" : user2.getZy1resv3();
        resv32 = TextUtils.isEmpty(user2.getZy2resv3()) ? "01" : user2.getZy2resv3();
        resv33 = TextUtils.isEmpty(user2.getZy3resv3()) ? "01" : user2.getZy3resv3();
        resv34 = TextUtils.isEmpty(user2.getZy4resv3()) ? "01" : user2.getZy4resv3();
        decribe1 = TextUtils.isEmpty(user2.getUpDescribe1()) ? "" : user2.getUpDescribe1();
        decribe2 = TextUtils.isEmpty(user2.getUpDescribe2()) ? "" : user2.getUpDescribe2();
        decribe3 = TextUtils.isEmpty(user2.getUpDescribe3()) ? "" : user2.getUpDescribe3();
        decribe4 = TextUtils.isEmpty(user2.getUpDescribe4()) ? "" : user2.getUpDescribe4();
        margen1 = TextUtils.isEmpty(user2.getMargin1()) ? "0" : user2.getMargin1();
        margen2 = TextUtils.isEmpty(user2.getMargin2()) ? "0" : user2.getMargin2();
        margen3 = TextUtils.isEmpty(user2.getMargin3()) ? "0" : user2.getMargin3();
        margen4 = TextUtils.isEmpty(user2.getMargin4()) ? "0" : user2.getMargin4();
        liulancishu1 = TextUtils.isEmpty(user2.getZy1resv1()) ? "0" : user2.getZy1resv1();
        liulancishu2 = TextUtils.isEmpty(user2.getZy2resv1()) ? "0" : user2.getZy2resv1();
        liulancishu3 = TextUtils.isEmpty(user2.getZy3resv1()) ? "0" : user2.getZy3resv1();
        liulancishu4 = TextUtils.isEmpty(user2.getZy4resv1()) ? "0" : user2.getZy4resv1();
        remark1 = TextUtils.isEmpty(user2.getRemark1()) ? "" : user2.getRemark1();
        remark2 = TextUtils.isEmpty(user2.getRemark2()) ? "" : user2.getRemark2();
        remark3 = TextUtils.isEmpty(user2.getRemark3()) ? "" : user2.getRemark3();
        remark4 = TextUtils.isEmpty(user2.getRemark4()) ? "" : user2.getRemark4();
        image1 = TextUtils.isEmpty(user2.getZyImage1()) ? "" : user2.getZyImage1();
        image2 = TextUtils.isEmpty(user2.getZyImage2()) ? "" : user2.getZyImage2();
        image3 = TextUtils.isEmpty(user2.getZyImage3()) ? "" : user2.getZyImage3();
        image4 = TextUtils.isEmpty(user2.getZyImage4()) ? "" : user2.getZyImage4();
        create1 = TextUtils.isEmpty(user2.getCreateTime1()) ? "" : user2.getCreateTime1();
        create2 = TextUtils.isEmpty(user2.getCreateTime2()) ? "" : user2.getCreateTime2();
        create3 = TextUtils.isEmpty(user2.getCreateTime3()) ? "" : user2.getCreateTime3();
        create4 = TextUtils.isEmpty(user2.getCreateTime4()) ? "" : user2.getCreateTime4();
        wodezhanghao = DemoHelper.getInstance().getCurrentUsernName();
        friendsNumber = user2.getFriendsNumber();
        shareRed = user2.getShareRed();
        upId1 = user2.getUpId1();
        upId2 = user2.getUpId2();
        upId3 = user2.getUpId3();
        upId4 = user2.getUpId4();
        sex = user2.getSex();
        sign = user2.getSignaTure();
        resv1 = user2.getResv1();
        resv2 = user2.getResv2();
        hxid = user2.getLoginId();
        if (shareType != null && !"".equals(shareType)) {
            String[] count;
            count = shareType.split("\\|");
            if (count.length > 0) {
                fxPingTai = count[0];
            }
            if (count.length > 1) {
                fxLeiXing = count[1];
            }
        }
        if (filesUrl != null && !"".equals(filesUrl) && !filesUrl.equalsIgnoreCase("null")) {
            String[] urls = filesUrl.split("\\|");
            if (urls.length > 0 && !"0".equals(urls[0])) {
                file1Url = urls[0];
                iv_v1.setImageResource(R.drawable.image_video);
            }
            if (urls.length > 1 && !"0".equals(urls[1])) {
                file2Url = urls[1];
                iv_v2.setImageResource(R.drawable.image_video);
            }
            if (urls.length > 2 && !"0".equals(urls[2])) {
                file3Url = urls[2];
                iv_v3.setImageResource(R.drawable.image_video);
            }
            if (urls.length > 3 && !"0".equals(urls[3])) {
                file4Url = urls[3];
                iv_v4.setImageResource(R.drawable.image_video);
            }
            if (urls.length > 4 && !"0".equals(urls[4])) {
                file5Url = urls[4];
                iv_v5.setImageResource(R.drawable.image_video);
            }
            if (urls.length > 5 && !"0".equals(urls[5])) {
                file6Url = urls[5];
                iv_v6.setImageResource(R.drawable.image_video);
            }
            if (urls.length > 6 && !"0".equals(urls[6])) {
                file7Url = urls[6];
                iv_v7.setImageResource(R.drawable.image_video);
            }
            if (urls.length > 7 && !"0".equals(urls[7])) {
                file8Url = urls[7];
                iv_v8.setImageResource(R.drawable.image_video);
            }
        }
        if (image1 != null && !"".equals(image1)) {
            tv_zy1_tupian.setVisibility(View.VISIBLE);
        } else {
            tv_zy1_tupian.setVisibility(View.INVISIBLE);
        }
        if (image2 != null && !"".equals(image2)) {
            tv_zy2_tupian.setVisibility(View.VISIBLE);
        } else {
            tv_zy2_tupian.setVisibility(View.INVISIBLE);
        }
        if (image3 != null && !"".equals(image3)) {
            tv_zy3_tupian.setVisibility(View.VISIBLE);
        } else {
            tv_zy3_tupian.setVisibility(View.INVISIBLE);
        }
        if (image4 != null && !"".equals(image4)) {
            tv_zy4_tupian.setVisibility(View.VISIBLE);
        } else {
            tv_zy4_tupian.setVisibility(View.INVISIBLE);
        }
        String companyName = user2.getCompany();
        if (companyName != null && !"".equals(companyName) && !"NULL".equals(companyName)) {
            try {
                companyName = URLDecoder.decode(companyName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            tv_qiye.setText("(" + companyName + ")");
        }
        String uNation = user2.getuNation();
        String resv5 = user2.getResv5();
        String resv6 = user2.getResv6();
        if (companyName == null || companyName.equals("")) {
            tv_qiye.setText(null);
        }
        if ("00".equals(resv6) && !"1".equals(uNation)) {
            tv_qiye.setText(null);
        }
        if (resv5 == null || "".equals(resv5)) {
            tv_qiye.setText(null);
        }
        if (hxid.length() > 9) {
            hxidx = hxid.substring(0, 10);
        }
        if (hxid.length() > 7) {
            hxidx2 = hxid.substring(0, 7);
        }
        String[] num;
        if (friendsNumber != null && !"0".equals(friendsNumber) && !friendsNumber.equalsIgnoreCase("null")) {
            num = friendsNumber.split("\\|");
            String onedown = "0";
            if (num.length > 0) {
                onedown = num[0];
            }
            if (shareRed != null && !"".equals(shareRed) && !shareRed.equalsIgnoreCase("null") && Double.parseDouble(shareRed) > 0 && Double.parseDouble(onedown) > 0) {
                iv_hb.setVisibility(View.VISIBLE);
                btn_fenxiang.setVisibility(View.INVISIBLE);
                exShareRed = "有";
            } else {
                iv_hb.setVisibility(View.INVISIBLE);
                btn_fenxiang.setVisibility(View.VISIBLE);
                exShareRed = "无";
            }
        } else {
            iv_hb.setVisibility(View.INVISIBLE);
            btn_fenxiang.setVisibility(View.VISIBLE);
            exShareRed = "无";
        }
        Company = user2.getCompany();
        CompanyAddress = user2.getCompanyAdress();
        Zhiye = user2.getZhiYe();
        home = user2.getHome();
        School = user2.getSchool();
        String lat = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) ? "" : DemoApplication.getInstance().getCurrentLat();
        String lng = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) ? "" : DemoApplication.getInstance().getCurrentLng();
        if (lat != null && lng != null) {
            if (!("".equals(lat) || "".equals(lng) || resv1.equals("") || resv2.equals(""))) {
                double latitude1 = Double.valueOf(lat);
                double longitude1 = Double.valueOf(lng);
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1, longitude1);
                double distance1 = DistanceUtil.getDistance(ll, ll1);
                double dou = distance1 / 1000;
                String str = String.format("%.2f", dou);//format 返回的是字符串
                if (str != null && dou >= 10000) {
                    tvJuli.setText("隐藏");
                } else {
                    distance = str + "km";
                    tvJuli.setText(distance);
                }
            } else {
                distance = "3km之外";
                tvJuli.setText("3km以外");
            }
        } else {
            distance = "3km之外";
            tvJuli.setText("3km以外");
        }
//        tv_liulan_cishu.setText(personalDtails+"次");
        imageStr = TextUtils.isEmpty(user2.getImage()) ? "" : user2.getImage();
        ZY1 = TextUtils.isEmpty(user2.getUpName1()) ? "未编辑专业" : user2.getUpName1();
        ZY2 = TextUtils.isEmpty(user2.getUpName2()) ? "未编辑专业" : user2.getUpName2();
        ZY3 = TextUtils.isEmpty(user2.getUpName3()) ? "未编辑专业" : user2.getUpName3();
        ZY4 = TextUtils.isEmpty(user2.getUpName4()) ? "未编辑专业" : user2.getUpName4();
        GZ1 = TextUtils.isEmpty(user2.getUcName1()) ? "关注项目1" : user2.getUcName1();
        GZ2 = TextUtils.isEmpty(user2.getUcName2()) ? "关注项目2" : user2.getUcName2();
        GZ3 = TextUtils.isEmpty(user2.getUcName3()) ? "关注项目3" : user2.getUcName3();
        GZ4 = TextUtils.isEmpty(user2.getUcName4()) ? "关注项目4" : user2.getUcName4();
        GZ5 = TextUtils.isEmpty(user2.getUcName5()) ? "关注项目5" : user2.getUcName5();
        GZ6 = TextUtils.isEmpty(user2.getUcName6()) ? "关注项目6" : user2.getUcName6();
        tv_name.setText(name);
        tvCompany.setText(Company);
        tvCompanyAddress.setText(CompanyAddress);
        tvhome.setText(home);
        tvZhiye.setText(Zhiye);
        tvSchool.setText(School);
        String cuTime = getNowTime2();
        if (!"".equals(create1) && create1.length() > 8) {
            String create = create1.substring(0, 4) + "-" + create1.substring(4, 6) + "-" + create1.substring(6, 8) + " " + create1.substring(8, 10);
            long day1 = jisuan(create, cuTime);
            int monTh = 0;
            if (day1 >= 30) {
                monTh = (int) day1 / 30;
                tv_chuangjian1.setText(monTh + "月");
            } else {
                tv_chuangjian1.setText(day1 + "天");
            }
        }
        if (!"".equals(create2) && create2.length() > 8) {
            String create = create2.substring(0, 4) + "-" + create2.substring(4, 6) + "-" + create2.substring(6, 8) + " " + create2.substring(8, 10);
            long day1 = jisuan(create, cuTime);
            int monTh = 0;
            if (day1 >= 30) {
                monTh = (int) day1 / 30;
                tv_chuangjian2.setText(monTh + "月");
            } else {
                tv_chuangjian2.setText(day1 + "天");
            }
        }
        if (!"".equals(create3) && create3.length() > 8) {
            String create = create3.substring(0, 4) + "-" + create3.substring(4, 6) + "-" + create3.substring(6, 8) + " " + create3.substring(8, 10);
            long day1 = jisuan(create, cuTime);
            int monTh = 0;
            if (day1 >= 30) {
                monTh = (int) day1 / 30;
                tv_chuangjian3.setText(monTh + "月");
            } else {
                tv_chuangjian3.setText(day1 + "天");
            }
        }
        if (!"".equals(create4) && create4.length() > 8) {
            String create = create4.substring(0, 4) + "-" + create4.substring(4, 6) + "-" + create4.substring(6, 8) + " " + create4.substring(8, 10);
            long day1 = jisuan(create, cuTime);
            int monTh = 0;
            if (day1 >= 30) {
                monTh = (int) day1 / 30;
                tv_chuangjian4.setText(monTh + "月");
            } else {
                tv_chuangjian4.setText(day1 + "天");
            }
        }
        if (!imageStr.equals("")) {
            String[] images = imageStr.split("\\|");
            int imNumb = images.length;
            rl_v1.setVisibility(View.VISIBLE);
            ivAvatar.setVisibility(View.VISIBLE);
            //ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + images[0], ivAvatar, DemoApplication.mOptions2);
            Glide.with(getApplicationContext()).load(FXConstant.URL_AVATAR + images[0]).into(ivAvatar);

            ivAvatar.setOnClickListener(new ImageListener(images, 0));
            if (imNumb > 1) {
                rl_v2.setVisibility(View.VISIBLE);
                ivAvatar2.setVisibility(View.VISIBLE);
                //ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + images[1], ivAvatar2, DemoApplication.mOptions2);
                Glide.with(getApplicationContext()).load(FXConstant.URL_AVATAR + images[1]).into(ivAvatar2);
                ivAvatar2.setOnClickListener(new ImageListener(images, 1));
                if (imNumb > 2) {
                    rl_v3.setVisibility(View.VISIBLE);
                    ivAvatar3.setVisibility(View.VISIBLE);
                    //ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + images[2], ivAvatar3, DemoApplication.mOptions2);
                    Glide.with(getApplicationContext()).load(FXConstant.URL_AVATAR + images[2]).into(ivAvatar3);
                    ivAvatar3.setOnClickListener(new ImageListener(images, 2));
                    if (imNumb > 3) {
                        rl_v4.setVisibility(View.VISIBLE);
                        ivAvatar4.setVisibility(View.VISIBLE);
                        //ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + images[3], ivAvatar4, DemoApplication.mOptions2);
                        Glide.with(getApplicationContext()).load(FXConstant.URL_AVATAR + images[3]).into(ivAvatar4);
                        ivAvatar4.setOnClickListener(new ImageListener(images, 3));
                        if (imNumb > 4) {
                            rl_v5.setVisibility(View.VISIBLE);
                            ivAvatar5.setVisibility(View.VISIBLE);
                           // ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + images[4], ivAvatar5, DemoApplication.mOptions2);
                            Glide.with(getApplicationContext()).load(FXConstant.URL_AVATAR + images[4]).into(ivAvatar5);
                            ivAvatar5.setOnClickListener(new ImageListener(images, 4));
                            if (imNumb > 5) {
                                rl_v6.setVisibility(View.VISIBLE);
                                ivAvatar6.setVisibility(View.VISIBLE);
                                //ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + images[5], ivAvatar6, DemoApplication.mOptions2);
                                Glide.with(getApplicationContext()).load(FXConstant.URL_AVATAR + images[5]).into(ivAvatar6);
                                ivAvatar6.setOnClickListener(new ImageListener(images, 5));
                                if (imNumb > 6) {
                                    rl_v7.setVisibility(View.VISIBLE);
                                    ivAvatar7.setVisibility(View.VISIBLE);
                                    //ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + images[6], ivAvatar7, DemoApplication.mOptions2);
                                    Glide.with(getApplicationContext()).load(FXConstant.URL_AVATAR + images[6]).into(ivAvatar7);
                                    ivAvatar7.setOnClickListener(new ImageListener(images, 6));
                                    if (imNumb > 7) {
                                        rl_v8.setVisibility(View.VISIBLE);
                                        ivAvatar8.setVisibility(View.VISIBLE);
                                        //ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + images[7], ivAvatar8, DemoApplication.mOptions2);
                                        Glide.with(getApplicationContext()).load(FXConstant.URL_AVATAR + images[7]).into(ivAvatar8);
                                        ivAvatar8.setOnClickListener(new ImageListener(images, 7));
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
        tvSign.setText("简介：" + sign);
        if (!remark1.equals("") && remark1 != null) {
            String[] remark = remark1.split(",");
            strremark1 = remark[0];
        }
        if (!remark2.equals("") && remark2 != null) {
            String[] remark = remark2.split(",");
            strremark2 = remark[0];
        }
        if (!remark3.equals("") && remark3 != null) {
            String[] remark = remark3.split(",");
            strremark3 = remark[0];
        }
        if (!remark4.equals("") && remark4 != null) {
            String[] remark = remark4.split(",");
            strremark4 = remark[0];
        }
        if (margen1 != null && Double.parseDouble(margen1) > 0) {
            tv1.setVisibility(View.VISIBLE);
            zy1_bao.setText(margen1 + "元");
        } else {
            tv1.setVisibility(View.INVISIBLE);
            zy1_bao.setText("无");
        }
        if (margen2 != null && Double.parseDouble(margen2) > 0) {
            tv2.setVisibility(View.VISIBLE);
            zy2_bao.setText(margen2 + "元");
        } else {
            tv2.setVisibility(View.INVISIBLE);
            zy2_bao.setText("无");
        }
        if (margen3 != null && Double.parseDouble(margen3) > 0) {
            tv3.setVisibility(View.VISIBLE);
            zy3_bao.setText(margen3 + "元");
        } else {
            tv3.setVisibility(View.INVISIBLE);
            zy3_bao.setText("无");
        }
        if (margen4 != null && Double.parseDouble(margen4) > 0) {
            tv4.setVisibility(View.VISIBLE);
            zy4_bao.setText(margen4 + "元");
        } else {
            tv4.setVisibility(View.INVISIBLE);
            zy4_bao.setText("无");
        }
        zy1_jiedancishu.setText(strremark1 + "次");
        zy2_jiedancishu.setText(strremark2 + "次");
        zy3_jiedancishu.setText(strremark3 + "次");
        zy4_jiedancishu.setText(strremark4 + "次");
        tvZY1.setText(ZY1);
        tvZY2.setText(ZY2);
        tvZY3.setText(ZY3);
        tvZY4.setText(ZY4);
        tvGZ1.setText(GZ1);
        tvGZ2.setText(GZ2);
        tvGZ3.setText(GZ3);
        tvGZ4.setText(GZ4);
        tvGZ5.setText(GZ5);
        tvGZ6.setText(GZ6);
        if ("未编辑专业".equals(ZY1)) {
            tv_chuangjian1.setText("0天");
            zy1_jiedancishu.setText("0次");
            zy1_bao.setText("无");
            rl1.setEnabled(false);
            btn_comit1.setEnabled(false);
            btn_comit1.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        } else {
            if (fxUpName == null) {
                fxUpName = ZY1 + "，";
            }
        }
        if ("未编辑专业".equals(ZY2)) {
            tv_chuangjian2.setText("0天");
            zy2_jiedancishu.setText("0次");
            zy2_bao.setText("无");
            rl2.setEnabled(false);
            btn_comit2.setEnabled(false);
            btn_comit2.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        } else {
            if (fxUpName == null) {
                fxUpName = ZY2 + "，";
            } else {
                fxUpName += ZY2 + "，";
            }
        }
        if ("未编辑专业".equals(ZY3)) {
            tv_chuangjian3.setText("0天");
            zy3_jiedancishu.setText("0次");
            zy3_bao.setText("无");
            rl3.setEnabled(false);
            btn_comit3.setEnabled(false);
            btn_comit3.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        } else {
            if (fxUpName == null) {
                fxUpName = ZY3 + "，";
            } else {
                fxUpName += ZY3 + "，";
            }
        }
        if ("未编辑专业".equals(ZY4)) {
            tv_chuangjian4.setText("0天");
            zy4_jiedancishu.setText("0次");
            zy4_bao.setText("无");
            rl4.setEnabled(false);
            btn_comit4.setEnabled(false);
            btn_comit4.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        } else {
            if (fxUpName == null) {
                fxUpName = ZY4 + "，";
            } else {
                fxUpName += ZY4 + "，";
            }
        }
        if ("00".equals(biaoshi) || "01".equals(biaoshi)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            saveCurrentImage();
                        }
                    });
                }
            }).start();
        }
    }

    public long jisuan(String s2, String s1) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
        Calendar calendar = new GregorianCalendar();
        Date d1 = null, d2 = null;
        try {
            d1 = df.parse(s1);
            d2 = df.parse(s2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (d1.getTime() - d2.getTime()) / (60 * 60 * 1000 * 24);
    }

    //    private int getGapCount(Date startDate, Date endDate) {
//        Calendar fromCalendar = Calendar.getInstance();
//        fromCalendar.setTime(startDate);
//        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
//        fromCalendar.set(Calendar.MINUTE, 0);
//        fromCalendar.set(Calendar.SECOND, 0);
//        fromCalendar.set(Calendar.MILLISECOND, 0);
//        Calendar toCalendar = Calendar.getInstance();
//        toCalendar.setTime(endDate);
//        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
//        toCalendar.set(Calendar.MINUTE, 0);
//        toCalendar.set(Calendar.SECOND, 0);
//        toCalendar.set(Calendar.MILLISECOND, 0);
//        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

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

    @Override
    public void updateCurrentPrice(Object success) {
        pass = DemoApplication.getApp().getCurrentPayPass();
    }

}