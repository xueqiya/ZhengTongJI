package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.UserAll;

import java.util.List;

/**
 * Created by Administrator on 2016/10/7.
 */

public interface IPaidanFinishView extends IView{
    void updateUserOrderList(List<UserAll> users, boolean hasMore);
}
