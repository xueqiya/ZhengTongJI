package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.FWFKInfo;
import com.sangu.apptongji.main.alluser.model.IFWFKModel;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.impl.FWFKModel;
import com.sangu.apptongji.main.alluser.presenter.IFWFKPresenter;
import com.sangu.apptongji.main.alluser.view.IFWFKView;

import java.util.List;

/**
 * Created by Administrator on 2016-10-19.
 */

public class FWFKPresenter implements IFWFKPresenter {
    private IFWFKModel fwfkModel;
    private IFWFKView view;

    public FWFKPresenter(Context context,IFWFKView view) {
        this.view = view;
        this.fwfkModel = new FWFKModel(context);
    }

    @Override
    public void loadFWFKList(String orderId) {
        fwfkModel.getFWFKList(orderId,new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<FWFKInfo> fwfkInfos = (List<FWFKInfo>) obj;
                view.updataFuWuFanKuiList(fwfkInfos);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}
