package com.sangu.apptongji.main.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.widget.CircleImageView;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class AddFriendsNextActivity extends BaseActivity implements IUAZView{
    EditText et_search=null;
    IUAZPresenter presenter=null;
    Button btn_add=null;
    TextView tv_name=null;
    TextView tv_title=null;
    RelativeLayout rl_detail=null;
    RelativeLayout re_search=null;
    CircleImageView iv_avatar=null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx_activity_addfriends_next);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        presenter = new UAZPresenter(this,this);
        re_search = (RelativeLayout) this
                .findViewById(R.id.re_search);
        final TextView tv_search = (TextView) re_search
                .findViewById(R.id.tv_search);
        et_search = (EditText) this.findViewById(R.id.et_search);
        btn_add = (Button) findViewById(R.id.btn_add);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_avatar = (CircleImageView) findViewById(R.id.iv_avatar);
        rl_detail = (RelativeLayout) findViewById(R.id.rl_detail);
        et_search.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    re_search.setVisibility(View.VISIBLE);
                    rl_detail.setVisibility(View.GONE);
                    tv_search.setText(et_search.getText().toString().trim());
                } else {
                    re_search.setVisibility(View.GONE);
                    tv_search.setText("");
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {

            }
        });
        re_search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final String uid = et_search.getText().toString().trim();
//                if ("22222222222".equals(uid)||"18337101357".equals(uid)){
//////                    Toast.makeText(getApplicationContext(),"该用户不存在",Toast.LENGTH_SHORT).show();
//////                    return;
//////                }
                presenter.loadThisDetail(uid);
            }
        });
    }

    private void addFriend() {
        final String uid = et_search.getText().toString().trim();
        if (uid == null || uid.equals("")) {
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(
                AddFriendsNextActivity.this);
        dialog.setMessage("正在发送请求...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        String url = FXConstant.URL_ADD_FRIEND;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    org.json.JSONObject jsonObject=new org.json.JSONObject(s);
                    String code = jsonObject.getString("code");
                    if(code.equals("SUCCESS")){
                        sendPushMessage(uid);
                        updateBmob(uid);
                        dialog.dismiss();
                        Toast.makeText(AddFriendsNextActivity.this, "发送请求成功！", Toast.LENGTH_LONG).show();
                    }else if (code.equals("1012")) {
                        dialog.dismiss();
                        Toast.makeText(AddFriendsNextActivity.this, "该帐号不存在！", Toast.LENGTH_SHORT).show();
                    }else if (code.equals("1013")){
                        dialog.dismiss();
                        Toast.makeText(AddFriendsNextActivity.this, "该帐号已经是您的好友！", Toast.LENGTH_SHORT).show();
                    }else if (code.equals("1014")){
                        dialog.dismiss();
                        Toast.makeText(AddFriendsNextActivity.this,"请勿重复发送请求！",Toast.LENGTH_SHORT).show();
                    }else {
                        dialog.dismiss();
                        Toast.makeText(AddFriendsNextActivity.this,"该账号不存在！",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                Log.e("volleyError",volleyError.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uLoginId",DemoHelper.getInstance().getCurrentUsernName());
                param.put("f_word",uid);
                return param;
            }
        };
        MySingleton.getInstance(AddFriendsNextActivity.this).addToRequestQueue(request);
    }

    private void sendPushMessage(final String hxid1) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String name = DemoApplication.getInstance().getCurrentUser().getName();
        if (name==null||"".equals(name)){
            name = DemoHelper.getInstance().getCurrentUsernName();
        }
        final String finalName = name;
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
                param.put("u_id",hxid1);
                param.put("body","好友消息");
                param.put("type","02");
                param.put("userId",myId);
                param.put("companyId", "0");
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(AddFriendsNextActivity.this).addToRequestQueue(request);
    }

    private void updateBmob(final String hxid1) {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("addFriendCount","1");
                param.put("userId",hxid1);
                return param;
            }
        };
        MySingleton.getInstance(AddFriendsNextActivity.this).addToRequestQueue(request);
    }

    @Override
    public void updateThisUser(Userful user) {
        rl_detail.setVisibility(View.VISIBLE);
        re_search.setVisibility(View.GONE);
        String image = TextUtils.isEmpty(user.getImage())?"":user.getImage();
        final String name = TextUtils.isEmpty(user.getName())?user.getLoginId():user.getName();
        String sex = TextUtils.isEmpty(user.getSex())?"01":user.getSex();
        if (!"".equals(image)){
            image = image.split("\\|")[0];
            iv_avatar.setVisibility(View.VISIBLE);
            tv_title.setVisibility(View.INVISIBLE);
        }else {
            iv_avatar.setVisibility(View.INVISIBLE);
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(name);
            tv_title.setTextColor(Color.WHITE);
            if ("01".equals(sex)){
                tv_title.setBackgroundResource(R.drawable.fx_bg_text_gra);
            }else {
                tv_title.setBackgroundResource(R.drawable.fx_bg_text_red);
            }
        }
        final String uid = et_search.getText().toString().trim();
        ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+image,iv_avatar,DemoApplication.mOptions);
        tv_name.setText(name);
        btn_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid.equals(DemoHelper.getInstance().getCurrentUsernName())){
                    Toast.makeText(AddFriendsNextActivity.this, "不能添加自己为好友！", Toast.LENGTH_SHORT).show();
                    return;
                }
                addFriend();
            }
        });
        iv_avatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddFriendsNextActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,uid));
            }
        });
        tv_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddFriendsNextActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,uid));
            }
        });
    }

    @Override
    public void showproLoading() {
    }
    @Override
    public void hideproLoading() {
    }
    @Override
    public void showproError() {
        Toast.makeText(AddFriendsNextActivity.this,"该账号不存在！",Toast.LENGTH_SHORT).show();
    }
}