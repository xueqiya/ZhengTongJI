package com.sangu.apptongji.main.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.TradeTrackInfo;
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.widget.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-01-24.
 */

public class TradeTrackAdapter extends RecyclerView.Adapter<TradeTrackAdapter.ViewHolder> {
    private int type;
    private List<TradeTrackInfo> list = new ArrayList<>();
    private Context context;
    private String lng;
    private String lat;
   // MediaPlayer mMediaPlayer;
    public TradeTrackAdapter(Context context, List<JSONObject> jsonArray, int type, String lat, String lng) {
        this.context = context;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        //Log.d("chen", "TradeTrackAdapter" + type);
        for (JSONObject json2 : jsonArray) {
            // 如果数据出错....
            if (json2 == null || json2.size() == 0) {
                continue;
            }
            TradeTrackInfo info = new TradeTrackInfo();
            String uId = json2.getString("uId");
            info.setuId(uId);

            JSONObject json = json2.getJSONObject("dynamicInfo");

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
            info.setStatusR(statusR);
            info.setDynamicSeq(dynamicSeq);
            info.setuLoginId(uLoginId);
            info.setCreateTime(createTime);
            info.setUserId(userId);
            info.setContent(content);
            info.setLocation(location);
            info.setAuthtype(authtype);
            info.setViews(views);
            info.setTask_label(task_label);
            info.setTask_position(task_position);
            info.setTask_locaName(task_locaName);
            info.setTask_jurisdiction(task_jurisdiction);
            info.setOrderState(orderState);
            info.setDeviceType(deviceType);
            info.setDynamicSeqR(dynamicSeqR);
            info.setCreateTimeR(createTimeR);
            info.setTypeR(typeR);
            info.setuIdR(uIdR);
            info.setResponsetimeR(responsetimeR);
            info.setOrdernumR(ordernumR);
            info.setTimestampR(timestampR);
            info.setAcceptNum(acceptNum);
            /*info.setsId(TextUtils.isEmpty(json.getString("sId")) ? "" : json.getString("sId"));
            info.setfId(TextUtils.isEmpty(json.getString("fId")) ? "" : json.getString("fId"));
            info.setTimestamp(TextUtils.isEmpty(json.getString("timestamp")) ? "" : json.getString("timestamp"));*/

            /*JSONObject json2 = json.getJSONObject("dynamicInfo");

            info.setuId(TextUtils.isEmpty(json2.getString("uId")) ? "" : json2.getString("uId"));
            info.setContent(TextUtils.isEmpty(json2.getString("content")) ? "" : json2.getString("content"));

            info.setTotalNumber(TextUtils.isEmpty(json2.getString("totalNumber")) ? "" : json2.getString("totalNumber"));
            info.setDisplay(TextUtils.isEmpty(json2.getString("display")) ? "" : json2.getString("display"));
            info.setState(TextUtils.isEmpty(json2.getString("state")) ? "" : json2.getString("state"));
            info.setFile(TextUtils.isEmpty(json2.getString("file")) ? "" : json2.getString("file"));
            info.setFile(TextUtils.isEmpty(json2.getString("file")) ? "" : json2.getString("file"));
            info.setLog(TextUtils.isEmpty(json2.getString("log")) ? "" : json2.getString("log"));
            info.setLat(TextUtils.isEmpty(json2.getString("lat")) ? "" : json2.getString("lat"));
            info.setTelephone(TextUtils.isEmpty(json2.getString("telephone")) ? "" : json2.getString("telephone"));
            info.setFinishTime(TextUtils.isEmpty(json2.getString("finishTime")) ? "" : json2.getString("finishTime"));
            JSONObject json3 = json2.getJSONObject("userInfo");
            info.setuName(TextUtils.isEmpty(json3.getString("uName")) ? "" : json3.getString("uName"));
            info.setuImage(TextUtils.isEmpty(json3.getString("uImage")) ? "" : json3.getString("uImage"));
            info.setuTelephone(TextUtils.isEmpty(json3.getString("uTelephone")) ? "" : json3.getString("uTelephone"));
            info.setDeviceType(TextUtils.isEmpty(json3.getString("deviceType")) ? "" : json3.getString("deviceType"));
            String distance = getDistance(info);*/
            String[] latlng = info.getTask_position().split("\\|");
            String distance = getDistance(latlng[0], latlng[1]);
            info.setDistance(distance);
            list.add(info);
        }
        //mMediaPlayer = new MediaPlayer();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_send_paidan, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TradeTrackInfo info = list.get(position);
        holder.tv_from.setText("消息来源：" + getFromAppName(info.getDeviceType()));
        holder.tv_title.setText(info.getTask_label());
        holder.tv_miaoshu.setText(info.getContent());
        if (TextUtils.isEmpty(info.getDistance())) {
            holder.tv_distance.setVisibility(View.GONE);
        } else {

            holder.tv_distance.setText(info.getDistance() + "km");
        }
        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DynaDetaActivity.class).putExtra("dynamicSeq", info.getDynamicSeq()).putExtra("createTime", info.getCreateTime()).putExtra("dType", "05"));
            }
        });
        holder.tv_miaoshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DynaDetaActivity.class).putExtra("dynamicSeq", info.getDynamicSeq()).putExtra("createTime", info.getCreateTime()).putExtra("dType", "05"));
            }
        });
        String resopnse = info.getResponsetimeR();
        if (!TextUtils.isEmpty(resopnse)) {
            int ss = (int) (Double.valueOf(resopnse) * 60);
            if (Double.valueOf(resopnse) > 1440) {
                holder.tv_show.setText("24");
                holder.tv_mini.setText("00");
            } else {
                String[] showTimes = getSpiltTime(ss).split("-");
                holder.tv_show.setText(showTimes[0]);
                holder.tv_mini.setText(showTimes[1]);
            }
        }

        if (!TextUtils.isEmpty(info.getOrdernumR())) {
            holder.tv_catch.setText("第" + info.getOrdernumR() + "个接收");
        } else {
            holder.tv_catch.setVisibility(View.GONE);
        }
        holder.iv_show_bubble.setVisibility(View.GONE);

        if (type == 0) {
            holder.btn_left.setText("确认接单");
            holder.btn_right.setText("否认接单");

            holder.btn_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(info,1);
                }
            });
            holder.btn_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(info,0);
                }
            });
        } else if (type == 1) {
            holder.btn_left.setText("已确认");
            holder.btn_right.setText("否认接单");
        } else if (type == 2) {
            holder.btn_left.setText("确认接单");
            holder.btn_right.setText("已否认");
        }
        /*final TradeTrackInfo info = list.get(position);
        holder.name.setText(info.getuName());
        if (Double.valueOf(info.getDistance()) > 50) {
            holder.tv_distance.setText("50km以外");
        } else {
            holder.tv_distance.setText(info.getDistance() + "km");
        }
        holder.tv_time.setVisibility(View.GONE);

        ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+info.getuImage().split("\\|")[0],holder.cv_avatar, DemoApplication.mOptions);
        if (TextUtils.isEmpty(info.getContent())) {
            holder.tv_need.setVisibility(View.GONE);
            holder.rl_send_me_play_voice.setVisibility(View.VISIBLE);
            holder.rl_send_me_play_voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(info.getFile())) {
                        ToastUtils.showNOrmalToast(context.getApplicationContext(),"语音获取失败");
                        return;
                    }
                    holder.iv_voice.setImageResource(R.drawable.voice_play);
                    AnimationDrawable animationDrawable = (AnimationDrawable) holder.iv_voice.getDrawable();
                    animationDrawable.start();
                    playVoice(info.getFile(), holder.iv_voice);
                }
            });
        } else {
            holder.rl_send_me_play_voice.setVisibility(View.GONE);
            holder.tv_need.setVisibility(View.VISIBLE);
            holder.tv_need.setText(info.getContent());
        }


        holder.tv_from.setText("来源：" + getFromAppName(info.getDeviceType()));
        if (type == 0) {
            holder.btn_dianhua.setText("确认接单");
            holder.btn_weizhi.setText("否认接单");

            holder.btn_weizhi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(info,1);
                }
            });
            holder.btn_dianhua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(info,0);
                }
            });
        } else if (type == 1) {
            holder.btn_dianhua.setText("已确认");
            holder.btn_weizhi.setText("否认接单");
        } else if (type == 2) {
            holder.btn_dianhua.setText("确认接单");
            holder.btn_weizhi.setText("已否认");
        }*/
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

    public void showDialog(final TradeTrackInfo info, int type) {
        LayoutInflater inflaterDl = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_trade_track, null);
        final Dialog dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
        TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
        Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (type == 0) {
            title_tv.setText("是否确定与该用户完成交易?");

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upDatePaidan(info,"02");
                    updateContact(info, "02");
                    pushToUser(info);
                    dialog.dismiss();
                }
            });
        } else {
            title_tv.setText("是否确认您未与该用户进行交易?");
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushToUser(info);
                    updateContact(info, "03");
                    dialog.dismiss();
                }
            });
        }
    }

    private void pushToUser(final TradeTrackInfo info) {
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "推送师傅派单" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "pushToShiFu volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("u_id", info.getUserId());
                params.put("userId", info.getUserId());
                params.put("body", "动态派单消息");
                params.put("type", "18");
                params.put("companyId", "0");
                params.put("companyName", "0");
                params.put("companyAdress", "0");
                params.put("dynamicSeq", info.getDynamicSeq());
                params.put("createTime", info.getCreateTime());
                params.put("fristId", "00");
                params.put("dType", "0");
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void updateContact(final TradeTrackInfo info, final String state) {
        String url = FXConstant.URL_UPDATE_DEAL_CONTACT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                list.remove(info);
                notifyDataSetChanged();
                Log.d("chen", "更新师傅派单" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.d("chen", "updateContact volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //dynamicSeq=&createTime=&uId=&contactId=&type=
                Map<String, String> params = new HashMap<String, String>();
                params.put("dynamicSeq",info.getDynamicSeq() );
                params.put("createTime", info.getCreateTime());
                params.put("uId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("contactId", info.getUserId());
                params.put("type", state);

                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    private void upDatePaidan(final TradeTrackInfo info, final String ss) {
        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_UPDATE_DYNAMIC_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "upDatePaidan更新派单" + s);

                notifyDataSetChanged();

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
                params.put("dynamicSeq",info.getDynamicSeq() );
                params.put("createTime", info.getCreateTime());
                params.put("orderState","02");
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, tv_time, tv_need, tv_from, tv_distance,tv_title,tv_catch,tv_show,tv_miaoshu,tv_mini,tv_paidan_state,tv_paidan_finish_time;
        Button btn_dianhua, btn_weizhi,btn_right,btn_left;
        RelativeLayout rl_send_me_play_voice,rl_time_contain;
        LinearLayout ll_finish_paidan_contain;
        CircleImageView cv_avatar;
        ImageView iv_voice,iv_show_bubble;

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

        }

    }

    private boolean isPlayer = false;
    /*private void playVoice(String file, final ImageView imageView) {
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

    }*/

    private String getDistance(String latS, String lngS) {
        String paidanLat = latS;
        String paidanLng = lngS;
        //4.9E-324  考虑到百度定位出错时 就不显示
        if (!(TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng) || TextUtils.isEmpty(paidanLat) || TextUtils.isEmpty(paidanLng) ||
                paidanLat.equalsIgnoreCase("4.9E-324") || paidanLng.equalsIgnoreCase("4.9E-324"))) {
            double latitude1 = Double.valueOf(lat);
            double longitude1 = Double.valueOf(lng);
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

   /* private String getDistance(TradeTrackInfo info) {
        String paidanLat = info.getLat();
        String paidanLng = info.getLog();
        //4.9E-324  考虑到百度定位出错时 就不显示
        if (!(TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng) || TextUtils.isEmpty(paidanLat) || TextUtils.isEmpty(paidanLng) ||
                paidanLat.equalsIgnoreCase("4.9E-324") || paidanLng.equalsIgnoreCase("4.9E-324"))) {
            double latitude1 = Double.valueOf(lat);
            double longitude1 = Double.valueOf(lng);
            final LatLng ll1 = new LatLng(Double.parseDouble(paidanLat), Double.parseDouble(paidanLng));
            LatLng ll = new LatLng(latitude1, longitude1);
            double distance = DistanceUtil.getDistance(ll, ll1);
            double dou = distance / 1000;
            String str = String.format("%.1f", dou);//format 返回的是字符串
            return str;
        } else {
            return null;
        }
    }*/


    private String getFromAppName(String deviceType) {
        if (deviceType.contains("正事多")) {
            return "正事多";
        } else if (deviceType.contains("接单全")) {
            return "接单全";
        }else if (deviceType.contains("安装维修")) {
            return "安装维修接单助手";
        }else if (deviceType.contains("家政清洁")) {
            return "同城家政清洁服务大全";
        } else if (deviceType.contains("抢单接活")) {
            return "抢单派单";
        } else {
            return "正事多";
        }
    }
}
