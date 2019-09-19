package com.sangu.apptongji.main.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.utils.FileStorage;
import com.hyphenate.chat.EMClient;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.mabeijianxi.smallvideorecord2.LocalMediaCompress;
import com.mabeijianxi.smallvideorecord2.model.AutoVBRMode;
import com.mabeijianxi.smallvideorecord2.model.BaseMediaBitrateConfig;
import com.mabeijianxi.smallvideorecord2.model.LocalMediaConfig;
import com.mabeijianxi.smallvideorecord2.model.OnlyCompressOverBean;
import com.recycle.dragrecyclerview.helper.MyItemTouchCallback;
import com.recycle.dragrecyclerview.helper.OnRecyclerItemClickListener;
import com.recycle.dragrecyclerview.utils.VibratorUtil;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.AddFriendsPreActivity;
import com.sangu.apptongji.main.activity.CollectionDynamicActivity;
import com.sangu.apptongji.main.activity.MessageOrderIntroduceActivity;
import com.sangu.apptongji.main.activity.ProfileActivity;
import com.sangu.apptongji.main.activity.ProfileUpdateTwoActivity;
import com.sangu.apptongji.main.activity.RedPromoteActivity;
import com.sangu.apptongji.main.activity.ScanCaptureActivity;
import com.sangu.apptongji.main.activity.SettingsActivity;
import com.sangu.apptongji.main.activity.TongjiDetailActivity;
import com.sangu.apptongji.main.activity.TopVipActivity;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.activity.ZYDetailActivity;
import com.sangu.apptongji.main.adapter.GridImageAdapter;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.order.avtivity.BZJJNActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.BZJZJActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.ConsumeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.CurrentDynamicActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.DanjuListActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.DingdanActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.FWFKListActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.HongbaoDetailListActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.SelfYuEActivity;
import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;
import com.sangu.apptongji.main.alluser.presenter.IOrderListQPresenter;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.IProfilePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.OrderListQuPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.ProfilePresenter;
import com.sangu.apptongji.main.alluser.view.IOrderListQView;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.alluser.view.IProfileView;
import com.sangu.apptongji.main.qiye.CompanyInfoActivity;
import com.sangu.apptongji.main.qiye.CompanyRegistActivity;
import com.sangu.apptongji.main.qiye.QiYeDetailsActivity;
import com.sangu.apptongji.main.qiye.QiyePaidanListActivity;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.FullyGridLayoutManager;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.main.widget.FXPopWindow;
import com.sangu.apptongji.utils.FileUtils;
import com.sangu.apptongji.utils.PreferenceManager;
import com.sangu.apptongji.utils.SelectPicPopupWindow;
import com.yalantis.ucrop.entity.LocalMedia;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class FragmentProfile extends Fragment implements View.OnClickListener,IProfileView,IPriceView,IOrderListQView ,IQiYeDetailView{
    private TextView tvCompany;
    private TextView tvCompanyAdress;
    private TextView tvHometown;
    private TextView tvSchool;
    private TextView tvZhiYe;
    private RelativeLayout re_my_xiaofei;
    private RelativeLayout re_mydingdan;
    private RelativeLayout re_fuwufankui;
    private TextView tv_major1;
    private TextView tv1_bzj;
    private TextView tv2_bzj;
    private TextView tv3_bzj;
    private TextView tv4_bzj;
    private TextView tv_major2;
    private TextView tv_major3;
    private TextView tv_major4;
    private TextView tv_consume1;
    private TextView tv_consume2;
    private TextView tv_consume3;
    private TextView tv_consume4;
    private TextView tv_consume5;
    private TextView tv_consume6;
    private TextView tv_yuE;
    private RelativeLayout btn_chongzhi;
    private RelativeLayout re_my_qiye;
    private RelativeLayout re_my_dongtai;
    private RelativeLayout rl_yuE;
    private RelativeLayout btn_tixian;
    private RelativeLayout re_my_erweima;
    private RelativeLayout re_about_zhengshi;
    private RelativeLayout re_setting_self;
    private RelativeLayout re_danju;
    private RelativeLayout rl_zhuanfa;
    private TextView tvChZJine;
    private TextView tvTixJine;
    private TextView tv_kaidan;
    private TextView tv1jdcs;
    private TextView tv2jdcs;
    private TextView tv3jdcs;
    private TextView tv4jdcs;
    private TextView tv1jine;
    private TextView tv2jine;
    private TextView tv3jine;
    private TextView tv4jine;
    private TextView tvUnreadChu;
    private TextView tvUnreadRu;
    private TextView tv1_bao;
    private TextView tv2_bao;
    private TextView tv3_bao;
    private TextView tv4_bao;
    private Button btn_zhy1;
    private Button btn_zhy2;
    private Button btn_zhy3;
    private Button btn_zhy4;
    private TextView unread_qiye_number,tv_vip,tv_qiye,tv_myinfo,tv_hb_jine,tv_xinzeng_all,unread_danju_number,tv_fenxiang;
    private TextView tv_area,tv_region,tv_redpromote;
    private int zhufaAll;
    private LinearLayout ll_liulan;
    private TextView tv_vipPromot,tv_redPromot,tv_messagePromot;

    //    @ViewInject(R.id.iv_qr)
//    private ImageView iv_qr;
    FXPopWindow fxPopWindow;
    private String prices,chZJE,tXJE,cishu1,cishu2,cishu3,cishu4,upId1,upId2,upId3,upId4,liulancishu1,liulancishu2,liulancishu3,
            liulancishu4,qiyeId,locationState,personalDtails,resv31,resv32,resv33,resv34,myUseId,fxUpName;
    private String pass,remark1,remark2,remark3,remark4,margan1,margan2,margan3,margan4,decribe1,decribe2,decribe3,decribe4,avatar1,name,uNation="";
    private Double price= 0.0;
    private String maj1,maj2,maj3,maj4,cons1,cons2,cons3,cons4,cons5,cons6,image1,image2,image3,image4,
            create1,create2,create3,create4,resv5,resv6="",shareRed,friendsNumber,margin_time1,margin_time2,margin_time3,margin_time4;
    //    private int jiedanCishu = 0;
//    private int xiaofeiCishu = 0;
    private int uAll = 0;
    private int mAll = 0;
    private SwipeRefreshLayout srl;
    private IProfilePresenter presenter;
    private IPricePresenter pricePresenter;
    private IOrderListQPresenter presenter1;
    private IQiYeInfoPresenter qiyePresenter;
    HeadThreadt headThreadt;
    private String filesUrl,file1Url,file2Url,file3Url,file4Url,file5Url,file6Url,file7Url,file8Url;
    private String [] str;
    private CustomProgressDialog pd;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private SelectPicPopupWindow menuWindow=null;
    private ItemTouchHelper itemTouchHelper;
    String liulanzhAll="0";
    int billCount = 0;
    int thirdPartyCount = 0;

    /*不滚动的GridView*/
    private ArrayList<String> imagePaths1=new ArrayList<>();
    boolean isChange = false;
    private String file1 = null,file2 = null,file3 = null,file4 = null,file5 = null,file6 = null,file7 = null,file8 = null,image="";
    VideodThread videoThread=null;
    HeadThread headThread =null;
    private boolean istuFirst = true;
    private boolean isvuFirst = true;

    private String userResv1="",userResv2="";

    private boolean hasBao = false;
    private boolean isFirst = true;
    private boolean isqiyeFirst = true;
    private Handler myHeadImagehandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (headThreadt!=null&&!headThreadt.isInterrupted()) {
                        headThreadt.interrupt();
                        headThreadt = null;
                    }
                    break;
                case 2:
                    qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
                    if ((!"1".equals(uNation)&&!"01".equals(resv6))||(isqiyeFirst&&!qiyeId.equals(""))){
                        qiyePresenter.loadQiYeInfo(qiyeId);
                    }
                    break;
                case 3:
                    unread_qiye_number.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    if (filesUrl!=null&&!"".equals(filesUrl)&&!filesUrl.equalsIgnoreCase("null")){
                        if (isvuFirst) {
                            if (videoThread == null) {
                                videoThread = new VideodThread();
                                videoThread.start();
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (headThread!=null) {
                        headThread.interrupt();
                        headThread = null;
                    }
                    if (pd!=null&&pd.isShowing()){
                        pd.dismiss();
                    }
                    break;
                case 6:
                    if (videoThread!=null){
                        videoThread.interrupt();
                        videoThread = null;
                    }
                    if (pd!=null&&pd.isShowing()){
                        pd.dismiss();
                    }
                    break;
            }
        }
    };

    private void uploadImage(final String image, int index) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();
        String url = FXConstant.URL_AVATAR+image;
        try {
            Request request = new Request.Builder().url(url).build();
            okhttp3.Response response = client.newCall(request).execute();
            InputStream is = response.body().byteStream();
            Bitmap bm = BitmapFactory.decodeStream(is);
            if (bm!=null) {
                saveToSD(bm,image,null,index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void uploadImage2(final String videoName, final CustomProgressDialog pd,int index) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();
        String url = FXConstant.URL_AVATAR+videoName;
        try {
            Request request = new Request.Builder().url(url).build();
            okhttp3.Response response = client.newCall(request).execute();
            InputStream is = response.body().byteStream();
            if (is!=null) {
                saveToSD2(is,videoName,null,index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToSD(Bitmap mBitmap,String imageName,String childPath,int index) {
        File file = new FileStorage("touxiang").createCropFile(imageName,childPath);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getActivity(), "com.sangu.apptongji.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            getActivity().grantUriPermission(getActivity().getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            imageUri = Uri.fromFile(file);
        }
        File f = new File(file.getPath());
        try {
            //如果文件不存在，则创建文件
            if(!f.exists()){
                f.createNewFile();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            FileOutputStream fos = new FileOutputStream(f);
            int options = 100;
            // 如果大于80kb则再次压缩,最多压缩三次
            while (baos.toByteArray().length / 1024 > 200 && options > 10) {
                // 清空baos
                baos.reset();
                // 这里压缩options%，把压缩后的数据存放到baos中
                mBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 30;
            }
            fos.write(baos.toByteArray());
            fos.close();
            baos.close();
            loadImage1(Environment.getExternalStorageDirectory() + "/zhengshier/touxiang/" + imageName,index);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveToSD2(InputStream input,String imageName,String childPath,int index) {
        File file = new FileStorage("touxiang").createCropFile(imageName,childPath);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getActivity(), "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            getActivity().grantUriPermission(getActivity().getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            imageUri = Uri.fromFile(file);
        }
        File f = new File(file.getPath());
        OutputStream output=null;
        try {
            //如果文件不存在，则创建文件
            if(!f.exists()){
                f.createNewFile();
            }
            output=new FileOutputStream(file);
            byte buffer[]=new byte[4*1024];//每次存4K
            int temp;
            //写入数据
            while((temp=input.read(buffer))!=-1){
                output.write(buffer,0,temp);
            }
            output.flush();
            if (index==1){
                file1 = Environment.getExternalStorageDirectory() + "/zhengshier/touxiang/" + imageName;
                selectMedia.get(0).setCutPath(file1);
            }else if (index==2){
                file2 = Environment.getExternalStorageDirectory() + "/zhengshier/touxiang/" + imageName;
                selectMedia.get(1).setCutPath(file2);
            }else if (index==3){
                file3 = Environment.getExternalStorageDirectory() + "/zhengshier/touxiang/" + imageName;
                selectMedia.get(2).setCutPath(file3);
            }else if (index==4){
                file4 = Environment.getExternalStorageDirectory() + "/zhengshier/touxiang/" + imageName;
                selectMedia.get(3).setCutPath(file4);
            }else if (index==5){
                file5 = Environment.getExternalStorageDirectory() + "/zhengshier/touxiang/" + imageName;
                selectMedia.get(4).setCutPath(file5);
            }else if (index==6){
                file6 = Environment.getExternalStorageDirectory() + "/zhengshier/touxiang/" + imageName;
                selectMedia.get(5).setCutPath(file6);
            }else if (index==7){
                file7 = Environment.getExternalStorageDirectory() + "/zhengshier/touxiang/" + imageName;
                selectMedia.get(6).setCutPath(file7);
            }else if (index==8){
                file8 = Environment.getExternalStorageDirectory() + "/zhengshier/touxiang/" + imageName;
                selectMedia.get(7).setCutPath(file8);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                output.close();
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        tv_vip = (TextView) v.findViewById(R.id.tv_vip);
        tv_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转充值vip界面
                SharedPreferences mSharedPreferences3 = getActivity().getSharedPreferences("sangu_promote_visible", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor3 = mSharedPreferences3.edit();
                editor3.putString("vipPromot","1");
                editor3.commit();
                tv_vipPromot.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getActivity(), TopVipActivity.class);

                startActivityForResult(intent,0);

            }
        });

        tv_vipPromot = (TextView) v.findViewById(R.id.tv_vipPromt);
        tv_messagePromot = (TextView) v.findViewById(R.id.tv_messagePromt);
        tv_redPromot = (TextView) v.findViewById(R.id.tv_redPromt);

        SharedPreferences mSharedPreferences1 = getActivity().getSharedPreferences("sangu_promote_visible", Context.MODE_PRIVATE);

        if (mSharedPreferences1.getString("vipPromot","0").equals("0")){
            tv_vipPromot.setVisibility(View.VISIBLE);
        }

        if (mSharedPreferences1.getString("redPromot","0").equals("0")){
            tv_redPromot.setVisibility(View.VISIBLE);
        }

        if (mSharedPreferences1.getString("messagePromot","0").equals("0")){
            tv_messagePromot.setVisibility(View.VISIBLE);
        }

        tv_redpromote = (TextView) v.findViewById(R.id.tv_redpromote);
        unread_qiye_number = (TextView) v.findViewById(R.id.unread_qiye_number);
        unread_danju_number = (TextView) v.findViewById(R.id.unread_danju_number);
        tv_xinzeng_all = (TextView) v.findViewById(R.id.tv_xinzeng_all);
        tv_qiye = (TextView) v.findViewById(R.id.tv_qiye);
        tv_hb_jine = (TextView) v.findViewById(R.id.tv_hb_jine);
        tv_myinfo = (TextView) v.findViewById(R.id.tv_myinfo);
        tv_fenxiang = (TextView) v.findViewById(R.id.tv_fenxiang);
        ll_liulan = (LinearLayout) v.findViewById(R.id.ll_wodeziliao);
        tv_area = (TextView) v.findViewById(R.id.tv_area);
        tv_region = (TextView) v.findViewById(R.id.tv_region);
        srl = (SwipeRefreshLayout) v.findViewById(R.id.srl);
        unread_qiye_number.setVisibility(View.INVISIBLE);
        myUseId = DemoHelper.getInstance().getCurrentUsernName();
        RequestQueue queue = MySingleton.getInstance(getActivity()).getRequestQueue();
        presenter = new ProfilePresenter(getActivity(),this);
        presenter.updateData();
        qiyePresenter = new QiYeInfoPresenter(getActivity(),this);
        presenter1 = new OrderListQuPresenter(getActivity(),this);
        pricePresenter = new PricePresenter(getActivity(),this);
        pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());

        WeakReference<FragmentProfile> reference =  new WeakReference<FragmentProfile>(FragmentProfile.this);
        pd = CustomProgressDialog.createDialog(reference.get().getActivity());
        pd.setCancelable(true);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                pd.dismiss();
            }
        });
        pd.setMessage("正在加载数据...");
     //   pd.show();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        srl.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_light, android.R.color.holo_green_light);
        srl.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        setListener();
        initViews();
    }

    private void initViews(){
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(getActivity(), onAddPicClickListener);
        adapter.setList(selectMedia);
        adapter.setSelectMax(8);
        recyclerView.setAdapter(adapter);
        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(adapter).setOnDragListener(new MyItemTouchCallback.OnDragListener() {
            @Override
            public void onFinishDrag() {

            }
        }));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition()!=selectMedia.size()) {
                    isChange = true;
                    tv_myinfo.setText("修改头像");
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(getActivity(), 70);   //震动70ms
                }
            }
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
            }
        });
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (position > selectMedia.size() - 1) {
                    return;
                }
                switch (selectMedia.get(position).getType()) {
                    case FunctionConfig.TYPE_IMAGE:
                        // 预览图片 可长按保存 也可自定义保存路径
//                        PictureConfig.getInstance().externalPicturePreview(MainActivity.this, "/custom_file", position, selectMedia);
                        PictureConfig.getInstance().externalPicturePreview(getActivity(), position, selectMedia);
                        break;
                    case FunctionConfig.TYPE_VIDEO:
                        // 预览视频
                        String videoPath = selectMedia.get(position).getCutPath();
                        PictureConfig.getInstance().externalPictureVideo(getActivity(),videoPath);
                        break;
                }
            }
        });
    }

    /**
     * 删除图片回调接口
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    selectImgs();
                    break;
                case 1:
                    // 删除图片
                    if (position > selectMedia.size()) {
                        ToastUtils.showNOrmalToast(getActivity().getApplicationContext(),"数据出现错误请重新打开页面！");
                        return;
                    }
                    if ( selectMedia.get(position).getType()==FunctionConfig.TYPE_VIDEO){
                        if (position==0){
                            file1=null;
                        }else if (position==1){
                            file2=null;
                        }else if (position==2){
                            file3=null;
                        }else if (position==3){
                            file4=null;
                        }else if (position==4){
                            file5=null;
                        }else if (position==5){
                            file6=null;
                        }else if (position==6){
                            file7=null;
                        }else if (position==7){
                            file8=null;
                        }
                    }
                    imagePaths1.remove(position);
                    selectMedia.remove(position);
                    isChange = true;
                    tv_myinfo.setText("修改头像");
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };

    private void initView() {

        tvCompany = (TextView) getView().findViewById(R.id.tv_company);
        tvCompanyAdress = (TextView) getView().findViewById(R.id.tv_company_address);
        tvHometown = (TextView) getView().findViewById(R.id.tv_hometown);
        tvSchool = (TextView) getView().findViewById(R.id.tv_xuexiao);
        tvZhiYe = (TextView) getView().findViewById(R.id.tv_zhiye);
        tv_major1 = (TextView) getView().findViewById(R.id.tv_zy1);
        tv_major2 = (TextView) getView().findViewById(R.id.tv_zy2);
        tv_major3 = (TextView) getView().findViewById(R.id.tv_zy3);
        tv_major4 = (TextView) getView().findViewById(R.id.tv_zy4);
        tv_consume1 = (TextView) getView().findViewById(R.id.tv_gz1);
        tv_consume2 = (TextView) getView().findViewById(R.id.tv_gz2);
        tv_consume3 = (TextView) getView().findViewById(R.id.tv_gz3);
        tv_consume4 = (TextView) getView().findViewById(R.id.tv_gz4);
        tv_consume5 = (TextView) getView().findViewById(R.id.tv_gz5);
        tv_consume6 = (TextView) getView().findViewById(R.id.tv_gz6);
        tv_yuE = (TextView) getView().findViewById(R.id.lingqian);
        tv1_bzj = (TextView) getView().findViewById(R.id.zy1_baozj);
        tv2_bzj = (TextView) getView().findViewById(R.id.zy2_baozj);
        tv3_bzj = (TextView) getView().findViewById(R.id.zy3_baozj);
        tv4_bzj = (TextView) getView().findViewById(R.id.zy4_baozj);
        tvChZJine = (TextView) getView().findViewById(R.id.tv_chongzhijine);
        tvTixJine = (TextView) getView().findViewById(R.id.tv_tixianjine);
        tv_kaidan = (TextView) getView().findViewById(R.id.tv_kaidan);
        tv1jdcs = (TextView) getView().findViewById(R.id.zy1_jiedancishu);
        tv2jdcs = (TextView) getView().findViewById(R.id.zy2_jiedancishu);
        tv3jdcs = (TextView) getView().findViewById(R.id.zy3_jiedancishu);
        tv4jdcs = (TextView) getView().findViewById(R.id.zy4_jiedancishu);
        tv1jine = (TextView) getView().findViewById(R.id.zy1_jine);
        tv2jine = (TextView) getView().findViewById(R.id.zy2_jine);
        tv3jine = (TextView) getView().findViewById(R.id.zy3_jine);
        tv4jine = (TextView) getView().findViewById(R.id.zy4_jine);
        tvUnreadChu = (TextView) getView().findViewById(R.id.tv_unread_chuzhang);
        tvUnreadRu = (TextView) getView().findViewById(R.id.tv_unread_ruzhang);
        tv1_bao = (TextView) getView().findViewById(R.id.tv1_bao);
        tv2_bao = (TextView) getView().findViewById(R.id.tv2_bao);
        tv3_bao = (TextView) getView().findViewById(R.id.tv3_bao);
        tv4_bao = (TextView) getView().findViewById(R.id.tv4_bao);
        btn_zhy1 = (Button) getView().findViewById(R.id.btn_zhy1);
        btn_zhy2 = (Button) getView().findViewById(R.id.btn_zhy2);
        btn_zhy3 = (Button) getView().findViewById(R.id.btn_zhy3);
        btn_zhy4 = (Button) getView().findViewById(R.id.btn_zhy4);
        re_my_xiaofei = (RelativeLayout) getView().findViewById(R.id.re_my_xiaofei);
        re_mydingdan = (RelativeLayout) getView().findViewById(R.id.re_my_dingdan);
        re_fuwufankui = (RelativeLayout) getView().findViewById(R.id.re_fuwufankui);
        btn_chongzhi = (RelativeLayout) getView().findViewById(R.id.btn_chongzhi);
        btn_tixian = (RelativeLayout) getView().findViewById(R.id.btn_tixian);
        re_my_qiye = (RelativeLayout) getView().findViewById(R.id.re_my_qiye);
        re_my_dongtai = (RelativeLayout) getView().findViewById(R.id.re_my_dongtai);
        rl_yuE = (RelativeLayout) getView().findViewById(R.id.rl_yuE);
        re_my_erweima = (RelativeLayout) getView().findViewById(R.id.re_my_erweima);
        re_about_zhengshi = (RelativeLayout) getView().findViewById(R.id.re_about_zhengshi);
        re_setting_self = (RelativeLayout) getView().findViewById(R.id.re_setting_self);
        re_danju = (RelativeLayout) getView().findViewById(R.id.re_danju);
        rl_zhuanfa = (RelativeLayout) getView().findViewById(R.id.rl_zhuanfa);
    }

    private void selectImgs(){
        menuWindow = new SelectPicPopupWindow(getActivity(),1,itemsOnClick);
        //设置弹窗位置
        menuWindow.showAtLocation(getActivity().findViewById(R.id.llImage), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:        //点击选取视频
                    goCamera(FunctionConfig.TYPE_VIDEO,FunctionConfig.MODE_SINGLE,false);
                    break;
                case R.id.item_popupwindows_Photo:       //点击从相册中选择图片
                    goCamera(FunctionConfig.TYPE_IMAGE,FunctionConfig.MODE_MULTIPLE,true);
                    break;
                default:
                    break;
            }
        }
    };
    private void goCamera(int type,int oneOrmor,boolean showCama){
        WeakReference<FragmentProfile> reference = new WeakReference<FragmentProfile>(FragmentProfile.this);
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(type) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
//                            .setOffsetX() // 自定义裁剪比例
//                            .setOffsetY() // 自定义裁剪比例
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(8) // 可选择图片的数量
                .setMinSelectNum(0)// 图片或视频最低选择数量，默认代表无限制
                .setSelectMode(oneOrmor) // 单选 or 多选
                .setShowCamera(showCama) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                .setEnablePreview(true) // 是否打开预览选项
                .setEnableCrop(false) // 是否打开剪切选项
                .setCircularCut(false)// 是否采用圆形裁剪
                .setPreviewVideo(true) // 是否预览视频(播放) mode or 多选有效
                .setCheckedBoxDrawable(0)
                .setRecordVideoDefinition(FunctionConfig.HIGH) // 视频清晰度
                .setRecordVideoSecond(60) // 视频秒数
                .setCustomQQ_theme(0)// 可自定义QQ数字风格，不传就默认是蓝色风格
                .setGif(false)// 是否显示gif图片，默认不显示
                .setCropW(0) // cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
                .setCropH(0) // cropH-->裁剪高度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
                .setMaxB(102400) // 压缩最大值 例如:200kb  就设置202400，202400 / 1024 = 200kb
                .setPreviewColor(ContextCompat.getColor(reference.get().getActivity(), R.color.blue)) //预览字体颜色
                .setCompleteColor(ContextCompat.getColor(reference.get().getActivity(), R.color.blue)) //已完成字体颜色
                .setPreviewBottomBgColor(0) //预览图片底部背景色
                .setPreviewTopBgColor(0)//预览图片标题背景色
                .setBottomBgColor(0) //图片列表底部背景色
                .setGrade(Luban.THIRD_GEAR) // 压缩档次 默认三档
                .setCheckNumMode(false)
                .setCompressQuality(100) // 图片裁剪质量,默认无损
                .setImageSpanCount(4) // 每行个数
                .setVideoS(0)// 查询多少秒内的视频 单位:秒
                .setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
                .setCompressFlag(1) // 1 系统自带压缩 2 luban压缩
                .setCompressW(0) // 压缩宽 如果值大于图片原始宽高无效
                .setCompressH(0) // 压缩高 如果值大于图片原始宽高无效
                .setThemeStyle(ContextCompat.getColor(reference.get().getActivity(), R.color.blue)) // 设置主题样式
                .setNumComplete(false) // 0/9 完成  样式
                .setClickVideo(false)// 点击声音
                .setFreeStyleCrop(false) // 裁剪是移动矩形框或是图片
                .create();
        PictureConfig.getInstance().init(options).openPhoto(reference.get().getActivity(), resultCallback);
    }

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            imagePaths1.clear();
            // 多选回调
            if (resultList.size()>selectMedia.size()){
                isChange = true;
                tv_myinfo.setText("修改头像");
            }
            Log.e("callBack_result1,re", resultList.size() + "");
            Log.e("callBack_result1,se", selectMedia.size() + "");
            if (selectMedia != null) {
                for (int i=0;i<selectMedia.size();i++){
                    if (!resultList.get(i).equals(selectMedia.get(i))&&resultList.get(i).getType()!=FunctionConfig.TYPE_VIDEO) {
                        imagePaths1.add(selectMedia.get(i).getCompressPath());
                    }
                }
                for (int j=selectMedia.size();j<resultList.size();j++){
                    imagePaths1.add(resultList.get(j).getCompressPath());
                }
                selectMedia = resultList;
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
//                if (imagePaths2.size()>0){
//                    WeakReference<FragmentProfile> reference =  new WeakReference<FragmentProfile>(FragmentProfile.this);
//                    pd = CustomProgressDialog.createDialog(reference.get().getActivity());
//                    pd.setCancelable(false);
//                    pd.setMessage("正在处理图片资源...");
//                    pd.show();
//                    imaThread = new ImaThread();
//                    imaThread.start();
//                }
            }
        }

        @Override
        public void onSelectSuccess(final LocalMedia media) {
            if (media!=null){
                isChange = true;
                tv_myinfo.setText("修改头像");
            }
            // 单选回调
            if (media.getType()==FunctionConfig.TYPE_VIDEO) {
                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity(), -1);
                mProgressDialog.setMessage("正在处理视频,过程可能会比较长,请耐心等待...");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
                mProgressDialog.setCancelable(false);
                mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
                BaseMediaBitrateConfig compressMode = null;
                compressMode = new AutoVBRMode(30);
                compressMode.setVelocity("ultrafast");
                LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
                final LocalMediaConfig config = buidler
                        .setVideoPath(media.getPath())
                        .captureThumbnailsTime(1)
                        .doH264Compress(compressMode)
                        .setFramerate(25)
                        .setScale(2.0f)
                        .build();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mProgressDialog!=null&&!mProgressDialog.isShowing()) {
                                    mProgressDialog.show();
                                }
                            }
                        });
                        OnlyCompressOverBean onlyCompressOverBean = new LocalMediaCompress(config).startCompress();
                        final String imagePath = onlyCompressOverBean.getPicPath();
                        final String videoPath = onlyCompressOverBean.getVideoPath();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mProgressDialog != null) {
                                    mProgressDialog.dismiss();
                                }
                                if (videoPath!=null) {
                                    imagePaths1.add(imagePath);
                                    LocalMedia media1 = new LocalMedia();
                                    media1.setType(media.getType());
                                    media1.setCompressed(false);
                                    media1.setCut(false);
                                    media1.setIsChecked(true);
                                    media1.setNum(imagePaths1.size());
                                    media1.setPath(imagePath);
                                    media1.setCutPath(videoPath);
                                    selectMedia.add(media1);
                                    if (selectMedia != null) {
                                        adapter.setList(selectMedia);
                                        adapter.notifyDataSetChanged();
                                    }
                                }else {
                                    Toast.makeText(getActivity(),"视频文件处理失败，请从新选择",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }else {
                imagePaths1.clear();
                imagePaths1.add(media.getCompressPath());
//                if (imagePaths2.size()>0) {
//                    WeakReference<FragmentProfile> reference =  new WeakReference<FragmentProfile>(FragmentProfile.this);
//                    pd = CustomProgressDialog.createDialog(reference.get().getActivity());
//                    pd.setCancelable(false);
//                    pd.setMessage("正在处理图片资源...");
//                    pd.show();
//                    imaThread = new ImaThread();
//                    imaThread.start();
//                }
            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (pricePresenter!=null) {
                pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
            }
        }
    }

    private void setListener(){
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clear();
                WeakReference<FragmentProfile> reference =  new WeakReference<FragmentProfile>(FragmentProfile.this);
                pd = CustomProgressDialog.createDialog(reference.get().getActivity());
                pd.setMessage("正在加载数据");
                pd.setCancelable(false);
                pd.show();
                presenter.updateData();
                pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
            }
        });
        ll_liulan.setOnClickListener(this);
        btn_zhy4.setOnClickListener(this);
        btn_zhy3.setOnClickListener(this);
        btn_zhy2.setOnClickListener(this);
        btn_zhy1.setOnClickListener(this);
        re_my_xiaofei.setOnClickListener(this);
        re_mydingdan.setOnClickListener(this);
        re_fuwufankui.setOnClickListener(this);
        re_my_erweima.setOnClickListener(this);
        re_about_zhengshi.setOnClickListener(this);
        re_setting_self.setOnClickListener(this);
        re_danju.setOnClickListener(this);
        rl_zhuanfa.setOnClickListener(this);
        btn_chongzhi.setOnClickListener(this);
        rl_yuE.setOnClickListener(this);
        re_my_qiye.setOnClickListener(this);
        re_my_dongtai.setOnClickListener(this);
        btn_tixian.setOnClickListener(this);
        tv_kaidan.setOnClickListener(this);
        tv_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,DemoHelper.getInstance().getCurrentUsernName())
                .putExtra("biaoshi","01"));
            }
        });
        tv_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChange){
                    imagePaths1.clear();
                    if (selectMedia.size()>0){
                        if (selectMedia.size()<=8) {
                            send();
                        }else {
                            Toast.makeText(getActivity(), "图片不能多于八张...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), "请选择上传的头像...",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    if (("".equals(maj1) || maj1 == null) && ("".equals(maj2) || maj2 == null) && ("".equals(maj3) || maj3 == null) && ("".equals(maj4) || maj4 == null)) {
                        intent.putExtra("isFirst", "11");
                    } else {
                        intent.putExtra("isFirst", "12");
                    }
                    if (("".equals(cons1) || cons1 == null) && ("".equals(cons2) || cons2 == null) && ("".equals(cons3) || cons3 == null) && ("".equals(cons4) || cons4 == null)) {
                        intent.putExtra("iscFirst", "13");
                    } else {
                        intent.putExtra("iscFirst", "14");
                    }
                    startActivityForResult(intent, 0);
                }
            }
        });
        fxPopWindow=new FXPopWindow(getActivity(),R.layout.fx_popupwindow_add2,new FXPopWindow.OnItemClickListener(){
            @Override
            public void onClick(int position) {
                switch (position){
                    //发起群聊
                    case 0:
//                        startActivity(new Intent(getActivity(),GroupAddMembersActivity.class));
                        break;
                    //添加新的好友
                    case 1:
                        startActivity(new Intent(getActivity(),AddFriendsPreActivity.class));
                        break;
                    //扫一扫
                    case 2:
                        startActivity(new Intent(getActivity(), ScanCaptureActivity.class).putExtra("payPass",pass));
                        break;
                    //帮助及反馈
                    case 3:
                        fxPopWindow.dismiss();
                        break;
                }
            }
        });
        final String loginId = DemoHelper.getInstance().getCurrentUsernName();
        tv1_bzj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (margan1==null||"".equals(margan1)||Double.parseDouble(margan1)<=0){
                    startActivityForResult(new Intent(getActivity(), BZJJNActivity.class).putExtra("YUE",String.valueOf(price)).putExtra("upId",loginId+"1").putExtra("maj",maj1).putExtra("biaoshi","00"),0);
                }else {
                    startActivityForResult(new Intent(getActivity(), BZJZJActivity.class).putExtra("JINE",margan1).putExtra("YUE",String.valueOf(price)).putExtra("upId",loginId+"1").putExtra("maj",maj1)
                            .putExtra("createTime",create1).putExtra("biaoshi","00").putExtra("margin_time",margin_time1),0);
                }
            }
        });
        tv2_bzj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (margan2==null||"".equals(margan2)||Double.parseDouble(margan2)<=0){
                    startActivityForResult(new Intent(getActivity(), BZJJNActivity.class).putExtra("YUE",String.valueOf(price)).putExtra("upId",loginId+"2").putExtra("maj",maj2).putExtra("biaoshi","00"),0);
                }else {
                    startActivityForResult(new Intent(getActivity(), BZJZJActivity.class).putExtra("JINE",margan2).putExtra("YUE",String.valueOf(price)).putExtra("upId",loginId+"2").putExtra("maj",maj2)
                            .putExtra("createTime",create2).putExtra("biaoshi","00").putExtra("margin_time",margin_time2),0);
                }
            }
        });
        tv3_bzj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (margan3==null||"".equals(margan3)||Double.parseDouble(margan3)<=0){
                    startActivityForResult(new Intent(getActivity(), BZJJNActivity.class).putExtra("YUE",String.valueOf(price)).putExtra("upId",loginId+"3").putExtra("maj",maj3).putExtra("biaoshi","00"),0);
                }else {
                    startActivityForResult(new Intent(getActivity(), BZJZJActivity.class).putExtra("JINE",margan3).putExtra("YUE",String.valueOf(price)).putExtra("upId",loginId+"3").putExtra("maj",maj3)
                            .putExtra("createTime",create3).putExtra("biaoshi","00").putExtra("margin_time",margin_time3),0);
                }
            }
        });
        tv4_bzj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (margan4==null||"".equals(margan4)||Double.parseDouble(margan4)<=0){
                    startActivityForResult(new Intent(getActivity(), BZJJNActivity.class).putExtra("YUE",String.valueOf(price)).putExtra("upId",loginId+"4").putExtra("maj",maj4).putExtra("biaoshi","00"),0);
                }else {
                    startActivityForResult(new Intent(getActivity(), BZJZJActivity.class).putExtra("JINE",margan4).putExtra("YUE",String.valueOf(price)).putExtra("upId",loginId+"4").putExtra("maj",maj4)
                            .putExtra("createTime",create4).putExtra("biaoshi","00").putExtra("margin_time",margin_time4),0);
                }
            }
        });
        final String hxid = DemoHelper.getInstance().getCurrentUsernName();
        tv_major1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_major1.getText().equals("填写专业1")){
                    String isDYC;
                    if (("".equals(maj1)||maj1==null)&&("".equals(maj2)||maj2==null)&&("".equals(maj3)||maj3==null)&&("".equals(maj4)||maj4==null)){
                        isDYC = "11";
                    }else {
                        isDYC = "12";
                    }
                    startActivityForResult(new Intent(getActivity(),ProfileUpdateTwoActivity.class).putExtra("type", ProfileUpdateTwoActivity.TYPE_MAJOR1).putExtra("isFirst",isDYC).putExtra("image",image1),0);
                }else {
                    Log.e("details,type1",resv31);
                    Intent intent = new Intent(getActivity(), ZYDetailActivity.class);
                    intent.putExtra("distance","002");
                    intent.putExtra("zyType",resv31);
                    intent.putExtra("zhuanye","01");
                    intent.putExtra("name",name);
                    intent.putExtra("fxUpName", fxUpName);
                    intent.putExtra("decribe", decribe1);
                    intent.putExtra("remark", remark1);
                    intent.putExtra("image", image1);
                    intent.putExtra("biaoshi", "00");
                    intent.putExtra("create", create1);
                    intent.putExtra("body", maj1);
                    intent.putExtra("margen", margan1);
                    intent.putExtra("pass", pass);
                    intent.putExtra("hxid", hxid);
                    intent.putExtra("upId", upId1);
                    intent.putExtra("liulancishu", liulancishu1);
                    startActivityForResult(intent,0);
                }
            }
        });
        tv_major2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_major2.getText().equals("填写专业2")){
                    String isDYC;
                    if (("".equals(maj1)||maj1==null)&&("".equals(maj2)||maj2==null)&&("".equals(maj3)||maj3==null)&&("".equals(maj4)||maj4==null)){
                        isDYC = "11";
                    }else {
                        isDYC = "12";
                    }
                    startActivityForResult(new Intent(getActivity(),ProfileUpdateTwoActivity.class).putExtra("type", ProfileUpdateTwoActivity.TYPE_MAJOR2).putExtra("isFirst",isDYC).putExtra("image",image2),0);
                }else {
                    Log.e("details,type1",resv32);
                    Intent intent2 = new Intent(getActivity(), ZYDetailActivity.class);
                    intent2.putExtra("distance","002");
                    intent2.putExtra("zyType",resv32);
                    intent2.putExtra("name",name);
                    intent2.putExtra("fxUpName", fxUpName);
                    intent2.putExtra("zhuanye","02");
                    intent2.putExtra("decribe", decribe2);
                    intent2.putExtra("remark", remark2);
                    intent2.putExtra("image", image2);
                    intent2.putExtra("biaoshi", "00");
                    intent2.putExtra("create", create2);
                    intent2.putExtra("body", maj2);
                    intent2.putExtra("margen", margan2);
                    intent2.putExtra("pass", pass);
                    intent2.putExtra("hxid", hxid);
                    intent2.putExtra("upId", upId2);
                    intent2.putExtra("liulancishu", liulancishu2);
                    startActivityForResult(intent2,0);
                }
            }
        });
        tv_major3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_major3.getText().equals("填写专业3")){
                    String isDYC;
                    if (("".equals(maj1)||maj1==null)&&("".equals(maj2)||maj2==null)&&("".equals(maj3)||maj3==null)&&("".equals(maj4)||maj4==null)){
                        isDYC = "11";
                    }else {
                        isDYC = "12";
                    }
                    startActivityForResult(new Intent(getActivity(),ProfileUpdateTwoActivity.class).putExtra("type", ProfileUpdateTwoActivity.TYPE_MAJOR3).putExtra("isFirst",isDYC).putExtra("image",image3),0);
                }else {
                    Log.e("details,type1",resv33);
                    Intent intent3 = new Intent(getActivity(), ZYDetailActivity.class);
                    intent3.putExtra("distance","002");
                    intent3.putExtra("zyType",resv33);
                    intent3.putExtra("name",name);
                    intent3.putExtra("fxUpName", fxUpName);
                    intent3.putExtra("zhuanye","03");
                    intent3.putExtra("decribe", decribe3);
                    intent3.putExtra("remark", remark3);
                    intent3.putExtra("image", image3);
                    intent3.putExtra("create", create3);
                    intent3.putExtra("body", maj3);
                    intent3.putExtra("biaoshi", "00");
                    intent3.putExtra("margen", margan3);
                    intent3.putExtra("pass", pass);
                    intent3.putExtra("hxid", hxid);
                    intent3.putExtra("upId", upId3);
                    intent3.putExtra("liulancishu", liulancishu3);
                    startActivityForResult(intent3,0);
                }
            }
        });
        tv_major4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_major4.getText().equals("填写专业4")){
                    String isDYC;
                    if (("".equals(maj1)||maj1==null)&&("".equals(maj2)||maj2==null)&&("".equals(maj3)||maj3==null)&&("".equals(maj4)||maj4==null)){
                        isDYC = "11";
                    }else {
                        isDYC = "12";
                    }
                    startActivityForResult(new Intent(getActivity(),ProfileUpdateTwoActivity.class).putExtra("type", ProfileUpdateTwoActivity.TYPE_MAJOR4).putExtra("isFirst",isDYC).putExtra("image",image4),0);
                }else {
                    Log.e("details,type1",resv34);
                    Intent intent4 = new Intent(getActivity(), ZYDetailActivity.class);
                    intent4.putExtra("distance","002");
                    intent4.putExtra("zyType",resv34);
                    intent4.putExtra("name",name);
                    intent4.putExtra("fxUpName", fxUpName);
                    intent4.putExtra("zhuanye","04");
                    intent4.putExtra("decribe", decribe4);
                    intent4.putExtra("remark", remark4);
                    intent4.putExtra("image", image4);
                    intent4.putExtra("create", create4);
                    intent4.putExtra("body", maj4);
                    intent4.putExtra("biaoshi", "00");
                    intent4.putExtra("margen", margan4);
                    intent4.putExtra("pass", pass);
                    intent4.putExtra("hxid", hxid);
                    intent4.putExtra("upId", upId4);
                    intent4.putExtra("liulancishu", liulancishu4);
                    startActivityForResult(intent4,0);
                }
            }
        });
    }
    
    private void clear() {
        file1 = null;
        file2 = null;
        file3 = null;
        file4 = null;
        file5 = null;
        file6 = null;
        file7 = null;
        file8 = null;
        file1Url = null;
        file2Url = null;
        file3Url = null;
        file4Url = null;
        file5Url = null;
        file6Url = null;
        file7Url = null;
        file8Url = null;
        tv_myinfo.setText("编辑");
        isChange = false;
        istuFirst = true;
        isvuFirst = true;
        selectMedia.clear();
        imagePaths1.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.updateData();
        if (data == null) {
            return;
        }
        try {
            if (resultCode == Activity.RESULT_OK && data!=null && !TextUtils.isEmpty(String.valueOf(data.getDoubleExtra("value",price)))) {
                String prices1 = String.valueOf(data.getDoubleExtra(("value"),price));
                if (prices1!=null&&!"".equals(prices1)) {
                    double jine1 = Double.parseDouble(prices1);
                    prices1 = String.format("%.2f", jine1);
                    tv_yuE.setText(prices1);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        price = DemoApplication.getInstance().getCurrenPrice();
        uNation = DemoApplication.getInstance().getCurrentQiYeRemark();
        presenter1.loadOrderList(DemoHelper.getInstance().getCurrentUsernName());
        pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
        if ("1".equals(uNation)&&"00".equals(resv6)) {
            queryUnreadCount();
        }
        queryllzhfUnread();
    }

    public void loadDanju(){
        Bundle bundle = getArguments();
        if (getActivity()!=null){
            billCount = bundle.getInt("billCount",0);
            thirdPartyCount = bundle.getInt("thirdPartyCount",0);
            if (unread_danju_number!=null){
                unread_danju_number.setText(String.valueOf(billCount+thirdPartyCount));
            }
            if (billCount+thirdPartyCount>0){
                unread_danju_number.setVisibility(View.VISIBLE);
            }else {
                unread_danju_number.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void queryllzhfUnread() {
        SharedPreferences sp;
        if (getActivity()!=null) {
            sp = getActivity().getSharedPreferences("sangu_zhufa_count", Context.MODE_PRIVATE);
            liulanzhAll = sp.getString("zhufaliulanAll", "0");
        }
        Bundle bundle = getArguments();
        zhufaAll = bundle.getInt("tongjiAll",0);
        if (zhufaAll>0){
            tv_xinzeng_all.setVisibility(View.VISIBLE);
            if (zhufaAll>99){
                tv_xinzeng_all.setText("99");
            }else {
                tv_xinzeng_all.setText(String.valueOf(zhufaAll));
            }
        }else {
            tv_xinzeng_all.setVisibility(View.INVISIBLE);
        }
    }

    private void queryUnreadCount() {
        String url = FXConstant.URL_QUERY_UNREADQIYE+ qiyeId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    if (s==null||"".equals(s)){
                        Log.e("dingdanac","offSendOrderCount为空");
                    }else {
                        JSONObject object = new JSONObject(s);
                        int qjcount = object.getInt("leaveCount");
                        int bbcount = object.getInt("reportCount");
                        int ddcount = object.getInt("orderCount");
                        if (qjcount+bbcount+ddcount>0) {
                            unread_qiye_number.setVisibility(View.VISIBLE);
                            unread_qiye_number.setText(String.valueOf(qjcount+bbcount+ddcount));
                        }else {
                            unread_qiye_number.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("dingdanac,e",volleyError.toString());
            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_wodeziliao:
                int count = Integer.parseInt(liulanzhAll)+zhufaAll;
                SharedPreferences mSharedPreferences1 = getActivity().getSharedPreferences("sangu_zhufa_count", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = mSharedPreferences1.edit();
                editor1.putString("zhufaliulanAll",count+"");
                editor1.commit();
                startActivity(new Intent(getActivity(), TongjiDetailActivity.class).putExtra("biaoshi","00").putExtra("qiyeId",resv5).putExtra(FXConstant.JSON_KEY_HXID,DemoHelper.getInstance().getCurrentUsernName()));
                break;
            case R.id.btn_zhy1:
                String isDYC;
                if (("".equals(maj1)||maj1==null)&&("".equals(maj2)||maj2==null)&&("".equals(maj3)||maj3==null)&&("".equals(maj4)||maj4==null)){
                    isDYC =  "11";
                }else {
                    isDYC =  "12";
                }
                startActivityForResult(new Intent(getActivity(),ProfileUpdateTwoActivity.class).putExtra("type", ProfileUpdateTwoActivity.TYPE_MAJOR1).putExtra("isFirst",isDYC).putExtra("image",image1)
                        .putExtra("zyResv3",resv31).putExtra("upName",maj1).putExtra("upDescribe",decribe1), 0);
                break;
            case R.id.btn_zhy2:
                String isDYC2;
                if (("".equals(maj1)||maj1==null)&&("".equals(maj2)||maj2==null)&&("".equals(maj3)||maj3==null)&&("".equals(maj4)||maj4==null)){
                    isDYC2 =  "11";
                }else {
                    isDYC2 =  "12";
                }
                startActivityForResult(new Intent(getActivity(),ProfileUpdateTwoActivity.class).putExtra("type", ProfileUpdateTwoActivity.TYPE_MAJOR2).putExtra("isFirst",isDYC2).putExtra("image",image2)
                        .putExtra("zyResv3",resv32).putExtra("upName",maj2).putExtra("upDescribe",decribe2), 0);
                break;
            case R.id.btn_zhy3:
                String isDYC3;
                if (("".equals(maj1)||maj1==null)&&("".equals(maj2)||maj2==null)&&("".equals(maj3)||maj3==null)&&("".equals(maj4)||maj4==null)){
                    isDYC3 =  "11";
                }else {
                    isDYC3 =  "12";
                }
                startActivityForResult(new Intent(getActivity(),ProfileUpdateTwoActivity.class).putExtra("type", ProfileUpdateTwoActivity.TYPE_MAJOR3).putExtra("isFirst",isDYC3).putExtra("image",image3)
                        .putExtra("zyResv3",resv33).putExtra("upName",maj3).putExtra("upDescribe",decribe3), 0);
                break;
            case R.id.btn_zhy4:
                String isDYC4;
                if (("".equals(maj1)||maj1==null)&&("".equals(maj2)||maj2==null)&&("".equals(maj3)||maj3==null)&&("".equals(maj4)||maj4==null)){
                    isDYC4 =  "11";
                }else {
                    isDYC4 =  "12";
                }
                startActivityForResult(new Intent(getActivity(),ProfileUpdateTwoActivity.class).putExtra("type", ProfileUpdateTwoActivity.TYPE_MAJOR4).putExtra("isFirst",isDYC4).putExtra("image",image4)
                        .putExtra("zyResv3",resv34).putExtra("upName",maj4).putExtra("upDescribe",decribe4), 0);
                break;
            case R.id.tv_titl:
                Intent intentt = new Intent(getActivity(), ProfileActivity.class);
                if (("".equals(maj1)||maj1==null)&&("".equals(maj2)||maj2==null)&&("".equals(maj3)||maj3==null)&&("".equals(maj4)||maj4==null)){
                    intentt.putExtra("isFirst","11");
                }else {
                    intentt.putExtra("isFirst","12");
                }
                if (("".equals(cons1)||cons1==null)&&("".equals(cons2)||cons2==null)&&("".equals(cons3)||cons3==null)&&("".equals(cons4)||cons4==null)){
                    intentt.putExtra("iscFirst","13");
                }else {
                    intentt.putExtra("iscFirst","14");
                }
                startActivityForResult(intentt,0);
                break;
            case R.id.rl_yuE:

                if (userResv1.length() > 3 && userResv2.length()>3){

                    String hasbzj1 = "00";
                    if (hasBao){
                        hasbzj1 = "01";
                    }
                    Intent intent11 = new Intent(getActivity(),SelfYuEActivity.class);
                    intent11.putExtra("baozhengjin",hasbzj1);
                    intent11.putExtra("biaoshi","000");
                    startActivityForResult(intent11,3);

                }else {

                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_regist_error, null);
                    final AlertDialog dialog3 = new AlertDialog.Builder(getActivity(),R.style.Dialog).create();
                    TextView tv_phone = (TextView) layout.findViewById(R.id.tv_phone);
                    TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                    Button btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
                    Button btn_kefu = (Button) layout.findViewById(R.id.btn_kefu);
                    dialog3.show();
                    dialog3.getWindow().setContentView(layout);
                    dialog3.setCanceledOnTouchOutside(false);
                    dialog3.setCancelable(false);
                    tv_phone.setVisibility(View.INVISIBLE);
                    title_tv.setText("检测到您账户有安全风险，禁止操作金额，有疑问可联系客户经理");
                    btn_kefu.setText("联系客户经理");
                    btn_kefu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog3.dismiss();

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            //url:统一资源定位符
                            //uri:统一资源标示符（更广）
                            intent.setData(Uri.parse("tel:" + "18337101357"));
                            //开启系统拨号器
                            getActivity().startActivity(intent);

                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog3.dismiss();
                        }
                    });

                }

                break;
            case R.id.re_my_xiaofei:
                Intent intent1 = new Intent(getActivity(), ConsumeActivity.class);
                intent1.putExtra("papass",pass);
                intent1.putExtra("biaoshi","01");
                startActivityForResult(intent1,5);
                break;
            case R.id.re_my_dingdan:
                startActivity(new Intent(getActivity(), DingdanActivity.class).putExtra("biaoshi","01"));
                break;
            case R.id.btn_chongzhi:

                //充值换成短信派单
                SharedPreferences mSharedPreferences2 = getActivity().getSharedPreferences("sangu_promote_visible", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = mSharedPreferences2.edit();
                editor2.putString("messagePromot","1");
                editor2.commit();
                tv_messagePromot.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(getActivity(), MessageOrderIntroduceActivity.class);

                startActivityForResult(intent,0);

//                Intent intent2 = new Intent(getActivity(), ChongZhiActivity.class);
//                intent2.putExtra("papass",pass);
//                intent2.putExtra("price",price);
//                intent2.putExtra("biaoshi","00");
//                startActivityForResult(intent2,1);
                break;
            case R.id.btn_tixian:

                //提现换成红包
                startActivity(new Intent(getActivity(), HongbaoDetailListActivity.class).putExtra("friendsNumber",friendsNumber).putExtra("shareRed",shareRed).putExtra("biaoshi","00"));

//                String hasbzj = "00";
//                if (hasBao){
//                    hasbzj = "01";
//                }
//                Intent intent3 = new Intent(getActivity(), TiXianActivity.class);
//                intent3.putExtra("baozhengjin",hasbzj);
//                intent3.putExtra("biaoshi","00");
//                intent3.putExtra("papass",pass);
//                intent3.putExtra("price",price);
//                startActivityForResult(intent3,3);
                break;
            case R.id.re_fuwufankui:
                startActivity(new Intent(getActivity(), FWFKListActivity.class));
                break;
            case R.id.tv_kaidan:
                startActivity(new Intent(getActivity(), FWFKListActivity.class));
                break;
            case R.id.re_my_dongtai:
                String biaoshi = "00";
                Intent intent5 = new Intent(getActivity(),CurrentDynamicActivity.class);
                intent5.putExtra("userId",DemoHelper.getInstance().getCurrentUsernName());
                intent5.putExtra("biaoshi",biaoshi);
                startActivity(intent5);
                break;
            case R.id.re_my_qiye:
                Log.e("fragmentpro,una",uNation);
                Log.e("fragmentpro,res",resv6);
                String hasbzj2 = "00";
                if (hasBao){
                    hasbzj2 = "01";
                }
                String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
                if ("1".equals(uNation)&&"00".equals(resv6)) {
                    Intent intent6 = new Intent(getActivity(), CompanyInfoActivity.class);
                    intent6.putExtra("resv5", qiyeId);
                    intent6.putExtra("resv6", resv6);
                    intent6.putExtra("pass", pass);
                    intent6.putExtra("baozhengjin",hasbzj2);
//                    intent6.putExtra("valueM",valueM);
//                    intent6.putExtra("valueU", valueU);
                    startActivity(intent6);
                }else if (("0".equals(uNation)||"2".equals(uNation))&&"00".equals(resv6)){
                    Intent intent6 = new Intent(getActivity(), CompanyRegistActivity.class);
                    intent6.putExtra("qiyeId",qiyeId);
                    intent6.putExtra("biaoshi","1");
                    startActivity(intent6);
                }else if ("01".equals(resv6)){
                    Intent intent6 = new Intent(getActivity(), QiYeDetailsActivity.class);
                    intent6.putExtra("qiyeId", qiyeId);
                    intent6.putExtra("biaoshi", "00");
                    intent6.putExtra("name", name);
                    startActivity(intent6);
                }else {
                    startActivity(new Intent(getActivity(),QiyePaidanListActivity.class).putExtra("biaoshi","00").putExtra("isQunzhu","00"));
                }
                break;
            case R.id.re_about_zhengshi:
                startActivityForResult(new Intent(getActivity(), SettingsActivity.class).putExtra("locationState",locationState),0);
                break;
            case R.id.re_my_erweima:
                startActivityForResult(new Intent(getActivity(), CollectionDynamicActivity.class),0);
              //  startActivity(new Intent(getActivity(),MyqrActivity.class).putExtra("biaoshi","haoyou").putExtra("touxiang",avatar1));
                break;
            case R.id.re_setting_self:
                startActivityForResult(new Intent(getActivity(), SettingsActivity.class).putExtra("locationState",locationState),0);
                break;
            case R.id.re_danju:
                startActivity(new Intent(getActivity(), DanjuListActivity.class).putExtra("billCount",billCount).putExtra("thirdPartyCount",thirdPartyCount));
                break;
            case R.id.rl_zhuanfa:

                //跳转到推荐补贴界面
                SharedPreferences mSharedPreferences3 = getActivity().getSharedPreferences("sangu_promote_visible", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor3 = mSharedPreferences3.edit();
                editor3.putString("redPromot","1");
                editor3.commit();
                tv_redPromot.setVisibility(View.INVISIBLE);
                startActivity(new Intent(getActivity(), RedPromoteActivity.class));

               // startActivity(new Intent(getActivity(), HongbaoDetailListActivity.class).putExtra("friendsNumber",friendsNumber).putExtra("shareRed",shareRed).putExtra("biaoshi","00"));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (headThreadt!=null) {
            headThreadt.interrupt();
            headThreadt = null;
        }
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        if (imagePaths1!=null){
            imagePaths1.clear();
            imagePaths1=null;
        }
        if (selectMedia!=null){
            selectMedia.clear();
            selectMedia=null;
        }
        if (headThread!=null){
            headThread.interrupt();
            headThread=null;
        }
    }

    @Override
    public void updateCurrentPrice(Object success) {

        chZJE = DemoApplication.getApp().getCurrentChzYuE();
        tXJE = DemoApplication.getApp().getCurrenttxYuE();
        price = DemoApplication.getApp().getCurrenPrice();
        pass = DemoApplication.getApp().getCurrentPayPass();
        if (chZJE.equals("")) {
            chZJE = "0.00";
        }
        double jine = Double.parseDouble(chZJE);
        chZJE = String.format("%.2f", jine);
        chZJE = chZJE + " 元";
        if (tXJE.equals("")) {
            tXJE = "0.00";
        }
        double jine1 = Double.parseDouble(tXJE);
        tXJE = String.format("%.2f", jine1);
        tXJE = tXJE + " 元";
        String jine2 = String.format("%.2f", price);
        prices = String.valueOf(jine2)+" 元";
        tv_yuE.setText(prices);

        try {

            JSONObject object = (JSONObject)success;

            String redPromoteBalance = object.getString("redPromoteBalance");

            String str = redPromoteBalance+"<small>元</small>";

           tv_redpromote.setText(Html.fromHtml(str));

        } catch (JSONException e) {

            e.printStackTrace();

        }

       // tvChZJine.setText(chZJE);
       // tvTixJine.setText(tXJE);
    }

    private void send() {
        WeakReference<FragmentProfile> reference =  new WeakReference<FragmentProfile>(FragmentProfile.this);
        pd = CustomProgressDialog.createDialog(reference.get().getActivity());
        pd.setCancelable(false);
        pd.setMessage("正在上传...");
        pd.show();
        if (imagePaths1==null||imagePaths1.size()==0){
            for (int i=0;i<selectMedia.size();i++){
                imagePaths1.add(selectMedia.get(i).getPath());
            }
        }
        file1 = null;
        file2 = null;
        file3 = null;
        file4 = null;
        file5 = null;
        file6 = null;
        file7 = null;
        file8 = null;
        if (selectMedia.size()>0){
            if (selectMedia.get(0).getType()==FunctionConfig.TYPE_VIDEO){
                file1 = selectMedia.get(0).getCutPath();
            }
            if (selectMedia.size()>1){
                if (selectMedia.get(1).getType()==FunctionConfig.TYPE_VIDEO){
                    file2 = selectMedia.get(1).getCutPath();
                }
                if (selectMedia.size()>2){
                    if (selectMedia.get(2).getType()==FunctionConfig.TYPE_VIDEO){
                        file3 = selectMedia.get(2).getCutPath();
                    }
                    if (selectMedia.size()>3){
                        if (selectMedia.get(3).getType()==FunctionConfig.TYPE_VIDEO){
                            file4 = selectMedia.get(3).getCutPath();
                        }
                        if (selectMedia.size()>4){
                            if (selectMedia.get(4).getType()==FunctionConfig.TYPE_VIDEO){
                                file5 = selectMedia.get(4).getCutPath();
                            }
                            if (selectMedia.size()>5){
                                if (selectMedia.get(5).getType()==FunctionConfig.TYPE_VIDEO){
                                    file6 = selectMedia.get(5).getCutPath();
                                }
                                if (selectMedia.size()>6){
                                    if (selectMedia.get(6).getType()==FunctionConfig.TYPE_VIDEO){
                                        file7 = selectMedia.get(6).getCutPath();
                                    }
                                    if (selectMedia.size()>7){
                                        if (selectMedia.get(7).getType()==FunctionConfig.TYPE_VIDEO){
                                            file8 = selectMedia.get(7).getCutPath();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        List<Param> param=new ArrayList<>();
        String key = "uImage";
        param.add(new Param("uLoginId", DemoHelper.getInstance().getCurrentUsernName()));
        OkHttpManager.getInstance().posts2(param,key,imagePaths1,null,new ArrayList<String>(),"uImage1",file1,"uImage2",file2,"uImage3",file3,"uImage4",file4,"uImage5",file5
                ,"uImage6",file6,"uImage7",file7,"uImage8",file8,FXConstant.URL_UPDATE,new OkHttpManager.HttpCallBack() {
                    @Override
                    public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                        pd.dismiss();
                        String code = jsonObject.getString("code");
                        if (code.equals("SUCCESS")) {
                            isChange = false;
                            tv_myinfo.setText("编辑");
                            FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/catch/"));
                            FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/touxiang/"));
                            FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/DCIM/mabeijianxi/"));
                            Toast.makeText(getActivity(), "更新成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(String errorMsg) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "网络繁忙，请稍后再试" + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    class HeadThread extends Thread{
        @Override
        public void run() {
            super.run();
            if (istuFirst){
                File f1,f2,f3,f4,f5,f6,f7,f8;
                if (!image.equals("")&&image.length()>1) {
                    str = image.split("\\|");
                    if (str.length > 0) {
                        f1 = new FileStorage("touxiang").createCropFile(str[0],null);
                        if (f1.exists()) {
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+str[0],1);
                        } else {
                            uploadImage(str[0],1);
                        }
                    }
                    if (str.length > 1) {
                        f2 = new FileStorage("touxiang").createCropFile(str[1],null);
                        if (f2.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+str[1],2);
                        }else {
                            uploadImage(str[1],2);
                        }
                    }
                    if (str.length > 2) {
                        f3 = new FileStorage("touxiang").createCropFile(str[2],null);
                        if (f3.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+str[2],3);
                        }else {
                            uploadImage(str[2],3);
                        }
                    }
                    if (str.length > 3) {
                        f4 = new FileStorage("touxiang").createCropFile(str[3],null);
                        if (f4.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+str[3],4);
                        }else {
                            uploadImage(str[3],4);
                        }
                    }
                    if (str.length > 4) {
                        f5 = new FileStorage("touxiang").createCropFile(str[4],null);
                        if (f5.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+str[4],5);
                        }else {
                            uploadImage(str[4],5);
                        }
                    }
                    if (str.length > 5) {
                        f6 = new FileStorage("touxiang").createCropFile(str[5],null);
                        if (f6.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+str[5],6);
                        }else {
                            uploadImage(str[5],6);
                        }
                    }
                    if (str.length > 6) {
                        f7 = new FileStorage("touxiang").createCropFile(str[6],null);
                        if (f7.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+str[6],7);
                        }else {
                            uploadImage(str[6],7);
                        }
                    }
                    if (str.length > 7) {
                        f8 = new FileStorage("touxiang").createCropFile(str[7],null);
                        if (f8.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+str[7],8);
                        }else {
                            uploadImage(str[7],8);
                        }
                    }
                }
                Message msg = new Message();
                msg.what = 4;
                myHeadImagehandler.sendMessage(msg);
            }
            istuFirst = false;
        }
    }

    class VideodThread extends Thread{
        @Override
        public void run() {
            super.run();
            if (isvuFirst){
                File f1,f2,f3,f4,f5,f6,f7,f8;
                if (!filesUrl.equals("")&&filesUrl.length()>1) {
                    if (file1Url!=null&&str.length>0) {
                        f1 = new FileStorage("touxiang").createCropFile(file1Url,null);
                        if (f1.exists()) {
                            file1 = Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+file1Url;
                            selectMedia.get(0).setCutPath(file1);
                        } else {
                            uploadImage2(file1Url, pd,1);
                        }
                    }
                    if (file2Url!=null&&str.length>1) {
                        f2 = new FileStorage("touxiang").createCropFile(file2Url,null);
                        if (f2.exists()) {
                            file2 = Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+file2Url;
                            selectMedia.get(1).setCutPath(file2);
                        } else {
                            uploadImage2(file2Url,pd,2);
                        }
                    }
                    if (file3Url!=null&&str.length>2) {
                        f3 = new FileStorage("touxiang").createCropFile(file3Url,null);
                        if (f3.exists()) {
                            file3 = Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+file3Url;
                            selectMedia.get(2).setCutPath(file3);
                        } else {
                            uploadImage2(file3Url,pd,3);
                        }
                    }
                    if (file4Url!=null&&str.length>3) {
                        f4 = new FileStorage("touxiang").createCropFile(file4Url,null);
                        if (f4.exists()) {
                            file4 = Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+file4Url;
                            selectMedia.get(3).setCutPath(file4);
                        } else {
                            uploadImage2(file4Url,pd,4);
                        }
                    }
                    if (file5Url!=null&&str.length>4) {
                        f5 = new FileStorage("touxiang").createCropFile(file5Url,null);
                        if (f5.exists()) {
                            file5 = Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+file5Url;
                            selectMedia.get(4).setCutPath(file5);
                        } else {
                            uploadImage2(file5Url,pd,5);
                        }
                    }
                    if (file6Url!=null&&str.length>5) {
                        f6 = new FileStorage("touxiang").createCropFile(file6Url,null);
                        if (f6.exists()) {
                            file6 = Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+file6Url;
                            selectMedia.get(5).setCutPath(file6);
                        } else {
                            uploadImage2(file6Url,pd,6);
                        }
                    }
                    if (file7Url!=null&&str.length>6) {
                        f7 = new FileStorage("touxiang").createCropFile(file7Url,null);
                        if (f7.exists()) {
                            file7 = Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+file7Url;
                            selectMedia.get(6).setCutPath(file7);
                        } else {
                            uploadImage2(file7Url,pd,7);
                        }
                    }
                    if (file8Url!=null&&str.length>7) {
                        f8 = new FileStorage("touxiang").createCropFile(file8Url,null);
                        if (f8.exists()) {
                            file8 = Environment.getExternalStorageDirectory()+"/zhengshier/touxiang/"+file8Url;
                            selectMedia.get(7).setCutPath(file8);
                        } else {
                            uploadImage2(file8Url,pd,8);
                        }
                    }
                }
                Message msg = new Message();
                msg.what = 6;
                myHeadImagehandler.sendMessage(msg);
            }
            isvuFirst = false;
        }
    }
    private void loadImage1(String filepath,int index){
        if (filepath!=null && imagePaths1 != null) {
            imagePaths1.add(filepath);
            int type = FunctionConfig.TYPE_IMAGE;
            if (index == 1) {
                if (file1Url != null && !"0".equals(file1Url)) {
                    type = FunctionConfig.TYPE_VIDEO;
                } else {
                    type = FunctionConfig.TYPE_IMAGE;
                }
            } else if (index == 2) {
                if (file2Url != null && !"0".equals(file2Url)) {
                    type = FunctionConfig.TYPE_VIDEO;
                } else {
                    type = FunctionConfig.TYPE_IMAGE;
                }
            } else if (index == 3) {
                if (file3Url != null && !"0".equals(file3Url)) {
                    type = FunctionConfig.TYPE_VIDEO;
                } else {
                    type = FunctionConfig.TYPE_IMAGE;
                }
            } else if (index == 4) {
                if (file4Url != null && !"0".equals(file4Url)) {
                    type = FunctionConfig.TYPE_VIDEO;
                } else {
                    type = FunctionConfig.TYPE_IMAGE;
                }
            } else if (index == 5) {
                if (file5Url != null && !"0".equals(file5Url)) {
                    type = FunctionConfig.TYPE_VIDEO;
                } else {
                    type = FunctionConfig.TYPE_IMAGE;
                }
            } else if (index == 6) {
                if (file6Url != null && !"0".equals(file6Url)) {
                    type = FunctionConfig.TYPE_VIDEO;
                } else {
                    type = FunctionConfig.TYPE_IMAGE;
                }
            } else if (index == 7) {
                if (file7Url != null && !"0".equals(file7Url)) {
                    type = FunctionConfig.TYPE_VIDEO;
                } else {
                    type = FunctionConfig.TYPE_IMAGE;
                }
            } else if (index == 8) {
                if (file8Url != null && !"0".equals(file8Url)) {
                    type = FunctionConfig.TYPE_VIDEO;
                } else {
                    type = FunctionConfig.TYPE_IMAGE;
                }
            }
            LocalMedia media = new LocalMedia();
            media.setType(type);
            media.setCompressed(false);
            media.setCut(false);
            media.setIsChecked(true);
            media.setNum(imagePaths1.size());
            media.setPath(filepath);
            selectMedia.add(media);
        }
    }
    private void loadImage2(String filepath){
        imagePaths1.add(filepath);
    }





    @Override
    public void updateUserInfo(Userful user) {

        userResv1 = user.getResv1();
        userResv2 = user.getResv2();

        filesUrl = user.getuFiles();
        image = TextUtils.isEmpty(user.getImage())?"":user.getImage();
        personalDtails = TextUtils.isEmpty(user.getPersonalDtails())?"0":user.getPersonalDtails();
        String company = TextUtils.isEmpty(user.getCompany())?"未设置":user.getCompany();
        String companyAddress = TextUtils.isEmpty(user.getCompanyAdress())?"未设置":user.getCompanyAdress();
        locationState = user.getLocationState();
        friendsNumber = user.getFriendsNumber();
        shareRed = user.getShareRed();
        resv5 = user.getResv5();
        resv6 = user.getResv6();
        uNation = user.getuNation();
        if ("1".equals(uNation)&&"00".equals(resv6)&&isqiyeFirst) {
            queryUnreadCount();
        }
        avatar1 = TextUtils.isEmpty(user.getImage())?"":user.getImage();
        name = TextUtils.isEmpty(user.getName())?user.getLoginId():user.getName();
        myHeadImagehandler.sendEmptyMessage(2);
        if (!company.equals("未设置")) {
            try {
                company = URLDecoder.decode(company, "UTF-8");
                companyAddress = URLDecoder.decode(companyAddress, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if ("1".equals(uNation)&&"00".equals(resv6)) {
            tv_qiye.setText("我的企业");
        }else if (("0".equals(uNation)||"2".equals(uNation))&&"00".equals(resv6)){
            tv_qiye.setText("我的企业");
        }else if ("01".equals(resv6)){
            tv_qiye.setText("我的企业");
        }else {
            tv_qiye.setText("我的派单");
        }
        tvCompany.setText(company);
        tvCompanyAdress.setText(companyAddress);
        tvHometown.setText(TextUtils.isEmpty(user.getHome())?"未设置":user.getHome());
        tvSchool.setText(TextUtils.isEmpty(user.getSchool())?"未设置":user.getSchool());
        tvZhiYe.setText(TextUtils.isEmpty(user.getZhiYe())?"未设置":user.getZhiYe());
        remark1 = TextUtils.isEmpty(user.getRemark1())?"":user.getRemark1();
        remark2 = TextUtils.isEmpty(user.getRemark2())?"":user.getRemark2();
        remark3 = TextUtils.isEmpty(user.getRemark3())?"":user.getRemark3();
        remark4 = TextUtils.isEmpty(user.getRemark4())?"":user.getRemark4();
        margan1 = TextUtils.isEmpty(user.getMargin1())?"0":user.getMargin1();
        margan2 = TextUtils.isEmpty(user.getMargin2())?"0":user.getMargin2();
        margan3 = TextUtils.isEmpty(user.getMargin3())?"0":user.getMargin3();
        margan4 = TextUtils.isEmpty(user.getMargin4())?"0":user.getMargin4();
        decribe1 = TextUtils.isEmpty(user.getUpDescribe1())?"":user.getUpDescribe1();
        decribe2 = TextUtils.isEmpty(user.getUpDescribe2())?"":user.getUpDescribe2();
        decribe3 = TextUtils.isEmpty(user.getUpDescribe3())?"":user.getUpDescribe3();
        decribe4 = TextUtils.isEmpty(user.getUpDescribe4())?"":user.getUpDescribe4();
        liulancishu1 = TextUtils.isEmpty(user.getZy1resv1())?"0":user.getZy1resv1();
        liulancishu2 = TextUtils.isEmpty(user.getZy2resv1())?"0":user.getZy2resv1();
        liulancishu3 = TextUtils.isEmpty(user.getZy3resv1())?"0":user.getZy3resv1();
        liulancishu4 = TextUtils.isEmpty(user.getZy4resv1())?"0":user.getZy4resv1();
        upId1 = user.getUpId1();
        upId2 = user.getUpId2();
        upId3 = user.getUpId3();
        upId4 = user.getUpId4();
        margin_time1 = user.getMargin_time1();
        margin_time2 = user.getMargin_time2();
        margin_time3 = user.getMargin_time3();
        margin_time4 = user.getMargin_time4();
        maj1 = TextUtils.isEmpty(user.getUpName1())?"":user.getUpName1();
        maj2 = TextUtils.isEmpty(user.getUpName2())?"":user.getUpName2();
        maj3 = TextUtils.isEmpty(user.getUpName3())?"":user.getUpName3();
        maj4 = TextUtils.isEmpty(user.getUpName4())?"":user.getUpName4();
        cons1 = TextUtils.isEmpty(user.getUcName1())?"":user.getUcName1();
        cons2 = TextUtils.isEmpty(user.getUcName2())?"":user.getUcName2();
        cons3 = TextUtils.isEmpty(user.getUcName3())?"":user.getUcName3();
        cons4 = TextUtils.isEmpty(user.getUcName4())?"":user.getUcName4();
        cons5 = TextUtils.isEmpty(user.getUcName5())?"":user.getUcName5();
        cons6 = TextUtils.isEmpty(user.getUcName6())?"":user.getUcName6();
        create1 = TextUtils.isEmpty(user.getCreateTime1())?"":user.getCreateTime1();
        create2 = TextUtils.isEmpty(user.getCreateTime2())?"":user.getCreateTime2();
        create3 = TextUtils.isEmpty(user.getCreateTime3())?"":user.getCreateTime3();
        create4 = TextUtils.isEmpty(user.getCreateTime4())?"":user.getCreateTime4();
        image1 = TextUtils.isEmpty(user.getZyImage1())?"":user.getZyImage1();
        image2 = TextUtils.isEmpty(user.getZyImage2())?"":user.getZyImage2();
        image3 = TextUtils.isEmpty(user.getZyImage3())?"":user.getZyImage3();
        image4 = TextUtils.isEmpty(user.getZyImage4())?"":user.getZyImage4();
        resv31 = TextUtils.isEmpty(user.getZy1resv3())?"01":user.getZy1resv3();
        resv32 = TextUtils.isEmpty(user.getZy2resv3())?"01":user.getZy2resv3();
        resv33 = TextUtils.isEmpty(user.getZy3resv3())?"01":user.getZy3resv3();
        resv34 = TextUtils.isEmpty(user.getZy4resv3())?"01":user.getZy4resv3();
        tv_major1.setText(TextUtils.isEmpty(maj1)?"填写专业1":maj1);
        tv_major2.setText(TextUtils.isEmpty(maj2)?"填写专业2":maj2);
        tv_major3.setText(TextUtils.isEmpty(maj3)?"填写专业3":maj3);
        tv_major4.setText(TextUtils.isEmpty(maj4)?"填写专业4":maj4);
        tv_consume1.setText(TextUtils.isEmpty(cons1)?"关注消费1":cons1);
        tv_consume2.setText(TextUtils.isEmpty(user.getUcName2())?"关注消费2":user.getUcName2());
        tv_consume3.setText(TextUtils.isEmpty(user.getUcName3())?"关注消费3":user.getUcName3());
        tv_consume4.setText(TextUtils.isEmpty(user.getUcName4())?"关注消费4":user.getUcName4());
        tv_consume5.setText(TextUtils.isEmpty(user.getUcName5())?"关注消费5":user.getUcName5());
        tv_consume6.setText(TextUtils.isEmpty(user.getUcName6())?"关注消费6":user.getUcName6());

        tvChZJine.setText(user.getMessageOrderCount()+" / "+user.getMessageOrderAll());
        tv_area.setText(user.getAreaProfession());
        tv_hb_jine.setText(user.getuClick());
        tv_region.setText(user.getAreaUserSize());

        if (shareRed!=null&&!"".equals(shareRed)&&!shareRed.equalsIgnoreCase("null")) {
            double jine1 = Double.parseDouble(shareRed);
            String hbYue = String.format("%.2f", jine1);
            hbYue = hbYue + " 元";
            tvTixJine.setText(hbYue);
        }else {
            tvTixJine.setText("0.00 元");
        }
        if (maj1==null||"".equals(maj1)||"null".equals(maj1)||"NULL".equals(maj1)){
            btn_zhy1.setVisibility(View.VISIBLE);
        }else {
            btn_zhy1.setVisibility(View.GONE);
            if (fxUpName==null){
                fxUpName = maj1+"，";
            }
        }
        if (maj2==null||"".equals(maj2)||"null".equals(maj2)||"NULL".equals(maj2)){
            btn_zhy2.setVisibility(View.VISIBLE);
        }else {
            btn_zhy2.setVisibility(View.GONE);
            if (fxUpName==null){
                fxUpName = maj2+"，";
            }else {
                fxUpName += maj2+"，";
            }
        }
        if (maj3==null||"".equals(maj3)||"null".equals(maj3)||"NULL".equals(maj3)){
            btn_zhy3.setVisibility(View.VISIBLE);
        }else {
            btn_zhy3.setVisibility(View.GONE);
            if (fxUpName==null){
                fxUpName = maj3+"，";
            }else {
                fxUpName += maj3+"，";
            }
        }
        if (maj4==null||"".equals(maj4)||"null".equals(maj4)||"NULL".equals(maj4)){
            btn_zhy4.setVisibility(View.VISIBLE);
        }else {
            btn_zhy4.setVisibility(View.GONE);
            if (fxUpName==null){
                fxUpName = maj4+"，";
            }else {
                fxUpName += maj4+"，";
            }
        }
        if (!(remark1.equals("")||remark1.equals(null))){
            String [] array = remark1.split(",");
            if (array.length==1) {
                cishu1 = array[0].trim();
            }else if (array.length==2){
                cishu1 = array[0].trim();
                String jine1 = array[1];
                if (jine1.equals("")||jine1.equals(null)){
                    jine1 = "0";
                }
                tv1jine.setText(jine1+"元");
            }
            if (cishu1.equals("")||cishu1.equals(null)){
                cishu1 = "0";
            }
            tv1jdcs.setText(cishu1+"次");
        }
        if (!(remark2.equals("")||remark2.equals(null))){
            String [] array = remark2.split(",");
            cishu2 = "";
            if (array.length==1) {
                cishu2 = array[0].trim();
            }else if (array.length==2){
                cishu2 = array[0].trim();
                String jine1 = array[1];
                if (jine1.equals("")||jine1.equals(null)){
                    jine1 = "0";
                }
                tv2jine.setText(jine1+"元");
            }
            if (cishu2.equals("")||cishu2.equals(null)){
                cishu2 = "0";
            }
            tv2jdcs.setText(cishu2+"次");
        }
        if (!(remark3.equals("")||remark3.equals(null))){
            String [] array = remark3.split(",");
            cishu3 = "";
            if (array.length==1) {
                cishu3 = array[0].trim();
            }else if (array.length==2){
                cishu3 = array[0].trim();
                String jine1 = array[1];
                if (jine1.equals("")||jine1.equals(null)){
                    jine1 = "0";
                }
                tv3jine.setText(jine1+"元");
            }
            if (cishu3.equals("")||cishu3.equals(null)){
                cishu3 = "0";
            }
            tv3jdcs.setText(cishu3+"次");
        }
        if (!(remark4.equals("")||remark4.equals(null))){
            String [] array = remark4.split(",");
            cishu4 = "";
            if (array.length==1) {
                cishu4 = array[0].trim();
            }else if (array.length==2){
                cishu4 = array[0].trim();
                String jine1 = array[1];
                if (jine1.equals("")||jine1.equals(null)){
                    jine1 = "0";
                }
                tv4jine.setText(jine1+"元");
            }
            if (cishu4.equals("")||cishu4.equals(null)){
                cishu4 = "0";
            }
            tv4jdcs.setText(cishu4+"次");
        }
        //double allc = Double.valueOf(cishu1)+Double.valueOf(cishu2)+Double.valueOf(cishu3)+Double.valueOf(cishu4)+Double.valueOf(cishu5)+Double.valueOf(cishu6);
        //tvajdcs.setText(String.valueOf(allc)+"次");

        if (!margan1.equals("0")||!margan2.equals("0")||!margan3.equals("0")||!margan4.equals("0")){
            hasBao = true;
        }
        if (margan1==null||"0".equals(margan1)){
            tv1_bao.setVisibility(View.VISIBLE);
            tv1_bzj.setText("点亮");
            tv1_bzj.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        }else {
            tv1_bao.setVisibility(View.GONE);
            tv1_bzj.setText(margan1 + "元");
            tv1_bzj.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        }
        if (margan2==null||"0".equals(margan2)){
            tv2_bao.setVisibility(View.VISIBLE);
            tv2_bzj.setText("点亮");
            tv2_bzj.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        }else {
            tv2_bao.setVisibility(View.GONE);
            tv2_bzj.setText(margan2 + "元");
            tv2_bzj.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        }
        if (margan3==null||"0".equals(margan3)){
            tv3_bao.setVisibility(View.VISIBLE);
            tv3_bzj.setText("点亮");
            tv3_bzj.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        }else {
            tv3_bao.setVisibility(View.GONE);
            tv3_bzj.setText(margan3 + "元");
            tv3_bzj.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        }
        if (margan4==null||"0".equals(margan4)){
            tv4_bao.setVisibility(View.VISIBLE);
            tv4_bzj.setText("点亮");
            tv4_bzj.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        }else {
            tv4_bao.setVisibility(View.GONE);
            tv4_bzj.setText(margan4 + "元");
            tv4_bzj.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        }
        if (avatar1!=null&&avatar1.length()>40) {
            String [] orderProjectArray = avatar1.split("\\|");
            avatar1 = orderProjectArray[0];
        }
        if (isFirst) {
            headThreadt = new HeadThreadt();
            headThreadt.start();
        }
        if (filesUrl!=null&&!"".equals(filesUrl)&&!filesUrl.equalsIgnoreCase("null")){
            String [] urls = filesUrl.split("\\|");
            if (urls.length>0&&!"0".equals(urls[0])){
                file1Url = urls[0];
            }
            if (urls.length>1&&!"0".equals(urls[1])){
                file2Url = urls[1];
            }
            if (urls.length>2&&!"0".equals(urls[2])){
                file3Url = urls[2];
            }
            if (urls.length>3&&!"0".equals(urls[3])){
                file4Url = urls[3];
            }
            if (urls.length>4&&!"0".equals(urls[4])){
                file5Url = urls[4];
            }
            if (urls.length>5&&!"0".equals(urls[5])){
                file6Url = urls[5];
            }
            if (urls.length>6&&!"0".equals(urls[6])){
                file7Url = urls[6];
            }
            if (urls.length>7&&!"0".equals(urls[7])){
                file8Url = urls[7];
            }
        }
        if (!"".equals(image)) {
            if (istuFirst) {
                if (headThread == null) {
                    headThread = new HeadThread();
                    headThread.start();
                }
            }
        }else {
            if (filesUrl!=null&&!"".equals(filesUrl)&&!filesUrl.equalsIgnoreCase("null")){
                if (isvuFirst) {
                    if (videoThread == null) {
                        videoThread = new VideodThread();
                        videoThread.start();
                    }
                }
            }else {
                if (pd!=null&&pd.isShowing()) {
                    pd.dismiss();
                }
            }
        }

        String vip = user.getVip();

        if (!vip.equals("0")){

            if (user.getVipLevel().equals("1")){

                tv_vip.setBackgroundResource(R.drawable.vip1);

            }else if (user.getVipLevel().equals("2")){
                tv_vip.setBackgroundResource(R.drawable.vip2);
            }else {
                tv_vip.setBackgroundResource(R.drawable.vip3);
            }

        }else {
            tv_vip.setBackgroundResource(R.drawable.viph);
        }

    }

    @Override
    public void showproLoading() {
        srl.setRefreshing(true);
    }

    @Override
    public void hideproLoading() {
        srl.setRefreshing(false);
        if (pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }

    @Override
    public void showproError() {
        srl.setRefreshing(false);
        if (pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
        Toast.makeText(getActivity(),"网络连接中断",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateQiyeInfo(QiYeInfo qiYeInfo) {
        uNation = qiYeInfo.getRemark();
        isqiyeFirst = false;
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

    class HeadThreadt extends Thread{
        @Override
        public void run() {
            super.run();
            if (isFirst&&headThreadt!=null){
                PreferenceManager.init(DemoApplication.getApp());
                PreferenceManager.getInstance().setCurrentUserNick(name);
                PreferenceManager.getInstance().setCurrentUserAvatar(avatar1);
                EMClient.getInstance().updateCurrentUserNick(name);
                Message msg = new Message();
                msg.what = 1;
                myHeadImagehandler.sendMessage(msg);
                isFirst = false;
            }
        }
    }
    @Override
    public void updateQUserOrderList(List<OrderInfo> orderInfos) {
        uAll = 0;
//        xiaofeiCishu = 0;
        for (int i=0;i<orderInfos.size();i++){
            String count = orderInfos.get(i).getOrderState();
            if (!(count.equals("07")||count.equals("09")||count.equals("05")||count.equals("01")||count.equals("02"))){
                uAll++;
            }
//            if (count.equals("05")){
//                xiaofeiCishu++;
//            }
        }
//        tvaxfcs.setText(xiaofeiCishu+"次");
        if (uAll>0){
            tvUnreadChu.setVisibility(View.VISIBLE);
            tvUnreadChu.setText(String.valueOf(uAll));
        }else {
            tvUnreadChu.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateQMerOrderList(List<OrderInfo> orderInfos) {
        mAll=0;
//        jiedanCishu = 0;
        for (int i=0;i<orderInfos.size();i++){
            String count = orderInfos.get(i).getOrderState();
            if (!(count.equals("07")||count.equals("08")||count.equals("09")||count.equals("05")||count.equals("01")||count.equals("02")||count.equals("12"))){
                mAll++;
            }
//            if (count.equals("05")){
//                jiedanCishu++;
//            }
        }
//        tvajdcs.setText(jiedanCishu+"次");
        if (mAll>0){
            tvUnreadRu.setVisibility(View.VISIBLE);
            tvUnreadRu.setText(String.valueOf(mAll));
        }else {
            tvUnreadRu.setVisibility(View.INVISIBLE);
        }
    }
}
