package com.fanxin.easeui.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.fanxin.easeui.adapter.LbsAdapter;
import com.hyphenate.easeui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-07-10.
 */
public class SuggestActivity extends Activity implements OnGetSuggestionResultListener {
    private SuggestionSearch mSuggestionSearch = null;
    private EditText et_search;
    private TextView tv_quxiao;
    private LbsAdapter adapter_list_Address;
    private ListView lvAddress;
    private List<SuggestionResult.SuggestionInfo> listinfo;
    private List<String> list;
    private List<String> lists;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter_list_Address!=null){
            adapter_list_Address = null;
        }
        if (mSuggestionSearch!=null){
            mSuggestionSearch.destroy();
            mSuggestionSearch = null;
        }
        if (listinfo!=null){
            listinfo.clear();
            listinfo=null;
        }
        if (list!=null){
            list.clear();
            list=null;
        }
        if (lists!=null){
            lists.clear();
            lists=null;
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_lbssearch);
        et_search = (EditText) findViewById(R.id.et_search);
        tv_quxiao = (TextView) findViewById(R.id.tv_quxiao);
        lvAddress = (ListView) findViewById(R.id.lvAddress);
        list = new ArrayList<>();
        listinfo = new ArrayList<>();
        lists = new ArrayList<>();
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        final String city = this.getIntent().getStringExtra("city");
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                if (cs.length() <= 0) {
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(cs.toString()).city(city));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult msg) {
        if (msg == null || msg.getAllSuggestions() == null) {
            Toast.makeText(this, "未检索到当前地址",             Toast.LENGTH_SHORT).show();
            return;
        }
        if (list != null) {
            list.clear();
        }
        if (lists != null) {
            lists.clear();
        }
        if (listinfo != null) {
            listinfo.clear();
        }
        for (SuggestionResult.SuggestionInfo info : msg.getAllSuggestions()) {
            if (info.pt == null) continue;
            listinfo.add(info);
            list.add(info.key);
            lists.add(info.city + info.district + info.key);
        }
        adapter_list_Address = new LbsAdapter(SuggestActivity.this, list, lists);
        lvAddress.setAdapter(adapter_list_Address);
        adapter_list_Address.notifyDataSetChanged();
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Log.e("suggestac,ci",listinfo.get(position).city);
                Log.e("suggestac,key",listinfo.get(position).key);
                Log.e("suggestac,dizhi",lists.get(position));
                intent.putExtra("city",listinfo.get(position).city);
                intent.putExtra("key",listinfo.get(position).key);
                intent.putExtra("dizhi",lists.get(position));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        if (listinfo.size() == 0) {
            Toast.makeText(SuggestActivity.this, "未检索到当前地址", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}
