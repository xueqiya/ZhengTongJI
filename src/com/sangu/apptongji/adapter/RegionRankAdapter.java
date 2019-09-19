package com.sangu.apptongji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.entity.BigDataInfo;

import java.util.ArrayList;
import java.util.List;

public class RegionRankAdapter extends BaseAdapter {


    private LayoutInflater mIndlater;
    private List<RegionRankInfo> mDatas = new ArrayList<>();

    public RegionRankAdapter(Context context, List<JSONObject> jsonArray,String type1){

        mIndlater = LayoutInflater.from(context);

        for (JSONObject json : jsonArray) {

            String region = json.getString("region");
            String count = json.getString("count");

            RegionRankInfo regionRankInfo = new RegionRankInfo();

            regionRankInfo.setCount(count);
            regionRankInfo.setRegion(region);

            mDatas.add(regionRankInfo);
        }


    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        RegionRankAdapter.ViewHolder viewHolder = null;

        if (convertView == null){

            convertView = mIndlater.inflate(R.layout.item_regionrank, parent, false);

            viewHolder = new RegionRankAdapter.ViewHolder();

            viewHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);

            viewHolder.tv_regionName = (TextView) convertView.findViewById(R.id.tv_regionName);

            convertView.setTag(viewHolder);

        }else
        {
            viewHolder = (RegionRankAdapter.ViewHolder) convertView.getTag();
        }

        RegionRankInfo regionRankInfo = mDatas.get(i);

        viewHolder.tv_regionName.setText("地区：" + regionRankInfo.getRegion());
        viewHolder.tv_count.setText("总数："+regionRankInfo.getCount());


        return convertView;
    }


    private class ViewHolder {

        TextView tv_regionName;

        TextView tv_count;

    }

}
