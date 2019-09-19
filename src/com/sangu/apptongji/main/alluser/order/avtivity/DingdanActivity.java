package com.sangu.apptongji.main.alluser.order.avtivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.order.adapter.MerOrderAdapter;
import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;
import com.sangu.apptongji.main.alluser.presenter.IOrderListPresenter;
import com.sangu.apptongji.main.alluser.presenter.IOrderListQPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.OrderListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.OrderListQuPresenter;
import com.sangu.apptongji.main.alluser.view.IOrderListQView;
import com.sangu.apptongji.main.alluser.view.IOrderListView;
import com.sangu.apptongji.main.qiye.QiyePaidanListActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/8/31.
 */

public class DingdanActivity extends BaseActivity implements IOrderListView,IOrderListQView,View.OnClickListener{
    private IOrderListPresenter presenter;
    private IOrderListQPresenter presenter1;
    private ListView lvOrderList=null;
    private List<OrderInfo> list=null;
    private List<OrderInfo> list1=null;
    private List<OrderInfo> list2=null;
    private MerOrderAdapter adapter=null;
    private TextView tv_paidan = null;
    private TextView unread_number_paidan = null;
    private ImageView ivBack=null;
    private RadioButton rbAll=null;
    private RadioButton rbWei=null;
    private RadioButton rbYi=null;
    private RadioButton rbTui=null;
    private TextView tvMunrYz=null;
    private TextView tvMunrDq=null;
    private TextView tvMunrTk=null;
    private TextView tv_none=null;
    private String state = null,biaoshi=null,id=null;;
    private String state1 = null,type=null,flg=null,oldState=null;
    private int mACount,mDqCount,mTuCount;

    //    private TextView tvBig;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (list!=null){
            list.clear();
            list=null;
        }
        if (list1!=null){
            list1.clear();
            list1=null;
        }
        if (list2!=null){
            list2.clear();
            list2=null;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydingdan);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        presenter = new OrderListPresenter(this,this);
        presenter1 = new OrderListQuPresenter(this,this);
        rbAll = (RadioButton) findViewById(R.id.radioMAll);
        rbWei = (RadioButton) findViewById(R.id.radioMWei);
        rbYi = (RadioButton) findViewById(R.id.radioMYi);
        rbTui = (RadioButton) findViewById(R.id.radioMTui);
        unread_number_paidan = (TextView) findViewById(R.id.unread_number_paidan);
        tv_paidan = (TextView) findViewById(R.id.tv_paidan);
        tvMunrYz = (TextView) findViewById(R.id.tv_unread_Myanzi);
        tvMunrDq = (TextView) findViewById(R.id.tv_unread_Mdaiqian);
        tvMunrTk = (TextView) findViewById(R.id.tv_unread_Mtuikuan);
        tv_none = (TextView) findViewById(R.id.tv_none);
//        tvBig = (TextView) findViewById(R.id.tv_big);
        rbAll.setOnClickListener(this);
        rbWei.setOnClickListener(this);
        rbYi.setOnClickListener(this);
        rbTui.setOnClickListener(this);
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        if ("01".equals(biaoshi)){
            id = DemoHelper.getInstance().getCurrentUsernName();
            tv_paidan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DingdanActivity.this,QiyePaidanListActivity.class).putExtra("biaoshi","00").putExtra("isQunzhu","00"));
                }
            });
        }else if ("00".equals(biaoshi)){
            id = DemoApplication.getInstance().getCurrentQiYeId();
            tv_paidan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DingdanActivity.this,QiyePaidanListActivity.class).putExtra("biaoshi","01").putExtra("isQunzhu","01"));
                }
            });
        }
        state = "2";
        state1 = "03";
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        ivBack = (ImageView) findViewById(R.id.iv_back);
        lvOrderList = (ListView) findViewById(R.id.lv_mydingdan);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setListener();
        if ("01".equals(biaoshi)||"00".equals(biaoshi)){
            deletePush("000");
            if ("01".equals(biaoshi)){
                deletePush("04");
            }
        }
    }

    private void deletePush(final String type) {
        String url = FXConstant.URL_DELETE_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                param.put("u_id",DemoHelper.getInstance().getCurrentUsernName());
                if ("01".equals(biaoshi)){
                    param.put("type",type);
                }else if ("00".equals(biaoshi)){
                    param.put("type","002");
                }
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radioMAll:
                oldState = state1;
                if (list1!=null) {
                    list1.clear();
                }
                rbAll.setChecked(true);
                rbWei.setChecked(false);
                rbYi.setChecked(false);
                rbTui.setChecked(false);
                state = "2";
                state1 = "03";
                presenter.loadOrderList(id,state);
                break;
            case R.id.radioMWei:
                oldState = state1;
                if (list2!=null) {
                    list2.clear();
                }
                rbWei.setChecked(true);
                rbAll.setChecked(false);
                rbYi.setChecked(false);
                rbTui.setChecked(false);
                state = "2";
                state1 = "04";
                presenter.loadOrderList(id,state);
                break;
            case R.id.radioMYi:
                oldState = state1;
                rbYi.setChecked(true);
                rbWei.setChecked(false);
                rbAll.setChecked(false);
                rbTui.setChecked(false);
                state = "1";
                state1 = "11";
                presenter.loadOrderList(id,state);
                break;
            case R.id.radioMTui:
                oldState = state1;
                rbTui.setChecked(true);
                rbWei.setChecked(false);
                rbAll.setChecked(false);
                rbYi.setChecked(false);
                state = "3";
                state1 = "11";
                presenter.loadOrderList(id,state);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (state1.equals("04")&&list2!=null) {
            list2.clear();
        }
        if (state1.equals("03")&&list1!=null) {
            list1.clear();
        }
        mDqCount=0;
        mTuCount=0;
        mACount=0;
        if (biaoshi != null && biaoshi.equals("01")) {

            queryBmob();
        }
        presenter.loadOrderList(id,state);
        presenter1.loadOrderList(DemoHelper.getInstance().getCurrentUsernName());
        if (adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void queryBmob() {
        String url = FXConstant.URL_QUERY_UNREADUSER+DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    if (s==null||"".equals(s)){
                        Log.e("dingdanac","offSendOrderCount为空");
                    }else {
                        JSONObject object = new JSONObject(s);
                        int offSendCount = object.getInt("offSendOrderCount");
                        if (offSendCount > 0) {
                            unread_number_paidan.setVisibility(View.VISIBLE);
                        } else {
                            unread_number_paidan.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("dingdanac,e",volleyError.toString());
            }
        });
        MySingleton.getInstance(DingdanActivity.this).addToRequestQueue(request);
    }

    private void setListener() {
        if ("01".equals(biaoshi)) {
            lvOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    OrderInfo info = null;
                    if (state1.equals("11")) {
                        info = list.get(position);
                    } else if (state1.equals("03")) {
                        info = list1.get(position);
                    } else if (state1.equals("04")) {
                        info = list2.get(position);
                    }
                    String orderId = info.getOrderId();
                    String orderState = info.getOrderState();
                    String userId = info.getUserId();
                    String merId = info.getMerId();
                    String orderBody = TextUtils.isEmpty(info.getOrderBody()) ? "" : info.getOrderBody();
                    String name = TextUtils.isEmpty(info.getU_name()) ? info.getUserId() : info.getU_name();
                    String head = TextUtils.isEmpty(info.getU_uImage()) ? "l1" : info.getU_uImage();
                    String orderTime = TextUtils.isEmpty(info.getOrderTime()) ? "" : info.getOrderTime();
                    orderTime = orderTime.substring(0, 4) + "-" + orderTime.substring(4, 6) + "-" + orderTime.substring(6, 8) + " "
                            + orderTime.substring(8, 10) + ":" + orderTime.substring(10, 12);
                    type = TextUtils.isEmpty(info.getResv3()) ? "01" : info.getResv3();
                    flg = TextUtils.isEmpty(info.getResv2()) ? "01" : info.getResv2();
                    if (flg.equals("01")) {
                        if (type.equals("01")) {
                            Intent intent = new Intent(DingdanActivity.this, MOrderDetailActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("userId", userId);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("orderTime", orderTime);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(DingdanActivity.this, MPaiDanDetActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("userId", userId);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("orderTime", orderTime);
                            startActivity(intent);
                        }
                    } else if (flg.equals("02")) {
                        if (type.equals("01")) {
                            Intent intent = new Intent(DingdanActivity.this, MOrderDetailTwoActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("userId", userId);
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("name", name);
                            intent.putExtra("head", head);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(DingdanActivity.this, MPaiDanDetTwoActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("userId", userId);
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("name", name);
                            intent.putExtra("head", head);
                            startActivity(intent);
                        }
                    } else if (flg.equals("03")) {
                        if (type.equals("01")) {
                            Intent intent = new Intent(DingdanActivity.this, MOrderDetailThreeActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("userId", userId);
                            intent.putExtra("orderBody", orderBody);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(DingdanActivity.this, MPaiDanDetThreeActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("userId", userId);
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("name", name);
                            intent.putExtra("head", head);
                            startActivity(intent);
                        }
                    }else if (flg.equals("04")) {
                        Intent intent = new Intent(DingdanActivity.this, MOrderDetailFourActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("orderState", orderState);
                        intent.putExtra("userId", userId);
                        intent.putExtra("orderTime", orderTime);
                        intent.putExtra("orderBody", orderBody);
                        intent.putExtra("name", name);
                        intent.putExtra("head", head);
                        startActivity(intent);
                    }else if (flg.equals("05")) {
                        Intent intent = new Intent(DingdanActivity.this, MOrderDetaiFiveActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("orderState", orderState);
                        intent.putExtra("userId", userId);
                        intent.putExtra("orderTime", orderTime);
                        intent.putExtra("orderBody", orderBody);
                        intent.putExtra("name", name);
                        intent.putExtra("head", head);
                        startActivity(intent);

                    }else if (flg.equals("06")) {

                        Intent intent = new Intent(DingdanActivity.this, NewsOrderDetailActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("orderState", orderState);
                        intent.putExtra("userId", userId);
                        intent.putExtra("orderTime", orderTime);
                        intent.putExtra("orderBody", orderBody);
                        intent.putExtra("name", name);
                        intent.putExtra("head", head);
                        intent.putExtra("conId", userId);
                        intent.putExtra("merId", merId);
                        startActivity(intent);
                    }
                }
            });
        }else {
            lvOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    OrderInfo info = null;
                    if (state1.equals("11")) {
                        info = list.get(position);
                    } else if (state1.equals("03")) {
                        info = list1.get(position);
                    } else if (state1.equals("04")) {
                        info = list2.get(position);
                    }
                    String companyId = info.getMerId();
                    String orderId = info.getOrderId();
                    String userId = info.getUserId();
                    String totalAmt = info.getTotalAmt();
                    String name = TextUtils.isEmpty(info.getU_name()) ? info.getUserId() : info.getU_name();
                    String merId = info.getMerId();
                    String orderState = info.getOrderState();
                    String orderBody = TextUtils.isEmpty(info.getOrderBody())?"":info.getOrderBody();
                    String head = TextUtils.isEmpty(info.getU_uImage())?"l1":info.getU_uImage();
                    String orderTime = TextUtils.isEmpty(info.getOrderTime())?"":info.getOrderTime();
                    orderTime = orderTime.substring(0,4)+"-"+orderTime.substring(4,6)+"-"+orderTime.substring(6,8)+" "
                            +orderTime.substring(8,10)+":"+orderTime.substring(10,12);
                    String type = TextUtils.isEmpty(info.getResv3())?"01":info.getResv3();
                    String flg = TextUtils.isEmpty(info.getResv2())?"01":info.getResv2();
                    String biaoshi1 ;
                    if ("01".equals(biaoshi)){
                        biaoshi1= "不可操作";
                    }else {
                        biaoshi1= "不可操作";
                        // TODO: 2017-01-17 自己的可以操作
                    }
                    if (orderState.equals("01")||orderState.equals("02")) {
                        Intent intent = new Intent(DingdanActivity.this,MOrderDetailActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("userId", userId);
                        intent.putExtra("orderState", orderState);
                        intent.putExtra("totalAmt", totalAmt);
                        intent.putExtra("pypass", "");
                        intent.putExtra("biaoshi", "01");
                        startActivity(intent);
                    }else {
                        if (flg.equals("01")) {
                            if (type.equals("01")) {
                                Intent intent = new Intent(DingdanActivity.this, MOrderDetailActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("userId", userId);
                                intent.putExtra("companyId",companyId);
                                intent.putExtra("orderState", orderState);
//                                intent.putExtra("biaoshi",biaoshi1);
                                intent.putExtra("totalAmt",totalAmt);
                                intent.putExtra("pypass", "");
                                intent.putExtra("biaoshi1","01");
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(DingdanActivity.this, MPaiDanDetActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("userId", userId);
                                intent.putExtra("companyId",companyId);
                                intent.putExtra("orderState", orderState);
//                                intent.putExtra("biaoshi",biaoshi1);
                                intent.putExtra("totalAmt",totalAmt);
                                intent.putExtra("pypass", "");
                                intent.putExtra("orderTime",orderTime);
                                intent.putExtra("biaoshi1","01");
                                startActivity(intent);
                            }
                        }else if (flg.equals("02")){
                            if (type.equals("01")) {
                                Intent intent = new Intent(DingdanActivity.this, MOrderDetailTwoActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("userId", userId);
                                intent.putExtra("companyId",companyId);
                                intent.putExtra("orderState", orderState);
//                                intent.putExtra("biaoshi",biaoshi1);
                                intent.putExtra("totalAmt",totalAmt);
                                intent.putExtra("pypass", "");
                                intent.putExtra("orderTime", orderTime);
                                intent.putExtra("orderBody", orderBody);
                                intent.putExtra("name", name);
                                intent.putExtra("head", head);
                                intent.putExtra("biaoshi1","01");
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(DingdanActivity.this, MPaiDanDetTwoActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("userId", userId);
                                intent.putExtra("companyId",companyId);
                                intent.putExtra("orderState", orderState);
//                                intent.putExtra("biaoshi",biaoshi1);
                                intent.putExtra("totalAmt",totalAmt);
                                intent.putExtra("pypass", "");
                                intent.putExtra("orderTime", orderTime);
                                intent.putExtra("orderBody", orderBody);
                                intent.putExtra("name", name);
                                intent.putExtra("head", head);
                                intent.putExtra("biaoshi1","01");
                                startActivity(intent);
                            }
                        }else if (flg.equals("03")){
                            if (type.equals("01")) {
                                Intent intent = new Intent(DingdanActivity.this, MOrderDetailThreeActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("userId", userId);
                                intent.putExtra("companyId",companyId);
                                intent.putExtra("totalAmt",totalAmt);
                                intent.putExtra("orderState", orderState);
//                                intent.putExtra("biaoshi",biaoshi1);
                                intent.putExtra("pypass", "");
                                intent.putExtra("biaoshi1","01");
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(DingdanActivity.this, MPaiDanDetThreeActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("userId", userId);
                                intent.putExtra("companyId",companyId);
                                intent.putExtra("orderState", orderState);
                                intent.putExtra("totalAmt",totalAmt);
//                                intent.putExtra("biaoshi",biaoshi1);
                                intent.putExtra("pypass", "");
                                intent.putExtra("orderTime", orderTime);
                                intent.putExtra("orderBody", orderBody);
                                intent.putExtra("name", name);
                                intent.putExtra("head", head);
                                intent.putExtra("biaoshi1","01");
                                startActivity(intent);
                            }
                        }else if (flg.equals("04")){
                            Intent intent = new Intent(DingdanActivity.this, MOrderDetailFourActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("userId", userId);
                            intent.putExtra("companyId",companyId);
                            intent.putExtra("totalAmt",totalAmt);
                            intent.putExtra("orderState", orderState);
//                            intent.putExtra("biaoshi",biaoshi1);
                            intent.putExtra("pypass", "");
                            intent.putExtra("biaoshi1","01");
                            startActivity(intent);
                        }else if (flg.equals("05")){
                            Intent intent = new Intent(DingdanActivity.this, MOrderDetaiFiveActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("userId", userId);
                            intent.putExtra("companyId",companyId);
                            intent.putExtra("totalAmt",totalAmt);
                            intent.putExtra("orderState", orderState);
//                            intent.putExtra("biaoshi",biaoshi1);
                            intent.putExtra("pypass", "");
                            intent.putExtra("biaoshi1","01");
                            startActivity(intent);

                        }else if (flg.equals("06")) {

                            Intent intent = new Intent(DingdanActivity.this, NewsOrderDetailActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("userId", userId);
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("name", name);
                            intent.putExtra("head", head);
                            intent.putExtra("conId", userId);
                            intent.putExtra("merId", merId);
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void updateUserOrderList(List<OrderInfo> orderInfos) {

    }

    @Override
    public void updateMerOrderList(List<OrderInfo> orderInfos) {
        if (state1.equals("11")) {
            if (orderInfos!=null) {
                this.list = orderInfos;
                adapter = new MerOrderAdapter(list, this);
                lvOrderList.setAdapter(adapter);
                if (list != null && list.size() > 0) {
                    tv_none.setVisibility(View.INVISIBLE);
                }else {
                    if ("03".equals(oldState)&&list1!=null&&list1.size()>0){
                        adapter.notifyDataSetChanged();
                    }else if ("04".equals(oldState)&&list2!=null&&list2.size()>0){
                        adapter.notifyDataSetChanged();
                    }else if ("11".equals(oldState)&&list!=null&&list.size()>0){
                        adapter.notifyDataSetChanged();
                    }
                    tv_none.setVisibility(View.VISIBLE);
                }
            }else {
                adapter = new MerOrderAdapter(new ArrayList<OrderInfo>(), this);
                lvOrderList.setAdapter(adapter);
                if ("03".equals(oldState)&&list1!=null&&list1.size()>0){
                    adapter.notifyDataSetChanged();
                }else if ("04".equals(oldState)&&list2!=null&&list2.size()>0){
                    adapter.notifyDataSetChanged();
                }else if ("11".equals(oldState)&&list!=null&&list.size()>0){
                    adapter.notifyDataSetChanged();
                }
                tv_none.setVisibility(View.VISIBLE);
            }
        }else if (state1.equals("03")){
            if (orderInfos!=null) {
                for (int i = 0; i < orderInfos.size(); i++) {
                    String str = orderInfos.get(i).getOrderState();
                    if (list1 != null && str.equals("03")) {
                        list1.add(orderInfos.get(i));
                    }
                }
                adapter = new MerOrderAdapter(this.list1, this);
                lvOrderList.setAdapter(adapter);
                if (list1 != null && list1.size() > 0) {
                    tv_none.setVisibility(View.INVISIBLE);
                }else {
                    if ("03".equals(oldState)&&list1!=null&&list1.size()>0){
                        adapter.notifyDataSetChanged();
                    }else if ("04".equals(oldState)&&list2!=null&&list2.size()>0){
                        adapter.notifyDataSetChanged();
                    }else if ("11".equals(oldState)&&list!=null&&list.size()>0){
                        adapter.notifyDataSetChanged();
                    }
                    tv_none.setVisibility(View.VISIBLE);
                }
            }else {
                adapter = new MerOrderAdapter(this.list1, this);
                lvOrderList.setAdapter(adapter);
                if ("03".equals(oldState)&&list1!=null&&list1.size()>0){
                    adapter.notifyDataSetChanged();
                }else if ("04".equals(oldState)&&list2!=null&&list2.size()>0){
                    adapter.notifyDataSetChanged();
                }else if ("11".equals(oldState)&&list!=null&&list.size()>0){
                    adapter.notifyDataSetChanged();
                }
                tv_none.setVisibility(View.VISIBLE);
            }
        }else if (state1.equals("04")){
            if (orderInfos!=null) {
                for (int i = 0; i < orderInfos.size(); i++) {
                    String str = orderInfos.get(i).getOrderState();
                    if (list2 != null && str.equals("04")) {
                        list2.add(orderInfos.get(i));
                    }
                }
                adapter = new MerOrderAdapter(this.list2, this);
                lvOrderList.setAdapter(adapter);
                if (list2 != null && list2.size() > 0) {
                    tv_none.setVisibility(View.INVISIBLE);
                }else {
                    if ("03".equals(oldState)&&list1!=null&&list1.size()>0){
                        adapter.notifyDataSetChanged();
                    }else if ("04".equals(oldState)&&list2!=null&&list2.size()>0){
                        adapter.notifyDataSetChanged();
                    }else if ("11".equals(oldState)&&list!=null&&list.size()>0){
                        adapter.notifyDataSetChanged();
                    }
                    tv_none.setVisibility(View.VISIBLE);
                }
            }else {
                adapter = new MerOrderAdapter(this.list2, this);
                lvOrderList.setAdapter(adapter);
                if ("03".equals(oldState)&&list1!=null&&list1.size()>0){
                    adapter.notifyDataSetChanged();
                }else if ("04".equals(oldState)&&list2!=null&&list2.size()>0){
                    adapter.notifyDataSetChanged();
                }else if ("11".equals(oldState)&&list!=null&&list.size()>0){
                    adapter.notifyDataSetChanged();
                }
                tv_none.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void updateQUserOrderList(List<OrderInfo> orderInfos) {

    }
    @Override
    public void updateQMerOrderList(List<OrderInfo> orderInfos) {
        for (int i=0;i<orderInfos.size();i++){
            String count = orderInfos.get(i).getOrderState();
            if ((count.equals("03"))){
                mACount++;
            }
            if (count.equals("04")){
                mDqCount++;
            }
            if (count.equals("06")||count.equals("10")){
                mTuCount++;
            }
        }
        if (mACount > 0){
            tvMunrYz.setVisibility(View.VISIBLE);
            tvMunrYz.setText(mACount+"");
        }else {
            tvMunrYz.setVisibility(View.INVISIBLE);
        }
        if (mDqCount>0){
            tvMunrDq.setVisibility(View.VISIBLE);
            tvMunrDq.setText(mDqCount+"");
        }else {
            tvMunrDq.setVisibility(View.INVISIBLE);
        }
        if (mTuCount>0){
            tvMunrTk.setVisibility(View.VISIBLE);
            tvMunrTk.setText(mTuCount+"");
        }else {
            tvMunrTk.setVisibility(View.INVISIBLE);
        }
    }
}