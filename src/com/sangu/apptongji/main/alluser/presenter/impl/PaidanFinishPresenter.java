package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.IPaidanFinishModel;
import com.sangu.apptongji.main.alluser.model.impl.PaidanFinishModel;
import com.sangu.apptongji.main.alluser.presenter.IPaidanFinishPresenter;
import com.sangu.apptongji.main.alluser.view.IPaidanFinishView;

import java.util.List;

/**
 * Created by Administrator on 2016/10/7.
 */

public class PaidanFinishPresenter implements IPaidanFinishPresenter {
    private IPaidanFinishModel friendListModel;
    private IPaidanFinishView view;

    public PaidanFinishPresenter(Context context, IPaidanFinishView view) {
        this.view = view;
        this.friendListModel = new PaidanFinishModel(context);
    }

    @Override
    public void loadShifuList(String dynamicSeq, String createTime, String sId, String state) {
        friendListModel.getShifuList(dynamicSeq,createTime,sId,state,new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<UserAll> users = (List<UserAll>) obj;
                view.updateUserOrderList(users,false);
            }

            @Override
            public void onError(Object error) {
                view.updateUserOrderList(null,true);
            }
        });
    }

    @Override
    public void updateShifuContace(String uId, String sId, String type) {
        friendListModel.updateShifuContace(uId,sId,type,new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {

            }

            @Override
            public void onError(Object error) {
                view.updateUserOrderList(null,true);
            }
        });
    }
}
