package com.sangu.apptongji.main.db;

/**
 * Created by Administrator on 2017-03-30.
 */

public class PermissCountList {
    private String permissCount; //可评价条数
    private String userId; //评价人
    private String merId; //被评价人

    public PermissCountList() {
    }

    public String getPermissCount() {
        return permissCount;
    }

    public void setPermissCount(String permissCount) {
        this.permissCount = permissCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }
}
