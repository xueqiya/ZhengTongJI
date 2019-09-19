package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.FriendListAdapter;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.presenter.IFriendListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.FriendListPresenter;
import com.sangu.apptongji.main.alluser.view.IFriendListView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.PreferenceManager;
import com.sangu.apptongji.utils.UserPermissionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-02.
 */

public class FriendActivity extends BaseActivity implements View.OnClickListener,IFriendListView {
    private IFriendListPresenter presenter;
    private XRecyclerView mRecyclerView=null;
    private List<UserAll> list=null;
    private FriendListAdapter adapter;
    private EditText searchbox;
    private String orderId,hasId1,hasId2,hasId3,hasId4,timestamp,orderBody;
    private String pass,biaoshi,filepath,orderDesc,companyId,cusName,cusPhone,cusAdress,infoPrice;
    private String flg,upId,orderProject,orderNumber,orderAmt,orderSum,conventionTime,time4;
    private ArrayList<String> imagePaths1=new ArrayList<>();
    private ArrayList<String> imagePaths2=new ArrayList<>();
    private CustomProgressDialog mProgress=null;
    private int currentPage=1;
    private boolean isNext = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgress!=null){
            if (mProgress.isShowing()){
                mProgress.dismiss();
            }
            mProgress=null;
        }
        if (list!=null){
            list.clear();
            list=null;
        }
        if (imagePaths1!=null){
            imagePaths1.clear();
            imagePaths1=null;
        }
        if (imagePaths2!=null){
            imagePaths2.clear();
            imagePaths2=null;
        }
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_friendlist);
        presenter = new FriendListPresenter(this,this);
        WeakReference<FriendActivity> reference =  new WeakReference<FriendActivity>(FriendActivity.this);
        mRecyclerView = (XRecyclerView) findViewById(R.id.refresh_list);
        searchbox = (EditText) findViewById(R.id.searchbox);
        searchbox.setVisibility(View.VISIBLE);
        imagePaths1 = new ArrayList<>();
        imagePaths2 = new ArrayList<>();
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在派单...");
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
                presenter.loadFriendList(null);
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
        flg = getIntent().getStringExtra("flg");
        upId = getIntent().getStringExtra("upId");
        orderProject = getIntent().getStringExtra("orderProject");
        orderNumber = getIntent().getStringExtra("orderNumber");
        orderAmt = getIntent().getStringExtra("orderAmt");
        orderSum = getIntent().getStringExtra("orderSum");
        conventionTime = getIntent().getStringExtra("conventionTime");
        time4 = getIntent().getStringExtra("time4");
        pass = this.getIntent().getStringExtra("payPass");
        orderBody = getIntent().getStringExtra("orderBody");
        orderId = getIntent().getStringExtra("orderId");
        timestamp = getIntent().getStringExtra("timestamp");
        hasId1 = getIntent().getStringExtra("hasId1");
        hasId2 = getIntent().getStringExtra("hasId2");
        hasId3 = getIntent().getStringExtra("hasId3");
        hasId4 = getIntent().getStringExtra("hasId4");
        biaoshi = this.getIntent().hasExtra("biaoshi")?getIntent().getStringExtra("biaoshi"):"";
        filepath = this.getIntent().hasExtra("filepath")?getIntent().getStringExtra("filepath"):"";
        orderDesc = this.getIntent().hasExtra("orderDesc")?getIntent().getStringExtra("orderDesc"):"0";
        companyId = this.getIntent().hasExtra("companyId")?getIntent().getStringExtra("companyId"):"";
        cusName = this.getIntent().hasExtra("cusName")?getIntent().getStringExtra("cusName"):"";
        cusPhone = this.getIntent().hasExtra("cusPhone")?getIntent().getStringExtra("cusPhone"):"";
        cusAdress = this.getIntent().hasExtra("cusAdress")?getIntent().getStringExtra("cusAdress"):"";
        infoPrice = this.getIntent().hasExtra("infoPrice")?getIntent().getStringExtra("infoPrice"):"0";
        imagePaths1 = this.getIntent().hasExtra("imagePaths1")?getIntent().getStringArrayListExtra("imagePaths1"):new ArrayList<String>();
        imagePaths2 = this.getIntent().hasExtra("imagePaths2")?getIntent().getStringArrayListExtra("imagePaths2"):new ArrayList<String>();
        mRecyclerView.refresh(false);
        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s); // 当数据改变时，调用过滤器;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void updateUserOrderList(final List<UserAll> users,boolean hasMore) {
        if (users==null){
            if (isNext){
                mRecyclerView.loadMoreComplete();
            }else {
                mRecyclerView.refreshComplete();
            }
        }else {
            if (isNext) {
                list.addAll(users);
                mRecyclerView.loadMoreComplete();
                adapter.notifyDataSetChanged();
                if (!hasMore) {
                    mRecyclerView.setNoMore(true);
                }
            } else {
                this.list = users;
                adapter = new FriendListAdapter(list, FriendActivity.this);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.refreshComplete();
            }
            adapter.setOnItemClickListener(new FriendListAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    UserAll userAll = adapter.getDate().get(position - 1);
                    final String username = userAll.getuLoginId();
                    final String name1 = userAll.getuName();
                    String image = userAll.getuImage();
                    final String mingzi = TextUtils.isEmpty(userAll.getuName()) ? username : userAll.getuName();
                    if (image!=null&&!"".equals(image)){
                        image = image.split("\\|")[0];
                    }
                    if ("01".equals(biaoshi)) {
                        UserAll user = list.get(position - 1);
                        final String userId = user.getuLoginId();
                        final String name = TextUtils.isEmpty(user.getuName()) ? userId : user.getuName();
                        LayoutInflater inflaterDl = LayoutInflater.from(FriendActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(FriendActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                        final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                        btnOK.setText("确定");
                        btnCancel.setText("取消");
                        title_tv.setText("确认派单给该用户？");
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                insertDataWithMany(name, userId);
                            }
                        });
                    } else if ("02".equals(biaoshi)) {
                        LayoutInflater inflaterDl = LayoutInflater.from(FriendActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(FriendActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                        final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                        btnOK.setText("确定");
                        btnCancel.setText("取消");
                        title_tv.setText("确认分享名片给该好友？");
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        final String finalImage = image;
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                String mingPianId = FriendActivity.this.getIntent().getStringExtra(FXConstant.JSON_KEY_HXID);
                                String filePath = FriendActivity.this.getIntent().getStringExtra("filePath");
                                EMMessage message = EMMessage.createImageSendMessage(filePath, false, username);
                                message.setAttribute("shareId", mingPianId);
                                message.setAttribute("types", "名片");
                                String userPic = PreferenceManager.getInstance().getCurrentUserAvatar();
                                if (!TextUtils.isEmpty(userPic)) {
                                    message.setAttribute("userPic", userPic);
                                }
                                String userName = PreferenceManager.getInstance().getCurrentUserNick();
                                if (!TextUtils.isEmpty(userName)) {
                                    message.setAttribute("userName", userName);
                                }
                                message.setAttribute(DemoHelper.getInstance().getCurrentUsernName(), userPic+"|"+userName);
                                message.setAttribute(username, finalImage +"|"+mingzi);
                                message.setAttribute("name",mingzi);
                                message.setAttribute("type","单聊");
                                message.setAttribute("shareRed","无");
                                EMClient.getInstance().chatManager().sendMessage(message);
                                Toast.makeText(getApplicationContext(), "分享成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } else if ("03".equals(biaoshi)) {
                        LayoutInflater inflaterDl = LayoutInflater.from(FriendActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(FriendActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                        final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                        btnOK.setText("确定");
                        btnCancel.setText("取消");
                        title_tv.setText("确认发送给 '" + mingzi + "' 吗？");
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (username.equals(hasId1) || username.equals(hasId2) || username.equals(hasId3) || username.equals(hasId4)) {
                                    LayoutInflater inflaterDl = LayoutInflater.from(FriendActivity.this);
                                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                                    final Dialog dialog2 = new AlertDialog.Builder(FriendActivity.this,R.style.Dialog).create();
                                    dialog2.show();
                                    dialog2.getWindow().setContentView(layout);
                                    dialog2.setCanceledOnTouchOutside(false);
                                    dialog2.setCancelable(false);
                                    TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                                    Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                                    tv_title.setText("该账号已在本个订单中,无需再次发送！");
                                    btn_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog2.dismiss();
                                        }
                                    });
                                } else {
                                    sendToUser(username, name1);
                                }
                            }
                        });
                    } else if ("04".equals(biaoshi)) {
                        LayoutInflater inflaterDl = LayoutInflater.from(FriendActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(FriendActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                        final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                        btnOK.setText("确定");
                        btnCancel.setText("取消");
                        title_tv.setText("确认发送给 '" + mingzi + "' 吗？");
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                senddztoUser(username);
                            }
                        });
                    } else if ("05".equals(biaoshi)) {
                        LayoutInflater inflaterDl = LayoutInflater.from(FriendActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(FriendActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                        final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                        btnOK.setText("确定");
                        btnCancel.setText("取消");
                        title_tv.setText("确认发送给 '" + mingzi + "' 吗？");
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                insertOrder2(username, "01");
                            }
                        });
                    } else {
                        startActivity(new Intent(FriendActivity.this, ZHuZhActivity.class).putExtra(FXConstant.JSON_KEY_HXID, username).putExtra("payPass", pass).putExtra("biaoshi", biaoshi));
                    }
                }
            });
        }
    }

    private void sendPushMessage2(final String userId,final int type) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id",userId);
                if (type==0) {
                    param.put("body","订单消息");
                    param.put("type","001");
                }else if (type==1){
                    param.put("body","订单消息");
                    param.put("type","12");
                }else {
                    param.put("body","电子单据消息");
                    param.put("type","11");
                }
                param.put("userId",myId);
                param.put("companyId","0");
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(FriendActivity.this).addToRequestQueue(request);
    }

    private void insertOrder2(final String userId,final String biaoshi){
        String url = FXConstant.URL_INSERT_ORDER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    String code = obj.getString("code");
                    JSONObject object = obj.getJSONObject("orderInfo");
                    orderId = object.getString("orderId");
                    if (code.equals("数据更新成功")) {
                        bianji2(userId,biaoshi);
                    } else {
                        Toast.makeText(FriendActivity.this, "下单失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("onResponse,s",volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("orderBody", orderBody);
                params.put("flg", flg);
                params.put("type", "01");
                params.put("upId", upId);
                return params;
            }
        };
        MySingleton.getInstance(FriendActivity.this).addToRequestQueue(request);
    }

    private void bianji2(final String userId, final String biaoshi){
        if ("04".equals(flg)){
            String path1 = null,path2 = null,path3 = null,path4 = null,path5 = null,path6 = null,path7 = null,path8 = null;
            if (imagePaths1.size()>0){
                path1 = imagePaths1.get(0);
            }
            if (imagePaths1.size()>1){
                path2 = imagePaths1.get(1);
            }
            if (imagePaths1.size()>2){
                path3 = imagePaths1.get(2);
            }
            if (imagePaths1.size()>3){
                path4 = imagePaths1.get(3);
            }
            if (imagePaths2.size() > 0) {
                path5 = imagePaths2.get(0);
            }
            if (imagePaths2.size() > 1) {
                path6 = imagePaths2.get(1);
            }
            if (imagePaths2.size() > 2) {
                path7 = imagePaths2.get(2);
            }
            if (imagePaths2.size() > 3) {
                path8 = imagePaths2.get(3);
            }
            List<Param> param=new ArrayList<>();
            if (!"".equals(conventionTime)) {
                param.add(new Param("conventionTime",conventionTime));
            }
            param.add(new Param("state", "02"));
            param.add(new Param("orderId",orderId));
            param.add(new Param("userId", userId));
            param.add(new Param("orderProject", orderProject));
            param.add(new Param("orderNumber", orderNumber));
            param.add(new Param("orderSum", orderSum));
            String url = FXConstant.URL_INSERT_OrderDetail;
            OkHttpManager.getInstance().posts2(param, null, new ArrayList<String>(), null, new ArrayList<String>(),"image01", path1, "image02", path2, "image03", path3, "image04", path4
                    , "image05", path5, "image06", path6, "image07", path7, "image08", path8, url, new OkHttpManager.HttpCallBack() {
                        @Override
                        public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                            String code = jsonObject.getString("code");
                            if (code.equals("数据更新成功")) {
                                sendPushMessage2(userId,0);
                                updateUscount(userId,0);
                                SendMessage4(userId);
                            } else {
                                Toast.makeText(FriendActivity.this, "编辑错误！", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(String errorMsg) {
                            Toast.makeText(FriendActivity.this,"网络连接错误,派单失败",Toast.LENGTH_SHORT).show();
                            if (errorMsg!=null) {
                                Log.e("offlineorder,e", errorMsg);
                            }
                        }
                    });
        }else {
            String url = FXConstant.URL_INSERT_OrderDetail;
            StringRequest request3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String code = object.getString("code");
                        if (code.equals("数据更新成功")) {
                            updateUscount(userId,0);
                            SendMessage4(userId);
                        } else {
                            Toast.makeText(FriendActivity.this, "网络错误，请重新编辑！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(FriendActivity.this, "网络错误，请重新编辑！", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params1 = new HashMap<>();
                    params1.put("orderId", orderId);
                    params1.put("userId", userId);
                    params1.put("state", "02");
                    if ("02".equals(flg)) {
                        params1.put("time4", time4);
                    }
                    params1.put("orderProject", orderProject);
                    params1.put("orderNumber", orderNumber);
                    if (!"05".equals(flg)) {
                        params1.put("orderAmt", orderAmt);
                    }
                    params1.put("orderSum", orderSum);
                    if (conventionTime != null && !"".equals(conventionTime)) {
                        params1.put("conventionTime", conventionTime);
                    }
                    return params1;
                }
            };
            MySingleton.getInstance(FriendActivity.this).addToRequestQueue(request3);
        }
    }

    private void updateUscount(final String hxid1 ,final int type) {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                if (type==0) {
                    param.put("orderUnReadCount", "1");
                }else if (type==1){
                    param.put("billCount", "1");
                }else if (type==2){
                    param.put("thirdPartyCount", "1");
                }
                param.put("userId",hxid1);
                return param;
            }
        };
        MySingleton.getInstance(FriendActivity.this).addToRequestQueue(request);
    }

    private void SendMessage4(final String userId) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】您有一个（"+orderBody+")的订单，需要验资，请在“正事多”手机端操作完成后，即可提供相应服务");
                param.put("telNum", userId);
                Log.e("utorderdeac,sm",param.toString());
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(FriendActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void senddztoUser(final String username) {
        String url = FXConstant.URL_FASONG_DZ_DANJU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("friendac,s",s);
                sendPushMessage2(username,2);
                updateUscount(username,1);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("success".equals(code)){
                    Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"发送失败！",Toast.LENGTH_SHORT).show();
                Log.e("friendac",volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("timestamp",timestamp);
                param.put("u_id",username);
                return param;
            }
        };
        MySingleton.getInstance(FriendActivity.this).addToRequestQueue(request);
    }

    private void sendToUser(final String username,String name) {
        if (name ==null||"".equals(name)){
            name="";
        }else {
            name = name+":";
        }
        String url = FXConstant.URL_FASONG_DZDANJU;
        final String finalName = name;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                sendPushMessage2(username,1);
                updateUscount(username,2);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("success".equals(code)){
                    SendMessage(username, finalName);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络连接中断,发送失败！",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("order_id",orderId);
                param.put("send_id",DemoHelper.getInstance().getCurrentUsernName());
                param.put("u_id",username);
                return param;
            }
        };
        MySingleton.getInstance(FriendActivity.this).addToRequestQueue(request);
    }

    private void SendMessage(final String userId,final String name) {
        String lists = userId+","+hasId3;
        if (hasId1!=null&&!"".equals(hasId1)){
            lists = lists+","+hasId1;
        }
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final String finalLists = lists;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】订单(" + orderBody + "),需要("+name+userId+")的用户签字验收");
                param.put("telNum", finalLists);
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(FriendActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void insertDataWithMany(final String userName, final String userId) {
        if (mProgress!=null){
            if (!mProgress.isShowing()){
                mProgress.setMessage("正在派单，请稍等...");
                mProgress.show();
            }
        }else {
            mProgress = CustomProgressDialog.createDialog(this);
            mProgress.setMessage("正在派单，请稍等...");
            mProgress.show();
            mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mProgress.dismiss();
                }
            });
        }
        String orderInfo = null,cusInfo=null;
        orderInfo = orderDesc+"|"+infoPrice+"|"+"0";
        cusInfo = cusName+"|"+cusPhone+"|"+cusAdress;
        final List<File> files = new ArrayList<File>();
        if (filepath!=null&&!"".equals(filepath)) {
            File file = null;
            file = new File(filepath);
            if (file.exists()) {
                files.add(file);
            }
        }
        final List<File> files1 = new ArrayList<File>();
        if (imagePaths1.size()>0){
            File file = null;
            file = new File(imagePaths1.get(0));
            if (file.exists()) {
                files1.add(file);
            }
        }
        if (imagePaths1.size()>1){
            File file = null;
            file = new File(imagePaths1.get(1));
            if (file.exists()) {
                files1.add(file);
            }
        }
        if (imagePaths1.size()>2){
            File file = null;
            file = new File(imagePaths1.get(2));
            if (file.exists()) {
                files1.add(file);
            }
        }
        if (imagePaths1.size()>3){
            File file = null;
            file = new File(imagePaths1.get(3));
            if (file.exists()) {
                files1.add(file);
            }
        }
        final List<File> files2 = new ArrayList<File>();
        if (imagePaths2.size() > 0) {
            File file = null;
            file = new File(imagePaths2.get(0));
            if (file.exists()) {
                files2.add(file);
            }
        }
        if (imagePaths2.size() > 1) {
            File file = null;
            file = new File(imagePaths2.get(1));
            if (file.exists()) {
                files2.add(file);
            }
        }
        if (imagePaths2.size() > 2) {
            File file = null;
            file = new File(imagePaths2.get(2));
            if (file.exists()) {
                files2.add(file);
            }
        }
        if (imagePaths2.size() > 3) {
            File file = null;
            file = new File(imagePaths2.get(3));
            if (file.exists()) {
                files2.add(file);
            }
        }
        List<Param> param=new ArrayList<>();
        param.add(new Param("companyId",companyId));
        param.add(new Param("ordName",userName));
        param.add(new Param("ordId", userId));
        param.add(new Param("sendIdentify","0"));
        param.add(new Param("cusInfo", cusInfo));
        param.add(new Param("orderInfo", orderInfo));
        param.add(new Param("Identification", "1"));
        String url = FXConstant.URL_INSERT_OFFSEND;
        String str = "signature1";
        String str1 = "beforeService";
        String str2 = "afterService";
        OkHttpManager.getInstance().postThfile(str, files, param, str1, files1, str2, files2, url, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                String code = jsonObject.getString("code");
                if ("success".equals(code)){
                    sendPushMessage(userId);
                    updateBmob(userId);
                    Toast.makeText(FriendActivity.this,"派单成功！",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                if (mProgress!=null){
                    if (mProgress.isShowing()){
                        mProgress.dismiss();
                        mProgress=null;
                    }
                }
                Toast.makeText(FriendActivity.this,"网络连接错误,派单失败",Toast.LENGTH_SHORT).show();
                if (errorMsg!=null) {
                    Log.e("offlineorder,e", errorMsg);
                }
            }
        });
    }

    private void sendPushMessage(final String hxid1) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id",hxid1);
                param.put("body","派单消息");
                param.put("type","05");
                param.put("userId",myId);
                param.put("companyId",companyId);
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(FriendActivity.this).addToRequestQueue(request);
    }

    private void updateBmob(final String userId) {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("friend,s",s);
                duanxintongzhi(userId,"【正事多】 通知:您有一条新的派单消息");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("friend,e",volleyError.toString());
                duanxintongzhi(userId,"【正事多】 通知:您有一条新的派单消息");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("offSendOrderCount","1");
                param.put("userId",userId);
                return param;
            }
        };
        MySingleton.getInstance(FriendActivity.this).addToRequestQueue(request);
    }
    private void duanxintongzhi(final String id, final String message) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message",message);
                param.put("telNum",id);
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(FriendActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

}
