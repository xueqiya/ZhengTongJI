package com.sangu.apptongji.main.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;

/**
 * Created by Administrator on 2018-08-21.
 */

public class CeshiActivity extends BaseActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ceshi);

        imageView = (ImageView) findViewById(R.id.image_source);

       String type = getIntent().getExtras().getString("type");

        if (type.equals("11")){

            imageView.setImageResource(R.drawable.orderintrduct1);

        }else if (type.equals("12")){

            imageView.setImageResource(R.drawable.orderintrduct3);

        }else if (type.equals("13")){

            imageView.setImageResource(R.drawable.orderintrduct2);

        }else if (type.equals("14")){
            imageView.setImageResource(R.drawable.orderintrduct4);
        }

    }
}
