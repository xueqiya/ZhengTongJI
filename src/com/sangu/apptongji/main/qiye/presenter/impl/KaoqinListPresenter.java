package com.sangu.apptongji.main.qiye.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.qiye.entity.KaoqinInfo;
import com.sangu.apptongji.main.qiye.model.IKaoqinListModel;
import com.sangu.apptongji.main.qiye.model.impl.KaoqinListModel;
import com.sangu.apptongji.main.qiye.presenter.IKaoqinListPresenter;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;
import com.sangu.apptongji.main.qiye.view.IKaoqinListView;

import java.util.List;

/**
 * Created by Administrator on 2017-01-12.
 */

public class KaoqinListPresenter implements IKaoqinListPresenter ,OnQiyeListener {
    private IKaoqinListModel kaoqinListModel;
    private IKaoqinListView kaoqinListView;

    public KaoqinListPresenter(Context context,IKaoqinListView iKaoqinListView) {
        this.kaoqinListView = iKaoqinListView;
        kaoqinListModel = new KaoqinListModel(context);
    }

    @Override
    public void loadKaoqinList(String remark, String startTime, String endTime) {
        kaoqinListModel.getKaoqqinList(remark,startTime,endTime,this);
    }

    @Override
    public void onSuccess(Object obj,boolean hasMore) {
        List<KaoqinInfo> kaoqinInfos = (List<KaoqinInfo>) obj;
        kaoqinListView.updateKaoqinList(kaoqinInfos);
    }

   /* public KaoqinListPresenter(Context context,IKaoqinListView iKaoqinListView) {
        this.kaoqinListView = iKaoqinListView;
        kaoqinListModel = new KaoqinListModel(context);
    }

    @Override
    public void loadKaoqinList() {
        kaoqinListModel.getKaoqqinList(this);
    }

    @Override
    public void onSuccess(Object obj,boolean hasMore) {
        QiyeKaoQinInfo kaoqinInfos = (QiyeKaoQinInfo) obj;
        kaoqinListView.updateKaoqinList(kaoqinInfos);
    }*/

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
