package com.sangu.apptongji.main.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

/**
 * Created by Administrator on 2018/12/10.
 */

public class DynamicLinkDetailActivity extends BaseActivity {

    private WebView webView;

    private Dialog mWeiboDialog;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_dynamiclinkdetail);

        webView = (WebView) findViewById(R.id.wv_webview);

        LoadWebViewInfo();

    }

    private void LoadWebViewInfo (){

        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        setting.setSupportZoom(false);//不支持缩放
        setting.setBuiltInZoomControls(false);//不出现放大和缩小的按钮
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);//不设置网络缓存

        webView.setWebViewClient(new WebViewClient() {
        });//IE内核

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100){

                    WeiboDialogUtils.closeDialog(mWeiboDialog);

                }
            }
        });//谷歌内核

        String url = getIntent().getStringExtra("redImage");

        if (url != null){

            if (url.contains("http") || url.contains("https")){

                webView.loadUrl(url);

            }else {

                webView.loadUrl("http://"+url);

            }

        }else {

            webView.loadUrl("http://www.fulu86.com");

        }

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(DynamicLinkDetailActivity.this, "加载中...");


    }

}
