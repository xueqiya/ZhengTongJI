package com.sangu.apptongji.main.alluser.entity;

import java.io.Serializable;

/**
 * Created by user on 2016/8/11.
 */

public class UserAll implements Serializable{
    public UserInfo getInfo() {
        return info;
    }
    public void setInfo(UserInfo info) {
        this.info = info;
    }
    private UserInfo info;
    private String uId;
    private String uLoginId;
    private String uNickName;
    private String uImage;
    private String uPassword;
    private String uSignaTure;
    private String uSex;
    private String uBirthday;
    private String uTelephone;
    private String uName;
    private String uEmail;
    private String uAge;
    private String uVocational;
    private String uCompany;
    private String uCompanyAddress;
    private String uNation;
    private String uProvince;
    private String uCity;
    private String uSchool;
    private String resv1;
    private String resv2;
    private String resv3;
    private String resv4;
    private String resv5;
    private String resv6;
    private ConList conList;
    private ProList proList;
    private String pL1;
    private String pL2;
    private String pL3;
    private String pL4;
    private String locationState;
    private String memberNumber;
    private String shareRed;
    private String friendsNumber;
    private String deviceType;
    private String margen1;
    private String margen2;
    private String margen3;
    private String margen4;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String sId;
    private String type;
    private String timestamp;
    private String messageOrderCount;
    private String messageOrderAll;

    //为了便于运算自己加的，和数据获取无关
    private String myType;

    public String getMessageOrderCount() {
        return messageOrderCount;
    }

    public void setMessageOrderCount(String messageOrderCount) {
        this.messageOrderCount = messageOrderCount;
    }

    public String getMessageOrderAll() {
        return messageOrderAll;
    }

    public void setMessageOrderAll(String messageOrderAll) {
        this.messageOrderAll = messageOrderAll;
    }

    public String getMyType() {
        return myType;
    }

    public void setMyType(String myType) {
        this.myType = myType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceType() {
        return deviceType;
    }


    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getResv5() {
        return resv5;
    }

    public void setResv5(String resv5) {
        this.resv5 = resv5;
    }

    public String getResv6() {
        return resv6;
    }

    public void setResv6(String resv6) {
        this.resv6 = resv6;
    }

    public String getFriendsNumber() {
        return friendsNumber;
    }

    public void setFriendsNumber(String friendsNumber) {
        this.friendsNumber = friendsNumber;
    }

    public String getShareRed() {
        return shareRed;
    }

    public void setShareRed(String shareRed) {
        this.shareRed = shareRed;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public String getLocationState() {
        return locationState;
    }

    public void setLocationState(String locationState) {
        this.locationState = locationState;
    }

    public String getMargen1() {
        return margen1;
    }

    public void setMargen1(String margen1) {
        this.margen1 = margen1;
    }

    public String getMargen2() {
        return margen2;
    }

    public void setMargen2(String margen2) {
        this.margen2 = margen2;
    }

    public String getMargen3() {
        return margen3;
    }

    public void setMargen3(String margen3) {
        this.margen3 = margen3;
    }

    public String getMargen4() {
        return margen4;
    }

    public void setMargen4(String margen4) {
        this.margen4 = margen4;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getpL1() {
        return pL1;
    }

    public void setpL1(String pL1) {
        this.pL1 = pL1;
    }

    public String getpL2() {
        return pL2;
    }

    public void setpL2(String pL2) {
        this.pL2 = pL2;
    }

    public String getpL3() {
        return pL3;
    }

    public void setpL3(String pL3) {
        this.pL3 = pL3;
    }

    public String getpL4() {
        return pL4;
    }

    public void setpL4(String pL4) {
        this.pL4 = pL4;
    }

    public UserAll() {
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuLoginId() {
        return uLoginId;
    }

    public void setuLoginId(String uLoginId) {
        this.uLoginId = uLoginId;
    }

    public String getuNickName() {
        return uNickName;
    }

    public void setuNickName(String uNickName) {
        this.uNickName = uNickName;
    }

    public String getuImage() {
        return uImage;
    }

    public void setuImage(String uImage) {
        this.uImage = uImage;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getuSignaTure() {
        return uSignaTure;
    }

    public void setuSignaTure(String uSignaTure) {
        this.uSignaTure = uSignaTure;
    }

    public String getuSex() {
        return uSex;
    }

    public void setuSex(String uSex) {
        this.uSex = uSex;
    }

    public String getuBirthday() {
        return uBirthday;
    }

    public void setuBirthday(String uBirthday) {
        this.uBirthday = uBirthday;
    }

    public String getuTelephone() {
        return uTelephone;
    }

    public void setuTelephone(String uTelephone) {
        this.uTelephone = uTelephone;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuAge() {
        return uAge;
    }

    public void setuAge(String uAge) {
        this.uAge = uAge;
    }

    public String getuVocational() {
        return uVocational;
    }

    public void setuVocational(String uVocational) {
        this.uVocational = uVocational;
    }

    public String getuCompany() {
        return uCompany;
    }

    public void setuCompany(String uCompany) {
        this.uCompany = uCompany;
    }

    public String getuCompanyAddress() {
        return uCompanyAddress;
    }

    public void setuCompanyAddress(String uCompanyAddress) {
        this.uCompanyAddress = uCompanyAddress;
    }

    public String getuNation() {
        return uNation;
    }

    public void setuNation(String uNation) {
        this.uNation = uNation;
    }

    public String getuProvince() {
        return uProvince;
    }

    public void setuProvince(String uProvince) {
        this.uProvince = uProvince;
    }

    public String getuCity() {
        return uCity;
    }

    public void setuCity(String uCity) {
        this.uCity = uCity;
    }

    public String getuSchool() {
        return uSchool;
    }

    public void setuSchool(String uSchool) {
        this.uSchool = uSchool;
    }

    public String getResv1() {
        return resv1;
    }

    public void setResv1(String resv1) {
        this.resv1 = resv1;
    }

    public String getResv2() {
        return resv2;
    }

    public void setResv2(String resv2) {
        this.resv2 = resv2;
    }

    public String getResv3() {
        return resv3;
    }

    public void setResv3(String resv3) {
        this.resv3 = resv3;
    }

    public ConList getConList() {
        return conList;
    }

    public void setConList(ConList conList) {
        this.conList=conList;
    }

    public ProList getProList() {
        return proList;
    }

    public void setProList(ProList proList) {
        this.proList = proList;
    }

    public String getResv4() {
        return resv4;
    }

    public void setResv4(String resv4) {
        this.resv4 = resv4;
    }
}

