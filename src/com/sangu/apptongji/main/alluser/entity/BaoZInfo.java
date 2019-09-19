package com.sangu.apptongji.main.alluser.entity;

/**
 * Created by Administrator on 2017-08-08.
 */

public class BaoZInfo {
    private String lat;
    private String lng;
    private String userId;
    private String createTime;
    private String dynamicSeq;
    private String redImage;
    private String gameRed;
    private String redSum;

    public String getRedSum() {
        return redSum;
    }

    public void setRedSum(String redSum) {
        this.redSum = redSum;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDynamicSeq() {
        return dynamicSeq;
    }

    public void setDynamicSeq(String dynamicSeq) {
        this.dynamicSeq = dynamicSeq;
    }

    public String getRedImage() {
        return redImage;
    }

    public void setRedImage(String redImage) {
        this.redImage = redImage;
    }

    public String getGameRed() {
        return gameRed;
    }

    public void setGameRed(String gameRed) {
        this.gameRed = gameRed;
    }
}
