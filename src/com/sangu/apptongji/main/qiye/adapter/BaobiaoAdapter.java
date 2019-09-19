package com.sangu.apptongji.main.qiye.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.qiye.entity.BaobiaoInfo;

import java.util.List;

/**
 * Created by Administrator on 2017-01-11.
 */

public class BaobiaoAdapter extends BaseAdapter {
    private List<BaobiaoInfo> data;
    private Context context;

    public BaobiaoAdapter(List<BaobiaoInfo> data, Context context) {
        this.data = data;
        this.context = context;
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
        BaobiaoInfo baibiaoInfo = data.get(position);
        ViewHolder holder;
        if (convertView==null){
            convertView = View.inflate(context, R.layout.item_baobiao,null);
            holder = new ViewHolder();
            holder.tv_leixing = (TextView) convertView.findViewById(R.id.tv_leixing);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            holder.tv_zhuangtai = (TextView) convertView.findViewById(R.id.tv_zhuangtai);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        String name = TextUtils.isEmpty(baibiaoInfo.getPlanTitle())?baibiaoInfo.getUserId():baibiaoInfo.getPlanTitle();
        String planContent = TextUtils.isEmpty(baibiaoInfo.getPlanContent())?"":baibiaoInfo.getPlanContent();
        String time = baibiaoInfo.getSignatureTime1();
        if (time.length()>12) {
            time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " "
                    + time.substring(8, 10) + ":" + time.substring(10, 12);
        }
        String signPic2 = baibiaoInfo.getSignature2();
        holder.tv_name.setText(name);
        holder.tv_time.setText(time);
        if (!"".equals(planContent)){
            holder.tv_leixing.setText("手写报表");
        }else {
            holder.tv_leixing.setText("文件报表");
        }
        if ("".equals(signPic2)){
            holder.tv_zhuangtai.setText("未回复");
            holder.tv_zhuangtai.setTextColor(Color.rgb(255,87,34));
        }else {
            holder.tv_zhuangtai.setText("已回复");
            holder.tv_zhuangtai.setTextColor(Color.rgb(136,136,136));
        }
        return convertView;
    }

    class ViewHolder{
        TextView tv_leixing;
        TextView tv_name;
        TextView tv_time;
        TextView tv_zhuangtai;
    }
}
