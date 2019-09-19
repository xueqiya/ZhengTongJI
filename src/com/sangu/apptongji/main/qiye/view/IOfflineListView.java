package com.sangu.apptongji.main.qiye.view;

import com.sangu.apptongji.main.qiye.entity.OffSendOrderList;

import java.util.List;

/**
 * Created by Administrator on 2017-01-17.
 */

public interface IOfflineListView extends IView {
    void updateOfflineList(List<OffSendOrderList> paiDanList);
    void showLoading();
    void hideLoading();
    void showError();
}
