package com.sangu.apptongji.main.qiye.model;

import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;

/**
 * Created by Administrator on 2016-12-30.
 */

public interface IQiYeListModel extends IModel {
    void getQiYeList(String currentPage,String lat,String lng,String comName,String zhuanye,boolean renshu,boolean jingyan,boolean hasBao,OnQiyeListener onQiyeListener);
}
