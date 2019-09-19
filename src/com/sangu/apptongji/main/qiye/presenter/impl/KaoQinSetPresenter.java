package com.sangu.apptongji.main.qiye.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.qiye.model.IKaoqinSetModel;
import com.sangu.apptongji.main.qiye.model.impl.KaoqinSetModel;
import com.sangu.apptongji.main.qiye.presenter.IKaoQinSetPresenter;
import com.sangu.apptongji.main.qiye.view.IKaoQinSetView;
import com.sangu.apptongji.widget.calendar.entity.DayBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-02-23.
 */

public class KaoQinSetPresenter implements IKaoQinSetPresenter {
    private Context context;
    private IKaoqinSetModel kaoqinSetModel;
    private IKaoQinSetView kaoQinSetView;

    public KaoQinSetPresenter(Context context, IKaoQinSetView kaoQinSetView) {
        this.kaoQinSetView = kaoQinSetView;
        this.context = context;
        this.kaoqinSetModel = new KaoqinSetModel(context);
    }

    @Override
    public void getMonthDate(final String data, String companyId) {
        kaoqinSetModel.getMonthDate(data,companyId,new IModel.AsyncCallback(){
            @Override
            public void onSuccess(Object s) {
                Log.d("chen", "收到考勤设置数据-->" + s);
                JSONObject jsonObject = JSON.parseObject(String.valueOf(s));
                JSONObject jsonObject1 = jsonObject.getJSONObject("comBackLog");
                if (jsonObject1 == null) {
                    kaoQinSetView.updateMonthData(null, data);
                    return;
                }
                String msg = jsonObject1.getString("backLog");
                List<DayBean> list = new ArrayList<>();
                for (String allTime : msg.split("-")) {
                    String[] allTime2 = allTime.split(",");
                    String[] times = allTime2[0].split("\\|");
                    //09:30|17:45|12:00|18:00
                    DayBean bean = new DayBean();
                    if (allTime2.length >= 3) {
                        bean.setLat(allTime2[2]);
                    } else {
                        bean.setLat("");
                    }
                    if (allTime2.length >= 4) {
                        bean.setLng(allTime2[3]);
                    } else {
                        bean.setLng("");
                    }
                    if (allTime2.length >= 5) {
                        bean.setLocation(allTime2[4]);
                    } else {
                        bean.setLocation("");
                    }
                    bean.setAmShangban(times[0]);
                    bean.setPmXiaban(times[1]);
                    bean.setAmXiaban(times[2]);
                    bean.setPmShangban(times[3]);
                    //0休息  1上班     2集合
                    if (allTime2[1].equalsIgnoreCase("0")) {
                        bean.setType(2);
                    } else if (allTime2[1].equalsIgnoreCase("1")) {
                        bean.setType(0);
                    } else {
                        bean.setType(1);
                    }
                    list.add(bean);
                }
                kaoQinSetView.updateMonthData(list,data);
            }

            @Override
            public void onError(Object error) {

            }
        } );
    }

    @Override
    public void updateMonthDate(String data, String companyId, String monthData) {
        kaoqinSetModel.updateMonthDate(data, companyId, monthData, new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object success) {
                Log.d("chen", "考勤信息更改" + success);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}
