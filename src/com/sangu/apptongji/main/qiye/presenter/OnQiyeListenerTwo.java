package com.sangu.apptongji.main.qiye.presenter;

/**
 * Created by Administrator on 2016-12-28.
 */

public interface OnQiyeListenerTwo {
    void onSuccess(Object obj, String total ,boolean hasMore);
    void onStart();
    void onFailed();
    void onFinish();
}
