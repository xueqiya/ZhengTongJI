package com.sangu.apptongji.main.alluser.order.entity;

/**
 * Created by Administrator on 2017-04-10.
 */

public class RechargeRecord {
    private String userId;
    private String userName;
    private String amount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
