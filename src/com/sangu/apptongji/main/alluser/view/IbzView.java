package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.BaoZInfo;

import java.util.List;

/**
 * Created by Administrator on 2017-08-08.
 */

public interface IbzView extends IView {
    void updateBzList(List<BaoZInfo> baozInfos);
}
