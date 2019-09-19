package com.sangu.apptongji.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.fanxin.easeui.EaseConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.CommentRewardActivity;
import com.sangu.apptongji.main.activity.SouSuoAdvertClickActivity;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.ChatActivity;
import com.sangu.apptongji.widget.CircleImageView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-10-17.
 */

public class DynamicCommentAdapter extends BaseAdapter {

    private Context context;
    List<JSONObject> data;
    String dType;
    String isAdvert;
    String advertUrl;
    public MyItemClickListener mItemClickListener;
    private int width;
    private int currentTag = 1;
    private String[] strings = {};


    public DynamicCommentAdapter(Context context, List<JSONObject> data, String dType, String isAdvert, String advertUrl, int screenWidth) {
        this.context = context;
        this.data = data;
        this.dType = dType;
        this.isAdvert = isAdvert;
        this.advertUrl = advertUrl;

        width  = screenWidth;

        SharedPreferences sp = context.getSharedPreferences("sangu_gonggao_info", Context.MODE_PRIVATE);

        if (sp!=null) {
            currentTag = Integer.valueOf(sp.getString("currentTag","1"));
        }

        if (currentTag == 2){

            if (sp != null) {
                SharedPreferences.Editor editor1 = sp.edit();
                editor1.putString("currentTag", "3");
                editor1.commit();
            }

        }else if (currentTag == 3){

            if (sp != null) {
                SharedPreferences.Editor editor1 = sp.edit();
                editor1.putString("currentTag", "1");
                editor1.commit();
            }

        }else {

            if (sp != null) {
                SharedPreferences.Editor editor1 = sp.edit();
                editor1.putString("currentTag", "2");
                editor1.commit();
            }
        };

        GetNotice();

    }

    //查询广告
    private void GetNotice(){

        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");

                //记录对应的条跳转 分别是image2 3 4 的跳转链接
                String type3 = object1.getString("type3");
                strings = type3.split("\\|");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("deviceType","android8");
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position);
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

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamiccomment, parent, false);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content_pinglun);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_pinglun_time);
            holder.iconImage = (CircleImageView) convertView.findViewById(R.id.iv_head);
            holder.tv_noImageName = (TextView) convertView.findViewById(R.id.tv_notImageName);
            holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
            holder.tv_userPro = (TextView) convertView.findViewById(R.id.tv_pro);
            holder.tv_reward = (TextView) convertView.findViewById(R.id.tv_reward);
            holder.tv_praise = (TextView) convertView.findViewById(R.id.tv_praise);
            holder.tv_commentType = (TextView) convertView.findViewById(R.id.tv_commentType);
            holder.rl_pinglun = (RelativeLayout) convertView.findViewById(R.id.rl_pinglun);
            holder.tv_distence = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.tv_recomIdentify = (TextView) convertView.findViewById(R.id.tv_recomIdentify);
            holder.tv_recomIdentify2 = (TextView) convertView.findViewById(R.id.tv_recomIdentify2);
            holder.image_advert = (ImageView) convertView.findViewById(R.id.image_advert);
            holder.rl_bg = (RelativeLayout) convertView.findViewById(R.id.rl_bg);

            convertView.setTag(holder);
        }
       final JSONObject object = data.get(position);
       // setCommentTextClick(holder.tv_content,object);

        JSONObject userInfo = object.getJSONObject("userInfo");
        com.alibaba.fastjson.JSONArray userProfession = userInfo.getJSONArray("userProfessions");


        String shareRed = userInfo.getString("shareRed");
        if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0) {
            holder.tv_userName.setTextColor(Color.RED);
        }

        String imageStr = TextUtils.isEmpty(userInfo.getString("uImage")) ? "" : userInfo.getString("uImage");

        String[] orderProjectArray = imageStr.split("\\|");
        imageStr = orderProjectArray[0];

        if (!(imageStr == "")) {

            holder.iconImage.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+imageStr,holder.iconImage);
            holder.tv_noImageName.setVisibility(View.INVISIBLE);

        }else {

            holder.iconImage.setVisibility(View.INVISIBLE);
            holder.tv_noImageName.setVisibility(View.VISIBLE);
            holder.tv_noImageName.setText(object.getString("userName"));

        }

        holder.tv_userName.setText(object.getString("userName"));

        holder.iconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,object.getString("userId")));
            }
        });

        holder.tv_noImageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,object.getString("userId")));
            }
        });

        if (object.getString("type") != null){

            if (object.getString("type").equals("0")){
                holder.tv_commentType.setText("建议方案");
                holder.tv_commentType.setBackgroundColor(Color.parseColor("#00acff"));
            }else if (object.getString("type").equals("1")){
                holder.tv_commentType.setText("详情询问");
                holder.tv_commentType.setBackgroundColor(Color.parseColor("#FF3E4A"));
            }else if (object.getString("type").equals("2")){
                holder.tv_commentType.setText("讨论留言");
                holder.tv_commentType.setBackgroundColor(Color.parseColor("#46c01b"));
            }else if (object.getString("type").equals("3")){
                holder.tv_commentType.setText("回    复");
                holder.tv_commentType.setBackgroundColor(Color.parseColor("#FF8D00"));

            }else if (object.getString("type").equals("4")){
                holder.tv_commentType.setText("投诉举报");
                holder.tv_commentType.setBackgroundColor(Color.parseColor("#FF00FF"));
            }

        }else{

            holder.tv_commentType.setText("建议方案");
            holder.tv_commentType.setBackgroundColor(Color.parseColor("#00acff"));

        }

        String proString = "";
        double marginString = 0;
        if (userProfession!=null&&userProfession.size() > 0) {
           String pl1 = userProfession.getJSONObject(0).getString("upName");
            String margin1 = userProfession.getJSONObject(0).getString("margin");
            if (margin1 != null && margin1.length() != 0 && Double.valueOf(margin1) > 99 ){
                marginString = Double.valueOf(margin1);
            }
            if (pl1.equalsIgnoreCase("null")) {
                pl1 = "";
            }
           if (pl1.equals("")){

           }else {
               proString = pl1;
           }
        }
        if (userProfession.size() > 1) {
           String pl2 = userProfession.getJSONObject(1).getString("upName");
            String margin2 = userProfession.getJSONObject(1).getString("margin");
            if (margin2 != null && margin2.length() != 0 &&  Double.valueOf(margin2) > 99 ){
                marginString = marginString + Double.valueOf(margin2);
            }
            if (pl2.equalsIgnoreCase("null")) {
                pl2 = "";
            }
            if (pl2.equals("")){

            }else {
                proString = proString+" "+pl2;
            }
        }
        if (userProfession.size() > 2) {
           String pl3 = userProfession.getJSONObject(2).getString("upName");
            String margin3 = userProfession.getJSONObject(2).getString("margin");
            if (margin3 != null && margin3.length() != 0 && Double.valueOf(margin3) > 99 ){

                marginString = marginString + Double.valueOf(margin3);
            }
            if (pl3.equalsIgnoreCase("null")) {
                pl3 = "";
            }
            if (pl3.equals("")){

            }else {
                proString = proString+" "+pl3;
            }
        }
        if (userProfession.size() > 3) {
           String pl4 = userProfession.getJSONObject(3).getString("upName");
            String margin4 = userProfession.getJSONObject(3).getString("margin");
            if (margin4 != null && margin4.length() != 0 && Double.valueOf(margin4) > 99 ){
                marginString = marginString + Double.valueOf(margin4);
            }
            if (pl4.equalsIgnoreCase("null")) {
                pl4 = "";
            }
            if (pl4.equals("")){

            }else {
                proString = proString+" "+pl4;
            }
        }

        holder.tv_userPro.setText(proString);

        if (marginString>99){
            holder.tv_recomIdentify.setVisibility(View.GONE);
            holder.tv_recomIdentify2.setVisibility(View.VISIBLE);

            if (marginString > 9999){

                holder.tv_recomIdentify2.setText("万元级保障商户");

            }else {


                DecimalFormat df = new DecimalFormat("######0");
                String numberStr = df.format(marginString);

                holder.tv_recomIdentify2.setText("承诺保障"+numberStr);
            }

        }else {

            holder.tv_recomIdentify.setVisibility(View.VISIBLE);
            holder.tv_recomIdentify2.setVisibility(View.GONE);
        }

        String praise = object.getString("praise");
        final String isPraise = object.getString("isPraise");

        if (Integer.valueOf(praise) > 0){

            if (Integer.valueOf(praise) > 99){

                holder.tv_praise.setText("赞 99+");

            }else {
                holder.tv_praise.setText("赞 "+Integer.valueOf(praise));
            }

        }else {

            holder.tv_praise.setText("赞");
        }

        if (isPraise != null && isPraise.equals("yes")){
            holder.tv_praise.setTextColor(Color.parseColor("#00AFFF"));
        }else {
            holder.tv_praise.setTextColor(Color.parseColor("#ffbebebe"));
        }

        String time = object.getString("timeStamp");
        time = time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" "+time.substring(8,10)+":"+time.substring(10,12);
        holder.tv_time.setText(time);

        //处理内容相关

        String[] contentArr = object.getString("content").split("\\|");

        if (contentArr.length>1){
            //回复的
            holder.tv_content.setText("                   "+contentArr[0]+contentArr[1]+contentArr[2]);
         //   String str1=String.format("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+contentArr[0]+"<font color=\"#00AFFF\">%s",contentArr[1]);
           // String str2 = str1+contentArr[2];
          //  holder.tv_content.setText(Html.fromHtml(str1));
            String str1 = "                   ";
            String str2 = contentArr[0];
            String str3 = contentArr[1];
            String str4 = contentArr[2];

            SpannableStringBuilder builder = new SpannableStringBuilder(str1 + str2 + str3 + str4 + "！");
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#00AFFF")),
                    (str1+str2).length(), (str1 + str2 +str3).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            holder.tv_content.setText(builder);

        }else {
            //正常的评论
            holder.tv_content.setText("                   "+object.getString("content"));
        }

        String resv1 = TextUtils.isEmpty(userInfo.getString("resv1")) ? "" : userInfo.getString("resv1");
        String resv2 = TextUtils.isEmpty(userInfo.getString("resv2")) ? "" : userInfo.getString("resv2");
        double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) ? "34.762711" : DemoApplication.getInstance().getCurrentLat());
        double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) ? "113.744531" : DemoApplication.getInstance().getCurrentLng());
        if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {

            final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
            LatLng ll = new LatLng(latitude1,longitude1);
            double distance = DistanceUtil.getDistance(ll, ll1);
            double dou = distance / 1000;
            String str = String.format("%.1f",dou);//format 返回的是字符串
            if (str!=null&&dou>=10000){
                holder.tv_distence.setText("隐藏");
            }else {
                holder.tv_distence.setText(str + "km");
            }

        }else {
            holder.tv_distence.setText("隐藏");
        }

            holder.tv_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (object.getString("userId").equals(DemoHelper.getInstance().getCurrentUsernName())){

                 //   Toast.makeText(context,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

                }else {

                    Intent intent = new Intent(context, CommentRewardActivity.class);
                    intent.putExtra("tagId",object.getString("userId"));
                    intent.putExtra("userName",object.getString("userName"));
                    intent.putExtra("dynamicId",object.getString("dynamicId"));
                    intent.putExtra("createTime",object.getString("createTime"));
                    intent.putExtra("dType",dType);

                    context.startActivity(intent);

                }

            }
        });


        holder.tv_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView v1 = (TextView) v;
                if (isPraise.equals("yes")){
                    //取消点赞

                    if (Integer.valueOf(object.getString("praise"))>0){

                        object.put("praise",""+(Integer.valueOf(object.getString("praise"))-1));
                        object.put("isPraise","no");
                        v1.setTextColor(Color.parseColor("#ffbebebe"));
                        UpdatePraiseCountWithType("减",position);
                    }

                }else {
                    //增加点赞
                    object.put("praise",""+(Integer.valueOf(object.getString("praise"))+1));
                    object.put("isPraise","yes");
                    v1.setTextColor(Color.parseColor("#00AFFF"));
                    UpdatePraiseCountWithType("加",position);
                }

                if (Integer.valueOf(object.getString("praise")) > 0){

                    if (Integer.valueOf(object.getString("praise")) > 99){

                        v1.setText("赞 99+");

                    }else {
                        v1.setText("赞 "+Integer.valueOf(object.getString("praise")));
                    }

                }else {

                    v1.setText("赞");
                }

            }
        });


        holder.rl_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v,position);
            }
        });

        if (position == 4 && isAdvert.equals("1")){

            String[] advertImage = advertUrl.split("\\|");

            String advertImageUrl = advertImage[currentTag-1];

            if (!(advertImageUrl == "")) {

                ViewGroup.LayoutParams lp = holder.rl_bg.getLayoutParams();
                lp.width = width;

                double c = (double) 472/1080;
                double b = width*c;

                int b1 = (int) b;
                lp.height = b1;

                holder.rl_bg.setLayoutParams(lp);


                holder.tv_content.setText("");
                holder.image_advert.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_ADVERTURL+advertImageUrl,holder.image_advert);

            }else {

                ViewGroup.LayoutParams lp = holder.rl_bg.getLayoutParams();
                lp.width = width;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                holder.rl_bg.setLayoutParams(lp);

                holder.image_advert.setVisibility(View.GONE);

            }

        }else {
            ViewGroup.LayoutParams lp = holder.rl_bg.getLayoutParams();
            lp.width = width;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            holder.rl_bg.setLayoutParams(lp);
            holder.image_advert.setVisibility(View.GONE);
        }

        holder.image_advert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String current = strings[currentTag-1];

                if (strings.length > 0){

                    Intent intent2 = new Intent(context, SouSuoAdvertClickActivity.class);
                    intent2.putExtra("url", current);
                    context.startActivity(intent2);

                }else {

                    Intent intent2 = new Intent(context, ChatActivity.class);
                    intent2.putExtra("userId", "13513895563");
                    intent2.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                    intent2.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                    intent2.putExtra(EaseConstant.EXTRA_USER_SHARERED,"无");
                    context.startActivity(intent2);

                }

            }
        });

        return convertView;
    }

    class ViewHolder {
        RelativeLayout rl_pinglun;
        TextView tv_content;
        TextView tv_time;
        CircleImageView iconImage;
        TextView tv_noImageName;
        TextView tv_userName;
        TextView tv_userPro;
        TextView tv_reward;
        TextView tv_praise;
        TextView tv_commentType;
        TextView tv_distence;
        TextView tv_recomIdentify;
        TextView tv_recomIdentify2;
        ImageView image_advert;
        RelativeLayout rl_bg;

    }

    private void UpdatePraiseCountWithType(String type,int position){

       final JSONObject object = data.get(position);
        String url;
        if (type.equals("加")){
            url = FXConstant.URL_AddCOMMENTPRAISE;
        }else {
            url = FXConstant.URL_DELETEPRAISE;
        }

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("DynamicCommentAdapter",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();

                param.put("uId",object.getString("userId"));
                param.put("commentTime",object.getString("timeStamp"));
                param.put("praiseId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("type","0");

                return param;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);

    }

}

