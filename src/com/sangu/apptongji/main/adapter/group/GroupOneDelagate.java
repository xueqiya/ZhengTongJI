package com.sangu.apptongji.main.adapter.group;

import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.GroupInfo;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhy on 16/6/22.
 */
public class GroupOneDelagate implements ItemViewDelegate<GroupInfo>
{

    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.item_group1;
    }

    @Override
    public boolean isForViewType(GroupInfo item, int position)
    {
        return item.getType()==0;
    }

    @Override
    public void convert(ViewHolder holder, GroupInfo chatMessage, int position)
    {
        String myUserId = DemoHelper.getInstance().getCurrentUsernName();
        String urls = chatMessage.getGroupImage();
        String [] str;
        List<String> urlList = new ArrayList<>();
        if (urls!=null&&!"".equals(urls)){
            str = urls.split("\\|");
            if (str.length>0) {
                urlList.add(str[0]);
            }
            if (str.length>1) {
                urlList.add(str[1]);
            }
        }
        for (int i=0;i<urlList.size();i++) {
            if (urlList.get(i).indexOf(myUserId) != -1) {
                urlList.remove(i);
                break;
            }
        }
        holder.setText(R.id.tv_group_name, chatMessage.getGroupName());
        if (urlList!=null&&urlList.size()>0) {
            holder.setCirImageUrl(R.id.clv1, FXConstant.URL_AVATAR + urlList.get(0));
        }
    }
}
