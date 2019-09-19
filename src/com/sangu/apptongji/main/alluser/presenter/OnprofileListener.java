package com.sangu.apptongji.main.alluser.presenter;

import com.sangu.apptongji.main.alluser.entity.Userful;

/**
 * Created by Administrator on 2016-11-14.
 */

public interface OnprofileListener {
    void onSuccess(Userful user);
    void onStart();
    void onFailed();
    void onFinish();
}
