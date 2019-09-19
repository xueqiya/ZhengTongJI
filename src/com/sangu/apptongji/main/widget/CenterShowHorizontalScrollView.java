package com.sangu.apptongji.main.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sangu.apptongji.R;

/**
 * Created by chenglin on 2017-6-23.
 */

public class CenterShowHorizontalScrollView extends HorizontalScrollView {
    private LinearLayout mShowLinear;
    private RelativeLayout rl1;
    private RelativeLayout rl2;
    private RelativeLayout rl3;
    private RelativeLayout rl4;

    public CenterShowHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mShowLinear = new LinearLayout(context);
        mShowLinear.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mShowLinear.setGravity(Gravity.CENTER_VERTICAL);
        this.addView(mShowLinear, params);
    }

    public void onClicked(View v,int pos) {
        if (v.getTag(R.id.item_position) != null) {
            int position = (Integer) v.getTag(R.id.item_position);
            View itemView = mShowLinear.getChildAt(position);
            if (pos==0) {
                rl1.setBackgroundResource(R.color.accent_blue);
                if (rl2!=null){
                    rl2.setBackgroundResource(R.color.gray);
                }
                if (rl3!=null){
                    rl3.setBackgroundResource(R.color.gray);
                }
                if (rl4!=null){
                    rl4.setBackgroundResource(R.color.gray);
                }
            }else if (pos==1){
                rl2.setBackgroundResource(R.color.accent_blue);
                if (rl1!=null){
                    rl1.setBackgroundResource(R.color.gray);
                }
                if (rl3!=null){
                    rl3.setBackgroundResource(R.color.gray);
                }
                if (rl4!=null){
                    rl4.setBackgroundResource(R.color.gray);
                }
            }else if (pos==2){
                rl3.setBackgroundResource(R.color.accent_blue);
                if (rl1!=null){
                    rl1.setBackgroundResource(R.color.gray);
                }
                if (rl2!=null){
                    rl2.setBackgroundResource(R.color.gray);
                }
                if (rl4!=null){
                    rl4.setBackgroundResource(R.color.gray);
                }
            }else if (pos==3){
                rl4.setBackgroundResource(R.color.accent_blue);
                if (rl1!=null){
                    rl1.setBackgroundResource(R.color.gray);
                }
                if (rl3!=null){
                    rl3.setBackgroundResource(R.color.gray);
                }
                if (rl2!=null){
                    rl2.setBackgroundResource(R.color.gray);
                }
            }
            int itemWidth = itemView.getWidth();
            int scrollViewWidth = this.getWidth();
            smoothScrollTo(itemView.getLeft() - (scrollViewWidth / 2 - itemWidth / 2), 0);
        }
    }

    public LinearLayout getLinear() {
        return mShowLinear;
    }

    public void addItemView(View itemView, int position) {
        if (position==0){
            rl1 = (RelativeLayout) itemView.findViewById(R.id.rl_moshi);
            rl1.setBackgroundResource(R.color.accent_blue);
        }else if (position==1){
            rl2 = (RelativeLayout) itemView.findViewById(R.id.rl_moshi);
        }else if (position==2){
            rl3 = (RelativeLayout) itemView.findViewById(R.id.rl_moshi);
        }else if (position==3){
            rl4 = (RelativeLayout) itemView.findViewById(R.id.rl_moshi);
        }
        itemView.setTag(R.id.item_position, position);
        mShowLinear.addView(itemView);
    }


}