package com.sangu.apptongji.main.alluser.model;

/**
 * Created by Administrator on 2017-07-24.
 */

public interface IDjDetailModel extends IModel {
    void getDzdanjuList(String u_id,String timestamp,AsyncCallback callback);
}
