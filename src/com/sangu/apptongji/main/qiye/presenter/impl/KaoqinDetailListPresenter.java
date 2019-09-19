package com.sangu.apptongji.main.qiye.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.qiye.entity.QiyeKaoQinDetailInfo;
import com.sangu.apptongji.main.qiye.model.IKaoqinDetailListModel;
import com.sangu.apptongji.main.qiye.model.impl.KaoqinDetailListModel;
import com.sangu.apptongji.main.qiye.presenter.IKaoqinDetailListPresenter;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;
import com.sangu.apptongji.main.qiye.view.IKaoqinDetailListView;

import java.util.List;

/**
 * Created by Administrator on 2017-01-12.
 */

public class KaoqinDetailListPresenter implements IKaoqinDetailListPresenter,OnQiyeListener {
    private IKaoqinDetailListModel kaoqinListModel;
    private IKaoqinDetailListView kaoqinListView;

    public KaoqinDetailListPresenter(Context context, IKaoqinDetailListView iKaoqinListView) {
        this.kaoqinListView = iKaoqinListView;
        kaoqinListModel = new KaoqinDetailListModel(context);
    }

    @Override
    public void loadKaoqinDetailList(String currentPage,String timeEnd,String timeStart,String timestamp, String workState) {
        kaoqinListModel.getKaoqqinList( currentPage, timeEnd, timeStart, timestamp,  workState,this);
    }

    @Override
    public void loadKaoqinPaihangDetailList(String currentPage, String timeEnd, String timeStart, String workState) {
        kaoqinListModel.getKaoqqinList( currentPage, timeEnd, timeStart,   workState,this);
    }

    @Override
    public void onSuccess(Object obj,boolean hasMore) {
        List<QiyeKaoQinDetailInfo>  kaoqinInfos = (List<QiyeKaoQinDetailInfo>) obj;
        kaoqinListView.updateKaoqinList(kaoqinInfos,hasMore);
    }

    @Override
    public void onStart() {
        kaoqinListView.showLoading();
    }

    @Override
    public void onFailed() {
        kaoqinListView.showError();
    }

    @Override
    public void onFinish() {
        kaoqinListView.hideLoading();
    }
}
