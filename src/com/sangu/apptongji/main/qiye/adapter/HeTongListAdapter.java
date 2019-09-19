package com.sangu.apptongji.main.qiye.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.qiye.entity.HeTongInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Administrator on 2017-01-17.
 */

public class HeTongListAdapter extends BaseAdapter {
    private List<HeTongInfo> data;
    private Context context;
    private LayoutInflater inflater;

    public HeTongListAdapter(List<HeTongInfo> data, Context context) {
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
            convertView = inflater.inflate(R.layout.list_item_hetong,null);
            holder = new ViewHolder();
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            holder.tv_chengyuan = (TextView) convertView.findViewById(R.id.tv_chengyuan);
            holder.tv_hetongleixing = (TextView) convertView.findViewById(R.id.tv_hetongleixing);
            holder.tv_qiye = (TextView) convertView.findViewById(R.id.tv_qiye);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        HeTongInfo hetongInfo = (HeTongInfo) getItem(position);
        String agreement1 = TextUtils.isEmpty(hetongInfo.getAgreement1())?"":hetongInfo.getAgreement1();
        String agreement2 = TextUtils.isEmpty(hetongInfo.getAgreement2())?"":hetongInfo.getAgreement2();
        if (!"".equals(agreement1)){
            holder.tv_hetongleixing.setText("劳务合同");
        }
        if (!"".equals(agreement2)){
            holder.tv_hetongleixing.setText("加盟合同");
        }
        String company = TextUtils.isEmpty(hetongInfo.getuCompany())?"":hetongInfo.getuCompany();
        try {
            company = URLDecoder.decode(company,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.tv_qiye.setText(company);
        String time = TextUtils.isEmpty(hetongInfo.getCreateTime())?"":hetongInfo.getCreateTime();
        time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " "
                + time.substring(8, 10) + ":" + time.substring(10, 12);
        holder.tv_time.setText(time);
        String name = hetongInfo.getuName();
        holder.tv_chengyuan.setText(name);
        return convertView;
    }
    class ViewHolder {
        TextView tv_hetongleixing,tv_qiye;
        TextView tv_chengyuan,tv_time;
    }

}
