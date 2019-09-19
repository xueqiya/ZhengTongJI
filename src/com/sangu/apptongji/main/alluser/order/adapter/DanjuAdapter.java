package com.sangu.apptongji.main.alluser.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;
import com.sangu.apptongji.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2017-07-19.
 */

public class DanjuAdapter extends BaseAdapter {
    private List<OrderInfo> data;
    private Context context;
    private LayoutInflater inflater;

    public DanjuAdapter(List<OrderInfo> data, Context context) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.list_item_mydingdan,parent,false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_title_name);
            holder.tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            holder.tvOrderBody = (TextView) convertView.findViewById(R.id.tv__xiangmu_name);
            holder.tvOrderState = (TextView) convertView.findViewById(R.id.tv_dingdanzhuangtai);
            holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tv_jiaoyishijian);
            holder.tvTotalAmt = (TextView) convertView.findViewById(R.id.tv_balance);
            holder.ivHead = (CircleImageView) convertView.findViewById(R.id.iv_head);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        OrderInfo orderInfo = (OrderInfo) getItem(position);
        String remark = TextUtils.isEmpty(orderInfo.getRemark())?"":orderInfo.getRemark();
        holder.tvName.setText(TextUtils.isEmpty(orderInfo.getU_name())?orderInfo.getUserId():orderInfo.getU_name());
        holder.tvOrderBody.setText(TextUtils.isEmpty(orderInfo.getOrderBody())?"":orderInfo.getOrderBody());
        String orderTime = TextUtils.isEmpty(orderInfo.getOrderTime())?"":orderInfo.getOrderTime();
        orderTime = orderTime.substring(0,4)+"-"+orderTime.substring(4,6)+"-"+orderTime.substring(6,8)+" "
                +orderTime.substring(8,10)+":"+orderTime.substring(10,12);
        holder.tvOrderTime.setText(orderTime);
        String totalAmt = TextUtils.isEmpty(orderInfo.getTotalAmt())?"":orderInfo.getTotalAmt();
        if (!(totalAmt == "")) {
            totalAmt = totalAmt + " 元";
        }
        holder.tvTotalAmt.setText(totalAmt);
        String state = orderInfo.getOrderState();
        if (state.equals("03")){
            holder.tvOrderState.setText("待提交");
        }else if (state.equals("04")){
            holder.tvOrderState.setText("待签收");
        }else {
            holder.tvOrderState.setTextColor(Color.parseColor("#c7c7c7"));
            holder.tvOrderState.setText("交易成功");
        }
        String head = TextUtils.isEmpty(orderInfo.getU_uImage())?"":orderInfo.getU_uImage();
        final String hxid = orderInfo.getUserId();
        if (!(head=="")){
            String[] orderProjectArray = head.split("\\|");
            head = orderProjectArray[0];
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + head,holder.ivHead, DemoApplication.mOptions);
            holder.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserDetailsActivity.class);
                    intent.putExtra(FXConstant.JSON_KEY_HXID,hxid);
                    context.startActivity(intent);
                }
            });
        }else {
            holder.ivHead.setVisibility(View.INVISIBLE);
            holder.tvTitleA.setVisibility(View.VISIBLE);
            holder.tvTitleA.setText(TextUtils.isEmpty(orderInfo.getU_name())?orderInfo.getUserId():orderInfo.getU_name());
            holder.tvTitleA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserDetailsActivity.class);
                    intent.putExtra(FXConstant.JSON_KEY_HXID,hxid);
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvName,tvTitleA;
        TextView tvOrderBody;
        TextView tvTotalAmt;
        TextView tvOrderTime;
        TextView tvOrderState;
        CircleImageView ivHead;
    }
}
