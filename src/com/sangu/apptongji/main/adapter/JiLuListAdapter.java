package com.sangu.apptongji.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.SouSuoAdvertClickActivity;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.widget.CircleImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2017-07-12.
 */

public class JiLuListAdapter extends BaseAdapter {
    private Context context;
    List<JSONObject> data;
    private String dataType;//记录是那个列表用的 0是默认原来的  1是我新加的动态推荐列表的
    String isAdvert;
    String advertUrl;
    private int width;
    private int currentTag = 0;

    public JiLuListAdapter(Context context, List<JSONObject> data, String type,String isAdvert, String advertUrl, int screenWidth) {
        this.context = context;
        this.data = data;
        dataType = type;

        this.isAdvert = isAdvert;
        this.advertUrl = advertUrl;
        width  = screenWidth;
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_fragment, parent, false);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
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
            holder.ivHead = (CircleImageView) convertView.findViewById(R.id.iv_head);
            holder.ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.tvQianming = (TextView) convertView.findViewById(R.id.tv_qianming);
            holder.tvProject1 = (TextView) convertView.findViewById(R.id.tv_project_one);
            holder.tvProject2 = (TextView) convertView.findViewById(R.id.tv_project_two);
            holder.tvProject3 = (TextView) convertView.findViewById(R.id.tv_project_three);
            holder.tvProject4 = (TextView) convertView.findViewById(R.id.tv_project_four);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.rl_bg = (RelativeLayout) convertView.findViewById(R.id.rl_bg);
            holder.image_advert = (ImageView) convertView.findViewById(R.id.image_advert);
            holder.tv_recomIdentify2 = (TextView) convertView.findViewById(R.id.tv_recomIdentify2);
            holder.tv_recomIdentify = (TextView) convertView.findViewById(R.id.tv_recomIdentify);
            holder.tv_recommendMark = (TextView) convertView.findViewById(R.id.tv_recommendMark);

            convertView.setTag(holder);

        }

        JSONObject object = data.get(position);

        JSONObject userInfo;
        if (dataType.equals("1")){
            userInfo = object.getJSONObject("u_userInfo");
        }else {
            userInfo = object.getJSONObject("userInfo");
        }
        JSONArray profession = object.getJSONArray("userProfession");

        if (userInfo!=null) {
            String shareRed = userInfo.getString("shareRed");
            String name = userInfo.getString("uName");
            final String loginId = userInfo.getString("uLoginId");
            String nianLing = TextUtils.isEmpty(userInfo.getString("uAge")) ? "27" : userInfo.getString("uAge");
            String time = object.getString("timestamp");
            String head = userInfo.getString("uImage");
            String uSex = userInfo.getString("uSex");
            String resv3 = userInfo.getString("resv3");
            String resv1 = userInfo.getString("resv1");
            String resv2 = userInfo.getString("resv2");
            String lat = DemoApplication.getInstance().getCurrentLat();
            String lng = DemoApplication.getInstance().getCurrentLng();
            String sign = userInfo.getString("uSignaTure");
            String pl1 = null, pl2 = null, pl3 = null, pl4 = null, image1 = null, image2 = null, image3 = null, image4 = null,
                    margan1 = null, margan2 = null, margan3 = null, margan4 = null;
            if (time != null) {
                time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " " + time.substring(8, 10) + ":" + time.substring(10, 12);
            }

            Double allMargin = 0.0;
            if (profession!=null&&profession.size() > 0) {
                pl1 = profession.getJSONObject(0).getString("upName");
                image1 = profession.getJSONObject(0).getString("image");
                margan1 = profession.getJSONObject(0).getString("margin");

                if (pl1.equalsIgnoreCase("null")) {
                    pl1 = "";
                }
                holder.tvProject1.setText(pl1);
            }
            if (profession.size() > 1) {
                pl2 = profession.getJSONObject(1).getString("upName");
                image2 = profession.getJSONObject(1).getString("image");
                margan2 = profession.getJSONObject(1).getString("margin");
                if (pl2.equalsIgnoreCase("null")) {
                    pl2 = "";
                }
                holder.tvProject2.setText(pl2);
            }
            if (profession.size() > 2) {
                pl3 = profession.getJSONObject(2).getString("upName");
                image3 = profession.getJSONObject(2).getString("image");
                margan3 = profession.getJSONObject(2).getString("margin");
                if (pl3.equalsIgnoreCase("null")) {
                    pl3 = "";
                }
                holder.tvProject3.setText(pl3);
            }
            if (profession.size() > 3) {
                pl4 = profession.getJSONObject(3).getString("upName");
                image4 = profession.getJSONObject(3).getString("image");
                margan4 = profession.getJSONObject(3).getString("margin");
                if (pl4.equalsIgnoreCase("null")) {
                    pl4 = "";
                }
                holder.tvProject4.setText(pl4);
            }
            holder.tvName.setText(TextUtils.isEmpty(name) ? loginId : name);
            if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0) {
                holder.tvName.setTextColor(Color.RED);
            }
            String company = userInfo.getString("uCompany");
            String uNation = userInfo.getString("uNation");
            String resv5 = userInfo.getString("resv5");
            String resv6 = userInfo.getString("resv6");
            if (company == null || company.equals("")) {
                company = "暂未加入企业";
            }
            if ("00".equals(resv6) && !"1".equals(uNation)) {
                company = "暂未加入企业";
            }
            if (resv5 == null || "".equals(resv5)) {
                company = "暂未加入企业";
            }
            try {
                company = URLDecoder.decode(company, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String member = userInfo.getString("menberNum");
            if (member == null || member.equalsIgnoreCase("null") || "".equals(member)) {
                member = "0";
            }
            if (!company.equals("暂未加入企业")) {
                holder.tv_company_count.setVisibility(View.VISIBLE);
            } else {
                holder.tv_company_count.setVisibility(View.INVISIBLE);
            }
            holder.tv_company_count.setText("(" + member + "人" + ")");
            holder.tvCompany.setText(company);
            holder.tvNianLing.setText(nianLing);
            if (pl1 == null || pl1.equals("")) {
                holder.ll_one.setVisibility(View.GONE);
            } else {
                holder.ll_one.setVisibility(View.VISIBLE);
            }
            if (pl2 == null || pl2.equals("")) {
                holder.ll_two.setVisibility(View.GONE);
            } else {
                holder.ll_two.setVisibility(View.VISIBLE);
            }
            if (pl3 == null || pl3.equals("")) {
                holder.ll_three.setVisibility(View.GONE);
            } else {
                holder.ll_three.setVisibility(View.VISIBLE);
            }
            if (pl4 == null || pl4.equals("")) {
                holder.ll_four.setVisibility(View.GONE);
            } else {
                holder.ll_four.setVisibility(View.VISIBLE);
            }
            if (image1 != null && !"".equals(image1)) {
                holder.ivZYTP1.setVisibility(View.VISIBLE);
            } else {
                holder.ivZYTP1.setVisibility(View.GONE);
            }
            if (margan1 != null) {
                if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                    allMargin = Double.valueOf(margan1);
                    holder.tvBao1.setVisibility(View.VISIBLE);
                } else {
                    holder.tvBao1.setVisibility(View.GONE);
                }
            }
            if (image2 != null && !"".equals(image2)) {
                holder.ivZYTP2.setVisibility(View.VISIBLE);
            } else {
                holder.ivZYTP2.setVisibility(View.GONE);
            }
            if (margan2 != null) {
                if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                    allMargin = allMargin + Double.valueOf(margan2);
                    holder.tvBao2.setVisibility(View.VISIBLE);
                } else {
                    holder.tvBao2.setVisibility(View.GONE);
                }
            }
            if (image3 != null && !"".equals(image3)) {
                holder.ivZYTP3.setVisibility(View.VISIBLE);
            } else {
                holder.ivZYTP3.setVisibility(View.GONE);
            }
            if (margan3 != null) {
                if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                    allMargin = allMargin + Double.valueOf(margan3);
                    holder.tvBao3.setVisibility(View.VISIBLE);
                } else {
                    holder.tvBao3.setVisibility(View.GONE);
                }
            }
            if (image4 != null && !"".equals(image4)) {

                holder.ivZYTP4.setVisibility(View.VISIBLE);
            } else {
                holder.ivZYTP4.setVisibility(View.GONE);
            }
            if (margan4 != null) {
                if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                    allMargin = allMargin + Double.valueOf(margan4);
                    holder.tvBao4.setVisibility(View.VISIBLE);
                } else {
                    holder.tvBao4.setVisibility(View.GONE);
                }
            }



            if (head != null && head.length() > 40) {
                holder.tvTitleA.setVisibility(View.INVISIBLE);
                holder.ivHead.setVisibility(View.VISIBLE);
                String[] orderProjectArray = head.split("\\|");
                head = orderProjectArray[0];
            }
            if (!(head == null || head.equals(""))) {
                holder.tvTitleA.setVisibility(View.INVISIBLE);
                holder.ivHead.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + head, holder.ivHead, DemoApplication.mOptions);
            } else {
                holder.ivHead.setVisibility(View.INVISIBLE);
                holder.tvTitleA.setVisibility(View.VISIBLE);
                holder.tvTitleA.setText(TextUtils.isEmpty(name) ? loginId : name);
            }
            if ("00".equals(uSex)) {
                holder.ivSex.setImageResource(R.drawable.nv);
                //保 255 62 74  图 255 170 76
                holder.tvNianLing.setBackgroundColor(Color.rgb(234, 121, 219));
                holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
            } else {
                holder.ivSex.setImageResource(R.drawable.nan);
                holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
            }
            holder.tvTitleA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, loginId));
                }
            });
            holder.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, loginId));
                }
            });
            if (lat != null && lng != null && resv1 != null) {
                if (!("".equals(lat) || "".equals(lng) || "".equals(resv1) || "".equals(resv2))) {
                    double latitude1 = Double.valueOf(lat);
                    double longitude1 = Double.valueOf(lng);
                    final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                    LatLng ll = new LatLng(latitude1, longitude1);
                    double distance = DistanceUtil.getDistance(ll, ll1);
                    double dou = distance / 1000;
                    String str = String.format("%.2f", dou);//format 返回的是字符串
                    if (str != null && dou >= 10000) {
                        holder.tvDistance.setText("隐藏");
                    } else {
                        holder.tvDistance.setText(str + "km");
                    }
                } else {
                    holder.tvDistance.setText("3km以外");
                }
            } else {
                holder.tvDistance.setText("3km以外");
            }

            if (dataType.equals("1")){
                holder.tvQianming.setText("");
            }else {
                holder.tvQianming.setText("时间： " + time);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,loginId));
                }
            });


            //判断质保
            if (dataType.equals("1")){

                holder.tvCompany.setText("");
                holder.tv_company_count.setText("");
                holder.rl_sex.setVisibility(View.INVISIBLE);

                if (allMargin>99){

                    holder.tv_recommendMark.setVisibility(View.VISIBLE);

                    holder.tv_recomIdentify2.setVisibility(View.VISIBLE);
                    holder.tv_recomIdentify.setVisibility(View.GONE);
                    DecimalFormat df = new DecimalFormat("######0");
                    String numberStr = df.format(allMargin);
                    holder.tv_recomIdentify2.setText("承诺保障"+numberStr);

                }else {

                    holder.tv_recommendMark.setVisibility(View.GONE);
                    holder.tv_recomIdentify2.setVisibility(View.INVISIBLE);
                    holder.tv_recomIdentify.setVisibility(View.VISIBLE);

                }

            }


            if (position%20==1 && isAdvert.equals("1")){

                String[] advertImage = advertUrl.split("\\|");

                if (currentTag == 3 || position==1){
                    currentTag = 0;
                }

                String advertImageUrl = advertImage[currentTag];

                currentTag++;

                if (!(advertImageUrl == "")) {

//                    ViewGroup.LayoutParams lp = holder.rl_bg.getLayoutParams();
//                    lp.width = width;
//
//                    double c = (double) 472/1080;
//                    double b = width*c;
//
//                    int b1 = (int) b;
//                    lp.height = b1;

                    // holder.rl_bg.setLayoutParams(lp);

                    // holder.tv_content.setText("");
                    holder.image_advert.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_ADVERTURL + advertImageUrl,holder.image_advert);

                }else {

//                    ViewGroup.LayoutParams lp = holder.rl_bg.getLayoutParams();
//                    lp.width = width;
//                    lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                    // holder.rl_bg.setLayoutParams(lp);

                    holder.image_advert.setVisibility(View.GONE);

                }

            }else {
//                ViewGroup.LayoutParams lp = holder.rl_bg.getLayoutParams();
//                lp.width = width;
//                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                //  holder.rl_bg.setLayoutParams(lp);
                holder.image_advert.setVisibility(View.GONE);
            }

            holder.image_advert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent2 = new Intent(context, SouSuoAdvertClickActivity.class);

                    if (currentTag == 1){

                        intent2.putExtra("url", "https://mp.weixin.qq.com/s/1Gv9UT1ltNkq3aX4XKmAzA");

                    }else if (currentTag == 2)
                    {
                        intent2.putExtra("url", "https://mp.weixin.qq.com/s/mE5q1I-5P-fd_rQEvGHLPQ");

                    }else if (currentTag == 3)
                    {
                        intent2.putExtra("url", "https://mp.weixin.qq.com/s/1Gv9UT1ltNkq3aX4XKmAzA");
                    }

                    context.startActivity(intent2);


//                    Intent intent2 = new Intent(context, ChatActivity.class);
//                    intent2.putExtra("userId", "13513895563");
//                    intent2.putExtra("chatType", Constant.CHATTYPE_SINGLE);
//                    intent2.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
//                    //  intent2.putExtra(EaseConstant.EXTRA_USER_IMG,"zhengshiduo.png");
//                    // intent2.putExtra(EaseConstant.EXTRA_USER_NAME,"李璐");
//                    intent2.putExtra(EaseConstant.EXTRA_USER_SHARERED,"无");
//                    context.startActivity(intent2);

                }
            });

        }

        return convertView;
    }

    class ViewHolder {
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
        RelativeLayout rl_bg;
        ImageView image_advert;
        TextView tv_recomIdentify2;
        TextView tv_recomIdentify;
        TextView tv_recommendMark;
    }

}
