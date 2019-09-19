package com.sangu.apptongji.main.alluser.model;

/**
 * Created by Administrator on 2016/10/7.
 */

public interface IFriendListModel extends IModel {
    void getFriendList(String currentPage, AsyncCallback callback);
}
