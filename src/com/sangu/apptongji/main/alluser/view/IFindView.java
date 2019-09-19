package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.UserAll;

import java.util.List;

/**
 * Created by user on 2016/8/29.
 */

public interface IFindView extends IView{
    void updateUserList(List<UserAll> users,boolean hasMore);
    void showLoading();
    void hideLoading();
    void showError(String mag);
}
