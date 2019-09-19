/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sangu.apptongji.main.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanxin.easeui.EaseConstant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.activity.AddFriendsNextActivity;
import com.sangu.apptongji.main.activity.HidenDynamicActivity;
import com.sangu.apptongji.main.activity.NewFriendsActivity;
import com.sangu.apptongji.main.activity.RegionRankActivity;
import com.sangu.apptongji.main.alluser.entity.Friend;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.presenter.IFriendPresenter;
import com.sangu.apptongji.main.alluser.presenter.IProfilePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.FriendQPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.ProfilePresenter;
import com.sangu.apptongji.main.alluser.view.IFriendView;
import com.sangu.apptongji.main.alluser.view.IProfileView;
import com.sangu.apptongji.main.qiye.NewMajorActivity;
import com.sangu.apptongji.main.qiye.entity.MSG;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IMajQPresenter;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.MajQPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQMajView;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.service.AlermService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**

 * contact list
 *
 */
public class ContactListFragment extends Fragment implements OnClickListener
        ,IFriendView, IQiYeDetailView ,IProfileView,IQMajView {
    private final static int MSG_REFRESH = 2;
    private View view=null;
    private IMajQPresenter majQPresenter=null;
    private IFriendPresenter friendPresenter=null;
    private IProfilePresenter profilePresenter=null;
    private ImageView tv_add=null;
    private ViewPager mPaper=null;
    private Bundle bundle;
    ContactQunzuFragment qunzuFragment = null;
    ContactChildListFragment contactFragment = null;
    ContactQiyeTwoFragment qiyeTwoFragment = null;
    ContactQiyeFragment qiyeFragment = null;
    Fragment[] mFragments=null;
    private IQiYeInfoPresenter presenter=null;
    private int currentIndex;
    boolean[] fragmentsUpdateFlag = { false, false };
    private boolean hasFriendq = false;
    private boolean hasqiyeq = false;
    private boolean iscreat = false;
    private boolean isShowQiye = false;
    private boolean isfirst = true;
    private String qiyeId,companyName,comAddress,remark,resv6="";
    private FragmentPagerAdapter mAdapter=null;
    private TextView tv_friend_count=null,tv_qiye_count=null,tv_qunzu_count=null;
    private TextView tv_friend=null,tv_qiye=null,tvUnreadf=null,tvUnreadq=null,tv_qunzu=null,tvUnreadQz;
    private RelativeLayout rl_friend,rl_qiye,rl_qunzu;
    private int currentPage=1;
    private int lastVisibleIndex;

    private RelativeLayout rl_tongji1,rl_tongji2;

    private Handler mainHandler = new Handler() {
        /*
         * （非 Javadoc）
         *
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG.INTO_03:
                    if (!mFragments[1].equals(qiyeTwoFragment)) {
                        mFragments[1] = qiyeTwoFragment;
                        fragmentsUpdateFlag[1] = true;
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 0:
                    try {
                        String queryInfo = (String) msg.obj;
                        JSONObject object = new JSONObject(queryInfo);
                        JSONArray queryList = object.getJSONArray("friendRequestList");
                        if (queryList.length()>0){
                            hasqiyeq = true;
                            tvUnreadq.setVisibility(View.VISIBLE);
                            tvUnreadq.setText(queryList.length()+"");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_REFRESH:
                    if (iscreat) {
                        if (qiyeId == null || "".equals(qiyeId)) {
                            profilePresenter.updateData();
                        }
                        if ("00".equals(resv6) || ("01".equals(resv6) && qiyeId != null && qiyeId.length() > 0)) {
                            if ("1".equals(remark) && mFragments[1].equals(qiyeFragment)) {
                                mainHandler.sendEmptyMessage(5);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void showFriendSize(String size) {
        tv_friend_count.setText("("+size+")");
    }
    public void showQiYeSize(String size) {
        tv_qiye_count.setText("("+size+")");
    }
    public void showQunZuSize(String size) {
        tv_qunzu_count.setText("("+size+")");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);
//        majQPresenter = new MajQPresenter(getActivity(),this);
//        profilePresenter = new ProfilePresenter(getActivity(),this);
//        presenter = new QiYeInfoPresenter(getActivity(),this);
//        friendPresenter = new FriendQPresenter(getActivity(),this);
//        qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
//        companyName = DemoApplication.getInstance().getCurrentCompanyName();
//        comAddress = DemoApplication.getInstance().getCurrentComAddress();
//        remark = DemoApplication.getInstance().getCurrentQiYeRemark();
//        resv6 = DemoApplication.getInstance().getCurrentResv6();
        return view;
    }

    private void initView(){

        tv_add = (ImageView) view.findViewById(R.id.tv_add);
        tv_add.setOnClickListener(this);


        rl_tongji1 = (RelativeLayout) view.findViewById(R.id.rl_tongji1);

        rl_tongji1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),RegionRankActivity.class);

                startActivity(intent);

            }
        });

        rl_tongji2 = (RelativeLayout) view.findViewById(R.id.rl_tongji2);
        rl_tongji2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),HidenDynamicActivity.class);

                startActivity(intent);

            }
        });



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initView();


//        bundle = new Bundle();
//        initLayout();
//        presenter.loadQiYeInfo(qiyeId);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//            mPaper.setOffscreenPageLimit(2);
//        }else {
//            mPaper.setOffscreenPageLimit(1);
//        }
//        if (("1".equals(remark)&&"00".equals(resv6))||"01".equals(resv6)){
//            mFragments = new Fragment[]{contactFragment,qiyeTwoFragment,qunzuFragment};
//        }else {
//            mFragments = new Fragment[]{contactFragment,qiyeFragment,qunzuFragment};
//        }
//        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
//            FragmentManager fm = getChildFragmentManager();
//            @Override
//            public int getCount() {
//                return mFragments.length;
//            }
//            @Override
//            public Fragment getItem(int position) {
//                return mFragments[position % mFragments.length];
//            }
//
//            @Override
//            public int getItemPosition(Object object) {
//                return POSITION_NONE;
//            }
//
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
//                //得到缓存的fragment
//                Fragment fragment = (Fragment) super.instantiateItem(container,
//                        position);
//                //得到tag，这点很重要
//                String fragmentTag = fragment.getTag();
//
//                if (fragmentsUpdateFlag[position % fragmentsUpdateFlag.length]) {
//                    //如果这个fragment需要更新
//                    FragmentTransaction ft = fm.beginTransaction();
//                    //移除旧的fragment
//                    ft.remove(fragment);
//                    //换成新的fragment
//                    fragment = mFragments[position % mFragments.length];
//                    //添加新fragment时必须用前面获得的tag，这点很重要
//                    ft.add(container.getId(), fragment, fragmentTag);
//                    ft.attach(fragment);
//                    ft.commit();
//                    //复位更新标志
//                    fragmentsUpdateFlag[position % fragmentsUpdateFlag.length] = false;
//                }
//                return fragment;
//            }
//        };
//        mPaper.setAdapter(mAdapter);
//        mPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                resetColor();
//                switch (position) {
//                    case 0:
//                        tv_friend.setTextColor(Color.rgb(33,150,243));
//                        tv_friend_count.setTextColor(Color.rgb(255,99,71));
//                        break;
//                    case 1:
//                        tv_qiye.setTextColor(Color.rgb(33,150,243));
//                        tv_qiye_count.setTextColor(Color.rgb(255,99,71));
//                        break;
//                    case 2:
//                        tv_qunzu.setTextColor(Color.rgb(33,150,243));
//                        tv_qunzu_count.setTextColor(Color.rgb(255,99,71));
//                        break;
//                    default:
//                        break;
//                }
//                currentIndex = position;
//            }
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            }
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//            }
//        });
//        iscreat = true;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (contactFragment!=null){
            contactFragment.onLowMemory();
        }
        if (qiyeTwoFragment!=null){
            qiyeTwoFragment.onLowMemory();
        }
        if (qunzuFragment!=null){
            qunzuFragment.onLowMemory();
        }
    }

    @Override
    public void updateUserInfo(Userful user) {
        qiyeId = user.getResv5();
        resv6 = user.getResv6();
        if ("00".equals(resv6)||("01".equals(resv6)&&qiyeId!=null&&qiyeId.length()>0)){
            if ("1".equals(remark)&&mFragments[1].equals(qiyeFragment)) {
                mainHandler.sendEmptyMessage(5);
            }
        }
        isfirst = false;
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
    public void onResume() {
//        qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
//        resv6 = DemoApplication.getInstance().getCurrentResv6();
//        if (qiyeId==null||"".equals(qiyeId)||isfirst){
//            profilePresenter.updateData();
//        }
//        hasFriendq = false;
//        hasqiyeq = false;
//        tvUnreadf.setVisibility(View.INVISIBLE);
//        tvUnreadq.setVisibility(View.INVISIBLE);
//        friendPresenter.loadFriendQList();
//        if ("00".equals(resv6)) {
//            majQPresenter.loadMajQList();
//        }
//        if ("00".equals(resv6)||("01".equals(resv6)&&qiyeId!=null&&qiyeId.length()>0)){
//            if ("1".equals(remark)&&mFragments[1].equals(qiyeFragment)) {
//                mainHandler.sendEmptyMessage(5);
//            }
//        }
        super.onResume();
    }
    @Override
    public void updateQiyeInfo(QiYeInfo qiYeInfo) {
        String loginTime = TextUtils.isEmpty(qiYeInfo.getLoginTime()) ? null : qiYeInfo.getLoginTime();
        if (loginTime!=null&&!"".equals(loginTime)&&loginTime.length() > 0) {
            String shangbanTime = loginTime.split("\\|")[0];
            String xiabanTime = loginTime.split("\\|")[1];
            if (shangbanTime!=null&&!"".equals(shangbanTime)){
                SharedPreferences sp = getActivity().getSharedPreferences("sangu_qiandao_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(qiyeId+"_qiandao_shb",shangbanTime);
                editor.putString(qiyeId+"_qiandao_xb",xiabanTime);
                editor.commit();
                Intent startAlarmServiceIntent=new Intent(getActivity(), AlermService.class);
                getActivity().startService(startAlarmServiceIntent);
            }
        }
        String comName;
        comName = qiYeInfo.getCompanyName();
        if (comName!=null){
            try {
                comName = URLDecoder.decode(comName,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String shareRed = qiYeInfo.getShareRed();
        final String friendsNumber = qiYeInfo.getFriendsNumber();
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            shareRed = "有";
        }else {
            shareRed = "无";
        }
        bundle.putString(EaseConstant.EXTRA_USER_IMG,qiYeInfo.getComImage());
        bundle.putString(EaseConstant.EXTRA_USER_NAME,comName);
        bundle.putString(EaseConstant.EXTRA_USER_SHARERED,shareRed);
        remark = TextUtils.isEmpty(qiYeInfo.getRemark())?"0":qiYeInfo.getRemark();
    }

    public void refresh() {
        if (!mainHandler.hasMessages(MSG_REFRESH)) {
            mainHandler.sendEmptyMessage(MSG_REFRESH);
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

    @Override
    public void updateMajQList(Object obj) {
        Message msg = new Message();
        msg.what = 0;
        msg.obj = obj ;
        mainHandler.sendMessage(msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case 0:
//                contactFragment.onrefresh();
//                break;
//            case 1:
//                qiyeTwoFragment.onrefresh();
//                break;
//            case 2:
//                qunzuFragment.onrefresh();
//                break;
//        }
    }
    @Override
    public void updateFriendQList(List<Friend> friends) {
        if (friends.size()>0) {
            hasFriendq = true;
            tvUnreadf.setVisibility(View.VISIBLE);
            tvUnreadf.setText(friends.size()+"");
        }
    }

    private void initLayout() {
        rl_friend = (RelativeLayout) view.findViewById(R.id.rl_friend);
        rl_qiye = (RelativeLayout) view.findViewById(R.id.rl_qiye);
        rl_qunzu = (RelativeLayout) view.findViewById(R.id.rl_qunzu);
        tv_add = (ImageView) view.findViewById(R.id.tv_add);
        mPaper = (ViewPager)view.findViewById(R.id.vp);
        tvUnreadf = (TextView) view.findViewById(R.id.tv_unread_friend);
        tvUnreadq = (TextView) view.findViewById(R.id.tv_unread_qiye);
        tvUnreadQz = (TextView) view.findViewById(R.id.tv_unread_qunzu);
        tv_qunzu = (TextView) view.findViewById(R.id.tv_qunzu);
        tv_qiye = (TextView) view.findViewById(R.id.tv_qiye);
        tv_qiye = (TextView) view.findViewById(R.id.tv_qiye);
        tv_friend = (TextView) view.findViewById(R.id.tv_friend);
        tv_qunzu_count = (TextView) view.findViewById(R.id.tv_qunzu_count);
        tv_friend_count = (TextView) view.findViewById(R.id.tv_friend_count);
        tv_qiye_count = (TextView) view.findViewById(R.id.tv_qiye_count);
        tv_add.setOnClickListener(this);
        rl_friend.setOnClickListener(this);
        rl_qiye.setOnClickListener(this);
        rl_qunzu.setOnClickListener(this);
        contactFragment = new ContactChildListFragment();
        qiyeTwoFragment = new ContactQiyeTwoFragment();
        qiyeFragment = new ContactQiyeFragment();
        qunzuFragment = new ContactQunzuFragment();
        qiyeTwoFragment.setArguments(bundle);
    }

    private void resetColor() {
        tv_friend.setTextColor(Color.rgb(170,170,170));
        tv_qiye.setTextColor(Color.rgb(170,170,170));
        tv_qunzu.setTextColor(Color.rgb(170,170,170));
        tv_friend_count.setTextColor(Color.rgb(170,170,170));
        tv_qiye_count.setTextColor(Color.rgb(170,170,170));
        tv_qunzu_count.setTextColor(Color.rgb(170,170,170));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_friend:
                if (currentIndex==0&&hasFriendq){
                        startActivityForResult(new Intent(getActivity(), NewFriendsActivity.class),0);
                }
                resetColor();
                tv_friend.setTextColor(Color.rgb(33,150,243));
                tv_friend_count.setTextColor(Color.rgb(255,99,71));
                mPaper.setCurrentItem(0);
                currentIndex = 0;
                break;
            case R.id.rl_qiye:
                if (currentIndex==1&&hasqiyeq){
                    startActivityForResult(new Intent(getActivity(),NewMajorActivity.class)
                            .putExtra("companyName",companyName).putExtra("comAddress",comAddress),1);
                }
                resetColor();
                tv_qiye.setTextColor(Color.rgb(33,150,243));
                tv_qiye_count.setTextColor(Color.rgb(255,99,71));
                mPaper.setCurrentItem(1);
                currentIndex = 1;
                break;
            case R.id.rl_qunzu:
                resetColor();
                tv_qunzu.setTextColor(Color.rgb(33,150,243));
                tv_qunzu_count.setTextColor(Color.rgb(255,99,71));
                mPaper.setCurrentItem(2);
                currentIndex = 2;
                break;
            case R.id.tv_add:
                startActivityForResult(new Intent(getActivity(), AddFriendsNextActivity.class),0);
                break;
            default:
                break;
        }
    }
}
