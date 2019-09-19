package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.FWFKInfo;

import java.util.List;

/**
 * Created by Administrator on 2016-10-19.
 */

public interface IFWFKView extends IView {
    void updataFuWuFanKuiList(List<FWFKInfo> fuWuFKinfo);
}
