package com.sangu.apptongji.main.alluser.model;

/**
 * Created by Administrator on 2018-01-16.
 */

public interface IQuickPublishModel {
    void quickSend(String userid,String title,String msg,String lng,String lat,final IModel.AsyncCallback callback);

    void quickUpdate(String userid, String sId, String content, String display, final IModel.AsyncCallback callback);

    void quickVoicePublish(String uId, String content, String file, String lng, String lat, final IModel.AsyncCallback callback);

    void quickVoiceUpdate(String uId, String content, String file, String sId, String display, final IModel.AsyncCallback callback);
}
