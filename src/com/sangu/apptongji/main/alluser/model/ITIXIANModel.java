package com.sangu.apptongji.main.alluser.model;

/**
 * Created by Administrator on 2018-02-02.
 */

public interface ITIXIANModel {
    public interface AsyncCallback {

        void onSuccess(Object success,String time);

        void onError(Object error);

    }
}
