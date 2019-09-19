package com.sangu.apptongji.main.qiye.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.qiye.entity.KaoqinInfo;
import com.sangu.apptongji.widget.CircleImageView;

import java.util.List;

/**
 * Created by user on 2016/9/1.
 */

public class KaoqinAdapter extends BaseAdapter{
    private List<KaoqinInfo> data;
    private Context context;
    private LayoutInflater inflater;

    public KaoqinAdapter(List<KaoqinInfo> data, Context context) {
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
            convertView = inflater.inflate(R.layout.list_item_kaoqin,null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            holder.tv_zhuangtai = (TextView) convertView.findViewById(R.id.tv_dingdanzhuangtai);
            holder.tvkaoqinTime = (TextView) convertView.findViewById(R.id.tv_from);
            holder.ivHead = (CircleImageView) convertView.findViewById(R.id.iv_head);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        KaoqinInfo orderInfo = (KaoqinInfo) getItem(position);
        holder.tv_zhuangtai.setVisibility(View.INVISIBLE);
        holder.tvName.setText(TextUtils.isEmpty(orderInfo.getuName())?"":orderInfo.getuName());
        String kaoqinTime = TextUtils.isEmpty(orderInfo.getClockTime())?"":orderInfo.getClockTime();
        kaoqinTime = kaoqinTime.substring(0, 4) + "-" + kaoqinTime.substring(4, 6) + "-" + kaoqinTime.substring(6, 8) + " "
                + kaoqinTime.substring(8, 10) + ":" + kaoqinTime.substring(10, 12);
        holder.tvkaoqinTime.setText(kaoqinTime);
        String head = TextUtils.isEmpty(orderInfo.getuImage())?"":orderInfo.getuImage();
        final String hxid = orderInfo.getUserId();
        if (!(head=="")){
            String[] orderProjectArray = head.split("\\|");
            head = orderProjectArray[0];
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + head,holder.ivHead, DemoApplication.mOptions);
            holder.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserDetailsActivity.class);
                    intent.putExtra(FXConstant.JSON_KEY_HXID,hxid);
                    context.startActivity(intent);
                }
            });
        }else {
            holder.ivHead.setVisibility(View.INVISIBLE);
            holder.tvTitleA.setVisibility(View.VISIBLE);
            holder.tvTitleA.setText(TextUtils.isEmpty(orderInfo.getuName())?"":orderInfo.getuName());
            holder.tvTitleA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserDetailsActivity.class);
                    intent.putExtra(FXConstant.JSON_KEY_HXID,hxid);
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }
    class ViewHolder {
        TextView tvName,tvTitleA,tv_zhuangtai;
        TextView tvkaoqinTime;
        CircleImageView ivHead;
    }

}
