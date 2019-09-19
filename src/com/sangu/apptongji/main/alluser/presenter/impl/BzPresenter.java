package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.BaoZInfo;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.IbzModel;
import com.sangu.apptongji.main.alluser.model.impl.BzModel;
import com.sangu.apptongji.main.alluser.presenter.IbzPresenter;
import com.sangu.apptongji.main.alluser.view.IbzView;

import java.util.List;

/**
 * Created by Administrator on 2017-08-08.
 */

public class BzPresenter implements IbzPresenter {
    private IbzModel bzModel;
    private IbzView bzView;

    public BzPresenter(Context context,IbzView bzView) {
        this.bzView = bzView;
        bzModel = new BzModel(context);
    }

    @Override
    public void loadbaozList() {
        bzModel.getBaozList(new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object success) {
                if (success!=null) {
                    List<BaoZInfo> lists = (List<BaoZInfo>) success;
                    bzView.updateBzList(lists);
                }else {
                    bzView.updateBzList(null);
                }
            }

            @Override
            public void onError(Object error) {
                bzView.updateBzList(null);
            }
        });
    }
}
