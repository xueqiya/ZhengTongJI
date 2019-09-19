package com.sangu.apptongji.main.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sangu.apptongji.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-16.
 */

public class KyqAdapter extends BaseAdapter{
    private List<KyqInfo> list;
    private Context context;
    private List<String> list_selected = new ArrayList<>();// 需要发送的数据
    private Map<Integer,Boolean> map=new HashMap<>();// 存放已被选中的CheckBox

    private String redAuth;//记录假设是红包权限跳转过来的就改变初始值未都不选中
    private String isFirstLoad;//是否是首次加载

    public KyqAdapter(List<KyqInfo> list,List<String> selected,Context context, String isRedAuth) {

        this.list_selected = selected;
        this.list = list;
        this.context = context;
        this.redAuth = isRedAuth;
        this.isFirstLoad = "yes";
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_address_friend_kyq, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final KyqInfo kyqInfo = list.get(position);
        String name = kyqInfo.getName();
        final String id = kyqInfo.getId();
        holder.tvName.setText(name);
        holder.tv_number.setText("("+id+")");

        if (redAuth.equals("yes")){

            if (!map.containsKey(position)){

                map.put(position,false);

            }
        }

        if(map!=null&&map.containsKey(position)){

            if (map.get(position)){

                holder.cb.setChecked(true);

            }else
            {
                holder.cb.setChecked(false);
            }

        }else {

            holder.cb.setChecked(true);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 移除
                if (holder.cb.isChecked()) {
                    holder.cb.setChecked(false);
                    map.put(position,false);
                    if (list_selected.contains(id)) {
                        list_selected.remove(id);
                    }
                } else {
                    holder.cb.setChecked(true);
                   // map.remove(position);
                    map.put(position,true);
                    if (!list_selected.contains(id)) {
                        list_selected.add(id);
                    }
                }
            }
        });
        return convertView;
    }

    public List<String> getList_selected(){
        return list_selected;
    }

    class ViewHolder{
        CheckBox cb;
        TextView tvName;
        TextView tv_number;
    }
}
