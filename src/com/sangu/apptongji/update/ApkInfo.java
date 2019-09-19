package com.sangu.apptongji.update;

/**
 * Created by Administrator on 2016-12-19.
 */

public class ApkInfo {
    private String versionnum;
    private String versiondesc;
    private String updateflg;
    private String apkurl;

    public ApkInfo() {
    }
    public String getVersionnum() {
        return versionnum;
    }

    public void setVersionnum(String versionnum) {
        this.versionnum = versionnum;
    }

    public String getVersiondesc() {
        return versiondesc;
    }

    public void setVersiondesc(String versiondesc) {
        this.versiondesc = versiondesc;
    }

    public String getUpdateflg() {
        return updateflg;
    }

    public void setUpdateflg(String updateflg) {
        this.updateflg = updateflg;
    }

    public String getApkurl() {
        return apkurl;
    }

    public void setApkurl(String apkurl) {
        this.apkurl = apkurl;
    }
}
