package com.sangu.apptongji.main.qiye;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.order.avtivity.MOrderDetaiFiveActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.MOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.MOrderDetailFourActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.MOrderDetailThreeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.MOrderDetailTwoActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.MPaiDanDetActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.MPaiDanDetThreeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.MPaiDanDetTwoActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailActivity;
import com.sangu.apptongji.main.qiye.adapter.PaidanAdapter;
import com.sangu.apptongji.main.qiye.entity.OffSendOrderList;
import com.sangu.apptongji.main.qiye.entity.PaiDanInfo;
import com.sangu.apptongji.main.qiye.presenter.IOfflineListPresenter;
import com.sangu.apptongji.main.qiye.presenter.IPaidanListPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.OfflineListPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.PaidanListPresenter;
import com.sangu.apptongji.main.qiye.view.IOfflineListView;
import com.sangu.apptongji.main.qiye.view.IPaidanListView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-01-17.
 */

public class QiyePaidanListActivity extends BaseActivity implements IPaidanListView,IOfflineListView,View.OnClickListener{
    private TextView tvtitle=null,tv_create=null;
    private TextView tvback=null;
    private View v1=null;
    private View v2=null;
    private ListView lv_paidan_list=null;
    private LinearLayout nav=null;
    private RadioButton radioPaidan=null,radioOffline=null;
    private PaidanAdapter adapter=null;
    private IPaidanListPresenter paidanListPresenter=null;
    private IOfflineListPresenter offlineListPresenter=null;
    private String biaoshi=null,isQunzhu=null;
    String companyId=null,userId=null;
    private CustomProgressDialog mProgress=null;
    boolean isPaidanClick = true;
    boolean isOfflineClick = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgress!=null){
            if (mProgress.isShowing()){
                mProgress.dismiss();
                mProgress=null;
            }
        }
    }
    
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_new_major);
        WeakReference<QiyePaidanListActivity> reference =  new WeakReference<QiyePaidanListActivity>(QiyePaidanListActivity.this);
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在加载请求列表...");
        mProgress.show();
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        paidanListPresenter = new PaidanListPresenter(this,reference.get());
        offlineListPresenter = new OfflineListPresenter(this,reference.get());
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        nav = (LinearLayout) findViewById(R.id.nav);
        radioPaidan = (RadioButton) findViewById(R.id.radioPaidan);
        radioOffline = (RadioButton) findViewById(R.id.radioOffline);
        tvback = (TextView) findViewById(R.id.tvback);
        tvtitle = (TextView) findViewById(R.id.tvtitle);
        tv_create = (TextView) findViewById(R.id.tv_create);
        lv_paidan_list = (ListView) this.findViewById(R.id.lv_new_yuangong);
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        isQunzhu = this.getIntent().getStringExtra("isQunzhu");
        radioPaidan.setOnClickListener(this);
        radioOffline.setOnClickListener(this);
        nav.setVisibility(View.VISIBLE);
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.VISIBLE);
        tvback.setText("返回");
        tvtitle.setText("派单列表");
        companyId= DemoApplication.getInstance().getCurrentQiYeId();
        userId = DemoHelper.getInstance().getCurrentUsernName();
        if ("01".equals(isQunzhu)){
            tv_create.setVisibility(View.VISIBLE);
        }
        tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(QiyePaidanListActivity.this,OfflineOrderActivity.class).putExtra("biaoshi","00"),0);
            }
        });
        if ("00".equals(biaoshi)){
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
                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                param.put("type","05");
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioPaidan:
                isPaidanClick = true;
                isOfflineClick = false;
                if (mProgress!=null){
                    if (!mProgress.isShowing()){
                        mProgress.show();
                    }
                }else {
                    mProgress = CustomProgressDialog.createDialog(this);
                    mProgress.setMessage("正在加载请求列表...");
                    mProgress.show();
                    mProgress.setCancelable(true);
                    mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mProgress.dismiss();
                        }
                    });
                }
                radioPaidan.setChecked(true);
                radioOffline.setChecked(false);
                if ("01".equals(biaoshi)) {
                    paidanListPresenter.loadPaidanList(companyId, "");
                }else {
                    paidanListPresenter.loadPaidanList(companyId, userId);
                }
                break;
            case R.id.radioOffline:
                isOfflineClick = true;
                isPaidanClick = false;
                if (mProgress!=null){
                    if (!mProgress.isShowing()){
                        mProgress.show();
                    }
                }else {
                    mProgress = CustomProgressDialog.createDialog(this);
                    mProgress.setMessage("正在加载请求列表...");
                    mProgress.show();
                    mProgress.setCancelable(true);
                    mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mProgress.dismiss();
                        }
                    });
                }
                radioPaidan.setChecked(false);
                radioOffline.setChecked(true);
                if ("01".equals(isQunzhu)) {
                    offlineListPresenter.loadOfflineList("01",companyId);
                }else {
                    offlineListPresenter.loadOfflineList("00",userId);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOfflineClick){
            if ("01".equals(isQunzhu)) {
                offlineListPresenter.loadOfflineList("01",companyId);
            }else {
                offlineListPresenter.loadOfflineList("00",userId);
            }
        }else {
            if ("01".equals(biaoshi)) {
                paidanListPresenter.loadPaidanList(companyId, "");
            }else {
                paidanListPresenter.loadPaidanList(companyId, userId);
            }
        }
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void updatePaidanList(final List<PaiDanInfo> paiDanList) {
        adapter = new PaidanAdapter(paiDanList,this,"00");
        lv_paidan_list.setAdapter(adapter);
        if (mProgress!=null){
            if (mProgress.isShowing()){
                mProgress.dismiss();
            }
        }
        lv_paidan_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                PaiDanInfo info = paiDanList.get(position);
                String remark = TextUtils.isEmpty(info.getRemark())?"":info.getRemark();
                String orderId = info.getOrderId();
                String merId = info.getMerId();
                String userId = info.getUserId();
                String orderState = info.getOrderState();
                String orderBody = TextUtils.isEmpty(info.getOrderBody())?"":info.getOrderBody();
                String name = TextUtils.isEmpty(info.getuName())?info.getUserId():info.getuName();
                String head = TextUtils.isEmpty(info.getuImage())?"l1":info.getuImage();
                String orderTime = TextUtils.isEmpty(info.getOrderTime())?"":info.getOrderTime();
                orderTime = orderTime.substring(0,4)+"-"+orderTime.substring(4,6)+"-"+orderTime.substring(6,8)+" "
                        +orderTime.substring(8,10)+":"+orderTime.substring(10,12);
                String type = TextUtils.isEmpty(info.getResv3())?"01":info.getResv3();
                String flg = TextUtils.isEmpty(info.getResv2())?"01":info.getResv2();
                String biaoshi1 ;
                if ("01".equals(biaoshi)){
                    biaoshi1= "不可操作";
                }else {
                    biaoshi1= "操作";
                    // TODO: 2017-01-17 自己的可以操作
                }
                if (orderState.equals("01")||orderState.equals("02")) {
                    Intent intent = new Intent(QiyePaidanListActivity.this,UOrderDetailActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("merId", merId);
                    intent.putExtra("orderState", orderState);
                    intent.putExtra("pypass", "");
                    intent.putExtra("biaoshi", "01");
                    startActivity(intent);
                }else {
                    if (flg.equals("01")) {
                        if (type.equals("01")) {
                            Intent intent = new Intent(QiyePaidanListActivity.this, MOrderDetailActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("userId", userId);
                            intent.putExtra("companyId",remark);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("biaoshi",biaoshi1);
                            intent.putExtra("orderTime",orderTime);
                            intent.putExtra("pypass", "");
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(QiyePaidanListActivity.this, MPaiDanDetActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("userId", userId);
                            intent.putExtra("companyId",remark);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("biaoshi",biaoshi1);
                            intent.putExtra("pypass", "");
                            intent.putExtra("orderTime",orderTime);
                            startActivity(intent);
                        }
                    }else if (flg.equals("02")){
                        if (type.equals("01")) {
                            Intent intent = new Intent(QiyePaidanListActivity.this, MOrderDetailTwoActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("merId", merId);
                            intent.putExtra("userId", userId);
                            intent.putExtra("companyId",remark);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("biaoshi",biaoshi1);
                            intent.putExtra("pypass", "");
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("name", name);
                            intent.putExtra("head", head);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(QiyePaidanListActivity.this, MPaiDanDetTwoActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("merId", merId);
                            intent.putExtra("userId", userId);
                            intent.putExtra("companyId",remark);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("biaoshi",biaoshi1);
                            intent.putExtra("pypass", "");
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("name", name);
                            intent.putExtra("head", head);
                            startActivity(intent);
                        }
                    }else if (flg.equals("03")){
                        if (type.equals("01")) {
                            Intent intent = new Intent(QiyePaidanListActivity.this, MOrderDetailThreeActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("userId", userId);
                            intent.putExtra("companyId",remark);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("biaoshi",biaoshi1);
                            intent.putExtra("orderTime",orderTime);
                            intent.putExtra("pypass", "");
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(QiyePaidanListActivity.this, MPaiDanDetThreeActivity.class);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("userId", userId);
                            intent.putExtra("companyId",remark);
                            intent.putExtra("orderState", orderState);
                            intent.putExtra("biaoshi",biaoshi1);
                            intent.putExtra("pypass", "");
                            intent.putExtra("orderTime", orderTime);
                            intent.putExtra("orderBody", orderBody);
                            intent.putExtra("name", name);
                            intent.putExtra("head", head);
                            startActivity(intent);
                        }
                    }else if (flg.equals("04")){
                        Intent intent = new Intent(QiyePaidanListActivity.this, MOrderDetailFourActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("userId", userId);
                        intent.putExtra("companyId",remark);
                        intent.putExtra("orderState", orderState);
                        intent.putExtra("biaoshi",biaoshi1);
                        intent.putExtra("orderTime",orderTime);
                        intent.putExtra("pypass", "");
                        startActivity(intent);
                    }else if (flg.equals("05")){
                        Intent intent = new Intent(QiyePaidanListActivity.this, MOrderDetaiFiveActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("userId", userId);
                        intent.putExtra("companyId",remark);
                        intent.putExtra("orderState", orderState);
                        intent.putExtra("biaoshi",biaoshi1);
                        intent.putExtra("orderTime",orderTime);
                        intent.putExtra("pypass", "");
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void updateOfflineList(final List<OffSendOrderList> paiDanList) {
        if (mProgress!=null){
            if (mProgress.isShowing()){
                mProgress.dismiss();
            }
        }
        if (paiDanList == null || paiDanList.size() == 0) {
            Toast.makeText(QiyePaidanListActivity.this,"暂时没有线下派单...",Toast.LENGTH_SHORT).show();
        }
        adapter = new PaidanAdapter(paiDanList,QiyePaidanListActivity.this);
        lv_paidan_list.setAdapter(adapter);
        lv_paidan_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OffSendOrderList offSendOrderList = paiDanList.get(position);
                Intent intent = new Intent(QiyePaidanListActivity.this,OfflineOrderActivity.class);
                intent.putExtra("offSendOrderList",offSendOrderList);
                intent.putExtra("biaoshi","01");
                startActivity(intent);
            }
        });
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

}
