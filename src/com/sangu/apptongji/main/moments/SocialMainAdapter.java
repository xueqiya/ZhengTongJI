package com.sangu.apptongji.main.moments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanxin.easeui.EaseConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.AppDownDetailActivity;
import com.sangu.apptongji.main.activity.DynamicLinkDetailActivity;
import com.sangu.apptongji.main.activity.DynamicRecommendActivity;
import com.sangu.apptongji.main.activity.HbHuoQuActivity;
import com.sangu.apptongji.main.activity.LoginActivity;
import com.sangu.apptongji.main.activity.MessageOrderIntroduceActivity;
import com.sangu.apptongji.main.activity.ProjectDynamicLinkDetailActivity;
import com.sangu.apptongji.main.activity.PushMessageActivity;
import com.sangu.apptongji.main.activity.RedPromoteActivity;
import com.sangu.apptongji.main.activity.SoftAgreementActivity;
import com.sangu.apptongji.main.activity.SoftUserAgreementActivity;
import com.sangu.apptongji.main.activity.SouSuoAdvertClickActivity;
import com.sangu.apptongji.main.activity.TopVipActivity;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.address.AddressListActivity;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuBzLocationActivity;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuFLocationActivity;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuMLocationActivity;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuYuanGongLocationActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.BZJJNActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.BZJZJActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.NewsOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFiveActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFourActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailThreeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailTwoActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.ZhFaActivity;
import com.sangu.apptongji.main.qiye.QiYeDetailsActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.main.utils.SoundPlayUtils;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.ExpandableTextView;
import com.sangu.apptongji.ui.ChatActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.sangu.apptongji.widget.CircleImageView;

import org.json.JSONException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class SocialMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int NORMAL_ITEM = 0;
    public static final int ZHUANFA_ITEM = 1;
    public MyItemClickListener mItemClickListener;
    public MyItemLongClickListener mItemLongClickListener;
    public MyItem2ClickListener mItem2ClickListener;
    public MyItem2LongClickListener mItem2LongClickListener;
    public MyItem3ClickListener mItem3ClickListener;
    public MyItem3LongClickListener mItem3LongClickListener;
    public MyItem4ClickListener mItem4ClickListener;
    public MyItem4LongClickListener mItem4LongClickListener;
    public MyItem5ClickListener mItem5ClickListener;
    public MyItem5LongClickListener mItem5LongClickListener;
    private String typeDetail = null;
    private String profession = null;
    private Activity context;
    private List<JSONObject> users = null;
    public RelativeLayout re_edittext = null;
    private String myuserID = null;
    private String myNick = null;
    private String dType = null, type2 = null;
    private int maxDescripLine = 5;
    private int lastPosition = -1;
    private String responseTime;
    private String resv1;
    private String resv2;
    private String isHaveMargin;
    private int isVip;
    private String vipLevel;
    private String firstDistance;

    private String isShowAdvert = "no";
    private String[] advertImages = {};
    private String[] advertClickTypes = {};
    private String[] advertClickContents = {};
    private int currentTag = 0;
    private String currentType = "0";
    private String vipTitle = "0";
    private String zbShareAuth = "0"; //没有权限分享 只能做任务
    private String isShareDynamic;

    private String isshowMessageTop = "0";

    public SocialMainAdapter(Activity context, List<JSONObject> jsonArray, String type, String profession, String type2, String isHaveMargin, int isVip, String isShare) {

        if (profession == null) {
            this.profession = "";
        } else {
            this.profession = profession;
        }

        GetNoticeInfo();

        this.type2 = type2;
        this.dType = type;
        this.context = context;
        this.users = jsonArray;
        this.isVip = isVip;
        this.isShowAdvert = "no";

        if (dType.equals("05")) {

            if (isHaveMargin == null) {
                this.isHaveMargin = "0";
                this.vipLevel = "1";
                this.currentType = "0";

            } else {
                this.isHaveMargin = isHaveMargin.split("\\|")[0];
                this.vipLevel = isHaveMargin.split("\\|")[1];
                this.currentType = isHaveMargin.split("\\|")[2];

                if (isHaveMargin.split("\\|").length > 3) {
                    this.zbShareAuth = isHaveMargin.split("\\|")[3];
                }

            }

        }

        this.isShareDynamic = isShare;

        RequestQueue queue = MySingleton.getInstance(DemoApplication.applicationContext).getRequestQueue();
        // 底部评论输入框
        if (context != null) {
            re_edittext = (RelativeLayout) context.findViewById(R.id.re_edittext);
        }
        myuserID = DemoHelper.getInstance().getCurrentUsernName();
        myNick = DemoApplication.getInstance().getCurrentUser().getName();

    }

    private void GetNoticeInfo() {

        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");

                String type = object1.getString("type");

                vipTitle = object1.getString("regionRedTime");
                isshowMessageTop = object1.getString("regionRedAmt");

                if (type.equals("1")) {

                    isShowAdvert = "yes";

                    String image1 = object1.getString("image2");//广告类型
                    advertImages = image1.split("\\|");

                    String image2 = object1.getString("image3");//广告点击类型
                    advertClickTypes = image2.split("\\|");

                    String image4 = object1.getString("image4");//广告点击类型对应内容
                    advertClickContents = image4.split("\\|");

                } else {
                    isShowAdvert = "no";
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                if (dType.equals("01")) {

                    //生活动态的
                    param.put("deviceType", "androiddynamic1");

                } else if (dType.equals("02")) {

                    //坐标动态的
                    param.put("deviceType", "androiddynamic2");

                } else if (dType.equals("03")) {
                    //商业动态的
                    param.put("deviceType", "androiddynamic3");

                } else if (dType.equals("06")) {
                    //新闻案例的
                    param.put("deviceType", "androiddynamic6");

                } else {

                    if (dType.equals("05") && currentType.equals("05")) {
                        //首页派单的
                        param.put("deviceType", "androiddynamic");

                    } else if (dType.equals("05") && currentType.equals("01")) {
                        //附近的
                        param.put("deviceType", "androidfujin");

                    } else if (dType.equals("05") && currentType.equals("02")) {
                        //招标的
                        param.put("deviceType", "androiddynazb");

                    } else if (dType.equals("05") && currentType.equals("03")) {
                        //专业相关的
                        param.put("deviceType", "androiddynaxg");

                    } else {

                        param.put("deviceType", "androiddynamic");

                    }

                }

                return param;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    private void addPinglinCount(final String createTime, final String dynamicSeq) {
        String url = FXConstant.URL_ADD_PINGLUN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("DynadateActivity", "评论增加成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("DynadateActivity", "评论增加失败" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("createTime", createTime);
                param.put("dynamicSeq", dynamicSeq);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if ("01".equals(dType)) {
            if (viewType == NORMAL_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_social_main, parent, false);
                return new ViewHolderOne(v, mItemClickListener, mItemLongClickListener);
            } else if (viewType == ZHUANFA_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhfsocial_main2, parent, false);
                return new ViewHolderOnezhf(v, mItemClickListener, mItemLongClickListener);
            }
        }
        if ("02".equals(dType)) {
            if (viewType == NORMAL_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_social_weizhi, parent, false);
                return new ViewHolderTwo(v, mItem2ClickListener, mItem2LongClickListener);
            } else if (viewType == ZHUANFA_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhfsocial_main, parent, false);
                return new ViewHolderTwozhf(v, mItem2ClickListener, mItem2LongClickListener);
            }
        }
        if ("03".equals(dType) || "04".equals(dType)) {
            if (viewType == NORMAL_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2_social_main, parent, false);
                return new ViewHolderThrf(v, mItem3ClickListener, mItem3LongClickListener);
            } else if (viewType == ZHUANFA_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2_zhfsocial_main, parent, false);
                return new ViewHolderThrfzhf(v, mItem3ClickListener, mItem3LongClickListener);
            }
        }
        if ("05".equals(dType)) {
            if (viewType == NORMAL_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item5_social_main, parent, false);
                return new ViewHolderFive(v, mItem4ClickListener, mItem4LongClickListener);
            } else if (viewType == ZHUANFA_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item5_zhfsocial_main, parent, false);
                return new ViewHolderFivezhf(v, mItem4ClickListener, mItem4LongClickListener);
            }
        }

        if ("06".equals(dType)) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newslist_layout, parent, false);
            return new ViewHolderSixzhf(v, mItem5ClickListener, mItem5LongClickListener);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        //生活界面
        if (holder instanceof ViewHolderOne) {
            final JSONObject json = users.get(position);
            // 如果数据出错....
            if (json == null || json.size() == 0) {
                users.remove(position);
                this.notifyDataSetChanged();
            }
            String views = json.getString("views");
            if (views == null || "".equals(views)) {
                views = "0";
            }
            if (views != null && Integer.valueOf(views) > 9999) {

                Double a = Double.valueOf(views) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                views = str + "万";

            }

            ((ViewHolderOne) holder).tv_count_llc.setText(views);
            final String userID = json.getString("uName");
            final String content = json.getString("content");
            String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
            String location = json.getString("location");
            final String sID = json.getString("uLoginId");
            // String token = json.getString("token");
            final String rel_time = json.getString("createTime");
            String countPinglun = json.getString("resv7");
            String video = json.getString("video");
            String videoPictures = json.getString("videoPictures");
            if (countPinglun == null || "".equals(countPinglun)) {
                countPinglun = "0";
            }
            if (countPinglun != null && Integer.valueOf(countPinglun) > 9999) {

                Double a = Double.valueOf(countPinglun) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                countPinglun = str + "万";

            }
            ((ViewHolderOne) holder).tv_count_pl.setText(countPinglun);
            String forwardTimes = json.getString("forwardTimes");
            if (forwardTimes == null || "".equals(forwardTimes)) {
                forwardTimes = "0";
            }
            if (forwardTimes != null && Integer.valueOf(forwardTimes) > 9999) {

                Double a = Double.valueOf(forwardTimes) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                forwardTimes = str + "万";

            }
            ((ViewHolderOne) holder).tv_count_zhf.setText(forwardTimes);
            String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
            String firstImage = "";
            if (!avatar.equals("")) {
                firstImage = avatar.split("\\|")[0];
            }
            final String dynamicSeq = json.getString("dynamicSeq");
            final String fromUId = sID;
            final String finalFirstImage = firstImage;
            if (!avatar.equals("")) {
                String[] orderProjectArray = avatar.split("\\|");
                avatar = orderProjectArray[0];
            }
            String sex = TextUtils.isEmpty(json.getString("uSex")) ? "01" : json.getString("uSex");
            String nianLing = TextUtils.isEmpty(json.getString("uAge")) ? "27" : json.getString("uAge");
            String company = TextUtils.isEmpty(json.getString("uCompany")) ? "暂未加入企业" : json.getString("uCompany");
            String uNation = json.getString("uNation");
            String resv5 = json.getString("resv5");
            String resv6 = json.getString("resv6");
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
            ((ViewHolderOne) holder).tvCompany.setText("(" + company + ")");

            if (json.getString("authType").equals("03")) {

                ((ViewHolderOne) holder).tvCompany.setTextColor(Color.parseColor("#FF0000"));
                ((ViewHolderOne) holder).tvCompany.setText("(仅用户可见)");

            }


            ((ViewHolderOne) holder).tvNianLing.setText(nianLing);
            if ("00".equals(sex)) {
                ((ViewHolderOne) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
                ((ViewHolderOne) holder).tvNianLing.setBackgroundColor(Color.rgb(234, 121, 219));
            } else {
                ((ViewHolderOne) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
                ((ViewHolderOne) holder).tvNianLing.setBackgroundResource(R.color.accent_blue);
            }
            if (!(avatar.equals(""))) {

                ((ViewHolderOne) holder).iv_avatar.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar, ((ViewHolderOne) holder).iv_avatar, DemoApplication.mOptions);
                ((ViewHolderOne) holder).tvTitleA.setVisibility(View.INVISIBLE);

            } else {
                ((ViewHolderOne) holder).iv_avatar.setVisibility(View.INVISIBLE);
                ((ViewHolderOne) holder).tvTitleA.setVisibility(View.VISIBLE);
                ((ViewHolderOne) holder).tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
            }
            ((ViewHolderOne) holder).tv_nick.setText(userID);
            final String shareRed = json.getString("shareRed");
            final String friendsNumber = json.getString("friendsNumber");
            String onceJine = null;
            if (friendsNumber != null && !"".equals(friendsNumber)) {
                onceJine = friendsNumber.split("\\|")[0];
            }
            if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0 && onceJine != null && !"".equals(onceJine) && Double.parseDouble(onceJine) > 0) {
                ((ViewHolderOne) holder).tv_nick.setTextColor(Color.RED);
            } else {
                ((ViewHolderOne) holder).tv_nick.setTextColor(Color.rgb(87, 107, 149));
            }
            ((ViewHolderOne) holder).tv_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            ((ViewHolderOne) holder).tvTitleA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            String resv1 = TextUtils.isEmpty(json.getString("resv1")) ? "" : json.getString("resv1");
            String resv2 = TextUtils.isEmpty(json.getString("resv2")) ? "" : json.getString("resv2");
            double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) ? "34.762711" : DemoApplication.getInstance().getCurrentLat());
            double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) ? "113.744531" : DemoApplication.getInstance().getCurrentLng());
            if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1, longitude1);
                double distance = DistanceUtil.getDistance(ll, ll1);
                double dou = distance / 1000;
                String str = String.format("%.2f", dou);//format 返回的是字符串
                if (str != null && dou >= 10000) {
                    ((ViewHolderOne) holder).tvDistance.setText("隐藏");
                } else {
                    ((ViewHolderOne) holder).tvDistance.setText(str + "km");
                }
            } else {
                ((ViewHolderOne) holder).tvDistance.setText("3km之内");
            }
            final String shareUserId = json.getString("shareUserId");
            // 设置文章中的图片
            ((ViewHolderOne) holder).image_1.setVisibility(View.GONE);
            ((ViewHolderOne) holder).image_2.setVisibility(View.GONE);
            ((ViewHolderOne) holder).image_3.setVisibility(View.GONE);
            ((ViewHolderOne) holder).image_4.setVisibility(View.GONE);
            ((ViewHolderOne) holder).image_5.setVisibility(View.GONE);
            ((ViewHolderOne) holder).image_6.setVisibility(View.GONE);
            ((ViewHolderOne) holder).image_7.setVisibility(View.GONE);
            ((ViewHolderOne) holder).image_8.setVisibility(View.GONE);
            View v2 = null;
            if (video != null && videoPictures != null) {
                ((ViewHolderOne) holder).rl_video.setVisibility(View.VISIBLE);
                boolean setUp = ((ViewHolderOne) holder).videoPlayer.setUp(FXConstant.URL_VIDEO + video, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        "");
                v2 = ((ViewHolderOne) holder).rl_video;
                if (setUp) {
                    Glide.with(context).load(FXConstant.URL_VIDEO + videoPictures)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .error(R.drawable.default_error)
                            .crossFade().into(((ViewHolderOne) holder).videoPlayer.thumbImageView);
                } else {
                    Toast.makeText(context, "视频播放失败", Toast.LENGTH_SHORT).show();
                }
                ((ViewHolderOne) holder).tv_video.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.e("socialmain,", "touch");
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            updateLiulancishu(rel_time, dynamicSeq);
                            updateDeLiulancishu(rel_time, sID);
                        }
                        return false;
                    }
                });
            } else {
                ((ViewHolderOne) holder).rl_video.setVisibility(View.GONE);
                if (!imageStr.equals("")) {
                    String[] images = imageStr.split("\\|");
                    int imNumb = images.length;
                    ((ViewHolderOne) holder).image_1.setVisibility(View.VISIBLE);
                    v2 = ((ViewHolderOne) holder).image_1;
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[0], ((ViewHolderOne) holder).image_1, DemoApplication.mOptions2);
                    if (shareUserId != null) {
                        ((ViewHolderOne) holder).image_1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (shareUserId.length() > 11) {
                                    context.startActivity(new Intent(context, QiYeDetailsActivity.class).putExtra("qiyeId", shareUserId));
                                } else {
                                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, shareUserId));
                                }
                            }
                        });
                    } else {
                        ((ViewHolderOne) holder).image_1.setOnClickListener(new ImageListener(images, 0, rel_time, dynamicSeq, sID));
                    }
                    // 五张图的时间情况比较特殊
                    if (imNumb > 1) {
                        ((ViewHolderOne) holder).image_2.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[1], ((ViewHolderOne) holder).image_2, DemoApplication.mOptions2);
                        ((ViewHolderOne) holder).image_2.setOnClickListener(new ImageListener(images, 1, rel_time, dynamicSeq, sID));
                        if (imNumb > 2) {
                            ((ViewHolderOne) holder).image_3.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[2], ((ViewHolderOne) holder).image_3, DemoApplication.mOptions2);
                            ((ViewHolderOne) holder).image_3.setOnClickListener(new ImageListener(images, 2, rel_time, dynamicSeq, sID));
                            if (imNumb > 3) {
                                ((ViewHolderOne) holder).image_4.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[3], ((ViewHolderOne) holder).image_4, DemoApplication.mOptions2);
                                ((ViewHolderOne) holder).image_4.setOnClickListener(new ImageListener(images, 3, rel_time, dynamicSeq, sID));
                                if (imNumb > 4) {
                                    ((ViewHolderOne) holder).image_5.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[4], ((ViewHolderOne) holder).image_5, DemoApplication.mOptions2);
                                    ((ViewHolderOne) holder).image_5.setOnClickListener(new ImageListener(images, 4, rel_time, dynamicSeq, sID));
                                    if (imNumb > 5) {
                                        ((ViewHolderOne) holder).image_6.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[5], ((ViewHolderOne) holder).image_6, DemoApplication.mOptions2);
                                        ((ViewHolderOne) holder).image_6.setOnClickListener(new ImageListener(images, 5, rel_time, dynamicSeq, sID));
                                        if (imNumb > 6) {
                                            ((ViewHolderOne) holder).image_7.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[6], ((ViewHolderOne) holder).image_7, DemoApplication.mOptions2);
                                            ((ViewHolderOne) holder).image_7.setOnClickListener(new ImageListener(images, 6, rel_time, dynamicSeq, sID));
                                            if (imNumb > 7) {
                                                ((ViewHolderOne) holder).image_8.setVisibility(View.VISIBLE);
                                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[7], ((ViewHolderOne) holder).image_8, DemoApplication.mOptions2);
                                                ((ViewHolderOne) holder).image_8.setOnClickListener(new ImageListener(images, 7, rel_time, dynamicSeq, sID));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 显示位置
//                if (location != null && !location.equals("0")) {
//                    holder.tv_location.setVisibility(View.VISIBLE);
//                    holder.tv_location.setText(location);
//                }
            // 显示文章内容
            // .setText(content);
            ((ViewHolderOne) holder).tv_content.setText(content, position);
            ((ViewHolderOne) holder).tv_content.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
            ((ViewHolderOne) holder).tv_content.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        String type;
                        if (json.getString("fromUId") != null && !json.getString("fromUId").equalsIgnoreCase("null")) {
                            type = "02";
                        } else {
                            type = "01";
                        }
                        Intent intent = new Intent(context, DynaDetaActivity.class);
                        intent.putExtra("sID", sID);
                        intent.putExtra("dynamicSeq", dynamicSeq);
                        intent.putExtra("createTime", rel_time);
                        intent.putExtra("dType", dType);
                        intent.putExtra("type", type);
                        intent.putExtra("type2", "00");
                        context.startActivityForResult(intent, 0);
                    }

                    return true;
                }
            });
            final String createTime = json.getString("createTime");

            String rel_time2 = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                    + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
            ((ViewHolderOne) holder).tv_time.setText(rel_time2);
            ((ViewHolderOne) holder).iv_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            final String finalCountPinglun = countPinglun;
            ((ViewHolderOne) holder).rl_pinglun.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCommentEditText("00", sID, myuserID, myNick, dynamicSeq, createTime, ((ViewHolderOne) holder).tv_count_pl, Integer.valueOf(finalCountPinglun));
                }
            });

            final View finalV = v2;
            ((ViewHolderOne) holder).rl_zhuanfa.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!DemoHelper.getInstance().isLoggedIn(context)) {
                        Toast.makeText(context, "登陆后才可以转发哦！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showZhfDialog(json.getString("views"), createTime, null, ((ViewHolderOne) holder).card, finalV, "0", dynamicSeq, sID, fromUId, finalFirstImage, userID, content, false, "0");
                }
            });

            if (isShareDynamic.equals("yes")) {

                isShareDynamic = "no";

                final LayoutInflater inflater1 = LayoutInflater.from(context);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog collectionDialog = new AlertDialog.Builder(context, R.style.Dialog).create();
                collectionDialog.show();
                collectionDialog.getWindow().setContentView(layout1);
                collectionDialog.setCanceledOnTouchOutside(true);
                collectionDialog.setCancelable(true);
                TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                title.setText("温馨提示");
                btnOK1.setText("确定");
                btnCancel1.setText("取消");

                title_tv1.setText("是否分享案例动态？");

                btnCancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectionDialog.dismiss();
                    }
                });

                btnOK1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        collectionDialog.dismiss();
                        showZhfDialog(json.getString("views"), createTime, null, ((ViewHolderOne) holder).card, finalV, "0", dynamicSeq, sID, fromUId, finalFirstImage, userID, content, false, "0");

                    }
                });

            }

            if (json.getString("authType").equals("03")) {

                ((ViewHolderOne) holder).authBtn3.setText("取消隐藏");
                ((ViewHolderOne) holder).authBtn3.setBackgroundResource(R.drawable.btn_corner_orange7);
                ((ViewHolderOne) holder).tvCompany.setText("(仅用户自己可见)");
                ((ViewHolderOne) holder).tvCompany.setTextColor(Color.parseColor("#FF0000"));

            } else {

                ((ViewHolderOne) holder).authBtn3.setText("隐藏");
                ((ViewHolderOne) holder).authBtn3.setBackgroundResource(R.drawable.btn_corner_blue7);

            }


            ((ViewHolderOne) holder).authBtn3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = FXConstant.URL_UPDATEDYNAMICRECOMD;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            if (json.getString("authType").equals("03")) {

                                json.put("authType", "01");
                                notifyDataSetChanged();
                                Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();

                            } else {

                                json.put("authType", "03");
                                notifyDataSetChanged();
                                Toast.makeText(context, "已隐藏该动态", Toast.LENGTH_SHORT).show();

                            }

                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();

                            param.put("userId", sID);
                            param.put("dynamicSeq", dynamicSeq);
                            param.put("createTime", createTime);

                            if (json.getString("authType").equals("03")) {

                                param.put("authtype", "01");

                            } else {
                                param.put("authtype", "03");
                            }

                            return param;
                        }
                    };

                    MySingleton.getInstance(context).addToRequestQueue(request);

                }
            });


            ((ViewHolderOne) holder).authBtn1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //禁止登录

                    String url = FXConstant.URL_INSERTFREEZELOGIN;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            Toast.makeText(context, "已禁止该用户登录", Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();

                            param.put("u_id", sID);

                            param.put("freezeType", "01");

                            return param;
                        }
                    };

                    MySingleton.getInstance(context).addToRequestQueue(request);

                }
            });

            ((ViewHolderOne) holder).authBtn2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    //禁止所有
                    UpdateUserAuth(sID, "4");

                }
            });

            //生活界面转发
        } else if (holder instanceof ViewHolderOnezhf) {
            final JSONObject json = users.get(position);
            // 如果数据出错....
            if (json == null || json.size() == 0) {
                users.remove(position);
                this.notifyDataSetChanged();
            }
            String views = json.getString("views");
            if (views == null || "".equals(views)) {
                views = "0";
            }
            if (views != null && Integer.valueOf(views) > 9999) {

                Double a = Double.valueOf(views) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                views = str + "万";

            }
            ((ViewHolderOnezhf) holder).tv_count_llc.setText(views);
            final String userID = json.getString("uName");
            final String content = json.getString("content");
            String newContent = TextUtils.isEmpty(json.getString("newcontent")) ? "" : json.getString("newcontent");
            final String firstName = json.getJSONObject("userInfo").getString("uName");
            String firstImage = json.getJSONObject("userInfo").getString("uImage");
            final String firstID = json.getJSONObject("userInfo").getString("uLoginId");
            String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
            String location = json.getString("location");
            final String sID = json.getString("uLoginId");
            // String token = json.getString("token");
            final String rel_time = json.getString("createTime");
            String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
            final String dynamicSeq = json.getString("dynamicSeq");
            final String firstId = json.getJSONObject("userInfo").getString("uLoginId");
            final String video = json.getString("video");
            final String videoPictures = json.getString("videoPictures");
            final String fromUId = sID;
            String countPinglun = json.getString("resv7");
            if (countPinglun == null || "".equals(countPinglun)) {
                countPinglun = "0";
            }
            if (countPinglun != null && Integer.valueOf(countPinglun) > 9999) {

                Double a = Double.valueOf(countPinglun) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                countPinglun = str + "万";

            }
            ((ViewHolderOnezhf) holder).tv_count_pl.setText(countPinglun);
            String forwardTimes = json.getString("forwardTimes");
            if (forwardTimes == null || "".equals(forwardTimes)) {
                forwardTimes = "0";
            }
            if (forwardTimes != null && Integer.valueOf(forwardTimes) > 9999) {

                Double a = Double.valueOf(forwardTimes) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                forwardTimes = str + "万";

            }
            ((ViewHolderOnezhf) holder).tv_count_zhf.setText(forwardTimes);
            if (avatar.length() > 40) {
                String[] orderProjectArray = avatar.split("\\|");
                avatar = orderProjectArray[0];
            }
            String sex = TextUtils.isEmpty(json.getString("uSex")) ? "01" : json.getString("uSex");
            String nianLing = TextUtils.isEmpty(json.getString("uAge")) ? "27" : json.getString("uAge");
            String company = TextUtils.isEmpty(json.getString("uCompany")) ? "暂未加入企业" : json.getString("uCompany");
            String uNation = json.getString("uNation");
            String resv5 = json.getString("resv5");
            String resv6 = json.getString("resv6");
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
            ((ViewHolderOnezhf) holder).tvCompany.setText("(" + company + ")");
            ((ViewHolderOnezhf) holder).tvNianLing.setText(nianLing);
            if ("00".equals(sex)) {
                ((ViewHolderOnezhf) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
                ((ViewHolderOnezhf) holder).tvNianLing.setBackgroundColor(Color.rgb(234, 121, 219));
            } else {
                ((ViewHolderOnezhf) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
                ((ViewHolderOnezhf) holder).tvNianLing.setBackgroundResource(R.color.accent_blue);
            }
            if (!(avatar == "")) {
                ((ViewHolderOnezhf) holder).iv_avatar.setVisibility(View.VISIBLE);
                ((ViewHolderOnezhf) holder).tvTitleA.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar, ((ViewHolderOnezhf) holder).iv_avatar, DemoApplication.mOptions);
            } else {
                ((ViewHolderOnezhf) holder).iv_avatar.setVisibility(View.INVISIBLE);
                ((ViewHolderOnezhf) holder).tvTitleA.setVisibility(View.VISIBLE);
                ((ViewHolderOnezhf) holder).tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
            }
            ((ViewHolderOnezhf) holder).tv_nick.setText(userID);
            final String shareRed = json.getString("shareRed");
            final String friendsNumber = json.getString("friendsNumber");
            String onceJine = null;
            if (friendsNumber != null && !"".equals(friendsNumber)) {
                onceJine = friendsNumber.split("\\|")[0];
            }
            if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0 && onceJine != null && !"".equals(onceJine) && Double.parseDouble(onceJine) > 0) {
                ((ViewHolderOnezhf) holder).tv_nick.setTextColor(Color.RED);
            } else {
                ((ViewHolderOnezhf) holder).tv_nick.setTextColor(Color.rgb(87, 107, 149));
            }
            ((ViewHolderOnezhf) holder).tv_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            ((ViewHolderOnezhf) holder).tvTitleA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            ((ViewHolderOnezhf) holder).tv_zhf_content.setText(newContent, position);
            ((ViewHolderOnezhf) holder).tv_zhf_content.setVisibility(TextUtils.isEmpty(newContent) ? View.GONE : View.VISIBLE);
            ((ViewHolderOnezhf) holder).tv_zhf_content.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        String type;
                        if (json.getString("fromUId") != null && !json.getString("fromUId").equalsIgnoreCase("null")) {
                            type = "02";
                        } else {
                            type = "01";
                        }
                        Log.d("chen", "点击内容1z");
                        Intent intent = new Intent(context, DynaDetaActivity.class);
                        intent.putExtra("sID", sID);
                        intent.putExtra("dynamicSeq", dynamicSeq);
                        intent.putExtra("createTime", rel_time);
                        intent.putExtra("dType", dType);
                        intent.putExtra("type", type);
                        intent.putExtra("type2", "00");
                        context.startActivityForResult(intent, 0);
                    }

                    return true;
                }
            });
            ((ViewHolderOnezhf) holder).tv_first_nick.setText(firstName + ":");
            if (firstImage != null) {
                if (firstImage.length() > 40) {
                    String[] orderProjectArray = firstImage.split("\\|");
                    firstImage = orderProjectArray[0];
                }
            }
//                Log.e("userID",userID);
//                if (firstImage.length() > 40) {
//                    String[] orderProjectArray = firstImage.split("\\|");
//                    firstImage = orderProjectArray[0];
//                }
            final String finalFirstImage = firstImage;
            String firstSex = json.getJSONObject("userInfo").getString("uSex");
            if ("00".equals(firstSex)) {
                ((ViewHolderOnezhf) holder).tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
            } else {
                ((ViewHolderOnezhf) holder).tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
            }
            if (firstImage != null && !"".equals(firstImage)) {
                ((ViewHolderOnezhf) holder).iv_first_avatar.setVisibility(View.VISIBLE);
                ((ViewHolderOnezhf) holder).tvfirst_TitleA.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + firstImage, ((ViewHolderOnezhf) holder).iv_first_avatar, DemoApplication.mOptions);
            } else {
                ((ViewHolderOnezhf) holder).iv_first_avatar.setVisibility(View.INVISIBLE);
                ((ViewHolderOnezhf) holder).tvfirst_TitleA.setVisibility(View.VISIBLE);
                ((ViewHolderOnezhf) holder).tvfirst_TitleA.setText(TextUtils.isEmpty(firstName) ? firstID : firstName);
            }
            ((ViewHolderOnezhf) holder).iv_first_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, firstID));
                }
            });
            ((ViewHolderOnezhf) holder).tv_first_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, firstID));
                }
            });
            ((ViewHolderOnezhf) holder).tv_nick.setText(userID);
            ((ViewHolderOnezhf) holder).tv_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            String resv1 = TextUtils.isEmpty(json.getString("resv1")) ? "" : json.getString("resv1");
            String resv2 = TextUtils.isEmpty(json.getString("resv2")) ? "" : json.getString("resv2");
            double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
            double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());
            if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1, longitude1);
                double distance = DistanceUtil.getDistance(ll, ll1);
                double dou = distance / 1000;
                String str = String.format("%.2f", dou);//format 返回的是字符串
                if (str != null && dou >= 10000) {
                    ((ViewHolderOnezhf) holder).tvDistance.setText("隐藏");
                } else {
                    ((ViewHolderOnezhf) holder).tvDistance.setText(str + "km");
                }
            } else {
                ((ViewHolderOnezhf) holder).tvDistance.setText("3km之内");
            }
            // 设置文章中的图片
            ((ViewHolderOnezhf) holder).image_1.setVisibility(View.GONE);
            ((ViewHolderOnezhf) holder).image_2.setVisibility(View.GONE);
            ((ViewHolderOnezhf) holder).image_3.setVisibility(View.GONE);
            ((ViewHolderOnezhf) holder).image_4.setVisibility(View.GONE);
            ((ViewHolderOnezhf) holder).image_5.setVisibility(View.GONE);
            ((ViewHolderOnezhf) holder).image_6.setVisibility(View.GONE);
            ((ViewHolderOnezhf) holder).image_7.setVisibility(View.GONE);
            ((ViewHolderOnezhf) holder).image_8.setVisibility(View.GONE);
            View v2 = null;
            if (video != null && videoPictures != null) {
                ((ViewHolderOnezhf) holder).rl_video.setVisibility(View.VISIBLE);
                boolean setUp = ((ViewHolderOnezhf) holder).videoPlayer.setUp(FXConstant.URL_VIDEO + video, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        "");
                v2 = ((ViewHolderOnezhf) holder).rl_video;
                if (setUp) {
                    Glide.with(context).load(FXConstant.URL_VIDEO + videoPictures)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .error(R.drawable.default_error)
                            .crossFade().into(((ViewHolderOnezhf) holder).videoPlayer.thumbImageView);
                } else {
                    Toast.makeText(context, "视频播放失败", Toast.LENGTH_SHORT).show();
                }
                ((ViewHolderOnezhf) holder).tv_video.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.e("socialmain,", "touch");
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            updateLiulancishu(rel_time, dynamicSeq);
                            updateDeLiulancishu(rel_time, sID);
                        }
                        return false;
                    }
                });
            } else {
                ((ViewHolderOnezhf) holder).rl_video.setVisibility(View.GONE);
                if (!imageStr.equals("")) {
                    String[] images = imageStr.split("\\|");
                    int imNumb = images.length;
                    ((ViewHolderOnezhf) holder).image_1.setVisibility(View.VISIBLE);
                    v2 = ((ViewHolderOnezhf) holder).image_1;
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[0], ((ViewHolderOnezhf) holder).image_1, DemoApplication.mOptions2);
                    ((ViewHolderOnezhf) holder).image_1.setOnClickListener(new ImageListener(images, 0, rel_time, dynamicSeq, sID));
                    // 五张图的时间情况比较特殊
                    if (imNumb > 1) {
                        ((ViewHolderOnezhf) holder).image_2.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[1], ((ViewHolderOnezhf) holder).image_2, DemoApplication.mOptions2);
                        ((ViewHolderOnezhf) holder).image_2.setOnClickListener(new ImageListener(images, 1, rel_time, dynamicSeq, sID));
                        if (imNumb > 2) {
                            ((ViewHolderOnezhf) holder).image_3.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[2], ((ViewHolderOnezhf) holder).image_3, DemoApplication.mOptions2);
                            ((ViewHolderOnezhf) holder).image_3.setOnClickListener(new ImageListener(images, 2, rel_time, dynamicSeq, sID));
                            if (imNumb > 3) {
                                ((ViewHolderOnezhf) holder).image_4.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[3], ((ViewHolderOnezhf) holder).image_4, DemoApplication.mOptions2);
                                ((ViewHolderOnezhf) holder).image_4.setOnClickListener(new ImageListener(images, 3, rel_time, dynamicSeq, sID));
                                if (imNumb > 4) {
                                    ((ViewHolderOnezhf) holder).image_5.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[4], ((ViewHolderOnezhf) holder).image_5, DemoApplication.mOptions2);
                                    ((ViewHolderOnezhf) holder).image_5.setOnClickListener(new ImageListener(images, 4, rel_time, dynamicSeq, sID));
                                    if (imNumb > 5) {
                                        ((ViewHolderOnezhf) holder).image_6.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[5], ((ViewHolderOnezhf) holder).image_6, DemoApplication.mOptions2);
                                        ((ViewHolderOnezhf) holder).image_6.setOnClickListener(new ImageListener(images, 5, rel_time, dynamicSeq, sID));
                                        if (imNumb > 6) {
                                            ((ViewHolderOnezhf) holder).image_7.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[6], ((ViewHolderOnezhf) holder).image_7, DemoApplication.mOptions2);
                                            ((ViewHolderOnezhf) holder).image_7.setOnClickListener(new ImageListener(images, 6, rel_time, dynamicSeq, sID));
                                            if (imNumb > 7) {
                                                ((ViewHolderOnezhf) holder).image_8.setVisibility(View.VISIBLE);
                                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[7], ((ViewHolderOnezhf) holder).image_8, DemoApplication.mOptions2);
                                                ((ViewHolderOnezhf) holder).image_8.setOnClickListener(new ImageListener(images, 7, rel_time, dynamicSeq, sID));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 显示位置
//                if (location != null && !location.equals("0")) {
//                    holder.tv_location.setVisibility(View.VISIBLE);
//                    holder.tv_location.setText(location);
//                }
            // 显示文章内容
            ((ViewHolderOnezhf) holder).tv_content2.setText(content);
            String rel_time2 = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                    + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
            ((ViewHolderOnezhf) holder).tv_time.setText(rel_time2);
            ((ViewHolderOnezhf) holder).iv_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            final String createTime = json.getString("createTime");
            final String finalCountPinglun = countPinglun;
            ((ViewHolderOnezhf) holder).rl_pinglun.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCommentEditText("01", sID, myuserID, myNick, dynamicSeq, createTime, ((ViewHolderOnezhf) holder).tv_count_pl, Integer.valueOf(finalCountPinglun));
                }
            });
            final View finalV = v2;
            ((ViewHolderOnezhf) holder).rl_zhuanfa.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!DemoHelper.getInstance().isLoggedIn(context)) {
                        Toast.makeText(context, "登陆后才可以转发哦！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showZhfDialog(json.getString("views"), createTime, null, ((ViewHolderOnezhf) holder).card, finalV, "0", dynamicSeq, firstId, fromUId, finalFirstImage, firstName, content, false, "0");
                }
            });
            //坐标界面
        } else if (holder instanceof ViewHolderTwo) {
            final JSONObject json = users.get(position);
            // 如果数据出错....
            if (json == null || json.size() == 0) {
                users.remove(position);
                this.notifyDataSetChanged();
            }
            String views = json.getString("views");
            if (views == null || "".equals(views)) {
                views = "0";
            }
            if (views != null && Integer.valueOf(views) > 9999) {

                Double a = Double.valueOf(views) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                views = str + "万";
            }
            ((ViewHolderTwo) holder).tv_count_llc.setText(views);
            final String redImage = json.getString("redImage");
            String redTime = json.getString("redTime");
            final String gameRed = json.getString("gameRed");
            String redSum = json.getString("redSum");
            final String userID = json.getString("uName");
            final String content = json.getString("content");
            String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
            final String sID = json.getString("uLoginId");
            final String dynamicSeq = json.getString("dynamicSeq");
            String location1 = TextUtils.isEmpty(json.getString("location")) ? "" : json.getString("location");
            final String video = json.getString("video");
            final String videoPictures = json.getString("videoPictures");

            String collection = json.getString("collectionCount");

            if (profession.equals("取消收藏")) {

                ((ViewHolderTwo) holder).tv_collectiontitle.setText("取消收藏");
                ((ViewHolderTwo) holder).tv_collectioncount.setVisibility(View.GONE);

            } else {

                if (Double.valueOf(collection) > 999) {

                    ((ViewHolderTwo) holder).tv_collectioncount.setText("999+");

                } else {

                    ((ViewHolderTwo) holder).tv_collectioncount.setText(collection);

                }

            }


            String lat = "", lng = "", address = "";
            if (redSum == null || "".equals(redSum) || redSum.equalsIgnoreCase("null")) {
                redSum = "0";
            }
            if (!"".equals(location1)) {
                lat = location1.split("\\|")[0];
                lng = location1.split("\\|")[1];
            }
            address = json.getString("price");
            // String token = json.getString("token");
            final String rel_time = json.getString("createTime");
            String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
            String countPinglun = json.getString("resv7");
            if (countPinglun == null || "".equals(countPinglun)) {
                countPinglun = "0";
            }
            if (countPinglun != null && Integer.valueOf(countPinglun) > 9999) {

                Double a = Double.valueOf(countPinglun) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                countPinglun = str + "万";

            }
            ((ViewHolderTwo) holder).tv_count_pl.setText(countPinglun);
            String forwardTimes = json.getString("forwardTimes");
            if (forwardTimes == null || "".equals(forwardTimes)) {
                forwardTimes = "0";
            }
            if (forwardTimes != null && Integer.valueOf(forwardTimes) > 9999) {

                Double a = Double.valueOf(forwardTimes) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                forwardTimes = str + "万";

            }
            ((ViewHolderTwo) holder).tv_count_zhf.setText(forwardTimes);
            String firstImage = "";
            if (!avatar.equals("")) {
                firstImage = avatar.split("\\|")[0];
            }
            final String createTime = json.getString("createTime");
            String locaImage = json.getString("locaImage");
            if (redImage != null && !"".equals(redImage) && !redImage.equalsIgnoreCase("null")) {
                ((ViewHolderTwo) holder).iv_ditu.setVisibility(View.VISIBLE);
                ((ViewHolderTwo) holder).iv_ditu.setImageResource(R.drawable.image_baozang);
                ((ViewHolderTwo) holder).mMapView.setVisibility(View.GONE);
                final String biaoshi;
                if ("0".equals(redSum)) {
                    ((ViewHolderTwo) holder).rl_geshu.setVisibility(View.GONE);
                    ((ViewHolderTwo) holder).iv_bz_type.setVisibility(View.VISIBLE);
                    ((ViewHolderTwo) holder).iv_bz_type.setImageResource(R.drawable.baoxiang_open);
                    biaoshi = "open";
                } else {
                    if ("no".equals(redTime)) {
                        ((ViewHolderTwo) holder).iv_bz_type.setVisibility(View.GONE);
                        ((ViewHolderTwo) holder).rl_geshu.setVisibility(View.VISIBLE);
                        ((ViewHolderTwo) holder).tv_geshu.setText(redSum);
                        biaoshi = "close";
                    } else {
                        String nowTime = getNowTime();
                        if (Long.parseLong(nowTime) < Long.parseLong(redTime)) {
                            ((ViewHolderTwo) holder).iv_bz_type.setVisibility(View.GONE);
                            ((ViewHolderTwo) holder).rl_geshu.setVisibility(View.VISIBLE);
                            ((ViewHolderTwo) holder).tv_geshu.setText(redSum);
                            biaoshi = "close";
                        } else {
                            ((ViewHolderTwo) holder).iv_bz_type.setVisibility(View.GONE);
                            ((ViewHolderTwo) holder).rl_geshu.setVisibility(View.GONE);
                            biaoshi = "none";
                        }
                    }
                }
                final String finalLat = lat;
                final String finalLng = lng;
                final String finalRedSum = redSum;
                ((ViewHolderTwo) holder).iv_ditu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!DemoHelper.getInstance().isLoggedIn(context)) {

                            Toast.makeText(context, "登陆后才可以操作哦！", Toast.LENGTH_SHORT).show();

                        } else {

                            SharedPreferences sp = context.getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
                            String location = sp.getString("location", null);
                            ScreenshotUtil.getBitmapByView(context, ((ViewHolderTwo) holder).rl_neirong, "分享名片红包", location, 3, false, 0, 0);
                            context.startActivity(new Intent(context, BaiDuBzLocationActivity.class).putExtra("biaoshi", biaoshi).putExtra("bzlat", finalLat)
                                    .putExtra("bzlng", finalLng).putExtra("pintuUrl", redImage).putExtra("createTime", createTime).putExtra("user_id", sID)
                                    .putExtra("dynamicSeq", dynamicSeq).putExtra("gameRed", gameRed).putExtra("name", userID).putExtra("geshu", finalRedSum));
                        }
                    }
                });
            } else {
                ((ViewHolderTwo) holder).iv_bz_type.setVisibility(View.GONE);
                ((ViewHolderTwo) holder).rl_geshu.setVisibility(View.GONE);
                if (locaImage != null && !"".equals(locaImage) && !locaImage.equalsIgnoreCase("null")) {
                    ((ViewHolderTwo) holder).iv_ditu.setVisibility(View.VISIBLE);
                    ((ViewHolderTwo) holder).mMapView.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + locaImage, ((ViewHolderTwo) holder).iv_ditu);
                } else {
                    ((ViewHolderTwo) holder).mMapView.setText(address);
                    ((ViewHolderTwo) holder).mMapView.setVisibility(View.VISIBLE);
                    ((ViewHolderTwo) holder).iv_ditu.setVisibility(View.GONE);
                }
                if (!"".equals(lat) && !"".equals(lng)) {
                    final String finalLat = lat;
                    final String finalLng = lng;
                    ((ViewHolderTwo) holder).mMapView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!DemoHelper.getInstance().isLoggedIn(context)) {
                                Toast.makeText(context, "登陆后才可以操作哦！", Toast.LENGTH_SHORT).show();
                            } else {
                                context.startActivity(new Intent(context, BaiDuYuanGongLocationActivity.class).putExtra("biaoshi", "01").putExtra("dynaLat", finalLat).putExtra("dynaLng", finalLng));
                            }
                        }
                    });

                    ((ViewHolderTwo) holder).iv_ditu.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!DemoHelper.getInstance().isLoggedIn(context)) {
                                Toast.makeText(context, "登陆后才可以操作哦！", Toast.LENGTH_SHORT).show();
                            } else {

                                //坐标动态点击跳转地图
                                final List<String> strLat = new ArrayList<>();
                                final List<String> strLong = new ArrayList<>();
                                final List<String> strLoginId = new ArrayList<>();
                                final List<String> strName = new ArrayList<>();
                                final List<String> strSex = new ArrayList<>();
                                Intent intent = new Intent(context, BaiDuMLocationActivity.class);
                                intent.putExtra("lat", (Serializable) strLat);
                                intent.putExtra("lng", (Serializable) strLong);
                                intent.putExtra("loginId", (Serializable) strLoginId);
                                intent.putExtra("name", (Serializable) strName);
                                intent.putExtra("sex", (Serializable) strSex);
                                intent.putExtra("mlat", finalLat);
                                intent.putExtra("mlon", finalLng);
                                intent.putExtra("sID", sID);
                                intent.putExtra("locationDynamic", "yes");//标示动态跳转到首页那个地图里处理逻辑
                                context.startActivityForResult(intent, 1);

                                //  context.startActivity(new Intent(context, BaiDuYuanGongLocationActivity.class).putExtra("biaoshi", "01").putExtra("dynaLat", finalLat).putExtra("dynaLng", finalLng));

                            }
                        }
                    });
                }
            }
            if (!avatar.equals("")) {
                String[] orderProjectArray = avatar.split("\\|");
                avatar = orderProjectArray[0];
            }
            String sex = TextUtils.isEmpty(json.getString("uSex")) ? "01" : json.getString("uSex");
            String nianLing = TextUtils.isEmpty(json.getString("uAge")) ? "27" : json.getString("uAge");
            String company = TextUtils.isEmpty(json.getString("uCompany")) ? "暂未加入企业" : json.getString("uCompany");
            String uNation = json.getString("uNation");
            String resv5 = json.getString("resv5");
            String resv6 = json.getString("resv6");
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
            ((ViewHolderTwo) holder).tvCompany.setText("(" + company + ")");

            if (json.getString("authType").equals("03")) {

                ((ViewHolderTwo) holder).tvCompany.setTextColor(Color.parseColor("#FF0000"));
                ((ViewHolderTwo) holder).tvCompany.setText("(仅用户可见)");

            }


            ((ViewHolderTwo) holder).tvNianLing.setText(nianLing);
            if ("00".equals(sex)) {
                ((ViewHolderTwo) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
                ((ViewHolderTwo) holder).tvNianLing.setBackgroundColor(Color.rgb(234, 121, 219));
            } else {
                ((ViewHolderTwo) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
                ((ViewHolderTwo) holder).tvNianLing.setBackgroundResource(R.color.accent_blue);
            }
            if (!("".equals(avatar))) {
                ((ViewHolderTwo) holder).iv_avatar.setVisibility(View.VISIBLE);
                ((ViewHolderTwo) holder).tvTitleA.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar, ((ViewHolderTwo) holder).iv_avatar, DemoApplication.mOptions);
            } else {
                ((ViewHolderTwo) holder).iv_avatar.setVisibility(View.INVISIBLE);
                ((ViewHolderTwo) holder).tvTitleA.setVisibility(View.VISIBLE);
                ((ViewHolderTwo) holder).tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
            }
            ((ViewHolderTwo) holder).tv_nick.setText(userID);
            final String shareRed = json.getString("shareRed");
            final String friendsNumber = json.getString("friendsNumber");
            String onceJine = null;
            if (friendsNumber != null && !"".equals(friendsNumber)) {
                onceJine = friendsNumber.split("\\|")[0];
            }
            if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0 && onceJine != null && !"".equals(onceJine) && Double.parseDouble(onceJine) > 0) {
                ((ViewHolderTwo) holder).tv_nick.setTextColor(Color.RED);
            } else {
                ((ViewHolderTwo) holder).tv_nick.setTextColor(Color.rgb(87, 107, 149));
            }
            ((ViewHolderTwo) holder).tv_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            ((ViewHolderTwo) holder).tvTitleA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            String resv1 = TextUtils.isEmpty(json.getString("resv1")) ? "" : json.getString("resv1");
            String resv2 = TextUtils.isEmpty(json.getString("resv2")) ? "" : json.getString("resv2");
            double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
            double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());
            if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1, longitude1);
                double distance = DistanceUtil.getDistance(ll, ll1);
                double dou = distance / 1000;
                String str = String.format("%.2f", dou);//format 返回的是字符串
                if (str != null && dou >= 10000) {
                    ((ViewHolderTwo) holder).tvDistance.setText("隐藏");
                } else {
                    ((ViewHolderTwo) holder).tvDistance.setText(str + "km");
                }
            } else {
                ((ViewHolderTwo) holder).tvDistance.setText("3km之内");
            }
            // 设置文章中的图片
            ((ViewHolderTwo) holder).image_1.setVisibility(View.GONE);
            ((ViewHolderTwo) holder).image_2.setVisibility(View.GONE);
            ((ViewHolderTwo) holder).image_3.setVisibility(View.GONE);
            ((ViewHolderTwo) holder).image_4.setVisibility(View.GONE);
            ((ViewHolderTwo) holder).image_5.setVisibility(View.GONE);
            ((ViewHolderTwo) holder).image_6.setVisibility(View.GONE);
            ((ViewHolderTwo) holder).image_7.setVisibility(View.GONE);
            ((ViewHolderTwo) holder).image_8.setVisibility(View.GONE);
            View v2 = null;
            if (video != null && videoPictures != null) {
                ((ViewHolderTwo) holder).rl_video.setVisibility(View.VISIBLE);
                boolean setUp = ((ViewHolderTwo) holder).videoPlayer.setUp(FXConstant.URL_VIDEO + video, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        "");
                v2 = ((ViewHolderTwo) holder).rl_video;
                if (setUp) {
                    Glide.with(context).load(FXConstant.URL_VIDEO + videoPictures)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .error(R.drawable.default_error)
                            .crossFade().into(((ViewHolderTwo) holder).videoPlayer.thumbImageView);
                } else {
                    Toast.makeText(context, "视频播放失败", Toast.LENGTH_SHORT).show();
                }
                ((ViewHolderTwo) holder).tv_video.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            Log.e("socialmain,", "touch");
                            updateLiulancishu(rel_time, dynamicSeq);
                            updateDeLiulancishu(rel_time, sID);
                        }
                        return false;
                    }
                });
            } else {
                ((ViewHolderTwo) holder).rl_video.setVisibility(View.GONE);
                if (!imageStr.equals("")) {
                    String[] images = imageStr.split("\\|");
                    int imNumb = images.length;
                    ((ViewHolderTwo) holder).image_1.setVisibility(View.VISIBLE);
                    v2 = ((ViewHolderTwo) holder).image_1;
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[0], ((ViewHolderTwo) holder).image_1, DemoApplication.mOptions2);
                    ((ViewHolderTwo) holder).image_1.setOnClickListener(new ImageListener(images, 0, rel_time, dynamicSeq, sID));
                    if (imNumb > 1) {
                        ((ViewHolderTwo) holder).image_2.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[1], ((ViewHolderTwo) holder).image_2, DemoApplication.mOptions2);
                        ((ViewHolderTwo) holder).image_2.setOnClickListener(new ImageListener(images, 1, rel_time, dynamicSeq, sID));
                        if (imNumb > 2) {
                            ((ViewHolderTwo) holder).image_3.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[2], ((ViewHolderTwo) holder).image_3, DemoApplication.mOptions2);
                            ((ViewHolderTwo) holder).image_3.setOnClickListener(new ImageListener(images, 2, rel_time, dynamicSeq, sID));
                            if (imNumb > 3) {
                                ((ViewHolderTwo) holder).image_4.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[3], ((ViewHolderTwo) holder).image_4, DemoApplication.mOptions2);
                                ((ViewHolderTwo) holder).image_4.setOnClickListener(new ImageListener(images, 3, rel_time, dynamicSeq, sID));
                                if (imNumb > 4) {
                                    ((ViewHolderTwo) holder).image_5.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[4], ((ViewHolderTwo) holder).image_5, DemoApplication.mOptions2);
                                    ((ViewHolderTwo) holder).image_5.setOnClickListener(new ImageListener(images, 4, rel_time, dynamicSeq, sID));
                                    if (imNumb > 5) {
                                        ((ViewHolderTwo) holder).image_6.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[5], ((ViewHolderTwo) holder).image_6, DemoApplication.mOptions2);
                                        ((ViewHolderTwo) holder).image_6.setOnClickListener(new ImageListener(images, 5, rel_time, dynamicSeq, sID));
                                        if (imNumb > 6) {
                                            ((ViewHolderTwo) holder).image_7.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[6], ((ViewHolderTwo) holder).image_7, DemoApplication.mOptions2);
                                            ((ViewHolderTwo) holder).image_7.setOnClickListener(new ImageListener(images, 6, rel_time, dynamicSeq, sID));
                                            if (imNumb > 7) {
                                                ((ViewHolderTwo) holder).image_8.setVisibility(View.VISIBLE);
                                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[7], ((ViewHolderTwo) holder).image_8, DemoApplication.mOptions2);
                                                ((ViewHolderTwo) holder).image_8.setOnClickListener(new ImageListener(images, 7, rel_time, dynamicSeq, sID));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 显示位置
//                if (location != null && !location.equals("0")) {
//                    holder.tv_location.setVisibility(View.VISIBLE);
//                    holder.tv_location.setText(location);
//                }
            // 显示文章内容
            // .setText(content);
            final String fromUId = sID;
            final String finalFirstImage = firstImage;
            final View finalV = v2;

            //坐标动态的收藏点击
            ((ViewHolderTwo) holder).rl_collection.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    final LayoutInflater inflater1 = LayoutInflater.from(context);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog collectionDialog = new AlertDialog.Builder(context, R.style.Dialog).create();
                    collectionDialog.show();
                    collectionDialog.getWindow().setContentView(layout1);
                    collectionDialog.setCanceledOnTouchOutside(true);
                    collectionDialog.setCancelable(true);
                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                    TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                    title.setText("温馨提示");
                    btnOK1.setText("确定");
                    btnCancel1.setText("取消");

                    if (profession.equals("取消收藏")) {
                        title_tv1.setText("是否确定取消收藏？");
                    } else {
                        title_tv1.setText("是否确定添加收藏？");
                    }


                    btnCancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            collectionDialog.dismiss();
                        }
                    });

                    btnOK1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            collectionDialog.dismiss();

                            String url;

                            if (profession.equals("取消收藏")) {

                                url = FXConstant.URL_DELETEDYNAMICCOLLECTION;

                            } else {

                                url = FXConstant.URL_INSERTDYNAMICCOLLECTION;

                            }

                            //String url = FXConstant.URL_INSERTDYNAMICCOLLECTION;

                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {

                                    try {

                                        org.json.JSONObject object = new org.json.JSONObject(s);
                                        String code = object.getString("code");

                                        if (code != null && code.equals("SUCCESS")) {

                                            Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();

                                            if (profession.equals("取消收藏")) {

                                                // users.remove(position);
                                                //  context.notifyDataSetChanged();

                                            }


                                        } else {

                                            Toast.makeText(context, "网络不稳定,请稍后再试", Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (JSONException e) {

                                        e.printStackTrace();

                                    }

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    Toast.makeText(context, "网络不稳定,请稍后再试", Toast.LENGTH_SHORT).show();

                                }

                            }) {

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<>();

                                    param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                    param.put("dynamicId", dynamicSeq);
                                    param.put("createTime", rel_time);
                                    param.put("type", dType);

                                    return param;

                                }

                            };

                            MySingleton.getInstance(context).addToRequestQueue(request);

                        }
                    });

                }
            });

            ((ViewHolderTwo) holder).rl_zhuanfa.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!DemoHelper.getInstance().isLoggedIn(context)) {
                        Toast.makeText(context, "登陆后才可以转发哦！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showZhfDialog(json.getString("views"), rel_time, redImage, ((ViewHolderTwo) holder).card, finalV, "0", dynamicSeq, sID, fromUId, finalFirstImage, userID, content, false, "0");
                }
            });

            if (json.getString("demandType").equals("店")) {
                ((ViewHolderTwo) holder).tv_demandType.setText("店");
                ((ViewHolderTwo) holder).tv_demandType.setVisibility(View.VISIBLE);
                ((ViewHolderTwo) holder).tv_content.setText("      " + content);
                ((ViewHolderTwo) holder).tv_demandType.setBackgroundResource(R.drawable.btn_corner_weizhired);

            } else if (json.getString("demandType").equals("玩")) {
                ((ViewHolderTwo) holder).tv_demandType.setText("玩");
                ((ViewHolderTwo) holder).tv_demandType.setVisibility(View.VISIBLE);
                ((ViewHolderTwo) holder).tv_content.setText("      " + content);
                ((ViewHolderTwo) holder).tv_demandType.setBackgroundResource(R.drawable.btn_corner_weizhigreen);

            } else {

                ((ViewHolderTwo) holder).tv_demandType.setVisibility(View.GONE);
                ((ViewHolderTwo) holder).tv_content.setText(content);
            }

            ((ViewHolderTwo) holder).tv_content.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        String type;
                        if (json.getString("fromUId") != null && !json.getString("fromUId").equalsIgnoreCase("null")) {
                            type = "02";
                        } else {
                            type = "01";
                        }

                        Intent intent = new Intent(context, DynaDetaActivity.class);
                        intent.putExtra("sID", sID);
                        intent.putExtra("dynamicSeq", dynamicSeq);
                        intent.putExtra("createTime", rel_time);
                        intent.putExtra("dType", dType);
                        intent.putExtra("type", type);
                        intent.putExtra("type2", "00");
                        context.startActivityForResult(intent, 0);

                    }
                    ;
                    return true;
                }
            });
            String rel_time2 = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                    + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
            ((ViewHolderTwo) holder).tv_time.setText(rel_time2);
            ((ViewHolderTwo) holder).iv_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            final String finalCountPinglun = countPinglun;
            ((ViewHolderTwo) holder).rl_pinglun.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCommentEditText("00", sID, myuserID, myNick, dynamicSeq, createTime, ((ViewHolderTwo) holder).tv_count_pl, Integer.valueOf(finalCountPinglun));
                }
            });


            if (json.getString("authType").equals("03")) {

                ((ViewHolderTwo) holder).authBtn3.setText("取消隐藏");
                ((ViewHolderTwo) holder).authBtn3.setBackgroundResource(R.drawable.btn_corner_orange7);
                ((ViewHolderTwo) holder).tvCompany.setText("(仅用户自己可见)");
                ((ViewHolderTwo) holder).tvCompany.setTextColor(Color.parseColor("#FF0000"));

            } else {

                ((ViewHolderTwo) holder).authBtn3.setText("隐藏");
                ((ViewHolderTwo) holder).authBtn3.setBackgroundResource(R.drawable.btn_corner_blue7);

            }


            ((ViewHolderTwo) holder).authBtn3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = FXConstant.URL_UPDATEDYNAMICRECOMD;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            if (json.getString("authType").equals("03")) {

                                json.put("authType", "01");
                                notifyDataSetChanged();
                                Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();

                            } else {

                                json.put("authType", "03");
                                notifyDataSetChanged();
                                Toast.makeText(context, "已隐藏该动态", Toast.LENGTH_SHORT).show();

                            }

                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();

                            param.put("userId", sID);
                            param.put("dynamicSeq", dynamicSeq);
                            param.put("createTime", createTime);

                            if (json.getString("authType").equals("03")) {

                                param.put("authtype", "01");

                            } else {
                                param.put("authtype", "03");
                            }

                            return param;
                        }
                    };

                    MySingleton.getInstance(context).addToRequestQueue(request);

                }
            });


            ((ViewHolderTwo) holder).authBtn1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //禁止登录

                    String url = FXConstant.URL_INSERTFREEZELOGIN;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            Toast.makeText(context, "已禁止该用户登录", Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();

                            param.put("u_id", sID);

                            param.put("freezeType", "01");

                            return param;
                        }
                    };

                    MySingleton.getInstance(context).addToRequestQueue(request);

                }
            });


            ((ViewHolderTwo) holder).authBtn2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    //禁止所有
                    UpdateUserAuth(sID, "4");

                }
            });

            //坐标界面转发
        } else if (holder instanceof ViewHolderTwozhf) {
            final JSONObject json = users.get(position);
            // 如果数据出错....
            if (json == null || json.size() == 0) {
                users.remove(position);
                this.notifyDataSetChanged();
            }
            String views = json.getString("views");
            if (views == null || "".equals(views)) {
                views = "0";
            }
            if (views != null && Integer.valueOf(views) > 9999) {

                Double a = Double.valueOf(views) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                views = str + "万";

            }
            ((ViewHolderTwozhf) holder).tv_count_llc.setText(views);
            final String userID = json.getString("uName");
            final String content = json.getString("content");
            String newContent = TextUtils.isEmpty(json.getString("newcontent")) ? "" : json.getString("newcontent");
            final String firstName = json.getJSONObject("userInfo").getString("uName");
            String firstImage = json.getJSONObject("userInfo").getString("uImage");
            String firstSex = json.getJSONObject("userInfo").getString("uSex");
            final String firstID = json.getJSONObject("userInfo").getString("uLoginId");
            String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
            String location = json.getString("location");
            final String sID = json.getString("uLoginId");
            final String createTime = json.getString("createTime");
            // String token = json.getString("token");
            final String rel_time = json.getString("createTime");
            String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
            final String dynamicSeq = json.getString("dynamicSeq");
            String countPinglun = json.getString("resv7");
            final String redImage = json.getString("redImage");
            String redTime = json.getString("redTime");
            final String gameRed = json.getString("gameRed");
            String redSum = json.getString("redSum");
            final String video = json.getString("video");
            final String videoPictures = json.getString("videoPictures");
            if (redSum == null || "".equals(redSum) || redSum.equalsIgnoreCase("null")) {
                redSum = "0";
            }
            if (countPinglun == null || "".equals(countPinglun)) {
                countPinglun = "0";
            }
            if (countPinglun != null && Integer.valueOf(countPinglun) > 9999) {

                Double a = Double.valueOf(countPinglun) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                countPinglun = str + "万";

            }
            ((ViewHolderTwozhf) holder).tv_count_pl.setText(countPinglun);
            String forwardTimes = json.getString("forwardTimes");
            if (forwardTimes == null || "".equals(forwardTimes)) {
                forwardTimes = "0";
            }
            if (forwardTimes != null && Integer.valueOf(forwardTimes) > 9999) {

                Double a = Double.valueOf(forwardTimes) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                forwardTimes = str + "万";

            }
            ((ViewHolderTwozhf) holder).tv_count_zhf.setText(forwardTimes);
            final String fromUId = sID;
            if (avatar.length() > 40) {
                String[] orderProjectArray = avatar.split("\\|");
                avatar = orderProjectArray[0];
            }
            String sex = TextUtils.isEmpty(json.getString("uSex")) ? "01" : json.getString("uSex");
            String nianLing = TextUtils.isEmpty(json.getString("uAge")) ? "27" : json.getString("uAge");
            String company = TextUtils.isEmpty(json.getString("uCompany")) ? "暂未加入企业" : json.getString("uCompany");
            String uNation = json.getString("uNation");
            String resv5 = json.getString("resv5");
            String resv6 = json.getString("resv6");
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
            ((ViewHolderTwozhf) holder).tvCompany.setText("(" + company + ")");
            ((ViewHolderTwozhf) holder).tvNianLing.setText(nianLing);
            if ("00".equals(sex)) {
                ((ViewHolderTwozhf) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
                ((ViewHolderTwozhf) holder).tvNianLing.setBackgroundColor(Color.rgb(234, 121, 219));
            } else {
                ((ViewHolderTwozhf) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
                ((ViewHolderTwozhf) holder).tvNianLing.setBackgroundResource(R.color.accent_blue);
            }
            if (!("" == avatar)) {
                ((ViewHolderTwozhf) holder).iv_avatar.setVisibility(View.VISIBLE);
                ((ViewHolderTwozhf) holder).tvTitleA.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar, ((ViewHolderTwozhf) holder).iv_avatar, DemoApplication.mOptions);
            } else {
                ((ViewHolderTwozhf) holder).iv_avatar.setVisibility(View.INVISIBLE);
                ((ViewHolderTwozhf) holder).tvTitleA.setVisibility(View.VISIBLE);
                ((ViewHolderTwozhf) holder).tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
            }
            String location1 = TextUtils.isEmpty(json.getString("location")) ? "" : json.getString("location");
            String lat = "", lng = "", address = "";
            if (!"".equals(location1)) {
                lat = location1.split("\\|")[0];
                lng = location1.split("\\|")[1];
            }
            address = json.getString("price");
            String locaImage = json.getString("locaImage");

            if (redImage != null && !"".equals(redImage) && !redImage.equalsIgnoreCase("null")) {
                ((ViewHolderTwozhf) holder).iv_ditu.setVisibility(View.VISIBLE);
                ((ViewHolderTwozhf) holder).iv_ditu.setImageResource(R.drawable.image_baozang);
                ((ViewHolderTwozhf) holder).mMapView.setVisibility(View.GONE);
                final String biaoshi;
                if ("0".equals(redSum)) {
                    ((ViewHolderTwozhf) holder).rl_geshu.setVisibility(View.GONE);
                    ((ViewHolderTwozhf) holder).iv_bz_type.setVisibility(View.VISIBLE);
                    ((ViewHolderTwozhf) holder).iv_bz_type.setImageResource(R.drawable.baoxiang_open);
                    biaoshi = "open";
                } else {
                    if ("no".equals(redTime)) {
                        ((ViewHolderTwozhf) holder).iv_bz_type.setVisibility(View.GONE);
                        ((ViewHolderTwozhf) holder).rl_geshu.setVisibility(View.VISIBLE);
                        ((ViewHolderTwozhf) holder).tv_geshu.setText(redSum);
                        biaoshi = "close";
                    } else {
                        String nowTime = getNowTime();
                        if (Long.parseLong(nowTime) < Long.parseLong(redTime)) {
                            ((ViewHolderTwozhf) holder).iv_bz_type.setVisibility(View.GONE);
                            ((ViewHolderTwozhf) holder).rl_geshu.setVisibility(View.VISIBLE);
                            ((ViewHolderTwozhf) holder).tv_geshu.setText(redSum);
                            biaoshi = "close";
                        } else {
                            ((ViewHolderTwozhf) holder).rl_geshu.setVisibility(View.GONE);
                            ((ViewHolderTwozhf) holder).iv_bz_type.setVisibility(View.GONE);
                            biaoshi = "none";
                        }
                    }
                }
                final String finalLat = lat;
                final String finalLng = lng;
                final String finalRedSum = redSum;
                ((ViewHolderTwozhf) holder).iv_ditu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!DemoHelper.getInstance().isLoggedIn(context)) {
                            Toast.makeText(context, "登陆后才可以操作哦！", Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPreferences sp = context.getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
                            String location = sp.getString("location", null);
                            ScreenshotUtil.getBitmapByView(context, ((ViewHolderTwozhf) holder).rl_neirong, "分享名片红包", location, 3, false, 0, 0);
                            context.startActivity(new Intent(context, BaiDuBzLocationActivity.class).putExtra("biaoshi", biaoshi).putExtra("bzlat", finalLat)
                                    .putExtra("bzlng", finalLng).putExtra("pintuUrl", redImage).putExtra("createTime", createTime).putExtra("geshu", finalRedSum)
                                    .putExtra("user_id", firstID).putExtra("dynamicSeq", dynamicSeq).putExtra("gameRed", gameRed).putExtra("name", userID));
                        }
                    }
                });
            } else {
                ((ViewHolderTwozhf) holder).iv_bz_type.setVisibility(View.GONE);
                ((ViewHolderTwozhf) holder).rl_geshu.setVisibility(View.GONE);
                if (locaImage != null && !"".equals(locaImage) && !"null".equals(locaImage) && !"NULL".equals(locaImage)) {
                    ((ViewHolderTwozhf) holder).iv_ditu.setVisibility(View.VISIBLE);
                    ((ViewHolderTwozhf) holder).mMapView.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + locaImage, ((ViewHolderTwozhf) holder).iv_ditu);
                } else {
                    ((ViewHolderTwozhf) holder).mMapView.setText(address);
                    ((ViewHolderTwozhf) holder).mMapView.setVisibility(View.VISIBLE);
                    ((ViewHolderTwozhf) holder).iv_ditu.setVisibility(View.GONE);
                }
                if (!"".equals(lat) && !"".equals(lng)) {
                    final String finalLat = lat;
                    final String finalLng = lng;
                    ((ViewHolderTwozhf) holder).mMapView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!DemoHelper.getInstance().isLoggedIn(context)) {
                                Toast.makeText(context, "登陆后才可以操作哦！", Toast.LENGTH_SHORT).show();
                            } else {
                                context.startActivity(new Intent(context, BaiDuYuanGongLocationActivity.class).putExtra("biaoshi", "01").putExtra("dynaLat", finalLat).putExtra("dynaLng", finalLng));
                            }
                        }
                    });
                    ((ViewHolderTwozhf) holder).iv_ditu.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!DemoHelper.getInstance().isLoggedIn(context)) {
                                Toast.makeText(context, "登陆后才可以操作哦！", Toast.LENGTH_SHORT).show();
                            } else {
                                context.startActivity(new Intent(context, BaiDuYuanGongLocationActivity.class).putExtra("biaoshi", "01").putExtra("dynaLat", finalLat).putExtra("dynaLng", finalLng));
                            }
                        }
                    });
                }
            }
            ((ViewHolderTwozhf) holder).tv_nick.setText(userID);
            final String shareRed = json.getString("shareRed");
            final String friendsNumber = json.getString("friendsNumber");
            String onceJine = null;
            if (friendsNumber != null && !"".equals(friendsNumber)) {
                onceJine = friendsNumber.split("\\|")[0];
            }
            if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0 && onceJine != null && !"".equals(onceJine) && Double.parseDouble(onceJine) > 0) {
                ((ViewHolderTwozhf) holder).tv_nick.setTextColor(Color.RED);
            } else {
                ((ViewHolderTwozhf) holder).tv_nick.setTextColor(Color.rgb(87, 107, 149));
            }
            ((ViewHolderTwozhf) holder).tv_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            ((ViewHolderTwozhf) holder).tvTitleA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            ((ViewHolderTwozhf) holder).tv_zhf_content.setText(newContent, position);
            ((ViewHolderTwozhf) holder).tv_zhf_content.setVisibility(TextUtils.isEmpty(newContent) ? View.GONE : View.VISIBLE);
            ((ViewHolderTwozhf) holder).tv_zhf_content.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        String type;
                        if (json.getString("fromUId") != null && !json.getString("fromUId").equalsIgnoreCase("null")) {
                            type = "02";
                        } else {
                            type = "01";
                        }

                        Intent intent = new Intent(context, DynaDetaActivity.class);
                        intent.putExtra("sID", sID);
                        intent.putExtra("dynamicSeq", dynamicSeq);
                        intent.putExtra("createTime", rel_time);
                        intent.putExtra("dType", dType);
                        intent.putExtra("type", type);
                        intent.putExtra("type2", "00");
                        context.startActivityForResult(intent, 0);
                    }
                    return true;
                }
            });
            ((ViewHolderTwozhf) holder).tv_first_nick.setText(firstName + ":");
            if (firstImage != null) {
                if (firstImage.length() > 40) {
                    String[] orderProjectArray = firstImage.split("\\|");
                    firstImage = orderProjectArray[0];
                }
            }
            if ("00".equals(firstSex)) {
                ((ViewHolderTwozhf) holder).tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
            } else {
                ((ViewHolderTwozhf) holder).tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
            }
            if (firstImage != null && !("".equals(firstImage))) {
                ((ViewHolderTwozhf) holder).iv_first_avatar.setVisibility(View.VISIBLE);
                ((ViewHolderTwozhf) holder).tvfirst_TitleA.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + firstImage, ((ViewHolderTwozhf) holder).iv_first_avatar, DemoApplication.mOptions);
            } else {
                ((ViewHolderTwozhf) holder).iv_first_avatar.setVisibility(View.INVISIBLE);
                ((ViewHolderTwozhf) holder).tvfirst_TitleA.setVisibility(View.VISIBLE);
                ((ViewHolderTwozhf) holder).tvfirst_TitleA.setText(TextUtils.isEmpty(firstName) ? firstID : firstName);
            }
            ((ViewHolderTwozhf) holder).iv_first_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, firstID));
                }
            });
            ((ViewHolderTwozhf) holder).tv_first_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, firstID));
                }
            });
            ((ViewHolderTwozhf) holder).tv_nick.setText(userID);
            ((ViewHolderTwozhf) holder).tv_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            String resv1 = TextUtils.isEmpty(json.getString("resv1")) ? "" : json.getString("resv1");
            String resv2 = TextUtils.isEmpty(json.getString("resv2")) ? "" : json.getString("resv2");
            double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
            double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());
            if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1, longitude1);
                double distance = DistanceUtil.getDistance(ll, ll1);
                double dou = distance / 1000;
                String str = String.format("%.2f", dou);//format 返回的是字符串
                if (str != null && dou >= 10000) {
                    ((ViewHolderTwozhf) holder).tvDistance.setText("隐藏");
                } else {
                    ((ViewHolderTwozhf) holder).tvDistance.setText(str + "km");
                }
            } else {
                ((ViewHolderTwozhf) holder).tvDistance.setText("3km之内");
            }
            // 设置文章中的图片
            ((ViewHolderTwozhf) holder).image_1.setVisibility(View.GONE);
            ((ViewHolderTwozhf) holder).image_2.setVisibility(View.GONE);
            ((ViewHolderTwozhf) holder).image_3.setVisibility(View.GONE);
            ((ViewHolderTwozhf) holder).image_4.setVisibility(View.GONE);
            ((ViewHolderTwozhf) holder).image_5.setVisibility(View.GONE);
            ((ViewHolderTwozhf) holder).image_6.setVisibility(View.GONE);
            ((ViewHolderTwozhf) holder).image_7.setVisibility(View.GONE);
            ((ViewHolderTwozhf) holder).image_8.setVisibility(View.GONE);
            View v2 = null;
            if (video != null && videoPictures != null) {
                ((ViewHolderTwozhf) holder).rl_video.setVisibility(View.VISIBLE);
                boolean setUp = ((ViewHolderTwozhf) holder).videoPlayer.setUp(FXConstant.URL_VIDEO + video, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        "");
                v2 = ((ViewHolderTwozhf) holder).rl_video;
                if (setUp) {
                    Glide.with(context).load(FXConstant.URL_VIDEO + videoPictures)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .error(R.drawable.default_error)
                            .crossFade().into(((ViewHolderTwozhf) holder).videoPlayer.thumbImageView);
                } else {
                    Toast.makeText(context, "视频播放失败", Toast.LENGTH_SHORT).show();
                }
                ((ViewHolderTwozhf) holder).tv_video.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.e("socialmain,", "touch");
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            updateLiulancishu(rel_time, dynamicSeq);
                            updateDeLiulancishu(rel_time, sID);
                        }
                        return false;
                    }
                });
            } else {
                ((ViewHolderTwozhf) holder).rl_video.setVisibility(View.GONE);
                if (!imageStr.equals("")) {
                    String[] images = imageStr.split("\\|");
                    int imNumb = images.length;
                    ((ViewHolderTwozhf) holder).image_1.setVisibility(View.VISIBLE);
                    v2 = ((ViewHolderTwozhf) holder).image_1;
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[0], ((ViewHolderTwozhf) holder).image_1, DemoApplication.mOptions2);
                    ((ViewHolderTwozhf) holder).image_1.setOnClickListener(new ImageListener(images, 0, rel_time, dynamicSeq, sID));
                    if (imNumb > 1) {
                        ((ViewHolderTwozhf) holder).image_2.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[1], ((ViewHolderTwozhf) holder).image_2, DemoApplication.mOptions2);
                        ((ViewHolderTwozhf) holder).image_2.setOnClickListener(new ImageListener(images, 1, rel_time, dynamicSeq, sID));
                        if (imNumb > 2) {
                            ((ViewHolderTwozhf) holder).image_3.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[2], ((ViewHolderTwozhf) holder).image_3, DemoApplication.mOptions2);
                            ((ViewHolderTwozhf) holder).image_3.setOnClickListener(new ImageListener(images, 2, rel_time, dynamicSeq, sID));
                            if (imNumb > 3) {
                                ((ViewHolderTwozhf) holder).image_4.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[3], ((ViewHolderTwozhf) holder).image_4, DemoApplication.mOptions2);
                                ((ViewHolderTwozhf) holder).image_4.setOnClickListener(new ImageListener(images, 3, rel_time, dynamicSeq, sID));
                                if (imNumb > 4) {
                                    ((ViewHolderTwozhf) holder).image_5.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[4], ((ViewHolderTwozhf) holder).image_5, DemoApplication.mOptions2);
                                    ((ViewHolderTwozhf) holder).image_5.setOnClickListener(new ImageListener(images, 4, rel_time, dynamicSeq, sID));
                                    if (imNumb > 5) {
                                        ((ViewHolderTwozhf) holder).image_6.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[5], ((ViewHolderTwozhf) holder).image_6, DemoApplication.mOptions2);
                                        ((ViewHolderTwozhf) holder).image_6.setOnClickListener(new ImageListener(images, 5, rel_time, dynamicSeq, sID));
                                        if (imNumb > 6) {
                                            ((ViewHolderTwozhf) holder).image_7.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[6], ((ViewHolderTwozhf) holder).image_7, DemoApplication.mOptions2);
                                            ((ViewHolderTwozhf) holder).image_7.setOnClickListener(new ImageListener(images, 6, rel_time, dynamicSeq, sID));
                                            if (imNumb > 7) {
                                                ((ViewHolderTwozhf) holder).image_8.setVisibility(View.VISIBLE);
                                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[7], ((ViewHolderTwozhf) holder).image_8, DemoApplication.mOptions2);
                                                ((ViewHolderTwozhf) holder).image_8.setOnClickListener(new ImageListener(images, 7, rel_time, dynamicSeq, sID));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 显示位置
//                if (location != null && !location.equals("0")) {
//                    holder.tv_location.setVisibility(View.VISIBLE);
//                    holder.tv_location.setText(location);
//                }
            // 显示文章内容
            // .setText(content);
            final String finalFirstImage = firstImage;
            final View finalV = v2;
            ((ViewHolderTwozhf) holder).rl_zhuanfa.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!DemoHelper.getInstance().isLoggedIn(context)) {
                        Toast.makeText(context, "登陆后才可以转发哦！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showZhfDialog(json.getString("views"), createTime, redImage, ((ViewHolderTwozhf) holder).card, finalV, "0", dynamicSeq, firstID, fromUId, finalFirstImage, firstName, content, false, "0");
                }
            });
            ((ViewHolderTwozhf) holder).tv_content2.setText(content);
            String rel_time2 = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                    + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
            ((ViewHolderTwozhf) holder).tv_time.setText(rel_time2);
            ((ViewHolderTwozhf) holder).iv_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            final String finalCountPinglun = countPinglun;
            ((ViewHolderTwozhf) holder).rl_pinglun.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCommentEditText("01", sID, myuserID, myNick, dynamicSeq, createTime, ((ViewHolderTwozhf) holder).tv_count_pl, Integer.valueOf(finalCountPinglun));
                }
            });
            //商业界面
        } else if (holder instanceof ViewHolderThrf) {
            ((ViewHolderThrf) holder).rl_xiaoliang.setVisibility(View.VISIBLE);
            final JSONObject json = users.get(position);
            // 如果数据出错....
            if (json == null || json.size() == 0) {
                users.remove(position);
                this.notifyDataSetChanged();
            }
            String views = json.getString("views");
            if (views == null || "".equals(views)) {
                views = "0";
            }
            if (views != null && Integer.valueOf(views) > 9999) {

                Double a = Double.valueOf(views) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                views = str + "万";

            }
            ((ViewHolderThrf) holder).tv_count_llc.setText(views);
            final String userID = json.getString("uName");
            final String content = json.getString("content");
            final String resv4 = TextUtils.isEmpty(json.getString("orderType")) ? "01" : json.getString("orderType");
            String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
            String location = json.getString("location");
            String price = json.getString("price");
            String salePrice = json.getString("salePrice");
            String xiaoliang = TextUtils.isEmpty(json.getString("transTimes")) ? "0" : json.getString("transTimes");
            String countPinglun = json.getString("resv7");
            final String video = json.getString("video");
            final String videoPictures = json.getString("videoPictures");

            String collection = json.getString("collectionCount");

            String vip = json.getString("vip");
            String vipLevel = json.getString("vipLevel");
            String messageOrderAll = json.getString("messageOrderAll");

            com.alibaba.fastjson.JSONArray userProfession = json.getJSONArray("userProfessions");

            String proString = "";
            double marginString = 0;
            if (userProfession != null && userProfession.size() > 0) {
                String pl1 = userProfession.getJSONObject(0).getString("upName");
                String margin1 = userProfession.getJSONObject(0).getString("margin");
                if (margin1 != null && margin1.length() != 0 && Double.valueOf(margin1) > 99) {
                    marginString = Double.valueOf(margin1);
                }
                if (pl1.equalsIgnoreCase("null")) {
                    pl1 = "";
                }
                if (pl1.equals("")) {

                } else {
                    proString = pl1;
                }
            }
            if (userProfession != null && userProfession.size() > 1) {
                String pl2 = userProfession.getJSONObject(1).getString("upName");
                String margin2 = userProfession.getJSONObject(1).getString("margin");
                if (margin2 != null && margin2.length() != 0 && Double.valueOf(margin2) > 99) {
                    marginString = marginString + Double.valueOf(margin2);
                }
                if (pl2.equalsIgnoreCase("null")) {
                    pl2 = "";
                }
                if (pl2.equals("")) {

                } else {
                    proString = proString + " " + pl2;
                }
            }
            if (userProfession != null && userProfession.size() > 2) {
                String pl3 = userProfession.getJSONObject(2).getString("upName");
                String margin3 = userProfession.getJSONObject(2).getString("margin");
                if (margin3 != null && margin3.length() != 0 && Double.valueOf(margin3) > 99) {

                    marginString = marginString + Double.valueOf(margin3);
                }
                if (pl3.equalsIgnoreCase("null")) {
                    pl3 = "";
                }
                if (pl3.equals("")) {

                } else {
                    proString = proString + " " + pl3;
                }
            }
            if (userProfession != null && userProfession.size() > 3) {
                String pl4 = userProfession.getJSONObject(3).getString("upName");
                String margin4 = userProfession.getJSONObject(3).getString("margin");
                if (margin4 != null && margin4.length() != 0 && Double.valueOf(margin4) > 99) {
                    marginString = marginString + Double.valueOf(margin4);
                }
                if (pl4.equalsIgnoreCase("null")) {
                    pl4 = "";
                }
                if (pl4.equals("")) {

                } else {
                    proString = proString + " " + pl4;
                }
            }

            if (marginString > 99) {

                ((ViewHolderThrf) holder).tv_marginLabel.setVisibility(View.VISIBLE);

                if (marginString > 9999) {

                    ((ViewHolderThrf) holder).tv_marginLabel.setText("万元级保障商户");

                } else {

                    DecimalFormat df = new DecimalFormat("######0");
                    String numberStr = df.format(marginString);

                    ((ViewHolderThrf) holder).tv_marginLabel.setText("承诺保障" + numberStr);
                }

            } else {

                ((ViewHolderThrf) holder).tv_marginLabel.setVisibility(View.GONE);
            }

            if (Double.valueOf(messageOrderAll) > 0) {

                ((ViewHolderThrf) holder).tv_messageLabel.setVisibility(View.VISIBLE);

            } else {
                ((ViewHolderThrf) holder).tv_messageLabel.setVisibility(View.GONE);
            }

            if (Double.valueOf(vip) > 0) {

                ((ViewHolderThrf) holder).tv_vipLabel.setText("VIP" + vipLevel);
                ((ViewHolderThrf) holder).tv_vipLabel.setVisibility(View.VISIBLE);

            } else {

                ((ViewHolderThrf) holder).tv_vipLabel.setVisibility(View.GONE);

            }

            if (profession.equals("取消收藏")) {

                ((ViewHolderThrf) holder).tv_collectiontitle.setText("取消收藏");
                ((ViewHolderThrf) holder).tv_collectioncount.setVisibility(View.GONE);

            } else {

                if (Double.valueOf(collection) > 999) {

                    ((ViewHolderThrf) holder).tv_collectioncount.setText("999+");

                } else {

                    ((ViewHolderThrf) holder).tv_collectioncount.setText(collection);

                }

            }


            if (price == null || "".equals(price)) {
                price = "0";
            }
            if (salePrice == null || "".equals(salePrice)) {
                salePrice = "0";
            }
            if (countPinglun == null || "".equals(countPinglun)) {
                countPinglun = "0";
            }
            if (countPinglun != null && Integer.valueOf(countPinglun) > 9999) {
                Double a = Double.valueOf(countPinglun) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                countPinglun = str + "万";

            }
            String forwardTimes = json.getString("forwardTimes");
            if (forwardTimes == null || "".equals(forwardTimes)) {
                forwardTimes = "0";
            }
            if (forwardTimes != null && Integer.valueOf(forwardTimes) > 9999) {

                Double a = Double.valueOf(forwardTimes) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                forwardTimes = str + "万";

            }
            ((ViewHolderThrf) holder).tv_count_zhf.setText(forwardTimes);
            ((ViewHolderThrf) holder).tv_count_pl.setText(countPinglun);
            ((ViewHolderThrf) holder).tv_xiaoliang.setText(xiaoliang);
            if (dType.equals("04")) {
                ((ViewHolderThrf) holder).tv_shpjg.setText("￥" + salePrice);
                ((ViewHolderThrf) holder).tv_shpyj.setText("￥" + price);

            } else if (dType.equals("03")) {

                final String txt = price.split("\\.")[0];
                Pattern p = Pattern.compile("[0-9]*");
                Matcher m = p.matcher(txt);
                if (m.matches()) {

                    ((ViewHolderThrf) holder).tv_shpjg.setText("" + new DecimalFormat("0.00").format(Double.parseDouble(price)));

                } else {

                    ((ViewHolderThrf) holder).tv_shpjg.setText("0.00");

                }

                if (salePrice.equals("0")) {
                    ((ViewHolderThrf) holder).tv_shpyj.setText(((ViewHolderThrf) holder).tv_shpjg.getText().toString().trim());
                } else {
                    ((ViewHolderThrf) holder).tv_shpyj.setText("" + new DecimalFormat("0.00").format(Double.parseDouble(salePrice)));
                }
            }

            final String sum = json.getString("sum");
            if (sum != null) {
                ((ViewHolderThrf) holder).tv_liulan_cishu.setVisibility(View.VISIBLE);
                ((ViewHolderThrf) holder).image_redpage.setVisibility(View.VISIBLE);

                String balance = TextUtils.isEmpty(json.getString("redBalance")) ? "0" : json.getString("redBalance");
                double redBalance = Double.valueOf(balance);
                double oncePrice = Double.valueOf(json.getString("oncePrice"));
                int cishu = (int) ((redBalance * 100) / (oncePrice * 100));
                //  ((ViewHolderThrf) holder).rl_huikui1.setVisibility(View.VISIBLE);
                //  ((ViewHolderThrf) holder).rl_huikui2.setVisibility(View.VISIBLE);
                //  ((ViewHolderThrf) holder).tv_huikui_zonge.setText("￥" + sum);
                //  ((ViewHolderThrf) holder).tv_huikui_yue.setText("￥" + redBalance);
                //  ((ViewHolderThrf) holder).tv_huikui_zhaunfa.setText("￥" + oncePrice);

                ((ViewHolderThrf) holder).tv_liulan_cishu.setText(cishu + "");

                if (cishu == 0) {

                    ((ViewHolderThrf) holder).tv_liulan_cishu.setVisibility(View.INVISIBLE);
                    ((ViewHolderThrf) holder).image_redpage.setVisibility(View.INVISIBLE);
                }

            } else {
                //   ((ViewHolderThrf) holder).rl_huikui1.setVisibility(View.GONE);
                //   ((ViewHolderThrf) holder).rl_huikui2.setVisibility(View.GONE);

                ((ViewHolderThrf) holder).tv_liulan_cishu.setVisibility(View.INVISIBLE);
                ((ViewHolderThrf) holder).image_redpage.setVisibility(View.INVISIBLE);
                //  ((ViewHolderThrf) holder).tv_liulan_cishu.setText("0");

            }

            final String sID = json.getString("uLoginId");
            // String token = json.getString("token");
            final String rel_time = json.getString("createTime");
            String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
            final String dynamicSeq = json.getString("dynamicSeq");
            final String fromUId = sID;
            if (!"".equals(avatar) && avatar.length() > 40) {
                String[] orderProjectArray = avatar.split("\\|");
                avatar = orderProjectArray[0];
            }
            String sex = TextUtils.isEmpty(json.getString("uSex")) ? "01" : json.getString("uSex");
            String nianLing = TextUtils.isEmpty(json.getString("uAge")) ? "27" : json.getString("uAge");
            String company = TextUtils.isEmpty(json.getString("uCompany")) ? "暂未加入企业" : json.getString("uCompany");
            String uNation = json.getString("uNation");
            String resv5 = json.getString("resv5");
            String resv6 = json.getString("resv6");
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
            ((ViewHolderThrf) holder).tvCompany.setText("(" + company + ")");

            if (json.getString("authType").equals("03")) {

                ((ViewHolderThrf) holder).tvCompany.setTextColor(Color.parseColor("#FF0000"));
                ((ViewHolderThrf) holder).tvCompany.setText("(仅用户可见)");

            }


            ((ViewHolderThrf) holder).tvNianLing.setText(nianLing);
            if ("00".equals(sex)) {
                ((ViewHolderThrf) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
                ((ViewHolderThrf) holder).tvNianLing.setBackgroundColor(Color.rgb(234, 121, 219));
            } else {
                ((ViewHolderThrf) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
                ((ViewHolderThrf) holder).tvNianLing.setBackgroundResource(R.color.accent_blue);
            }
            if (!"".equals(avatar)) {
                ((ViewHolderThrf) holder).iv_avatar.setVisibility(View.VISIBLE);
                ((ViewHolderThrf) holder).tvTitleA.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar, ((ViewHolderThrf) holder).iv_avatar, DemoApplication.mOptions);
            } else {
                ((ViewHolderThrf) holder).iv_avatar.setVisibility(View.INVISIBLE);
                ((ViewHolderThrf) holder).tvTitleA.setVisibility(View.VISIBLE);
                ((ViewHolderThrf) holder).tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
            }
            ((ViewHolderThrf) holder).tv_nick.setText(userID);
            final String shareRed = json.getString("shareRed");
            final String friendsNumber = json.getString("friendsNumber");
            String onceJine = null;
            if (friendsNumber != null && !"".equals(friendsNumber)) {
                onceJine = friendsNumber.split("\\|")[0];
            }
            if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0 && onceJine != null && !"".equals(onceJine) && Double.parseDouble(onceJine) > 0) {
                ((ViewHolderThrf) holder).tv_nick.setTextColor(Color.RED);
            } else {
                ((ViewHolderThrf) holder).tv_nick.setTextColor(Color.rgb(87, 107, 149));
            }
            ((ViewHolderThrf) holder).tv_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            ((ViewHolderThrf) holder).tvTitleA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            String resv1 = TextUtils.isEmpty(json.getString("resv1")) ? "" : json.getString("resv1");
            String resv2 = TextUtils.isEmpty(json.getString("resv2")) ? "" : json.getString("resv2");
            double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
            double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());
            if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1, longitude1);
                double distance = DistanceUtil.getDistance(ll, ll1);
                double dou = distance / 1000;
                String str = String.format("%.2f", dou);//format 返回的是字符串
                if (str != null && dou >= 10000) {
                    ((ViewHolderThrf) holder).tvDistance.setText("隐藏");
                } else {
                    ((ViewHolderThrf) holder).tvDistance.setText(str + "km");
                }
            } else {
                ((ViewHolderThrf) holder).tvDistance.setText("3km之内");
            }
            String balance = "";
            if (dType.equals("04")) {
                balance = salePrice;
            } else if (dType.equals("03")) {
                balance = price;
            }
            final ViewHolderThrf finalHolder = ((ViewHolderThrf) holder);
            final String finalBalance = balance;
            if (DemoHelper.getInstance().isLoggedIn(context)) {
                final String finalRel_time = rel_time;
                ((ViewHolderThrf) holder).btn_xiadan.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String distance = finalHolder.tvDistance.getText().toString();
                        showDialog(sID, resv4, "动态订单", distance, finalBalance, finalRel_time, dynamicSeq);
                    }
                });
            } else {
                ((ViewHolderThrf) holder).btn_xiadan.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                });
            }
            // 设置文章中的图片
            ((ViewHolderThrf) holder).image_1.setVisibility(View.GONE);
            ((ViewHolderThrf) holder).image_2.setVisibility(View.GONE);
            ((ViewHolderThrf) holder).image_3.setVisibility(View.GONE);
            ((ViewHolderThrf) holder).image_4.setVisibility(View.GONE);
            ((ViewHolderThrf) holder).image_5.setVisibility(View.GONE);
            ((ViewHolderThrf) holder).image_6.setVisibility(View.GONE);
            ((ViewHolderThrf) holder).image_7.setVisibility(View.GONE);
            ((ViewHolderThrf) holder).image_8.setVisibility(View.GONE);
            View v2 = null;
            if (video != null && videoPictures != null) {
                ((ViewHolderThrf) holder).rl_video.setVisibility(View.VISIBLE);
                boolean setUp = ((ViewHolderThrf) holder).videoPlayer.setUp(FXConstant.URL_VIDEO + video, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        "");
                v2 = ((ViewHolderThrf) holder).rl_video;
                if (setUp) {
                    Glide.with(context).load(FXConstant.URL_VIDEO + videoPictures)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .error(R.drawable.default_error)
                            .crossFade().into(((ViewHolderThrf) holder).videoPlayer.thumbImageView);
                } else {
                    Toast.makeText(context, "视频播放失败", Toast.LENGTH_SHORT).show();
                }
                ((ViewHolderThrf) holder).tv_video.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.e("socialmain,", "touch");
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            updateLiulancishu(rel_time, dynamicSeq);
                            updateDeLiulancishu(rel_time, sID);
                        }
                        return false;
                    }
                });

            } else {
                ((ViewHolderThrf) holder).rl_video.setVisibility(View.GONE);

                if (!imageStr.equals("")) {
                    String[] images = imageStr.split("\\|");
                    int imNumb = images.length;
                    ((ViewHolderThrf) holder).image_1.setVisibility(View.VISIBLE);
                    v2 = ((ViewHolderThrf) holder).image_1;
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[0], ((ViewHolderThrf) holder).image_1, DemoApplication.mOptions2);

                    //长度大于8的证明是链接 点击跳转网页  如果点击下方浏览一系列按钮 就跳到详情
                    if (json.getString("task_jurisdiction") != null && json.getString("task_jurisdiction").length() > 8) {
                        ((ViewHolderThrf) holder).tv_content.performLongClick();
                    } else {
                        ((ViewHolderThrf) holder).image_1.setOnClickListener(new ImageListener(images, 0, rel_time, dynamicSeq, sID));
                    }

                    if (imNumb > 1) {
                        ((ViewHolderThrf) holder).image_2.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[1], ((ViewHolderThrf) holder).image_2, DemoApplication.mOptions2);

                        //长度大于8的证明是链接 点击跳转网页  如果点击下方浏览一系列按钮 就跳到详情
                        if (json.getString("task_jurisdiction") != null && json.getString("task_jurisdiction").length() > 8) {
                            ((ViewHolderThrf) holder).tv_content.performLongClick();
                        } else {
                            ((ViewHolderThrf) holder).image_2.setOnClickListener(new ImageListener(images, 1, rel_time, dynamicSeq, sID));
                        }

                        if (imNumb > 2) {
                            ((ViewHolderThrf) holder).image_3.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[2], ((ViewHolderThrf) holder).image_3, DemoApplication.mOptions2);

                            //长度大于8的证明是链接 点击跳转网页  如果点击下方浏览一系列按钮 就跳到详情
                            if (json.getString("task_jurisdiction") != null && json.getString("task_jurisdiction").length() > 8) {
                                ((ViewHolderThrf) holder).tv_content.performLongClick();
                            } else {
                                ((ViewHolderThrf) holder).image_3.setOnClickListener(new ImageListener(images, 2, rel_time, dynamicSeq, sID));
                            }

                            if (imNumb > 3) {
                                ((ViewHolderThrf) holder).image_4.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[3], ((ViewHolderThrf) holder).image_4, DemoApplication.mOptions2);

                                //长度大于8的证明是链接 点击跳转网页  如果点击下方浏览一系列按钮 就跳到详情
                                if (json.getString("task_jurisdiction") != null && json.getString("task_jurisdiction").length() > 8) {
                                    ((ViewHolderThrf) holder).tv_content.performLongClick();
                                } else {
                                    ((ViewHolderThrf) holder).image_4.setOnClickListener(new ImageListener(images, 3, rel_time, dynamicSeq, sID));
                                }

                                if (imNumb > 4) {
                                    ((ViewHolderThrf) holder).image_5.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[4], ((ViewHolderThrf) holder).image_5, DemoApplication.mOptions2);


                                    //长度大于8的证明是链接 点击跳转网页  如果点击下方浏览一系列按钮 就跳到详情
                                    if (json.getString("task_jurisdiction") != null && json.getString("task_jurisdiction").length() > 8) {
                                        ((ViewHolderThrf) holder).tv_content.performLongClick();
                                    } else {
                                        ((ViewHolderThrf) holder).image_5.setOnClickListener(new ImageListener(images, 4, rel_time, dynamicSeq, sID));
                                    }

                                    if (imNumb > 5) {
                                        ((ViewHolderThrf) holder).image_6.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[5], ((ViewHolderThrf) holder).image_6, DemoApplication.mOptions2);


                                        //长度大于8的证明是链接 点击跳转网页  如果点击下方浏览一系列按钮 就跳到详情
                                        if (json.getString("task_jurisdiction") != null && json.getString("task_jurisdiction").length() > 8) {
                                            ((ViewHolderThrf) holder).tv_content.performLongClick();
                                        } else {
                                            ((ViewHolderThrf) holder).image_6.setOnClickListener(new ImageListener(images, 5, rel_time, dynamicSeq, sID));
                                        }

                                        if (imNumb > 6) {
                                            ((ViewHolderThrf) holder).image_7.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[6], ((ViewHolderThrf) holder).image_7, DemoApplication.mOptions2);

                                            //长度大于8的证明是链接 点击跳转网页  如果点击下方浏览一系列按钮 就跳到详情
                                            if (json.getString("task_jurisdiction") != null && json.getString("task_jurisdiction").length() > 8) {
                                                ((ViewHolderThrf) holder).tv_content.performLongClick();
                                            } else {
                                                ((ViewHolderThrf) holder).image_7.setOnClickListener(new ImageListener(images, 6, rel_time, dynamicSeq, sID));
                                            }

                                            if (imNumb > 7) {
                                                ((ViewHolderThrf) holder).image_8.setVisibility(View.VISIBLE);
                                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[7], ((ViewHolderThrf) holder).image_8, DemoApplication.mOptions2);

                                                //长度大于8的证明是链接 点击跳转网页  如果点击下方浏览一系列按钮 就跳到详情
                                                if (json.getString("task_jurisdiction") != null && json.getString("task_jurisdiction").length() > 8) {
                                                    ((ViewHolderThrf) holder).tv_content.performLongClick();
                                                } else {
                                                    ((ViewHolderThrf) holder).image_8.setOnClickListener(new ImageListener(images, 7, rel_time, dynamicSeq, sID));
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 显示位置
//            if (location != null && !location.equals("0")) {
//                holder.tv_location.setVisibility(View.VISIBLE);
//                holder.tv_location.setText(location);
//            }
            // 显示文章内容
            // .setText(content);
            String firstImage = "";
            if (!avatar.equals("")) {
                firstImage = avatar.split("\\|")[0];
            }
            final String finalFirstImage = firstImage;
            final View finalV = v2;


            //商业动态的收藏点击
            ((ViewHolderThrf) holder).rl_collection.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    final LayoutInflater inflater1 = LayoutInflater.from(context);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog collectionDialog = new AlertDialog.Builder(context, R.style.Dialog).create();
                    collectionDialog.show();
                    collectionDialog.getWindow().setContentView(layout1);
                    collectionDialog.setCanceledOnTouchOutside(true);
                    collectionDialog.setCancelable(true);
                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                    TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                    title.setText("温馨提示");
                    btnOK1.setText("确定");
                    btnCancel1.setText("取消");

                    if (profession.equals("取消收藏")) {
                        title_tv1.setText("是否确定取消收藏？");
                    } else {
                        title_tv1.setText("是否确定添加收藏？");
                    }

                    btnCancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            collectionDialog.dismiss();
                        }
                    });

                    btnOK1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            collectionDialog.dismiss();

                            String url;

                            if (profession.equals("取消收藏")) {

                                url = FXConstant.URL_DELETEDYNAMICCOLLECTION;

                            } else {

                                url = FXConstant.URL_INSERTDYNAMICCOLLECTION;

                            }

                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {

                                    try {

                                        org.json.JSONObject object = new org.json.JSONObject(s);
                                        String code = object.getString("code");

                                        if (code != null && code.equals("SUCCESS")) {

                                            Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();

                                        } else {

                                            Toast.makeText(context, "网络不稳定,请稍后再试", Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (JSONException e) {

                                        e.printStackTrace();

                                    }

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    Toast.makeText(context, "网络不稳定,请稍后再试", Toast.LENGTH_SHORT).show();

                                }

                            }) {

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<>();

                                    param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                    param.put("dynamicId", dynamicSeq);
                                    param.put("createTime", rel_time);
                                    param.put("type", dType);

                                    return param;

                                }

                            };

                            MySingleton.getInstance(context).addToRequestQueue(request);

                        }
                    });

                }
            });


            ((ViewHolderThrf) holder).image_redpage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ViewHolderThrf) holder).rl_zhuanfa.performClick();
                }
            });

            ((ViewHolderThrf) holder).rl_zhuanfa.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!DemoHelper.getInstance().isLoggedIn(context)) {
                        Toast.makeText(context, "登陆后才可以转发哦！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (sum != null) {
                        double redBalance = Double.valueOf(json.getString("redBalance"));
                        final String oncePrice = json.getString("oncePrice");
                        if (redBalance > 0) {

                            String url = FXConstant.URL_SEARCH_REDAUTH;

                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {

                                    Log.d("chen", "onResponse" + s);

                                    try {

                                        org.json.JSONObject object = new org.json.JSONObject(s);
                                        String code = object.getString("code");

                                        if (code == null || "".equals(code) || code.equalsIgnoreCase("null")) {
                                            //处理null

                                            UserPermissionUtil.getUserPermission(context, DemoHelper.getInstance().getCurrentUsernName(), "8", new UserPermissionUtil.UserPermissionListener() {
                                                @Override
                                                public void onAllow() {

                                                    PermissionUtil permissionUtil = new PermissionUtil((FragmentActivity) context);
                                                    permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                                                                    , Manifest.permission.READ_PHONE_STATE
                                                                    , Manifest.permission.ACCESS_WIFI_STATE},
                                                            new PermissionListener() {
                                                                @Override
                                                                public void onGranted() {
                                                                    queryhbzgCount(json.getString("views"), rel_time, null, ((ViewHolderThrf) holder).card, finalV, oncePrice, dynamicSeq, sID, fromUId, finalFirstImage, userID, content, true);

                                                                }

                                                                @Override
                                                                public void onDenied(List<String> deniedPermission) {
                                                                    Toast.makeText(context.getApplicationContext(), "您拒绝了获取定位的权限！无法赚取红包 请手动开启权限", Toast.LENGTH_SHORT).show();
                                                                }

                                                                @Override
                                                                public void onShouldShowRationale(List<String> deniedPermission) {
                                                                    Toast.makeText(context.getApplicationContext(), "您拒绝了获取定位的权限！无法赚取红包 请手动开启权限", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                                                    context.startActivity(intent);
                                                                }
                                                            });


                                                }

                                                @Override
                                                public void onBan() {
                                                    ToastUtils.showNOrmalToast(context.getApplicationContext(), "您的账户已被禁止转发红包");

                                                }
                                            });

                                        } else {
                                            //禁止转了  要求发短信才可以

                                            final LayoutInflater inflater1 = LayoutInflater.from(context);
                                            RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                                            final Dialog dialog1 = new AlertDialog.Builder(context, R.style.Dialog).create();
                                            dialog1.show();
                                            dialog1.getWindow().setContentView(layout1);
                                            dialog1.setCanceledOnTouchOutside(true);
                                            dialog1.setCancelable(true);
                                            TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                                            Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                                            final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                                            TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                                            title.setText("温馨提示");
                                            btnOK1.setText("确定");
                                            btnCancel1.setText("取消");
                                            title_tv1.setText("您需要邀请20个用户注册才可以继续赚取红包");
                                            btnCancel1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog1.dismiss();
                                                }
                                            });
                                            btnOK1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog1.dismiss();

                                                    //跳转到通讯录界面邀请好友注册  标识是红包

                                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                                                            != PackageManager.PERMISSION_GRANTED) {
                                                        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CONTACTS}, 1);
                                                    } else {
                                                        Intent intent = new Intent(context, AddressListActivity.class);

                                                        intent.putExtra("redAuth", "yes");

                                                        context.startActivityForResult(intent, 0);
                                                    }
                                                }
                                            });
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    if (volleyError != null) {
                                        Log.e("hongbao", volleyError.getMessage());
                                        Log.d("chen", "hongbao" + volleyError.getMessage());
                                    }
                                    Toast.makeText(context, "网络不稳定,请稍后重试", Toast.LENGTH_SHORT).show();

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> param = new HashMap<>();

                                    param.put("rid", DemoHelper.getInstance().getCurrentUsernName());

                                    return param;
                                }
                            };
                            MySingleton.getInstance(context).addToRequestQueue(request);

                        } else {
                            showZhfDialog(json.getString("views"), rel_time, null, ((ViewHolderThrf) holder).card, finalV, "0", dynamicSeq, sID, fromUId, finalFirstImage, userID, content, false, "0");
                        }
                    } else {
                        showZhfDialog(json.getString("views"), rel_time, null, ((ViewHolderThrf) holder).card, finalV, "0", dynamicSeq, sID, fromUId, finalFirstImage, userID, content, false, "0");
                    }
                }
            });
            ((ViewHolderThrf) holder).tv_content.setText(content);
            // ((ViewHolderThrf) holder).tv_content.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
            ((ViewHolderThrf) holder).tv_content.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type;
                    if (json.getString("fromUId") != null && !json.getString("fromUId").equalsIgnoreCase("null")) {
                        type = "02";
                    } else {
                        type = "01";
                    }

                    String task_jurisdiction = json.getString("task_jurisdiction");

                    //长度大于8的证明是链接 点击跳转网页  如果点击下方浏览一系列按钮 就跳到详情
                    if (task_jurisdiction != null && task_jurisdiction.length() > 8) {

                        Intent intent = new Intent(context, ProjectDynamicLinkDetailActivity.class);

                        intent.putExtra("orderType", json.getString("orderType"));
                        intent.putExtra("lat", json.getString("lat"));
                        intent.putExtra("lng", json.getString("lng"));
                        intent.putExtra("salePrice", json.getString("salePrice"));
                        intent.putExtra("price", json.getString("price"));
                        intent.putExtra("sID", sID);
                        intent.putExtra("profession", profession);
                        intent.putExtra("dynamicSeq", dynamicSeq);
                        intent.putExtra("createTime", rel_time);
                        intent.putExtra("task_jurisdiction", task_jurisdiction);
                        intent.putExtra("task_label", json.getString("task_label"));
                        intent.putExtra("dType", dType);
                        intent.putExtra("type", type);
                        context.startActivityForResult(intent, 0);

                    } else {
                        Intent intent = new Intent(context, DynaDetaActivity.class);
                        intent.putExtra("sID", sID);
                        intent.putExtra("dynamicSeq", dynamicSeq);
                        intent.putExtra("createTime", rel_time);
                        intent.putExtra("dType", dType);
                        intent.putExtra("type", type);
                        intent.putExtra("type2", "00");
                        context.startActivityForResult(intent, 0);
                    }

                }
            });
            String rel_time2 = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                    + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
            ((ViewHolderThrf) holder).tv_time.setText(rel_time2);
            ((ViewHolderThrf) holder).iv_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            final String createTime = json.getString("createTime");
            final String finalCountPinglun = countPinglun;
            ((ViewHolderThrf) holder).rl_pinglun.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCommentEditText("00", sID, myuserID, myNick, dynamicSeq, createTime, ((ViewHolderThrf) holder).tv_count_pl, Integer.valueOf(finalCountPinglun));
                }
            });

            //线下价  点击导航
            ((ViewHolderThrf) holder).btn_right.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String userID = json.getString("uName");
                    final String loginId = json.getString("uLoginId");
                    final String sex1 = json.getString("uSex");
                    final String lat = json.getString("lat");
                    final String lng = json.getString("lng");

                    if (lat.equals("0") || lng.equals("0")) {

                        if (json.getString("resv1") != null && json.getString("resv2") != null) {

                            String[] strLat = new String[]{json.getString("resv2")};
                            final String[] strLong = new String[]{json.getString("resv1")};
                            final String[] strLoginId = new String[]{loginId};
                            final String[] strName = new String[]{userID};
                            final String[] strSex = new String[]{sex1};
                            Intent intent = new Intent(context, BaiDuFLocationActivity.class);
                            intent.putExtra("lat", strLat);
                            intent.putExtra("lng", strLong);
                            intent.putExtra("loginId", strLoginId);
                            intent.putExtra("name", strName);
                            intent.putExtra("sex", strSex);
                            intent.putExtra("biaoshi", "导航");
                            context.startActivity(intent);

                        } else {

                            Toast.makeText(context, "位置获取失败,建议直接与用户沟通", Toast.LENGTH_SHORT).show();

                        }

                    } else {

                        String[] strLat = new String[]{lat};
                        final String[] strLong = new String[]{lng};
                        final String[] strLoginId = new String[]{loginId};
                        final String[] strName = new String[]{userID};
                        final String[] strSex = new String[]{sex1};
                        Intent intent = new Intent(context, BaiDuFLocationActivity.class);
                        intent.putExtra("lat", strLat);
                        intent.putExtra("lng", strLong);
                        intent.putExtra("loginId", strLoginId);
                        intent.putExtra("name", strName);
                        intent.putExtra("sex", strSex);
                        intent.putExtra("biaoshi", "导航");
                        context.startActivity(intent);

                    }

                }
            });


            if (json.getString("authType").equals("03")) {

                ((ViewHolderThrf) holder).authBtn3.setText("取消隐藏");
                ((ViewHolderThrf) holder).authBtn3.setBackgroundResource(R.drawable.btn_corner_orange7);
                ((ViewHolderThrf) holder).tvCompany.setText("(仅用户自己可见)");
                ((ViewHolderThrf) holder).tvCompany.setTextColor(Color.parseColor("#FF0000"));

            } else {

                ((ViewHolderThrf) holder).authBtn3.setText("隐藏");
                ((ViewHolderThrf) holder).authBtn3.setBackgroundResource(R.drawable.btn_corner_blue7);

            }


            ((ViewHolderThrf) holder).authBtn3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = FXConstant.URL_UPDATEDYNAMICRECOMD;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            if (json.getString("authType").equals("03")) {

                                json.put("authType", "01");
                                notifyDataSetChanged();
                                Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();

                            } else {

                                json.put("authType", "03");
                                notifyDataSetChanged();
                                Toast.makeText(context, "已隐藏该动态", Toast.LENGTH_SHORT).show();

                            }

                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();

                            param.put("userId", sID);
                            param.put("dynamicSeq", dynamicSeq);
                            param.put("createTime", createTime);

                            if (json.getString("authType").equals("03")) {

                                param.put("authtype", "01");

                            } else {
                                param.put("authtype", "03");
                            }

                            return param;
                        }
                    };

                    MySingleton.getInstance(context).addToRequestQueue(request);

                }
            });


            ((ViewHolderThrf) holder).authBtn1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //禁止登录

                    String url = FXConstant.URL_INSERTFREEZELOGIN;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            Toast.makeText(context, "已禁止该用户登录", Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();

                            param.put("u_id", sID);

                            param.put("freezeType", "01");

                            return param;
                        }
                    };

                    MySingleton.getInstance(context).addToRequestQueue(request);

                }
            });

            ((ViewHolderThrf) holder).authBtn2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    //禁止所有
                    UpdateUserAuth(sID, "4");

                }
            });

            //商业界面转发
        } else if (holder instanceof ViewHolderThrfzhf) {
            ((ViewHolderThrfzhf) holder).rl_xiaoliang.setVisibility(View.VISIBLE);
            final JSONObject json = users.get(position);
            // 如果数据出错....
            if (json == null || json.size() == 0) {
                users.remove(position);
                this.notifyDataSetChanged();
            }
            String views1 = json.getString("views");

            if (views1 == null || "".equals(views1)) {
                views1 = "0";
            }
            if (views1 != null && Integer.valueOf(views1) > 9999) {
                Double a = Double.valueOf(views1) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                views1 = str + "万";
            }
            ((ViewHolderThrfzhf) holder).tv_count_llc.setText(views1);
            final String userID = json.getString("uName");
            final String content = json.getString("content");
            String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
            String newContent = TextUtils.isEmpty(json.getString("newcontent")) ? "" : json.getString("newcontent");
            final String firstName = json.getJSONObject("userInfo").getString("uName");
            String firstImage = json.getJSONObject("userInfo").getString("uImage");
            final String firstID = json.getJSONObject("userInfo").getString("uLoginId");
            String location = json.getString("location");
            String price = json.getString("price");
            String salePrice = json.getString("salePrice");
            String xiaoliang = TextUtils.isEmpty(json.getString("transTimes")) ? "0" : json.getString("transTimes");
            String countPinglun = json.getString("resv7");
            final String video = json.getString("video");
            final String videoPictures = json.getString("videoPictures");
            if (countPinglun == null || "".equals(countPinglun)) {
                countPinglun = "0";
            }
            if (price == null || "".equals(price)) {
                price = "0";
            }

            if (salePrice == null || "".equals(salePrice)) {
                salePrice = "0";
            }
            if (countPinglun != null && Integer.valueOf(countPinglun) > 9999) {

                Double a = Double.valueOf(countPinglun) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                countPinglun = str + "万";

            }
            ((ViewHolderThrfzhf) holder).tv_count_pl.setText(countPinglun);
            String forwardTimes = json.getString("forwardTimes");
            if (forwardTimes == null || "".equals(forwardTimes)) {
                forwardTimes = "0";
            }
            if (forwardTimes != null && Integer.valueOf(forwardTimes) > 9999) {

                Double a = Double.valueOf(forwardTimes) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                forwardTimes = str + "万";

            }
            ((ViewHolderThrfzhf) holder).tv_count_zhf.setText(forwardTimes);
            ((ViewHolderThrfzhf) holder).tv_xiaoliang.setText(xiaoliang);
            if (dType.equals("04")) {
                ((ViewHolderThrfzhf) holder).tv_shpjg.setText("￥" + salePrice);
                ((ViewHolderThrfzhf) holder).tv_shpyj.setText("￥" + price);
            } else if (dType.equals("03")) {
                ((ViewHolderThrfzhf) holder).tv_shpjg.setText("￥" + price);
            }
            ((ViewHolderThrfzhf) holder).tv_first_nick.setText(firstName + ":");
            if (firstImage != null) {
                if (firstImage.length() > 1) {
                    String[] orderProjectArray = firstImage.split("\\|");
                    firstImage = orderProjectArray[0];
                }
            }
            String firstSex = json.getJSONObject("userInfo").getString("uSex");
            if ("00".equals(firstSex)) {
                ((ViewHolderThrfzhf) holder).tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
            } else {
                ((ViewHolderThrfzhf) holder).tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
            }
            if (firstImage != null && !(firstImage == "")) {
                ((ViewHolderThrfzhf) holder).iv_first_avatar.setVisibility(View.VISIBLE);
                ((ViewHolderThrfzhf) holder).tvfirst_TitleA.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + firstImage, ((ViewHolderThrfzhf) holder).iv_first_avatar, DemoApplication.mOptions);
            } else {
                ((ViewHolderThrfzhf) holder).iv_first_avatar.setVisibility(View.INVISIBLE);
                ((ViewHolderThrfzhf) holder).tvfirst_TitleA.setVisibility(View.VISIBLE);
                ((ViewHolderThrfzhf) holder).tvfirst_TitleA.setText(TextUtils.isEmpty(firstName) ? firstID : firstName);
            }
            ((ViewHolderThrfzhf) holder).iv_first_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, firstID));
                }
            });
            ((ViewHolderThrfzhf) holder).tv_first_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, firstID));
                }
            });
            final String sum = json.getString("sum");
            if (sum != null) {
                String balance = TextUtils.isEmpty(json.getString("redBalance")) ? "0" : json.getString("redBalance");
                double redBalance = Double.valueOf(balance);
                double oncePrice = Double.valueOf(json.getString("oncePrice"));
                int cishu = (int) ((redBalance * 100) / (oncePrice * 100));
                ((ViewHolderThrfzhf) holder).rl_huikui1.setVisibility(View.VISIBLE);
                ((ViewHolderThrfzhf) holder).rl_huikui2.setVisibility(View.VISIBLE);
                ((ViewHolderThrfzhf) holder).tv_huikui_zonge.setText("￥" + sum);
                ((ViewHolderThrfzhf) holder).tv_huikui_yue.setText("￥" + redBalance);
                ((ViewHolderThrfzhf) holder).tv_huikui_zhaunfa.setText("￥" + oncePrice);
                ((ViewHolderThrfzhf) holder).tv_liulan_cishu.setText(cishu + "个");
            } else {
                ((ViewHolderThrfzhf) holder).rl_huikui1.setVisibility(View.GONE);
                ((ViewHolderThrfzhf) holder).rl_huikui2.setVisibility(View.GONE);
            }
            final String sID = json.getString("uLoginId");
            final String resv4 = TextUtils.isEmpty(json.getString("orderType")) ? "01" : json.getString("orderType");
            // String token = json.getString("token");
            final String rel_time = json.getString("createTime");
            String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
            final String dynamicSeq = json.getString("dynamicSeq");
            final String fromUId = sID;
            final String finalFirstImage = firstImage;
            ((ViewHolderThrfzhf) holder).tv_zhf_content.setText(newContent, position);
            ((ViewHolderThrfzhf) holder).tv_zhf_content.setVisibility(TextUtils.isEmpty(newContent) ? View.GONE : View.VISIBLE);
            ((ViewHolderThrfzhf) holder).tv_zhf_content.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type;
                    if (json.getString("fromUId") != null && !json.getString("fromUId").equalsIgnoreCase("null")) {
                        type = "02";
                    } else {
                        type = "01";
                    }
                    Intent intent = new Intent(context, DynaDetaActivity.class);
                    intent.putExtra("sID", sID);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("createTime", rel_time);
                    intent.putExtra("dType", dType);
                    intent.putExtra("type", type);
                    intent.putExtra("type2", "00");
                    context.startActivityForResult(intent, 0);
                }
            });
            if (avatar.length() > 40) {
                String[] orderProjectArray = avatar.split("\\|");
                avatar = orderProjectArray[0];
            }
            String sex = TextUtils.isEmpty(json.getString("uSex")) ? "01" : json.getString("uSex");
            String nianLing = TextUtils.isEmpty(json.getString("uAge")) ? "27" : json.getString("uAge");
            String company = TextUtils.isEmpty(json.getString("uCompany")) ? "暂未加入企业" : json.getString("uCompany");
            String uNation = json.getString("uNation");
            String resv5 = json.getString("resv5");
            String resv6 = json.getString("resv6");
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
            ((ViewHolderThrfzhf) holder).tvCompany.setText("(" + company + ")");
            ((ViewHolderThrfzhf) holder).tvNianLing.setText(nianLing);
            if ("00".equals(sex)) {
                ((ViewHolderThrfzhf) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
                ((ViewHolderThrfzhf) holder).tvNianLing.setBackgroundColor(Color.rgb(234, 121, 219));
            } else {
                ((ViewHolderThrfzhf) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
                ((ViewHolderThrfzhf) holder).tvNianLing.setBackgroundResource(R.color.accent_blue);
            }
            if (!(avatar == "")) {
                ((ViewHolderThrfzhf) holder).iv_avatar.setVisibility(View.VISIBLE);
                ((ViewHolderThrfzhf) holder).tvTitleA.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar, ((ViewHolderThrfzhf) holder).iv_avatar, DemoApplication.mOptions);
            } else {
                ((ViewHolderThrfzhf) holder).iv_avatar.setVisibility(View.INVISIBLE);
                ((ViewHolderThrfzhf) holder).tvTitleA.setVisibility(View.VISIBLE);
                ((ViewHolderThrfzhf) holder).tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
            }
            ((ViewHolderThrfzhf) holder).tv_nick.setText(userID);
            final String shareRed = json.getString("shareRed");
            final String friendsNumber = json.getString("friendsNumber");
            String onceJine = null;
            if (friendsNumber != null && !"".equals(friendsNumber)) {
                onceJine = friendsNumber.split("\\|")[0];
            }
            if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0 && onceJine != null && !"".equals(onceJine) && Double.parseDouble(onceJine) > 0) {
                ((ViewHolderThrfzhf) holder).tv_nick.setTextColor(Color.RED);
            } else {
                ((ViewHolderThrfzhf) holder).tv_nick.setTextColor(Color.rgb(87, 107, 149));
            }
            ((ViewHolderThrfzhf) holder).tv_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            ((ViewHolderThrfzhf) holder).tvTitleA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            String resv1 = TextUtils.isEmpty(json.getString("resv1")) ? "" : json.getString("resv1");
            String resv2 = TextUtils.isEmpty(json.getString("resv2")) ? "" : json.getString("resv2");
            double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
            double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());
            if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1, longitude1);
                double distance = DistanceUtil.getDistance(ll, ll1);
                double dou = distance / 1000;
                String str = String.format("%.2f", dou);//format 返回的是字符串
                if (str != null && dou >= 10000) {
                    ((ViewHolderThrfzhf) holder).tvDistance.setText("隐藏");
                } else {
                    ((ViewHolderThrfzhf) holder).tvDistance.setText(str + "km");
                }
            } else {
                ((ViewHolderThrfzhf) holder).tvDistance.setText("3km之内");
            }
            String balance = "";
            if (dType.equals("04")) {
                balance = salePrice;
            } else if (dType.equals("03")) {
                balance = price;
            }
            final ViewHolderThrfzhf finalHolder = ((ViewHolderThrfzhf) holder);
            final String finalBalance = balance;
            if (DemoHelper.getInstance().isLoggedIn(context)) {
                final String finalRel_time = rel_time;
                ((ViewHolderThrfzhf) holder).btn_xiadan.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String distance = finalHolder.tvDistance.getText().toString();
                        showDialog(sID, resv4, "动态订单", distance, finalBalance, finalRel_time, dynamicSeq);
                    }
                });
            } else {
                ((ViewHolderThrfzhf) holder).btn_xiadan.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                });
            }
            // 设置文章中的图片
            ((ViewHolderThrfzhf) holder).image_1.setVisibility(View.GONE);
            ((ViewHolderThrfzhf) holder).image_2.setVisibility(View.GONE);
            ((ViewHolderThrfzhf) holder).image_3.setVisibility(View.GONE);
            ((ViewHolderThrfzhf) holder).image_4.setVisibility(View.GONE);
            ((ViewHolderThrfzhf) holder).image_5.setVisibility(View.GONE);
            ((ViewHolderThrfzhf) holder).image_6.setVisibility(View.GONE);
            ((ViewHolderThrfzhf) holder).image_7.setVisibility(View.GONE);
            ((ViewHolderThrfzhf) holder).image_8.setVisibility(View.GONE);
            View v2 = null;
            if (video != null && videoPictures != null) {
                ((ViewHolderThrfzhf) holder).rl_video.setVisibility(View.VISIBLE);
                boolean setUp = ((ViewHolderThrfzhf) holder).videoPlayer.setUp(FXConstant.URL_VIDEO + video, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        "");
                v2 = ((ViewHolderThrfzhf) holder).rl_video;
                if (setUp) {
                    Glide.with(context).load(FXConstant.URL_VIDEO + videoPictures)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .error(R.drawable.default_error)
                            .crossFade().into(((ViewHolderThrfzhf) holder).videoPlayer.thumbImageView);
                } else {
                    Toast.makeText(context, "视频播放失败", Toast.LENGTH_SHORT).show();
                }
                ((ViewHolderThrfzhf) holder).tv_video.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.e("socialmain,", "touch");
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            updateLiulancishu(rel_time, dynamicSeq);
                            updateDeLiulancishu(rel_time, sID);
                        }
                        return false;
                    }
                });
            } else {
                ((ViewHolderThrfzhf) holder).rl_video.setVisibility(View.GONE);
                if (!imageStr.equals("")) {
                    String[] images = imageStr.split("\\|");
                    int imNumb = images.length;
                    ((ViewHolderThrfzhf) holder).image_1.setVisibility(View.VISIBLE);
                    v2 = ((ViewHolderThrfzhf) holder).image_1;
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[0], ((ViewHolderThrfzhf) holder).image_1, DemoApplication.mOptions2);


                    ((ViewHolderThrfzhf) holder).image_1.setOnClickListener(new ImageListener(images, 0, rel_time, dynamicSeq, sID));
                    if (imNumb > 1) {
                        ((ViewHolderThrfzhf) holder).image_2.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[1], ((ViewHolderThrfzhf) holder).image_2, DemoApplication.mOptions2);
                        ((ViewHolderThrfzhf) holder).image_2.setOnClickListener(new ImageListener(images, 1, rel_time, dynamicSeq, sID));
                        if (imNumb > 2) {
                            ((ViewHolderThrfzhf) holder).image_3.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[2], ((ViewHolderThrfzhf) holder).image_3, DemoApplication.mOptions2);
                            ((ViewHolderThrfzhf) holder).image_3.setOnClickListener(new ImageListener(images, 2, rel_time, dynamicSeq, sID));
                            if (imNumb > 3) {
                                ((ViewHolderThrfzhf) holder).image_4.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[3], ((ViewHolderThrfzhf) holder).image_4, DemoApplication.mOptions2);
                                ((ViewHolderThrfzhf) holder).image_4.setOnClickListener(new ImageListener(images, 3, rel_time, dynamicSeq, sID));
                                if (imNumb > 4) {
                                    ((ViewHolderThrfzhf) holder).image_5.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[4], ((ViewHolderThrfzhf) holder).image_5, DemoApplication.mOptions2);
                                    ((ViewHolderThrfzhf) holder).image_5.setOnClickListener(new ImageListener(images, 4, rel_time, dynamicSeq, sID));
                                    if (imNumb > 5) {
                                        ((ViewHolderThrfzhf) holder).image_6.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[5], ((ViewHolderThrfzhf) holder).image_6, DemoApplication.mOptions2);
                                        ((ViewHolderThrfzhf) holder).image_6.setOnClickListener(new ImageListener(images, 5, rel_time, dynamicSeq, sID));
                                        if (imNumb > 6) {
                                            ((ViewHolderThrfzhf) holder).image_7.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[6], ((ViewHolderThrfzhf) holder).image_7, DemoApplication.mOptions2);
                                            ((ViewHolderThrfzhf) holder).image_7.setOnClickListener(new ImageListener(images, 6, rel_time, dynamicSeq, sID));
                                            if (imNumb > 7) {
                                                ((ViewHolderThrfzhf) holder).image_8.setVisibility(View.VISIBLE);
                                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[7], ((ViewHolderThrfzhf) holder).image_8, DemoApplication.mOptions2);
                                                ((ViewHolderThrfzhf) holder).image_8.setOnClickListener(new ImageListener(images, 7, rel_time, dynamicSeq, sID));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 显示位置
//            if (location != null && !location.equals("0")) {
//                holder.tv_location.setVisibility(View.VISIBLE);
//                holder.tv_location.setText(location);
//            }
            // 显示文章内容
            // .setText(content);
            final View finalV = v2;
            ((ViewHolderThrfzhf) holder).rl_zhuanfa.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!DemoHelper.getInstance().isLoggedIn(context)) {
                        Toast.makeText(context, "登陆后才可以转发哦！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (sum != null) {
                        String balance = TextUtils.isEmpty(json.getString("redBalance")) ? "0" : json.getString("redBalance");
                        double redBalance = Double.valueOf(balance);
                        final String oncePrice = json.getString("oncePrice");
                        if (redBalance > 0) {

                            String url = FXConstant.URL_SEARCH_REDAUTH;

                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {

                                    Log.d("chen", "onResponse" + s);

                                    try {

                                        org.json.JSONObject object = new org.json.JSONObject(s);
                                        String code = object.getString("code");

                                        if (code == null || "".equals(code) || code.equalsIgnoreCase("null")) {
                                            //处理null

                                            UserPermissionUtil.getUserPermission(context, DemoHelper.getInstance().getCurrentUsernName(), "8", new UserPermissionUtil.UserPermissionListener() {
                                                @Override
                                                public void onAllow() {

                                                    PermissionUtil permissionUtil = new PermissionUtil((FragmentActivity) context);
                                                    permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                                                                    , Manifest.permission.READ_PHONE_STATE
                                                                    , Manifest.permission.ACCESS_WIFI_STATE},
                                                            new PermissionListener() {
                                                                @Override
                                                                public void onGranted() {
                                                                    queryhbzgCount(json.getString("views"), rel_time, null, ((ViewHolderThrfzhf) holder).card, finalV, oncePrice, dynamicSeq, sID, fromUId, finalFirstImage, userID, content, true);
                                                                }

                                                                @Override
                                                                public void onDenied(List<String> deniedPermission) {
                                                                    Toast.makeText(context.getApplicationContext(), "您拒绝了获取定位的权限！无法赚取红包 请手动开启权限", Toast.LENGTH_SHORT).show();
                                                                }

                                                                @Override
                                                                public void onShouldShowRationale(List<String> deniedPermission) {
                                                                    Toast.makeText(context.getApplicationContext(), "您拒绝了获取定位的权限！无法赚取红包 请手动开启权限", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                                                    context.startActivity(intent);
                                                                }
                                                            });

                                                }

                                                @Override
                                                public void onBan() {
                                                    ToastUtils.showNOrmalToast(context.getApplicationContext(), "您的账户已被禁止转发红包");

                                                }
                                            });

                                        } else {
                                            //禁止转了  要求发短信才可以

                                            final LayoutInflater inflater1 = LayoutInflater.from(context);
                                            RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                                            final Dialog dialog1 = new AlertDialog.Builder(context, R.style.Dialog).create();
                                            dialog1.show();
                                            dialog1.getWindow().setContentView(layout1);
                                            dialog1.setCanceledOnTouchOutside(true);
                                            dialog1.setCancelable(true);
                                            TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                                            Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                                            final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                                            TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                                            title.setText("温馨提示");
                                            btnOK1.setText("确定");
                                            btnCancel1.setText("取消");
                                            title_tv1.setText("您需要邀请20个用户注册才可以继续赚取红包");
                                            btnCancel1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog1.dismiss();
                                                }
                                            });
                                            btnOK1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog1.dismiss();

                                                    //跳转到通讯录界面邀请好友注册  标识是红包

                                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                                                            != PackageManager.PERMISSION_GRANTED) {
                                                        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CONTACTS}, 1);
                                                    } else {
                                                        Intent intent = new Intent(context, AddressListActivity.class);

                                                        intent.putExtra("redAuth", "yes");

                                                        context.startActivityForResult(intent, 0);
                                                    }
                                                }
                                            });
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    if (volleyError != null) {
                                        Log.e("hongbao", volleyError.getMessage());
                                        Log.d("chen", "hongbao" + volleyError.getMessage());
                                    }
                                    Toast.makeText(context, "网络不稳定,请稍后重试", Toast.LENGTH_SHORT).show();

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> param = new HashMap<>();

                                    param.put("rid", DemoHelper.getInstance().getCurrentUsernName());

                                    return param;
                                }
                            };
                            MySingleton.getInstance(context).addToRequestQueue(request);

                        } else {
                            showZhfDialog(json.getString("views"), rel_time, null, ((ViewHolderThrfzhf) holder).card, finalV, "0", dynamicSeq, sID, fromUId, finalFirstImage, userID, content, false, "0");
                        }
                    } else {
                        showZhfDialog(json.getString("views"), rel_time, null, ((ViewHolderThrfzhf) holder).card, finalV, "0", dynamicSeq, sID, fromUId, finalFirstImage, userID, content, false, "0");
                    }
                }
            });
            ((ViewHolderThrfzhf) holder).tv_content2.setText(content);
            String rel_time2 = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                    + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
            ((ViewHolderThrfzhf) holder).tv_time.setText(rel_time2);
            ((ViewHolderThrfzhf) holder).iv_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            final String createTime = json.getString("createTime");
            final String finalCountPinglun = countPinglun;
            ((ViewHolderThrfzhf) holder).rl_pinglun.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCommentEditText("01", sID, myuserID, myNick, dynamicSeq, createTime, ((ViewHolderThrfzhf) holder).tv_count_pl, Integer.valueOf(finalCountPinglun));
                }
            });
            //派单界面
        } else if (holder instanceof ViewHolderFive) {
            final JSONObject json = users.get(position);
            // 如果数据出错....
            if (json == null || json.size() == 0) {
                users.remove(position);
                this.notifyDataSetChanged();
            }

            String views = json.getString("views");
            if (views == null || "".equals(views)) {
                views = "0";
            }
            if (views != null && Integer.valueOf(views) > 9999) {

                Double a = Double.valueOf(views) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                views = str + "万";

            }
            ((ViewHolderFive) holder).tv_count_llc.setText(views);
            final String userID = json.getString("uName");
            final String loginId = json.getString("uLoginId");
            final String sex1 = json.getString("uSex");
            final String content = "              【" + json.getString("task_label") + "】：" + json.getString("content");
            String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
            String location = json.getString("location");
            String countPinglun = json.getString("resv7");
            final String task_label = json.getString("task_label");
            final String task_position = json.getString("task_position");
            String task_locaName = json.getString("task_locaName");
            String task_jurisdiction = json.getString("task_jurisdiction");
            String recommend = json.getString("recommendCount");
            String ordercomment = json.getString("orderCommentCount");
            String demandType = json.getString("demandType");

            if (demandType.equals("1")) {

                ((ViewHolderFive) holder).tv_demandType.setText("需要产品");
                ((ViewHolderFive) holder).tv_demandType.setBackgroundColor(Color.parseColor("#FF8D00"));

            } else if (demandType.equals("2")) {

                ((ViewHolderFive) holder).tv_demandType.setText("需要方案");
                ((ViewHolderFive) holder).tv_demandType.setBackgroundColor(Color.parseColor("#46c01b"));

            } else if (demandType.equals("3")) {

                ((ViewHolderFive) holder).tv_demandType.setText("工程招标");
                ((ViewHolderFive) holder).tv_demandType.setBackgroundColor(Color.parseColor("#FF0000"));

            } else {

                ((ViewHolderFive) holder).tv_demandType.setText("需要服务");
                ((ViewHolderFive) holder).tv_demandType.setBackgroundColor(Color.parseColor("#3EC5FF"));

            }


            responseTime = json.getString("responseTime");
            firstDistance = json.getString("firstDistance");
            resv1 = json.getString("resv1");
            resv2 = json.getString("resv2");
            final String video = json.getString("video");
            final String videoPictures = json.getString("videoPictures");

            final String sID = json.getString("uLoginId");
            final String createTime = json.getString("createTime");
            // String token = json.getString("token");
            final String rel_time = json.getString("createTime");
            final String floorPrice = json.getString("floorPrice");
            String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
            final String dynamicSeq = json.getString("dynamicSeq");
            String sex = TextUtils.isEmpty(json.getString("uSex")) ? "01" : json.getString("uSex");
            String nianLing = TextUtils.isEmpty(json.getString("uAge")) ? "27" : json.getString("uAge");
            String company = TextUtils.isEmpty(json.getString("uCompany")) ? "暂未加入企业" : json.getString("uCompany");
            String uNation = json.getString("uNation");
            String resv5 = json.getString("resv5");
            String resv6 = json.getString("resv6");

            String[] strloca = null;

            if (task_position != null && !"".equals(task_position)) {
                strloca = task_position.split("\\|");
            }
            String resv2 = "", resv1 = "";
            if (strloca != null && strloca.length > 0) {
                resv2 = strloca[0];
            }
            if (strloca != null && strloca.length > 1) {
                resv1 = strloca[1];
            }
            double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
            double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());

            SharedPreferences mSharedPreferences1 = context.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);

//            if (!sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences1.getString(dynamicSeq, "123").equalsIgnoreCase("123") && !ComparePriceAndVipLevel(json.getString("floorPrice"),task_position) && (Double.parseDouble(isHaveMargin) < 100 ||  Double.parseDouble(isHaveMargin) < Double.parseDouble(floorPrice))) {
//                Log.d("chen", content + "没有转发过");
//
//                ((ViewHolderFive) holder).tvDistance.setText("分享看距离");
//
//                ((ViewHolderFive) holder).tv_nick.setText(userID.substring(0,1)+"**");
//
//            } else{
//                Log.d("chen", content + "转发过");

            ((ViewHolderFive) holder).tv_nick.setText(userID);

            if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1, longitude1);
                double distance = DistanceUtil.getDistance(ll, ll1);
                double dou = distance / 1000;
                String str = String.format("%.2f", dou);//format 返回的是字符串
                if (str != null && dou >= 10000) {
                    ((ViewHolderFive) holder).tvDistance.setText("隐藏");
                } else {
                    ((ViewHolderFive) holder).tvDistance.setText(str + "km");
                }
            } else {
                ((ViewHolderFive) holder).tvDistance.setText("3km之内");
            }

            // }

            ((ViewHolderFive) holder).tvDistance.setVisibility(View.GONE);

            SharedPreferences mSharedPreferences = context.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);

            //((ViewHolderFive) holder).tv_location.setTextSize(11);


            //转发过
            ((ViewHolderFive) holder).tv_nick.setText(userID);
            if (task_locaName == null || "".equals(task_locaName) || task_locaName.equalsIgnoreCase("null")) {
                ((ViewHolderFive) holder).tv_location.setText("地点错误，建议主动联系用户沟通");

            } else {

                if (json.getString("remark").equals("05")) {
                    task_locaName = subString(task_locaName);
                    final String lat = task_position.split("\\|")[0];
                    final String lng = task_position.split("\\|")[1];
                    ((ViewHolderFive) holder).tv_location.setText(task_locaName + " 距离" + ((ViewHolderFive) holder).tvDistance.getText().toString().trim());

                    ((ViewHolderFive) holder).tv_location.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String[] strLat = new String[]{lat};
                            final String[] strLong = new String[]{lng};
                            final String[] strLoginId = new String[]{loginId};
                            final String[] strName = new String[]{userID};
                            final String[] strSex = new String[]{sex1};
                            Intent intent = new Intent(context, BaiDuFLocationActivity.class);
                            intent.putExtra("lat", strLat);
                            intent.putExtra("lng", strLong);
                            intent.putExtra("loginId", strLoginId);
                            intent.putExtra("name", strName);
                            intent.putExtra("sex", strSex);
                            intent.putExtra("biaoshi", "导航");
                            context.startActivity(intent);
                        }
                    });

                    ((ViewHolderFive) holder).btn_daohang.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String[] strLat = new String[]{lat};
                            final String[] strLong = new String[]{lng};
                            final String[] strLoginId = new String[]{loginId};
                            final String[] strName = new String[]{userID};
                            final String[] strSex = new String[]{sex1};
                            Intent intent = new Intent(context, BaiDuFLocationActivity.class);
                            intent.putExtra("lat", strLat);
                            intent.putExtra("lng", strLong);
                            intent.putExtra("loginId", strLoginId);
                            intent.putExtra("name", strName);
                            intent.putExtra("sex", strSex);
                            intent.putExtra("biaoshi", "导航");
                            context.startActivity(intent);
                        }
                    });
                }

            }


//            if (currentType.equals("02")){
//
//
//                if ((!json.getString("uLoginId").equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(json.getString("dynamicSeq"), "123").equalsIgnoreCase("123")) && !(isVip>0&&vipLevel.equals("3"))){
//
//                    if (!DemoHelper.getInstance().isLoggedIn(context)){
//
//                        ((ViewHolderFive) holder).tv_location.setText("未登录");
//                        ((ViewHolderFive) holder).tv_nick.setText(userID.substring(0,1)+"**");
//
//                    }else {
//
//                        //0审核中  1通过  2拒绝  3伪审核中（可以查看）
//                        if (zbShareAuth.equals("1") || zbShareAuth.equals("3")){
//
//                            ((ViewHolderFive) holder).tv_location.setText("分享后   可查看");
//                            ((ViewHolderFive) holder).tv_nick.setText(userID.substring(0,1)+"**");
//
//                        }else {
//
//                            ((ViewHolderFive) holder).tv_location.setText("完成任务开通权限");
//                            ((ViewHolderFive) holder).tv_nick.setText(userID.substring(0,1)+"**");
//
//                        }
//
//                    }
//
//                }else {
//
//
//
//                }
//
//
//            }else {
//
//                if (!json.getString("uLoginId").equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(json.getString("dynamicSeq"), "123").equalsIgnoreCase("123") && !ComparePriceAndVipLevel(json.getString("floorPrice"),task_position) && (Double.parseDouble(isHaveMargin) < 100 ||  Double.parseDouble(isHaveMargin) < Double.parseDouble(json.getString("floorPrice")))) {
//                    //没有转发过
//
//                    if (currentType.equals("05")){
//
//                        if ( Double.parseDouble(isHaveMargin) < Double.parseDouble(json.getString("floorPrice")) && Double.parseDouble(isHaveMargin) > 0)
//                        {
//                            ((ViewHolderFive) holder).tv_location.setText("质保低于出价 需分享后查看");
//
//                        }else
//                        {
//
//                            if (!DemoHelper.getInstance().isLoggedIn(context)){
//
//                                ((ViewHolderFive) holder).tv_location.setText("未登录");
//
//                            }else {
//
//                                ((ViewHolderFive) holder).tv_location.setText("无质保 需分享后 才能查看");
//
//                            }
//
//                        }
//
//                    }else {
//
//                        if (!DemoHelper.getInstance().isLoggedIn(context)){
//
//                            ((ViewHolderFive) holder).tv_location.setText("未登录");
//
//                        }else {
//
//                            ((ViewHolderFive) holder).tv_location.setText("分享后可查看(VIP可直接查看)");
//
//                        }
//
//                    }
//
//
//
//                } else{
//
//                    //转发过
//
//                    if (task_locaName==null||"".equals(task_locaName)||task_locaName.equalsIgnoreCase("null")){
//                        ((ViewHolderFive) holder).tv_location.setText("地点错误，建议主动联系用户沟通");
//
//                    }else {
//
//                        if (json.getString("remark").equals("05")){
//                            task_locaName = subString(task_locaName);
//                            final String lat = task_position.split("\\|")[0];
//                            final String lng = task_position.split("\\|")[1];
//                            ((ViewHolderFive) holder).tv_location.setText(task_locaName+" 距离"+((ViewHolderFive) holder).tvDistance.getText().toString().trim());
//
//                            ((ViewHolderFive) holder).tv_location.setOnClickListener(new OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    String[] strLat = new String[]{lat};
//                                    final String[] strLong = new String[]{lng};
//                                    final String[] strLoginId = new String[]{loginId};
//                                    final String[] strName = new String[]{userID};
//                                    final String[] strSex = new String[]{sex1};
//                                    Intent intent = new Intent(context, BaiDuFLocationActivity.class);
//                                    intent.putExtra("lat", strLat);
//                                    intent.putExtra("lng", strLong);
//                                    intent.putExtra("loginId", strLoginId);
//                                    intent.putExtra("name", strName);
//                                    intent.putExtra("sex", strSex);
//                                    intent.putExtra("biaoshi","导航");
//                                    context.startActivity(intent);
//                                }
//                            });
//
//                            ((ViewHolderFive) holder).btn_daohang.setOnClickListener(new OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    String[] strLat = new String[]{lat};
//                                    final String[] strLong = new String[]{lng};
//                                    final String[] strLoginId = new String[]{loginId};
//                                    final String[] strName = new String[]{userID};
//                                    final String[] strSex = new String[]{sex1};
//                                    Intent intent = new Intent(context, BaiDuFLocationActivity.class);
//                                    intent.putExtra("lat", strLat);
//                                    intent.putExtra("lng", strLong);
//                                    intent.putExtra("loginId", strLoginId);
//                                    intent.putExtra("name", strName);
//                                    intent.putExtra("sex", strSex);
//                                    intent.putExtra("biaoshi","导航");
//                                    context.startActivity(intent);
//                                }
//                            });
//                        }
//
//                    }
//
//                }
//
//            }


            final String sum = json.getString("sum");
            if (sum != null) {
                String balance = TextUtils.isEmpty(json.getString("redBalance")) ? "0" : json.getString("redBalance");
                double redBalance = Double.valueOf(balance);

                if (redBalance > 0) {

                    ((ViewHolderFive) holder).image_shareRed.setVisibility(View.VISIBLE);

                } else {
                    ((ViewHolderFive) holder).image_shareRed.setVisibility(View.INVISIBLE);
                }

//                double oncePrice = Double.valueOf(json.getString("oncePrice"));
//                int cishu = (int) ((redBalance*100)/(oncePrice*100));
//                ((ViewHolderFive) holder).ll_huikui.setVisibility(View.VISIBLE);
//                ((ViewHolderFive) holder).tv_huikui_zonge.setText("￥" + sum);
//                ((ViewHolderFive) holder).tv_huikui_yue.setText("￥" + redBalance);
//                ((ViewHolderFive) holder).tv_huikui_zhaunfa.setText("￥" + oncePrice);
//                ((ViewHolderFive) holder).tv_liulan_cishu.setText(cishu + "个");

            } else {
                ((ViewHolderFive) holder).image_shareRed.setVisibility(View.INVISIBLE);
            }

            if (countPinglun == null || "".equals(countPinglun)) {
                countPinglun = "0";
            }
            if (countPinglun != null && Integer.valueOf(countPinglun) > 99) {
                countPinglun = "99+";
            }

            if (countPinglun.equals("0")) {

                ((ViewHolderFive) holder).tv_recommend.setVisibility(View.INVISIBLE);
                ((ViewHolderFive) holder).tv_recommend.setText("");

            } else {

                ((ViewHolderFive) holder).tv_recommend.setVisibility(View.VISIBLE);

                if (Integer.valueOf(countPinglun) > 99) {

                    ((ViewHolderFive) holder).tv_recommend.setText("报价 99+");

                } else {

                    ((ViewHolderFive) holder).tv_recommend.setText("报价 " + Integer.valueOf(countPinglun));

                }

                ((ViewHolderFive) holder).tv_recommend.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (sID.equals(DemoHelper.getInstance().getCurrentUsernName())) {

                            context.startActivity(new Intent(context, DynamicRecommendActivity.class).putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime));

                        }

                    }
                });

            }

            //原来的报价人数 现在改成推荐人数（附近）
            //  ((ViewHolderFive) holder).tv_count_pl.setText(recommend);

            if (Integer.valueOf(recommend) == 0) {

                if (currentType.equals("02")) {

                    ((ViewHolderFive) holder).tv_count_pl.setText("0");
                    ((ViewHolderFive) holder).tv_title_pl.setText("招标：");

                } else {

                    ((ViewHolderFive) holder).tv_count_pl.setText("");
                    ((ViewHolderFive) holder).tv_title_pl.setText("匹配中");

                }

                ((ViewHolderFive) holder).authBtn5.setBackgroundResource(R.drawable.btn_corner_blue7);

            } else {

                ((ViewHolderFive) holder).authBtn5.setBackgroundResource(R.drawable.btn_corner_orange7);

                if (currentType.equals("02")) {

                    ((ViewHolderFive) holder).tv_count_pl.setText(recommend);
                    ((ViewHolderFive) holder).tv_title_pl.setText("招标:");

                } else {

                    ((ViewHolderFive) holder).tv_count_pl.setText(recommend);
                    ((ViewHolderFive) holder).tv_title_pl.setText("匹配:");
                }

            }

            String forwardTimes = json.getString("forwardTimes");
            if (forwardTimes == null || "".equals(forwardTimes)) {
                forwardTimes = "0";
            }
            if (forwardTimes != null && Integer.valueOf(forwardTimes) > 9999) {

                Double a = Double.valueOf(forwardTimes) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                forwardTimes = str + "万";

            }
            ((ViewHolderFive) holder).tv_count_zhf.setText(forwardTimes);

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

            if (Integer.valueOf(ordercomment) == 0) {

                ((ViewHolderFive) holder).tv_comcount.setText("0");

            } else {

                if (Integer.valueOf(ordercomment) > 99) {

                    ((ViewHolderFive) holder).tv_comcount.setText("99+");

                } else {

                    ((ViewHolderFive) holder).tv_comcount.setText("" + Integer.valueOf(ordercomment));

                }

            }

            if (floorPrice == null || Double.parseDouble(floorPrice) <= 0) {

                ((ViewHolderFive) holder).tv_chujia.setVisibility(View.INVISIBLE);

            } else {

                ((ViewHolderFive) holder).tv_chujia.setVisibility(View.VISIBLE);

                if (Double.valueOf(floorPrice) > 9999) {

                    Double a = Double.valueOf(floorPrice) / 10000;

                    DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                    String str = myformat.format(a);
                    ((ViewHolderFive) holder).tv_chujia.setText("意向价： " + str + "万元");

                } else {

                    ((ViewHolderFive) holder).tv_chujia.setText("意向价： " + floorPrice + "元");
                }

            }

            final String authType = json.getString("authType");
            final String deviceType = json.getString("deviceType");

            if (authType.equals("03")) {

                ((ViewHolderFive) holder).tvCompany.setTextColor(Color.parseColor("#FF0000"));
                ((ViewHolderFive) holder).tvCompany.setText("(仅用户可见)");

                ((ViewHolderFive) holder).authBtn4.setText("取消隐藏");
                ((ViewHolderFive) holder).authBtn4.setBackgroundResource(R.drawable.btn_corner_orange7);

            } else {
                ((ViewHolderFive) holder).authBtn4.setText("隐藏");
                ((ViewHolderFive) holder).authBtn4.setBackgroundResource(R.drawable.btn_corner_blue7);
                ((ViewHolderFive) holder).tvCompany.setTextColor(Color.parseColor("#FF0000"));
                ((ViewHolderFive) holder).tvCompany.setText("(" + deviceType + ")");
            }


            ((ViewHolderFive) holder).tvNianLing.setText(nianLing);
            if ("00".equals(sex)) {
                ((ViewHolderFive) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
                ((ViewHolderFive) holder).tvNianLing.setBackgroundColor(Color.rgb(234, 121, 219));
            } else {
                ((ViewHolderFive) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
                ((ViewHolderFive) holder).tvNianLing.setBackgroundResource(R.color.accent_blue);
            }
            final String fromUId = sID;
            if (avatar.length() > 40) {
                String[] orderProjectArray = avatar.split("\\|");
                avatar = orderProjectArray[0];
            }
            if (!(avatar == "")) {
                ((ViewHolderFive) holder).iv_avatar.setVisibility(View.VISIBLE);
                ((ViewHolderFive) holder).tvTitleA.setVisibility(View.INVISIBLE);
                //ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+avatar,((ViewHolderFive) holder).iv_avatar, DemoApplication.mOptions);
                Glide.with(context).load(FXConstant.URL_AVATAR + avatar).into(((ViewHolderFive) holder).iv_avatar);
            } else {
                ((ViewHolderFive) holder).iv_avatar.setVisibility(View.INVISIBLE);
                ((ViewHolderFive) holder).tvTitleA.setVisibility(View.VISIBLE);
                ((ViewHolderFive) holder).tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
            }

            final String shareRed = json.getString("shareRed");
            final String friendsNumber = json.getString("friendsNumber");
            String exShareRed = "无";
            String onceJine = null;
            if (friendsNumber != null && !"".equals(friendsNumber)) {
                onceJine = friendsNumber.split("\\|")[0];
            }
            if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0 && onceJine != null && !"".equals(onceJine) && Double.parseDouble(onceJine) > 0) {
                ((ViewHolderFive) holder).tv_nick.setTextColor(Color.RED);
                exShareRed = "有";
            } else {
                ((ViewHolderFive) holder).tv_nick.setTextColor(Color.rgb(87, 107, 149));
                exShareRed = "无";
            }

            final String orderState = TextUtils.isEmpty(json.getString("orderState")) ? "00" : json.getString("orderState");

            ((ViewHolderFive) holder).tv_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID).putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime));
                }
            });

            ((ViewHolderFive) holder).tvTitleA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID)
                            .putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime));

                }
            });


            String firstImage = "";
            if (!avatar.equals("")) {
                firstImage = avatar.split("\\|")[0];
            }
            final String finalFirstImage = firstImage;

            final ViewHolderFive finalHolder = ((ViewHolderFive) holder);
            if (!DemoHelper.getInstance().isLoggedIn(context)) {
                ((ViewHolderFive) holder).btn_xiadan.setEnabled(true);
                ((ViewHolderFive) holder).btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green);
                ((ViewHolderFive) holder).btn_xiadan.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                });
            } else {

                if (!sID.equals(DemoHelper.getInstance().getCurrentUsernName())) {

                    if ("01".equals(orderState) || "02".equals(orderState)) {

                        ((ViewHolderFive) holder).btn_xiadan.setText("交易完成");
                        ((ViewHolderFive) holder).btn_xiadan.setEnabled(true);
                        ((ViewHolderFive) holder).btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_oriange);

                    } else if ("03".equals(orderState)) {

                        ((ViewHolderFive) holder).btn_xiadan.setText("派单结束");
                        ((ViewHolderFive) holder).btn_xiadan.setEnabled(true);
                        ((ViewHolderFive) holder).btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green_1);

                    } else {

                        String redImage = json.getString("redImage");

                        if ((redImage != null && redImage.length() > 7) || currentType.equals("02")) {

                            ((ViewHolderFive) holder).btn_xiadan.setText("投标报价");
                            ((ViewHolderFive) holder).btn_daohang.setText("订单坐标");

                        } else {

                            ((ViewHolderFive) holder).btn_xiadan.setText("接单报价");

                        }

                        ((ViewHolderFive) holder).btn_xiadan.setEnabled(true);
                        ((ViewHolderFive) holder).btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green);
                    }

                    final String finalExShareRed = exShareRed;

                    ((ViewHolderFive) holder).btn_xiadan.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            UserPermissionUtil.getUserPermission(context, DemoHelper.getInstance().getCurrentUsernName(), "7", new UserPermissionUtil.UserPermissionListener() {
                                @Override
                                public void onAllow() {

                                    if (json.getString("dynamicAuth").equals("0")) {

                                        //洽谈中 弹出提示框

                                        LayoutInflater inflaterD5 = LayoutInflater.from(context);
                                        LinearLayout layout5 = (LinearLayout) inflaterD5.inflate(R.layout.dialog_vipchat_alert, null);
                                        final Dialog dialog5 = new AlertDialog.Builder(context, R.style.Dialog).create();
                                        dialog5.show();
                                        dialog5.getWindow().setContentView(layout5);
                                        WindowManager.LayoutParams params = dialog5.getWindow().getAttributes();
                                        Display display = context.getWindowManager().getDefaultDisplay();
                                        params.width = (int) (display.getWidth() * 0.7); //使用这种方式更改了dialog的框宽
                                        dialog5.getWindow().setAttributes(params);
                                        dialog5.setCancelable(true);
                                        dialog5.setCanceledOnTouchOutside(true);

                                    } else {

                                        if ("01".equals(orderState) || "02".equals(orderState) || "03".equals(orderState)) {

                                            LayoutInflater inflaterD5 = LayoutInflater.from(context);
                                            LinearLayout layout5 = (LinearLayout) inflaterD5.inflate(R.layout.dialog_passivereceipt_alert, null);
                                            final Dialog dialog5 = new AlertDialog.Builder(context, R.style.Dialog).create();
                                            dialog5.show();
                                            dialog5.getWindow().setContentView(layout5);
                                            WindowManager.LayoutParams params = dialog5.getWindow().getAttributes();
                                            Display display = context.getWindowManager().getDefaultDisplay();
                                            params.width = (int) (display.getWidth() * 0.75); //使用这种方式更改了dialog的框宽
                                            dialog5.getWindow().setAttributes(params);
                                            dialog5.setCancelable(true);
                                            dialog5.setCanceledOnTouchOutside(true);

                                            TextView tv_mid = (TextView) layout5.findViewById(R.id.tv_mid);

                                            tv_mid.setOnClickListener(new OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    //跳转短信派单
                                                    dialog5.dismiss();
                                                    Intent intent = new Intent(context, MessageOrderIntroduceActivity.class);

                                                    context.startActivityForResult(intent, 0);

                                                }

                                            });

                                        } else {


                                            //判断是招标信息还是普通动态
                                            String redImage = json.getString("redImage");

                                            if (redImage != null && redImage.length() > 7) {

                                                Intent intent = new Intent(context, DynamicLinkDetailActivity.class);

                                                intent.putExtra("redImage", redImage);

                                                context.startActivityForResult(intent, 0);

                                            } else {
                                                //先弹出新加的联系方式提示框  然后点击报价在弹出的原来的

                                                LayoutInflater inflaterD6 = LayoutInflater.from(context);
                                                LinearLayout layout6 = (LinearLayout) inflaterD6.inflate(R.layout.dialog_communication_alert, null);
                                                final Dialog dialog6 = new AlertDialog.Builder(context, R.style.Dialog).create();
                                                dialog6.show();
                                                dialog6.getWindow().setContentView(layout6);
                                                WindowManager.LayoutParams params2 = dialog6.getWindow().getAttributes();
                                                Display display2 = context.getWindowManager().getDefaultDisplay();
                                                params2.width = (int) (display2.getWidth() * 0.7); //使用这种方式更改了dialog的框宽
                                                dialog6.getWindow().setAttributes(params2);
                                                dialog6.setCancelable(true);
                                                dialog6.setCanceledOnTouchOutside(true);

                                                TextView tv_chat1 = (TextView) layout6.findViewById(R.id.tv_chatmode1);
                                                TextView tv_chat2 = (TextView) layout6.findViewById(R.id.tv_chatmode2);
                                                TextView tv_chat3 = (TextView) layout6.findViewById(R.id.tv_chatmode3);
                                                TextView tv_chat4 = (TextView) layout6.findViewById(R.id.tv_chatmode4);

                                                tv_chat1.setOnClickListener(new OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        dialog6.dismiss();

                                                        String type;
                                                        if (json.getString("fromUId") != null && !json.getString("fromUId").equalsIgnoreCase("null")) {
                                                            type = "02";
                                                        } else {
                                                            type = "01";
                                                        }

                                                        Intent intent = new Intent(context, DynaDetaActivity.class);
                                                        intent.putExtra("sID", sID);
                                                        intent.putExtra("dynamicSeq", dynamicSeq);
                                                        intent.putExtra("createTime", rel_time);
                                                        intent.putExtra("dType", dType);
                                                        intent.putExtra("type", type);
                                                        intent.putExtra("type2", "00");
                                                        context.startActivityForResult(intent, 0);

                                                    }
                                                });

                                                tv_chat2.setOnClickListener(new OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        dialog6.dismiss();

                                                        Intent intent = new Intent(context, ChatActivity.class);
                                                        intent.putExtra(EaseConstant.EXTRA_USER_ID, sID);
                                                        intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                                                        intent.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                                                        intent.putExtra(EaseConstant.EXTRA_USER_IMG, finalFirstImage);
                                                        intent.putExtra(EaseConstant.EXTRA_USER_NAME, userID);
                                                        intent.putExtra(EaseConstant.EXTRA_USER_SHARERED, finalExShareRed);
                                                        context.startActivity(intent);
                                                        //dynamic/insertDynamicDealContact  dynamicSeq=&createTime=&uId=&contactId=&type=
                                                        insertDynamicContact(dynamicSeq, createTime, DemoHelper.getInstance().getCurrentUsernName(), sID, "00");

                                                    }
                                                });

                                                tv_chat3.setOnClickListener(new OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        dialog6.dismiss();

                                                        PermissionUtil permissionUtil = new PermissionUtil((FragmentActivity) context);
                                                        permissionUtil.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                                                new PermissionListener() {
                                                                    @Override
                                                                    public void onGranted() {
                                                                        UserPermissionUtil.getUserPermission(context, DemoHelper.getInstance().getCurrentUsernName(), "2", new UserPermissionUtil.UserPermissionListener() {
                                                                            @Override
                                                                            public void onAllow() {
                                                                                //发送通话记录
                                                                                sendContactTrack("电话", dynamicSeq, createTime, sID);
                                                                                //所有权限都已经授权
                                                                                if (!"1833710135".equals(sID) && !"1000000".equals(sID) && !"2000000".equals(sID) && !"3000000".equals(sID)) {
                                                                                    Intent intent = new Intent();
                                                                                    intent.setAction(Intent.ACTION_CALL);
                                                                                    //url:统一资源定位符
                                                                                    //uri:统一资源标示符（更广）
                                                                                    intent.setData(Uri.parse("tel:" + sID));
                                                                                    //开启系统拨号器
                                                                                    context.startActivity(intent);
                                                                                } else {
                                                                                    Intent intent = new Intent();
                                                                                    intent.setAction(Intent.ACTION_CALL);
                                                                                    //url:统一资源定位符
                                                                                    //uri:统一资源标示符（更广）
                                                                                    intent.setData(Uri.parse("tel:" + "13513895563"));
                                                                                    //开启系统拨号器
                                                                                    context.startActivity(intent);
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onBan() {
                                                                                ToastUtils.showNOrmalToast(context, "您的账户已被禁止打电话");

                                                                            }
                                                                        });

                                                                    }

                                                                    @Override
                                                                    public void onDenied(List<String> deniedPermission) {
                                                                        //Toast第一个被拒绝的权限
                                                                        Toast.makeText(context, "您拒绝了拨打电话的权限！", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    @Override
                                                                    public void onShouldShowRationale(List<String> deniedPermission) {
                                                                        //Toast第一个勾选不在提示的权限
                                                                        Toast.makeText(context, "您拒绝了拨打电话的权限,请前往设置手动打开！", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                    }
                                                });

                                                tv_chat4.setOnClickListener(new OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        dialog6.dismiss();


                                                        //跳转新的报价界面 判断是否阅读过规则

                                                        final SharedPreferences mSharedPreferences5 = context.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
                                                        String userPromte = mSharedPreferences5.getString("userPromte1", "0");

                                                        if (userPromte.equals("1")) {

                                                            String url = FXConstant.URL_SELECTAllORDERBYID;

                                                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String s) {

                                                                    JSONObject object = JSON.parseObject(s);

                                                                    JSONArray array = object.getJSONArray("list");

                                                                    if (array.size() > 0) {

                                                                        Toast.makeText(context, "入账单有未完成订单,禁止报价", Toast.LENGTH_SHORT).show();

                                                                    } else {

                                                                        //阅读过
                                                                        Intent intent = new Intent(context, NewsOrderDetailActivity.class);

                                                                        intent.putExtra("createTime", rel_time);
                                                                        intent.putExtra("dynamicSeq", dynamicSeq);
                                                                        intent.putExtra("task_label", task_label);
                                                                        intent.putExtra("task_locaName", json.getString("task_locaName"));
                                                                        intent.putExtra("floorPrice", json.getString("floorPrice"));
                                                                        intent.putExtra("content", json.getString("content"));
                                                                        intent.putExtra("contentImage", json.getString("image1"));
                                                                        intent.putExtra("task_position", json.getString("task_position"));
                                                                        intent.putExtra("uName", json.getString("uName"));
                                                                        intent.putExtra("thridInfo", json.getString("thridInfo"));
                                                                        intent.putExtra("typeDetail", "01");
                                                                        intent.putExtra("hxid", sID);
                                                                        intent.putExtra("biaoshi", "03");
                                                                        context.startActivityForResult(intent, 0);

                                                                    }

                                                                }
                                                            }, new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError volleyError) {
                                                                    Toast.makeText(context, "网络不稳定，请稍后重试", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }) {
                                                                @Override
                                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                                    Map<String, String> param = new HashMap<>();

                                                                    param.put("userId", DemoHelper.getInstance().getCurrentUsernName());

                                                                    return param;
                                                                }
                                                            };

                                                            MySingleton.getInstance(context).addToRequestQueue(request);


                                                        } else {
                                                            //没有阅读过

                                                            LayoutInflater inflaterD7 = LayoutInflater.from(context);
                                                            LinearLayout layout7 = (LinearLayout) inflaterD7.inflate(R.layout.dialog_orderprocess, null);
                                                            final Dialog dialog7 = new AlertDialog.Builder(context, R.style.Dialog).create();
                                                            dialog7.show();
                                                            dialog7.getWindow().setContentView(layout7);
                                                            WindowManager.LayoutParams params3 = dialog7.getWindow().getAttributes();
                                                            Display display3 = context.getWindowManager().getDefaultDisplay();
                                                            params3.width = (int) (display3.getWidth() * 0.85); //使用这种方式更改了dialog的框宽
                                                            dialog7.getWindow().setAttributes(params3);
                                                            dialog7.setCancelable(true);
                                                            dialog7.setCanceledOnTouchOutside(true);

                                                            TextView tv_midBtn = (TextView) layout7.findViewById(R.id.tv_midBtn);
                                                            TextView tv_soft = (TextView) layout7.findViewById(R.id.tv_softAgreement);
                                                            TextView tv_user = (TextView) layout7.findViewById(R.id.tv_userAgreement);


                                                            tv_soft.setOnClickListener(new OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {

                                                                    Intent intent = new Intent(context, SoftAgreementActivity.class);

                                                                    context.startActivity(intent);

                                                                }
                                                            });
                                                            tv_user.setOnClickListener(new OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    Intent intent = new Intent(context, SoftUserAgreementActivity.class);

                                                                    context.startActivity(intent);
                                                                }
                                                            });


                                                            tv_midBtn.setOnClickListener(new OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {

                                                                    dialog7.dismiss();

                                                                    SharedPreferences.Editor editor = mSharedPreferences5.edit();
                                                                    if (editor != null) {
                                                                        editor.putString("userPromte1", "1");
                                                                        editor.commit();
                                                                    }

                                                                    String url = FXConstant.URL_SELECTAllORDERBYID;

                                                                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String s) {
                                                                            JSONObject object = JSON.parseObject(s);

                                                                            JSONArray array = object.getJSONArray("list");

                                                                            if (array.size() > 0) {

                                                                                Toast.makeText(context, "入账单有未完成订单,禁止报价", Toast.LENGTH_SHORT).show();

                                                                            } else {

                                                                                Intent intent = new Intent(context, NewsOrderDetailActivity.class);

                                                                                intent.putExtra("createTime", rel_time);
                                                                                intent.putExtra("dynamicSeq", dynamicSeq);
                                                                                intent.putExtra("task_label", task_label);
                                                                                intent.putExtra("task_locaName", json.getString("task_locaName"));
                                                                                intent.putExtra("floorPrice", json.getString("floorPrice"));
                                                                                intent.putExtra("content", json.getString("content"));
                                                                                intent.putExtra("contentImage", json.getString("image1"));
                                                                                intent.putExtra("task_position", json.getString("task_position"));
                                                                                intent.putExtra("uName", json.getString("uName"));
                                                                                intent.putExtra("thridInfo", json.getString("thridInfo"));
                                                                                intent.putExtra("typeDetail", "01");
                                                                                intent.putExtra("hxid", sID);
                                                                                intent.putExtra("biaoshi", "03");
                                                                                context.startActivityForResult(intent, 0);

                                                                            }
                                                                        }
                                                                    }, new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError volleyError) {
                                                                            Toast.makeText(context, "网络不稳定，请稍后重试", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }) {
                                                                        @Override
                                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                                            Map<String, String> param = new HashMap<>();

                                                                            param.put("userId", DemoHelper.getInstance().getCurrentUsernName());

                                                                            return param;
                                                                        }
                                                                    };

                                                                    MySingleton.getInstance(context).addToRequestQueue(request);

                                                                }
                                                            });

                                                        }







                                                        /*

                                                        LayoutInflater inflaterD5 = LayoutInflater.from(context);
                                                        LinearLayout layout5 = (LinearLayout) inflaterD5.inflate(R.layout.dialog_dynamicoffer, null);
                                                        final Dialog dialog5 = new AlertDialog.Builder(context, R.style.Dialog).create();
                                                        dialog5.show();
                                                        dialog5.getWindow().setContentView(layout5);
                                                        WindowManager.LayoutParams params = dialog5.getWindow().getAttributes() ;
                                                        Display display = context.getWindowManager().getDefaultDisplay();
                                                        params.width =(int) (display.getWidth()*0.9); //使用这种方式更改了dialog的框宽
                                                        dialog5.getWindow().setAttributes(params);
                                                        dialog5.setCancelable(true);
                                                        dialog5.setCanceledOnTouchOutside(true);
                                                        TextView tv_agreen = (TextView)layout5.findViewById(R.id.tv_agreen);

                                                        tv_agreen.setOnClickListener(new OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                dialog5.dismiss();

                                                                final int[] clickPos = {0};
                                                                LayoutInflater inflaterDl = LayoutInflater.from(context);
                                                                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_horscrollview, null);
                                                                final Dialog dialog = new AlertDialog.Builder(context,R.style.Dialog).create();
                                                                dialog.show();
                                                                dialog.getWindow().setContentView(layout);
                                                                dialog.setCancelable(true);
                                                                dialog.setCanceledOnTouchOutside(true);
                                                                final CenterShowHorizontalScrollView ct_scrollView = (CenterShowHorizontalScrollView) dialog.findViewById(R.id.ct_scrollView);
                                                                Button btn_contact = (Button) dialog.findViewById(R.id.btn_contact);
                                                                Button btn_commit = (Button) dialog.findViewById(R.id.btn_commit);
                                                                final View titleItem1 = View.inflate(context, R.layout.item_select_img, null);
                                                                ImageView iv1 = (ImageView) titleItem1.findViewById(R.id.iv_moshi);
                                                                Bitmap bm1 = BitmapUtils.readBitMap(context,R.drawable.xiadanmoshiyi);
                                                                iv1.setImageBitmap(bm1);
                                                                ct_scrollView.addItemView(titleItem1, 0);
                                                                titleItem1.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        clickPos[0] = 0;
                                                                        ct_scrollView.onClicked(v,0);
                                                                    }
                                                                });
                                                                final View titleItem2 = View.inflate(context, R.layout.item_select_img, null);
                                                                ImageView iv2 = (ImageView) titleItem2.findViewById(R.id.iv_moshi);
                                                                Bitmap bm2 = BitmapUtils.readBitMap(context,R.drawable.xiadanmoshier);
                                                                iv2.setImageBitmap(bm2);
                                                                ct_scrollView.addItemView(titleItem2, 1);
                                                                titleItem2.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        clickPos[0] = 1;
                                                                        ct_scrollView.onClicked(v,1);
                                                                    }
                                                                });
                                                                final View titleItem3 = View.inflate(context, R.layout.item_select_img, null);
                                                                ImageView iv3 = (ImageView) titleItem3.findViewById(R.id.iv_moshi);
                                                                Bitmap bm3 = BitmapUtils.readBitMap(context,R.drawable.xiadanmoshisi);
                                                                iv3.setImageBitmap(bm3);
                                                                ct_scrollView.addItemView(titleItem3, 2);
                                                                titleItem3.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        clickPos[0] = 2;
                                                                        ct_scrollView.onClicked(v,2);
                                                                    }
                                                                });
                                                                final View titleItem4 = View.inflate(context, R.layout.item_select_img, null);
                                                                ImageView iv4 = (ImageView) titleItem4.findViewById(R.id.iv_moshi);
                                                                Bitmap bm4 = BitmapUtils.readBitMap(context,R.drawable.xiadanmoshiwu);
                                                                iv4.setImageBitmap(bm4);
                                                                ct_scrollView.addItemView(titleItem4, 3);
                                                                titleItem4.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        clickPos[0] = 3;
                                                                        ct_scrollView.onClicked(v,3);
                                                                    }
                                                                });
                                                                btn_contact.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        dialog.dismiss();
                                                                        Intent intent = new Intent(context, ChatActivity.class);
                                                                        intent.putExtra(EaseConstant.EXTRA_USER_ID,sID);
                                                                        intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                                                                        intent.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                                                                        intent.putExtra(EaseConstant.EXTRA_USER_IMG, finalFirstImage);
                                                                        intent.putExtra(EaseConstant.EXTRA_USER_NAME,userID);
                                                                        intent.putExtra(EaseConstant.EXTRA_USER_SHARERED, finalExShareRed);
                                                                        context.startActivity(intent);
                                                                        //dynamic/insertDynamicDealContact  dynamicSeq=&createTime=&uId=&contactId=&type=
                                                                        insertDynamicContact(dynamicSeq, createTime, DemoHelper.getInstance().getCurrentUsernName(), sID, "00");
                                                                    }
                                                                });
                                                                btn_commit.setOnClickListener(new OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        dialog.dismiss();
                                                                        if (clickPos[0]==0){
                                                                            Intent intent = new Intent(context, UOrderDetailActivity.class);
                                                                            intent.putExtra("createTime", rel_time);
                                                                            intent.putExtra("dynamicSeq", dynamicSeq);
                                                                            intent.putExtra("task_label", task_label);
                                                                            intent.putExtra("typeDetail", "01");
                                                                            intent.putExtra("hxid", sID);
                                                                            intent.putExtra("biaoshi", "03");
                                                                            context.startActivityForResult(intent, 0);
                                                                        }else if (clickPos[0]==1){
                                                                            Intent intent = new Intent(context, UOrderDetailTwoActivity.class);
                                                                            intent.putExtra("createTime", rel_time);
                                                                            intent.putExtra("dynamicSeq", dynamicSeq);
                                                                            intent.putExtra("task_label", task_label);
                                                                            intent.putExtra("typeDetail", "01");
                                                                            intent.putExtra("hxid", sID);
                                                                            intent.putExtra("biaoshi", "03");
                                                                            context.startActivityForResult(intent, 0);
                                                                        }else if (clickPos[0]==2){
                                                                            Intent intent = new Intent(context, UOrderDetailFourActivity.class);
                                                                            intent.putExtra("createTime", rel_time);
                                                                            intent.putExtra("dynamicSeq", dynamicSeq);
                                                                            intent.putExtra("task_label", task_label);
                                                                            intent.putExtra("typeDetail", "01");
                                                                            intent.putExtra("hxid", sID);
                                                                            intent.putExtra("biaoshi", "03");
                                                                            context.startActivityForResult(intent, 0);
                                                                        }else if (clickPos[0]==3){
                                                                            Intent intent = new Intent(context, UOrderDetailFiveActivity.class);
                                                                            intent.putExtra("createTime", rel_time);
                                                                            intent.putExtra("dynamicSeq", dynamicSeq);
                                                                            intent.putExtra("task_label", task_label);
                                                                            intent.putExtra("typeDetail", "01");
                                                                            intent.putExtra("hxid", sID);
                                                                            intent.putExtra("biaoshi", "03");
                                                                            context.startActivityForResult(intent, 0);
                                                                        }
                                                                    }
                                                                });

                                                            }
                                                        });
                                                        */

                                                    }
                                                });

                                            }

                                        }

                                    }

                                }

                                @Override
                                public void onBan() {

                                    ToastUtils.showNOrmalToast(context, "您的账户已被禁止接单报价,有问题联系客服");

                                }
                            });

                        }
                    });

                } else if (sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && DemoHelper.getInstance().isLoggedIn(context)) {

                    if ("03".equals(orderState)) {

                        ((ViewHolderFive) holder).btn_xiadan.setText("派单结束");
                        ((ViewHolderFive) holder).btn_xiadan.setEnabled(false);
                        ((ViewHolderFive) holder).btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green_1);

                    } else {

                        ((ViewHolderFive) holder).btn_xiadan.setText("查看");
                        ((ViewHolderFive) holder).btn_xiadan.setEnabled(true);
                        ((ViewHolderFive) holder).btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green);
                        ((ViewHolderFive) holder).btn_xiadan.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                //判断是招标信息还是普通动态
                                String redImage = json.getString("redImage");

                                if (redImage != null && redImage.length() > 7) {

                                    Intent intent = new Intent(context, DynamicLinkDetailActivity.class);

                                    intent.putExtra("redImage", redImage);

                                    context.startActivityForResult(intent, 0);

                                } else {

                                    String type;
                                    if (json.getString("fromUId") != null && !json.getString("fromUId").equalsIgnoreCase("null")) {
                                        type = "02";
                                    } else {
                                        type = "01";
                                    }

                                    Intent intent = new Intent(context, DynaDetaActivity.class);
                                    intent.putExtra("sID", sID);
                                    intent.putExtra("dynamicSeq", dynamicSeq);
                                    intent.putExtra("createTime", rel_time);
                                    intent.putExtra("dType", dType);
                                    intent.putExtra("type", type);
                                    intent.putExtra("type2", "00");
                                    context.startActivityForResult(intent, 0);

                                }


                            }
                        });

                    }

                }

            }
            // 设置文章中的图片
            ((ViewHolderFive) holder).image_1.setVisibility(View.GONE);
            ((ViewHolderFive) holder).image_2.setVisibility(View.GONE);
            ((ViewHolderFive) holder).image_3.setVisibility(View.GONE);
            ((ViewHolderFive) holder).image_5.setVisibility(View.GONE);
            ((ViewHolderFive) holder).image_4.setVisibility(View.GONE);
            ((ViewHolderFive) holder).image_6.setVisibility(View.GONE);
            ((ViewHolderFive) holder).image_7.setVisibility(View.GONE);
            ((ViewHolderFive) holder).image_8.setVisibility(View.GONE);
            View v2 = null;
            if (video != null && videoPictures != null) {
                ((ViewHolderFive) holder).rl_video.setVisibility(View.VISIBLE);
                boolean setUp = ((ViewHolderFive) holder).videoPlayer.setUp(FXConstant.URL_VIDEO + video, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
                v2 = ((ViewHolderFive) holder).rl_video;
                if (setUp) {
                    Glide.with(context).load(FXConstant.URL_VIDEO + videoPictures)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .error(R.drawable.default_error)
                            .crossFade().into(((ViewHolderFive) holder).videoPlayer.thumbImageView);
                } else {
                    Toast.makeText(context, "视频播放失败", Toast.LENGTH_SHORT).show();
                }
                ((ViewHolderFive) holder).tv_video.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.e("socialmain,", "touch");
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            updateLiulancishu(rel_time, dynamicSeq);
                            updateDeLiulancishu(rel_time, sID);
                        }
                        return false;
                    }
                });
            } else {
                ((ViewHolderFive) holder).rl_video.setVisibility(View.GONE);
                if (!imageStr.equals("")) {
                    String[] images = imageStr.split("\\|");
                    int imNumb = images.length;
                    ((ViewHolderFive) holder).image_1.setVisibility(View.VISIBLE);
                    v2 = ((ViewHolderFive) holder).image_1;
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[0], ((ViewHolderFive) holder).image_1, DemoApplication.mOptions2);
                    ((ViewHolderFive) holder).image_1.setOnClickListener(new ImageListener(images, 0, rel_time, dynamicSeq, sID));
                    if (imNumb > 1) {
                        ((ViewHolderFive) holder).image_2.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[1], ((ViewHolderFive) holder).image_2, DemoApplication.mOptions2);
                        ((ViewHolderFive) holder).image_2.setOnClickListener(new ImageListener(images, 1, rel_time, dynamicSeq, sID));
                        if (imNumb > 2) {
                            ((ViewHolderFive) holder).image_3.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[2], ((ViewHolderFive) holder).image_3, DemoApplication.mOptions2);
                            ((ViewHolderFive) holder).image_3.setOnClickListener(new ImageListener(images, 2, rel_time, dynamicSeq, sID));
                            if (imNumb > 3) {
                                ((ViewHolderFive) holder).image_4.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[3], ((ViewHolderFive) holder).image_4, DemoApplication.mOptions2);
                                ((ViewHolderFive) holder).image_4.setOnClickListener(new ImageListener(images, 3, rel_time, dynamicSeq, sID));
                                if (imNumb > 4) {
                                    ((ViewHolderFive) holder).image_5.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[4], ((ViewHolderFive) holder).image_5, DemoApplication.mOptions2);
                                    ((ViewHolderFive) holder).image_5.setOnClickListener(new ImageListener(images, 4, rel_time, dynamicSeq, sID));
                                    if (imNumb > 5) {
                                        ((ViewHolderFive) holder).image_6.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[5], ((ViewHolderFive) holder).image_6, DemoApplication.mOptions2);
                                        ((ViewHolderFive) holder).image_6.setOnClickListener(new ImageListener(images, 5, rel_time, dynamicSeq, sID));
                                        if (imNumb > 6) {
                                            ((ViewHolderFive) holder).image_7.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[6], ((ViewHolderFive) holder).image_7, DemoApplication.mOptions2);
                                            ((ViewHolderFive) holder).image_7.setOnClickListener(new ImageListener(images, 6, rel_time, dynamicSeq, sID));
                                            if (imNumb > 7) {
                                                ((ViewHolderFive) holder).image_8.setVisibility(View.VISIBLE);
                                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[7], ((ViewHolderFive) holder).image_8, DemoApplication.mOptions2);
                                                ((ViewHolderFive) holder).image_8.setOnClickListener(new ImageListener(images, 7, rel_time, dynamicSeq, sID));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            final View finalV = v2;
            ((ViewHolderFive) holder).rl_zhuanfa.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (!DemoHelper.getInstance().isLoggedIn(context)) {
                        Toast.makeText(context, "登陆后才可以转发哦！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    paidanZhuanfa = dynamicSeq;
                    updateLiulancishu(rel_time, dynamicSeq);
                    String oncePrice = json.getString("oncePrice");
                    showZhfDialog(json.getString("views"), rel_time, null, ((ViewHolderFive) holder).card, finalV, oncePrice, dynamicSeq, sID, fromUId, finalFirstImage, userID, content, false, floorPrice + "|" + task_position);
                }
            });
            ((ViewHolderFive) holder).tv_content.setText(content);
            // ((ViewHolderFive) holder).tv_content.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
            ((ViewHolderFive) holder).tv_content.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        if (json.getString("dynamicAuth").equals("0")) {

                            //洽谈中 弹出提示框

                            LayoutInflater inflaterD5 = LayoutInflater.from(context);
                            LinearLayout layout5 = (LinearLayout) inflaterD5.inflate(R.layout.dialog_vipchat_alert, null);
                            final Dialog dialog5 = new AlertDialog.Builder(context, R.style.Dialog).create();
                            dialog5.show();
                            dialog5.getWindow().setContentView(layout5);
                            WindowManager.LayoutParams params = dialog5.getWindow().getAttributes();
                            Display display = context.getWindowManager().getDefaultDisplay();
                            params.width = (int) (display.getWidth() * 0.7); //使用这种方式更改了dialog的框宽
                            dialog5.getWindow().setAttributes(params);
                            dialog5.setCancelable(true);
                            dialog5.setCanceledOnTouchOutside(true);


                        } else {


                            //判断是招标信息还是普通动态
                            String redImage = json.getString("redImage");

                            if (redImage != null && redImage.length() > 7) {

                                Intent intent = new Intent(context, DynamicLinkDetailActivity.class);

                                intent.putExtra("redImage", redImage);

                                context.startActivityForResult(intent, 0);

                            } else {

                                String type;
                                if (json.getString("fromUId") != null && !json.getString("fromUId").equalsIgnoreCase("null")) {
                                    type = "02";
                                } else {
                                    type = "01";
                                }
                                Intent intent = new Intent(context, DynaDetaActivity.class);
                                intent.putExtra("sID", sID);
                                intent.putExtra("dynamicSeq", dynamicSeq);
                                intent.putExtra("createTime", rel_time);
                                intent.putExtra("dType", dType);
                                intent.putExtra("type", type);
                                intent.putExtra("type2", "00");
                                intent.putExtra("isHaveMargin", isHaveMargin);
                                context.startActivityForResult(intent, 0);

                            }


                        }

                    }
                    return true;
                }
            });
            String rel_time2 = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                    + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
            ((ViewHolderFive) holder).tv_time.setText(rel_time2);
            ((ViewHolderFive) holder).iv_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {


                    context.startActivity(new Intent(context, UserDetailsActivity.class).
                            putExtra(FXConstant.JSON_KEY_HXID, sID).putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime));


                }
            });
            //final String createTime = json.getString("createTime");
            final String finalCountPinglun = countPinglun;
            ((ViewHolderFive) holder).rl_pinglun.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    showCommentEditText("00", sID, myuserID, myNick, dynamicSeq, createTime, ((ViewHolderFive) holder).tv_count_pl, Integer.valueOf(finalCountPinglun));
                }
            });
            ((ViewHolderFive) holder).rl_comment.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCommentEditText("00", sID, myuserID, myNick, dynamicSeq, createTime, ((ViewHolderFive) holder).tv_count_pl, Integer.valueOf(finalCountPinglun));
                }
            });

            //派单界面转发
            final SharedPreferences mSharedPreferences2 = context.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);

//            if (!currentType.equals("02") && !sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(json.getString("dynamicSeq"), "123").equalsIgnoreCase("123") && !ComparePriceAndVipLevel(json.getString("floorPrice"),task_position) && (Double.parseDouble(isHaveMargin) < 100 ||  Double.parseDouble(isHaveMargin) < Double.parseDouble(json.getString("floorPrice")))) {
//                Log.d("chen", content + "没有转发过");
//                ((ViewHolderFive) holder).rl_pre_click.setVisibility(View.VISIBLE);
//                /*((ViewHolderFive) holder).rl_pre_click.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        Log.d("chen", "开始点击覆盖层");
//                        return true;
//                    }
//                });*/
//                ((ViewHolderFive) holder).rl_pre_click.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (!DemoHelper.getInstance().isLoggedIn(context)){
//                            Toast.makeText(context,"请您先登录！",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        paidanZhuanfa = dynamicSeq;
//                        updateLiulancishu(rel_time, dynamicSeq);
//                        String oncePrice = json.getString("oncePrice");
//
//
//                        Date date = new Date(System.currentTimeMillis());
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//                        String nowtime = dateFormat.format(date);
//
//                        String limitTime = mSharedPreferences2.getString("limitTime","0");
//                        String limitType = mSharedPreferences2.getString("limitType","0");
//
//                        if (limitTime.equals(nowtime)) {
//
//
//                            //相等 判断是第一次还是第二次
//
//                            if (limitType.equals("1")){
//
//                                SharedPreferences.Editor editor = mSharedPreferences2.edit();
//                                if (editor!=null) {
//                                    editor.putString("limitType","2");
//                                    editor.putString("limitTime",nowtime);
//                                    editor.commit();
//                                }
//
//                                LayoutInflater inflater1 = LayoutInflater.from(context);
//                                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
//                                final Dialog dialog1 = new AlertDialog.Builder(context, R.style.Dialog).create();
//                                dialog1.show();
//                                dialog1.getWindow().setContentView(layout1);
//                                dialog1.setCanceledOnTouchOutside(true);
//                                dialog1.setCancelable(true);
//                                TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
//                                Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
//                                final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
//                                TextView title = (TextView) layout1.findViewById(R.id.tv_title);
//                                title.setText("温馨提示");
//                                btnOK1.setText("确定");
//                                btnCancel1.setText("取消");
//                                title_tv1.setText("高级会员正在洽谈\n清稍后...");
//                                btnCancel1.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog1.dismiss();
//                                    }
//                                });
//                                btnOK1.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog1.dismiss();
//                                    }
//                                });
//
//                            }else {
//
//                                showZhfDialog(json.getString("views"),rel_time,null,((ViewHolderFive) holder).card, finalV,oncePrice,dynamicSeq,sID,fromUId,finalFirstImage,userID,content,false,floorPrice+"|"+task_position);
//
//                            }
//
//                         }else {
//
//
//                            // 不相等  直接从第一次开始
//
//                            SharedPreferences.Editor editor = mSharedPreferences2.edit();
//                            if (editor!=null) {
//                                editor.putString("limitType","1");
//                                editor.putString("limitTime",nowtime);
//                                editor.commit();
//                            }
//
//                            LayoutInflater inflater1 = LayoutInflater.from(context);
//                            RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
//                            final Dialog dialog1 = new AlertDialog.Builder(context, R.style.Dialog).create();
//                            dialog1.show();
//                            dialog1.getWindow().setContentView(layout1);
//                            dialog1.setCanceledOnTouchOutside(true);
//                            dialog1.setCancelable(true);
//                            TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
//                            Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
//                            final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
//                            TextView title = (TextView) layout1.findViewById(R.id.tv_title);
//                            title.setText("温馨提示");
//                            btnOK1.setText("确定");
//                            btnCancel1.setText("取消");
//                            title_tv1.setText("高质保用户正在洽谈\n清稍后...");
//                            btnCancel1.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialog1.dismiss();
//                                }
//                            });
//                            btnOK1.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialog1.dismiss();
//                                }
//                            });
//
//                        }
//
//                    }
//                });
//
//            } else {


            if (currentType.equals("02")) {

                //判断是否转发过 然后看是否有权限转发

                if (zbShareAuth.equals("1") || zbShareAuth.equals("3")) {

//                        //判断是否转发过 然后做点击事件
//                        if (!sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(json.getString("dynamicSeq"), "123").equalsIgnoreCase("123") && !(isVip>0&&vipLevel.equals("3"))){
//                            //没有转发过
//
//                            ((ViewHolderFive) holder).rl_pre_click.setOnClickListener(new OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//
//                                    if (!DemoHelper.getInstance().isLoggedIn(context)){
//                                        Toast.makeText(context,"请您先登录！",Toast.LENGTH_SHORT).show();
//                                        return;
//                                    }
//
//                                    paidanZhuanfa = dynamicSeq;
//                                    updateLiulancishu(rel_time, dynamicSeq);
//                                    String oncePrice = json.getString("oncePrice");
//
//                                    showZhfDialog(json.getString("views"),rel_time,null,((ViewHolderFive) holder).card, finalV,oncePrice,dynamicSeq,sID,fromUId,finalFirstImage,userID,content,false,floorPrice+"|"+task_position);
//
//                                }
//                            });
//
//
//                        }else {

                    //转发过
                    ((ViewHolderFive) holder).rl_pre_click.setVisibility(View.GONE);
                    //  }


                } else {

                    ((ViewHolderFive) holder).rl_pre_click.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!DemoHelper.getInstance().isLoggedIn(context)) {
                                Toast.makeText(context, "请您先登录！", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            //直接跳转任务界面
                            Intent intent = new Intent(context, RedPromoteActivity.class);

                            context.startActivity(intent);

                        }
                    });

                }

            } else {


                Log.d("chen", content + "转发过");

                ((ViewHolderFive) holder).rl_pre_click.setVisibility(View.GONE);

            }


            //}


            if (isShowAdvert.equals("yes") && position % 5 == 0 && position > 0) {

                ((ViewHolderFive) holder).image_advert.setVisibility(View.VISIBLE);

                if (currentTag == advertImages.length || currentTag > advertImages.length) {

                    currentTag = 0;

                }

                ImageLoader.getInstance().displayImage(FXConstant.URL_ADVERTURL + advertImages[currentTag], ((ViewHolderFive) holder).image_advert);

                currentTag++;

                ((ViewHolderFive) holder).image_advert.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String type1 = advertClickTypes[currentTag - 1];
                        String type2 = advertClickContents[currentTag - 1];

                        if (type1.equals("1")) {

                            // 打电话
                            if (type2.length() != 11) {

                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                //url:统一资源定位符
                                //uri:统一资源标示符（更广）
                                intent.setData(Uri.parse("tel:" + "13513895563"));
                                //开启系统拨号器
                                context.startActivity(intent);

                            } else {

                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                //url:统一资源定位符
                                //uri:统一资源标示符（更广）
                                intent.setData(Uri.parse("tel:" + type2));
                                //开启系统拨号器
                                context.startActivity(intent);

                            }

                        } else if (type1.equals("2")) {

                            if (!DemoHelper.getInstance().isLoggedIn(context)) {

                                Toast.makeText(context, "登陆之后才可以发布招标！", Toast.LENGTH_SHORT).show();

                            } else {

                                //发布需求
                                Intent intent2 = new Intent(context, MomentsPublishActivity.class);
                                intent2.putExtra("biaoshi", "xuqiu");
                                context.startActivity(intent2);

                            }

                        } else if (type1.equals("3")) {

                            //下载app  跳转app对应详情

                            Intent intent2 = new Intent(context, AppDownDetailActivity.class);
                            intent2.putExtra("appType", "0");
                            intent2.putExtra("appIdentify", type2);
                            context.startActivity(intent2);

                        } else if (type1.equals("4")) {

                            //打开网页

                            Uri uri = Uri.parse(type2);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);

                        } else if (type1.equals("5")) {

                            //跳转应用内网页  可以电话咨询或者聊天咨询

                            Intent intent2 = new Intent(context, SouSuoAdvertClickActivity.class);
                            intent2.putExtra("url", type2);
                            context.startActivity(intent2);

                        }

                    }
                });


            } else {

                ((ViewHolderFive) holder).image_advert.setVisibility(View.GONE);
            }


            ((ViewHolderFive) holder).authBtn1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //禁止登录

                    String url = FXConstant.URL_INSERTFREEZELOGIN;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            Toast.makeText(context, "已禁止该用户登录", Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();

                            param.put("u_id", sID);

                            param.put("freezeType", "01");

                            return param;
                        }
                    };

                    MySingleton.getInstance(context).addToRequestQueue(request);

                }
            });


            ((ViewHolderFive) holder).authBtn2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    //禁止派单

                    UpdateUserAuth(sID, "5");

//                    String url = FXConstant.URL_INSERTPROHIBIT;
//                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//
//                            Toast.makeText(context, "已禁止该用户发布派单", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//
//                            Map<String, String> param = new HashMap<>();
//
//                            param.put("uId", sID);
//
//                            param.put("prohibittype", "5");
//
//                            return param;
//                        }
//                    };
//
//                    MySingleton.getInstance(context).addToRequestQueue(request);

                }
            });
            ((ViewHolderFive) holder).authBtn3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    //禁止所有

                    UpdateUserAuth(sID, "4");
                    UpdateUserAuth(sID, "5");

                }
            });
            ((ViewHolderFive) holder).authBtn4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    //隐藏
                    String url = FXConstant.URL_UPDATEDYNAMICRECOMD;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            if (authType.equals("03")) {

                                json.put("authType", "01");
                                notifyDataSetChanged();

                                Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();

                            } else {

                                json.put("authType", "03");
                                notifyDataSetChanged();

                                Toast.makeText(context, "已隐藏该动态", Toast.LENGTH_SHORT).show();

                            }

                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> param = new HashMap<>();

                            param.put("userId", sID);
                            param.put("dynamicSeq", dynamicSeq);
                            param.put("createTime", createTime);

                            if (authType.equals("03")) {

                                param.put("authtype", "01");

                            } else {
                                param.put("authtype", "03");
                            }

                            return param;
                        }
                    };

                    MySingleton.getInstance(context).addToRequestQueue(request);

                }
            });
            ((ViewHolderFive) holder).authBtn5.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    //匹配

                    Intent intent = new Intent(context, PushMessageActivity.class);

                    intent.putExtra("task_label", task_label);
                    intent.putExtra("task_position", task_position);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("createTime", createTime);
                    intent.putExtra("sID", sID);
                    intent.putExtra("deviceType", deviceType);
                    intent.putExtra("content", json.getString("content"));
                    intent.putExtra("task_locaName", json.getString("task_locaName"));
                    intent.putExtra("uName", json.getString("uName"));

                    context.startActivity(intent);


                }
            });
            ((ViewHolderFive) holder).authBtn6.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    //修改


                    LayoutInflater inflaterDl = LayoutInflater.from(context);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                    final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                    TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                    RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                    RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                    RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                    tv_item1.setText("改为交易完成");
                    tv_item2.setText("改为派单结束");

                    tv_item1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();

                            updatePaidan(dynamicSeq, createTime, "02", json);

                        }
                    });

                    tv_item2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();

                            updatePaidan(dynamicSeq, createTime, "03", json);

                        }
                    });

                    re_item3.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();

                        }
                    });

                }
            });

        } else if (holder instanceof ViewHolderFivezhf) {
            final JSONObject json = users.get(position);
            // 如果数据出错....
            if (json == null || json.size() == 0) {
                users.remove(position);
                this.notifyDataSetChanged();
            }
            String views = json.getString("views");
            if (views == null || "".equals(views)) {
                views = "0";
            }
            if (views != null && Integer.valueOf(views) > 9999) {

                Double a = Double.valueOf(views) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                views = str + "万";

            }
            ((ViewHolderFivezhf) holder).tv_count_llc.setText(views);
            final String userID = json.getString("uName");
            final String content = json.getString("content");
            String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
            String newContent = TextUtils.isEmpty(json.getString("newcontent")) ? "" : json.getString("newcontent");
            final String firstName = json.getJSONObject("userInfo").getString("uName");
            String firstImage = json.getJSONObject("userInfo").getString("uImage");
            final String firstID = json.getJSONObject("userInfo").getString("uLoginId");
            String location = json.getString("location");
            String countPinglun = json.getString("resv7");
            String task_position = json.getString("task_position");
            String task_locaName = json.getString("task_locaName");
            String task_jurisdiction = json.getString("task_jurisdiction");
            responseTime = json.getString("responseTime");
            firstDistance = json.getString("firstDistance");
            resv1 = json.getString("resv1");
            resv2 = json.getString("resv2");
            ((ViewHolderFivezhf) holder).tv_location.setText(task_locaName);
            if (countPinglun == null || "".equals(countPinglun)) {
                countPinglun = "0";
            }
            if (countPinglun != null && Integer.valueOf(countPinglun) > 9999) {
                Double a = Double.valueOf(countPinglun) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                countPinglun = str + "万";

            }
            ((ViewHolderFivezhf) holder).tv_count_pl.setText(countPinglun);
            String forwardTimes = json.getString("forwardTimes");
            if (forwardTimes == null || "".equals(forwardTimes)) {
                forwardTimes = "0";
            }
            if (forwardTimes != null && Integer.valueOf(forwardTimes) > 9999) {
                Double a = Double.valueOf(forwardTimes) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                forwardTimes = str + "万";

            }
            ((ViewHolderFivezhf) holder).tv_count_zhf.setText(forwardTimes);
            ((ViewHolderFivezhf) holder).tv_first_nick.setText(firstName + ":");
            if (firstImage != null) {
                if (firstImage.length() > 1) {
                    String[] orderProjectArray = firstImage.split("\\|");
                    firstImage = orderProjectArray[0];
                }
            }
            String firstSex = json.getJSONObject("userInfo").getString("uSex");
            if ("00".equals(firstSex)) {
                ((ViewHolderFivezhf) holder).tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
            } else {
                ((ViewHolderFivezhf) holder).tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
            }
            if (firstImage != null && !(firstImage == "")) {
                ((ViewHolderFivezhf) holder).iv_first_avatar.setVisibility(View.VISIBLE);
                ((ViewHolderFivezhf) holder).tvfirst_TitleA.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + firstImage, ((ViewHolderFivezhf) holder).iv_first_avatar, DemoApplication.mOptions);
            } else {
                ((ViewHolderFivezhf) holder).iv_first_avatar.setVisibility(View.INVISIBLE);
                ((ViewHolderFivezhf) holder).tvfirst_TitleA.setVisibility(View.VISIBLE);
                ((ViewHolderFivezhf) holder).tvfirst_TitleA.setText(TextUtils.isEmpty(firstName) ? firstID : firstName);
            }
            ((ViewHolderFivezhf) holder).iv_first_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, firstID));
                }
            });
            ((ViewHolderFivezhf) holder).tv_first_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, firstID));
                }
            });
            final String sID = json.getString("uLoginId");
            final String resv4 = TextUtils.isEmpty(json.getString("orderType")) ? "01" : json.getString("orderType");
            // String token = json.getString("token");
            final String rel_time = json.getString("createTime");
            String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
            final String dynamicSeq = json.getString("dynamicSeq");
            final String fromUId = sID;
            final String finalFirstImage = firstImage;
            ((ViewHolderFivezhf) holder).tv_zhf_content.setText(newContent, position);
            ((ViewHolderFivezhf) holder).tv_zhf_content.setVisibility(TextUtils.isEmpty(newContent) ? View.GONE : View.VISIBLE);
            ((ViewHolderFivezhf) holder).tv_zhf_content.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        String type;
                        if (json.getString("fromUId") != null && !json.getString("fromUId").equalsIgnoreCase("null")) {
                            type = "02";
                        } else {
                            type = "01";
                        }
                        Intent intent = new Intent(context, DynaDetaActivity.class);
                        intent.putExtra("sID", sID);
                        intent.putExtra("dynamicSeq", dynamicSeq);
                        intent.putExtra("createTime", rel_time);
                        intent.putExtra("dType", dType);
                        intent.putExtra("type", type);
                        intent.putExtra("type2", "00");
                        context.startActivityForResult(intent, 0);
                    }
                    return true;
                }
            });
            if (avatar.length() > 40) {
                String[] orderProjectArray = avatar.split("\\|");
                avatar = orderProjectArray[0];
            }
            String sex = TextUtils.isEmpty(json.getString("uSex")) ? "01" : json.getString("uSex");
            String nianLing = TextUtils.isEmpty(json.getString("uAge")) ? "27" : json.getString("uAge");
            String company = TextUtils.isEmpty(json.getString("uCompany")) ? "暂未加入企业" : json.getString("uCompany");
            String uNation = json.getString("uNation");
            String resv5 = json.getString("resv5");
            String resv6 = json.getString("resv6");
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
            ((ViewHolderFivezhf) holder).tvCompany.setText("(" + company + ")");
            ((ViewHolderFivezhf) holder).tvNianLing.setText(nianLing);
            if ("00".equals(sex)) {
                ((ViewHolderFivezhf) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
                ((ViewHolderFivezhf) holder).tvNianLing.setBackgroundColor(Color.rgb(234, 121, 219));
            } else {
                ((ViewHolderFivezhf) holder).tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
                ((ViewHolderFivezhf) holder).tvNianLing.setBackgroundResource(R.color.accent_blue);
            }
            if (!(avatar == "")) {
                ((ViewHolderFivezhf) holder).iv_avatar.setVisibility(View.VISIBLE);
                ((ViewHolderFivezhf) holder).tvTitleA.setVisibility(View.INVISIBLE);
                //ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+avatar,((ViewHolderFivezhf) holder).iv_avatar, DemoApplication.mOptions);
                Glide.with(context).load(FXConstant.URL_AVATAR + avatar).into(((ViewHolderFive) holder).iv_avatar);
            } else {
                ((ViewHolderFivezhf) holder).iv_avatar.setVisibility(View.INVISIBLE);
                ((ViewHolderFivezhf) holder).tvTitleA.setVisibility(View.VISIBLE);
                ((ViewHolderFivezhf) holder).tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
            }
            ((ViewHolderFivezhf) holder).tv_nick.setText(userID);
            final String shareRed = json.getString("shareRed");
            final String friendsNumber = json.getString("friendsNumber");
            String onceJine = null;
            if (friendsNumber != null && !"".equals(friendsNumber)) {
                onceJine = friendsNumber.split("\\|")[0];
            }
            if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0 && onceJine != null && !"".equals(onceJine) && Double.parseDouble(onceJine) > 0) {
                ((ViewHolderFivezhf) holder).tv_nick.setTextColor(Color.RED);
            } else {
                ((ViewHolderFivezhf) holder).tv_nick.setTextColor(Color.rgb(87, 107, 149));
            }
            ((ViewHolderFivezhf) holder).tv_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            ((ViewHolderFivezhf) holder).tvTitleA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            String resv1 = TextUtils.isEmpty(json.getString("resv1")) ? "" : json.getString("resv1");
            String resv2 = TextUtils.isEmpty(json.getString("resv2")) ? "" : json.getString("resv2");
            double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
            double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());
            if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1, longitude1);
                double distance = DistanceUtil.getDistance(ll, ll1);
                double dou = distance / 1000;
                String str = String.format("%.1f", dou);//format 返回的是字符串
                if (str != null && dou >= 10000) {
                    ((ViewHolderFivezhf) holder).tvDistance.setText("隐藏");
                } else {
                    ((ViewHolderFivezhf) holder).tvDistance.setText(str + "km");
                }
            } else {
                ((ViewHolderFivezhf) holder).tvDistance.setText("3km之内");
            }
            ((ViewHolderFivezhf) holder).btn_xiadan.setEnabled(true);
            ((ViewHolderFivezhf) holder).btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green);
            ((ViewHolderFivezhf) holder).btn_xiadan.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflaterDl = LayoutInflater.from(context);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                    final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                    RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                    RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                    TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                    TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                    tv_item1.setText("报价单1");
                    tv_item2.setText("报价单2");
                    re_item1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            typeDetail = "01";
                            dialog.dismiss();
                        }
                    });
                    re_item2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            typeDetail = "02";
                            dialog.dismiss();
                        }
                    });
                    re_item3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            if (!DemoHelper.getInstance().isLoggedIn(context)) {
                ((ViewHolderFivezhf) holder).btn_xiadan.setEnabled(true);
                ((ViewHolderFivezhf) holder).btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green);
                ((ViewHolderFivezhf) holder).btn_xiadan.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                });
            }
            // 设置文章中的图片
            ((ViewHolderFivezhf) holder).image_1.setVisibility(View.GONE);
            ((ViewHolderFivezhf) holder).image_2.setVisibility(View.GONE);
            ((ViewHolderFivezhf) holder).image_3.setVisibility(View.GONE);
            ((ViewHolderFivezhf) holder).image_4.setVisibility(View.GONE);
            ((ViewHolderFivezhf) holder).image_5.setVisibility(View.GONE);
            ((ViewHolderFivezhf) holder).image_6.setVisibility(View.GONE);
            ((ViewHolderFivezhf) holder).image_7.setVisibility(View.GONE);
            ((ViewHolderFivezhf) holder).image_8.setVisibility(View.GONE);
            View v2 = null;
            if (!imageStr.equals("")) {
                String[] images = imageStr.split("\\|");
                int imNumb = images.length;
                ((ViewHolderFivezhf) holder).image_1.setVisibility(View.VISIBLE);
                v2 = ((ViewHolderFivezhf) holder).image_1;
                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[0], ((ViewHolderFivezhf) holder).image_1, DemoApplication.mOptions2);
                ((ViewHolderFivezhf) holder).image_1.setOnClickListener(new ImageListener(images, 0, rel_time, dynamicSeq, sID));
                if (imNumb > 1) {
                    ((ViewHolderFivezhf) holder).image_2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[1], ((ViewHolderFivezhf) holder).image_2, DemoApplication.mOptions2);
                    ((ViewHolderFivezhf) holder).image_2.setOnClickListener(new ImageListener(images, 1, rel_time, dynamicSeq, sID));
                    if (imNumb > 2) {
                        ((ViewHolderFivezhf) holder).image_3.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[2], ((ViewHolderFivezhf) holder).image_3, DemoApplication.mOptions2);
                        ((ViewHolderFivezhf) holder).image_3.setOnClickListener(new ImageListener(images, 2, rel_time, dynamicSeq, sID));
                        if (imNumb > 3) {
                            ((ViewHolderFivezhf) holder).image_4.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[3], ((ViewHolderFivezhf) holder).image_4, DemoApplication.mOptions2);
                            ((ViewHolderFivezhf) holder).image_4.setOnClickListener(new ImageListener(images, 3, rel_time, dynamicSeq, sID));
                            if (imNumb > 4) {
                                ((ViewHolderFivezhf) holder).image_5.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[4], ((ViewHolderFivezhf) holder).image_5, DemoApplication.mOptions2);
                                ((ViewHolderFivezhf) holder).image_5.setOnClickListener(new ImageListener(images, 4, rel_time, dynamicSeq, sID));
                                if (imNumb > 5) {
                                    ((ViewHolderFivezhf) holder).image_6.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[5], ((ViewHolderFivezhf) holder).image_6, DemoApplication.mOptions2);
                                    ((ViewHolderFivezhf) holder).image_6.setOnClickListener(new ImageListener(images, 5, rel_time, dynamicSeq, sID));
                                    if (imNumb > 6) {
                                        ((ViewHolderFivezhf) holder).image_7.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[6], ((ViewHolderFivezhf) holder).image_7, DemoApplication.mOptions2);
                                        ((ViewHolderFivezhf) holder).image_7.setOnClickListener(new ImageListener(images, 6, rel_time, dynamicSeq, sID));
                                        if (imNumb > 7) {
                                            ((ViewHolderFivezhf) holder).image_8.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[7], ((ViewHolderFivezhf) holder).image_8, DemoApplication.mOptions2);
                                            ((ViewHolderFivezhf) holder).image_8.setOnClickListener(new ImageListener(images, 7, rel_time, dynamicSeq, sID));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 显示位置
//            if (location != null && !location.equals("0")) {
//                holder.tv_location.setVisibility(View.VISIBLE);
//                holder.tv_location.setText(location);
//            }
            // 显示文章内容
            // .setText(content);
            final View finalV = v2;
            ((ViewHolderFivezhf) holder).rl_zhuanfa.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!DemoHelper.getInstance().isLoggedIn(context)) {
                        Toast.makeText(context, "登陆后才可以转发哦！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    updateLiulancishu(rel_time, dynamicSeq);
                    showZhfDialog(json.getString("views"), rel_time, null, ((ViewHolderFivezhf) holder).card, finalV, "0", dynamicSeq, firstID, fromUId, finalFirstImage, firstName, content, false, "0");
                }
            });
            ((ViewHolderFivezhf) holder).tv_content2.setText(content);
            String rel_time2 = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                    + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
            ((ViewHolderFivezhf) holder).tv_time.setText(rel_time2);
            ((ViewHolderFivezhf) holder).iv_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
                }
            });
            final String createTime = json.getString("createTime");
            final String finalCountPinglun = countPinglun;
            ((ViewHolderFivezhf) holder).rl_pinglun.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    showCommentEditText("00", sID, myuserID, myNick, dynamicSeq, createTime, ((ViewHolderFivezhf) holder).tv_count_pl, Integer.valueOf(finalCountPinglun));
                }
            });

            //新闻案例
        } else if (holder instanceof ViewHolderSixzhf) {
            final JSONObject json = users.get(position);
            // 如果数据出错....
            if (json == null || json.size() == 0) {
                users.remove(position);
                this.notifyDataSetChanged();
            }

            String content = json.getString("content");

            ((ViewHolderSixzhf) holder).tv_content.setText(content);

            String hot = json.getString("image3");
            String source = json.getString("location");
            String views = json.getString("views");
            String time = json.getString("createTime");
            String commentCount = json.getString("resv7");

            if (hot != null && hot.indexOf("正事多") == -1) {

                ((ViewHolderSixzhf) holder).tv_hot.setVisibility(View.VISIBLE);

            } else {

                ((ViewHolderSixzhf) holder).tv_hot.setVisibility(View.GONE);
            }

            ((ViewHolderSixzhf) holder).tv_source.setText(source);


            if (Double.parseDouble(views) > 9999) {

                Double a = Double.valueOf(views) / 10000;

                DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String commentCount2 = myformat.format(a);

                ((ViewHolderSixzhf) holder).tv_views.setText(commentCount2 + "万浏览");

            } else {

                ((ViewHolderSixzhf) holder).tv_views.setText(views + "浏览");

            }


            if (commentCount != null) {

                if (Double.parseDouble(commentCount) > 9999) {

                    Double a = Double.valueOf(commentCount) / 10000;

                    DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                    String commentCount2 = myformat.format(a);

                    ((ViewHolderSixzhf) holder).tv_commentcount.setText(commentCount2 + "万评论");

                } else {

                    ((ViewHolderSixzhf) holder).tv_commentcount.setText(commentCount + "评论");

                }

            } else {

                ((ViewHolderSixzhf) holder).tv_commentcount.setText("0评论");

            }

            //20180917123011
            ((ViewHolderSixzhf) holder).tv_times.setText(time.substring(4, 6) + "-" + time.substring(6, 8) + " " + time.substring(8, 10) + ":" + time.substring(10, 12));

            String videoStr = json.getString("videoPictures");

            if (videoStr != null) {

                ((ViewHolderSixzhf) holder).rl_image.setVisibility(View.GONE);
                ((ViewHolderSixzhf) holder).rl_video.setVisibility(View.VISIBLE);

                ImageLoader.getInstance().displayImage(FXConstant.URL_ARTICLEVIDEO + videoStr, ((ViewHolderSixzhf) holder).image_videoImage);

            } else {

                ((ViewHolderSixzhf) holder).rl_image.setVisibility(View.VISIBLE);
                ((ViewHolderSixzhf) holder).rl_video.setVisibility(View.GONE);
                String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
                String[] images = imageStr.split("\\|");

                if (images.length == 1) {

                    ImageLoader.getInstance().displayImage(FXConstant.URL_ARTICLE + images[0], ((ViewHolderSixzhf) holder).fristImageView, DemoApplication.mOptions2);
                    ((ViewHolderSixzhf) holder).thridImageView.setVisibility(View.INVISIBLE);
                    ((ViewHolderSixzhf) holder).secondImageView.setVisibility(View.INVISIBLE);

                } else if (images.length == 2) {

                    ImageLoader.getInstance().displayImage(FXConstant.URL_ARTICLE + images[0], ((ViewHolderSixzhf) holder).fristImageView, DemoApplication.mOptions2);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_ARTICLE + images[1], ((ViewHolderSixzhf) holder).secondImageView, DemoApplication.mOptions2);
                    ((ViewHolderSixzhf) holder).thridImageView.setVisibility(View.INVISIBLE);

                } else if (images.length == 3) {

                    ImageLoader.getInstance().displayImage(FXConstant.URL_ARTICLE + images[0], ((ViewHolderSixzhf) holder).fristImageView, DemoApplication.mOptions2);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_ARTICLE + images[1], ((ViewHolderSixzhf) holder).secondImageView, DemoApplication.mOptions2);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_ARTICLE + images[2], ((ViewHolderSixzhf) holder).thridImageView, DemoApplication.mOptions2);

                }
            }

            if (isShowAdvert.equals("yes") && position % 5 == 0 && position > 0) {

                ((ViewHolderSixzhf) holder).image_advert.setVisibility(View.VISIBLE);

                if (currentTag == advertImages.length || currentTag > advertImages.length) {

                    currentTag = 0;

                }

                ImageLoader.getInstance().displayImage(FXConstant.URL_ADVERTURL + advertImages[currentTag], ((ViewHolderSixzhf) holder).image_advert);

                currentTag++;

                ((ViewHolderSixzhf) holder).image_advert.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String type1 = advertClickTypes[currentTag - 1];
                        String type2 = advertClickContents[currentTag - 1];

                        if (type1.equals("1")) {

                            // 打电话
                            if (type2.length() != 11) {

                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                //url:统一资源定位符
                                //uri:统一资源标示符（更广）
                                intent.setData(Uri.parse("tel:" + "13513895563"));
                                //开启系统拨号器
                                context.startActivity(intent);

                            } else {

                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                //url:统一资源定位符
                                //uri:统一资源标示符（更广）
                                intent.setData(Uri.parse("tel:" + type2));
                                //开启系统拨号器
                                context.startActivity(intent);

                            }

                        } else if (type1.equals("2")) {

                            if (!DemoHelper.getInstance().isLoggedIn(context)) {

                                Toast.makeText(context, "登陆之后才可以发布招标！", Toast.LENGTH_SHORT).show();

                            } else {

                                //发布需求
                                Intent intent2 = new Intent(context, MomentsPublishActivity.class);
                                intent2.putExtra("biaoshi", "xuqiu");
                                context.startActivity(intent2);

                            }

                        } else if (type1.equals("3")) {

                            //下载app  跳转app对应详情

                            Intent intent2 = new Intent(context, AppDownDetailActivity.class);
                            intent2.putExtra("appType", "0");
                            intent2.putExtra("appIdentify", type2);
                            context.startActivity(intent2);

                        } else if (type1.equals("4")) {

                            //打开网页

                            Uri uri = Uri.parse(type2);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);

                        } else if (type1.equals("5")) {

                            //跳转应用内网页  可以电话咨询或者聊天咨询

                            Intent intent2 = new Intent(context, SouSuoAdvertClickActivity.class);
                            intent2.putExtra("url", type2);
                            context.startActivity(intent2);

                        }

                    }
                });


            } else {

                ((ViewHolderSixzhf) holder).image_advert.setVisibility(View.GONE);
            }

        }
    }

    private void sendContactTrack(String msg, final String dynamicSeq, final String createTime, final String hxid) {
        // uazPresenter.sendContactTrack(DemoHelper.getInstance().getCurrentUsernName(), hxid, msg);
        if (msg.equalsIgnoreCase("电话") || msg.equalsIgnoreCase("聊天")) {
            if (TextUtils.isEmpty(dynamicSeq) || TextUtils.isEmpty(createTime) || TextUtils.isEmpty(hxid)) {
                return;
            }
            String url = FXConstant.URL_UPDATE_INSERT_DYNAMIC_CONTACT;
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.d("chen", "insertDynamicContact onResponse" + s);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("chen", "insertDynamicContact onErrorResponse" + volleyError.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //uId=&dynamicSeq=&createTime=&dis=&responsetime=&ordernum=&status=
                    Map<String, String> param = new HashMap<>();
                    param.put("dynamicSeq", dynamicSeq);
                    param.put("createTime", createTime);
                    param.put("uId", DemoHelper.getInstance().getCurrentUsernName());
                    param.put("contactId", hxid);
                    param.put("type", "00");
                    return param;
                }
            };
            MySingleton.getInstance(context).addToRequestQueue(request);
        }
    }

    private void updatePaidan(final String dynamicSeq, final String createTime, final String type, final JSONObject object) {
        String url = FXConstant.URL_UPDATE_DYNAMIC_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "updatePaidan" + s);

                object.put("orderState", type);
                notifyDataSetChanged();

                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "updatePaidan volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //dynamic/updateDynamicPush  seq  time  orderState  01
                Map<String, String> params = new HashMap<String, String>();
                params.put("dynamicSeq", dynamicSeq);
                params.put("createTime", createTime);
                params.put("orderState", type);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void UpdateUserAuth(final String userId, final String type) {

        String url = FXConstant.URL_INSERTPROHIBIT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();

                param.put("uId", userId);

                param.put("prohibittype", type);

                return param;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);

    }


    private Boolean ComparePriceAndVipLevel(String price, String task_position) {

        if (isVip > 0) {

            String[] strloca = null;

            if (task_position != null && !"".equals(task_position)) {
                strloca = task_position.split("\\|");
            }
            String resv2 = "", resv1 = "";
            if (strloca != null && strloca.length > 0) {
                resv2 = strloca[0];
            }
            if (strloca != null && strloca.length > 1) {
                resv1 = strloca[1];
            }
            double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
            double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());

            String str = "";
            if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1, longitude1);
                double distance = DistanceUtil.getDistance(ll, ll1);
                double dou = distance / 1000;
                str = String.format("%.2f", dou);//format 返回的是字符串

            } else {
                str = "空";
            }

            if (str.equals("空")) {

                return false;

            } else {

                if (vipLevel.equals("1")) {

                    if (Double.valueOf(str) < 50 && Double.valueOf(price) < 101) {

                        return true;

                    } else {

                        return false;

                    }

                } else if (vipLevel.equals("2")) {

                    if (Double.valueOf(str) < 100 && Double.valueOf(price) < 1001) {

                        return true;

                    } else {

                        return false;

                    }

                } else {

                    return true;

                }

            }

        } else {

            return false;

        }

    }

    private String subString(String task_locaName) {
        String str = task_locaName;
        if (str.contains("区") || str.contains("县")) {
            int i2;
            if (str.contains("区")) {
                i2 = task_locaName.indexOf("区");
            } else {
                i2 = task_locaName.indexOf("县");
            }
            if (str.contains("市")) {
                if (str.contains("省")) {
                    int i1 = task_locaName.indexOf("省");
                    str = task_locaName.substring(i1 + 1, i2 + 1);
                } else {
                    str = task_locaName.substring(0, i2 + 1);
                }
            } else {
                str = task_locaName.substring(0, i2 + 1);
            }
        } else {
            if (str.contains("市")) {
                if (str.contains("省")) {
                    int i1 = task_locaName.indexOf("省");
                    int i2 = task_locaName.indexOf("市");
                    str = task_locaName.substring(i1 + 1, i2 + 1);
                } else {
                    int i2 = task_locaName.indexOf("市");
                    str = task_locaName.substring(0, i2 + 1);
                }
            } else {
                if (str.contains("省")) {
                    int i1 = task_locaName.indexOf("省");
                    str = task_locaName.substring(i1 + 1, str.length());
                } else {
                    str = task_locaName;
                }
            }
        }

        return str;
    }

    private void queryhbzgCount(final String views, final String rel_time, String o, final LinearLayout card, final View v2, final String oncePrice, final String dynamicSeq, final String sID, final String fromUId, final String finalFirstImage, final String userID, final String content, boolean b) {
        String url = FXConstant.URL_Get_UserInfo + DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                JSONObject userInfo = object.getJSONObject("userInfo");
                String dynamicTimes = userInfo.getString("dynamicTimes");
                final String score = userInfo.getString("score");
                final String withdrawals = DemoApplication.getInstance().getCurrentWithdrawals();
                if (dynamicTimes == null || "".equals(dynamicTimes) || Double.parseDouble(dynamicTimes) == 0) {
                    LayoutInflater inflater2 = LayoutInflater.from(context);
                    RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog2 = new AlertDialog.Builder(context, R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout2);
                    dialog2.setCanceledOnTouchOutside(true);
                    dialog2.setCancelable(true);
                    TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                    Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                    final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                    TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                    if ("提现".equals(withdrawals) && "否".equals(score)) {
                        title2.setText("温馨提示");
                        btnOK2.setText("前去评分");
                        btnCancel2.setText("下次再说");
                        title_tv2.setText("您的红包次数已用完,前去应用市场为软件评分,即可永久获得一次分享次数！");
                    } else {
                        title2.setText("温馨提示");
                        btnOK2.setText("增加次数");
                        btnCancel2.setText("取消操作");
                        title_tv2.setText("您的红包次数已用完！");
                    }
                    btnCancel2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    btnOK2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            if ("提现".equals(withdrawals) && "否".equals(score)) {
                                try {
                                    Uri uri = Uri.parse("market://details?id="
                                            + context.getPackageName());//需要评分的APP包名
                                    Intent intent5 = new Intent(Intent.ACTION_VIEW, uri);
                                    intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent5);
                                    updateHbTimes();
                                    updateScore();
                                } catch (Exception e) {
                                    Toast.makeText(context, "跳转失败,请下载应用宝之后评分", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                context.startActivity(new Intent(context, HbHuoQuActivity.class));
                            }
                        }
                    });
                } else {
                    showZhfDialog(views, rel_time, null, card, v2, oncePrice, dynamicSeq, sID, fromUId, finalFirstImage, userID, content, true, "0");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void updateScore() {
        String url = FXConstant.URL_UPDATE_TIME;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("score", "是");
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void updateHbTimes() {
        String url = FXConstant.URL_XIUGAI_HBTIMES;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(context, "红包次数增加成功！", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("shareTimes", "1");
                param.put("homePageTimes", "1");
                param.put("dynamicTimes", "1");
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void showZhfDialog(final String views, final String createTime, final String redImage, final View v1, final View v2, final String jinE, final String dynamicSeq, final String sID, final String fromUId, final String finalFirstImage, final String userID, final String content, final boolean pinjie, final String floorPrice1) {

        String str11 = "";
        String str22 = "";
        String str33 = "";

        if (floorPrice1.equals("0")) {
            str11 = "0";
            str22 = "0";
            str33 = "0";
        } else {
            str11 = floorPrice1.split("\\|")[0];
            str22 = floorPrice1.split("\\|")[1];
            str33 = floorPrice1.split("\\|")[2];
        }

        final String floorPrice = str11;
        final String task_position = str22 + "|" + str33;


        if (dType.equals("05")) {

            LayoutInflater inflaterDl = LayoutInflater.from(context);
            final LinearLayout layout = (LinearLayout) inflaterDl.inflate(R.layout.dialog_dynamicorder_share, null);
            final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();

            TextView tv_first = (TextView) layout.findViewById(R.id.tv_firstTitle);
            TextView tv_second = (TextView) layout.findViewById(R.id.tv_secondTitle);
            String str1 = "<font color='#FF2600'><big>最佳</big></font> 接单方案";
            String str2 = "<font color='#5BB252'><big>免费</big></font> 接单方案";
            tv_first.setText(Html.fromHtml(str1));
            tv_second.setText(Html.fromHtml(str2));

            dialog.show();
            dialog.getWindow().setContentView(layout);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            Display display = context.getWindowManager().getDefaultDisplay();
            params.width = (int) (display.getWidth() * 0.75);                     //使用这种方式更改了dialog的框宽
            dialog.getWindow().setAttributes(params);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            RelativeLayout r1 = (RelativeLayout) layout.findViewById(R.id.rl_wxcricle);
            r1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    queryfxhbCount(redImage, v1, v2, jinE, dynamicSeq, createTime, sID, fromUId, userID, pinjie, 1, content, floorPrice1);
                }
            });

            RelativeLayout r2 = (RelativeLayout) layout.findViewById(R.id.r2_qzone);
            r2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    queryfxhbCount(redImage, v1, v2, jinE, dynamicSeq, createTime, sID, fromUId, userID, pinjie, 3, content, floorPrice1);
                }
            });

            RelativeLayout r3 = (RelativeLayout) layout.findViewById(R.id.r3_wxchat);
            r3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    fenxiangtowxf(v1, v2, jinE, dynamicSeq, createTime, sID, fromUId, userID, pinjie, 5, content, floorPrice1);
                }
            });

            Button bt_vip = (Button) layout.findViewById(R.id.bt_vip);

            if (vipTitle != null && !vipTitle.equals("0")) {

                bt_vip.setText(vipTitle);

            }

            bt_vip.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(context, TopVipActivity.class);

                    context.startActivityForResult(intent, 0);
                }
            });

            Button bt_margin = (Button) layout.findViewById(R.id.bt_margin);
            bt_margin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //直接跳转质保页面
                    dialog.dismiss();
                    if (DemoHelper.getInstance().isLoggedIn(context)) {

                        String url = FXConstant.URL_SELECTPROSINGLE;
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                JSONObject object = JSON.parseObject(s);
                                JSONObject proMap = object.getJSONObject("list");
                                if (proMap == null) {
                                    ToastUtils.showNOrmalToast(context, "请您优先编辑第一个专业");
                                    return;
                                }

                                String upName = proMap.getString("upName");
                                String margin = proMap.getString("margin");
                                String createTime = proMap.getString("creatTime");
                                String marginTime = proMap.getString("margin_time");

                                if (margin == null || "".equals(margin) || Double.parseDouble(margin) <= 0) {
                                    context.startActivityForResult(new Intent(context, BZJJNActivity.class).putExtra("upId", DemoHelper.getInstance().getCurrentUsernName() + "1").putExtra("maj", upName).putExtra("biaoshi", "00"), 0);
                                } else {
                                    context.startActivityForResult(new Intent(context, BZJZJActivity.class).putExtra("JINE", margin).putExtra("upId", DemoHelper.getInstance().getCurrentUsernName() + "1").putExtra("maj", upName)
                                            .putExtra("createTime", createTime).putExtra("biaoshi", "00").putExtra("margin_time", marginTime), 0);
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(context, "网络不稳定！", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> param = new HashMap<>();
                                param.put("up_id", myuserID + "1");
                                return param;
                            }
                        };
                        MySingleton.getInstance(context).addToRequestQueue(request);

                    }

                }
            });

            Button bt_message = (Button) layout.findViewById(R.id.bt_message);

            if (isshowMessageTop != null && !isshowMessageTop.equals("0")) {

                bt_message.setText(isshowMessageTop);
                bt_message.setVisibility(View.VISIBLE);
            }

            bt_message.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                    Intent intent = new Intent(context, MessageOrderIntroduceActivity.class);

                    context.startActivityForResult(intent, 0);

                }
            });

        } else {

            LayoutInflater inflater5 = LayoutInflater.from(context);
            final RelativeLayout layout5 = (RelativeLayout) inflater5.inflate(R.layout.dialog_fenxiang, null);
            final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
            dialog.show();
            Window window = dialog.getWindow();
            dialog.show();
            window.setWindowAnimations(R.style.dialogWindowAnim);
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            //这句就是设置dialog横向满屏了。
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            dialog.getWindow().setContentView(layout5);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    updateTJLiulancishu("转发", createTime, dynamicSeq, userID);
                }
            });
            TextView tv_title = (TextView) layout5.findViewById(R.id.tv_title);
            RelativeLayout rl1 = (RelativeLayout) layout5.findViewById(R.id.rl1);
            RelativeLayout rl2 = (RelativeLayout) layout5.findViewById(R.id.rl2);
            RelativeLayout rl3 = (RelativeLayout) layout5.findViewById(R.id.rl3);
            RelativeLayout rl4 = (RelativeLayout) layout5.findViewById(R.id.rl4);
            RelativeLayout rl5 = (RelativeLayout) layout5.findViewById(R.id.rl5);
            RelativeLayout rl6 = (RelativeLayout) layout5.findViewById(R.id.rl6);
            rl6.setVisibility(View.INVISIBLE);

//        rl6.setVisibility(View.INVISIBLE);
//        rl5.setVisibility(View.INVISIBLE);
//        tv4.setText("动    态");
//        iv4.setImageResource(R.drawable.app_logo);
            if ("05".equals(dType)) {

                SharedPreferences mSharedPreferences = context.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);

                if (!sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(dynamicSeq, "123").equalsIgnoreCase("123") && !ComparePriceAndVipLevel(floorPrice, task_position) && (Double.parseDouble(isHaveMargin) < 100 || Double.parseDouble(isHaveMargin) < Double.parseDouble(floorPrice))) {
                    Log.d("chen", content + "没有转发过");

                    tv_title.setText("分享后  可接单");
                    rl5.setVisibility(View.INVISIBLE);
                    rl4.setVisibility(View.INVISIBLE);
                    rl3.setVisibility(View.INVISIBLE);

                } else {
                    Log.d("chen", content + "转发过");
                    tv_title.setText("分享至");
                }

                rl6.setVisibility(View.INVISIBLE);
            }
            rl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    queryfxhbCount(redImage, v1, v2, jinE, dynamicSeq, createTime, sID, fromUId, userID, pinjie, 0, content, floorPrice);
                }
            });
            rl2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    queryfxhbCount(redImage, v1, v2, jinE, dynamicSeq, createTime, sID, fromUId, userID, pinjie, 1, content, floorPrice);
                }
            });
            rl3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    queryfxhbCount(redImage, v1, v2, jinE, dynamicSeq, createTime, sID, fromUId, userID, pinjie, 2, content, floorPrice);
                }
            });
            rl4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    final int jietuType;
                    if ("01".equals(dType)) {
                        jietuType = 1;//生活
                    } else if ("02".equals(dType)) {
                        if (redImage != null && !"".equals(redImage) && !redImage.equalsIgnoreCase("null")) {
                            jietuType = 3;//宝藏
                        } else {
                            jietuType = 2;//坐标
                        }
                    } else if (dType.equals("03") || dType.equals("04")) {
                        jietuType = 4;
                    } else {
                        jietuType = 5;
                    }
                    fenxiangtoqqf(v1, v2, jinE, dynamicSeq, createTime, sID, fromUId, userID, false, jietuType, content, floorPrice);
                }
            });
            rl5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    final int jietuType;
                    if ("01".equals(dType)) {
                        jietuType = 1;//生活
                    } else if ("02".equals(dType)) {
                        if (redImage != null && !"".equals(redImage) && !redImage.equalsIgnoreCase("null")) {
                            jietuType = 3;//宝藏
                        } else {
                            jietuType = 2;//坐标
                        }
                    } else if (dType.equals("03") || dType.equals("04")) {
                        jietuType = 4;
                    } else {
                        jietuType = 5;
                    }
                    fenxiangtowxf(v1, v2, jinE, dynamicSeq, createTime, sID, fromUId, userID, false, jietuType, content, floorPrice);
                }
            });

            rl6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(context, ZhFaActivity.class);
                    intent.putExtra("dType", dType);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("createTime", createTime);
                    intent.putExtra("jinE", jinE);
                    intent.putExtra("firstUId", sID);
                    intent.putExtra("fromUId", fromUId);
                    intent.putExtra("firstImage", finalFirstImage);
                    intent.putExtra("firstName", userID);
                    intent.putExtra("firstContent", content);
                    if (dType.equalsIgnoreCase("05")) {
                        intent.putExtra("responseTime", responseTime);
                        intent.putExtra("firstDistance", firstDistance);
                        intent.putExtra("resv1", resv1);
                        intent.putExtra("resv2", resv2);
                    }
                    context.startActivityForResult(intent, 0);
                }
            });

        }

    }

    private void insertDynamicContact(final String dynamicSeq, final String createTime, final String uId, final String contactId, final String type) {
        String url = FXConstant.URL_UPDATE_INSERT_DYNAMIC_CONTACT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "insertDynamicContact onResponse" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "insertDynamicContact onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //uId=&dynamicSeq=&createTime=&dis=&responsetime=&ordernum=&status=
                Map<String, String> param = new HashMap<>();
                param.put("dynamicSeq", dynamicSeq);
                param.put("createTime", createTime);
                param.put("uId", uId);
                param.put("contactId", contactId);
                param.put("type", type);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    private void queryfxhbCount(String redImage, final View v1, final View v2, final String fenxiangJine, final String dynamicSeq, final String createTime, final String sID, final String fromUId, final String userID, final boolean pinjie, final int type, final String content, final String floorPrice) {
        final int jietuType;

        if ("01".equals(dType)) {
            jietuType = 1;//生活
        } else if ("02".equals(dType)) {
            if (redImage != null && !"".equals(redImage) && !redImage.equalsIgnoreCase("null")) {
                jietuType = 3;//宝藏
            } else {
                jietuType = 2;//坐标
            }
        } else if (dType.equals("03") || dType.equals("04")) {
            jietuType = 4;
        } else {
            jietuType = 5;
        }
        String url = FXConstant.URL_QUERY_HBCOUNT + DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                int sum = object.getIntValue("sum");
                sum = sum + 1;
                if (type == 0) {
                    fenxiangtoqq(v1, v2, fenxiangJine, dynamicSeq, createTime, sID, fromUId, userID, pinjie, jietuType, sum, content, floorPrice);
                } else if (type == 1) {
                    fenxiangtowx(v1, v2, fenxiangJine, dynamicSeq, createTime, sID, fromUId, userID, pinjie, jietuType, sum, content, floorPrice);
                } else if (type == 2) {
                    fenxiangtowb(v1, v2, fenxiangJine, dynamicSeq, createTime, sID, fromUId, userID, pinjie, jietuType, sum, content, floorPrice);
                } else if (type == 3) {
                    fenxiangtoqqf(v1, v2, fenxiangJine, dynamicSeq, createTime, sID, fromUId, userID, pinjie, jietuType, content, floorPrice);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (type == 0) {
                    fenxiangtoqq(v1, v2, fenxiangJine, dynamicSeq, createTime, sID, fromUId, userID, pinjie, jietuType, -2, content, floorPrice);
                } else if (type == 1) {
                    fenxiangtowx(v1, v2, fenxiangJine, dynamicSeq, createTime, sID, fromUId, userID, pinjie, jietuType, -2, content, floorPrice);
                } else if (type == 2) {
                    fenxiangtowb(v1, v2, fenxiangJine, dynamicSeq, createTime, sID, fromUId, userID, pinjie, jietuType, -2, content, floorPrice);
                } else if (type == 3) {
                    fenxiangtoqqf(v1, v2, fenxiangJine, dynamicSeq, createTime, sID, fromUId, userID, false, jietuType, content, floorPrice);

                }
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void fenxiangtowb(final View v1, final View v2, final String fenxiangJine, final String dynamicSeq, final String createTime, final String sID, final String fromUId, final String userID, final boolean pinjie, final int jietuType, final int sum, final String content, final String floorPrice1) {
        String fenxiangType = null;

        String str11 = "";
        String str22 = "";
        String str33 = "";

        if (floorPrice1.equals("0")) {
            str11 = "0";
            str22 = "0";
            str33 = "0";
        } else {
            str11 = floorPrice1.split("\\|")[0];
            str22 = floorPrice1.split("\\|")[1];
            str33 = floorPrice1.split("\\|")[2];
        }

        final String floorPrice = str11;
        final String task_position = str22 + "|" + str33;

        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "weiboLifeDynamic";
            } else if (dType.equals("02")) {
                fenxiangType = "weiboLocationDynamic";
            } else {
                fenxiangType = "weiboBusinessDynamic";
            }
        }
        final String text;
        if (!pinjie) {
            text = "不限行业接派单，全民分享赚红包" + content;
        } else {
            text = "我转发他的动态，获得了一个红包" + content;
        }

        if (!pinjie) {
            ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, jietuType, false, 0, 0);
        } else {
            ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", fenxiangJine, sum, true, 0, 0);
        }
        final SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();

        SharedPreferences mSharedPreferences = context.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
        if (dType.equals("05") && !sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(dynamicSeq, "123").equalsIgnoreCase("123") && !ComparePriceAndVipLevel(floorPrice, task_position) && (Double.parseDouble(isHaveMargin) < 100 || Double.parseDouble(isHaveMargin) < Double.parseDouble(floorPrice))) {
            // Log.d("chen", content + "没有转发过");

            SharedPreferences sp5 = context.getSharedPreferences("sangu_gonggao_info", Context.MODE_PRIVATE);

            if (sp5 != null) {

                String shareType = sp5.getString("shareType", "0");

                if (shareType.equals("1")) {
                    ScreenshotUtil.saveDrawableById(context, R.drawable.ordershare1);
                    sp.setTitle("");
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                } else if (shareType.equals("2")) {
                    ScreenshotUtil.saveDrawableById(context, R.drawable.ordershare2);
                    sp.setTitle("");
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                } else {
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                    sp.setTitle("正事多-接单派单工具");
                }

            } else {
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("正事多-接单派单工具");
            }

        } else {
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
            sp.setTitle("正事多-接单派单工具");
        }

        Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wb.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(context, "分享到微博失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(context, "成功分享到微博！", Toast.LENGTH_SHORT).show();
                if (!dType.equals("05")) {
                    updateTJzhuanfa(sID, 2);
                }
                updatePaidanZhuanfa();
                addzhuanfaCount(dynamicSeq, myuserID, fenxiangJine, sID, fromUId, userID, createTime);
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(context, "取消分享到微博！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wb.share(sp);

        /*
        LayoutInflater inflaterDl = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(context,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!pinjie){
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null,jietuType,false);
                }else {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", fenxiangJine,sum,true);
                }
                final SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("正事多-接单派单工具");
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wb.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(context, "分享到微博失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(context, "成功分享到微博！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,2);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID,createTime);
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(context, "取消分享到微博！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wb.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByV2(context, v2);
                final SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                String url;
                if ("05".equals(dType)) {
                    url = "http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime;
                }else {
                    url = "http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime+"&type="+ finalFenxiangType;
                }
                sp.setText(text+url);
                sp.setTitle("正事多-接单派单工具");
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wb.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(context, "分享到微博失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(context, "成功分享到微博！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,2);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID,createTime);
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(context, "取消分享到微博！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wb.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    private void fenxiangtowx(final View v1, final View v2, final String fenxiangJine, final String dynamicSeq, final String createTime, final String sID, final String fromUId, final String userID, final boolean pinjie, final int jietuType, final int sum, final String content, final String floorPrice1) {
        String fenxiangType = null;

        String str11 = "";
        String str22 = "";
        String str33 = "";

        if (floorPrice1.equals("0")) {
            str11 = "0";
            str22 = "0";
            str33 = "0";
        } else {
            str11 = floorPrice1.split("\\|")[0];
            str22 = floorPrice1.split("\\|")[1];
            str33 = floorPrice1.split("\\|")[2];
        }

        final String floorPrice = str11;
        final String task_position = str22 + "|" + str33;

        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "weixinLifeDynamic";
            } else if (dType.equals("02")) {
                fenxiangType = "weixinLocationDynamic";
            } else {
                fenxiangType = "weixinBusinessDynamic";
            }
        }
        final String text;
        if (!pinjie) {
            text = "不限行业接派单，全民分享赚红包" + content;
        } else {
            text = "我转发他的动态，获得了一个红包" + content;
        }

        if (!pinjie) {
            ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, jietuType, false, 0, 0);
        } else {
            ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", fenxiangJine, sum, true, 0, 0);
        }
        final WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);

        SharedPreferences mSharedPreferences = context.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
        if (dType.equals("05") && !sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(dynamicSeq, "123").equalsIgnoreCase("123") && !ComparePriceAndVipLevel(floorPrice, task_position) && (Double.parseDouble(isHaveMargin) < 100 || Double.parseDouble(isHaveMargin) < Double.parseDouble(floorPrice))) {
            // Log.d("chen", content + "没有转发过");

            SharedPreferences sp5 = context.getSharedPreferences("sangu_gonggao_info", Context.MODE_PRIVATE);

            if (sp5 != null) {

                String shareType = sp5.getString("shareType", "0");

                if (shareType.equals("1")) {
                    ScreenshotUtil.saveDrawableById(context, R.drawable.ordershare1);
                    sp.setTitle("");
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                } else if (shareType.equals("2")) {
                    ScreenshotUtil.saveDrawableById(context, R.drawable.ordershare2);
                    sp.setTitle("");
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                } else {

                    if (currentType.equals("01")) {
                        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 51, false, 0, 0);
                    } else if (currentType.equals("02")) {
                        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 52, false, 0, 0);
                    } else if (currentType.equals("03")) {
                        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 53, false, 0, 0);
                    } else {
                        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, jietuType, false, 0, 0);
                    }

                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                    sp.setTitle("【正事多】里面有接单派单名片红包");
                }

            } else {
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("【正事多】里面有接单派单名片红包");
            }

        } else {

            if (dType.equals("05")) {

                if (currentType.equals("01")) {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 51, false, 0, 0);
                } else if (currentType.equals("02")) {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 52, false, 0, 0);
                } else if (currentType.equals("03")) {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 53, false, 0, 0);
                } else {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, jietuType, false, 0, 0);
                }

            }

            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
            sp.setTitle("【正事多】里面有接单派单名片红包");
        }


        Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(context, "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(context, "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                if (!dType.equals("05")) {
                    updateTJzhuanfa(sID, 1);
                }
                updatePaidanZhuanfa();
                addzhuanfaCount(dynamicSeq, myuserID, fenxiangJine, sID, fromUId, userID, createTime);
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(context, "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wx.share(sp);

       /* LayoutInflater inflaterDl = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(context,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!pinjie){
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null,jietuType,false);
                }else {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", fenxiangJine,sum,true);
                }
                final WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
                sp.setShareType(Platform.SHARE_IMAGE);
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("【正事多】里面有接单派单名片红包");
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(context, "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(context, "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,1);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID,createTime);
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(context, "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByV2(context, v2);
                final WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setShareType(Platform.SHARE_WEBPAGE);
                sp.setTitle(text);
                if ("05".equals(dType)) {
                    sp.setUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime);
                }else {
                    sp.setUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime+"&type="+ finalFenxiangType);
                }
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(context, "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(context, "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,1);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID,createTime);
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(context, "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    private void fenxiangtowxf(final View v1, final View v2, final String fenxiangJine, final String dynamicSeq, final String createTime, final String sID, final String fromUId, final String userID, boolean pinjie, final int jietuType, final String content, final String floorPrice1) {
        String fenxiangType = null;

        String str11 = "";
        String str22 = "";
        String str33 = "";

        if (floorPrice1.equals("0")) {
            str11 = "0";
            str22 = "0";
            str33 = "0";
        } else {
            str11 = floorPrice1.split("\\|")[0];
            str22 = floorPrice1.split("\\|")[1];
            str33 = floorPrice1.split("\\|")[2];
        }

        final String floorPrice = str11;
        final String task_position = str22 + "|" + str33;

        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "weixinLifeDynamic";
            } else if (dType.equals("02")) {
                fenxiangType = "weixinLocationDynamic";
            } else {
                fenxiangType = "weixinBusinessDynamic";
            }
        }
        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, jietuType, false, 0, 0);
        final Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);


        SharedPreferences mSharedPreferences = context.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
        if (dType.equals("05") && !sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(dynamicSeq, "123").equalsIgnoreCase("123") && !ComparePriceAndVipLevel(floorPrice, task_position) && (Double.parseDouble(isHaveMargin) < 100 || Double.parseDouble(isHaveMargin) < Double.parseDouble(floorPrice))) {
            // Log.d("chen", content + "没有转发过");

            SharedPreferences sp5 = context.getSharedPreferences("sangu_gonggao_info", Context.MODE_PRIVATE);

            if (sp5 != null) {

                String shareType = sp5.getString("shareType", "0");

                if (shareType.equals("1")) {
                    ScreenshotUtil.saveDrawableById(context, R.drawable.ordershare1);
                    sp.setTitle("");
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                } else if (shareType.equals("2")) {
                    ScreenshotUtil.saveDrawableById(context, R.drawable.ordershare2);
                    sp.setTitle("");
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                } else {

                    if (currentType.equals("01")) {
                        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 51, false, 0, 0);
                    } else if (currentType.equals("02")) {
                        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 52, false, 0, 0);
                    } else if (currentType.equals("03")) {
                        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 53, false, 0, 0);
                    } else {
                        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, jietuType, false, 0, 0);
                    }

                    sp.setTitle("【正事多】里面有接单派单名片红包");
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                }

            } else {
                sp.setTitle("【正事多】里面有接单派单名片红包");
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
            }

        } else {

            if (dType.equals("05")) {

                if (currentType.equals("01")) {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 51, false, 0, 0);
                } else if (currentType.equals("02")) {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 52, false, 0, 0);
                } else if (currentType.equals("03")) {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 53, false, 0, 0);
                } else {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, jietuType, false, 0, 0);
                }

            }

            sp.setTitle("【正事多】里面有接单派单名片红包");
            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
        }


        Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(context, "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(context, "成功分享到好友！", Toast.LENGTH_SHORT).show();
                if (!dType.equals("05")) {
                    updateTJzhuanfa(sID, 1);
                }
                updatePaidanZhuanfa();
                addzhuanfaCount(dynamicSeq, myuserID, fenxiangJine, sID, fromUId, userID, createTime);
                // addzhuanfaCount(dynamicSeq,myuserID,null,sID,fromUId,userID,createTime);
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(context, "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wx.share(sp);

        /*LayoutInflater inflaterDl = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(context,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null,jietuType,false);
                final Wechat.ShareParams sp = new Wechat.ShareParams();
                sp.setShareType(Platform.SHARE_IMAGE);
                sp.setTitle("【正事多】里面有接单派单名片红包");
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(context, "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(context, "成功分享到好友！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,1);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,null,sID,fromUId,userID,createTime);
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(context, "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByV2(context, v2);
                final Wechat.ShareParams sp = new Wechat.ShareParams();
                sp.setShareType(Platform.SHARE_WEBPAGE);
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("不限行业接派单，全民分享赚红包"+content);
                if ("05".equals(dType)) {
                    sp.setUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime);
                }else {
                    sp.setUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime+"&type="+ finalFenxiangType);
                }
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(context, "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(context, "成功分享到微信好友！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,1);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,null,sID,fromUId,userID,createTime);
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(context, "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    private void fenxiangtoqqf(final View v1, final View v2, final String fenxiangJine, final String dynamicSeq, final String createTime, final String sID, final String fromUId, final String userID, final boolean pinjie, final int jietuType, final String content, final String floorPrice1) {
        String fenxiangType = null;

        String str11 = "";
        String str22 = "";
        String str33 = "";

        if (floorPrice1.equals("0")) {
            str11 = "0";
            str22 = "0";
            str33 = "0";
        } else {
            str11 = floorPrice1.split("\\|")[0];
            str22 = floorPrice1.split("\\|")[1];
            str33 = floorPrice1.split("\\|")[2];
        }

        final String floorPrice = str11;
        final String task_position = str22 + "|" + str33;

        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "qqLifeDynamic";
            } else if (dType.equals("02")) {
                fenxiangType = "qqLocationDynamic";
            } else {
                fenxiangType = "qqBusinessDynamic";
            }
        }


        final QQ.ShareParams sp = new QQ.ShareParams();
        SharedPreferences mSharedPreferences = context.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
        if (dType.equals("05") && !sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(dynamicSeq, "123").equalsIgnoreCase("123") && !ComparePriceAndVipLevel(floorPrice, task_position) && (Double.parseDouble(isHaveMargin) < 100 || Double.parseDouble(isHaveMargin) < Double.parseDouble(floorPrice))) {
            // Log.d("chen", content + "没有转发过");

            SharedPreferences sp5 = context.getSharedPreferences("sangu_gonggao_info", Context.MODE_PRIVATE);

            if (sp5 != null) {

                String shareType = sp5.getString("shareType", "0");

                if (shareType.equals("1")) {
                    ScreenshotUtil.saveDrawableById(context, R.drawable.ordershare1);
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                } else if (shareType.equals("2")) {
                    ScreenshotUtil.saveDrawableById(context, R.drawable.ordershare2);
                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                } else {

                    if (currentType.equals("01")) {
                        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 51, false, 0, 0);
                    } else if (currentType.equals("02")) {
                        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 52, false, 0, 0);
                    } else if (currentType.equals("03")) {
                        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 53, false, 0, 0);
                    } else {
                        ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, jietuType, false, 0, 0);
                    }


                    sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                }

            } else {
                ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, jietuType, false, 0, 0);
                sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
            }

        } else {

            if (dType.equals("05")) {

                if (currentType.equals("01")) {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 51, false, 0, 0);
                } else if (currentType.equals("02")) {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 52, false, 0, 0);
                } else if (currentType.equals("03")) {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, 53, false, 0, 0);
                } else {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, jietuType, false, 0, 0);
                }

            } else {

                ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, jietuType, false, 0, 0);

            }

            sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");

        }

        sp.setTitle(null);
        sp.setText(null);
        Platform qqf = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qqf.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(context, "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(context, "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
                if (!dType.equals("05")) {
                    updateTJzhuanfa(sID, 0);
                }
                updatePaidanZhuanfa();
                addzhuanfaCount(dynamicSeq, myuserID, fenxiangJine, sID, fromUId, userID, createTime);
//                if (dType.equals("05")){
//                    addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID,createTime);
//                }else {
//                    addzhuanfaCount(dynamicSeq,myuserID,null,sID,fromUId,userID,createTime);
//                }

            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(context, "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        qqf.share(sp);

        /*LayoutInflater inflaterDl = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(context,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null,jietuType,false);
                final QQ.ShareParams sp = new QQ.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle(null);
                sp.setText(null);
                Platform qqf = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qqf.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(context, "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(context, "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,0);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,null,sID,fromUId,userID,createTime);
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(context, "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qqf.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByV2(context,v2);
                final QQ.ShareParams sp = new QQ.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                if ("05".equals(dType)) {
                    sp.setTitleUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime);
                    sp.setSiteUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime);
                }else {
                    sp.setTitleUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime+"&type="+ finalFenxiangType);
                    sp.setSiteUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime+"&type="+ finalFenxiangType);
                }
                sp.setSite("分享链接");
                sp.setTitle("正事多-接单派单工具");
                sp.setText("不限行业接派单，全民分享赚红包"+content);
                Platform qqf = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qqf.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(context, "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(context, "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,0);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,null,sID,fromUId,userID,createTime);
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(context, "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qqf.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    private void fenxiangtoqq(final View v1, final View v2, final String fenxiangJine, final String dynamicSeq, final String createTime, final String sID, final String fromUId, final String userID, final boolean pinjie, final int jietuType, final int sum, final String content, final String floorPrice1) {
        String fenxiangType = null;

        String str11 = "";
        String str22 = "";
        String str33 = "";

        if (floorPrice1.equals("0")) {
            str11 = "0";
            str22 = "0";
            str33 = "0";
        } else {
            str11 = floorPrice1.split("\\|")[0];
            str22 = floorPrice1.split("\\|")[1];
            str33 = floorPrice1.split("\\|")[2];
        }

        final String floorPrice = str11;
        final String task_position = str22 + "|" + str33;

        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "qqLifeDynamic";
            } else if (dType.equals("02")) {
                fenxiangType = "qqLocationDynamic";
            } else {
                fenxiangType = "qqBusinessDynamic";
            }
        }
        final String text;
        if (!pinjie) {
            text = "不限行业接派单，全民分享赚红包" + content;
        } else {
            text = "我转发他的动态，获得了一个红包" + content;
        }
        final QZone.ShareParams sp = new QZone.ShareParams();
        sp.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");

        if (!pinjie) {
            ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null, jietuType, false, 0, 0);
        } else {
            ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", fenxiangJine, sum, true, 0, 0);
        }
        final QZone.ShareParams sp2 = new QZone.ShareParams();

        SharedPreferences mSharedPreferences = context.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
        if (dType.equals("05") && !sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(dynamicSeq, "123").equalsIgnoreCase("123") && !ComparePriceAndVipLevel(floorPrice, task_position) && (Double.parseDouble(isHaveMargin) < 100 || Double.parseDouble(isHaveMargin) < Double.parseDouble(floorPrice))) {
            // Log.d("chen", content + "没有转发过");

            SharedPreferences sp5 = context.getSharedPreferences("sangu_gonggao_info", Context.MODE_PRIVATE);

            if (sp5 != null) {

                String shareType = sp5.getString("shareType", "0");

                if (shareType.equals("1")) {
                    ScreenshotUtil.saveDrawableById(context, R.drawable.ordershare1);
                    sp2.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                } else if (shareType.equals("2")) {
                    ScreenshotUtil.saveDrawableById(context, R.drawable.ordershare2);
                    sp2.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png");
                } else {
                    sp2.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
                }

            } else {
                sp2.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
            }

        } else {
            sp2.setImagePath(Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut.png");
        }

        sp2.setTitle(null);
        sp2.setText(null);
        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qzone.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // Log.d("chen", "开始转发qq空间onError");
                Toast.makeText(context, "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //Log.d("chen", "开始转发qq空间onComplete");
                Toast.makeText(context, "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                if (!dType.equals("05")) {
                    updateTJzhuanfa(sID, 0);
                }
                updatePaidanZhuanfa();
                addzhuanfaCount(dynamicSeq, myuserID, fenxiangJine, sID, fromUId, userID, createTime);
            }

            public void onCancel(Platform arg0, int arg1) {
                //Log.d("chen", "开始转发qq空间onCancel");
                Toast.makeText(context, "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        //Log.d("chen", "开始转发qq空间");
        qzone.share(sp2);




      /*  LayoutInflater inflaterDl = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(context,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!pinjie){
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", null,jietuType,false);
                }else {
                    ScreenshotUtil.getBitmapByView(context, v1, "分享动态红包", fenxiangJine,sum,true);
                }
                final QZone.ShareParams sp = new QZone.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle(null);
                sp.setText(null);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                       // Log.d("chen", "开始转发qq空间onError");
                        Toast.makeText(context, "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        //Log.d("chen", "开始转发qq空间onComplete");
                        Toast.makeText(context, "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,0);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID,createTime);
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        //Log.d("chen", "开始转发qq空间onCancel");
                        Toast.makeText(context, "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                //Log.d("chen", "开始转发qq空间");
                qzone.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByV2(context, v2);
                final QZone.ShareParams sp = new QZone.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                if ("05".equals(dType)) {
                    sp.setTitleUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime);
                    sp.setSiteUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime);
                }else {
                    sp.setTitleUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime+"&type="+ finalFenxiangType);
                    sp.setSiteUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime+"&type="+ finalFenxiangType);
                }
                sp.setSite("分享链接");
                sp.setTitle("正事多-接单派单工具");
                sp.setText(text);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(context, "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(context, "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,0);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID,createTime);
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(context, "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qzone.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    private String paidanZhuanfa;

    private void updatePaidanZhuanfa() {
        if (TextUtils.isEmpty(paidanZhuanfa) && !dType.equalsIgnoreCase("05")) {
            return;
        }
        SharedPreferences mSharedPreferences = context.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(paidanZhuanfa, "321");
        editor.commit();
        notifyDataSetChanged();
    }

    private void addzhuanfaCount(final String dynamicSeq, final String myuserID, final String jinE, final String sID, final String fromUId, final String userID, final String createTime) {
        String url = FXConstant.URL_ADD_COUNT_ZHUFA;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateTJLiulancishu("转发", createTime, dynamicSeq, userID);
                if ("05".equals(dType)) {
                    if (jinE != null && Double.parseDouble(jinE) > 0 && isVip > 0 && Double.parseDouble(isHaveMargin) > 99) {
                        addhongbao(jinE, dynamicSeq, myuserID, userID);
                    }
                } else {
                    if (jinE != null && Double.parseDouble(jinE) > 0) {
                        dongtai(sID, fromUId, dynamicSeq, jinE, userID);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("u_id", myuserID);
                param.put("dynamicSeq", dynamicSeq);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void addhongbao(final String jinE, final String dynamicSeq, final String myuserID, final String userId) {
        String url = FXConstant.URL_ADD_RED_XUQIU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("socailmain,s", s);
                JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("已经转发过次动态".equals(code)) {
                    LayoutInflater inflater1 = LayoutInflater.from(context);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(context, R.style.Dialog).create();
                    dialog1.show();
                    dialog1.getWindow().setContentView(layout1);
                    dialog1.setCanceledOnTouchOutside(true);
                    dialog1.setCancelable(true);
                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                    TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                    title.setText("温馨提示");
                    btnOK1.setText("确定");
                    btnCancel1.setText("取消");
                    title_tv1.setText("您已经分享过此条动态,本次分享无红包奖励");
                    btnCancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });
                    btnOK1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });
                } else if (code.equals("success")) {
                    SoundPlayUtils.play(2);
                    LayoutInflater inflaterDl = LayoutInflater.from(context);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_hongbao, null);
                    final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    TextView tv_title1 = (TextView) layout.findViewById(R.id.tv_title1);
                    TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
                    TextView tv_yue = (TextView) layout.findViewById(R.id.tv_yue);
                    TextPaint tp = tv_title.getPaint();
                    tp.setFakeBoldText(true);
                    tv_title.setText(jinE + "元");
                    tv_title1.setText("推荐红包");
                    tv_yue.setText("正事多 动态红包");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("111", "222");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("u_id", myuserID);
                param.put("dynamicSeq", dynamicSeq);
                param.put("amount", jinE);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void dongtai(final String firstUId, final String fromUId, final String dynamicSeq, final String jinE, final String name) {

        final String content = "我获得了一个分享动态红包!";
        String url = FXConstant.URL_PUBLISH;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    if (code.equals("SUCCESS")) {
                        String timel1 = getNowTime();
                        SharedPreferences mSharedPreferences = context.getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        if (dType.equals("01")) {
                            editor.putString("time1", timel1);
                            editor.putString("time5", timel1);
                        } else if (dType.equals("02")) {
                            editor.putString("time2", timel1);
                            editor.putString("time6", timel1);
                        } else if (dType.equals("03")) {
                            editor.putString("time3", timel1);
                            editor.putString("time7", timel1);
                        } else {
                            editor.putString("time4", timel1);
                            editor.putString("time8", timel1);
                        }
                        editor.commit();
                        updateBmob();
                    }
                    if ("已经转发过次动态".equals(message)) {
                        LayoutInflater inflater1 = LayoutInflater.from(context);
                        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog1 = new AlertDialog.Builder(context, R.style.Dialog).create();
                        dialog1.show();
                        dialog1.getWindow().setContentView(layout1);
                        dialog1.setCanceledOnTouchOutside(true);
                        dialog1.setCancelable(true);
                        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                        title.setText("温馨提示");
                        btnOK1.setText("确定");
                        btnCancel1.setText("取消");
                        title_tv1.setText("您已经分享过此条动态,本次分享无红包奖励");
                        btnCancel1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                        btnOK1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                    } else {
                        SoundPlayUtils.play(2);
                        LayoutInflater inflaterDl = LayoutInflater.from(context);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_hongbao, null);
                        final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        TextView tv_title1 = (TextView) layout.findViewById(R.id.tv_title1);
                        TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
                        TextView tv_yue = (TextView) layout.findViewById(R.id.tv_yue);
                        TextPaint tp = tv_title.getPaint();
                        tp.setFakeBoldText(true);
                        tv_title.setText(jinE + "元");
                        tv_title1.setText(name);
                        tv_yue.setText("正事多 动态红包");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("dType", dType);
                param.put("firstUId", firstUId);
                param.put("fromUId", fromUId);
                param.put("dynamicSeq", dynamicSeq);
                param.put("content", content);
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(context, DemoHelper.getInstance().getCurrentUsernName(), "4", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(context).addToRequestQueue(request);
            }

            @Override
            public void onBan() {
                ToastUtils.showNOrmalToast(context.getApplicationContext(), "您的账户已被禁止发送动态");

            }
        });

    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    private void updateBmob() {
        String url = FXConstant.URL_UPDATE_DYNATIME;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                if (dType.equals("01")) {
                    param.put("type5", getNowTime());
                } else if (dType.equals("02")) {
                    param.put("type6", getNowTime());
                } else if (dType.equals("03")) {
                    param.put("type7", getNowTime());
                } else if (dType.equals("05")) {
                    param.put("type8", getNowTime());
                }
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (users.get(position).getString("fromUId") != null) {
            type = ZHUANFA_ITEM;
        } else {
            type = NORMAL_ITEM;
        }
        return type;
    }

    public class ViewHolderSixzhf extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tv_content;

        ImageView fristImageView;
        ImageView secondImageView;
        ImageView thridImageView;
        ImageView image_videoImage;

        TextView tv_hot;
        TextView tv_source;
        TextView tv_views;
        TextView tv_times;

        LinearLayout rl_image;
        RelativeLayout rl_video;

        ImageView image_advert;
        TextView tv_commentcount;

        private MyItem5ClickListener mListener;
        private MyItem5LongClickListener mLongClickListener;

        public ViewHolderSixzhf(View convertView, MyItem5ClickListener listener, MyItem5LongClickListener longClickListener) {
            super(convertView);

            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            fristImageView = (ImageView) convertView.findViewById(R.id.image_firstImage);
            secondImageView = (ImageView) convertView.findViewById(R.id.image_secondImage);
            thridImageView = (ImageView) convertView.findViewById(R.id.image_thridImage);
            tv_hot = (TextView) convertView.findViewById(R.id.tv_hotinditify);
            tv_source = (TextView) convertView.findViewById(R.id.tv_source);
            tv_views = (TextView) convertView.findViewById(R.id.tv_viewscount);
            tv_times = (TextView) convertView.findViewById(R.id.tv_times);
            rl_image = (LinearLayout) convertView.findViewById(R.id.rl_image);
            rl_video = (RelativeLayout) convertView.findViewById(R.id.rl_video);
            image_videoImage = (ImageView) convertView.findViewById(R.id.image_videoImage);

            image_advert = (ImageView) convertView.findViewById(R.id.image_advert);

            tv_commentcount = (TextView) convertView.findViewById(R.id.tv_commentcount);

            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }

    }

    public class ViewHolderFivezhf extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CircleImageView iv_avatar;
        CircleImageView iv_first_avatar;
        // 昵称
        TextView tvDistance;
        TextView tv_count_llc;
        TextView tv_nick;
        TextView tv_first_nick;
        // 时间
        TextView tv_time;
        TextView tvTitleA;
        TextView tvfirst_TitleA;
        // 三行图片
        LinearLayout ll_one;
        LinearLayout ll_two;
        ImageView image_1;
        ImageView image_2;
        ImageView image_3;
        ImageView image_4;
        ImageView image_5;
        ImageView image_6;
        ImageView image_8;
        ImageView image_7;
        // 动态内容
        TextView tv_location;
        ExpandableTextView tv_zhf_content;
        TextView tv_content2;
        // 位置
        TextView tv_count_zhf;
        TextView tv_count_pl;
        TextView tvNianLing;
        TextView tvCompany;

        LinearLayout card;
        RelativeLayout rl_zhuanfa;
        RelativeLayout rl_pinglun;
        Button btn_xiadan;
        private MyItem4ClickListener mListener;
        private MyItem4LongClickListener mLongClickListener;

        public ViewHolderFivezhf(View convertView, MyItem4ClickListener listener, MyItem4LongClickListener longClickListener) {
            super(convertView);
            tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
            tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            tv_count_llc = (TextView) convertView.findViewById(R.id.tv_count_llc);
            tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
            tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            iv_avatar = (CircleImageView) convertView.findViewById(R.id.sdv_image);
            tv_first_nick = (TextView) convertView.findViewById(R.id.tv_first_nick);
            tvfirst_TitleA = (TextView) convertView.findViewById(R.id.tv_zhf_titl);
            iv_first_avatar = (CircleImageView) convertView.findViewById(R.id.sdv_first_image);
            tv_zhf_content = (ExpandableTextView) convertView.findViewById(R.id.tv_zhf_content);
            tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            image_1 = (ImageView) convertView.findViewById(R.id.image_1);
            image_2 = (ImageView) convertView.findViewById(R.id.image_2);
            image_3 = (ImageView) convertView.findViewById(R.id.image_3);
            image_4 = (ImageView) convertView.findViewById(R.id.image_4);
            image_5 = (ImageView) convertView.findViewById(R.id.image_5);
            image_6 = (ImageView) convertView.findViewById(R.id.image_6);
            image_7 = (ImageView) convertView.findViewById(R.id.image_7);
            image_8 = (ImageView) convertView.findViewById(R.id.image_8);
            ll_one = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
            card = (LinearLayout) convertView.findViewById(R.id.card);
            tv_content2 = (TextView) convertView.findViewById(R.id.tv_content);
            tv_count_zhf = (TextView) convertView.findViewById(R.id.tv_count_zhf);
            tv_count_pl = (TextView) convertView.findViewById(R.id.tv_count_pl);
            rl_zhuanfa = (RelativeLayout) convertView.findViewById(R.id.rl_zhuanfa);
            rl_pinglun = (RelativeLayout) convertView.findViewById(R.id.rl_pinglun);
            btn_xiadan = (Button) convertView.findViewById(R.id.btn_xiadan);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }
    }

    public class ViewHolderFive extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CircleImageView iv_avatar;
        // 昵称
        TextView tvDistance;
        TextView tv_count_llc;
        TextView tv_nick;
        // 时间
        TextView tv_time;
        TextView tv_chujia;
        TextView tvTitleA;
        TextView tv_huikui_zonge;
        TextView tv_huikui_yue;
        TextView tv_huikui_zhaunfa;
        TextView tv_liulan_cishu;
        LinearLayout ll_huikui;
        // 三行图片
        LinearLayout ll_one;
        LinearLayout ll_two;
        ImageView image_1;
        ImageView image_2;
        ImageView image_3;
        ImageView image_4;
        ImageView image_5;
        ImageView image_6;
        ImageView image_8;
        ImageView image_7;
        ImageView image_shareRed;
        // 动态内容
        TextView tv_location;
        TextView tv_content;
        // 位置
        TextView tv_count_zhf;
        TextView tv_count_pl;
        TextView tvNianLing;
        TextView tvCompany;
        TextView tv_recommend;
        TextView tv_video;
        LinearLayout card;
        RelativeLayout rl_zhuanfa;
        RelativeLayout rl_pinglun;
        RelativeLayout rl_video;
        RelativeLayout rl_all_contain;
        RelativeLayout rl_comment;
        TextView tv_comcount;
        ImageView rl_pre_click;
        JCVideoPlayerStandard videoPlayer;
        Button btn_xiadan;
        Button btn_daohang;
        TextView tv_demandType;
        ImageView image_advert;
        TextView tv_title_pl;

        TextView authBtn1;
        TextView authBtn2;
        TextView authBtn3;
        TextView authBtn4;
        TextView authBtn5;
        TextView authBtn6;


        private MyItem4ClickListener mListener;
        private MyItem4LongClickListener mLongClickListener;

        public ViewHolderFive(View convertView, MyItem4ClickListener listener, MyItem4LongClickListener longClickListener) {
            super(convertView);
            tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
            tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            tv_video = (TextView) convertView.findViewById(R.id.tv_video);
            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            tv_count_llc = (TextView) convertView.findViewById(R.id.tv_count_llc);
            tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
            tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            tv_chujia = (TextView) convertView.findViewById(R.id.tv_chujia);
            tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            iv_avatar = (CircleImageView) convertView.findViewById(R.id.sdv_image);
            image_1 = (ImageView) convertView.findViewById(R.id.image_1);
            image_2 = (ImageView) convertView.findViewById(R.id.image_2);
            image_3 = (ImageView) convertView.findViewById(R.id.image_3);
            image_4 = (ImageView) convertView.findViewById(R.id.image_4);
            image_5 = (ImageView) convertView.findViewById(R.id.image_5);
            image_6 = (ImageView) convertView.findViewById(R.id.image_6);
            image_7 = (ImageView) convertView.findViewById(R.id.image_7);
            image_8 = (ImageView) convertView.findViewById(R.id.image_8);
            ll_huikui = (LinearLayout) convertView.findViewById(R.id.ll_huikui);
            tv_huikui_zonge = (TextView) convertView.findViewById(R.id.tv_huikui_zonge);
            tv_huikui_yue = (TextView) convertView.findViewById(R.id.tv_huikui_yue);
            tv_huikui_zhaunfa = (TextView) convertView.findViewById(R.id.tv_huikui_zhaunfa);
            tv_liulan_cishu = (TextView) convertView.findViewById(R.id.tv_liulan_cishu);
            tv_title_pl = (TextView) convertView.findViewById(R.id.tv_title_pl);
            image_shareRed = (ImageView) convertView.findViewById(R.id.image_shareRed);
            tv_comcount = (TextView) convertView.findViewById(R.id.tv_comcount);
            rl_comment = (RelativeLayout) convertView.findViewById(R.id.rl_comment);
            tv_recommend = (TextView) convertView.findViewById(R.id.tv_recommend);
            ll_one = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
            card = (LinearLayout) convertView.findViewById(R.id.card);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            tv_count_zhf = (TextView) convertView.findViewById(R.id.tv_count_zhf);
            tv_count_pl = (TextView) convertView.findViewById(R.id.tv_count_pl);
            rl_zhuanfa = (RelativeLayout) convertView.findViewById(R.id.rl_zhuanfa);
            rl_pinglun = (RelativeLayout) convertView.findViewById(R.id.rl_pinglun);
            rl_video = (RelativeLayout) convertView.findViewById(R.id.rl_video);
            rl_all_contain = (RelativeLayout) convertView.findViewById(R.id.rl_all_contain);
            rl_pre_click = (ImageView) convertView.findViewById(R.id.rl_pre_click);
            videoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            btn_xiadan = (Button) convertView.findViewById(R.id.btn_xiadan);
            btn_daohang = (Button) convertView.findViewById(R.id.btn_daohang);
            tv_demandType = (TextView) convertView.findViewById(R.id.tv_demandType);

            image_advert = (ImageView) convertView.findViewById(R.id.image_advert);

            authBtn1 = (TextView) convertView.findViewById(R.id.authBtn1);
            authBtn2 = (TextView) convertView.findViewById(R.id.authBtn2);
            authBtn3 = (TextView) convertView.findViewById(R.id.authBtn3);
            authBtn4 = (TextView) convertView.findViewById(R.id.authBtn4);
            authBtn5 = (TextView) convertView.findViewById(R.id.authBtn5);
            authBtn6 = (TextView) convertView.findViewById(R.id.authBtn6);


            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }
    }

    public class ViewHolderThrfzhf extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CircleImageView iv_avatar;
        CircleImageView iv_first_avatar;
        // 昵称
        TextView tvDistance;
        TextView tv_count_llc;
        TextView tv_xiaoliang;
        TextView tv_nick;
        TextView tv_first_nick;
        // 时间
        TextView tv_time;
        TextView tv_video;
        TextView tvTitleA;
        TextView tvfirst_TitleA;
        // 三行图片
        LinearLayout ll_one;
        LinearLayout ll_two;
        ImageView image_1;
        ImageView image_2;
        ImageView image_3;
        ImageView image_4;
        ImageView image_5;
        ImageView image_6;
        ImageView image_8;
        ImageView image_7;
        // 动态内容
        ExpandableTextView tv_zhf_content;
        TextView tv_content2;
        // 位置
        TextView tv_shpjg;
        TextView tv_shpyj;
        TextView tv_huikui_zonge;
        TextView tv_huikui_yue;
        TextView tv_huikui_zhaunfa;
        TextView tv_liulan_cishu;
        TextView tv_count_zhf;
        TextView tv_count_pl;
        TextView tvNianLing;
        TextView tvCompany;
        LinearLayout card;
        RelativeLayout rl_xiaoliang;
        RelativeLayout rl_huikui1;
        RelativeLayout rl_huikui2;
        RelativeLayout rl_zhuanfa;
        RelativeLayout rl_pinglun;
        RelativeLayout rl_video;
        JCVideoPlayerStandard videoPlayer;
        Button btn_xiadan;
        private MyItem3ClickListener mListener;
        private MyItem3LongClickListener mLongClickListener;

        public ViewHolderThrfzhf(View convertView, MyItem3ClickListener listener, MyItem3LongClickListener longClickListener) {
            super(convertView);
            tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
            tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            tv_count_llc = (TextView) convertView.findViewById(R.id.tv_count_llc);
            tv_xiaoliang = (TextView) convertView.findViewById(R.id.tv_xiaoliang);
            tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
            tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            tv_video = (TextView) convertView.findViewById(R.id.tv_video);
            tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            iv_avatar = (CircleImageView) convertView.findViewById(R.id.sdv_image);
            tv_first_nick = (TextView) convertView.findViewById(R.id.tv_first_nick);
            tvfirst_TitleA = (TextView) convertView.findViewById(R.id.tv_zhf_titl);
            iv_first_avatar = (CircleImageView) convertView.findViewById(R.id.sdv_first_image);
            tv_zhf_content = (ExpandableTextView) convertView.findViewById(R.id.tv_zhf_content);
            image_1 = (ImageView) convertView.findViewById(R.id.image_1);
            image_2 = (ImageView) convertView.findViewById(R.id.image_2);
            image_3 = (ImageView) convertView.findViewById(R.id.image_3);
            image_4 = (ImageView) convertView.findViewById(R.id.image_4);
            image_5 = (ImageView) convertView.findViewById(R.id.image_5);
            image_6 = (ImageView) convertView.findViewById(R.id.image_6);
            image_7 = (ImageView) convertView.findViewById(R.id.image_7);
            image_8 = (ImageView) convertView.findViewById(R.id.image_8);
            ll_one = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
            tv_content2 = (TextView) convertView.findViewById(R.id.tv_content);
            tv_shpjg = (TextView) convertView.findViewById(R.id.tv_shpjg);
            tv_shpyj = (TextView) convertView.findViewById(R.id.tv_shpyj);
            tv_huikui_zonge = (TextView) convertView.findViewById(R.id.tv_huikui_zonge);
            tv_huikui_yue = (TextView) convertView.findViewById(R.id.tv_huikui_yue);
            tv_huikui_zhaunfa = (TextView) convertView.findViewById(R.id.tv_huikui_zhaunfa);
            tv_liulan_cishu = (TextView) convertView.findViewById(R.id.tv_liulan_cishu);
            tv_count_zhf = (TextView) convertView.findViewById(R.id.tv_count_zhf);
            tv_count_pl = (TextView) convertView.findViewById(R.id.tv_count_pl);
            rl_xiaoliang = (RelativeLayout) convertView.findViewById(R.id.rl_xiaoliang);
            card = (LinearLayout) convertView.findViewById(R.id.card);
            rl_zhuanfa = (RelativeLayout) convertView.findViewById(R.id.rl_zhuanfa);
            rl_pinglun = (RelativeLayout) convertView.findViewById(R.id.rl_pinglun);
            rl_huikui1 = (RelativeLayout) convertView.findViewById(R.id.rl_huikui1);
            rl_huikui2 = (RelativeLayout) convertView.findViewById(R.id.rl_huikui2);
            rl_video = (RelativeLayout) convertView.findViewById(R.id.rl_video);
            videoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            btn_xiadan = (Button) convertView.findViewById(R.id.btn_xiadan);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }
    }

    public class ViewHolderThrf extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CircleImageView iv_avatar;
        // 昵称
        TextView tvDistance;
        TextView tv_count_llc;
        TextView tv_xiaoliang;
        TextView tv_nick;
        // 时间
        TextView tv_time;
        TextView tv_video;
        TextView tvTitleA;
        // 三行图片
        LinearLayout ll_one;
        LinearLayout ll_two;
        ImageView image_1;
        ImageView image_2;
        ImageView image_3;
        ImageView image_4;
        ImageView image_5;
        ImageView image_6;
        ImageView image_8;
        ImageView image_7;
        // 动态内容
        TextView tv_content;
        // 位置
        TextView tv_shpjg;
        TextView tv_shpyj;
        TextView tv_huikui_zonge;
        TextView tv_huikui_yue;
        TextView tv_huikui_zhaunfa;
        TextView tv_liulan_cishu;
        TextView tv_count_zhf;
        TextView tv_count_pl;
        TextView tvNianLing;
        TextView tvCompany;
        RelativeLayout rl_xiaoliang;
        RelativeLayout rl_huikui1;
        RelativeLayout rl_huikui2;
        RelativeLayout rl_zhuanfa;
        RelativeLayout rl_pinglun;
        RelativeLayout rl_video;
        JCVideoPlayerStandard videoPlayer;
        LinearLayout card;
        Button btn_xiadan;
        Button btn_right;//右边的线下价 按导航处理
        ImageView image_redpage;//红包点击

        RelativeLayout rl_collection;
        TextView tv_collectioncount;
        TextView tv_collectiontitle;

        TextView tv_marginLabel;
        TextView tv_vipLabel;
        TextView tv_messageLabel;
        ImageView image_advert;

        private MyItem3ClickListener mListener;
        private MyItem3LongClickListener mLongClickListener;

        TextView authBtn1;
        TextView authBtn2;
        TextView authBtn3;

        public ViewHolderThrf(View convertView, MyItem3ClickListener listener, MyItem3LongClickListener longClickListener) {
            super(convertView);

            authBtn1 = (TextView) convertView.findViewById(R.id.authBtn1);
            authBtn2 = (TextView) convertView.findViewById(R.id.authBtn2);
            authBtn3 = (TextView) convertView.findViewById(R.id.authBtn3);

            tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
            tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            tv_count_llc = (TextView) convertView.findViewById(R.id.tv_count_llc);
            tv_xiaoliang = (TextView) convertView.findViewById(R.id.tv_xiaoliang);
            tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
            tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            tv_video = (TextView) convertView.findViewById(R.id.tv_video);
            tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            iv_avatar = (CircleImageView) convertView.findViewById(R.id.sdv_image);
            image_1 = (ImageView) convertView.findViewById(R.id.image_1);
            image_2 = (ImageView) convertView.findViewById(R.id.image_2);
            image_3 = (ImageView) convertView.findViewById(R.id.image_3);
            image_4 = (ImageView) convertView.findViewById(R.id.image_4);
            image_5 = (ImageView) convertView.findViewById(R.id.image_5);
            image_6 = (ImageView) convertView.findViewById(R.id.image_6);
            image_7 = (ImageView) convertView.findViewById(R.id.image_7);
            image_8 = (ImageView) convertView.findViewById(R.id.image_8);
            ll_one = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            tv_shpjg = (TextView) convertView.findViewById(R.id.tv_price1);
            tv_shpyj = (TextView) convertView.findViewById(R.id.tv_price2);
            tv_huikui_zonge = (TextView) convertView.findViewById(R.id.tv_huikui_zonge);
            tv_huikui_yue = (TextView) convertView.findViewById(R.id.tv_huikui_yue);
            tv_huikui_zhaunfa = (TextView) convertView.findViewById(R.id.tv_huikui_zhaunfa);
            tv_liulan_cishu = (TextView) convertView.findViewById(R.id.tv_redcount);
            btn_right = (Button) convertView.findViewById(R.id.bt_right);
            tv_count_zhf = (TextView) convertView.findViewById(R.id.tv_count_zhf);
            tv_count_pl = (TextView) convertView.findViewById(R.id.tv_count_pl);
            rl_xiaoliang = (RelativeLayout) convertView.findViewById(R.id.rl_xiaoliang);
            card = (LinearLayout) convertView.findViewById(R.id.card);
            rl_zhuanfa = (RelativeLayout) convertView.findViewById(R.id.rl_zhuanfa);
            rl_pinglun = (RelativeLayout) convertView.findViewById(R.id.rl_pinglun);
            rl_huikui1 = (RelativeLayout) convertView.findViewById(R.id.rl_huikui1);
            rl_huikui2 = (RelativeLayout) convertView.findViewById(R.id.rl_huikui2);
            rl_video = (RelativeLayout) convertView.findViewById(R.id.rl_video);
            videoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            btn_xiadan = (Button) convertView.findViewById(R.id.bt_left);
            image_redpage = (ImageView) convertView.findViewById(R.id.image_redpage);

            rl_collection = (RelativeLayout) convertView.findViewById(R.id.rl_collection);
            tv_collectioncount = (TextView) convertView.findViewById(R.id.tv_collectioncount);
            tv_collectiontitle = (TextView) convertView.findViewById(R.id.tv_collectiontitle);

            tv_marginLabel = (TextView) convertView.findViewById(R.id.tv_marginLabel);
            tv_vipLabel = (TextView) convertView.findViewById(R.id.tv_vipLabel);
            tv_messageLabel = (TextView) convertView.findViewById(R.id.tv_messageLabel);

            image_advert = (ImageView) convertView.findViewById(R.id.image_advert);

            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }
    }

    public class ViewHolderTwozhf extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CircleImageView iv_avatar;
        CircleImageView iv_first_avatar;
        // 昵称
        TextView tvDistance;
        TextView tv_count_llc;
        TextView tv_nick;
        TextView tv_first_nick;
        // 时间
        TextView tv_time;
        TextView tv_video;
        TextView tvTitleA;
        TextView tvfirst_TitleA;
        // 三行图片
        LinearLayout ll_one;
        LinearLayout ll_two;
        ImageView image_1;
        ImageView image_2;
        ImageView image_3;
        ImageView image_4;
        ImageView image_5;
        ImageView image_6;
        ImageView image_8;
        ImageView image_7;
        ImageView iv_ditu;
        ImageView iv_bz_type;
        // 动态内容
        TextView tv_count_zhf;
        TextView tv_count_pl;
        ExpandableTextView tv_zhf_content;
        TextView tv_content2;
        // 位置
        TextView tv_location;
        TextView tvNianLing;
        TextView tvCompany;
        TextView tv_geshu;
        RelativeLayout card;
        RelativeLayout rl_geshu;
        RelativeLayout rl_neirong;
        RelativeLayout rl_zhuanfa;
        RelativeLayout rl_pinglun;
        RelativeLayout rl_video;
        JCVideoPlayerStandard videoPlayer;
        TextView mMapView;
        TextView tv_collectiontitle;

        private MyItem2ClickListener mListener;
        private MyItem2LongClickListener mLongClickListener;

        public ViewHolderTwozhf(View convertView, MyItem2ClickListener listener, MyItem2LongClickListener longClickListener) {
            super(convertView);
            tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
            tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            tv_geshu = (TextView) convertView.findViewById(R.id.tv_geshu);
            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            tv_count_llc = (TextView) convertView.findViewById(R.id.tv_count_llc);
            mMapView = (TextView) convertView.findViewById(R.id.bmapView2);
            tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
            tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            tv_video = (TextView) convertView.findViewById(R.id.tv_video);
            tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            card = (RelativeLayout) convertView.findViewById(R.id.card);
            rl_geshu = (RelativeLayout) convertView.findViewById(R.id.rl_geshu);
            rl_neirong = (RelativeLayout) convertView.findViewById(R.id.rl_neirong);
            rl_zhuanfa = (RelativeLayout) convertView.findViewById(R.id.rl_zhuanfa);
            rl_pinglun = (RelativeLayout) convertView.findViewById(R.id.rl_pinglun);
            rl_video = (RelativeLayout) convertView.findViewById(R.id.rl_video);
            videoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            iv_avatar = (CircleImageView) convertView.findViewById(R.id.sdv_image);
            tv_count_pl = (TextView) convertView.findViewById(R.id.tv_count_pl);
            tv_count_zhf = (TextView) convertView.findViewById(R.id.tv_count_zhf);
            tv_first_nick = (TextView) convertView.findViewById(R.id.tv_first_nick);
            tvfirst_TitleA = (TextView) convertView.findViewById(R.id.tv_zhf_titl);
            iv_first_avatar = (CircleImageView) convertView.findViewById(R.id.sdv_first_image);
            tv_zhf_content = (ExpandableTextView) convertView.findViewById(R.id.tv_zhf_content);
            image_1 = (ImageView) convertView.findViewById(R.id.image_1);
            image_2 = (ImageView) convertView.findViewById(R.id.image_2);
            image_3 = (ImageView) convertView.findViewById(R.id.image_3);
            image_4 = (ImageView) convertView.findViewById(R.id.image_4);
            image_5 = (ImageView) convertView.findViewById(R.id.image_5);
            image_6 = (ImageView) convertView.findViewById(R.id.image_6);
            image_7 = (ImageView) convertView.findViewById(R.id.image_7);
            image_8 = (ImageView) convertView.findViewById(R.id.image_8);
            iv_ditu = (ImageView) convertView.findViewById(R.id.iv_ditu);
            iv_bz_type = (ImageView) convertView.findViewById(R.id.iv_bz_type);
            ll_one = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
            tv_content2 = (TextView) convertView.findViewById(R.id.tv_content);
            tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            tv_collectiontitle = (TextView) convertView.findViewById(R.id.tv_collectiontitle);

            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }
    }

    public class ViewHolderTwo extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CircleImageView iv_avatar;
        // 昵称
        TextView tvDistance;
        TextView tv_count_llc;
        TextView tv_nick;
        // 时间
        TextView tv_time;
        TextView tv_video;
        TextView tvTitleA;
        TextView tvNianLing;
        TextView tvCompany;
        // 三行图片
        LinearLayout ll_one;
        LinearLayout ll_two;
        ImageView image_1;
        ImageView image_2;
        ImageView image_3;
        ImageView image_4;
        ImageView image_5;
        ImageView image_6;
        ImageView image_8;
        ImageView image_7;
        ImageView iv_ditu;
        ImageView iv_bz_type;
        // 动态内容
        TextView tv_count_pl;
        TextView tv_count_zhf;
        TextView tv_geshu;
        TextView tv_content;
        // 位置
        TextView tv_location;
        LinearLayout card;
        RelativeLayout rl_geshu;
        RelativeLayout rl_neirong;
        RelativeLayout rl_zhuanfa;
        RelativeLayout rl_pinglun;
        RelativeLayout rl_video;
        RelativeLayout rl_collection;
        TextView tv_collectioncount;
        JCVideoPlayerStandard videoPlayer;
        TextView mMapView;
        TextView tv_collectiontitle;
        ImageView image_advert;
        private MyItem2ClickListener mListener;
        private MyItem2LongClickListener mLongClickListener;

        TextView tv_demandType;
        TextView authBtn1;
        TextView authBtn2;
        TextView authBtn3;

        public ViewHolderTwo(View convertView, MyItem2ClickListener listener, MyItem2LongClickListener longClickListener) {
            super(convertView);

            authBtn1 = (TextView) convertView.findViewById(R.id.authBtn1);
            authBtn2 = (TextView) convertView.findViewById(R.id.authBtn2);
            authBtn3 = (TextView) convertView.findViewById(R.id.authBtn3);

            tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
            tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            tv_count_llc = (TextView) convertView.findViewById(R.id.tv_count_llc);
            mMapView = (TextView) convertView.findViewById(R.id.bmapView2);
            tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
            tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            tv_video = (TextView) convertView.findViewById(R.id.tv_video);
            tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            iv_avatar = (CircleImageView) convertView.findViewById(R.id.sdv_image);
            card = (LinearLayout) convertView.findViewById(R.id.card);
            rl_geshu = (RelativeLayout) convertView.findViewById(R.id.rl_geshu);
            rl_neirong = (RelativeLayout) convertView.findViewById(R.id.rl_neirong);
            rl_zhuanfa = (RelativeLayout) convertView.findViewById(R.id.rl_zhuanfa);
            rl_pinglun = (RelativeLayout) convertView.findViewById(R.id.rl_pinglun);
            rl_video = (RelativeLayout) convertView.findViewById(R.id.rl_video);
            videoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            tv_count_pl = (TextView) convertView.findViewById(R.id.tv_count_pl);
            image_1 = (ImageView) convertView.findViewById(R.id.image_1);
            image_2 = (ImageView) convertView.findViewById(R.id.image_2);
            image_3 = (ImageView) convertView.findViewById(R.id.image_3);
            image_4 = (ImageView) convertView.findViewById(R.id.image_4);
            image_5 = (ImageView) convertView.findViewById(R.id.image_5);
            image_6 = (ImageView) convertView.findViewById(R.id.image_6);
            image_7 = (ImageView) convertView.findViewById(R.id.image_7);
            image_8 = (ImageView) convertView.findViewById(R.id.image_8);
            iv_ditu = (ImageView) convertView.findViewById(R.id.iv_ditu);
            iv_bz_type = (ImageView) convertView.findViewById(R.id.iv_bz_type);
            ll_one = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            tv_count_zhf = (TextView) convertView.findViewById(R.id.tv_count_zhf);
            tv_geshu = (TextView) convertView.findViewById(R.id.tv_geshu);
            tv_location = (TextView) convertView.findViewById(R.id.tv_location);

            image_advert = (ImageView) convertView.findViewById(R.id.image_advert);

            tv_demandType = (TextView) convertView.findViewById(R.id.tv_demandType);

            rl_collection = (RelativeLayout) convertView.findViewById(R.id.rl_collection);
            tv_collectioncount = (TextView) convertView.findViewById(R.id.tv_collectioncount);
            tv_collectiontitle = (TextView) convertView.findViewById(R.id.tv_collectiontitle);

            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }
    }

    public class ViewHolderOnezhf extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CircleImageView iv_avatar;
        CircleImageView iv_first_avatar;
        // 昵称
        TextView tvDistance;
        TextView tv_count_llc;
        TextView tv_nick;
        TextView tv_first_nick;
        // 时间
        TextView tv_time;
        TextView tv_video;
        TextView tvTitleA;
        TextView tvfirst_TitleA;
        TextView tvNianLing;
        TextView tvCompany;
        // 三行图片
        LinearLayout ll_one;
        LinearLayout ll_two;
        ImageView image_1;
        ImageView image_2;
        ImageView image_3;
        ImageView image_4;
        ImageView image_5;
        ImageView image_6;
        ImageView image_8;
        ImageView image_7;
        // 动态内容
        TextView tv_count_pl;
        TextView tv_count_zhf;
        ExpandableTextView tv_zhf_content;
        TextView tv_content2;
        // 位置
        TextView tv_location;
        RelativeLayout rl_zhuanfa;
        RelativeLayout rl_pinglun;
        RelativeLayout rl_video;
        JCVideoPlayerStandard videoPlayer;
        LinearLayout card;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public ViewHolderOnezhf(View convertView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(convertView);
            tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
            tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            tv_count_llc = (TextView) convertView.findViewById(R.id.tv_count_llc);
            tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
            tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            tv_video = (TextView) convertView.findViewById(R.id.tv_video);
            tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            rl_zhuanfa = (RelativeLayout) convertView.findViewById(R.id.rl_zhuanfa);
            rl_pinglun = (RelativeLayout) convertView.findViewById(R.id.rl_pinglun);
            rl_video = (RelativeLayout) convertView.findViewById(R.id.rl_video);
            videoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            card = (LinearLayout) convertView.findViewById(R.id.card);
            iv_avatar = (CircleImageView) convertView.findViewById(R.id.sdv_image);
            tv_count_pl = (TextView) convertView.findViewById(R.id.tv_count_pl);
            tv_count_zhf = (TextView) convertView.findViewById(R.id.tv_count_zhf);
            tv_first_nick = (TextView) convertView.findViewById(R.id.tv_first_nick);
            tvfirst_TitleA = (TextView) convertView.findViewById(R.id.tv_zhf_titl);
            iv_first_avatar = (CircleImageView) convertView.findViewById(R.id.sdv_first_image);
            tv_zhf_content = (ExpandableTextView) convertView.findViewById(R.id.tv_zhf_content);
            image_1 = (ImageView) convertView.findViewById(R.id.image_1);
            image_2 = (ImageView) convertView.findViewById(R.id.image_2);
            image_3 = (ImageView) convertView.findViewById(R.id.image_3);
            image_4 = (ImageView) convertView.findViewById(R.id.image_4);
            image_5 = (ImageView) convertView.findViewById(R.id.image_5);
            image_6 = (ImageView) convertView.findViewById(R.id.image_6);
            image_7 = (ImageView) convertView.findViewById(R.id.image_7);
            image_8 = (ImageView) convertView.findViewById(R.id.image_8);
            ll_one = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
            tv_content2 = (TextView) convertView.findViewById(R.id.tv_content);
            tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CircleImageView iv_avatar;
        // 昵称
        TextView tvDistance;
        TextView tv_nick;
        TextView tv_count_llc;
        // 时间
        TextView tv_time;
        TextView tv_video;
        TextView tvTitleA;
        // 三行图片
        LinearLayout ll_one;
        LinearLayout ll_two;
        ImageView image_1;
        ImageView image_2;
        ImageView image_3;
        ImageView image_4;
        ImageView image_5;
        ImageView image_6;
        ImageView image_8;
        ImageView image_7;
        // 动态内容
        TextView tv_count_pl;
        TextView tv_count_zhf;
        ExpandableTextView tv_content;
        // 位置
        TextView tv_location;
        TextView tvNianLing;
        TextView tvCompany;
        RelativeLayout rl_zhuanfa;
        RelativeLayout rl_pinglun;
        RelativeLayout rl_video;
        JCVideoPlayerStandard videoPlayer;
        private RelativeLayout card;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;
        ImageView image_advert;
        TextView authBtn1;
        TextView authBtn2;
        TextView authBtn3;

        public ViewHolderOne(View convertView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(convertView);

            authBtn1 = (TextView) convertView.findViewById(R.id.authBtn1);
            authBtn2 = (TextView) convertView.findViewById(R.id.authBtn2);
            authBtn3 = (TextView) convertView.findViewById(R.id.authBtn3);

            tvNianLing = (TextView) convertView.findViewById(R.id.tv_nianling);
            tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            tv_count_llc = (TextView) convertView.findViewById(R.id.tv_count_llc);
            tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
            tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            tv_video = (TextView) convertView.findViewById(R.id.tv_video);
            tvTitleA = (TextView) convertView.findViewById(R.id.tv_titl);
            tv_count_pl = (TextView) convertView.findViewById(R.id.tv_count_pl);
            tv_count_zhf = (TextView) convertView.findViewById(R.id.tv_count_zhf);
            iv_avatar = (CircleImageView) convertView.findViewById(R.id.sdv_image);
            card = (RelativeLayout) convertView.findViewById(R.id.card);
            rl_zhuanfa = (RelativeLayout) convertView.findViewById(R.id.rl_zhuanfa);
            rl_pinglun = (RelativeLayout) convertView.findViewById(R.id.rl_pinglun);
            rl_video = (RelativeLayout) convertView.findViewById(R.id.rl_video);
            videoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            image_1 = (ImageView) convertView.findViewById(R.id.image_1);
            image_2 = (ImageView) convertView.findViewById(R.id.image_2);
            image_3 = (ImageView) convertView.findViewById(R.id.image_3);
            image_4 = (ImageView) convertView.findViewById(R.id.image_4);
            image_5 = (ImageView) convertView.findViewById(R.id.image_5);
            image_6 = (ImageView) convertView.findViewById(R.id.image_6);
            image_7 = (ImageView) convertView.findViewById(R.id.image_7);
            image_8 = (ImageView) convertView.findViewById(R.id.image_8);
            ll_one = (LinearLayout) convertView.findViewById(R.id.ll_one);
            ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
            tv_content = (ExpandableTextView) convertView.findViewById(R.id.tv_content);
            tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            image_advert = (ImageView) convertView.findViewById(R.id.image_advert);

            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    public void setOnItem4ClickListener(MyItem4ClickListener listener) {
        this.mItem4ClickListener = listener;
    }

    public void setOnItem4LongClickListener(MyItem4LongClickListener listener) {
        this.mItem4LongClickListener = listener;
    }

    public void setOnItem5ClickListener(MyItem5ClickListener listener) {
        this.mItem5ClickListener = listener;
    }

    public void setOnItem5LongClickListener(MyItem5LongClickListener listener) {
        this.mItem5LongClickListener = listener;
    }

    public void setOnItem3ClickListener(MyItem3ClickListener listener) {
        this.mItem3ClickListener = listener;
    }

    public void setOnItem3LongClickListener(MyItem3LongClickListener listener) {
        this.mItem3LongClickListener = listener;
    }

    public void setOnItem2ClickListener(MyItem2ClickListener listener) {
        this.mItem2ClickListener = listener;
    }

    public void setOnItem2LongClickListener(MyItem2LongClickListener listener) {
        this.mItem2LongClickListener = listener;
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface MyItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public interface MyItem2ClickListener {
        void onItemClick(View view, int position);
    }

    public interface MyItem2LongClickListener {
        void onItemLongClick(View view, int position);
    }

    public interface MyItem3ClickListener {
        void onItemClick(View view, int position);
    }

    public interface MyItem3LongClickListener {
        void onItemLongClick(View view, int position);
    }

    public interface MyItem4ClickListener {
        void onItemClick(View view, int position);
    }

    public interface MyItem4LongClickListener {
        void onItemLongClick(View view, int position);
    }

    public interface MyItem5ClickListener {
        void onItemClick(View view, int position);
    }

    public interface MyItem5LongClickListener {
        void onItemLongClick(View view, int position);
    }

    private void showDialog(final String sID, final String resv4, final String zy1, final String distance, final String balance, final String rel_time, final String dynamicSeq) {
        LayoutInflater inflaterDl = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        View v2 = dialog.findViewById(R.id.v2);
        v2.setVisibility(View.GONE);
        re_item2.setVisibility(View.GONE);
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDetail = "01";
                addToDingdan(DemoHelper.getInstance().getCurrentUsernName(), sID, zy1, typeDetail, resv4, distance, balance, rel_time, dynamicSeq);
                dialog.dismiss();
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDetail = "02";
                addToDingdan(DemoHelper.getInstance().getCurrentUsernName(), sID, zy1, typeDetail, resv4, distance, balance, rel_time, dynamicSeq);
                dialog.dismiss();
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void addToDingdan(final String wodezhanghao, final String hxid, final String zy1, final String typeDetail, String zyType, final String distance, final String balance, final String rel_time, final String dynamicSeq) {

        String pass = DemoApplication.getApp().getCurrentPayPass();
        zyType = TextUtils.isEmpty(zyType) ? "01" : zyType;

        if (wodezhanghao.equals(hxid)) {
            Toast.makeText(context, "不能给自己下单！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (zyType.equals("01")) {

            Intent intent = new Intent(context, UOrderDetailActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("createTime", rel_time);
            intent.putExtra("dynamicSeq", dynamicSeq);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", zy1);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "01");
            intent.putExtra("balance", balance);
            context.startActivityForResult(intent, 0);

        } else if (zyType.equals("02")) {

            Intent intent = new Intent(context, UOrderDetailTwoActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("createTime", rel_time);
            intent.putExtra("dynamicSeq", dynamicSeq);
            intent.putExtra("distance", distance);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", zy1);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "01");
            intent.putExtra("balance", balance);
            context.startActivityForResult(intent, 0);

        } else if (zyType.equals("03")) {

            Intent intent = new Intent(context, UOrderDetailThreeActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("createTime", rel_time);
            intent.putExtra("dynamicSeq", dynamicSeq);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", zy1);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "01");
            intent.putExtra("balance", balance);
            context.startActivityForResult(intent, 0);

        } else if (zyType.equals("04")) {

            Intent intent = new Intent(context, UOrderDetailFourActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("createTime", rel_time);
            intent.putExtra("dynamicSeq", dynamicSeq);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", zy1);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "01");
            intent.putExtra("balance", balance);
            context.startActivityForResult(intent, 0);

        } else if (zyType.equals("05")) {

            Intent intent = new Intent(context, UOrderDetailFiveActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("createTime", rel_time);
            intent.putExtra("dynamicSeq", dynamicSeq);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", zy1);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "01");
            intent.putExtra("balance", balance);
            context.startActivityForResult(intent, 0);

        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (users == null || users.size() == 0) ? 0 : users.size();
    }

    class ImageListener implements OnClickListener {
        String[] images;
        String createTime;
        String dynamicSeq;
        String sID;
        int page;

        public ImageListener(String[] images, int page, String createTime, String dynamicSeq, String sID) {
            this.dynamicSeq = dynamicSeq;
            this.createTime = createTime;
            this.sID = sID;
            this.images = images;
            this.page = page;
        }

        @Override
        public void onClick(View v) {
            updateLiulancishu(createTime, dynamicSeq);
            if (dType.equals("01") || dType.equals("02") || dType.equals("03")) {
                updateDeLiulancishu(createTime, sID);
            }
            Intent intent = new Intent(context, BigImageActivity.class);
            intent.putExtra("images", images);
            intent.putExtra("page", page);
            intent.putExtra("biaoshi", "13");
            context.startActivityForResult(intent, 0);
        }
    }

    private void updateDeLiulancishu(final String createTime, final String loginId) {
        String url = FXConstant.URL_ADD_USERCISHU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Userdetailac,add", "增加浏览次数成功" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError != null) {
                    Log.e("Userdetailac,add", "增加浏览次数错误");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("uLoginId", loginId);
                param.put("timestamp", createTime);
                if (dType.equals("01") || dType.equals("05")) {
                    param.put("lifeDynamics", "1");
                } else if (dType.equals("02")) {
                    param.put("locationDynamics", "1");
                } else if (dType.equals("03")) {
                    param.put("businessDynamics", "1");
                }
                param.put("v_id", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void updateTJzhuanfa(final String loginId, final int type) {
        String url = FXConstant.URL_TONGJI_ZHUANFA;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Userdetailac,add", "增加浏览次数成功" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError != null) {
                    Log.e("Userdetailac,add", "增加浏览次数错误");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("uLoginId", loginId);
                if ("01".equals(dType) || "05".equals(dType)) {
                    param.put("lifeDynamics", "1");
                    if (type == 0) {
                        param.put("type", "qqLifeDynamic");
                    } else if (type == 1) {
                        param.put("type", "weixinLifeDynamic");
                    } else if (type == 2) {
                        param.put("type", "weiboLifeDynamic");
                    }
                } else if ("02".equals(dType)) {
                    param.put("locationDynamics", "1");
                    if (type == 0) {
                        param.put("type", "qqLocationDynamic");
                    } else if (type == 1) {
                        param.put("type", "weixinLocationDynamic");
                    } else if (type == 2) {
                        param.put("type", "weiboLocationDynamic");
                    }
                } else if ("03".equals(dType)) {
                    param.put("businessDynamics", "1");
                    if (type == 0) {
                        param.put("type", "qqBusinessDynamic");
                    } else if (type == 1) {
                        param.put("type", "weixinBusinessDynamic");
                    } else if (type == 2) {
                        param.put("type", "weiboLocationDynamic");
                    }
                }
                param.put("f_id", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void updateLiulancishu(final String createTime, final String dynamicSeq) {
        String url = FXConstant.URL_TONGJI_LIULANCISHU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("socialmainada", "成功,s=" + s);
                updateTJLiulancishu("浏览", createTime, dynamicSeq, "00");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("socialmainada", "失败,e=" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("createTime", createTime);
                param.put("dynamicSeq", dynamicSeq);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }


    private void updateTJLiulancishu(final String type, final String createTime, final String dynamicSeq, final String pushId) {
        String url = FXConstant.URL_JILU_LIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("chen", "成功,updateTJLiulancishus=" + s);
                sendPushMessage("00", pushId, createTime, dynamicSeq, type);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("chen", "失败,updateTJLiulancishu=" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("create_time", createTime);
                param.put("dynamic_seq", dynamicSeq);
                param.put("timestamp", getNowTime());
                param.put("type", type);
                param.put("v_id", DemoHelper.getInstance().getCurrentUsernName());
                if (dType.equalsIgnoreCase("05")) {
                    if (TextUtils.isEmpty(responseTime) || responseTime.equalsIgnoreCase("0")) {
                        String time = getMin(createTime);
                        Log.d("chen", "增加浏览time" + time);
                        if (!TextUtils.isEmpty(time)) {
                            param.put("responseTime", time);
                        }
                    }
                    if (TextUtils.isEmpty(firstDistance) || firstDistance.equalsIgnoreCase("0")) {
                        String distance = getDistance(resv2, resv1);
                        Log.d("chen", "增加浏览distance" + distance);
                        if (!TextUtils.isEmpty(distance)) {
                            param.put("distance", distance);
                        }
                    }
                }
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private String getDistance(String latS, String lngS) {
        String paidanLat = latS;
        String paidanLng = lngS;
        //4.9E-324  考虑到百度定位出错时 就不显示
        if (!(TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) || TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) || TextUtils.isEmpty(paidanLat) || TextUtils.isEmpty(paidanLng) ||
                paidanLat.equalsIgnoreCase("4.9E-324") || paidanLng.equalsIgnoreCase("4.9E-324"))) {
            double latitude1 = Double.valueOf(DemoApplication.getInstance().getCurrentLat());
            double longitude1 = Double.valueOf(DemoApplication.getInstance().getCurrentLng());
            final LatLng ll1 = new LatLng(Double.parseDouble(paidanLat), Double.parseDouble(paidanLng));
            LatLng ll = new LatLng(latitude1, longitude1);
            double distance = DistanceUtil.getDistance(ll, ll1);
            double dou = distance / 1000;
            String str = String.format("%.1f", dou);//format 返回的是字符串
            return str;
        } else {
            return null;
        }
    }


    private String getMin(String s) {
        if (s == null || TextUtils.isEmpty(s)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        long spaceTime = 0;
        try {
            Date date = format.parse(s);
            spaceTime = System.currentTimeMillis() - date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        double dou = (spaceTime / 1000.00) / 60;
        String str = String.format("%.1f", dou);//format 返回的是字符串
        return str;

    }

    /**
     * 显示发表评论的输入框
     */
    public void showCommentEditText(final String firstid, final String pushId, final String sID, final String sName, final String dynamicId, final String creatTime, final TextView tv_count, final int count) {
        if (!DemoHelper.getInstance().isLoggedIn(context)) {
            Toast.makeText(context, "登陆后才可以评论哦！", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(context, DynaDetaActivity.class);
        intent.putExtra("sID", sID);
        intent.putExtra("dynamicSeq", dynamicId);
        intent.putExtra("createTime", creatTime);
        intent.putExtra("dType", dType);
        intent.putExtra("type", "01");
        intent.putExtra("type2", "00");
        context.startActivityForResult(intent, 0);

/*
        if (re_edittext == null || re_edittext.getVisibility() != View.VISIBLE) {
            re_edittext = (RelativeLayout) context.findViewById(R.id.re_edittext);
            re_edittext.setVisibility(View.VISIBLE);
            final EditText et_comment = (EditText) re_edittext.findViewById(R.id.et_comment);
            Button btn_send = (Button) re_edittext.findViewById(R.id.btn_send);
            btn_send.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = et_comment.getText().toString().trim();
                    if (TextUtils.isEmpty(comment)) {
                        Toast.makeText(context, "请输入评论", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.e("social,pinglun","开始");
                    submitComment(firstid,pushId,sID, comment, sName, dynamicId,creatTime,tv_count,count);
                    et_comment.setText("");
                    hideCommentEditText();
                }
            });
        }
        */
    }

    /**
     * 提交评论
     */
    private void submitComment(final String firstid, final String pushId, final String sID, final String comment, String sName, final String dynamicId, final String createTime, final TextView tv_count, int count) {
        if (sName == null || "".equals(sName)) {
            sName = DemoApplication.getInstance().getCurrentUser().getName();
        }
        // 更新后台
        if (sName == null || "".equals(sName)) {
            sName = sID;
        }
        final int[] i = {count};
        String url = FXConstant.URL_INSERT_DYNACOMMENT;
        final String finalSName = sName;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    String code = object.getString("code");
                    if ("success".equals(code)) {
                        sendPushMessage(firstid, pushId, createTime, dynamicId, "评论");
                        addPinglinCount(createTime, dynamicId);
                        Toast.makeText(context, "评论成功！", Toast.LENGTH_SHORT).show();
                        i[0]++;
                        tv_count.setText(i[0] + "");
                    } else {
                        Toast.makeText(context, "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                        Log.e("socialMainAdapter", s);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "网络连接错误，评论失败！", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("userId", sID);
                param.put("dynamicId", dynamicId);
                param.put("createTime", createTime);
                param.put("content", comment);
                param.put("userName", finalSName);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void sendPushMessage(final String firstid, final String hxid1, final String createTime, final String dynamicId, final String type) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                if (type.equals("转发")) {

                    param.put("type", "21");
                    param.put("body", "有用户分享了您的动态");

                } else {
                    param.put("type", "09");
                    param.put("body", "评论消息");
                }

                param.put("u_id", hxid1);
                param.put("userId", myId);
                param.put("companyId", "0");
                param.put("companyName", "0");
                param.put("companyAdress", "0");
                param.put("dynamicSeq", dynamicId);
                param.put("createTime", createTime);
                param.put("fristId", firstid);
                param.put("dType", dType);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * 隐藏发表评论的输入框
     */
    public void hideCommentEditText() {
        if (re_edittext != null && re_edittext.getVisibility() == View.VISIBLE && context != null) {
            re_edittext.setVisibility(View.GONE);
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
