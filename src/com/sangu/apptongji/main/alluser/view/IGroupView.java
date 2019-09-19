package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.GroupInfo;

import java.util.List;

/**
 * Created by Administrator on 2017-11-14.
 */

public interface IGroupView extends IView {
    void updataGroupList(List<GroupInfo> groupInfos,boolean hasMore);
}
