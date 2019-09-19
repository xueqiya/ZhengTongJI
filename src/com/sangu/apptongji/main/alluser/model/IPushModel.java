package com.sangu.apptongji.main.alluser.model;

/**
 * Created by Administrator on 2017-12-06.
 */

public interface IPushModel extends IModel {
    void getPushList(String u_id,AsyncCallback callback);
}
