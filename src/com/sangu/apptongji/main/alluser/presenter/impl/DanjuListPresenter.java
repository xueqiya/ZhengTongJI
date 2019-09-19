package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.model.IDanjuListModel;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.impl.DanjuListModel;
import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;
import com.sangu.apptongji.main.alluser.presenter.IDanjuListPresenter;
import com.sangu.apptongji.main.alluser.view.IDanjuListView;

import java.util.List;

/**
 * Created by user on 2016/9/1.
 */

public class DanjuListPresenter implements IDanjuListPresenter {
    private IDanjuListView danjuListView;
    private IDanjuListModel danjuListModel;

    public DanjuListPresenter(Context context,IDanjuListView view) {
        this.danjuListModel = new DanjuListModel(context);
        this.danjuListView = view;
    }

    @Override
    public void loadDanjuList(String id) {
        danjuListModel.getDanjuList(id, new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (obj!=null) {
                    List<OrderInfo> orderInfos = (List<OrderInfo>) obj;
                    danjuListView.updateQDanjuList(orderInfos);
                }else {
                    danjuListView.updateQDanjuList(null);
                }
            }

            @Override
            public void onError(Object error) {
                danjuListView.updateQDanjuList(null);
            }
        });
    }
}
