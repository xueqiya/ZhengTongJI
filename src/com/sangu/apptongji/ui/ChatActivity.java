package com.sangu.apptongji.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fanxin.easeui.EaseConstant;
import com.fanxin.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.fragment.MainActivity;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.runtimepermissions.PermissionsManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
  *
 */
public class ChatActivity extends BaseActivity implements IUAZView,IQiYeDetailView{
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    public String toChatUsername="",type;
    public String titleName="",image="",resv1= "113.744531",resv2= "34.762711",sex="01",locationState="0";
    private IUAZPresenter presenter;
    private IQiYeInfoPresenter qiYeInfoPresenter;
    private Intent onHomeIntent;
    private boolean isHideLocation = false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        presenter = new UAZPresenter(this,this);
        qiYeInfoPresenter = new QiYeInfoPresenter(this,this);
        activityInstance = this;
        //get user id or group id
        type = getIntent().getStringExtra(Constant.EXTRA_USER_TYPE);
        int chatType = getIntent().getExtras().getInt("chatType");
        toChatUsername = getIntent().getExtras().getString("userId");
        chatFragment = new ChatFragment();
        if (type==null||"".equals(type)||"0".equals(type)) {
            if (Constant.CHATTYPE_GROUP == (chatType)) {
                qiYeInfoPresenter.loadQiYeInfo(toChatUsername);
            } else {
                presenter.loadThisDetail(toChatUsername);
            }
        }else {
            if ("单聊".equals(type)){
                presenter.loadThisDetail(toChatUsername);
            }else if ("企业".equals(type)){
                qiYeInfoPresenter.loadQiYeInfo(toChatUsername);
            }else {
                titleName = getIntent().getStringExtra(Constant.EXTRA_USER_NAME);
                resv1 = "";
                resv2 = "";
                chatFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
            }
        }
    }

    @Override
    public void updateQiyeInfo(QiYeInfo qiYeInfo){
        try {
            String shareRed = qiYeInfo.getShareRed();
            String name = qiYeInfo.getCompanyName();
            String friendsNumber = qiYeInfo.getFriendsNumber();
            titleName = URLDecoder.decode(name,"UTF-8");
            image = qiYeInfo.getComImage();
            resv1 = TextUtils.isEmpty(qiYeInfo.getComLongitude())?"113.744531":qiYeInfo.getComLongitude();
            resv2 = TextUtils.isEmpty(qiYeInfo.getComLatitude())?"34.762711":qiYeInfo.getComLatitude();
            String imgUrl=null;
            if (image!=null&&!"".equals(image)){
                imgUrl = image.split("\\|")[0];
            }
            String [] num;
            if (friendsNumber!=null&&!"0".equals(friendsNumber)) {
                num = friendsNumber.split("\\|");
                String onedown = num[0];
                if (shareRed!=null&&!"".equals(shareRed)&&!shareRed.equalsIgnoreCase("null")&&Double.parseDouble(shareRed)>0&&Double.parseDouble(onedown)>0){
                    shareRed="有";
                }else {
                    shareRed="无";
                }
            }else {
                shareRed="无";
            }
            Bundle bundle = getIntent().getExtras();
            bundle.putString(EaseConstant.EXTRA_USER_IMG,imgUrl);
            bundle.putString(EaseConstant.EXTRA_USER_NAME,titleName);
            bundle.putString(EaseConstant.EXTRA_USER_SHARERED,shareRed);
            chatFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void updateThisUser(Userful user2) {
        String userId = user2.getLoginId();
        if ("18337101357".equals(userId)){
            titleName = "正事多客服";
        }else {
            titleName = user2.getName();
        }
        String shareRed = user2.getShareRed();
        String friendsNumber = user2.getFriendsNumber();
        locationState = user2.getLocationState();
        image = user2.getImage();
        if (user2.getResv4().equalsIgnoreCase("00")) {

            resv1 = null;
            resv2 = null;
            isHideLocation = true;

        } else {

            resv1 = TextUtils.isEmpty(user2.getResv1()) ? null : user2.getResv1();
            resv2 = TextUtils.isEmpty(user2.getResv2()) ? null : user2.getResv2();
        }
        sex = TextUtils.isEmpty(user2.getSex())?"01":user2.getSex();
        String imgUrl=null;
        if (image!=null&&!"".equals(image)){
            imgUrl = image.split("\\|")[0];
        }
        String[] num;
        if (friendsNumber != null && !"0".equals(friendsNumber) && !friendsNumber.equalsIgnoreCase("null")) {
            num = friendsNumber.split("\\|");
            String onedown = "0";
            if (num.length > 0) {
                onedown = num[0];
            }
            if (shareRed != null && !"".equals(shareRed) && !shareRed.equalsIgnoreCase("null") && Double.parseDouble(shareRed) > 0 && Double.parseDouble(onedown) > 0) {
                shareRed = "有";
            } else {
                shareRed = "无";
            }
        } else {
            shareRed = "无";
        }
        //pass parameters to chat fragment
        Bundle bundle = getIntent().getExtras();
        bundle.putString(EaseConstant.EXTRA_USER_IMG,imgUrl);
        bundle.putString(EaseConstant.EXTRA_USER_NAME,titleName);
        bundle.putString(EaseConstant.EXTRA_USER_SHARERED,shareRed);
        chatFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    @Override
    public void showproLoading() {

    }

    @Override
    public void hideproLoading() {

    }

    @Override
    public void showproError() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        onHomeIntent = intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (onHomeIntent!=null){
            // make sure only one chat activity is opened
            String username = onHomeIntent.getStringExtra("userId");
            if (toChatUsername.equals(username))
                super.onNewIntent(onHomeIntent);
            else {
                finish();
                startActivity(onHomeIntent);
            }
            onHomeIntent = null;
        }
    }

    public String getTitleName(){
        if (titleName==null)
            return "";
        return titleName;
    }

    public String getImage(){
        if (image==null||image.equalsIgnoreCase("null"))
            return "";
        return image;
    }

    public String getResv1(){
        return resv1;
    }
    public String getLocationState(){
        if (locationState==null)
            return "01";
        return locationState;
    }

    public String getResv2(){
        return resv2;
    }

    public boolean isHideLocation() {
        return isHideLocation;
    }

    public String getSex(){
        return sex;
    }

    public String getchatId(){
        return toChatUsername;
    }

    @Override
    public void onBackPressed() {
        if (chatFragment!=null) {
            chatFragment.onBackPressed();
        }
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    public void showLoading() {
    }
    @Override
    public void hideLoading() {
    }
    @Override
    public void showError() {
    }
}
