package com.sangu.apptongji.main.alluser.model;

import com.sangu.apptongji.main.alluser.presenter.OnFindListener;

/**
 * Created by user on 2016/8/29.
 */

public interface IFindModel extends IModel{
    void getUserList(String u_id,String str, String overall_flg ,String lng, String lat, String zy, String bZj,String sex,String ageStart,String ageEnd,String name,String comName,boolean gongsi, boolean jingyan, boolean hongbao,OnFindListener onFindListener);
}
