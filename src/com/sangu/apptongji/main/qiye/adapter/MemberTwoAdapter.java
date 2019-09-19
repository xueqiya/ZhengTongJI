package com.sangu.apptongji.main.qiye.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.qiye.entity.MemberInfo;
import com.sangu.apptongji.widget.CircleImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Administrator on 2017-01-03.
 */

public class MemberTwoAdapter extends RecyclerView.Adapter<MemberTwoAdapter.ViewHolder> {
    private List<MemberInfo> data = null;
    private Context context;
    public MyItemClickListener mItemClickListener;
    public MyItemLongClickListener mItemLongClickListener;

    public MemberTwoAdapter(List<MemberInfo> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_fragment,parent,false),mItemClickListener,mItemLongClickListener);
        return viewHolder;
//        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_fragment,parent,false),mItemClickListener,mItemLongClickListener);
//        return viewHolder;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        final MemberInfo allUser = data.get(position);
        if (allUser == null || data.size() == 0) {
            data.remove(position);
            this.notifyDataSetChanged();
        }
        holder.tvName.setText(TextUtils.isEmpty(allUser.getuName()) ? allUser.getuLoginId() : allUser.getuName());
        String shareRed = allUser.getShareRed();
        final String friendsNumber = allUser.getFriendsNumber();
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            holder.tvName.setTextColor(Color.RED);
        }else {
            holder.tvName.setTextColor(Color.BLACK);
        }
        String nianLing = TextUtils.isEmpty(allUser.getuAge()) ? "27" : allUser.getuAge();
        holder.tvNianLing.setText(nianLing);
        String company = TextUtils.isEmpty(allUser.getuCompany()) ? "暂未加入企业" : allUser.getuCompany();
        if (company == null || company.equals("")) {
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
        if (!company.equals("暂未加入企业")){
            holder.tv_company_count.setVisibility(View.VISIBLE);
        }else {
            holder.tv_company_count.setVisibility(View.INVISIBLE);
        }
        holder.tv_company_count.setText("("+member+")");
        holder.tvCompany.setText(company);
        holder.tvProject1.setText(allUser.getUpName1());
        holder.tvProject2.setText(allUser.getUpName2());
        holder.tvProject3.setText(allUser.getUpName3());
        holder.tvProject4.setText(allUser.getUpName4());
        String image1 = allUser.getZy1Image();
        String image2 = allUser.getZy2Image();
        String image3 = allUser.getZy3Image();
        String image4 = allUser.getZy4Image();
        String margan1 = allUser.getMargin1();
        String margan2 = allUser.getMargin2();
        String margan3 = allUser.getMargin3();
        String margan4 = allUser.getMargin4();
        if (allUser.getUpName1()==null||allUser.getUpName1().equals("")){
            holder.ll_one.setVisibility(View.GONE);
        }else {
            holder.ll_one.setVisibility(View.VISIBLE);
        }
        if (allUser.getUpName2()==null||allUser.getUpName2().equals("")){
            holder.ll_two.setVisibility(View.GONE);
        }else {
            holder.ll_two.setVisibility(View.VISIBLE);
        }
        if (allUser.getUpName3()==null||allUser.getUpName3().equals("")){
            holder.ll_three.setVisibility(View.GONE);
        }else {
            holder.ll_three.setVisibility(View.VISIBLE);
        }
        if (allUser.getUpName4()==null||allUser.getUpName4().equals("")){
            holder.ll_four.setVisibility(View.GONE);
        }else {
            holder.ll_four.setVisibility(View.VISIBLE);
        }
        if (!"".equals(image1)&&image1!=null) {
            holder.ivZYTP1.setVisibility(View.VISIBLE);
        }else {
            holder.ivZYTP1.setVisibility(View.GONE);
        }
        if (margan1!=null) {
            if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                holder.tvBao1.setVisibility(View.VISIBLE);
            }else {
                holder.tvBao1.setVisibility(View.GONE);
            }
        }
        if (!"".equals(image2)&&image2!=null) {
            holder.ivZYTP2.setVisibility(View.VISIBLE);
        }else {
            holder.ivZYTP2.setVisibility(View.GONE);
        }
        if (margan2!=null) {
            if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                holder.tvBao2.setVisibility(View.VISIBLE);
            }else {
                holder.tvBao2.setVisibility(View.GONE);
            }
        }
        if (!"".equals(image3)&&image3!=null) {
            holder.ivZYTP3.setVisibility(View.VISIBLE);
        }else {
            holder.ivZYTP3.setVisibility(View.GONE);
        }
        if (margan3!=null) {
            if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                holder.tvBao3.setVisibility(View.VISIBLE);
            }else {
                holder.tvBao3.setVisibility(View.GONE);
            }
        }
        if (!"".equals(image4)&&image4!=null) {
            holder.ivZYTP4.setVisibility(View.VISIBLE);
        }else {
            holder.ivZYTP4.setVisibility(View.GONE);
        }
        if (margan4!=null) {
            if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                holder.tvBao4.setVisibility(View.VISIBLE);
            }else {
                holder.tvBao4.setVisibility(View.GONE);
            }
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
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + head,holder.ivHead, DemoApplication.mOptions);
        } else {
            holder.ivHead.setVisibility(View.INVISIBLE);
            holder.tvTitleA.setVisibility(View.VISIBLE);
            holder.tvTitleA.setText(TextUtils.isEmpty(allUser.getuName()) ? allUser.getuLoginId() : allUser.getuName());
        }
        if ("00".equals(allUser.getuSex())) {
            holder.ivSex.setImageResource(R.drawable.nv);
            //保 255 62 74  图 255 170 76
            holder.tvNianLing.setBackgroundColor(Color.rgb(234,121,219));
            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
        } else {
            holder.ivSex.setImageResource(R.drawable.nan);
            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
        }
        String resv3 = TextUtils.isEmpty(allUser.getResv3()) ? "" : allUser.getResv3();
        String resv1 = TextUtils.isEmpty(allUser.getResv1()) ? "" : allUser.getResv1();
        String resv2 = TextUtils.isEmpty(allUser.getResv2()) ? "" : allUser.getResv2();
        double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
        double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());
        if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
            LatLng ll = new LatLng(latitude1,longitude1);
            double distance = DistanceUtil.getDistance(ll, new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1)));
            double dou = distance / 1000;
            String str = String.format("%.2f",dou);//format 返回的是字符串
            if (str!=null&&dou>=10000){
                holder.tvDistance.setText("隐藏");
            }else {
                holder.tvDistance.setText(str + "km");
            }
        } else {
            holder.tvDistance.setText("3km之内");
        }
        String sign = allUser.getuSignaTure();
        if (sign==null||"".equals(sign)){
            sign = "未设置简介";
        }
        holder.tvQianming.setText(sign);

//        holder.setIsRecyclable(false);
//        final MemberInfo allUser = data.get(position);
//        if (allUser == null || data.size() == 0) {
//            data.remove(position);
//            this.notifyDataSetChanged();
//        }
//        String locationState = allUser.getLocationState();
//        holder.tvName.setText(TextUtils.isEmpty(allUser.getuName()) ? allUser.getuLoginId() : allUser.getuName());
//        String nianLing = TextUtils.isEmpty(allUser.getuAge()) ? "27" : allUser.getuAge();
//        holder.tvNianLing.setText(nianLing);
//        String company = TextUtils.isEmpty(allUser.getuCompany()) ? "暂未加入企业" : allUser.getuCompany();
//        if (company.equals("") || company == null) {
//            company = "暂未加入企业";
//        }
//        try {
//            company = URLDecoder.decode(company, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        holder.tvCompany.setText(company);
//        String head = TextUtils.isEmpty(allUser.getuImage()) ? "" : allUser.getuImage();
//        if (head.length() > 40) {
//            holder.tvTitleA.setVisibility(View.INVISIBLE);
//            holder.ivHead.setVisibility(View.VISIBLE);
//            String[] orderProjectArray = head.split("\\|");
//            head = orderProjectArray[0];
//        }
//        if (!(head.equals("") || head.equals(null))) {
//            holder.tvTitleA.setVisibility(View.INVISIBLE);
//            holder.ivHead.setVisibility(View.VISIBLE);
//            holder.ivHead.setImageURI(Uri.parse(FXConstant.URL_AVATAR
//                    + head));
//        } else {
//            holder.ivHead.setVisibility(View.INVISIBLE);
//            holder.tvTitleA.setVisibility(View.VISIBLE);
//            holder.tvTitleA.setText(TextUtils.isEmpty(allUser.getuName()) ? allUser.getuLoginId() : allUser.getuName());
//        }
//        String signTime = allUser.getSignInfo_signTime();
//        if (signTime.length()>0){
//            signTime = signTime.substring(0,8);
//        }
//        String lateTimes = TextUtils.isEmpty(allUser.getDataLateTimes())?"0":allUser.getDataLateTimes();
//        String kaiqinTimes = TextUtils.isEmpty(allUser.getDataDayresv1())?"0":allUser.getDataDayresv1();
//        String leaveTimes = allUser.getDataLeaveTimes();
//        String orderTimes = allUser.getDataOrderTimes();
//        String totalTransAmount = allUser.getDataTotalTransAmount();
//        String dayTransAmount = allUser.getDataDayTransAmount();
//        String nowTime = getcurrentTime().substring(0,8);
//        String remark = allUser.getSignInfo_remark();
//        String signState = allUser.getSignInfo_signState();
//        if (kaiqinTimes==null||kaiqinTimes.equals("null")){
//            kaiqinTimes = "0";
//        }
//        if (lateTimes==null||lateTimes.equals("null")){
//            lateTimes = "0";
//        }
//        holder.tv_kaoqin_cishu.setText(lateTimes+"次");
//        holder.tvChidaoCount.setText(kaiqinTimes+"次");
//        holder.tvQingjiaCount.setText(leaveTimes+"次");
//        holder.tvPaidanCount.setText(orderTimes+"次");
//        holder.tvJiaoyie.setText("￥"+totalTransAmount);
//        holder.tvJiaoyie_jinri.setText(dayTransAmount);
//        if (nowTime.equals(signTime)){
//            holder.ivHead.setVisibility(View.INVISIBLE);
//            holder.tvTitleA.setVisibility(View.VISIBLE);
//            if ("02".equals(remark)){
//                holder.tvTitleA.setText("迟到");
//            }else if ("01".equals(remark)){
//                if ("01".equals(signState)) {
//                    holder.tvTitleA.setText("上班");
//                }else if ("00".equals(signState)){
//                    holder.tvTitleA.setText("下班");
//                }
//            }else if ("03".equals(remark)){
//                holder.tvTitleA.setText("请假");
//            }else if ("04".equals(remark)){
//                holder.tvTitleA.setText("出行");
//            }else if ("05".equals(remark)){
//                holder.tvTitleA.setText("离岗");
//            }
//        }
//        if (("00").equals(allUser.getuSex())) {
//            holder.ivSex.setImageResource(R.drawable.nv);
//            //保 255 62 74  图 255 170 76
//            holder.rl_sex.setBackgroundColor(Color.rgb(255,73,204));
//            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
//        } else {
//            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
//            holder.ivSex.setImageResource(R.drawable.nan);
//        }
//        String resv3 = TextUtils.isEmpty(allUser.getResv3()) ? "" : allUser.getResv3();
//        String resv1 = TextUtils.isEmpty(allUser.getResv1()) ? "" : allUser.getResv1();
//        String resv2 = TextUtils.isEmpty(allUser.getResv2()) ? "" : allUser.getResv2();
//        double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
//        double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());
//        if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
//            LatLng ll = new LatLng(latitude1,longitude1);
//            double distance = DistanceUtil.getDistance(ll, new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1)));
//            double dou = distance / 1000;
//            String str = String.format("%.1f",dou);//format 返回的是字符串
//            holder.tvDistance.setText(str + "km");
//        } else {
//            holder.tvDistance.setText("3km之内");
//        }
//        if (locationState.equals("00")){
//            holder.tvDistance.setText("隐藏");
//        }
//        final String loginId = allUser.getuLoginId();
//        holder.tvTitleA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,loginId));
//            }
//        });
//        holder.ivHead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,loginId));
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return (data == null || data.size() == 0)?0:data.size();
    }


    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface MyItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        RelativeLayout rl_sex;
        TextView tvName,tvNianLing;
        TextView tvCompany,tv_company_count;
        TextView tvTitleA,tvBao1,tvBao2,tvBao3,tvBao4,tvBu1,tvBu2,tvBu3,tvBu4,ivZYTP1,ivZYTP2,ivZYTP3,ivZYTP4;
        TextView tvDistance;
        ImageView ivSex;
        CircleImageView ivHead;
        TextView tvProject1,tvProject2,tvProject3,tvProject4;
        TextView tvQianming;
        LinearLayout ll_one,ll_two,ll_three,ll_four;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public ViewHolder(View convertView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(convertView);
            rl_sex = (RelativeLayout) convertView.findViewById(R.id.rl_sex);
            ll_one = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
            ll_three = (LinearLayout) convertView.findViewById(R.id.ll_three);
            ll_four = (LinearLayout) convertView.findViewById(R.id.ll_four);
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            tvBao1 = (TextView) convertView.findViewById(R.id.tv_zy1_bao);
            tvBao2 = (TextView) convertView.findViewById(R.id.tv_zy2_bao);
            tvBao3 = (TextView) convertView.findViewById(R.id.tv_zy3_bao);
            tvBao4 = (TextView) convertView.findViewById(R.id.tv_zy4_bao);
            ivZYTP1 = (TextView) convertView.findViewById(R.id.iv_zy1_tupian);
            ivZYTP2 = (TextView) convertView.findViewById(R.id.iv_zy2_tupian);
            ivZYTP3 = (TextView) convertView.findViewById(R.id.iv_zy3_tupian);
            ivZYTP4 = (TextView) convertView.findViewById(R.id.iv_zy4_tupian);
            tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
            tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            tv_company_count = (TextView) convertView.findViewById(R.id.tv_company_count);
            //holder.tvTime = (TextView) convertView.findViewById(R.id.tv_date);
            ivHead = (CircleImageView) convertView.findViewById(R.id.iv_head);
            ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
            tvQianming = (TextView) convertView.findViewById(R.id.tv_qianming);
            tvProject1 = (TextView) convertView.findViewById(R.id.tv_project_one);
            tvProject2 = (TextView) convertView.findViewById(R.id.tv_project_two);
            tvProject3 = (TextView) convertView.findViewById(R.id.tv_project_three);
            tvProject4 = (TextView) convertView.findViewById(R.id.tv_project_four);
            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            this.mListener=listener;
            this.mLongClickListener=longClickListener;
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mLongClickListener != null){
                mLongClickListener.onItemLongClick(v,getPosition());
            }
            return true;
        }
//        RelativeLayout rl_sex;
//        TextView tvName,tvNianLing,tv_chidao;
//        TextView tvCompany;
//        TextView tvTitleA,tvChidaoCount,tvPaidanCount,tvQingjiaCount,tvJiaoyie,tvJiaoyie_jinri,tv_kaoqin_cishu;
//        TextView tvDistance;
//        ImageView ivSex;
//        SimpleDraweeView ivHead;
//        private MyItemClickListener mListener;
//        private MyItemLongClickListener mLongClickListener;
//
//        public ViewHolder(View convertView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
//            super(convertView);
//            rl_sex = (RelativeLayout) convertView.findViewById(R.id.rl_sex);
//            tvName = (TextView) convertView.findViewById(R.id.tv_name);
//            tv_chidao = (TextView) convertView.findViewById(R.id.tv_chidao);
//            tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
//            tv_kaoqin_cishu = (TextView) convertView.findViewById(R.id.tv_kaoqin_cishu);
//            tvChidaoCount = (TextView) convertView.findViewById(R.id.tv_chidao_cishu);
//            tvPaidanCount = (TextView) convertView.findViewById(R.id.tv_paidan_cishu);
//            tvQingjiaCount = (TextView) convertView.findViewById(R.id.tv_qingjia_cishu);
//            tvJiaoyie = (TextView) convertView.findViewById(R.id.tv_jiaoyie_jine);
//            tvJiaoyie_jinri = (TextView) convertView.findViewById(R.id.tv_jinri_jiaoyie_jine);
//            tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
//            tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
//            ivHead = (SimpleDraweeView) convertView.findViewById(R.id.iv_head);
//            ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
//            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
//            tv_chidao.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    context.startActivity(new Intent(context, KaoQinActivity.class));
//                }
//            });
//            tvChidaoCount.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    context.startActivity(new Intent(context, KaoQinActivity.class));
//                }
//            });
//            this.mListener=listener;
//            this.mLongClickListener=longClickListener;
//            convertView.setOnClickListener(this);
//            convertView.setOnLongClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            if(mListener != null){
//                mListener.onItemClick(v,getPosition());
//            }
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            if(mLongClickListener != null){
//                mLongClickListener.onItemLongClick(v,getPosition());
//            }
//            return true;
//        }
    }
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.mItemLongClickListener = listener;
    }
}
