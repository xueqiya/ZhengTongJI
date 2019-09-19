package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.model.IProfileModel;
import com.sangu.apptongji.main.alluser.model.impl.ProfileModel;
import com.sangu.apptongji.main.alluser.presenter.IProfilePresenter;
import com.sangu.apptongji.main.alluser.presenter.OnprofileListener;
import com.sangu.apptongji.main.alluser.view.IProfileView;

/**
 * Created by user on 2016/8/29.
 */

public class ProfilePresenter implements IProfilePresenter,OnprofileListener {
    private IProfileModel profileModel;
    private IProfileView view;

    public ProfilePresenter(Context context,IProfileView view) {
        this.view = view;
        this.profileModel = new ProfileModel(context);
    }
    @Override
    public void updateData() {
        profileModel.getCurrentUserInfo(this);
    }

    @Override
    public void onSuccess(Userful user) {
        view.updateUserInfo(user);
    }

    @Override
    public void onStart() {
        view.showproLoading();
    }
    @Override
    public void onFailed() {
        view.showproError();
    }
    @Override
    public void onFinish() {
        view.hideproLoading();
    }
}
