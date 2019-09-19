package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.model.ISearchModel;
import com.sangu.apptongji.main.alluser.model.ITIXIANModel;
import com.sangu.apptongji.main.alluser.model.impl.SearchModel;
import com.sangu.apptongji.main.alluser.order.entity.DIDAList;
import com.sangu.apptongji.main.alluser.presenter.ISearchPresenter;
import com.sangu.apptongji.main.alluser.view.ISearchView;

import java.util.List;

/**
 * Created by Administrator on 2016-09-28.
 */

public class SearchPresenter implements ISearchPresenter {
    private ISearchModel searchModel;
    private ISearchView view;

    public SearchPresenter(Context context,ISearchView view) {
        this.view = view;
        searchModel = new SearchModel(context);
    }

    @Override
    public void loadOrderDetail(String upId) {
        searchModel.getThisDIDA(upId, new ITIXIANModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj,String time) {
                List<DIDAList> didaLists = (List<DIDAList>) obj;
                view.updateThisDiDa(didaLists,time);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}
