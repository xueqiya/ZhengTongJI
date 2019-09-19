package com.sangu.apptongji.main.qiye.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-03-22.
 */

public class OffSendOrderList implements Serializable{
    private String companyId;//企业id
//    private String cusName;//客户
//    private String cusPhone;//客户
//    private String cusAdress;//客户
    private String cusInfo;//客户信息，名字/电话/地址
//    private String orderDesc;//订单描述
//    private String infoPrice;//服务费用
//    private String orderPrice;//订单费用
    private String orderInfo;//订单信息，描述/服务费用/订单费用
    private String time;//签名时间2/签名时间3
    private String time_seq;//签名时间1
    private String signature1;//签名1
    private String signature2;//签名2
    private String signature3;//签名3
    private String signature4;//签名4
    private String beforeService1;//服务前1
    private String beforeService2;//服务前2
    private String beforeService3;//服务前3
    private String beforeService4;//服务前4
    private String afterService1;//服务后1
    private String afterService2;//服务后2
    private String afterService3;//服务后3
    private String afterService4;//服务后4
    private String ordName;//订单人名字
    private String ordId;//订单人id
    private String imageCount;//记录第二次传的图片张数 "|"拼接
    //派单信息
    private String sendIdentify;//员工或者好友的标识

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCusInfo() {
        return cusInfo;
    }

    public void setCusInfo(String cusInfo) {
        this.cusInfo = cusInfo;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime_seq() {
        return time_seq;
    }

    public void setTime_seq(String time_seq) {
        this.time_seq = time_seq;
    }

    public String getSignature1() {
        return signature1;
    }

    public void setSignature1(String signature1) {
        this.signature1 = signature1;
    }

    public String getSignature2() {
        return signature2;
    }

    public void setSignature2(String signature2) {
        this.signature2 = signature2;
    }

    public String getSignature3() {
        return signature3;
    }

    public void setSignature3(String signature3) {
        this.signature3 = signature3;
    }

    public String getSignature4() {
        return signature4;
    }

    public void setSignature4(String signature4) {
        this.signature4 = signature4;
    }

    public String getBeforeService1() {
        return beforeService1;
    }

    public void setBeforeService1(String beforeService1) {
        this.beforeService1 = beforeService1;
    }

    public String getBeforeService2() {
        return beforeService2;
    }

    public void setBeforeService2(String beforeService2) {
        this.beforeService2 = beforeService2;
    }

    public String getBeforeService3() {
        return beforeService3;
    }

    public void setBeforeService3(String beforeService3) {
        this.beforeService3 = beforeService3;
    }

    public String getBeforeService4() {
        return beforeService4;
    }

    public void setBeforeService4(String beforeService4) {
        this.beforeService4 = beforeService4;
    }

    public String getAfterService1() {
        return afterService1;
    }

    public void setAfterService1(String afterService1) {
        this.afterService1 = afterService1;
    }

    public String getAfterService2() {
        return afterService2;
    }

    public void setAfterService2(String afterService2) {
        this.afterService2 = afterService2;
    }

    public String getAfterService3() {
        return afterService3;
    }

    public void setAfterService3(String afterService3) {
        this.afterService3 = afterService3;
    }

    public String getAfterService4() {
        return afterService4;
    }

    public void setAfterService4(String afterService4) {
        this.afterService4 = afterService4;
    }

    public String getOrdName() {
        return ordName;
    }

    public void setOrdName(String ordName) {
        this.ordName = ordName;
    }

    public String getOrdId() {
        return ordId;
    }

    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }

    public String getImageCount() {
        return imageCount;
    }

    public void setImageCount(String imageCount) {
        this.imageCount = imageCount;
    }

    public String getSendIdentify() {
        return sendIdentify;
    }

    public void setSendIdentify(String sendIdentify) {
        this.sendIdentify = sendIdentify;
    }
}
