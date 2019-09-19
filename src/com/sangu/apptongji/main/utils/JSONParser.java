package com.sangu.apptongji.main.utils;

import android.text.TextUtils;

import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.main.alluser.entity.BaoZInfo;
import com.sangu.apptongji.main.alluser.entity.DianziDanju;
import com.sangu.apptongji.main.alluser.entity.FWFKInfo;
import com.sangu.apptongji.main.alluser.entity.Friend;
import com.sangu.apptongji.main.alluser.entity.GroupInfo;
import com.sangu.apptongji.main.alluser.entity.LiuLanDetail;
import com.sangu.apptongji.main.alluser.entity.MerDetail;
import com.sangu.apptongji.main.alluser.entity.PushDetail;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.entity.ZhuFaDetail;
import com.sangu.apptongji.main.alluser.order.entity.DIDAList;
import com.sangu.apptongji.main.alluser.order.entity.OrderDetail;
import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;
import com.sangu.apptongji.main.qiye.entity.BaobiaoInfo;
import com.sangu.apptongji.main.qiye.entity.HeTongInfo;
import com.sangu.apptongji.main.qiye.entity.KaoqinInfo;
import com.sangu.apptongji.main.qiye.entity.MemberInfo;
import com.sangu.apptongji.main.qiye.entity.OffSendOrderList;
import com.sangu.apptongji.main.qiye.entity.PaiDanInfo;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.entity.QingjiaInfo;
import com.sangu.apptongji.main.qiye.entity.QiyeKaoQinDetailInfo;
import com.sangu.apptongji.main.qiye.entity.QiyeKaoQinInfo;
import com.sangu.apptongji.update.ApkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/8/29.
 */

public class JSONParser {

    public static List<GroupInfo> parseGroupList(JSONArray array,String groupSize) throws JSONException{
        List<GroupInfo> groupInfos = new ArrayList<>();
        if (array!=null&&!"".equals(array)){
            for (int i =0; i <array.length();i++){
                GroupInfo groupInfo = new GroupInfo();
                JSONObject object = array.getJSONObject(i);
                String groupId = object.isNull("groupId")?"":object.getString("groupId");//再次跟uId一样详情Id
                String groupName = object.isNull("groupName")?"":object.getString("groupName");
                String groupImage = object.isNull("groupImage")?"":object.getString("groupImage");
                String groupMember = object.isNull("groupMember")?"":object.getString("groupMember");
                String createTime = object.isNull("createTime")?"":object.getString("createTime");
                String updateName = object.isNull("updateName")?"":object.getString("updateName");
                String leader = object.isNull("leader")?"":object.getString("leader");
                if (groupImage!=null&&!"".equals(groupImage)){
                    int size = groupImage.split("\\|").length;
                    if (size>5){
                        groupInfo.setType(4);
                    }else if (size>2){
                        groupInfo.setType(size - 2);
                    }else {
                        groupInfo.setType(0);
                    }
                }else {
                    groupInfo.setType(0);
                }
                groupInfo.setGroupSize(groupSize);
                groupInfo.setGroupId(groupId);
                groupInfo.setGroupName(groupName);
                groupInfo.setGroupImage(groupImage);
                groupInfo.setGroupMember(groupMember);
                groupInfo.setCreateTime(createTime);
                groupInfo.setUpdateName(updateName);
                groupInfo.setLeader(leader);
                groupInfos.add(groupInfo);
            }
        }
        return groupInfos;
    }

    public static ApkInfo parseApkInfo(JSONObject object)throws  JSONException{
        ApkInfo info = new ApkInfo();
        info.setApkurl(object.isNull("apkurl")?"Zhengshier.apk":object.getString("apkurl"));
        info.setVersionnum(object.getString("versionnum"));
        info.setUpdateflg(object.getString("updateflg"));
        info.setVersiondesc(object.isNull("versiondesc")?"检测到有最新的版本，更新版本体验新功能!":object.getString("versiondesc"));
        return info;
    }

    public static List<LiuLanDetail> parseLiulanList(JSONArray array) throws JSONException{
        List<LiuLanDetail> merDetails = new ArrayList<>();
        if (array!=null&&!"".equals(array)){
            for (int i =0; i <array.length();i++){
                JSONObject userInfo = array.getJSONObject(i).optJSONObject("userInfo");
                LiuLanDetail merDetail = new LiuLanDetail();
                JSONObject object = array.getJSONObject(i);
                String v_id = object.isNull("v_id")?"":object.getString("v_id");//再次跟uId一样详情Id
                String timestamp = object.isNull("timestamp")?"":object.getString("timestamp");
                String uLoginId = object.isNull("uLoginId")?"":object.getString("uLoginId");
                if (userInfo!=null){
                    JSONArray userProfessions = userInfo.getJSONArray("userProfessions");
                    String friendsNumber = userInfo.isNull("friendsNumber")?"":userInfo.getString("friendsNumber");
                    String shareRed = userInfo.isNull("shareRed")?"":userInfo.getString("shareRed");
                    String uCompany = userInfo.isNull("uCompany")?"":userInfo.getString("uCompany");
                    String menberNum = userInfo.isNull("menberNum")?"":userInfo.getString("menberNum");
                    String name = userInfo.isNull("uName")?"":userInfo.getString("uName");
                    String sign = userInfo.isNull("uSignaTure")?"":userInfo.getString("uSignaTure");
                    String uAge = userInfo.isNull("uAge")?"":userInfo.getString("uAge");
                    String head = userInfo.isNull("uImage")?"":userInfo.getString("uImage");
                    String sex = userInfo.isNull("uSex")?"":userInfo.getString("uSex");
                    String resv1 = userInfo.isNull("resv1")?"":userInfo.getString("resv1");
                    String resv2 = userInfo.isNull("resv2")?"":userInfo.getString("resv2");
                    String resv5 = userInfo.isNull("resv5")?"":userInfo.getString("resv5");
                    String resv6 = userInfo.isNull("resv6")?"":userInfo.getString("resv6");
                    String uNation = userInfo.isNull("uNation")?"":userInfo.getString("uNation");
                    merDetail.setFriendsNumber(friendsNumber);
                    merDetail.setuCompany(uCompany);
                    merDetail.setMemberNum(menberNum);
                    merDetail.setShareRed(shareRed);
                    merDetail.setSign(sign);
                    merDetail.setuNation(uNation);
                    merDetail.setResv5(resv5);
                    merDetail.setResv6(resv6);
                    merDetail.setNianling(uAge);
                    merDetail.setName(TextUtils.isEmpty(name) ? "" : name);
                    merDetail.setHead(TextUtils.isEmpty(head) ? "" : head);
                    merDetail.setSex(TextUtils.isEmpty(sex) ? "" : sex);
                    merDetail.setResv1(TextUtils.isEmpty(resv1) ? "" : resv1);
                    merDetail.setResv2(TextUtils.isEmpty(resv2) ? "" : resv2);
                    for (int j=0;j<userProfessions.length();j++){
                        JSONObject proObj = userProfessions.getJSONObject(j);
                        String upTypeId = proObj.isNull("upTypeId")?"":proObj.getString("upTypeId");
                        String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                        String margin = proObj.isNull("margin")?"":proObj.getString("margin");
                        String image = proObj.isNull("image")?"":proObj.getString("image");
                        if (upTypeId.equals("1")) {
                            merDetail.setUpName1(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage1(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin1(TextUtils.isEmpty(margin) ? "" : margin);
                        } else if (upTypeId.equals("2")) {
                            merDetail.setUpName2(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage2(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin2(TextUtils.isEmpty(margin) ? "" : margin);
                        } else if (upTypeId.equals("3")) {
                            merDetail.setUpName3(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage3(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin3(TextUtils.isEmpty(margin) ? "" : margin);
                        } else if (upTypeId.equals("4")) {
                            merDetail.setUpName4(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage4(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin4(TextUtils.isEmpty(margin) ? "" : margin);
                        }
                    }
                }
                merDetail.setV_id(v_id);
                merDetail.setuLoginId(uLoginId);
                merDetail.setTimestamp(timestamp);
                merDetails.add(merDetail);
            }
        }
        return merDetails;
    }

    public static List<ZhuFaDetail> parseZhuFaList(JSONArray array) throws JSONException{
        List<ZhuFaDetail> merDetails = new ArrayList<>();
        if (array!=null&&!"".equals(array)){
            for (int i =0; i <array.length();i++){
                JSONObject userInfo = array.getJSONObject(i).optJSONObject("userInfo");
                ZhuFaDetail merDetail = new ZhuFaDetail();
                JSONObject object = array.getJSONObject(i);
                String f_id = object.isNull("f_id")?"":object.getString("f_id");//再次跟uId一样详情Id
                String timestamp = object.isNull("timestamp")?"":object.getString("timestamp");
                String uLoginId = object.isNull("uLoginId")?"":object.getString("uLoginId");
                if (userInfo!=null){
                    JSONArray userProfessions = userInfo.getJSONArray("userProfessions");
                    String friendsNumber = userInfo.isNull("friendsNumber")?"":userInfo.getString("friendsNumber");
                    String shareRed = userInfo.isNull("shareRed")?"":userInfo.getString("shareRed");
                    String uCompany = userInfo.isNull("uCompany")?"":userInfo.getString("uCompany");
                    String menberNum = userInfo.isNull("menberNum")?"":userInfo.getString("menberNum");
                    String name = userInfo.isNull("uName")?"":userInfo.getString("uName");
                    String sign = userInfo.isNull("uSignaTure")?"":userInfo.getString("uSignaTure");
                    String uAge = userInfo.isNull("uAge")?"":userInfo.getString("uAge");
                    String head = userInfo.isNull("uImage")?"":userInfo.getString("uImage");
                    String sex = userInfo.isNull("uSex")?"":userInfo.getString("uSex");
                    String uNation = userInfo.isNull("uNation")?"":userInfo.getString("uNation");
                    String resv1 = userInfo.isNull("resv1")?"":userInfo.getString("resv1");
                    String resv2 = userInfo.isNull("resv2")?"":userInfo.getString("resv2");
                    String resv5 = userInfo.isNull("resv5")?"":userInfo.getString("resv5");
                    String resv6 = userInfo.isNull("resv6")?"":userInfo.getString("resv6");
                    merDetail.setFriendsNumber(friendsNumber);
                    merDetail.setuCompany(uCompany);
                    merDetail.setMemberNum(menberNum);
                    merDetail.setShareRed(shareRed);
                    merDetail.setSign(sign);
                    merDetail.setNianling(uAge);
                    merDetail.setuNation(uNation);
                    merDetail.setResv5(resv5);
                    merDetail.setResv6(resv6);
                    merDetail.setName(TextUtils.isEmpty(name) ? "" : name);
                    merDetail.setHead(TextUtils.isEmpty(head) ? "" : head);
                    merDetail.setSex(TextUtils.isEmpty(sex) ? "" : sex);
                    merDetail.setResv1(TextUtils.isEmpty(resv1) ? "" : resv1);
                    merDetail.setResv2(TextUtils.isEmpty(resv2) ? "" : resv2);
                    for (int j=0;j<userProfessions.length();j++){
                        JSONObject proObj = userProfessions.getJSONObject(j);
                        String upTypeId = proObj.isNull("upTypeId")?"":proObj.getString("upTypeId");
                        String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                        String margin = proObj.isNull("margin")?"":proObj.getString("margin");
                        String image = proObj.isNull("image")?"":proObj.getString("image");

                        if (upTypeId.equals("1")) {
                            merDetail.setUpName1(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage1(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin1(TextUtils.isEmpty(margin) ? "" : margin);
                        } else if (upTypeId.equals("2")) {
                            merDetail.setUpName2(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage2(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin2(TextUtils.isEmpty(margin) ? "" : margin);
                        } else if (upTypeId.equals("3")) {
                            merDetail.setUpName3(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage3(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin3(TextUtils.isEmpty(margin) ? "" : margin);
                        } else if (upTypeId.equals("4")) {
                            merDetail.setUpName4(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage4(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin4(TextUtils.isEmpty(margin) ? "" : margin);
                        }
                    }
                }
                merDetail.setF_id(f_id);
                merDetail.setuLoginId(uLoginId);
                merDetail.setTimestamp(timestamp);
                merDetails.add(merDetail);
            }
        }
        return merDetails;
    }

    public static List<MerDetail> parseMerDetailList(JSONArray array) throws JSONException{
        List<MerDetail> merDetails = new ArrayList<>();
        if (array!=null&&!"".equals(array)){
            for (int i =0; i <array.length();i++){
                JSONObject userInfo = array.getJSONObject(i).optJSONObject("userInfo");
                JSONObject companyInfo = array.getJSONObject(i).optJSONObject("companyInfo");
                MerDetail merDetail = new MerDetail();
                JSONObject object = array.getJSONObject(i);
                String mer_id = object.isNull("mer_id")?"":object.getString("mer_id");
                String order_id = object.isNull("order_id")?"":object.getString("order_id");
                String timestamp = object.isNull("timestamp")?"":object.getString("timestamp");
                String transaction_amount = object.isNull("transaction_amount")?"":object.getString("transaction_amount");
                String transaction_type = object.isNull("transaction_type")?"":object.getString("transaction_type");
                String pay_type = object.isNull("pay_type")?"":object.getString("pay_type");
                String user_id = object.isNull("user_id")?"":object.getString("user_id");
                String acc_type = object.isNull("acc_type")?"":object.getString("acc_type");
                if (userInfo!=null){
                    JSONArray userProfessions = userInfo.getJSONArray("userProfessions");
                    String shareRed = userInfo.isNull("shareRed")?"":userInfo.getString("shareRed");
                    String friendsNumber = userInfo.isNull("friendsNumber")?"":userInfo.getString("friendsNumber");
                    String uCompany = userInfo.isNull("uCompany")?"":userInfo.getString("uCompany");
                    String menberNum = userInfo.isNull("menberNum")?"":userInfo.getString("menberNum");
                    String name = userInfo.isNull("uName")?"":userInfo.getString("uName");
                    String sign = userInfo.isNull("uSignaTure")?"":userInfo.getString("uSignaTure");
                    String uAge = userInfo.isNull("uAge")?"":userInfo.getString("uAge");
                    String head = userInfo.isNull("uImage")?"":userInfo.getString("uImage");
                    String sex = userInfo.isNull("uSex")?"":userInfo.getString("uSex");
                    String resv1 = userInfo.isNull("resv1")?"":userInfo.getString("resv1");
                    String resv2 = userInfo.isNull("resv2")?"":userInfo.getString("resv2");
                    merDetail.setFriendsNumber(friendsNumber);
                    merDetail.setShareRed(shareRed);
                    merDetail.setMemberNum("0");
                    merDetail.setQiYeOrGeRen("00");
                    merDetail.setSign(sign);
                    merDetail.setNianling(uAge);
                    merDetail.setName(TextUtils.isEmpty(name) ? "" : name);
                    merDetail.setHead(TextUtils.isEmpty(head) ? "" : head);
                    merDetail.setSex(TextUtils.isEmpty(sex) ? "" : sex);
                    merDetail.setResv1(TextUtils.isEmpty(resv1) ? "" : resv1);
                    merDetail.setResv2(TextUtils.isEmpty(resv2) ? "" : resv2);
                    for (int j=0;j<userProfessions.length();j++){
                        JSONObject proObj = userProfessions.getJSONObject(j);
                        String upTypeId = proObj.isNull("upTypeId")?"":proObj.getString("upTypeId");
                        String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                        String margin = proObj.isNull("margin")?"":proObj.getString("margin");
                        String image = proObj.isNull("image")?"":proObj.getString("image");

                        if (upTypeId.equals("1")) {
                            merDetail.setUpName1(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage1(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin1(TextUtils.isEmpty(margin) ? "" : margin);
                        } else if (upTypeId.equals("2")) {
                            merDetail.setUpName2(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage2(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin2(TextUtils.isEmpty(margin) ? "" : margin);
                        } else if (upTypeId.equals("3")) {
                            merDetail.setUpName3(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage3(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin3(TextUtils.isEmpty(margin) ? "" : margin);
                        } else if (upTypeId.equals("4")) {
                            merDetail.setUpName4(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage4(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin4(TextUtils.isEmpty(margin) ? "" : margin);
                        }
                    }
                }else if (companyInfo!=null){
                    JSONArray userProfessions = companyInfo.getJSONArray("userProfessionList");
                    String name = companyInfo.isNull("companyName")?"":companyInfo.getString("companyName");
                    String sign = companyInfo.isNull("comSignature")?"":companyInfo.getString("comSignature");
                    String head = companyInfo.isNull("comImage")?"":companyInfo.getString("comImage");
                    String sex = "";
                    String yincang = companyInfo.isNull("resv1")?"":companyInfo.getString("resv1");
                    String resv1 = companyInfo.isNull("comLongitude")?"":companyInfo.getString("comLongitude");
                    String resv2 = companyInfo.isNull("comLatitude")?"":companyInfo.getString("comLatitude");
                    String memberNum = companyInfo.isNull("memberNum")?"":companyInfo.getString("memberNum");
                    merDetail.setYincang(yincang);
                    merDetail.setMemberNum(memberNum);
                    merDetail.setQiYeOrGeRen("01");
                    merDetail.setSign(sign);
                    merDetail.setName(TextUtils.isEmpty(name) ? "" : name);
                    merDetail.setHead(TextUtils.isEmpty(head) ? "" : head);
                    merDetail.setSex(TextUtils.isEmpty(sex) ? "" : sex);
                    merDetail.setResv1(TextUtils.isEmpty(resv1) ? "" : resv1);
                    merDetail.setResv2(TextUtils.isEmpty(resv2) ? "" : resv2);
                    for (int j=0;j<userProfessions.length();j++){
                        JSONObject proObj = userProfessions.getJSONObject(j);
                        String upTypeId = proObj.isNull("upTypeId")?"":proObj.getString("upTypeId");
                        String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                        String margin = proObj.isNull("margin")?"":proObj.getString("margin");
                        String image = proObj.isNull("image")?"":proObj.getString("image");

                        if (upTypeId.equals("1")) {
                            merDetail.setUpName1(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage1(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin1(TextUtils.isEmpty(margin) ? "" : margin);
                        } else if (upTypeId.equals("2")) {
                            merDetail.setUpName2(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage2(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin2(TextUtils.isEmpty(margin) ? "" : margin);
                        } else if (upTypeId.equals("3")) {
                            merDetail.setUpName3(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage3(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin3(TextUtils.isEmpty(margin) ? "" : margin);
                        } else if (upTypeId.equals("4")) {
                            merDetail.setUpName4(TextUtils.isEmpty(upName) ? "" : upName);
                            merDetail.setZyImage4(TextUtils.isEmpty(image) ? "" : image);
                            merDetail.setMargin4(TextUtils.isEmpty(margin) ? "" : margin);
                        }
                    }
                }
                merDetail.setMer_id(mer_id);
                merDetail.setOrder_id(order_id);
                merDetail.setTimestamp(timestamp);
                merDetail.setTransaction_amount(transaction_amount);
                merDetail.setTransaction_type(transaction_type);
                merDetail.setPay_type(pay_type);
                merDetail.setUser_id(user_id);
                merDetail.setAcc_type(acc_type);
                merDetails.add(merDetail);
            }
        }
        return merDetails;
    }

    public static List<HeTongInfo> parseHetongList(JSONArray array ) throws JSONException{
        List<HeTongInfo> heTongInfos = new ArrayList<>();
        if (array!=null||!"".equals(array)) {
            for (int i = 0; i < array.length();i++) {
                HeTongInfo heTongInfo = new HeTongInfo();
                JSONObject object1 = array.getJSONObject(i);
                String id = object1.getString("userId");
                String companyId = object1.isNull("companyId")?"":object1.getString("companyId");
                String comSignature = object1.isNull("comSignature")?"":object1.getString("comSignature");
                String agreement1 = object1.isNull("agreement1")?"":object1.getString("agreement1");
                String agreement2 = object1.isNull("agreement2")?"":object1.getString("agreement2");
                String createTime = object1.isNull("createTime")?"":object1.getString("createTime");
                String userSignature = object1.isNull("userSignature")?"":object1.getString("userSignature");
                String signatureTime = object1.isNull("signatureTime")?"":object1.getString("signatureTime");
                JSONObject userInfo = object1.getJSONObject("userInfo");
                String uCompany = userInfo.isNull("uCompany")?"":userInfo.getString("uCompany");
                String uName = userInfo.isNull("uName")?"":userInfo.getString("uName");
                String resv1 = object1.isNull("resv1")?"":object1.getString("resv1");
                heTongInfo.setAgreement1(agreement1);
                heTongInfo.setAgreement2(agreement2);
                heTongInfo.setUserId(id);
                heTongInfo.setCompanyId(companyId);
                heTongInfo.setComSignature(comSignature);
                heTongInfo.setUserSignature(userSignature);
                heTongInfo.setuCompany(uCompany);
                heTongInfo.setuName(uName);
                heTongInfo.setCreateTime(createTime);
                heTongInfo.setSignatureTime(signatureTime);
                heTongInfo.setResv1(resv1);
                if (!"".equals(userSignature)&&!"".equals(comSignature)) {
                    heTongInfos.add(heTongInfo);
                }
            }
        }
        return heTongInfos;
    }

    public static  List<PushDetail> parsePushList(JSONArray array) throws JSONException{
        List<PushDetail> pushList = new ArrayList<>();
        for (int i=0;i<array.length();i++){
            JSONObject object = array.getJSONObject(i);
            PushDetail pushDetail = new PushDetail();
            pushDetail.setU_id(object.isNull("u_id")?"":object.getString("u_id"));
            pushDetail.setTimestamp(object.isNull("timestamp")?"":object.getString("timestamp"));
            pushDetail.setType(object.isNull("type")?"":object.getString("type"));
            pushDetail.setUserId(object.isNull("userId")?"":object.getString("userId"));
            pushDetail.setCompanyId(object.isNull("companyId")?"":object.getString("companyId"));
            pushDetail.setCompanyName(object.isNull("companyName")?"":object.getString("companyName"));
            pushDetail.setCompanyAdress(object.isNull("companyAdress")?"":object.getString("companyAdress"));
            pushDetail.setDynamicSeq(object.isNull("dynamicSeq")?"":object.getString("dynamicSeq"));
            pushDetail.setCreateTime(object.isNull("createTime")?"":object.getString("createTime"));
            pushDetail.setFristId(object.isNull("fristId")?"":object.getString("fristId"));
            pushDetail.setdType(object.isNull("dType")?"":object.getString("dType"));
            pushList.add(pushDetail);
        }
        return pushList;
    }

    public static  List<QingjiaInfo> parseQingjiaList(JSONArray array) throws JSONException{
        List<QingjiaInfo> qingjiaList = new ArrayList<>();
        for (int i=0;i<array.length();i++){
            JSONObject object = array.getJSONObject(i);
            QingjiaInfo qingjiaInfo = new QingjiaInfo();
            qingjiaInfo.setRemark(object.isNull("remark")?"":object.getString("remark"));
            qingjiaInfo.setResv1(object.isNull("resv1")?"":object.getString("resv1"));
            qingjiaInfo.setResv2(object.isNull("resv2")?"":object.getString("resv2"));
            qingjiaInfo.setResv3(object.isNull("resv3")?"":object.getString("resv3"));
            qingjiaInfo.setCompanyId(object.isNull("companyId")?"":object.getString("companyId"));
            qingjiaInfo.setCreatTime(object.isNull("creatTime")?"":object.getString("creatTime"));
            qingjiaInfo.setUserId(object.isNull("userId")?"":object.getString("userId"));
            qingjiaInfo.setLeaveTimeStart(object.isNull("leaveTimeStart")?"":object.getString("leaveTimeStart"));
            qingjiaInfo.setLeaveTimeEnd(object.isNull("leaveTimeEnd")?"":object.getString("leaveTimeEnd"));
            qingjiaInfo.setSignPic1(object.isNull("signPic1")?"":object.getString("signPic1"));
            qingjiaInfo.setSignPic2(object.isNull("signPic2")?"":object.getString("signPic2"));
            qingjiaInfo.setLeaveReason(object.isNull("leaveReason")?"":object.getString("leaveReason"));
            qingjiaList.add(qingjiaInfo);
        }
        return qingjiaList;
    }
    public static  List<BaobiaoInfo> parseBaobiaoList(JSONArray array,String tiaojian1,String tiaojian2) throws JSONException{
        List<BaobiaoInfo> baobiaoList = new ArrayList<>();
        for (int i=0;i<array.length();i++){
            JSONObject object = array.getJSONObject(i);
            BaobiaoInfo baobiaoInfo = new BaobiaoInfo();
            String create = object.isNull("createTime")?"":object.getString("createTime");
            if (create.length()>0){
                create=create.substring(0,8);
            }
            String id = object.getString("userId");
            baobiaoInfo.setResv1(object.isNull("resv1")?"":object.getString("resv1"));
            baobiaoInfo.setResv2(object.isNull("resv2")?"":object.getString("resv2"));
            baobiaoInfo.setResv3(object.isNull("resv3")?"":object.getString("resv3"));
            baobiaoInfo.setCompanyId(object.isNull("companyId")?"":object.getString("companyId"));
            baobiaoInfo.setCreatTime(object.isNull("createTime")?"":object.getString("createTime"));
            baobiaoInfo.setUserId(object.isNull("userId")?"":object.getString("userId"));
            baobiaoInfo.setPlanTitle(object.isNull("planTitle")?"":object.optString("planTitle"));
            baobiaoInfo.setPlanContent(object.isNull("planContent")?"":object.optString("planContent"));
            baobiaoInfo.setPlanAdvise(object.isNull("planAdvise")?"":object.optString("planAdvise"));
            baobiaoInfo.setSignature1(object.isNull("signature1")?"":object.optString("signature1"));
            baobiaoInfo.setSignature2(object.isNull("signature2")?"":object.optString("signature2"));
            baobiaoInfo.setSignatureTime1(object.isNull("signatureTime1")?"":object.optString("signatureTime1"));
            baobiaoInfo.setSignatureTime2(object.isNull("signatureTime2")?"":object.optString("signatureTime2"));
            if ("".equals(tiaojian1)&&"".equals(tiaojian2)) {
                baobiaoList.add(baobiaoInfo);
            }else if (tiaojian1.equals(create)&&"".equals(tiaojian2)){
                baobiaoList.add(baobiaoInfo);
            }else if ("01".equals(tiaojian2)){
                String userId = DemoHelper.getInstance().getCurrentUsernName();
                if ("".equals(tiaojian1)) {
                    if (userId.equals(id)){
                        baobiaoList.add(baobiaoInfo);
                    }
                }else if (tiaojian1.equals(create)){
                    if (userId.equals(id)){
                        baobiaoList.add(baobiaoInfo);
                    }
                }
            }
        }
        return baobiaoList;
    }

    public static List<MemberInfo> parseMemberList(JSONArray array) throws JSONException{
        List<MemberInfo> memberInfoList = new ArrayList<>();
        for (int i=0;i<array.length();i++){
            JSONObject object = array.getJSONObject(i);
            MemberInfo user = new MemberInfo();
            JSONArray userProfessions = object.getJSONArray("userProfessions");
            JSONObject dataCountObj = object.optJSONObject("dataCount");
            JSONObject signInfoObj=null;
            String signInfo = object.isNull("signStateInfo")?"":object.getString("signStateInfo");
            if (!signInfo.equals("")) {
                signInfoObj = object.getJSONObject("signStateInfo");
            }

            user.setComClockInfo(object.isNull("comClockInfo")?"":object.getString("comClockInfo"));

            if (dataCountObj!=null&&!"".equals(dataCountObj)){
                user.setDataLateTimes(dataCountObj.isNull("lateTimes")?"0":dataCountObj.getString("lateTimes"));
                user.setDataLeaveTimes(dataCountObj.isNull("leaveTimes")?"0":dataCountObj.getString("leaveTimes"));
                user.setDataOrderTimes(dataCountObj.isNull("orderTimes")?"0":dataCountObj.getString("orderTimes"));
                user.setDataTotalTransAmount(dataCountObj.isNull("totalTransAmount")?"0":dataCountObj.getString("totalTransAmount"));
                user.setDataDayTransAmount(dataCountObj.isNull("dayTransAmount")?"0":dataCountObj.getString("dayTransAmount"));
                user.setDataDayresv1(dataCountObj.isNull("resv1")?"0":dataCountObj.getString("resv1"));
            }else {
                user.setDataLateTimes("0");
                user.setDataLeaveTimes("0");
                user.setDataOrderTimes("0");
                user.setDataTotalTransAmount("0");
                user.setDataDayTransAmount("0");
            }
            if (userProfessions != null) {
                for (int j = 0; j < userProfessions.length(); j++) {
                    JSONObject proObj = userProfessions.getJSONObject(j);
                    String upTypeId = proObj.isNull("upTypeId")?"":proObj.getString("upTypeId");
                    String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                    String margin = proObj.isNull("margin")?"":proObj.getString("margin");
                    String image = proObj.isNull("image")?"":proObj.getString("image");
                    String upId = proObj.isNull("upId")?"":proObj.getString("upId");
                    String upDescribe = proObj.isNull("upDescribe")?"":proObj.getString("upDescribe");
                    String creatTime = proObj.isNull("creatTime")?"":proObj.getString("creatTime");
                    String remark = proObj.isNull("remark")?"0,0":proObj.getString("remark");
                    String zyresv1 = proObj.isNull("resv1")?"":proObj.getString("resv1");
                    String zyresv2 = proObj.isNull("resv2")?"":proObj.getString("resv2");
                    String zyresv3 = proObj.isNull("resv3")?"":proObj.getString("resv3");
                    if (upTypeId.equals("1")) {
                        user.setUpName1(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setZy1Image(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargin1(TextUtils.isEmpty(margin) ? "" : margin);
                        user.setUpId1(TextUtils.isEmpty(upId) ? "" : upId);
                        user.setUpDescribe1(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                        user.setCreatTime1(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                        user.setRemark1(TextUtils.isEmpty(remark) ? "" : remark);
                        user.setZy1Resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                        user.setZy1Resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                        user.setZy1Resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                    } else if (upTypeId.equals("2")) {
                        user.setUpName2(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setZy2Image(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargin2(TextUtils.isEmpty(margin) ? "" : margin);
                        user.setUpId2(TextUtils.isEmpty(upId) ? "" : upId);
                        user.setUpDescribe2(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                        user.setCreatTime2(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                        user.setRemark2(TextUtils.isEmpty(remark) ? "" : remark);
                        user.setZy2Resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                        user.setZy2Resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                        user.setZy2Resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                    } else if (upTypeId.equals("3")) {
                        user.setUpName3(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setZy3Image(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargin3(TextUtils.isEmpty(margin) ? "" : margin);
                        user.setUpId3(TextUtils.isEmpty(upId) ? "" : upId);
                        user.setUpDescribe3(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                        user.setCreatTime3(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                        user.setRemark3(TextUtils.isEmpty(remark) ? "" : remark);
                        user.setZy3Resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                        user.setZy3Resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                        user.setZy3Resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                    } else if (upTypeId.equals("4")) {
                        user.setUpName4(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setZy4Image(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargin4(TextUtils.isEmpty(margin) ? "" : margin);
                        user.setUpId4(TextUtils.isEmpty(upId) ? "" : upId);
                        user.setUpDescribe4(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                        user.setCreatTime4(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                        user.setRemark4(TextUtils.isEmpty(remark) ? "" : remark);
                        user.setZy4Resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                        user.setZy4Resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                        user.setZy4Resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                    }
                }
            }
            if (signInfoObj!=null){
                user.setSignInfo_userId(signInfoObj.isNull("userId")?"":signInfoObj.getString("userId"));
                user.setSignInfo_signState(signInfoObj.isNull("signState")?"":signInfoObj.getString("signState"));
                user.setSignInfo_signTime(signInfoObj.isNull("signTime")?"":signInfoObj.getString("signTime"));
                user.setSignInfo_latitude(signInfoObj.isNull("latitude")?"":signInfoObj.getString("latitude"));
                user.setSignInfo_longitude(signInfoObj.isNull("longitude")?"":signInfoObj.getString("longitude"));
                user.setSignInfo_remark(signInfoObj.isNull("remark")?"":signInfoObj.getString("remark"));
            }else {
                user.setSignInfo_userId("");
                user.setSignInfo_signState("");
                user.setSignInfo_signTime("");
                user.setSignInfo_latitude("");
                user.setSignInfo_longitude("");
                user.setSignInfo_remark("");
            }

            user.setuName(object.isNull("uName")?"":object.getString("uName"));
            user.setMemberNumber(object.isNull("menberNum")?"0":object.getString("menberNum"));
            user.setLocationState(object.isNull("locationState")?"01":object.getString("locationState"));
            user.setShareRed(object.isNull("shareRed")?"":object.getString("shareRed"));
            user.setFriendsNumber(object.isNull("friendsNumber")?"":object.getString("friendsNumber"));
            user.setuImage(object.isNull("uImage")?"":object.getString("uImage"));
            user.setuCity(object.isNull("uCity")?"":object.getString("uCity"));
            user.setuCompany(object.isNull("uCompany")?"":object.getString("uCompany"));
            user.setuCompanyAddress(object.isNull("uCompanyAddress")?"":object.getString("uCompanyAddress"));
            user.setuLoginId(object.isNull("uLoginId")?"":object.getString("uLoginId"));
            user.setuSex(object.isNull("uSex")?"":object.getString("uSex"));
            user.setuAge(object.isNull("uAge")?"":object.getString("uAge"));
            user.setResv1(object.isNull("resv1")?"":object.getString("resv1"));
            user.setResv2(object.isNull("resv2")?"":object.getString("resv2"));
            user.setResv3(object.isNull("resv3")?"":object.getString("resv3"));
            user.setResv5(object.isNull("resv5")?"":object.getString("resv5"));
            user.setResv6(object.isNull("resv6")?"":object.getString("resv6"));
            user.setuSignaTure(object.isNull("uSignaTure")?"":object.getString("uSignaTure"));
            memberInfoList.add(user);
        }
        return memberInfoList;
    }

    public static List<QiYeInfo> parseQiyeList(JSONArray array) throws JSONException{
        List<QiYeInfo> qiYeInfoList = new ArrayList<QiYeInfo>();
        String remarkp = "";
        for (int i=0;i<array.length();i++){
            JSONObject object = array.getJSONObject(i);
            remarkp = object.isNull("remark")?"4":object.getString("remark");
            QiYeInfo user = new QiYeInfo();
            user.setUpName1("");
            user.setZyImage1("");
            user.setMargin1("");
            user.setUpId1("");
            user.setUpDescribe1("");
            user.setCreateTime1("");
            user.setRemark1("");
            user.setZy1resv1("");
            user.setZy1resv2("");
            user.setZy1resv3("");
            user.setUpName2("");
            user.setZyImage2("");
            user.setMargin2("");
            user.setUpId2("");
            user.setUpDescribe2("");
            user.setCreateTime2("");
            user.setRemark2("");
            user.setZy2resv1("");
            user.setZy2resv2("");
            user.setZy2resv3("");
            user.setUpName3("");
            user.setZyImage3("");
            user.setMargin3("");
            user.setUpId3("");
            user.setUpDescribe3("");
            user.setCreateTime3("");
            user.setRemark3("");
            user.setZy3resv1("");
            user.setZy3resv2("");
            user.setZy3resv3("");
            user.setUpName4("");
            user.setZyImage4("");
            user.setMargin4("");
            user.setUpId4("");
            user.setUpDescribe4("");
            user.setCreateTime4("");
            user.setRemark4("");
            user.setZy4resv1("");
            user.setZy4resv2("");
            user.setZy4resv3("");
            JSONArray userProfessions = object.optJSONArray("userProfessionList");
            if (userProfessions != null) {
                for (int j = 0; j < userProfessions.length(); j++) {
                    JSONObject proObj = userProfessions.getJSONObject(j);
                    String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                    String margin = proObj.isNull("margin")?"":proObj.getString("margin");
                    String image = proObj.isNull("image")?"":proObj.getString("image");
                    String upId = proObj.isNull("upId")?"":proObj.getString("upId");
                    String upDescribe = proObj.isNull("upDescribe")?"":proObj.getString("upDescribe");
                    String creatTime = proObj.isNull("creatTime")?"":proObj.getString("creatTime");
                    String remark = proObj.isNull("remark")?"0,0":proObj.getString("remark");
                    String zyresv1 = proObj.isNull("resv1")?"":proObj.getString("resv1");
                    String zyresv2 = proObj.isNull("resv2")?"":proObj.getString("resv2");
                    String zyresv3 = proObj.isNull("resv3")?"":proObj.getString("resv3");
                    String upTypeId = proObj.isNull("upTypeId")?"":proObj.getString("upTypeId");
                    if (upTypeId.equals("1")) {
                        user.setUpName1(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setZyImage1(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargin1(TextUtils.isEmpty(margin) ? "" : margin);
                        user.setUpId1(TextUtils.isEmpty(upId) ? "" : upId);
                        user.setUpDescribe1(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                        user.setCreateTime1(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                        user.setRemark1(TextUtils.isEmpty(remark) ? "" : remark);
                        user.setZy1resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                        user.setZy1resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                        user.setZy1resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                    } else if (upTypeId.equals("2")) {
                        user.setUpName2(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setZyImage2(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargin2(TextUtils.isEmpty(margin) ? "" : margin);
                        user.setUpId2(TextUtils.isEmpty(upId) ? "" : upId);
                        user.setUpDescribe2(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                        user.setCreateTime2(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                        user.setRemark2(TextUtils.isEmpty(remark) ? "" : remark);
                        user.setZy2resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                        user.setZy2resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                        user.setZy2resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                    } else if (upTypeId.equals("3")) {
                        user.setUpName3(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setZyImage3(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargin3(TextUtils.isEmpty(margin) ? "" : margin);
                        user.setUpId3(TextUtils.isEmpty(upId) ? "" : upId);
                        user.setUpDescribe3(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                        user.setCreateTime3(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                        user.setRemark3(TextUtils.isEmpty(remark) ? "" : remark);
                        user.setZy3resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                        user.setZy3resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                        user.setZy3resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                    } else if (upTypeId.equals("4")) {
                        user.setUpName4(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setZyImage4(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargin4(TextUtils.isEmpty(margin) ? "" : margin);
                        user.setUpId4(TextUtils.isEmpty(upId) ? "" : upId);
                        user.setUpDescribe4(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                        user.setCreateTime4(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                        user.setRemark4(TextUtils.isEmpty(remark) ? "" : remark);
                        user.setZy4resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                        user.setZy4resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                        user.setZy4resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                    }
                }
            }
            user.setCompanyId(object.isNull("companyId")?"":object.getString("companyId"));
            user.setCompanyName(object.isNull("companyName")?"":object.getString("companyName"));
            user.setCompanyShortName(object.isNull("companyShortName")?"":object.getString("companyShortName"));
            user.setComImage(object.isNull("comImage")?"":object.getString("comImage"));
            user.setComSignature(object.isNull("comSignature")?"":object.getString("comSignature"));
            user.setComType(object.isNull("comType")?"":object.getString("comType"));
            user.setComTel(object.isNull("comTel")?"":object.getString("comTel"));
            user.setComEmail(object.isNull("comEmail")?"":object.getString("comEmail"));
            user.setComAddress(object.isNull("comAddress")?"":object.getString("comAddress"));
            user.setComPushState(object.isNull("comPushState")?"":object.getString("comPushState"));
            user.setComLocationState(object.isNull("comLocationState")?"":object.getString("comLocationState"));
            user.setComPutinAmt(object.isNull("comPutinAmt")?"":object.getString("comPutinAmt"));
            user.setComSubsidyAmt(object.isNull("comSubsidyAmt")?"":object.getString("comSubsidyAmt"));
            user.setComForwardAmt(object.isNull("comForwardAmt")?"":object.getString("comForwardAmt"));
            user.setComLongitude(object.isNull("comLongitude")?"":object.getString("comLongitude"));
            user.setComLatitude(object.isNull("comLatitude")?"":object.getString("comLatitude"));
            user.setCreateTime(object.isNull("createTime")?"":object.getString("createTime"));
            user.setLoginTime(object.isNull("loginTime")?"":object.getString("loginTime"));
            user.setRemark(object.isNull("remark")?"":object.getString("remark"));
            user.setFriendsNumber(object.isNull("friendsNumber")?"":object.getString("friendsNumber"));
            user.setShareRed(object.isNull("shareRed")?"":object.getString("shareRed"));
            user.setSingleShare(object.isNull("singleShare")?"":object.getString("singleShare"));
            user.setMemberNum(object.isNull("memberNum")?"":object.getString("memberNum"));
            user.setManagerId(object.isNull("managerId")?"":object.getString("managerId"));
            user.setBusinessLicense(object.isNull("businessLicense")?"":object.getString("businessLicense"));
            user.setIpCardPic(object.isNull("ipCardPic")?"":object.getString("ipCardPic"));
            user.setBehalfName(object.isNull("behalfName")?"":object.getString("behalfName"));
            user.setResv1(object.isNull("resv1")?"":object.getString("resv1"));
            user.setResv2(object.isNull("resv2")?"":object.getString("resv2"));
            user.setResv3(object.isNull("resv3")?"":object.getString("resv3"));
            user.setResv4(object.isNull("resv4")?"":object.getString("resv4"));
            user.setResv5(object.isNull("resv5")?"":object.getString("resv5"));
            user.setResv6(object.isNull("resv6")?"":object.getString("resv6"));
            if (remarkp.equals("1")) {
                qiYeInfoList.add(user);
            }
        }
        return qiYeInfoList;
    }
    public static List<OffSendOrderList> parseOfflineList(JSONArray array) throws JSONException{
        List<OffSendOrderList> orderLists = new ArrayList<>();
        for (int i=0;i<array.length();i++){
            JSONObject object = array.getJSONObject(i);
            OffSendOrderList user = new OffSendOrderList();
            user.setOrdId(object.isNull("ordId")?null:object.getString("ordId"));
            user.setImageCount(object.isNull("imageCount")?null:object.getString("imageCount"));
            user.setTime(object.isNull("time")?null:object.getString("time"));
            user.setOrdName(object.isNull("ordName")?null:object.getString("ordName"));
            user.setSendIdentify(object.isNull("sendIdentify")?null:object.getString("sendIdentify"));
            user.setOrderInfo(object.isNull("orderInfo")?null:object.getString("orderInfo"));
            user.setCompanyId(object.isNull("companyId")?null:object.getString("companyId"));
            user.setCusInfo(object.isNull("cusInfo")?null:object.getString("cusInfo"));
            user.setTime_seq(object.isNull("time_seq")?null:object.getString("time_seq"));
            user.setSignature1(object.isNull("signature1")?null:object.getString("signature1"));
            user.setSignature2(object.isNull("signature2")?null:object.getString("signature2"));
            user.setSignature3(object.isNull("signature3")?null:object.getString("signature3"));
            user.setSignature4(object.isNull("signature4")?null:object.getString("signature4"));
            user.setBeforeService1(object.isNull("beforeService1")?null:object.getString("beforeService1"));
            user.setBeforeService2(object.isNull("beforeService2")?null:object.getString("beforeService2"));
            user.setBeforeService3(object.isNull("beforeService3")?null:object.getString("beforeService3"));
            user.setBeforeService4(object.isNull("beforeService4")?null:object.getString("beforeService4"));
            user.setAfterService1(object.isNull("afterService1")?null:object.getString("afterService1"));
            user.setAfterService2(object.isNull("afterService2")?null:object.getString("afterService2"));
            user.setAfterService3(object.isNull("afterService3")?null:object.getString("afterService3"));
            user.setAfterService4(object.isNull("afterService4")?null:object.getString("afterService4"));
            orderLists.add(user);
        }
        return orderLists;
    }
    public static List<PaiDanInfo> parsePaidanList(JSONArray array) throws JSONException{
        List<PaiDanInfo> paiDanInfo = new ArrayList<PaiDanInfo>();
        for (int i=0;i<array.length();i++){
            JSONObject object = array.getJSONObject(i);
            PaiDanInfo user = new PaiDanInfo();
            JSONObject userInfo = object.optJSONObject("userInfo");
            if (userInfo != null) {
                String uName = userInfo.isNull("uName")?"":userInfo.getString("uName");
                String uImage = userInfo.isNull("uImage")?"":userInfo.getString("uImage");
                String uLoginId = userInfo.isNull("uLoginId")?"":userInfo.getString("uLoginId");
                user.setuName(uName);
                user.setuImage(uImage);
                user.setuLoginId(uLoginId);
            }
            user.setUserId(object.isNull("userId")?"":object.getString("userId"));
            user.setOrderId(object.isNull("orderId")?"":object.getString("orderId"));
            user.setMerId(object.isNull("merId")?"":object.getString("merId"));
            user.setRemark(object.isNull("remark")?"":object.getString("remark"));
            user.setOrderBody(object.isNull("orderBody")?"":object.getString("orderBody"));
            user.setOrderTime(object.isNull("orderTime")?"":object.getString("orderTime"));
            user.setOrderState(object.isNull("orderState")?"":object.getString("orderState"));
            user.setResv2(object.isNull("resv2")?"":object.getString("resv2"));
            user.setResv3(object.isNull("resv3")?"":object.getString("resv3"));
            paiDanInfo.add(user);
        }
        return paiDanInfo;
    }
    public static List<KaoqinInfo> parseKaoqinList(JSONArray array) throws JSONException{
        List<KaoqinInfo> kaoqinInfos = new ArrayList<KaoqinInfo>();
        for (int i=0;i<array.length();i++){
            JSONObject object = array.getJSONObject(i);
            KaoqinInfo user = new KaoqinInfo();
            JSONObject userInfo = object.optJSONObject("userInfo");
            if (userInfo != null) {
                String uName = userInfo.isNull("uName")?"":userInfo.getString("uName");
                String uCompany = userInfo.isNull("uCompany")?"":userInfo.getString("uCompany");
                String uImage = userInfo.isNull("uImage")?"":userInfo.getString("uImage");
                String uCompanyAddress = userInfo.isNull("uCompanyAddress")?"":userInfo.getString("uCompanyAddress");
                String resv6 = userInfo.isNull("resv6")?"":userInfo.getString("resv6");
                user.setuName(uName);
                user.setuImage(uImage);
                user.setuCompany(uCompany);
                user.setuCompanyAddress(uCompanyAddress);
                user.setResv6(resv6);
            }
            user.setUserId(object.isNull("userId")?"":object.getString("userId"));
            user.setClockTime(object.isNull("clockTime")?"":object.getString("clockTime"));
            user.setClockType(object.isNull("clockType")?"":object.getString("clockType"));
            user.setCompanyId(object.isNull("companyId")?"":object.getString("companyId"));
            user.setLatitude(object.isNull("latitude")?"":object.getString("latitude"));
            user.setLongitude(object.isNull("longitude")?"":object.getString("longitude"));
            user.setClockPlace(object.isNull("clockPlace")?"":object.getString("clockPlace"));
            user.setRemark(object.isNull("remark")?"":object.getString("remark"));
            user.setResv1(object.isNull("resv1")?"":object.getString("resv1"));
            user.setResv2(object.isNull("resv2")?"":object.getString("resv2"));
            user.setResv3(object.isNull("resv3")?"":object.getString("resv3"));
            kaoqinInfos.add(user);
        }
        return kaoqinInfos;
    }

    public static QiYeInfo parseQiye(JSONObject object) throws JSONException{
        QiYeInfo user = new QiYeInfo();
//        String userConsumerlist = object.optString("userConsumerList");
        JSONArray userProfessions = object.optJSONArray("userProfessionList");
        if (userProfessions != null) {
            for (int j = 0; j < userProfessions.length(); j++) {
                JSONObject proObj = userProfessions.getJSONObject(j);
                String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                String margin = proObj.isNull("margin")?"":proObj.getString("margin");
                String image = proObj.isNull("image")?"":proObj.getString("image");
                String upId = proObj.getString("upId");
                String upDescribe = proObj.isNull("upDescribe")?"":proObj.getString("upDescribe");
                String creatTime = proObj.isNull("creatTime")?"":proObj.getString("creatTime");
                String remark = proObj.isNull("remark")?"0,0":proObj.getString("remark");
                String margin_time = proObj.isNull("margin_time")?"":proObj.getString("margin_time");
                String zyresv1 = proObj.isNull("resv1")?"":proObj.getString("resv1");
                String zyresv2 = proObj.isNull("resv2")?"":proObj.getString("resv2");
                String zyresv3 = proObj.isNull("resv3")?"":proObj.getString("resv3");
                String upTypeId = proObj.isNull("upTypeId")?"":proObj.getString("upTypeId");
                if (upTypeId.equals("1")) {
                    user.setUpName1(TextUtils.isEmpty(upName) ? "" : upName);
                    user.setMargin_time1(TextUtils.isEmpty(margin_time) ? "" : margin_time);
                    user.setZyImage1(TextUtils.isEmpty(image) ? "" : image);
                    user.setMargin1(TextUtils.isEmpty(margin) ? "" : margin);
                    user.setUpId1(TextUtils.isEmpty(upId) ? "" : upId);
                    user.setUpDescribe1(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                    user.setCreateTime1(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                    user.setRemark1(TextUtils.isEmpty(remark) ? "" : remark);
                    user.setZy1resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                    user.setZy1resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                    user.setZy1resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                } else if (upTypeId.equals("2")) {
                    user.setUpName2(TextUtils.isEmpty(upName) ? "" : upName);
                    user.setMargin_time2(TextUtils.isEmpty(margin_time) ? "" : margin_time);
                    user.setZyImage2(TextUtils.isEmpty(image) ? "" : image);
                    user.setMargin2(TextUtils.isEmpty(margin) ? "" : margin);
                    user.setUpId2(TextUtils.isEmpty(upId) ? "" : upId);
                    user.setUpDescribe2(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                    user.setCreateTime2(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                    user.setRemark2(TextUtils.isEmpty(remark) ? "" : remark);
                    user.setZy2resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                    user.setZy2resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                    user.setZy2resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                } else if (upTypeId.equals("3")) {
                    user.setUpName3(TextUtils.isEmpty(upName) ? "" : upName);
                    user.setMargin_time3(TextUtils.isEmpty(margin_time) ? "" : margin_time);
                    user.setZyImage3(TextUtils.isEmpty(image) ? "" : image);
                    user.setMargin3(TextUtils.isEmpty(margin) ? "" : margin);
                    user.setUpId3(TextUtils.isEmpty(upId) ? "" : upId);
                    user.setUpDescribe3(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                    user.setCreateTime3(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                    user.setRemark3(TextUtils.isEmpty(remark) ? "" : remark);
                    user.setZy3resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                    user.setZy3resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                    user.setZy3resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                } else if (upTypeId.equals("4")) {
                    user.setUpName4(TextUtils.isEmpty(upName) ? "" : upName);
                    user.setMargin_time4(TextUtils.isEmpty(margin_time) ? "" : margin_time);
                    user.setZyImage4(TextUtils.isEmpty(image) ? "" : image);
                    user.setMargin4(TextUtils.isEmpty(margin) ? "" : margin);
                    user.setUpId4(TextUtils.isEmpty(upId) ? "" : upId);
                    user.setUpDescribe4(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                    user.setCreateTime4(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                    user.setRemark4(TextUtils.isEmpty(remark) ? "" : remark);
                    user.setZy4resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                    user.setZy4resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                    user.setZy4resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                }
            }
        }
        user.setRedInterval(object.isNull("redInterval")?"":object.getString("redInterval"));
        user.setPersonalDtails(object.isNull("personalDtails")?"":object.getString("personalDtails"));
        user.setFriendsNumber(object.isNull("friendsNumber")?"":object.getString("friendsNumber"));
        user.setShareRed(object.isNull("shareRed")?"":object.getString("shareRed"));
        user.setShareType(object.isNull("shareType")?"":object.getString("shareType"));
        user.setSingleShare(object.isNull("singleShare")?"":object.getString("singleShare"));
        user.setRecruitImages(object.isNull("recruitImages")?"":object.getString("recruitImages"));
        user.setRecruitBody(object.isNull("recruitBody")?"":object.getString("recruitBody"));
        user.setRecruitMoney(object.isNull("recruitMoney")?"0":object.getString("recruitMoney"));
        user.setJoinImages(object.isNull("joinImages")?"":object.getString("joinImages"));
        user.setJoinBody(object.isNull("joinBody")?"":object.getString("joinBody"));
        user.setJoinMoney(object.isNull("joinMoney")?"0":object.getString("joinMoney"));
        user.setCompanyId(object.isNull("companyId")?"":object.getString("companyId"));
        user.setCompanyName(object.isNull("companyName")?"":object.getString("companyName"));
        user.setCompanyShortName(object.isNull("companyShortName")?"0":object.getString("companyShortName"));
        user.setComImage(object.isNull("comImage")?"":object.getString("comImage"));
        user.setComSignature(object.isNull("comSignature")?"":object.getString("comSignature"));
        user.setComType(object.isNull("comType")?"":object.getString("comType"));
        user.setComTel(object.isNull("comTel")?"":object.getString("comTel"));
        user.setComEmail(object.isNull("comEmail")?"":object.getString("comEmail"));
        user.setComAddress(object.isNull("comAddress")?"":object.getString("comAddress"));
        user.setComPushState(object.isNull("comPushState")?"":object.getString("comPushState"));
        user.setComLocationState(object.isNull("comLocationState")?"":object.getString("comLocationState"));
        user.setComPutinAmt(object.isNull("comPutinAmt")?"":object.getString("comPutinAmt"));
        user.setMemberNum(object.isNull("memberNum")?"":object.getString("memberNum"));
        user.setComSubsidyAmt(object.isNull("comSubsidyAmt")?"":object.getString("comSubsidyAmt"));
        user.setComForwardAmt(object.isNull("comForwardAmt")?"":object.getString("comForwardAmt"));
        user.setComLongitude(object.isNull("comLongitude")?"":object.getString("comLongitude"));
        user.setComLatitude(object.isNull("comLatitude")?"":object.getString("comLatitude"));
        user.setCreateTime(object.isNull("createTime")?"":object.getString("createTime"));
        user.setLoginTime(object.isNull("loginTime")?"":object.getString("loginTime"));
        user.setBusinessLicense(object.isNull("businessLicense")?"":object.getString("businessLicense"));
        user.setIpCardPic(object.isNull("ipCardPic")?"":object.getString("ipCardPic"));
        user.setBehalfName(object.isNull("behalfName")?"":object.getString("behalfName"));
        user.setManagerId(object.isNull("managerId")?"":object.getString("managerId"));
        user.setRemark(object.isNull("remark")?"":object.getString("remark"));
        user.setResv1(object.isNull("resv1")?"":object.getString("resv1"));
        user.setResv2(object.isNull("resv2")?"":object.getString("resv2"));
        user.setResv3(object.isNull("resv3")?"":object.getString("resv3"));
        user.setResv4(object.isNull("resv4")?"":object.getString("resv4"));
        user.setResv5(object.isNull("resv5")?"":object.getString("resv5"));
        user.setResv6(object.isNull("resv6")?"":object.getString("resv6"));
        user.setSignPattern(object.isNull("signPattern")?"":object.getString("signPattern"));
        user.setSignShareType(object.isNull("signShareType")?"":object.getString("signShareType"));
        user.setSignShareChannel(object.isNull("signShareChannel")?"":object.getString("signShareChannel"));
        user.setSignShareNeed(object.isNull("signShareNeed")?"":object.getString("signShareNeed"));
        user.setSignShareContent(object.isNull("signShareContent")?"":object.getString("signShareContent"));
        user.setSignShareImage(object.isNull("signShareImage")?"":object.getString("signShareImage"));
        return user;
    }

    public static Userful parseUser(JSONObject object) throws JSONException{
        Userful user = new Userful();
        JSONArray userConsumers = object.getJSONArray("userConsumers");
        JSONArray userProfessions = object.getJSONArray("userProfessions");
        if (userConsumers!=null) {
            for (int i = 0; i < userConsumers.length(); i++) {
                JSONObject conObj = userConsumers.getJSONObject(i);
                String ucName = conObj.getString("ucName");
                String ucTypeId = conObj.getString("ucTypeId");
                if (ucTypeId.equals("1")){
                    user.setUcName1(ucName);
                }else if (ucTypeId.equals("2")){
                    user.setUcName2(ucName);
                }else if (ucTypeId.equals("3")){
                    user.setUcName3(ucName);
                }else if (ucTypeId.equals("4")){
                    user.setUcName4(ucName);
                }else if (ucTypeId.equals("5")){
                    user.setUcName5(ucName);
                }else if (ucTypeId.equals("6")){
                    user.setUcName6(ucName);
                }
            }
        }
        if (userProfessions != null) {
            for (int j = 0; j < userProfessions.length(); j++) {
                JSONObject proObj = userProfessions.getJSONObject(j);
                String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                String margin = proObj.isNull("margin")?"":proObj.getString("margin");
                String image = proObj.isNull("image")?"":proObj.getString("image");
                String upId = proObj.isNull("upId")?"":proObj.getString("upId");
                String upDescribe = proObj.isNull("upDescribe")?"":proObj.getString("upDescribe");
                String creatTime = proObj.isNull("creatTime")?"":proObj.getString("creatTime");
                String margin_time = proObj.isNull("margin_time")?"":proObj.getString("margin_time");
                String remark = proObj.isNull("remark")?"0,0":proObj.getString("remark");
                String zyresv1 = proObj.isNull("resv1")?"":proObj.getString("resv1");
                String zyresv2 = proObj.isNull("resv2")?"":proObj.getString("resv2");
                String zyresv3 = proObj.isNull("resv3")?"":proObj.getString("resv3");
                String upTypeId = proObj.isNull("upTypeId")?"":proObj.getString("upTypeId");
                if (upTypeId.equals("1")) {
                    user.setMargin_time1(margin_time);
                    user.setUpName1(TextUtils.isEmpty(upName) ? "" : upName);
                    user.setZyImage1(TextUtils.isEmpty(image) ? "" : image);
                    user.setMargin1(TextUtils.isEmpty(margin) ? "" : margin);
                    user.setUpId1(TextUtils.isEmpty(upId) ? "" : upId);
                    user.setUpDescribe1(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                    user.setCreateTime1(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                    user.setRemark1(TextUtils.isEmpty(remark) ? "" : remark);
                    user.setZy1resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                    user.setZy1resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                    user.setZy1resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                } else if (upTypeId.equals("2")) {
                    user.setMargin_time2(margin_time);
                    user.setUpName2(TextUtils.isEmpty(upName) ? "" : upName);
                    user.setZyImage2(TextUtils.isEmpty(image) ? "" : image);
                    user.setMargin2(TextUtils.isEmpty(margin) ? "" : margin);
                    user.setUpId2(TextUtils.isEmpty(upId) ? "" : upId);
                    user.setUpDescribe2(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                    user.setCreateTime2(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                    user.setRemark2(TextUtils.isEmpty(remark) ? "" : remark);
                    user.setZy2resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                    user.setZy2resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                    user.setZy2resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                } else if (upTypeId.equals("3")) {
                    user.setMargin_time3(margin_time);
                    user.setUpName3(TextUtils.isEmpty(upName) ? "" : upName);
                    user.setZyImage3(TextUtils.isEmpty(image) ? "" : image);
                    user.setMargin3(TextUtils.isEmpty(margin) ? "" : margin);
                    user.setUpId3(TextUtils.isEmpty(upId) ? "" : upId);
                    user.setUpDescribe3(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                    user.setCreateTime3(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                    user.setRemark3(TextUtils.isEmpty(remark) ? "" : remark);
                    user.setZy3resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                    user.setZy3resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                    user.setZy3resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                } else if (upTypeId.equals("4")) {
                    user.setMargin_time4(margin_time);
                    user.setUpName4(TextUtils.isEmpty(upName) ? "" : upName);
                    user.setZyImage4(TextUtils.isEmpty(image) ? "" : image);
                    user.setMargin4(TextUtils.isEmpty(margin) ? "" : margin);
                    user.setUpId4(TextUtils.isEmpty(upId) ? "" : upId);
                    user.setUpDescribe4(TextUtils.isEmpty(upDescribe) ? "" : upDescribe);
                    user.setCreateTime4(TextUtils.isEmpty(creatTime) ? "" : creatTime);
                    user.setRemark4(TextUtils.isEmpty(remark) ? "" : remark);
                    user.setZy4resv1(TextUtils.isEmpty(zyresv1) ? "" : zyresv1);
                    user.setZy4resv2(TextUtils.isEmpty(zyresv2) ? "" : zyresv2);
                    user.setZy4resv3(TextUtils.isEmpty(zyresv3) ? "" : zyresv3);
                }
            }
        }

        user.setWechatAuth(object.isNull("wechatAuth")?"":object.getString("wechatAuth"));
        user.setTaskImage3(object.isNull("taskImage3")?"":object.getString("taskImage3"));
        user.setTaskImage2(object.isNull("taskImage2")?"":object.getString("taskImage2"));
        user.setTaskImage1(object.isNull("taskImage1")?"":object.getString("taskImage1"));
        user.setVipLevel(object.isNull("vipLevel")?"1":object.getString("vipLevel"));
        user.setVip(object.isNull("vip")?"0":object.getString("vip"));
        user.setMessageOrderCount(object.isNull("messageOrderCount")?"0":object.getString("messageOrderCount"));
        user.setMessageOrderAll(object.isNull("messageOrderAll")?"0":object.getString("messageOrderAll"));
        user.setuClick(object.isNull("uClick")?"1":object.getString("uClick"));
        user.setAreaUserSize(object.isNull("areaUserSize")?"1":object.getString("areaUserSize"));
        user.setAreaProfession(object.isNull("areaProfession")?"1":object.getString("areaProfession"));
        user.setName(object.isNull("uName")?"":object.getString("uName"));
        user.setScore(object.isNull("score")?"":object.getString("score"));
        user.setRedInterval(object.isNull("redInterval")?"":object.getString("redInterval"));
        user.setuNation(object.isNull("uNation")?"":object.getString("uNation"));
        user.setResv4(object.isNull("resv4")?"":object.getString("resv4"));
        user.setPersonalDtails(object.isNull("personalDtails")?"":object.getString("personalDtails"));
        user.setuFiles(object.isNull("uFiles")?"":object.getString("uFiles"));
        user.setShareRed(object.isNull("shareRed")?"":object.getString("shareRed"));
        user.setSingleShare(object.isNull("singleShare")?"":object.getString("singleShare"));
        user.setDynamicTimes(object.isNull("dynamicTimes")?"":object.getString("dynamicTimes"));
        user.setHomePageTimes(object.isNull("homePageTimes")?"":object.getString("homePageTimes"));
        user.setFriendsNumber(object.isNull("friendsNumber")?"0|0|0":object.getString("friendsNumber"));
        user.setMenberNum(object.isNull("menberNum")?"0":object.getString("menberNum"));
        user.setShareType(object.isNull("shareType")?"":object.getString("shareType"));
        user.setLocationState(object.isNull("locationState")?"":object.getString("locationState"));
        user.setImage(object.isNull("uImage")?"":object.getString("uImage"));
        user.setHome(object.isNull("uCity")?"":object.getString("uCity"));
        user.setCompany(object.isNull("uCompany")?"":object.getString("uCompany"));
        user.setCompanyAdress(object.isNull("uCompanyAddress")?"":object.getString("uCompanyAddress"));
        user.setSchool(object.isNull("uSchool")?"":object.getString("uSchool"));
        user.setZhiYe(object.isNull("uVocational")?"":object.getString("uVocational"));
        user.setLoginId(object.isNull("uLoginId")?"":object.getString("uLoginId"));
        user.setSex(object.isNull("uSex")?"":object.getString("uSex"));
        user.setuAge(object.isNull("uAge")?"":object.getString("uAge"));
        user.setResv1(object.isNull("resv1")?"":object.getString("resv1"));
        user.setResv2(object.isNull("resv2")?"":object.getString("resv2"));
        user.setResv3(object.isNull("resv3")?"":object.getString("resv3"));
        user.setResv5(object.isNull("resv5")?"":object.getString("resv5"));
        user.setResv6(object.isNull("resv6")?"":object.getString("resv6"));
        user.setBirthday(object.isNull("uBirthday")?"":object.getString("uBirthday"));
        user.setSignaTure(object.isNull("uSignaTure")?"":object.getString("uSignaTure"));
        return user;
    }

    public static OrderDetail parseOrderDetail(JSONObject object)throws  JSONException{
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderProject(object.isNull("orderProject")?"":object.getString("orderProject"));
        orderDetail.setOrderNumber(object.isNull("orderNumber")?"":object.getString("orderNumber"));
        orderDetail.setOrderAmt(object.isNull("orderAmt")?"":object.getString("orderAmt"));
        orderDetail.setOrderSum(object.isNull("orderSum")?"":object.getString("orderSum"));
        orderDetail.setoSignature(object.isNull("oSignature")?"":object.getString("oSignature"));
        orderDetail.setmSignature(object.isNull("mSignature")?"":object.getString("mSignature"));
        orderDetail.setTime1(object.isNull("time1")?"":object.getString("time1"));
        orderDetail.setTime2(object.isNull("time2")?"":object.getString("time2"));
        orderDetail.setTime3(object.isNull("time3")?"":object.getString("time3"));
        orderDetail.setTime4(object.isNull("time4")?"":object.getString("time4"));
        orderDetail.setImage1(object.isNull("image1")?"":object.getString("image1"));
        orderDetail.setImage2(object.isNull("image2")?"":object.getString("image2"));
        orderDetail.setImage3(object.isNull("image3")?"":object.getString("image3"));
        orderDetail.setImage4(object.isNull("image4")?"":object.getString("image4"));
        orderDetail.setCreate_time(object.isNull("create_time")?"":object.getString("create_time"));
        orderDetail.setDynamicSeq(object.isNull("dynamicSeq")?"":object.getString("dynamicSeq"));
        orderDetail.setSimage1(object.isNull("image01")?"":object.getString("image01"));
        orderDetail.setSimage2(object.isNull("image02")?"":object.getString("image02"));
        orderDetail.setSimage3(object.isNull("image03")?"":object.getString("image03"));
        orderDetail.setSimage4(object.isNull("image04")?"":object.getString("image04"));
        orderDetail.setSimage5(object.isNull("image05")?"":object.getString("image05"));
        orderDetail.setSimage6(object.isNull("image06")?"":object.getString("image06"));
        orderDetail.setSimage7(object.isNull("image07")?"":object.getString("image07"));
        orderDetail.setSimage8(object.isNull("image08")?"":object.getString("image08"));
        orderDetail.setoImage(object.isNull("oImage")?"":object.getString("oImage"));
        orderDetail.setmImage(object.isNull("mImage")?"":object.getString("mImage"));
        orderDetail.setRemark(object.isNull("remark")?"":object.getString("remark"));
        orderDetail.setResv1(object.isNull("resv1")?"":object.getString("resv1"));
        orderDetail.setResv2(object.isNull("resv2")?"":object.getString("resv2"));
        orderDetail.setResv3(object.isNull("resv3")?"":object.getString("resv3"));
        orderDetail.setResv4(object.isNull("resv4")?"":object.getString("resv4"));
        orderDetail.setResv5(object.isNull("resv5")?"":object.getString("resv5"));
        orderDetail.setResv6(object.isNull("resv6")?"":object.getString("resv6"));
        orderDetail.setSend_id1(object.isNull("send_id1")?"":object.getString("send_id1"));
        orderDetail.setSend_id2(object.isNull("send_id2")?"":object.getString("send_id2"));
        return orderDetail;
    }
    public static List<DIDAList> parseDIDAList(JSONArray ary)throws  JSONException{
        List<DIDAList> didaLists = new ArrayList<DIDAList>();
        for (int i = 0; i < ary.length(); i++) {
            JSONObject object = ary.getJSONObject(i);
            DIDAList didaList = new DIDAList();
            didaList.setOrderId(object.isNull("orderId")?"":object.getString("orderId"));
            didaList.setOrderType(object.isNull("orderType")?"":object.getString("orderType"));
            didaList.setOrderAmt(object.isNull("orderAmt")?"":object.getString("orderAmt"));
            didaList.setOrderTotalAmt(object.isNull("orderTotalAmt")?"":object.getString("orderTotalAmt"));
            didaList.setOrderState(object.isNull("orderState")?"":object.getString("orderState"));
            didaList.setOrderTime(object.isNull("orderTime")?"":object.getString("orderTime"));
            didaList.setUserId(object.isNull("userId")?"":object.getString("userId"));
            didaList.setMerId(object.isNull("merId")?"":object.getString("merId"));
            didaList.setRemark(object.isNull("remark")?"":object.getString("remark"));
            didaList.setOrderBody(object.isNull("orderBody")?"":object.getString("orderBody"));
            didaList.setResv1(object.isNull("resv1")?"":object.getString("resv1"));
            didaList.setResv2(object.isNull("resv2")?"":object.getString("resv2"));
            didaList.setResv3(object.isNull("resv3")?"":object.getString("resv3"));
            didaLists.add(didaList);
        }
        return didaLists;
    }

    public static List<Friend> parseFriendQList(JSONArray ary) throws JSONException {
        List<Friend> friends = new ArrayList<Friend>();
        for (int i = 0; i < ary.length(); i++) {
            JSONObject object = ary.getJSONObject(i);
            Friend friend = new Friend();
            friend.setuLoginId(object.isNull("uLoginId") ? "" : object.getString("uLoginId"));
            friend.setuId(object.isNull("uId")?"":object.getString("uId"));
            friend.setFriendsNumber(object.isNull("friendsNumber")?"":object.getString("friendsNumber"));
            friend.setShareRed(object.isNull("shareRed")?"":object.getString("shareRed"));
            friend.setResv1(object.isNull("resv1")?"":object.getString("resv1"));
            friend.setResv2(object.isNull("resv2")?"":object.getString("resv2"));
            friend.setuName(object.isNull("uName") ? "" : object.getString("uName"));
            friend.setuSignaTure(object.isNull("uSignaTure")?"":object.getString("uSignaTure"));
            friend.setuImage(object.isNull("uImage")?"":object.getString("uImage"));
            friend.setuSex(object.isNull("uSex")?"01":object.getString("uSex"));
            friend.setResv3(object.isNull("resv3")?"":object.getString("resv3"));
            friends.add(friend);
        }
        return friends;
    }
    public static List<UserAll> parsePaidanShifuList(JSONArray ary) throws JSONException {
        List<UserAll> users = new ArrayList<UserAll>();
        for (int i = 0; i < ary.length(); i++) {
            UserAll user = new UserAll();
            JSONObject userInfo= ary.getJSONObject(i);
            user.setTimestamp(userInfo.isNull("timestamp")?"":userInfo.getString("timestamp"));
            user.setType(userInfo.isNull("type")?"00":userInfo.getString("type"));
            JSONObject object = userInfo.getJSONObject("userInfo");
            //user.setsId(dindan.isNull("sId")?"":dindan.getString("sId"));
            String locationState = object.getString("locationState");
            user.setuId(object.isNull("uId") ? "" : object.getString("uId"));
            user.setuLoginId(object.isNull("uLoginId") ? "" : object.getString("uLoginId"));
            user.setuName(object.isNull("uName") ? "" : object.getString("uName"));
            user.setuCompany(object.isNull("uCompany") ? "暂未加入企业" : object.getString("uCompany"));
            user.setuCompanyAddress(object.isNull("uCompanyAddress") ? "公司地址未设置" : object.getString("uCompanyAddress"));
            user.setuAge(object.isNull("uAge") ? "27" : object.getString("uAge"));
            user.setuSex(object.isNull("uSex") ? "" : object.getString("uSex"));
            user.setResv1(object.isNull("resv1") ? "" : object.getString("resv1"));
            user.setResv2(object.isNull("resv2") ? "" : object.getString("resv2"));
            user.setResv3(object.isNull("resv3") ? "" : object.getString("resv3"));
            user.setResv4(object.isNull("resv4") ? "" : object.getString("resv4"));
            user.setResv5(object.isNull("resv5") ? "" : object.getString("resv5"));
            user.setResv6(object.isNull("resv6") ? "" : object.getString("resv6"));
            user.setuCity(object.isNull("uCity") ? "" : object.getString("uCity"));
            user.setuSchool(object.isNull("uSchool") ? "未设置" : object.getString("uSchool"));
            user.setDeviceType(object.isNull("deviceType") ? "" : object.getString("deviceType"));
            user.setuVocational(object.isNull("uVocational") ? "" : object.getString("uVocational"));
            user.setuImage(object.isNull("uImage") ? "" : object.getString("uImage"));
            user.setuSignaTure(object.isNull("uSignaTure") ? "" : object.getString("uSignaTure"));
            user.setShareRed(object.isNull("shareRed") ? "" : object.getString("shareRed"));
            user.setuNation(object.isNull("uNation") ? "" : object.getString("uNation"));
            user.setFriendsNumber(object.isNull("friendsNumber") ? "" : object.getString("friendsNumber"));
            user.setMemberNumber(object.isNull("menberNum") ? "0" : object.getString("menberNum"));
            user.setLocationState(object.isNull("locationState")?"01":object.getString("locationState"));

            JSONArray proList = object.getJSONArray("userProfessions");
            if (proList != null) {
                for (int j = 0; j < proList.length(); j++) {
                    JSONObject proObj = proList.getJSONObject(j);
                    String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                    String margin = proObj.isNull("margin")?"0":proObj.getString("margin");
                    String image = proObj.isNull("image")?"":proObj.getString("image");
                    String upTypeId = proObj.getString("upTypeId");
                    if (upTypeId.equals("1")) {
                        user.setpL1(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage1(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen1(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("2")) {
                        user.setpL2(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage2(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen2(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("3")) {
                        user.setpL3(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage3(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen3(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("4")) {
                        user.setpL4(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage4(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen4(TextUtils.isEmpty(margin) ? "0" : margin);
                    }
                }
            }
            users.add(user);
        }
        return users;
    }

    public static List<UserAll> parseFriendList(JSONArray ary) throws JSONException {
        List<UserAll> users = new ArrayList<UserAll>();
        for (int i = 0; i < ary.length(); i++) {
            JSONObject object = ary.getJSONObject(i);
            UserAll user = new UserAll();
            String locationState = object.getString("locationState");
            user.setuId(object.isNull("uId") ? "" : object.getString("uId"));
            user.setuLoginId(object.isNull("uLoginId") ? "" : object.getString("uLoginId"));
            user.setuName(object.isNull("uName") ? "" : object.getString("uName"));
            user.setuCompany(object.isNull("uCompany") ? "暂未加入企业" : object.getString("uCompany"));
            user.setuCompanyAddress(object.isNull("uCompanyAddress") ? "公司地址未设置" : object.getString("uCompanyAddress"));
            user.setuAge(object.isNull("uAge") ? "27" : object.getString("uAge"));
            user.setuSex(object.isNull("uSex") ? "" : object.getString("uSex"));
            user.setResv1(object.isNull("resv1") ? "" : object.getString("resv1"));
            user.setResv2(object.isNull("resv2") ? "" : object.getString("resv2"));
            user.setResv3(object.isNull("resv3") ? "" : object.getString("resv3"));
            user.setResv4(object.isNull("resv4") ? "" : object.getString("resv4"));
            user.setResv5(object.isNull("resv5") ? "" : object.getString("resv5"));
            user.setResv6(object.isNull("resv6") ? "" : object.getString("resv6"));
            user.setuCity(object.isNull("uCity") ? "" : object.getString("uCity"));
            user.setuSchool(object.isNull("uSchool") ? "未设置" : object.getString("uSchool"));
            user.setDeviceType(object.isNull("deviceType") ? "" : object.getString("deviceType"));
            user.setuVocational(object.isNull("uVocational") ? "" : object.getString("uVocational"));
            user.setuImage(object.isNull("uImage") ? "" : object.getString("uImage"));
            user.setuSignaTure(object.isNull("uSignaTure") ? "" : object.getString("uSignaTure"));
            user.setShareRed(object.isNull("shareRed") ? "" : object.getString("shareRed"));
            user.setuNation(object.isNull("uNation") ? "" : object.getString("uNation"));
            user.setFriendsNumber(object.isNull("friendsNumber") ? "" : object.getString("friendsNumber"));
            user.setMemberNumber(object.isNull("menberNum") ? "0" : object.getString("menberNum"));
            user.setLocationState(object.isNull("locationState")?"01":object.getString("locationState"));
            JSONArray proList = object.getJSONArray("userProfessions");
            if (proList != null) {
                for (int j = 0; j < proList.length(); j++) {
                    JSONObject proObj = proList.getJSONObject(j);
                    String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                    String margin = proObj.isNull("margin")?"0":proObj.getString("margin");
                    String image = proObj.isNull("image")?"":proObj.getString("image");
                    String upTypeId = proObj.getString("upTypeId");
                    if (upTypeId.equals("1")) {
                        user.setpL1(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage1(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen1(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("2")) {
                        user.setpL2(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage2(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen2(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("3")) {
                        user.setpL3(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage3(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen3(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("4")) {
                        user.setpL4(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage4(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen4(TextUtils.isEmpty(margin) ? "0" : margin);
                    }
                }
            }
            users.add(user);
        }
        return users;
    }

    public static List<UserAll> parsePaimgList(JSONArray array) throws JSONException {
        List<UserAll> users = new ArrayList<UserAll>();
        for (int i = 0; i < array.length(); i++) {
            //从json数组中获取一个json对象
            JSONObject object2 = array.getJSONObject(i);
            JSONObject object = object2.getJSONObject("vInfo");
            UserAll user = new UserAll();
            String locationState = object.isNull("locationState")?"":object.getString("locationState");
            user.setuId(object.isNull("uId") ? "" : object.getString("uId"));
            user.setuLoginId(object.isNull("uLoginId") ? "" : object.getString("uLoginId"));
            user.setuName(object.isNull("uName") ? "" : object.getString("uName"));
            user.setuCompany(object.isNull("uCompany") ? "暂未加入企业" : object.getString("uCompany"));
            user.setuCompanyAddress(object.isNull("uCompanyAddress") ? "公司地址未设置" : object.getString("uCompanyAddress"));
            user.setuAge(object.isNull("uAge") ? "27" : object.getString("uAge"));
            user.setuSex(object.isNull("uSex") ? "" : object.getString("uSex"));
            user.setResv1(object.isNull("resv1") ? "" : object.getString("resv1"));
            user.setResv2(object.isNull("resv2") ? "" : object.getString("resv2"));
            user.setResv3(object.isNull("resv3") ? "" : object.getString("resv3"));
            user.setResv5(object.isNull("resv5") ? "" : object.getString("resv5"));
            user.setResv6(object.isNull("resv6") ? "" : object.getString("resv6"));
            user.setuCity(object.isNull("uCity") ? "" : object.getString("uCity"));
            user.setuSchool(object.isNull("uSchool") ? "未设置" : object.getString("uSchool"));
            user.setuVocational(object.isNull("uVocational") ? "" : object.getString("uVocational"));
            user.setuImage(object.isNull("uImage") ? "" : object.getString("uImage"));
            user.setuNation(object.isNull("uNation") ? "" : object.getString("uNation"));
            user.setuSignaTure(object.isNull("uSignaTure") ? "" : object.getString("uSignaTure"));
            user.setMemberNumber(object.isNull("menberNum") ? "0" : object.getString("menberNum"));
            user.setLocationState(object.isNull("locationState")?"01":object.getString("locationState"));
            user.setShareRed(object.isNull("shareRed") ? "" : object.getString("shareRed"));
            user.setFriendsNumber(object.isNull("friendsNumber") ? "" : object.getString("friendsNumber"));
            JSONArray proList = object.getJSONArray("userProfessions");
            if (proList != null) {
                for (int j = 0; j < proList.length(); j++) {
                    JSONObject proObj = proList.getJSONObject(j);
                    String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                    String margin = proObj.isNull("margin")?"0":proObj.getString("margin");
                    String image = proObj.isNull("image")?"":proObj.getString("image");
                    String upTypeId = proObj.isNull("upTypeId")?"":proObj.getString("upTypeId");
                    if (upTypeId.equals("1")) {
                        user.setpL1(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage1(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen1(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("2")) {
                        user.setpL2(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage2(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen2(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("3")) {
                        user.setpL3(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage3(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen3(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("4")) {
                        user.setpL4(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage4(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen4(TextUtils.isEmpty(margin) ? "0" : margin);
                    }
                }
            }
            if (!"00".equals(locationState)&&!"10000001200".equals(object.getString("uLoginId"))&&!"22222222222".equals(object.getString("uLoginId"))) {
                users.add(user);
            }
        }
        return users;
    }

    public static List<UserAll> parseUserListNew(JSONArray array) throws JSONException {
        List<UserAll> users = new ArrayList<UserAll>();
        for (int i = 0; i < array.length(); i++) {
            //从json数组中获取一个json对象
            JSONObject obj = array.getJSONObject(i);
            JSONObject object=obj.getJSONObject("user");
            UserAll user = new UserAll();
            String locationState = object.isNull("locationState")?"":object.getString("locationState");
            user.setuId(object.isNull("uId") ? "" : object.getString("uId"));
            user.setuLoginId(object.isNull("uLoginId") ? "" : object.getString("uLoginId"));
            user.setuName(object.isNull("uName") ? "" : object.getString("uName"));
            user.setuCompany(object.isNull("uCompany") ? "暂未加入企业" : object.getString("uCompany"));
            user.setuCompanyAddress(object.isNull("uCompanyAddress") ? "公司地址未设置" : object.getString("uCompanyAddress"));
            user.setuAge(object.isNull("uAge") ? "27" : object.getString("uAge"));
            user.setuSex(object.isNull("uSex") ? "" : object.getString("uSex"));
            user.setResv1(object.isNull("resv1") ? "" : object.getString("resv1"));
            user.setResv2(object.isNull("resv2") ? "" : object.getString("resv2"));
            user.setResv3(object.isNull("resv3") ? "" : object.getString("resv3"));
            user.setResv5(object.isNull("resv5") ? "" : object.getString("resv5"));
            user.setResv6(object.isNull("resv6") ? "" : object.getString("resv6"));
            user.setuCity(object.isNull("uCity") ? "" : object.getString("uCity"));
            user.setuSchool(object.isNull("uSchool") ? "未设置" : object.getString("uSchool"));
            user.setuVocational(object.isNull("uVocational") ? "" : object.getString("uVocational"));
            user.setuImage(object.isNull("uImage") ? "" : object.getString("uImage"));
            user.setuSignaTure(object.isNull("uSignaTure") ? "" : object.getString("uSignaTure"));
            user.setMemberNumber(object.isNull("menberNum") ? "0" : object.getString("menberNum"));
            user.setLocationState(object.isNull("locationState")?"01":object.getString("locationState"));
            user.setShareRed(object.isNull("shareRed") ? "" : object.getString("shareRed"));
            user.setuNation(object.isNull("uNation") ? "" : object.getString("uNation"));
            user.setFriendsNumber(object.isNull("friendsNumber") ? "" : object.getString("friendsNumber"));
            JSONArray proList = object.getJSONArray("userProfessions");
            if (proList != null) {
                for (int j = 0; j < proList.length(); j++) {
                    JSONObject proObj = proList.getJSONObject(j);
                    String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                    String margin = proObj.isNull("margin")?"0":proObj.getString("margin");
                    String image = proObj.isNull("image")?"":proObj.getString("image");
                    String upTypeId = proObj.isNull("upTypeId")?"":proObj.getString("upTypeId");
                    if (upTypeId.equals("1")) {
                        user.setpL1(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage1(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen1(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("2")) {
                        user.setpL2(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage2(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen2(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("3")) {
                        user.setpL3(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage3(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen3(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("4")) {
                        user.setpL4(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage4(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen4(TextUtils.isEmpty(margin) ? "0" : margin);
                    }
                }
            }
            if (!"00".equals(locationState)&&!"10000001200".equals(object.getString("uLoginId"))&&!"22222222222".equals(object.getString("uLoginId"))) {
                users.add(user);
            }
        }
        return users;
    }

    public static List<UserAll> parseUserList(JSONArray array) throws JSONException {
        List<UserAll> users = new ArrayList<UserAll>();
        for (int i = 0; i < array.length(); i++) {
            //从json数组中获取一个json对象
            JSONObject object = array.getJSONObject(i);
            UserAll user = new UserAll();

            String locationState = object.isNull("locationState")?"":object.getString("locationState");
            user.setMessageOrderAll(object.isNull("messageOrderAll") ? "" : object.getString("messageOrderAll"));
            user.setMessageOrderCount(object.isNull("messageOrderCount") ? "" : object.getString("messageOrderCount"));
            user.setuId(object.isNull("uId") ? "" : object.getString("uId"));
            user.setuLoginId(object.isNull("uLoginId") ? "" : object.getString("uLoginId"));
            user.setuName(object.isNull("uName") ? "" : object.getString("uName"));
            user.setuCompany(object.isNull("uCompany") ? "暂未加入企业" : object.getString("uCompany"));
            user.setuCompanyAddress(object.isNull("uCompanyAddress") ? "公司地址未设置" : object.getString("uCompanyAddress"));
            user.setuAge(object.isNull("uAge") ? "27" : object.getString("uAge"));
            user.setuSex(object.isNull("uSex") ? "" : object.getString("uSex"));
            user.setResv1(object.isNull("resv1") ? "" : object.getString("resv1"));
            user.setResv2(object.isNull("resv2") ? "" : object.getString("resv2"));
            user.setResv3(object.isNull("resv3") ? "" : object.getString("resv3"));
            user.setResv5(object.isNull("resv5") ? "" : object.getString("resv5"));
            user.setResv6(object.isNull("resv6") ? "" : object.getString("resv6"));
            user.setuCity(object.isNull("uCity") ? "" : object.getString("uCity"));
            user.setuSchool(object.isNull("uSchool") ? "未设置" : object.getString("uSchool"));
            user.setuVocational(object.isNull("uVocational") ? "" : object.getString("uVocational"));
            user.setuImage(object.isNull("uImage") ? "" : object.getString("uImage"));
            user.setuSignaTure(object.isNull("uSignaTure") ? "" : object.getString("uSignaTure"));
            user.setMemberNumber(object.isNull("menberNum") ? "0" : object.getString("menberNum"));
            user.setLocationState(object.isNull("locationState")?"01":object.getString("locationState"));
            user.setShareRed(object.isNull("shareRed") ? "" : object.getString("shareRed"));
            user.setuNation(object.isNull("uNation") ? "" : object.getString("uNation"));
            user.setFriendsNumber(object.isNull("friendsNumber") ? "" : object.getString("friendsNumber"));
            JSONArray proList = object.getJSONArray("userProfessions");
            if (proList != null) {
                for (int j = 0; j < proList.length(); j++) {
                    JSONObject proObj = proList.getJSONObject(j);
                    String upName = proObj.isNull("upName")?"":proObj.getString("upName");
                    String margin = proObj.isNull("margin")?"0":proObj.getString("margin");
                    String image = proObj.isNull("image")?"":proObj.getString("image");
                    String upTypeId = proObj.isNull("upTypeId")?"":proObj.getString("upTypeId");
                    if (upTypeId.equals("1")) {
                        user.setpL1(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage1(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen1(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("2")) {
                        user.setpL2(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage2(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen2(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("3")) {
                        user.setpL3(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage3(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen3(TextUtils.isEmpty(margin) ? "0" : margin);
                    } else if (upTypeId.equals("4")) {
                        user.setpL4(TextUtils.isEmpty(upName) ? "" : upName);
                        user.setImage4(TextUtils.isEmpty(image) ? "" : image);
                        user.setMargen4(TextUtils.isEmpty(margin) ? "0" : margin);
                    }
                }
            }
            users.add(user);
        }
        return users;
    }

    public static DianziDanju parseDZdanjuDetail(JSONObject object) throws JSONException {
        DianziDanju dianziDanju = new DianziDanju();
        dianziDanju.setSum(object.isNull("sum")?"":object.getString("sum"));
        dianziDanju.setRemark(object.isNull("remark")?"":object.getString("remark"));
        dianziDanju.setScore(object.isNull("score")?"":object.getString("score"));
        dianziDanju.setOrder_price(object.isNull("order_price")?"":object.getString("order_price"));
        dianziDanju.setOrder_number(object.isNull("order_number")?"":object.getString("order_number"));
        dianziDanju.setBeforeService(object.isNull("beforeService")?"":object.getString("beforeService"));
        dianziDanju.setAfterService(object.isNull("afterService")?"":object.getString("afterService"));
        dianziDanju.setFlag(object.isNull("flag")?"":object.getString("flag"));
        dianziDanju.setU_id(object.isNull("u_id")?"":object.getString("u_id"));
        dianziDanju.setTimestamp(object.isNull("timestamp")?"":object.getString("timestamp"));
        dianziDanju.setImage1(object.isNull("image1")?"":object.getString("image1"));
        dianziDanju.setImage2(object.isNull("image2")?"":object.getString("image2"));
        dianziDanju.setImage3(object.isNull("image3")?"":object.getString("image3"));
        dianziDanju.setImage4(object.isNull("image4")?"":object.getString("image4"));
        dianziDanju.setImage5(object.isNull("image5")?"":object.getString("image5"));
        dianziDanju.setImage6(object.isNull("image6")?"":object.getString("image6"));
        dianziDanju.setImage7(object.isNull("image7")?"":object.getString("image7"));
        dianziDanju.setImage8(object.isNull("image8")?"":object.getString("image8"));
        dianziDanju.setContent(object.isNull("content")?"":object.getString("content"));
        dianziDanju.setTitle(object.isNull("title")?"":object.getString("title"));
        dianziDanju.setUpd_id(object.isNull("upd_id")?"":object.getString("upd_id"));
        dianziDanju.setSend_id(object.isNull("send_id")?"":object.getString("send_id"));
        dianziDanju.setTime1(object.isNull("time1")?"":object.getString("time1"));
        dianziDanju.setTime2(object.isNull("time2")?"":object.getString("time2"));
        dianziDanju.setTime3(object.isNull("time3")?"":object.getString("time3"));
        dianziDanju.setTime4(object.isNull("time4")?"":object.getString("time4"));
        dianziDanju.setTime5(object.isNull("time5")?"":object.getString("time5"));
        dianziDanju.setTime6(object.isNull("time6")?"":object.getString("time6"));
        dianziDanju.setTime7(object.isNull("time7")?"":object.getString("time7"));
        dianziDanju.setTime8(object.isNull("time8")?"":object.getString("time8"));
        return dianziDanju;
    }
    public static List<DianziDanju> parseDZdanjuList(JSONArray array) throws JSONException {
        List<DianziDanju> dianziDanjus = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            DianziDanju dianziDanju = new DianziDanju();
            dianziDanju.setScore(object.isNull("score")?"":object.getString("score"));
            dianziDanju.setOrder_price(object.isNull("order_price")?"":object.getString("order_price"));
            dianziDanju.setOrder_number(object.isNull("order_number")?"":object.getString("order_number"));
            dianziDanju.setBeforeService(object.isNull("beforeService")?"":object.getString("beforeService"));
            dianziDanju.setAfterService(object.isNull("afterService")?"":object.getString("afterService"));
            dianziDanju.setFlag(object.isNull("flag")?"":object.getString("flag"));
            dianziDanju.setU_id(object.isNull("u_id")?"":object.getString("u_id"));
            dianziDanju.setTimestamp(object.isNull("timestamp")?"":object.getString("timestamp"));
            dianziDanju.setImage1(object.isNull("image1")?"":object.getString("image1"));
            dianziDanju.setImage2(object.isNull("image2")?"":object.getString("image2"));
            dianziDanju.setImage3(object.isNull("image3")?"":object.getString("image3"));
            dianziDanju.setImage4(object.isNull("image4")?"":object.getString("image4"));
            dianziDanju.setImage5(object.isNull("image5")?"":object.getString("image5"));
            dianziDanju.setImage6(object.isNull("image6")?"":object.getString("image6"));
            dianziDanju.setImage7(object.isNull("image7")?"":object.getString("image7"));
            dianziDanju.setImage8(object.isNull("image8")?"":object.getString("image8"));
            dianziDanju.setContent(object.isNull("content")?"":object.getString("content"));
            dianziDanju.setTitle(object.isNull("title")?"":object.getString("title"));
            dianziDanju.setUpd_id(object.isNull("upd_id")?"":object.getString("upd_id"));
            dianziDanju.setSend_id(object.isNull("send_id")?"":object.getString("send_id"));
            dianziDanju.setTime1(object.isNull("time1")?"":object.getString("time1"));
            dianziDanju.setTime2(object.isNull("time2")?"":object.getString("time2"));
            dianziDanju.setTime3(object.isNull("time3")?"":object.getString("time3"));
            dianziDanju.setTime4(object.isNull("time4")?"":object.getString("time4"));
            dianziDanju.setTime5(object.isNull("time5")?"":object.getString("time5"));
            dianziDanju.setTime6(object.isNull("time6")?"":object.getString("time6"));
            dianziDanju.setTime7(object.isNull("time7")?"":object.getString("time7"));
            dianziDanju.setTime8(object.isNull("time8")?"":object.getString("time8"));
            dianziDanjus.add(dianziDanju);
        }
        return dianziDanjus;
    }

    public static List<BaoZInfo> parseBaozList(JSONArray array) throws JSONException {
        List<BaoZInfo> baoZInfos = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String location = object.isNull("location")?"0|0":object.getString("location");
            String[] str = location.split("\\|");
            BaoZInfo baoZInfo = new BaoZInfo();
            baoZInfo.setLat(str[0]);
            baoZInfo.setLng(str[1]);
            baoZInfo.setUserId(object.isNull("uLoginId")?"":object.getString("uLoginId"));
            baoZInfo.setCreateTime(object.isNull("createTime")?"":object.getString("createTime"));
            baoZInfo.setDynamicSeq(object.isNull("dynamicSeq")?"":object.getString("dynamicSeq"));
            baoZInfo.setRedImage(object.isNull("redImage")?"":object.getString("redImage"));
            baoZInfo.setGameRed(object.isNull("gameRed")?"":object.getString("gameRed"));
            baoZInfo.setRedSum(object.isNull("redSum")?"":object.getString("redSum"));
            baoZInfos.add(baoZInfo);
        }
        return baoZInfos;
    }

    public static List<OrderInfo> parseOrderList(JSONArray array) throws JSONException {
        List<OrderInfo> orderInfos = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setFinalSum(object.isNull("finalSum")?"":object.getString("finalSum"));
            orderInfo.setOrderId(object.isNull("orderId")?"":object.getString("orderId"));
            orderInfo.setMerId(object.isNull("merId")?"":object.getString("merId"));
            orderInfo.setU_name(object.isNull("u_name")?"":object.getString("u_name"));
            orderInfo.setM_name(object.isNull("m_name")?"":object.getString("m_name"));
            orderInfo.setM_uImage(object.isNull("m_uImage")?"":object.getString("m_uImage"));
            orderInfo.setU_uImage(object.isNull("u_uImage")?"":object.getString("u_uImage"));
            orderInfo.setUserId(object.isNull("userId")?"":object.getString("userId"));
            orderInfo.setRemark(object.isNull("remark")?"":object.getString("remark"));
            orderInfo.setOrderState(object.isNull("orderState")?"":object.getString("orderState"));
            orderInfo.setSend_id1(object.isNull("send_id1")?"":object.getString("send_id1"));
            orderInfo.setSend_id2(object.isNull("send_id2")?"":object.getString("send_id2"));
            orderInfo.setOrderTime(object.isNull("orderTime")?"":object.getString("orderTime"));
            orderInfo.setTotalAmt(object.isNull("totalAmt")?"":object.getString("totalAmt"));
            orderInfo.setOrderBody(object.isNull("orderBody")?"":object.getString("orderBody"));
            orderInfo.setResv1(object.isNull("resv1")?"":object.getString("resv1"));
            orderInfo.setResv2(object.isNull("resv2")?"01":object.getString("resv2"));
            orderInfo.setResv3(object.isNull("resv3")?"01":object.getString("resv3"));
            orderInfos.add(orderInfo);
        }
        return orderInfos;
    }
    public static List<FWFKInfo> parseFWFKList(JSONArray array) throws JSONException {
        List<FWFKInfo> fWFKInfos = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            FWFKInfo fWFKInfo = new FWFKInfo();
            fWFKInfo.setFeedbackSeq(object.isNull("feedbackSeq")?"":object.getString("feedbackSeq"));
            fWFKInfo.setMerId(object.isNull("merId")?"":object.getString("merId"));
            fWFKInfo.setLevel1(object.isNull("level1")?"":object.getString("level1"));
            fWFKInfo.setLevel2(object.isNull("level2")?"":object.getString("level2"));
            fWFKInfo.setLevel3(object.isNull("level3")?"":object.getString("level3"));
            fWFKInfo.setLevel4(object.isNull("level4")?"":object.getString("level4"));
            fWFKInfo.setUserId(object.isNull("userId")?"":object.getString("userId"));
            fWFKInfo.setAdvice(object.isNull("advice")?"":object.getString("advice"));
            fWFKInfo.setUserSign(object.isNull("userSign")?"":object.getString("userSign"));
            fWFKInfo.setMerSign(object.isNull("merSign")?"":object.getString("merSign"));
            fWFKInfo.setCreateTime(object.isNull("createTime")?"":object.getString("createTime"));
            fWFKInfo.setRemark(object.isNull("remark")?"":object.getString("remark"));
            fWFKInfos.add(fWFKInfo);
        }
        return fWFKInfos;
    }

    public static QiyeKaoQinInfo parseQiyeKaoQin(JSONObject object) throws JSONException{
        QiyeKaoQinInfo info = new QiyeKaoQinInfo();
        info.setLateState(object.isNull("lateState")?0:object.getInt("lateState"));
        info.setLeaveState(object.isNull("leaveState")?0:object.getInt("leaveState"));
        info.setOverTimeState(object.isNull("overTimeState")?0:object.getInt("overTimeState"));
        info.setWorkState(object.isNull("workState")?0:object.getInt("workState"));
        return info;
    }

   /* public static List<SendToMePaidan> parseSendToMePaidanList(JSONArray array) throws JSONException {

        List<SendToMePaidan> users = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            //从json数组中获取一个json对象
            JSONObject object = array.getJSONObject(i);
            SendToMePaidan info = new SendToMePaidan();
            info.setsId(object.isNull("sId")?"":object.getString("sId"));
            info.setuId(object.isNull("uId")?"":object.getString("uId"));
            info.setContent(object.isNull("content")?"":object.getString("content"));
            info.setLabel(object.isNull("label")?"":object.getString("label"));
            info.setTimestamp(object.isNull("timestamp")?"":object.getString("timestamp"));
            info.setLog(object.isNull("log")?"":object.getString("log"));
            info.setLat(object.isNull("lat")?"":object.getString("lat"));
            info.setState(object.isNull("state")?"":object.getString("state"));
            info.setDisplay(object.isNull("display")?"":object.getString("display"));
            info.setDistance(object.isNull("distance")?"":object.getString("distance"));
            info.setTotalNumber(object.isNull("totalNumber")?"":object.getString("totalNumber"));
            info.setFinishTime(object.isNull("finishTime")?"":object.getString("finishTime"));

            JSONObject object2 = object.getJSONObject("userInfo");
            info.setuLoginId(object2.isNull("uLoginId")?"":object2.getString("uLoginId"));
            info.setuNickName(object2.isNull("uNickName")?"":object2.getString("uNickName"));
            info.setuImage(object2.isNull("uImage")?"":object2.getString("uImage"));
            info.setuPassword(object2.isNull("uPassword")?"":object2.getString("uPassword"));
            info.setuSignaTure(object2.isNull("uSignaTure")?"":object2.getString("uSignaTure"));
            info.setuSex(object2.isNull("uSex")?"":object2.getString("uSex"));
            info.setuBirthday(object2.isNull("uBirthday")?"":object2.getString("uBirthday"));
            info.setuTelephone(object2.isNull("uTelephone")?"":object2.getString("uTelephone"));
            info.setuName(object2.isNull("uName")?"":object2.getString("uName"));
            info.setuEmail(object2.isNull("uEmail")?"":object2.getString("uEmail"));
            info.setuAge(object2.isNull("uAge")?"":object2.getString("uAge"));
            info.setuVocational(object2.isNull("uVocational")?"":object2.getString("uVocational"));
            info.setuCompany(object2.isNull("uCompany")?"":object2.getString("uCompany"));
            info.setuCompanyAddress(object2.isNull("uCompanyAddress")?"":object2.getString("uCompanyAddress"));
            info.setuNation(object2.isNull("uNation")?"":object2.getString("uNation"));
            info.setuProvince(object2.isNull("uProvince")?"":object2.getString("uProvince"));
            info.setuCity(object2.isNull("uCity")?"":object2.getString("uCity"));
            info.setuSchool(object2.isNull("uSchool")?"":object2.getString("uSchool"));
            info.setShippingAddress(object2.isNull("shippingAddress")?"":object2.getString("shippingAddress"));
            info.setPushState(object2.isNull("pushState")?"":object2.getString("pushState"));
            info.setLocationState(object2.isNull("locationState")?"":object2.getString("locationState"));
            info.setSubsidyAmt(object2.isNull("subsidyAmt")?"":object2.getString("subsidyAmt"));
            info.setPutinAmt(object2.isNull("putinAmt")?"":object2.getString("putinAmt"));
            info.setForwardAmt(object2.isNull("forwardAmt")?"":object2.getString("forwardAmt"));
            info.setResv1(object2.isNull("resv1")?"":object2.getString("resv1"));
            info.setResv2(object2.isNull("resv2")?"":object2.getString("resv2"));
            info.setResv3(object2.isNull("resv3")?"":object2.getString("resv3"));
            info.setResv4(object2.isNull("resv4")?"":object2.getString("resv4"));
            info.setResv5(object2.isNull("resv5")?"":object2.getString("resv5"));
            info.setResv6(object2.isNull("resv6")?"":object2.getString("resv6"));
            info.setUserConsumers(object2.isNull("userConsumers")?"":object2.getString("userConsumers"));
            info.setUserProfessions(object2.isNull("userProfessions")?"":object2.getString("userProfessions"));
            info.setSignStateInfo(object2.isNull("signStateInfo")?"":object2.getString("signStateInfo"));
            info.setLiveAuthInfo(object2.isNull("liveAuthInfo")?"":object2.getString("liveAuthInfo"));
            info.setDataCount(object2.isNull("dataCount")?"":object2.getString("dataCount"));
            info.setMenberNum(object2.isNull("menberNum")?"":object2.getString("menberNum"));
            info.setuLocation(object2.isNull("uLocation")?"":object2.getString("uLocation"));
            info.setShareRed(object2.isNull("shareRed")?"":object2.getString("shareRed"));
            info.setSingleShare(object2.isNull("singleShare")?"":object2.getString("singleShare"));
            info.setPersonalDtails(object2.isNull("personalDtails")?0:object2.getInt("personalDtails"));
            info.setFriendsNumber(object2.isNull("friendsNumber")?"":object2.getString("friendsNumber"));
            info.setShareAmt(object2.isNull("shareAmt")?0:object2.getInt("shareAmt"));
            info.setCreateTime(object2.isNull("createTime")?"":object2.getString("createTime"));
            info.setuFiles(object2.isNull("uFiles")?"":object2.getString("uFiles"));
            info.setUpdateTime(object2.isNull("updateTime")?"":object2.getString("updateTime"));
            info.setV_id(object2.isNull("v_id")?"":object2.getString("v_id"));
            info.setFriendsTotal(object2.isNull("friendsTotal")?0:object2.getInt("friendsTotal"));
            info.setRedInterval(object2.isNull("redInterval")?"":object2.getString("redInterval"));
            info.setRegion(object2.isNull("region")?"":object2.getString("region"));
            info.setShareTimes(object2.isNull("shareTimes")?0:object2.getInt("shareTimes"));
            info.setHomePageTimes(object2.isNull("homePageTimes")?0:object2.getInt("homePageTimes"));
            info.setDynamicTimes(object2.isNull("dynamicTimes")?0:object2.getInt("dynamicTimes"));
            info.setDeviceType(object2.isNull("deviceType")?"":object2.getString("deviceType"));
            info.setShareType(object2.isNull("shareType")?"":object2.getString("shareType"));
            info.setShareRedSort(object2.isNull("shareRedSort")?"":object2.getString("shareRedSort"));
            info.setScore(object2.isNull("score")?"":object2.getString("score"));
            info.setComClockInfo(object2.isNull("comClockInfo")?"":object2.getString("comClockInfo"));
            info.setSetSignaTime(object.isNull("setSignaTime")?"":object.getString("setSignaTime"));
            users.add(info);
        }
        return users;

    }*/

    public static List<QiyeKaoQinDetailInfo> parseQiyeKaoQinDetailList(JSONArray array, String workState) throws JSONException{

        List<QiyeKaoQinDetailInfo> users = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            //从json数组中获取一个json对象
            JSONObject object = array.getJSONObject(i);
            QiyeKaoQinDetailInfo info = new QiyeKaoQinDetailInfo();
            info.setCompanyId(object.isNull("companyId")?"":object.getString("companyId"));
            info.setuId(object.isNull("uId")?"":object.getString("uId"));
            info.setTimestamp(object.isNull("timestamp")?"":object.getString("timestamp"));
            info.setMornTime(object.isNull("mornTime")?"":object.getString("mornTime"));
            info.setNoonTime(object.isNull("noonTime")?"":object.getString("noonTime"));
            info.setAfternoonTime(object.isNull("afternoonTime")?"":object.getString("afternoonTime"));
            info.setNightTime(object.isNull("nightTime")?"":object.getString("nightTime"));
            info.setLatitude(object.isNull("latitude")?"":object.getString("latitude"));
            info.setLongitude(object.isNull("longitude")?"":object.getString("longitude"));
            info.setWorkState(object.isNull("workState")?"":object.getString("workState"));
            info.setLateState(object.isNull("lateState")?"":object.getString("lateState"));
            info.setLeaveEarly(object.isNull("leaveEarly")?"":object.getString("leaveEarly"));
            info.setOverTimeState(object.isNull("overTimeState")?"":object.getString("overTimeState"));
            info.setLeaveState(object.isNull("leaveState")?"":object.getString("leaveState"));
            info.setTotalTransAmount(object.isNull("totalTransAmount")?"":object.getString("totalTransAmount"));
            info.setDayTransAmount(object.isNull("dayTransAmount")?"":object.getString("dayTransAmount"));
            info.setWorkTime(object.isNull("workTime")?"":object.getString("workTime"));
            info.setLateTime(object.isNull("lateTime")?"":object.getString("lateTime"));
            info.setLeaveEarlyTime(object.isNull("leaveEarlyTime")?"":object.getString("leaveEarlyTime"));
            info.setOverTime(object.isNull("overTime")?"":object.getString("overTime"));
            info.setLeaveTime(object.isNull("leaveTime")?"":object.getString("leaveTime"));
            info.setVisitorTime(object.isNull("visitorTime")?"":object.getString("visitorTime"));

            JSONObject object2 = object.getJSONObject("userInfo");
            info.setuLoginId(object2.isNull("uLoginId")?"":object2.getString("uLoginId"));
            info.setuNickName(object2.isNull("uNickName")?"":object2.getString("uNickName"));
            info.setuImage(object2.isNull("uImage")?"":object2.getString("uImage"));
            info.setuPassword(object2.isNull("uPassword")?"":object2.getString("uPassword"));
            info.setuSignaTure(object2.isNull("uSignaTure")?"":object2.getString("uSignaTure"));
            info.setuSex(object2.isNull("uSex")?"":object2.getString("uSex"));
            info.setuBirthday(object2.isNull("uBirthday")?"":object2.getString("uBirthday"));
            info.setuTelephone(object2.isNull("uTelephone")?"":object2.getString("uTelephone"));
            info.setuName(object2.isNull("uName")?"":object2.getString("uName"));
            info.setuEmail(object2.isNull("uEmail")?"":object2.getString("uEmail"));
            info.setuAge(object2.isNull("uAge")?"":object2.getString("uAge"));
            info.setuVocational(object2.isNull("uVocational")?"":object2.getString("uVocational"));
            info.setuCompany(object2.isNull("uCompany")?"":object2.getString("uCompany"));
            info.setuCompanyAddress(object2.isNull("uCompanyAddress")?"":object2.getString("uCompanyAddress"));
            info.setuNation(object2.isNull("uNation")?"":object2.getString("uNation"));
            info.setuProvince(object2.isNull("uProvince")?"":object2.getString("uProvince"));
            info.setuCity(object2.isNull("uCity")?"":object2.getString("uCity"));
            info.setuSchool(object2.isNull("uSchool")?"":object2.getString("uSchool"));
            info.setShippingAddress(object2.isNull("shippingAddress")?"":object2.getString("shippingAddress"));
            info.setPushState(object2.isNull("pushState")?"":object2.getString("pushState"));
            info.setLocationState(object2.isNull("locationState")?"":object2.getString("locationState"));
            info.setSubsidyAmt(object2.isNull("subsidyAmt")?"":object2.getString("subsidyAmt"));
            info.setPutinAmt(object2.isNull("putinAmt")?"":object2.getString("putinAmt"));
            info.setForwardAmt(object2.isNull("forwardAmt")?"":object2.getString("forwardAmt"));
            info.setResv1(object2.isNull("resv1")?"":object2.getString("resv1"));
            info.setResv2(object2.isNull("resv2")?"":object2.getString("resv2"));
            info.setResv3(object2.isNull("resv3")?"":object2.getString("resv3"));
            info.setResv4(object2.isNull("resv4")?"":object2.getString("resv4"));
            info.setResv5(object2.isNull("resv5")?"":object2.getString("resv5"));
            info.setResv6(object2.isNull("resv6")?"":object2.getString("resv6"));
            info.setUserConsumers(object2.isNull("userConsumers")?"":object2.getString("userConsumers"));
            info.setUserProfessions(object2.isNull("userProfessions")?"":object2.getString("userProfessions"));
            info.setSignStateInfo(object2.isNull("signStateInfo")?"":object2.getString("signStateInfo"));
            info.setLiveAuthInfo(object2.isNull("liveAuthInfo")?"":object2.getString("liveAuthInfo"));
            info.setDataCount(object2.isNull("dataCount")?"":object2.getString("dataCount"));
            info.setMenberNum(object2.isNull("menberNum")?"":object2.getString("menberNum"));
            info.setuLocation(object2.isNull("uLocation")?"":object2.getString("uLocation"));
            info.setShareRed(object2.isNull("shareRed")?"":object2.getString("shareRed"));
            info.setSingleShare(object2.isNull("singleShare")?"":object2.getString("singleShare"));
            info.setPersonalDtails(object2.isNull("personalDtails")?0:object2.getInt("personalDtails"));
            info.setFriendsNumber(object2.isNull("friendsNumber")?"":object2.getString("friendsNumber"));
            info.setShareAmt(object2.isNull("shareAmt")?0:object2.getInt("shareAmt"));
            info.setCreateTime(object2.isNull("createTime")?"":object2.getString("createTime"));
            info.setuFiles(object2.isNull("uFiles")?"":object2.getString("uFiles"));
            info.setUpdateTime(object2.isNull("updateTime")?"":object2.getString("updateTime"));
            info.setV_id(object2.isNull("v_id")?"":object2.getString("v_id"));
            info.setFriendsTotal(object2.isNull("friendsTotal")?0:object2.getInt("friendsTotal"));
            info.setRedInterval(object2.isNull("redInterval")?"":object2.getString("redInterval"));
            info.setRegion(object2.isNull("region")?"":object2.getString("region"));
            info.setShareTimes(object2.isNull("shareTimes")?0:object2.getInt("shareTimes"));
            info.setHomePageTimes(object2.isNull("homePageTimes")?0:object2.getInt("homePageTimes"));
            info.setDynamicTimes(object2.isNull("dynamicTimes")?0:object2.getInt("dynamicTimes"));
            info.setDeviceType(object2.isNull("deviceType")?"":object2.getString("deviceType"));
            info.setShareType(object2.isNull("shareType")?"":object2.getString("shareType"));
            info.setShareRedSort(object2.isNull("shareRedSort")?"":object2.getString("shareRedSort"));
            info.setScore(object2.isNull("score")?"":object2.getString("score"));
            info.setComClockInfo(object2.isNull("comClockInfo")?"":object2.getString("comClockInfo"));
            info.setSetSignaTime(object.isNull("setSignaTime")?"":object.getString("setSignaTime"));
            if (workState.equalsIgnoreCase("workState=01")) {
                info.setType("workState");
            } else if (workState.equalsIgnoreCase("lateState=01")) {
                info.setType("lateState");
            }else if (workState.equalsIgnoreCase("overTimeState=01")) {
                info.setType("overTimeState");
            }else if (workState.equalsIgnoreCase("leaveState=01")) {
                info.setType("leaveState");
            }
            users.add(info);
        }

        return users;
    }
}
