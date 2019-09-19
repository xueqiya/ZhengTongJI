package com.sangu.apptongji.main;

/**
 * Created by ustc on 2016/6/27.
 */
public class FXConstant {
    //本地服务器对外映射
    //public static final String HOST = "http://yfx7gh.natappfree.cc/";
    //本地服务器
//    public static final String HOST = "http://192.168.0.116:8080/SGConsole/";
    //实际上架用到的url 线上服务器
    public static final String HOST = "http://www.fulu86.com/";

    //统计版本
    public static final String URL_SELECTDYNAMICBYAUTH = HOST + "dynamic/queryDynamicByAuthType";
    public static final String URL_SELECTREGIONALRANK = HOST + "user/selectRegionalRank";
    public static final String URL_INSERTFREEZELOGIN = HOST + "freezeLogin/insertFreezeLogin";
    public static final String URL_INSERTPROHIBIT = HOST + "prohibit/insertProhibit";
    public static final String URL_UPDATEDYNAMICRECOMD = HOST + "dynamic/updateDynamicInfoRecomd";
    public static final String URL_SELECTUSERBYANTISTOP = HOST + "user/selectUserinfoByAntistop";
    public static final String URL_UPDATEMESSAGECOUNT = HOST + "user/updateMessageOrderCount";
    public static final String URL_INSERTMEMODYNAMIC = HOST + "memodynamic/insertMemoDynamic";
    public static final String URL_SENDPUSHBYDISTANCE = HOST + "dynamic/sendPushByDistance";
    public static final String URL_SELECTPROHIBIT = HOST + "prohibit/selectProhibitListByType";
    public static final String URL_SELECTFREEZELOGIN = HOST + "freezeLogin/selectFreezeLoginByType";
    public static final String URL_SELECTMERACCOUNTPROHIBIT = HOST + "merAccount/selectMerAccountProhibit";

    public static final String URL_AVATAR = HOST + "upload/userFiles/";
    public static final String URL_DZDANJU = HOST + "upload/memorandum/";
    public static final String URL_VIDEO = HOST + "upload/dynamicVideo/";
    public static final String URL_SIGN = HOST + "upload/";
    public static final String URL_SIGN_FOUR = HOST + "upload/sendOffOrder/";
    public static final String URL_PAIDANQIANZI = HOST + "upload/ODSign/";
    public static final String URL_ZY_AVATAR = HOST + "upload/up/";
    public static final String URL_FuwuFanKuiTuPian_Query = HOST + "upload/sign/";
    public static final String URL_DOWNLOAD_APK = HOST + "upload/apkPackage/";
    public static final String URL_SOCIAL_PHOTO = HOST + "upload/dynamic/";
    public static final String URL_QIYEFAREN_SHENFENZHENG = HOST + "upload/company/id/";
    public static final String URL_QIYE_ZHIZHAO = HOST + "upload/company/business/";
    public static final String URL_QIYE_ZY = HOST + "upload/company/userprofession/";
    public static final String URL_QIYE_TOUXIANG = HOST + "upload/company/pic/";
    public static final String URL_QINGJIA_QIANMING = HOST + "upload/leave/";
    public static final String URL_QINGJIASHXB_FENXIANG = HOST + "upload/company/propagate/";
    public static final String URL_HETONG_QIANMING = HOST + "upload/agreement/";
    public static final String URL_BAOBIAO_QIANMING = HOST + "upload/company/planSignture/";
    public static final String URL_RENZHENG_TUPIAN = HOST + "upload/authentication/ID/";
    public static final String URL_ZHIFU_HUIDIAO = HOST + "alipay/NotifyUrl";
    public static final String URL_ZHIFUWX_DIAOQI = HOST + "wxpay/pay";
    public static final String URL_WX_TIXIAN = HOST + "wxpay/Withdrawals";
    public static final String URL_QIYE_ZHAOPIN = HOST + "upload/company/recruit/";
    public static final String URL_QIYE_JIAMENG = HOST + "upload/company/join/";
    public static final String URL_OFFLINE_SIGN = HOST + "upload/offSendOrder/planSignture/";
    public static final String URL_OFFLINE_FUWU = HOST + "upload/offSendOrder/planService/";
    public static final String URL_UPLOAD_SPEED = HOST + "upload/speed";
    public static final String URL_ADVERTURL = HOST + "upload/notice/";
    public static final String URL_ARTICLE = HOST + "upload/article/";
    public static final String URL_ARTICLEVIDEO = HOST + "upload/articleVideo/";


    public static final String URL_TASKIMAGEUPDATE = HOST + "user/userInfoTaskUpdate";
    public static final String URL_SELECTAllORDERBYID = HOST + "order/selectAllOrderByUserId";
    public static final String URL_SELECTAllCOMPANYNAME = HOST + "company/selectAllCompanyName";
    public static final String URL_SELECTDYNAMICBYIDENTIFY = HOST + "dynamic/selectDynamicByIdentify";
    public static final String URL_INSERTDYNAMICCOLLECTION = HOST + "dynamic/insertCollectionDynamic";
    public static final String URL_DELETEDYNAMICCOLLECTION = HOST + "dynamic/deleteCollectionDynamic";
    public static final String URL_SELECTDYNAMICCOLLECTION = HOST + "dynamic/selectDynamicCollection";
    public static final String URL_DELETEDYNAMICPUSH = HOST + "dynamic/deleteDynamicPush";
    public static final String URL_INSERTOFFLINEORDER = HOST + "dynamic/insertOffLineOrder";
    public static final String URL_COMMENTREWARD = HOST + "merAccount/rewardAccount";
    public static final String URL_AddCOMMENTPRAISE = HOST + "dynamic/addCommentPraise";
    public static final String URL_DELETEPRAISE = HOST + "dynamic/cancleCommentPraise";
    public static final String URL_INSERTCOUNTDOWN = HOST + "data/insertCountDownload";//记录点击了什么
    public static final String URL_WITHDRAWALPROMOTE = HOST + "merAccount/withDrawalPromote";
    public static final String URL_SELECTREDPROMOTE = HOST + "redPromote/selectRedPromoteReconrd";
    public static final String URL_INSERTREDPROMOTE = HOST + "redPromote/insertPromoteRecord";
    public static final String URL_CONTACTDATA = HOST + "userDetailContact/selectUserDetailContact";
    public static final String URL_SELECTPROFESSRANK = HOST + "userProfession/selectSupplyUpName";
    public static final String URL_SELECTPROSINGLE = HOST + "userProfession/selectSingleInfo";
    public static final String URL_SELECTREGIONRANK = HOST + "userProfession/selectSupplyUpNameByRegion";
    public static final String URL_PATMESSAGECOUNT = HOST + "merAccount/payMessageSendOrder";
    public static final String URL_PAYVIP = HOST + "merAccount/payVip";
    public static final String URL_UPDATE_SUPPORTCOUNT = HOST + "functionSupport/updateFunctionSupportInfo";
    public static final String URL_QUERY_SUPPORTCOUNT = HOST + "functionSupport/selectFunctionSupportInfo";
    public static final String URL_RECORD_PHONE = HOST + "inFriend/insertInFriendRecord";
    public static final String URL_SEARCH_REDAUTH = HOST + "red/selectRed";
    public static final String URL_INSERT_INVITE = HOST + "data/insertInviteInfo";
    public static final String URL_DUANXIN_TONGZHI = HOST + "user/Notice";
    public static final String URL_DYNAMIC_DUANXIN = HOST + "dynamic/sendOrder";
    public static final String URL_SEARCH_ADDRESS_FRIEND = HOST + "friend/userSelect";
    public static final String URL_SEARCH_UPDATE = HOST + "versionInfo/queryVersionInfo";
    public static final String URL_REGISTER = HOST + "user/register";
    public static final String URL_DUANXIN = HOST + "user/sendMassage";
    public static final String URL_AUTHCODE = HOST + "user/getAuthCode";
    public static final String URL_SEND_AUTHCODE = HOST + "user/sendAuthCode";
    public static final String URL_LOGIN = HOST + "user/login";
    public static final String URL_TONGJI_ZHUANFA = HOST + "user/ForwardAmountUpdate";
    public static final String URL_FriendList = HOST + "friend/queryFriend";
    public static final String URL_SELECT_CONTACT = HOST + "speedList/selectContact";
    public static final String URL_UPDATE_CONTACT = HOST + "speedList/updateContact";
    public static final String URL_Search_User = HOST + "select/selectOverall";
    public static final String URL_Search_LiuLanList = HOST + "visitor/selectVisitor";
    public static final String URL_SearchZheXian_LiuLanList = HOST + "visitor/selectBrowseTimes";
    public static final String URL_Search_ZhuanFaList = HOST + "user/selectFwdAmtList";
    public static final String URL_Get_UserInfo = HOST + "user/userInfoQuery?uLoginId=";
    public static final String URL_UPDATE_Groupnanme = HOST + "update_groupname";
    public static final String URL_UPDATE = HOST + "user/userInfoUpdate";
    public static final String URL_UPDATE_TIME = HOST + "user/userLoginUpdate";
    public static final String URL_UPDATE_DLMM = HOST + "user/updatePassword";
    public static final String URL_ADD_FRIEND = HOST + "friend/addFriend";
    public static final String URL_CONFIRMADD_FRIEND = HOST + "friend/confirmAdd";
    public static final String URL_CONFIRMQUERY_FRIEND = HOST + "friend/confirmQuery";
    public static final String URL_SHIMING_RENZHENG = HOST + "Authentication/addAuthentication";
    public static final String URL_CHAXUN_RENZHENG = HOST + "Authentication/selectAuthentication";
    public static final String URL_XIUGAI_RENZHENG = HOST + "Authentication/updateAuthentication";
    public static final String URL_MINGZI_DAN = HOST + "Transaction/transaction";
    public static final String URL_QUERY_HBCOUNT = HOST + "Transaction/selectSumByTransactionType?mer_id=";
    public static final String URL_ADD_USERCISHU = HOST + "user/personalDtails";
    public static final String URL_ADD_COMCISHU = HOST + "company/insertComVisitorInfo";
    public static final String URL_DELETE_BLACKLIST = HOST + "shield/deleteShieldInfo";
    public static final String URL_ADDTO_BLACKLIST = HOST + "shield/insertShieldInfo";
    public static final String URL_BLACKLIST = HOST + "shield/selectShieldInfo";
    public static final String URL_QUERY_PINJIAZIGE = HOST + "user/selectUserCommentTimes";
    public static final String URL_QUERY_PINJIALIST = HOST + "user/selectUserCommentsInfo";
    public static final String URL_DELETE_PINJIA = HOST + "user/deleteUserComment";
    public static final String URL_PINJIA = HOST + "user/insertUserComment";
    public static final String URL_XIUGAI_HBTIMES = HOST + "user/updateShareTimes";
    public static final String URL_GONGGAO = HOST + "data/selectNotice";
    public static final String URL_SEARCH_ALL_UNREAD = HOST + "user/statistics?uLoginId=";
    public static final String URL_SEARCH_ALLZHFLL = HOST + "user/selectAccessSource";
    public static final String URL_RECEIVE_LOCAL = HOST + "carTeam/queryByUId?u_id=";
    public static final String URL_RECEIVE_ALLLOCAL = HOST + "carTeam/queryByTeamId?teamId=";
    public static final String URL_INSERT_SHARETEAM = HOST + "carTeam/insertCarTeam";
    public static final String URL_DELETE_SHARETEAM = HOST + "carTeam/deleteCarTeam";
    public static final String URL_SENDPUSHMSG = HOST + "aliyunPush/push";
    public static final String URL_SENDPUSHMSGLIST = HOST + "aliyunPush/pushByUids";
    public static final String URL_SEARCH_RED = HOST + "data/shareRedSum";
    public static final String URL_INSERT_READHIS = HOST + "user/regLog";
    public static final String URL_INSERT_JIEDANSET = HOST + "screen/updateScreenInfo";
    public static final String URL_QUERY_JIEDANSET = HOST + "screen/selectScreenInfo";
    public static final String URL_QUERY_SHARERED = HOST + "data/userShareRed";
    public static final String URL_INSERT_CONTACT_TRACK = HOST + "data/insertContact";
    public static final String URL_UPDATE_KUAIPAI_PUSH = HOST + "speedList/updateReceipt";
    public static final String URL_INSERT_LOGIN = HOST + "user/insertLoginLog";
    public static final String URL_UPDATE_LOGIN = HOST + "user/updateLoginLog";
    public static final String URL_SELECT_PROHIBIT = HOST + "prohibit/selectProhibit";
    public static final String URL_QUERY_KAOQIN_SET = HOST + "company/selectComBackLog";
    public static final String URL_UPDATE_KAOQIN_SET = HOST + "company/insertComBackLog";
    public static final String URL_UPDATE_DYNAMIC_RECEIPT = HOST + "dynamic/updateDynamicReceipt";
    public static final String URL_UPDATE_DYNAMIC_PUSH = HOST + "dynamic/updateDynamicPush";
    public static final String URL_UPDATE_INSERT_DYNAMIC_CONTACT = HOST + "dynamic/insertDynamicDealContact";
    public static final String URL_QUERY_FREEZE_LOGIN = HOST + "user/selectFreezeLoginInfo";
    //bmob移接
    public static final String URL_UPDATE_DYNATIME = HOST + "dynamic/dynamicTimeUpdate";
    public static final String URL_QUERY_DYNATIME = HOST + "dynamic/selectDynamicTime";
    public static final String URL_UPDATE_INSTALL = HOST + "data/updatePushDevice";
    public static final String URL_DELETE_INSTALL = HOST + "data/deletePushDevice";
    public static final String URL_QUERY_INSTALL = HOST + "data/selectPushDevice?uId=";
    public static final String URL_QUERY_UNREADQIYE = HOST + "company/selectCompanyData?companyId=";
    public static final String URL_UPDATE_UNREADQIYE = HOST + "company/updateCompanyData";
    public static final String URL_QUERY_UNREADUSER = HOST + "user/selectUserData?userId=";
    public static final String URL_UPDATE_UNREADUSER = HOST + "user/updateUserData";
    public static final String URL_QUERY_APPLYORDER = HOST + "dynamic/selectApplyOrder";
    public static final String URL_INSERT_APPLYORDER = HOST + "dynamic/insertApplyOrder";
    public static final String URL_QUERY_DYNACOMMENT = HOST + "dynamic/selectDynamicComment";
    public static final String URL_INSERT_DYNACOMMENT = HOST + "dynamic/insertDynamicComment";
    public static final String URL_DELETE_DYNACOMMENT = HOST + "dynamic/deleteDynamicComment";
    public static final String URL_INSERT_OFFSEND = HOST + "company/insertOffSendOrder";
    public static final String URL_QUERY_OFFSEND = HOST + "company/selectOffSendOrder";
    public static final String URL_UPDATE_OFFSEND = HOST + "company/updateOffSendOrder";
    public static final String URL_DELETE_OFFSEND = HOST + "company/deleteOffSendOrder";
    public static final String URL_SEARCH_PUSHLIST = HOST + "aliyunPush/selectOffLinePush";
    public static final String URL_DELETE_PUSH = HOST + "aliyunPush/deleteOffLinePush";


    //企业接口

    public static final String URL_INSERTMEMO = HOST + "memo/insertMemo";
    public static final String URL_INSERTABNORMAL = HOST + "company/insertAbnormalSign";
    public static final String URL_UPDATEComClock = HOST + "company/updateComClock";
    public static final String URL_UPDATE_QIYE = HOST + "company/doUpdate";
    public static final String URL_COMPANY_INSERT = HOST + "company/companyInsert";
    public static final String URL_Get_QiyeInfo = HOST + "company/doSelect?companyId=";
    public static final String URL_Get_QiyeList = HOST + "company/doListSelect";
    public static final String URL_ADDTO_QIYE = HOST + "company/joinCompany";
    public static final String URL_QUERY_ADD = HOST + "company/applyQuery?companyId=";
    public static final String URL_QUERY_CONFIRM = HOST + "company/doConfirm";
    public static final String URL_QUERY_QIYEMAJAR = HOST + "company/queryCompany";
    public static final String URL_YUGONG_QIANDAO = HOST + "company/clockOutRecord";
    public static final String URL_INSERTE_QINGJIA = HOST + "company/insertLeaveInfo";
    public static final String URL_UPDATE_QINGJIA = HOST + "company/updateLeaveInfo";
    public static final String URL_QINGJIA_LIST = HOST + "company/selectLeaveInfo";
    public static final String URL_KAIQUN_LIST = HOST + "company/selectUserRecord";
    public static final String URL_QIYE_KAOQIN = HOST + "company/queryComClockCount";
    public static final String URL_QUERY_QIYE_KAOQIN_PAIHANG = HOST + "company/queryComClockByType";
    public static final String URL_QIYE_KAOQIN_DETAIL = HOST + "company/queryComClock";
    public static final String URL_QIYE_PAIDAN = HOST + "company/companyDispatch";
    public static final String URL_QIYE_DONGJIEJINE = HOST + "company/freeze";
    public static final String URL_QIYE_JIEDONGJINE = HOST + "company/merThaw";
    public static final String URL_QIYE_PAIDANLIST = HOST + "company/selectCompanyDispatch";
    public static final String URL_QIYE_YUANGONGTONGJI = HOST + "data/insertData";
    public static final String URL_QIYE_SHANCHU = HOST + "company/deleteCompanyUser";
    public static final String URL_QIYE_SHANCHUTONGJI = HOST + "data/deleteData";
    public static final String URL_QIYE_CHUANGJIANHETONG = HOST + "company/insertAgreementInfo";
    public static final String URL_QIYE_CHUANGJIANBAOBIAO = HOST + "company/insertPlanInfo";
    public static final String URL_QIYE_XIUGAIANHETONG = HOST + "company/updateAgreementInfo";
    public static final String URL_QIYE_XIUGAIANBAOBIAO = HOST + "company/updatePlanInfo";
    public static final String URL_QIYE_SHANCHUHETONG = HOST + "company/deleteAgreement";
    public static final String URL_QIYE_HETONGLIST = HOST + "company/selectAgreementInfo";
    public static final String URL_QIYE_BAOBIAOLIST = HOST + "company/selectPlanInfo";
    public static final String URL_QUERY_ZHFHBCSHU = HOST + "payOrder/queryShareRecord";
    public static final String URL_QUERY_READDCOUNT = HOST + "company/selectTimesByComId?companyId=";
    public static final String URL_QUERY_READDETAIL = HOST + "company/selectByComId";
    public static final String URL_QUERY_PUSH_DINGDAN = HOST + "speedList/selectReceipt";
    public static final String URL_QUERY_MY_SEND_DINGDAN = HOST + "speedList/selectList";
    public static final String URL_QUERY_FINAL_LIST = HOST + "speedList/selectFinalList";

    //群组接口
    public static final String URL_INSERT_GROUP = HOST + "group/createGroup";
    public static final String URL_SELECT_GROUP = HOST + "group/selectGroupInfo";
    public static final String URL_SELECT_GROUPINFO = HOST + "group/selectGroupByGroupId?groupId=";
    public static final String URL_SELECT_MEMBERLIST = HOST + "group/selectGroupUserInfo";
    public static final String URL_EXIT_GROUP = HOST + "group/exitGroup";
    public static final String URL_DELETE_GROUP = HOST + "group/disbandGroup?groupId=";
    public static final String URL_INVITE_MEMBER = HOST + "group/inviteUserJoin";
    public static final String URL_UPDATE_GROUPINFO = HOST + "group/updateGroupInfo";

    //订单接口
    public static final String URL_CHAXUN_DZ_DANJU_LIST = HOST + "memorandum/selectMemorandum";
    public static final String URL_CHAXUN_DZ_DANJU_DETAIL = HOST + "memorandum/selectSigleInfo";
    public static final String URL_FASONG_DZ_DANJU = HOST + "memorandum/sendMemorandumInfo";
    public static final String URL_INSERT_DZ_DANJU = HOST + "memorandum/insertMemorandum";
    public static final String URL_UPDATE_DZ_DANJU = HOST + "memorandum/updateMemorandumInfo";
    public static final String URL_DELETE_DZ_DANJU = HOST + "memorandum/deleteId";
    public static final String URL_DELETE_BAOJIA = HOST + "dynamic/revokeOrder";
    public static final String URL_UPDATE_BAOJIA = HOST + "dynamic/updateDynamicOrder";
    public static final String URL_FASONG_DZDANJU = HOST + "order/addOrderUser";
    public static final String URL_CHAXUN_DZDANJU = HOST + "order/selectOrderUser";
    public static final String URL_INSERT_ORDER = HOST + "order/orderInfoInsert";
    public static final String URL_OrderList = HOST + "order/orderInfoQueryByState";
    public static final String URL_ALLOrderList = HOST + "order/orderInfoQuery";
    public static final String URL_INSERT_OrderDetail = HOST + "order/orderDetailInsert";
    public static final String URL_INSERT_DYNAMIC_ORDER = HOST + "dynamic/robOrder";
    public static final String URL_Order_Detail = HOST + "order/orderDetailQuery";
    public static final String URL_DYNAMIC_CHANGE = HOST + "dynamic/placeOrder";
    public static final String URL_Order_Detail_update = HOST + "order/orderDetailUpdate";
    public static final String URL_Update_OrderState = HOST + "merAccount/updateState";
    public static final String URL_DingDan_Pay = HOST + "merAccount/pay";
    public static final String URL_SHOUHOU_UPDATE = HOST + "order/orderUpdateState";
    public static final String URL_Search_DingDanList = HOST + "order/oiQueryByUpId";
    public static final String URL_Search_LiuLanCiShu = HOST + "user/professionRecord";
    public static final String URL_FuwuFanKui_Create = HOST + "feedback/feedbackCreate";
    public static final String URL_FuwuFanKui_Update = HOST + "feedback/feedbackUpdate";
    public static final String URL_FuwuFanKui_Query = HOST + "feedback/feedbackInfoQuery";
    //账户接口
    public static final String URL_ZhiFu = HOST + "alipay/pay";
    public static final String URL_Query_YuE = HOST + "merAccount/query";
    public static final String URL_INSERT_KUAISUDINGDAN = HOST + "speedList/insertList";
    public static final String URL_INSERT_CONTACT = HOST + "speedList/insertContact";
    public static final String URL_UPDATE_TOTALNUM = HOST + "speedList/updateTotalNumber";
    public static final String URL_UPDATE_KUAISUDINGDAN = HOST + "speedList/updateList";
    public static final String URL_QUERY_PAIDAN_BY_ID = HOST + "speedList/selectListBySid";
    public static final String URL_DELETE_PAIDAN_BY_ID = HOST + "speedList/deleteList";
    public static final String URL_INSERTREASON = HOST + "speedList/insertReason";
    public static final String URL_Query_BKTXYuE = HOST + "merAccount/selectOrderAmount";
    public static final String URL_SHEZHI_QIYEFXHONGBAO = HOST + "company/balanceToShareRed";
    public static final String URL_TIQU_QIYEFXHONGBAO = HOST + "company/shareRedWithdrawals";
    public static final String URL_HUOQU_QIYEFXHONGBAO = HOST + "company/shareRed";
    public static final String URL_SHEZHI_FXHONGBAO = HOST + "merAccount/shareRed";
    public static final String URL_TIQU_FXHONGBAO = HOST + "merAccount/shareRedWithdrawals";
    public static final String URL_HUOQU_FXHONGBAO = HOST + "user/shareRed";
    public static final String URL_ZJBAOZHJ = HOST + "user/margin";
    public static final String URL_TQBAOZHJ = HOST + "user/marginExtract";
    public static final String URL_ZHUANZHANG = HOST + "merAccount/changeAccount";
    public static final String URL_UPDATEZHHU = HOST + "merAccount/updateMerAccount";
    public static final String URL_INSERT_SHARECORD = HOST + "user/insertShareRedRecord";
    public static final String URL_QUERY_SHARECORD = HOST + "user/queryShareRedRecord";
    public static final String URL_INSERT_SEARCH = HOST + "data/insertSearchRecord";

    public static final String GET_USER_LOG_BY_DAY = HOST + "company/selectComUserLogByDate";
    public static final String GET_USER_LOG_BY_MONTH = HOST + "company/selectComUserByDate";
    public static final String UPDATE_COM_USER_LOGO = HOST + "company/updateComUserLogByDate";
    public static final String GET_COM_CLOCK_BY_TIME = HOST + "company/queryComClockByTime";
    public static final String SET_USER_BACK_LOG = HOST + "company/insertUserBackLog";

    //朋友圈接口
    // 服务器端
    public static final String URL_DELETE_PUBLISH = HOST + "dynamic/deleteDynamic";
    public static final String URL_QUERY_BZQUANXIAN = HOST + "dynamic/queryGameTimes";
    public static final String URL_QUERY_BZLIST = HOST + "dynamic/selectGameDynamic";
    public static final String URL_QUERY_ORDER = HOST + "dynamic/queryDynamicOrder?u_id=";
    public static final String URL_ADD_RED_XUQIU = HOST + "dynamic/forwardRed";
    public static final String URL_ADD_COUNT_ZHUFA = HOST + "dynamic/forwardTimes";
    public static final String URL_ADD_PINTUHB = HOST + "dynamic/dynamicGame";
    public static final String URL_ADD_PINGLUN = HOST + "dynamic/criticismTimes";
    public static final String URL_REDUCE_PINGLUN = HOST + "dynamic/criticismTime";
    public static final String URL_PUBLISH = HOST + "dynamic/createDynamic";
    public static final String URL_XIUGAI_PUBLISH = HOST + "dynamic/updateDynamicInfo";
    public static final String URL_PUBLISH_QUERY = HOST + "dynamic/queryDynamicByType";
    public static final String URL_QUERY_PUBLIC_PAIDAN = HOST + "dynamic/selectDynamicReceipt";
    public static final String URL_PUBLISHDETAIL_QUERY = HOST + "dynamic/queryDynamicBySeq";
    public static final String URL_TONGJI_XIAOLIANG = HOST + "dynamic/transUpdate";
    public static final String URL_TONGJI_LIULANCISHU = HOST + "dynamic/views";
    public static final String URL_JILU_LIST = HOST + "dynamic/insertDynamicCount";
    public static final String URL_SELECT_JILULIST = HOST + "dynamic/selectDynamicCount";
    public static final String URL_SEARCH_PUSH_DYNALIST = HOST + "dynamic/queryByLabel";
    public static final String URL_SEARCH_BAOJIA_FROM = HOST + "dynamic/queryOwnTaskDynamic";
    public static final String URL_PAIDAN = HOST + "speedList/selectReceipt";
    public static final String URL_SEARCH_BAOJIA_TO = HOST + "dynamic/queryOwnOrderTask";
    public static final String URL_QUERY_DEAL_CONTACT = HOST + "dynamic/selectDynamicDealContact";
    public static final String URL_UPDATE_DEAL_CONTACT = HOST + "dynamic/updateDynamicDealContact";
    public static final String JSON_KEY_NICK = "uName";
    public static final String JSON_KEY_HXID = "hxid";
    public static final String JSON_KEY_LOID = "uLoginId";
    public static final String JSON_KEY_AVATAR = "uImage";
    public static final String DIR_AVATAR = "/sdcard/zhengshier/";
    //进入用户详情页传递json字符串
    public static final String KEY_USER_INFO = "use２rInfo";
    //添加好友通知
    public static final String CMD_ADD_FRIEND = "ADD_FRIEND";
    public static final String CMD_AGREE_FRIEND = "AGREE_FRIEND";
    public static final String CMD_ADD_REASON = "ADD_REASON";
    public static final String FXLIVE_CHATROOM_ID = "218352836658856384";


}
