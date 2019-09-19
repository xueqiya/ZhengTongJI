package com.sangu.apptongji.main.alluser.model;

import com.sangu.apptongji.main.alluser.presenter.OnFindListener;

/**
 * Created by user on 2016/8/29.
 */

public interface IPaimgModel extends IModel{
    void getUserList(String qiyeId, String com_id, String currentPage, OnFindListener onFindListener);
}
