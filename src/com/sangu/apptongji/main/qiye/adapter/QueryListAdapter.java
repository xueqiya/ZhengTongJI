package com.sangu.apptongji.main.qiye.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.qiye.QianDingHetongActivity;
import com.sangu.apptongji.main.qiye.entity.QueryInfo;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.utils.UserPermissionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-30.
 */

public class QueryListAdapter extends BaseAdapter {
    private List<QueryInfo> date=null;
    private Context context;
    private String comAddress=null,companyName=null,recruitImages=null,joinImages=null,recruitBody,joinBody,joinMoney=null,recruitMoney=null;

    public QueryListAdapter(List<QueryInfo> date, Context context,String companyName,String comAddress
            ,String recruitImages,String recruitBody,String recruitMoney,String joinImages,String joinBody,String joinMoney) {
        this.date = date;
        this.context = context;
        this.companyName = companyName;
        this.comAddress = comAddress;
        this.recruitImages = recruitImages;
        this.recruitBody = recruitBody;
        this.recruitMoney = recruitMoney;
        this.joinImages = joinImages;
        this.joinBody = joinBody;
        this.joinMoney = joinMoney;
        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
    }

    @Override
    public int getCount() {
        return date.size();
    }

    @Override
    public Object getItem(int position) {
        return date.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final QueryInfo f = date.get(position);
        Viewholder holder;
        if (convertView==null){
            convertView = View.inflate(context, R.layout.fx_item_newfriend_msg,null);
            holder = new Viewholder();
            holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            holder.tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            holder.tv_reason = (TextView) convertView.findViewById(R.id.tv_reason);
            holder.tv_added = (TextView) convertView.findViewById(R.id.tv_added);
            holder.btn_add = (Button) convertView.findViewById(R.id.btn_add);
            holder.btn_jujue = (Button) convertView.findViewById(R.id.btn_cancle);
            convertView.setTag(holder);
        }
        holder = (Viewholder) convertView.getTag();
//        final InviteMessage msg = (InviteMessage) getItem(total - 1 - position);
        String reason = "请求加入企业";
        final String nick = TextUtils.isEmpty(f.getName())?f.getuLoginId():f.getName();
        final String uId = f.getuLoginId();
        String resv5 = f.getResv5();
        String sex = f.getSex();
        String time = f.getTime();
        if (time!=null&&time.length()>11){
            time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " "
                    + time.substring(8, 10) + ":" + time.substring(10, 12);
        }
        if (resv5==null||"".equals(resv5)||"|".equals(resv5)){
            resv5 = "0";
        }
        String avatar = TextUtils.isEmpty(f.getAvatar())?"":f.getAvatar();
        final Viewholder finalHolder = holder;
        if (avatar!=null&&!avatar.equals("")) {
            avatar = avatar.split("\\|")[0];
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar,holder.iv_avatar);
        }else {
            holder.tvTitleA.setVisibility(View.VISIBLE);
            holder.iv_avatar.setVisibility(View.INVISIBLE);
            holder.tvTitleA.setText(nick);
            if ("00".equals(sex)){
                holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
            }else {
                holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
            }
        }
        holder.tv_name.setText(nick);
        holder.tv_time.setText(time);
        holder.tv_reason.setText(reason);
        holder.tv_added.setVisibility(View.GONE);
        holder.btn_add.setVisibility(View.VISIBLE);
        holder.btn_jujue.setVisibility(View.VISIBLE);
        String shareRed = f.getShareRed();
        final String friendsNumber = f.getFriendsNumber();
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            holder.tv_name.setTextColor(Color.RED);
        }else {
            holder.tv_name.setTextColor(Color.BLACK);
        }
        final String finalAvatar = avatar;
        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,uId));
            }
        });
        holder.tvTitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,uId));
            }
        });
        final String finalResv = resv5;
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userResv5 = f.getUserResv5();
                final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
                if (userResv5==null||"".equals(userResv5)||"null".equals(userResv5)) {
                    queryHetList(qiyeId, "01", finalHolder.btn_add, finalHolder.btn_jujue, uId, finalHolder.tv_added, finalAvatar, nick, finalResv);
                }else {
                    new AlertDialog.Builder(context)
                            .setMessage("\n该用户已被加入别的企业\n")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    queryHetList(qiyeId, "03", finalHolder.btn_jujue, finalHolder.btn_add, uId, finalHolder.tv_added, finalAvatar, nick, finalResv);
                                }
                            }).setCancelable(false)
                            .show();
                }
            }

        });
        final String finalResv1 = resv5;
        holder.btn_jujue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userResv5 = f.getUserResv5();
                final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
                if (userResv5==null||"".equals(userResv5)||"null".equals(userResv5)) {
                    queryHetList(qiyeId, "02", finalHolder.btn_jujue, finalHolder.btn_add, uId, finalHolder.tv_added, finalAvatar, nick, finalResv1);
                }else {
                    new AlertDialog.Builder(context)
                            .setMessage("\n该用户已被加入别的企业\n")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    queryHetList(qiyeId, "03", finalHolder.btn_jujue, finalHolder.btn_add, uId, finalHolder.tv_added, finalAvatar, nick, finalResv1);
                                }
                            }).setCancelable(false)
                            .show();
                }
            }
        });

        return convertView;
    }

    private void queryHetList(final String qiyeId, final String biaoshi, final Button button, final Button button2, final String uId, final TextView textview, final String avatar, final String name, final String resv5){
        String url = FXConstant.URL_QIYE_HETONGLIST;
        final boolean[] hasHetong = {false};
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String agreement1 = "",agreement2= "",createTime= "",userSignature= "",qiyeId="",resv1 = null;
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.optJSONArray("agreementInfos");
                    if (array!=null||!"".equals(array)) {
                        for (int i = 0; i < array.length();i++) {
                            JSONObject object1 = array.getJSONObject(i);
                            String id = object1.getString("userId");
                            if (uId.equals(id)){
                                qiyeId = object1.isNull("companyId")?"":object1.getString("companyId");
                                agreement1 = object1.isNull("agreement1")?"":object1.getString("agreement1");
                                agreement2 = object1.isNull("agreement2")?"":object1.getString("agreement2");
                                createTime = object1.isNull("createTime")?"":object1.getString("createTime");
                                userSignature = object1.isNull("userSignature")?"":object1.getString("userSignature");
                                resv1 = object1.isNull("resv1")?"":object1.getString("resv1");
                                hasHetong[0] = true;
                            }
                        }
                    }
                    if ("01".equals(biaoshi)) {
                        if (hasHetong[0]) {
                            String image,body,feiyong,biaoshi,fufei;
                            if (resv1!=null&&"0".equals(resv1)){
                                image = recruitImages;
                                body = recruitBody;
                                feiyong = recruitMoney;
                                biaoshi = "yingpin";
                                if (recruitMoney!=null&&!"".equals(recruitMoney)&&Double.parseDouble(recruitMoney)>0){
                                    fufei = "付费";
                                }else {
                                    fufei = "无付费";
                                }
                            }else {
                                image = joinImages;
                                body = joinBody;
                                feiyong = joinMoney;
                                biaoshi = "jiameng";
                                if (joinMoney!=null&&!"".equals(joinMoney)&&Double.parseDouble(joinMoney)>0){
                                    fufei = "付费";
                                }else {
                                    fufei = "无付费";
                                }
                            }
                            context.startActivity(new Intent(context, QianDingHetongActivity.class).putExtra("userId", uId).putExtra("companyName", companyName).putExtra("qiyeId", qiyeId)
                                    .putExtra("comAddress", comAddress).putExtra("createTime", createTime).putExtra("userSignature", userSignature).putExtra("fufei",fufei)
                                    .putExtra("biaoshi2", "gongsi").putExtra("image",image).putExtra("body",body).putExtra("feiyong",resv5).putExtra("biaoshi",biaoshi));
                        } else {
                            acceptInvitation(button, button2, uId, textview, avatar, name, resv5);
                        }
                    }else {
                        deleteHetong(qiyeId,button, button2, uId, textview, avatar, name, resv5,biaoshi);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context,"网络连接错误，请重试",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("companyId",qiyeId);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void deleteHetong(final String qiyeId, final Button button, final Button button2, final String uId, final TextView textview, final String avatar, final String name, final String resv5, final String biaoshi) {
        String url = FXConstant.URL_QIYE_SHANCHUHETONG;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                acceptInvitation2(button,button2,uId,textview, avatar,name,resv5,biaoshi);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                acceptInvitation2(button,button2,uId,textview, avatar,name,resv5,biaoshi);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("userId", uId);
                param.put("companyId",qiyeId);
                param.put("margin",resv5);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * 同意加入请求
     *
     * @param
     * @param button
     * @param
     */
    private void acceptInvitation(final Button button,final Button button2, final String uId,final TextView textview, final String avatar, final String name,final String resv5){
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("正在同意...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        String url = FXConstant.URL_QUERY_CONFIRM;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("SUCCESS")){
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    EMClient.getInstance().groupManager().asyncAddUsersToGroup(qiyeId, new String[]{uId}, new EMCallBack() {
                                        @Override
                                        public void onSuccess() {
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (pd != null && pd.isShowing()) {
                                                        pd.dismiss();
                                                    }
                                                    updateHbTimes(uId);
                                                    try {
                                                        String comName = URLDecoder.decode(companyName,"UTF-8");
                                                        reduceQjcount();
                                                        duanxintongzhi(uId,"【正事多】 通知:您已被同意加入"+comName+"企业");
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                    textview.setVisibility(View.VISIBLE);
                                                    button.setEnabled(false);
                                                    button2.setEnabled(false);
                                                    button.setVisibility(View.GONE);
                                                    button2.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                        @Override
                                        public void onError(int i, String s) {
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (pd != null && pd.isShowing()) {
                                                        pd.dismiss();
                                                    }
                                                    try {
                                                        String comName = URLDecoder.decode(companyName,"UTF-8");
                                                        updateHbTimes(uId);
                                                        reduceQjcount();
                                                        duanxintongzhi(uId,"【正事多】 通知:您已被同意加入"+comName+"企业");
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                    textview.setVisibility(View.VISIBLE);
                                                    button.setEnabled(false);
                                                    button2.setEnabled(false);
                                                    button.setVisibility(View.GONE);
                                                    button2.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                        @Override
                                        public void onProgress(int i, String s) {
                                        }
                                    });
                                } catch (final Exception e) {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pd.dismiss();
                                            Toast.makeText(context,  ""+ e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        Log.d("网络错误", volleyError.toString());
                    }
                });
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("userId", uId);
                param.put("companyId",qiyeId);
                param.put("companyName",companyName);
                param.put("comAddress",comAddress);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
    private void updateHbTimes(final String uId) {
        String url = FXConstant.URL_XIUGAI_HBTIMES;
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
                Map<String, String> param = new HashMap<String, String>();
                param.put("uLoginId", uId);
                param.put("shareTimes", "1");
                param.put("homePageTimes", "1");
                param.put("dynamicTimes", "1");
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void reduceQjcount() {
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
                param.put("joinCompanyCount","-1");
                param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void duanxintongzhi(final String id, final String message) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(context, "同意成功！", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message",message);
                param.put("telNum",id);
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(context, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(context).addToRequestQueue(request);
            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(context.getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }
    private void acceptInvitation2(final Button button, final Button button2,final String uId,final TextView textview, final String avatar, final String name,final String resv5,final String biaoshi) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("正在处理...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        String url = FXConstant.URL_QUERY_CONFIRM;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    String code = object.getString("code");
                    if (pd!=null&&pd.isShowing()){
                        pd.dismiss();
                    }
                    Log.e("querylista,co",code);
                    if (code.equals("SUCCESS")){
                        reduceQjcount();
                        Toast.makeText(context,"删除成功！",Toast.LENGTH_SHORT).show();
                        textview.setVisibility(View.VISIBLE);
                        textview.setText("已删除");
                        button.setEnabled(false);
                        button2.setEnabled(false);
                        button.setVisibility(View.GONE);
                        button2.setVisibility(View.GONE);
                    }else if ("03".equals(biaoshi)||"02".equals(biaoshi)){
                        reduceQjcount();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pd!=null&&pd.isShowing()){
                            pd.dismiss();
                        }
                        if ("03".equals(biaoshi)||"02".equals(biaoshi)){
                            reduceQjcount();
                        }
                        Log.d("网络错误", volleyError.toString());
                    }
                });
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("userId", uId);
                param.put("companyId",qiyeId);
                param.put("companyName",companyName);
                param.put("comAddress",comAddress);
                param.put("deleteFlg","1");
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    class Viewholder{
        ImageView iv_avatar;
        TextView tvTitleA;
        TextView tv_time;
        TextView tv_name;
        TextView tv_reason;
        TextView tv_added;
        Button btn_add,btn_jujue;

    }
}
