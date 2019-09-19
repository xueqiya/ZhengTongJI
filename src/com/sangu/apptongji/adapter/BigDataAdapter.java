package com.sangu.apptongji.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.entity.BigDataInfo;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-08-22.
 */

public class BigDataAdapter extends BaseAdapter {


    private LayoutInflater mIndlater;
    private List<BigDataInfo> mDatas = new ArrayList<>();
    private int baseWidth;//屏幕宽度
    private String type;//地区排行还是专业排行
    private int rank; //自己城市当前排名
    private int a;
    private String isHave = "no";

    public  BigDataAdapter(Context context, List<JSONObject> jsonArray, int screenWidth , String type1){

        mIndlater = LayoutInflater.from(context);

        baseWidth = screenWidth*3/4;

        type = type1;
        a = 1;

        SharedPreferences sp =context.getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
        final String city = sp.getString("city", "");

        for (JSONObject json : jsonArray) {

            if (type1.equals("地区")){

                String count1 = json.getString("count1");
                String count2 = json.getString("count2");
                String region = json.getString("region");

                BigDataInfo bigdataInfo = new BigDataInfo();

                bigdataInfo.setCount1(count1);
                bigdataInfo.setCount2(count2);
                bigdataInfo.setRegion(region);

                mDatas.add(bigdataInfo);

                if (city.equals(region)){
                    isHave = "yes";
                    int b = a;
                    rank = b;

                    if (a == 1){
                    }else {
                        mDatas.add(0,bigdataInfo);
                    }

                }else
                {
                    a++;
                }

            }else {

                String count = json.getString("count");
                String count2 = json.getString("count2");
                String upName = json.getString("upName");
                String identify = json.getString("identify");

                BigDataInfo bigdataInfo = new BigDataInfo();

                bigdataInfo.setCount(count);
                bigdataInfo.setCount2(count2);
                bigdataInfo.setUpName(upName);
                bigdataInfo.setIdentify(identify);

                mDatas.add(bigdataInfo);

            }

        }

    }


        @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null){

            convertView = mIndlater.inflate(R.layout.item_bigdata, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.fristLong = (View) convertView.findViewById(R.id.top_view);

            viewHolder.secondLong = (View) convertView.findViewById(R.id.bottom_view);

            viewHolder.tv_region = (TextView) convertView.findViewById(R.id.tv_region);

            viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.all_content);

            convertView.setTag(viewHolder);

        }else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BigDataInfo bigDataInfo = mDatas.get(position);

        if (type.equals("地区")){

            if (isHave.equals("yes")){

                if (rank==1){

                    if (position == 0){

                        viewHolder.tv_region.setText(bigDataInfo.getRegion()+" 第"+rank+"名"+" 当前地区排名");

                    }else
                    {
                        viewHolder.tv_region.setText(bigDataInfo.getRegion()+" 第"+Integer.valueOf(position+1)+"名");
                    }

                }else
                {
                    if (position == 0){

                        viewHolder.tv_region.setText(bigDataInfo.getRegion()+" 第"+rank+"名"+" 当前地区排名");

                    }else
                    {
                        viewHolder.tv_region.setText(bigDataInfo.getRegion()+" 第"+Integer.valueOf(position)+"名");
                    }
                }

            }else
            {

                if (position == 0){

                    viewHolder.tv_region.setText(bigDataInfo.getRegion()+" 第"+Integer.valueOf(position+1)+"名"+" 地区指数排名");

                }else
                {
                    viewHolder.tv_region.setText(bigDataInfo.getRegion()+" 第"+Integer.valueOf(position+1)+"名");
                }

            }


            ViewGroup.LayoutParams lp = viewHolder.fristLong.getLayoutParams();
            ViewGroup.LayoutParams lp2 = viewHolder.secondLong.getLayoutParams();

            double count1 = Double.parseDouble(bigDataInfo.getCount1());
            double count2 = Double.parseDouble(bigDataInfo.getCount2());

            if (count1 > count2){

                //第一个大以第一个为主

                lp.width = baseWidth;

                viewHolder.fristLong.setLayoutParams(lp);

                //判断第二个比第一个少多少，如果少的多就多加百分之二十

                if (count1/5 > count2){

                    double b = (count2/count1)*baseWidth+baseWidth/5;

                    int b1 = (int) b;

                    lp2.width = b1;

                    viewHolder.secondLong.setLayoutParams(lp2);

                }else
                {
                    double b = (count2/count1)*baseWidth;

                    int b1 = (int) b;

                    lp2.width = b1;

                    viewHolder.secondLong.setLayoutParams(lp2);
                }

            }else
            {
                //第二个大 以第二个为主
                lp2.width = baseWidth;
                viewHolder.secondLong.setLayoutParams(lp2);

                if (count2/5 > count1){

                    double b = (count1/count2)*baseWidth + baseWidth/5;
                    int b1 = (int) b;

                    lp.width = b1;
                    viewHolder.fristLong.setLayoutParams(lp);

                }else
                {
                    double b = (count1/count2)*baseWidth;

                    int b1 = (int) b;

                    lp.width = b1;
                    viewHolder.fristLong.setLayoutParams(lp);
                }

            }

        }else
        {

            viewHolder.tv_region.setText("【"+bigDataInfo.getUpName()+"】");

            ViewGroup.LayoutParams lp = viewHolder.fristLong.getLayoutParams();
            ViewGroup.LayoutParams lp2 = viewHolder.secondLong.getLayoutParams();

            double count = Double.parseDouble(bigDataInfo.getCount());
            double count2 = Double.parseDouble(bigDataInfo.getCount2());

            if (count > count2){

                //第一个大以第一个为主

                lp.width = baseWidth;

                viewHolder.fristLong.setLayoutParams(lp);

                //判断第二个比第一个少多少，如果少的多就多加百分之二十

                if (count/5 > count2){

                    double b = (count2/count)*baseWidth+baseWidth/5;

                    int b1 = (int) b;

                    lp2.width = b1;

                    viewHolder.secondLong.setLayoutParams(lp2);

                }else
                {
                    double b = (count2/count)*baseWidth;

                    int b1 = (int) b;

                    lp2.width = b1;

                    viewHolder.secondLong.setLayoutParams(lp2);
                }

            }else
            {
                //第二个大 以第二个为主
                lp2.width = baseWidth;
                viewHolder.secondLong.setLayoutParams(lp2);

                if (count2/5 > count){

                    double b = (count/count2)*baseWidth + baseWidth/5;
                    int b1 = (int) b;

                    lp.width = b1;
                    viewHolder.fristLong.setLayoutParams(lp);

                }else
                {
                    double b = (count/count2)*baseWidth;

                    int b1 = (int) b;

                    lp.width = b1;
                    viewHolder.fristLong.setLayoutParams(lp);
                }

            }
        }

        return convertView;

    }



    private class ViewHolder {

        View fristLong;
        View secondLong;
        TextView tv_region;
        RelativeLayout  relativeLayout;

    }


}
