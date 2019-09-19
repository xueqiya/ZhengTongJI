package com.sangu.apptongji.main.qiye.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.qiye.model.IModel;
import com.sangu.apptongji.main.qiye.model.IQMajModel;
import com.sangu.apptongji.main.qiye.model.impl.MajQModel;
import com.sangu.apptongji.main.qiye.presenter.IMajQPresenter;
import com.sangu.apptongji.main.qiye.view.IQMajView;

/**
 * Created by Administrator on 2017-05-16.
 */

public class MajQPresenter implements IMajQPresenter {
    private IQMajView view;
    private IQMajModel majModel;

    public MajQPresenter(Context context,IQMajView view) {
        this.view = view;
        majModel = new MajQModel(context);
    }

    @Override
    public void loadMajQList() {
        majModel.getMajQList(new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {
                view.updateMajQList(obj);
            }

            @Override
            public void onError(Object error) {
            }
        });
    }
}
