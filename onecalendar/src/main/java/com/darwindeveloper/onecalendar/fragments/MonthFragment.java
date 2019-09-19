package com.darwindeveloper.onecalendar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.darwindeveloper.onecalendar.R;
import com.darwindeveloper.onecalendar.clases.CalendarAdapter;
import com.darwindeveloper.onecalendar.clases.Day;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by DARWIN on 3/3/2017.
 */

public class MonthFragment extends Fragment implements CalendarAdapter.DayOnClickListener {
    public static final String MONTH = "com.darwindeveloper.onecalendar.monthfragemnt.month";
    public static final String YEAR = "com.darwindeveloper.onecalendar.monthfragemnt.year";
    public static final String BCDays = "com.darwindeveloper.onecalendar.monthfragemnt.BCDAYS";
    public static final String BCDaysNV = "com.darwindeveloper.onecalendar.monthfragemnt.BCDAYSNV";
    public static final String BCCDay = "com.darwindeveloper.onecalendar.monthfragemnt.BCCDAY";
    public static final String TCDAYS = "com.darwindeveloper.onecalendar.monthfragemnt.TCDAYS";
    public static final String TCDAYSNV = "com.darwindeveloper.onecalendar.monthfragemnt.TCDAYSNV";
    public static final String TCSDAY = "com.darwindeveloper.onecalendar.monthfragemnt.TCSDAY";
    public static final String BCSDAY = "com.darwindeveloper.onecalendar.monthfragemnt.BCSDAY";
    private Context context;
    private View rootView;
    private RecyclerView recyclerViewDays;
    private CalendarAdapter calendarAdapter;
    private ArrayList<Day> days = new ArrayList<>();
    private ArrayList<Object> memoArrs = new ArrayList<>();
    private ArrayList<Object> comToPersArrs = new ArrayList<>();
    private int imonth, iyear, currentDay, backgroundColorDays, backgroundColorDaysNV, backgroundColorCurrentDay, textColorCurrentDayDay, textColorDays, textColorDaysNV;
    private String enero = "Enero";
    private String febrero = "Febrero";
    private String marzo = "Marzo";
    private String abril = "Abril";
    private String mayo = "Mayo";
    private String junio = "Junio";
    private String julio = "Julio";
    private String agosto = "Agosto";
    private String septiembre = "Septiembre";
    private String octubre = "Octubre";
    private String noviembre = "Noviembre";
    private String diciembre = "Deciembre";
    public static Map<String,ArrayList<Day>> monthList=new HashMap<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        imonth = getCurrentMonth();
        iyear = getCurrentYear();
        currentDay = getCurrentDayMonth();
        backgroundColorDays = getArguments().getInt(BCDays);
        backgroundColorDaysNV = getArguments().getInt(BCDaysNV);
        backgroundColorCurrentDay = getArguments().getInt(BCCDay);
        textColorCurrentDayDay = getArguments().getInt(TCSDAY);
        textColorDays = getArguments().getInt(TCDAYS);
        textColorDaysNV = getArguments().getInt(TCDAYSNV);
    }

    public ArrayList<Day> getDaysByMap(int year,int month,String id){
        ArrayList<Day> days=monthList.get(year+getStringMonth(month)+id);
        if(days==null||days.size()==0){
            if(year==getCurrentYear()&&month==getCurrentMonth()){
                return fillUpMonth(year,month,getCurrentDayMonth());
            }else{
                return fillUpMonth(year,month,-1);
            }
        }
        return days;
    }

    public ArrayList<Day> getDaysByMap(int year,int month){
        ArrayList<Day> days=monthList.get(year+getStringMonth(month));
        if(days==null||days.size()==0){
            if(year==getCurrentYear()&&month==getCurrentMonth()){
                return fillUpMonth(year,month,getCurrentDayMonth());
            }else{
                return fillUpMonth(year,month,-1);
            }
        }
        return days;
    }

    public void removeData(HashSet<String> set){
//        for (String date : set) {
//            monthList.remove(date);
//        }
        monthList.clear();

    }

    public ArrayList<Day> getDaysByDate(String date){
        Log.e("key==get=",date);
        ArrayList<Day> days=monthList.get(date);
        return days;
    }


    public void setCompanyMemo( List<Object> memo, List<Object> memo2){

        memoArrs.clear();
        memoArrs.addAll(memo);

        comToPersArrs.clear();
        comToPersArrs.addAll(memo2);

    }

    public void setDaysByMap(String date,ArrayList<Day> days){
        monthList.put(date,days);
        Log.e("key==set=",date);
        calendarAdapter.notifyItemChanged(0, 41);
        calendarAdapter.notifyDataSetChanged();
    }

    public void setDaysByMap2(String date,ArrayList<Day> days){
        monthList.put(date,days);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_month, container, false);
        recyclerViewDays = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerViewDays.setLayoutManager(new GridLayoutManager(context, 7));
        int month = getArguments().getInt(MONTH);
        int year = getArguments().getInt(YEAR);
        //fillUpMonth(month, year);
        days.addAll(getDaysByMap(year,month));
        //days=getDaysByMap(year,month);
        calendarAdapter = new CalendarAdapter(context, days, getArguments().getInt(TCSDAY), getArguments().getInt(BCSDAY),memoArrs,comToPersArrs);
        calendarAdapter.setDayOnClickListener(this);
        recyclerViewDays.setAdapter(calendarAdapter);
        return rootView;
    }

    private void fillUpMonth(int month, int year) {
        String nameFirstDay = getNameDay(1, month, year);
        //Log.e("day==",nameFirstDay);
        int blankSpaces = 0;
        switch (nameFirstDay) {
            case "Monday":
                blankSpaces = 1;
                break;
            case "Tuesday":
                blankSpaces = 2;
                break;
            case "Wednesday":
                blankSpaces = 3;
                break;
            case "Thursday":
                blankSpaces = 4;
                break;
            case "Friday":
                blankSpaces = 5;
                break;
            case "Saturday":
                blankSpaces = 6;
                break;
        }
        int squares = 0;
        if (blankSpaces > 0) {
            int tmonth;
            int tyear = year;
            if (month == 0) {
                tmonth = 11;
                tyear = year - 1;
            } else {
                tmonth = month - 1;
            }
            int numdays = getNumberOfDaysMonthYear(tyear, tmonth);
            for (int i = numdays - blankSpaces + 1; i <= numdays; i++) {
                days.add(new Day(new Date(tyear, tmonth, i), false, textColorDaysNV, backgroundColorDaysNV));
                squares++;
            }
        }

        int numberOfDaysMonthYear = getNumberOfDaysMonthYear(year, month);
        for (int i = 1; i <= numberOfDaysMonthYear; i++) {
            int week=getCurrentWeek(year,month,i);
            if (this.iyear == year && this.imonth == month && this.currentDay == i) {
                if(week==1||week==7){
                    days.add(new Day(new Date(year, month, i), "0",true,true));
                }else{
                    days.add(new Day(new Date(year, month, i), "1",true,true));
                }
            } else {
                //days.add(new Day(new Date(year, month, i), textColorDays, backgroundColorDays));
                if(week==1||week==7){
                    days.add(new Day(new Date(year, month, i), "0",true,false));
                }else{
                    days.add(new Day(new Date(year, month, i), "1",true,false));
                }
            }
            squares++;
        }
        if (squares < 42) {
            int tmonth;
            int tyear = year;
            if (month == 11) {
                tmonth = 0;
                tyear = year + 1;
            } else {
                tmonth = month + 1;
            }
            for (int i = 1; i < 20; i++) {
                days.add(new Day(new Date(tyear, tmonth, i), false, textColorDaysNV, backgroundColorDaysNV));
                squares++;
                if (squares == 42) {
                    break;
                }
            }
        }
    }

    /**
     * retorna el mes actual iniciando desde 0=enero
     *
     * @return
     */
    public int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public int getCurrentWeek(int year, int month,int day) {
        Calendar mycal = new GregorianCalendar(year, month, day);
        return mycal.get(Calendar.DAY_OF_WEEK);
    }
    /**
     * retorna el año actual
     *
     * @return
     */
    public int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * retorna el dia del mes actual
     *
     * @return
     */
    public int getCurrentDayMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * calcula el numero de dias que tiene un mes de una año especifico
     *
     * @param year
     * @param month
     * @return
     */
    public int getNumberOfDaysMonthYear(int year, int month) {
        Calendar mycal = new GregorianCalendar(year, month, 1);
        return mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    /**
     * nos retorna el nombre de un dia especifico de una año (en ingles o español segun la configuracion)
     *
     * @param day
     * @param month
     * @param year
     * @return nombre del dia
     */
    public String getNameDay(int day, int month, int year) {
        Date date1 = (new GregorianCalendar(year, month, day)).getTime();
        // Then get the day of week from the Date based on specific locale.
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date1);
    }

    /**
     * un objeto de tipo day para obtener la fecha (año,mes,dia) con un objeto calendar
     * <p>
     * otros metodos como setBackgroundColor(int backgroundColor) y getBackgroundColor() color del fondo del numero de dia del mes
     * setTextColor(int textColor) y getTextColor() color del texto numero de dia del mes
     *
     * @param day
     * @param position
     */
    @Override
    public void dayOnClick(Day day, int position) {
        if(day.isValid()){
            setItemSelected(position);
            onDayClickListener.dayOnClick(day, position);
        }
    }

    /**
     * similar a dayOnClick solo que este se ejecuta cuando haya un clic prolongado
     *
     * @param day
     * @param position
     */
    @Override
    public void dayOnLongClik(Day day, int position) {
        onDayClickListener.dayOnLongClik(day, position);
    }

    public interface OnSwipeListener {
        void rightSwipe();

        void leftSwipe();
    }


    private OnSwipeListener onSwipeListener;

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    public interface OnDayClickListener {
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

    private OnDayClickListener onDayClickListener;

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
    }

    public void setItemSelected(int position) {
        Day day=days.get(position);
        days.get(position).setSelected(true);
        for (int i = 0; i < days.size(); i++) {
            if (i != position)
                days.get(i).setSelected(false);
        }
        monthList.put(day.getDate().getYear()+getStringMonth(day.getDate().getMonth()),days);
        calendarAdapter.notifyItemChanged(0, 41);
        calendarAdapter.notifyDataSetChanged();

    }


    /**
     * marca un dia en el calendario como seleccionado
     *
     * @param position
     */
    public void addItemSelected(int position) {
        days.get(position).setSelected(true);
        calendarAdapter.notifyItemChanged(position);
        calendarAdapter.notifyDataSetChanged();
    }


    /**
     * remueve un dia en el calendario como seleccionado
     *
     * @param position
     */
    public void removeItemSelected(int position) {
        days.get(position).setSelected(false);
        calendarAdapter.notifyItemChanged(position);
        calendarAdapter.notifyDataSetChanged();
    }


    public ArrayList<Day> getDays() {
        return days;
    }

    private ArrayList<Day> fillUpMonth(int year,int month, int day) {
        ArrayList<Day> days = new ArrayList<>();
        String nameFirstDay = getNameDay(1, month, year);
        int blankSpaces = 0;
        switch (nameFirstDay) {
            case "Monday":
                blankSpaces = 1;
                break;
            case "Tuesday":
                blankSpaces = 2;
                break;
            case "Wednesday":
                blankSpaces = 3;
                break;
            case "Thursday":
                blankSpaces = 4;
                break;
            case "Friday":
                blankSpaces = 5;
                break;
            case "Saturday":
                blankSpaces = 6;
                break;
        }
        int squares = 0;
        if (blankSpaces > 0) {
            int tmonth;
            int tyear = year;
            if (month == 0) {
                tmonth = 11;
                tyear = year - 1;
            } else {
                tmonth = month - 1;
            }
            int numdays = getNumberOfDaysMonthYear(tyear, tmonth);
            for (int i = numdays - blankSpaces + 1; i <= numdays; i++) {
                days.add(new Day(new Date(tyear, tmonth, i), false));
                squares++;
            }
        }

        int numberOfDaysMonthYear = getNumberOfDaysMonthYear(year, month);
        for (int i = 1; i <= numberOfDaysMonthYear; i++) {
            int week=getCurrentWeek(year,month,i);
            if (day == i) {
                if(week==1||week==7){
                    days.add(new Day(new Date(year, month, i), "0",true,true));
                }else{
                    days.add(new Day(new Date(year, month, i), "1",true,true));
                }
            } else {
                //days.add(new Day(new Date(year, month, i), textColorDays, backgroundColorDays));
                if(week==1||week==7){
                    days.add(new Day(new Date(year, month, i), "0",true,false));
                }else{
                    days.add(new Day(new Date(year, month, i), "1",true,false));
                }
            }
            squares++;
        }
        if (squares < 42) {
            int tmonth;
            int tyear = year;
            if (month == 11) {
                tmonth = 0;
                tyear = year + 1;
            } else {
                tmonth = month + 1;
            }
            for (int i = 1; i < 20; i++) {
                days.add(new Day(new Date(tyear, tmonth, i), false));
                squares++;
                if (squares == 42) {
                    break;
                }
            }
        }
        monthList.put(year+getStringMonth(month),days);
        return days;
    }

    public String getStringMonth(int numMonth) {
        switch (numMonth) {
            case 0:
                return enero;
            case 1:
                return febrero;
            case 2:
                return marzo;
            case 3:
                return abril;
            case 4:
                return mayo;
            case 5:
                return junio;
            case 6:
                return julio;
            case 7:
                return agosto;
            case 8:
                return septiembre;
            case 9:
                return octubre;
            case 10:
                return noviembre;
            case 11:
                return diciembre;
        }
        return enero;
    }
}
