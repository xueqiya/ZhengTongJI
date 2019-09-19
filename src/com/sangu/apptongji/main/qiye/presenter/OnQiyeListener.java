package com.sangu.apptongji.main.qiye.presenter;

/**
 * Created by Administrator on 2016-12-28.
 */

public interface OnQiyeListener {
    void onSuccess(Object obj,boolean hasMore);
    void onStart();
    void onFailed();
    void onFinish();
}
