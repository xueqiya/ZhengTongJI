package com.sangu.apptongji.main.qiye.model;

import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;

/**
 * Created by Administrator on 2016-12-28.
 */

public interface IQiYeDetailModel extends IModel {
    void getQiYeInfo(String qiyeid, OnQiyeListener onQiyeListener);
}
