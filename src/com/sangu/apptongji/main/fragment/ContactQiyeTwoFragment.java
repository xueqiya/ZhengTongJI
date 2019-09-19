package com.sangu.apptongji.main.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fanxin.easeui.EaseConstant;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuQiyeLocationActivity;
import com.sangu.apptongji.main.qiye.adapter.MemberAdapter;
import com.sangu.apptongji.main.qiye.entity.MemberInfo;
import com.sangu.apptongji.main.qiye.presenter.IMemberPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.MemberPresenterTwo;
import com.sangu.apptongji.main.qiye.view.IMemberView2;
import com.sangu.apptongji.ui.ChatActivity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-01-04.
 */

public class ContactQiyeTwoFragment extends Fragment implements IMemberView2 {
    private XRecyclerView mRecyclerView=null;
    private MemberAdapter adapter=null;
    private TextView tv_join_qymsg=null;
    private List<MemberInfo> memberInfos=null;
    private IMemberPresenter memberPresenter=null;
    private ImageView iv_headview;
    private boolean isFirst = true;
    private int currentPage=1;
    private boolean isNext = false;
    private boolean isVisible = true;
    View headView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_qiye_two,container,false);
        headView = inflater.inflate(R.layout.head_view, null);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        memberInfos = new ArrayList<>();
        final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        memberPresenter = new MemberPresenterTwo(getActivity(),this);
        iv_headview = (ImageView) headView.findViewById(R.id.iv_headview);
        mRecyclerView = (XRecyclerView) getView().findViewById(R.id.refresh_list);
        tv_join_qymsg = (TextView) getView().findViewById(R.id.tv_join_qymsg);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_dark);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.addHeaderView(headView);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                isNext = false;
                if (memberPresenter!=null) {
                    memberPresenter.loadMemberList(currentPage);
                }
            }
            @Override
            public void onLoadMore() {
                Log.e("contact,","加载更多");
                currentPage++;
                isNext = true;
                if (memberPresenter!=null) {
                    memberPresenter.loadMemberList(currentPage);
                }
            }
        });
        tv_join_qymsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                String comImg = bundle.getString(EaseConstant.EXTRA_USER_IMG);
                String comName = bundle.getString(EaseConstant.EXTRA_USER_NAME);
                String shareRed = bundle.getString(EaseConstant.EXTRA_USER_SHARERED);
                String imgUrl=null;
                if (comImg!=null&&!"".equals(comImg)){
                    imgUrl = comImg.split("\\|")[0];
                }
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                // it is group chat
                intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                intent.putExtra("userId", qiyeId);
                intent.putExtra(EaseConstant.EXTRA_USER_TYPE,"企业");
                intent.putExtra(EaseConstant.EXTRA_USER_IMG,imgUrl);
                intent.putExtra(EaseConstant.EXTRA_USER_NAME,comName);
                intent.putExtra(EaseConstant.EXTRA_USER_SHARERED,shareRed);
                startActivityForResult(intent,21);
            }
        });
        if (memberPresenter!=null) {
            currentPage=1;
            isNext = false;
            memberPresenter.loadMemberList(currentPage);
        }
    }

    public void onrefresh(){
        if (memberPresenter!=null) {
            currentPage=1;
            isNext = false;
            memberPresenter.loadMemberList(currentPage);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (getActivity()!=null&&!isVisible) {
            if (memberInfos != null) {
                memberInfos.clear();
            }
            if (adapter != null && mRecyclerView != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void getBitmapFromSharedPreferences(){
        if (isFirst) {
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
                Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
                iv_headview.setImageBitmap(bitmap);
                isFirst = false;
            }
        }
    }

    @Override
    public void updateUserList(List<MemberInfo> users,String total,boolean hasMore) {
        if (users!=null) {
            ((ContactListFragment) (ContactQiyeTwoFragment.this.getParentFragment())).showQiYeSize(total + "");
        }else {
            ((ContactListFragment) (ContactQiyeTwoFragment.this.getParentFragment())).showQiYeSize(total);
        }
        if (memberInfos==null){
            memberInfos = new ArrayList<>();
        }
        if (isNext) {
            memberInfos.addAll(users);
            mRecyclerView.loadMoreComplete();
            adapter.notifyDataSetChanged();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        }else {
            this.memberInfos = users;
            adapter = new MemberAdapter(memberInfos, getActivity());
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.refreshComplete();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
            adapter.setOnItemClickListener(new MemberAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    MemberInfo users = memberInfos.get(position - 2);
                    String hxid = users.getuLoginId();
                    Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                    intent.putExtra("hxid", hxid);
                    startActivity(intent);
                }
            });
            headView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), BaiDuQiyeLocationActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
        }
        getBitmapFromSharedPreferences();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (memberPresenter!=null) {
            currentPage=1;
            isNext = false;
            memberPresenter.loadMemberList(currentPage);
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (memberInfos==null||memberInfos.size()==0){
                if (memberPresenter!=null) {
                    currentPage=1;
                    isNext = false;
                    memberPresenter.loadMemberList(currentPage);
                }
            }
            if (memberPresenter!=null) {
                isVisible = true;
            }
        }else {
            isVisible = false;
        }
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showError() {
        if (mRecyclerView!=null){
            mRecyclerView.refreshComplete();
        }
        Toast.makeText(getActivity(),"网络连接中断",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (memberInfos!=null){
            memberInfos.clear();
            memberInfos=null;
        }
    }
}
