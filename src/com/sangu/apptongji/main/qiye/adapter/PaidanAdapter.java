package com.sangu.apptongji.main.qiye.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.qiye.entity.OffSendOrderList;
import com.sangu.apptongji.main.qiye.entity.PaiDanInfo;
import com.sangu.apptongji.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2017-01-17.
 */

public class PaidanAdapter extends BaseAdapter {
    private List<PaiDanInfo> data;
    private List<OffSendOrderList> data2;
    private Context context;
    private LayoutInflater inflater;
    String type = null;

    public PaidanAdapter(List<PaiDanInfo> data, Context context,String type) {
        this.type = type;
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    public PaidanAdapter(List<OffSendOrderList> data, Context context) {
        this.data2 = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (type!=null){
            return data.size();
        }else {
            return data2.size();
        }
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
            convertView = inflater.inflate(R.layout.list_item_kaoqin,null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvjiaoyizhuangtai = (TextView) convertView.findViewById(R.id.tv_dingdanzhuangtai);
            holder.tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            holder.tvkaoqinTime = (TextView) convertView.findViewById(R.id.tv_from);
            holder.ivHead = (CircleImageView) convertView.findViewById(R.id.iv_head);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        if (type!=null) {
            holder.ivHead.setVisibility(View.GONE);
            holder.tvTitleA.setVisibility(View.GONE);
            PaiDanInfo orderInfo = data.get(position);
            holder.tvName.setText(TextUtils.isEmpty(orderInfo.getuName()) ? "" : orderInfo.getuName());
            String kaoqinTime = TextUtils.isEmpty(orderInfo.getOrderTime()) ? "" : orderInfo.getOrderTime();
            kaoqinTime = kaoqinTime.substring(0, 4) + "-" + kaoqinTime.substring(4, 6) + "-" + kaoqinTime.substring(6, 8) + " "
                    + kaoqinTime.substring(8, 10) + ":" + kaoqinTime.substring(10, 12);
            holder.tvkaoqinTime.setText(kaoqinTime);
            String orderState = orderInfo.getOrderState();
            if ("05".equals(orderState)||"07".equals(orderState)) {
                holder.tvjiaoyizhuangtai.setText("交易完成");
                holder.tvjiaoyizhuangtai.setTextColor(Color.rgb(136, 136, 136));
            } else {
                holder.tvjiaoyizhuangtai.setText("交易中");
                holder.tvjiaoyizhuangtai.setTextColor(Color.rgb(255, 99, 71));
            }
        }else {
            holder.ivHead.setVisibility(View.GONE);
            holder.tvTitleA.setVisibility(View.GONE);
            OffSendOrderList offSendOrderList = data2.get(position);
            String sign3 = offSendOrderList.getSignature3();
            holder.tvName.setText(TextUtils.isEmpty(offSendOrderList.getOrdName()) ? offSendOrderList.getOrdId() : offSendOrderList.getOrdName());
            String rel_time = TextUtils.isEmpty(offSendOrderList.getTime_seq()) ? "" : offSendOrderList.getTime_seq();
            if (rel_time.length()>12) {
                rel_time = rel_time.substring(0, 4) + "-" + rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                        + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
                holder.tvkaoqinTime.setText(rel_time);
            }
            if (sign3!=null&&!"".equals(sign3)&&!"null".equals(sign3)) {
                Log.e("paidanadap",sign3);
                holder.tvjiaoyizhuangtai.setText("交易完成");
                holder.tvjiaoyizhuangtai.setTextColor(Color.rgb(136, 136, 136));
            } else {
                holder.tvjiaoyizhuangtai.setText("交易中");
                holder.tvjiaoyizhuangtai.setTextColor(Color.rgb(255, 99, 71));
            }
        }
        return convertView;
    }
    class ViewHolder {
        TextView tvName,tvTitleA;
        TextView tvkaoqinTime,tvjiaoyizhuangtai;
        CircleImageView ivHead;
    }

}
