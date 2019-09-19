package com.sangu.apptongji.main.alluser.entity;

/**
 * Created by Administrator on 2018-01-18.
 */

public class SendToMePaidan {
    private String content;
    private String display;
    private String distance;
    private String label;
    private String sId;
    private String state;
    private String timestamp;
    private String totalNumber;
    private String finishTime;
    private String uId;
    private String log;
    private String lat;
    private String file;

    private String acceptNum;
    private String orderId;
    private String orderTime;
    private String merId;
    private String orderBody;//下单人的公司
    private String remark;//接单人的公司
    private String resv2;
    private String resv3;
    private String uLoginId;
    private String uImage;
    private String uName;
    private String dynamicSeq;
    private String createTime;
    private String userId;
    private String location;
    private String authtype;
    private String views;
    private String task_label;
    private String task_position;
    private String task_locaName;
    private String task_jurisdiction;
    private String orderState;
    private String deviceType;
    private String dynamicSeqR;
    private String createTimeR;
    private String typeR;
    private String uIdR;
    private String responsetimeR;
    private String ordernumR;
    private String timestampR;
    private String statusR;

    public String getStatusR() {
        return statusR;
    }

    public void setStatusR(String statusR) {
        this.statusR = statusR;
    }

    public String getAcceptNum() {
        return acceptNum;
    }

    public void setAcceptNum(String acceptNum) {
        this.acceptNum = acceptNum;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getOrderBody() {
        return orderBody;
    }

    public void setOrderBody(String orderBody) {
        this.orderBody = orderBody;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getuLoginId() {
        return uLoginId;
    }

    public void setuLoginId(String uLoginId) {
        this.uLoginId = uLoginId;
    }

    public String getuImage() {
        return uImage;
    }

    public void setuImage(String uImage) {
        this.uImage = uImage;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getDynamicSeq() {
        return dynamicSeq;
    }

    public void setDynamicSeq(String dynamicSeq) {
        this.dynamicSeq = dynamicSeq;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAuthtype() {
        return authtype;
    }

    public void setAuthtype(String authtype) {
        this.authtype = authtype;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
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

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDynamicSeqR() {
        return dynamicSeqR;
    }

    public void setDynamicSeqR(String dynamicSeqR) {
        this.dynamicSeqR = dynamicSeqR;
    }

    public String getCreateTimeR() {
        return createTimeR;
    }

    public void setCreateTimeR(String createTimeR) {
        this.createTimeR = createTimeR;
    }

    public String getTypeR() {
        return typeR;
    }

    public void setTypeR(String typeR) {
        this.typeR = typeR;
    }

    public String getuIdR() {
        return uIdR;
    }

    public void setuIdR(String uIdR) {
        this.uIdR = uIdR;
    }

    public String getResponsetimeR() {
        return responsetimeR;
    }

    public void setResponsetimeR(String responsetimeR) {
        this.responsetimeR = responsetimeR;
    }

    public String getOrdernumR() {
        return ordernumR;
    }

    public void setOrdernumR(String ordernumR) {
        this.ordernumR = ordernumR;
    }

    public String getTimestampR() {
        return timestampR;
    }

    public void setTimestampR(String timestampR) {
        this.timestampR = timestampR;
    }

    //自己为了便于计算加的本地
    private String show;

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    private UserInfo userInfo;

    public class  UserInfo{
        //user
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
        private String shippingAddress;
        private String pushState;
        private String locationState;
        private String subsidyAmt;
        private String putinAmt;
        private String forwardAmt;
        private String resv1;
        private String resv2;
        private String resv3;
        private String resv4;
        private String resv5;
        private String resv6;
        private String userConsumers;
        private String userProfessions;
        private String signStateInfo;
        private String liveAuthInfo;
        private String dataCount;
        private String menberNum;
        private String uLocation;
        private String shareRed;
        private String singleShare;
        private int personalDtails;
        private String friendsNumber;
        private int shareAmt;
        private String createTime;
        private String uFiles;
        private String updateTime;
        private String v_id;
        private int friendsTotal;
        private String redInterval;
        private String region;
        private int shareTimes;
        private int homePageTimes;
        private int dynamicTimes;
        private String deviceType;
        private String shareType;
        private String shareRedSort;
        private String score;
        private String comClockInfo;
        private String setSignaTime;

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

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public String getPushState() {
            return pushState;
        }

        public void setPushState(String pushState) {
            this.pushState = pushState;
        }

        public String getLocationState() {
            return locationState;
        }

        public void setLocationState(String locationState) {
            this.locationState = locationState;
        }

        public String getSubsidyAmt() {
            return subsidyAmt;
        }

        public void setSubsidyAmt(String subsidyAmt) {
            this.subsidyAmt = subsidyAmt;
        }

        public String getPutinAmt() {
            return putinAmt;
        }

        public void setPutinAmt(String putinAmt) {
            this.putinAmt = putinAmt;
        }

        public String getForwardAmt() {
            return forwardAmt;
        }

        public void setForwardAmt(String forwardAmt) {
            this.forwardAmt = forwardAmt;
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

        public String getResv4() {
            return resv4;
        }

        public void setResv4(String resv4) {
            this.resv4 = resv4;
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

        public String getUserConsumers() {
            return userConsumers;
        }

        public void setUserConsumers(String userConsumers) {
            this.userConsumers = userConsumers;
        }

        public String getUserProfessions() {
            return userProfessions;
        }

        public void setUserProfessions(String userProfessions) {
            this.userProfessions = userProfessions;
        }

        public String getSignStateInfo() {
            return signStateInfo;
        }

        public void setSignStateInfo(String signStateInfo) {
            this.signStateInfo = signStateInfo;
        }

        public String getLiveAuthInfo() {
            return liveAuthInfo;
        }

        public void setLiveAuthInfo(String liveAuthInfo) {
            this.liveAuthInfo = liveAuthInfo;
        }

        public String getDataCount() {
            return dataCount;
        }

        public void setDataCount(String dataCount) {
            this.dataCount = dataCount;
        }

        public String getMenberNum() {
            return menberNum;
        }

        public void setMenberNum(String menberNum) {
            this.menberNum = menberNum;
        }

        public String getuLocation() {
            return uLocation;
        }

        public void setuLocation(String uLocation) {
            this.uLocation = uLocation;
        }

        public String getShareRed() {
            return shareRed;
        }

        public void setShareRed(String shareRed) {
            this.shareRed = shareRed;
        }

        public String getSingleShare() {
            return singleShare;
        }

        public void setSingleShare(String singleShare) {
            this.singleShare = singleShare;
        }

        public int getPersonalDtails() {
            return personalDtails;
        }

        public void setPersonalDtails(int personalDtails) {
            this.personalDtails = personalDtails;
        }

        public String getFriendsNumber() {
            return friendsNumber;
        }

        public void setFriendsNumber(String friendsNumber) {
            this.friendsNumber = friendsNumber;
        }

        public int getShareAmt() {
            return shareAmt;
        }

        public void setShareAmt(int shareAmt) {
            this.shareAmt = shareAmt;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getuFiles() {
            return uFiles;
        }

        public void setuFiles(String uFiles) {
            this.uFiles = uFiles;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getV_id() {
            return v_id;
        }

        public void setV_id(String v_id) {
            this.v_id = v_id;
        }

        public int getFriendsTotal() {
            return friendsTotal;
        }

        public void setFriendsTotal(int friendsTotal) {
            this.friendsTotal = friendsTotal;
        }

        public String getRedInterval() {
            return redInterval;
        }

        public void setRedInterval(String redInterval) {
            this.redInterval = redInterval;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public int getShareTimes() {
            return shareTimes;
        }

        public void setShareTimes(int shareTimes) {
            this.shareTimes = shareTimes;
        }

        public int getHomePageTimes() {
            return homePageTimes;
        }

        public void setHomePageTimes(int homePageTimes) {
            this.homePageTimes = homePageTimes;
        }

        public int getDynamicTimes() {
            return dynamicTimes;
        }

        public void setDynamicTimes(int dynamicTimes) {
            this.dynamicTimes = dynamicTimes;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getShareType() {
            return shareType;
        }

        public void setShareType(String shareType) {
            this.shareType = shareType;
        }

        public String getShareRedSort() {
            return shareRedSort;
        }

        public void setShareRedSort(String shareRedSort) {
            this.shareRedSort = shareRedSort;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getComClockInfo() {
            return comClockInfo;
        }

        public void setComClockInfo(String comClockInfo) {
            this.comClockInfo = comClockInfo;
        }

        public String getSetSignaTime() {
            return setSignaTime;
        }

        public void setSetSignaTime(String setSignaTime) {
            this.setSignaTime = setSignaTime;
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
