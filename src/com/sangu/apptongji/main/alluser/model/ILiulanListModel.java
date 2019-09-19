package com.sangu.apptongji.main.alluser.model;

import com.sangu.apptongji.main.alluser.presenter.OnLiuLanListener;

/**
 * Created by Administrator on 2017-08-30.
 */

public interface ILiulanListModel extends IModel{
    void getThisUser(String currentPage,String uLoginId,String leixing,OnLiuLanListener onLiuLanListener);
}
