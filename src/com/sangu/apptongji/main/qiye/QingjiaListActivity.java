package com.sangu.apptongji.main.qiye;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.sangu.apptongji.main.qiye.adapter.QingjiaAdapter;
import com.sangu.apptongji.main.qiye.entity.QingjiaInfo;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-01-11.
 */

public class QingjiaListActivity extends BaseActivity {
    private TextView tv_back;
    private TextView tv_title;
    private ListView list=null;
    private String biaoshi=null;
    private QingjiaAdapter adapter=null;
    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    try {
                        String str = (String) msg.obj;
                        JSONObject object = new JSONObject(str);
                        JSONArray array = object.getJSONArray("leaveInfos");
                        final List<QingjiaInfo> datas = JSONParser.parseQingjiaList(array);
                        adapter = new QingjiaAdapter(datas,QingjiaListActivity.this);
                        list.setAdapter(adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                QingjiaInfo qingjiaInfo = datas.get(position);
                                String name = qingjiaInfo.getResv1();
                                String remark = qingjiaInfo.getRemark();
                                String createTime = qingjiaInfo.getCreatTime();
                                String userId = qingjiaInfo.getUserId();
                                String companyId = qingjiaInfo.getCompanyId();
                                String leaveTimeStart = qingjiaInfo.getLeaveTimeStart();
                                String leaveTimeEnd = qingjiaInfo.getLeaveTimeEnd();
                                String leaveReason = qingjiaInfo.getLeaveReason();
                                String pic1 = qingjiaInfo.getSignPic1();
                                String pic2 = qingjiaInfo.getSignPic2();
                                Intent intent = new Intent(QingjiaListActivity.this,QingjiaActivity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("remark",remark);
                                intent.putExtra("createTime",createTime);
                                intent.putExtra("userId",userId);
                                intent.putExtra("biaoshi2",biaoshi);
                                intent.putExtra("leaveTimeEnd",leaveTimeEnd);
                                intent.putExtra("leaveTimeStart",leaveTimeStart);
                                intent.putExtra("companyId",companyId);
                                intent.putExtra("leaveReason",leaveReason);
                                intent.putExtra("pic1",pic1);
                                intent.putExtra("pic2",pic2);
                                intent.putExtra("biaoshi","01");
                                startActivityForResult(intent,0);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        list = (ListView) findViewById(R.id.list);
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        tv_back.setText("企业详情");
        tv_title.setText("请假列表");
        setViews();
        deletePush();
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
                param.put("type","06");
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
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
        String url = FXConstant.URL_QINGJIA_LIST;
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
                Toast.makeText(QingjiaListActivity.this,"网络出问题了...",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("companyId",qiyeId);
                if ("00".equals(biaoshi)){
                    param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                }
                return param;
            }
        };
        MySingleton.getInstance(QingjiaListActivity.this).addToRequestQueue(request);
    }
}
