package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.Friend;

import java.util.List;

/**
 * Created by user on 2016/8/31.
 */

public interface IFriendView extends IView{
    void updateFriendQList(List<Friend> friends);
}
