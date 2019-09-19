package com.darwindeveloper.onecalendar.clases;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.darwindeveloper.onecalendar.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DARWIN on 1/3/2017.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewDayHolder> {
    private Context context;
    private ArrayList<Day> dias;
    private ArrayList<Object> memoArrs;
    private ArrayList<Object> comToPersArrs;
    private int textColorSelectedDay, backgroundColorSelectedDay;

    public CalendarAdapter(Context context, ArrayList<Day> dias, int textColorSelectedDay, int backgroundColorSelectedDay,ArrayList<Object> memoArrs,ArrayList<Object> comToPersArrs) {
        this.context = context;
        this.dias = dias;
        this.memoArrs = memoArrs;
        this.comToPersArrs = comToPersArrs;
//        this.textColorSelectedDay = textColorSelectedDay;
//        this.backgroundColorSelectedDay = backgroundColorSelectedDay;
    }

    @Override
    public ViewDayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendar, parent, false);
        return new ViewDayHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewDayHolder holder, final int position) {
        final Day dia = dias.get(position);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dia.getDate());
        int nday = cal.get(Calendar.DAY_OF_MONTH);

        holder.dia.setText(nday + "");
        if (dia.isValid()) {
            holder.tvType.setVisibility(View.VISIBLE);
            Log.e("day==",position+"="+dia.isPerson()+"="+dia.isLegal());

            if (dia.isPerson()) {
                //个人的
                if (dia.isLegal()) {
                    if (dia.getStatus().equals("0")) {
                        holder.itemView.setBackgroundColor(0x00000000);
                        holder.dia.setTextColor(0xffC6C6C6);
                        holder.tvType.setTextColor(0xffC6C6C6);
                        holder.tvType.setText("休");
                    } else if (dia.getStatus().equals("1") || dia.getStatus().equals("2")) {
                        holder.dia.setTextColor(0xffffffff);
                        holder.tvType.setTextColor(0xffffffff);
                        if (dia.getStatus().equals("1")) {
                            holder.tvType.setText("班");
                        } else {
                            holder.tvType.setText("集");
                        }
                        if (dia.getWorkStatus().equals("01")) {
                            if (dia.isSelected()) {
                                holder.llItem.setBackgroundResource(R.drawable.bg_late_selected);
                                holder.dia.setTextColor(0xff020202);
                                holder.tvType.setTextColor(0xff020202);
                            } else {
                                holder.llItem.setBackgroundResource(R.drawable.bg_late_normal);
                            }
                        }else if (dia.getWorkStatus().equals("02")) {
                            if (dia.isSelected()) {
                                holder.llItem.setBackgroundResource(R.drawable.bg_leave_selected);
                                holder.dia.setTextColor(0xff020202);
                                holder.tvType.setTextColor(0xff020202);
                            } else {
                                holder.llItem.setBackgroundResource(R.drawable.bg_leave_normal);
                            }
                        }else if (dia.getWorkStatus().equals("03")) {
                            if (dia.isSelected()) {
                                holder.llItem.setBackgroundResource(R.drawable.bg_trip_selected);
                                holder.dia.setTextColor(0xff020202);
                                holder.tvType.setTextColor(0xff020202);
                            } else {
                                holder.llItem.setBackgroundResource(R.drawable.bg_trip_normal);
                            }
                        }else if (dia.getWorkStatus().equals("04")) {
                            if (dia.isSelected()) {
                                holder.llItem.setBackgroundResource(R.drawable.bg_work_selected);
                                holder.dia.setTextColor(0xff020202);
                                holder.tvType.setTextColor(0xff020202);
                            } else {
                                holder.llItem.setBackgroundResource(R.drawable.bg_work_normal);
                            }
                        }
                    }
                } else {
                    holder.itemView.setBackgroundColor(0x00000000);
                    holder.dia.setTextColor(0xffC6C6C6);
                    holder.tvType.setTextColor(0xffC6C6C6);
                    if (dia.getStatus().equals("0")) {
                        holder.tvType.setText("休");
                    } else if (dia.getStatus().equals("1")) {
                        holder.tvType.setText("班");
                    } else if (dia.getStatus().equals("2")) {
                        holder.tvType.setText("集");
                    }
                }
            } else {

                //企业的

                holder.dia.setTextColor(0xffffffff);
                if (dia.getStatus().equals("0")) {
                    holder.tvType.setText("休");
                    if (dia.isSelected()) {
                        holder.dia.setTextColor(0xff343132);
                        holder.tvType.setTextColor(0xff343132);
                        holder.llItem.setBackgroundResource(R.drawable.bg_rest_selected);
                    } else {
                        holder.llItem.setBackgroundResource(R.drawable.bg_rest_normal);
                    }
                } else if (dia.getStatus().equals("1")) {
                    holder.tvType.setText("班");
                    if (dia.isSelected()) {
                        holder.dia.setTextColor(0xff343132);
                        holder.tvType.setTextColor(0xff343132);
                        holder.llItem.setBackgroundResource(R.drawable.bg_work_selected);
                    } else {
                        holder.llItem.setBackgroundResource(R.drawable.bg_work_normal);
                    }
                } else if (dia.getStatus().equals("2")) {
                    holder.tvType.setText("集");
                    if (dia.isSelected()) {
                        holder.dia.setTextColor(0xff343132);
                        holder.tvType.setTextColor(0xff343132);
                        holder.llItem.setBackgroundResource(R.drawable.bg_gather_selected);
                    } else {
                        holder.llItem.setBackgroundResource(R.drawable.bg_gather_normal);
                    }
                }

                if (dia.getTypeIdentify() != null && dia.getTypeIdentify().equals("设置企业")){
                    //设置企业的时候备忘录黑点的提示
                    holder.memoIdentify.setVisibility(View.INVISIBLE);

                    //首页展示企业的时候备忘录的黑点提示
                    if (memoArrs.size()>0){

                        int year = cal.get(Calendar.YEAR) ;
                        int currentYear = year-1900;
                        int month = cal.get(Calendar.MONTH)+1;
                        String currentMonth = "";
                        if (month<10){
                            currentMonth = "0"+month;
                        }else {
                            currentMonth = ""+month;
                        }

                        String currentDay = "";
                        if (nday<10){
                            currentDay = "0"+nday;
                        }else {
                            currentDay = nday+"";
                        }

                        for (Object object : memoArrs){

                            try {

                                JSONObject object1 = new JSONObject(object.toString());

                                String memoTime = object1.getString("memotime");

                                if (memoTime.equals(currentYear+currentMonth+currentDay)){

                                    if (dia.isSelected()) {

                                        holder.left_memoIdentify.setBackgroundResource(R.drawable.bg_drop_black);

                                    }else {
                                        holder.left_memoIdentify.setBackgroundResource(R.drawable.bg_drop_white);
                                    }

                                    holder.left_memoIdentify.setVisibility(View.VISIBLE);

                                }else {
                                    holder.left_memoIdentify.setVisibility(View.INVISIBLE);
                                }


                            }catch (Exception e){


                            }

                        }

                    }


                    if (comToPersArrs.size()>0){

                        int year = cal.get(Calendar.YEAR) ;
                        int currentYear = year-1900;
                        int month = cal.get(Calendar.MONTH)+1;
                        String currentMonth = "";
                        if (month<10){
                            currentMonth = "0"+month;
                        }else {
                            currentMonth = ""+month;
                        }

                        String currentDay = "";
                        if (nday<10){
                            currentDay = "0"+nday;
                        }else {
                            currentDay = nday+"";
                        }

                        for (Object object : comToPersArrs){

                            try {

                                JSONObject object1 = new JSONObject(object.toString());

                                String memoTime = object1.getString("memotime");

                                if (memoTime.equals(currentYear+currentMonth+currentDay)){

                                    if (dia.isSelected()) {

                                        holder.right_memoIdentify.setBackgroundResource(R.drawable.bg_drop_black);

                                    }else {
                                        holder.right_memoIdentify.setBackgroundResource(R.drawable.bg_drop_white);
                                    }

                                    holder.right_memoIdentify.setVisibility(View.VISIBLE);

                                }else {
                                    holder.right_memoIdentify.setVisibility(View.INVISIBLE);
                                }

                            }catch (Exception e){

                            }
                        }

                    }

                }else {

                    //首页展示企业的时候备忘录的黑点提示
                    if (memoArrs.size()>0){

                        int year = cal.get(Calendar.YEAR) ;
                        int currentYear = year-1900;
                        int month = cal.get(Calendar.MONTH)+1;
                        String currentMonth = "";
                        if (month<10){
                            currentMonth = "0"+month;
                        }else {
                            currentMonth = ""+month;
                        }

                        String currentDay = "";
                        if (nday<10){
                            currentDay = "0"+nday;
                        }else {
                            currentDay = nday+"";
                        }

                        for (Object object : memoArrs){

                            try {

                                JSONObject object1 = new JSONObject(object.toString());

                                String memoTime = object1.getString("memotime");

                                if (memoTime.equals(currentYear+currentMonth+currentDay)){

                                    if (dia.isSelected()) {

                                        holder.memoIdentify.setBackgroundResource(R.drawable.bg_drop_black);

                                    }else {
                                        holder.memoIdentify.setBackgroundResource(R.drawable.bg_drop_white);
                                    }

                                    holder.memoIdentify.setVisibility(View.VISIBLE);

                                }else {
                                    holder.memoIdentify.setVisibility(View.INVISIBLE);
                                }


                            }catch (Exception e){


                            }

                        }

                    }

                }

            }

        } else {
            holder.tvType.setVisibility(View.GONE);
            holder.dia.setTextColor(0xffD9D9D9);
            holder.itemView.setBackgroundColor(0x00000000);

            holder.dia.setText("");
            holder.memoIdentify.setVisibility(View.INVISIBLE);

        }
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayOnClickListener.dayOnClick(dia, position);
            }
        });
        holder.llItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dayOnClickListener.dayOnLongClik(dia, position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return dias.size();
    }


    public class ViewDayHolder extends RecyclerView.ViewHolder {
        LinearLayout llItem;
        TextView dia;
        TextView tvType;
        TextView memoIdentify;
        TextView left_memoIdentify;
        TextView right_memoIdentify;

        public ViewDayHolder(View itemView) {
            super(itemView);
            llItem = (LinearLayout) itemView.findViewById(R.id.ll_item);
            dia = (TextView) itemView.findViewById(R.id.textViewDay);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            memoIdentify = (TextView) itemView.findViewById(R.id.memoIdentify);
            left_memoIdentify = (TextView) itemView.findViewById(R.id.left_memoIdentify);
            right_memoIdentify = (TextView) itemView.findViewById(R.id.right_memoIdentify);
        }
    }


    public interface DayOnClickListener {
        /**
         * un objeto de tipo day para obtener la fecha (año,mes,dia) con un objeto calendar
         * <p>
         * otros metodos como setBackgroundColor(int backgroundColor) y getBackgroundColor() color del fondo del numero de dia del mes
         * setTextColor(int textColor) y getTextColor() color del texto numero de dia del mes
         *
         * @param day
         */
        void dayOnClick(Day day, int position);

        /**
         * similar a dayOnClick solo que este se ejecuta cuando haya un clic prolongado
         *
         * @param day
         */
        void dayOnLongClik(Day day, int position);


    }


    private DayOnClickListener dayOnClickListener;

    public void setDayOnClickListener(DayOnClickListener dayOnClickListener) {
        this.dayOnClickListener = dayOnClickListener;
    }


}
