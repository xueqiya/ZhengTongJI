package com.sangu.apptongji.main.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.DemoModel;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.AddFriendsPreActivity;
import com.sangu.apptongji.main.activity.JieDanSettingActivity;
import com.sangu.apptongji.main.activity.MessageOrderIntroduceActivity;
import com.sangu.apptongji.main.activity.ScanCaptureActivity;
import com.sangu.apptongji.main.activity.SettingSelfActivity;
import com.sangu.apptongji.main.activity.SupportFunctionAvtivity;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.order.avtivity.SelfYuEActivity;
import com.sangu.apptongji.main.alluser.presenter.IProfilePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.ProfilePresenter;
import com.sangu.apptongji.main.alluser.view.IProfileView;
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.moments.MomentsPublishActivity;
import com.sangu.apptongji.main.qiye.CompanyInfoActivity;
import com.sangu.apptongji.main.qiye.CompanyRegistActivity;
import com.sangu.apptongji.main.qiye.JoinQiyeShfeiActivity;
import com.sangu.apptongji.main.qiye.KaoQinActivity;
import com.sangu.apptongji.main.qiye.QiYeDetailsActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.main.widget.DragFrameLayout;
import com.sangu.apptongji.main.widget.NoCacheViewPager;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.moments.WechatMoments;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static com.alibaba.sdk.android.ams.common.global.AmsGlobalHolder.getPackageName;

public class FragmentFind extends Fragment implements View.OnClickListener, IProfileView {
    private View view = null;
    private IProfilePresenter profilePresenter = null;
    private NoCacheViewPager mPaper = null;
    private String locationState = null, uNation = null, resv6 = null;
    private ImageButton btn_fabu = null;
    DragFrameLayout frameLayout = null;
    private DemoModel settingsModel = null;
    private FragmentPagerAdapter mAdapter = null;
    private RadioButton rb_shxb = null;
    Fragment[] mFragments = null;
    PersonFragment personFragment = null;
    DynamicFragment dynamicFragment = null;
    QiyeFragment qiyeFragment = null;
    QunZuFragment qunZuFragment = null;
    private TextView tv_ren = null, tv_qite = null, tv_qiye = null, tv_qunzu = null, unread_number_dongtai = null, unread_number_yonghu = null;
    RelativeLayout rl_bu;
    private TextView unread_number_bigdata;
    private ImageView iv_new_hongbao = null;
    private Button btn_tou = null;
    private boolean hasBao = true;
    private boolean isVisible = true;
    private String sex, city;
    private int type;
    private int currentIndex;
    Bundle bundle1;
    String time1 = null, time2 = null, time3 = null, time4 = null, time5 = null, time6 = null, time7 = null, time8 = null,
            shareRed, friendsNumber, companyName, dynamic_seq, createTime;
    private int xuqiuSize = 0;
    private AlertDialog dialog2;
    private AlertDialog dialog1;
    private AlertDialog dialog3;
    private String isFristLoad;//记录是否是首次加载 是的话就先滑动到动态界面
    private int currentTag=0;
    private TextView scrollAlert;
    private List<String> companyNameAarr=new ArrayList<>();
    private RelativeLayout rl_moveIssue;
    private TextView tv_issueTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find, container, false);
        RequestQueue queue = MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        profilePresenter = new ProfilePresenter(getActivity(), this);
        profilePresenter.updateData();
        currentIndex = 0;
        isFristLoad = "yes";

        SelectAllCompanyName();

        SetTimerScroll();

        return view;
    }

    //查询线上所有企业名字
    private void SelectAllCompanyName(){
        String url = FXConstant.URL_SELECTAllCOMPANYNAME;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                JSONObject object = JSON.parseObject(s);

                JSONArray array = object.getJSONArray("nameList");

                Object[] strings = array.toArray();

                if (strings.length>0){

                    for (Object object1 : strings) {

                        String companyName = (String)object1;
                        try {

                            companyName = URLDecoder.decode(companyName, "UTF-8");
                            companyNameAarr.add(companyName);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void SetTimerScroll(){

        final RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.scroll_alert);
        final TextView textView = (TextView) view.findViewById(R.id.tv_alert);

        final String[] arr1 ={"马女士预出价300元找维修家具","刘女士预出价500元找厨师做家宴","派单/300元马女士需要明天做家宴厨师","接单/300元/王先生接单厨师家宴",};

        final String[] arr2 = {"1秒前","4秒前","2秒前","3秒前","7秒前","5秒前","8秒前","9秒前"};

        final String[] arr3 = {"找装修翻新","找定做衣柜","找整木定做","需要重新装修","需要修防水","找装修工","找推拉门","找做酒柜","找厨师上门","找安装工","找维修师傅","找干杂活","找发单员","找临时司机","找2天跟班","找明天做饭","找设计师","找木工油漆","找搬货运","找广告印刷","找门头安装","找空调移机","找电工师傅"
        };
        final String[] arr4 = {"财政局家属院附近","如家酒店附近","建业附近","建材市场附近","万科附近","农业路附近","黄河路附近","北环附近","西环附近","锦江附近","政府附近","售楼部附近","家具市场附近","国税局附近","建材市场附近"
        };
        final String[] arr5 = {"1分前 绿色三谷 接到维修 下单", "7秒前 绿色三谷 接到安装 下单", "8分前 绿色三谷 接到培训 下单", "5分前 绿色三谷 接到翻新 下单","1天前 绿色三谷 接到酒店翻新", "5天前 绿色三谷 接到万科工程"
        };

        CountDownTimer cdt = new CountDownTimer(8000000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

                String alertTextStr = "";
                float textPaintWidth = 400;
                if (currentTag == 0){

                    int min=0;
                    int max=3;
                    Random random = new Random();
                    int num = random.nextInt(max)%(max-min+1) + min;
                    alertTextStr = arr1[num];

                    textView.setTextColor(Color.parseColor("#ffbebebe"));
                    layout.setBackgroundResource(R.drawable.btn_stroke_grey);

                }else {

                    if (currentTag % 2 == 0 && companyNameAarr.size()>0) {

                        if (currentTag%4 == 0){

                            //加载绿色三谷内容
                            int min=0;
                            int max=5;
                            Random random = new Random();
                            int num = random.nextInt(max)%(max-min+1) + min;
                            alertTextStr = arr5[num];

                            textView.setTextColor(Color.parseColor("#ffbebebe"));
                            layout.setBackgroundResource(R.drawable.btn_stroke_grey);


                        }else
                        {
                            int min=0;
                            int max=companyNameAarr.size();
                            Random random = new Random();
                            int num = random.nextInt(max)%(max-min+1) + min;

                            alertTextStr = companyNameAarr.get(num) + " 入驻";

                            textView.setTextColor(Color.parseColor("#ffffff"));
                            layout.setBackgroundResource(R.drawable.btn_corner_yellow13);
                        }

                    }else {

                        int min1=0;
                        int max1=7;
                        Random random1 = new Random();
                        int num1 = random1.nextInt(max1)%(max1-min1+1) + min1;

                        int min2=0;
                        int max2=22;
                        Random random2 = new Random();
                        int num2 = random2.nextInt(max2)%(max2-min2+1) + min2;

                        int min3=0;
                        int max3=14;
                        Random random3 = new Random();
                        int num3 = random3.nextInt(max3)%(max3-min3+1) + min3;

                        alertTextStr = arr2[num1]+" "+arr3[num2]+" 坐标 "+arr4[num3];
                        textView.setTextColor(Color.parseColor("#ffbebebe"));
                        layout.setBackgroundResource(R.drawable.btn_stroke_grey);

                    }

                }

                if (getActivity() != null){

                    TextPaint textPaint = textView.getPaint();
                    textPaintWidth = textPaint.measureText(alertTextStr);
                    textView.setText(alertTextStr);

                    WindowManager wm = getActivity().getWindowManager();
                    DisplayMetrics dm = new DisplayMetrics();
                    wm.getDefaultDisplay().getMetrics(dm);
                    int width = dm.widthPixels;         // 屏幕宽度（像素）
                    int height = dm.heightPixels;       // 屏幕高度（像素）
                    float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
                    int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
                    // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
                    int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
                    int screenHeight = (int) (height / density);// 屏幕高度(dp)


                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(layout, "translationX", 1200, width/2-textPaintWidth/2-40).setDuration(3000);
                    objectAnimator.start();

                    currentTag ++;

                }

            }
            @Override
            public void onFinish() {

            }
        };

        cdt.start();

    }


    private void initViews() {
        iv_new_hongbao = (ImageView) getView().findViewById(R.id.iv_new_hongbao);
        rl_bu = (RelativeLayout) getView().findViewById(R.id.rl_bu);
        rb_shxb = (RadioButton) getView().findViewById(R.id.rb_shxb);
//        final FXPopWindow fxPopWindow = new FXPopWindow(getActivity(), R.layout.popupwindow_moret, new FXPopWindow.OnItemClickListener() {
//            @Override
//            public void onClick(int position) {
//                switch (position) {
//                    //接单
//                    case 0:
//                        showDialogs(0);
//                        break;
//                    //需求
//                    case 1:
//                        showDialogs(1);
//                        break;
//                    //管理
//                    case 2:
//                        if ("1".equals(uNation) && "00".equals(resv6)) {
//                            showDialogs(2);
//                        } else {
//                            LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
//                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
//                            dialog2 = new AlertDialog.Builder(getActivity(), R.style.Dialog).create();
//                            dialog2.show();
//                            dialog2.getWindow().setContentView(layout);
//                            dialog2.setCanceledOnTouchOutside(false);
//                            dialog2.setCancelable(false);
//                            TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
//                            Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
//                            tv_title.setText("只有企业创始人才能选择管理模式！");
//                            btn_ok.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialog2.dismiss();
//                                }
//                            });
//                        }
//                        break;
//                    //社交
//                    case 3:
//                        showDialogs(3);
//                        break;
//                }
//            }
//        });



        rb_shxb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
                  //  fxPopWindow.showPopupWindow(rl_bu);


                    //暂时点击先用为发布动态
                    Intent intent6 = new Intent(getActivity(), MomentsPublishActivity.class);
                    intent6.putExtra("biaoshi", "xuqiu");
                    startActivityForResult(intent6, 0);

                    // 点击先处理为滑动到动态页面
//                    btn_tou.setVisibility(View.INVISIBLE);
//                    btn_fabu.setVisibility(View.VISIBLE);
//                    btn_fabu.setImageResource(R.drawable.robot_orange);
//                    resetColor();
//                    tv_qite.setTextColor(Color.rgb(33, 150, 243));
//                    mPaper.setCurrentItem(1);
//                    if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
//                        MainActivity.instance.setSousuoColor(2);
//                    } else if (MainTwoActivity.instance != null) {
//                        MainTwoActivity.instance.setSousuoColor(2);
//                    }

                } else {
                    Toast.makeText(getActivity(), "登陆后才可以操作哦！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDialogs(final int i) {
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("sangu_shxb_zhuangtai", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        LayoutInflater inflater1 = LayoutInflater.from(getActivity());
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        dialog1 = new AlertDialog.Builder(getActivity(), R.style.Dialog).create();
        dialog1.show();
        dialog1.getWindow().setContentView(layout1);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        final TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        final TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        if (i == 0) {
            title.setText("确定选择接单模式吗？");
            title_tv1.setText("可接收派单提示信息，进行接单报价");
        } else if (i == 1) {
            title.setText("确定选择需求模式吗？");
            title_tv1.setText("快速发出需求，智能快速派单");
        } else if (i == 2) {
            title.setText("确定选择管理模式吗？");
            title_tv1.setText("管理考勤、请假条签批、设置企业");
        } else {
            title.setText("确定选择社交模式吗？");
            title_tv1.setText("即时通讯、发坐标、抢红包");
        }
        btnOK1.setText("确定");
        btnCancel1.setText("取消");
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
                if (i == 0) {
                    rb_shxb.setBackgroundResource(R.drawable.fx_bg_text_blue);
                    rb_shxb.setText("接单");
                    if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
                        queryOeder();
                    }
                    if (currentIndex == 0) {
                        setRobot(2);
                        btn_tou.setVisibility(View.VISIBLE);
                    }
                    editor.putString("zhuangtai", "01");
                    editor.commit();
                    showDialogs2(7);
                } else if (i == 1) {
                    rb_shxb.setBackgroundResource(R.drawable.fx_bg_text_orange);
                    rb_shxb.setText("需求");
                    if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
                        queryOeder();
                    }
                    if (currentIndex == 0) {
                        setRobot(3);
                        btn_tou.setVisibility(View.VISIBLE);
                    }
                    editor.putString("zhuangtai", "02");
                    editor.commit();
                    showDialogs2(8);
                } else if (i == 2) {
                    rb_shxb.setBackgroundResource(R.drawable.fx_bg_text_darkred);
                    rb_shxb.setText("管理");
                    if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
                        queryOeder();
                    }
                    if (currentIndex == 0) {
                        setRobot(4);
                        btn_tou.setVisibility(View.VISIBLE);
                    }
                    editor.putString("zhuangtai", "03");
                    editor.commit();
                    showDialogs2(9);
                } else {
                    rb_shxb.setBackgroundResource(R.drawable.fx_bg_text_blue);
                    rb_shxb.setText("社交");
                    if (currentIndex == 0) {
                        setRobot(5);
                        btn_tou.setVisibility(View.INVISIBLE);
                    }
                    editor.putString("zhuangtai", "04");
                    editor.commit();
                    showDialogs2(10);
                }
            }
        });
    }

    private void showDialogs2(final int i) {
        LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(getActivity(), R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        TextView tv_item3 = (TextView) dialog.findViewById(R.id.tv_item3);
        TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
        re_item5.setVisibility(View.VISIBLE);
        tv_title.setText("是否分享到QQ空间/朋友圈/微博");
        tv_item1.setText("分 享 到 QQ 空 间");
        tv_item2.setText("分 享 到 朋 友 圈");
        tv_item5.setText("分 享 到 微 博");
        tv_item3.setText("取     消");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fenxiangtoqq(i);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fenxiangtowx(i);
            }
        });
        re_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fenxiangtowb(i);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void fenxiangtowb(int type) {
        ScreenshotUtil.getBitmapByView(getActivity(), getView().findViewById(R.id.rl_all), "分享名片红包", null, type, false,0,0);
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle("正事多app");
        sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
        Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wb.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getActivity(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getActivity(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getActivity(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wb.share(sp);
    }

    private void fenxiangtowx(int type) {
        ScreenshotUtil.getBitmapByView(getActivity(), getView().findViewById(R.id.rl_all), "分享名片红包", null, type, false,0,0);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle("正事多app");
        sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
        Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getActivity(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getActivity(), "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getActivity(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wx.share(sp);
    }

    private void fenxiangtoqq(int type) {
        ScreenshotUtil.getBitmapByView(getActivity(), getView().findViewById(R.id.rl_all), "分享名片红包", null, type, false,0,0);
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setText(null);
        sp.setTitle(null);
        sp.setTitleUrl(null);
        sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qzone.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getActivity(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getActivity(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getActivity(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        qzone.share(sp);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
            queryUpdatedyna();
        } else {
            unread_number_dongtai.setVisibility(View.INVISIBLE);
        }

        SharedPreferences sp1 = getActivity().getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
        city = sp1.getString("city", "郑州市");
        city = city.substring(0, city.length() - 1);
        btn_tou.setText(city);
        SharedPreferences sp = getActivity().getSharedPreferences("sangu_shxb_zhuangtai", Context.MODE_PRIVATE);
        String act = sp.getString("zhuangtai", "01");
//        if ("01".equals(act)) {
//            if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
//                queryOeder();
//            }
//            rb_shxb.setBackgroundResource(R.drawable.fx_bg_text_blue);
//            rb_shxb.setText("接单");
//            if (currentIndex == 0) {
//                setRobot(2);
//                btn_tou.setVisibility(View.VISIBLE);
//            }
//        } else if ("02".equals(act)) {
//            if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
//                queryOeder();
//            }
//            rb_shxb.setBackgroundResource(R.drawable.fx_bg_text_orange);
//            rb_shxb.setText("需求");
//            if (currentIndex == 0) {
//                setRobot(3);
//                btn_tou.setVisibility(View.VISIBLE);
//            }
//        } else if ("03".equals(act)) {
//            if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
//                queryOeder();
//            }
//            rb_shxb.setBackgroundResource(R.drawable.fx_bg_text_darkred);
//            rb_shxb.setText("管理");
//            if (currentIndex == 0) {
//                setRobot(4);
//                btn_tou.setVisibility(View.VISIBLE);
//            }
//        } else if ("04".equals(act)) {
//            rb_shxb.setBackgroundResource(R.drawable.fx_bg_text_blue);
//            rb_shxb.setText("社交");
//            if (currentIndex == 0) {
//                setRobot(5);
//                btn_tou.setVisibility(View.INVISIBLE);
//            }
//        }

        if (isFristLoad.equals("yes"))
        {

            // 首页进来先加载动态相关数据
            btn_tou.setVisibility(View.INVISIBLE);
            btn_fabu.setVisibility(View.INVISIBLE);
           // btn_fabu.setImageResource(R.drawable.robot_orange);
            btn_fabu.setImageResource(R.drawable.zhengshiduo);
            rl_moveIssue.setVisibility(View.VISIBLE);

            resetColor();
            SharedPreferences sp2 = getActivity().getSharedPreferences("sangu_dynamicClick_info", Context.MODE_PRIVATE);
            if (sp2 != null){
                SharedPreferences.Editor editor2 = sp2.edit();

                editor2.putString("dynamicType", "05");
                editor2.commit();
            }

            type=1;
            tv_qite.setTextColor(Color.rgb(33, 150, 243));
            mPaper.setCurrentItem(1);
            if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                MainActivity.instance.setSousuoColor(2);
            } else if (MainTwoActivity.instance != null) {
                MainTwoActivity.instance.setSousuoColor(2);
            }

            isFristLoad = "no";

        }


    }


    private void queryOeder() {
        String url = FXConstant.URL_QUERY_ORDER + DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    JSONObject object = JSON.parseObject(s);
                    JSONArray array = object.getJSONArray("list");
                    if (array != null && array.size() > 0) {
                        xuqiuSize = array.size();
                        JSONObject detail = array.getJSONObject(0);
                        createTime = detail.getString("timestamp");
                        dynamic_seq = detail.getString("dynamic_seq");
                        btn_tou.setText(String.valueOf(array.size()));
                    } else {
                        xuqiuSize = 0;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (iv_new_hongbao != null) {
                isVisible = true;
                SharedPreferences sp = getActivity().getSharedPreferences("sangu_shxb_zhuangtai", Context.MODE_PRIVATE);
                String act = sp.getString("zhuangtai", "01");
//                if ("01".equals(act)) {
//                    if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
//                        queryOeder();
//                    }
//                    rb_shxb.setBackgroundResource(R.drawable.fx_bg_text_blue);
//                    rb_shxb.setText("接单");
//                    if (currentIndex == 0) {
//                        setRobot(2);
//                        btn_tou.setVisibility(View.VISIBLE);
//                    }
//                } else if ("02".equals(act)) {
//                    if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
//                        queryOeder();
//                    }
//                    rb_shxb.setBackgroundResource(R.drawable.fx_bg_text_orange);
//                    rb_shxb.setText("需求");
//                    if (currentIndex == 0) {
//                        setRobot(3);
//                        btn_tou.setVisibility(View.VISIBLE);
//                    }
//                } else if ("03".equals(act)) {
//                    if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
//                        queryOeder();
//                    }
//                    rb_shxb.setBackgroundResource(R.drawable.fx_bg_text_darkred);
//                    rb_shxb.setText("管理");
//                    if (currentIndex == 0) {
//                        setRobot(4);
//                        btn_tou.setVisibility(View.VISIBLE);
//                    }
//                } else if ("04".equals(act)) {
//                    rb_shxb.setBackgroundResource(R.drawable.fx_bg_text_blue);
//                    rb_shxb.setText("社交");
//                    if (currentIndex == 0) {
//                        setRobot(5);
//                        btn_tou.setVisibility(View.INVISIBLE);
//                    }
//                }
            }
        } else {
            isVisible = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e("fragment,user,", "true");
        } else {
            Log.e("fragment,user,", "false");
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (getActivity() != null && !isVisible) {
            personFragment.onLowMemory();
            dynamicFragment.onLowMemory();
            qiyeFragment.onLowMemory();
        } else if (getActivity() != null) {
            if (currentIndex == 0) {
                dynamicFragment.onLowMemory();
                qiyeFragment.onLowMemory();
            } else if (currentIndex == 1) {
                personFragment.onLowMemory();
                qiyeFragment.onLowMemory();
            } else {
                personFragment.onLowMemory();
                dynamicFragment.onLowMemory();
            }
        }
    }

    public int getcurrentIndex() {
        return currentIndex;
    }

    public void onRefreshPer() {
        Bundle bundle = getArguments();
        String zhY = bundle.getString("zhY");
        String name = bundle.getString("name");
        String comName = bundle.getString("comName");
        String isclear = bundle.getString("isclear");
        bundle1.putString("zhY", zhY);
        bundle1.putString("name", name);
        bundle1.putString("comName", comName);
        bundle1.putString("isclear", isclear);
        Log.e("MainAc,find", "刷新person");
        personFragment.onRefresh();
    }

    public void onRefreshDyna() {
        Bundle bundle = getArguments();
        String content = bundle.getString("content");
        String type = bundle.getString("type");

        bundle1.putString("content", content);
        bundle1.putString("type", type);

        Log.e("MainAc,find", "刷新dyna");
        dynamicFragment.onRefresh();
    }

    public void onRefreshQiye() {
        Bundle bundle = getArguments();
        String baozhjin = bundle.getString("baozhjin");
        String qiye_mch = bundle.getString("qiye_mch");
        String qiye_zhy = bundle.getString("qiye_zhy");
        String isclear = bundle.getString("isclear");
        bundle1.putString("baozhjin", baozhjin);
        bundle1.putString("qiye_mch", qiye_mch);
        bundle1.putString("qiye_zhy", qiye_zhy);
        bundle1.putString("isclear", isclear);
        Log.e("MainAc,find", "刷新qiye");
        qiyeFragment.onRefresh();
    }

    private void queryUpdatedyna() {
        String time;
        if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
            time = DemoApplication.getInstance().getCurrentUser().getResv3();
            time = dataOne(time);
        } else {
            time = "20170901142225";
        }
        SharedPreferences sp = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
        final String timec1 = sp.getString("time1", time); //招标
        final String timec2 = sp.getString("time2", time); //生活
        final String timec3 = sp.getString("time3", time); //坐标
        final String timec4 = sp.getString("time4", time); //商业
        final String timec5 = sp.getString("time5", time); //坐标有红包
        final String timec6 = sp.getString("time6", time); //商业有红包

        final String timec7 = sp.getString("time7", time);
        final String timec8 = sp.getString("time8", time);
        final String timec10 = sp.getString("time10", time);
        String url = FXConstant.URL_QUERY_DYNATIME;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    time1 = object.getString("type1");
                    time2 = object.getString("type2");
                    time3 = object.getString("type3");
                    time4 = object.getString("type4");
                    time5 = object.getString("type5");
                    time6 = object.getString("type6");
                    time7 = object.getString("type7");
                    time8 = object.getString("type8");
                    String comShareRed = object.getString("comShareRed");
                    long t1, t2, t3, t4, t5, t6, t7, t8, t10;
                    if (time1 != null) {
                        t1 = Long.parseLong(time1);
                        if (timec1 != null && t1 > Long.parseLong(timec1)) {
                            unread_number_dongtai.setVisibility(View.VISIBLE);
                          //  return;
                        } else {
                            unread_number_dongtai.setVisibility(View.INVISIBLE);
                        }
                    }




                    if (time2 != null) {
                        t2 = Long.parseLong(time2);
                        if (timec2 != null && t2 > Long.parseLong(timec2)) {
                            unread_number_bigdata.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            unread_number_bigdata.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (time3 != null) {
                        t3 = Long.parseLong(time3);
                        if (timec3 != null && t3 > Long.parseLong(timec3)) {
                            unread_number_bigdata.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            unread_number_bigdata.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (time4 != null) {

                        t4 = Long.parseLong(time4);
                        if (timec4 != null && t4 > Long.parseLong(timec4)) {
                            unread_number_bigdata.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            unread_number_bigdata.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (time5 != null) {
                        t5 = Long.parseLong(time5);
                        if (timec5 != null && t5 > Long.parseLong(timec5)) {
                            unread_number_bigdata.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            unread_number_bigdata.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (time6 != null) {
                        t6 = Long.parseLong(time6);
                        if (timec6 != null && t6 > Long.parseLong(timec6)) {
                            unread_number_bigdata.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            unread_number_bigdata.setVisibility(View.INVISIBLE);
                        }
                    }

//                    if (time7 != null) {
//                        t7 = Long.parseLong(time7);
//                        if (timec7 != null && t7 > Long.parseLong(timec7)) {
//                            unread_number_dongtai.setVisibility(View.VISIBLE);
//                            return;
//                        } else {
//                            unread_number_dongtai.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                    if (time8 != null) {
//                        t8 = Long.parseLong(time8);
//                        if (timec8 != null && t8 > Long.parseLong(timec8)) {
//                            unread_number_dongtai.setVisibility(View.VISIBLE);
//                            return;
//                        } else {
//                            unread_number_dongtai.setVisibility(View.INVISIBLE);
//                        }
//                    }
                    if (comShareRed != null) {
                        t10 = Long.parseLong(comShareRed);
                        if (timec10 != null && t10 > Long.parseLong(timec10)) {
                            iv_new_hongbao.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            iv_new_hongbao.setVisibility(View.INVISIBLE);
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    public void setRobot(int type) {
        this.type = type;
        if (btn_fabu != null) {
            if (type == 1) {
               // btn_fabu.setImageResource(R.drawable.robot_orange);
                btn_fabu.setImageResource(R.drawable.zhengshiduo);
                rl_moveIssue.setVisibility(View.VISIBLE);
                btn_fabu.setVisibility(View.INVISIBLE);

                SharedPreferences sp = getActivity().getSharedPreferences("sangu_dynamicClick_info", Context.MODE_PRIVATE);

                String dynamicType = sp.getString("dynamicType","0");

                if (dynamicType.equals("02")){
                    tv_issueTitle.setText("发布招标");
                }else {
                    tv_issueTitle.setText("给我派人");
                }

            } else if (type == 2) {
                //btn_fabu.setImageResource(R.drawable.jiedan);
                btn_fabu.setVisibility(View.VISIBLE);
                btn_fabu.setImageResource(R.drawable.zhengshiduo);
                rl_moveIssue.setVisibility(View.GONE);
            } else if (type == 3) {
               // btn_fabu.setImageResource(R.drawable.xuqiu);
                btn_fabu.setVisibility(View.VISIBLE);
                btn_fabu.setImageResource(R.drawable.zhengshiduo);
                rl_moveIssue.setVisibility(View.GONE);
            } else if (type == 4) {
               // btn_fabu.setImageResource(R.drawable.guanli);
                btn_fabu.setVisibility(View.VISIBLE);
                btn_fabu.setImageResource(R.drawable.zhengshiduo);
                rl_moveIssue.setVisibility(View.GONE);
            } else if (type == 6) {
                SharedPreferences sp = getActivity().getSharedPreferences("sangu_shxb_zhuangtai", Context.MODE_PRIVATE);
                String act = sp.getString("zhuangtai", "01");
                if ("01".equals(act)) {
                    if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
                        queryOeder();
                    }
                   // btn_tou.setVisibility(View.VISIBLE);
                    //btn_fabu.setImageResource(R.drawable.jiedan);
                    btn_fabu.setVisibility(View.VISIBLE);
                    btn_fabu.setImageResource(R.drawable.zhengshiduo);
                    rl_moveIssue.setVisibility(View.GONE);
                } else if ("02".equals(act)) {
                    if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
                        queryOeder();
                    }
                   // btn_tou.setVisibility(View.VISIBLE);
                   // btn_fabu.setImageResource(R.drawable.xuqiu);
                    btn_fabu.setImageResource(R.drawable.zhengshiduo);
                    btn_fabu.setVisibility(View.VISIBLE);
                    rl_moveIssue.setVisibility(View.GONE);
                } else if ("03".equals(act)) {
                    if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
                        queryOeder();
                    }
                  //  btn_tou.setVisibility(View.VISIBLE);
                  //  btn_fabu.setImageResource(R.drawable.guanli);
                    btn_fabu.setImageResource(R.drawable.zhengshiduo);
                    btn_fabu.setVisibility(View.VISIBLE);
                    rl_moveIssue.setVisibility(View.GONE);
                } else if ("04".equals(act)) {
//                    if ("00".equals(sex)) {
//                        btn_fabu.setImageResource(R.drawable.robot_women);
//                    } else {
//                        btn_fabu.setImageResource(R.drawable.robot_men);
//                    }
                    btn_fabu.setImageResource(R.drawable.zhengshiduo);
                    btn_fabu.setVisibility(View.VISIBLE);
                    rl_moveIssue.setVisibility(View.GONE);
                }
            } else if (type == 10) {

                btn_fabu.setImageResource(R.drawable.zhengshiduo);
                rl_moveIssue.setVisibility(View.VISIBLE);
                btn_fabu.setVisibility(View.INVISIBLE);

                tv_issueTitle.setText("发 布");

            }else {
              //  btn_tou.setVisibility(View.INVISIBLE);
//                if ("00".equals(sex)) {
//                    btn_fabu.setImageResource(R.drawable.robot_women);
//                } else {
//                    btn_fabu.setImageResource(R.drawable.robot_men);
//                }

                btn_fabu.setImageResource(R.drawable.zhengshiduo);


                btn_fabu.setVisibility(View.VISIBLE);
                rl_moveIssue.setVisibility(View.GONE);

//                if (currentIndex == 1){
//
//                    btn_fabu.setVisibility(View.INVISIBLE);
//                    rl_moveIssue.setVisibility(View.VISIBLE);
//
//                }else {
//
//                    btn_fabu.setVisibility(View.VISIBLE);
//                    rl_moveIssue.setVisibility(View.GONE);
//
//                }

            }
        }
    }

    private String getNowTime2() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    private String dataOne(String time) {

        if (time == null || "".equals(time)) {

            time = getNowTime2();

        }

        String times = null;
        try {
            times = time.substring(0, 4) + time.substring(5, 7) + time.substring(8, 10) + time.substring(11, 13) + time.substring(14, 16) + time.substring(17, 19);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    private void initView() {
        btn_tou = (Button) view.findViewById(R.id.btn_tou);
        btn_fabu = (ImageButton) view.findViewById(R.id.btn_fabu);
        rl_moveIssue = (RelativeLayout) view.findViewById(R.id.rl_moveIssue);
        tv_issueTitle = (TextView) view.findViewById(R.id.tv_issueTitle);
        frameLayout = (DragFrameLayout) view.findViewById(R.id.becausefloat);

        btn_tou.setVisibility(View.INVISIBLE);
        frameLayout.setDragImageListener(new DragFrameLayout.DragImageClickListener() {
            @Override
            public void onClick() {
                if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
                    SharedPreferences sp = getActivity().getSharedPreferences("sangu_shxb_zhuangtai", Context.MODE_PRIVATE);
                    String act = sp.getString("zhuangtai", "01");
                    LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.fx_popupwindow_robit, null);
                    final Dialog dialog = new AlertDialog.Builder(getActivity(), R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                    RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                    RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                    RelativeLayout re_item4 = (RelativeLayout) dialog.findViewById(R.id.re_item4);
                    RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
                    RelativeLayout re_item6 = (RelativeLayout) dialog.findViewById(R.id.re_item6);

                    TextView tv1 = (TextView) dialog.findViewById(R.id.tv1);
                    TextView tv2 = (TextView) dialog.findViewById(R.id.tv2);
                    TextView tv3 = (TextView) dialog.findViewById(R.id.tv3);
                    TextView tv4 = (TextView) dialog.findViewById(R.id.tv4);
                    TextView tv5 = (TextView) dialog.findViewById(R.id.tv5);
                    TextView tv6 = (TextView) dialog.findViewById(R.id.tv6);

                    re_item6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                            startActivity(new Intent(getActivity(), MessageOrderIntroduceActivity.class));

                        }
                    });

                    if (type == 1) {
                        dialog.dismiss();

                        final SharedPreferences sp2 = getActivity().getSharedPreferences("sangu_dynamicClick_info", Context.MODE_PRIVATE);


                       String dynamicType =  sp2.getString("dynamicType","0");

                       if (dynamicType.equals("02")){

                           // 招标 弹出另外一个发布框

                           LayoutInflater inflater3 = LayoutInflater.from(getActivity());
                           LinearLayout layout3 = (LinearLayout) inflater3.inflate(R.layout.dialog_zbalert, null);
                           dialog3 = new AlertDialog.Builder(getActivity(), R.style.Dialog).create();
                           dialog3.show();
                           dialog3.getWindow().setContentView(layout3);
                           WindowManager.LayoutParams params = dialog3.getWindow().getAttributes() ;
                           Display display = getActivity().getWindowManager().getDefaultDisplay();
                           params.width =(int) (display.getWidth()*0.7);                     //使用这种方式更改了dialog的框宽
                           dialog3.getWindow().setAttributes(params);
                           dialog3.setCanceledOnTouchOutside(true);
                           dialog3.setCancelable(true);

                           TextView btn_issue = (TextView) layout3.findViewById(R.id.issueBtn);

                           btn_issue.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {

                                   dialog3.dismiss();
                                   PermissionUtil permissionUtil = new PermissionUtil(getActivity());
                                   permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE},
                                           new PermissionListener() {
                                               @Override
                                               public void onGranted() {
                                                   //所有权限都已经授权
                                                   Intent intent6 = new Intent(getActivity(), MomentsPublishActivity.class);
                                                   intent6.putExtra("biaoshi", "xuqiu");
                                                   intent6.putExtra("newType", "1");
                                                   startActivityForResult(intent6, 0);
                                               }

                                               @Override
                                               public void onDenied(List<String> deniedPermission) {
                                                   //Toast第一个被拒绝的权限
                                                   showLocationPermission();
                                               }

                                               @Override
                                               public void onShouldShowRationale(List<String> deniedPermission) {
                                                   //Toast第一个勾选不在提示的权限
                                                   showLocationPermission();
                                               }
                                           });

                               }
                           });

                       }else {

                           LayoutInflater inflater3 = LayoutInflater.from(getActivity());
                           LinearLayout layout3 = (LinearLayout) inflater3.inflate(R.layout.dialog_xuqiu, null);
                           dialog3 = new AlertDialog.Builder(getActivity(), R.style.Dialog).create();
                           dialog3.show();
                           dialog3.getWindow().setContentView(layout3);
                           WindowManager.LayoutParams params = dialog3.getWindow().getAttributes() ;
                           Display display = getActivity().getWindowManager().getDefaultDisplay();
                           params.width =(int) (display.getWidth()*0.8);                     //使用这种方式更改了dialog的框宽
                           dialog3.getWindow().setAttributes(params);
                           dialog3.setCanceledOnTouchOutside(true);
                           dialog3.setCancelable(true);

                           TextView btn_issue = (TextView) layout3.findViewById(R.id.issueBtn);
                           TextView tv_issue = (TextView) layout3.findViewById(R.id.tv_issue2);
                           TextView btn_bottom = (TextView) layout3.findViewById(R.id.btn_bottom);

                           tv_issue.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   dialog3.dismiss();
                                   PermissionUtil permissionUtil = new PermissionUtil(getActivity());
                                   permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE},
                                           new PermissionListener() {
                                               @Override
                                               public void onGranted() {
                                                   //所有权限都已经授权
                                                   Intent intent6 = new Intent(getActivity(), MomentsPublishActivity.class);
                                                   intent6.putExtra("biaoshi", "xuqiu");
                                                   intent6.putExtra("type", "thrid");
                                                   String dType = sp2.getString("dynamicType","0");

                                                   if (dType.equals("02")){
                                                       intent6.putExtra("newType", "1");
                                                   }
                                                   startActivityForResult(intent6, 0);
                                               }

                                               @Override
                                               public void onDenied(List<String> deniedPermission) {
                                                   //Toast第一个被拒绝的权限
                                                   showLocationPermission();
                                               }

                                               @Override
                                               public void onShouldShowRationale(List<String> deniedPermission) {
                                                   //Toast第一个勾选不在提示的权限
                                                   showLocationPermission();
                                               }
                                           });
                               }
                           });

                           btn_issue.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   dialog3.dismiss();
                                   PermissionUtil permissionUtil = new PermissionUtil(getActivity());
                                   permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE},
                                           new PermissionListener() {
                                               @Override
                                               public void onGranted() {
                                                   //所有权限都已经授权
                                                   Intent intent6 = new Intent(getActivity(), MomentsPublishActivity.class);
                                                   intent6.putExtra("biaoshi", "xuqiu");
                                                   String dType = sp2.getString("dynamicType","0");

                                                   if (dType.equals("02")){
                                                       intent6.putExtra("newType", "1");
                                                   }
                                                   startActivityForResult(intent6, 0);
                                               }

                                               @Override
                                               public void onDenied(List<String> deniedPermission) {
                                                   //Toast第一个被拒绝的权限
                                                   showLocationPermission();
                                               }

                                               @Override
                                               public void onShouldShowRationale(List<String> deniedPermission) {
                                                   //Toast第一个勾选不在提示的权限
                                                   showLocationPermission();
                                               }
                                           });
                               }
                           });


                           btn_bottom.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   dialog3.dismiss();
                                   //startActivity(new Intent(getActivity(), SettingSelfActivity.class).putExtra("friendsNumber",friendsNumber).putExtra("shareRed",shareRed).putExtra("biaoshi","00"));
                                   //startActivity(new Intent(getActivity(), QuickPublishActivity.class));
                                   startActivity(new Intent(getActivity(), JieDanSettingActivity.class));
                               }
                           });

                       }

                    } else if ("04".equals(act)) {
                        final String payPass = DemoApplication.getInstance().getCurrentPayPass();
                        if (currentIndex == 0) {
                            tv1.setText("扫  一  扫");
                        } else if (currentIndex == 1) {
                            tv1.setText("发  动  态");
                        } else if (currentIndex == 2) {
                            tv1.setText("注册企业");
                        } else if (currentIndex == 3) {
                            tv1.setText("申请直播");
                        }
                        if ("00".equals(locationState)) {
                            tv3.setText("位置开启");
                        } else {
                            tv3.setText("位置关闭");
                        }
                        if (settingsModel.getSettingMsgSound()) {
                            tv2.setText("声音关闭");
                        } else {
                            tv2.setText("声音开启");
                        }
                        re_item1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (currentIndex == 0) {
                                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                                    } else {
                                        startActivity(new Intent(getActivity(), ScanCaptureActivity.class).putExtra("payPass", payPass));
                                    }
                                } else if (currentIndex == 1) {
                                    showDialog();
                                } else if (currentIndex == 2) {
                                    String hasbzj1 = "00";
                                    if (hasBao) {
                                        hasbzj1 = "01";
                                    }
                                    String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
                                    if ("1".equals(uNation) && "00".equals(resv6)) {
                                        Intent intent6 = new Intent(getActivity(), CompanyInfoActivity.class);
                                        intent6.putExtra("resv5", qiyeId);
                                        intent6.putExtra("resv6", resv6);
                                        intent6.putExtra("pass", payPass);
                                        intent6.putExtra("baozhengjin", hasbzj1);
                                        startActivity(intent6);
                                    } else if ("0".equals(uNation) && "00".equals(resv6)) {
                                        Intent intent6 = new Intent(getActivity(), CompanyRegistActivity.class);
                                        intent6.putExtra("qiyeId", qiyeId);
                                        intent6.putExtra("biaoshi", "1");
                                        startActivity(intent6);
                                    } else if ("01".equals(resv6)) {
                                        Intent intent6 = new Intent(getActivity(), QiYeDetailsActivity.class);
                                        intent6.putExtra("qiyeId", qiyeId);
                                        startActivity(intent6);
                                    } else {
                                        Intent intent6 = new Intent(getActivity(), CompanyRegistActivity.class);
                                        intent6.putExtra("qiyeId", "");
                                        intent6.putExtra("biaoshi", "0");
                                        startActivity(intent6);
                                    }
                                } else if (currentIndex == 3) {
                                    Toast.makeText(getActivity(), "连续登陆三个月才能申请AR权限哦", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        re_item2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                startActivity(new Intent(getActivity(), SelfYuEActivity.class).putExtra("payPass", payPass).putExtra("biaoshi", "000"));
                            }
                        });
                        re_item3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                startActivity(new Intent(getActivity(), AddFriendsPreActivity.class));
                            }
                        });
                        re_item4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (settingsModel.getSettingMsgSound()) {
                                    settingsModel.setSettingMsgSound(false);
                                } else {
                                    settingsModel.setSettingMsgSound(true);
                                }
                            }
                        });
                        re_item5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if ("01".equals(locationState)) {
                                    weizhi("下线");
                                } else {
                                    weizhi("上线");
                                }
                            }
                        });

                    } else {
                        if (currentIndex == 0) {
                            if (xuqiuSize > 0) {
                                dialog.dismiss();
                                startActivity(new Intent(getActivity(), DynaDetaActivity.class).putExtra("dynamicSeq", dynamic_seq)
                                        .putExtra("createTime", createTime).putExtra("biaoshi", "01").putExtra("dType", "05").putExtra("sID", DemoHelper.getInstance().getCurrentUsernName()));
                            } else {
                                if ("03".equals(act)) {
                                    re_item5.setVisibility(View.GONE);
                                    tv1.setText("加入企业设置");
                                    tv4.setText("主页分享红包");
                                    tv5.setText("打卡考勤记录");
                                    tv2.setText("点  亮  质  保");
                                    re_item1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            startActivityForResult(new Intent(getActivity(), JoinQiyeShfeiActivity.class).putExtra("companyName", companyName).putExtra("biaoshi", "01"), 1);
                                        }
                                    });
                                    re_item2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            startActivity(new Intent(getActivity(), SettingSelfActivity.class).putExtra("friendsNumber", friendsNumber)
                                                    .putExtra("shareRed", shareRed).putExtra("biaoshi", "00"));

                                        }
                                    });
                                    re_item3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            startActivity(new Intent(getActivity(), KaoQinActivity.class));
                                        }
                                    });
                                    re_item4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            MainActivity.instance.setCurrentIndex();
                                        }
                                    });
                                } else if ("02".equals(act)) {
                                    dialog.dismiss();
                                    LayoutInflater inflater3 = LayoutInflater.from(getActivity());
                                    LinearLayout layout3 = (LinearLayout) inflater3.inflate(R.layout.dialog_xuqiu, null);
                                    dialog3 = new AlertDialog.Builder(getActivity(), R.style.Dialog).create();
                                    dialog3.show();
                                    dialog3.getWindow().setContentView(layout3);
                                    WindowManager.LayoutParams params = dialog3.getWindow().getAttributes() ;
                                    Display display = getActivity().getWindowManager().getDefaultDisplay();
                                    params.width =(int) (display.getWidth()*0.8);                     //使用这种方式更改了dialog的框宽
                                    dialog3.getWindow().setAttributes(params);
                                    dialog3.setCanceledOnTouchOutside(true);
                                    dialog3.setCancelable(true);

                                    TextView btn_issue = (TextView) layout3.findViewById(R.id.issueBtn);
                                    TextView tv_issue = (TextView) layout3.findViewById(R.id.tv_issue2);
                                    TextView btn_bottom = (TextView) layout3.findViewById(R.id.btn_bottom);

                                    tv_issue.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog3.dismiss();
                                            PermissionUtil permissionUtil = new PermissionUtil(getActivity());
                                            permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE},
                                                    new PermissionListener() {
                                                        @Override
                                                        public void onGranted() {
                                                            //所有权限都已经授权
                                                            Intent intent6 = new Intent(getActivity(), MomentsPublishActivity.class);
                                                            intent6.putExtra("biaoshi", "xuqiu");
                                                            intent6.putExtra("type", "thrid");
                                                            startActivityForResult(intent6, 0);
                                                        }

                                                        @Override
                                                        public void onDenied(List<String> deniedPermission) {
                                                            //Toast第一个被拒绝的权限
                                                            showLocationPermission();
                                                        }

                                                        @Override
                                                        public void onShouldShowRationale(List<String> deniedPermission) {
                                                            //Toast第一个勾选不在提示的权限
                                                            showLocationPermission();
                                                        }
                                                    });
                                        }
                                    });

                                    btn_issue.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            dialog3.dismiss();
                                            PermissionUtil permissionUtil = new PermissionUtil(getActivity());
                                            permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE},
                                                    new PermissionListener() {
                                                        @Override
                                                        public void onGranted() {
                                                            //所有权限都已经授权
                                                            Intent intent6 = new Intent(getActivity(), MomentsPublishActivity.class);
                                                            intent6.putExtra("biaoshi", "xuqiu");
                                                            startActivityForResult(intent6, 0);
                                                        }

                                                        @Override
                                                        public void onDenied(List<String> deniedPermission) {
                                                            //Toast第一个被拒绝的权限
                                                            showLocationPermission();
                                                        }

                                                        @Override
                                                        public void onShouldShowRationale(List<String> deniedPermission) {
                                                            //Toast第一个勾选不在提示的权限
                                                            showLocationPermission();
                                                        }
                                                    });

                                        }
                                    });


                                    btn_bottom.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog3.dismiss();
                                            //startActivity(new Intent(getActivity(), SettingSelfActivity.class).putExtra("friendsNumber", friendsNumber).putExtra("shareRed", shareRed).putExtra("biaoshi", "00"));
                                            //startActivity(new Intent(getActivity(), QuickPublishActivity.class));
                                            startActivity(new Intent(getActivity(), JieDanSettingActivity.class));
                                        }
                                    });
                                } else {
                                    dialog.dismiss();
                                    LayoutInflater inflater3 = LayoutInflater.from(getActivity());
                                    RelativeLayout layout3 = (RelativeLayout) inflater3.inflate(R.layout.dialog_jiedan, null);
                                    final Dialog dialog3 = new AlertDialog.Builder(getActivity(), R.style.Dialog).create();
                                    dialog3.show();
                                    dialog3.getWindow().setContentView(layout3);
                                    dialog3.setCanceledOnTouchOutside(true);
                                    dialog3.setCancelable(true);
                                    RelativeLayout rl_item1 = (RelativeLayout) layout3.findViewById(R.id.re_item1);
                                    RelativeLayout rl_item2 = (RelativeLayout) layout3.findViewById(R.id.re_item2);
                                    RelativeLayout rl_item3 = (RelativeLayout) layout3.findViewById(R.id.re_item3);
                                    RelativeLayout rl_item4 = (RelativeLayout) layout3.findViewById(R.id.re_item4);
                                    RelativeLayout r1_item5 = (RelativeLayout) layout3.findViewById(R.id.re_item5);
                                    RelativeLayout r1_item6 = (RelativeLayout) layout3.findViewById(R.id.re_item7);

                                    rl_item1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog3.dismiss();
                                            // startActivity(new Intent(getActivity(), QuickPublishActivity.class));
                                            PermissionUtil permissionUtil = new PermissionUtil(getActivity());
                                            permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE},
                                                    new PermissionListener() {
                                                        @Override
                                                        public void onGranted() {
                                                            //所有权限都已经授权
                                                            Intent intent6 = new Intent(getActivity(), MomentsPublishActivity.class);
                                                            intent6.putExtra("biaoshi", "xuqiu");
                                                            startActivityForResult(intent6, 0);
                                                        }

                                                        @Override
                                                        public void onDenied(List<String> deniedPermission) {
                                                            //Toast第一个被拒绝的权限
                                                            showLocationPermission();
                                                        }

                                                        @Override
                                                        public void onShouldShowRationale(List<String> deniedPermission) {
                                                            //Toast第一个勾选不在提示的权限
                                                            showLocationPermission();
                                                        }
                                                    });
                                        }
                                    });
                                    rl_item2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog3.dismiss();
                                            MainActivity.instance.setCurrentIndex();

                                        }
                                    });
                                    rl_item3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog3.dismiss();
                                            Intent intent2 = new Intent(getActivity(), MomentsPublishActivity.class);
                                            intent2.putExtra("biaoshi", "xinwen");
                                            startActivityForResult(intent2, 0);
                                        }
                                    });
                                    rl_item4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog3.dismiss();
                                            startActivity(new Intent(getActivity(), SettingSelfActivity.class).putExtra("friendsNumber", friendsNumber).putExtra("shareRed", shareRed).putExtra("biaoshi", "00"));

                                        }
                                    });

                                    r1_item5.setOnClickListener(new  View.OnClickListener(){

                                        @Override
                                        public void onClick(View view) {
                                            dialog3.dismiss();

                                            Intent intent3 = new Intent(getActivity(), MomentsPublishActivity.class);
                                            intent3.putExtra("biaoshi", "shangye");
                                            startActivityForResult(intent3, 0);

                                        }

                                    });

                                    r1_item6.setOnClickListener(new  View.OnClickListener(){

                                        @Override
                                        public void onClick(View view) {
                                            dialog3.dismiss();

                                            Intent intent3 = new Intent(getActivity(), SupportFunctionAvtivity.class);

                                            startActivityForResult(intent3, 0);

                                        }

                                    });

                                   /* tv1.setText("设置专业/产品");
                                    tv4.setText("名片推广红包");
                                    tv5.setText("红包商业动态");
                                    tv2.setText("点  亮  质  保");
                                    tv3.setText("发布定位宝藏");
                                    re_item1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            startActivity(new Intent(getActivity(), ProfileActivity.class));
                                        }
                                    });
                                    re_item2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            startActivity(new Intent(getActivity(), SettingSelfActivity.class).putExtra("friendsNumber", friendsNumber).putExtra("shareRed", shareRed).putExtra("biaoshi", "00"));
                                        }
                                    });
                                    re_item3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            Intent intent3 = new Intent(getActivity(), MomentsPublishActivity.class);
                                            intent3.putExtra("biaoshi", "shangye");
                                            startActivityForResult(intent3, 0);
                                        }
                                    });
                                    re_item4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            MainActivity.instance.setCurrentIndex();
                                        }
                                    });
                                    re_item5.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            Intent intent2 = new Intent(getActivity(), MomentsPublishActivity.class);
                                            intent2.putExtra("biaoshi", "xinwen");
                                            startActivityForResult(intent2, 0);
                                        }
                                    });*/
                                }
                            }
                        } else {
                            if (currentIndex == 1) {
                                tv1.setText("发      动      态");
                            } else if (currentIndex == 2) {
                                tv1.setText("申   请   企   业");
                            } else if (currentIndex == 3) {
                                tv1.setText("发      动      态");
                            }
                            tv4.setText("名片推广红包");
                            tv5.setText("红包商业动态");
                            tv2.setText("点  亮  质  保");
                            tv3.setText("发布定位宝藏");
                            re_item1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    if (currentIndex == 1) {
                                        showDialog();
                                    } else if (currentIndex == 2) {
                                        String hasbzj1 = "00";
                                        if (hasBao) {
                                            hasbzj1 = "01";
                                        }
                                        String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
                                        if ("1".equals(uNation) && "00".equals(resv6)) {
                                            Intent intent6 = new Intent(getActivity(), CompanyInfoActivity.class);
                                            intent6.putExtra("resv5", qiyeId);
                                            intent6.putExtra("resv6", resv6);
                                            intent6.putExtra("baozhengjin", hasbzj1);
                                            startActivity(intent6);
                                        } else if ("0".equals(uNation) && "00".equals(resv6)) {
                                            Intent intent6 = new Intent(getActivity(), CompanyRegistActivity.class);
                                            intent6.putExtra("qiyeId", qiyeId);
                                            intent6.putExtra("biaoshi", "1");
                                            startActivity(intent6);
                                        } else if ("01".equals(resv6)) {
                                            Intent intent6 = new Intent(getActivity(), QiYeDetailsActivity.class);
                                            intent6.putExtra("qiyeId", qiyeId);
                                            startActivity(intent6);
                                        } else {
                                            Intent intent6 = new Intent(getActivity(), CompanyRegistActivity.class);
                                            intent6.putExtra("qiyeId", "");
                                            intent6.putExtra("biaoshi", "0");
                                            startActivity(intent6);
                                        }
                                    } else if (currentIndex == 3) {
                                        showDialog();
                                        //Toast.makeText(getActivity(), "连续登陆三个月才能申请AR权限哦", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            re_item2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    startActivity(new Intent(getActivity(), SettingSelfActivity.class).putExtra("friendsNumber", friendsNumber).putExtra("shareRed", shareRed).putExtra("biaoshi", "00"));
                                }
                            });
                            re_item3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Intent intent3 = new Intent(getActivity(), MomentsPublishActivity.class);
                                    intent3.putExtra("biaoshi", "shangye");
                                    startActivityForResult(intent3, 0);
                                }
                            });
                            re_item4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    MainActivity.instance.setCurrentIndex();
                                }
                            });
                            re_item5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Intent intent2 = new Intent(getActivity(), MomentsPublishActivity.class);
                                    intent2.putExtra("biaoshi", "xinwen");
                                    startActivityForResult(intent2, 0);
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "登陆后才可以操作哦！", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                final String payPass = DemoApplication.getInstance().getCurrentPayPass();
                //权限通过
                startActivity(new Intent(getActivity(), ScanCaptureActivity.class).putExtra("payPass", payPass));
            } else {  //权限拒绝
                Toast.makeText(getActivity(), "您拒绝了访问摄像头权限，请前往设置手动打开权限！", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settingsModel = DemoHelper.getInstance().getModel();
        initViews();
        initLayout();
        initView();



        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mPaper.setOffscreenPageLimit(2);
        } else {
            mPaper.setOffscreenPageLimit(0);
        }
        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments[position % mFragments.length];
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };
        mPaper.setAdapter(mAdapter);
        mPaper.setOnPageChangeListener(new NoCacheViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                JCVideoPlayer.releaseAllVideos();
                resetColor();
                switch (position) {
                    case 0:
                        btn_fabu.setVisibility(View.VISIBLE);
                        rl_moveIssue.setVisibility(View.INVISIBLE);
                        tv_ren.setTextColor(Color.rgb(33, 150, 243));
                        if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                            MainActivity.instance.setSousuoColor(0);
                            MainActivity.instance.setRobot(6);
                        } else if (MainTwoActivity.instance != null) {
                            MainTwoActivity.instance.setSousuoColor(0);
                            MainTwoActivity.instance.setRobot(6);
                        }
                        break;
                    case 1:
                        SharedPreferences sp = getActivity().getSharedPreferences("sangu_dynamicClick_info", Context.MODE_PRIVATE);

                        String dType = sp.getString("dynamicType","0");

                        unread_number_dongtai.setVisibility(View.INVISIBLE);

                        if (dType.equals("06")){

                            btn_tou.setVisibility(View.INVISIBLE);
                            btn_fabu.setVisibility(View.VISIBLE);
                            rl_moveIssue.setVisibility(View.INVISIBLE);

                        }else {

                            btn_tou.setVisibility(View.INVISIBLE);
                            btn_fabu.setVisibility(View.INVISIBLE);
                            rl_moveIssue.setVisibility(View.VISIBLE);
                        }

                        tv_qite.setTextColor(Color.rgb(33, 150, 243));
                        if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                            MainActivity.instance.setSousuoColor(2);
                        } else if (MainTwoActivity.instance != null) {
                            MainTwoActivity.instance.setSousuoColor(2);
                        }
                        break;
                    case 2:
                        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString("time10", getNowTime());
                        editor.commit();
                        btn_fabu.setVisibility(View.VISIBLE);
                        rl_moveIssue.setVisibility(View.INVISIBLE);
                        btn_tou.setVisibility(View.INVISIBLE);
                        tv_qiye.setTextColor(Color.rgb(33, 150, 243));
                        if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                            MainActivity.instance.setSousuoColor(3);
                            MainActivity.instance.setRobot(5);
                        } else if (MainTwoActivity.instance != null) {
                            MainTwoActivity.instance.setSousuoColor(3);
                            MainTwoActivity.instance.setRobot(5);
                        }
                        break;
                    case 3:
                        btn_fabu.setVisibility(View.GONE);
                        btn_tou.setVisibility(View.INVISIBLE);
                        rl_moveIssue.setVisibility(View.INVISIBLE);
                        tv_qunzu.setTextColor(Color.rgb(33, 150, 243));
                        if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                            MainActivity.instance.setSousuoColor(1);
                            MainActivity.instance.setRobot(5);
                        } else if (MainTwoActivity.instance != null) {
                            MainTwoActivity.instance.setSousuoColor(1);
                            MainTwoActivity.instance.setRobot(5);
                        }
                        break;
                    default:
                        break;
                }
                currentIndex = position;
//                if (currentIndex==1){
//                    unread_number_dongtai.setVisibility(View.INVISIBLE);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void hidUnread() {
        unread_number_dongtai.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            dynamicFragment.onRefresh();
        }
    }

    private void initLayout() {
        mPaper = (NoCacheViewPager) view.findViewById(R.id.vp);
        tv_qiye = (TextView) view.findViewById(R.id.tv_qiye);
        tv_qunzu = (TextView) view.findViewById(R.id.tv_qunzu);
        unread_number_dongtai = (TextView) view.findViewById(R.id.unread_number_dongtai);
        unread_number_yonghu = (TextView) view.findViewById(R.id.unread_number_yonghu);
        unread_number_bigdata = (TextView) view.findViewById(R.id.unread_number_bigdata);

        tv_ren = (TextView) view.findViewById(R.id.tv_ren);
        tv_qite = (TextView) view.findViewById(R.id.tv_qite);
        tv_qiye.setOnClickListener(this);
        tv_qunzu.setOnClickListener(this);
        tv_qite.setOnClickListener(this);
        tv_ren.setOnClickListener(this);
        bundle1 = new Bundle();
        personFragment = new PersonFragment();
        dynamicFragment = new DynamicFragment();
        qiyeFragment = new QiyeFragment();
        qunZuFragment = new QunZuFragment();
        personFragment.setArguments(bundle1);
        dynamicFragment.setArguments(bundle1);
        qiyeFragment.setArguments(bundle1);
        mFragments = new Fragment[]{personFragment, dynamicFragment, qiyeFragment, qunZuFragment};

    }

    private void resetColor() {
        tv_ren.setTextColor(Color.rgb(170, 170, 170));
        tv_qite.setTextColor(Color.rgb(170, 170, 170));
        tv_qiye.setTextColor(Color.rgb(170, 170, 170));
        tv_qunzu.setTextColor(Color.rgb(170, 170, 170));
    }

    @Override
    public void onClick(View v) {
        JCVideoPlayer.releaseAllVideos();
        switch (v.getId()) {
            case R.id.tv_ren:
                unread_number_yonghu.setVisibility(View.INVISIBLE);
                btn_fabu.setVisibility(View.VISIBLE);
                rl_moveIssue.setVisibility(View.INVISIBLE);
                resetColor();
                tv_ren.setTextColor(Color.rgb(33, 150, 243));
                mPaper.setCurrentItem(0);
                if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                    MainActivity.instance.setSousuoColor(0);
                    MainActivity.instance.setRobot(6);
                } else if (MainTwoActivity.instance != null) {
                    MainTwoActivity.instance.setSousuoColor(0);
                    MainTwoActivity.instance.setRobot(6);
                }
                break;
            case R.id.tv_qite:

                SharedPreferences sp = getActivity().getSharedPreferences("sangu_dynamicClick_info", Context.MODE_PRIVATE);

                String dType = sp.getString("dynamicType","0");

                if (dType.equals("06")){

                    btn_tou.setVisibility(View.INVISIBLE);
                    btn_fabu.setVisibility(View.VISIBLE);
                    rl_moveIssue.setVisibility(View.INVISIBLE);

                }else {

                    btn_tou.setVisibility(View.INVISIBLE);
                    btn_fabu.setVisibility(View.INVISIBLE);
                    rl_moveIssue.setVisibility(View.VISIBLE);
                }


                resetColor();
                tv_qite.setTextColor(Color.rgb(33, 150, 243));
                mPaper.setCurrentItem(1);
                if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                    MainActivity.instance.setSousuoColor(2);
                } else if (MainTwoActivity.instance != null) {
                    MainTwoActivity.instance.setSousuoColor(2);
                }
                break;
            case R.id.tv_qiye:
                SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("time10", getNowTime());
                editor.commit();
                btn_tou.setVisibility(View.INVISIBLE);
                btn_fabu.setVisibility(View.VISIBLE);
                rl_moveIssue.setVisibility(View.INVISIBLE);
                resetColor();
                tv_qiye.setTextColor(Color.rgb(33, 150, 243));
                mPaper.setCurrentItem(2);
                if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                    MainActivity.instance.setSousuoColor(3);
                    MainActivity.instance.setRobot(5);
                } else if (MainTwoActivity.instance != null) {
                    MainTwoActivity.instance.setSousuoColor(3);
                    MainTwoActivity.instance.setRobot(5);
                }
                break;
            case R.id.tv_qunzu:
                unread_number_bigdata.setVisibility(View.INVISIBLE);
                btn_tou.setVisibility(View.INVISIBLE);
                btn_fabu.setVisibility(View.GONE);
                rl_moveIssue.setVisibility(View.INVISIBLE);
                resetColor();
                tv_qunzu.setTextColor(Color.rgb(33, 150, 243));
                mPaper.setCurrentItem(3);
                if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                    MainActivity.instance.setSousuoColor(1);
                    MainActivity.instance.setRobot(10);
                } else if (MainTwoActivity.instance != null) {
                    MainTwoActivity.instance.setSousuoColor(1);
                    MainTwoActivity.instance.setRobot(10);
                }
                break;
            default:
                break;
        }
    }

    private void weizhi(final String shangbanzt) {
        String url = FXConstant.URL_UPDATE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if ("上线".equals(shangbanzt)) {
                    locationState = "01";
                } else {
                    locationState = "00";
                }
                Toast.makeText(getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
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
                    param.put("locationState", "01");
                } else {
                    param.put("locationState", "00");
                }
                return param;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void showDialog() {
        LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_buttom2_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(getActivity(), R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        RelativeLayout re_item4 = (RelativeLayout) dialog.findViewById(R.id.re_item4);
        RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
        RelativeLayout re_item6 = (RelativeLayout) dialog.findViewById(R.id.re_item6);
       // re_item4.setVisibility(View.GONE);
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), MomentsPublishActivity.class);
                intent1.putExtra("biaoshi", "shenghuo");
                startActivityForResult(intent1, 0);
                dialog.dismiss();
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), MomentsPublishActivity.class);
                intent2.putExtra("biaoshi", "xinwen");
                startActivityForResult(intent2, 0);
                dialog.dismiss();
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getActivity(), MomentsPublishActivity.class);
                intent3.putExtra("biaoshi", "shangye");
                startActivityForResult(intent3, 0);
                dialog.dismiss();
            }
        });
        re_item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(getActivity(), MomentsPublishActivity.class);
                intent6.putExtra("biaoshi", "xuqiu");
                startActivityForResult(intent6, 0);
                dialog.dismiss();
            }
        });
        re_item6.setVisibility(View.GONE);
        re_item6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(getActivity(), MomentsPublishActivity.class);
                intent6.putExtra("biaoshi", "xuqiu");
                startActivityForResult(intent6, 0);
                dialog.dismiss();
            }
        });
        re_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void updateUserInfo(Userful user) {
        shareRed = user.getShareRed();
        companyName = user.getCompany();
        friendsNumber = user.getFriendsNumber();
        String margan1 = TextUtils.isEmpty(user.getMargin1()) ? "0" : user.getMargin1();
        String margan2 = TextUtils.isEmpty(user.getMargin2()) ? "0" : user.getMargin2();
        String margan3 = TextUtils.isEmpty(user.getMargin3()) ? "0" : user.getMargin3();
        String margan4 = TextUtils.isEmpty(user.getMargin4()) ? "0" : user.getMargin4();
        if (!margan1.equals("0") || !margan2.equals("0") || !margan3.equals("0") || !margan4.equals("0")) {
            hasBao = true;
        }
        sex = user.getSex();
        resv6 = user.getResv6();
        uNation = user.getuNation();
        locationState = user.getLocationState();
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
    public void onDestroy() {
        super.onDestroy();
        if (dialog1 != null) {
            dialog1.dismiss();
        }
        if (dialog2 != null) {
            dialog2.dismiss();
        }
        if (dialog3 != null) {
            dialog3.dismiss();
        }

    }

    private void showLocationPermission() {
        LayoutInflater inflater3 = LayoutInflater.from(getActivity());
        RelativeLayout layout3 = (RelativeLayout) inflater3.inflate(R.layout.dialog_alert, null);
        final Dialog dialog3 = new AlertDialog.Builder(getActivity(), R.style.Dialog).create();
        dialog3.show();
        dialog3.getWindow().setContentView(layout3);
        dialog3.setCanceledOnTouchOutside(true);
        dialog3.setCancelable(true);
        TextView title = (TextView) layout3.findViewById(R.id.title_tv);
        Button btn_cancel = (Button) layout3.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) layout3.findViewById(R.id.btn_ok);
        title.setText("检测到您未打开定位权限，无法发布需求，是否现在开启定位权限？");
        btn_cancel.setText("取消");
        btn_ok.setText("确定");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog3.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog3.dismiss();
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    localIntent.setAction(Intent.ACTION_VIEW);
                    localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                }
                startActivity(localIntent);

            }
        });

    }
}
