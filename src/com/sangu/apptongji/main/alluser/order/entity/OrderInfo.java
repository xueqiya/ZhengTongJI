package com.sangu.apptongji.main.alluser.order.entity;

import java.io.Serializable;

/**
 * Created by user on 2016/8/29.
 */

public class OrderInfo implements Serializable{
    private String orderId;
    private String orderType;
    private String orderState;
    private String orderTime;
    private String userId;
    private String merId;
    private String orderTotalAmt;
    private String orderAmt;
    private String orderBody;
    private String remark;
    private String resv1;
    private String resv2;
    private String resv3;
    private String u_nickname;
    private String u_name;
    private String u_uImage;
    private String u_uCompany;
    private String m_nickname;
    private String m_name;
    private String m_uImage;
    private String m_uCompany;
    private String totalAmt;
    private String send_id1;
    private String send_id2;
    private String finalSum;

    public String getFinalSum() {
        return finalSum;
    }

    public void setFinalSum(String finalSum) {
        this.finalSum = finalSum;
    }

    public String getSend_id1() {
        return send_id1;
    }

    public void setSend_id1(String send_id1) {
        this.send_id1 = send_id1;
    }

    public String getSend_id2() {
        return send_id2;
    }

    public void setSend_id2(String send_id2) {
        this.send_id2 = send_id2;
    }

    public void setOrderId(String orderId){
        this.orderId = orderId;
    }
    public String getOrderId(){
        return this.orderId;
    }
    public void setOrderType(String orderType){
        this.orderType = orderType;
    }
    public String getOrderType(){
        return this.orderType;
    }
    public void setOrderState(String orderState){
        this.orderState = orderState;
    }
    public String getOrderState(){
        return this.orderState;
    }
    public void setOrderTime(String orderTime){
        this.orderTime = orderTime;
    }
    public String getOrderTime(){
        return this.orderTime;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }
    public void setMerId(String merId){
        this.merId = merId;
    }
    public String getMerId(){
        return this.merId;
    }
    public void setOrderTotalAmt(String orderTotalAmt){
        this.orderTotalAmt = orderTotalAmt;
    }
    public String getOrderTotalAmt(){
        return this.orderTotalAmt;
    }
    public void setOrderAmt(String orderAmt){
        this.orderAmt = orderAmt;
    }
    public String getOrderAmt(){
        return this.orderAmt;
    }
    public void setOrderBody(String orderBody){
        this.orderBody = orderBody;
    }
    public String getOrderBody(){
        return this.orderBody;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
    public String getRemark(){
        return this.remark;
    }
    public void setResv1(String resv1){
        this.resv1 = resv1;
    }
    public String getResv1(){
        return this.resv1;
    }
    public void setResv2(String resv2){
        this.resv2 = resv2;
    }
    public String getResv2(){
        return this.resv2;
    }
    public void setResv3(String resv3){
        this.resv3 = resv3;
    }
    public String getResv3(){
        return this.resv3;
    }
    public void setU_nickname(String u_nickname){
        this.u_nickname = u_nickname;
    }
    public String getU_nickname(){
        return this.u_nickname;
    }
    public void setU_name(String u_name){
        this.u_name = u_name;
    }
    public String getU_name(){
        return this.u_name;
    }
    public void setU_uImage(String u_uImage){
        this.u_uImage = u_uImage;
    }
    public String getU_uImage(){
        return this.u_uImage;
    }
    public void setU_uCompany(String u_uCompany){
        this.u_uCompany = u_uCompany;
    }
    public String getU_uCompany(){
        return this.u_uCompany;
    }
    public void setM_nickname(String m_nickname){
        this.m_nickname = m_nickname;
    }
    public String getM_nickname(){
        return this.m_nickname;
    }
    public void setM_name(String m_name){
        this.m_name = m_name;
    }
    public String getM_name(){
        return this.m_name;
    }
    public void setM_uImage(String m_uImage){
        this.m_uImage = m_uImage;
    }
    public String getM_uImage(){
        return this.m_uImage;
    }
    public void setM_uCompany(String m_uCompany){
        this.m_uCompany = m_uCompany;
    }
    public String getM_uCompany(){
        return this.m_uCompany;
    }
    public void setTotalAmt(String totalAmt){
        this.totalAmt = totalAmt;
    }
    public String getTotalAmt(){
        return this.totalAmt;
    }
}
