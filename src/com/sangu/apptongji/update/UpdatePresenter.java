package com.sangu.apptongji.update;

import android.content.Context;

import com.sangu.apptongji.main.alluser.model.IModel;

/**
 * Created by Administrator on 2016-12-19.
 */

public class UpdatePresenter implements IUpdatePresenter {
    private IUpdateView updateView;
    private IUpdateModel updateModel;

    public UpdatePresenter(Context context,IUpdateView iUpdateView) {
        this.updateView = iUpdateView;
        updateModel = new UpdateModel(context);
    }

    @Override
    public void checkUpdate() {
        updateModel.getUpdateInfo(new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object success) {
                ApkInfo info = (ApkInfo) success;
                updateView.updateApk(info);
            }
            @Override
            public void onError(Object error) {
            }
        });
    }
}
