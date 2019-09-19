package com.sangu.apptongji.main.qiye.view;

import com.sangu.apptongji.main.qiye.entity.QiyeKaoQinDetailInfo;

import java.util.List;

/**
 * Created by Administrator on 2018-01-09.
 */

public interface IKaoqinDetailListView extends IView {
    void updateKaoqinList(List<QiyeKaoQinDetailInfo> kaoqinInfos,boolean hasMore);
    void showLoading();
    void hideLoading();
    void showError();
}
