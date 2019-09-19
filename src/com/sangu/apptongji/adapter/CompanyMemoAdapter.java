package com.sangu.apptongji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.sangu.apptongji.R;

import java.util.ArrayList;
import java.util.List;

public class CompanyMemoAdapter extends BaseAdapter {

    private LayoutInflater mIndlater;
    private List<JSONObject> mDatas = new ArrayList<>();


    public  CompanyMemoAdapter(Context context, List<JSONObject> jsonArray, String type1){

        mIndlater = LayoutInflater.from(context);

        this.mDatas = jsonArray;

    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null){

            convertView = mIndlater.inflate(R.layout.item_companymemo, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.tv_memotype = (TextView) convertView.findViewById(R.id.tv_memotype);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.timeAdress = (TextView) convertView.findViewById(R.id.timeAdress);

            convertView.setTag(viewHolder);

        }else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONObject object = mDatas.get(i);

       if (object.getString("content") != null && object.getString("content").length()>0){

           viewHolder.tv_content.setText(object.getString("content"));

       }else {
           viewHolder.tv_content.setText(object.getString("(无标题)"));
       }

       //20190219194037
       String beginTime = object.getString("begintime");
       String time = beginTime.substring(8,10)+":"+beginTime.substring(10,12);
       String region = object.getString("region");


       if (region != null && region.length() > 0){

           viewHolder.timeAdress.setText(time + " " + region);

       }else {

           viewHolder.timeAdress.setText(time);
       }


        return convertView;
    }



    private class ViewHolder {

        TextView tv_memotype;
        TextView tv_content;
        TextView timeAdress;

    }

}
