package com.sangu.apptongji.main.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.sangu.apptongji.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-12-16.
 */

public class SendAdapter extends BaseAdapter implements Filterable{
    private List<PhoneInfo> list;
    private Context context;
    private PersonFilter filter;

    public SendAdapter(List<PhoneInfo> list, Context context) {
        this.list = list;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_friend_send,parent,false);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder==null){
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            convertView.setTag(holder);
        }
        PhoneInfo kyqInfo = list.get(position);
        String name = kyqInfo.getName();
        final String id = kyqInfo.getNumber();
        holder.tvName.setText(name);
        holder.tv_phone.setText("("+id+")");
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new PersonFilter(list);
        }
        return filter;
    }

    private class PersonFilter extends android.widget.Filter {

        private List<PhoneInfo> original;

        public PersonFilter(List<PhoneInfo> list) {
            this.original = list;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = original;
                results.count = original.size();
            } else {
                List<PhoneInfo> mList = new ArrayList<PhoneInfo>();
                for (PhoneInfo p: original) {
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase())
                            || new String(p.getNumber() + "").toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                        mList.add(p);
                    }
                }
                results.values = mList;
                results.count = mList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (List<PhoneInfo>)results.values;
            notifyDataSetChanged();
        }
    }

    class ViewHolder{
        TextView tvName;
        TextView tv_phone;
    }
}
