package com.sangu.apptongji.main.alluser.presenter;

/**
 * Created by Administrator on 2016/10/7.
 */

public interface IPaidanFinishPresenter extends IPresenter {
    void loadShifuList(String dynamicSeq, String createTime, String sId, String state);
    void updateShifuContace(String uId, String sId, String type);
}
