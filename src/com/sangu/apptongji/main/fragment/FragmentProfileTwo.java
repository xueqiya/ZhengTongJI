package com.sangu.apptongji.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.activity.LoginActivity;
import com.sangu.apptongji.main.activity.RegisterOneActivity;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2016-12-07.
 */

public class FragmentProfileTwo extends Fragment {
    private Button register,login;
    private ImageView iv_qq;
    private ImageView iv_wx;
    private ImageView iv_wb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_two, container, false);
        register = (Button) v.findViewById(R.id.register);
        login = (Button) v.findViewById(R.id.login);
        iv_qq = (ImageView) v.findViewById(R.id.iv_qq);
        iv_wx = (ImageView) v.findViewById(R.id.iv_wx);
        iv_wb = (ImageView) v.findViewById(R.id.iv_wb);
        iv_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Platform qzone = ShareSDK.getPlatform(QQ.NAME);
//回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
                qzone.setPlatformActionListener(new PlatformActionListener() {

                    @Override
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getActivity(),"授权失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getActivity(),"授权成功",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), RegisterOneActivity.class));
                    }

                    @Override
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getActivity(),"您取消了授权",Toast.LENGTH_SHORT).show();
                    }
                });
//authorize与showUser单独调用一个即可
                qzone.authorize();//单独授权,OnComplete返回的hashmap是空的
            }
        });
        iv_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
//回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
                weixin.setPlatformActionListener(new PlatformActionListener() {

                    @Override
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getActivity(),"授权失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getActivity(),"授权成功",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), RegisterOneActivity.class));
                    }

                    @Override
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getActivity(),"您取消了授权",Toast.LENGTH_SHORT).show();
                    }
                });
//authorize与showUser单独调用一个即可
                weixin.authorize();//单独授权,OnComplete返回的hashmap是空的
            }
        });
        iv_wb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
                weibo.setPlatformActionListener(new PlatformActionListener() {

                    @Override
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getActivity(),"授权失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getActivity(),"授权成功",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), RegisterOneActivity.class));
                    }

                    @Override
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getActivity(),"您取消了授权",Toast.LENGTH_SHORT).show();
                    }
                });
//authorize与showUser单独调用一个即可
                weibo.authorize();//单独授权,OnComplete返回的hashmap是空的
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterOneActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return v;
    }
}
