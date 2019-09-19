package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;

import java.util.List;

/**
 * Created by user on 2016/9/1.
 */

public interface IOrderListQView extends IView{
    void updateQUserOrderList(List<OrderInfo> orderInfos);
    void updateQMerOrderList(List<OrderInfo> orderInfos);
}
