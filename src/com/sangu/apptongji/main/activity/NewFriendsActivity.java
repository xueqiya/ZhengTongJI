package com.sangu.apptongji.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.db.InviteMessgeDao;
import com.sangu.apptongji.domain.InviteMessage;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.NewFriendsAdapter;
import com.sangu.apptongji.main.alluser.entity.Friend;
import com.sangu.apptongji.main.alluser.presenter.IFriendPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.FriendQPresenter;
import com.sangu.apptongji.main.alluser.view.IFriendView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 申请与通知
 * 
 */
public class NewFriendsActivity extends BaseActivity implements IFriendView{
    private IFriendPresenter friendPresenter;
    private ListView listView;
    private List<InviteMessage> msgs;
    private List<Friend> list ;
    private InviteMessgeDao dao;
    private NewFriendsAdapter adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (msgs!=null){
            msgs.clear();
            msgs=null;
        }
        if (list!=null){
            list.clear();
            list=null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx_activity_new_friends);
        friendPresenter = new FriendQPresenter(this,this);
        listView = (ListView) findViewById(R.id.listview);
        TextView et_search = (TextView) findViewById(R.id.et_search);
        TextView tv_add = (TextView) findViewById(R.id.tv_add);
        friendPresenter.loadFriendQList();
        et_search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewFriendsActivity.this,
                        AddFriendsNextActivity.class));
            }

        });
        tv_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewFriendsActivity.this,
                        AddFriendsNextActivity.class));
            }

        });
        dao = new InviteMessgeDao(this);
        msgs = dao.getMessagesList();
        deletePush();
    }

    private void deletePush() {
        String url = FXConstant.URL_DELETE_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                param.put("type","02");
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
   
    public void back(View v) {
        finish();
    }

    @Override
    public void updateFriendQList(List<Friend> friends) {
        // 设置adapter
        this.list=friends;
        if (list == null) {
            return;
        }
        adapter = new NewFriendsAdapter(this,msgs,list);
        listView.setAdapter(adapter);
        dao.saveUnreadMessageCount(0);
    }
}
