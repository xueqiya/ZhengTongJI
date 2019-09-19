package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.LiuLanDetail;
import com.sangu.apptongji.main.alluser.model.ILiulanListModel;
import com.sangu.apptongji.main.alluser.model.impl.LiulanListModel;
import com.sangu.apptongji.main.alluser.presenter.ILiulanListPresenter;
import com.sangu.apptongji.main.alluser.presenter.OnLiuLanListener;
import com.sangu.apptongji.main.alluser.view.ILiulanListView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-08-30.
 */

public class LiulanListPresenter implements ILiulanListPresenter,OnLiuLanListener {
    private ILiulanListView liulanListView;
    private ILiulanListModel listModel;

    public LiulanListPresenter(Context context,ILiulanListView liulanListView) {
        this.liulanListView = liulanListView;
        listModel = new LiulanListModel(context);
    }

    @Override
    public void loadLiuLanList(String currentPage, String uLoginId, String leixing) {
        listModel.getThisUser(currentPage,uLoginId,leixing,this);
    }

    @Override
    public void onSuccess(List<LiuLanDetail> liuLanDetails, JSONObject object,String size, boolean hasMore) {
        liulanListView.updateLiuLanList(liuLanDetails,object,size,hasMore);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onFinish() {

    }
}
