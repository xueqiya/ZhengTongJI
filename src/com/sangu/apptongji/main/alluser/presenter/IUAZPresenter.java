package com.sangu.apptongji.main.alluser.presenter;

/**
 * Created by Administrator on 2016-09-26.
 */

public interface IUAZPresenter extends IPresenter{
    void loadThisDetail(String hxid);
    void sendContactTrack(String u_id, String contact_id,String type);
}
