package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.adapter.UserNewsAdapter;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.UserAdapter;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushMessageDetailActivity extends BaseActivity {

    private TextView tv_allcount,tv_marginCount,tv_clean,tv_again,tv_sendClick;
    private EditText et_distance,et_keyworld;
    private ListView listView;
    private String task_label,task_position,dynamicSeq,createTime,sID,distance,deviceType,content,task_locaName,uName;

    private Dialog mWeiboDialog;

    List<UserAll> datas = new ArrayList<UserAll>();
    private UserNewsAdapter adapter=null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_pushmessagedetail);

        task_label = getIntent().getStringExtra("task_label");
        task_position = getIntent().getStringExtra("task_position");
        dynamicSeq = getIntent().getStringExtra("dynamicSeq");
        createTime = getIntent().getStringExtra("createTime");
        sID = getIntent().getStringExtra("sID");
        distance = getIntent().getStringExtra("distance");
        deviceType = getIntent().getStringExtra("deviceType");
        content = getIntent().getStringExtra("content");
        task_locaName = getIntent().getStringExtra("task_locaName");
        uName = getIntent().getStringExtra("uName");


        tv_allcount = (TextView) findViewById(R.id.tv_allcount);
        tv_marginCount = (TextView) findViewById(R.id.tv_marginCount);
        tv_clean = (TextView) findViewById(R.id.tv_clean);
        tv_again = (TextView) findViewById(R.id.tv_again);

        listView = (ListView) findViewById(R.id.listView);

        et_distance = (EditText) findViewById(R.id.et_distance);
        et_keyworld = (EditText) findViewById(R.id.et_keyworld);

        tv_sendClick = (TextView) findViewById(R.id.tv_sendClick);

        tv_sendClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //发送短信给师傅
                Intent intent = new Intent(PushMessageDetailActivity.this,SendMessageToUserActivity.class);

                if (et_keyworld.getText().toString().trim() != null && et_keyworld.getText().toString().trim().length() > 0){

                    intent.putExtra("task_label",et_keyworld.getText().toString().trim());

                }else {

                    intent.putExtra("task_label",task_label);
                }


                intent.putExtra("task_position",task_position);
                intent.putExtra("dynamicSeq",dynamicSeq);
                intent.putExtra("createTime",createTime);
                intent.putExtra("sID",sID);
                intent.putExtra("deviceType",deviceType);
                intent.putExtra("content",content);
                intent.putExtra("distance",et_distance.getText().toString().trim());
                intent.putExtra("task_locaName",task_locaName);
                intent.putExtra("uName",uName);
                Bundle bundle = new Bundle();
                bundle.putSerializable("datas", (Serializable) datas);
                intent.putExtras(bundle);

                startActivity(intent);


             //   intent.putExtra("deviceType",deviceType);

            }
        });


        et_distance.setText(distance);
        et_keyworld.setText(task_label);

        RequestAllList();


        tv_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datas.clear();

                adapter.notifyDataSetChanged();
            }
        });

        tv_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestAllList();

            }
        });

    }

    private void RequestAllList(){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(PushMessageDetailActivity.this, "加载中...");

        String url = FXConstant.URL_SELECTUSERBYANTISTOP;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);

                try {

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("code");

                    List<UserAll> users = JSONParser.parseUserList(array);

                    if (datas.size() > 0){

                        List<UserAll> users2 = new ArrayList<>();

                        for (UserAll all : users){

                            String have = "no";

                            for (UserAll all1 : datas){

                                if (all1.getuLoginId().equals(all.getuLoginId())){
                                    have = "yes";
                                }

                            }

                            if (have.equals("yes")){

                            }else {

                                users2.add(all);
                            }

                        }

                        if (users2.size() > 0){
                            datas.addAll(users2);
                        }

                    }else {

                        datas.addAll(users);
                    }


                    //datas = users;

                    if (datas.size() > 0){

                        String[] coor = task_position.split("\\|");

                        String  lng = coor[1];
                        String  lat = coor[0];

                        adapter = new UserNewsAdapter(datas,PushMessageDetailActivity.this,lat,lng);

                        listView.setAdapter(adapter);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                ClickItem(i);

                            }
                        });

                    }

                    int message = 0;
                    int margin = 0;

                    for (UserAll all : datas){

                        if (Double.parseDouble(all.getMessageOrderCount()) > 0){
                            message ++;
                        }

                        String margan1 = all.getMargen1();
                        String margan2 = all.getMargen2();
                        String margan3 = all.getMargen3();
                        String margan4 = all.getMargen4();

                        Double allmargin = 0.0;
                        if (margan1 != null){
                            if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                                allmargin = allmargin + Double.valueOf(margan1);
                            }
                        }
                        if (margan2 != null){
                            if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                                allmargin = allmargin + Double.valueOf(margan2);
                            }
                        }
                        if (margan3 != null){
                            if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                                allmargin = allmargin + Double.valueOf(margan3);
                            }
                        }
                        if (margan4 != null){
                            if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                                allmargin = allmargin + Double.valueOf(margan4);
                            }
                        }

                        if (allmargin > 99){
                            margin ++;
                        }

                    }


                    tv_allcount.setText("一共"+datas.size()+"条数据"+"("+message+"个有短信)");

                    tv_marginCount.setText("有质保的共"+margin+"人");

                } catch (JSONException e) {

                    e.printStackTrace();

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();

                String[] coor = task_position.split("\\|");

                param.put("userId",sID);
                param.put("log",coor[1]);
                param.put("lat",coor[0]);
                param.put("task_label",et_keyworld.getText().toString().trim());
                param.put("km",et_distance.getText().toString().trim());

                return param;
            }
        };

        MySingleton.getInstance(PushMessageDetailActivity.this).addToRequestQueue(request);

    }


    private void ClickItem(final int i){

        final LayoutInflater inflater1 = LayoutInflater.from(PushMessageDetailActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog collectionDialog = new AlertDialog.Builder(PushMessageDetailActivity.this, R.style.Dialog).create();
        collectionDialog.show();
        collectionDialog.getWindow().setContentView(layout1);
        collectionDialog.setCanceledOnTouchOutside(true);
        collectionDialog.setCancelable(true);
        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        title.setText("提示");
        btnOK1.setText("确定");
        btnCancel1.setText("取消");

        title_tv1.setText("是否剔除该用户？");

        btnOK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                collectionDialog.dismiss();

                datas.remove(i);

                adapter.notifyDataSetChanged();;

                int message = 0;
                int margin = 0;

                for (UserAll all : datas){

                    if (Double.parseDouble(all.getMessageOrderCount()) > 0){
                        message ++;
                    }

                    String margan1 = all.getMargen1();
                    String margan2 = all.getMargen2();
                    String margan3 = all.getMargen3();
                    String margan4 = all.getMargen4();

                    Double allmargin = 0.0;
                    if (margan1 != null){
                        if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                            allmargin = allmargin + Double.valueOf(margan1);
                        }
                    }
                    if (margan2 != null){
                        if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                            allmargin = allmargin + Double.valueOf(margan2);
                        }
                    }
                    if (margan3 != null){
                        if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                            allmargin = allmargin + Double.valueOf(margan3);
                        }
                    }
                    if (margan4 != null){
                        if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                            allmargin = allmargin + Double.valueOf(margan4);
                        }
                    }

                    if (allmargin > 99){
                        margin ++;
                    }

                }


                tv_allcount.setText("一共"+datas.size()+"条数据"+"("+message+"个有短信)");

                tv_marginCount.setText("有质保的共"+margin+"人");

            }
        });


        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectionDialog.dismiss();
            }
        });

    }

}
