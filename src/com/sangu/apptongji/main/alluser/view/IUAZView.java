package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.Userful;

/**
 * Created by Administrator on 2016-09-26.
 */

public interface IUAZView extends IView{
    void updateThisUser(Userful user2);
    void showproLoading();
    void hideproLoading();
    void showproError();
}
