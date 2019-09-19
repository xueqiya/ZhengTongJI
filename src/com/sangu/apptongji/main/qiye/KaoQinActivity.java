package com.sangu.apptongji.main.qiye;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.qiye.adapter.KaoqinAdapter;
import com.sangu.apptongji.main.qiye.entity.KaoqinInfo;
import com.sangu.apptongji.main.qiye.presenter.IKaoqinListPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.KaoqinListPresenter;
import com.sangu.apptongji.main.qiye.view.IKaoQinSetView;
import com.sangu.apptongji.main.qiye.view.IKaoqinListView;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.widget.calendar.entity.DayBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lishaokang on 2017-01-12.
 */

public class KaoQinActivity extends BaseActivity implements IKaoqinListView,View.OnClickListener,IKaoQinSetView {
    /*private ProgressDialog pd;
    private IKaoqinListPresenter kaoqinListPresenter;
    private IKaoQinSetPresenter kaoQinSetPresenter;
    private TextView tv_people_count,tv_working,tv_work_overtime,tv_late,tv_leave,tv_working_rank,tv_yeji_rank,tv_late_rank,tv_liuliang_rank,tv_month,tv_set;
    private ImageView iv_avatar;
    private String comImage,memberNum,loginTime;
    private Boolean isWorkTime = true;
    private MonthCalendar monthcalendar;
    private String companyId;
    private int currentPager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_kaoqin);
        comImage = getIntent().getStringExtra("avatar");
        memberNum = getIntent().getStringExtra("memberNum");
        loginTime = getIntent().getStringExtra("loginTime");
        companyId = getIntent().getStringExtra("companyId");
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("正在加载数据...");
        kaoqinListPresenter = new KaoqinListPresenter(this,this);
        kaoqinListPresenter.loadKaoqinList();

        tv_people_count = (TextView) findViewById(R.id.tv_people_count);
        tv_working = (TextView) findViewById(R.id.tv_working);
        tv_work_overtime = (TextView) findViewById(R.id.tv_work_yeji);
        tv_late = (TextView) findViewById(R.id.tv_late);
        tv_leave = (TextView) findViewById(R.id.tv_leave);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_set = (TextView) findViewById(R.id.tv_set);
        tv_working_rank = (TextView) findViewById(R.id.tv_working_rank);
        tv_yeji_rank = (TextView) findViewById(R.id.tv_yeji_rank);
        tv_late_rank = (TextView) findViewById(R.id.tv_late_rank);
        tv_liuliang_rank = (TextView) findViewById(R.id.tv_liuliang_rank);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        monthcalendar = (MonthCalendar) findViewById(R.id.monthcalendar);

        tv_people_count.setOnClickListener(this);
        tv_set.setOnClickListener(this);
        tv_month.setOnClickListener(this);
        tv_working.setOnClickListener(this);
        tv_work_overtime.setOnClickListener(this);
        tv_late.setOnClickListener(this);
        tv_leave.setOnClickListener(this);
        tv_working_rank.setOnClickListener(this);
        tv_yeji_rank.setOnClickListener(this);
        tv_late_rank.setOnClickListener(this);
        tv_liuliang_rank.setOnClickListener(this);
        iv_avatar.setOnClickListener(this);
        //判断是否加班时间
        isWorkTime();
        tv_people_count.setText(memberNum + "人");
        ImageLoader.getInstance().displayImage(comImage,iv_avatar, DemoApplication.mOptions);
        monthcalendar.setOnMonthCalendarChangedListener(new OnMonthCalendarChangedListener() {
            @Override
            public void onMonthCalendarChanged(int pager, DateTime dateTime) {
                String data = dateTime.toLocalDate().toString();
                currentPager = pager;
                Log.d("chen", "时间" + data);
                String[] datas = data.split("-");
                String currentTimeStamp = datas[0] + datas[1];
                tv_month.setText(datas[0] + "年" + datas[1] + "月");
                if (companyId != null && !TextUtils.isEmpty(companyId)) {
                    kaoQinSetPresenter.getMonthDate(currentTimeStamp,companyId);
                }
            }
        });
        kaoQinSetPresenter = new KaoQinSetPresenter(this,this);
    }

    private void isWorkTime() {
        //08:30|17:30
        if (TextUtils.isEmpty(loginTime)) {

            loginTime = "08:30|17:30";
            Log.d("chen", "没有获取到山下班时间");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String shangbanTime = loginTime.split("\\|")[0];
        String xiabanbanTime = loginTime.split("\\|")[1];
        try {
            Date shangbandate = simpleDateFormat.parse(shangbanTime);
            Date xiabanbandate = simpleDateFormat.parse(xiabanbanTime);
            Date date = new Date(System.currentTimeMillis());
            Date currentdate = simpleDateFormat.parse(simpleDateFormat.format(date));
            if (currentdate.getTime() >= shangbandate.getTime() && currentdate.getTime() < xiabanbandate.getTime()) {
                isWorkTime = true;
            } else {
                isWorkTime = false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_working:
                Intent intent = new Intent(KaoQinActivity.this, KaoQinDetaliActivity.class);
                intent.putExtra("type","workState=01");
                intent.putExtra("tv_working", tv_working.getText());
                intent.putExtra("tv_work_overtime", tv_work_overtime.getText());
                intent.putExtra("tv_working", tv_working.getText());
                intent.putExtra("tv_late", tv_late.getText());
                intent.putExtra("tv_leave", tv_leave.getText());
                intent.putExtra("iv_avatar", comImage);
                intent.putExtra("memberNum", memberNum);
                startActivity(intent);
                break;
            case R.id.tv_work_yeji:
                Intent intent2 = new Intent(KaoQinActivity.this, KaoQinDetaliActivity.class);
                intent2.putExtra("type","overTimeState=01");
                intent2.putExtra("tv_working", tv_working.getText());
                intent2.putExtra("tv_work_overtime", tv_work_overtime.getText());
                intent2.putExtra("tv_late", tv_late.getText());
                intent2.putExtra("tv_leave", tv_leave.getText());
                intent2.putExtra("iv_avatar", comImage);
                intent2.putExtra("memberNum", memberNum);

                startActivity(intent2);
                break;
            case R.id.tv_late:
                Intent intent3 = new Intent(KaoQinActivity.this, KaoQinDetaliActivity.class);
                intent3.putExtra("type","lateState=01");
                intent3.putExtra("tv_working", tv_working.getText());
                intent3.putExtra("tv_work_overtime", tv_work_overtime.getText());
                intent3.putExtra("tv_working", tv_working.getText());
                intent3.putExtra("tv_late", tv_late.getText());
                intent3.putExtra("tv_leave", tv_leave.getText());
                intent3.putExtra("iv_avatar", comImage);
                intent3.putExtra("memberNum", memberNum);

                startActivity(intent3);
                break;
            case R.id.tv_leave:
                Intent intent4 = new Intent(KaoQinActivity.this, KaoQinDetaliActivity.class);
                intent4.putExtra("type","leaveState=01");
                intent4.putExtra("tv_working", tv_working.getText());
                intent4.putExtra("tv_work_overtime", tv_work_overtime.getText());
                intent4.putExtra("tv_working", tv_working.getText());
                intent4.putExtra("tv_late", tv_late.getText());
                intent4.putExtra("tv_leave", tv_leave.getText());
                intent4.putExtra("iv_avatar", comImage);
                intent4.putExtra("memberNum", memberNum);
                startActivity(intent4);
                break;
            case R.id.tv_working_rank:
                Intent intent11 = new Intent(KaoQinActivity.this, KaoQinDetaliActivity.class);
                intent11.putExtra("type","workTime=01");
                intent11.putExtra("cType", "paihang");
                startActivity(intent11);
                break;
            case R.id.tv_yeji_rank:
                Intent intent12 = new Intent(KaoQinActivity.this, KaoQinDetaliActivity.class);
                intent12.putExtra("type","dayTransAmount=01");
                intent12.putExtra("cType", "paihang");
                startActivity(intent12);
                break;
            case R.id.tv_late_rank:
                Intent intent13 = new Intent(KaoQinActivity.this, KaoQinDetaliActivity.class);
                intent13.putExtra("type","lateTime=01");
                intent13.putExtra("cType", "paihang");
                startActivity(intent13);
                break;
            case R.id.tv_liuliang_rank:
                Intent intent14 = new Intent(KaoQinActivity.this, KaoQinDetaliActivity.class);
                intent14.putExtra("type","visitorTime=01");
                intent14.putExtra("cType", "paihang");
                startActivity(intent14);
                break;
            case R.id.tv_month:

                break;
            case R.id.tv_set:
                startActivity(new Intent(KaoQinActivity.this,KaoQinSetActivity.class).putExtra("companyId",companyId));
                break;
        }

    }

    @Override
    public void updateKaoqinList(QiyeKaoQinInfo kaoqinInfos) {
        if (kaoqinInfos != null) {
            if (isWorkTime) {
                tv_working.setText("上班情况 (" + kaoqinInfos.getWorkState() + "人)");
                tv_work_overtime.setText("加班情况 (0人)");
            } else {
                tv_working.setText("上班情况 (0人)");
                tv_work_overtime.setText("加班情况 (" + kaoqinInfos.getOverTimeState() + "人)");

            }
            tv_late.setText("迟到情况 (" + kaoqinInfos.getLateState() + "人)");
            tv_leave.setText("请假情况 (" + kaoqinInfos.getLeaveState() + "人)");
        }
    }

    @Override
    public void showLoading() {
        if (pd!=null&&!pd.isShowing()) {
            pd.show();
        }
    }

    @Override
    public void hideLoading() {
        if (pd!=null&&pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    public void showError() {
        if (pd!=null&&pd.isShowing()) {
            pd.dismiss();
        }
        Toast.makeText(KaoQinActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void updateMonthData(List<DayBean> list, String data) {
        if (list != null) {
            monthcalendar.setDataWithInfo(list, currentPager);
        }

    }*/

    private RadioButton rbAll;
    private RadioButton rbWei;
    private RadioButton rbYi;
    private RadioButton rbTui;
    private TextView tv_back;
    private List<KaoqinInfo> list;
    private KaoqinAdapter adapter;
    private ProgressDialog pd;
    private ListView lvKaoqinList;
    private IKaoqinListPresenter kaoqinListPresenter;
    private String remark, startTime, endTime;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (list!=null){
            list.clear();
            list=null;
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_consme);
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("正在加载数据...");
        kaoqinListPresenter = new KaoqinListPresenter(this,this);
        startTime = "20170101000000";
        endTime = getcurrentTime();
        remark = "02";
        kaoqinListPresenter.loadKaoqinList(remark,startTime,endTime);
        tv_back = (TextView) findViewById(R.id.tv_back);
        rbAll = (RadioButton) findViewById(R.id.radioUAll);
        rbWei = (RadioButton) findViewById(R.id.radioUWei);
        rbYi = (RadioButton) findViewById(R.id.radioUYi);
        rbTui = (RadioButton) findViewById(R.id.radioUTui);
        tv_back.setText("企业详情");
        rbAll.setText("迟到");
        rbWei.setText("请假");
        rbYi.setText("出行");
        rbTui.setText("离岗");
        lvKaoqinList = (ListView) findViewById(R.id.lv_myconsume);
        rbAll.setOnClickListener(this);
        rbWei.setOnClickListener(this);
        rbYi.setOnClickListener(this);
        rbTui.setOnClickListener(this);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
    public String getcurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radioUAll:
                if (list!=null) {
                    list.clear();
                }
                rbAll.setChecked(true);
                rbWei.setChecked(false);
                rbYi.setChecked(false);
                rbTui.setChecked(false);
                remark = "02";
                kaoqinListPresenter.loadKaoqinList(remark,startTime,endTime);
                if (list!=null) {
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.radioUWei:
                if (list!=null) {
                    list.clear();
                }
                rbWei.setChecked(true);
                rbAll.setChecked(false);
                rbYi.setChecked(false);
                rbTui.setChecked(false);
                remark = "03";
                kaoqinListPresenter.loadKaoqinList(remark,startTime,endTime);
                if (list!=null) {
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.radioUYi:
                if (list!=null) {
                    list.clear();
                }
                rbYi.setChecked(true);
                rbWei.setChecked(false);
                rbAll.setChecked(false);
                rbTui.setChecked(false);
                remark = "04";
                kaoqinListPresenter.loadKaoqinList(remark,startTime,endTime);
                if (list!=null) {
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.radioUTui:
                if (list!=null) {
                    list.clear();
                }
                rbTui.setChecked(true);
                rbWei.setChecked(false);
                rbAll.setChecked(false);
                rbYi.setChecked(false);
                remark = "05";
                kaoqinListPresenter.loadKaoqinList(remark,startTime,endTime);
                if (list!=null) {
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void updateKaoqinList(List<KaoqinInfo> kaoqinInfos) {
        this.list = kaoqinInfos;
        adapter = new KaoqinAdapter(list,this);
        lvKaoqinList.setAdapter(adapter);
    }


    @Override
    public void showLoading() {
        if (pd!=null&&!pd.isShowing()) {
            pd.show();
        }
    }
    @Override
    public void hideLoading() {
        if (pd!=null&&pd.isShowing()) {
            pd.dismiss();
        }
    }
    @Override
    public void showError() {
        if (pd!=null&&pd.isShowing()) {
            pd.dismiss();
        }
        Toast.makeText(KaoQinActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateMonthData(List<DayBean> list, String data) {

    }
}
