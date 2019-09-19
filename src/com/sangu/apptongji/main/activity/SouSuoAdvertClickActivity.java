package com.sangu.apptongji.main.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.fanxin.easeui.EaseConstant;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.ChatActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

/**
 * Created by Administrator on 2018/11/23.
 */

public class SouSuoAdvertClickActivity extends BaseActivity {

    private WebView wv_webView;
    private Dialog mWeiboDialog;
    TextView tv_midBtn;
    TextView tv_midBtn2;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_sousuoadvertclick);

        wv_webView = (WebView) findViewById(R.id.wv_webview);

        tv_midBtn = (TextView) findViewById(R.id.tv_midBtn);
        tv_midBtn2 = (TextView) findViewById(R.id.tv_midBtn2);

        tv_midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent(SouSuoAdvertClickActivity.this, ChatActivity.class);
                intent2.putExtra("userId", "13513895563");
                intent2.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                intent2.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                //  intent2.putExtra(EaseConstant.EXTRA_USER_IMG,"zhengshiduo.png");
                // intent2.putExtra(EaseConstant.EXTRA_USER_NAME,"李璐");
                intent2.putExtra(EaseConstant.EXTRA_USER_SHARERED,"无");
                startActivity(intent2);

            }
        });

        tv_midBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                //url:统一资源定位符
                //uri:统一资源标示符（更广）
                intent.setData(Uri.parse("tel:" + "13513895563"));
                //开启系统拨号器
                startActivity(intent);

            }
        });

        SetWebViewInfo();

    }


    private void SetWebViewInfo (){

        WebSettings setting = wv_webView.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        setting.setSupportZoom(false);//不支持缩放
        setting.setBuiltInZoomControls(false);//不出现放大和缩小的按钮
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);//不设置网络缓存

        wv_webView.setWebViewClient(new WebViewClient() {
        });//IE内核

        wv_webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100){

                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }
            }
        });//谷歌内核

        String url = getIntent().getStringExtra("url");

        if (url != null){

            if (url.contains("http") || url.contains("https")){

                wv_webView.loadUrl(url);

            }else {

                wv_webView.loadUrl("http://"+url);

            }

        }else {

            wv_webView.loadUrl("http://www.fulu86.com");

        }

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SouSuoAdvertClickActivity.this, "加载中...");

    }

}
