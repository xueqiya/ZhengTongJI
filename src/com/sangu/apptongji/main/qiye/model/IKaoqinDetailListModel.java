package com.sangu.apptongji.main.qiye.model;

import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;

/**
 * Created by Administrator on 2017-01-12.
 */

public interface IKaoqinDetailListModel extends IModel {
    void getKaoqqinList(String currentPage,String timeEnd,String timeStart,String timestamp, String workState,OnQiyeListener onQiyeListener);
    void getKaoqqinList(String currentPage,String timeEnd,String timeStart, String workState,OnQiyeListener onQiyeListener);

}
