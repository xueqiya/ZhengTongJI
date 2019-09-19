package com.sangu.apptongji.main.alluser.presenter;

import com.sangu.apptongji.main.alluser.entity.ZhuFaDetail;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016-11-14.
 */

public interface OnZhuFaListener {
    void onSuccess(List<ZhuFaDetail> zhuFaDetails, JSONObject object,String size, boolean hasMore);
    void onStart();
    void onFailed();
    void onFinish();
}
