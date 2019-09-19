package com.sangu.apptongji.main.qiye.entity;

/**
 * Created by Administrator on 2017-02-16.
 */

public class HeTongInfo {
    private String companyId;
    private String userId;
    private String agreement1;
    private String agreement2;
    private String createTime;
    private String comSignature;
    private String userSignature;
    private String signatureTime;
    private String uCompany;
    private String uName;
    private String resv1;

    public String getSignatureTime() {
        return signatureTime;
    }

    public String getResv1() {
        return resv1;
    }

    public void setResv1(String resv1) {
        this.resv1 = resv1;
    }

    public void setSignatureTime(String signatureTime) {
        this.signatureTime = signatureTime;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAgreement1() {
        return agreement1;
    }

    public void setAgreement1(String agreement1) {
        this.agreement1 = agreement1;
    }

    public String getAgreement2() {
        return agreement2;
    }

    public void setAgreement2(String agreement2) {
        this.agreement2 = agreement2;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getComSignature() {
        return comSignature;
    }

    public void setComSignature(String comSignature) {
        this.comSignature = comSignature;
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    public String getuCompany() {
        return uCompany;
    }

    public void setuCompany(String uCompany) {
        this.uCompany = uCompany;
    }

}
