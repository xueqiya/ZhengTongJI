package com.sangu.apptongji.main.alluser.model;

/**
 * Created by Administrator on 2016/10/7.
 */

public interface IPaidanFinishModel extends IModel {
    void getShifuList(String dynamicSeq, String createTime, String currentPage, String state, AsyncCallback callback);

    void updateShifuContace(String uId, String sId, String type, AsyncCallback callback);
}
