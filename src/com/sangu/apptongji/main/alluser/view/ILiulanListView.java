package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.LiuLanDetail;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-08-30.
 */

public interface ILiulanListView extends IView {
    void updateLiuLanList(List<LiuLanDetail> liuLanDetails, JSONObject object,String size, boolean hasMore);
}
