package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.order.entity.OrderDetail;

/**
 * Created by user on 2016/9/8.
 */

public interface IOrderDetailView  extends IView{
    void updateCurrentOrderDetail(OrderDetail orderDetail);
}
