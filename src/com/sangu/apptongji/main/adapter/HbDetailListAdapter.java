package com.sangu.apptongji.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
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
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.entity.MerDetail;
import com.sangu.apptongji.main.qiye.QiYeDetailsActivity;
import com.sangu.apptongji.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016-10-25.
 */

public class HbDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int GEREN_ITEM = 0;
    public static final int QIYE_ITEM = 1;
    private List<MerDetail> data;
    private Context context;
    public MyItemClickListener mItemClickListener;
    public MyItemLongClickListener mItemLongClickListener;

    public HbDetailListAdapter(List<MerDetail> data, Context context) {
        super();
        this.data = data;
        this.context = context;
    }

    public void appendData(List<MerDetail> data, Context context) {//分页关键
        this.data.addAll(data);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==QIYE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2_find_fragment, parent, false);
            return new QiYeViewHolder(v,mItemClickListener,mItemLongClickListener);
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_fragment, parent, false);
            return new GeRenViewHolder(v,mItemClickListener,mItemLongClickListener);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        if (holder instanceof QiYeViewHolder) {
            MerDetail allUser = data.get(position);
            String memberNum = allUser.getMemberNum();
            String image1 = allUser.getZyImage1();
            String image2 = allUser.getZyImage2();
            String image3 = allUser.getZyImage3();
            String image4 = allUser.getZyImage4();
            String margan1 = allUser.getMargin1();
            String margan2 = allUser.getMargin2();
            String margan3 = allUser.getMargin3();
            String margan4 = allUser.getMargin4();
            String transaction_type = allUser.getTransaction_type();
            String jinE = allUser.getTransaction_amount();
            final String qiyeId = allUser.getUser_id();
            String resv1 = TextUtils.isEmpty(allUser.getYincang()) ? "" : allUser.getYincang();
            String type2 = allUser.getAcc_type();
            if (type2!=null&&"收入".equals(type2)){
                double jine = Double.parseDouble(jinE);
                jinE = String.format("%.2f", jine);
                ((QiYeViewHolder) holder).tvName.setText(transaction_type+"    +"+jinE+"元");
                ((QiYeViewHolder) holder).tvName.setTextColor(Color.RED);
                ((QiYeViewHolder) holder).tvName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            }else {
                double jine = Double.parseDouble(jinE);
                jinE = String.format("%.2f", jine);
                ((QiYeViewHolder) holder).tvName.setText(transaction_type+"    -"+jinE+"元");
                ((QiYeViewHolder) holder).tvName.setTextColor(Color.BLACK);
                ((QiYeViewHolder) holder).tvName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            }
//            String shareRed = allUser.getShareRed();
//            String friendsNumber = allUser.getFriendsNumber();
//            String onceJine = null;
//            if (friendsNumber!=null&&!"".equals(friendsNumber)){
//                onceJine = friendsNumber.split("\\|")[0];
//            }
//            if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
//
//            }else {
//
//            }
            if ("0".equals(resv1)) {
                ((QiYeViewHolder) holder).tv_memberNum.setText("隐藏");
            } else {
                ((QiYeViewHolder) holder).tv_memberNum.setText(memberNum + "人");
            }
//            String shareRed = allUser.getShareRed();
//            if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0) {
//                holder.tvName.setTextColor(Color.RED);
//            }else {
//                holder.tvName.setTextColor(Color.BLACK);
//            }
            ((QiYeViewHolder) holder).tvProject1.setText(allUser.getUpName1());
            ((QiYeViewHolder) holder).tvProject2.setText(allUser.getUpName2());
            ((QiYeViewHolder) holder).tvProject3.setText(allUser.getUpName3());
            ((QiYeViewHolder) holder).tvProject4.setText(allUser.getUpName4());
            if (!"".equals(image1) && image1 != null) {
                ((QiYeViewHolder) holder).ivZYTP1.setVisibility(View.VISIBLE);
            } else {
                ((QiYeViewHolder) holder).ivZYTP1.setVisibility(View.INVISIBLE);
            }
            if (margan1 != null&&!margan1.equalsIgnoreCase("null")) {
                if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                    ((QiYeViewHolder) holder).tvBao1.setVisibility(View.VISIBLE);
                } else {
                    ((QiYeViewHolder) holder).tvBao1.setVisibility(View.INVISIBLE);
                }
            }
            if (!"".equals(image2) && image2 != null) {
                ((QiYeViewHolder) holder).ivZYTP2.setVisibility(View.VISIBLE);
            } else {
                ((QiYeViewHolder) holder).ivZYTP2.setVisibility(View.INVISIBLE);
            }
            if (margan2 != null&&!margan2.equalsIgnoreCase("null")) {
                if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                    ((QiYeViewHolder) holder).tvBao2.setVisibility(View.VISIBLE);
                } else {
                    ((QiYeViewHolder) holder).tvBao2.setVisibility(View.INVISIBLE);
                }
            }
            if (!"".equals(image3) && image3 != null) {
                ((QiYeViewHolder) holder).ivZYTP3.setVisibility(View.VISIBLE);
            } else {
                ((QiYeViewHolder) holder).ivZYTP3.setVisibility(View.INVISIBLE);
            }
            if (margan3 != null&&!margan3.equalsIgnoreCase("null")) {
                if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                    ((QiYeViewHolder) holder).tvBao3.setVisibility(View.VISIBLE);
                } else {
                    ((QiYeViewHolder) holder).tvBao3.setVisibility(View.INVISIBLE);
                }
            }
            if (!"".equals(image4) && image4 != null) {
                ((QiYeViewHolder) holder).ivZYTP4.setVisibility(View.VISIBLE);
            } else {
                ((QiYeViewHolder) holder).ivZYTP4.setVisibility(View.INVISIBLE);
            }
            if (margan4 != null&&!margan4.equalsIgnoreCase("null")) {
                if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                    ((QiYeViewHolder) holder).tvBao4.setVisibility(View.VISIBLE);
                } else {
                    ((QiYeViewHolder) holder).tvBao4.setVisibility(View.INVISIBLE);
                }
            }
            String head = TextUtils.isEmpty(allUser.getHead()) ? "" : allUser.getHead();
            if (head.length() > 40) {
                ((QiYeViewHolder) holder).tvTitleA.setVisibility(View.INVISIBLE);
                ((QiYeViewHolder) holder).ivHead.setVisibility(View.VISIBLE);
                String[] orderProjectArray = head.split("\\|");
                head = orderProjectArray[0];
            }
            if (!(head.equals("") || head.equals(null))) {
                ((QiYeViewHolder) holder).tvTitleA.setVisibility(View.INVISIBLE);
                ((QiYeViewHolder) holder).ivHead.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + head, ((QiYeViewHolder) holder).ivHead, DemoApplication.mOptions);
            } else {
                ((QiYeViewHolder) holder).ivHead.setVisibility(View.INVISIBLE);
                ((QiYeViewHolder) holder).tvTitleA.setVisibility(View.VISIBLE);
                ((QiYeViewHolder) holder).tvTitleA.setText("企");
            }
            ((QiYeViewHolder) holder).tvTitleA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, QiYeDetailsActivity.class).putExtra("qiyeId",qiyeId));
                }
            });
            ((QiYeViewHolder) holder).ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, QiYeDetailsActivity.class).putExtra("qiyeId", qiyeId));
                }
            });
            String qiyeLat = TextUtils.isEmpty(allUser.getResv2()) ? "39.904690" : allUser.getResv2();
            String qiyeLon = TextUtils.isEmpty(allUser.getResv1()) ? "116.407170" : allUser.getResv1();
            String mLat1 = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) ? "39.904690" : DemoApplication.getInstance().getCurrentLat();
            String mLon1 = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) ? "116.407170" : DemoApplication.getInstance().getCurrentLng();
            final LatLng ll1 = new LatLng(Double.parseDouble(qiyeLat), Double.parseDouble(qiyeLon));
            double distance = 0;
            if (ll1 != null && mLat1 != null & mLon1 != null) {
                distance = DistanceUtil.getDistance(ll1, new LatLng(Double.parseDouble(mLat1), Double.parseDouble(mLon1)));
            }
            double dou = distance / 1000;
            String str = String.format("%.2f", dou);//format 返回的是字符串
            if (str!=null&&dou>=10000){
                ((QiYeViewHolder) holder).tvDistance.setText("隐藏");
            }else {
                ((QiYeViewHolder) holder).tvDistance.setText(str + "km");
            }
            String create = allUser.getTimestamp();
            if (create!=null&&create.length()>13){
                String time = create.substring(0, 4) + "-" + create.substring(4, 6) + "-" + create.substring(6, 8) + " "
                        + create.substring(8, 10) + ":" + create.substring(10, 12);
                ((QiYeViewHolder) holder).tvQianming.setText("时间："+time);
            }
        }else if (holder instanceof GeRenViewHolder){
            MerDetail userInfo = data.get(position);
            ((GeRenViewHolder) holder).tv_company_count.setVisibility(View.INVISIBLE);
            String name = userInfo.getName();
            final String loginId = userInfo.getUser_id();
            String nianLing = TextUtils.isEmpty(userInfo.getNianling()) ? "**" : userInfo.getNianling();
            String head = userInfo.getHead();
            String uSex = userInfo.getSex();
            String resv1 = userInfo.getResv1();
            String resv2 = userInfo.getResv2();
            String lat = DemoApplication.getInstance().getCurrentLat();
            String lng = DemoApplication.getInstance().getCurrentLng();
            String sign = userInfo.getSign();
            String image1 = null,image2 = null,image3 = null,image4 = null,
                    margan1 = null,margan2 = null,margan3 = null,margan4 = null;
            image1 = userInfo.getZyImage1();
            image2 = userInfo.getZyImage2();
            image3 = userInfo.getZyImage3();
            image4 = userInfo.getZyImage4();
            margan1 = userInfo.getMargin1();
            margan2 = userInfo.getMargin2();
            margan3 = userInfo.getMargin3();
            margan4 = userInfo.getMargin4();
            String pl1 = userInfo.getUpName1();
            String pl2 = userInfo.getUpName2();
            String pl3 = userInfo.getUpName3();
            String pl4 = userInfo.getUpName4();
            ((GeRenViewHolder) holder).tvProject1.setText(userInfo.getUpName1());
            ((GeRenViewHolder) holder).tvProject2.setText(userInfo.getUpName2());
            ((GeRenViewHolder) holder).tvProject3.setText(userInfo.getUpName3());
            ((GeRenViewHolder) holder).tvProject4.setText(userInfo.getUpName4());
            if (pl1==null||pl1.equals("")){
                ((GeRenViewHolder) holder).ll_one.setVisibility(View.GONE);
            }else {
                ((GeRenViewHolder) holder).ll_one.setVisibility(View.VISIBLE);
            }
            if (pl2==null||pl2.equals("")){
                ((GeRenViewHolder) holder).ll_two.setVisibility(View.GONE);
            }else {
                ((GeRenViewHolder) holder).ll_two.setVisibility(View.VISIBLE);
            }
            if (pl3==null||pl3.equals("")){
                ((GeRenViewHolder) holder).ll_three.setVisibility(View.GONE);
            }else {
                ((GeRenViewHolder) holder).ll_three.setVisibility(View.VISIBLE);
            }
            if (pl4==null||pl4.equals("")){
                ((GeRenViewHolder) holder).ll_four.setVisibility(View.GONE);
            }else {
                ((GeRenViewHolder) holder).ll_four.setVisibility(View.VISIBLE);
            }
            if (!"".equals(image1) && image1 != null) {
                ((GeRenViewHolder) holder).ivZYTP1.setVisibility(View.VISIBLE);
            } else {
                ((GeRenViewHolder) holder).ivZYTP1.setVisibility(View.INVISIBLE);
            }
            if (margan1 != null&&!margan1.equalsIgnoreCase("null")) {
                if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                    ((GeRenViewHolder) holder).tvBao1.setVisibility(View.VISIBLE);
                } else {
                    ((GeRenViewHolder) holder).tvBao1.setVisibility(View.INVISIBLE);
                }
            }else {
                ((GeRenViewHolder) holder).tvBao1.setVisibility(View.INVISIBLE);
            }
            if (!"".equals(image2) && image2 != null) {
                ((GeRenViewHolder) holder).ivZYTP2.setVisibility(View.VISIBLE);
            } else {
                ((GeRenViewHolder) holder).ivZYTP2.setVisibility(View.INVISIBLE);
            }
            if (margan2 != null&&!margan2.equalsIgnoreCase("null")) {
                if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                    ((GeRenViewHolder) holder).tvBao2.setVisibility(View.VISIBLE);
                } else {
                    ((GeRenViewHolder) holder).tvBao2.setVisibility(View.INVISIBLE);
                }
            }else {
                ((GeRenViewHolder) holder).tvBao2.setVisibility(View.INVISIBLE);
            }
            if (!"".equals(image3) && image3 != null) {
                ((GeRenViewHolder) holder).ivZYTP3.setVisibility(View.VISIBLE);
            } else {
                ((GeRenViewHolder) holder).ivZYTP3.setVisibility(View.INVISIBLE);
            }
            if (margan3 != null&&!margan3.equalsIgnoreCase("null")) {
                if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                    ((GeRenViewHolder) holder).tvBao3.setVisibility(View.VISIBLE);
                } else {
                    ((GeRenViewHolder) holder).tvBao3.setVisibility(View.INVISIBLE);
                }
            }else {
                ((GeRenViewHolder) holder).tvBao3.setVisibility(View.INVISIBLE);
            }
            if (!"".equals(image4) && image4 != null) {
                ((GeRenViewHolder) holder).ivZYTP4.setVisibility(View.VISIBLE);
            } else {
                ((GeRenViewHolder) holder).ivZYTP4.setVisibility(View.INVISIBLE);
            }
            if (margan4 != null&&!margan4.equalsIgnoreCase("null")) {
                if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                    ((GeRenViewHolder) holder).tvBao4.setVisibility(View.VISIBLE);
                } else {
                    ((GeRenViewHolder) holder).tvBao4.setVisibility(View.INVISIBLE);
                }
            }else {
                ((GeRenViewHolder) holder).tvBao4.setVisibility(View.INVISIBLE);
            }
            String jinE = userInfo.getTransaction_amount();
            String type2 = userInfo.getAcc_type();
            ((GeRenViewHolder) holder).tvCompany.setVisibility(View.VISIBLE);
            if (type2!=null&&"收入".equals(type2)){
                double jine = Double.parseDouble(jinE);
                jinE = String.format("%.2f", jine);
                ((GeRenViewHolder) holder).tvCompany.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                ((GeRenViewHolder) holder).tvCompany.setText("主页红包"+"    +"+jinE+"元");
                ((GeRenViewHolder) holder).tvCompany.setTextColor(Color.RED);
                ((GeRenViewHolder) holder).tvName.setText(TextUtils.isEmpty(name) ? loginId : name);
            }else {
                double jine = Double.parseDouble(jinE);
                jinE = String.format("%.2f", jine);
                ((GeRenViewHolder) holder).tvCompany.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                ((GeRenViewHolder) holder).tvCompany.setText("主页红包"+"    -"+jinE+"元");
                ((GeRenViewHolder) holder).tvCompany.setTextColor(Color.BLACK);
                ((GeRenViewHolder) holder).tvName.setText(TextUtils.isEmpty(name) ? loginId : name);
            }
            String shareRed = userInfo.getShareRed();
            String friendsNumber = userInfo.getFriendsNumber();
            String onceJine = null;
            if (friendsNumber!=null&&!"".equals(friendsNumber)){
                onceJine = friendsNumber.split("\\|")[0];
            }
            if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
                ((GeRenViewHolder) holder).tvName.setTextColor(Color.RED);
            }else {
                ((GeRenViewHolder) holder).tvName.setTextColor(Color.BLACK);
            }
            if (nianLing==null||"".equals(nianLing)||nianLing.equalsIgnoreCase("null")){
                nianLing = "**";
            }
            ((GeRenViewHolder) holder).tvNianLing.setText(nianLing);
            if (head!=null&&head.length() > 20) {
                ((GeRenViewHolder) holder).tvTitleA.setVisibility(View.INVISIBLE);
                ((GeRenViewHolder) holder).ivHead.setVisibility(View.VISIBLE);
                String[] orderProjectArray = head.split("\\|");
                head = orderProjectArray[0];
            }
            if (!(head==null || head.equals("") || head.equalsIgnoreCase("null"))) {
                ((GeRenViewHolder) holder).tvTitleA.setVisibility(View.INVISIBLE);
                ((GeRenViewHolder) holder).ivHead.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+head,((GeRenViewHolder) holder).ivHead, DemoApplication.mOptions);
            } else {
                ((GeRenViewHolder) holder).ivHead.setVisibility(View.INVISIBLE);
                ((GeRenViewHolder) holder).tvTitleA.setVisibility(View.VISIBLE);
                ((GeRenViewHolder) holder).tvTitleA.setText(TextUtils.isEmpty(name) ? loginId : name);
            }
            if ("00".equals(uSex)) {
                ((GeRenViewHolder) holder).ivSex.setImageResource(R.drawable.nv);
                //保 255 62 74  图 255 170 76
                ((GeRenViewHolder) holder).tvNianLing.setBackgroundColor(Color.rgb(234,121,219));
                ((GeRenViewHolder) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
            } else {
                ((GeRenViewHolder) holder).ivSex.setImageResource(R.drawable.nan);
                ((GeRenViewHolder) holder).tvNianLing.setBackgroundColor(Color.rgb(62,197,255));
                ((GeRenViewHolder) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
            }
            ((GeRenViewHolder) holder).tvTitleA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,loginId));
                }
            });
            ((GeRenViewHolder) holder).ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,loginId));
                }
            });
            if (lat!=null&&lng!=null&&resv1!=null&&resv2!=null) {
                if (!("".equals(lat) || "".equals(lng) || resv1.equals("") || resv2.equals(""))) {
                    double latitude1 = Double.valueOf(lat);
                    double longitude1 = Double.valueOf(lng);
                    final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                    LatLng ll = new LatLng(latitude1, longitude1);
                    double distance = DistanceUtil.getDistance(ll, ll1);
                    double dou = distance / 1000;
                    String str = String.format("%.2f", dou);//format 返回的是字符串
                    if (str!=null&&dou>=10000){
                        ((GeRenViewHolder) holder).tvDistance.setText("隐藏");
                    }else {
                        ((GeRenViewHolder) holder).tvDistance.setText(str + "km");
                    }
                } else {
                    ((GeRenViewHolder) holder).tvDistance.setText("3km以外");
                }
            }else {
                ((GeRenViewHolder) holder).tvDistance.setText("3km以外");
            }
//            if (sign==null||"".equals(sign)){
//                sign = "未设置签名";
//            }
            String create = userInfo.getTimestamp();
            if (create!=null&&create.length()>12){
                String time = create.substring(0, 4) + "-" + create.substring(4, 6) + "-" + create.substring(6, 8) + " "
                        + create.substring(8, 10) + ":" + create.substring(10, 12);
                ((GeRenViewHolder) holder).tvQianming.setText("时间："+time);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type1;
        if ("01".equals(data.get(position).getQiYeOrGeRen())){
            type1 = QIYE_ITEM;
        }else {
            type1 = GEREN_ITEM;
        }
        return type1;
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.mItemLongClickListener = listener;
    }

    public class GeRenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        RelativeLayout rl_sex;
        TextView tvName,tvNianLing;
        TextView tvCompany,tv_company_count;
        TextView tvTitleA,tvBao1,tvBao2,tvBao3,tvBao4,tvBu1,tvBu2,tvBu3,tvBu4,ivZYTP1,ivZYTP2,ivZYTP3,ivZYTP4;
        TextView tvDistance;
        ImageView ivSex;
        ImageView ivHead;
        TextView tvProject1,tvProject2,tvProject3,tvProject4;
        TextView tvQianming;
        LinearLayout ll_one,ll_two,ll_three,ll_four,card;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public GeRenViewHolder(View convertView,MyItemClickListener listener,MyItemLongClickListener longClickListener) {
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
            ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
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
    }

    public class QiYeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        RelativeLayout rl_sex;
        TextView tvName,tvNianLing;
        TextView tvCompany,tv_memberNum,tv_company;
        TextView tvTitleA,tvBao1,tvBao2,tvBao3,tvBao4,tvBu1,tvBu2,tvBu3,tvBu4,ivZYTP1,ivZYTP2,ivZYTP3,ivZYTP4;
        TextView tvDistance;
        ImageView ivSex;
        LinearLayout card;
        CircleImageView ivHead;
        TextView tvProject1,tvProject2,tvProject3,tvProject4;
        TextView tvQianming;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public QiYeViewHolder(View convertView,MyItemClickListener listener,MyItemLongClickListener longClickListener) {
            super(convertView);
            card = (LinearLayout) convertView.findViewById(R.id.card);
            rl_sex = (RelativeLayout) convertView.findViewById(R.id.rl_sex);
            tv_memberNum = (TextView) convertView.findViewById(R.id.tv_memberNum);
            tv_company = (TextView) convertView.findViewById(R.id.tv_company);
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
            //holder.tvTime = (TextView) convertView.findViewById(R.id.tv_date);
            ivHead = (CircleImageView) convertView.findViewById(R.id.iv_head);
            ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
            tvQianming = (TextView) convertView.findViewById(R.id.tv_qianming);
            tvProject1 = (TextView) convertView.findViewById(R.id.tv_project_one);
            tvProject2 = (TextView) convertView.findViewById(R.id.tv_project_two);
            tvProject3 = (TextView) convertView.findViewById(R.id.tv_project_three);
            tvProject4 = (TextView) convertView.findViewById(R.id.tv_project_four);
            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            rl_sex.setVisibility(View.INVISIBLE);
            tvCompany.setVisibility(View.INVISIBLE);
            tvDistance.setVisibility(View.VISIBLE);
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

}
