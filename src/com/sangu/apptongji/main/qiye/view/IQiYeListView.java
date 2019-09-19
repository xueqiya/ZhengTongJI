package com.sangu.apptongji.main.qiye.view;

import com.sangu.apptongji.main.alluser.view.IView;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;

import java.util.List;

/**
 * Created by Administrator on 2016-12-30.
 */

public interface IQiYeListView extends IView {
    void updateUserList(List<QiYeInfo> users,boolean hasMore);
    void showLoading();
    void hideLoading();
    void showError();
}
