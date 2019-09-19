package com.sangu.apptongji.main.utils;

import android.content.Context;

import com.sangu.apptongji.main.address.GetPhoneNumberFromMobile;
import com.sangu.apptongji.main.address.PhoneInfo;
import com.sangu.apptongji.main.callback.IError;
import com.sangu.apptongji.main.callback.IFailure;
import com.sangu.apptongji.main.callback.ISuccess;

import java.util.List;

/**
 * Created by Administrator on 2017-12-28.
 */

public class SyAddressBookBuilder {
    private ISuccess mISuccess = null;
    private IFailure mIFailure = null;
    private IError mIError = null;
    private Context mContext = null;
    private static List<PhoneInfo> lists;

    SyAddressBookBuilder() {
    }

    public final SyAddressBookBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    public final SyAddressBookBuilder failure(IFailure iFailure) {
        this.mIFailure = iFailure;
        return this;
    }

    public final SyAddressBookBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }

    public final SyAddressBookBuilder loader(Context context) {
        this.mContext = context;
        lists = GetPhoneNumberFromMobile.getPhoneNumberFromMobile(mContext);
        return this;
    }

    public final SyAddressBookUtil build() {
        return new SyAddressBookUtil(lists,mISuccess, mIFailure, mIError,mContext);
    }

}
