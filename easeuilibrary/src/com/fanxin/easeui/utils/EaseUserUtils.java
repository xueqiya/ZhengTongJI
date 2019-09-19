package com.fanxin.easeui.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanxin.easeui.EaseConstant;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.fanxin.easeui.controller.EaseUI;
import com.fanxin.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.fanxin.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath){
        File file=new File(filePath);
        long blockSize=0;
        try {
                blockSize = getFileSize(file);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小","获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception
    {
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        else{
            file.createNewFile();
            Log.e("获取文件大小","文件不存在!");
        }
        return size;
    }

    /**
     * 转换文件大小
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileS==0){
            return wrongSize;
        }
        fileSizeString = df.format((double) fileS / 1024);
        return fileSizeString;
    }

    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
        EaseUser user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            String avatarUrl=user.getAvatar();
            if(!avatarUrl.contains("http:")){
                avatarUrl= EaseConstant.URL_AVATAR+avatarUrl;
            }
            try {
                int avatarResId = Integer.parseInt(avatarUrl);
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(avatarUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.fx_default_useravatar).into(imageView);
            }
        }else{
            Glide.with(context).load(R.drawable.fx_default_useravatar).into(imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
            EaseUser user = getUserInfo(username);
            if(user != null && user.getNick() != null){
                textView.setText(user.getNick());
            }else{
                textView.setText(username);
            }
        }
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView, EMMessage msg) {
        EaseUser user = getUserInfo(username);
        if (user != null && user.getAvatar() != null) {
            String avatarUrl = user.getAvatar();
            if (!avatarUrl.contains("http:")) {
                avatarUrl = EaseConstant.URL_AVATAR + avatarUrl;

            }
            try {
                int avatarResId = Integer.parseInt(avatarUrl);
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(avatarUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.fx_default_useravatar).into(imageView);
            }
        } else {
            try {

                String string = msg.getStringAttribute(username);

                String[] images = string.split("\\|");

                if (images.length > 1){

                    Glide.with(context).load(EaseConstant.URL_AVATAR + images[0]).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.fx_default_useravatar).into(imageView);
                }

//                String userInfo = msg.getStringAttribute("userInfo");
//                JSONObject jsonObject = JSONObject.parseObject(userInfo);
//                String avatarUrl = jsonObject.getString("avatar");
//                if (!avatarUrl.contains("http:")) {
//                    avatarUrl = EaseConstant.URL_AVATAR + avatarUrl;
//                }
//                Glide.with(context).load(avatarUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.fx_default_useravatar).into(imageView);
            } catch (HyphenateException e) {
                Glide.with(context).load(R.drawable.fx_default_useravatar).into(imageView);
                e.printStackTrace();
            } catch (JSONException e) {

                Glide.with(context).load(R.drawable.fx_default_useravatar).into(imageView);
                e.printStackTrace();
            }


        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView, EMMessage msg) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNick() != null) {
                textView.setText(user.getNick());
            } else {

                try {
                    String userInfo = msg.getStringAttribute("userInfo");
                    JSONObject jsonObject = JSONObject.parseObject(userInfo);
                    String nick = jsonObject.getString("nick");
                    textView.setText(nick);
                } catch (HyphenateException e) {
                    textView.setText(username);
                    e.printStackTrace();
                } catch (JSONException e) {

                    textView.setText(username);
                    e.printStackTrace();
                }

            }
        }
    }

}
