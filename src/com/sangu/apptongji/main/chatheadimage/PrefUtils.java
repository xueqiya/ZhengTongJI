package com.sangu.apptongji.main.chatheadimage;

import com.sangu.apptongji.DemoApplication;

/**
 * Created by Administrator on 2017-08-04.
 */

public class PrefUtils extends PreferencesUtils {

    // 这里的MainApplication、BaseApplication替换成自己项目的Application子类实例即可
    public static final DemoApplication mAppContext = (DemoApplication)DemoApplication.getInstance();

    /*************************** 用户登录信息 ***********************************************/

    public static void setUserId(long userId) {
        PreferencesUtils.putLong(mAppContext, SharePrefConstant.UserId,
                userId);
    }

    public static long getUserId() {
        return PrefUtils.getLong(mAppContext, SharePrefConstant.UserId);
    }

    public static void setUserPic(String UserPic) {
        PrefUtils.putString(mAppContext, SharePrefConstant.UserPic,
                UserPic);
    }

    public static String getUserPic() {
        return PrefUtils.getString(mAppContext, SharePrefConstant.UserPic);
    }

    public static void setUserName(String name) {
        PrefUtils.putString(mAppContext, SharePrefConstant.UserName, name);
    }

    public static String getUserName() {
        return PrefUtils
                .getString(mAppContext, SharePrefConstant.UserName);
    }

    public static void setUserChatId(String chatId) {
        PrefUtils.putString(mAppContext, SharePrefConstant.UserChatId,
                chatId);
    }

    public static String getUserChatId() {
        return PrefUtils.getString(mAppContext,
                SharePrefConstant.UserChatId);
    }

    /**
     * 用户是否已经登录
     * @return 如果已经登录，则返回true
     */
    public static boolean isLogin() {
        long userId = getUserId();
        String userName = getUserName();
        return userId > 0 && !StringUtils.isNullOrEmpty(userName);
    }

    public static void clearUserInfo(){
        setUserId(0);
        setUserPic("");
        setUserName("");
        setUserChatId("");
    }
    /*************************** 用户登录信息************end ***********************************/
}
