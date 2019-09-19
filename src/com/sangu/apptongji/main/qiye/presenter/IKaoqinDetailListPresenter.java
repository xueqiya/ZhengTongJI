package com.sangu.apptongji.main.qiye.presenter;

/**
 * Created by Administrator on 2017-01-12.
 */

public interface IKaoqinDetailListPresenter extends IPresenter {
    void loadKaoqinDetailList(String currentPage,String timeEnd,String timeStart,String timestamp, String workState);
    void loadKaoqinPaihangDetailList(String currentPage,String timeEnd,String timeStart, String workState);
}
