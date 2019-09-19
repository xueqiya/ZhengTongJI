package com.sangu.apptongji.main.alluser.model;

import com.sangu.apptongji.main.alluser.presenter.OnprofileListener;

/**
 * Created by user on 2016/8/29.
 */

public interface IProfileModel extends IModel{
    public void getCurrentUserInfo(OnprofileListener onprofileListener);
}
