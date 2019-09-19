package com.sangu.apptongji.main.alluser.order.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.sangu.apptongji.R;

import java.util.List;

/**
 * Created by Administrator on 2017-03-16.
 */

public class BktxListAdapter extends BaseAdapter {
    Context context;
    List<JSONObject> datas;
    String biaoshi;

    public BktxListAdapter(Context context, List<JSONObject> friends,String biaoshi) {
        this.biaoshi = biaoshi;
        this.context = context;
        this.datas=friends;
    }

    @Override
    public int getCount() {
        return (datas == null || datas.size() == 0)?0:datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_qingjia,null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_title1 = (TextView) convertView.findViewById(R.id.tv_title1);
            holder.tv_title2 = (TextView) convertView.findViewById(R.id.tv_title2);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            holder.tv_zhuangtai = (TextView) convertView.findViewById(R.id.tv_zhuangtai);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        JSONObject data = datas.get(position);
        String jine2 = data.getString("companyAmt");
        holder.tv_title1.setText("类型");
        holder.tv_title2.setText("金额");
        String jine = data.getString("order_amt");
        String time = data.getString("time");
        double jinq = Double.parseDouble(jine);
        String prices1 = String.format("%.2f", jinq);
        if ("001".equals(biaoshi)&&jine2!=null&&Double.parseDouble(jine2)>=0){
            double jinq2 = Double.parseDouble(jine2);
            String prices2 = String.format("%.2f", jinq2);
            holder.tv_time.setText(prices2+"元");
            holder.tv_name.setText("订单交易");
            holder.tv_zhuangtai.setText("剩余"+time+"天可提现");
        }else {
            holder.tv_time.setText(prices1 + "元");
            holder.tv_name.setText("订单交易");
            holder.tv_zhuangtai.setText("剩余" + time + "天可提现");
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_name;
        TextView tv_title1;
        TextView tv_title2;
        TextView tv_time;
        TextView tv_zhuangtai;
    }
}
