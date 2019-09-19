package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.FriendCheckAdapter;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.presenter.IPaidanFinishPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PaidanFinishPresenter;
import com.sangu.apptongji.main.alluser.view.IPaidanFinishView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-01-23.
 */

public class FinishPaidanActivity extends BaseActivity implements IPaidanFinishView {
    private IPaidanFinishPresenter presenter;
    private XRecyclerView mRecyclerView = null;
    private List<UserAll> list = new ArrayList<>();
    private List<UserAll> delList = new ArrayList<>();
    private FriendCheckAdapter adapter;
    private TextView tv_commit,tv_title;
    private ArrayList<String> imagePaths1 = new ArrayList<>();
    private ArrayList<String> imagePaths2 = new ArrayList<>();
    private CustomProgressDialog mProgress = null;
    private int currentPage = 1;
    private boolean isNext = false;
    private String dynamicSeq;
    private String createTime;
    //如果是0表示带选择，，1表示只是看
    private String type = "0";


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_paidan_finish);
        dynamicSeq = getIntent().getStringExtra("dynamicSeq");
        createTime = getIntent().getStringExtra("createTime");
        if (TextUtils.isEmpty(type)) {
            type = "0";
        }
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        tv_title = (TextView) findViewById(R.id.tv_title);
        presenter = new PaidanFinishPresenter(this, this);
        WeakReference<FinishPaidanActivity> reference = new WeakReference<FinishPaidanActivity>(FinishPaidanActivity.this);
        mRecyclerView = (XRecyclerView) findViewById(R.id.refresh_list);
        imagePaths1 = new ArrayList<>();
        imagePaths2 = new ArrayList<>();
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        mProgress.show();
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
                currentPage = 1;
                if ("1".equalsIgnoreCase(type)) {
                    presenter.loadShifuList(dynamicSeq,createTime, DemoHelper.getInstance().getCurrentUsernName(),"1");
                } else {
                    presenter.loadShifuList(dynamicSeq,createTime, DemoHelper.getInstance().getCurrentUsernName(),"0");
                }

            }

            @Override
            public void onLoadMore() {
                mRecyclerView.loadMoreComplete();
                mRecyclerView.setNoMore(true);
//                isNext = false;
//                currentPage=1;
//                presenter.loadFriendList(null);
            }
        });
        mRecyclerView.refresh(false);
        if ("1".equalsIgnoreCase(type)) {
            tv_commit.setVisibility(View.GONE);
            tv_title.setText("师傅列表");
        }
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater2 = LayoutInflater.from(FinishPaidanActivity.this);
                RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                final Dialog dialog2 = new AlertDialog.Builder(FinishPaidanActivity.this, R.style.Dialog).create();
                dialog2.show();
                dialog2.getWindow().setContentView(layout2);
                dialog2.setCanceledOnTouchOutside(true);
                dialog2.setCancelable(true);
                TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                title2.setText("提示:");
                btnOK2.setText("确定");
                btnCancel2.setText("取消");
                title_tv2.setText("是否确定是该师傅与您完成的交易");
                btnCancel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });
                btnOK2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<UserAll> users = adapter.getselectedList();
                        updateContact(users);
                        updatePaidan();
                        pushToShiFu(users);
                        //发送信息
                        /*Intent intent=new Intent();
                        intent.setAction("com.broadcast.reflash");
                        sendBroadcast(intent);
                        updateContact(users);
                        updatePaidan(users);
                        pushToShiFu(users);*/
                        dialog2.dismiss();
                        finish();
                    }
                });
            }
        });
    }

    private void updatePaidan() {
        String url = FXConstant.URL_UPDATE_DYNAMIC_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "updatePaidan" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "updatePaidan volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //dynamic/updateDynamicPush  seq  time  orderState  01
                Map<String, String> params = new HashMap<String, String>();
                params.put("dynamicSeq",dynamicSeq );
                params.put("createTime", createTime);
                params.put("orderState","01");
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void pushToShiFu(List<UserAll> users) {
        for (final UserAll user : users) {
            String url = FXConstant.URL_SENDPUSHMSG;
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.d("chen", "推送师傅派单" + s);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("chen", "pushToShiFu volleyError" + volleyError.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //sId=&uId=&type=
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("u_id", user.getuId());
                    params.put("userId", user.getuId());
                    params.put("body", "动态派单消息");
                    params.put("type", "17");
                    params.put("companyId", "0");
                    params.put("companyName", "0");
                    params.put("companyAdress", "0");
                    params.put("dynamicSeq", "0");
                    params.put("createTime", "0");
                    params.put("fristId", "00");
                    params.put("dType", "0");
                    return params;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(request);
        }
    }

  /*  private void updatePaidan(List<UserAll> users) {
        String phone = "";
        for (int i = 0; i < users.size(); i++) {
            if (i == 0) {
                phone = users.get(i).getuId();
            } else {
                phone = phone + "," + users.get(i).getuId();
            }
        }
        String url = FXConstant.URL_UPDATE_KUAISUDINGDAN;
        final String finalPhone = phone;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        final String time = format.format(new Date(System.currentTimeMillis()));
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "更新派单" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "updatePaidan volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //sId=&uId=&type=
                Map<String, String> params = new HashMap<String, String>();
                params.put("sId", sId);
                params.put("display", "01");
                params.put("telephone", finalPhone);
                params.put("content", content);
                params.put("finishTime", time);
                params.put("state", "01");
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);

    }*/

    private void updateContact(List<UserAll> users) {
        for (final UserAll user : users) {
            String url = FXConstant.URL_UPDATE_DEAL_CONTACT;
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.d("chen", "更新师傅派单" + s);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("chen", "updateContact volleyError" + volleyError.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //dynamicSeq=&createTime=&uId=&contactId=&type=
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("dynamicSeq",dynamicSeq );
                    params.put("createTime", createTime);
                    params.put("uId", user.getuId());
                    params.put("contactId", DemoHelper.getInstance().getCurrentUsernName());
                    params.put("type", "01");
                    Log.d("chen", "更新师傅派单的参数" + dynamicSeq + " aa " + createTime + " bb" + DemoHelper.getInstance().getCurrentUsernName()
                            + "cc" + user.getuId());
                    //更新师傅派单的参数2018030916552210000001044 aa 20180309165522 bb10000001044cc10000001055
                    return params;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(request);
        }
    }


    @Override
    public void back(View view) {
        super.back(view);
    }


    public static List<UserAll> removeRepetitionItem(List<UserAll> allList, List<UserAll> deleteList) {
        if (null == deleteList || deleteList.size() == 0) {
            return allList;
        }
        Iterator<UserAll> it = allList.iterator();
        UserAll model = null;
        while (it.hasNext()) {
            model = it.next();
            for (int i = 0; i < deleteList.size(); i++) {
                if (model.getuId().equals(deleteList.get(i).getuId())) {
                    it.remove();
                }
            }
        }
        return allList;
    }

    @Override
    public void updateUserOrderList(List<UserAll> users, boolean hasMore) {
        if (delList != null && delList.size() > 0 && users != null) {
            users = removeRepetitionItem(users, delList);
        }
        if (users == null) {
            if (isNext) {
                mRecyclerView.loadMoreComplete();
            } else {
                mRecyclerView.refreshComplete();
            }
        } else {
            for (UserAll user : users) {
                user.setMyType("16");
            }
            if (isNext) {
                list.addAll(users);
                mRecyclerView.loadMoreComplete();
                adapter.notifyDataSetChanged();
                if (!hasMore) {
                    mRecyclerView.setNoMore(true);
                }
            } else {
                this.list = users;

                adapter = new FriendCheckAdapter(list, FinishPaidanActivity.this, type);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.refreshComplete();
                if (list.size() == 0) {
                    mRecyclerView.setNoMore(true);
                }
            }
        }
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgress != null) {
            if (mProgress.isShowing()) {
                mProgress.dismiss();
            }
            mProgress = null;
        }
        if (list != null) {
            list.clear();
            list = null;
        }
        if (imagePaths1 != null) {
            imagePaths1.clear();
            imagePaths1 = null;
        }
        if (imagePaths2 != null) {
            imagePaths2.clear();
            imagePaths2 = null;
        }
    }
}
