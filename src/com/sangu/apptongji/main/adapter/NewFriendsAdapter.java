package com.sangu.apptongji.main.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.fanxin.easeui.domain.EaseUser;
import com.fanxin.easeui.utils.EaseCommonUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.db.UserDao;
import com.sangu.apptongji.domain.InviteMessage;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.entity.Friend;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewFriendsAdapter extends BaseAdapter {
    Context context;
    List<InviteMessage> msgs=null;
    List<Friend> friends=null;
    int total = 0;

    public NewFriendsAdapter(Context context, List<InviteMessage> msgs,List<Friend> friends) {
        this.context = context;
        this.friends=friends;
        this.msgs = msgs;
        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        total = msgs.size();
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Friend f = friends.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.fx_item_newfriend_msg,null);
            holder = new ViewHolder();
            holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            holder.tv_reason = (TextView) convertView.findViewById(R.id.tv_reason);
            holder.tv_added = (TextView) convertView.findViewById(R.id.tv_added);
            holder.btn_add = (Button) convertView.findViewById(R.id.btn_add);
            holder.btnrefuse = (Button) convertView.findViewById(R.id.btn_cancle);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.btnrefuse.setVisibility(View.INVISIBLE);
//        final InviteMessage msg = (InviteMessage) getItem(total - 1 - position);
        String reason = "请求加您好友";
        final String nick = TextUtils.isEmpty(f.getuName())?f.getuLoginId():f.getuName();
        final String uId = f.getuId();
        String avatar = TextUtils.isEmpty(f.getuImage())?"":f.getuImage();
        String sex = f.getuSex();
        String resv3 = f.getResv3();
        final ViewHolder finalHolder = holder;
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
        holder.tv_time.setText(resv3);
        holder.tv_name.setText(nick);
        holder.tv_reason.setText(reason);
        holder.tv_added.setVisibility(View.GONE);
        holder.btn_add.setVisibility(View.VISIBLE);
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
        holder.iv_avatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,uId));
            }
        });
        holder.tvTitleA.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,uId));
            }
        });
        holder.btn_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptInvitation(finalHolder.btn_add,uId,finalHolder.tv_added, finalAvatar,nick);
            }

        });
        return convertView;
    }

    private static class ViewHolder {
        ImageView iv_avatar;
        TextView tvTitleA;
        TextView tv_time;
        TextView tv_name;
        TextView tv_reason;
        TextView tv_added;
        Button btn_add,btnrefuse;

    }
    /**
     * 同意好友请求
     *
     * @param
     * @param button
     * @param
     */
    private void acceptInvitation(final Button button, final String uId,
                                  final TextView textview, final String avatar, final String name) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("正在同意...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        String url = FXConstant.URL_CONFIRMADD_FRIEND;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("SUCCESS")){
                        reduceQjcount();
                        EaseUser user = new EaseUser(uId);
                        user.setuName(name);
                        user.setNick(name);
                        user.setuLoginId(uId);
                        user.setAvatar(avatar);
                        EaseCommonUtils.setUserInitialLetter(user);
                        // 存入内存
                        DemoHelper.getInstance().getContactList().put(user.getUsername(), user);
                        // 存入db
                        UserDao dao = new UserDao(context);
                        dao.saveContact(user);
                        Toast.makeText(context,"同意成功！",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        textview.setVisibility(View.VISIBLE);
                        button.setEnabled(false);
                        button.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Toast.makeText(context,"同意失败！",Toast.LENGTH_SHORT).show();
                Log.d("同意好友请求网络错误",volleyError.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("f_request",uId);
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
                param.put("addFriendCount","-1");
                param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
