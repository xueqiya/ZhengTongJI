package com.sangu.apptongji.main.alluser.view;

import com.sangu.apptongji.main.alluser.entity.ZhuFaDetail;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-08-30.
 */

public interface IZhuanFaListView extends IView {
    void updateZhuFaList(List<ZhuFaDetail> zhuFaDetails, JSONObject object, String size,boolean hasMore);
}
