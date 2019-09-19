package com.sangu.apptongji.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;


public class AddFriendsPreActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx_activity_addfriends_pre);
        this.findViewById(R.id.tv_search).setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
             startActivity(new Intent(AddFriendsPreActivity.this,AddFriendsNextActivity.class));
            }
            
        });
    }
    

}
