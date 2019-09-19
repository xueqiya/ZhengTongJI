package com.sangu.apptongji.main.qiye.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.qiye.entity.MemberInfo;
import com.sangu.apptongji.main.qiye.model.IMemberModel;
import com.sangu.apptongji.main.qiye.model.impl.MemberModel;
import com.sangu.apptongji.main.qiye.presenter.IMemberPresenter;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;
import com.sangu.apptongji.main.qiye.view.IMemberView;

import java.util.List;

/**
 * Created by Administrator on 2017-01-03.
 */

public class MemberPresenter implements IMemberPresenter ,OnQiyeListener{
    private IMemberView iMemberView;
    private IMemberModel iMemberModel;

    public MemberPresenter(Context context,IMemberView iMemberView) {
        this.iMemberView = iMemberView;
        iMemberModel = new MemberModel(context);
    }

    @Override
    public void loadMemberList(int currentPage) {
        iMemberModel.getMemberList(currentPage,this);
    }

    @Override
    public void onSuccess(Object obj,boolean hasMore) {
        List<MemberInfo> memberInfoList = (List<MemberInfo>) obj;
        iMemberView.updateUserList(memberInfoList,hasMore);
    }

    @Override
    public void onStart() {
        iMemberView.showLoading();
    }

    @Override
    public void onFailed() {
        iMemberView.showError();
    }

    @Override
    public void onFinish() {
        iMemberView.hideLoading();
    }
}
