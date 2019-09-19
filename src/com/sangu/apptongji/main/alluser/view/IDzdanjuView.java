package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.DianziDanju;

import java.util.List;

/**
 * Created by Administrator on 2017-07-21.
 */

public interface IDzdanjuView extends IView{
    void updateDzdanjuList(List<DianziDanju> lists, boolean hasMore);
}
