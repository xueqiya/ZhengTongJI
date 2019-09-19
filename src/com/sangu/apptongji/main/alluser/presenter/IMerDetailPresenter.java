package com.sangu.apptongji.main.alluser.presenter;

/**
 * Created by Administrator on 2017-04-27.
 */

public interface IMerDetailPresenter extends IPresenter {
    void loadMerDetail(String currentPage,String hxid,String leixing,String type,String beginDate,String endDate);
}
