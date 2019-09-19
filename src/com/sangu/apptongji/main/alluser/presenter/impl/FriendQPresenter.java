package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.Friend;
import com.sangu.apptongji.main.alluser.model.IFriendQModel;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.impl.FriendModel;
import com.sangu.apptongji.main.alluser.presenter.IFriendPresenter;
import com.sangu.apptongji.main.alluser.view.IFriendView;

import java.util.List;

/**
 * Created by user on 2016/8/31.
 */

public class FriendQPresenter implements IFriendPresenter {
    private IFriendQModel friendQModel;
    private IFriendView friendView;

    public FriendQPresenter(Context context,IFriendView friendView) {
        this.friendQModel = new FriendModel(context);
        this.friendView = friendView;
    }

    @Override
    public void loadFriendQList() {
        friendQModel.getFriendQList(new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<Friend> friends = (List<Friend>) obj;
                friendView.updateFriendQList(friends);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}
