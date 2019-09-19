package com.pintu.deutils.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pintu.deutils.adapter.GridItemsAdapter;
import com.pintu.deutils.bean.ItemBean;
import com.pintu.deutils.util.GameUtil;
import com.pintu.deutils.util.ImagesUtil;
import com.pintu.deutils.util.ScreenUtil;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.main.utils.SoundPlayUtils;
import com.sangu.apptongji.ui.BaseActivity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 拼图逻辑主界面：面板显示
 *
 * @author xys
 */
public class PuzzleMain extends BaseActivity implements OnClickListener {

    // 拼图完成时显示的最后一个图片
    public static Bitmap mLastBitmap;
    // 设置为N*N显示
    public static int TYPE = 3;
//    // 步数显示
//    public static int COUNT_INDEX = 0;
//    // 计时显示
//    public static int TIMER_INDEX = 0;
    /**
     * UI更新Handler
     */
//    private Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    // 更新计时器
//                    TIMER_INDEX++;
//                    mTvTimer.setText("" + TIMER_INDEX);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
    // 选择的图片
    private Bitmap mPicSelected;
    // PuzzlePanel
    private GridView mGvPuzzleMainDetail;
    private ImageView mImageView;
    // Button
    private Button mBtnBack;
    private Button mBtnImage;
    private Button mBtnRestart;
    //    // 显示步数
//    private TextView mTvPuzzleMainCounts;
//    // 计时器
//    private TextView mTvTimer;
    // 切图后的图片
    private List<Bitmap> mBitmapItemLists = new ArrayList<Bitmap>();
    // GridView适配器
    private GridItemsAdapter mAdapter;
    // Flag 是否已显示原图
    private boolean mIsShowImg;
    //    // 计时器类
//    private Timer mTimer;
//    /**
//     * 计时器线程
//     */
//    private TimerTask mTimerTask;
    private String createTime,user_id,pintuUrl,dynamicSeq,gameRed,name;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.xpuzzle_puzzle_detail_main);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        name = getIntent().getStringExtra("name");
        pintuUrl = getIntent().getStringExtra("pintuUrl");
        user_id = getIntent().getStringExtra("user_id");
        gameRed = getIntent().getStringExtra("gameRed");
        createTime = getIntent().getStringExtra("createTime");
        dynamicSeq = getIntent().getStringExtra("dynamicSeq");
        // 获取选择的图片
        Bitmap picSelectedTemp;
        // 选择默认图片还是自定义图片
        picSelectedTemp = getBitmapFromSharedPreferences();
        // 对图片处理
        handlerImage(picSelectedTemp);
        // 初始化Views
        initViews();
        // 生成游戏数据
        generateGame();
        // GridView点击事件
        mGvPuzzleMainDetail.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                // 判断是否可移动
                if (GameUtil.isMoveable(position)) {
                    // 交换点击Item与空格的位置
                    GameUtil.swapItems(
                            GameUtil.mItemBeans.get(position),
                            GameUtil.mBlankItemBean);
                    // 重新获取图片
                    recreateData();
                    // 通知GridView更改UI
                    mAdapter.notifyDataSetChanged();
                    // 更新步数
//                    COUNT_INDEX++;
//                    mTvPuzzleMainCounts.setText("" + COUNT_INDEX);
                    // 判断是否成功
                    if (GameUtil.isSuccess()) {
                        // 将最后一张图显示完整
                        recreateData();
                        mBitmapItemLists.remove(TYPE * TYPE - 1);
                        mBitmapItemLists.add(mLastBitmap);
                        // 通知GridView更改UI
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(PuzzleMain.this, "拼图成功!",
                                Toast.LENGTH_LONG).show();
                        mGvPuzzleMainDetail.setEnabled(false);
//                        mTimer.cancel();
//                        mTimerTask.cancel();
                        showFxDialog();
                    }
                }
            }
        });
        // 返回按钮点击事件
        mBtnBack.setOnClickListener(this);
        // 显示原图按钮点击事件
        mBtnImage.setOnClickListener(this);
        // 重置按钮点击事件
        mBtnRestart.setOnClickListener(this);
    }

    private void showFxDialog() {
        LayoutInflater inflaterDl = LayoutInflater.from(PuzzleMain.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(PuzzleMain.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
        re_item5.setVisibility(View.VISIBLE);
        tv_item1.setText("分享到QQ空间");
        tv_item2.setText("分享到朋友圈");
        tv_item5.setText("分享到微博");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fenxiangtoqq();
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fenxiangtowx();
            }
        });
        re_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fenxiangtowb();
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void fenxiangtoqq() {
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setText(null);
        sp.setTitle(null);
        sp.setTitleUrl(null);
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qzone.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                updateTJzhuanfa(user_id,0);
                queryPtYue();
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        qzone.share(sp);
    }

    private void fenxiangtowx() {
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle("正事多app");
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
            }
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getApplicationContext(), "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                updateTJzhuanfa(user_id,1);
                queryPtYue();
            }
            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wx.share(sp);
    }

    private void fenxiangtowb() {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle("正事多app");
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wb.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getApplicationContext(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
                updateTJzhuanfa(user_id,2);
                queryPtYue();
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wb.share(sp);
    }

    private void updateTJzhuanfa(final String loginId,final int type) {
        String url = FXConstant.URL_TONGJI_ZHUANFA;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Userdetailac,add","增加浏览次数成功"+s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null) {
                    Log.e("Userdetailac,add", "增加浏览次数错误");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uLoginId",loginId);
                param.put("locationDynamics", "1");
                if (type==0) {
                    param.put("type", "qqLocationDynamic");
                }else if (type==1){
                    param.put("type", "weixinLocationDynamic");
                }else if (type==2){
                    param.put("type", "weiboLocationDynamic");
                }
                param.put("f_id",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(PuzzleMain.this).addToRequestQueue(request);
    }

    private void queryPtYue() {
        String url = FXConstant.URL_PUBLISHDETAIL_QUERY;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("puzzmain,q",s);
                JSONObject object = JSON.parseObject(s);
                JSONArray array = object.getJSONArray("clist");
                JSONObject detail = array.getJSONObject(0);
                String redSum = detail.getString("redSum");
                if (redSum!=null&&!"".equals(redSum)&&!redSum.equalsIgnoreCase("null")&&Double.parseDouble(redSum)>0){
                    addHongbao();
                }else {
                    LayoutInflater inflater1 = LayoutInflater.from(PuzzleMain.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(PuzzleMain.this,R.style.Dialog).create();
                    dialog1.show();
                    dialog1.getWindow().setContentView(layout1);
                    dialog1.setCanceledOnTouchOutside(true);
                    dialog1.setCancelable(true);
                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                    TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                    title.setText("温馨提示");
                    btnOK1.setText("确定");
                    btnCancel1.setText("取消");
                    title_tv1.setText("手慢了，红包已经被抢完了！");
                    btnCancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                    btnOK1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "网络连接错误！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("dynamicSeq",dynamicSeq);
                param.put("createTime",createTime);
                return param;
            }
        };
        MySingleton.getInstance(PuzzleMain.this).addToRequestQueue(request);
    }

    private void addHongbao() {
        if (name==null||"".equals(name)){
            name = user_id;
        }
        String url = FXConstant.URL_ADD_PINTUHB;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("puzzmain,a",s);
                JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("success".equals(code)){
                    SoundPlayUtils.play(2);
                    LayoutInflater inflaterDl = LayoutInflater.from(PuzzleMain.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_hongbao, null);
                    final Dialog dialog = new AlertDialog.Builder(PuzzleMain.this,R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    TextView tv_title1 = (TextView) layout.findViewById(R.id.tv_title1);
                    TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
                    TextView tv_yue = (TextView) layout.findViewById(R.id.tv_yue);
                    TextPaint tp = tv_title.getPaint();
                    tp.setFakeBoldText(true);
                    tv_title.setText(gameRed + "元");
                    tv_title1.setText(name);
                    tv_yue.setText("正事多 动态红包");
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }else {
                    LayoutInflater inflaterDl = LayoutInflater.from(PuzzleMain.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                    final Dialog dialog2 = new AlertDialog.Builder(PuzzleMain.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.setCancelable(false);
                    TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                    Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                    tv_title.setText("您已获得过本红包，快去抢其他红包吧");
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "网络连接错误！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("user_id",user_id);
                param.put("mer_id", DemoHelper.getInstance().getCurrentUsernName());
                param.put("timestamp",createTime);
                Log.e("puzzmain,p",param.toString());
                return param;
            }
        };
        MySingleton.getInstance(PuzzleMain.this).addToRequestQueue(request);
    }

    private Bitmap getBitmapFromSharedPreferences(){
        Bitmap bitmap = null;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sangu_pintu", getApplicationContext().MODE_PRIVATE);
        //第一步:取出字符串形式的Bitmap
        String imageString = sharedPreferences.getString("image", "");
        //第二步:利用Base64将字符串转换为ByteArrayInputStream
        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
        if (byteArray.length == 0) {
            return null;
        } else {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            //第三步:利用ByteArrayInputStream生成Bitmap
            bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
        }
        return bitmap;
    }

    /**
     * Button点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮点击事件
            case R.id.btn_puzzle_main_back:
                PuzzleMain.this.finish();
                break;
            // 显示原图按钮点击事件
            case R.id.btn_puzzle_main_img:
                Animation animShow = AnimationUtils.loadAnimation(
                        PuzzleMain.this, R.anim.image_show_anim);
                Animation animHide = AnimationUtils.loadAnimation(
                        PuzzleMain.this, R.anim.image_hide_anim);
                if (mIsShowImg) {
                    mImageView.startAnimation(animHide);
                    mImageView.setVisibility(View.GONE);
                    mIsShowImg = false;
                } else {
                    mImageView.startAnimation(animShow);
                    mImageView.setVisibility(View.VISIBLE);
                    mIsShowImg = true;
                }
                break;
            // 重置按钮点击事件
            case R.id.btn_puzzle_main_restart:
                cleanConfig();
                generateGame();
                recreateData();
                // 通知GridView更改UI
//                mTvPuzzleMainCounts.setText("" + COUNT_INDEX);
                mAdapter.notifyDataSetChanged();
                mGvPuzzleMainDetail.setEnabled(true);
                break;
            default:
                break;
        }
    }

    /**
     * 生成游戏数据
     */
    private void generateGame() {
        // 切图 获取初始拼图数据 正常顺序
        new ImagesUtil().createInitBitmaps(
                TYPE, mPicSelected, PuzzleMain.this);
        // 生成随机数据
        GameUtil.getPuzzleGenerator();
        // 获取Bitmap集合
        for (ItemBean temp : GameUtil.mItemBeans) {
            mBitmapItemLists.add(temp.getBitmap());
        }
        // 数据适配器
        mAdapter = new GridItemsAdapter(this, mBitmapItemLists);
        mGvPuzzleMainDetail.setAdapter(mAdapter);
//        // 启用计时器
//        mTimer = new Timer(true);
//        // 计时器线程
//        mTimerTask = new TimerTask() {
//
//            @Override
//            public void run() {
//                Message msg = new Message();
//                msg.what = 1;
//                mHandler.sendMessage(msg);
//            }
//        };
//        // 每1000ms执行 延迟0s
//        mTimer.schedule(mTimerTask, 0, 1000);
    }

    /**
     * 添加显示原图的View
     */
    private void addImgView() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(
                R.id.rl_puzzle_main_main_layout);
        mImageView = new ImageView(PuzzleMain.this);
        mImageView.setImageBitmap(mPicSelected);
        int x = (int) (mPicSelected.getWidth() * 0.9F);
        int y = (int) (mPicSelected.getHeight() * 0.9F);
        LayoutParams params = new LayoutParams(x, y);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mImageView.setLayoutParams(params);
        relativeLayout.addView(mImageView);
        mImageView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanConfig();
    }

    /**
     * 返回时调用
     */
    @Override
    protected void onStop() {
        super.onStop();
        // 清空相关参数设置
//        cleanConfig();
//        this.finish();
    }

    /**
     * 清空相关参数设置
     */
    private void cleanConfig() {
        // 清空相关参数设置
        GameUtil.mItemBeans.clear();
        // 停止计时器
//        mTimer.cancel();
//        mTimerTask.cancel();
//        COUNT_INDEX = 0;
//        TIMER_INDEX = 0;
    }

    /**
     * 重新获取图片
     */
    private void recreateData() {
        mBitmapItemLists.clear();
        for (ItemBean temp : GameUtil.mItemBeans) {
            mBitmapItemLists.add(temp.getBitmap());
        }
    }

    /**
     * 对图片处理 自适应大小
     *
     * @param bitmap bitmap
     */
    private void handlerImage(Bitmap bitmap) {
        // 将图片放大到固定尺寸
        int screenWidth = ScreenUtil.getScreenSize(this).widthPixels;
        int screenHeigt = ScreenUtil.getScreenSize(this).heightPixels;
        mPicSelected = new ImagesUtil().resizeBitmap(
                screenWidth * 0.9f, screenWidth * 0.9f, bitmap);
    }

    /**
     * 初始化Views
     */
    private void initViews() {
        // Button
        mBtnBack = (Button) findViewById(R.id.btn_puzzle_main_back);
        mBtnImage = (Button) findViewById(R.id.btn_puzzle_main_img);
        mBtnRestart = (Button) findViewById(R.id.btn_puzzle_main_restart);
        // Flag 是否已显示原图
        mIsShowImg = false;
        // GridView
        mGvPuzzleMainDetail = (GridView) findViewById(
                R.id.gv_puzzle_main_detail);
        // 设置为N*N显示
        mGvPuzzleMainDetail.setNumColumns(TYPE);
        LayoutParams gridParams = new LayoutParams(
                mPicSelected.getWidth(),
                mPicSelected.getHeight());
        // 其他格式属性
        gridParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        gridParams.setMargins(0, ScreenshotUtil.dip2px(getApplicationContext(),90),0,0);
        // Grid显示
        mGvPuzzleMainDetail.setLayoutParams(gridParams);
        mGvPuzzleMainDetail.setHorizontalSpacing(0);
        mGvPuzzleMainDetail.setVerticalSpacing(0);
//        // TV步数
//        mTvPuzzleMainCounts = (TextView) findViewById(
//                R.id.tv_puzzle_main_counts);
//        mTvPuzzleMainCounts.setText("" + COUNT_INDEX);
//        // TV计时器
//        mTvTimer = (TextView) findViewById(R.id.tv_puzzle_main_time);
//        mTvTimer.setText("0秒");
        // 添加显示原图的View
        addImgView();
    }
}
