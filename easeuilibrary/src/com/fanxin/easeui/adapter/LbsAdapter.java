package com.fanxin.easeui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.easeui.R;

import java.util.List;

/**
 * Created by Administrator on 2017-07-10.
 */

public class LbsAdapter extends BaseAdapter {
    private Context mcontext;
    private List<String> key;
    private List<String> all;

    public LbsAdapter(Context context, List<String> list, List<String> lists) {
        this.key = list;
        this.all = lists;
    }

    @Override
    public int getCount() {
        return all.size();
    }

    @Override
    public Object getItem(int position) {
        return all.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lbssearch, parent, false);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.tv_up = (TextView) convertView.findViewById(R.id.tv_up);
            holder.tv_down = (TextView) convertView.findViewById(R.id.tv_down);
            convertView.setTag(holder);
        }
        holder.tv_up.setText(key.get(position));
        holder.tv_down.setText(all.get(position));
        return convertView;
    }
    class ViewHolder {
        TextView tv_up;
        TextView tv_down;
    }
}

