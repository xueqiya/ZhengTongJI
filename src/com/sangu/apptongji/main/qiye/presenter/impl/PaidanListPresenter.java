package com.sangu.apptongji.main.qiye.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.qiye.entity.PaiDanInfo;
import com.sangu.apptongji.main.qiye.model.IPaidanListModel;
import com.sangu.apptongji.main.qiye.model.impl.PaidanListModel;
import com.sangu.apptongji.main.qiye.presenter.IPaidanListPresenter;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;
import com.sangu.apptongji.main.qiye.view.IPaidanListView;

import java.util.List;

/**
 * Created by Administrator on 2017-01-17.
 */

public class PaidanListPresenter implements IPaidanListPresenter,OnQiyeListener {
    private IPaidanListView paidanListView;
    private IPaidanListModel paidanListModel;

    public PaidanListPresenter(Context context,IPaidanListView paidanListView) {
        this.paidanListView = paidanListView;
        paidanListModel = new PaidanListModel(context);
    }

    @Override
    public void loadPaidanList(String companyId, String userId) {
        paidanListModel.getPaidanList(companyId,userId,this);
    }

    @Override
    public void onSuccess(Object obj,boolean hasMore) {
        List<PaiDanInfo> paiDanInfoList = (List<PaiDanInfo>) obj;
        paidanListView.updatePaidanList(paiDanInfoList);
    }
    @Override
    public void onStart() {
        paidanListView.showLoading();
    }
    @Override
    public void onFailed() {
        paidanListView.showError();
    }
    @Override
    public void onFinish() {
        paidanListView.hideLoading();
    }
}
