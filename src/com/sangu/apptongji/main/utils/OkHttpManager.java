package com.sangu.apptongji.main.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by huangfangyi/qq84543217 on 2016/6/30.
 */
public class OkHttpManager {
    public static Context context;
    public static OkHttpManager serverTask;
    private static OkHttpClient okHttpClient;
    private static final int RESULT_ERROR = 1000;
    private static final int RESULT_SUCESS = 2000;
    private HttpCallBack httpCallBack;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int reusltCode = msg.what;
            switch (reusltCode) {
                case RESULT_ERROR:
                    httpCallBack.onFailure((String) msg.obj);
                    Toast.makeText(context, "网络无响应", Toast.LENGTH_SHORT).show();
//                    Log.d("result----->", (String) msg.obj);
                    break;
                case RESULT_SUCESS:
                    String result = (String) msg.obj;
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        httpCallBack.onResponse(jsonObject);
                    } catch (JSONException e) {
                        httpCallBack.onFailure((String) msg.obj);
//                        Toast.makeText(context, "响应数据解析错误", Toast.LENGTH_SHORT).show();
                    }
//                    Log.d("result----->", result);
                    break;
            }

        }
    };

    public OkHttpManager(Context context) {
        this.context = context;
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120000L, TimeUnit.MILLISECONDS)
                .readTimeout(120000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
    }

    public static synchronized void init(Context context) {
        if (serverTask == null) {
            serverTask = new OkHttpManager(context);
        }
    }

    public static OkHttpManager getInstance() {
        if (serverTask == null) {
            throw new RuntimeException("please init first!");
        }
        return serverTask;
    }

    public static OkHttpManager getInstance2() {
        return new OkHttpManager(context);
    }

    //纯粹键值对post请求
    public void post(List<Param> params, String url, HttpCallBack httpCallBack) {
//        Log.d("url----->>", url);
        this.httpCallBack = httpCallBack;
        FormBody.Builder bodyBulder = new FormBody.Builder();
        for (Param param : params) {
            bodyBulder.add(param.getKey(), param.getValue());
//            Log.d("param.getKey()----->>", param.getKey());
//            Log.d("param.getValue()----->>", param.getValue());
        }
        RequestBody requestBody = bodyBulder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        startRequest(request);
    }

    //键值对+文件 post请求
    public void post(String str,List<Param> params, List<File> files, String url, HttpCallBack httpCallBack) {
//        Log.d("url----->>", url);
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue()));
            /*Log.d("param.getKey()----->>", param.getKey());
            Log.d("param.getValue()----->>", param.getValue());*/
        }
        for (File file : files) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.str----->>", str);
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        startRequest(request);
    }
    public void post(List<Param> params, List<File> files1,List<File> files, String url, HttpCallBack httpCallBack) {
//        Log.d("url----->>", url);
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue()));
//            Log.d("param.getKey()----->>", param.getKey());
//            Log.d("param.getValue()----->>", param.getValue());
        }
        for (File file : files) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + "merSign" + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        for (File file : files1) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + "userSign" + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        startRequest(request);
    }
    public void postsfz(List<Param> params, List<File> files1,List<File> files, String url, HttpCallBack httpCallBack) {
//        Log.d("url----->>", url);
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue()));
//            Log.d("param.getKey()----->>", param.getKey());
//            Log.d("param.getValue()----->>", param.getValue());
        }
        for (File file : files) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + "businessPicture" + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        for (File file : files1) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + "idPhoto" + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        startRequest(request);
    }
    public void postQuickPaidanWithVoice(List<Param> params,String str,List<File> files,String url, HttpCallBack httpCallBack) {
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue()));
//            Log.d("param.getKey()----->>", param.getKey());
//            Log.d("param.getValue()----->>", param.getValue());
        }
        for (File file : files) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        startRequest(request);
    }
    public void postTfile(List<Param> params,String str,List<File> files,String str1,List<File> files1,String str2,List<File> files2, String url, HttpCallBack httpCallBack) {
//        Log.d("url----->>", url);
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue()));
//            Log.d("param.getKey()----->>", param.getKey());
//            Log.d("param.getValue()----->>", param.getValue());
        }
        for (File file : files2) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str2 + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        for (File file : files1) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str1 + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        for (File file : files) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        startRequest(request);
    }
    public void postTfile(List<Param> params,
                          String str,List<File> files,String str1,List<File> files1,String str2,List<File> files2, String str3,List<File> files3,
                          String url, HttpCallBack httpCallBack) {
//        Log.d("url----->>", url);
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue()));
//            Log.d("param.getKey()----->>", param.getKey());
//            Log.d("param.getValue()----->>", param.getValue());
        }
        for (File file : files2) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str2 + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        for (File file : files1) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str1 + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        for (File file : files) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        for (File file : files3) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str3 + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        startRequest(request);
    }

    public void postThfile(String str,List<File> files,List<Param> params,String str1,List<File> files1,String str2,List<File> files2, String url, HttpCallBack httpCallBack) {
//        Log.e("url----->>", url);
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue()));
//            Log.e("param.getKey()----->>", param.getKey());
//            Log.e("param.getValue()----->>", param.getValue());
        }
        for (int i=0;i<files2.size();i++){
            File file2 = files2.get(i);
            if (file2 != null && file2.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str2 + String.valueOf(i+1) + "\"; filename=\"" + file2.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file2.getName())), file2));
//                Log.e("file2.str----->>", str2 + String.valueOf(i+1));
//                Log.e("file2.getName()----->>", file2.getName());
            }
        }
        for (int i=0;i<files1.size();i++){
            File file1 = files1.get(i);
            if (file1 != null && file1.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str1 + String.valueOf(i+1) + "\"; filename=\"" + file1.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file1.getName())), file1));
//                Log.e("file1.str----->>", str1 + String.valueOf(i+1));
//                Log.e("file1.getName()----->>", file1.getName());
            }
        }
        for (File file : files) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.e("file.str----->>", str);
//                Log.e("file.getName()----->>", file.getName());
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        startRequest(request);
    }

    public void postTfile2(String str,List<File> files,List<Param> params,String str1,List<File> files1,String str2,List<File> files2, String url, HttpCallBack httpCallBack) {
//        Log.d("url----->>", url);
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue()));
//            Log.d("param.getKey()----->>", param.getKey());
//            Log.d("param.getValue()----->>", param.getValue());
        }
        for (File file : files2) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str2 + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file.str2----->>", str2);
//                Log.d("file.getName()----->>", file.getName());
            }

        }
        for (File file : files1) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str1 + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file1.str----->>", str1);
//                Log.d("file1.getName()----->>", file.getName());
            }

        }
        for (File file : files) {
            if (file != null && file.exists()) {
                //TODO-本项目固化文件的键名为“file”
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + str + "\"; filename=\"" + file.getName()+ "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
//                Log.d("file2.str----->>", str);
//                Log.d("file2.getName()----->>", file.getName());
            }

        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        startRequest(request);
    }

    //键值对+文件 post请求
    public void posts(List<Param> params, String key, ArrayList<String> images, String url, HttpCallBack httpCallBack) {
//        Log.d("url----->>", url);
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        int num = images.size();
        String imageStr="0";
        for (int i = 0; i < num; i++) {
            String filename = images.get(i);
            File file = new File(filename);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\""),
                    RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
            if (i == 0) {
                imageStr = filename;
            } else {
                imageStr = imageStr + "split" + filename;
                Log.e("imageStr---->>>>>>.",imageStr);
            }
        }
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue()));
//            Log.d("param.getKey()----->>", param.getKey());
//            Log.d("param.getValue()----->>", param.getValue());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        startRequest(request);

    }

    //键值对+文件 post请求
    public void posts2(List<Param> params, String key, ArrayList<String> images,String key2,ArrayList<String> images2
            ,String str1,String files1,String str2,String files2,String str3,String files3,String str4,String files4,String str5,String files5
            ,String str6,String files6,String str7,String files7,String str8,String files8
            , String url, HttpCallBack httpCallBack) {
//        Log.e("url----->>", url);
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        int num = images.size();
        String imageStr="0";
        int num2 = images2.size();
        String imageStr2="0";
        for (int i = 0; i < num; i++) {
            String filename = images.get(i);
            File file = new File(filename);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\""),
                    RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
            if (i == 0) {
                imageStr = filename;
            } else {
                imageStr = imageStr + "split" + filename;
                Log.e("imageStr---->>>>>>.",imageStr);
            }
        }
        for (int i = 0; i < num2; i++) {
            String filename = images2.get(i);
            File file = new File(filename);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + key2 + "\"; filename=\"" + file.getName() + "\""),
                    RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
            if (i == 0) {
                imageStr2 = filename;
            } else {
                imageStr2 = imageStr2 + "split" + filename;
                Log.e("imageStr---->>>>>>.",imageStr2);
            }
        }
        if (files1 != null && new File(files1).exists()) {
            File file1 = new File(files1);
            //TODO-本项目固化文件的键名为“file”
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + str1 + "\"; filename=\"" + file1.getName()+ "\""),
                    RequestBody.create(MediaType.parse(guessMimeType(file1.getName())), file1));
                Log.e("file1.str----->>", str1);
                Log.e("file1.getName()----->>", file1.getName());
        }
        if (files2 != null && new File(files2).exists()) {
            //TODO-本项目固化文件的键名为“file”
            File file2 = new File(files2);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + str2 + "\"; filename=\"" + file2.getName()+ "\""),
                    RequestBody.create(MediaType.parse(guessMimeType(file2.getName())), file2));
//                Log.e("file2.str----->>", str2);
//                Log.e("file2.getName()----->>", file2.getName());
        }
        if (files3 != null && new File(files3).exists()) {
            //TODO-本项目固化文件的键名为“file”
            File file3 = new File(files3);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + str3 + "\"; filename=\"" + file3.getName()+ "\""),
                    RequestBody.create(MediaType.parse(guessMimeType(file3.getName())), file3));
//                Log.e("file1.str----->>", str1 + String.valueOf(i+1));
//                Log.e("file1.getName()----->>", file1.getName());
        }
        if (files4 != null && new File(files4).exists()) {
            //TODO-本项目固化文件的键名为“file”
            File file4 = new File(files4);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + str4 + "\"; filename=\"" + file4.getName()+ "\""),
                    RequestBody.create(MediaType.parse(guessMimeType(file4.getName())), file4));
//                Log.e("file1.str----->>", str1 + String.valueOf(i+1));
//                Log.e("file1.getName()----->>", file1.getName());
        }
        if (files5 != null && new File(files5).exists()) {
            //TODO-本项目固化文件的键名为“file”
            File file5 = new File(files5);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + str5 + "\"; filename=\"" + file5.getName()+ "\""),
                    RequestBody.create(MediaType.parse(guessMimeType(file5.getName())), file5));
//                Log.e("file1.str----->>", str1 + String.valueOf(i+1));
//                Log.e("file1.getName()----->>", file1.getName());
        }
        if (files6 != null && new File(files6).exists()) {
            //TODO-本项目固化文件的键名为“file”
            File file6 = new File(files6);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + str6 + "\"; filename=\"" + file6.getName()+ "\""),
                    RequestBody.create(MediaType.parse(guessMimeType(file6.getName())), file6));
//                Log.e("file1.str----->>", str1 + String.valueOf(i+1));
//                Log.e("file1.getName()----->>", file1.getName());
        }
        if (files7 != null && new File(files7).exists()) {
            //TODO-本项目固化文件的键名为“file”
            File file7 = new File(files7);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + str7 + "\"; filename=\"" + file7.getName()+ "\""),
                    RequestBody.create(MediaType.parse(guessMimeType(file7.getName())), file7));
//                Log.e("file1.str----->>", str1 + String.valueOf(i+1));
//                Log.e("file1.getName()----->>", file1.getName());
        }
        if (files8 != null && new File(files8).exists()) {
            //TODO-本项目固化文件的键名为“file”
            File file8 = new File(files8);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + str8 + "\"; filename=\"" + file8.getName()+ "\""),
                    RequestBody.create(MediaType.parse(guessMimeType(file8.getName())), file8));
//                Log.e("file1.str----->>", str1 + String.valueOf(i+1));
//                Log.e("file1.getName()----->>", file1.getName());
        }
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue()));
            Log.e("param.getKey()----->>", param.getKey());
            Log.e("param.getValue()----->>", param.getValue());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        startRequest(request);

    }

    public void postMoment(List<Param> params, ArrayList<String> images, String url, HttpCallBack httpCallBack) {
//        Log.d("url----->>", url);
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        int num = images.size();
        String imageStr="0";
        for (int i = 0; i < num; i++) {
            String filename = images.get(i);
            File file = new File(filename);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + "image" + "\"; filename=\"" + file.getName() + "\""),
                    RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
            if (i == 0) {
                imageStr = filename;
            } else {
                imageStr = imageStr + "split" + filename;
                Log.e("imageStr---->>>>>>.", imageStr);
            }
        }
        for (Param param : params) {
            Log.d("chen", "发送Key " + param.getKey() + "    值value  " + param.getValue());
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue()));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        startRequest(request);

    }

    //键值对+文件 post请求
    public void postMoments(String str,List<Param> params, ArrayList<String> images, String url, HttpCallBack httpCallBack) {
//        Log.d("url----->>", url);
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        int num = images.size();
        String imageStr="0";
        if (str!=null&&!"".equals(str)) {
            for (int i = 0; i < num; i++) {
                String filename = images.get(i);
                File file = new File(filename);
                if (file != null && file.exists()) {
                    builder.addPart(Headers.of("Content-Disposition",
                            "form-data; name=\"" + str + "\"; filename=\"" + file.getName() + "\""),
                            RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
                    if (i == 0) {
                        imageStr = filename;
                    } else {
                        imageStr = imageStr + "split" + filename;
//                Log.e("imageStr---->>>>>>.", imageStr);
                    }
                }
            }
        }
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue() == null ? "":param.getValue()));
//            Log.d("param.getKey()----->>", param.getKey());
//            Log.d("param.getValue()----->>", param.getValue());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        startRequest(request);
    }

    //键值对+文件 post请求
    public void postUpdateCompany(List<Param> params,String key, String path, String url, HttpCallBack httpCallBack) {
        this.httpCallBack = httpCallBack;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        String imageStr="0";
        if (path!=null&&!"".equals(path)) {
                File file = new File(path);
                if (file != null && file.exists()) {
                    builder.addPart(Headers.of("Content-Disposition",
                            "form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\""),
                            RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
                }
        }
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.getKey() + "\""), RequestBody.create(MediaType.parse(guessMimeType(param.getKey())), param.getValue() == null ? "":param.getValue()));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        startRequest(request);
    }

    private void startRequest(Request request) {

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = handler.obtainMessage();
                message.what = RESULT_ERROR;
                message.obj =  e.toString();
                message.sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = handler.obtainMessage();
                message.what = RESULT_SUCESS;
                message.obj = response.body().string();
                message.sendToTarget();
            }
        });
    }

    public interface HttpCallBack {
        void onResponse(JSONObject jsonObject);

        void onFailure(String errorMsg);
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
