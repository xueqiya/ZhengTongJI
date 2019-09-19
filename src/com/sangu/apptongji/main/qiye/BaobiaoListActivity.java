package com.sangu.apptongji.main.qiye;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sangu.apptongji.main.qiye.adapter.BaobiaoAdapter;
import com.sangu.apptongji.main.qiye.entity.BaobiaoInfo;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-02-20.
 */

public class BaobiaoListActivity extends BaseActivity {
    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_previous;
    private TextView tv_time;
    private TextView tv_next;
    private ListView list;
    private RelativeLayout rl_shijian;
    List<BaobiaoInfo> datas=null;
    private BaobiaoAdapter adapter=null;
    String selectTime,time1,tiaojian1="",tiaojian2,biaoshi;
    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    try {
                        String str = (String) msg.obj;
                        JSONObject object = new JSONObject(str);
                        JSONArray array = object.getJSONArray("workPlans");
                        datas = JSONParser.parseBaobiaoList(array,tiaojian1,tiaojian2);
                        adapter = new BaobiaoAdapter(datas, BaobiaoListActivity.this);
                        list.setAdapter(adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                BaobiaoInfo qingjiaInfo = datas.get(position);
                                String name = TextUtils.isEmpty(qingjiaInfo.getResv1()) ? qingjiaInfo.getUserId() : qingjiaInfo.getResv1();
                                String createTime = qingjiaInfo.getCreatTime();
                                String userId = qingjiaInfo.getUserId();
                                String qiyeId = qingjiaInfo.getCompanyId();
                                String planContent = qingjiaInfo.getPlanContent();
                                String planAdvise = qingjiaInfo.getPlanAdvise();
                                String signature1 = qingjiaInfo.getSignature1();
                                String signature2 = qingjiaInfo.getSignature2();
                                String signatureTime1 = qingjiaInfo.getSignatureTime1();
                                String signatureTime2 = qingjiaInfo.getSignatureTime2();
                                String resv1 = qingjiaInfo.getResv1();
                                String resv2 = qingjiaInfo.getResv2();
                                if (!"".equals(resv2)) {
                                    Intent intent = new Intent(BaobiaoListActivity.this, WorkstatementTwoActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("resv1", resv1);
                                    intent.putExtra("resv2", resv2);
                                    intent.putExtra("createTime", createTime);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("planAdvise", planAdvise);
                                    intent.putExtra("signature1", signature1);
                                    intent.putExtra("qiyeId", qiyeId);
                                    intent.putExtra("signature2", signature2);
                                    intent.putExtra("signatureTime1", signatureTime1);
                                    intent.putExtra("signatureTime2", signatureTime2);
                                    intent.putExtra("biaoshi", biaoshi);
                                    if (!"".equals(signature2)) {
                                        intent.putExtra("biaoshi2", "quanbu");
                                    } else {
                                        intent.putExtra("biaoshi2", "gongsi");
                                    }
                                    startActivityForResult(intent, 0);
                                } else {
                                    Intent intent = new Intent(BaobiaoListActivity.this, WorkstatementActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("planContent", planContent);
                                    intent.putExtra("createTime", createTime);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("planAdvise", planAdvise);
                                    intent.putExtra("signature1", signature1);
                                    intent.putExtra("qiyeId", qiyeId);
                                    intent.putExtra("signature2", signature2);
                                    intent.putExtra("signatureTime1", signatureTime1);
                                    intent.putExtra("signatureTime2", signatureTime2);
                                    intent.putExtra("biaoshi", biaoshi);
                                    if (!"".equals(signature2)) {
                                        intent.putExtra("biaoshi2", "quanbu");
                                    } else {
                                        intent.putExtra("biaoshi2", "gongsi");
                                    }
                                    startActivityForResult(intent, 0);
                                }
                            }
                        });
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (datas!=null){
            datas.clear();
            datas=null;
        }
    }
    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_friend_zhuanzhang);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_previous = (TextView) findViewById(R.id.tv_previous);
        tv_time = (TextView) findViewById(R.id.tv_from);
        tv_next = (TextView) findViewById(R.id.tv_next);
        list = (ListView) findViewById(R.id.list);
        rl_shijian = (RelativeLayout) findViewById(R.id.rl_shijian);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        tiaojian2 = this.getIntent().hasExtra("tiaojian2")?this.getIntent().getStringExtra("tiaojian2"):"";
        biaoshi = this.getIntent().hasExtra("biaoshi")?this.getIntent().getStringExtra("biaoshi"):"";
        tv_back.setText("企业详情");
        tv_title.setText("员工工作报表");
        rl_shijian.setVisibility(View.VISIBLE);
        time1 = getcurrentTime();
        selectTime = time1;
        tv_time.setText(time1);
        tv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = getDateStr1(selectTime,1);
                tiaojian1 = time.substring(0,4)+time.substring(5,7)+time.substring(8,10);
                setViews();
                tv_time.setText(time);
            }
        });
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectTime.equals(time1)){
                    return;
                }
                String time = getDateStr2(selectTime,1);
                tiaojian1 = time.substring(0,4)+time.substring(5,7)+time.substring(8,10);
                setViews();
                tv_time.setText(time);
            }
        });
        setViews();
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
                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                param.put("type","07");
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private String getDateStr1(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() - (long)Num * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        selectTime = dateOk;
        return dateOk;
    }
    private String getDateStr2(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() + (long)Num * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        selectTime = dateOk;
        return dateOk;
    }
    private String getcurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            setViews();
        }
    }

    private void setViews() {
        final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        String url = FXConstant.URL_QIYE_BAOBIAOLIST;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = s;
                myhandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("错误信息",volleyError.toString());
                Toast.makeText(BaobiaoListActivity.this,"网络出问题了...",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("companyId",qiyeId);
//                if ("01".equals(tiaojian2)){
//                    param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
//                }
                return param;
            }
        };
        MySingleton.getInstance(BaobiaoListActivity.this).addToRequestQueue(request);
    }
}
