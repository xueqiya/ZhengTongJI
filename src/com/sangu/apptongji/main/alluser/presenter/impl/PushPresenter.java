package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.PushDetail;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.IPushModel;
import com.sangu.apptongji.main.alluser.model.impl.PushModel;
import com.sangu.apptongji.main.alluser.presenter.IPushPresenter;
import com.sangu.apptongji.main.alluser.view.IPushView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-12-06.
 */

public class PushPresenter implements IPushPresenter {
    private IPushView pushView;
    private IPushModel pushModel;

    public PushPresenter(Context context, IPushView view) {
        this.pushModel = new PushModel(context);
        this.pushView = view;
    }

    @Override
    public void loadPushList(String u_id) {
        pushModel.getPushList(u_id, new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object success) {
                pushView.updatePushList((List<PushDetail>) success);
            }

            @Override
            public void onError(Object error) {
                pushView.updatePushList(new ArrayList<PushDetail>());
            }
        });
    }
}
