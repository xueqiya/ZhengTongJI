package com.sangu.apptongji.main.alluser.model;

/**
 * Created by Administrator on 2017-11-14.
 */

public interface IGroupModel extends IModel {
    void getUserList(String u_id,String currentPage,AsyncCallback callback);
}
