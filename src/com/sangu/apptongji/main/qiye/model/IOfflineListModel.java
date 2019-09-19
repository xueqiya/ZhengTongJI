package com.sangu.apptongji.main.qiye.model;

import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;

/**
 * Created by Administrator on 2017-01-17.
 */

public interface IOfflineListModel extends IModel {
    void getOfflineList(String type, String queryId, OnQiyeListener onQiyeListener);
}
