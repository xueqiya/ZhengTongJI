package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.Userful;

/**
 * Created by user on 2016/8/29.
 */

public interface IProfileView extends IView{
    void updateUserInfo(Userful user);
    void showproLoading();
    void hideproLoading();
    void showproError();
}
