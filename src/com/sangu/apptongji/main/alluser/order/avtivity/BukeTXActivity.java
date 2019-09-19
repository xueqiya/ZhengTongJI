package com.sangu.apptongji.main.alluser.order.avtivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.order.adapter.BktxListAdapter;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-07-24.
 */

public class BukeTXActivity extends BaseActivity {
    private TextView tv_back;
    private TextView tv_title;
    private ListView list=null;
    private String data,biaoshi;
    private BktxListAdapter adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_friend_zhuanzhang);
        data = getIntent().getStringExtra("data");
        biaoshi = getIntent().getStringExtra("biaoshi");
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        list = (ListView) findViewById(R.id.list);
        tv_back.setText("余额");
        tv_title.setText("交易金额");
        if (data!=null){
            JSONObject object = JSON.parseObject(data);
            JSONArray array = object.getJSONArray("code");
            List<JSONObject> objects = new ArrayList<>();
            for (int i=0;i<array.size();i++){
                JSONObject object1 = array.getJSONObject(i);
                String jine = object1.getString("order_amt");
                if ("000".equals(biaoshi)&&jine!=null&&Double.parseDouble(jine)>0){
                    objects.add(object1);
                }else if ("001".equals(biaoshi)){
//                    if (jine2!=null){
//                        if (Double.parseDouble(jine2)>0){
//                            objects.add(object1);
//                        }
//                    }else {
//                        objects.add(object1);
//                    }
                    objects.add(object1);
                }
            }
            adapter = new BktxListAdapter(this,objects,biaoshi);
            list.setAdapter(adapter);
        }
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
}
