package com.sangu.apptongji.main.qiye.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.model.IQiYeDetailModel;
import com.sangu.apptongji.main.qiye.model.impl.QiYeDetailModel;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016-12-28.
 */

public class QiYeInfoPresenter implements IQiYeInfoPresenter,OnQiyeListener {
    private IQiYeDetailModel qiYeDetailModel;
    private IQiYeDetailView qiYeDetailView;

    public QiYeInfoPresenter(Context context,IQiYeDetailView qiYeDetailView) {
        this.qiYeDetailView = qiYeDetailView;
        qiYeDetailModel = new QiYeDetailModel(context);
    }

    @Override
    public void loadQiYeInfo(String qiyeId) {
        qiYeDetailModel.getQiYeInfo(qiyeId,this);
    }

    @Override
    public void onSuccess(Object obj,boolean hasMore) {
        try {
            QiYeInfo info = (QiYeInfo) obj;
            qiYeDetailView.updateQiyeInfo(info);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        qiYeDetailView.showLoading();
    }

    @Override
    public void onFailed() {
        qiYeDetailView.showError();
    }

    @Override
    public void onFinish() {
        qiYeDetailView.hideLoading();
    }
}
