package com.sangu.apptongji.update;

import com.sangu.apptongji.main.alluser.model.IModel;

/**
 * Created by Administrator on 2016-12-19.
 */

public interface IUpdateModel extends IModel{
    void getUpdateInfo(AsyncCallback callback);
}
