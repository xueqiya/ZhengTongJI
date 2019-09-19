package com.sangu.apptongji.main.qiye.view;

import com.sangu.apptongji.main.qiye.entity.KaoqinInfo;

import java.util.List;

/**
 * Created by Administrator on 2017-01-12.
 */

public interface IKaoqinListView extends IView {
    //void updateKaoqinList(QiyeKaoQinInfo kaoqinInfos);
    void updateKaoqinList(List<KaoqinInfo> kaoqinInfos);
    void showLoading();
    void hideLoading();
    void showError();
}
