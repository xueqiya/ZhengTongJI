package com.sangu.apptongji.main.qiye.model;

import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;

/**
 * Created by Administrator on 2017-01-17.
 */

public interface IPaidanListModel extends IModel {
    void getPaidanList(String companyId,String userId,OnQiyeListener onQiyeListener);
}
