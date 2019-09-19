package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;

import java.util.List;

/**
 * Created by user on 2016/9/1.
 */

public interface IDanjuListView extends IView{
    void updateQDanjuList(List<OrderInfo> orderInfos);
}
