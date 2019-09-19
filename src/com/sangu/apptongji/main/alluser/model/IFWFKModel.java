package com.sangu.apptongji.main.alluser.model;

/**
 * Created by Administrator on 2016-10-19.
 */

public interface IFWFKModel extends IModel{
    void getFWFKList(String orderId,AsyncCallback callback);
}
