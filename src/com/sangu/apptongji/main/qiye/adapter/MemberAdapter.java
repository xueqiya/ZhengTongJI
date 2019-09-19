package com.sangu.apptongji.main.qiye.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.qiye.entity.MemberInfo;
import com.sangu.apptongji.widget.CircleImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-01-03.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private List<MemberInfo> data = null;
    private Context context;
    public MyItemClickListener mItemClickListener;
    public MyItemLongClickListener mItemLongClickListener;

    public MemberAdapter(List<MemberInfo> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_fragment,parent,false),mItemClickListener,mItemLongClickListener);
        return viewHolder;
    }
    public String getcurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
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
        String locationState = allUser.getLocationState();
        holder.tvName.setText(TextUtils.isEmpty(allUser.getuName()) ? allUser.getuLoginId() : allUser.getuName());
        String shareRed = allUser.getShareRed();
        String friendsNumber = allUser.getFriendsNumber();
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
        if (company.equals("") || company == null) {
            company = "暂未加入企业";
        }
        try {
            company = URLDecoder.decode(company, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.tvCompany.setText(company);
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
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + head,holder.ivHead,DemoApplication.mOptions);
        } else {
            holder.ivHead.setVisibility(View.INVISIBLE);
            holder.tvTitleA.setVisibility(View.VISIBLE);
            holder.tvTitleA.setText(TextUtils.isEmpty(allUser.getuName()) ? allUser.getuLoginId() : allUser.getuName());
        }
        String signTime = allUser.getSignInfo_signTime();
        if (signTime.length()>0){
            signTime = signTime.substring(0,8);
        }
        String lateTimes = TextUtils.isEmpty(allUser.getDataLateTimes())?"0":allUser.getDataLateTimes();
        String kaiqinTimes = TextUtils.isEmpty(allUser.getDataDayresv1())?"0":allUser.getDataDayresv1();
        String leaveTimes = allUser.getDataLeaveTimes();
        String orderTimes = allUser.getDataOrderTimes();
        String totalTransAmount = allUser.getDataTotalTransAmount();
        String dayTransAmount = allUser.getDataDayTransAmount();
        String nowTime = getcurrentTime().substring(0,8);
        String remark = allUser.getSignInfo_remark();
        String signState = allUser.getSignInfo_signState();
        if (kaiqinTimes==null||kaiqinTimes.equals("null")){
            kaiqinTimes = "0";
        }
        if (lateTimes==null||lateTimes.equals("null")){
            lateTimes = "0";
        }
        holder.tv_kaoqin_cishu.setText(lateTimes+"次");
        holder.tvChidaoCount.setText(kaiqinTimes+"次");
        holder.tvQingjiaCount.setText(leaveTimes+"次");
        holder.tvPaidanCount.setText(orderTimes+"次");
        holder.tvJiaoyie.setText("￥"+totalTransAmount);
        holder.tvJiaoyie_jinri.setText(dayTransAmount);
        if (nowTime.equals(signTime)){
            holder.ivHead.setVisibility(View.INVISIBLE);
            holder.tvTitleA.setVisibility(View.VISIBLE);
            if ("02".equals(remark)){
                holder.tvTitleA.setText("迟到");
            }else if ("01".equals(remark)){
                if ("01".equals(signState)) {
                    holder.tvTitleA.setText("上班");
                }else if ("00".equals(signState)){
                    holder.tvTitleA.setText("下班");
                }
            }else if ("03".equals(remark)){
                holder.tvTitleA.setText("请假");
            }else if ("04".equals(remark)){
                holder.tvTitleA.setText("出行");
            }else if ("05".equals(remark)){
                holder.tvTitleA.setText("离岗");
            }
        }
        if (("00").equals(allUser.getuSex())) {
            holder.ivSex.setImageResource(R.drawable.nv);
            //保 255 62 74  图 255 170 76
            holder.tvNianLing.setBackgroundColor(Color.rgb(255,73,204));
            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
        } else {
            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
            holder.ivSex.setImageResource(R.drawable.nan);
        }
        String resv3 = TextUtils.isEmpty(allUser.getResv3()) ? "" : allUser.getResv3();
        String resv1 = TextUtils.isEmpty(allUser.getResv1()) ? "" : allUser.getResv1();
        String resv2 = TextUtils.isEmpty(allUser.getResv2()) ? "" : allUser.getResv2();
        double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) ? "34.762711" : DemoApplication.getInstance().getCurrentLat());
        double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) ? "113.744531" : DemoApplication.getInstance().getCurrentLng());
        Log.e("membeiada",allUser.getuLoginId());
        if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
            final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
            LatLng ll = new LatLng(latitude1,longitude1);
            double distance = DistanceUtil.getDistance(ll,ll1);
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
        if (locationState.equals("00")){
            holder.tvDistance.setText("隐藏");
        }
        final String loginId = allUser.getuLoginId();
        holder.tvTitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,loginId));
            }
        });
        holder.ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,loginId));
            }
        });
    }
    @Override
    public int getItemCount() {
        return (data == null || data.size() == 0)?0:data.size();
    }


    public interface MyItemClickListener {
        void onItemClick(View view,int position);
    }
    public interface MyItemLongClickListener {
        void onItemLongClick(View view,int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        RelativeLayout rl_sex;
        TextView tvName,tvNianLing,tv_chidao;
        TextView tvCompany;
        TextView tvTitleA,tvChidaoCount,tvPaidanCount,tvQingjiaCount,tvJiaoyie,tvJiaoyie_jinri,tv_kaoqin_cishu;
        TextView tvDistance;
        ImageView ivSex;
        CircleImageView ivHead;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public ViewHolder(View convertView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(convertView);
            rl_sex = (RelativeLayout) convertView.findViewById(R.id.rl_sex);
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tv_chidao = (TextView) convertView.findViewById(R.id.tv_chidao);
            tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            tv_kaoqin_cishu = (TextView) convertView.findViewById(R.id.tv_kaoqin_cishu);
            tvChidaoCount = (TextView) convertView.findViewById(R.id.tv_chidao_cishu);
            tvPaidanCount = (TextView) convertView.findViewById(R.id.tv_paidan_cishu);
            tvQingjiaCount = (TextView) convertView.findViewById(R.id.tv_qingjia_cishu);
            tvJiaoyie = (TextView) convertView.findViewById(R.id.tv_jiaoyie_jine);
            tvJiaoyie_jinri = (TextView) convertView.findViewById(R.id.tv_jinri_jiaoyie_jine);
            tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
            tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            ivHead = (CircleImageView) convertView.findViewById(R.id.iv_head);
            ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
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
    }
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.mItemLongClickListener = listener;
    }
}
