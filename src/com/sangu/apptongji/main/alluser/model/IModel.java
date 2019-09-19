package com.sangu.apptongji.main.alluser.model;

/**
 * Created by user on 2016/8/29.
 */

public interface IModel {

    public interface AsyncCallback {

        void onSuccess(Object success);

        void onError(Object error);

    }
}
