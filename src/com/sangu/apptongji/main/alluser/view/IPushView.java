package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.PushDetail;

import java.util.List;

/**
 * Created by Administrator on 2017-12-06.
 */

public interface IPushView extends IView {
    void updatePushList(List<PushDetail> pushLists);
}
