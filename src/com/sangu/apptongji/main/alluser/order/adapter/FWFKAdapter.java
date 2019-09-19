package com.sangu.apptongji.main.alluser.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.entity.FWFKInfo;

import java.util.List;

/**
 * Created by Administrator on 2016-10-19.
 */

public class FWFKAdapter extends BaseAdapter {
    private List<FWFKInfo> data;
    private Context context;
    private LayoutInflater inflater;

    public FWFKAdapter(List<FWFKInfo> data, Context context) {
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
            convertView = inflater.inflate(R.layout.fuwulist_item,parent,false);
            holder = new ViewHolder();
            holder.tv_fuwu_fen = (TextView) convertView.findViewById(R.id.tv_fuwu_fen);
            holder.tv_title_name = (TextView) convertView.findViewById(R.id.tv_title_name);
            holder.tv_kaidan_shijian = (TextView) convertView.findViewById(R.id.tv_kaidan_shijian);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        FWFKInfo fwfkInfo = (FWFKInfo) getItem(position);
        String remark = fwfkInfo.getRemark();
        holder.tv_title_name.setText(fwfkInfo.getUserId());
        if (remark==null||"".equals(remark)||"null".equals(remark)||"NULL".equals(remark)){
            holder.tv_fuwu_fen.setText("暂无");
        }else {
            holder.tv_fuwu_fen.setText(remark + "分");
        }
        String time = fwfkInfo.getCreateTime();
        String time1 = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " "
                + time.substring(8, 10) + ":" + time.substring(10, 12);
        holder.tv_kaidan_shijian.setText(time1);
        return convertView;
    }
    class ViewHolder{
        TextView tv_title_name;
        TextView tv_fuwu_fen;
        TextView tv_kaidan_shijian;
    }
}
