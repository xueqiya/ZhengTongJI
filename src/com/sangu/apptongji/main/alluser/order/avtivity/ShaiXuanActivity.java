package com.sangu.apptongji.main.alluser.order.avtivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;

/**
 * Created by Administrator on 2017-06-09.
 */

public class ShaiXuanActivity extends BaseActivity implements View.OnClickListener{
    private RadioButton rb_xb_bx,rb_xb_nan,rb_xb_nv,rb_zhy_bx,rb_zhy_has,rb_qy_bx,rb_qy_has,rb_bzj_bx,rb_bzj_has
            ,rb_bt_bx,rb_bt_has,rb_jy_bx,rb_jy_has,rb_jdl_bx,rb_jdl_has,rb_hb_bx,rb_hb_has,rb_nl_bx;
    private EditText et_start,et_end;
    private Button btn_sousuo,btn_clear;
    private String hasZhy,hasCom,sex,ageStart,hasBao,hasJy,hasJdl,hasHb,ageEnd;
    private LinearLayout ll_et;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_shaixuan);
        hasZhy = getIntent().getStringExtra("hasZhy");
        hasCom = getIntent().getStringExtra("hasCom");
        sex = getIntent().getStringExtra("sex");
        ageStart = getIntent().getStringExtra("ageStart");
        ageEnd = getIntent().getStringExtra("ageEnd");
        hasBao = getIntent().getStringExtra("hasBao");
        hasJy = getIntent().getStringExtra("hasJy");
        hasJdl = getIntent().getStringExtra("hasJdl");
        hasHb = getIntent().getStringExtra("hasHb");
        initView();
        setListener();
        initViews();
    }

    private void initViews() {
        if (sex!=null&&!"".equals(sex)){
            rb_xb_bx.setChecked(false);
            rb_xb_bx.setTextColor(Color.rgb(170,170,170));
            if ("00".equals(sex)){
                rb_xb_nv.setChecked(true);
                rb_xb_nv.setTextColor(Color.WHITE);
            }else {
                rb_xb_nan.setChecked(true);
                rb_xb_nan.setTextColor(Color.WHITE);
            }
        }else {
            rb_xb_bx.setChecked(true);
            rb_xb_bx.setTextColor(Color.WHITE);
        }
        if (hasZhy!=null&&!"".equals(hasZhy)){
            rb_zhy_bx.setChecked(false);
            rb_zhy_bx.setTextColor(Color.rgb(170,170,170));
            rb_zhy_has.setChecked(true);
            rb_zhy_has.setTextColor(Color.WHITE);
        }else {
            rb_zhy_bx.setChecked(true);
            rb_zhy_bx.setTextColor(Color.WHITE);
        }
        if (hasCom!=null&&!"".equals(hasCom)){
            rb_qy_bx.setChecked(false);
            rb_qy_bx.setTextColor(Color.rgb(170,170,170));
            rb_qy_has.setChecked(true);
            rb_qy_has.setTextColor(Color.WHITE);
        }else {
            rb_qy_bx.setChecked(true);
            rb_qy_bx.setTextColor(Color.WHITE);
        }
        if (hasBao!=null&&!"".equals(hasBao)){
            rb_bzj_bx.setChecked(false);
            rb_bzj_bx.setTextColor(Color.rgb(170,170,170));
            rb_bzj_has.setChecked(true);
            rb_bzj_has.setTextColor(Color.WHITE);
        }else {
            rb_bzj_bx.setChecked(true);
            rb_bzj_bx.setTextColor(Color.WHITE);
        }
        if (hasJy!=null&&!"".equals(hasJy)){
            rb_jy_bx.setChecked(false);
            rb_jy_bx.setTextColor(Color.rgb(170,170,170));
            rb_jy_has.setChecked(true);
            rb_jy_has.setTextColor(Color.WHITE);
        }else {
            rb_jy_bx.setChecked(true);
            rb_jy_bx.setTextColor(Color.WHITE);
        }
        if (hasJdl!=null&&!"".equals(hasJdl)){
            rb_jdl_bx.setChecked(false);
            rb_jdl_bx.setTextColor(Color.rgb(170,170,170));
            rb_jdl_has.setChecked(true);
            rb_jdl_has.setTextColor(Color.WHITE);
        }else {
            rb_jdl_bx.setChecked(true);
            rb_jdl_bx.setTextColor(Color.WHITE);
        }
        if (hasHb!=null&&!"".equals(hasHb)){
            rb_hb_bx.setChecked(false);
            rb_hb_bx.setTextColor(Color.rgb(170,170,170));
            rb_hb_has.setChecked(true);
            rb_hb_has.setTextColor(Color.WHITE);
        }else {
            rb_hb_bx.setChecked(true);
            rb_hb_bx.setTextColor(Color.WHITE);
        }
        if (ageStart!=null&&!"".equals(ageStart)){
            et_start.setText(ageStart);
            rb_nl_bx.setChecked(false);
            rb_nl_bx.setTextColor(Color.rgb(170,170,170));
        }
        if (ageEnd!=null&&!"".equals(ageEnd)){
            et_end.setText(ageEnd);
            rb_nl_bx.setChecked(false);
            rb_nl_bx.setTextColor(Color.rgb(170,170,170));
        }
        if ((ageEnd==null&&ageStart==null)||(ageEnd.equals("")&&ageStart.equals(""))){
            rb_nl_bx.setChecked(true);
            rb_nl_bx.setTextColor(Color.WHITE);
        }else {
            rb_nl_bx.setChecked(false);
            rb_nl_bx.setTextColor(Color.rgb(170,170,170));
        }
    }

    private void initView() {
        ll_et = (LinearLayout) findViewById(R.id.ll_et);
        rb_xb_bx = (RadioButton) findViewById(R.id.rb_xb_bx);
        rb_xb_nan = (RadioButton) findViewById(R.id.rb_xb_nan);
        rb_xb_nv = (RadioButton) findViewById(R.id.rb_xb_nv);
        rb_zhy_bx = (RadioButton) findViewById(R.id.rb_zhy_bx);
        rb_zhy_has = (RadioButton) findViewById(R.id.rb_zhy_has);
        rb_qy_bx = (RadioButton) findViewById(R.id.rb_qy_bx);
        rb_qy_has = (RadioButton) findViewById(R.id.rb_qy_has);
        rb_bzj_bx = (RadioButton) findViewById(R.id.rb_bzj_bx);
        rb_bzj_has = (RadioButton) findViewById(R.id.rb_bzj_has);
        rb_bt_bx = (RadioButton) findViewById(R.id.rb_bt_bx);
        rb_bt_has = (RadioButton) findViewById(R.id.rb_bt_has);
        rb_jy_bx = (RadioButton) findViewById(R.id.rb_jy_bx);
        rb_jy_has = (RadioButton) findViewById(R.id.rb_jy_has);
        rb_jdl_bx = (RadioButton) findViewById(R.id.rb_jdl_bx);
        rb_jdl_has = (RadioButton) findViewById(R.id.rb_jdl_has);
        rb_hb_bx = (RadioButton) findViewById(R.id.rb_hb_bx);
        rb_hb_has = (RadioButton) findViewById(R.id.rb_hb_has);
        rb_nl_bx = (RadioButton) findViewById(R.id.rb_nl_bx);
        et_start = (EditText) findViewById(R.id.et_start);
        et_end = (EditText) findViewById(R.id.et_end);
        btn_sousuo = (Button) findViewById(R.id.btn_sousuo);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        ll_et.setFocusable(true);
        ll_et.setFocusableInTouchMode(true);
        ll_et.requestFocus();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void setListener() {
        btn_clear.setOnClickListener(this);
        rb_xb_bx.setOnClickListener(this);
        rb_xb_nan.setOnClickListener(this);
        rb_xb_nv.setOnClickListener(this);
        rb_zhy_bx.setOnClickListener(this);
        rb_zhy_has.setOnClickListener(this);
        rb_qy_bx.setOnClickListener(this);
        rb_qy_has.setOnClickListener(this);
        rb_bzj_bx.setOnClickListener(this);
        rb_bzj_has.setOnClickListener(this);
        rb_bt_bx.setOnClickListener(this);
        rb_bt_has.setOnClickListener(this);
        rb_jy_bx.setOnClickListener(this);
        rb_jy_has.setOnClickListener(this);
        rb_jdl_bx.setOnClickListener(this);
        rb_jdl_has.setOnClickListener(this);
        rb_hb_bx.setOnClickListener(this);
        rb_hb_has.setOnClickListener(this);
        rb_nl_bx.setOnClickListener(this);
        btn_sousuo.setOnClickListener(this);
        et_start.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_end.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_start.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s!=null&&s.length()>2){
                    s.delete(2,3);
                }
                if (s!=null&&s.toString().length()>0&&Double.parseDouble(s.toString())>0){
                    rb_nl_bx.setChecked(false);
                    rb_xb_bx.setTextColor(Color.rgb(170,170,170));
                }else {
                    rb_nl_bx.setChecked(true);
                    rb_nl_bx.setTextColor(Color.WHITE);
                }
            }
        });
        et_end.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s!=null&&s.length()>2){
                    s.delete(2,3);
                }
                if (s!=null&&s.toString().length()>0&&Double.parseDouble(s.toString())>0){
                    rb_nl_bx.setChecked(false);
                    rb_nl_bx.setTextColor(Color.rgb(170,170,170));
                }else {
                    rb_nl_bx.setChecked(true);
                    rb_nl_bx.setTextColor(Color.WHITE);
                }
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    private void clearAll() {
        rb_xb_bx.setChecked(true);
        rb_xb_bx.setTextColor(Color.WHITE);
        rb_xb_nan.setChecked(false);
        rb_xb_nan.setTextColor(Color.rgb(170,170,170));
        rb_xb_nv.setChecked(false);
        rb_xb_nv.setTextColor(Color.rgb(170,170,170));
        rb_zhy_bx.setChecked(true);
        rb_zhy_bx.setTextColor(Color.WHITE);
        rb_zhy_has.setChecked(false);
        rb_zhy_has.setTextColor(Color.rgb(170,170,170));
        rb_qy_bx.setChecked(true);
        rb_qy_bx.setTextColor(Color.WHITE);
        rb_qy_has.setChecked(false);
        rb_qy_has.setTextColor(Color.rgb(170,170,170));
        rb_bzj_bx.setChecked(true);
        rb_bzj_bx.setTextColor(Color.WHITE);
        rb_bzj_has.setChecked(false);
        rb_bzj_has.setTextColor(Color.rgb(170,170,170));
        rb_bt_bx.setChecked(true);
        rb_bt_bx.setTextColor(Color.WHITE);
        rb_bt_has.setChecked(false);
        rb_bt_has.setTextColor(Color.rgb(170,170,170));
        rb_jy_bx.setChecked(true);
        rb_jy_bx.setTextColor(Color.WHITE);
        rb_jy_has.setChecked(false);
        rb_jy_has.setTextColor(Color.rgb(170,170,170));
        rb_jdl_bx.setChecked(true);
        rb_jdl_bx.setTextColor(Color.WHITE);
        rb_jdl_has.setChecked(false);
        rb_jdl_has.setTextColor(Color.rgb(170,170,170));
        rb_hb_bx.setChecked(true);
        rb_hb_bx.setTextColor(Color.WHITE);
        rb_hb_has.setChecked(false);
        rb_hb_has.setTextColor(Color.rgb(170,170,170));
        rb_nl_bx.setChecked(true);
        rb_nl_bx.setTextColor(Color.WHITE);
        et_start.setText(null);
        et_end.setText(null);
        hasZhy = null;
        sex = null;
        hasCom = null;
        hasBao = null;
        hasJy = null;
        hasJdl = null;
        hasHb = null;
        ageStart = null;
        ageEnd = null;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_clear:
                clearAll();
                Intent intent2 = new Intent();
                setResult(RESULT_OK,intent2);
                finish();
                break;
            case R.id.rb_xb_bx:
                rb_xb_bx.setChecked(true);
                rb_xb_bx.setTextColor(Color.WHITE);
                rb_xb_nan.setChecked(false);
                rb_xb_nan.setTextColor(Color.rgb(170,170,170));
                rb_xb_nv.setChecked(false);
                rb_xb_nv.setTextColor(Color.rgb(170,170,170));
                break;
            case R.id.rb_xb_nan:
                rb_xb_bx.setChecked(false);
                rb_xb_bx.setTextColor(Color.rgb(170,170,170));
                rb_xb_nan.setChecked(true);
                rb_xb_nan.setTextColor(Color.WHITE);
                rb_xb_nv.setChecked(false);
                rb_xb_nv.setTextColor(Color.rgb(170,170,170));
                break;
            case R.id.rb_xb_nv:
                rb_xb_bx.setChecked(false);
                rb_xb_bx.setTextColor(Color.rgb(170,170,170));
                rb_xb_nv.setChecked(true);
                rb_xb_nv.setTextColor(Color.WHITE);
                rb_xb_nan.setChecked(false);
                rb_xb_nan.setTextColor(Color.rgb(170,170,170));
                break;
            case R.id.rb_zhy_bx:
                rb_zhy_has.setChecked(false);
                rb_zhy_has.setTextColor(Color.rgb(170,170,170));
                rb_zhy_bx.setChecked(true);
                rb_zhy_bx.setTextColor(Color.WHITE);
                break;
            case R.id.rb_zhy_has:
                rb_zhy_has.setChecked(true);
                rb_zhy_has.setTextColor(Color.WHITE);
                rb_zhy_bx.setChecked(false);
                rb_zhy_bx.setTextColor(Color.rgb(170,170,170));
                break;
            case R.id.rb_qy_bx:
                rb_qy_bx.setChecked(true);
                rb_qy_bx.setTextColor(Color.WHITE);
                rb_qy_has.setChecked(false);
                rb_qy_has.setTextColor(Color.rgb(170,170,170));
                break;
            case R.id.rb_qy_has:
                rb_qy_has.setChecked(true);
                rb_qy_has.setTextColor(Color.WHITE);
                rb_qy_bx.setChecked(false);
                rb_qy_bx.setTextColor(Color.rgb(170,170,170));
                break;
            case R.id.rb_bzj_bx:
                rb_bzj_has.setChecked(false);
                rb_bzj_has.setTextColor(Color.rgb(170,170,170));
                rb_bzj_bx.setChecked(true);
                rb_bzj_bx.setTextColor(Color.WHITE);
                break;
            case R.id.rb_bzj_has:
                rb_bzj_bx.setChecked(false);
                rb_bzj_bx.setTextColor(Color.rgb(170,170,170));
                rb_bzj_has.setChecked(true);
                rb_bzj_has.setTextColor(Color.WHITE);
                break;
            case R.id.rb_bt_bx:
                rb_bt_has.setChecked(false);
                rb_bt_has.setTextColor(Color.rgb(170,170,170));
                rb_bt_bx.setChecked(true);
                rb_bt_bx.setTextColor(Color.WHITE);
                break;
            case R.id.rb_bt_has:
                rb_bt_bx.setChecked(false);
                rb_bt_bx.setTextColor(Color.rgb(170,170,170));
                rb_bt_has.setChecked(true);
                rb_bt_has.setTextColor(Color.WHITE);
                break;
            case R.id.rb_jy_bx:
                rb_jy_has.setChecked(false);
                rb_jy_has.setTextColor(Color.rgb(170,170,170));
                rb_jy_bx.setChecked(true);
                rb_jy_bx.setTextColor(Color.WHITE);
                break;
            case R.id.rb_jy_has:
                rb_jy_bx.setChecked(false);
                rb_jy_bx.setTextColor(Color.rgb(170,170,170));
                rb_jy_has.setChecked(true);
                rb_jy_has.setTextColor(Color.WHITE);
                break;
            case R.id.rb_jdl_bx:
                rb_jdl_has.setChecked(false);
                rb_jdl_has.setTextColor(Color.rgb(170,170,170));
                rb_jdl_bx.setChecked(true);
                rb_jdl_bx.setTextColor(Color.WHITE);
                break;
            case R.id.rb_jdl_has:
                rb_jdl_bx.setChecked(false);
                rb_jdl_bx.setTextColor(Color.rgb(170,170,170));
                rb_jdl_has.setChecked(true);
                rb_jdl_has.setTextColor(Color.WHITE);
                break;
            case R.id.rb_hb_bx:
                rb_hb_has.setChecked(false);
                rb_hb_has.setTextColor(Color.rgb(170,170,170));
                rb_hb_bx.setChecked(true);
                rb_hb_bx.setTextColor(Color.WHITE);
                break;
            case R.id.rb_hb_has:
                rb_hb_bx.setChecked(false);
                rb_hb_bx.setTextColor(Color.rgb(170,170,170));
                rb_hb_has.setChecked(true);
                rb_hb_has.setTextColor(Color.WHITE);
                break;
            case R.id.rb_nl_bx:
                rb_hb_bx.setTextColor(Color.rgb(170,170,170));
                et_start.setText(null);
                et_end.setText(null);
                break;
            case R.id.btn_sousuo:
                String ageStart = et_start.getText().toString().trim();
                String ageEnd = et_end.getText().toString().trim();
                String sex=null,hasZhy=null,hasCom=null,hasBao=null,hasJy=null,hasJdl=null,hasHb=null;
                if (rb_xb_nan.isChecked()){
                    sex = "01";
                }else if (rb_xb_nv.isChecked()){
                    sex = "00";
                }
                if (rb_zhy_has.isChecked()){
                    hasZhy = "1";
                }
                if (rb_qy_has.isChecked()){
                    hasCom = "1";
                }
                if (rb_bzj_has.isChecked()){
                    hasBao = "1";
                }
                if (rb_jy_has.isChecked()){
                    hasJy="1";
                }
                if (rb_jdl_has.isChecked()){
                    hasJdl="1";
                }
                if (rb_hb_has.isChecked()){
                    hasHb = "1";
                }
                Intent intent = new Intent();
                intent.putExtra("hasZhy",hasZhy);
                intent.putExtra("sex",sex);
                intent.putExtra("hasCom",hasCom);
                intent.putExtra("hasBao",hasBao);
                intent.putExtra("hasJy",hasJy);
                intent.putExtra("hasJdl",hasJdl);
                intent.putExtra("hasHb",hasHb);
                intent.putExtra("ageStart",ageStart);
                intent.putExtra("ageEnd",ageEnd);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

}
