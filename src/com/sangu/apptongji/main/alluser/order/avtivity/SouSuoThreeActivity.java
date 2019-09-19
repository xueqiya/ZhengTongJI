package com.sangu.apptongji.main.alluser.order.avtivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-09.
 */

public class SouSuoThreeActivity extends BaseActivity implements View.OnClickListener{
    private EditText et_search;
    private RadioButton radiomc;
    private RadioButton radiozhy;
    private RadioButton radio_bzhjin_Bx;
    private RadioButton radio_youbzhjin;
    private Button btn_send;
    private Button btn_clear;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_sousuo_three);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_send = (Button) findViewById(R.id.btn_send);
        radiozhy = (RadioButton) findViewById(R.id.radiozhy);
        radiomc = (RadioButton) findViewById(R.id.radiomc);
        radio_bzhjin_Bx = (RadioButton) findViewById(R.id.radio_bzhjin_Bx);
        radio_youbzhjin = (RadioButton) findViewById(R.id.radio_youbzhjin);
        et_search = (EditText) findViewById(R.id.et_search);
        radio_bzhjin_Bx.setChecked(true);
        radiozhy.setOnClickListener(this);
        radiomc.setOnClickListener(this);
        radio_bzhjin_Bx.setOnClickListener(this);
        radio_youbzhjin.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_send.setOnClickListener(this);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radio_bzhjin_Bx:
                radio_youbzhjin.setChecked(false);
                radio_bzhjin_Bx.setChecked(true);
                radio_bzhjin_Bx.setTextColor(Color.WHITE);
                radio_youbzhjin.setTextColor(Color.rgb(170,170,170));
                break;
            case R.id.radio_youbzhjin:
                radio_bzhjin_Bx.setChecked(false);
                radio_youbzhjin.setChecked(true);
                radio_youbzhjin.setTextColor(Color.WHITE);
                radio_bzhjin_Bx.setTextColor(Color.rgb(170,170,170));
                break;
            case R.id.radiozhy:
                radiomc.setChecked(false);
                radiozhy.setChecked(true);
                break;
            case R.id.radiomc:
                radiomc.setChecked(true);
                radiozhy.setChecked(false);
                break;
            case R.id.btn_send:
                String content = TextUtils.isEmpty(et_search.getText().toString().trim())?"0":et_search.getText().toString().trim();
                Intent intent = new Intent();
                if (radio_youbzhjin.isChecked()&&radiozhy.isChecked()) {
                    intent.putExtra("baozhjin","1");
                }else {
                    intent.putExtra("baozhjin","0");
                }
                if (radiozhy.isChecked()) {
                    intent.putExtra("qiye_mch", "0");
                    intent.putExtra("qiye_zhy", content);
                    insertSearchRecord("企业_专业",content);
                }else {
                    intent.putExtra("qiye_mch", content);
                    intent.putExtra("qiye_zhy", "0");
                    insertSearchRecord("企业_名称",content);
                }
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_clear:
                Intent intent1 = new Intent();
                intent1.putExtra("baozhjin","0");
                intent1.putExtra("qiye_mch","0");
                intent1.putExtra("qiye_zhy","0");
                intent1.putExtra("isclear","1");
                setResult(RESULT_OK, intent1);
                finish();
                break;
        }
    }

    private void insertSearchRecord(final String type, final String content) {
        String url = FXConstant.URL_INSERT_SEARCH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "insertSearchRecord onResponse" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "insertSearchRecord onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //uId=&searchtype=&searchcontent=
                Map<String, String> param = new HashMap<>();
                param.put("uId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("searchtype", type);
                param.put("searchcontent", content);
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
}
