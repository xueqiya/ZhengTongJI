package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.UserAll;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-11-14.
 */

public interface IGrMemView extends IView {
    void updataGroupList(List<UserAll> userAlls, JSONObject groupInfo, boolean hasMore);
    void showproLoading();
    void hideproLoading();
    void showproError();
}
