package com.sangu.apptongji.main.alluser.model;

/**
 * Created by user on 2016/9/1.
 */

public interface IOrderListModel extends IModel{
    void getUserOrderList(String id,String state,AsyncCallback callback);
    void getMerOrderList(String id,String state,AsyncCallback callback);

}
