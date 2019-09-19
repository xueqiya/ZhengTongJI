package com.sangu.apptongji.main.alluser.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2018-01-18.
 */

public class MySendPaidanInfo {
    private String content;
    private String distance;
    private String totalNumber;
    private String timestamp;
    private String label;
    private String sId;
    private String uId;
    private String log;
    private String lat;
    private String state;
    private String display;
    private String file;
    private String views;
    private String userID;
    private String loginId;
    private String sex1;
    private String imageStr;
    private String location;
    private String countPinglun;
    private String task_label;
    private String task_position;
    private String task_locaName;
    private String task_jurisdiction;
    private String video;
    private String videoPictures;
    private String createTime;
    private String acceptNum;
    private String responseTime;
    private String firstDistance;
    private String sum;
    private String fromUId;
    private String dynamicSeq;
    private String orderState;
    private JSONArray array;
    private JSONObject json;

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public String getDynamicSeq() {
        return dynamicSeq;
    }

    public void setDynamicSeq(String dynamicSeq) {
        this.dynamicSeq = dynamicSeq;
    }

    public JSONArray getArray() {
        return array;
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getFromUId() {
        return fromUId;
    }

    public void setFromUId(String fromUId) {
        this.fromUId = fromUId;
    }

    public String getFirstDistance() {
        return firstDistance;
    }

    public void setFirstDistance(String firstDistance) {
        this.firstDistance = firstDistance;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAcceptNum() {
        return acceptNum;
    }

    public void setAcceptNum(String acceptNum) {
        this.acceptNum = acceptNum;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getSex1() {
        return sex1;
    }

    public void setSex1(String sex1) {
        this.sex1 = sex1;
    }

    public String getImageStr() {
        return imageStr;
    }

    public void setImageStr(String imageStr) {
        this.imageStr = imageStr;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountPinglun() {
        return countPinglun;
    }

    public void setCountPinglun(String countPinglun) {
        this.countPinglun = countPinglun;
    }

    public String getTask_label() {
        return task_label;
    }

    public void setTask_label(String task_label) {
        this.task_label = task_label;
    }

    public String getTask_position() {
        return task_position;
    }

    public void setTask_position(String task_position) {
        this.task_position = task_position;
    }

    public String getTask_locaName() {
        return task_locaName;
    }

    public void setTask_locaName(String task_locaName) {
        this.task_locaName = task_locaName;
    }

    public String getTask_jurisdiction() {
        return task_jurisdiction;
    }

    public void setTask_jurisdiction(String task_jurisdiction) {
        this.task_jurisdiction = task_jurisdiction;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoPictures() {
        return videoPictures;
    }

    public void setVideoPictures(String videoPictures) {
        this.videoPictures = videoPictures;
    }

    //便于计算自己加的
    private int disstanceAddTime;

    public int getDisstanceAddTime() {
        return disstanceAddTime;
    }

    public void setDisstanceAddTime(int disstanceAddTime) {
        this.disstanceAddTime = disstanceAddTime;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    /*private String finishTime;
                private String userInfo;*/
    //展示出来的文字  不是获取到了，是自己计算便于展示的而添加的
    private String show;
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}
