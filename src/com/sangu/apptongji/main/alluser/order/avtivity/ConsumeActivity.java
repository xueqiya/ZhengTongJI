package com.sangu.apptongji.main.alluser.order.avtivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.order.adapter.UserOrderAdapter;
import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;
import com.sangu.apptongji.main.alluser.presenter.IOrderListPresenter;
import com.sangu.apptongji.main.alluser.presenter.IOrderListQPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.OrderListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.OrderListQuPresenter;
import com.sangu.apptongji.main.alluser.view.IOrderListQView;
import com.sangu.apptongji.main.alluser.view.IOrderListView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/8/31.
 */

public class ConsumeActivity extends BaseActivity implements IOrderListView,IOrderListQView,View.OnClickListener{
    private IOrderListPresenter presenter;
    private IOrderListQPresenter presenter1;
    private ListView lvOrderList= null;
    private List<OrderInfo> list=null;
    private List<OrderInfo> list1=null;
    private List<OrderInfo> list2=null;
    private UserOrderAdapter adapter;
    private ImageView ivBack= null;
    private RadioButton rbAll= null;
    private RadioButton rbWei= null;
    private RadioButton rbYi= null;
    private RadioButton rbTui= null;
    private TextView tvUunrYz= null;
    private TextView tvUunrDq= null;
    private TextView tvUunrTk= null;
    private TextView tv_none= null;
    private String state = null;
    private String state1 = null;
    private String pass=null,type,flg,biaoshi,id,oldState=null,orderBody;
    private int uACount,uDqCount,uTuCount;

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
        setContentView(R.layout.activity_consme);
        presenter = new OrderListPresenter(this,this);
        presenter1 = new OrderListQuPresenter(this,this);
        rbAll = (RadioButton) findViewById(R.id.radioUAll);
        rbWei = (RadioButton) findViewById(R.id.radioUWei);
        rbYi = (RadioButton) findViewById(R.id.radioUYi);
        rbTui = (RadioButton) findViewById(R.id.radioUTui);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvUunrYz = (TextView) findViewById(R.id.tv_unread_Uyanzi);
        tvUunrDq = (TextView) findViewById(R.id.tv_unread_Udaiqian);
        tvUunrTk = (TextView) findViewById(R.id.tv_unread_Utuikuan);
        tv_none = (TextView) findViewById(R.id.tv_none);
        lvOrderList = (ListView) findViewById(R.id.lv_myconsume);
        rbAll.setOnClickListener(this);
        rbWei.setOnClickListener(this);
        rbYi.setOnClickListener(this);
        rbTui.setOnClickListener(this);
        Intent intent = this.getIntent();
        pass = intent.getStringExtra("papass");
        biaoshi = intent.getStringExtra("biaoshi");
        if (pass==null){
            pass = DemoApplication.getInstance().getCurrentPayPass();
        }
        if (biaoshi.equals("01")){
            id = DemoHelper.getInstance().getCurrentUsernName();
        }else if (biaoshi.equals("00")){
            id = DemoApplication.getInstance().getCurrentQiYeId();
        }
        state = "2";
        state1 = "03";
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setListener();
        if ("01".equals(biaoshi)){
            deletePush();
        }
    }

    private void deletePush() {
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
                param.put("type","001");
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radioUAll:
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
            case R.id.radioUWei:
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
            case R.id.radioUYi:
                oldState = state1;
                rbYi.setChecked(true);
                rbWei.setChecked(false);
                rbAll.setChecked(false);
                rbTui.setChecked(false);
                state = "1";
                state1 = "11";
                presenter.loadOrderList(id,state);
                break;
            case R.id.radioUTui:
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
        uDqCount=0;
        uTuCount=0;
        uACount=0;
        presenter.loadOrderList(id,state);
        presenter1.loadOrderList(DemoHelper.getInstance().getCurrentUsernName());
        if (adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void setListener() {
        lvOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                OrderInfo info = null;
                if (state1.equals("11")) {
                    info = list.get(position);
                }else if (state1.equals("03")){
                    info = list1.get(position);
                }else if (state1.equals("04")){
                    info = list2.get(position);
                }
                String biaoshi;
                String remark = TextUtils.isEmpty(info.getRemark())?"":info.getRemark();
                String orderId = info.getOrderId();
                String merId = info.getMerId();
                String orderState = info.getOrderState();
                String orderBody = TextUtils.isEmpty(info.getOrderBody())?"":info.getOrderBody();
                String name = TextUtils.isEmpty(info.getU_name())?info.getUserId():info.getU_name();
                String head = TextUtils.isEmpty(info.getU_uImage())?"l1":info.getU_uImage();
                String send_id1 = info.getSend_id1();
                String send_id2 = info.getSend_id2();
                String orderTime = TextUtils.isEmpty(info.getOrderTime())?"":info.getOrderTime();
                orderTime = orderTime.substring(0,4)+"-"+orderTime.substring(4,6)+"-"+orderTime.substring(6,8)+" "
                        +orderTime.substring(8,10)+":"+orderTime.substring(10,12);
                type = TextUtils.isEmpty(info.getResv3())?"01":info.getResv3();
                flg = TextUtils.isEmpty(info.getResv2())?"01":info.getResv2();
                if (orderState.equals("01")||orderState.equals("02")) {
                    Intent intent = new Intent(ConsumeActivity.this,UOrderDetailActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("merId", merId);
                    intent.putExtra("orderState", orderState);
                    intent.putExtra("pypass", pass);
                    intent.putExtra("biaoshi", "01");
                    startActivity(intent);
                }else {
                    if (flg.equals("01")) {
                        if (send_id2!=null&&!"".equals(send_id2)&&!send_id2.equalsIgnoreCase("null")){
                            biaoshi = "02";
                        }else if (send_id1!=null&&!"".equals(send_id1)&&!send_id1.equalsIgnoreCase("null")){
                            biaoshi = "01";
                        }else {
                            biaoshi = "00";
                        }
                        if (type.equals("01")) {
                            Intent intent = new Intent(ConsumeActivity.this, UTOrderDetailActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("biaoshi", biaoshi);
                            intent.putExtra("biaoshi2", "00");
                            intent.putExtra("merId", merId);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("companyId",remark);
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("pypass", pass);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(ConsumeActivity.this, UTPaiDanDeActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("merId", merId);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("companyId",remark);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("pypass", pass);
                            intent.putExtra("orderTime",orderTime);
                            startActivity(intent);
                        }
                    }else if (flg.equals("02")){
                        if (send_id2!=null&&!"".equals(send_id2)&&!send_id2.equalsIgnoreCase("null")){
                            biaoshi = "02";
                        }else if (send_id1!=null&&!"".equals(send_id1)&&!send_id1.equalsIgnoreCase("null")){
                            biaoshi = "01";
                        }else {
                            biaoshi = "00";
                        }
                        if (type.equals("01")) {
                            Intent intent = new Intent(ConsumeActivity.this, UTOrderDetailTwoActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("merId", merId);
                            intent.putExtra("biaoshi", biaoshi);
                            intent.putExtra("biaoshi2", "00");
                            intent.putExtra("companyId",remark);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("pypass", pass);
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("name", name);
                            intent.putExtra("head", head);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(ConsumeActivity.this, UTPaiDanDeTwoActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("merId", merId);
                            intent.putExtra("companyId",remark);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("pypass", pass);
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("name", name);
                            intent.putExtra("head", head);
                            startActivity(intent);
                        }
                    }else if (flg.equals("03")){
                        if (type.equals("01")) {
                            Intent intent = new Intent(ConsumeActivity.this, UTOrderDetailThreeActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("merId", merId);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("companyId",remark);
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("pypass", pass);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(ConsumeActivity.this, UTPaiDanDeThreeActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("merId", merId);
                            intent.putExtra("companyId",remark);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("pypass", pass);
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("name", name);
                            intent.putExtra("head", head);
                            startActivity(intent);
                        }
                    }else if (flg.equals("04")){
                        if (send_id2!=null&&!"".equals(send_id2)&&!send_id2.equalsIgnoreCase("null")){
                            biaoshi = "02";
                        }else if (send_id1!=null&&!"".equals(send_id1)&&!send_id1.equalsIgnoreCase("null")){
                            biaoshi = "01";
                        }else {
                            biaoshi = "00";
                        }
                        Intent intent = new Intent(ConsumeActivity.this, UTOrderDetailFourActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("merId", merId);
                        intent.putExtra("biaoshi", biaoshi);
                        intent.putExtra("biaoshi2", "00");
                        intent.putExtra("companyId",remark);
                        intent.putExtra("orderState", orderState);
                        intent.putExtra("pypass", pass);
                        intent.putExtra("orderTime", orderTime);
                        intent.putExtra("orderBody", orderBody);
                        intent.putExtra("name", name);
                        intent.putExtra("head", head);
                        startActivity(intent);
                    }else if (flg.equals("05")){
                        if (send_id2!=null&&!"".equals(send_id2)&&!send_id2.equalsIgnoreCase("null")){
                            biaoshi = "02";
                        }else if (send_id1!=null&&!"".equals(send_id1)&&!send_id1.equalsIgnoreCase("null")){
                            biaoshi = "01";
                        }else {
                            biaoshi = "00";
                        }
                        Intent intent = new Intent(ConsumeActivity.this, UTOrderDetailFiveActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("merId", merId);
                        intent.putExtra("biaoshi", biaoshi);
                        intent.putExtra("biaoshi2", "00");
                        intent.putExtra("companyId",remark);
                        intent.putExtra("orderState", orderState);
                        intent.putExtra("pypass", pass);
                        intent.putExtra("orderTime", orderTime);
                        intent.putExtra("orderBody", orderBody);
                        intent.putExtra("name", name);
                        intent.putExtra("head", head);
                        startActivity(intent);

                    }else if (flg.equals("06")) {

                        Intent intent = new Intent(ConsumeActivity.this, NewsOrderDetailActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("orderState", orderState);
                        intent.putExtra("userId", DemoHelper.getInstance().getCurrentUsernName());
                        intent.putExtra("orderTime", orderTime);
                        intent.putExtra("orderBody", orderBody);
                        intent.putExtra("name", name);
                        intent.putExtra("head", head);
                        intent.putExtra("conId", DemoHelper.getInstance().getCurrentUsernName());
                        intent.putExtra("merId", merId);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void updateUserOrderList(List<OrderInfo> orderInfos) {
        if (state1.equals("11")) {
            if (orderInfos!=null) {
                this.list = orderInfos;
                adapter = new UserOrderAdapter(this.list, this);
                lvOrderList.setAdapter(adapter);
                if (list != null && list.size() > 0) {
                    tv_none.setVisibility(View.INVISIBLE);
                }else {
                    if ("03".equals(oldState)&&list1.size()>0){
                        adapter.notifyDataSetChanged();
                    }else if ("04".equals(oldState)&&list2.size()>0){
                        adapter.notifyDataSetChanged();
                    }else if ("11".equals(oldState)&&list.size()>0){
                        adapter.notifyDataSetChanged();
                    }
                    tv_none.setVisibility(View.VISIBLE);
                }
            }else {
                adapter = new UserOrderAdapter(new ArrayList<OrderInfo>(), this);
                lvOrderList.setAdapter(adapter);
                if ("03".equals(oldState)&&list1.size()>0){
                    adapter.notifyDataSetChanged();
                }else if ("04".equals(oldState)&&list2.size()>0){
                    adapter.notifyDataSetChanged();
                }else if ("11".equals(oldState)&&list.size()>0){
                    adapter.notifyDataSetChanged();
                }
                tv_none.setVisibility(View.VISIBLE);
            }
        }else if (state1.equals("03")){
            if (orderInfos!=null) {
                for (int i = 0; i < orderInfos.size(); i++) {
                    String str = orderInfos.get(i).getOrderState();
                    if (list1 != null && ("03".equals(str)||"12".equals(str))) {
                        list1.add(orderInfos.get(i));
                    }
                }
                adapter = new UserOrderAdapter(this.list1, this);
                lvOrderList.setAdapter(adapter);
                if (list1 != null && list1.size() > 0) {
                    if (tv_none != null) {
                        tv_none.setVisibility(View.INVISIBLE);
                    }
                }else {
                    if ("03".equals(oldState)&&list1.size()>0){
                        adapter.notifyDataSetChanged();
                    }else if ("04".equals(oldState)&&list2.size()>0){
                        adapter.notifyDataSetChanged();
                    }else if ("11".equals(oldState)&&list.size()>0){
                        adapter.notifyDataSetChanged();
                    }
                    if (tv_none != null) {
                        tv_none.setVisibility(View.VISIBLE);
                    }
                }
            }else {
                adapter = new UserOrderAdapter(this.list1, this);
                lvOrderList.setAdapter(adapter);
                if ("03".equals(oldState)&&list1.size()>0){
                    adapter.notifyDataSetChanged();
                }else if ("04".equals(oldState)&&list2.size()>0){
                    adapter.notifyDataSetChanged();
                }else if ("11".equals(oldState)&&list.size()>0){
                    adapter.notifyDataSetChanged();
                }
                if (tv_none != null) {
                    tv_none.setVisibility(View.VISIBLE);
                }
            }
        }else if (state1.equals("04")){
            if (orderInfos!=null) {
                for (int i = 0; i < orderInfos.size(); i++) {
                    String str = orderInfos.get(i).getOrderState();
                    if (str.equals("04")) {
                        list2.add(orderInfos.get(i));
                    }
                }
                adapter = new UserOrderAdapter(this.list2, this);
                lvOrderList.setAdapter(adapter);
                if (list2 != null && list2.size() > 0) {
                    tv_none.setVisibility(View.INVISIBLE);
                }else {
                    if ("03".equals(oldState)&&list1.size()>0){
                        adapter.notifyDataSetChanged();
                    }else if ("04".equals(oldState)&&list2.size()>0){
                        adapter.notifyDataSetChanged();
                    }else if ("11".equals(oldState)&&list.size()>0){
                        adapter.notifyDataSetChanged();
                    }
                    if (tv_none != null) {
                        tv_none.setVisibility(View.VISIBLE);
                    }
                }
            }else {
                adapter = new UserOrderAdapter(this.list2, this);
                lvOrderList.setAdapter(adapter);
                if ("03".equals(oldState)&&list1.size()>0){
                    adapter.notifyDataSetChanged();
                }else if ("04".equals(oldState)&&list2.size()>0){
                    adapter.notifyDataSetChanged();
                }else if ("11".equals(oldState)&&list.size()>0){
                    adapter.notifyDataSetChanged();
                }
                if (tv_none != null) {
                    tv_none.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void updateMerOrderList(List<OrderInfo> orderInfos) {

    }

    @Override
    public void updateQUserOrderList(List<OrderInfo> orderInfos) {
        for (int i=0;i<orderInfos.size();i++){
            String count = orderInfos.get(i).getOrderState();
            if ((count.equals("03"))){
                uACount++;
            }
            if (count.equals("04")){
                uDqCount++;
            }
            if (count.equals("06")||count.equals("08")||count.equals("10")){
                uTuCount++;
            }
        }
        if (uACount > 0){
            tvUunrYz.setVisibility(View.VISIBLE);
            tvUunrYz.setText(uACount+"");
        }else {
            tvUunrYz.setVisibility(View.INVISIBLE);
        }
        if (uDqCount>0){
            tvUunrDq.setVisibility(View.VISIBLE);
            tvUunrDq.setText(uDqCount+"");
        }else {
            tvUunrDq.setVisibility(View.INVISIBLE);
        }
        if (uTuCount>0){
            tvUunrTk.setVisibility(View.VISIBLE);
            tvUunrTk.setText(uTuCount+"");
        }else {
            tvUunrTk.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateQMerOrderList(List<OrderInfo> orderInfos) {

    }
}