package com.sangu.apptongji.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.AppDownDetailActivity;
import com.sangu.apptongji.main.activity.SouSuoAdvertClickActivity;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.moments.MomentsPublishActivity;
import com.sangu.apptongji.widget.CircleImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class UserNewsAdapter extends BaseAdapter {

    private LayoutInflater mIndlater;
    private List<UserAll> data = null;
    private Context context;
    String lat;
    String lng;


    public UserNewsAdapter(List<UserAll> data, Context context, String lat, String lng) {

        mIndlater = LayoutInflater.from(context);
        this.context = context;

        this.lat = lat;
        this.lng = lng;
        this.data = data;

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

        ViewHolder holder = null;

        if (convertView == null){

            convertView = mIndlater.inflate(R.layout.item_usernewadapter, parent, false);

            holder = new ViewHolder();

            holder.rl_sex = (RelativeLayout) convertView.findViewById(R.id.rl_sex);
            holder.card = (LinearLayout) convertView.findViewById(R.id.card);
            holder.ll_one = (LinearLayout) convertView.findViewById(R.id.ll_one);
            holder.ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
            holder.ll_three = (LinearLayout) convertView.findViewById(R.id.ll_three);
            holder.ll_four = (LinearLayout) convertView.findViewById(R.id.ll_four);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            holder.tvBao1 = (TextView) convertView.findViewById(R.id.tv_zy1_bao);
            holder.tvBao2 = (TextView) convertView.findViewById(R.id.tv_zy2_bao);
            holder.tvBao3 = (TextView) convertView.findViewById(R.id.tv_zy3_bao);
            holder.tvBao4 = (TextView) convertView.findViewById(R.id.tv_zy4_bao);
            holder.ivZYTP1 = (TextView) convertView.findViewById(R.id.iv_zy1_tupian);
            holder.ivZYTP2 = (TextView) convertView.findViewById(R.id.iv_zy2_tupian);
            holder.ivZYTP3 = (TextView) convertView.findViewById(R.id.iv_zy3_tupian);
            holder.ivZYTP4 = (TextView) convertView.findViewById(R.id.iv_zy4_tupian);
            holder.tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
            holder.tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            holder.tv_company_count = (TextView) convertView.findViewById(R.id.tv_company_count);
            //holder.tvTime = (TextView) convertView.findViewById(R.id.tv_date);
            holder.ivHead = (CircleImageView) convertView.findViewById(R.id.iv_head);
            holder.ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.tvQianming = (TextView) convertView.findViewById(R.id.tv_qianming);
            holder.tvProject1 = (TextView) convertView.findViewById(R.id.tv_project_one);
            holder.tvProject2 = (TextView) convertView.findViewById(R.id.tv_project_two);
            holder.tvProject3 = (TextView) convertView.findViewById(R.id.tv_project_three);
            holder.tvProject4 = (TextView) convertView.findViewById(R.id.tv_project_four);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.image_advert = (ImageView) convertView.findViewById(R.id.image_advert);

            convertView.setTag(holder);

        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        UserAll allUser = data.get(position);
        if (allUser == null || data.size() == 0) {
            data.remove(position);
            this.notifyDataSetChanged();
        }
        String shareRed = allUser.getShareRed();
        String friendsNumber = allUser.getFriendsNumber();
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }

        String uName =  allUser.getuName();

        holder.tvName.setText(uName);
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            holder.tvName.setTextColor(Color.RED);
        }else {
            holder.tvName.setTextColor(Color.BLACK);
        }
        String nianLing = TextUtils.isEmpty(allUser.getuAge()) ? "**" : allUser.getuAge();
        if (nianLing==null||nianLing.equalsIgnoreCase("null")||"".equals(nianLing)){
            nianLing = "**";
        }
        holder.tvNianLing.setText(nianLing);
        String company = TextUtils.isEmpty(allUser.getuCompany()) ? "暂未加入企业" : allUser.getuCompany();
        String uNation = allUser.getuNation();
        String resv5 = allUser.getResv5();
        String resv6 = allUser.getResv6();
        if (company == null || company.equals("")) {
            company = "暂未加入企业";
        }
        if ("00".equals(resv6)&&!"1".equals(uNation)){
            company = "暂未加入企业";
        }
        if (resv5==null||"".equals(resv5)){
            company = "暂未加入企业";
        }
        try {
            company = URLDecoder.decode(company, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String member = allUser.getMemberNumber();
        if (member==null||"".equals(member)){
            member = "0";
        }
//        if (!company.equals("暂未加入企业")){
//            holder.tv_company_count.setVisibility(View.VISIBLE);
//        }else {
//            holder.tv_company_count.setVisibility(View.INVISIBLE);
//        }

        holder.tv_company_count.setVisibility(View.VISIBLE);
        holder.tv_company_count.setText("("+member+"人"+")");

        holder.tvCompany.setTextColor(Color.parseColor("#FF0000"));
        holder.tvCompany.setText(allUser.getMessageOrderCount()+" / "+allUser.getMessageOrderAll());

        holder.tvProject1.setText(allUser.getpL1());
        holder.tvProject2.setText(allUser.getpL2());
        holder.tvProject3.setText(allUser.getpL3());
        holder.tvProject4.setText(allUser.getpL4());
        String image1 = allUser.getImage1();
        String image2 = allUser.getImage2();
        String image3 = allUser.getImage3();
        String image4 = allUser.getImage4();
        String margan1 = allUser.getMargen1();
        String margan2 = allUser.getMargen2();
        String margan3 = allUser.getMargen3();
        String margan4 = allUser.getMargen4();
        if (allUser.getpL1()==null||allUser.getpL1().equals("")){
            holder.ll_one.setVisibility(View.GONE);
        }else {
            holder.ll_one.setVisibility(View.VISIBLE);
        }
        if (allUser.getpL2()==null||allUser.getpL2().equals("")){
            holder.ll_two.setVisibility(View.GONE);
        }else {
            holder.ll_two.setVisibility(View.VISIBLE);
        }
        if (allUser.getpL3()==null||allUser.getpL3().equals("")){
            holder.ll_three.setVisibility(View.GONE);
        }else {
            holder.ll_three.setVisibility(View.VISIBLE);
        }
        if (allUser.getpL4()==null||allUser.getpL4().equals("")){
            holder.ll_four.setVisibility(View.GONE);
        }else {
            holder.ll_four.setVisibility(View.VISIBLE);
        }
        if (image1!=null&&!"".equals(image1)) {
            holder.ivZYTP1.setVisibility(View.VISIBLE);
        }else {
            holder.ivZYTP1.setVisibility(View.GONE);
        }

        Double allMargin = 0.0;
        if (margan1!=null) {
            if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                holder.tvBao1.setVisibility(View.VISIBLE);
                allMargin = allMargin + Double.valueOf(margan1);
            }else {
                holder.tvBao1.setVisibility(View.GONE);
            }
        }
        if (image2!=null&&!"".equals(image2)) {
            holder.ivZYTP2.setVisibility(View.VISIBLE);

        }else {
            holder.ivZYTP2.setVisibility(View.GONE);
        }
        if (margan2!=null) {
            if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                holder.tvBao2.setVisibility(View.VISIBLE);
                allMargin = allMargin + Double.valueOf(margan2);
            }else {
                holder.tvBao2.setVisibility(View.GONE);
            }
        }
        if (image3!=null&&!"".equals(image3)) {
            holder.ivZYTP3.setVisibility(View.VISIBLE);
        }else {
            holder.ivZYTP3.setVisibility(View.GONE);
        }
        if (margan3!=null) {
            if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                holder.tvBao3.setVisibility(View.VISIBLE);
                allMargin = allMargin + Double.valueOf(margan3);
            }else {
                holder.tvBao3.setVisibility(View.GONE);
            }
        }
        if (image4!=null&&!"".equals(image4)) {
            holder.ivZYTP4.setVisibility(View.VISIBLE);
        }else {
            holder.ivZYTP4.setVisibility(View.GONE);
        }
        if (margan4!=null) {
            if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                holder.tvBao4.setVisibility(View.VISIBLE);
                allMargin = allMargin + Double.valueOf(margan4);
            }else {
                holder.tvBao4.setVisibility(View.GONE);
            }
        }

        holder.tv_company_count.setTextColor(Color.parseColor("#FF0000"));
        if (allMargin > 99){

            holder.tv_company_count.setText("质保"+allMargin+"元");

        }else {
            holder.tv_company_count.setText("质保0元");
        }


        String head = TextUtils.isEmpty(allUser.getuImage()) ? "" : allUser.getuImage();
        if (head.length() > 40) {
            holder.tvTitleA.setVisibility(View.INVISIBLE);
            holder.ivHead.setVisibility(View.VISIBLE);
            String[] orderProjectArray = head.split("\\|");
            head = orderProjectArray[0];
        }
        if (!(head.equals("") || head.equals(null))) {
            holder.tvTitleA.setVisibility(View.INVISIBLE);
            holder.ivHead.setVisibility(View.VISIBLE);
            //ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+head,holder.ivHead, DemoApplication.mOptions);
            Glide.with(context).load(FXConstant.URL_AVATAR+head).into(holder.ivHead);

        } else {
            holder.ivHead.setVisibility(View.INVISIBLE);
            holder.tvTitleA.setVisibility(View.VISIBLE);
            holder.tvTitleA.setText(TextUtils.isEmpty(allUser.getuName()) ? allUser.getuId() : allUser.getuName());
        }
        if (("00").equals(allUser.getuSex())) {
            holder.ivSex.setImageResource(R.drawable.nv);
            //保 255 62 74  图 255 170 76
            holder.tvNianLing.setBackgroundColor(Color.rgb(234,121,219));
            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
        } else {
            holder.ivSex.setImageResource(R.drawable.nan);
            holder.tvNianLing.setBackgroundResource(R.color.accent_blue);
            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
        }
        String resv3 = TextUtils.isEmpty(allUser.getResv3()) ? "" : allUser.getResv3();
        String resv1 = TextUtils.isEmpty(allUser.getResv1()) ? "" : allUser.getResv1();
        String resv2 = TextUtils.isEmpty(allUser.getResv2()) ? "" : allUser.getResv2();
        if (lat!=null&&lng!=null) {
            if (!("".equals(lat) || "".equals(lng) || resv1.equals("") || resv2.equals(""))) {
                double latitude1 = Double.valueOf(lat);
                double longitude1 = Double.valueOf(lng);
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1, longitude1);
                double distance = DistanceUtil.getDistance(ll, ll1);
                double dou = distance / 1000;
                String str = String.format("%.2f", dou);//format 返回的是字符串
                if (str!=null&&dou>=10000){
                    holder.tvDistance.setText("隐藏");
                }else {
                    holder.tvDistance.setText(str + "km");
                }
            } else {
                holder.tvDistance.setText("3km以外");
            }
        }else {
            holder.tvDistance.setText("3km以外");
        }
//        String sign = allUser.getuSignaTure();
//        if (sign==null||"".equals(sign)){
//            sign = "未设置简介";
//        }

        holder.tvQianming.setText("登录时间："+allUser.getResv3());

        return convertView;

    }


    private class ViewHolder {

        RelativeLayout rl_sex;
        TextView tvName,tvNianLing;
        TextView tvCompany,tv_company_count;
        TextView tvTitleA,tvBao1,tvBao2,tvBao3,tvBao4,tvBu1,tvBu2,tvBu3,tvBu4,ivZYTP1,ivZYTP2,ivZYTP3,ivZYTP4;
        TextView tvDistance;
        ImageView ivSex;
        CircleImageView ivHead;
        TextView tvProject1,tvProject2,tvProject3,tvProject4;
        TextView tvQianming;
        ImageView image_advert;
        LinearLayout ll_one,ll_two,ll_three,ll_four,card;

    }

}
