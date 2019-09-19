package com.sangu.apptongji.main.qiye.model;

import com.sangu.apptongji.main.qiye.presenter.OnQiyeListenerTwo;

/**
 * Created by Administrator on 2017-01-03.
 */

public interface IMemberModelTwo extends IModel {
    void getMemberList(int currentPage, OnQiyeListenerTwo onQiyeListener);
}
