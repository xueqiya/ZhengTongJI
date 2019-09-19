package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.JiLuListAdapter;
import com.sangu.apptongji.main.adapter.QdBaojiaAdapter;
import com.sangu.apptongji.main.alluser.order.avtivity.NewsOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFiveActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFourActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailTwoActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-10-16.
 */

public class DynamicRecommendActivity extends BaseActivity{

    private int currentPage=1;
    private XRecyclerView mRecyclerView=null;
    private CustomProgressDialog mProgress=null;
    private TextView tv_none1;
    private String dynamicSeq="",createTime="";
    private String data;
    JiLuListAdapter adapter3=null;
    QdBaojiaAdapter adapter2=null;
    List<JSONObject> datas=new ArrayList<>();
    private ListView lv_listview;
    private Dialog mWeiboDialog;
    private String isAdvert="0",advertImageUrl="";
    private String myuserID;
    private String orderBodyTask;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_dynamicrecommend);

        GetNoticeInfo();

        dynamicSeq = getIntent().getStringExtra("dynamicSeq");
        createTime = getIntent().getStringExtra("createTime");

        tv_none1 = (TextView) findViewById(R.id.tv_none1);
        lv_listview = (ListView) findViewById(R.id.lv_listview);
        myuserID = DemoHelper.getInstance().getCurrentUsernName();

        SelectRecommedList();

    }


    private void SelectRecommedList(){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(DynamicRecommendActivity.this, "加载中...");

       String url = FXConstant.URL_PUBLISHDETAIL_QUERY;
       StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
           @Override
           public void onResponse(String s) {

               WeiboDialogUtils.closeDialog(mWeiboDialog);
               JSONObject object = JSON.parseObject(s);
               JSONArray array = object.getJSONArray("clist");
               if (array==null||array.size()==0){
                   ToastUtils.showNOrmalToast(getApplicationContext(),"网络连接错误...");
                   return;
               }
               JSONObject json = array.getJSONObject(0);
               data = json.toString();

               //解析获取推荐数据
               JSONObject object2 = JSONObject.parseObject(data);

               String task_label = object2.getString("task_label");
               orderBodyTask = task_label;
//               JSONArray array2 = object2.getJSONArray("memodynamic");
//
//               if (array2==null||"".equals(array2)||array2.size()==0){
//
//                   if (datas!=null&&datas.size()==0) {
//                       datas = new ArrayList<>();
//                       int width = 1080;
//                       if (DynamicRecommendActivity.this != null){
//
//                           WindowManager wm = DynamicRecommendActivity.this.getWindowManager();
//                           DisplayMetrics dm = new DisplayMetrics();
//                           wm.getDefaultDisplay().getMetrics(dm);
//                           width = dm.widthPixels;
//
//                       }
//                       adapter3 = new JiLuListAdapter(DynamicRecommendActivity.this, datas,"1",isAdvert,advertImageUrl,width);
//                       lv_listview.setAdapter(adapter3);
//                   }
//
//               }else {
//
//                   if (datas==null){
//                       datas = new ArrayList<>();
//                   }
//                   for (int i=0;i<array2.size();i++){
//                       JSONObject data = array2.getJSONObject(i);
//                       datas.add(data);
//                   }
//
//                   int width = 1080;
//                   if (DynamicRecommendActivity.this != null){
//
//                       WindowManager wm = DynamicRecommendActivity.this.getWindowManager();
//                       DisplayMetrics dm = new DisplayMetrics();
//                       wm.getDefaultDisplay().getMetrics(dm);
//                       width = dm.widthPixels;
//
//                   }
//                   adapter3 = new JiLuListAdapter(DynamicRecommendActivity.this,datas,"1",isAdvert,advertImageUrl,width);
//
//                   lv_listview.setAdapter(adapter3);
//
//               }


               JSONArray array3 = object2.getJSONArray("dynamicOrders");

               String firstId = object2.getString("firstUId");
               String uLoginId = object2.getString("uLoginId");
               String currentId;
               if (firstId==null||firstId.equalsIgnoreCase("null")){
                   currentId = uLoginId;
               }else {
                   currentId = firstId;
               }
               if (currentId.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                   for (int i = 0; i < array3.size(); i++) {
                       JSONObject data = array3.getJSONObject(i);
                       datas.add(data);
                   }
               }else {
                   for (int i = 0; i < array3.size(); i++) {
                       JSONObject data = array3.getJSONObject(i);
                       String u_id = data.getString("u_id");
                       if (u_id.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                           datas.add(data);
                           break;
                       }
                   }
               }
               adapter2 = new QdBaojiaAdapter(DynamicRecommendActivity.this,datas);

               lv_listview.setAdapter(adapter2);

               lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                       final String orderId = datas.get(position).getString("order_id");
                       final String dynamic_seq = datas.get(position).getString("dynamic_seq");
                       final String timestamp = datas.get(position).getString("timestamp");
                       final String type = datas.get(position).getString("type");
                       final String u_id = datas.get(position).getString("u_id");
                       final String quote = datas.get(position).getString("quote");
                       final String state = datas.get(position).getString("state");
                       final String d_id = datas.get(position).getString("d_id");

                       if (state.equals("1")){

                           LayoutInflater inflaterDl = LayoutInflater.from(DynamicRecommendActivity.this);
                           RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                           final Dialog dialog = new AlertDialog.Builder(DynamicRecommendActivity.this,R.style.Dialog).create();
                           dialog.show();
                           dialog.getWindow().setContentView(layout);
                           dialog.setCanceledOnTouchOutside(false);
                           dialog.setCancelable(false);
                           TextView tv_title = (TextView) dialog.findViewById(R.id.title_tv);
                           Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                           tv_title.setText("该订单已进行过验资，请在入账单/出账单中查看订单");
                           btn_ok.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   dialog.dismiss();
                               }
                           });
                           return;
                       }
                       if (!datas.get(position).getString("u_id").equals(myuserID)){


                           if ("06".equals(type)) {

                               // 06 代表新的订单模式
                               Intent intent = new Intent(DynamicRecommendActivity.this, NewsOrderDetailActivity.class);
                               intent.putExtra("orderId",orderId);
                               intent.putExtra("dynamicSeq",dynamic_seq);
                               intent.putExtra("timestamp",timestamp);
                               intent.putExtra("task_label",orderBodyTask);
                               intent.putExtra("u_id",u_id);
                               intent.putExtra("quote",quote);
                               intent.putExtra("biaoshi","1");
                               intent.putExtra("conId",d_id);
                               intent.putExtra("merId",u_id);
                               intent.putExtra("createTime",createTime);
                               intent.putExtra("orderState","02");

                               startActivity(intent);

                           }else {

                               String url = FXConstant.URL_Order_Detail;
                               StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                   @Override
                                   public void onResponse(String s) {
                                       if ("02".equals(type)) {
                                           Intent intent = new Intent(DynamicRecommendActivity.this, UOrderDetailTwoActivity.class);
                                           intent.putExtra("orderId",orderId);
                                           intent.putExtra("dynamic_seq",dynamic_seq);
                                           intent.putExtra("timestamp",timestamp);
                                           intent.putExtra("task_label",orderBodyTask);
                                           intent.putExtra("u_id",u_id);
                                           intent.putExtra("quote",quote);
                                           intent.putExtra("shuju",s);
                                           intent.putExtra("biaoshi","04");
                                           startActivity(intent);
                                       }else if ("04".equals(type)){
                                           Intent intent = new Intent(DynamicRecommendActivity.this, UOrderDetailFourActivity.class);
                                           intent.putExtra("orderId",orderId);
                                           intent.putExtra("dynamic_seq",dynamic_seq);
                                           intent.putExtra("timestamp",timestamp);
                                           intent.putExtra("task_label",orderBodyTask);
                                           intent.putExtra("u_id",u_id);
                                           intent.putExtra("quote",quote);
                                           intent.putExtra("shuju",s);
                                           intent.putExtra("biaoshi","04");
                                           startActivity(intent);
                                       }else if ("05".equals(type)){
                                           Intent intent = new Intent(DynamicRecommendActivity.this, UOrderDetailFiveActivity.class);
                                           intent.putExtra("orderId",orderId);
                                           intent.putExtra("dynamic_seq",dynamic_seq);
                                           intent.putExtra("timestamp",timestamp);
                                           intent.putExtra("task_label",orderBodyTask);
                                           intent.putExtra("u_id",u_id);
                                           intent.putExtra("quote",quote);
                                           intent.putExtra("shuju",s);
                                           intent.putExtra("biaoshi","04");
                                           startActivity(intent);
                                       }else {
                                           Intent intent = new Intent(DynamicRecommendActivity.this, UOrderDetailActivity.class);
                                           intent.putExtra("orderId",orderId);
                                           intent.putExtra("dynamic_seq",dynamic_seq);
                                           intent.putExtra("timestamp",timestamp);
                                           intent.putExtra("task_label",orderBodyTask);
                                           intent.putExtra("u_id",u_id);
                                           intent.putExtra("quote",quote);
                                           intent.putExtra("shuju",s);
                                           intent.putExtra("biaoshi","04");
                                           startActivity(intent);
                                       }

                                   }

                               }, new Response.ErrorListener() {
                                   @Override
                                   public void onErrorResponse(VolleyError volleyError) {
                                       Toast.makeText(getApplicationContext(),"网络连接错误",Toast.LENGTH_SHORT).show();
                                   }
                               }){
                                   @Override
                                   protected Map<String, String> getParams() throws AuthFailureError {
                                       Map<String,String> params = new HashMap<>();
                                       params.put("orderId",orderId);
                                       return params;
                                   }
                               };
                               MySingleton.getInstance(DynamicRecommendActivity.this).addToRequestQueue(request);

                           }

                       }else {
                           LayoutInflater inflaterDl = LayoutInflater.from(DynamicRecommendActivity.this);
                           RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                           final Dialog dialog = new AlertDialog.Builder(DynamicRecommendActivity.this,R.style.Dialog).create();
                           dialog.show();
                           dialog.getWindow().setContentView(layout);
                           dialog.setCanceledOnTouchOutside(true);
                           RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                           RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                           RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                           TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                           TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                           re_item2.setVisibility(View.GONE);
                           tv_item1.setText("删除报价");
                           tv_item2.setText("修改报价");
                           re_item1.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   dialog.dismiss();
                                   deleteBaoJia(dynamic_seq,timestamp,orderId,position);
                               }
                           });
                           re_item2.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   dialog.dismiss();

                                   if ("06".equals(type)) {

                                       // 06 代表新的订单模式
                                       Intent intent = new Intent(DynamicRecommendActivity.this, NewsOrderDetailActivity.class);
                                       intent.putExtra("orderId",orderId);
                                       intent.putExtra("dynamicSeq",dynamic_seq);
                                       intent.putExtra("timestamp",timestamp);
                                       intent.putExtra("task_label",orderBodyTask);
                                       intent.putExtra("u_id",u_id);
                                       intent.putExtra("quote",quote);
                                       intent.putExtra("biaoshi","1");
                                       intent.putExtra("conId",d_id);
                                       intent.putExtra("merId",u_id);
                                       intent.putExtra("createTime",createTime);

                                       startActivity(intent);

                                   }else {

                                       String url = FXConstant.URL_Order_Detail;
                                       StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                           @Override
                                           public void onResponse(String s) {
                                               if ("02".equals(type)) {
                                                   Intent intent = new Intent(DynamicRecommendActivity.this, UOrderDetailTwoActivity.class);
                                                   intent.putExtra("orderId",orderId);
                                                   intent.putExtra("dynamic_seq",dynamic_seq);
                                                   intent.putExtra("timestamp",timestamp);
                                                   intent.putExtra("u_id",u_id);
                                                   intent.putExtra("quote",quote);
                                                   intent.putExtra("shuju",s);
                                                   intent.putExtra("biaoshi","07");
                                                   startActivity(intent);
                                               }else if ("04".equals(type)){
                                                   Intent intent = new Intent(DynamicRecommendActivity.this, UOrderDetailFourActivity.class);
                                                   intent.putExtra("orderId",orderId);
                                                   intent.putExtra("dynamic_seq",dynamic_seq);
                                                   intent.putExtra("timestamp",timestamp);
                                                   intent.putExtra("u_id",u_id);
                                                   intent.putExtra("quote",quote);
                                                   intent.putExtra("shuju",s);
                                                   intent.putExtra("biaoshi","07");
                                                   startActivity(intent);
                                               }else if ("05".equals(type)){
                                                   Intent intent = new Intent(DynamicRecommendActivity.this, UOrderDetailFiveActivity.class);
                                                   intent.putExtra("orderId",orderId);
                                                   intent.putExtra("dynamic_seq",dynamic_seq);
                                                   intent.putExtra("timestamp",timestamp);
                                                   intent.putExtra("u_id",u_id);
                                                   intent.putExtra("quote",quote);
                                                   intent.putExtra("shuju",s);
                                                   intent.putExtra("biaoshi","07");
                                                   startActivity(intent);
                                               }else {
                                                   Intent intent = new Intent(DynamicRecommendActivity.this, UOrderDetailActivity.class);
                                                   intent.putExtra("orderId",orderId);
                                                   intent.putExtra("dynamic_seq",dynamic_seq);
                                                   intent.putExtra("timestamp",timestamp);
                                                   intent.putExtra("u_id",u_id);
                                                   intent.putExtra("quote",quote);
                                                   intent.putExtra("shuju",s);
                                                   intent.putExtra("biaoshi","07");
                                                   startActivity(intent);
                                               }
                                           }
                                       }, new Response.ErrorListener() {
                                           @Override
                                           public void onErrorResponse(VolleyError volleyError) {
                                               Toast.makeText(getApplicationContext(),"网络连接错误",Toast.LENGTH_SHORT).show();
                                           }
                                       }){
                                           @Override
                                           protected Map<String, String> getParams() throws AuthFailureError {
                                               Map<String,String> params = new HashMap<>();
                                               params.put("orderId",orderId);
                                               return params;
                                           }
                                       };
                                       MySingleton.getInstance(DynamicRecommendActivity.this).addToRequestQueue(request);

                                   }

                               }
                           });
                           re_item3.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   dialog.dismiss();
                               }
                           });
                       }
                   }
               });

           }

       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError volleyError) {

               WeiboDialogUtils.closeDialog(mWeiboDialog);
               Toast.makeText(getApplicationContext(), "网络不稳定,请稍后重试", Toast.LENGTH_SHORT).show();

           }

       }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> param = new HashMap<>();
               param.put("dynamicSeq",dynamicSeq);
               param.put("createTime",createTime);

               if (DemoHelper.getInstance().isLoggedIn(DynamicRecommendActivity.this)) {
                   param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
               }
               return param;
           }
       };

        MySingleton.getInstance(DynamicRecommendActivity.this).addToRequestQueue(request);

    }

    private void reducePinglinCount(){
        String url = FXConstant.URL_REDUCE_PINGLUN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

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
                param.put("createTime",createTime);
                param.put("dynamicSeq",dynamicSeq);
                param.put("type","1");
                return param;
            }
        };
        MySingleton.getInstance(DynamicRecommendActivity.this).addToRequestQueue(request);
    }

    private void deleteBaoJia(final String dynamic_seq, final String timestamp, final String orderId, final int position) {
        String url = FXConstant.URL_DELETE_BAOJIA;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("dynamicac,dels",s);
                reducePinglinCount();
                Toast.makeText(DynamicRecommendActivity.this,"删除成功!",Toast.LENGTH_SHORT).show();
                datas.remove(position);
                adapter2.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null){
                    Log.e("dynamicac,dele",volleyError.toString());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("dynamic_seq",dynamic_seq);
                param.put("timestamp",timestamp);
                param.put("order_id",orderId);
                param.put("u_id",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(DynamicRecommendActivity.this).addToRequestQueue(request);
    }


    private void GetNoticeInfo(){

        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");

                String type = object1.getString("type");

                if (type.equals("1")){

                    isAdvert = "1";
                    String image1 = object1.getString("image2");
                    String image2 = object1.getString("image3");
                    String image3 = object1.getString("image4");

                    advertImageUrl = image1+"|"+image2+"|"+image3;

                }else {
                    isAdvert = "0";
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
                param.put("deviceType","android12");
                return param;
            }
        };
        MySingleton.getInstance(DynamicRecommendActivity.this).addToRequestQueue(request);

    }

}
