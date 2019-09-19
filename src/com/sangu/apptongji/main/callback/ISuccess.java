package com.sangu.apptongji.main.callback;

import com.sangu.apptongji.main.address.KyqInfo;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Administrator on 2017-12-28.
 */

public interface ISuccess {
    void onSuccess(List<KyqInfo> listsKyq,List<String> list_selected,JSONArray arrayKyq,double per);
}
