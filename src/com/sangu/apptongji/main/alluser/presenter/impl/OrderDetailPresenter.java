package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.IOrderDetailModel;
import com.sangu.apptongji.main.alluser.model.impl.OrderDetailModel;
import com.sangu.apptongji.main.alluser.order.entity.OrderDetail;
import com.sangu.apptongji.main.alluser.presenter.IOrderDetailPresenter;
import com.sangu.apptongji.main.alluser.view.IOrderDetailView;

/**
 * Created by user on 2016/9/8.
 */

public class OrderDetailPresenter implements IOrderDetailPresenter {
    private IOrderDetailView view;
    private IOrderDetailModel orderDetailModel;

    public OrderDetailPresenter(Context context,IOrderDetailView view) {
        this.view = view;
        this.orderDetailModel = new OrderDetailModel(context);
    }

    @Override
    public void loadOrderDetail(String orderId) {
        orderDetailModel.getCurrentOrderDetail(orderId, new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {
                OrderDetail orderDetail = (OrderDetail) obj;
                view.updateCurrentOrderDetail(orderDetail);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}
