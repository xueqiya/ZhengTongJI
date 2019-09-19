package com.sangu.apptongji.main.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.NewFriendsActivity;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.adapter.FriendListAdapter;
import com.sangu.apptongji.main.address.AddressListActivity;
import com.sangu.apptongji.main.address.KyqInfo;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuFLocationActivity;
import com.sangu.apptongji.main.alluser.presenter.IFriendListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.FriendListPresenter;
import com.sangu.apptongji.main.alluser.view.IFriendListView;
import com.sangu.apptongji.main.callback.IError;
import com.sangu.apptongji.main.callback.ISuccess;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.SyAddressBookUtil;

import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-12-27.
 */

public class ContactChildListFragment extends Fragment implements View.OnClickListener,IFriendListView {
    private XRecyclerView mRecyclerView;
    private List<UserAll> list;
    private FriendListAdapter adapter;
    private IFriendListPresenter presenter;
    private TextView tv1,tv2,tv_search_friends;
    private RelativeLayout rl_search_friends;
    private ImageView iv_headview;
    private boolean isVisible = true;
    private int currentPage=1;
    private boolean hasMore;
    private boolean isNext = false;
    private boolean showTishi = true;
    private double per=0.0;
    private Context context;
    View v,headView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        v = inflater.inflate(R.layout.fx_item_contact_list_header,container, false);
        RequestQueue queue = MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        headView = inflater.inflate(R.layout.head_view, null);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new FriendListPresenter(getActivity(),this);
        iv_headview = (ImageView) headView.findViewById(R.id.iv_headview);
        rl_search_friends = (RelativeLayout) v.findViewById(R.id.rl_search_friends);
        tv1 = (TextView) v.findViewById(R.id.tv1);
        tv2 = (TextView) v.findViewById(R.id.tv2);
        tv_search_friends = (TextView) v.findViewById(R.id.tv_search_friends);
        rl_search_friends.setOnClickListener(this);
        list = new ArrayList<>();
        initView();
    }

    public void onrefresh(){
        if (presenter!=null) {
            isNext = false;
            hasMore = true;
            presenter.loadFriendList("1");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (list==null||list.size()==0){
                if (presenter!=null) {
                    isNext = false;
                    hasMore = true;
                    presenter.loadFriendList("1");
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
    public void onLowMemory() {
        super.onLowMemory();
        if (getActivity()!=null&&!isVisible) {
            if (list != null) {
                list.clear();
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void getBitmapFromSharedPreferences(){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("sangu_ditucut", getActivity().getApplicationContext().MODE_PRIVATE);
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
        }
    }
    private void initView() {
        v.findViewById(R.id.re_newfriends).setOnClickListener(this);
        mRecyclerView = (XRecyclerView) v.findViewById(R.id.refresh_list);
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
                presenter.loadFriendList(currentPage+"");
            }

            @Override
            public void onLoadMore() {
                currentPage = currentPage + 1;
                isNext = true;
                presenter.loadFriendList(currentPage+"");
            }
        });
        presenter.loadFriendList(currentPage+"");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                tv1.setVisibility(View.GONE);
                tv2.setVisibility(View.GONE);
                break;
//            case PermissionsActivity.PERMISSIONS_GRANTED:
//                startActivityForResult(new Intent(getActivity(), AddressListActivity.class),0);
//                break;
//            case PermissionsActivity.PERMISSIONS_DENIED:
//                Toast.makeText(getActivity(),"您拒绝了访问通讯录权限，请手动前往设置打开权限",Toast.LENGTH_SHORT).show();
//                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity()!=null){
            if (per==0){
                tv_search_friends.setText("通讯录同步（"+0+"%)");
            }else {
                tv_search_friends.setText("通讯录同步（"+(int)per+"%)");
            }
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.GONE);
                tv_search_friends.setText("通讯录同步");
            }else if (showTishi){
                searchFriend();
            }
        }
    }

    private void searchFriend() {
        MyRunnable runnable = new MyRunnable(ContactChildListFragment.this);
        new Thread(runnable).start();
    }

    private static class MyRunnable implements Runnable{

        WeakReference<ContactChildListFragment> mactivity;

        public MyRunnable(ContactChildListFragment activity){
            mactivity = new WeakReference<ContactChildListFragment>(activity);
        }

        @Override
        public void run() {
            SyAddressBookUtil.builder()
                    .loader(mactivity.get().getContext())
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(List<KyqInfo> listsKyq, List<String> list_selected, org.json.JSONArray arrayKyq,double per) {
                            Log.e("syaddress,per",per+"");
                            mactivity.get().per=per;
                            if (arrayKyq!=null&&arrayKyq.length()>0) {
                                int size = arrayKyq.length();
                                if (size>99){
                                    size = 99;
                                }
                                mactivity.get().tv1.setVisibility(View.GONE);
                                mactivity.get().tv2.setVisibility(View.VISIBLE);
                                mactivity.get().tv2.setText(size + "");
                            }else {
                                mactivity.get().tv1.setVisibility(View.GONE);
                                mactivity.get().tv2.setVisibility(View.GONE);
                            }
                            mactivity.get().showTishi = false ;
                            if (per==0){
                                mactivity.get().tv_search_friends.setText("通讯录同步（"+0+"%)");
                            }else {
                                mactivity.get().tv_search_friends.setText("通讯录同步（"+(int)per+"%)");
                            }
                        }
                    })
                    .error(new IError() {
                        @Override
                        public void onError(String msg) {
                        }
                    })
                    .build()
                    .getKyqList();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (list!=null){
            list.clear();
            list=null;
        }
    }

    @Override
    public void updateUserOrderList(List<UserAll> users,boolean hasMore) {
        if (users!=null) {
            ((ContactListFragment) (ContactChildListFragment.this.getParentFragment())).showFriendSize(users.size() + "");
        }else {
            ((ContactListFragment) (ContactChildListFragment.this.getParentFragment())).showFriendSize("0");
        }
        if (users==null){
            if (isNext){
                mRecyclerView.loadMoreComplete();
            }else {
                mRecyclerView.refreshComplete();
            }
        }else {
            this.hasMore = hasMore;
            if (isNext) {
                list.addAll(users);
                mRecyclerView.loadMoreComplete();
                adapter.notifyDataSetChanged();
                if (!hasMore) {
                    mRecyclerView.setNoMore(true);
                }
            } else {
                this.list = users;
                adapter = new FriendListAdapter(list, getActivity());
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.refreshComplete();
                if (!hasMore) {
                    mRecyclerView.setNoMore(true);
                }
                adapter.setOnItemClickListener(new FriendListAdapter.MyItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String resv4 = list.get(position - 2).getResv4();
                        if ("00".equals(resv4)) {
                            LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                            final Dialog dialog2 = new AlertDialog.Builder(getActivity(),R.style.Dialog).create();
                            dialog2.show();
                            dialog2.getWindow().setContentView(layout);
                            dialog2.setCanceledOnTouchOutside(false);
                            dialog2.setCancelable(false);
                            TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                            Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                            tv_title.setText("对方已下线,无法操作");
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                }
                            });
                        } else {
                            String username = list.get(position - 2).getuLoginId();
                            startActivity(new Intent(getActivity(), UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, username));
                        }
                    }
                });
            }
            if (users.size() > 0) {
                final String[] strLat = new String[users.size()];
                final String[] strLong = new String[users.size()];
                final String[] strLoginId = new String[users.size()];
                final String[] strName = new String[users.size()];
                final String[] strSex = new String[users.size()];
                for (int i = 0; i < users.size(); i++) {
                    strLong[i] = users.get(i).getResv1();
                    strLat[i] = users.get(i).getResv2();
                    strLoginId[i] = users.get(i).getuLoginId();
                    strName[i] = users.get(i).getuName();
                    strSex[i] = TextUtils.isEmpty(users.get(i).getuSex()) ? "01" : users.get(i).getuSex();
                }
                headView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String lat = DemoApplication.getInstance().getCurrentLat();
                        String lng = DemoApplication.getInstance().getCurrentLng();
                        Intent intent = new Intent(getActivity(), BaiDuFLocationActivity.class);
                        intent.putExtra("lat", strLat);
                        intent.putExtra("lng", strLong);
                        intent.putExtra("mylat", lat);
                        intent.putExtra("mylng", lng);
                        intent.putExtra("loginId", strLoginId);
                        intent.putExtra("name", strName);
                        intent.putExtra("sex", strSex);
                        startActivity(intent);
                    }
                });
            }
            getBitmapFromSharedPreferences();
            if (per==0){
                tv_search_friends.setText("通讯录同步（"+0+"%)");
            }else {
                tv_search_friends.setText("通讯录同步（"+(int)per+"%)");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_newfriends:
                // 进入申请与通知页面
                startActivityForResult(new Intent(getActivity(), NewFriendsActivity.class),0);
                break;
            case R.id.rl_search_friends:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},1);
            }else {
                startActivityForResult(new Intent(getActivity(), AddressListActivity.class),0);
            }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限通过
                startActivityForResult(new Intent(getActivity(), AddressListActivity.class),0);
            } else {  //权限拒绝
                Toast.makeText(getActivity(),"您拒绝了访问通讯录权限，请前往设置手动打开权限！",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
