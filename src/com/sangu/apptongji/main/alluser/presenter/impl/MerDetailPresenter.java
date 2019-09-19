package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.MerDetail;
import com.sangu.apptongji.main.alluser.model.IMerDetailModel;
import com.sangu.apptongji.main.alluser.model.impl.MerDetailModel;
import com.sangu.apptongji.main.alluser.presenter.IMerDetailPresenter;
import com.sangu.apptongji.main.alluser.presenter.OnMerListener;
import com.sangu.apptongji.main.alluser.view.IMerDetailView;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Administrator on 2017-04-27.
 */

public class MerDetailPresenter implements IMerDetailPresenter,OnMerListener {
    private IMerDetailModel merDetailModel;
    private IMerDetailView merDetailView;

    public MerDetailPresenter(Context context,IMerDetailView merDetailView) {
        this.merDetailView = merDetailView;
        this.merDetailModel = new MerDetailModel(context);
    }

    @Override
    public void loadMerDetail(String currentPage,String hxid,String leixing,String type, String beginDate, String endDate) {
        merDetailModel.getThisUser(currentPage,hxid,leixing,type,beginDate,endDate,this);
    }

    @Override
    public void onSuccess(List<MerDetail> merDetails, JSONArray array,String size,String income,String expenditure, boolean hasMore) {
        merDetailView.updateUserList(merDetails,array,size,income,expenditure,hasMore);
    }

    @Override
    public void onStart() {
        merDetailView.showLoading();
    }

    @Override
    public void onFailed() {
        merDetailView.showError();
    }

    @Override
    public void onFinish() {
        merDetailView.hideLoading();
    }
}
