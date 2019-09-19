package com.sangu.apptongji.main.qiye.view;

import com.sangu.apptongji.main.qiye.entity.QiYeInfo;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016-12-28.
 */

public interface IQiYeDetailView extends IView {
    void updateQiyeInfo(QiYeInfo qiYeInfo) throws UnsupportedEncodingException;
    void showLoading();
    void hideLoading();
    void showError();
}
