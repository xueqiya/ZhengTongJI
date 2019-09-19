package com.sangu.apptongji.main.alluser.model;

import com.sangu.apptongji.main.alluser.presenter.OnGrMemListener;

/**
 * Created by Administrator on 2017-11-14.
 */

public interface IGrMemModel extends IModel {
    void getUserList(String groupId, String currentPage, OnGrMemListener listener);
}
