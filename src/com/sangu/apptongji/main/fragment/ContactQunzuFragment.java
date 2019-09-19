package com.sangu.apptongji.main.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.fanxin.easeui.EaseConstant;
import com.fanxin.easeui.ui.EaseGroupRemoveListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.adapter.group.GroupListAdapter;
import com.sangu.apptongji.main.alluser.entity.GroupInfo;
import com.sangu.apptongji.main.alluser.order.avtivity.FriendCheckActivity;
import com.sangu.apptongji.main.alluser.presenter.IGroupPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.GroupPresenter;
import com.sangu.apptongji.main.alluser.view.IGroupView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.ChatActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-11-06.
 */

public class ContactQunzuFragment extends Fragment implements View.OnClickListener,IGroupView{
    View v,headView;
    private IGroupPresenter groupPresenter;
    private GroupListAdapter adapter;
    private ImageView iv_headview;
    private RelativeLayout rl_create_qunzu;
    private XRecyclerView mRecyclerView;
    private List<GroupInfo> datas = new ArrayList<>();
    private int currentPage=1;
    private boolean isNext = false;
    private boolean isVisible = false;
    private GroupListener groupListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_contact_qunzu,container, false);
        RequestQueue queue = MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        headView = inflater.inflate(R.layout.head_view, null);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        groupPresenter = new GroupPresenter(getActivity(),this);
        iv_headview = (ImageView) headView.findViewById(R.id.iv_headview);
        rl_create_qunzu = (RelativeLayout) v.findViewById(R.id.rl_create_qunzu);
        mRecyclerView = (XRecyclerView) v.findViewById(R.id.refresh_list);
        rl_create_qunzu.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_dark);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
//        mRecyclerView.addHeaderView(headView);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                isNext = false;
                if (groupPresenter!=null) {
                    groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
                }
            }
            @Override
            public void onLoadMore() {
                Log.e("contact,","加载更多");
                currentPage++;
                isNext = true;
                if (groupPresenter!=null) {
                    groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
                }
            }
        });
        mRecyclerView.refresh(false);
        groupListener = new GroupListener();
        EMClient.getInstance().groupManager().addGroupChangeListener(groupListener);
    }

    public void onrefresh(){

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (datas==null||datas.size()==0){
                if (groupPresenter!=null) {
                    isNext = false;
                    groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
                }
            }
            if (groupPresenter!=null){
                isVisible = true;
            }
        }else {
            isVisible = false;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (getActivity()!=null&&!isVisible){
            if (datas != null) {
                datas.clear();
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_create_qunzu:
                    startActivityForResult(new Intent(getActivity(), FriendCheckActivity.class),0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK){
            mRecyclerView.refresh(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (groupListener != null) {
            EMClient.getInstance().groupManager().removeGroupChangeListener(groupListener);
        }
    }

    @Override
    public void updataGroupList(final List<GroupInfo> groupInfos, boolean hasMore) {
        if (groupInfos!=null) {
            ((ContactListFragment) (ContactQunzuFragment.this.getParentFragment())).showQunZuSize(groupInfos.size() + "");
        }else {
            ((ContactListFragment) (ContactQunzuFragment.this.getParentFragment())).showQunZuSize("0");
        }
        if (!isNext) {
            datas = groupInfos;
            adapter = new GroupListAdapter(getActivity(), datas);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.refreshComplete();
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    GroupInfo groupInfo = groupInfos.get(position-1);
                    String groupMember = groupInfo.getGroupMember();
                    String qunzuImg = groupInfo.getGroupImage();
                    String qunzuName = groupInfo.getGroupName()+"("+groupMember+")";
                    String shareRed = "无";
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    // it is group chat
                    intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, groupInfo.getGroupId());
                    intent.putExtra(EaseConstant.EXTRA_USER_TYPE,"群组");
                    intent.putExtra(EaseConstant.EXTRA_USER_IMG,qunzuImg);
                    intent.putExtra(EaseConstant.EXTRA_USER_NAME,qunzuName);
                    intent.putExtra(EaseConstant.EXTRA_USER_SHARERED,shareRed);
                    startActivityForResult(intent,21);
                }
                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }else {
            datas.addAll(groupInfos);
            adapter.notifyDataSetChanged();
        }
        if (!hasMore){
            mRecyclerView.setNoMore(true);
        }
    }

    class GroupListener extends EaseGroupRemoveListener {

        @Override
        public void onRequestToJoinReceived(String s, String s1, String s2, String s3) {

        }

        @Override
        public void onRequestToJoinAccepted(String s, String s1, String s2) {

        }

        @Override
        public void onRequestToJoinDeclined(String s, String s1, String s2, String s3) {

        }

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            if (groupPresenter!=null) {
                groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
            }
        }

        @Override
        public void onGroupDestroyed(final String groupId, String groupName) {
            if (groupPresenter!=null) {
                groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
            }
        }

        @Override
        public void onMuteListAdded(String s, List<String> list, long l) {
            if (groupPresenter!=null) {
                groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
            }
        }

        @Override
        public void onMuteListRemoved(String s, List<String> list) {
            if (groupPresenter!=null) {
                groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
            }
        }

        @Override
        public void onAdminAdded(String s, String s1) {
            if (groupPresenter!=null) {
                groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
            }
        }

        @Override
        public void onAdminRemoved(String s, String s1) {
            if (groupPresenter!=null) {
                groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
            }
        }

        @Override
        public void onOwnerChanged(String s, String s1, String s2) {
            if (groupPresenter!=null) {
                groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
            }
        }

        @Override
        public void onMemberJoined(String s, String s1) {
            if (groupPresenter!=null) {
                groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
            }
        }

        @Override
        public void onMemberExited(String s, String s1) {
            if (groupPresenter!=null) {
                groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
            }
        }

        @Override
        public void onAnnouncementChanged(String s, String s1) {
            if (groupPresenter!=null) {
                groupPresenter.loadGroupList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
            }
        }

        @Override
        public void onSharedFileAdded(String s, EMMucSharedFile emMucSharedFile) {

        }

        @Override
        public void onSharedFileDeleted(String s, String s1) {

        }

    }

}
