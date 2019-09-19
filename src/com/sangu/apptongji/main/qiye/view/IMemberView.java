package com.sangu.apptongji.main.qiye.view;

import com.sangu.apptongji.main.qiye.entity.MemberInfo;

import java.util.List;

/**
 * Created by Administrator on 2017-01-03.
 */

public interface IMemberView extends IView {
    void updateUserList(List<MemberInfo> users,boolean hasMore);
    void showLoading();
    void hideLoading();
    void showError();
}
