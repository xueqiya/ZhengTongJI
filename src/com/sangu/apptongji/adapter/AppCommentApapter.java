package com.sangu.apptongji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sangu.apptongji.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/6.
 */

public class AppCommentApapter extends BaseAdapter {

    private List<String> mDatas = new ArrayList<>();
    private Context context;
    private LayoutInflater mIndlater;

    public AppCommentApapter(Context context){

        mIndlater = LayoutInflater.from(context);
        this.context = context;
        mDatas.add("能接单");
        mDatas.add("能创建自己品牌的平台");
        mDatas.add("只想接单");
        mDatas.add("找啥啥都有");
        mDatas.add("能解决问题");
        mDatas.add("能接工程单");
        mDatas.add("一人接单，九人哭");
        mDatas.add("管理考勤");
        mDatas.add("一年接一单就知足");
        mDatas.add("实时监控交易考勤");

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

            convertView = mIndlater.inflate(R.layout.item_appcomment, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.tv_commentName = (TextView) convertView.findViewById(R.id.tv_commentname);
            viewHolder.tv_commentScore = (TextView) convertView.findViewById(R.id.score);
            viewHolder.tv_commentTime = (TextView) convertView.findViewById(R.id.tv_commenttime);
            viewHolder.tv_commentContent = (TextView) convertView.findViewById(R.id.tv_commentcontent);
            viewHolder.score1 = (ImageView) convertView.findViewById(R.id.starImage1);
            viewHolder.score2 = (ImageView) convertView.findViewById(R.id.starImage2);
            viewHolder.score3 = (ImageView) convertView.findViewById(R.id.starImage3);
            viewHolder.score4 = (ImageView) convertView.findViewById(R.id.starImage4);
            viewHolder.score5 = (ImageView) convertView.findViewById(R.id.starImage5);

            convertView.setTag(viewHolder);

        }else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String appName = mDatas.get(position);

        if (position == 0){

            viewHolder.tv_commentName.setText(appName);
            viewHolder.tv_commentScore.setText("5分");
            viewHolder.tv_commentContent.setText("“聊天”第一次用到了“正经地方”能聊天、能接单");
            viewHolder.score1.setVisibility(View.VISIBLE);
            viewHolder.score2.setVisibility(View.VISIBLE);
            viewHolder.score3.setVisibility(View.VISIBLE);
            viewHolder.score4.setVisibility(View.VISIBLE);
            viewHolder.score5.setVisibility(View.VISIBLE);

        }else if (position == 1){

            viewHolder.tv_commentName.setText(appName);
            viewHolder.tv_commentScore.setText("5分");
            viewHolder.tv_commentContent.setText("什么叫平台，这才是，有很多伪平台，实际上是整合，做着以一个品牌，整合一个行业的事，起码这里可以创建自己的品牌");
            viewHolder.score1.setVisibility(View.VISIBLE);
            viewHolder.score2.setVisibility(View.VISIBLE);
            viewHolder.score3.setVisibility(View.VISIBLE);
            viewHolder.score4.setVisibility(View.VISIBLE);
            viewHolder.score5.setVisibility(View.VISIBLE);

        }else if (position == 2){

            viewHolder.tv_commentName.setText(appName);
            viewHolder.tv_commentScore.setText("1分");
            viewHolder.tv_commentContent.setText("我只想接单，不分享，不推荐，不给抽成，也不想做广告，可以吗？");
            viewHolder.score1.setVisibility(View.VISIBLE);
            viewHolder.score2.setVisibility(View.INVISIBLE);
            viewHolder.score3.setVisibility(View.INVISIBLE);
            viewHolder.score4.setVisibility(View.INVISIBLE);
            viewHolder.score5.setVisibility(View.INVISIBLE);
        }else if (position == 3){
            viewHolder.tv_commentName.setText(appName);
            viewHolder.tv_commentScore.setText("4分");
            viewHolder.tv_commentContent.setText("生活也好、商务也好，有这个就够啦，因为找啥啥都有");
            viewHolder.score1.setVisibility(View.VISIBLE);
            viewHolder.score2.setVisibility(View.VISIBLE);
            viewHolder.score3.setVisibility(View.VISIBLE);
            viewHolder.score4.setVisibility(View.VISIBLE);
            viewHolder.score5.setVisibility(View.INVISIBLE);
        }else if (position == 4){
            viewHolder.tv_commentName.setText(appName);
            viewHolder.tv_commentScore.setText("5分");
            viewHolder.tv_commentContent.setText("是啊,总不能用一种服务，就得安装一个APP啊，太麻烦，发啥单都有人接，关键是能解决问题就好啦");
            viewHolder.score1.setVisibility(View.VISIBLE);
            viewHolder.score2.setVisibility(View.VISIBLE);
            viewHolder.score3.setVisibility(View.VISIBLE);
            viewHolder.score4.setVisibility(View.VISIBLE);
            viewHolder.score5.setVisibility(View.VISIBLE);
        }else if (position == 5){
            viewHolder.tv_commentName.setText(appName);
            viewHolder.tv_commentScore.setText("5分");
            viewHolder.tv_commentContent.setText("“服务行业，可不想打车，一天可以做几单十几单，上半年接了一单工程到现在都没有做完，一单顶千单");
            viewHolder.score1.setVisibility(View.VISIBLE);
            viewHolder.score2.setVisibility(View.VISIBLE);
            viewHolder.score3.setVisibility(View.VISIBLE);
            viewHolder.score4.setVisibility(View.VISIBLE);
            viewHolder.score5.setVisibility(View.VISIBLE);
        }else if (position == 6){
            viewHolder.tv_commentName.setText(appName);
            viewHolder.tv_commentScore.setText("3分");
            viewHolder.tv_commentContent.setText("一人接单，九人哭，这就是竞争啊");
            viewHolder.score1.setVisibility(View.VISIBLE);
            viewHolder.score2.setVisibility(View.VISIBLE);
            viewHolder.score3.setVisibility(View.VISIBLE);
            viewHolder.score4.setVisibility(View.INVISIBLE);
            viewHolder.score5.setVisibility(View.INVISIBLE);
        }else if (position == 7){
            viewHolder.tv_commentName.setText(appName);
            viewHolder.tv_commentScore.setText("4分");
            viewHolder.tv_commentContent.setText("我们这里很多公司、厂长都用这个，做管理考勤，定位工地员工");
            viewHolder.score1.setVisibility(View.VISIBLE);
            viewHolder.score2.setVisibility(View.VISIBLE);
            viewHolder.score3.setVisibility(View.VISIBLE);
            viewHolder.score4.setVisibility(View.VISIBLE);
            viewHolder.score5.setVisibility(View.INVISIBLE);
        }else if (position == 8){
            viewHolder.tv_commentName.setText(appName);
            viewHolder.tv_commentScore.setText("5分");
            viewHolder.tv_commentContent.setText("一年接一单就知足啦，我们做装修的，比较喜欢他们的，管理考勤功能，签到打卡，定位业务员");
            viewHolder.score1.setVisibility(View.VISIBLE);
            viewHolder.score2.setVisibility(View.VISIBLE);
            viewHolder.score3.setVisibility(View.VISIBLE);
            viewHolder.score4.setVisibility(View.VISIBLE);
            viewHolder.score5.setVisibility(View.VISIBLE);
        }else if (position == 9){
            viewHolder.tv_commentName.setText(appName);
            viewHolder.tv_commentScore.setText("5分");
            viewHolder.tv_commentContent.setText("全部转移到线上，老板天天，在外旅游，因为他可以时时监控：交易、财务、考勤、签字、签批，天天压力好大啊");
            viewHolder.score1.setVisibility(View.VISIBLE);
            viewHolder.score2.setVisibility(View.VISIBLE);
            viewHolder.score3.setVisibility(View.VISIBLE);
            viewHolder.score4.setVisibility(View.VISIBLE);
            viewHolder.score5.setVisibility(View.VISIBLE);
        }


        return convertView;

    }



    private class ViewHolder {

        TextView tv_commentName;
        TextView tv_commentScore;
        TextView tv_commentContent;
        TextView tv_commentTime;
        ImageView score1;
        ImageView score2;
        ImageView score3;
        ImageView score4;
        ImageView score5;

    }

}
