package com.sangu.apptongji.main.alluser.presenter;

import com.sangu.apptongji.main.alluser.entity.MerDetail;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Administrator on 2016-11-14.
 */

public interface OnMerListener {
    void onSuccess(List<MerDetail> merDetails, JSONArray array,String size,String income,String expenditure, boolean hasMore);
    void onStart();
    void onFailed();
    void onFinish();
}
