package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.MerDetail;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Administrator on 2017-04-27.
 */

public interface IMerDetailView extends IView {
    void updateUserList(List<MerDetail> merDetails, JSONArray array,String size,String income,String expenditure, boolean hasMore);
    void showLoading();
    void hideLoading();
    void showError();
}
