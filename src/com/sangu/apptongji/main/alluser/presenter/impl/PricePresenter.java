package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.IPriceModel;
import com.sangu.apptongji.main.alluser.model.impl.PriceModel;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;

/**
 * Created by user on 2016/9/8.
 */

public class PricePresenter implements IPricePresenter {
    private IPriceView view;
    private IPriceModel priceModel;

    public PricePresenter(Context context,IPriceView view) {
        this.view = view;
        this.priceModel = new PriceModel(context);
    }

    @Override
    public void updatePriceData(String id) {
        priceModel.getCurrentPrice(id,new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object success) {
                view.updateCurrentPrice(success);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}
