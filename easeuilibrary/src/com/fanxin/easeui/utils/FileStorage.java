package com.fanxin.easeui.utils;

import android.os.Environment;

import java.io.File;
import java.util.UUID;

/**
 * Created by Administrator on 2017-06-19.
 */

public class FileStorage {
    private File cropIconDir;

    public FileStorage(String childPath) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File external = Environment.getExternalStorageDirectory();
            String rootDir = "/" + "zhengshier";
            cropIconDir = new File(external, rootDir + "/"+childPath);
            if (!cropIconDir.exists()) {
                cropIconDir.mkdirs();
            }
        }
    }

    public File createCropFile(String fileName,String childPath) {
        if (cropIconDir != null) {
            if (fileName==null) {
                fileName = UUID.randomUUID().toString() + ".png";
            }
        }
        return new File(cropIconDir, fileName);
    }

}
