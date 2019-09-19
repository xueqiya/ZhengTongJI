package com.sangu.apptongji.main.alluser.model;

import com.sangu.apptongji.main.alluser.presenter.OnprofileListener;

/**
 * Created by Administrator on 2016-09-26.
 */

public interface IUAZModel extends IModel{
    void getThisUser(String hxid,OnprofileListener onprofileListener);
    void sendContactTrack(String u_id, String contact_id,String type);
}
