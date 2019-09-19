package com.sangu.apptongji.main.chatheadimage;

import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2017-08-04.
 */

public class UserInfoCacheSvc {
    public static List<UserApiModel> getAllList(){
        Dao<UserApiModel, Integer> daoScene = SqliteHelper.getInstance().getUserDao();
        try {
            List<UserApiModel> list = daoScene.queryBuilder().query();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static UserApiModel getByChatUserName(String chatUserName){
        Dao<UserApiModel, Integer> dao = SqliteHelper.getInstance().getUserDao();
        try {
            UserApiModel model = dao.queryBuilder().where().eq("EaseMobUserName", chatUserName).queryForFirst();
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static UserApiModel getById(long id){
        Dao<UserApiModel, Integer> dao = SqliteHelper.getInstance().getUserDao();
        try {
            UserApiModel model = dao.queryBuilder().where().eq("Id", id).queryForFirst();
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean createOrUpdate(String chatUserName, String userNickName, String avatarUrl){
        try {
            Dao<UserApiModel, Integer> dao = SqliteHelper.getInstance().getUserDao();

            UserApiModel user = getByChatUserName(chatUserName);

            int changedLines = 0;
            if (user == null){
                user = new UserApiModel();
                user.setUsername(userNickName);
                user.setHeadImg(avatarUrl);
                user.setEaseMobUserName(chatUserName);

                changedLines = dao.create(user);
            }else {
                user.setUsername(userNickName);
                user.setHeadImg(avatarUrl);
                user.setEaseMobUserName(chatUserName);

                changedLines = dao.update(user);
            }

            if(changedLines > 0){
                Log.i("UserInfoCacheSvc", "操作成功~");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("UserInfoCacheSvc", "操作异常~");
        }

        return false;
    }

    public static boolean createOrUpdate(UserApiModel model){

        if(model == null) return false;

        try {
            Dao<UserApiModel, Integer> dao = SqliteHelper.getInstance().getUserDao();

            UserApiModel user = getById(model.Id);

            if (!StringUtils.isNullOrEmpty(model.getHeadImg())){
                String fullPath = model.getHeadImg();
//特别注意：这里用是图片的完整链接地址，如果要取缩略图，需要服务端配合；

                model.setHeadImg(fullPath);
            }
            int changedLines = 0;
            if (user == null){
                changedLines = dao.create(model);
            }else {
                model.setRecordId(user.getRecordId());
                changedLines = dao.update(model);
            }

            if(changedLines > 0){
                Log.i("UserInfoCacheSvc", "操作成功~");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("UserInfoCacheSvc", "操作异常~");
        }

        return false;
    }
}
