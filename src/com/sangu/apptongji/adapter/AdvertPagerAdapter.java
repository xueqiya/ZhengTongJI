package com.sangu.apptongji.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/11/30.
 */

public class AdvertPagerAdapter extends PagerAdapter {

    private Context context;
    private List<View> mlist;
    public AdvertPagerAdapter.MyItemClickListener mItemClickListener;

    public AdvertPagerAdapter(Context context, List<View> mlists){

        this.context = context;

        mlist = mlists;

    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(mlist.get(position));




        return mlist.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mlist.get(position));
    }


    public interface MyItemClickListener {
        void onItemClick(View view, int position, ImageView v1);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

}
