package com.sangu.apptongji.main.alluser.presenter;

import com.sangu.apptongji.main.alluser.entity.UserAll;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016-11-14.
 */

public interface OnGrMemListener {
    void onSuccess(List<UserAll> userAlls, JSONObject obj, boolean hasMore);
    void onStart();
    void onFailed();
    void onFinish();
}
