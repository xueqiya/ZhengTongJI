package com.sangu.apptongji.main.alluser.presenter.impl;

import android.content.Context;

import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.IOrderListModel;
import com.sangu.apptongji.main.alluser.model.impl.OrderListModel;
import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;
import com.sangu.apptongji.main.alluser.presenter.IOrderListPresenter;
import com.sangu.apptongji.main.alluser.view.IOrderListView;

import java.util.List;

/**
 * Created by user on 2016/9/1.
 */

public class OrderListPresenter implements IOrderListPresenter {
    private IOrderListView orderListView;
    private IOrderListModel orderListModel;

    public OrderListPresenter(Context context,IOrderListView view) {
        this.orderListModel = new OrderListModel(context);
        this.orderListView = view;
    }

    @Override
    public void loadOrderList(String id,String state) {
        orderListModel.getMerOrderList(id,state,new IModel.AsyncCallback() {

            @Override
            public void onSuccess(Object obj) {
                if (obj!=null) {
                    List<OrderInfo> orderInfos = (List<OrderInfo>) obj;
                    orderListView.updateMerOrderList(orderInfos);
                }else {
                    orderListView.updateMerOrderList(null);
                }
            }

            @Override
            public void onError(Object error) {

            }
        });

        orderListModel.getUserOrderList(id,state,new IModel.AsyncCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (obj!=null) {
                    List<OrderInfo> orderInfos = (List<OrderInfo>) obj;
                    orderListView.updateUserOrderList(orderInfos);
                }else {
                    orderListView.updateUserOrderList(null);
                }
            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}
