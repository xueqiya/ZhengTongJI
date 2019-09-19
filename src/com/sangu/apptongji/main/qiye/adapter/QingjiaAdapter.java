package com.sangu.apptongji.main.qiye.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.qiye.entity.QingjiaInfo;

import java.util.List;

/**
 * Created by Administrator on 2017-01-11.
 */

public class QingjiaAdapter extends BaseAdapter {
    private List<QingjiaInfo> data;
    private Context context;

    public QingjiaAdapter(List<QingjiaInfo> data, Context context) {
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
        QingjiaInfo qingjiaInfo = data.get(position);
        ViewHolder holder;
        if (convertView==null){
            convertView = View.inflate(context, R.layout.item_qingjia,null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            holder.tv_zhuangtai = (TextView) convertView.findViewById(R.id.tv_zhuangtai);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        String name = qingjiaInfo.getResv1();
        String time = qingjiaInfo.getCreatTime();
        time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " "
                + time.substring(8, 10) + ":" + time.substring(10, 12);
        String signPic2 = qingjiaInfo.getSignPic2();
        holder.tv_name.setText(name);
        holder.tv_time.setText(time);
        if (signPic2==null||"".equals(signPic2)){
            holder.tv_zhuangtai.setText("未批准");
            holder.tv_zhuangtai.setTextColor(Color.rgb(255,87,34));
        }else {
            holder.tv_zhuangtai.setText("已批准");
            holder.tv_zhuangtai.setTextColor(Color.rgb(136,136,136));
        }
        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
        TextView tv_time;
        TextView tv_zhuangtai;
    }
}
