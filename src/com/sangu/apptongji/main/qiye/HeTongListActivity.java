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
import com.sangu.apptongji.main.qiye.adapter.HeTongListAdapter;
import com.sangu.apptongji.main.qiye.entity.HeTongInfo;
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
 * Created by Administrator on 2017-02-16.
 */

public class HeTongListActivity extends BaseActivity {
    private ListView lv_hetong=null;
    private HeTongListAdapter adapter=null;
    private TextView tv_shezhi;
    HetongListThread thread=null;
    String biaoshi=null,image1,image2,body1,body2,feiyong1,feiyong2,companyName;
    private Handler hetListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (thread!=null){
                        thread.interrupt();
                        thread = null;
                    }
                    String s = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(s);
                        JSONArray array = object.optJSONArray("agreementInfos");
                        final List<HeTongInfo> heTongInfos = JSONParser.parseHetongList(array);
                        adapter = new HeTongListAdapter(heTongInfos,HeTongListActivity.this);
                        lv_hetong.setAdapter(adapter);
                        lv_hetong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final String comSignature = heTongInfos.get(position).getComSignature();
                                final String createTime = heTongInfos.get(position).getCreateTime();
                                final String agreement1 = heTongInfos.get(position).getAgreement1();
                                final String agreement2 = heTongInfos.get(position).getAgreement2();
                                final String userSignature = heTongInfos.get(position).getUserSignature();
                                final String signatureTime = heTongInfos.get(position).getSignatureTime();
                                final String resv1 = heTongInfos.get(position).getResv1();
                                String image,body,feiyong,biaoshi,fufei;
                                if ("0".equals(resv1)){
                                    image = image1;
                                    body = body1;
                                    feiyong = feiyong1;
                                    biaoshi = "yingpin";
                                    if (feiyong1!=null&&!"".equals(feiyong1)&&Double.parseDouble(feiyong1)>0){
                                        fufei = "付费";
                                    }else {
                                        fufei = "无付费";
                                    }
                                }else {
                                    image = image2;
                                    body = body2;
                                    feiyong = feiyong2;
                                    biaoshi = "jiameng";
                                    if (feiyong2!=null&&!"".equals(feiyong2)&&Double.parseDouble(feiyong2)>0){
                                        fufei = "付费";
                                    }else {
                                        fufei = "无付费";
                                    }
                                }
                                Log.e("hetonglist,resv1",resv1);
                                Log.e("hetonglist,image",image);
                                Log.e("hetonglist,body",body);
                                Log.e("hetonglist,feiyong",feiyong);
                                startActivity(new Intent(HeTongListActivity.this, QianDingHetongActivity.class).putExtra("agreement1", agreement1).putExtra("agreement2", agreement2).putExtra("createTime", createTime).putExtra("userSignature", userSignature)
                                        .putExtra("biaoshi2", "quanbu").putExtra("comSignature", comSignature).putExtra("signatureTime",signatureTime)
                                        .putExtra("image",image).putExtra("body",body).putExtra("feiyong",feiyong).putExtra("biaoshi",biaoshi).putExtra("fufei",fufei));
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
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.item_hetong);
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        image1 = this.getIntent().getStringExtra("image1");
        image2 = this.getIntent().getStringExtra("image2");
        body1 = this.getIntent().getStringExtra("body1");
        body2 = this.getIntent().getStringExtra("body2");
        feiyong1 = this.getIntent().getStringExtra("feiyong1");
        feiyong2 = this.getIntent().getStringExtra("feiyong2");
        companyName = this.getIntent().getStringExtra("companyName");
        lv_hetong = (ListView) findViewById(R.id.lv_hetong);
        tv_shezhi = (TextView) findViewById(R.id.tv_shezhi);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        thread = new HetongListThread();
        thread.start();
        if ("gongsi".equals(biaoshi)){
            tv_shezhi.setVisibility(View.VISIBLE);
        }else {
            tv_shezhi.setVisibility(View.INVISIBLE);
        }
        tv_shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(HeTongListActivity.this, JoinQiyeShfeiActivity.class).putExtra("companyName", companyName).putExtra("biaoshi","01"), 1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (thread!=null&&thread.isInterrupted()){
            thread.start();
        }else if (thread==null){
            thread = new HetongListThread();
            thread.start();
        }

    }

    class HetongListThread extends Thread{
        @Override
        public void run() {
            final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
            String url = FXConstant.URL_QIYE_HETONGLIST;
            StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = s;
                    hetListHandler.sendMessage(msg);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String ,String> param = new HashMap<>();
                    if ("gongsi".equals(biaoshi)){
                        param.put("companyId",qiyeId);
                    }else {
                        param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                    }
                    return param;
                }
            };
            MySingleton.getInstance(HeTongListActivity.this).addToRequestQueue(request);
        }
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread!=null){
            thread.interrupt();
            thread = null;
        }
    }
}
