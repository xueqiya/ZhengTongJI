package com.sangu.apptongji.main.qiye.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.qiye.entity.OffSendOrderList;
import com.sangu.apptongji.main.qiye.model.IOfflineListModel;
import com.sangu.apptongji.main.qiye.model.impl.OfflineListModel;
import com.sangu.apptongji.main.qiye.presenter.IOfflineListPresenter;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;
import com.sangu.apptongji.main.qiye.view.IOfflineListView;

import java.util.List;

/**
 * Created by Administrator on 2017-01-17.
 */

public class OfflineListPresenter implements IOfflineListPresenter,OnQiyeListener {
    private IOfflineListView offlineListView;
    private IOfflineListModel offlineListModel;

    public OfflineListPresenter(Context context,IOfflineListView offlineListView) {
        this.offlineListView = offlineListView;
        offlineListModel = new OfflineListModel(context);
    }

    @Override
    public void loadOfflineList(String type, String queryId) {
        offlineListModel.getOfflineList(type,queryId,this);
    }

    @Override
    public void onSuccess(Object obj,boolean hasMore) {
        List<OffSendOrderList> offSendOrderLists = (List<OffSendOrderList>) obj;
        offlineListView.updateOfflineList(offSendOrderLists);
    }
    @Override
    public void onStart() {
        offlineListView.showLoading();
    }
    @Override
    public void onFailed() {
        offlineListView.showError();
    }
    @Override
    public void onFinish() {
        offlineListView.hideLoading();
    }
}
