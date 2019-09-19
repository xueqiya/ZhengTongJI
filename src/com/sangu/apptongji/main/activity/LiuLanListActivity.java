package com.sangu.apptongji.main.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.LiulanListAdapter;
import com.sangu.apptongji.main.adapter.ZhuFaListAdapter;
import com.sangu.apptongji.main.alluser.entity.LiuLanDetail;
import com.sangu.apptongji.main.alluser.entity.ZhuFaDetail;
import com.sangu.apptongji.main.alluser.presenter.ILiulanListPresenter;
import com.sangu.apptongji.main.alluser.presenter.IZhuFaListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.LiulanListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.ZhuFaListPresenter;
import com.sangu.apptongji.main.alluser.view.ILiulanListView;
import com.sangu.apptongji.main.alluser.view.IZhuanFaListView;
import com.sangu.apptongji.main.widget.ArcView;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-08-18.
 */

public class LiuLanListActivity extends BaseActivity implements ILiulanListView,IZhuanFaListView ,View.OnClickListener{
    private ILiulanListPresenter liulanPresenter;
    private IZhuFaListPresenter zhuFaPresenter;
    private TextView tv_none1,tv_liulan_count,tv_zhuye,tv_xinzeng_zhuye
            ,tv_shenghuo,tv_xinzeng_shenhuo,tv_zuobiao,tv_xinzneg_zuobiao,tv_shangye,tv_xinzneg_shangye;
    private XRecyclerView mRecyclerView=null;
    private List<LiuLanDetail> list1=null;
    private List<ZhuFaDetail> list2=null;
    private LiulanListAdapter adapter;
    private ZhuFaListAdapter adapter2;
    private RelativeLayout rl1,rl2,rl3,rl4;
    private CustomProgressDialog mProgress=null;
    private int currentPage=1;
    private boolean isfirst = true;
    private boolean isNext = false;
    private TextView tv_back,tv_title;
    String type,hxid,leixing,homePage,lifeDynamics,locationDynamics,businessDynamics;
    private ArcView arcView1;
    private RelativeLayout rl_qq,rl_weixin,rl_weibo,rl_app;
    private TextView tv_qq,tv_weixin,tv_weibo,tv_app;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_liulan_list);
        WeakReference<LiuLanListActivity> reference =  new WeakReference<LiuLanListActivity>(LiuLanListActivity.this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText("返回");
        tv_title.setText("谁看过我");
        type = getIntent().getStringExtra("type");
        leixing = getIntent().getStringExtra("leixing");
        hxid = getIntent().getStringExtra(FXConstant.JSON_KEY_HXID);
        rl_qq = (RelativeLayout) findViewById(R.id.rl_qq);
        rl_weixin = (RelativeLayout) findViewById(R.id.rl_weixin);
        rl_weibo = (RelativeLayout) findViewById(R.id.rl_weibo);
        rl_app = (RelativeLayout) findViewById(R.id.rl_app);
        tv_qq = (TextView) findViewById(R.id.tv_qq);
        tv_weixin = (TextView) findViewById(R.id.tv_weixin);
        tv_weibo = (TextView) findViewById(R.id.tv_weibo);
        tv_app = (TextView) findViewById(R.id.tv_app);
        tv_none1 = (TextView) findViewById(R.id.tv_none1);
        tv_liulan_count = (TextView) findViewById(R.id.tv_liulan_count);
        tv_zhuye = (TextView) findViewById(R.id.tv_zhuye);
        tv_xinzeng_zhuye = (TextView) findViewById(R.id.tv_xinzeng_zhuye);
        tv_shenghuo = (TextView) findViewById(R.id.tv_shenghuo);
        tv_xinzeng_shenhuo = (TextView) findViewById(R.id.tv_xinzeng_shenhuo);
        tv_zuobiao = (TextView) findViewById(R.id.tv_zuobiao);
        tv_xinzneg_zuobiao = (TextView) findViewById(R.id.tv_xinzneg_zuobiao);
        tv_shangye = (TextView) findViewById(R.id.tv_shangye);
        tv_xinzneg_shangye = (TextView) findViewById(R.id.tv_xinzneg_shangye);
        arcView1 = (ArcView) findViewById(R.id.arc1);
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        rl2 = (RelativeLayout) findViewById(R.id.rl2);
        rl3 = (RelativeLayout) findViewById(R.id.rl3);
        rl4 = (RelativeLayout) findViewById(R.id.rl4);
        rl1.setOnClickListener(this);
        rl2.setOnClickListener(this);
        rl3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        mRecyclerView = (XRecyclerView) findViewById(R.id.refresh_list);
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_dark);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isNext = false;
                currentPage=1;
                if ("00".equals(type)){
                    liulanPresenter.loadLiuLanList("1",hxid,leixing);
                }else {
                    zhuFaPresenter.loadZhuFaList("1",hxid,leixing);
                }
            }

            @Override
            public void onLoadMore() {
                isNext = true;
                currentPage++;
                if ("00".equals(type)){
                    liulanPresenter.loadLiuLanList(currentPage+"",hxid,leixing);
                }else {
                    zhuFaPresenter.loadZhuFaList(currentPage+"",hxid,leixing);
                }
            }
        });
        if ("00".equals(type)){
            liulanPresenter = new LiulanListPresenter(this,this);
            liulanPresenter.loadLiuLanList("1",hxid,leixing);
        }else {
            zhuFaPresenter = new ZhuFaListPresenter(this,this);
            zhuFaPresenter.loadZhuFaList("1",hxid,leixing);
        }
        mRecyclerView.refresh(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl1:
                tv_zhuye.setTextColor(Color.rgb(255,0,0));
                tv_shenghuo.setTextColor(Color.rgb(110,104,106));
                tv_zuobiao.setTextColor(Color.rgb(110,104,106));
                tv_shangye.setTextColor(Color.rgb(110,104,106));
                tv_xinzeng_zhuye.setVisibility(View.INVISIBLE);
                leixing = "homePage";
                isNext = false;
                currentPage=1;
                if ("00".equals(type)){
                    SharedPreferences mSharedPreferences1 = getSharedPreferences("sangu_liulan_count", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = mSharedPreferences1.edit();
                    editor1.putString("homePage",homePage);
                    editor1.commit();
                    liulanPresenter.loadLiuLanList("1",hxid,leixing);
                }else {
                    SharedPreferences mSharedPreferences1 = getSharedPreferences("sangu_zhufa_count", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = mSharedPreferences1.edit();
                    editor1.putString("homePage",homePage);
                    editor1.commit();
                    zhuFaPresenter.loadZhuFaList("1",hxid,leixing);
                }
                break;
            case R.id.rl2:
                tv_zhuye.setTextColor(Color.rgb(110,104,106));
                tv_shenghuo.setTextColor(Color.rgb(255,0,0));
                tv_zuobiao.setTextColor(Color.rgb(110,104,106));
                tv_shangye.setTextColor(Color.rgb(110,104,106));
                tv_xinzeng_shenhuo.setVisibility(View.INVISIBLE);
                leixing = "lifeDynamics";
                isNext = false;
                currentPage=1;
                if ("00".equals(type)){
                    SharedPreferences mSharedPreferences2 = getSharedPreferences("sangu_liulan_count", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = mSharedPreferences2.edit();
                    editor2.putString("lifeDynamics",lifeDynamics);
                    editor2.commit();
                    liulanPresenter.loadLiuLanList("1",hxid,leixing);
                }else {
                    SharedPreferences mSharedPreferences2 = getSharedPreferences("sangu_zhufa_count", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = mSharedPreferences2.edit();
                    editor2.putString("lifeDynamics",lifeDynamics);
                    editor2.commit();
                    zhuFaPresenter.loadZhuFaList("1",hxid,leixing);
                }
                break;
            case R.id.rl3:
                tv_zhuye.setTextColor(Color.rgb(110,104,106));
                tv_shenghuo.setTextColor(Color.rgb(110,104,106));
                tv_zuobiao.setTextColor(Color.rgb(255,0,0));
                tv_shangye.setTextColor(Color.rgb(110,104,106));
                tv_xinzneg_zuobiao.setVisibility(View.INVISIBLE);
                leixing = "locationDynamics";
                isNext = false;
                currentPage=1;
                if ("00".equals(type)){
                    SharedPreferences mSharedPreferences3 = getSharedPreferences("sangu_liulan_count", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = mSharedPreferences3.edit();
                    editor3.putString("locationDynamics",locationDynamics);
                    editor3.commit();
                    liulanPresenter.loadLiuLanList("1",hxid,leixing);
                }else {
                    SharedPreferences mSharedPreferences3 = getSharedPreferences("sangu_zhufa_count", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = mSharedPreferences3.edit();
                    editor3.putString("locationDynamics",locationDynamics);
                    editor3.commit();
                    zhuFaPresenter.loadZhuFaList("1",hxid,leixing);
                }
                break;
            case R.id.rl4:
                tv_zhuye.setTextColor(Color.rgb(110,104,106));
                tv_shenghuo.setTextColor(Color.rgb(110,104,106));
                tv_zuobiao.setTextColor(Color.rgb(110,104,106));
                tv_shangye.setTextColor(Color.rgb(255,0,0));
                tv_xinzneg_shangye.setVisibility(View.INVISIBLE);
                leixing = "businessDynamics";
                isNext = false;
                currentPage=1;
                if ("00".equals(type)){
                    SharedPreferences mSharedPreferences4 = getSharedPreferences("sangu_liulan_count", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = mSharedPreferences4.edit();
                    editor4.putString("businessDynamics",businessDynamics);
                    editor4.commit();
                    liulanPresenter.loadLiuLanList("1",hxid,leixing);
                }else {
                    SharedPreferences mSharedPreferences4 = getSharedPreferences("sangu_zhufa_count", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = mSharedPreferences4.edit();
                    editor4.putString("businessDynamics",businessDynamics);
                    editor4.commit();
                    zhuFaPresenter.loadZhuFaList("1",hxid,leixing);
                }
                break;
        }
    }

    @Override
    public void updateLiuLanList(List<LiuLanDetail> liuLanDetails, JSONObject object, String size, boolean hasMore) {
        if (isNext){
            list1.addAll(liuLanDetails);
            mRecyclerView.loadMoreComplete();
            adapter.notifyDataSetChanged();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        }else {
            list1 = liuLanDetails;
            adapter = new LiulanListAdapter(list1, LiuLanListActivity.this);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.refreshComplete();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        }
        adapter.setOnItemClickListener(new LiulanListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String hxId = list1.get(position-1).getV_id();
                startActivity(new Intent(LiuLanListActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,hxId));
            }
        });
        if (object!=null) {
            String qqHomePage = object.optString("qqHomePage");
            String qqLifeDynamic = object.optString("qqLifeDynamic");
            String qqLocationDynamic = object.optString("qqLocationDynamic");
            String qqBusinessDynamic = object.optString("qqBusinessDynamic");
            String weixinHomePage = object.optString("weixinHomePage");
            String weixinLifeDynamic = object.optString("weixinLifeDynamic");
            String weixinLocationDynamic = object.optString("weixinLocationDynamic");
            String weixinBusinessDynamic = object.optString("weixinBusinessDynamic");
            String weiboHomePage = object.optString("weiboHomePage");
            String weiboLifeDynamic = object.optString("weiboLifeDynamic");
            String weiboLocationDynamic = object.optString("weiboLocationDynamic");
            String weiboBusinessDynamic = object.optString("weiboBusinessDynamic");
            homePage = object.optString("homePage");
            lifeDynamics = object.optString("lifeDynamics");
            locationDynamics = object.optString("locationDynamics");
            businessDynamics = object.optString("businessDynamics");
            if ("businessDynamics".equals(leixing)) {
                int count=0;
                if (businessDynamics!=null){
                    count = Integer.parseInt(businessDynamics)-Integer.parseInt(qqBusinessDynamic)
                            -Integer.parseInt(weixinBusinessDynamic)-Integer.parseInt(weiboBusinessDynamic);
                }
                if (Integer.parseInt(qqBusinessDynamic)>9999){
                    qqBusinessDynamic = "9999+";
                }
                if (Integer.parseInt(weixinBusinessDynamic)>9999){
                    weixinBusinessDynamic = "9999+";
                }
                if (Integer.parseInt(weiboBusinessDynamic)>9999){
                    weiboBusinessDynamic = "9999+";
                }
                if (count>9999){
                    tv_app.setText("9999+");
                }else {
                    tv_app.setText(count+"");
                }
                tv_qq.setText(qqBusinessDynamic);
                tv_weixin.setText(weixinBusinessDynamic);
                tv_weibo.setText(weiboBusinessDynamic);

            }else if ("locationDynamics".equals(leixing)){
                int count=0;
                if (locationDynamics!=null){
                    count = Integer.parseInt(locationDynamics)-Integer.parseInt(qqLocationDynamic)
                            -Integer.parseInt(weixinLocationDynamic)-Integer.parseInt(weiboLocationDynamic);
                }
                if (Integer.parseInt(qqLocationDynamic)>9999){
                    qqLocationDynamic = "9999+";
                }
                if (Integer.parseInt(weixinLocationDynamic)>9999){
                    weixinLocationDynamic = "9999+";
                }
                if (Integer.parseInt(weiboLocationDynamic)>9999){
                    weiboLocationDynamic = "9999+";
                }
                if (count>9999){
                    tv_app.setText("9999+");
                }else {
                    tv_app.setText(count+"");
                }
                tv_qq.setText(qqLocationDynamic);
                tv_weixin.setText(weixinLocationDynamic);
                tv_weibo.setText(weiboLocationDynamic);
            }else if ("lifeDynamics".equals(leixing)){
                int count=0;
                if (lifeDynamics!=null){
                    count = Integer.parseInt(lifeDynamics)-Integer.parseInt(qqLifeDynamic)
                            -Integer.parseInt(weixinLifeDynamic)-Integer.parseInt(weiboLifeDynamic);
                }
                if (Integer.parseInt(qqLifeDynamic)>9999){
                    qqLifeDynamic = "9999+";
                }
                if (Integer.parseInt(weixinLifeDynamic)>9999){
                    weixinLifeDynamic = "9999+";
                }
                if (Integer.parseInt(weiboLifeDynamic)>9999){
                    weiboLifeDynamic = "9999+";
                }
                if (count>9999){
                    tv_app.setText("9999+");
                }else {
                    tv_app.setText(count+"");
                }
                tv_qq.setText(qqLifeDynamic);
                tv_weixin.setText(weixinLifeDynamic);
                tv_weibo.setText(weiboLifeDynamic);
            }else {
                int count=0;
                if (homePage!=null){
                    count = Integer.parseInt(homePage)-Integer.parseInt(qqHomePage)
                            -Integer.parseInt(weixinHomePage)-Integer.parseInt(weiboHomePage);
                }
                if (Integer.parseInt(qqHomePage)>9999){
                    qqHomePage = "9999+";
                }
                if (Integer.parseInt(weixinHomePage)>9999){
                    weixinHomePage = "9999+";
                }
                if (Integer.parseInt(weiboHomePage)>9999){
                    weiboHomePage = "9999+";
                }
                if (count>9999){
                    tv_app.setText("9999+");
                }else {
                    tv_app.setText(count+"");
                }
                tv_qq.setText(qqHomePage);
                tv_weixin.setText(weixinHomePage);
                tv_weibo.setText(weiboHomePage);
            }
        }
        if (homePage==null||"".equals(homePage)||homePage.equalsIgnoreCase("null")){
            homePage = "0";
        }
        if (lifeDynamics==null||"".equals(lifeDynamics)||lifeDynamics.equalsIgnoreCase("null")){
            lifeDynamics = "0";
        }
        if (locationDynamics==null||"".equals(locationDynamics)||locationDynamics.equalsIgnoreCase("null")){
            locationDynamics = "0";
        }
        if (businessDynamics==null||"".equals(businessDynamics)||businessDynamics.equalsIgnoreCase("null")){
            businessDynamics = "0";
        }
        SharedPreferences sp = getSharedPreferences("sangu_liulan_count", Context.MODE_PRIVATE);
        if (isfirst){
            if ("businessDynamics".equals(leixing)) {
                tv_shangye.setTextColor(Color.rgb(255,0,0));
                SharedPreferences.Editor editor4 = sp.edit();
                editor4.putString("businessDynamics", businessDynamics);
                editor4.commit();
            }else if ("locationDynamics".equals(leixing)){
                tv_zuobiao.setTextColor(Color.rgb(255,0,0));
                SharedPreferences.Editor editor4 = sp.edit();
                editor4.putString("locationDynamics", locationDynamics);
                editor4.commit();
            }else if ("lifeDynamics".equals(leixing)){
                tv_shenghuo.setTextColor(Color.rgb(255,0,0));
                SharedPreferences.Editor editor4 = sp.edit();
                editor4.putString("lifeDynamics", lifeDynamics);
                editor4.commit();
            }else {
                tv_zhuye.setTextColor(Color.rgb(255,0,0));
                SharedPreferences.Editor editor4 = sp.edit();
                editor4.putString("homePage", homePage);
                editor4.commit();
            }
        }
        String homePagel = sp.getString("homePage","0");
        String lifeDynamicsl = sp.getString("lifeDynamics","0");
        String locationDynamicsl = sp.getString("locationDynamics","0");
        String businessDynamicsl = sp.getString("businessDynamics","0");

        if (Integer.valueOf(homePage)>Integer.valueOf(homePagel)){
            tv_xinzeng_zhuye.setVisibility(View.VISIBLE);
            tv_xinzeng_zhuye.setText(String.valueOf(Integer.valueOf(homePage)-Integer.valueOf(homePagel)));
        }else {
            tv_xinzeng_zhuye.setVisibility(View.INVISIBLE);
        }

        if (Integer.valueOf(lifeDynamics)>Integer.valueOf(lifeDynamicsl)){
            tv_xinzeng_shenhuo.setVisibility(View.VISIBLE);
            tv_xinzeng_shenhuo.setText(String.valueOf(Integer.valueOf(lifeDynamics)-Integer.valueOf(lifeDynamicsl)));
        }else {
            tv_xinzeng_shenhuo.setVisibility(View.INVISIBLE);
        }

        if (Integer.valueOf(locationDynamics)>Integer.valueOf(locationDynamicsl)){
            tv_xinzneg_zuobiao.setVisibility(View.VISIBLE);
            tv_xinzneg_zuobiao.setText(String.valueOf(Integer.valueOf(locationDynamics)-Integer.valueOf(locationDynamicsl)));
        }else {
            tv_xinzneg_zuobiao.setVisibility(View.INVISIBLE);
        }

        if (Integer.valueOf(businessDynamics)>Integer.valueOf(businessDynamicsl)){
            tv_xinzneg_shangye.setVisibility(View.VISIBLE);
            tv_xinzneg_shangye.setText(String.valueOf(Integer.valueOf(businessDynamics)-Integer.valueOf(businessDynamicsl)));
        }else {
            tv_xinzneg_shangye.setVisibility(View.INVISIBLE);
        }
        tv_zhuye.setText("个人主页("+homePage+")");
        tv_shenghuo.setText("派单动态("+lifeDynamics+")");
        tv_zuobiao.setText("坐标动态("+locationDynamics+")");
        tv_shangye.setText("商业动态("+businessDynamics+")");
        if (homePage.equals("0")&&lifeDynamics.equals("0")&&locationDynamics.equals("0")&&businessDynamics.equals("0")){
            arcView1.setVisibility(View.INVISIBLE);
            tv_none1.setVisibility(View.VISIBLE);
        }else {
            arcView1.setVisibility(View.VISIBLE);
            tv_none1.setVisibility(View.INVISIBLE);
            if (isfirst) {
                List<Times> times = new ArrayList<>();
                Times t1 = new Times();
                Times t2 = new Times();
                Times t3 = new Times();
                Times t4 = new Times();
                int peopleSum1 = 0, peopleSum2 = 0, peopleSum3 = 0, peopleSum4 = 0;
                peopleSum1 = Integer.valueOf(homePage);
                t1.hour = peopleSum1;
                t1.text = "个人主页";
                times.add(t1);
                peopleSum2 = Integer.valueOf(lifeDynamics);
                t2.hour = peopleSum2;
                t2.text = "派单动态";
                times.add(t2);
                peopleSum3 = Integer.valueOf(locationDynamics);
                t3.hour = peopleSum3;
                t3.text = "坐标动态";
                times.add(t3);
                peopleSum4 = Integer.valueOf(businessDynamics);
                t4.hour = peopleSum4;
                t4.text = "商业动态";
                times.add(t4);
                ArcView.ArcViewAdapter myAdapter = arcView1.new ArcViewAdapter<Times>() {
                    @Override
                    public double getValue(Times times) {
                        return times.hour;
                    }
                };//设置adapter
                myAdapter.setData(times);//设置数据集
                arcView1.setMaxNum(4);//设置可以显示的最大数值 该数值之后的会合并为其他
                isfirst = false;
            }
        }
        int liulan = Integer.valueOf(homePage)+Integer.valueOf(lifeDynamics)+Integer.valueOf(locationDynamics)+Integer.valueOf(businessDynamics);
        tv_liulan_count.setText("浏 览 "+liulan+" 次");
    }

    @Override
    public void updateZhuFaList(List<ZhuFaDetail> zhuFaDetails, JSONObject object, String size, boolean hasMore) {
        if (isNext){
            list2.addAll(zhuFaDetails);
            mRecyclerView.loadMoreComplete();
            adapter2.notifyDataSetChanged();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        }else {
            list2 = zhuFaDetails;
            adapter2 = new ZhuFaListAdapter(list2, LiuLanListActivity.this);
            mRecyclerView.setAdapter(adapter2);
            mRecyclerView.refreshComplete();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        }
        adapter2.setOnItemClickListener(new ZhuFaListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String hxId = list2.get(position-1).getF_id();
                startActivity(new Intent(LiuLanListActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,hxId));
            }
        });
        if (object!=null) {
            String qqHomePage = object.optString("qqHomePage");
            String qqLifeDynamic = object.optString("qqLifeDynamic");
            String qqLocationDynamic = object.optString("qqLocationDynamic");
            String qqBusinessDynamic = object.optString("qqBusinessDynamic");
            String weixinHomePage = object.optString("weixinHomePage");
            String weixinLifeDynamic = object.optString("weixinLifeDynamic");
            String weixinLocationDynamic = object.optString("weixinLocationDynamic");
            String weixinBusinessDynamic = object.optString("weixinBusinessDynamic");
            String weiboHomePage = object.optString("weiboHomePage");
            String weiboLifeDynamic = object.optString("weiboLifeDynamic");
            String weiboLocationDynamic = object.optString("weiboLocationDynamic");
            String weiboBusinessDynamic = object.optString("weiboBusinessDynamic");
            homePage = object.optString("homePage");
            lifeDynamics = object.optString("lifeDynamics");
            locationDynamics = object.optString("locationDynamics");
            businessDynamics = object.optString("businessDynamics");
            if ("businessDynamics".equals(leixing)) {
                int count=0;
                if (businessDynamics!=null){
                    count = Integer.parseInt(businessDynamics)-Integer.parseInt(qqBusinessDynamic)
                            -Integer.parseInt(weixinBusinessDynamic)-Integer.parseInt(weiboBusinessDynamic);
                }
                if (Integer.parseInt(qqBusinessDynamic)>9999){
                    qqBusinessDynamic = "9999+";
                }
                if (Integer.parseInt(weixinBusinessDynamic)>9999){
                    weixinBusinessDynamic = "9999+";
                }
                if (Integer.parseInt(weiboBusinessDynamic)>9999){
                    weiboBusinessDynamic = "9999+";
                }
                if (count>9999){
                    tv_app.setText("9999+");
                }else {
                    tv_app.setText(count+"");
                }
                tv_qq.setText(qqBusinessDynamic);
                tv_weixin.setText(weixinBusinessDynamic);
                tv_weibo.setText(weiboBusinessDynamic);

            }else if ("locationDynamics".equals(leixing)){
                int count=0;
                if (locationDynamics!=null){
                    count = Integer.parseInt(locationDynamics)-Integer.parseInt(qqLocationDynamic)
                            -Integer.parseInt(weixinLocationDynamic)-Integer.parseInt(weiboLocationDynamic);
                }
                if (Integer.parseInt(qqLocationDynamic)>9999){
                    qqLocationDynamic = "9999+";
                }
                if (Integer.parseInt(weixinLocationDynamic)>9999){
                    weixinLocationDynamic = "9999+";
                }
                if (Integer.parseInt(weiboLocationDynamic)>9999){
                    weiboLocationDynamic = "9999+";
                }
                if (count>9999){
                    tv_app.setText("9999+");
                }else {
                    tv_app.setText(count+"");
                }
                tv_qq.setText(qqLocationDynamic);
                tv_weixin.setText(weixinLocationDynamic);
                tv_weibo.setText(weiboLocationDynamic);
            }else if ("lifeDynamics".equals(leixing)){
                int count=0;
                if (lifeDynamics!=null){
                    count = Integer.parseInt(lifeDynamics)-Integer.parseInt(qqLifeDynamic)
                            -Integer.parseInt(weixinLifeDynamic)-Integer.parseInt(weiboLifeDynamic);
                }
                if (Integer.parseInt(qqLifeDynamic)>9999){
                    qqLifeDynamic = "9999+";
                }
                if (Integer.parseInt(weixinLifeDynamic)>9999){
                    weixinLifeDynamic = "9999+";
                }
                if (Integer.parseInt(weiboLifeDynamic)>9999){
                    weiboLifeDynamic = "9999+";
                }
                if (count>9999){
                    tv_app.setText("9999+");
                }else {
                    tv_app.setText(count+"");
                }
                tv_qq.setText(qqLifeDynamic);
                tv_weixin.setText(weixinLifeDynamic);
                tv_weibo.setText(weiboLifeDynamic);
            }else {
                int count=0;
                if (homePage!=null){
                    count = Integer.parseInt(homePage)-Integer.parseInt(qqHomePage)
                            -Integer.parseInt(weixinHomePage)-Integer.parseInt(weiboHomePage);
                }
                if (Integer.parseInt(qqHomePage)>9999){
                    qqHomePage = "9999+";
                }
                if (Integer.parseInt(weixinHomePage)>9999){
                    weixinHomePage = "9999+";
                }
                if (Integer.parseInt(weiboHomePage)>9999){
                    weiboHomePage = "9999+";
                }
                if (count>9999){
                    tv_app.setText("9999+");
                }else {
                    tv_app.setText(count+"");
                }
                tv_qq.setText(qqHomePage);
                tv_weixin.setText(weixinHomePage);
                tv_weibo.setText(weiboHomePage);
            }
        }
        if (homePage==null||"".equals(homePage)||homePage.equalsIgnoreCase("null")){
            homePage = "0";
        }
        if (lifeDynamics==null||"".equals(lifeDynamics)||lifeDynamics.equalsIgnoreCase("null")){
            lifeDynamics = "0";
        }
        if (locationDynamics==null||"".equals(locationDynamics)||locationDynamics.equalsIgnoreCase("null")){
            locationDynamics = "0";
        }
        if (businessDynamics==null||"".equals(businessDynamics)||businessDynamics.equalsIgnoreCase("null")){
            businessDynamics = "0";
        }
        SharedPreferences sp = getSharedPreferences("sangu_zhufa_count", Context.MODE_PRIVATE);
        if (isfirst){
            if ("businessDynamics".equals(leixing)) {
                tv_shangye.setTextColor(Color.rgb(255,0,0));
                SharedPreferences.Editor editor4 = sp.edit();
                editor4.putString("businessDynamics", businessDynamics);
                editor4.commit();
            }else if ("locationDynamics".equals(leixing)){
                tv_zuobiao.setTextColor(Color.rgb(255,0,0));
                SharedPreferences.Editor editor4 = sp.edit();
                editor4.putString("locationDynamics", locationDynamics);
                editor4.commit();
            }else if ("lifeDynamics".equals(leixing)){
                tv_shenghuo.setTextColor(Color.rgb(255,0,0));
                SharedPreferences.Editor editor4 = sp.edit();
                editor4.putString("lifeDynamics", lifeDynamics);
                editor4.commit();
            }else {
                tv_zhuye.setTextColor(Color.rgb(255,0,0));
                SharedPreferences.Editor editor4 = sp.edit();
                editor4.putString("homePage", homePage);
                editor4.commit();
            }
        }
        String homePagez = sp.getString("homePage","0");
        String lifeDynamicsz = sp.getString("lifeDynamics","0");
        String locationDynamicsz = sp.getString("locationDynamics","0");
        String businessDynamicsz = sp.getString("businessDynamics","0");

        if (Integer.valueOf(homePage)>Integer.valueOf(homePagez)){
            tv_xinzeng_zhuye.setVisibility(View.VISIBLE);
            tv_xinzeng_zhuye.setText(String.valueOf(Integer.valueOf(homePage)-Integer.valueOf(homePagez)));
        }else {
            tv_xinzeng_zhuye.setVisibility(View.INVISIBLE);
        }

        if (Integer.valueOf(lifeDynamics)>Integer.valueOf(lifeDynamicsz)){
            tv_xinzeng_shenhuo.setVisibility(View.VISIBLE);
            tv_xinzeng_shenhuo.setText(String.valueOf(Integer.valueOf(lifeDynamics)-Integer.valueOf(lifeDynamicsz)));
        }else {
            tv_xinzeng_shenhuo.setVisibility(View.INVISIBLE);
        }

        if (Integer.valueOf(locationDynamics)>Integer.valueOf(locationDynamicsz)){
            tv_xinzneg_zuobiao.setVisibility(View.VISIBLE);
            tv_xinzneg_zuobiao.setText(String.valueOf(Integer.valueOf(locationDynamics)-Integer.valueOf(locationDynamicsz)));
        }else {
            tv_xinzneg_zuobiao.setVisibility(View.INVISIBLE);
        }

        if (Integer.valueOf(businessDynamics)>Integer.valueOf(businessDynamicsz)){
            tv_xinzneg_shangye.setVisibility(View.VISIBLE);
            tv_xinzneg_shangye.setText(String.valueOf(Integer.valueOf(businessDynamics)-Integer.valueOf(businessDynamicsz)));
        }else {
            tv_xinzneg_shangye.setVisibility(View.INVISIBLE);
        }
        if (size==null||"".equals(size)||size.equalsIgnoreCase("null")){
            size = "0";
        }
        tv_zhuye.setText("个人主页("+homePage+")");
        tv_shenghuo.setText("派单动态("+lifeDynamics+")");
        tv_zuobiao.setText("坐标动态("+locationDynamics+")");
        tv_shangye.setText("商业动态("+businessDynamics+")");
        if (homePage.equals("0")&&lifeDynamics.equals("0")&&locationDynamics.equals("0")&&businessDynamics.equals("0")){
            arcView1.setVisibility(View.INVISIBLE);
            tv_none1.setVisibility(View.VISIBLE);
        }else {
            arcView1.setVisibility(View.VISIBLE);
            tv_none1.setVisibility(View.INVISIBLE);
            if (isfirst) {
                List<Times> times = new ArrayList<>();
                Times t1 = new Times();
                Times t2 = new Times();
                Times t3 = new Times();
                Times t4 = new Times();
                int peopleSum1 = 0, peopleSum2 = 0, peopleSum3 = 0, peopleSum4 = 0;
                peopleSum1 = Integer.valueOf(homePage);
                t1.hour = peopleSum1;
                t1.text = "个人主页";
                times.add(t1);
                peopleSum2 = Integer.valueOf(lifeDynamics);
                t2.hour = peopleSum2;
                t2.text = "派单动态";
                times.add(t2);
                peopleSum3 = Integer.valueOf(locationDynamics);
                t3.hour = peopleSum3;
                t3.text = "坐标动态";
                times.add(t3);
                peopleSum4 = Integer.valueOf(businessDynamics);
                t4.hour = peopleSum4;
                t4.text = "商业动态";
                times.add(t4);
                ArcView.ArcViewAdapter myAdapter = arcView1.new ArcViewAdapter<Times>() {
                    @Override
                    public double getValue(Times times) {
                        return times.hour;
                    }
                };//设置adapter
                myAdapter.setData(times);//设置数据集
                arcView1.setMaxNum(4);//设置可以显示的最大数值 该数值之后的会合并为其他
                isfirst = false;
            }
        }
        int zhufa = Integer.valueOf(homePage)+Integer.valueOf(lifeDynamics)+Integer.valueOf(locationDynamics)+Integer.valueOf(businessDynamics);
        tv_liulan_count.setText("转 发 "+zhufa+" 次");
    }
    static class Times {
        double hour;
        String text;
    }
}
