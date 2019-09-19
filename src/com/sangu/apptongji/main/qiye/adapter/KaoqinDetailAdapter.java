package com.sangu.apptongji.main.qiye.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.qiye.entity.QiyeKaoQinDetailInfo;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018-01-09.
 */

public class KaoqinDetailAdapter extends RecyclerView.Adapter<KaoqinDetailAdapter.ViewHolder> {
    public MyItemClickListener mItemClickListener;
    private List<QiyeKaoQinDetailInfo> data = null;
    private Context context;
    private String cType;

    public KaoqinDetailAdapter(Context context, List<QiyeKaoQinDetailInfo> data, String cType) {
        this.context = context;
        this.data = data;
        this.cType = cType;
    }

    @Override
    public KaoqinDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kaoqin_detail, parent, false);
        return new KaoqinDetailAdapter.ViewHolder(v,mItemClickListener);
    }

    @Override
    public void onBindViewHolder(KaoqinDetailAdapter.ViewHolder holder, int position) {
        QiyeKaoQinDetailInfo info = data.get(position);
        if (info.getType() != null && !cType.equalsIgnoreCase("paihang")) {
            if (info.getType().equalsIgnoreCase("workState")) {
                //今天上班时间
                if (TextUtils.isEmpty(getTodayWorkTime(info))) {
                    holder.tv_working_time.setVisibility(View.INVISIBLE);
                } else {
                    holder.tv_working_time.setText("(工作" + getTodayWorkTime(info) + "h)");
                }
            } else if (info.getType().equalsIgnoreCase("lateState")) {
                //今天迟到时间
                //Log.d("chen", "迟到" + info.getSetSignaTime());

                if (info.getMornTime().length() == 0){

                    holder.tv_working_time.setText( "(未签到)");

                }else {

                    if (info.getLateTime() != null){
                        String[] strings = info.getLateTime().split("\\.");
                        holder.tv_working_time.setText( "(迟到" + strings[0] + "分钟)");
                    }else {
                        if (TextUtils.isEmpty(getTodayLateTime(info))) {
                            holder.tv_working_time.setText( "(未签到)");
                        } else {
                            holder.tv_working_time.setText( "(迟到" + getTodayLateTime(info) + "分钟)");
                        }
                    }

                }


            } else if (info.getType().equalsIgnoreCase("overTimeState")) {
            } else if (info.getType().equalsIgnoreCase("leaveState")) {
            }
        } else {
            holder.tv_working_time.setVisibility(View.INVISIBLE);
        }
        //月上班时间
        holder.tv_shangban_time.setText("上班时间(" + getM2H(info.getWorkTime()) + "h)");
        holder.tv_leave.setText("请假情况(" + getM2H(info.getLeaveTime()) + "h)");
        holder.tv_late.setText("迟到情况(" + getM2H(info.getLateTime()) + "h)");
        holder.tv_work_yeji.setText("业绩销量(" + info.getTotalTransAmount() + "元)");
        holder.tv_people_name.setText(TextUtils.isEmpty(info.getuName())?info.getuId() : info.getuName());
        if (!TextUtils.isEmpty(info.getuImage())) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + info.getuImage().split("\\|")[0],holder.iv_avatar, DemoApplication.mOptions);
        }
    }

    private String getTodayLateTime(QiyeKaoQinDetailInfo info) {
        double lateWork = 0;
        //"09:30|17:30|12:00|13:30"
        String[] times = info.getSetSignaTime().split("\\|");
        if (times.length == 1 || info.getSetSignaTime().equalsIgnoreCase("无")) {
            return "";
        }
        //早上上班时间
        String mornWorkTime = times[0];
        //晚上下班时间
        String nightOffWorkTime = times[1];

        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyyMMddHH:mm");
        SimpleDateFormat dateFormater2 = new SimpleDateFormat(
                "yyyyMMdd");
        mornWorkTime =  dateFormater2.format(new Date(System.currentTimeMillis())) + mornWorkTime;
        nightOffWorkTime =  dateFormater2.format(new Date(System.currentTimeMillis())) + nightOffWorkTime;
        try {
            Date date1 = dateFormater.parse(mornWorkTime);
            Date date2 = dateFormater.parse(nightOffWorkTime);
            //如果小于0 表示现在时间已经超过下班时间直接用下班时间减去上班时间
            Log.d("chen", date2.getTime() + "www" + new Date(System.currentTimeMillis()).getTime());
            if (date2.getTime() - new Date(System.currentTimeMillis()).getTime() < 0) {
                lateWork = new Date(System.currentTimeMillis()).getTime() - date1.getTime();
            } else {
                lateWork = new Date(System.currentTimeMillis()).getTime() - date1.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //保留一位小数  转化为分钟
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(1);
        return nf.format(lateWork / (60 * 1000));
    }

    private String getM2H(String workTime) {
        if (Double.valueOf(workTime) == 0) {
            return "0.0";
        }
        //保留一位小数  转化为小时
        double c = Double.valueOf(workTime) / 60.0;
        java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.0");
        return myformat.format(c);
    }

    public String getTodayWorkTime(QiyeKaoQinDetailInfo info) {
        if (TextUtils.isEmpty(info.getMornTime())) {
            return "";
        }
        double todayWorkTime = 0;
        //20180109091356
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        Date d = null;
        Date d2 = null;

        try {
            d = dateFormater.parse(info.getMornTime());

            if (TextUtils.isEmpty(info.getAfternoonTime())) {
                Log.d("chen", "正在上班，并且没有到午休 " + info.getMornTime() + "www" + new Date(System.currentTimeMillis()).getTime() + "  ww " + System.currentTimeMillis() + " www " + d.getTime());
                //正在上班，并且没有到午休
                todayWorkTime = new Date(System.currentTimeMillis()).getTime() - d.getTime();
                Log.d("chen", "正在上班，并且没有到午休2  " + todayWorkTime);
            } else {

                d2 = dateFormater.parse(info.getAfternoonTime());
                todayWorkTime = d2.getTime() - d.getTime();

                if (!TextUtils.isEmpty(info.getNoonTime()) && TextUtils.isEmpty(info.getNightTime())) {
                    Log.d("chen", "午休结束并开始下午的上班,下午还没有下班"+ dateFormater.format(new Date(System.currentTimeMillis())));
                    //午休结束并开始下午的上班,下午还没有下班
                    //下午上班时间
                    d2 = dateFormater.parse(info.getNoonTime());
                    todayWorkTime = todayWorkTime + new Date(System.currentTimeMillis()).getTime() - d2.getTime();
                } else if (!TextUtils.isEmpty(info.getNoonTime()) && !TextUtils.isEmpty(info.getNightTime())) {
                    Log.d("chen", "下午下班了"+ dateFormater.format(new Date(System.currentTimeMillis())));
                    d = dateFormater.parse(info.getNoonTime());
                    d2 = dateFormater.parse(info.getNightTime());
                    //下午下班了
                    todayWorkTime = todayWorkTime + (d.getTime() - d2.getTime());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        double c = Double.valueOf(todayWorkTime) / (60.0 * 1000.0 * 60.0);
        java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.0");
        Log.d("chen", "正在上班，并且没有到午休3  " + c);
        return myformat.format(c);
    }



    @Override
    public int getItemCount() {
        return (data == null || data.size() == 0)?0:data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_people_name,tv_working_time,tv_shangban_time,tv_work_yeji,tv_leave,tv_late;
        private ImageView iv_avatar;
        private MyItemClickListener mListener;

        public ViewHolder(View convertView, MyItemClickListener listener) {
            super(convertView);
            tv_people_name = (TextView) convertView.findViewById(R.id.tv_people_name);
            tv_working_time = (TextView) convertView.findViewById(R.id.tv_working_time);
            tv_shangban_time = (TextView) convertView.findViewById(R.id.tv_shangban_time);
            tv_work_yeji = (TextView) convertView.findViewById(R.id.tv_work_yeji);
            tv_leave = (TextView) convertView.findViewById(R.id.tv_leave);
            tv_late = (TextView) convertView.findViewById(R.id.tv_late);
            iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            this.mListener=listener;
            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getPosition());
            }
        }

    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public interface MyItemClickListener {
        void onItemClick(View view,int position);
    }
}
