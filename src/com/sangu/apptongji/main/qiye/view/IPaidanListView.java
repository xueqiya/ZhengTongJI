package com.sangu.apptongji.main.qiye.view;

import com.sangu.apptongji.main.qiye.entity.PaiDanInfo;

import java.util.List;

/**
 * Created by Administrator on 2017-01-17.
 */

public interface IPaidanListView extends IView {
    void updatePaidanList(List<PaiDanInfo> paiDanList);
    void showLoading();
    void hideLoading();
    void showError();
}
