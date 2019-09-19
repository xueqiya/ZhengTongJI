package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.model.IGrMemModel;
import com.sangu.apptongji.main.alluser.model.impl.GrMemModel;
import com.sangu.apptongji.main.alluser.presenter.IGrMemPresenter;
import com.sangu.apptongji.main.alluser.presenter.OnGrMemListener;
import com.sangu.apptongji.main.alluser.view.IGrMemView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-11-16.
 */

public class GrMemPresenter implements IGrMemPresenter,OnGrMemListener{
    private IGrMemView grMemView;
    private IGrMemModel grMemModel;

    public GrMemPresenter(Context context,IGrMemView grMemView) {
        this.grMemView = grMemView;
        grMemModel = new GrMemModel(context);
    }

    @Override
    public void loadGroupList(String groupId, String currentPage) {
        grMemModel.getUserList(groupId,currentPage,this);
    }

    @Override
    public void onSuccess(List<UserAll> userAlls, JSONObject obj, boolean hasMore) {
        grMemView.updataGroupList(userAlls,obj,hasMore);
    }

    @Override
    public void onStart() {
        grMemView.showproLoading();
    }

    @Override
    public void onFailed() {
        grMemView.showproError();
    }

    @Override
    public void onFinish() {
        grMemView.hideproLoading();
    }
}
