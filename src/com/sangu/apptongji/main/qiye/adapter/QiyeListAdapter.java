package com.sangu.apptongji.main.qiye.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.AppDownDetailActivity;
import com.sangu.apptongji.main.activity.SouSuoAdvertClickActivity;
import com.sangu.apptongji.main.moments.MomentsPublishActivity;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.widget.CircleImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-30.
 */

public class QiyeListAdapter extends RecyclerView.Adapter<QiyeListAdapter.ViewHolder> {
    private List<QiYeInfo> data = null;
    public MyItemClickListener mItemClickListener;
    public MyItemLongClickListener mItemLongClickListener;
    private int lastPosition = -1;

    private Activity context;

    private String isShowAdvert = "no";
    private String[] advertImages = {};
    private String[] advertClickTypes = {};
    private String[] advertClickContents = {};
    private int currentTag = 0;

    public QiyeListAdapter(Activity context, List<QiYeInfo> data) {

        this.context = context;

        GetNoticeInfo();

        this.data = data;


    }


    private void GetNoticeInfo(){

        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");

                String type = object1.getString("type");

                if (type.equals("1")){

                    isShowAdvert = "yes";

                    String image1 = object1.getString("image2");//广告类型
                    advertImages = image1.split("\\|");

                    String image2 = object1.getString("image3");//广告点击类型
                    advertClickTypes = image2.split("\\|");

                    String image4 = object1.getString("image4");//广告点击类型对应内容
                    advertClickContents = image4.split("\\|");

                }else {
                    isShowAdvert = "no";
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();

                param.put("deviceType","androidcompany");

                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public interface MyItemClickListener {
        void onItemClick(View view,int position);
    }
    public interface MyItemLongClickListener {
        void onItemLongClick(View view,int position);
    }
    public void appendData(List<QiYeInfo> list, Context context) {//分页关键
        this.data.addAll(list);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View normal_views = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item2_find_fragment, parent, false);
        return new ViewHolder(normal_views,mItemClickListener,mItemLongClickListener);
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
        QiYeInfo allUser = data.get(position);
        String memberNum = allUser.getMemberNum();
        String image1 = allUser.getZyImage1();
        String image2 = allUser.getZyImage2();
        String image3 = allUser.getZyImage3();
        String image4 = allUser.getZyImage4();
        String margan1 = allUser.getMargin1();
        String margan2 = allUser.getMargin2();
        String margan3 = allUser.getMargin3();
        String margan4 = allUser.getMargin4();
        String comSign = allUser.getComSignature();
        String comName = allUser.getCompanyName();
        String resv1 = TextUtils.isEmpty(allUser.getResv1()) ? "" : allUser.getResv1();
        try {
            comName = comName.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            comName = comName.replaceAll("\\+", "%2B");
            comName = URLDecoder.decode(comName, "UTF-8");

            comSign = comSign.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            comSign = comSign.replaceAll("\\+", "%2B");
            comSign = URLDecoder.decode(comSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if ("0".equals(resv1)) {
            holder.tv_memberNum.setText("隐藏");
        } else {
            holder.tv_memberNum.setText(memberNum + "人");
        }
        holder.tvName.setText(comName);
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
        holder.tvProject1.setText(allUser.getUpName1());
        holder.tvProject2.setText(allUser.getUpName2());
        holder.tvProject3.setText(allUser.getUpName3());
        holder.tvProject4.setText(allUser.getUpName4());
        if (!"".equals(image1) && image1 != null) {
            holder.ivZYTP1.setVisibility(View.VISIBLE);
        } else {
            holder.ivZYTP1.setVisibility(View.INVISIBLE);
        }
        if (margan1 != null) {
            if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                holder.tvBao1.setVisibility(View.VISIBLE);
            } else {
                holder.tvBao1.setVisibility(View.INVISIBLE);
            }
        }
        if (!"".equals(image2) && image2 != null) {
            holder.ivZYTP2.setVisibility(View.VISIBLE);
        } else {
            holder.ivZYTP2.setVisibility(View.INVISIBLE);
        }
        if (margan2 != null) {
            if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                holder.tvBao2.setVisibility(View.VISIBLE);
            } else {
                holder.tvBao2.setVisibility(View.INVISIBLE);
            }
        }
        if (!"".equals(image3) && image3 != null) {
            holder.ivZYTP3.setVisibility(View.VISIBLE);
        } else {
            holder.ivZYTP3.setVisibility(View.INVISIBLE);
        }
        if (margan3 != null) {
            if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                holder.tvBao3.setVisibility(View.VISIBLE);
            } else {
                holder.tvBao3.setVisibility(View.INVISIBLE);
            }
        }
        if (!"".equals(image4) && image4 != null) {
            holder.ivZYTP4.setVisibility(View.VISIBLE);
        } else {
            holder.ivZYTP4.setVisibility(View.INVISIBLE);
        }
        if (margan4 != null) {
            if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                holder.tvBao4.setVisibility(View.VISIBLE);
            } else {
                holder.tvBao4.setVisibility(View.INVISIBLE);
            }
        }
        String head = TextUtils.isEmpty(allUser.getComImage()) ? "" : allUser.getComImage();
        if (head.length() > 40) {
            holder.tvTitleA.setVisibility(View.INVISIBLE);
            holder.ivHead.setVisibility(View.VISIBLE);
            String[] orderProjectArray = head.split("\\|");
            head = orderProjectArray[0];
        }
        if (!(head.equals("") || head.equals(null))) {
            holder.tvTitleA.setVisibility(View.INVISIBLE);
            holder.ivHead.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + head, holder.ivHead, DemoApplication.mOptions);
        } else {
            holder.ivHead.setVisibility(View.INVISIBLE);
            holder.tvTitleA.setVisibility(View.VISIBLE);
            holder.tvTitleA.setText("企");
        }
        String qiyeLat = TextUtils.isEmpty(allUser.getComLatitude()) ? "39.904690" : allUser.getComLatitude();
        String qiyeLon = TextUtils.isEmpty(allUser.getComLongitude()) ? "116.407170" : allUser.getComLongitude();
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
            holder.tvDistance.setText("隐藏");
        }else {
            holder.tvDistance.setText(str + "km");
        }
        holder.tvQianming.setText(comSign);


        if (isShowAdvert.equals("yes") && position%10==0 && position>0){

            holder.image_advert.setVisibility(View.VISIBLE);

            if (currentTag == advertImages.length || currentTag > advertImages.length){

                currentTag = 0;

            }

            ImageLoader.getInstance().displayImage(FXConstant.URL_ADVERTURL + advertImages[currentTag],holder.image_advert);

            currentTag ++ ;

            holder.image_advert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String type1 = advertClickTypes[currentTag-1];
                    String type2 = advertClickContents[currentTag-1];

                    if (type1.equals("1")){

                        // 打电话
                        if (type2.length() != 11){

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            //url:统一资源定位符
                            //uri:统一资源标示符（更广）
                            intent.setData(Uri.parse("tel:" + "13513895563"));
                            //开启系统拨号器
                            context.startActivity(intent);

                        }else {

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            //url:统一资源定位符
                            //uri:统一资源标示符（更广）
                            intent.setData(Uri.parse("tel:" + type2));
                            //开启系统拨号器
                            context.startActivity(intent);

                        }

                    }else if (type1.equals("2")){

                        if (!DemoHelper.getInstance().isLoggedIn(context)) {

                            Toast.makeText(context,"登陆之后才可以发布招标！",Toast.LENGTH_SHORT).show();

                        }else {

                            //发布需求
                            Intent intent2 = new Intent(context, MomentsPublishActivity.class);
                            intent2.putExtra("biaoshi", "xuqiu");
                            context.startActivity(intent2);

                        }

                    }else if (type1.equals("3")){

                        //下载app  跳转app对应详情

                        Intent intent2 = new Intent(context, AppDownDetailActivity.class);
                        intent2.putExtra("appType", "0");
                        intent2.putExtra("appIdentify", type2);
                        context.startActivity(intent2);

                    }else if (type1.equals("4")){

                        //打开网页

                        Uri uri = Uri.parse(type2);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);

                    }else if (type1.equals("5")){

                        //跳转应用内网页  可以电话咨询或者聊天咨询

                        Intent intent2 = new Intent(context, SouSuoAdvertClickActivity.class);
                        intent2.putExtra("url", type2);
                        context.startActivity(intent2);

                    }

                }
            });


        }else {

            holder.image_advert.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return (data == null || data.size() == 0)?0:data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        RelativeLayout rl_sex;
        TextView tvName,tvNianLing;
        TextView tvCompany,tv_memberNum;
        TextView tvTitleA,tvBao1,tvBao2,tvBao3,tvBao4,tvBu1,tvBu2,tvBu3,tvBu4,ivZYTP1,ivZYTP2,ivZYTP3,ivZYTP4;
        TextView tvDistance;
        ImageView ivSex;
        LinearLayout card;
        CircleImageView ivHead;
        TextView tvProject1,tvProject2,tvProject3,tvProject4;
        TextView tvQianming;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        ImageView image_advert;

        public ViewHolder(View convertView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(convertView);
            card = (LinearLayout) convertView.findViewById(R.id.card);
            rl_sex = (RelativeLayout) convertView.findViewById(R.id.rl_sex);
            tv_memberNum = (TextView) convertView.findViewById(R.id.tv_memberNum);
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
            image_advert = (ImageView) convertView.findViewById(R.id.image_advert);

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
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.mItemLongClickListener = listener;
    }
}
