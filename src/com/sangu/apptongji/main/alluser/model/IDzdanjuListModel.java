package com.sangu.apptongji.main.alluser.model;

/**
 * Created by Administrator on 2017-07-21.
 */

public interface IDzdanjuListModel extends IModel {
    void getDzdanjuList(String u_id,String currentPage,AsyncCallback callback);
}
