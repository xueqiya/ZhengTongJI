package com.sangu.apptongji.main.alluser.model;

import com.sangu.apptongji.main.alluser.presenter.OnZhuFaListener;

/**
 * Created by Administrator on 2017-08-30.
 */

public interface IZhuFaListModel extends IModel{
    void getThisUser(String currentPage, String uLoginId, String leixing, OnZhuFaListener onZhuFaListener);
}
