package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.GroupInfo;
import com.sangu.apptongji.main.alluser.model.IGroupModel;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.impl.GroupModel;
import com.sangu.apptongji.main.alluser.presenter.IGroupPresenter;
import com.sangu.apptongji.main.alluser.view.IGroupView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-11-14.
 */

public class GroupPresenter implements IGroupPresenter {
    private IGroupView groupView;
    private IGroupModel groupModel;

    public GroupPresenter(Context context, IGroupView groupView) {
        this.groupView = groupView;
        groupModel = new GroupModel(context);
    }

    @Override
    public void loadGroupList(String uId, String currentPage) {
        groupModel.getUserList(uId, currentPage, new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object success) {
                if (success!=null) {
                    List<GroupInfo> groupInfos = (List<GroupInfo>) success;
                    if (groupInfos.size()==20) {
                        groupView.updataGroupList(groupInfos,true);
                    }else {
                        groupView.updataGroupList(groupInfos,false);
                    }
                }else {
                    groupView.updataGroupList(new ArrayList<GroupInfo>(),false);
                }
            }

            @Override
            public void onError(Object error) {
                groupView.updataGroupList(new ArrayList<GroupInfo>(),false);
            }
        });
    }
}
