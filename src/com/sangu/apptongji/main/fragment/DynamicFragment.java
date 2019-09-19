package com.sangu.apptongji.main.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.DynamicLinkDetailActivity;
import com.sangu.apptongji.main.activity.NewsDetailActivity;
import com.sangu.apptongji.main.activity.ProjectDynamicLinkDetailActivity;
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.moments.SocialMainAdapter;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Administrator on 2016/10/6.
 */

public class DynamicFragment extends Fragment implements View.OnClickListener {
    private String dType, dynamicType;
    private String newType;
    private XRecyclerView mRecyclerView;
    private List<JSONObject> articles = new ArrayList<>();
    private RadioButton radio_dynamic_type;
    private RadioButton radio_dynamic_ShHuo;
    private RadioButton radio_dynamic_XinWen;
    private RadioButton radio_dynamic_ShYe;
    private RadioButton radio_dynamic_YouHui;
    private RadioButton radio_dynamic_xuqiu;
    private ImageView iv_unread_zuobiao, iv_unread_shangye;
    private RelativeLayout re_edittext, rl_type, rl_none, rl_qiehuan;
    private TextView unread_number_xianshang, unread_number_shhuo, unread_number_zuobiao, unread_number_shangye, tv_none,
            unread_number_type, tv_tishi, tv_qiehuan;
    View v;
    //	private JSONArray datas = new JSONArray();
    private SocialMainAdapter adapter;
    private boolean isfirst = true;
    private boolean isShowHb = false;
    SharedPreferences mSharedPreferences = null;
    SharedPreferences.Editor editor = null;
    private int page, currentIndex, unread, unread2;
    String time1 = null, time2 = null, time3 = null, time4 = null, time5 = null, time6 = null, time7 = null, time8 = null, dynamicShareRed, dynamicGame;
    private boolean hasVisible = false;
    private boolean isVisible = true;
    private int size = 0;
    private String isHaveMargin = "0";
    private int isVip = 0;
    private String vipLevel = "1";
    private String profession;
    private Dialog mWeiboDialog;
    private String isClick = "yes";
    private String currentType = "0";//记录一下当前选择的是派单还是附近
    private String zbShareAuth = "0";

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (articles != null) {
            articles.clear();
            articles = null;
        }
        if (adapter != null) {
            adapter = null;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fx_activity_social_main, container, false);
        RequestQueue queue = MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        radio_dynamic_type = (RadioButton) v.findViewById(R.id.radio_dynamic_type);
        mRecyclerView = (XRecyclerView) v.findViewById(R.id.id_stickynavlayout_innerscrollview);
        re_edittext = (RelativeLayout) v.findViewById(R.id.re_edittext);
        rl_type = (RelativeLayout) v.findViewById(R.id.rl_type);
        rl_qiehuan = (RelativeLayout) v.findViewById(R.id.rl_qiehuan);
        rl_none = (RelativeLayout) v.findViewById(R.id.rl_none);
        page = 1;
        currentIndex = 4;
        unread = 0;
        unread2 = 0;
        dType = "05";
        newType = "05";
        dynamicType = "00";
        //   radio_dynamic_type.setText("公共圈");

        return v;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (getActivity() != null && !isVisible) {
            if (articles != null) {
                articles.clear();
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (articles == null || articles.size() == 0) {
                if (mRecyclerView != null) {
                    page = 1;
                    getData(dynamicType, page, dType, null);
                    adapter = new SocialMainAdapter(getActivity(), articles, dType, profession, "00", isHaveMargin + "|" + vipLevel + "|" + newType + "|" + zbShareAuth, isVip, "0");
                    mRecyclerView.setAdapter(adapter);
                }
            }
            if (getActivity() != null && DemoHelper.getInstance().isLoggedIn(getActivity())) {
                if (isfirst) {
                    dType = "05";
                    isfirst = false;
                }
                if (MainActivity.instance != null) {
                    if ("05".equals(dType)) {
                        MainActivity.instance.setRobot(1);
                    } else {
                        MainActivity.instance.setRobot(5);
                    }
                }
            } else if (MainTwoActivity.instance != null) {
                if (isfirst) {
                    dType = "05";
                    isfirst = false;
                }
                if ("05".equals(dType)) {
                    MainTwoActivity.instance.setRobot(1);
                } else {
                    MainTwoActivity.instance.setRobot(5);
                }
            }
            if (mRecyclerView != null) {
                isVisible = true;
            }
//            if ("05".equals(dType)&&hasVisible&&getActivity()!=null&&DemoHelper.getInstance().isLoggedIn(getActivity())) {
//                hasVisible = false;
//                if (profession == null || "".equals(profession) || profession.equalsIgnoreCase("null")) {
//                    LayoutInflater inflater1 = LayoutInflater.from(getActivity());
//                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
//                    final Dialog dialog1 = new AlertDialog.Builder(getActivity(),R.style.Dialog).create();
//                    dialog1.show();
//                    dialog1.getWindow().setContentView(layout1);
//                    dialog1.setCanceledOnTouchOutside(true);
//                    dialog1.setCancelable(true);
//                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
//                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
//                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
//                    TextView title = (TextView) layout1.findViewById(R.id.tv_title);
//                    title.setText("温馨提示");
//                    btnOK1.setText("现在编辑");
//                    btnCancel1.setText("稍后再说");
//                    title_tv1.setText("未编辑专业，无法接收派单提醒");
//                    btnCancel1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog1.dismiss();
//                        }
//                    });
//                    btnOK1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog1.dismiss();
//                            startActivity(new Intent(getActivity(), ProfileActivity.class));
//                        }
//                    });
//                    return;
//                } else {
//                    if (size > 0) {
//                        LayoutInflater inflater1 = LayoutInflater.from(getActivity());
//                        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
//                        final Dialog dialog1 = new AlertDialog.Builder(getActivity(),R.style.Dialog).create();
//                        dialog1.show();
//                        dialog1.getWindow().setContentView(layout1);
//                        dialog1.setCanceledOnTouchOutside(true);
//                        dialog1.setCancelable(true);
//                        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
//                        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
//                        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
//                        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
//                        title.setText("温馨提示");
//                        btnOK1.setText("确定");
//                        btnCancel1.setText("取消");
//                        title_tv1.setText("找到与您专业匹配的" + size + "条需求！");
//                        btnCancel1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog1.dismiss();
//                            }
//                        });
//                        btnOK1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog1.dismiss();
//                            }
//                        });
//                    }
//                }
//            }
        } else {
            isVisible = false;
        }
    }

    private void setAdapter1() {
        adapter.setOnItemClickListener(new SocialMainAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!DemoHelper.getInstance().isLoggedIn(getActivity())) {
                    Toast.makeText(getActivity(), "请您先登录！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (re_edittext.getVisibility() == View.VISIBLE) {
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    JSONObject object = articles.get(position - 1);
                    String type;
                    if (object.getString("fromUId") != null) {
                        type = "02";
                    } else {
                        type = "01";
                    }
                    String sID = object.getString("uLoginId");
                    String dynamic_seq = object.getString("dynamicSeq");
                    String createTime = object.getString("createTime");
                    Intent intent = new Intent(getActivity(), DynaDetaActivity.class);
                    intent.putExtra("sID", sID);
                    intent.putExtra("profession", profession);
                    intent.putExtra("dynamicSeq", dynamic_seq);
                    intent.putExtra("createTime", createTime);
                    intent.putExtra("dType", dType);
                    intent.putExtra("type", type);
                    intent.putExtra("type2", "00");
                    startActivityForResult(intent, 0);
                }
            }
        });
    }

    private void setAdapter2() {
        adapter.setOnItem2ClickListener(new SocialMainAdapter.MyItem2ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!DemoHelper.getInstance().isLoggedIn(getActivity())) {
                    Toast.makeText(getActivity(), "请您先登录！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (re_edittext.getVisibility() == View.VISIBLE) {
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    JSONObject object = articles.get(position - 1);
                    String type;
                    if (object.getString("fromUId") != null) {
                        type = "02";
                    } else {
                        type = "01";
                    }
                    String sID = object.getString("uLoginId");
                    String dynamic_seq = object.getString("dynamicSeq");
                    String createTime = object.getString("createTime");
                    Intent intent = new Intent(getActivity(), DynaDetaActivity.class);
                    intent.putExtra("sID", sID);
                    intent.putExtra("profession", profession);
                    intent.putExtra("dynamicSeq", dynamic_seq);
                    intent.putExtra("createTime", createTime);
                    intent.putExtra("dType", dType);
                    intent.putExtra("type", type);
                    startActivityForResult(intent, 0);
                }
            }
        });
    }

    private void setAdapter6() {
        adapter.setOnItem5ClickListener(new SocialMainAdapter.MyItem5ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("chen", "点击进入");
                if (!DemoHelper.getInstance().isLoggedIn(getActivity())) {
                    Toast.makeText(getActivity(), "请您先登录！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (re_edittext.getVisibility() == View.VISIBLE) {
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    JSONObject object = articles.get(position - 1);

                    String contentUrl = object.getString("image2");
                    String sID = object.getString("uLoginId");
                    String dynamic_seq = object.getString("dynamicSeq");
                    String createTime = object.getString("createTime");
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("content", contentUrl);
                    intent.putExtra("sID", sID);
                    intent.putExtra("dynamicSeq", dynamic_seq);
                    intent.putExtra("createTime", createTime);
                    startActivityForResult(intent, 6);
                }
            }
        });
    }

    private void setAdapter3() {

        adapter.setOnItem3ClickListener(new SocialMainAdapter.MyItem3ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("chen", "点击进入");
                if (!DemoHelper.getInstance().isLoggedIn(getActivity())) {
                    Toast.makeText(getActivity(), "请您先登录！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (re_edittext.getVisibility() == View.VISIBLE) {
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    JSONObject object = articles.get(position - 1);
                    String type;
                    if (object.getString("fromUId") != null) {
                        type = "02";
                    } else {
                        type = "01";
                    }
                    String sID = object.getString("uLoginId");
                    String dynamic_seq = object.getString("dynamicSeq");
                    String createTime = object.getString("createTime");
                    String task_jurisdiction = object.getString("task_jurisdiction");

                    //长度大于8的证明是链接 点击跳转网页  如果点击下方浏览一系列按钮 就跳到详情
                    if (task_jurisdiction != null && task_jurisdiction.length() > 8) {

                        Intent intent = new Intent(getActivity(), ProjectDynamicLinkDetailActivity.class);

                        intent.putExtra("orderType", object.getString("orderType"));
                        intent.putExtra("lat", object.getString("lat"));
                        intent.putExtra("lng", object.getString("lng"));
                        intent.putExtra("salePrice", object.getString("salePrice"));
                        intent.putExtra("price", object.getString("price"));
                        intent.putExtra("sID", sID);
                        intent.putExtra("profession", profession);
                        intent.putExtra("dynamicSeq", dynamic_seq);
                        intent.putExtra("createTime", createTime);
                        intent.putExtra("task_jurisdiction", task_jurisdiction);
                        intent.putExtra("task_label", object.getString("task_label"));
                        intent.putExtra("dType", dType);
                        intent.putExtra("type", type);
                        startActivityForResult(intent, 0);

                    } else {

                        Intent intent = new Intent(getActivity(), DynaDetaActivity.class);
                        intent.putExtra("sID", sID);
                        intent.putExtra("profession", profession);
                        intent.putExtra("dynamicSeq", dynamic_seq);
                        intent.putExtra("createTime", createTime);
                        intent.putExtra("dType", dType);
                        intent.putExtra("type", type);
                        startActivityForResult(intent, 0);

                    }

                }
            }
        });
    }

    private void setAdapter4() {
        adapter.setOnItem4ClickListener(new SocialMainAdapter.MyItem4ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (!DemoHelper.getInstance().isLoggedIn(getActivity())) {
                    Toast.makeText(getActivity(), "请您先登录！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (re_edittext.getVisibility() == View.VISIBLE) {
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } else {

                    JSONObject object = articles.get(position - 1);

                    if (object.getString("dynamicAuth").equals("0")) {

                        //洽谈中 弹出提示框
                        LayoutInflater inflaterD5 = LayoutInflater.from(getActivity());
                        LinearLayout layout5 = (LinearLayout) inflaterD5.inflate(R.layout.dialog_vipchat_alert, null);
                        final Dialog dialog5 = new AlertDialog.Builder(getActivity(), R.style.Dialog).create();
                        dialog5.show();
                        dialog5.getWindow().setContentView(layout5);
                        WindowManager.LayoutParams params = dialog5.getWindow().getAttributes();
                        Display display = getActivity().getWindowManager().getDefaultDisplay();
                        params.width = (int) (display.getWidth() * 0.7); //使用这种方式更改了dialog的框宽
                        dialog5.getWindow().setAttributes(params);
                        dialog5.setCancelable(true);
                        dialog5.setCanceledOnTouchOutside(true);

                    } else {

                        //判断是招标信息还是普通动态
                        String redImage = object.getString("redImage");

                        if (redImage != null && redImage.length() > 7) {

                            Intent intent = new Intent(getActivity(), DynamicLinkDetailActivity.class);

                            intent.putExtra("redImage", redImage);

                            startActivityForResult(intent, 0);

                        } else {

                            String type;
                            if (object.getString("fromUId") != null) {
                                type = "02";
                            } else {
                                type = "01";
                            }
                            String sID = object.getString("uLoginId");
                            String dynamic_seq = object.getString("dynamicSeq");
                            String createTime = object.getString("createTime");
                            Intent intent = new Intent(getActivity(), DynaDetaActivity.class);
                            intent.putExtra("sID", sID);
                            intent.putExtra("profession", profession);
                            intent.putExtra("dynamicSeq", dynamic_seq);
                            intent.putExtra("createTime", createTime);
                            intent.putExtra("dType", dType);
                            intent.putExtra("type", type);
                            intent.putExtra("type2", "00");
                            startActivityForResult(intent, 0);

                        }

                    }

                }
            }
        });
    }

    public void onRefresh() {
        if (mRecyclerView != null) {
            Bundle bundle = getArguments();
            String content = bundle.getString("content");
            String type = bundle.getString("type");

            if (content == null || "".equals(content) || "0".equals(content)) {
                mRecyclerView.refresh(false);
            } else {
                Log.e("MainAc,dyna", "刷新dyna" + content);
                getData(dynamicType, 1, dType, content);
            }

            adapter = new SocialMainAdapter(getActivity(), articles, dType, profession, "00", isHaveMargin + "|" + vipLevel + "|" + newType + "|" + zbShareAuth, isVip, "0");
            mRecyclerView.setAdapter(adapter);
            if ("01".equals(dType)) {
                setAdapter1();
            } else if ("02".equals(dType)) {
                setAdapter2();
            } else if ("03".equals(dType) || "04".equals(dType)) {
                setAdapter3();
            } else if ("05".equals(dType)) {
                setAdapter4();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hasVisible = true;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                adapter.hideCommentEditText();
                return false;
            }
        });

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                if (articles == null || articles.size() == 0) {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
                    getData(dynamicType, page, dType, null);
                    adapter = new SocialMainAdapter(getActivity(), articles, dType, profession, "00", isHaveMargin + "|" + vipLevel + "|" + newType + "|" + zbShareAuth, isVip, "0");
                    mRecyclerView.setAdapter(adapter);
                    if ("01".equals(dType)) {
                        setAdapter1();
                    } else if ("02".equals(dType)) {
                        setAdapter2();
                    } else if ("03".equals(dType)) {
                        setAdapter3();
                    } else if ("05".equals(dType)) {
                        setAdapter4();
                    }
                } else {
                    getData(dynamicType, page, dType, null);
                }
            }

            @Override
            public void onLoadMore() {
                page = page + 1;
                getData(dynamicType, page, dType, null);
            }
        });
        initView();
    }

    private void updateClick() {
        String timel1 = getNowTime();
        mSharedPreferences = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        if ("00".equals(dynamicType)) {
            if (currentIndex == 1) {
                editor.putString("time1", timel1);
            } else if (currentIndex == 2) {
                editor.putString("time2", timel1);
            } else if (currentIndex == 3) {
                editor.putString("time3", timel1);
            } else if (currentIndex == 4) {
                editor.putString("time4", timel1);
            }
        } else {
            if (currentIndex == 1) {
                editor.putString("time5", timel1);
            } else if (currentIndex == 2) {
                editor.putString("time6", timel1);
            } else if (currentIndex == 3) {
                editor.putString("time7", timel1);
            } else if (currentIndex == 4) {
                editor.putString("time8", timel1);
            }
        }
        editor.commit();
    }

    @Override
    public void onClick(View v) {

        SharedPreferences sp = getActivity().getSharedPreferences("sangu_dynamicClick_info", Context.MODE_PRIVATE);

        switch (v.getId()) {
            case R.id.rl_qiehuan:
//                if (re_edittext.getVisibility()==View.VISIBLE){
//                    re_edittext.setVisibility(View.INVISIBLE);
//                    ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//                JCVideoPlayer.releaseAllVideos();
//                page = 1;
//                if ("好友圈".equals(radio_dynamic_type.getText())){
//                    radio_dynamic_type.setChecked(true);
//                    radio_dynamic_type.setText("公共圈");
//                    dynamicType = "00";
//                    rl_none.setVisibility(View.INVISIBLE);
//                    updateClick();
//                    if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
//                        queryUpdatedyna();
//                        queryAllUnread();
//                    }
//                    mRecyclerView.refresh(false);
//                }else {
//                    radio_dynamic_type.setChecked(true);
//                    radio_dynamic_type.setText("好友圈");
//                    dynamicType = "01";
//                    updateClick();
//                    if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
//                        queryUpdatedyna();
//                        queryAllUnread();
//                    }
//                    mRecyclerView.refresh(false);
//                }

                if (sp != null) {

                    SharedPreferences.Editor editor1 = sp.edit();

                    editor1.putString("dynamicType", "06");
                    editor1.commit();

                }
                JCVideoPlayer.releaseAllVideos();
                if (re_edittext.getVisibility() == View.VISIBLE) {
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                    MainActivity.instance.setRobot(5);
                } else if (MainTwoActivity.instance != null) {
                    MainTwoActivity.instance.setRobot(5);
                }
                page = 1;

                currentIndex = 1;

                radio_dynamic_ShHuo.setChecked(false);
                radio_dynamic_XinWen.setChecked(false);
                radio_dynamic_ShYe.setChecked(false);
                radio_dynamic_YouHui.setChecked(false);
                radio_dynamic_xuqiu.setChecked(false);
                //  dType = "01";
                dType = "06";
                newType = "06";
                mRecyclerView.refresh(false);
                isClick = "yes";

                tv_qiehuan.setTextColor(Color.parseColor("#FF6347"));
                if (articles != null && articles.size() > 0) {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
                }

                break;
            case R.id.radio_dynamic_ShHuo:

                if (sp != null) {
                    SharedPreferences.Editor editor1 = sp.edit();

                    editor1.putString("dynamicType", "01");
                    editor1.commit();

                }

                currentType = "1";

                JCVideoPlayer.releaseAllVideos();
                if (re_edittext.getVisibility() == View.VISIBLE) {
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                    MainActivity.instance.setRobot(1);
                } else if (MainTwoActivity.instance != null) {
                    MainTwoActivity.instance.setRobot(1);
                }
                page = 1;
                if (currentIndex != 1 && unread_number_shhuo.getVisibility() == View.VISIBLE) {
                    if (dynamicType.equals("00")) {
                        if (unread > 0) {
                            unread--;
                        }
                    } else {
                        if (unread2 > 0) {
                            unread2--;
                        }
                    }
                }
                if (unread == 0 && unread2 == 0 && DemoHelper.getInstance().isLoggedIn(getActivity())) {
                    unread_number_type.setVisibility(View.INVISIBLE);
                    MainActivity.instance.hidUnread();
                }
                currentIndex = 1;
                unread_number_shhuo.setVisibility(View.INVISIBLE);

                radio_dynamic_ShHuo.setChecked(true);
                radio_dynamic_XinWen.setChecked(false);
                radio_dynamic_ShYe.setChecked(false);
                radio_dynamic_YouHui.setChecked(false);
                radio_dynamic_xuqiu.setChecked(false);
                //  dType = "01";
                dType = "05";
                newType = "01";
                mRecyclerView.refresh(false);
                isClick = "yes";
                tv_qiehuan.setTextColor(Color.parseColor("#ffbebebe"));
                if (articles != null && articles.size() > 0) {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
                }
//                adapter = new SocialMainAdapter(getActivity(), articles, dType, profession, "00",isHaveMargin);
//                mRecyclerView.setAdapter(adapter);
//                setAdapter4();

                break;
            case R.id.radio_dynamic_XinWen:

                if (sp != null) {

                    SharedPreferences.Editor editor1 = sp.edit();
                    editor1.putString("dynamicType", "02");
                    editor1.commit();

                }

                JCVideoPlayer.releaseAllVideos();
                if (re_edittext.getVisibility() == View.VISIBLE) {
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                    MainActivity.instance.setRobot(1);
                } else if (MainTwoActivity.instance != null) {
                    MainTwoActivity.instance.setRobot(1);
                }
                page = 1;
                if (currentIndex != 2 && unread_number_zuobiao.getVisibility() == View.VISIBLE) {
                    if (dynamicType.equals("00")) {
                        if (unread > 0) {
                            unread--;
                        }
                    } else {
                        if (unread2 > 0) {
                            unread2--;
                        }
                    }
                }
                if (unread == 0 && unread2 == 0 && DemoHelper.getInstance().isLoggedIn(getActivity())) {
                    unread_number_type.setVisibility(View.INVISIBLE);
                    MainActivity.instance.hidUnread();
                }
//                if (iv_unread_zuobiao.getVisibility()==View.VISIBLE){
//                    isShowHb = true;
//                }else {
//                    isShowHb = false;
//                }

                isShowHb = false;

                currentIndex = 2;
                unread_number_zuobiao.setVisibility(View.INVISIBLE);
                iv_unread_zuobiao.setVisibility(View.INVISIBLE);
                String timel2 = getNowTime();
                mSharedPreferences = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
                editor = mSharedPreferences.edit();
                if ("00".equals(dynamicType)) {
                    editor.putString("time1", timel2);
                } else {
                    editor.putString("time6", timel2);
                }
                editor.commit();
                radio_dynamic_ShHuo.setChecked(false);
                radio_dynamic_XinWen.setChecked(true);
                radio_dynamic_ShYe.setChecked(false);
                radio_dynamic_YouHui.setChecked(false);
                radio_dynamic_xuqiu.setChecked(false);
                //   dType = "02";
                dType = "05";
                newType = "02";
                mRecyclerView.refresh(false);
                isClick = "yes";
                tv_qiehuan.setTextColor(Color.parseColor("#ffbebebe"));
                if (articles != null && articles.size() > 0) {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
                }
                break;
            case R.id.radio_dynamic_ShYe:
                if (sp != null) {

                    SharedPreferences.Editor editor1 = sp.edit();

                    editor1.putString("dynamicType", "03");
                    editor1.commit();

                }
                JCVideoPlayer.releaseAllVideos();
                if (re_edittext.getVisibility() == View.VISIBLE) {
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (DemoHelper.getInstance().isLoggedIn(getActivity()) || MainActivity.instance != null) {
                    MainActivity.instance.setRobot(1);
                } else if (MainTwoActivity.instance != null) {
                    MainTwoActivity.instance.setRobot(1);
                }
                page = 1;
                if (currentIndex != 3 && unread_number_shangye.getVisibility() == View.VISIBLE) {
                    if (dynamicType.equals("00")) {
                        if (unread > 0) {
                            unread--;
                        }
                    } else {
                        if (unread2 > 0) {
                            unread2--;
                        }
                    }
                }
                if (unread == 0 && unread2 == 0 && DemoHelper.getInstance().isLoggedIn(getActivity())) {
                    unread_number_type.setVisibility(View.INVISIBLE);
                    MainActivity.instance.hidUnread();
                }
//                if (iv_unread_shangye.getVisibility()==View.VISIBLE){
//                    isShowHb = true;
//                }else {
//                    isShowHb = false;
//                }

                isShowHb = false;

                currentIndex = 3;
                unread_number_shangye.setVisibility(View.INVISIBLE);
                iv_unread_shangye.setVisibility(View.INVISIBLE);
                String timel3 = getNowTime();
                mSharedPreferences = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
                editor = mSharedPreferences.edit();
                if ("00".equals(dynamicType)) {
                    editor.putString("time3", timel3);
                } else {
                    editor.putString("time7", timel3);
                }
                editor.commit();
                radio_dynamic_ShHuo.setChecked(false);
                radio_dynamic_XinWen.setChecked(false);
                radio_dynamic_ShYe.setChecked(true);
                radio_dynamic_YouHui.setChecked(false);
                radio_dynamic_xuqiu.setChecked(false);
                // dType = "03";
                dType = "05";
                newType = "03";
                mRecyclerView.refresh(false);
                isClick = "yes";
                tv_qiehuan.setTextColor(Color.parseColor("#ffbebebe"));
                if (articles != null && articles.size() > 0) {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
                }

                break;
            case R.id.radio_dynamic_YouHui:
                JCVideoPlayer.releaseAllVideos();
                if (re_edittext.getVisibility() == View.VISIBLE) {
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                    MainActivity.instance.setRobot(5);
                } else if (MainTwoActivity.instance != null) {
                    MainTwoActivity.instance.setRobot(5);
                }
                page = 1;
                radio_dynamic_ShHuo.setChecked(false);
                radio_dynamic_XinWen.setChecked(false);
                radio_dynamic_ShYe.setChecked(false);
                radio_dynamic_xuqiu.setChecked(false);
                radio_dynamic_YouHui.setChecked(true);
                dType = "04";
                mRecyclerView.refresh(false);
                adapter = new SocialMainAdapter(getActivity(), articles, dType, profession, "00", isHaveMargin + "|" + vipLevel + "|" + newType + "|" + zbShareAuth, isVip, "0");
                mRecyclerView.setAdapter(adapter);
                setAdapter3();
                break;
            case R.id.radio_dynamic_xuqiu:
                if (sp != null) {
                    SharedPreferences.Editor editor1 = sp.edit();

                    editor1.putString("dynamicType", "05");
                    editor1.commit();

                }

                currentType = "0";

                JCVideoPlayer.releaseAllVideos();
                if (re_edittext.getVisibility() == View.VISIBLE) {
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (DemoHelper.getInstance().isLoggedIn(getActivity()) && MainActivity.instance != null) {
                    MainActivity.instance.setRobot(1);
                } else if (MainTwoActivity.instance != null) {
                    MainTwoActivity.instance.setRobot(1);
                }
                page = 1;
                if (currentIndex != 4 && unread_number_xianshang.getVisibility() == View.VISIBLE) {
                    if (dynamicType.equals("00")) {
                        if (unread > 0) {
                            unread--;
                        }
                    } else {
                        if (unread2 > 0) {
                            unread2--;
                        }
                    }
                }
                if (unread == 0 && unread2 == 0 && DemoHelper.getInstance().isLoggedIn(getActivity())) {
                    unread_number_type.setVisibility(View.INVISIBLE);
                    MainActivity.instance.hidUnread();
                }
                currentIndex = 4;
                unread_number_xianshang.setVisibility(View.INVISIBLE);
                String timel4 = getNowTime();
                mSharedPreferences = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
                editor = mSharedPreferences.edit();
                if ("00".equals(dynamicType)) {
                    editor.putString("time4", timel4);
                } else {
                    editor.putString("time8", timel4);
                }
                editor.commit();
                radio_dynamic_ShHuo.setChecked(false);
                radio_dynamic_XinWen.setChecked(false);
                radio_dynamic_ShYe.setChecked(false);
                radio_dynamic_YouHui.setChecked(false);
                radio_dynamic_xuqiu.setChecked(true);
                dType = "05";
                newType = "05";
                mRecyclerView.refresh(false);
                isClick = "yes";
                tv_qiehuan.setTextColor(Color.parseColor("#ffbebebe"));
                if (articles != null && articles.size() > 0) {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
                }
                break;
        }
    }

    private void initView() {
        radio_dynamic_ShHuo = (RadioButton) v.findViewById(R.id.radio_dynamic_ShHuo);
        radio_dynamic_XinWen = (RadioButton) v.findViewById(R.id.radio_dynamic_XinWen);
        radio_dynamic_ShYe = (RadioButton) v.findViewById(R.id.radio_dynamic_ShYe);
        radio_dynamic_YouHui = (RadioButton) v.findViewById(R.id.radio_dynamic_YouHui);
        radio_dynamic_xuqiu = (RadioButton) v.findViewById(R.id.radio_dynamic_xuqiu);
        tv_none = (TextView) v.findViewById(R.id.tv_none);
        unread_number_type = (TextView) v.findViewById(R.id.unread_number_type);
        tv_tishi = (TextView) v.findViewById(R.id.tv_tishi);
        iv_unread_zuobiao = (ImageView) v.findViewById(R.id.iv_unread_zuobiao);
        iv_unread_shangye = (ImageView) v.findViewById(R.id.iv_unread_shangye);
        unread_number_xianshang = (TextView) v.findViewById(R.id.unread_number_xianshang);
        unread_number_shangye = (TextView) v.findViewById(R.id.unread_number_shangye);
        unread_number_shhuo = (TextView) v.findViewById(R.id.unread_number_shhuo);
        unread_number_zuobiao = (TextView) v.findViewById(R.id.unread_number_zuobiao);
        tv_qiehuan = (TextView) v.findViewById(R.id.tv_qiehuan);
        rl_type.setOnClickListener(this);
        rl_qiehuan.setOnClickListener(this);
        radio_dynamic_ShHuo.setOnClickListener(this);
        radio_dynamic_XinWen.setOnClickListener(this);
        radio_dynamic_ShYe.setOnClickListener(this);
        radio_dynamic_YouHui.setOnClickListener(this);
        radio_dynamic_xuqiu.setOnClickListener(this);
        adapter = new SocialMainAdapter(getActivity(), articles, dType, profession, "00", isHaveMargin + "|" + vipLevel + "|" + newType + "|" + zbShareAuth, isVip, "0");
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                adapter.hideCommentEditText();
                return false;
            }
        });
        mRecyclerView.refresh(false);
        setAdapter4();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  mRecyclerView.refresh(false);
    }

    private void getData(final String dynamicType, final int page_num, final String dtType, final String selectCon) {

        List<Param> params = new ArrayList<>();
        params.add(new Param("currentPage", page_num + ""));
        params.add(new Param("dType", dtType));
        SharedPreferences sp = getActivity().getSharedPreferences("sangu_dynamicClick_info", Context.MODE_PRIVATE);
        if ("06".equals(dtType)) {
            params.add(new Param("image3", "正事多"));
        }

//        if (sp != null) {
//
//            String dType2 = sp.getString("dynamicType", "00");
//
//            if (dType2.equals("01")){
//
//                String lng = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) ? "0" : DemoApplication.getInstance().getCurrentLng();
//                String lat = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) ? "0" : DemoApplication.getInstance().getCurrentLat();
//
//                if (lng.equals("0") || lat.equals("0")){
//                    Toast.makeText(getActivity(),"位置信息有误,无法获取附近派单,请检查定位或重启",Toast.LENGTH_SHORT).show();
//                }else {
//
//                    params.add(new Param("lat", lat));
//                    params.add(new Param("log", lng));
//
//                }
//            }
//        }


        Bundle bundle = getArguments();
        String type = bundle.getString("type");

        if (type != null && type.equals("专业")) {

            params.add(new Param("identification", "1"));

        } else if (type != null && type.equals("距离")) {

            String lng = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) ? "0" : DemoApplication.getInstance().getCurrentLng();
            String lat = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) ? "0" : DemoApplication.getInstance().getCurrentLat();

            if (lng.equals("0") || lat.equals("0")) {

            } else {

                params.add(new Param("lat", lat));
                params.add(new Param("log", lng));

            }

        } else {

            if (selectCon != null) {
                params.add(new Param("selectContent", selectCon));
            }

        }

//        if (isShowHb){
//            params.add(new Param("shareRed","1"));
//            isShowHb = false;
//        }
        if ("01".equals(dynamicType)) {
            params.add(new Param("dynamicType", dynamicType));
        }

        if (newType.equals("03")) {

            params.add(new Param("identification", "1"));

        } else if (newType.equals("01")) {

            String lng = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) ? "0" : DemoApplication.getInstance().getCurrentLng();
            String lat = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) ? "0" : DemoApplication.getInstance().getCurrentLat();

            if (lng.equals("0") || lat.equals("0")) {
                Toast.makeText(getActivity(), "位置信息有误,无法获取附近派单,请检查定位或重启", Toast.LENGTH_SHORT).show();
            } else {

                params.add(new Param("lat", lat));
                params.add(new Param("log", lng));

            }

        } else if (newType.equals("02")) {

            //查看招标信息
            params.add(new Param("newType", "1"));

        }


        if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
            params.add(new Param("loginId", DemoHelper.getInstance().getCurrentUsernName()));
        }
        /*for (Param param : params) {
            Log.d("param.getKey()----->>", param.getKey());
              Log.d("param.getValue()----->>", param.getValue());
        }*/
        OkHttpManager.getInstance().post(params, FXConstant.URL_PUBLISH_QUERY, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                String loginId = DemoHelper.getInstance().getCurrentUsernName();
                if (jsonObject == null) {
                    tv_none.setVisibility(View.VISIBLE);
                    rl_none.setVisibility(View.VISIBLE);
                    if (page_num == 1) {
                        mRecyclerView.refreshComplete();
                    } else {
                        mRecyclerView.loadMoreComplete();
                    }
                    return;
                }
                JSONArray friendList = jsonObject.getJSONArray("friendList");
                JSONArray users_temp = jsonObject.getJSONArray("clist");

                if (dtType.equals("06")) {

                    users_temp = jsonObject.getJSONArray("code");

                } else {

                    users_temp = jsonObject.getJSONArray("clist");

                }

                if (page_num == 1) {
                    mRecyclerView.refreshComplete();
                } else {
                    mRecyclerView.loadMoreComplete();
                }
                if (users_temp == null || users_temp.size() == 0 || users_temp.toString().equals("[]")) {
                    if (page_num == 1) {
                        if (articles != null) {
                            articles.clear();
                        }
                    }
                    if ("01".equals(dynamicType)) {
                        if (DemoHelper.getInstance().getContactList() == null || DemoHelper.getInstance().getContactList().size() == 0) {
                            if (!DemoHelper.getInstance().isLoggedIn(getActivity())) {
                                tv_tishi.setText("您还没有登陆,登陆之后才能看好友动态哦~");
                            } else {
                                tv_tishi.setText("您还没有好友,赶快去添加好友吧~");
                            }
                            rl_none.setVisibility(View.VISIBLE);
                        } else {
                            rl_none.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        rl_none.setVisibility(View.INVISIBLE);
                    }
                    if (page_num == 1) {
                        tv_none.setVisibility(View.INVISIBLE);
                    }
                } else {
                    tv_none.setVisibility(View.INVISIBLE);
                    rl_none.setVisibility(View.INVISIBLE);
                }
                //因为确认现在四个动态都有20条以上数据 所以全部从第二页判断是否还有更多数据
                if (users_temp != null && users_temp.size() < 20 && page_num != 1) {

                    mRecyclerView.setNoMore(true);
                }

                if (users_temp != null) {
                    if (page_num == 1) {
                        //	datas = users_temp;
                        if (articles != null) {
                            articles.clear();
                        } else {
                            articles = new ArrayList<JSONObject>();
                        }

                        if (dtType.equals("06")) {

                            JSONArray newsTopArr = jsonObject.getJSONArray("top");

                            if (newsTopArr != null && newsTopArr.size() > 0) {

                                for (int i = 0; i < newsTopArr.size(); i++) {
                                    JSONObject json = newsTopArr.getJSONObject(i);
                                    if (articles != null) {
                                        articles.add(json);
                                    }

                                }

                            }

                        }

                        for (int i = 0; i < users_temp.size(); i++) {
                            JSONObject json = users_temp.getJSONObject(i);
                            String authType = TextUtils.isEmpty(json.getString("authType")) ? "" : json.getString("authType");
                            String uLoginId = json.getString("uLoginId");
                            String firstId = json.getString("firstUId");
                            if ("05".equals(dtType)) {
                                if (firstId == null || firstId.equalsIgnoreCase("null")) {
                                    if ("01".equals(authType)) {
                                        if (articles != null) {
                                            articles.add(json);
                                        }
                                    } else if ("03".equals(authType)) {//authType=02\03
                                        if (articles != null) {
                                            articles.add(json);
                                        }
                                    } else if ("02".equals(authType)) {
                                        if (uLoginId.equals(loginId)) {
                                            String sID = json.getString("uId");
                                            if (articles != null) {
                                                articles.add(json);
                                            }
                                        } else {
                                            if (friendList != null) {
                                                for (int j = 0; j < friendList.size(); j++) {
                                                    JSONObject json1 = friendList.getJSONObject(j);
                                                    String id = json1.getString("fId");
                                                    if (id.equals(uLoginId)) {
                                                        String sID = json.getString("uId");
                                                        if (articles != null) {
                                                            articles.add(json);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                if ("01".equals(authType)) {
                                    if (articles != null) {
                                        articles.add(json);
                                    }
                                } else if ("03".equals(authType)) {//authType=02\03
                                    if (articles != null) {
                                        articles.add(json);
                                    }
                                } else if ("02".equals(authType)) {
                                    if (uLoginId.equals(loginId)) {
                                        String sID = json.getString("uId");
                                        if (articles != null) {
                                            articles.add(json);
                                        }
                                    } else {
                                        if (friendList != null) {
                                            for (int j = 0; j < friendList.size(); j++) {
                                                JSONObject json1 = friendList.getJSONObject(j);
                                                String id = json1.getString("fId");
                                                if (id.equals(uLoginId)) {
                                                    String sID = json.getString("uId");
                                                    if (articles != null) {
                                                        articles.add(json);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {

                                    if (articles != null) {
                                        articles.add(json);
                                    }
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < users_temp.size(); i++) {
                            JSONObject json = users_temp.getJSONObject(i);
                            String authType = json.getString("authType");
                            String uLoginId = json.getString("uLoginId");
                            String firstId = json.getString("firstUId");
                            if ("05".equals(dtType)) {
                                if (firstId == null || firstId.equalsIgnoreCase("null")) {
                                    if ("01".equals(authType)) {
                                        String sID = json.getString("uId");
                                        if (articles != null) {
                                            articles.add(json);
                                        }
                                    } else if ("03".equals(authType)) {//authType=02\03
                                        if (articles != null) {
                                            articles.add(json);
                                        }
                                    } else if ("02".equals(authType)) {
                                        if (uLoginId.equals(loginId)) {
                                            String sID = json.getString("uId");
                                            if (articles != null) {
                                                articles.add(json);
                                            }
                                        } else {
                                            if (friendList != null) {
                                                for (int j = 0; j < friendList.size(); j++) {
                                                    JSONObject json1 = friendList.getJSONObject(j);
                                                    String id = json1.getString("fId");
                                                    if (id.equals(uLoginId)) {
                                                        String sID = json.getString("uId");
                                                        if (articles != null) {
                                                            articles.add(json);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                if ("01".equals(authType)) {
                                    String sID = json.getString("uId");
                                    if (articles != null) {
                                        articles.add(json);
                                    }
                                } else if ("03".equals(authType)) {//authType=02\03
                                    if (articles != null) {
                                        articles.add(json);
                                    }
                                } else if ("02".equals(authType)) {
                                    if (uLoginId.equals(loginId)) {
                                        String sID = json.getString("uId");
                                        if (articles != null) {
                                            articles.add(json);
                                        }
                                    } else {
                                        if (friendList != null) {
                                            for (int j = 0; j < friendList.size(); j++) {
                                                JSONObject json1 = friendList.getJSONObject(j);
                                                String id = json1.getString("fId");
                                                if (id.equals(uLoginId)) {
                                                    String sID = json.getString("uId");
                                                    if (articles != null) {
                                                        articles.add(json);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (articles != null) {
                                        articles.add(json);
                                    }
                                }
                            }
                        }
                    }
                    if ("05".equals(dtType)) {

                        if (page_num == 1 && isClick.equals("yes")) {
                            profession = jsonObject.getString("profession");
                            size = jsonObject.getIntValue("size");
                            isHaveMargin = jsonObject.getString("margin");
                            isVip = jsonObject.getIntValue("vip");
                            vipLevel = jsonObject.getString("vipLevel");
                            zbShareAuth = jsonObject.getString("zbShareAuth");


                            isClick = "no";

                            //因为vip等级是后来加的，暂时先跟质保放一个参数 不然修改adapter牵涉的地方有点多
                            adapter = new SocialMainAdapter(getActivity(), articles, dType, profession, "00", isHaveMargin + "|" + vipLevel + "|" + newType + "|" + zbShareAuth, isVip, "0");
                            mRecyclerView.setAdapter(adapter);
                            setAdapter4();

                        } else {

                            profession = jsonObject.getString("profession");
                            size = jsonObject.getIntValue("size");
                            isHaveMargin = jsonObject.getString("margin");
                            isVip = jsonObject.getIntValue("vip");
                            vipLevel = jsonObject.getString("vipLevel");

                            adapter.notifyDataSetChanged();
                        }

                    } else if ("03".equals(dtType)) {

                        if (page_num == 1 && isClick.equals("yes")) {

                            isClick = "no";
                            adapter = new SocialMainAdapter(getActivity(), articles, dType, profession, "00", isHaveMargin + "|" + vipLevel + "|" + newType + "|" + zbShareAuth, isVip, "0");
                            mRecyclerView.setAdapter(adapter);
                            setAdapter3();

                        } else {
                            profession = null;
                            adapter.notifyDataSetChanged();
                        }


                    } else if ("02".equals(dtType)) {

                        if (page_num == 1 && isClick.equals("yes")) {

                            isClick = "no";
                            adapter = new SocialMainAdapter(getActivity(), articles, dType, profession, "00", isHaveMargin + "|" + vipLevel + "|" + newType + "|" + zbShareAuth, isVip, "0");
                            mRecyclerView.setAdapter(adapter);
                            setAdapter2();

                        } else {
                            profession = null;
                            adapter.notifyDataSetChanged();
                        }

                    } else if ("06".equals(dtType)) {

                        if (page_num == 1 && isClick.equals("yes")) {

                            isClick = "no";
                            adapter = new SocialMainAdapter(getActivity(), articles, dType, profession, "00", isHaveMargin + "|" + vipLevel + "|" + newType + "|" + zbShareAuth, isVip, "0");
                            mRecyclerView.setAdapter(adapter);
                            setAdapter6();

                        } else {
                            profession = null;
                            adapter.notifyDataSetChanged();
                        }


                    } else {

                        profession = null;
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                if (page_num == 1) {
                    mRecyclerView.refreshComplete();
                } else {
                    mRecyclerView.loadMoreComplete();
                }
            }
        });
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    private String getNowTime2() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    @Override
    public void onResume() {
        super.onResume();
        //   updateClick();
        if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
            queryUpdatedyna();
            queryAllUnread();
        }
    }

    private void queryAllUnread() {
        String time;
        if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
            time = DemoApplication.getInstance().getCurrentUser().getResv3();
            time = dataOne(time);
        } else {
            time = "20170910141425";
        }
        if (time == null || "".equals(time)) {
            time = "20170910141425";
        }
        SharedPreferences sp = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
        final String timec5 = sp.getString("time5", time);
        final String timec6 = sp.getString("time6", time);
        final String timec7 = sp.getString("time7", time);
        final String timec8 = sp.getString("time8", time);
        String url = FXConstant.URL_QUERY_UNREADUSER + DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    if (s == null || "".equals(s)) {
                        Log.e("dingdanac", "offSendOrderCount为空");
                    } else {
                        org.json.JSONObject object = new org.json.JSONObject(s);
                        time5 = object.optString("fLifeDynamic");
                        time6 = object.optString("fLocationDynamic");
                        time7 = object.optString("fBusinessDynamic");
                        time8 = object.optString("fSendOrderDynamic");
                        if (time5 == null || "".equals(time5) || time5.equalsIgnoreCase("null")) {
                            time5 = "0";
                        }
                        if (time6 == null || "".equals(time6) || time6.equalsIgnoreCase("null")) {
                            time6 = "0";
                        }
                        if (time7 == null || "".equals(time7) || time7.equalsIgnoreCase("null")) {
                            time7 = "0";
                        }
                        if (time8 == null || "".equals(time8) || time8.equalsIgnoreCase("null")) {
                            time8 = "0";
                        }
                        long t5, t6, t7, t8;
                        if ("00".equals(dynamicType)) {
                            unread2 = 0;
                            if (time5 != null) {
                                t5 = Long.parseLong(time5);
                                if (t5 > Long.parseLong(timec5) && currentIndex != 1) {
                                    unread2++;
                                }
                            }
                            if (time6 != null) {
                                t6 = Long.parseLong(time6);
                                if (t6 > Long.parseLong(timec6) && currentIndex != 2) {
                                    unread2++;
                                }
                            }
                            if (time7 != null) {
                                t7 = Long.parseLong(time7);
                                if (t7 > Long.parseLong(timec7) && currentIndex != 3) {
                                    unread2++;
                                }
                            }
                            if (time8 != null) {
                                t8 = Long.parseLong(time8);
                                if (t8 > Long.parseLong(timec8) && currentIndex != 4) {
                                    unread2++;
                                }
                            }
                            updateTypeUnread();
                        } else {
                            unread2 = 0;
                            if (time5 != null) {
                                t5 = Long.parseLong(time5);
                                if (t5 > Long.parseLong(timec5) && currentIndex != 1) {
                                    unread2++;
                                    //    unread_number_shhuo.setVisibility(View.VISIBLE);
                                } else {
                                    //  unread_number_shhuo.setVisibility(View.INVISIBLE);
                                }
                            }
                            if (time6 != null) {
                                t6 = Long.parseLong(time6);
                                if (t6 > Long.parseLong(timec6) && currentIndex != 2) {
                                    unread2++;
                                    unread_number_zuobiao.setVisibility(View.VISIBLE);
                                } else {
                                    unread_number_zuobiao.setVisibility(View.INVISIBLE);
                                }
                            }
                            if (time7 != null) {
                                t7 = Long.parseLong(time7);
                                if (t7 > Long.parseLong(timec7) && currentIndex != 3) {
                                    unread2++;
                                    unread_number_shangye.setVisibility(View.VISIBLE);
                                } else {
                                    unread_number_shangye.setVisibility(View.INVISIBLE);
                                }
                            }
                            if (time8 != null) {
                                t8 = Long.parseLong(time8);
                                if (t8 > Long.parseLong(timec8) && currentIndex != 4) {
                                    unread2++;
                                    unread_number_xianshang.setVisibility(View.VISIBLE);
                                } else {
                                    unread_number_xianshang.setVisibility(View.INVISIBLE);
                                }
                            }
                            updateTypeUnread();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("dingdanac,e", volleyError.toString());
            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void queryUpdatedyna() {
        String time;
        if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
            time = DemoApplication.getInstance().getCurrentUser().getResv3();
            time = dataOne(time);
        } else {
            time = "20170910141425";
        }
        if (time == null || "".equals(time)) {
            time = "20170910141425";
        }
        SharedPreferences sp = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
        final String timec1 = sp.getString("time1", time);//招标
        final String timec2 = sp.getString("time2", time);//生活
        final String timec3 = sp.getString("time3", time);//坐标
        final String timec4 = sp.getString("time4", time);//商业
        final String timec5 = sp.getString("time5", time);//坐标红包
        final String timec6 = sp.getString("time6", time);//商业红包

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
                    dynamicShareRed = object.getString("dynamicShareRed");
                    dynamicGame = object.getString("dynamicGame");
                    long t1, t2, t3, t4, t5, t6, t9, t10;
                    if ("00".equals(dynamicType)) {
                        unread = 0;
                        if (time1 != null) {
                            t1 = Long.parseLong(time1);
                            if (t1 > Long.parseLong(timec1) && currentIndex != 1) {
                                unread++;
                                unread_number_zuobiao.setVisibility(View.VISIBLE);
                            } else {
                                unread_number_zuobiao.setVisibility(View.INVISIBLE);
                            }
                        }

                        updateTypeUnread();
                    } else {
                        unread = 0;
                        iv_unread_shangye.setVisibility(View.INVISIBLE);
                        iv_unread_zuobiao.setVisibility(View.INVISIBLE);
                        if (time1 != null) {
                            t1 = Long.parseLong(time1);
                            if (t1 > Long.parseLong(timec1) && currentIndex != 1) {
                                unread++;
                            }
                        }
                        if (time2 != null) {
                            t2 = Long.parseLong(time2);
                            t9 = Long.parseLong(dynamicGame);
                            if (t9 > Long.parseLong(timec2) && currentIndex != 2) {
                                unread++;
                            } else {
                                if (t2 > Long.parseLong(timec2) && currentIndex != 2) {
                                    unread++;
                                }
                            }
                        }
                        if (time3 != null) {
                            t3 = Long.parseLong(time3);
                            t10 = Long.parseLong(dynamicShareRed);
                            if (t10 > Long.parseLong(timec3) && currentIndex != 3) {
                                unread++;
                            } else {
                                if (t3 > Long.parseLong(timec3) && currentIndex != 3) {
                                    unread++;
                                }
                            }
                        }
                        if (time4 != null) {
                            t4 = Long.parseLong(time4);
                            if (t4 > Long.parseLong(timec4) && currentIndex != 4) {
                                unread++;
                            }
                        }
                        updateTypeUnread();
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

    private void updateTypeUnread() {
        if (unread > 0 || unread2 > 0) {
            //  unread_number_type.setVisibility(View.VISIBLE);
        } else {
            unread_number_type.setVisibility(View.INVISIBLE);
            if (MainActivity.instance != null) {
                MainActivity.instance.hidUnread();
            }
        }
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
}
