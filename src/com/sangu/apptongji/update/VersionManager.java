package com.sangu.apptongji.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by Administrator on 2016-12-12.
 */

public class VersionManager {
    private Context context;
    private AppVersion appVersion;
    private DownloadManager downloadManager;
    private SharedPreferences prefs;
    private static final String DL_ID = "update_app_id";
    // 文件在sd卡的真实目录
    private String apkAbsolutePath;

    public void setOnUpdateListener(OnUpdateListener listener) {
        this.listener = listener;
    }

    private OnUpdateListener listener;

    public void setAppVersion(AppVersion appVersion) {
        this.appVersion = appVersion;
    }

    private static VersionManager instance = null;

    public static VersionManager getInstance(Context context,
                                             AppVersion appVersion) {
        if (instance == null) {
            instance = new VersionManager(context, appVersion);
        }
        return instance;
    }

    private VersionManager(Context context, AppVersion appVersion) {
        this.appVersion = appVersion;
        this.context = context;
        apkAbsolutePath = getSDPath() + appVersion.getFilePath() + "/"
                + appVersion.getFileName();
        downloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (isFileExist(apkAbsolutePath)) {
            File file = new File(apkAbsolutePath);
            Uri apkUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                apkUri = FileProvider.getUriForFile(context, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
                context.grantUriPermission(context.getPackageName(), apkUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                apkUri = Uri.fromFile(file);
            }
            file.delete();
        }
    }

    public void checkUpdateInfo() {
        int localVersion = getVersionCode(context);

        if (appVersion.getUpdateType() != null && appVersion.getUpdateType().equals("download")){

            listener.hasNewVersion(true);

        }else {

            // 这里只要两个版本不一致就更新，不考虑版本降级的情况...
            if (Integer.valueOf(appVersion.getVersionCode()) > localVersion) {
                listener.hasNewVersion(true);
            } else {
                listener.hasNewVersion(false);
            }

        }

    }

    /**
     * 获取版本号(内部识别号)
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 下载文件
     */
    public void downLoad() {
        if (!isSdCardExist()) {
            listener.onError("文件无法下载请检测您的sd卡");
            return;
        }
        if (isFileExist(apkAbsolutePath)) {
            // 如果文件已经存在则安装app
            installApk();
        }
        // 开始下载
        Uri resource = Uri.parse(appVersion.getApkUrl());
        DownloadManager.Request request = new DownloadManager.Request(resource);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        // 设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
                .getFileExtensionFromUrl(appVersion.getApkUrl()));
        request.setMimeType(mimeString);
        // 在通知栏中显示
        request.setNotificationVisibility(View.VISIBLE);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(
                "/" + appVersion.getFilePath() + "/", appVersion.getFileName());

        if (appVersion.getUpdateType() != null && appVersion.getUpdateType().equals("download")){
            request.setTitle(appVersion.getAppName());
        }else {
            request.setTitle("正事多版本更新");
        }

        long id = downloadManager.enqueue(request);
        // 保存id
        prefs.edit().putLong(DL_ID, id).commit();
        // 注册广播监听下载
        context.registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void installApk() {
        File file = new File(apkAbsolutePath);
        Uri apkUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkUri = FileProvider.getUriForFile(context, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            context.grantUriPermission(context.getPackageName(), apkUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(install);
        } else {
            apkUri = Uri.fromFile(file);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
    }

    private boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    private String getSDPath() {
        if (isSdCardExist()) {
            return Environment.getExternalStorageDirectory() + "/";
        }
        return null;
    }

    private boolean isFileExist(String path) {
        return new File(path).exists();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(prefs.getLong(DL_ID, 0));
            Cursor c = downloadManager.query(query);
            if (c.moveToFirst()) {
                int status = c.getInt(c
                        .getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_RUNNING:
                        // 正在下载，不做任何事情
                        listener.onDownloading();
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        // 下载完成首先取消注册广播，然后安装app
                        listener.onSuccess();
                        context.unregisterReceiver(receiver);
                        installApk();
                        break;
                    case DownloadManager.STATUS_FAILED:
                        // 下载失败 清除已下载的内容，重新下载
                        context.unregisterReceiver(receiver);
                        listener.onError("下载失败，请重试");
                        downloadManager.remove(prefs.getLong(DL_ID, 0));
                        prefs.edit().clear().commit();
                        break;
                }
            }
        }
    };

    /**
     * app版本信息
     *
     * @author sunger
     *
     */
    public static class AppVersion {
        // apk下载url
        private String apkUrl;
        // apk最新版本
        private String versionCode;

        private String updateType;//记录是更新正事多还是下载推广app

        public String getUpdateType() {
            return updateType;
        }

        public void setUpdateType(String updateType) {
            this.updateType = updateType;
        }

        private String appName;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        private String fileName;

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        /**
         * 文件在sd卡的相对路径
         */
        private String filePath;

        public String getApkUrl() {
            return apkUrl;
        }
        public void setApkUrl(String apkUrl) {
            this.apkUrl = apkUrl;
        }
    }

    public interface OnUpdateListener {
        /**
         * 是否有新版本更新，如果为true这里开始调用
         *
         * @param has
         */
        public void hasNewVersion(boolean has);
        /**
         * 正在开始下载
         */
        public void onDownloading();
        /**
         * 下载完成，并且安装成功
         */
        public void onSuccess();
        /**
         * 更新失败
         *
         * @param msg
         *            失败的消息
         */
        public void onError(String msg);
    }
}
