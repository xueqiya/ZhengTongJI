package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.DynamicCommentAdapter;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-09-18.
 */

public class NewsDetailActivity extends BaseActivity{

    public static final int MSG_PROGRESS = 1;
    public static final int MSG_PROGRESS_GONE = 2;

    WebView webView;
    private Dialog mWeiboDialog;

    private ListView listView;
    private ScrollView sv_scrollview;
    DynamicCommentAdapter adapter4 = null;
    List<JSONObject> datas=new ArrayList<>();
    EditText et_comment = null;
    private String tagId="",commentType="no",isReply="no",currentContent="",dynamicSeq=null,myuserID,myNick;
    private TextView tv_commentType1,tv_commentType2,tv_commentType3,tv_commentType4;
    private Button btn_send=null;
    private TextView tv_commentClick;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_newsdetail_layout);

        webView = (WebView) findViewById(R.id.webview);
        listView = (ListView) findViewById(R.id.lv_newsComment);
        sv_scrollview = (ScrollView) findViewById(R.id.sv_scrollview);
        tv_commentClick = (TextView) findViewById(R.id.tv_commentClick);

        et_comment = (EditText)findViewById(R.id.et_comment);
        btn_send = (Button) findViewById(R.id.btn_send);
        tv_commentType1 = (TextView) findViewById(R.id.tv_commentType1);
        tv_commentType2 = (TextView) findViewById(R.id.tv_commentType2);
        tv_commentType3 = (TextView) findViewById(R.id.tv_commentType3);
        tv_commentType4 = (TextView) findViewById(R.id.tv_commentType4);

        myuserID = DemoHelper.getInstance().getCurrentUsernName();
        myNick = DemoApplication.getInstance().getCurrentUser().getName();
        dynamicSeq = this.getIntent().getStringExtra("dynamicSeq");

        //加载webview链接数据
        SetWebViewInfo();

        //查询评论数据
        getdata();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DemoHelper.getInstance().isLoggedIn(NewsDetailActivity.this)){
                    Toast.makeText(NewsDetailActivity.this,"登陆后才可以评论哦！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String comment = et_comment.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(NewsDetailActivity.this, "请输入评论", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (commentType.equals("no") && !isReply.equals("yes")){
                    Toast.makeText(NewsDetailActivity.this, "请选择一个标签", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(NewsDetailActivity.this, "请稍后...");
                submitComment(myuserID, comment, myNick, dynamicSeq,"0");
                et_comment.setText("");
            }
        });

        tv_commentType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commentType = "0";
                tv_commentType1.setBackgroundColor(Color.parseColor("#00acff"));
                tv_commentType2.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType3.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType4.setBackgroundColor(Color.parseColor("#ffbebebe"));

            }
        });

        tv_commentType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentType = "1";
                tv_commentType1.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType2.setBackgroundColor(Color.parseColor("#FF3E4A"));
                tv_commentType3.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType4.setBackgroundColor(Color.parseColor("#ffbebebe"));
            }
        });

        tv_commentType3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentType = "2";
                tv_commentType1.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType2.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType3.setBackgroundColor(Color.parseColor("#46c01b"));
                tv_commentType4.setBackgroundColor(Color.parseColor("#ffbebebe"));
            }
        });

        tv_commentType4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentType = "4";
                tv_commentType1.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType2.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType3.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType4.setBackgroundColor(Color.parseColor("#FF00FF"));
            }
        });

        tv_commentClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int a = webView.getHeight();
                sv_scrollview.scrollTo(0,a);

            }
        });

    }

    private void addPinglinCount(){

        String url = FXConstant.URL_ADD_PINGLUN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("DynadateActivity","评论增加成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("DynadateActivity","评论增加失败"+volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("createTime","0");
                param.put("dynamicSeq",dynamicSeq);

                return param;
            }
        };
        MySingleton.getInstance(NewsDetailActivity.this).addToRequestQueue(request);
    }

    /**
     * 提交评论
     */
    private void submitComment(final String sID, final String comment, String sName, final String dynamicId, final String creatTime) {
        // 更新后台
        String url = FXConstant.URL_INSERT_DYNACOMMENT;
        final String finalSName = sName;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSONObject.parseObject(s);
                String code = object.getString("code");
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                if ("success".equals(code)){

//                    if (!tagId.equals(DemoHelper.getInstance().getCurrentUsernName())){
//                        sendPushMessage(NewsDetailActivity.this.sID,"评论");
//                    }

                    addPinglinCount();
                    Toast.makeText(NewsDetailActivity.this,"评论成功！",Toast.LENGTH_SHORT).show();

                    if (datas!=null&&datas.size()>0){
                        datas.clear();
                    }

                    isReply = "no";
                    currentContent = "";
                    tagId = sID;
                    getdata();

                }else {
                    Toast.makeText(NewsDetailActivity.this,"网络繁忙，请稍后再试",Toast.LENGTH_SHORT).show();
                    Log.e("socialMainAdapter",s);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(NewsDetailActivity.this,"网络繁忙，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("userId",sID);
                param.put("dynamicId",dynamicId);
                param.put("createTime","0");

                if (isReply.equals("yes")){
                    param.put("content",comment+currentContent);
                }else {
                    param.put("content",comment);
                }

                param.put("userName", finalSName);
                param.put("tagId", tagId);
                if (!isReply.equals("yes")){
                    param.put("type", commentType);
                }else {
                    param.put("type", "3");
                }

                return param;
            }
        };
        MySingleton.getInstance(NewsDetailActivity.this).addToRequestQueue(request);
    }

    private void SetWebViewInfo (){

        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        setting.setSupportZoom(false);//不支持缩放
        setting.setBuiltInZoomControls(false);//不出现放大和缩小的按钮
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);//不设置网络缓存

        webView.setWebViewClient(new WebViewClient() {
        });//IE内核

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100){

                    WeiboDialogUtils.closeDialog(mWeiboDialog);

                    listView.setVisibility(View.VISIBLE);

                }
            }
        });//谷歌内核

        String url = getIntent().getStringExtra("content");

        if (url != null){

            webView.loadUrl(url);

        }else {

            webView.loadUrl("http://www.fulu86.com");

        }

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(NewsDetailActivity.this, "加载中...");

    }


    private void getdata() {
        String url = FXConstant.URL_QUERY_DYNACOMMENT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSONObject.parseObject(s);
                JSONArray array = object.getJSONArray("list");
                if (array==null||"".equals(array)||array.size()==0){

                    datas = new ArrayList<>();
                    //  adapter = new PingLunAdapter(DynaDetaActivity.this,datas);
                    int width = 1080;
                    if (NewsDetailActivity.this != null){

                        WindowManager wm = NewsDetailActivity.this.getWindowManager();
                        DisplayMetrics dm = new DisplayMetrics();
                        wm.getDefaultDisplay().getMetrics(dm);
                        width = dm.widthPixels;

                    }
                    adapter4 = new DynamicCommentAdapter(NewsDetailActivity.this,datas,"06","0","",width);
                    setHeight();
                    listView.setAdapter(adapter4);
                }else {
                    if (datas==null){
                        datas = new ArrayList<>();
                    }
                    for (int i=0;i<array.size();i++){
                        JSONObject data = array.getJSONObject(i);
                        datas.add(data);
                    }
                    // adapter = new PingLunAdapter(DynaDetaActivity.this,datas);
                    int width = 1080;
                    if (NewsDetailActivity.this != null){

                        WindowManager wm = NewsDetailActivity.this.getWindowManager();
                        DisplayMetrics dm = new DisplayMetrics();
                        wm.getDefaultDisplay().getMetrics(dm);
                        width = dm.widthPixels;

                    }
                    adapter4 = new DynamicCommentAdapter(NewsDetailActivity.this,datas,"06","0","",width);
                    setHeight();
                    listView.setAdapter(adapter4);

                    adapter4.setOnItemClickListener(new DynamicCommentAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            JSONObject jsonObject = datas.get(position);
                            String clickId = datas.get(position).getString("userId");
                            String objId = datas.get(position).getString("timeStamp");


                            showDeleteDialog(clickId,objId,position);


//                            if (myuserID.equals(clickId)){
//                                showDeleteDialog(clickId,objId,position);
//                            }else {
//                                et_comment.setFocusable(true);
//                                et_comment.setFocusableInTouchMode(true);
//                                et_comment.requestFocus();
//                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                                isReply = "yes";
//                                tagId = datas.get(position).getString("userId");
//                                String userName = datas.get(position).getString("userName");
//                                String content = datas.get(position).getString("content");
//                                String[] contentArr = content.split("\\|");
//                                if (contentArr.length>1){
//                                    //回复的别人回复的
//
//                                    currentContent = "//|@"+userName+":|"+contentArr[0].substring(0,contentArr[0].length()-2);
//                                }else {
//                                    //回复的
//                                    currentContent = "//|@"+userName+":|"+content;
//                                }
//
//                            }

                        }
                    });

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("dynamicId",dynamicSeq);
                param.put("createTime","0");
                param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(NewsDetailActivity.this).addToRequestQueue(request);
    }


    public void setHeight(){

        int height = 240;
        int count = adapter4.getCount();
        for(int i=0;i<count;i++){
            View temp = adapter4.getView(i,null,listView);
            temp.measure(0,0);
            height += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = this.listView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        params.height = height;
        listView.setLayoutParams(params);

    }

    private void showDeleteDialog(final String clickId, final String objId, final int position) {
        LayoutInflater inflaterDl = LayoutInflater.from(NewsDetailActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(NewsDetailActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        tv_item1.setText("回复");
        tv_item2.setText("删除");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                et_comment.setFocusable(true);
                et_comment.setFocusableInTouchMode(true);
                et_comment.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                isReply = "yes";
                tagId = datas.get(position).getString("userId");
                String userName = datas.get(position).getString("userName");
                String content = datas.get(position).getString("content");
                String[] contentArr = content.split("\\|");
                if (contentArr.length>1){
                    //回复的别人回复的
                    currentContent = "//|@"+userName+":|"+contentArr[0].substring(0,contentArr[0].length()-2);
                }else {
                    //回复的
                    currentContent = "//|@"+userName+":|"+content;
                }

            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                new AlertDialog.Builder(NewsDetailActivity.this)
                        .setTitle("确认")
                        .setMessage("是否确定删除评论？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String url = FXConstant.URL_DELETE_DYNACOMMENT;
                                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        Log.e("dynamicac,deleone",s);

                                        JSONObject object = JSONObject.parseObject(s);
                                        String code = object.getString("code");
                                        if ("success".equals(code)){
                                            reducePinglinCount();
                                            Toast.makeText(NewsDetailActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                            datas.remove(position);
                                            adapter4.notifyDataSetChanged();
                                        }else {
                                            Toast.makeText(NewsDetailActivity.this,"网络繁忙，请稍后再试",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {

                                        Toast.makeText(NewsDetailActivity.this,"网络不稳定,稍后重试",Toast.LENGTH_SHORT).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> param = new HashMap<>();
                                        param.put("dynamicId",dynamicSeq);
                                        param.put("createTime","0");
                                        param.put("userId",clickId);
                                        param.put("timeStamp",objId);
                                        return param;
                                    }
                                };
                                MySingleton.getInstance(NewsDetailActivity.this).addToRequestQueue(request);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void reducePinglinCount(){
        String url = FXConstant.URL_REDUCE_PINGLUN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("DynadateActivity","评论减少成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("DynadateActivity","评论减少失败"+volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("createTime","0");
                param.put("dynamicSeq",dynamicSeq);

                return param;
            }
        };
        MySingleton.getInstance(NewsDetailActivity.this).addToRequestQueue(request);
    }

}
