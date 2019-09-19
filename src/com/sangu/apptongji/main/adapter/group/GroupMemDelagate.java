package com.sangu.apptongji.main.adapter.group;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.ui.GroupDetailsActivity;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by zhy on 16/6/22.
 */
public class GroupMemDelagate implements ItemViewDelegate<UserAll>
{
    private Context mContext;
    private boolean delete = false;

    public GroupDetailsActivity.OnDeleteClickListener onDeleteClickListener;

    public GroupMemDelagate(Context context, boolean del,GroupDetailsActivity.OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
        delete = del;
        mContext = context;
    }

    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.item_find_de_fragment;
    }

    @Override
    public boolean isForViewType(UserAll item, int position)
    {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, UserAll allUser, final int position)
    {
        holder.setIsRecyclable(true);
        if (delete){
            holder.setVisible(R.id.iv_delete,View.VISIBLE);
        }else {
            holder.setVisible(R.id.iv_delete,View.GONE);
        }
        final String loginId = allUser.getuLoginId();
        String shareRed = allUser.getShareRed();
        String friendsNumber = allUser.getFriendsNumber();
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        holder.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onItemClick(v,position);
            }
        });
        holder.setText(R.id.tv_name,TextUtils.isEmpty(allUser.getuName()) ? allUser.getuId() : allUser.getuName());
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            holder.setTextColor(R.id.tv_name,Color.RED);
        }else {
            holder.setTextColor(R.id.tv_name,Color.BLACK);
        }
        String nianLing = TextUtils.isEmpty(allUser.getuAge()) ? "**" : allUser.getuAge();
        if (nianLing==null||nianLing.equalsIgnoreCase("null")||"".equals(nianLing)){
            nianLing = "**";
        }
        holder.setText(R.id.tv_nianling,nianLing);
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
            holder.setVisible(R.id.tv_company_count,View.VISIBLE);
        }else {
            holder.setVisible(R.id.tv_company_count,View.INVISIBLE);
        }
        holder.setText(R.id.tv_company_count,"("+member+"人"+")");
        holder.setText(R.id.tv_company,company);
        holder.setText(R.id.tv_project_one,allUser.getpL1());
        holder.setText(R.id.tv_project_two,allUser.getpL2());
        holder.setText(R.id.tv_project_three,allUser.getpL3());
        holder.setText(R.id.tv_project_four,allUser.getpL4());
        String image1 = allUser.getImage1();
        String image2 = allUser.getImage2();
        String image3 = allUser.getImage3();
        String image4 = allUser.getImage4();
        String margan1 = allUser.getMargen1();
        String margan2 = allUser.getMargen2();
        String margan3 = allUser.getMargen3();
        String margan4 = allUser.getMargen4();
        if (allUser.getpL1()==null||allUser.getpL1().equals("")){
            holder.setVisible(R.id.ll_one,View.GONE);
        }else {
            holder.setVisible(R.id.ll_one,View.VISIBLE);
        }
        if (allUser.getpL2()==null||allUser.getpL2().equals("")){
            holder.setVisible(R.id.ll_two,View.GONE);
        }else {
            holder.setVisible(R.id.ll_two,View.VISIBLE);
        }
        if (allUser.getpL3()==null||allUser.getpL3().equals("")){
            holder.setVisible(R.id.ll_three,View.GONE);
        }else {
            holder.setVisible(R.id.ll_three,View.VISIBLE);
        }
        if (allUser.getpL4()==null||allUser.getpL4().equals("")){
            holder.setVisible(R.id.ll_four,View.GONE);
        }else {
            holder.setVisible(R.id.ll_four,View.VISIBLE);
        }
        if (image1!=null&&!"".equals(image1)) {
            holder.setVisible(R.id.iv_zy1_tupian,View.VISIBLE);
        }else {
            holder.setVisible(R.id.iv_zy1_tupian,View.GONE);
        }
        if (margan1!=null) {
            if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                holder.setVisible(R.id.tv_zy1_bao,View.VISIBLE);
            }else {
                holder.setVisible(R.id.tv_zy1_bao,View.GONE);
            }
        }
        if (image2!=null&&!"".equals(image2)) {
            holder.setVisible(R.id.iv_zy2_tupian,View.VISIBLE);
        }else {
            holder.setVisible(R.id.iv_zy2_tupian,View.GONE);
        }
        if (margan2!=null) {
            if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                holder.setVisible(R.id.tv_zy2_bao,View.VISIBLE);
            }else {
                holder.setVisible(R.id.tv_zy2_bao,View.GONE);
            }
        }
        if (image3!=null&&!"".equals(image3)) {
            holder.setVisible(R.id.iv_zy3_tupian,View.VISIBLE);
        }else {
            holder.setVisible(R.id.iv_zy3_tupian,View.GONE);
        }
        if (margan3!=null) {
            if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                holder.setVisible(R.id.tv_zy3_bao,View.VISIBLE);
            }else {
                holder.setVisible(R.id.tv_zy3_bao,View.GONE);
            }
        }
        if (image4!=null&&!"".equals(image4)) {
            holder.setVisible(R.id.iv_zy4_tupian,View.VISIBLE);
        }else {
            holder.setVisible(R.id.iv_zy4_tupian,View.GONE);
        }
        if (margan4!=null) {
            if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                holder.setVisible(R.id.tv_zy4_bao,View.VISIBLE);
            }else {
                holder.setVisible(R.id.tv_zy4_bao,View.GONE);
            }
        }
        String head = TextUtils.isEmpty(allUser.getuImage()) ? "" : allUser.getuImage();
        if (head.length() > 40) {
            holder.setVisible(R.id.tv_titl,View.INVISIBLE);
            holder.setVisible(R.id.iv_head,View.VISIBLE);
            String[] orderProjectArray = head.split("\\|");
            head = orderProjectArray[0];
        }
        if (!(head.equals("") || head.equals(null))) {
            holder.setVisible(R.id.tv_titl,View.INVISIBLE);
            holder.setVisible(R.id.iv_head,View.VISIBLE);
            holder.setCirImageUrl(R.id.iv_head,FXConstant.URL_AVATAR+head);
        } else {
            holder.setVisible(R.id.tv_titl,View.VISIBLE);
            holder.setVisible(R.id.iv_head,View.INVISIBLE);
            holder.setText(R.id.tv_titl,TextUtils.isEmpty(allUser.getuName()) ? allUser.getuId() : allUser.getuName());
        }
        if (("00").equals(allUser.getuSex())) {
            holder.setImageResource(R.id.iv_sex,R.drawable.nv);
            //保 255 62 74  图 255 170 76
            holder.setBackgroundColor(R.id.tv_nianling,Color.rgb(234,121,219));
            holder.setBackgroundRes(R.id.tv_titl,R.drawable.fx_bg_text_red);
        } else {
            holder.setImageResource(R.id.iv_sex,R.drawable.nan);
            //保 255 62 74  图 255 170 76
            holder.setBackgroundRes(R.id.tv_nianling,R.color.accent_blue);
            holder.setBackgroundRes(R.id.tv_titl,R.drawable.fx_bg_text_gra);
        }
        holder.setOnClickListener(R.id.iv_head, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,loginId));
            }
        });
        holder.setOnClickListener(R.id.tv_titl, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,loginId));
            }
        });
        String resv3 = TextUtils.isEmpty(allUser.getResv3()) ? "" : allUser.getResv3();
        String resv1 = TextUtils.isEmpty(allUser.getResv1()) ? "" : allUser.getResv1();
        String resv2 = TextUtils.isEmpty(allUser.getResv2()) ? "" : allUser.getResv2();
        String lat = DemoApplication.getInstance().getCurrentLat();
        String lng = DemoApplication.getInstance().getCurrentLng();
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
                    holder.setText(R.id.tv_distance,"隐藏");
                }else {
                    holder.setText(R.id.tv_distance,str + "km");
                }
            } else {
                holder.setText(R.id.tv_distance,"3km以外");
            }
        }else {
            holder.setText(R.id.tv_distance,"3km以外");
        }
        String sign = allUser.getuSignaTure();
        if (sign==null||"".equals(sign)){
            sign = "未设置简介";
        }
        holder.setText(R.id.tv_qianming,sign);
    }
}
