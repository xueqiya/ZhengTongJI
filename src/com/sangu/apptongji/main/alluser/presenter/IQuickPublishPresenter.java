package com.sangu.apptongji.main.alluser.presenter;

/**
 * Created by Administrator on 2018-01-16.
 */

public interface IQuickPublishPresenter {
    void quickPublish(String userid,String title,String msg,String lng,String lat);

    void quickUpdate(String userid, String sid, String title, String display);

    void quickVoicePublish(String uId, String content, String file, String lng, String lat);

    void quickVoiceUpdate(String uId, String sid, String content, String file, String display);
}

