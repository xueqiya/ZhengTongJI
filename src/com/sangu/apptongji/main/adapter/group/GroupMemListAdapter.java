package com.sangu.apptongji.main.adapter.group;

import android.content.Context;

import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.ui.GroupDetailsActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017-11-16.
 */

public class GroupMemListAdapter extends MultiItemTypeAdapter<UserAll>  {

    public GroupMemListAdapter(Context context, List<UserAll> datas,boolean showDel,GroupDetailsActivity.OnDeleteClickListener onDeleteClickListener) {
        super(context, datas);
        GroupMemDelagate delagate = new GroupMemDelagate(context,showDel,onDeleteClickListener);
        addItemViewDelegate(delagate);
    }

}
