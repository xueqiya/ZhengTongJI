package com.sangu.apptongji.main.moments;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2016-12-02.
 */

public class BiImageActivity extends BaseActivity {
    private PhotoView iv_image;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_big_image);
        iv_image = (PhotoView) findViewById(R.id.image);
        String imageIndex = this.getIntent().getStringExtra("imageIndex");
        String imageUrl = this.getIntent().getStringExtra("imageUrl");
        String imagePath = this.getIntent().getStringExtra("imagePath");
        if (imageUrl!=null&&!"".equalsIgnoreCase(imageUrl)){
            ImageLoader.getInstance().displayImage(imageUrl,iv_image, DemoApplication.mOptions2);
        }else if (imagePath!=null&&!"".equalsIgnoreCase(imagePath)){
            iv_image.setImageURI(Uri.fromFile(new File(imagePath)));
        }else {
            if (imageIndex.equals("01")) {
                iv_image.setImageResource(R.drawable.xiadanmoshiyi);
            }else if (imageIndex.equals("02")){
                iv_image.setImageResource(R.drawable.xiadanmoshier);
            }else if (imageIndex.equals("03")){
                iv_image.setImageResource(R.drawable.xiadanmoshisan);
            }else if (imageIndex.equals("04")){
                iv_image.setImageResource(R.drawable.xiadanmoshisi);
            }else if (imageIndex.equals("05")){
                iv_image.setImageResource(R.drawable.xiadanmoshiwu);
            }
        }
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
}
