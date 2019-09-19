package com.sangu.apptongji.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;

import java.util.List;

/**
 * Created by Administrator on 2017-03-28.
 */

public class PingLunAdapter extends BaseAdapter {
    private Context context;
    List<JSONObject> data;
    public MyItemClickListener mItemClickListener;

    public PingLunAdapter(Context context, List<JSONObject> data) {
        this.context = context;
        this.data = data;
    }

    public interface MyItemClickListener {
        void onItemClick(View view,int position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pinglun, parent, false);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.rl_pinglun = (RelativeLayout) convertView.findViewById(R.id.rl_click);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content_pinglun);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_pinglun_time);
            convertView.setTag(holder);
        }
        JSONObject object = data.get(position);
        setCommentTextClick(holder.tv_content,object);
        String time = object.getString("timeStamp");
        time = time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" "+time.substring(8,10)+":"+time.substring(10,12);
        holder.tv_time.setText(time);
        holder.rl_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
            }
        });
        return convertView;
    }

    // 设置点赞的
    private void setCommentTextClick(TextView mTextView2,JSONObject object) {
        if (object==null) {
            mTextView2.setVisibility(View.GONE);
        } else {
            mTextView2.setVisibility(View.VISIBLE);
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int start = 0;
        String userID_temp = null;
        userID_temp = object.getString("userId");
        String nick = object.getString("userName");
        String content = object.getString("content");
        String content_1 = ": " + content;
        ssb.append(nick + content_1);
        ssb.setSpan(new TextViewURLSpan(nick, userID_temp, 1), start,
                start + nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView2.setText(ssb);
        mTextView2.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private class TextViewURLSpan extends ClickableSpan {
        private String userID;
        // 0是点赞里面的名字。1是评论里面的名字；2是评论中的删除
        private int type = 0;
        private TextView ctextView;
        private JSONArray cjsons;
        private View view;
        private int goodSize;
        private String scID;

        public TextViewURLSpan(String nick, String us,
                               String scID, int type, TextView ctextView, JSONArray cjsons,
                               View view, int goodSize) {
            this.userID = userID;
            this.type = type;
            this.ctextView = ctextView;
            this.cjsons = cjsons;
            this.view = view;
            this.goodSize = goodSize;
            this.scID = scID;
        }

        public TextViewURLSpan(String nick, String userID, int type) {
            this.userID = userID;
            this.type = type;

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (type != 2) {
                ds.setColor(context.getResources().getColor(R.color.text_color));
            }
            ds.setUnderlineText(false); // 去掉下划线
        }

        @Override
        public void onClick(final View widget) {

            if (widget instanceof TextView) {
                ((TextView) widget).setHighlightColor(context.getResources()
                        .getColor(android.R.color.darker_gray));
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ((TextView) widget)
                                .setHighlightColor(context.getResources()
                                        .getColor(android.R.color.transparent));
                    }
                }, 1000);
            }
            if (type == 2) {
//                showDeleteDialog(userID, postion, scID, type, ctextView, cjsons,
//                        view, goodSize);

            } else {
                context.startActivity(
                        new Intent(context, UserDetailsActivity.class)
                                .putExtra(FXConstant.JSON_KEY_HXID, userID));
            }
        }

    }
    class ViewHolder {
        RelativeLayout rl_pinglun;
        TextView tv_content;
        TextView tv_time;

    }
}
