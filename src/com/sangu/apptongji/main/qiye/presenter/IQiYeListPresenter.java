package com.sangu.apptongji.main.qiye.presenter;

/**
 * Created by Administrator on 2016-12-30.
 */

public interface IQiYeListPresenter extends IPresenter {
    void loadQiyeList(String currentPage,String lat,String lng,String comName,String zhuanye,boolean renshu,boolean jingyan,boolean hasBao);
}
