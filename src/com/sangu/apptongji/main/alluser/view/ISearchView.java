package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.order.entity.DIDAList;

import java.util.List;

/**
 * Created by Administrator on 2016-09-28.
 */

public interface ISearchView extends  IView{
    void updateThisDiDa(List<DIDAList> didaLists, String time);
}
