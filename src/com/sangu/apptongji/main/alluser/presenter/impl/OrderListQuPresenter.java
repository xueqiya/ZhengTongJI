package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.IOrderListQModel;
import com.sangu.apptongji.main.alluser.model.impl.OrderListQuModel;
import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;
import com.sangu.apptongji.main.alluser.presenter.IOrderListQPresenter;
import com.sangu.apptongji.main.alluser.view.IOrderListQView;

import java.util.List;

/**
 * Created by Administrator on 2016-11-15.
 */

public class OrderListQuPresenter implements IOrderListQPresenter {
    private IOrderListQView orderListView;
    private IOrderListQModel orderListModel;

    public OrderListQuPresenter(Context context,IOrderListQView view) {
        this.orderListModel = new OrderListQuModel(context);
        this.orderListView = view;
    }
    @Override
    public void loadOrderList(String state) {
        orderListModel.getMerOrderList(state,new IModel.AsyncCallback() {

            @Override
            public void onSuccess(Object obj) {
                if (!(obj=="")) {
                    List<OrderInfo> orderInfos = (List<OrderInfo>) obj;
                    orderListView.updateQMerOrderList(orderInfos);
                }else {
                    orderListView.updateQMerOrderList(null);
                }
            }

            @Override
            public void onError(Object error) {

            }
        });

        orderListModel.getUserOrderList(state,new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (!(obj=="")) {
                    List<OrderInfo> orderInfos = (List<OrderInfo>) obj;
                    orderListView.updateQUserOrderList(orderInfos);
                }else {
                    orderListView.updateQUserOrderList(null);
                }
            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}
