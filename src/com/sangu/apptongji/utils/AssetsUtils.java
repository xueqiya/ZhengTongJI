package com.sangu.apptongji.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Administrator on 2018-01-26.
 */

public class AssetsUtils {
    public static List<String> getKeyWordFilter(Context context) {
        try {
            InputStream is = context.getAssets().open("keyword"+".txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer, "utf-8");
            return Arrays.asList(text.split(","));
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
            return null;
        }
    }
}
