package com.sangu.apptongji.main.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.utils.BitmapUtils;
import com.sangu.apptongji.ui.BaseActivity;

/**
 * Created by Administrator on 2017-06-22.
 */

public class GonggaoDetailActivity extends BaseActivity {
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    private ImageView iv5;
    private ImageView iv6;
    private String imageType;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_gonggao_ddetail);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        iv5 = (ImageView) findViewById(R.id.iv5);
        iv6 = (ImageView) findViewById(R.id.iv6);
        imageType = this.getIntent().getStringExtra("imageType");
        if (imageType.equals("01")){
            Bitmap bitmap = BitmapUtils.readBitMap(this,R.drawable.jiedan_one);
            iv1.setImageBitmap(bitmap);
            Bitmap bitmap2 = BitmapUtils.readBitMap(this,R.drawable.jiedan_two);
            iv2.setImageBitmap(bitmap2);
            Bitmap bitmap3 = BitmapUtils.readBitMap(this,R.drawable.jiedan_three);
            iv3.setImageBitmap(bitmap3);
            Bitmap bitmap4 = BitmapUtils.readBitMap(this,R.drawable.jiedan_four);
            iv4.setImageBitmap(bitmap4);
            Bitmap bitmap5 = BitmapUtils.readBitMap(this,R.drawable.jiedan_five);
            iv5.setImageBitmap(bitmap5);
            Bitmap bitmap6 = BitmapUtils.readBitMap(this,R.drawable.jiedan_six);
            iv6.setImageBitmap(bitmap6);
        }else if (imageType.equals("02")){
            Bitmap bitmap = BitmapUtils.readBitMap(this,R.drawable.red_one);
            iv1.setImageBitmap(bitmap);
            Bitmap bitmap2 = BitmapUtils.readBitMap(this,R.drawable.red_two);
            iv2.setImageBitmap(bitmap2);
            Bitmap bitmap3 = BitmapUtils.readBitMap(this,R.drawable.red_three);
            iv3.setImageBitmap(bitmap3);
            iv4.setVisibility(View.GONE);
            iv5.setVisibility(View.GONE);
            iv6.setVisibility(View.GONE);
        }else if (imageType.equals("03")){
            Bitmap bitmap = BitmapUtils.readBitMap(this,R.drawable.tuiguang_one);
            iv1.setImageBitmap(bitmap);
            Bitmap bitmap2 = BitmapUtils.readBitMap(this,R.drawable.tuiguang_two);
            iv2.setImageBitmap(bitmap2);
            Bitmap bitmap3 = BitmapUtils.readBitMap(this,R.drawable.tuiguang_three);
            iv3.setImageBitmap(bitmap3);
            Bitmap bitmap4 = BitmapUtils.readBitMap(this,R.drawable.tuiguang_four);
            iv4.setImageBitmap(bitmap4);
            iv5.setVisibility(View.GONE);
            iv6.setVisibility(View.GONE);
        }else if (imageType.equals("04")){
            Bitmap bitmap = BitmapUtils.readBitMap(this,R.drawable.sousuo_one);
            iv1.setImageBitmap(bitmap);
            Bitmap bitmap2 = BitmapUtils.readBitMap(this,R.drawable.sousuo_two);
            iv2.setImageBitmap(bitmap2);
            Bitmap bitmap3 = BitmapUtils.readBitMap(this,R.drawable.sousuo_three);
            iv3.setImageBitmap(bitmap3);
            Bitmap bitmap4 = BitmapUtils.readBitMap(this,R.drawable.sousuo_four);
            iv4.setImageBitmap(bitmap4);
            iv5.setVisibility(View.GONE);
            iv6.setVisibility(View.GONE);
        }else {
            Bitmap bitmap = BitmapUtils.readBitMap(this,R.drawable.kaoqin_one);
            iv1.setImageBitmap(bitmap);
            Bitmap bitmap2 = BitmapUtils.readBitMap(this,R.drawable.kaoqin_two);
            iv2.setImageBitmap(bitmap2);
            Bitmap bitmap3 = BitmapUtils.readBitMap(this,R.drawable.kaoqin_three);
            iv3.setImageBitmap(bitmap3);
            iv4.setVisibility(View.GONE);
            iv5.setVisibility(View.GONE);
            iv6.setVisibility(View.GONE);
        }
    }

}
