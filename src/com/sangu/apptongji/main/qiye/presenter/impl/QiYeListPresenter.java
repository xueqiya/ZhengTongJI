package com.sangu.apptongji.main.qiye.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.model.IQiYeListModel;
import com.sangu.apptongji.main.qiye.model.impl.QiYeListModel;
import com.sangu.apptongji.main.qiye.presenter.IQiYeListPresenter;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;
import com.sangu.apptongji.main.qiye.view.IQiYeListView;

import java.util.List;

/**
 * Created by Administrator on 2016-12-30.
 */

public class QiYeListPresenter implements IQiYeListPresenter ,OnQiyeListener{
    private IQiYeListModel qiYeListModel;
    private IQiYeListView qiYeListView;

    public QiYeListPresenter(Context context,IQiYeListView qiYeListView) {
        this.qiYeListView = qiYeListView;
        qiYeListModel = new QiYeListModel(context);
    }

    @Override
    public void loadQiyeList(String currentPage,String lat,String lng,String comName,String zhuanye,boolean renshu,boolean jingyan,boolean hasBao) {
        qiYeListModel.getQiYeList(currentPage,lat,lng,comName,zhuanye,renshu,jingyan,hasBao,this);
    }

    @Override
    public void onSuccess(Object obj,boolean hasMore) {
        List<QiYeInfo> qiYeInfoList = (List<QiYeInfo>) obj;
        qiYeListView.updateUserList(qiYeInfoList,hasMore);
    }

    @Override
    public void onStart() {
        qiYeListView.showLoading();
    }

    @Override
    public void onFailed() {
        qiYeListView.showError();
    }

    @Override
    public void onFinish() {
        qiYeListView.hideLoading();
    }
}
