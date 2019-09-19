package com.sangu.apptongji.main.alluser.presenter;

/**
 * Created by user on 2016/8/29.
 */

public interface IFindPresenter extends IPresenter{
    void loadUserList(String u_id,String str,String overall_flg,String lng,String lat,String zy,String bZj,String sex,String ageStart,String ageEnd,String name,String comName,boolean gongsi,boolean jingyan,boolean hongbao);

}
