package com.sangu.apptongji.main.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuFLocationActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFiveActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFourActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailThreeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailTwoActivity;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/11/13.
 */

public class ProjectDynamicLinkDetailActivity extends BaseActivity {


    private WebView wv_webView;
    private TextView tv_leftPrice,tv_rightPrice;
    private Button tv_leftBtn,tv_rightBtn;
    private RelativeLayout rl_reddetail;
    private Dialog mWeiboDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_linkdynamic);

        wv_webView = (WebView) findViewById(R.id.wv_webview);
        tv_leftPrice = (TextView) findViewById(R.id.tv_price1);
        tv_rightPrice = (TextView) findViewById(R.id.tv_price2);
        tv_leftBtn = (Button) findViewById(R.id.bt_left);
        tv_rightBtn = (Button) findViewById(R.id.bt_right);
        rl_reddetail = (RelativeLayout) findViewById(R.id.rl_reddetail);

        setInitValueInfo();

        SetWebViewInfo();

    }

    private void setInitValueInfo(){

        String price = getIntent().getStringExtra("price");
        String salePrice = getIntent().getStringExtra("salePrice");

        if (price==null||"".equals(price)){
            price="0";
        }
        if (salePrice==null||"".equals(salePrice)){
            salePrice="0";
        }

        final String txt = price.split("\\.")[0];
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(txt);
        if(m.matches()) {

            tv_leftPrice.setText(""+new DecimalFormat("0.00").format(Double.parseDouble(price)));

        }else {

            tv_leftPrice.setText("0.00");

        }

        if (salePrice.equals("0")){
            tv_rightPrice.setText(tv_leftPrice.getText().toString().trim());
        }else {
            tv_rightPrice.setText(""+new DecimalFormat("0.00").format(Double.parseDouble(salePrice)));
        }


        tv_leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  zyType = getIntent().getStringExtra("orderType");
                String  sID = getIntent().getStringExtra("sID");
                String dynamicSeq = getIntent().getStringExtra("dynamicSeq");
                String createTime = getIntent().getStringExtra("createTime");
                String zy1 = getIntent().getStringExtra("task_label");
                String balance = getIntent().getStringExtra("price");

                if (sID.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                    Toast.makeText(ProjectDynamicLinkDetailActivity.this, "不能给自己下单！", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (zyType.equals("01")) {

                    Intent intent = new Intent(ProjectDynamicLinkDetailActivity.this, UOrderDetailActivity.class);
                    intent.putExtra("wodezhanghao", DemoHelper.getInstance().getCurrentUsernName());
                    intent.putExtra("createTime", createTime);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("hxid", sID);
                    intent.putExtra("zy1", zy1);
                    intent.putExtra("orderBody", zy1);
                  //  intent.putExtra("pypass", pass);
                    intent.putExtra("typeDetail", "01");
                    intent.putExtra("biaoshi", "01");
                    intent.putExtra("balance", balance);
                    startActivityForResult(intent,0);

                }else if (zyType.equals("02")){

                    Intent intent = new Intent(ProjectDynamicLinkDetailActivity.this, UOrderDetailTwoActivity.class);
                    intent.putExtra("wodezhanghao", DemoHelper.getInstance().getCurrentUsernName());
                    intent.putExtra("createTime", createTime);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("hxid", sID);
                    intent.putExtra("zy1", zy1);
                    intent.putExtra("orderBody", zy1);
                  //  intent.putExtra("distance", distance);
                  //  intent.putExtra("pypass", pass);
                    intent.putExtra("typeDetail", "01");
                    intent.putExtra("biaoshi", "01");
                    intent.putExtra("balance", balance);
                    startActivityForResult(intent,0);

                }else if (zyType.equals("03")){

                    Intent intent = new Intent(ProjectDynamicLinkDetailActivity.this, UOrderDetailThreeActivity.class);
                    intent.putExtra("wodezhanghao", DemoHelper.getInstance().getCurrentUsernName());
                    intent.putExtra("createTime", createTime);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("hxid", sID);
                    intent.putExtra("zy1", zy1);
                    intent.putExtra("orderBody", zy1);
                 //   intent.putExtra("pypass", pass);
                    intent.putExtra("typeDetail", "01");
                    intent.putExtra("biaoshi", "01");
                    intent.putExtra("balance", balance);
                    startActivityForResult(intent,0);

                }else if (zyType.equals("04")){

                    Intent intent = new Intent(ProjectDynamicLinkDetailActivity.this, UOrderDetailFourActivity.class);
                    intent.putExtra("wodezhanghao", DemoHelper.getInstance().getCurrentUsernName());
                    intent.putExtra("createTime", createTime);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("hxid", sID);
                    intent.putExtra("zy1", zy1);
                    intent.putExtra("orderBody", zy1);
                 //   intent.putExtra("pypass", pass);
                    intent.putExtra("typeDetail", "01");
                    intent.putExtra("biaoshi", "01");
                    intent.putExtra("balance", balance);
                    startActivityForResult(intent,0);

                }else if (zyType.equals("05")){

                    Intent intent = new Intent(ProjectDynamicLinkDetailActivity.this, UOrderDetailFiveActivity.class);
                    intent.putExtra("wodezhanghao", DemoHelper.getInstance().getCurrentUsernName());
                    intent.putExtra("createTime", createTime);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("hxid", sID);
                    intent.putExtra("zy1", zy1);
                    intent.putExtra("orderBody", zy1);
                  //  intent.putExtra("pypass", pass);
                    intent.putExtra("typeDetail", "01");
                    intent.putExtra("biaoshi", "01");
                    intent.putExtra("balance", balance);
                    startActivityForResult(intent,0);

                }

            }
        });


        tv_rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String lat = getIntent().getStringExtra("lat");
                final String lng = getIntent().getStringExtra("lng");
                final String loginId = getIntent().getStringExtra("sID");

                if (lat.equals("0") || lng.equals("0")) {

                    Toast.makeText(ProjectDynamicLinkDetailActivity.this, "位置信息有误,建议直接联系商户", Toast.LENGTH_SHORT).show();

                }else {

                    String[] strLat = new String[]{lat};
                    final String[] strLong = new String[]{lng};
                    final String[] strLoginId = new String[]{loginId};
                    final String[] strName = new String[]{"商户"};
                    final String[] strSex = new String[]{"01"};
                    Intent intent = new Intent(ProjectDynamicLinkDetailActivity.this, BaiDuFLocationActivity.class);
                    intent.putExtra("lat", strLat);
                    intent.putExtra("lng", strLong);
                    intent.putExtra("loginId", strLoginId);
                    intent.putExtra("name", strName);
                    intent.putExtra("sex", strSex);
                    intent.putExtra("biaoshi","导航");

                    startActivity(intent);

                }

            }
        });

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
                    rl_reddetail.setVisibility(View.VISIBLE);
                }
            }
        });//谷歌内核

        String url = getIntent().getStringExtra("task_jurisdiction");

        if (url != null){

            if (url.contains("http") || url.contains("https")){

                wv_webView.loadUrl(url);

            }else {

                wv_webView.loadUrl("http://"+url);

            }

        }else {

            wv_webView.loadUrl("http://www.fulu86.com");

        }

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ProjectDynamicLinkDetailActivity.this, "加载中...");

    }


}
