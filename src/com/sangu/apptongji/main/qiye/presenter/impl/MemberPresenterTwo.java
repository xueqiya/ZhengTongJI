package com.sangu.apptongji.main.qiye.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.qiye.entity.MemberInfo;
import com.sangu.apptongji.main.qiye.model.IMemberModelTwo;
import com.sangu.apptongji.main.qiye.model.impl.MemberModelTwo;
import com.sangu.apptongji.main.qiye.presenter.IMemberPresenter;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListenerTwo;
import com.sangu.apptongji.main.qiye.view.IMemberView2;

import java.util.List;

/**
 * Created by Administrator on 2017-01-03.
 */

public class MemberPresenterTwo implements IMemberPresenter ,OnQiyeListenerTwo {
    private IMemberView2 iMemberView;
    private IMemberModelTwo iMemberModel;

    public MemberPresenterTwo(Context context, IMemberView2 iMemberView) {
        this.iMemberView = iMemberView;
        iMemberModel = new MemberModelTwo(context);
    }

    @Override
    public void loadMemberList(int currentPage) {
        iMemberModel.getMemberList(currentPage,this);
    }

    @Override
    public void onSuccess(Object obj,String total ,boolean hasMore) {
        List<MemberInfo> memberInfoList = (List<MemberInfo>) obj;
        iMemberView.updateUserList(memberInfoList,total,hasMore);
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
