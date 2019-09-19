package com.sangu.apptongji.main.zhengshiinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;

/**
 * Created by Administrator on 2017-01-24.
 */

public class GongnengDetailActivity extends BaseActivity {
    private TextView tv_helpto_use,tv_title,tv1,tv2;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_zhengshier_detail2);
        tv_helpto_use = (TextView) findViewById(R.id.tv_helpto_use);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setVisibility(View.INVISIBLE);
        tv_title.setText("版本情况");
        String biaoshi = this.getIntent().getStringExtra("biaoshi");
        setViews(biaoshi);
    }

    private void setViews(String biaoshi) {
        switch (biaoshi){
            case "1":
                tv_helpto_use.setText("版本1.0.0");
                tv1.setText("更新了聊天的功能");
                break;
            case "2":
                tv_helpto_use.setText("版本1.0.5");
                tv1.setText("更新了修改头像");
                break;
            case "3":
                tv_helpto_use.setText("版本1.0.6");
                tv1.setText("优化了软件的体验");
                break;
            case "4":
                tv_helpto_use.setText("版本1.0.7");
                tv1.setText("更新增加和修改专业");
                break;
            case "5":
                tv_helpto_use.setText("版本1.0.8");
                tv1.setText("更新增加和修改消费，增加了支付功能");
                break;
            case "6":
                tv_helpto_use.setText("版本1.0.9");
                tv1.setText("更新了全新的订单模式");
                break;
            case "7":
                tv_helpto_use.setText("版本2.0.0");
                tv1.setText("全面完善了订单模式，修改订单中的一些bug");
                break;
            case "8":
                tv_helpto_use.setText("版本2.0.1");
                tv1.setText("修改了上传头像的显示问题");
                break;
            case "9":
                tv_helpto_use.setText("版本2.0.2");
                tv1.setText("完善了软件的功能和体验");
                break;
            case "10":
                tv_helpto_use.setText("版本2.0.3");
                tv1.setText("修复了部分反馈的bug");
                break;
            case "11":
                tv_helpto_use.setText("版本2.0.4");
                tv1.setText("聊天里面加入地图，实时显示位置");
                break;
            case "12":
                tv_helpto_use.setText("版本2.0.5");
                tv1.setText("加入了搜索功能，可根据需求进行筛选");
                break;
            case "13":
                tv_helpto_use.setText("版本2.0.6");
                tv1.setText("修复了搜索存在的bug");
                break;
            case "14":
                tv_helpto_use.setText("版本2.0.7");
                tv1.setText("加入了发布动态");
                break;
            case "15":
                tv_helpto_use.setText("版本2.0.8");
                tv1.setText("修改了动态的单一模式，可发布多种类型的动态");
                break;
            case "16":
                tv_helpto_use.setText("版本2.0.9");
                tv1.setText("完善了许多细节，增加了软件的可使用性");
                break;
            case "17":
                tv_helpto_use.setText("版本2.1.0");
                tv1.setText("新加入了企业模式，可定时上下班签到，可直接在企业中下单和派单");
                break;
            case "18":
                tv_helpto_use.setText("版本2.1.1");
                tv1.setText("1、企业管理新增上传合同、签到图片功能\n"+"2、企业新增工作报表功能\n"+"3、新闻动态替换为坐标分享");
                break;
            case "19":
                tv_helpto_use.setText("版本2.1.2");
                tv1.setText("修复了一下影响体验的bug");
                break;
            case "20":
                tv_helpto_use.setText("版本2.1.3");
                tv1.setText("1、优化动态布局\n" +
                        "2、新增在线任务动态，用户可接取任务获得报酬\n" +
                        "3、修复地图相关误差及部分bug");
                break;
            case "21":
                tv_helpto_use.setText("版本2.1.4");
                tv1.setText("1、增加了对动态的评价\n" +
                        "2、新增线下派单，企业用户可直接在软件进行线下派单\n" +
                        "3、新增对人物的评价，每与该用户完成一次交易可进行一次评价\n"+
                        "4、新增企业未读消息的提示");
                break;
            case "22":
                tv_helpto_use.setText("版本2.1.5");
                tv1.setText("1、新增微信支付、支付宝支付功能\n" +
                        "2、支持微信提现\n" +
                        "3、增加推送功能,使用户接收交易消息更加及时\n");
                break;
            case "23":
                tv_helpto_use.setText("版本2.1.6");
                tv1.setText("1、优化用户余额界面\n" +
                        "2、优化首页搜索功能\n" +
                        "3、优化企业设置相关功能,优化加入企业流程\n");
                break;
            case "24":
                tv_helpto_use.setText("版本2.1.7");
                tv1.setText("1、优化动态、企业搜索\n" +
                        "2、增加个人详情分享红包功能\n" +
                        "3、个人中心增加红包相关统计信息");
                break;
            case "25":
                tv_helpto_use.setText("版本2.1.8");
                tv1.setText("1、优化动态、增加朋友圈好友圈分类\n" +
                        "2、优化红包设置及主页分享功能\n" +
                        "3、优化订单流程、修改部分bug");
                break;
            case "26":
                tv_helpto_use.setText("版本2.1.9");
                tv1.setText("1、优化动态、增加朋友圈好友圈分类\n" +
                        "2、修复了部分手机不能微信分享的bug\n" +
                        "3、优化订单流程、修改部分bug");
                break;
            case "27":
                tv_helpto_use.setText("版本2.2.0");
                tv1.setText("1、修改动态切换圈子方式\n" +
                        "2、优化地图搜索功能\n" +
                        "3、优化坐标分享\n" +
                        "4、修复部分已知bug");
                break;
            case "28":
                tv_helpto_use.setText("版本2.2.1");
                tv1.setText("1、优化地图搜索功能\n" +
                        "2、优化用户体验\n" +
                        "3、修复部分已知bug");
                break;
            case "29":
                tv_helpto_use.setText("版本2.2.2");
                tv1.setText("1、优化地图搜索功能\n" +
                        "2、优化首页搜索功能\n" +
                        "3、修复部分部分7.0系统手机闪退问题\n" +
                        "4、修复部分7.0系统手机无法上传头像问题");
                break;
            case "30":
                tv_helpto_use.setText("版本2.2.3");
                tv1.setText("1、优化地图搜索功能\n" +
                        "2、优化首页搜索功能\n" +
                        "3、新增需求订单动态\n" +
                        "4、新增上传小视频");
                break;
            case "31":
                tv_helpto_use.setText("版本2.2.4");
                tv1.setText("1、优化接单派单接活发出需求\n" +
                        "2、优化考勤签到打卡请假管理\n" +
                        "3、优化推广分享名片奖励红包\n" +
                        "4、优化坐标发布定位通讯聊天\n" +
                        "5、新增网购售后安装维修派单");
                break;
            case "32":
                tv_helpto_use.setText("版本2.2.5");
                tv1.setText("1、修复部分已知bug\n" +
                        "2、新增两种订单模式\n" +
                        "3、优化订单流程\n");
                break;
            case "33":
                tv_helpto_use.setText("版本2.2.6");
                tv1.setText("1、修复部分已知bug\n" +
                        "2、优化统计数据\n" +
                        "3、优化订单模式\n");
                break;
            case "34":
                tv_helpto_use.setText("版本2.2.7");
                tv1.setText("1、修复部分已知bug\n" +
                        "2、优化统计数据\n" +
                        "3、优化红包流程\n");
                break;
            case "35":
                tv_helpto_use.setText("版本2.2.8");
                tv1.setText("1、修复部分已知bug\n" +
                        "2、修改支付流程\n" +
                        "3、优化订单流程\n"+
                        "4、增加多种下单模式\n");
                break;
            case "36":
                tv_helpto_use.setText("版本2.2.9");
                tv1.setText("1、修复部分已知bug\n" +
                        "2、优化订单流程\n" +
                        "3、新增企业数据统计\n"+
                        "4、新增多种分享模式\n");
                break;
            case "37":
                tv_helpto_use.setText("版本3.0.0");
                tv1.setText("1、修复部分手机更新失败的bug\n" +
                        "2、优化整合统计数据\n" +
                        "3、优化企业数据统计\n"+
                        "4、优化多种分享模式\n");
                break;
            case "38":
                tv_helpto_use.setText("版本3.0.1");
                tv1.setText("1、修复部分bug\n" +
                        "2、优化派单流程\n" +
                        "3、优化同步流程\n"+
                        "4、优化界面\n");
                break;
            case "39":
                tv_helpto_use.setText("版本3.0.2");
                tv1.setText("1、修复部分bug\n" +
                        "2、新增推送\n" +
                        "3、新增群组聊天\n"+
                        "4、新增群组位置共享\n");
                break;
            case "40":
                tv_helpto_use.setText("版本3.0.3");
                tv1.setText("1、修复部分bug\n" +
                        "2、新增查看报价和推送\n" +
                        "3、新增企业签到提醒\n");
                break;
            case "41":
                tv_helpto_use.setText("版本3.0.4");
                tv1.setText("1、修复部分bug\n");
                break;
            case "42":
                tv_helpto_use.setText("版本3.0.5");
                tv1.setText("1、新增极速派单功能\n" +
                        "2、优化红包相关操作\n"+
                        "3、修复部分bug\n");
                break;
            case "43":
                tv_helpto_use.setText("版本3.0.6");
                tv1.setText("1、优化极速派单功能\n" +
                        "2、优化红包相关操作\n"+
                        "3、修复部分bug\n");
                break;
            case "44":
                tv_helpto_use.setText("版本3.0.7");
                tv1.setText("1、优化派单功能\n" +
                        "2、优化红包相关操作\n"+
                        "3、修复部分bug\n");
                break;
            case "45":
                tv_helpto_use.setText("版本3.0.8");
                tv1.setText("1、优化派单功能\n" +
                        "2、优化红包相关操作\n"+
                        "3、修复部分bug\n");
                break;
            case "46":
                tv_helpto_use.setText("版本3.1");
                tv1.setText("1、优化派单功能\n" +
                        "2、优化红包相关操作\n"+
                        "3、修复部分bug\n");
                break;
            case "47":
                tv_helpto_use.setText("版本3.2");
                tv1.setText("1、优化派单功能\n" +
                        "2、优化红包及质保功能\n"+
                        "3、修复部分bug\n");
                break;
            case "48":
                tv_helpto_use.setText("版本3.3");
                tv1.setText("1、优化派单功能\n" +
                        "2、新增大数据统计\n"+
                        "3、新增短信派单\n");
                break;
            case "49":
                tv_helpto_use.setText("版本3.4");
                tv1.setText("1、优化接单派单功能\n" +
                        "2、优化短信派单\n"+
                        "3、优化数据统计\n");
                break;
            case "50":
                tv_helpto_use.setText("版本3.5");
                tv1.setText("1、优化接单派单功能\n" +
                        "2、优化短信派单\n");
                break;
            case "51":
                tv_helpto_use.setText("版本3.6");
                tv1.setText("1、优化接单派单功能\n" +
                        "2、新增推荐补贴\n"+
                        "3、新增VIP功能\n");
                break;
            case "52":
                tv_helpto_use.setText("版本3.7");
                tv1.setText("1、优化接单派单功能\n" +
                        "2、优化交易保障\n"+
                        "3、优化评论相关\n");
            case "53":
                tv_helpto_use.setText("版本3.8");
                tv1.setText("1、优化接单派单功能\n" +
                        "2、优化交易保障\n"+
                        "3、修复bug\n");
                break;
            case "54":
                tv_helpto_use.setText("版本4.0");
                tv1.setText("1、优化接单派单功能\n" +
                        "2、优化交易保障\n"+
                        "3、修复bug\n");
                break;
            case "55":
                tv_helpto_use.setText("版本4.1");
                tv1.setText("1、优化接单派单功能\n" +
                        "2、优化联系方式\n"+
                        "3、修复bug\n");
                break;
            case "56":
                tv_helpto_use.setText("版本4.2");
                tv1.setText("1、优化接单派单功能\n" +
                        "2、优化联系方式\n"+
                        "3、修复bug\n");
                break;
            case "57":
                tv_helpto_use.setText("版本4.3");
                tv1.setText("1、优化接单派单功能\n" +
                        "2、优化派单分类\n"+
                        "3、修复bug\n");
                break;
            case "58":
                tv_helpto_use.setText("版本4.4");
                tv1.setText("1、优化接单派单功能\n" +
                        "2、优化派单分类\n"+
                        "3、修复bug\n");
                break;
            case "59":
                tv_helpto_use.setText("版本4.5");
                tv1.setText("1、优化接单派单功能\n" +
                        "2、新增补贴任务\n"+
                        "3、修复bug\n");
                break;
        }
    }
}
