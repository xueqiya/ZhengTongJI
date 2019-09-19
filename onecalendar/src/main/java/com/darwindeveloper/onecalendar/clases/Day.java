package com.darwindeveloper.onecalendar.clases;

import android.graphics.Color;

import java.util.Date;

/**
 * Created by DARWIN on 1/3/2017.
 */

public class Day {
    private Date date;
    private String status;
    private String time;
    private String x;
    private String y;
    private String address;
    private String typeIdentify;//标示 是首页加载企业  还是设置企业  还是加载个人  还是设置个人
    private boolean valid, selected,isPerson,isLegal;//isPerson是否为个人类型,isLegal是否为合法日期
    private String workStatus;
    private int textColor = Color.parseColor("#0099cc");
    private int textColorNV = Color.parseColor("#d2d2d2");
    private int backgroundColor = Color.parseColor("#FFF5F5F5");
    private int backgroundColorNV = Color.parseColor("#FFF5F5F5");

    public Day(Date date, int textColor, int backgroundColor) {
        this.date = date;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        valid = true;
    }

    public Day(Date date, String status, boolean valid, boolean selected) {
        this.date = date;
        this.status = status;
        this.valid = valid;
        this.selected = selected;
    }

    public Day(Date date, boolean valid, int textColorNV, int backgroundColorNV) {
        this.date = date;
        this.valid = valid;
        this.textColorNV = textColorNV;
        this.backgroundColorNV = backgroundColorNV;
    }

    public String getTypeIdentify() {
        return typeIdentify;
    }

    public void setTypeIdentify(String typeIdentify) {
        this.typeIdentify = typeIdentify;
    }

    public boolean isLegal() {
        return isLegal;
    }

    public void setLegal(boolean legal) {
        isLegal = legal;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Day(Date date, boolean valid) {
        this.date = date;
        this.valid = valid;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public boolean isPerson() {
        return isPerson;
    }

    public void setPerson(boolean person) {
        isPerson = person;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColorNV() {
        return textColorNV;
    }

    public void setTextColorNV(int textColorNV) {
        this.textColorNV = textColorNV;
    }

    public int getBackgroundColorNV() {
        return backgroundColorNV;
    }

    public void setBackgroundColorNV(int backgroundColorNV) {
        this.backgroundColorNV = backgroundColorNV;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
