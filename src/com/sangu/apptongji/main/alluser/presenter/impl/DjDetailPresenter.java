package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.DianziDanju;
import com.sangu.apptongji.main.alluser.model.IDjDetailModel;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.impl.DjDetailModel;
import com.sangu.apptongji.main.alluser.presenter.IDjDetailPresenter;
import com.sangu.apptongji.main.alluser.view.IDjDetailView;

/**
 * Created by Administrator on 2017-07-24.
 */

public class DjDetailPresenter implements IDjDetailPresenter {
    private IDjDetailView djDetailView;
    private IDjDetailModel djDetailModel;

    public DjDetailPresenter(Context context,IDjDetailView djDetailView) {
        this.djDetailView = djDetailView;
        this.djDetailModel = new DjDetailModel(context);
    }

    @Override
    public void loadDZdjDetail(String id, String timestamp) {
        djDetailModel.getDzdanjuList(id, timestamp, new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object success) {
                DianziDanju dianziDanju = (DianziDanju) success;
                djDetailView.updateDzdjDetail(dianziDanju);
            }

            @Override
            public void onError(Object error) {
                djDetailView.updateDzdjDetail(null);
            }
        });
    }
}
