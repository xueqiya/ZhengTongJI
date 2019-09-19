package com.sangu.apptongji.main.alluser.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-03-16.
 */

public class XuqiuListAdapter extends BaseAdapter {
    Context context;
    List<JSONObject> friends;
    String createTime,dynamicSeq;

    public XuqiuListAdapter(Context context, List<JSONObject> friends, String createTime,String dynamicSeq) {
        this.context = context;
        this.friends=friends;
        this.createTime=createTime;
        this.dynamicSeq=dynamicSeq;
    }

    @Override
    public int getCount() {
        return (friends == null || friends.size() == 0)?0:friends.size();
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
        JSONObject users = friends.get(position);
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
        try {
            String reason = "请求接单";
            final String nick = users.isNull("uName")?users.getString("uId"):users.getString("uName");
            final String uId = users.getString("uId");
            String avatar = users.isNull("uImage")?"":users.getString("uImage");
            String sex = users.getString("uSex");
            String time = users.getString("resv1");
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
            holder.tv_time.setText(time);
            holder.tv_name.setText(nick);
            holder.tv_reason.setText(reason);
            holder.tv_added.setVisibility(View.GONE);
            holder.btn_add.setVisibility(View.VISIBLE);
            holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserDetailsActivity.class);
                    intent.putExtra(FXConstant.JSON_KEY_HXID,uId);
                    intent.putExtra("xiaoliang","01");
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("dynamicSeq",dynamicSeq);
                    context.startActivity(intent);
                }
            });
            holder.tvTitleA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserDetailsActivity.class);
                    intent.putExtra(FXConstant.JSON_KEY_HXID,uId);
                    intent.putExtra("xiaoliang","01");
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("dynamicSeq",dynamicSeq);
                    context.startActivity(intent);
                }
            });
            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserDetailsActivity.class);
                    intent.putExtra(FXConstant.JSON_KEY_HXID,uId);
                    intent.putExtra("xiaoliang","01");
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("dynamicSeq",dynamicSeq);
                    context.startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}
