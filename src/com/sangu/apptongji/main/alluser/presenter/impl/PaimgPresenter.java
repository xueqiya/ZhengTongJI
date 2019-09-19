package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.model.IPaimgModel;
import com.sangu.apptongji.main.alluser.model.impl.PaimgModel;
import com.sangu.apptongji.main.alluser.presenter.IPaimgPresenter;
import com.sangu.apptongji.main.alluser.presenter.OnFindListener;
import com.sangu.apptongji.main.alluser.view.IPaimgView;

import java.util.List;

/**
 * Created by Administrator on 2017-09-30.
 */

public class PaimgPresenter implements IPaimgPresenter,OnFindListener {
    private IPaimgModel paimgModel;
    private IPaimgView paimgView;

    public PaimgPresenter(Context context,IPaimgView paimgView) {
        this.paimgView = paimgView;
        paimgModel = new PaimgModel(context);
    }

    @Override
    public void loadUserList(String qiyeId, String com_id, String currentPage) {
        paimgModel.getUserList(qiyeId,com_id,currentPage,this);
    }

    @Override
    public void onSuccess(List<UserAll> users, boolean hasMore) {
        paimgView.updateUserList(users,hasMore);
    }

    @Override
    public void onStart() {
        paimgView.showLoading();
    }

    @Override
    public void onFailed(String msg) {
        paimgView.showError(msg);
    }

    @Override
    public void onFinish() {
        paimgView.hideLoading();
    }
}
