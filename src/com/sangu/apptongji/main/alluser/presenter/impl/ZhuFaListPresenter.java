package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.ZhuFaDetail;
import com.sangu.apptongji.main.alluser.model.IZhuFaListModel;
import com.sangu.apptongji.main.alluser.model.impl.ZhuFaListModel;
import com.sangu.apptongji.main.alluser.presenter.IZhuFaListPresenter;
import com.sangu.apptongji.main.alluser.presenter.OnZhuFaListener;
import com.sangu.apptongji.main.alluser.view.IZhuanFaListView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-08-30.
 */

public class ZhuFaListPresenter implements IZhuFaListPresenter,OnZhuFaListener {
    private IZhuanFaListView liulanListView;
    private IZhuFaListModel listModel;

    public ZhuFaListPresenter(Context context,IZhuanFaListView liulanListView) {
        this.liulanListView = liulanListView;
        listModel = new ZhuFaListModel(context);
    }

    @Override
    public void loadZhuFaList(String currentPage, String uLoginId, String leixing) {
        listModel.getThisUser(currentPage,uLoginId,leixing,this);
    }

    @Override
    public void onSuccess(List<ZhuFaDetail> zhuFaDetails, JSONObject object, String size,boolean hasMore) {
        liulanListView.updateZhuFaList(zhuFaDetails,object,size,hasMore);
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
