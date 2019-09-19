package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;

import java.util.List;

/**
 * Created by user on 2016/9/1.
 */

public interface IOrderListView extends IView{
    void updateUserOrderList(List<OrderInfo> orderInfos);
    void updateMerOrderList(List<OrderInfo> orderInfos);
}
