package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.DianziDanju;
import com.sangu.apptongji.main.alluser.model.IDzdanjuListModel;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.impl.DzdanjuListModel;
import com.sangu.apptongji.main.alluser.presenter.IDzdanjuListPresenter;
import com.sangu.apptongji.main.alluser.view.IDzdanjuView;

import java.util.List;

/**
 * Created by Administrator on 2017-07-21.
 */

public class DzdanjuListPresenter implements IDzdanjuListPresenter {
    private IDzdanjuView dzdanjuView;
    private IDzdanjuListModel dzdanjuListModel;

    public DzdanjuListPresenter(Context context,IDzdanjuView dzdanjuView) {
        this.dzdanjuView = dzdanjuView;
        this.dzdanjuListModel = new DzdanjuListModel(context);
    }

    @Override
    public void loadDZdanjuList(String id, String currentPage) {
        dzdanjuListModel.getDzdanjuList(id, currentPage, new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object success) {
                List<DianziDanju> dianziDanjus = (List<DianziDanju>) success;
                if (dianziDanjus!=null&&dianziDanjus.size()>19) {
                    dzdanjuView.updateDzdanjuList(dianziDanjus,true);
                }else {
                    dzdanjuView.updateDzdanjuList(dianziDanjus,false);
                }
            }

            @Override
            public void onError(Object error) {
                dzdanjuView.updateDzdanjuList(null,true);
            }
        });
    }
}
