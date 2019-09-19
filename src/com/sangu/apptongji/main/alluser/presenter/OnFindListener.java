package com.sangu.apptongji.main.alluser.presenter;

import com.sangu.apptongji.main.alluser.entity.UserAll;

import java.util.List;

/**
 * Created by Administrator on 2016-11-14.
 */

public interface OnFindListener {
    void onSuccess(List<UserAll> users,boolean hasMore);
    void onStart();
    void onFailed(String msg);
    void onFinish();
}
