package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.model.IFriendListModel;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.impl.FriendListModel;
import com.sangu.apptongji.main.alluser.presenter.IFriendListPresenter;
import com.sangu.apptongji.main.alluser.view.IFriendListView;

import java.util.List;

/**
 * Created by Administrator on 2016/10/7.
 */

public class FriendListPresenter implements IFriendListPresenter {
    private IFriendListModel friendListModel;
    private IFriendListView view;

    public FriendListPresenter(Context context,IFriendListView view) {
        this.view = view;
        this.friendListModel = new FriendListModel(context);
    }

    @Override
    public void loadFriendList(final String currentPage) {
        friendListModel.getFriendList(currentPage,new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<UserAll> users = (List<UserAll>) obj;
                if (users!=null&&users.size()>19) {
                    view.updateUserOrderList(users,true);
                }else {
                    view.updateUserOrderList(users,false);
                }
            }

            @Override
            public void onError(Object error) {
                view.updateUserOrderList(null,true);
            }
        });
    }
}
