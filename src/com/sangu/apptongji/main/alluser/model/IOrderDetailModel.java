package com.sangu.apptongji.main.alluser.model;

/**
 * Created by user on 2016/9/8.
 */

public interface IOrderDetailModel extends IModel {
    void getCurrentOrderDetail(String orderId,AsyncCallback callback);
}
