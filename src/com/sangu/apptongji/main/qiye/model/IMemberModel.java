package com.sangu.apptongji.main.qiye.model;

import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;

/**
 * Created by Administrator on 2017-01-03.
 */

public interface IMemberModel extends IModel {
    void getMemberList(int currentPage,OnQiyeListener onQiyeListener);
}
