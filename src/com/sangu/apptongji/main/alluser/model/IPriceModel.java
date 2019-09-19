package com.sangu.apptongji.main.alluser.model;

/**
 * Created by user on 2016/9/8.
 */

public interface IPriceModel extends IModel{
    public void getCurrentPrice(String id,AsyncCallback callback);
}
