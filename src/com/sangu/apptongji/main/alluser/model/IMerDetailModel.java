package com.sangu.apptongji.main.alluser.model;

import com.sangu.apptongji.main.alluser.presenter.OnMerListener;

/**
 * Created by Administrator on 2017-04-27.
 */

public interface IMerDetailModel extends IModel {
    void getThisUser(String currentPage,String hxid,String leixing,String type,String beginDate,String endDate,OnMerListener onMerListener);
}
