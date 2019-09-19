package com.sangu.apptongji.main.adapter.group;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.GroupInfo;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017-11-14.
 */

public class GroupListAdapter extends MultiItemTypeAdapter<GroupInfo> {

    public GroupListAdapter(Context context, List<GroupInfo> datas) {
        super(context, datas);
        addItemViewDelegate(0,new GroupOneDelagate());
        addItemViewDelegate(1,new GroupTwoDelagate());
        addItemViewDelegate(2,new GroupThreeDelagate());
        addItemViewDelegate(3,new GroupFourDelagate());
        addItemViewDelegate(4,new GroupFiveDelagate());
    }
}
