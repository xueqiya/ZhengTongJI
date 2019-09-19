package com.sangu.apptongji.main.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanxin.easeui.EaseConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.DynamicRecommendActivity;
import com.sangu.apptongji.main.activity.LoginActivity;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuFLocationActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFiveActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFourActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailTwoActivity;
import com.sangu.apptongji.main.moments.BigImageActivity;
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.utils.BitmapUtils;
import com.sangu.apptongji.main.widget.CenterShowHorizontalScrollView;
import com.sangu.apptongji.ui.ChatActivity;
import com.sangu.apptongji.widget.CircleImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2017-12-18.
 */

public class PushDynaAdapter extends RecyclerView.Adapter<PushDynaAdapter.ViewHolder> {
    public MyItemClickListener mItemClickListener;
    public MyItemLongClickListener mItemLongClickListener;
    private List<JSONObject> users=null;
    private Context context;

    public PushDynaAdapter(Context context, List<JSONObject> jsonArray) {
        this.context = context;
        this.users = jsonArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item5_social_main, parent, false);
        return new ViewHolder(v,mItemClickListener,mItemLongClickListener);
    }

    private String subString(String task_locaName) {
        String str = task_locaName;
        if (str.contains("区")||str.contains("县")){
            int i2;
            if (str.contains("区")) {
                i2 = task_locaName.indexOf("区");
            } else {
                i2 = task_locaName.indexOf("县");
            }
            if (str.contains("市")){
                if (str.contains("省")) {
                    int i1 = task_locaName.indexOf("省");
                    str = task_locaName.substring(i1 + 1, i2 + 1);
                }else {
                    str = task_locaName.substring(0, i2 + 1);
                }
            }else {
                str = task_locaName.substring(0, i2 + 1);
            }
        }else {
            if (str.contains("市")){
                if (str.contains("省")) {
                    int i1 = task_locaName.indexOf("省");
                    int i2 = task_locaName.indexOf("市");
                    str = task_locaName.substring(i1 + 1,i2 + 1);
                }else {
                    int i2 = task_locaName.indexOf("市");
                    str = task_locaName.substring(0, i2 + 1);
                }
            }else {
                if (str.contains("省")) {
                    int i1 = task_locaName.indexOf("省");
                    str = task_locaName.substring(i1+1,str.length());
                }else {
                    str= task_locaName;
                }
            }
        }
        return str;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final JSONObject json = users.get(position);
        // 如果数据出错....



        if (json == null || json.size() == 0) {
            users.remove(position);
            this.notifyDataSetChanged();
        }
        String views = json.getString("views");
        if (views==null||"".equals(views)){
            views = "0";
        }
        if (views!=null&&Integer.valueOf(views)>999){
            views = "999+";
        }
        holder.tv_count_llc.setText(views);
        holder.rl_pre_click.setVisibility(View.GONE);
        final String userID = json.getString("uName");
        final String loginId = json.getString("uLoginId");
        final String sex1 = json.getString("uSex");
        final String content = "              【"+json.getString("task_label")+"】："+json.getString("content");
        String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
        String location = json.getString("location");
        String countPinglun = json.getString("resv7");
        final String task_label = json.getString("task_label");
        String task_position = json.getString("task_position");
        String task_locaName = json.getString("task_locaName");
        String task_jurisdiction = json.getString("task_jurisdiction");
        String recommend = json.getString("recommendCount");
        String ordercomment = json.getString("orderCommentCount");
        String demandType = json.getString("demandType");
        if (demandType.equals("1")){

            holder.tv_demandType.setText("需要产品");
            holder.tv_demandType.setBackgroundColor(Color.parseColor("#FF8D00"));

        }else if (demandType.equals("2")){

            holder.tv_demandType.setText("需要方案");
            holder.tv_demandType.setBackgroundColor(Color.parseColor("#46c01b"));

        }else if (demandType.equals("3")){

            holder.tv_demandType.setText("工程招标");
            holder.tv_demandType.setBackgroundColor(Color.parseColor("#FF0000"));

        }else {

            holder.tv_demandType.setText("需要服务");
            holder.tv_demandType.setBackgroundColor(Color.parseColor("#3EC5FF"));

        }
        final String video = json.getString("video");
        final String videoPictures = json.getString("videoPictures");
        if (task_locaName==null||"".equals(task_locaName)||task_locaName.equalsIgnoreCase("null")){
            holder.tv_location.setText("地点错误，建议主动联系用户沟通");
        }else {
            task_locaName = subString(task_locaName);
            final String lat = task_position.split("\\|")[0];
            final String lng = task_position.split("\\|")[1];
            holder.tv_location.setText(task_locaName);
            holder.tv_location.setOnClickListener(new View.OnClickListener() {
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
                    intent.putExtra("biaoshi","导航");
                    context.startActivity(intent);
                }
            });
            holder.btn_daohang.setOnClickListener(new View.OnClickListener() {
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
                    intent.putExtra("biaoshi","导航");
                    context.startActivity(intent);
                }
            });
        }

        final String sum = json.getString("sum");
        if (sum != null) {
            String balance = TextUtils.isEmpty(json.getString("redBalance"))?"0":json.getString("redBalance");
            double redBalance = Double.valueOf(balance);

            if (redBalance > 0){

                holder.image_shareRed.setVisibility(View.VISIBLE);

            }else {
                holder.image_shareRed.setVisibility(View.INVISIBLE);
            }

//                double oncePrice = Double.valueOf(json.getString("oncePrice"));
//                int cishu = (int) ((redBalance*100)/(oncePrice*100));
//                ((ViewHolderFive) holder).ll_huikui.setVisibility(View.VISIBLE);
//                ((ViewHolderFive) holder).tv_huikui_zonge.setText("￥" + sum);
//                ((ViewHolderFive) holder).tv_huikui_yue.setText("￥" + redBalance);
//                ((ViewHolderFive) holder).tv_huikui_zhaunfa.setText("￥" + oncePrice);
//                ((ViewHolderFive) holder).tv_liulan_cishu.setText(cishu + "个");

        } else {
            holder.image_shareRed.setVisibility(View.INVISIBLE);
        }

        if (countPinglun==null||"".equals(countPinglun)){
            countPinglun = "0";
        }
        if (countPinglun!=null&&Integer.valueOf(countPinglun)>999){
            countPinglun = "999+";
        }
        holder.tv_count_pl.setText(countPinglun);
        String forwardTimes = json.getString("forwardTimes");
        if (forwardTimes==null||"".equals(forwardTimes)){
            forwardTimes = "0";
        }
        if (forwardTimes!=null&&Integer.valueOf(forwardTimes)>999){
            forwardTimes = "999+";
        }
        holder.tv_count_zhf.setText(forwardTimes);
        final String sID = json.getString("uLoginId");
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

        if (Integer.valueOf(recommend) == 0){

            holder.tv_recommend.setVisibility(View.INVISIBLE);
            holder.tv_recommend.setText("");

        }else {

            holder.tv_recommend.setVisibility(View.VISIBLE);

            if (Integer.valueOf(recommend) > 99){

                holder.tv_recommend.setText("推荐 99+");

            }else {

                holder.tv_recommend.setText("推荐 "+Integer.valueOf(recommend));

            }

            holder.tv_recommend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context,DynamicRecommendActivity.class).putExtra("dynamicSeq",dynamicSeq).putExtra("createTime",rel_time));

                }
            });

        }

        if (Integer.valueOf(ordercomment) == 0){

            holder.tv_comcount.setText("0");

        }else {

            if (Integer.valueOf(ordercomment) > 99) {

                holder.tv_comcount.setText("99+");

            }else {

                holder.tv_comcount.setText(""+Integer.valueOf(ordercomment));

            }

        }

        if (floorPrice==null||Double.parseDouble(floorPrice)<=0){
            holder.tv_chujia.setVisibility(View.INVISIBLE);
        }else {
            holder.tv_chujia.setVisibility(View.VISIBLE);
            holder.tv_chujia.setText("意向出价： " + floorPrice+"元");
        }
        if (!task_jurisdiction.equals("01") && !task_jurisdiction.equals("02")){

             holder.tvCompany.setTextColor(Color.parseColor("#FFA500"));
            holder.tvCompany.setText("("+task_jurisdiction+")");

        }else {

            holder.tvCompany.setTextColor(Color.parseColor("#AAAAAA"));
            holder.tvCompany.setText("("+company+")");

        }
        holder.tvNianLing.setText(nianLing);
        if ("00".equals(sex)){
            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
            holder.tvNianLing.setBackgroundColor(Color.rgb(234,121,219));
        }else {
            holder.tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
            holder.tvNianLing.setBackgroundResource(R.color.accent_blue);
        }
        final String fromUId = sID;
        if (avatar.length() > 40) {
            String[] orderProjectArray = avatar.split("\\|");
            avatar = orderProjectArray[0];
        }
        if (!(avatar == "")) {
            holder.iv_avatar.setVisibility(View.VISIBLE);
            holder.tvTitleA.setVisibility(View.INVISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+avatar,holder.iv_avatar, DemoApplication.mOptions);
        } else {
            holder.iv_avatar.setVisibility(View.INVISIBLE);
            holder.tvTitleA.setVisibility(View.VISIBLE);
            holder.tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
        }
        holder.tv_nick.setText(userID);
        final String shareRed = json.getString("shareRed");
        final String friendsNumber = json.getString("friendsNumber");
        String exShareRed="无";
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            holder.tv_nick.setTextColor(Color.RED);
            exShareRed = "有";
        }else {
            holder.tv_nick.setTextColor(Color.rgb(87,107,149));
            exShareRed = "无";
        }
        holder.tv_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });
        holder.tvTitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,sID));
            }
        });
        String[] strloca=null;
        if (task_position!=null&&!"".equals(task_position)){
            strloca = task_position.split("\\|");
        }
        String resv2="",resv1="";
        if (strloca!=null&&strloca.length>0) {
            resv2 = strloca[0];
        }
        if (strloca!=null&&strloca.length>1) {
            resv1 = strloca[1];
        }
        double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
        double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());
        if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
            final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
            LatLng ll = new LatLng(latitude1,longitude1);
            double distance = DistanceUtil.getDistance(ll, ll1);
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
        String firstImage = "";
        if (!avatar.equals("")){
            firstImage = avatar.split("\\|")[0];
        }
        final String finalFirstImage = firstImage;
        String xiaoliang = TextUtils.isEmpty(json.getString("orderState")) ? "00" : json.getString("orderState");
        final ViewHolder finalHolder = holder;
        if (!DemoHelper.getInstance().isLoggedIn(context)){
            holder.btn_xiadan.setEnabled(true);
            holder.btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green);
            holder.btn_xiadan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            });
        }else {
            if ("00".equals(xiaoliang)||"01".equals(xiaoliang)||xiaoliang.equalsIgnoreCase("null")) {
                if (!sID.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                    if ("01".equals(xiaoliang)) {
                        holder.btn_xiadan.setText("进行中");
                        holder.btn_xiadan.setEnabled(true);
                        holder.btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_oriange);
                    }else {
                        holder.btn_xiadan.setText("接单报价");
                        holder.btn_xiadan.setEnabled(true);
                        holder.btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green);
                    }
                    final String finalExShareRed = exShareRed;
                    holder.btn_xiadan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
                                }
                            });
                            btn_commit.setOnClickListener(new View.OnClickListener() {
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
                                        context.startActivity(intent);
                                    }else if (clickPos[0]==1){
                                        Intent intent = new Intent(context, UOrderDetailTwoActivity.class);
                                        intent.putExtra("createTime", rel_time);
                                        intent.putExtra("dynamicSeq", dynamicSeq);
                                        intent.putExtra("task_label", task_label);
                                        intent.putExtra("typeDetail", "01");
                                        intent.putExtra("hxid", sID);
                                        intent.putExtra("biaoshi", "03");
                                        context.startActivity(intent);
                                    }else if (clickPos[0]==2){
                                        Intent intent = new Intent(context, UOrderDetailFourActivity.class);
                                        intent.putExtra("createTime", rel_time);
                                        intent.putExtra("dynamicSeq", dynamicSeq);
                                        intent.putExtra("task_label", task_label);
                                        intent.putExtra("typeDetail", "01");
                                        intent.putExtra("hxid", sID);
                                        intent.putExtra("biaoshi", "03");
                                        context.startActivity(intent);
                                    }else if (clickPos[0]==3){
                                        Intent intent = new Intent(context, UOrderDetailFiveActivity.class);
                                        intent.putExtra("createTime", rel_time);
                                        intent.putExtra("dynamicSeq", dynamicSeq);
                                        intent.putExtra("task_label", task_label);
                                        intent.putExtra("typeDetail", "01");
                                        intent.putExtra("hxid", sID);
                                        intent.putExtra("biaoshi", "03");
                                        context.startActivity(intent);
                                    }
                                }
                            });

                        }
                    });
                } else if (sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && DemoHelper.getInstance().isLoggedIn(context)) {
                    holder.btn_xiadan.setText("查看");
                    holder.btn_xiadan.setEnabled(true);
                    holder.btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green);
                    holder.btn_xiadan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String type;
                            if (json.getString("fromUId") != null&&!json.getString("fromUId").equalsIgnoreCase("null")) {
                                type = "02";
                            } else {
                                type = "01";
                            }

                            Intent intent = new Intent(context, DynaDetaActivity.class);
                            intent.putExtra("sID", sID);
                            intent.putExtra("dynamicSeq",dynamicSeq);
                            intent.putExtra("createTime",rel_time);
                            intent.putExtra("dType", "05");
                            intent.putExtra("type", type);
                            intent.putExtra("type2", "00");
                            context.startActivity(intent);
                        }
                    });
                }
            }else {
                holder.btn_xiadan.setText("交易完成");
                holder.btn_xiadan.setEnabled(false);
                holder.btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_gray);
            }
        }
        // 设置文章中的图片
        holder.image_1.setVisibility(View.GONE);
        holder.image_2.setVisibility(View.GONE);
        holder.image_3.setVisibility(View.GONE);
        holder.image_5.setVisibility(View.GONE);
        holder.image_4.setVisibility(View.GONE);
        holder.image_6.setVisibility(View.GONE);
        holder.image_7.setVisibility(View.GONE);
        holder.image_8.setVisibility(View.GONE);
        View v2 = null;
        if (video!=null&&videoPictures!=null){
            holder.rl_video.setVisibility(View.VISIBLE);
            boolean setUp = holder.videoPlayer.setUp(FXConstant.URL_VIDEO+video, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            v2 = holder.rl_video;
            if (setUp) {
                Glide.with(context).load(FXConstant.URL_VIDEO+videoPictures)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .error(R.drawable.default_error)
                        .crossFade().into(holder.videoPlayer.thumbImageView);
            }else {
                Toast.makeText(context,"视频播放失败",Toast.LENGTH_SHORT).show();
            }
            holder.tv_video.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }else {
            holder.rl_video.setVisibility(View.GONE);
            if (!imageStr.equals("")) {
                String[] images = imageStr.split("\\|");
                int imNumb = images.length;
                holder.image_1.setVisibility(View.VISIBLE);
                v2 = holder.image_1;
                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO+images[0],holder.image_1, DemoApplication.mOptions2);
                holder.image_1.setOnClickListener(new ImageListener(images, 0,rel_time, dynamicSeq,sID));
                if (imNumb > 1) {
                    holder.image_2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO+images[1],holder.image_2, DemoApplication.mOptions2);
                    holder.image_2.setOnClickListener(new ImageListener(images, 1,rel_time, dynamicSeq,sID));
                    if (imNumb > 2) {
                        holder.image_3.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO+images[2],holder.image_3, DemoApplication.mOptions2);
                        holder.image_3.setOnClickListener(new ImageListener(images, 2,rel_time, dynamicSeq,sID));
                        if (imNumb > 3) {
                            holder.image_4.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO+images[3],holder.image_4, DemoApplication.mOptions2);
                            holder.image_4.setOnClickListener(new ImageListener(images, 3,rel_time, dynamicSeq,sID));
                            if (imNumb > 4) {
                                holder.image_5.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO+images[4],holder.image_5, DemoApplication.mOptions2);
                                holder.image_5.setOnClickListener(new ImageListener(images, 4,rel_time, dynamicSeq,sID));
                                if (imNumb > 5) {
                                    holder.image_6.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[5], holder.image_6, DemoApplication.mOptions2);
                                    holder.image_6.setOnClickListener(new ImageListener(images, 5, rel_time, dynamicSeq,sID));
                                    if (imNumb > 6) {
                                        holder.image_7.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[6], holder.image_7, DemoApplication.mOptions2);
                                        holder.image_7.setOnClickListener(new ImageListener(images, 6, rel_time, dynamicSeq,sID));
                                        if (imNumb > 7) {
                                            holder.image_8.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[7], holder.image_8, DemoApplication.mOptions2);
                                            holder.image_8.setOnClickListener(new ImageListener(images, 7, rel_time, dynamicSeq,sID));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        holder.tv_content.setText(content);
        //holder.tv_content.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
        holder.tv_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String type;
                if (json.getString("fromUId") != null&&!json.getString("fromUId").equalsIgnoreCase("null")) {
                    type = "02";
                } else {
                    type = "01";
                }
                Intent intent = new Intent(context, DynaDetaActivity.class);
                intent.putExtra("sID", sID);
                intent.putExtra("dynamicSeq",dynamicSeq);
                intent.putExtra("createTime",rel_time);
                intent.putExtra("dType", "05");
                intent.putExtra("type", type);
                intent.putExtra("type2", "00");
                context.startActivity(intent);
                return true;
            }
        });
        String rel_time2 = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
        holder.tv_time.setText(rel_time2);
        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface MyItemClickListener {
        void onItemClick(View view,int position);
    }
    public interface MyItemLongClickListener {
        void onItemLongClick(View view,int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
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
        JCVideoPlayerStandard videoPlayer;
        Button btn_xiadan;
        Button btn_daohang;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        RelativeLayout rl_comment;
        TextView tv_comcount;
        TextView tv_demandType;
        ImageView rl_pre_click;

        public ViewHolder(View convertView,MyItemClickListener listener,MyItemLongClickListener longClickListener) {
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

            image_shareRed = (ImageView) convertView.findViewById(R.id.image_shareRed);
            tv_comcount = (TextView) convertView.findViewById(R.id.tv_comcount);
            rl_comment = (RelativeLayout) convertView.findViewById(R.id.rl_comment);
            tv_recommend = (TextView) convertView.findViewById(R.id.tv_recommend);
            rl_pre_click = (ImageView) convertView.findViewById(R.id.rl_pre_click);

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
            videoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            btn_xiadan = (Button) convertView.findViewById(R.id.btn_xiadan);
            btn_daohang = (Button) convertView.findViewById(R.id.btn_daohang);
            tv_demandType = (TextView) convertView.findViewById(R.id.tv_demandType);

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

    class ImageListener implements View.OnClickListener {
        String[] images;
        String createTime;
        String dynamicSeq;
        String sID;
        int page;
        public ImageListener(String[] images, int page,String createTime,String dynamicSeq,String sID) {
            this.dynamicSeq = dynamicSeq;
            this.createTime = createTime;
            this.sID = sID;
            this.images = images;
            this.page = page;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, BigImageActivity.class);
            intent.putExtra("images", images);
            intent.putExtra("page", page);
            intent.putExtra("biaoshi","13");
            context.startActivity(intent);
        }
    }

}
