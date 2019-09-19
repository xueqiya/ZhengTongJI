package com.sangu.apptongji.main.qiye.model;

import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;

/**
 * Created by Administrator on 2017-01-12.
 */

public interface IKaoqinListModel extends IModel {
    //void getKaoqqinList(OnQiyeListener onQiyeListener);
    void getKaoqqinList(final String remark, final String startTime, final String endTime, final OnQiyeListener onQiyeListener);
}
