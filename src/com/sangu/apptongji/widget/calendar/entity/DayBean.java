package com.sangu.apptongji.widget.calendar.entity;

import org.joda.time.DateTime;

/**
 * Created by Administrator on 2018-02-22.
 */

public class DayBean {
    public DateTime dateTime;
    // 0 表示工作  1 表示活动  2 表示假期
    public int type;
    public boolean isChangeAble;
    public boolean isCurrentMonth;

    private String amShangban;
    private String amXiaban;
    private String pmShangban;
    private String pmXiaban;
    //表示是否修改过
    private boolean isChanged;
    //纬度
    private String lat;
    //经度
    private String lng;
    //地址
    private String location;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public String getAmShangban() {
        return amShangban;
    }

    public void setAmShangban(String amShangban) {
        this.amShangban = amShangban;
    }

    public String getAmXiaban() {
        return amXiaban;
    }

    public void setAmXiaban(String amXiaban) {
        this.amXiaban = amXiaban;
    }

    public String getPmShangban() {
        return pmShangban;
    }

    public void setPmShangban(String pmShangban) {
        this.pmShangban = pmShangban;
    }

    public String getPmXiaban() {
        return pmXiaban;
    }

    public void setPmXiaban(String pmXiaban) {
        this.pmXiaban = pmXiaban;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isChangeAble() {
        return isChangeAble;
    }

    public void setChangeAble(boolean changeAble) {
        isChangeAble = changeAble;
    }

    @Override
    public String toString() {
        return "DayBean{" +
                "dateTime=" + dateTime +
                ", type=" + type +
                ", isChangeAble=" + isChangeAble +
                ", isCurrentMonth=" + isCurrentMonth +
                ", amShangban='" + amShangban + '\'' +
                ", amXiaban='" + amXiaban + '\'' +
                ", pmShangban='" + pmShangban + '\'' +
                ", pmXiaban='" + pmXiaban + '\'' +
                ", isChanged=" + isChanged +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
