package com.sangu.apptongji.main.zhengshiinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fanxin.easeui.EaseConstant;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.ChatActivity;

/**
 * Created by Administrator on 2017-01-24.
 */

public class KefuActivity extends BaseActivity {
    private RelativeLayout rl_kefu;
    private ImageView ivhead;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_kefu);
        rl_kefu = (RelativeLayout) findViewById(R.id.rl_kefu);
        ivhead = (ImageView) findViewById(R.id.iv_head);
        rl_kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KefuActivity.this, ChatActivity.class);
                intent.putExtra("userId", "22222222222");
                intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                intent.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                intent.putExtra(EaseConstant.EXTRA_USER_IMG,"zhengshiduo.png");
                intent.putExtra(EaseConstant.EXTRA_USER_NAME,"正事多客服");
                intent.putExtra(EaseConstant.EXTRA_USER_SHARERED,"无");
                startActivity(intent);
            }
        });
        ivhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KefuActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,"22222222222"));
            }
        });
    }
}
