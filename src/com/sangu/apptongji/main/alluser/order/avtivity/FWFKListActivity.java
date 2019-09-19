package com.sangu.apptongji.main.alluser.order.avtivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.entity.FWFKInfo;
import com.sangu.apptongji.main.alluser.order.adapter.FWFKAdapter;
import com.sangu.apptongji.main.alluser.presenter.IFWFKPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.FWFKPresenter;
import com.sangu.apptongji.main.alluser.view.IFWFKView;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2016-10-19.
 */

public class FWFKListActivity extends BaseActivity implements IFWFKView,View.OnClickListener{
    private Button btn_tianjia;
    private ListView lv_fuwufankui;

    private IFWFKPresenter presenter;
    private List<FWFKInfo> lists;
    private FWFKAdapter adapter;
    private String zy,zhl,dj,xf,biaoti,dafen,jianyi,userSign,merSign,biaoshi,orderId,feedbackSeq,resv5,companyId;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lists!=null){
            lists.clear();
            lists=null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuwulist_activity);
        btn_tianjia = (Button) findViewById(R.id.btn_tianjia);
        lv_fuwufankui = (ListView) findViewById(R.id.lv_fuwufankui);
        biaoshi = this.getIntent().hasExtra("biaoshi")?this.getIntent().getStringExtra("biaoshi"):"00";
        orderId = this.getIntent().hasExtra("orderId")?this.getIntent().getStringExtra("orderId"):"";
        resv5 = this.getIntent().hasExtra("resv5")?this.getIntent().getStringExtra("resv5"):"";
        companyId = this.getIntent().hasExtra("companyId")?this.getIntent().getStringExtra("companyId"):"";
        presenter = new FWFKPresenter(this,this);
        btn_tianjia.setOnClickListener(this);
        if ("00".equals(biaoshi)||"02".equals(biaoshi)){
            btn_tianjia.setVisibility(View.INVISIBLE);
        }
        if (!biaoshi.equals("01")) {
            lv_fuwufankui.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    zy = lists.get(position).getLevel1();
                    feedbackSeq=lists.get(position).getFeedbackSeq();
                    zhl = lists.get(position).getLevel2();
                    dj = lists.get(position).getLevel3();
                    xf = lists.get(position).getLevel4();
                    biaoti = lists.get(position).getUserId();
                    dafen = lists.get(position).getRemark();
                    jianyi = lists.get(position).getAdvice();
                    userSign = lists.get(position).getUserSign();
                    merSign = lists.get(position).getMerSign();
                    if (userSign!=null&&userSign.length()>0){
                        biaoshi = "02";
                    }
                    Intent intent = new Intent(FWFKListActivity.this, FWFKDetailActivity.class);
                    intent.putExtra("zy", zy);
                    intent.putExtra("zhl", zhl);
                    intent.putExtra("dj", dj);
                    intent.putExtra("xf", xf);
                    intent.putExtra("resv5", resv5);
                    intent.putExtra("companyId", companyId);
                    intent.putExtra("biaoti", biaoti);
                    intent.putExtra("feedbackSeq", feedbackSeq);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("biaoshi2", biaoshi);
                    intent.putExtra("biaoshi", "11");
                    intent.putExtra("dafen", dafen);
                    intent.putExtra("jianyi", jianyi);
                    intent.putExtra("userSign", userSign);
                    intent.putExtra("merSign", merSign);
                    startActivityForResult(intent,0);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadFWFKList(orderId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tianjia:
                startActivity(new Intent(FWFKListActivity.this,FWFKDetailActivity.class).putExtra("biaoshi","12").putExtra("orderId",orderId));
                break;
        }
    }

    @Override
    public void updataFuWuFanKuiList(List<FWFKInfo> fuWuFKinfo) {
        this.lists = fuWuFKinfo;
        adapter = new FWFKAdapter(lists,this);
        lv_fuwufankui.setAdapter(adapter);
    }
}
