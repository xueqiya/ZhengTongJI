package com.sangu.apptongji.main.alluser.model;

/**
 * Created by user on 2016/9/1.
 */

public interface IOrderListQModel extends IModel{
    void getUserOrderList(String state, AsyncCallback callback);
    void getMerOrderList(String state, AsyncCallback callback);

}
