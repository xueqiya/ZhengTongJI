package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.model.IUAZModel;
import com.sangu.apptongji.main.alluser.model.impl.UAZModel;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.OnprofileListener;
import com.sangu.apptongji.main.alluser.view.IUAZView;

/**
 * Created by Administrator on 2016-09-26.
 */

public class UAZPresenter implements IUAZPresenter,OnprofileListener {
    private IUAZView view;
    private IUAZModel uazModel;

    public UAZPresenter(Context context,IUAZView view) {
        this.view = view;
        this.uazModel = new UAZModel(context);
    }

    @Override
    public void sendContactTrack(String u_id, String contact_id,String type) {
        uazModel.sendContactTrack(u_id,contact_id,type);
    }
    @Override
    public void loadThisDetail(String hxid) {
        uazModel.getThisUser(hxid,this);
    }
    @Override
    public void onSuccess(Userful user) {
        view.updateThisUser(user);
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
