package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.model.IFindModel;
import com.sangu.apptongji.main.alluser.model.impl.FindModel;
import com.sangu.apptongji.main.alluser.presenter.IFindPresenter;
import com.sangu.apptongji.main.alluser.presenter.OnFindListener;
import com.sangu.apptongji.main.alluser.view.IFindView;

import java.util.List;

/**
 * Created by user on 2016/8/29.
 */

public class FindPresenter implements IFindPresenter,OnFindListener{
    private IFindModel findModel;
    private IFindView findView;

    public FindPresenter(Context context,IFindView findView) {
        this.findModel = new FindModel(context);
        this.findView = findView;
    }

    @Override
    public void loadUserList(String u_id,String str,String overall_flg ,String lng,String lat,String zy,String bZj,String sex,String ageStart,String ageEnd,String name,String comName,boolean gongsi,boolean jingyan,boolean hongbao) {
        findModel.getUserList(u_id,str,overall_flg,lng,lat,zy,bZj,sex,ageStart,ageEnd,name,comName,gongsi,jingyan,hongbao,this);
    }

    @Override
    public void onSuccess(List<UserAll> users,boolean hasMore) {
        if (findView != null) {
            findView.updateUserList(users,hasMore);
        }

    }

    @Override
    public void onStart() {
        findView.showLoading();
    }

    @Override
    public void onFailed(String msg) {
        findView.showError(msg);
    }

    @Override
    public void onFinish() {
        findView.hideLoading();
    }
}
