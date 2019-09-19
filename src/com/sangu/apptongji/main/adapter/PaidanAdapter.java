package com.sangu.apptongji.main.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.JieDanSettingActivity;
import com.sangu.apptongji.main.activity.PaidanListActivity;
import com.sangu.apptongji.main.activity.SelectTradeUserActivity;
import com.sangu.apptongji.main.alluser.entity.MySendPaidanInfo;
import com.sangu.apptongji.main.alluser.entity.SendToMePaidan;
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.moments.ReviseXuQiuActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.main.widget.zxing.activity.DensityUtil;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.sangu.apptongji.widget.bubble.BubblePopupWindow;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2018-1-17.
 */

public class PaidanAdapter extends RecyclerView.Adapter<PaidanAdapter.ViewHolder> {

    private List<MySendPaidanInfo> users = new ArrayList<>();
    private List<SendToMePaidan> paidanList = new ArrayList<>();

    private Context context;
    private int type = 0;
    private Thread thread;
    private volatile boolean isRun = true;
    private int timess = 0;
    private boolean isHasSending = false;
    private String reactTime;

    MediaPlayer mMediaPlayer;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    PaidanAdapter.this.notifyDataSetChanged();
                    break;
            }

        }
    };
    private String lat;
    private String lng;
    private CustomProgressDialog mProgress;


    public PaidanAdapter(Context context, List<JSONObject> jsonArray, int type, String lat, String lng) {
        this.type = type;
        this.context = context;
        this.lat = lat;
        this.lng = lng;

        if (type == 0) {
            for (JSONObject json : jsonArray) {
                String dynamicSeq = json.getString("dynamicSeq");
                String createTime = json.getString("createTime");
                String userId = json.getString("userId");
                String content = json.getString("content");
                String location = json.getString("location");
                String authtype = json.getString("authtype");
                String views = json.getString("views");
                String task_label = json.getString("task_label");
                String acceptNum = json.getString("acceptNum");
                String uLoginId = json.getString("uLoginId");
                //String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
                String task_position = json.getString("task_position");
                String task_locaName = json.getString("task_locaName");
                String task_jurisdiction = json.getString("task_jurisdiction");
                String orderState = json.getString("orderState");
                String deviceType = json.getString("deviceType");
                String dynamicSeqR = json.getString("dynamicSeqR");
                String createTimeR = json.getString("createTimeR");
                String typeR = json.getString("typeR");
                String uIdR = json.getString("uIdR");
                String responsetimeR = json.getString("responsetimeR");
                String ordernumR = json.getString("ordernumR");
                String timestampR = json.getString("timestampR");
                String statusR = json.getString("statusR");
                SendToMePaidan paiDanInfo = new SendToMePaidan();
                paiDanInfo.setStatusR(statusR);
                paiDanInfo.setDynamicSeq(dynamicSeq);
                paiDanInfo.setuLoginId(uLoginId);
                paiDanInfo.setCreateTime(createTime);
                paiDanInfo.setUserId(userId);
                paiDanInfo.setContent(content);
                paiDanInfo.setLocation(location);
                paiDanInfo.setAuthtype(authtype);
                paiDanInfo.setViews(views);
                paiDanInfo.setTask_label(task_label);
                paiDanInfo.setTask_position(task_position);
                paiDanInfo.setTask_locaName(task_locaName);
                paiDanInfo.setTask_jurisdiction(task_jurisdiction);
                paiDanInfo.setOrderState(orderState);
                paiDanInfo.setDeviceType(deviceType);
                paiDanInfo.setDynamicSeqR(dynamicSeqR);
                paiDanInfo.setCreateTimeR(createTimeR);
                paiDanInfo.setTypeR(typeR);
                paiDanInfo.setuIdR(uIdR);
                paiDanInfo.setResponsetimeR(responsetimeR);
                paiDanInfo.setOrdernumR(ordernumR);
                paiDanInfo.setTimestampR(timestampR);
                paiDanInfo.setAcceptNum(acceptNum);
                paiDanInfo.setShow("");
                String[] latlng = paiDanInfo.getTask_position().split("\\|");
                String distance = getDistance(latlng[0], latlng[1]);
                Log.d("chen", "lat" + lat + " lng" + lng + "派单" + paiDanInfo.getTask_label() + " 距离" + distance);
                paiDanInfo.setDistance(distance);
                //表明我是第一个打开这个消息
                if (TextUtils.isEmpty(acceptNum) || acceptNum.equalsIgnoreCase("0")) {
                    Log.d("chen", "这条是我一个查看");
                    //Log.d("chen", "上传距离" + distance);
                    //Log.d("chen", "上传时间" + getMin(paiDanInfo.getCreateTime()));
                    paiDanInfo.setAcceptNum("1");
                    updatePaidanInfo(paiDanInfo, getMin(paiDanInfo.getCreateTime()), paiDanInfo.getDistance());
                }
                if (ordernumR == null || TextUtils.isEmpty(ordernumR)) {
                    updateMyInfo(paiDanInfo, null);
                    paiDanInfo.setOrdernumR("1");
                }
                paidanList.add(paiDanInfo);
            }

        } else {
            for (JSONObject json : jsonArray) {
                // 如果数据出错....
                if (json == null || json.size() == 0) {
                    continue;
                }
                String views = json.getString("views");
                if (views == null || "".equals(views)) {
                    views = "0";
                }
                if (views != null && Integer.valueOf(views) > 9999) {
                    views = "9999+";
                }
                String userID = json.getString("uName");
                String createTime = json.getString("createTime");
                String loginId = json.getString("uLoginId");
                String sex1 = json.getString("uSex");
                String content = json.getString("content");
                String firstDistance = json.getString("firstDistance");
                String acceptNum = json.getString("acceptNum");
                String responseTime = json.getString("responseTime");
                String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
                String location = json.getString("location");
                String countPinglun = json.getString("resv7");
                String task_label = json.getString("task_label");
                String task_position = json.getString("task_position");
                String task_locaName = json.getString("task_locaName");
                String orderState = json.getString("orderState");
                String task_jurisdiction = json.getString("task_jurisdiction");
                String video = json.getString("video");
                String videoPictures = json.getString("videoPictures");
                String sum = json.getString("sum");
                String fromUId = json.getString("fromUId");
                String dynamicSeq = json.getString("dynamicSeq");
                JSONArray array = json.getJSONArray("dynamicOrders");
                MySendPaidanInfo info = new MySendPaidanInfo();
                info.setOrderState(orderState);
                info.setDynamicSeq(dynamicSeq);
                info.setJson(json);
                info.setArray(array);
                info.setSum(sum);
                info.setFromUId(fromUId);
                info.setViews(views);
                info.setUserID(userID);
                info.setLoginId(loginId);
                info.setSex1(sex1);
                info.setContent(content);
                info.setImageStr(imageStr);
                info.setLocation(location);
                info.setCountPinglun(countPinglun);
                info.setTask_label(task_label);
                info.setTask_position(task_position);
                info.setTask_locaName(task_locaName);
                info.setTask_jurisdiction(task_jurisdiction);
                info.setVideo(video);
                info.setVideoPictures(videoPictures);
                info.setCreateTime(createTime);
                info.setAcceptNum(acceptNum);
                info.setResponseTime(responseTime);
                info.setFirstDistance(firstDistance);
                info.setShow("");
                users.add(info);

            }
        }
        startRun();

        /*if (type == 0) {
            for (JSONObject jsonObject : jsonArray) {
                Log.d("chen", jsonObject.toString());
                paidanList.add(JSON.parseObject(jsonObject.toString(), SendToMePaidan.class));
            }
        } else {
            for (JSONObject json : jsonArray) {
                // 如果数据出错....
                if (json == null || json.size() == 0) {
                    continue;
                }
                MySendPaidanInfo info = new MySendPaidanInfo();
                info.setContent(TextUtils.isEmpty(json.getString("content")) ? "" : json.getString("content"));
                info.setDisplay(TextUtils.isEmpty(json.getString("display")) ? "" : json.getString("display"));
                if (info.getDisplay().equalsIgnoreCase("00")) {
                    info.setDistance(json.getString("distance").equalsIgnoreCase("") ? "0" : json.getString("distance"));
                } else {
                    info.setDistance(json.getString("distance").equalsIgnoreCase("0") ? "5" : json.getString("distance"));
                }
                info.setTotalNumber(TextUtils.isEmpty(json.getString("totalNumber")) ? "" : json.getString("totalNumber"));
                info.setTimestamp(TextUtils.isEmpty(json.getString("timestamp")) ? "" : json.getString("timestamp"));
                info.setsId(TextUtils.isEmpty(json.getString("sId")) ? "" : json.getString("sId"));
                info.setuId(TextUtils.isEmpty(json.getString("uId")) ? "" : json.getString("uId"));
                info.setFile(TextUtils.isEmpty(json.getString("file")) ? "" : json.getString("file"));
                info.setState(TextUtils.isEmpty(json.getString("state")) ? "" : json.getString("state"));
                info.setShow("派");
                info.setDisstanceAddTime(0);
                users.add(info);
            }
        }*/
        //startRun();
        //mMediaPlayer = new MediaPlayer();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_send_paidan, parent, false);
        /*if (type == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send_me_paidan, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_send_paidan, parent, false);
        }*/
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (type == 0) {
            final SendToMePaidan sendToMePaidan = paidanList.get(position);

            holder.btn_left.setText("反应速度");
            holder.btn_right.setText("接单报价");
            holder.tv_title.setText(sendToMePaidan.getTask_label());
            holder.tv_miaoshu.setText(sendToMePaidan.getContent());
            holder.tv_from.setText("来源：" + getFromAppName(sendToMePaidan.getDeviceType()));
            String[] latlng = sendToMePaidan.getTask_position().split("\\|");
            holder.tv_distance.setText(TextUtils.isEmpty(sendToMePaidan.getDistance()) ? "" : sendToMePaidan.getDistance() + "km");
            if (sendToMePaidan.getAcceptNum() == null || TextUtils.isEmpty(sendToMePaidan.getAcceptNum()) || sendToMePaidan.getAcceptNum().equalsIgnoreCase("0")) {
                holder.tv_catch.setText("第1个接收");
            } else {
                holder.tv_catch.setText("第" + sendToMePaidan.getOrdernumR() + "个接收");
            }
            if (TextUtils.isEmpty(sendToMePaidan.getResponsetimeR()) || sendToMePaidan.getResponsetimeR().equalsIgnoreCase("0")) {
                if (sendToMePaidan.getShow().equalsIgnoreCase("-1")) {
                    //表明现在过了24小时
                    holder.tv_show.setText("24");
                    holder.tv_mini.setText("00");
                } else {
                    if (sendToMePaidan.getShow() != null && !TextUtils.isEmpty(sendToMePaidan.getShow())) {
                        String[] showTimes = sendToMePaidan.getShow().split("-");
                        //如果小于1分钟就直接把秒放中间
                        if (showTimes[0].equalsIgnoreCase("0")) {
                            holder.tv_show.setText(showTimes[1]);
                            holder.tv_mini.setVisibility(View.GONE);
                        } else {
                            holder.tv_show.setText(showTimes[0]);
                            holder.tv_mini.setText(showTimes[1]);
                        }
                    } else {
                        holder.tv_show.setText("");
                        holder.tv_mini.setText("");
                    }


                }
            } else {
                String resopnse = sendToMePaidan.getResponsetimeR();
                int ss = (int) (Double.valueOf(resopnse) * 60);
                if (Double.valueOf(resopnse) >= 1440) {
                    holder.tv_show.setText("24");
                    holder.tv_mini.setText("00");
                } else {
                    String[] showTimes = getSpiltTime(ss).split("-");
                    if (showTimes[0].equalsIgnoreCase("0")) {
                        holder.tv_show.setText(showTimes[1]);
                        holder.tv_mini.setVisibility(View.GONE);
                    } else {
                        holder.tv_show.setText(showTimes[0]);
                        holder.tv_mini.setText(showTimes[1]);
                    }

                }

            }

            holder.iv_show_bubble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(sendToMePaidan.getStatusR())) {
                        return;
                    }
                    if (TextUtils.isEmpty(sendToMePaidan.getResponsetimeR())) {
                        updateMyInfo(sendToMePaidan,getMin(sendToMePaidan.getCreateTimeR()));
                    }
                    final BubblePopupWindow leftTopWindow = new BubblePopupWindow(context);
                    View bubbleView = LayoutInflater.from(context).inflate(R.layout.layout_popup_view, null);
                    leftTopWindow.setBubbleView(bubbleView); // 设置气泡内容
                    leftTopWindow.show(holder.iv_show_bubble, Gravity.LEFT, -400); // 显示弹窗
                    TextView textView1 = (TextView) bubbleView.findViewById(R.id.tv_1);
                    TextView textView2 = (TextView) bubbleView.findViewById(R.id.tv_2);
                    TextView textView3 = (TextView) bubbleView.findViewById(R.id.tv_3);
                    TextView textView4 = (TextView) bubbleView.findViewById(R.id.tv_4);
                    TextView textView5 = (TextView) bubbleView.findViewById(R.id.tv_5);
                    textView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateMyInfoState(sendToMePaidan, "不对称");
                            sendToMePaidan.setStatusR("不对称");
                            leftTopWindow.dismiss();

                        }
                    });
                    textView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateMyInfoState(sendToMePaidan, "没谈成");
                            sendToMePaidan.setStatusR("没谈成");
                            leftTopWindow.dismiss();

                        }
                    });
                    textView3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateMyInfoState(sendToMePaidan, "不接单");
                            sendToMePaidan.setStatusR("不接单");
                            leftTopWindow.dismiss();

                        }
                    });
                    textView4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateMyInfoState(sendToMePaidan, "晚一步");
                            sendToMePaidan.setStatusR("晚一步");
                            leftTopWindow.dismiss();

                        }
                    });
                    textView5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, JieDanSettingActivity.class));
                            leftTopWindow.dismiss();

                        }
                    });
                }
            });
            holder.btn_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DynaDetaActivity.class);
                    intent.putExtra("sID", sendToMePaidan.getUserId());
                    intent.putExtra("profession", "有");
                    intent.putExtra("dynamicSeq", sendToMePaidan.getDynamicSeq());
                    intent.putExtra("createTime", sendToMePaidan.getCreateTime());
                    intent.putExtra("dType", "05");
                    intent.putExtra("type", "01");
                    intent.putExtra("type2", "00");
                    context.startActivity(intent);
                    updateMyInfo(sendToMePaidan, getMin(sendToMePaidan.getCreateTimeR()));
                }
            });
            holder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DynaDetaActivity.class);
                    intent.putExtra("sID", sendToMePaidan.getUserId());
                    intent.putExtra("profession", "有");
                    intent.putExtra("dynamicSeq", sendToMePaidan.getDynamicSeq());
                    intent.putExtra("createTime", sendToMePaidan.getCreateTime());
                    intent.putExtra("dType", "05");
                    intent.putExtra("type", "01");
                    intent.putExtra("type2", "00");
                    context.startActivity(intent);
                    updateMyInfo(sendToMePaidan, getMin(sendToMePaidan.getCreateTimeR()));
                }
            });
            holder.tv_distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DynaDetaActivity.class);
                    intent.putExtra("sID", sendToMePaidan.getUserId());
                    intent.putExtra("profession", "有");
                    intent.putExtra("dynamicSeq", sendToMePaidan.getDynamicSeq());
                    intent.putExtra("createTime", sendToMePaidan.getCreateTime());
                    intent.putExtra("dType", "05");
                    intent.putExtra("type", "01");
                    intent.putExtra("type2", "00");
                    context.startActivity(intent);
                    updateMyInfo(sendToMePaidan, getMin(sendToMePaidan.getCreateTimeR()));
                }
            });
            holder.tv_from.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DynaDetaActivity.class);
                    intent.putExtra("sID", sendToMePaidan.getUserId());
                    intent.putExtra("profession", "有");
                    intent.putExtra("dynamicSeq", sendToMePaidan.getDynamicSeq());
                    intent.putExtra("createTime", sendToMePaidan.getCreateTime());
                    intent.putExtra("dType", "05");
                    intent.putExtra("type", "01");
                    intent.putExtra("type2", "00");
                    context.startActivity(intent);
                    updateMyInfo(sendToMePaidan, getMin(sendToMePaidan.getCreateTimeR()));
                }
            });
            holder.tv_catch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DynaDetaActivity.class);
                    intent.putExtra("sID", sendToMePaidan.getUserId());
                    intent.putExtra("profession", "有");
                    intent.putExtra("dynamicSeq", sendToMePaidan.getDynamicSeq());
                    intent.putExtra("createTime", sendToMePaidan.getCreateTime());
                    intent.putExtra("dType", "05");
                    intent.putExtra("type", "01");
                    intent.putExtra("type2", "00");
                    context.startActivity(intent);
                    updateMyInfo(sendToMePaidan, getMin(sendToMePaidan.getCreateTimeR()));
                }
            });
            holder.tv_miaoshu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DynaDetaActivity.class);
                    intent.putExtra("sID", sendToMePaidan.getUserId());
                    intent.putExtra("profession", "有");
                    intent.putExtra("dynamicSeq", sendToMePaidan.getDynamicSeq());
                    intent.putExtra("createTime", sendToMePaidan.getCreateTime());
                    intent.putExtra("dType", "05");
                    intent.putExtra("type", "01");
                    intent.putExtra("type2", "00");
                    context.startActivity(intent);
                    updateMyInfo(sendToMePaidan, getMin(sendToMePaidan.getCreateTimeR()));
                }
            });
            holder.btn_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(reactTime)) {
                        ToastUtils.showNOrmalToast(context.getApplicationContext(), "由于您的没有接过单无法统计到您的接单速度");
                        return;
                    }
                    LayoutInflater inflaterDl = LayoutInflater.from(context);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_jiedan_speed, null);
                    final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setContentView(layout);
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.gravity = Gravity.CENTER;
                    lp.width = DensityUtil.dip2px(context,250);//宽高可设置具体大
                    lp.height = DensityUtil.dip2px(context,350);
                    dialog.getWindow().setAttributes(lp);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    TextView tv_time = (TextView) layout.findViewById(R.id.tv_time);
                    TextView tv_show = (TextView) layout.findViewById(R.id.tv_show);
                    tv_time.setText(reactTime);
                    if (reactTime.contains("s")) {
                        tv_show.setText("反应 很 积极");
                    } else if (reactTime.contains("m")) {
                        tv_show.setText("反应   一般");
                    } else  if (reactTime.contains("h")){
                        tv_show.setText("反应 很 消极");
                    }
                    updateMyInfo(sendToMePaidan, getMin(sendToMePaidan.getCreateTimeR()));
                }
            });
            /*final SendToMePaidan paidan = paidanList.get(position);
            holder.tv_title.setText(paidan.getContent());*/

            /*final SendToMePaidan paidan = paidanList.get(position);
            holder.name.setText(TextUtils.isEmpty(paidan.getUserInfo().getuName()) ? paidan.getuId() : paidan.getUserInfo().getuName());
            if (paidan.getDisplay().equalsIgnoreCase("00")) {
                holder.tv_time.setText(paidan.getShow());
            } else {
                paidan.setShow("结束");
                holder.tv_time.setText("结束");
            }

            if (TextUtils.isEmpty(paidan.getContent())) {
                holder.tv_need.setVisibility(View.GONE);
                holder.rl_send_me_play_voice.setVisibility(View.VISIBLE);
                holder.rl_send_me_play_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.iv_voice.setImageResource(R.drawable.voice_play);
                        AnimationDrawable animationDrawable = (AnimationDrawable) holder.iv_voice.getDrawable();
                        animationDrawable.start();
                        playVoice(paidan.getFile(), holder.iv_voice);
                    }
                });
            } else {
                holder.rl_send_me_play_voice.setVisibility(View.GONE);
                holder.tv_need.setVisibility(View.VISIBLE);
                holder.tv_need.setText(paidan.getContent());
            }
            String distance = getDistance(paidan);

            if (TextUtils.isEmpty(distance)) {
                //如果为空  表明有问题，就不显示
                holder.tv_distance.setText("");
            } else {
                holder.tv_distance.setText(distance + "km");
                paidan.setDistance(distance);
            }
            holder.btn_dianhua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isNoOneAccept(paidan);
                }
            });
            holder.btn_weizhi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这里我直接把师傅的位置传过去，这样直接能看见
                    Intent intent = new Intent(context, BaiDuPaidanLocationActivity.class);
                    Log.d("chen", "loginId" + paidan.getuId() + "name" + paidan.getUserInfo().getuName() + " sex" + paidan.getUserInfo().getuSex());
                    ArrayList<String> lat1 = new ArrayList<String>();
                    lat1.add(paidan.getLat());
                    ArrayList<String> log1 = new ArrayList<String>();
                    log1.add(paidan.getLog());
                    ArrayList<String> loginId = new ArrayList<String>();
                    loginId.add(paidan.getuId());
                    ArrayList<String> name = new ArrayList<String>();
                    name.add(paidan.getUserInfo().getuName());
                    ArrayList<String> sex = new ArrayList<String>();
                    sex.add(paidan.getUserInfo().getuSex());
                    intent.putExtra("mlat", paidan.getLat());
                    intent.putExtra("mlon", paidan.getLog());
                    intent.putStringArrayListExtra("lat", lat1);
                    intent.putStringArrayListExtra("lng", log1);
                    intent.putStringArrayListExtra("loginId", loginId);
                    intent.putStringArrayListExtra("name", name);
                    intent.putStringArrayListExtra("sex", sex);
                    context.startActivity(intent);
                }
            });

            holder.tv_from.setText("来源：" + getFromAppName(paidan.getUserInfo().getDeviceType()));*/

        } else {
            holder.tv_from.setVisibility(View.INVISIBLE);
            holder.iv_show_bubble.setVisibility(View.INVISIBLE);
            final MySendPaidanInfo info = users.get(position);
            holder.tv_title.setText(info.getTask_label());
            holder.tv_miaoshu.setText(info.getContent());
            holder.btn_left.setText("编辑内容");

            if (!info.getOrderState().equalsIgnoreCase("00")) {
                holder.btn_right.setText("已交易");
            }else {
                holder.btn_right.setText("修改状态");
            }

            if (info.getResponseTime() != null && !TextUtils.isEmpty(info.getResponseTime()) && !info.getResponseTime().equalsIgnoreCase("0")) {
                if (getSpiltTime((int) (Double.valueOf(info.getResponseTime()) * 60)).equalsIgnoreCase("-1")) {
                    holder.tv_show.setText("24");
                    holder.tv_mini.setText("00");
                } else {
                    String[] times = getSpiltTime((int) (Double.valueOf(info.getResponseTime()) * 60)).split("-");
                    if (times[0].equalsIgnoreCase("0")) {
                        holder.tv_show.setText(times[1]);
                        holder.tv_mini.setVisibility(View.GONE);
                    } else {
                        holder.tv_show.setText(times[0]);
                        holder.tv_mini.setText(times[1]);
                    }
                    /*holder.tv_show.setText(times[0]);
                    holder.tv_mini.setText(times[1]);*/
                }

            } else {
                if (info.getShow().equalsIgnoreCase("-1")) {
                    //表明现在过了24小时
                    holder.tv_show.setText("24");
                    holder.tv_mini.setText("00");
                } else {
                    if (info.getShow() != null && !TextUtils.isEmpty(info.getShow())) {
                        String[] showTimes = info.getShow().split("-");
                        if (showTimes[0].equalsIgnoreCase("0")) {
                            holder.tv_show.setText(showTimes[1]);
                            holder.tv_mini.setVisibility(View.GONE);
                        } else {
                            holder.tv_show.setText(showTimes[0]);
                            holder.tv_mini.setText(showTimes[1]);
                        }
                        /*holder.tv_mini.setText(showTimes[1]);
                        holder.tv_show.setText(showTimes[0]);*/
                    } else {
                        holder.tv_show.setText("");
                        holder.tv_mini.setText("");
                    }


                }
            }
            Log.d("chen", "onBindViewHolder 人" + info.getAcceptNum());
            holder.tv_distance.setText(info.getFirstDistance() == null ? "0km" : info.getFirstDistance() + "km");
            holder.tv_catch.setText(info.getAcceptNum() == null ? "0人查看" : info.getAcceptNum() + "人查看");

            holder.btn_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!info.getOrderState().equalsIgnoreCase("00")) {
                        ToastUtils.showNOrmalToast(context.getApplicationContext(), "派单已完成");
                        return;
                    }
                    //TODO
                    LayoutInflater inflaterDl = LayoutInflater.from(context);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_change_state, null);
                    final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    Button btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
                    Button btn_delete = (Button) layout.findViewById(R.id.btn_delete);
                    Button btn_finish = (Button) layout.findViewById(R.id.btn_finish);
                    TextView title = (TextView) layout.findViewById(R.id.title_tv);
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDelete(info, position);
                            dialog.dismiss();
                        }
                    });
                    btn_finish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //dynamicSeq=&createTime=&contactId=&type=
                          //  ((PaidanListActivity) context).startActivity(new Intent(context, FinishPaidanActivity.class)
                          //          .putExtra("dynamicSeq", info.getDynamicSeq()).putExtra("createTime", info.getCreateTime()));

                            ((PaidanListActivity) context).startActivity(new Intent(context, SelectTradeUserActivity.class)
                                    .putExtra("dynamicSeq", info.getDynamicSeq()).putExtra("createTime", info.getCreateTime()));
                            dialog.dismiss();
                        }
                    });

                }
            });
            holder.btn_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!info.getOrderState().equalsIgnoreCase("00")) {
                        ToastUtils.showNOrmalToast(context.getApplicationContext(), "订单正在进行或已完成无法操作");
                        return;
                    }
                    String data = info.getJson().toString();
                    context.startActivity(new Intent(context,ReviseXuQiuActivity.class).putExtra("data",data));
                }
            });

            holder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DynaDetaActivity.class);
                    intent.putExtra("sID", info.getUserID());
                    intent.putExtra("profession", "有");
                    intent.putExtra("dynamicSeq", info.getDynamicSeq());
                    intent.putExtra("createTime", info.getCreateTime());
                    intent.putExtra("dType", "05");
                    intent.putExtra("type", "01");
                    intent.putExtra("type2", "00");
                    context.startActivity(intent);
                }
            });
            holder.tv_distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DynaDetaActivity.class);
                    intent.putExtra("sID", info.getUserID());
                    intent.putExtra("profession", "有");
                    intent.putExtra("dynamicSeq", info.getDynamicSeq());
                    intent.putExtra("createTime", info.getCreateTime());
                    intent.putExtra("dType", "05");
                    intent.putExtra("type", "01");
                    intent.putExtra("type2", "00");
                    context.startActivity(intent);
                }
            });
            holder.tv_catch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DynaDetaActivity.class);
                    intent.putExtra("sID", info.getUserID());
                    intent.putExtra("profession", "有");
                    intent.putExtra("dynamicSeq", info.getDynamicSeq());
                    intent.putExtra("createTime", info.getCreateTime());
                    intent.putExtra("dType", "05");
                    intent.putExtra("type", "01");
                    intent.putExtra("type2", "00");
                    context.startActivity(intent);
                }
            });
            holder.tv_miaoshu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DynaDetaActivity.class);
                    intent.putExtra("sID", info.getUserID());
                    intent.putExtra("profession", "有");
                    intent.putExtra("dynamicSeq", info.getDynamicSeq());
                    intent.putExtra("createTime", info.getCreateTime());
                    intent.putExtra("dType", "05");
                    intent.putExtra("type", "01");
                    intent.putExtra("type2", "00");
                    context.startActivity(intent);
                }
            });

           /* final MySendPaidanInfo info = users.get(position);
            if (!TextUtils.isEmpty(info.getFile())) {
                holder.tv_title.setVisibility(View.GONE);
                holder.rl_play_voice.setVisibility(View.VISIBLE);
                holder.rl_play_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (info.getFile().length() > 5) {
                            holder.iv_voice.setImageResource(R.drawable.voice_play);
                            AnimationDrawable animationDrawable = (AnimationDrawable) holder.iv_voice.getDrawable();
                            animationDrawable.start();
                            playVoice(info.getFile(), holder.iv_voice);
                        } else {
                            ToastUtils.showNOrmalToast(context.getApplicationContext(), "语音文件出现问题");
                        }
                    }
                });
            } else {
                holder.rl_play_voice.setVisibility(View.GONE);
                holder.tv_title.setVisibility(View.VISIBLE);
                holder.tv_title.setText(info.getContent());
            }
            if (Double.valueOf(info.getDistance()) > 50) {
                holder.tv_distance.setText("50km以外");
            } else {
                holder.tv_distance.setText(info.getDistance() + "km");
            }


            holder.tv_catch.setText(info.getTotalNumber() + "人");


            if (info.getState().equalsIgnoreCase("00")) {
                holder.ll_finish_paidan_contain.setVisibility(View.GONE);
                holder.tv_show.setVisibility(View.VISIBLE);
                holder.tv_show.setText(info.getShow());
            } else if (info.getState().equalsIgnoreCase("01")) {
                holder.tv_show.setVisibility(View.GONE);
                holder.ll_finish_paidan_contain.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(info.getTimestamp())) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    try {
                        Date date = format.parse(info.getTimestamp());
                        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String time = st.format(date);
                        holder.tv_paidan_finish_time.setText(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    holder.tv_paidan_state.setText("交易中");
                }

            } else if (info.getState().equalsIgnoreCase("02")) {
                holder.tv_show.setVisibility(View.GONE);
                holder.ll_finish_paidan_contain.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(info.getTimestamp())) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    try {
                        Date date = format.parse(info.getTimestamp());
                        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String time = st.format(date);
                        holder.tv_paidan_finish_time.setText(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    holder.tv_paidan_state.setText("交易完成");
                }

            } else if (info.getState().equalsIgnoreCase("03")) {
                holder.tv_show.setVisibility(View.GONE);
                holder.ll_finish_paidan_contain.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(info.getTimestamp())) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    try {
                        Date date = format.parse(info.getTimestamp());
                        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String time = st.format(date);
                        holder.tv_paidan_finish_time.setText(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    holder.tv_paidan_state.setText("师傅拒绝");
                }

            }

            holder.rl_time_contain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.getState().equalsIgnoreCase("00")) {
                        LayoutInflater inflaterDl = LayoutInflater.from(context);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_resend_paidan, null);
                        final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                        Button btnOK = (Button) layout.findViewById(R.id.btn_ok);

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resendPaidan(users.get(position));
                                dialog.dismiss();
                            }
                        });
                    } else {
                        Intent intent = new Intent(context, FinishPaidanActivity.class);
                        intent.putExtra("type", "1");
                        intent.putExtra("sId", info.getsId());
                        intent.putExtra("content", info.getContent());
                        context.startActivity(intent);
                    }


                }
            });
            holder.btn_edit_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!info.getState().equalsIgnoreCase("00")) {
                        return;
                    }

                    LayoutInflater inflaterDl = LayoutInflater.from(context);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_resend_paidan, null);
                    final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                    Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                    TextView title = (TextView) layout.findViewById(R.id.title_tv);
                    title.setText("是否修改派人要求?");
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, QuickPublishActivity.class).putExtra("sid", info.getsId()).putExtra("file", info.getFile()).putExtra("content", info.getContent()));
                            dialog.dismiss();
                        }
                    });
                }
            });
            holder.btn_change_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!info.getState().equalsIgnoreCase("00")) {
                        return;
                    }
                    //TODO
                    LayoutInflater inflaterDl = LayoutInflater.from(context);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_change_state, null);
                    final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    Button btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
                    Button btn_delete = (Button) layout.findViewById(R.id.btn_delete);
                    Button btn_finish = (Button) layout.findViewById(R.id.btn_finish);
                    TextView title = (TextView) layout.findViewById(R.id.title_tv);
                    title.setText("是否修改派人要求?");
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            queryIsContact(info, dialog);
                            dialog.dismiss();
                        }
                    });
                    btn_finish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((PaidanListActivity) context).startActivityForResult(new Intent(context, FinishPaidanActivity.class).putExtra("sId", info.getsId()).putExtra("content", info.getContent()), 1);
                            dialog.dismiss();
                        }
                    });

                }
            });*/

        }
    }


    //20180117103954
    public int getTimeSpace(String sendTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        long spaceTime = 0;
        try {
            Date date = format.parse(sendTime);
            spaceTime = System.currentTimeMillis() - date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ((int) (spaceTime / 1000.00));
        /*//如果小于90秒
        if (spaceTime < 90 * 1000) {
            return 90 - ((int) (spaceTime / 1000.00));
        } else {
            return -1;
        }*/

    }


    /**
     * 开启倒计时
     */
    private int time = 0;

    private void startRun() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        if (type == 0) {
                            if (paidanList.size() == 0) {
                                break;
                            }
                            for (SendToMePaidan info : paidanList) {

                                if (!TextUtils.isEmpty(info.getResponsetimeR())) {
                                    info.setShow("24-00");
                                    continue;
                                }
                                //得到秒
                                int ss = getTimeSpace(info.getCreateTimeR());
                                if (ss > 24 * 3600) {
                                    updateMyInfo(info, "1440");
                                    info.setResponsetimeR("1440");
                                }
                                info.setShow(getSpiltTime(ss));
                            }

                           /* for (SendToMePaidan info : paidanList) {
                                if (info.getDisplay().equalsIgnoreCase("01")) {
                                    continue;
                                }
                                int spaceTime = getTimeSpace(info.getTimestamp());
                                //Log.d("chen", "spaceTime" + spaceTime);
                                if (spaceTime == -1) {
                                    //如果超过90秒
                                    info.setDisplay("01");
                                    if (TextUtils.isEmpty(info.getDistance()) || Double.valueOf(info.getDistance()) <= 0) {
                                        updatePaidan(info.getsId(), "01", "5", info.getContent(), false);
                                    } else {
                                        updatePaidan(info.getsId(), "01", info.getDistance(), info.getContent(), false);
                                    }
                                    handler.sendEmptyMessage(1);
                                } else {
                                    info.setShow(String.valueOf(spaceTime));
                                }
                            }*/
                            /*//进行统一的发送
                            for (SendToMePaidan info : paidanList) {

                                if (info.getDisplay().equalsIgnoreCase("00")) {
                                    //如果是00表面没过了90秒
                                    continue;
                                }
                                if (info.getShow().contains(":")) {
                                    return;
                                }
                                if (TextUtils.isEmpty(info.getDistance()) || Double.valueOf(info.getDistance()) <= 0) {
                                    updatePaidan(info.getsId(), "01", "5",info.getContent());
                                    //paidanList.remove(info);
                                } else {
                                    updatePaidan(info.getsId(), "01", info.getDistance(),info.getContent());
                                    //paidanList.remove(info);
                                }
                                handler.sendEmptyMessage(1);
                                ((PaidanListActivity) context).updateAllPaidanWithNoCreateNewAdapter();
                            }*/
                        } else {

                            synchronized (context) {
                                if (users.size() == 0) {
                                    break;
                                }
                                for (MySendPaidanInfo info : users) {
                                    if (!TextUtils.isEmpty(info.getResponseTime()) && Double.valueOf(info.getResponseTime()) > 0) {
                                        String time = getSpiltTime((int) (Double.valueOf(info.getResponseTime()) * 60));
                                        if (time.equalsIgnoreCase("-1")) {
                                            info.setShow("24-00");
                                        } else {
                                            info.setShow(time);
                                        }
                                        continue;
                                    }

                                    int ss = getTimeSpace(info.getCreateTime());
                                    StringBuffer stringBuffer = new StringBuffer();

                                    if (ss < 3600) {
                                        int min = 0;
                                        int s = 0;
                                        if (ss < 60) {
                                            min = 0;
                                            s = ss;
                                        } else {
                                            min = ss / 60;
                                            s = ss % 60;
                                        }
                                        stringBuffer.append(min + "-" + s);

                                    } else {
                                        double hour = 0;
                                        int s = 0;
                                        if (ss < 3600 * 24) {
                                            hour = Double.valueOf(ss) / 3600.00;
                                            s = ss % 60;
                                            String str = String.format("%.1f", hour);//format 返回的是字符串
                                            stringBuffer.append(str + "-" + s);
                                        } else {
                                        /*hour = 24;
                                        s = 0;*/
                                            //超过24小时按24算
                                            stringBuffer.append("-1");
                                        }
                                    }
                                   //Log.d("chen", stringBuffer.toString() + "我派出的页面刷新中2"+ info.getContent());
                                    info.setShow(stringBuffer.toString());
                                }
                                if (time == 3) {
                                    //Log.d("chen", "开始刷新");
                                    //刷新数据
                                        ((PaidanListActivity) context).updateMySendPaidan();
                                    time = 0;
                                }
                                time++;
                            }
                           /* timess++;
                            if (isHasSending && timess > 25) {
                                timess = 0;
                                ((PaidanListActivity) context).updateAllPaidanWithNoCreateNewAdapter();

                            }
                            //用于计算距离
                            addDisstance();

                            //如果有一个单子是正在倒计时就把下次加一
                            isHasSending = false;
                            for (MySendPaidanInfo info : users) {
                                if (info.getDisplay().equalsIgnoreCase("01")) {
                                    //如果是01表面已过了90秒
                                    info.setShow("派");
                                    continue;
                                }
                                int spaceTime = getTimeSpace(info.getTimestamp());
                                if (spaceTime == -1) {
                                    //如果超过90秒
                                    info.setShow("派");
                                    info.setDisplay("01");
                                    Log.d("chen", "已超时--" + info.getContent());
                                    updatePaidan(info.getsId(), "01", info.getDistance(), info.getContent(), true);
                                } else {
                                    info.setShow(String.valueOf(spaceTime));
                                    isHasSending = true;

                                }
                            }*/
                        }
                        handler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }


    private List<MySendPaidanInfo> users2 = new ArrayList<>();

    public void setNewData(List<JSONObject> jsonArray) {
        if (type == 0) {
            /*paidanList.clear();
            for (JSONObject jsonObject : jsonArray) {
                paidanList.add(JSON.parseObject(jsonObject.toString(), SendToMePaidan.class));
            }
            startRun();
            Log.d("chen", "新的数据已加载");*/
        } else {
            users2.clear();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject json = jsonArray.get(i);
                // 如果数据出错....
                if (json == null || json.size() == 0) {
                    continue;
                }
                String views = json.getString("views");
                if (views == null || "".equals(views)) {
                    views = "0";
                }
                if (views != null && Integer.valueOf(views) > 9999) {
                    views = "9999+";
                }
                String userID = json.getString("uName");
                String createTime = json.getString("createTime");
                String loginId = json.getString("uLoginId");
                String sex1 = json.getString("uSex");
                String content = json.getString("content");
                String acceptNum = json.getString("acceptNum");
                String firstDistance = json.getString("firstDistance");
                String responseTime = json.getString("responseTime");
                String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
                String location = json.getString("location");
                String orderState = json.getString("orderState");
                String countPinglun = json.getString("resv7");
                String task_label = json.getString("task_label");
                String task_position = json.getString("task_position");
                String task_locaName = json.getString("task_locaName");
                String task_jurisdiction = json.getString("task_jurisdiction");
                String video = json.getString("video");
                String videoPictures = json.getString("videoPictures");
                MySendPaidanInfo info = new MySendPaidanInfo();
                info.setOrderState(orderState);
                info.setViews(views);
                info.setUserID(userID);
                info.setLoginId(loginId);
                info.setSex1(sex1);
                info.setContent(content);
                info.setImageStr(imageStr);
                info.setLocation(location);
                info.setCountPinglun(countPinglun);
                info.setTask_label(task_label);
                info.setTask_position(task_position);
                info.setTask_locaName(task_locaName);
                info.setTask_jurisdiction(task_jurisdiction);
                info.setVideo(video);
                info.setVideoPictures(videoPictures);
                info.setCreateTime(createTime);
                info.setAcceptNum(acceptNum);
                info.setResponseTime(responseTime);
                info.setFirstDistance(firstDistance);
                info.setShow("");
                users2.add(info);
            }
            //users.clear();
            for (MySendPaidanInfo info : users) {
                for (MySendPaidanInfo info2 : users2) {
                    //Log.d("chen", "开始刷新 " + info2.getContent() + "  人  " + info2.getAcceptNum());
                    if (info.getCreateTime().equalsIgnoreCase(info2.getCreateTime())) {
                        info.setContent(info2.getContent());
                        info.setTask_label(info2.getTask_label());
                        info.setFirstDistance(info2.getFirstDistance());
                        info.setAcceptNum(info2.getAcceptNum());
                        info.setResponseTime(info2.getResponseTime());
                        info.setOrderState(info2.getOrderState());
                    }
                }
            }
            //users.addAll(users2);
            //重新将时间充值
            time = 0;
        }

        handler.sendEmptyMessage(1);
    }

    public void stopThread() {
        if (isRun) {
            isRun = false;
        }
    }

    private String getDistance(String latS, String lngS) {
        String paidanLat = latS;
        String paidanLng = lngS;
        //4.9E-324  考虑到百度定位出错时 就不显示
        if (!(TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng) || TextUtils.isEmpty(paidanLat) || TextUtils.isEmpty(paidanLng) ||
                paidanLat.equalsIgnoreCase("4.9E-324") || paidanLng.equalsIgnoreCase("4.9E-324")) ) {
            double latitude1 = Double.valueOf(lat);
            double longitude1 = Double.valueOf(lng);
            final LatLng ll1 = new LatLng(Double.parseDouble(paidanLat), Double.parseDouble(paidanLng));
            LatLng ll = new LatLng(latitude1, longitude1);
            double distance = DistanceUtil.getDistance(ll, ll1);
            double dou = distance / 1000;
            String str = String.format("%.1f", dou);//format 返回的是字符串
            return str;
        } else {
            if (TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) || TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng())) {
                return null;
            }
            double latitude1 = Double.valueOf(DemoApplication.getInstance().getCurrentLat());
            double longitude1 = Double.valueOf(DemoApplication.getInstance().getCurrentLng());
            final LatLng ll1 = new LatLng(Double.parseDouble(paidanLat), Double.parseDouble(paidanLng));
            LatLng ll = new LatLng(latitude1, longitude1);
            double distance = DistanceUtil.getDistance(ll, ll1);
            double dou = distance / 1000;
            String str = String.format("%.1f", dou);//format 返回的是字符串
            return str;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, tv_time, tv_need, tv_title, tv_catch, tv_show, tv_from, tv_distance, tv_paidan_state, tv_paidan_finish_time, tv_miaoshu, tv_mini;
        Button btn_dianhua, btn_weizhi, btn_right, btn_left;
        RelativeLayout rl_time_contain, rl_play_voice, rl_send_me_play_voice;
        ImageView iv_voice, iv_show_bubble;
        LinearLayout ll_finish_paidan_contain;


        public ViewHolder(View convertView) {
            super(convertView);
            tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_catch = (TextView) convertView.findViewById(R.id.tv_catch);
            tv_show = (TextView) convertView.findViewById(R.id.tv_show);
            tv_from = (TextView) convertView.findViewById(R.id.tv_from);
            tv_miaoshu = (TextView) convertView.findViewById(R.id.tv_miaoshu);
            tv_mini = (TextView) convertView.findViewById(R.id.tv_mini);
            tv_paidan_state = (TextView) convertView.findViewById(R.id.tv_paidan_state);
            tv_paidan_finish_time = (TextView) convertView.findViewById(R.id.tv_paidan_finish_time);
            iv_voice = (ImageView) convertView.findViewById(R.id.iv_voice);
            iv_show_bubble = (ImageView) convertView.findViewById(R.id.iv_show_bubble);

            btn_right = (Button) convertView.findViewById(R.id.btn_right);
            btn_left = (Button) convertView.findViewById(R.id.btn_left);
            rl_time_contain = (RelativeLayout) convertView.findViewById(R.id.rl_time_contain);
            ll_finish_paidan_contain = (LinearLayout) convertView.findViewById(R.id.ll_finish_paidan_contain);
            /*if (type == 0) {
                name = (TextView) convertView.findViewById(R.id.tv_name);
                tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                tv_need = (TextView) convertView.findViewById(R.id.tv_need);
                tv_from = (TextView) convertView.findViewById(R.id.tv_from);
                tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
                btn_dianhua = (Button) convertView.findViewById(R.id.btn_dianhua);
                btn_weizhi = (Button) convertView.findViewById(R.id.btn_weizhi);
                rl_send_me_play_voice = (RelativeLayout) convertView.findViewById(R.id.rl_send_me_play_voice);
                iv_voice = (ImageView) convertView.findViewById(R.id.iv_voice);
            } else {
                tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
                tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                tv_catch = (TextView) convertView.findViewById(R.id.tv_catch);
                tv_show = (TextView) convertView.findViewById(R.id.tv_show);
                tv_paidan_state = (TextView) convertView.findViewById(R.id.tv_paidan_state);
                tv_paidan_finish_time = (TextView) convertView.findViewById(R.id.tv_paidan_finish_time);
                iv_voice = (ImageView) convertView.findViewById(R.id.iv_voice);

                btn_change_status = (Button) convertView.findViewById(R.id.btn_tongji);
                btn_edit_content = (Button) convertView.findViewById(R.id.btn_react_speed);
                rl_time_contain = (RelativeLayout) convertView.findViewById(R.id.rl_time_contain);
                rl_play_voice = (RelativeLayout) convertView.findViewById(R.id.rl_play_voice);
                ll_finish_paidan_contain = (LinearLayout) convertView.findViewById(R.id.ll_finish_paidan_contain);


            }*/

        }

    }


    private String getSpiltTime(int ss) {
        StringBuffer stringBuffer = new StringBuffer();

        if (ss < 3600) {
            int min = 0;
            int s = 0;
            if (ss < 60) {
                min = 0;
                s = ss;
            } else {
                min = ss / 60;
                s = ss % 60;
            }
            stringBuffer.append(min + "-" + s);

        } else {
            double hour = 0;
            int s = 0;
            if (ss < 3600 * 24) {
                hour = Double.valueOf(ss) / 3600.00;
                s = ss % 60;
                String str = String.format("%.1f", hour);//format 返回的是字符串
                stringBuffer.append(str + "-" + s);
            } else {
                //超过24小时按24算
                stringBuffer.append("-1");
            }
        }
        return stringBuffer.toString();
    }

    private void updateMyInfoState(final SendToMePaidan info, final String status) {
        String url = FXConstant.URL_UPDATE_DYNAMIC_RECEIPT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "updatePaidanPush onResponse" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "updatePaidanPush onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //uId=&dynamicSeq=&createTime=&dis=&responsetime=&ordernum=&status=
                Map<String, String> param = new HashMap<>();
                param.put("dynamicSeq", info.getDynamicSeq());
                param.put("createTime", info.getCreateTime());
                param.put("uId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("status", status);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private String getFromAppName(String deviceType) {
        if (deviceType == null) {
            return "正事多";
        }
        if (deviceType.contains("正事多")) {
            return "正事多";
        } else if (deviceType.contains("接单全")) {
            return "接单全";
        } else if (deviceType.contains("安装维修")) {
            return "安装维修接单助手";
        } else if (deviceType.contains("家政清洁")) {
            return "同城家政清洁服务大全";
        } else if (deviceType.contains("抢单接活")) {
            return "抢单派单";
        } else {
            return "正事多";
        }
    }

    private void updatePaidanPush(final SendToMePaidan info) {
        String url = FXConstant.URL_UPDATE_DYNAMIC_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "updatePaidanPush onResponse" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "updatePaidanPush onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("dynamicSeq", info.getDynamicSeq());
                param.put("createTime", info.getCreateTime());
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    private void updatePaidanInfo(final SendToMePaidan info, final String time, final String firstDistance) {
        String url = FXConstant.URL_UPDATE_DYNAMIC_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "updatePaidanInfo onResponse" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "updatePaidanInfo onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                //创建时间和seq必传,dynamicSeq=&createTime=&acceptNum=&responseTime=&firstDistance=
                param.put("uId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("dynamicSeq", info.getDynamicSeq());
                param.put("createTime", info.getCreateTime());
                param.put("acceptNum", "1");
                param.put("responseTime", time);
                param.put("firstDistance", firstDistance);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    private void updateMyInfo(final SendToMePaidan info, final String time) {
        String url = FXConstant.URL_UPDATE_DYNAMIC_RECEIPT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "updateMyInfo onResponse" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "updateMyInfo onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                //创建时间、seq和uId必传，uId=&dynamicSeq=&createTime=&dis=&responsetime=&ordernum=
                param.put("uId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("dynamicSeq", info.getDynamicSeq());
                param.put("createTime", info.getCreateTime());
                if (time == null) {
                    if (info.getAcceptNum() == null || TextUtils.isEmpty(info.getAcceptNum()) || info.getAcceptNum().equalsIgnoreCase("0")) {
                        param.put("ordernum", "1");
                        Log.d("chen", "ordernum1  " + param.get("ordernum"));
                    } else {
                        param.put("ordernum", (Integer.valueOf(info.getAcceptNum()) + 1) + "");
                        Log.d("chen", "ordernum2  " + param.get("ordernum"));
                        info.setOrdernumR(String.valueOf(Integer.valueOf(info.getAcceptNum())+ 1));
                    }
                } else {
                    param.put("responsetime", time);
                    info.setResponsetimeR(time);
                }
                return param;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);

    }


    private void showDelete(final MySendPaidanInfo info, final int position) {

        //取消派单 删除推送 顺便修改动态的orderState
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("是否确认取消派单?");
        builder.setTitle("确认");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (mProgress != null && !mProgress.isShowing()) {
                            mProgress.setMessage("请稍后...");
                            mProgress.show();
                        } else {
                            mProgress = CustomProgressDialog.createDialog(context);
                            mProgress.setMessage("请稍后...");
                            mProgress.setCancelable(true);
                            mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    mProgress.dismiss();
                                }
                            });
                            mProgress.show();
                        }

                        String url = FXConstant.URL_DELETEDYNAMICPUSH;
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (mProgress != null && mProgress.isShowing()) {
                                    mProgress.dismiss();
                                }

                                Log.e("Dynamicac,s", "删除结果s=" + s);
                                JSONObject object = JSON.parseObject(s);
                                String code = object.getString("code");

                                if (code.equals("SUCCESS")) {

                                    Toast.makeText(context, "已取消派单", Toast.LENGTH_SHORT).show();
                                    users.remove(position);
                                    notifyDataSetChanged();

                                } else {
                                    Toast.makeText(context, "网络不稳定，请稍后再试", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                if (mProgress != null && mProgress.isShowing()) {
                                    mProgress.dismiss();
                                }

                                Toast.makeText(context, "网络不稳定,取消失败", Toast.LENGTH_SHORT).show();
                            }

                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();

                                params.put("dynamicSeq", info.getDynamicSeq());
                                params.put("createTime", info.getCreateTime());
                                params.put("orderState","03");
                                return params;

                            }

                        };

                        MySingleton.getInstance(context).addToRequestQueue(request);
                    }
                }
        );

        builder.show();

/*
        final List<JSONObject> datas = new ArrayList<>();
        if (info.getSum() != null && info.getFromUId() != null) {
            String nowTime = getNowTime();
            long time = Long.valueOf(nowTime) - Long.valueOf(info.getCreateTime());
            if (time < 1000000) {
                Toast.makeText(context.getApplicationContext(), "转发有反馈的动态24小时之后才能删除！", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        JSONArray array = info.getArray();
        for (int i = 0; i < array.size(); i++) {
            JSONObject data = array.getJSONObject(i);
            datas.add(data);
        }

        final boolean[] canDelete = {true};
        final boolean[] hasOrder = {false};
        final boolean[] finishTime = {true};
        final boolean[] finishOrder = {true};
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确认删除吗?");
        builder.setTitle("确认");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (mProgress != null && !mProgress.isShowing()) {
                            mProgress.setMessage("正在删除,请稍后...");
                            mProgress.show();
                        } else {
                            mProgress = CustomProgressDialog.createDialog(context);
                            mProgress.setMessage("正在删除,请稍后...");
                            mProgress.setCancelable(true);
                            mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    mProgress.dismiss();
                                }
                            });
                            mProgress.show();
                        }
                        String nowTime = getNowTime();
                        long time = Long.valueOf(nowTime) - Long.valueOf(info.getCreateTime());
                        if (time < 10000) {
                            LayoutInflater inflaterDl = LayoutInflater.from(context);
                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                            final Dialog dialog2 = new AlertDialog.Builder(context, R.style.Dialog).create();
                            dialog2.show();
                            dialog2.getWindow().setContentView(layout);
                            dialog2.setCanceledOnTouchOutside(false);
                            dialog2.setCancelable(false);
                            TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                            Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                            tv_title.setText("发布的需求动态一小时之后才可以删除");
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                    if (mProgress != null && mProgress.isShowing()) {
                                        mProgress.dismiss();
                                    }
                                }
                            });
                        } else if (datas == null || datas.size() == 0) {
                            String url = FXConstant.URL_DELETE_PUBLISH;
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    if (mProgress != null && mProgress.isShowing()) {
                                        mProgress.dismiss();
                                    }
                                    Log.e("Dynamicac,s", "删除结果s=" + s);
                                    JSONObject object = JSON.parseObject(s);
                                    String code = object.getString("code");
                                    if (code.equals("SUCCESS")) {
                                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                        users.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    if (mProgress != null && mProgress.isShowing()) {
                                        mProgress.dismiss();
                                    }
                                    Toast.makeText(context, "网络连接错误,删除失败", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("dynamicSeq", info.getDynamicSeq());
                                    params.put("createTime", info.getCreateTime());
                                    return params;
                                }
                            };
                            MySingleton.getInstance(context).addToRequestQueue(request);
                        } else {
                            for (int i = 0; i < datas.size(); i++) {
                                final String state = datas.get(i).getString("state");
                                Log.e("Dynamicac,state", state);
                                if (state.equals("1")) {
                                    canDelete[0] = false;
                                    finishTime[0] = false;
                                    finishOrder[0] = false;
                                    hasOrder[0] = true;
                                    final String orderId = datas.get(i).getString("order_id");
                                    String url = FXConstant.URL_Order_Detail;
                                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {
                                            JSONObject object1 = JSONObject.parseObject(s);
                                            JSONObject object = object1.getJSONObject("odi");
                                            String resv4 = object.getString("resv4");
                                            if (resv4 != null && !"".equals(resv4) && !resv4.equalsIgnoreCase("null")) {
                                                finishOrder[0] = true;
                                                String time1 = resv4.substring(0, 4);
                                                String time2 = resv4.substring(5, 7);
                                                String time3 = resv4.substring(8, 10);
                                                String nowTime = getNowTime2();
                                                String nowTime1 = nowTime.substring(0, 4);
                                                String nowTime2 = nowTime.substring(4, 6);
                                                String nowTime3 = nowTime.substring(6, 8);
                                                int day1 = (Integer.parseInt(nowTime1) - Integer.parseInt(time1)) * 365;
                                                int day2 = (Integer.parseInt(nowTime2) - Integer.parseInt(time2)) * 30;
                                                int day3 = (Integer.parseInt(nowTime3) - Integer.parseInt(time3));
                                                if (day1 + day2 + day3 >= 15) {
                                                    Log.e("Dynamicac,s", "完成时间不足，无法删除");
                                                    canDelete[0] = true;
                                                    finishTime[0] = true;
                                                } else {
                                                    canDelete[0] = false;
                                                    finishTime[0] = false;
                                                }
                                            } else {
                                                Log.e("Dynamicac,s", "resv4不为空无法删除");
                                                canDelete[0] = false;
                                                finishOrder[0] = false;
                                            }
                                            if (!canDelete[0]) {
                                                LayoutInflater inflaterDl = LayoutInflater.from(context);
                                                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                                                final Dialog dialog2 = new AlertDialog.Builder(context, R.style.Dialog).create();
                                                dialog2.show();
                                                dialog2.getWindow().setContentView(layout);
                                                dialog2.setCanceledOnTouchOutside(false);
                                                dialog2.setCancelable(false);
                                                TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                                                Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                                                if (hasOrder[0] && !finishOrder[0]) {
                                                    tv_title.setText("该动态有相关交易未完成,暂时无法删除");
                                                } else if (hasOrder[0] && finishOrder[0] && !finishTime[0]) {
                                                    tv_title.setText("该需求相关交易完成后两周才允许删除动态");
                                                } else {
                                                    tv_title.setText("发布的需求动态一小时之后才可以删除");
                                                }
                                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog2.dismiss();
                                                        if (mProgress != null && mProgress.isShowing()) {
                                                            mProgress.dismiss();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Log.e("Dynamicac,s", "开始删除");
                                                String url = FXConstant.URL_DELETE_PUBLISH;
                                                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String s) {
                                                        if (mProgress != null && mProgress.isShowing()) {
                                                            mProgress.dismiss();
                                                        }
                                                        Log.e("Dynamicac,s", "删除结果s=" + s);
                                                        JSONObject object = JSON.parseObject(s);
                                                        String code = object.getString("code");
                                                        if (code.equals("SUCCESS")) {
                                                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                                            users.remove(position);
                                                            notifyDataSetChanged();
                                                        } else {
                                                            Toast.makeText(context, "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError volleyError) {
                                                        if (mProgress != null && mProgress.isShowing()) {
                                                            mProgress.dismiss();
                                                        }
                                                        Toast.makeText(context, "网络连接错误,删除失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                }) {
                                                    @Override
                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                        Map<String, String> params = new HashMap<String, String>();
                                                        params.put("dynamicSeq", info.getDynamicSeq());
                                                        params.put("createTime", info.getCreateTime());
                                                        return params;
                                                    }
                                                };
                                                MySingleton.getInstance(context).addToRequestQueue(request);
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            Toast.makeText(context.getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("orderId", orderId);
                                            return params;
                                        }
                                    };
                                    MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
                                    break;
                                }
                            }
                        }

                    }
                }
        );

        builder.show();

        */
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    private String getNowTime2() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
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

    private void queryIsContact(final MySendPaidanInfo info, final Dialog dialog) {
        String url = FXConstant.URL_SELECT_CONTACT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "queryIsContact onResponse" + s);
                JSONObject jsonObject = JSON.parseObject(s);
                JSONArray users_temp = jsonObject.getJSONArray("list");
                if (users_temp.size() == 0) {
                    dialog.dismiss();
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
                    title2.setText("提示:");
                    btnOK2.setText("确定");
                    btnCancel2.setText("取消");
                    title_tv2.setText("线下交易以电子单据的方式保存可以更有效的保障质量、售后等，是否确定删除？");
                    btnCancel2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    btnOK2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletePaidan(info);
                            dialog2.dismiss();
                        }
                    });
                } else {
                    ToastUtils.showNOrmalToast(context.getApplicationContext(), "订单正在进行，无法删除");
                    dialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "queryIsContact onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("s_id", info.getsId());
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void deletePaidan(final MySendPaidanInfo info) {
        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_DELETE_PAIDAN_BY_ID;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "删除派单" + s);
                users.remove(info);
                handler.sendEmptyMessage(1);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "deletePaidan volleyError" + volleyError.getMessage());
                ToastUtils.showNOrmalToast(context, "删除订单失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("s_id", info.getsId());
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private boolean isPlayer = false;

    private void playVoice(String file, final ImageView imageView) {
        if (isPlayer) {
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(FXConstant.URL_UPLOAD_SPEED + "/" + file); // 设置数据源
                mMediaPlayer.prepare(); // prepare自动播放
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                        animationDrawable.stop();
                        imageView.setImageResource(R.drawable.ease_chatfrom_voice_playing_f3);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isNoOneAccept(final SendToMePaidan info) {
        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_QUERY_PAIDAN_BY_ID;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "查询派单" + s);
                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    org.json.JSONObject object2 = object.getJSONObject("speedList");
                    String display = object2.isNull("display") ? "00" : object2.getString("display");
                    if (display.equalsIgnoreCase("01")) {
                        showDialog("已错过");
                        PaidanAdapter.this.updatePaidanPush(info.getsId(), DemoHelper.getInstance().getCurrentUsernName());
                        paidanList.remove(info);
                        handler.sendEmptyMessage(1);
                    } else {
                        callPhone(info);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "isNoOneAccept volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("s_id", info.getsId());
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
        return false;
    }

    private void callPhone(final SendToMePaidan info) {
        PermissionUtil permissionUtil = new PermissionUtil((PaidanListActivity) context);
        permissionUtil.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                new PermissionListener() {
                    @Override
                    public void onGranted() {
                        UserPermissionUtil.getUserPermission(context, DemoHelper.getInstance().getCurrentUsernName(), "2", new UserPermissionUtil.UserPermissionListener() {
                            @Override
                            public void onAllow() {
                                //改变派单状态
                                info.setDisplay("01");
                                handler.sendEmptyMessage(1);
                                updatePaidan(info.getsId(), null, info.getDistance(), info.getContent(), false);
                                paidanList.remove(info);
                                inserContact(info);
                                //所有权限都已经授权
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                //url:统一资源定位符
                                //uri:统一资源标示符（更广）
                                intent.setData(Uri.parse("tel:" + info.getuId()));
                                //开启系统拨号器
                                context.startActivity(intent);
                            }

                            @Override
                            public void onBan() {
                                ToastUtils.showNOrmalToast(context.getApplicationContext(), "您的账户已被禁止打聊天");

                            }
                        });

                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        //Toast第一个被拒绝的权限
                        Toast.makeText(context.getApplicationContext(), "您拒绝了拨打电话的权限！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        //Toast第一个勾选不在提示的权限
                        Toast.makeText(context.getApplicationContext(), "您拒绝了拨打电话的权限,请前往设置手动打开！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void inserContact(final SendToMePaidan info) {
        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_INSERT_CONTACT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "inserContact" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "inserContact volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sId", info.getsId());
                params.put("uId", DemoHelper.getInstance().getCurrentUsernName());
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void showDialog(String s) {
        LayoutInflater inflaterDl = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_chating_paidan, null);
        final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        TextView title = (TextView) layout.findViewById(R.id.title_tv);
        title.setText(s);
        Button yes = (Button) layout.findViewById(R.id.btn_ok);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((PaidanListActivity) context).updateAllPaidanWithNoCreateNewAdapter();
            }
        });
    }

    private void resendPaidan(final MySendPaidanInfo info) {
        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_UPDATE_KUAISUDINGDAN;
        //http://192.168.1.120/speedList/updateList?sId=62&uId=15513994458&content=123&label=&log=1&lat=1&state=00&display=01&distance=200&totalNumber=300
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "resendPaidan" + s);
                ((PaidanListActivity) context).updateAllPaidanWithNoCreateNewAdapter();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "resendPaidan volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sId", info.getsId());
                params.put("uId", info.getuId());
                params.put("content", info.getContent());
                params.put("display", "00");
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    @Override
    public int getItemCount() {
        return type == 1 ? users.size() : paidanList.size();

    }


    private void addDisstance() {
        for (MySendPaidanInfo info : users) {
            if (info.getDisplay().equalsIgnoreCase("01")) {
                info.setDisstanceAddTime(0);
                continue;
            }
            //during 90s
            info.setDisstanceAddTime(info.getDisstanceAddTime() + 1);
            if (info.getDisstanceAddTime() == 10) {
                info.setDistance(String.valueOf(Double.valueOf(info.getDistance()) + 1.5));
            } else if (info.getDisstanceAddTime() == 45) {
                info.setDistance(String.valueOf(Double.valueOf(info.getDistance()) + 3));
            } else if (info.getDisstanceAddTime() == 78) {
                info.setDistance(String.valueOf(Double.valueOf(info.getDistance()) + 5));
                updatePaidan(info.getsId(), "01", info.getDistance(), info.getContent(), true);
            }
        }
    }


    private void updatePaidan(final String sId, final String display, final String disstance, final String content, final boolean isReflash) {
        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_UPDATE_KUAISUDINGDAN;
        //http://192.168.1.120/speedList/updateList?sId=62&uId=15513994458&content=123&label=&log=1&lat=1&state=00&display=01&distance=200&totalNumber=300
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "更新派单" + s);
                if (isReflash) {
                    ((PaidanListActivity) context).updateAllPaidanWithNoCreateNewAdapter();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "updatePaidan volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sId", sId);
                params.put("content", content);
                if (display != null) {
                    params.put("display", display);
                }
                params.put("distance", disstance);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void updatePaidanPush(final String sId, final String uid) {
        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_UPDATE_KUAIPAI_PUSH;
        //http://192.168.1.120/speedList/updateList?sId=62&uId=15513994458&content=123&label=&log=1&lat=1&state=00&display=01&distance=200&totalNumber=300
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "更新派单推送" + s);
                //((PaidanListActivity) context).updateAllPaidanWithNoCreateNewAdapter();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "更新派单推送 volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sId", sId);
                params.put("uId", uid);
                params.put("type", "01");
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        isRun = false;
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void setReactTime(int countResponse, Double sumResponse) {
        if (sumResponse == null ) {
            reactTime = null;
            return;
        }
        try {
            int ss = (int) (sumResponse * 60.0) / countResponse;
            if (ss <= 60) {
                reactTime = ss + "s";
            } else if (3600 >= ss && ss > 60) {
                reactTime = Integer.valueOf(ss/60) + "m";
            } else {
                double dou = (Double.valueOf(ss) / 3600.00);
                String str = String.format("%.1f", dou);//format 返回的是字符串
                reactTime = str + "h";
            }
        } catch (Exception e) {
            e.printStackTrace();
            reactTime = null;
        }
    }
    public void updateAllPaidanPush() {
        if (paidanList == null || paidanList.size() == 0) {
            return;
        }
        Log.d("chen", "我要开始删除推送消息了！！！");
        for (SendToMePaidan info : paidanList) {
            if (info.getDisplay().equalsIgnoreCase("01")) {
                updatePaidanPush(info.getsId(), DemoHelper.getInstance().getCurrentUsernName());
            }
        }
    }


}
