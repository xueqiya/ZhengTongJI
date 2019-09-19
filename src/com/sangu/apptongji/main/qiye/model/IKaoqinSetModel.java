package com.sangu.apptongji.main.qiye.model;

/**
 * Created by Administrator on 2018-02-23.
 */

public interface IKaoqinSetModel {
    void getMonthDate(String data, String companyId,final com.sangu.apptongji.main.alluser.model.IModel.AsyncCallback callback);
    void updateMonthDate(String data, String companyId, String monthData,final com.sangu.apptongji.main.alluser.model.IModel.AsyncCallback callback);
}
