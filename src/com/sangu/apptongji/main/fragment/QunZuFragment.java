package com.sangu.apptongji.main.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.adapter.BigDataAdapter;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.ProjectDynamicLinkDetailActivity;
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.moments.SocialMainAdapter;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import org.json.JSONException;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-27.
 */

public class QunZuFragment extends Fragment {
    //    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
//    private Button btn_commit;
    //    private ImageView iv_image;
    private ImageView iv1;
    private TextView tv1;
    private RadioButton regionBtn;
    private RadioButton professionBtn;
    private ListView listView;
    private List<com.alibaba.fastjson.JSONObject> datas = new ArrayList<>();
    private BigDataAdapter mAdaoter;
    private Dialog mWeiboDialog;
    private TextView tv_leftOrder,tv_rightOrder;
    private int currentPage = 1;
    private int lastVisibleIndex;
    private String currentType = "";//当前加载的是地区数据还是专业数据
    private TextView tv_order11,tv_order12,tv_order13,tv_order14,tv_allorder;

    private TextView tv_liftDynamic,tv_locationDynamic,tv_proDynamic,tv_bigData;
    private XRecyclerView mRecyclerView;
    private TextView unread_number_shhuo,unread_number_zuobiao,unread_number_shangye;
    private ImageView iv_unread_zuobiao,iv_unread_shangye;
    private String locationRed="",proRed="";

    private int page;
    private SocialMainAdapter adapter;
    private List<com.alibaba.fastjson.JSONObject> articles=new ArrayList<>();
    private String dType;
    private String newType;
    private String isHaveMargin = "0";
    private int isVip = 0;
    private String vipLevel = "1";
    private String profession;
    private String isClick = "yes";
    private int size=0;
    private ScrollView image_bigData;
    String time1=null,time2=null,time3=null,time4=null,time5=null,time6=null;

    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_bigdata,container,false);
//        iv1 = (ImageView) v.findViewById(R.id.iv1);
//        tv1 = (TextView) v.findViewById(R.id.tv1);

      //  initView();

        return v;
    }



    private void initView(){
/*
        regionBtn = (RadioButton) v.findViewById(R.id.btn_region);
        professionBtn = (RadioButton) v.findViewById(R.id.btn_profession);
        listView = (ListView) v.findViewById(R.id.data_listView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE
                        && lastVisibleIndex == mAdaoter.getCount()-1 && currentType.equals("专业")) {

                    if (datas.size()%20 == 0 && datas.size() == currentPage*20){

                        currentPage++;
                        Log.e("12222",""+currentPage);
                        GetProfessionRankInfo();
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");

                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

            }
        });

        tv_leftOrder = (TextView) v.findViewById(R.id.tv_leftorder);
        tv_rightOrder = (TextView) v.findViewById(R.id.tv_rightorder);


        tv_order11 = (TextView) v.findViewById(R.id.tv_order11);
        tv_order12 = (TextView) v.findViewById(R.id.tv_order12);
        tv_order13 = (TextView) v.findViewById(R.id.tv_order13);
        tv_order14 = (TextView) v.findViewById(R.id.tv_order14);
        tv_allorder = (TextView) v.findViewById(R.id.tv_allorder);

        tv_order11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(getActivity(),CeshiActivity.class);
                intent1.putExtra("type","11");
                startActivityForResult(intent1,1);
            }
        });

        tv_order12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(),CeshiActivity.class);
                intent2.putExtra("type","12");
                startActivityForResult(intent2,2);
            }
        });

        tv_order13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getActivity(),CeshiActivity.class);
                intent3.putExtra("type","13");
                startActivityForResult(intent3,3);
            }
        });

        tv_order14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(getActivity(),CeshiActivity.class);
                intent4.putExtra("type","14");
                startActivityForResult(intent4,4);
            }
        });
*/
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public Bitmap readBitMap(int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }


    private void getData(final String dynamicType,final int page_num,final String dtType,final String selectCon) {

        List<Param> params=new ArrayList<>();
        params.add(new Param("currentPage", page_num+""));
        params.add(new Param("dType", dtType));
        if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
            params.add(new Param("loginId",DemoHelper.getInstance().getCurrentUsernName()));
        }

        if (locationRed.equals("yes") && dType.equals("02")){

            params.add(new Param("shareRed", "1"));
            locationRed = "";
        }

        if (proRed.equals("yes") && dType.equals("03")){

            params.add(new Param("shareRed", "1"));
            proRed = "";

        }

        OkHttpManager.getInstance().post(params, FXConstant.URL_PUBLISH_QUERY, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                String loginId = DemoHelper.getInstance().getCurrentUsernName();
                if (jsonObject == null) {
                    if (page_num == 1) {
                        mRecyclerView.refreshComplete();
                    } else {
                        mRecyclerView.loadMoreComplete();
                    }
                    return;
                }
                com.alibaba.fastjson.JSONArray friendList = jsonObject.getJSONArray("friendList");
                com.alibaba.fastjson.JSONArray users_temp = jsonObject.getJSONArray("clist");

                if (dtType.equals("06")){

                    users_temp = jsonObject.getJSONArray("code");

                }else {

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

                } else {

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
                            articles = new ArrayList<com.alibaba.fastjson.JSONObject>();
                        }

                        if (dtType.equals("06")){

                            com.alibaba.fastjson.JSONArray newsTopArr = jsonObject.getJSONArray("top");

                            if (newsTopArr != null && newsTopArr.size()>0){

                                for (int i = 0; i < newsTopArr.size(); i++) {
                                    com.alibaba.fastjson.JSONObject json = newsTopArr.getJSONObject(i);
                                    if (articles != null) {
                                        articles.add(json);
                                    }

                                }

                            }

                        }

                        for (int i = 0; i < users_temp.size(); i++) {
                            com.alibaba.fastjson.JSONObject json = users_temp.getJSONObject(i);
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
                                        if (uLoginId.equals(loginId)) {
                                            String sID = json.getString("uId");
                                            if (articles != null) {
                                                articles.add(json);
                                            }
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
                                                    com.alibaba.fastjson.JSONObject json1 = friendList.getJSONObject(j);
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
                                    if (uLoginId.equals(loginId)) {
                                        String sID = json.getString("uId");
                                        if (articles != null) {
                                            articles.add(json);
                                        }
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
                                                com.alibaba.fastjson.JSONObject json1 = friendList.getJSONObject(j);
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
                                }else {

                                    if (articles != null) {
                                        articles.add(json);
                                    }
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < users_temp.size(); i++) {
                            com.alibaba.fastjson.JSONObject json = users_temp.getJSONObject(i);
                            String authType = json.getString("authType");
                            String uLoginId = json.getString("uLoginId");
                            String firstId = json.getString("firstUId");
                            if ("01".equals(authType)) {
                                String sID = json.getString("uId");
                                if (articles != null) {
                                    articles.add(json);
                                }
                            } else if ("03".equals(authType)) {//authType=02\03
                                if (uLoginId.equals(loginId)) {
                                    String sID = json.getString("uId");
                                    if (articles != null) {
                                        articles.add(json);
                                    }
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
                                            com.alibaba.fastjson.JSONObject json1 = friendList.getJSONObject(j);
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
                            }else {
                                if (articles != null) {
                                    articles.add(json);
                                }
                            }
                        }
                    }

                    if ("03".equals(dtType)){

                        if (page_num == 1 && isClick.equals("yes")) {

                            isClick = "no";
                            adapter = new SocialMainAdapter(getActivity(),articles,dType,profession,"00",isHaveMargin+"|"+vipLevel+"|"+newType+"|0",isVip,"0");
                            mRecyclerView.setAdapter(adapter);
                            setAdapter3();

                        }else {
                            profession = null;
                            adapter.notifyDataSetChanged();
                        }


                    }else if ("02".equals(dtType)){

                        if (page_num == 1 && isClick.equals("yes")) {

                            isClick = "no";
                            adapter = new SocialMainAdapter(getActivity(),articles,dType,profession,"00",isHaveMargin+"|"+vipLevel+"|"+newType+"|0",isVip,"0");
                            mRecyclerView.setAdapter(adapter);
                            setAdapter2();

                        }else {
                            profession = null;
                            adapter.notifyDataSetChanged();
                        }

                    }else if ("01".equals(dtType)){

                        if (page_num == 1 && isClick.equals("yes")) {

                            isClick = "no";
                            adapter = new SocialMainAdapter(getActivity(),articles,dType,profession,"00",isHaveMargin+"|"+vipLevel+"|"+newType+"|0",isVip,"0");
                            mRecyclerView.setAdapter(adapter);
                            setAdapter1();

                        }else {
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


    private void setAdapter3() {

        adapter.setOnItem3ClickListener(new SocialMainAdapter.MyItem3ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("chen", "点击进入");
                if (!DemoHelper.getInstance().isLoggedIn(getActivity())){
                    Toast.makeText(getActivity(),"请您先登录！",Toast.LENGTH_SHORT).show();
                    return;
                }
                com.alibaba.fastjson.JSONObject object = articles.get(position-1);
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
                if (task_jurisdiction!= null && task_jurisdiction.length()>8){

                    Intent intent = new Intent(getActivity(), ProjectDynamicLinkDetailActivity.class);

                    intent.putExtra("orderType",object.getString("orderType"));
                    intent.putExtra("lat",object.getString("lat"));
                    intent.putExtra("lng",object.getString("lng"));
                    intent.putExtra("salePrice",object.getString("salePrice"));
                    intent.putExtra("price",object.getString("price"));
                    intent.putExtra("sID", sID);
                    intent.putExtra("profession",profession);
                    intent.putExtra("dynamicSeq",dynamic_seq);
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("task_jurisdiction",task_jurisdiction);
                    intent.putExtra("task_label",object.getString("task_label"));
                    intent.putExtra("dType", dType);
                    intent.putExtra("type", type);
                    startActivityForResult(intent,0);

                }else {

                    Intent intent = new Intent(getActivity(), DynaDetaActivity.class);
                    intent.putExtra("sID", sID);
                    intent.putExtra("profession",profession);
                    intent.putExtra("dynamicSeq",dynamic_seq);
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("dType", dType);
                    intent.putExtra("type", type);
                    startActivityForResult(intent,0);

                }
            }
        });
    }


    private void setAdapter2() {
        adapter.setOnItem2ClickListener(new SocialMainAdapter.MyItem2ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!DemoHelper.getInstance().isLoggedIn(getActivity())){
                    Toast.makeText(getActivity(),"请您先登录！",Toast.LENGTH_SHORT).show();
                    return;
                }
                com.alibaba.fastjson.JSONObject object = articles.get(position-1);
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
                intent.putExtra("profession",profession);
                intent.putExtra("dynamicSeq",dynamic_seq);
                intent.putExtra("createTime",createTime);
                intent.putExtra("dType", dType);
                intent.putExtra("type", type);
                startActivityForResult(intent,0);
            }
        });
    }


    private void setAdapter1() {
        adapter.setOnItemClickListener(new SocialMainAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!DemoHelper.getInstance().isLoggedIn(getActivity())){
                    Toast.makeText(getActivity(),"请您先登录！",Toast.LENGTH_SHORT).show();
                    return;
                }
                com.alibaba.fastjson.JSONObject object = articles.get(position-1);
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
                intent.putExtra("profession",profession);
                intent.putExtra("dynamicSeq",dynamic_seq);
                intent.putExtra("createTime",createTime);
                intent.putExtra("dType", dType);
                intent.putExtra("type", type);
                intent.putExtra("type2", "00");
                startActivityForResult(intent,0);

            }
        });
    }


    private void InItViews(){

        mRecyclerView = (XRecyclerView) v.findViewById(R.id.xl_recyclerView);
        tv_liftDynamic = (TextView) v.findViewById(R.id.tv_lifeDynamic);
        tv_locationDynamic = (TextView) v.findViewById(R.id.tv_locationDynamic);
        tv_proDynamic = (TextView) v.findViewById(R.id.tv_proDynamic);
        tv_bigData = (TextView) v.findViewById(R.id.tv_bigData);
        image_bigData = (ScrollView) v.findViewById(R.id.image_bigData);
        unread_number_shhuo = (TextView) v.findViewById(R.id.unread_number_shhuo);
        unread_number_zuobiao = (TextView) v.findViewById(R.id.unread_number_zuobiao);
        unread_number_shangye = (TextView) v.findViewById(R.id.unread_number_shangye);
        iv_unread_zuobiao = (ImageView) v.findViewById(R.id.iv_unread_zuobiao);
        iv_unread_shangye = (ImageView) v.findViewById(R.id.iv_unread_shangye);


        dType = "02";
        page = 1;
        newType = "02";


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                if (articles==null||articles.size()==0) {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
                    getData("00", page, dType, null);
                    adapter = new SocialMainAdapter(getActivity(), articles, dType,profession,"00",isHaveMargin+"|"+vipLevel+"|"+newType+"|0",isVip,"0");
                    mRecyclerView.setAdapter(adapter);
                    if ("01".equals(dType)){
                        setAdapter1();
                    }else if ("02".equals(dType)){
                        setAdapter2();
                    }else if ("03".equals(dType)){
                        setAdapter3();
                    }
                }else {
                    getData("00", page, dType, null);
                }
            }

            @Override
            public void onLoadMore() {

                page = page + 1;
                getData("00",page, dType, null);

            }
        });


        tv_liftDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                unread_number_shhuo.setVisibility(View.INVISIBLE);

                SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("time2", getNowTime());
                editor.commit();



                tv_liftDynamic.setTextColor(Color.parseColor("#FF3E4A"));
                tv_locationDynamic.setTextColor(Color.parseColor("#ffbebebe"));
                tv_proDynamic.setTextColor(Color.parseColor("#ffbebebe"));
                tv_bigData.setTextColor(Color.parseColor("#ffbebebe"));

                mRecyclerView.setVisibility(View.VISIBLE);
                image_bigData.setVisibility(View.GONE);

                dType ="01";
                newType = "01";

                isClick = "yes";

                if (articles!=null&&articles.size()>0) {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
                }
                mRecyclerView.refresh(false);

            }
        });


        tv_locationDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                unread_number_zuobiao.setVisibility(View.INVISIBLE);

                SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("time3", getNowTime());
                editor.putString("time5", getNowTime());
                editor.commit();

                if (iv_unread_zuobiao.getVisibility() == View.VISIBLE){

                    iv_unread_zuobiao.setVisibility(View.INVISIBLE);
                    locationRed = "yes";

                }

                tv_liftDynamic.setTextColor(Color.parseColor("#ffbebebe"));
                tv_locationDynamic.setTextColor(Color.parseColor("#FF3E4A"));
                tv_proDynamic.setTextColor(Color.parseColor("#ffbebebe"));
                tv_bigData.setTextColor(Color.parseColor("#ffbebebe"));

                mRecyclerView.setVisibility(View.VISIBLE);
                image_bigData.setVisibility(View.GONE);

                dType ="02";
                newType = "02";

                isClick = "yes";

                if (articles!=null&&articles.size()>0) {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
                }
                mRecyclerView.refresh(false);

            }
        });

        tv_proDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                unread_number_shangye.setVisibility(View.INVISIBLE);

                SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("time4", getNowTime());
                editor.putString("time6", getNowTime());
                editor.commit();

                if (iv_unread_shangye.getVisibility() == View.VISIBLE){

                    iv_unread_shangye.setVisibility(View.INVISIBLE);
                    proRed = "yes";

                }

                tv_liftDynamic.setTextColor(Color.parseColor("#ffbebebe"));
                tv_locationDynamic.setTextColor(Color.parseColor("#ffbebebe"));
                tv_proDynamic.setTextColor(Color.parseColor("#FF3E4A"));
                tv_bigData.setTextColor(Color.parseColor("#ffbebebe"));

                mRecyclerView.setVisibility(View.VISIBLE);
                image_bigData.setVisibility(View.GONE);
                dType ="03";
                newType = "03";
                isClick = "yes";
                if (articles!=null&&articles.size()>0) {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
                }
                mRecyclerView.refresh(false);

            }
        });

        tv_bigData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                tv_liftDynamic.setTextColor(Color.parseColor("#ffbebebe"));
                tv_locationDynamic.setTextColor(Color.parseColor("#ffbebebe"));
                tv_proDynamic.setTextColor(Color.parseColor("#ffbebebe"));
                tv_bigData.setTextColor(Color.parseColor("#FF3E4A"));

                mRecyclerView.setVisibility(View.GONE);
                image_bigData.setVisibility(View.VISIBLE);


            }
        });


        QueryUnReadInfo();

    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    private String dataOne(String time) {
        if (time==null||"".equals(time)){
            time = getNowTime2();
        }
        String times = null;
        try {
            times = time.substring(0,4)+time.substring(5,7)+time.substring(8,10)+time.substring(11,13)+time.substring(14,16)+time.substring(17,19);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }
    private String getNowTime2() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    private void QueryUnReadInfo(){

        String time;
        if (DemoHelper.getInstance().isLoggedIn(getActivity())){
            time = DemoApplication.getInstance().getCurrentUser().getResv3();
            time = dataOne(time);
        }else {
            time = "20170910141425";
        }
        if (time==null||"".equals(time)){
            time = "20170910141425";
        }
        SharedPreferences sp = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
        final String timec1 = sp.getString("time1",time);//招标
        final String timec2 = sp.getString("time2",time);//生活
        final String timec3 = sp.getString("time3",time);//坐标
        final String timec4 = sp.getString("time4",time);//商业
        final String timec5 = sp.getString("time5",time);//坐标红包
        final String timec6 = sp.getString("time6",time);//商业红包

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

                    long t1, t2, t3, t4,t5,t6;

                    if (time2 != null) {
                        t2 = Long.parseLong(time2);
                        if (t2 > Long.parseLong(timec2)) {
                            unread_number_shhuo.setVisibility(View.VISIBLE);
                        } else {
                            unread_number_shhuo.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (time3 != null) {
                        t3 = Long.parseLong(time3);
                        if (t3 > Long.parseLong(timec3)) {
                            unread_number_zuobiao.setVisibility(View.VISIBLE);
                        } else {
                            unread_number_zuobiao.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (time4 != null) {
                        t4= Long.parseLong(time4);
                        if (t4 > Long.parseLong(timec4)) {
                            unread_number_shangye.setVisibility(View.VISIBLE);
                        } else {
                            unread_number_shangye.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (time5 != null) {
                        t5 = Long.parseLong(time5);
                        if (t5 > Long.parseLong(timec5)) {
                            iv_unread_zuobiao.setVisibility(View.VISIBLE);
                        } else {
                            iv_unread_zuobiao.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (time6 != null) {
                        t6 = Long.parseLong(time6);
                        if (t6 > Long.parseLong(timec6)) {
                            iv_unread_shangye.setVisibility(View.VISIBLE);
                        } else {
                            iv_unread_shangye.setVisibility(View.INVISIBLE);
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        InItViews();


       // GetRegionRankInfo();

//        regionBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
//
//                currentType = "地区";
//                regionBtn.setTextColor(Color.rgb(255,0,0));
//                professionBtn.setTextColor(Color.rgb(170,170,170));
//                currentPage = 1;
//                GetRegionRankInfo();
//
//            }
//        });
//
//        professionBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
//                currentType = "专业";
//                regionBtn.setTextColor(Color.rgb(170,170,170));
//                professionBtn.setTextColor(Color.rgb(250,0,0));
//                currentPage = 1;
//                datas.clear();
//                GetProfessionRankInfo();
//
//            }
//        });


//        Bitmap bit = readBitMap(R.drawable.qunzu_jieshao);
//        iv1.setImageBitmap(bit);
//        tv1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                intent.putExtra("userId", "22222222222");
//                intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
//                intent.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
//                intent.putExtra(EaseConstant.EXTRA_USER_IMG,"zhengshiduo.png");
//                intent.putExtra(EaseConstant.EXTRA_USER_NAME,"正事多客服");
//                intent.putExtra(EaseConstant.EXTRA_USER_SHARERED,"无");
//                startActivity(intent);
//            }
//        });
//        btn_commit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
////                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
//                Toast.makeText(getActivity(),"连续登陆三个月才能申请AR权限哦",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void GetProfessionRankInfo (){

        String url = FXConstant.URL_SELECTPROFESSRANK;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                try {

                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(s);
                    com.alibaba.fastjson.JSONArray array = jsonObject.getJSONArray("clist");

                    if (currentPage == 1){

                        datas.clear();

                    }

                    for (int i=0;i<array.size();i++) {

                        com.alibaba.fastjson.JSONObject object1 = array.getJSONObject(i);

                        datas.add(object1);

                    }

                    int width = 1080;
                    if (getActivity() != null){

                        WindowManager wm = getActivity().getWindowManager();
                        DisplayMetrics dm = new DisplayMetrics();
                        wm.getDefaultDisplay().getMetrics(dm);
                        width = dm.widthPixels;

                    }

                    mAdaoter = new BigDataAdapter(getActivity(),datas,width,"专业");

                    listView.setAdapter(mAdaoter);
                    if (currentPage != 1){

                        listView.setSelection(datas.size()-23);

                    }

                } catch (com.alibaba.fastjson.JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(getActivity(),"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("currentPage",currentPage+"");

                return param;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    private void GetRegionRankInfo (){

        String url = FXConstant.URL_SELECTREGIONRANK;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                try {

                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(s);
                    com.alibaba.fastjson.JSONArray array = jsonObject.getJSONArray("clist");

                    if (currentPage == 1){

                        com.alibaba.fastjson.JSONArray count2 = jsonObject.getJSONArray("count2");
                        com.alibaba.fastjson.JSONObject count2Obj = count2.getJSONObject(0);

                        String left = count2Obj.getString("demand_total");
                        String right = count2Obj.getString("receipt_amount");

                        int a = 500000 + Integer.valueOf(left)*10+Integer.valueOf(right);

                        tv_allorder.setText("单量\n"+a);

                       // tv_leftOrder.setText(a+"");
                       // tv_rightOrder.setText(right);
                    }

                    datas.clear();

                    for (int i=0;i<array.size();i++) {

                        com.alibaba.fastjson.JSONObject object1 = array.getJSONObject(i);

                        datas.add(object1);

                    }

                    int width = 1080;
                    if (getActivity() != null){

                        WindowManager wm = getActivity().getWindowManager();
                        DisplayMetrics dm = new DisplayMetrics();
                        wm.getDefaultDisplay().getMetrics(dm);
                        width = dm.widthPixels;


                        mAdaoter = new BigDataAdapter(getActivity(),datas,width,"地区");

                        listView.setAdapter(mAdaoter);

                    }

                } catch (com.alibaba.fastjson.JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(getActivity(),"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("currentPage",currentPage+"");

                return param;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }


}
