package com.sangu.apptongji.main.chatheadimage;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-08-04.
 */

public class UserApiModel implements Serializable {
    // 记得导入ormlite库
    @DatabaseField(generatedId=true)
    private int RecordId;

    @DatabaseField

    public long Id;

    @DatabaseField
    public String Username;

    @DatabaseField
    public String HeadImg;

    @DatabaseField
    public String EaseMobUserName;


    public UserApiModel(){
        Id = 0;
        Username = "";
        HeadImg = "";
        EaseMobUserName = "";
    }

//    public static UserApiModel parse(String jsonString) {
//        Gson gson = new Gson();
//        UserApiModel obj = gson.fromJson(jsonString, UserApiModel.class);
//        return obj;
//    }

    public int getRecordId() {
        return RecordId;
    }

    public void setRecordId(int recordId) {
        RecordId = recordId;
    }


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getHeadImg() {
        return HeadImg;
    }

    public void setHeadImg(String headImg) {
        HeadImg = headImg;
    }

    public String getEaseMobUserName() {
        return EaseMobUserName;
    }

    public void setEaseMobUserName(String easeMobUserName) {
        EaseMobUserName = easeMobUserName;
    }

}
