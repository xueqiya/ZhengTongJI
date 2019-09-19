package com.sangu.apptongji.main.alluser.order.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.entity.DianziDanju;

import java.util.List;

/**
 * Created by Administrator on 2017-07-19.
 */

public class DZdanjuListAdapter extends BaseAdapter {
    private List<DianziDanju> data;
    private Context context;
    private LayoutInflater inflater;

    public DZdanjuListAdapter(List<DianziDanju> data, Context context) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void appendData(List<DianziDanju> list,Context context) {//分页关键
        this.data.addAll(list);
        this.context = context.getApplicationContext();
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
            convertView = inflater.inflate(R.layout.item_dianzi_danju,parent,false);
            holder = new ViewHolder();
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            holder.tv_neirong = (TextView) convertView.findViewById(R.id.tv_neirong);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        DianziDanju orderInfo = (DianziDanju) getItem(position);
        String orderTime = TextUtils.isEmpty(orderInfo.getTimestamp())?"":orderInfo.getTimestamp();
        orderTime = orderTime.substring(0,4)+"-"+orderTime.substring(4,6)+"-"+orderTime.substring(6,8)+" "
                +orderTime.substring(8,10)+":"+orderTime.substring(10,12);
        holder.tv_time.setText(orderTime);
        String flag = orderInfo.getFlag();
        if ("01".equals(flag)){
            String oP1="",oP2="",oP3="",oP4="",oP5="";
            String orderProject = TextUtils.isEmpty(orderInfo.getTitle()) ? "" : orderInfo.getTitle();
            int maxSplit = 6;
            String[] orderProjectArray = orderProject.split(",", maxSplit);
            if (orderProjectArray.length>0) {
                oP1 = orderProjectArray[0];
            }
            if (orderProjectArray.length>1) {
                oP2 = orderProjectArray[1];
            }
            if (orderProjectArray.length>2) {
                oP3 = orderProjectArray[2];
            }
            if (orderProjectArray.length>3) {
                oP4 = orderProjectArray[3];
            }
            if (orderProjectArray.length>4) {
                oP5 = orderProjectArray[4];
            }
            holder.tv_neirong.setText(oP1+oP2+oP3+oP4+oP5);
        }else {
            String title = TextUtils.isEmpty(orderInfo.getTitle()) ? "" : orderInfo.getTitle();
            holder.tv_neirong.setText(title);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_neirong,tv_time;
    }
}
