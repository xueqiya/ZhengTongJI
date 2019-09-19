package com.sangu.apptongji.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.widget.CircleImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by user on 2016/8/12.
 */

public class UserLogAdapter extends RecyclerView.Adapter<UserLogAdapter.ViewHolder> {
    private List<UserAll> data = null;
    private Context context;
    public MyItemClickListener mItemClickListener;
    public MyItemLongClickListener mItemLongClickListener;
    private int lastPosition = -1;
    String lat;
    String lng;
    private OnDeleteClickLister mDeleteClickListener;

    public UserLogAdapter(List<UserAll> data, Context context, String lat, String lng) {
        this.context = context;
        this.lat = lat;
        this.lng = lng;
        this.data = data;
    }

    public void appendData(List<UserAll> list,Context context) {//分页关键
        this.data.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_fragment_del, parent, false);
        return new ViewHolder(v,mItemClickListener,mItemLongClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //动画加载
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
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
        holder.tvName.setText(TextUtils.isEmpty(allUser.getuName()) ? allUser.getuId() : allUser.getuName());
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
        if (!company.equals("暂未加入企业")){
            holder.tv_company_count.setVisibility(View.VISIBLE);
        }else {
            holder.tv_company_count.setVisibility(View.INVISIBLE);
        }
        holder.tv_company_count.setText("("+member+"人"+")");
        holder.tvCompany.setText(company);
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
        if (margan1!=null) {
            if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                holder.tvBao1.setVisibility(View.VISIBLE);
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
        String sign = allUser.getuSignaTure();
        if (sign==null||"".equals(sign)){
            sign = "未设置简介";
        }
        holder.tvQianming.setText(sign);
//            if (!resv3.equals("")) {
//                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                Date curDate = new Date(System.currentTimeMillis());
//                String time = sDateFormat.format(curDate);
//                int currTime1 = Integer.valueOf(time.substring(5, 7));
//                String res = resv3.substring(5, 7);
//                int preTime1 = Integer.valueOf(res);
//                int d1 = currTime1 - preTime1;
//                if (d1 > 0) {
//                    holder.tvTime.setText(d1 + "月前");
//                }else {
//                    int currTime2 = Integer.valueOf(String.valueOf(time).substring(8, 10));
//                    int preTime2 = Integer.valueOf(allUser.getResv3().substring(8, 10));
//                    int d2 = currTime2 - preTime2;
//                    if (d2 > 0) {
//                        holder.tvTime.setText(d2 + "天前");
//                    }else  {
//                        int currTime3 = Integer.valueOf(String.valueOf(time).substring(11, 13));
//                        int preTime3 = Integer.valueOf(allUser.getResv3().substring(11, 13));
//                        int d3 = currTime3 - preTime3;
//                        if (d3 > 0) {
//                            holder.tvTime.setText(d3 + "小时前");
//                        } else  {
//                            holder.tvTime.setText("1小时前");
//                        }
//                    }
//                }
//            } else {
//                holder.tvTime.setText("2天前");
//            }
        holder.tvDelete.setTag(position);
        if (!holder.tvDelete.hasOnClickListeners()) {
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteClickListener != null) {
                        mDeleteClickListener.onDeleteClick(v, (Integer) v.getTag());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (data == null || data.size() == 0)?0:data.size();
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position, ImageView v1);
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
        LinearLayout ll_one,ll_two,ll_three,ll_four,card;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;
        TextView tvDelete;

        public ViewHolder(View convertView,MyItemClickListener listener,MyItemLongClickListener longClickListener) {
            super(convertView);
            rl_sex = (RelativeLayout) convertView.findViewById(R.id.rl_sex);
            card = (LinearLayout) convertView.findViewById(R.id.card);
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
            tvDelete=(TextView)convertView.findViewById(R.id.tv_delete);
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getPosition(),ivHead);
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

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
