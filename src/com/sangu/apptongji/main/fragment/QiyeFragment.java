package com.sangu.apptongji.main.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.order.avtivity.SouSuoThreeActivity;
import com.sangu.apptongji.main.alluser.presenter.IProfilePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.ProfilePresenter;
import com.sangu.apptongji.main.alluser.view.IProfileView;
import com.sangu.apptongji.main.qiye.QiYeDetailsActivity;
import com.sangu.apptongji.main.qiye.adapter.QiyeListAdapter;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeListPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeListPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeListView;

import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-12-27.
 */

public class QiyeFragment extends Fragment implements IQiYeListView ,View.OnClickListener,IProfileView{
    private IQiYeListPresenter presenter=null;
    private IProfilePresenter profilePresenter=null;
    private XRecyclerView mRecyclerView=null;
    private List<QiYeInfo> list=new ArrayList<>();
    private QiyeListAdapter adapter=null;
    private ImageView iv_headview;
    private Bitmap bitmap;
    private RadioButton rbRShu=null;
    private RadioButton rbJy=null;
    private RadioButton rbBt=null;
    private RadioButton rbSs=null;
    View v,v5;
    private String lng, lat,name="",comName="0",comZhuy="0";
    private int currentPage = 1;
    private boolean isNext = false;
    private boolean isVisible = true;
    boolean isRshuChecked = false;
    boolean isJyChecked = false;
    boolean hasbao = false;
    boolean hasMore = true;
    View headView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_qiye,container,false);
        headView = inflater.inflate(R.layout.head_view, null);
        WeakReference<QiyeFragment> reference =  new WeakReference<QiyeFragment>(QiyeFragment.this);
        profilePresenter = new ProfilePresenter(getActivity(),reference.get());
        if (DemoHelper.getInstance().isLoggedIn(getActivity())){
            profilePresenter.updateData();
        }
        v5 = v.findViewById(R.id.v5);
        iv_headview = (ImageView) headView.findViewById(R.id.iv_headview);
        mRecyclerView = (XRecyclerView) v.findViewById(R.id.id_stickynavlayout_innerscrollview);
        rbRShu = (RadioButton) v.findViewById(R.id.radioRShu);
        rbJy = (RadioButton) v.findViewById(R.id.radioEP);
        rbBt = (RadioButton) v.findViewById(R.id.radioBT);
        rbSs = (RadioButton) v.findViewById(R.id.radioSS);
        rbBt.setEnabled(false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        WeakReference<QiyeFragment> reference =  new WeakReference<QiyeFragment>(QiyeFragment.this);
        presenter = new QiYeListPresenter(getActivity(),reference.get());
        rbRShu.setOnClickListener(reference.get());
        rbJy.setOnClickListener(reference.get());
        rbBt.setOnClickListener(reference.get());
        rbSs.setOnClickListener(reference.get());
        lng = TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng();
        lat = TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_dark);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

       // mRecyclerView.addHeaderView(headView);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                hasMore = true;
                isNext = false;
                presenter.loadQiyeList(currentPage+"",lat,lng,comName,comZhuy,isRshuChecked,isJyChecked,hasbao);
            }

            @Override
            public void onLoadMore() {
                isNext = true;
                currentPage = currentPage + 1;
                presenter.loadQiyeList(currentPage + "", lat, lng, comName, comZhuy, isRshuChecked, isJyChecked, hasbao);
            }
        });
        mRecyclerView.refresh(false);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (getActivity()!=null&&!isVisible) {
            Log.e("onLowMemory,qiye","内存不足");
            if (list != null) {
                list.clear();
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void onRefresh(){
        if (mRecyclerView!=null) {
            Bundle bundle = getArguments();
            String baozhjin = bundle.getString("baozhjin");
            comName = bundle.getString("qiye_mch");
            comZhuy = bundle.getString("qiye_zhy");
            String isclear = bundle.getString("isclear");
            if ("1".equals(isclear)) {
                name = "0";
                comName = "0";
                comZhuy = "0";
                hasbao = false;
            }
            Log.e("MainAc,qiye", "刷新qiye");
            if (baozhjin != null && !"".equals(baozhjin) && !"0".equals(baozhjin)) {
                isNext = false;
                hasbao = true;
                currentPage = 1;
                mRecyclerView.refresh(false);
            } else {
                isNext = false;
                hasbao = false;
                currentPage = 1;
                mRecyclerView.refresh(false);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null) {
            switch (requestCode) {
                case 0:
                    String baozhjin = data.getStringExtra("baozhjin");
                    String qiye_mch = data.getStringExtra("qiye_mch");
                    String qiye_zhy = data.getStringExtra("qiye_zhy");
                    comName = qiye_mch;
                    comZhuy = qiye_zhy;
                    currentPage = 1;
                    if (baozhjin != null && !"".equals(baozhjin) && !"0".equals(baozhjin)) {
                        isNext=false;
                        hasbao = true;
                        mRecyclerView.refresh(false);
                    } else {
                        isNext=false;
                        hasbao = false;
                        mRecyclerView.refresh(false);
                    }
                    break;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (list==null||list.size()==0) {
                if (presenter!=null) {
                    currentPage = 1;
                    hasMore = true;
                    isNext = false;
                    mRecyclerView.refresh(false);
                }
            }
            if (presenter!=null){
                isVisible = true;
            }
        }else {
            isVisible = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radioRShu:
                isRshuChecked = !isRshuChecked;
                isJyChecked = false;
                rbJy.setChecked(isJyChecked);
                rbRShu.setChecked(isRshuChecked);
                isNext = false;
                currentPage = 1;
                mRecyclerView.refresh(false);
                break;
            case R.id.radioEP:
                isJyChecked = !isJyChecked;
                isRshuChecked = false;
                rbRShu.setChecked(isRshuChecked);
                rbJy.setChecked(isJyChecked);
                isNext = false;
                currentPage = 1;
                mRecyclerView.refresh(false);
                break;
            case R.id.radioSS:
                rbSs.setChecked(false);
                startActivityForResult(new Intent(getActivity(),SouSuoThreeActivity.class),0);
                break;
        }
    }
    @Override
    public void updateUserList(final List<QiYeInfo> users,boolean hasMore) {
        this.hasMore = hasMore;
        if (isNext) {
            list.addAll(users);
            mRecyclerView.loadMoreComplete();
            adapter.notifyDataSetChanged();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        } else {
            this.list = users;
            Log.e("qiyefrag,li",list.size()+"");
            adapter = new QiyeListAdapter(getActivity(),list);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.refreshComplete();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
            adapter.setOnItemClickListener(new QiyeListAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (position==0||position==1){
                        return;
                    }else {
                        QiYeInfo users = list.get(position-2);
                        String qiyeId = users.getCompanyId();
                        Intent intent = new Intent(getActivity(), QiYeDetailsActivity.class);
                        intent.putExtra("qiyeId", qiyeId);
                        intent.putExtra("biaoshi", "01");
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                }
            });
            getBitmapFromSharedPreferences();
        }
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

    private void getBitmapFromSharedPreferences(){
        if (getActivity()!=null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sangu_ditucut", getActivity().MODE_PRIVATE);
            //第一步:取出字符串形式的Bitmap
            String imageString = sharedPreferences.getString("image", "");
            //第二步:利用Base64将字符串转换为ByteArrayInputStream
            byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
            if (byteArray.length == 0) {
                return;
            } else {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                //第三步:利用ByteArrayInputStream生成Bitmap
                bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
                iv_headview.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateUserInfo(Userful user) {
        name = user.getName();
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
