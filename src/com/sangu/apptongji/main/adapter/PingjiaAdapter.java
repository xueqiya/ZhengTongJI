package com.sangu.apptongji.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.db.UserEvaluationList;

import java.util.List;

/**
 * Created by Administrator on 2017-03-30.
 */

public class PingjiaAdapter extends RecyclerView.Adapter<PingjiaAdapter.ViewHolder> {
    private Context context;
    List<UserEvaluationList> data;
    public MyItemClickListener mItemClickListener;
    public MyItemLongClickListener mItemLongClickListener;

    public PingjiaAdapter(Context context, List<UserEvaluationList> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pingjia,parent,false),mItemClickListener,mItemLongClickListener);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (data == null || data.size() == 0)?0:data.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        UserEvaluationList userEvaluationList = data.get(position);
        final String clickId = userEvaluationList.getCommentId();
        final String name = userEvaluationList.getCommentName();
        final String content = userEvaluationList.getContent();
        String starCount = userEvaluationList.getStarCount();
        String create = userEvaluationList.getTimestamp();
        if (create!=null&&create.length()>11) {
            String time = create.substring(0, 4) + "-" + create.substring(4, 6) + "-" + create.substring(6, 8) + " "
                    + create.substring(8, 10) + ":" + create.substring(10, 12);
            holder.tv_time.setText(time);
        }
        holder.tv_name_pinglun.setText(name);
        holder.tv_pinglun_content.setText(content);
        if (starCount==null||"".equals(starCount)){
            holder.iv1.setImageResource(R.drawable.goods_full);
            holder.iv2.setImageResource(R.drawable.goods_full);
            holder.iv3.setImageResource(R.drawable.goods_full);
            holder.iv4.setImageResource(R.drawable.goods_full);
            holder.iv5.setImageResource(R.drawable.goods_full);
        }
        if (starCount!=null){
            if (Integer.valueOf(starCount)==0){
                holder.iv1.setImageResource(R.drawable.goods_none);
                holder.iv2.setImageResource(R.drawable.goods_none);
                holder.iv3.setImageResource(R.drawable.goods_none);
                holder.iv4.setImageResource(R.drawable.goods_none);
                holder.iv5.setImageResource(R.drawable.goods_none);
            }
            if (Integer.valueOf(starCount)>0){
                holder.iv1.setImageResource(R.drawable.goods_half);
            }
            if (Integer.valueOf(starCount)>1){
                holder.iv1.setImageResource(R.drawable.goods_full);
            }
            if (Integer.valueOf(starCount)>2){
                holder.iv2.setImageResource(R.drawable.goods_half);
            }
            if (Integer.valueOf(starCount)>3){
                holder.iv2.setImageResource(R.drawable.goods_full);
            }
            if (Integer.valueOf(starCount)>4){
                holder.iv3.setImageResource(R.drawable.goods_half);
            }
            if (Integer.valueOf(starCount)>5){
                holder.iv3.setImageResource(R.drawable.goods_full);
            }
            if (Integer.valueOf(starCount)>6){
                holder.iv4.setImageResource(R.drawable.goods_half);
            }
            if (Integer.valueOf(starCount)>7){
                holder.iv4.setImageResource(R.drawable.goods_full);
            }
            if (Integer.valueOf(starCount)>8){
                holder.iv5.setImageResource(R.drawable.goods_half);
            }
            if (Integer.valueOf(starCount)>9){
                holder.iv5.setImageResource(R.drawable.goods_full);
            }
        }
        holder.tv_name_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,clickId));
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView tv_name_pinglun;
        TextView tv_pinglun_content;
        TextView tv_time;
        ImageView iv1;
        ImageView iv2;
        ImageView iv3;
        ImageView iv4;
        ImageView iv5;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public ViewHolder(View convertView, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(convertView);
            tv_name_pinglun = (TextView) convertView.findViewById(R.id.tv_name_pinglun);
            tv_pinglun_content = (TextView) convertView.findViewById(R.id.tv_pinglun_content);
            tv_time = (TextView) convertView.findViewById(R.id.tv_pinglun_time);
            iv1 = (ImageView) convertView.findViewById(R.id.iv1);
            iv2 = (ImageView) convertView.findViewById(R.id.iv2);
            iv3 = (ImageView) convertView.findViewById(R.id.iv3);
            iv4 = (ImageView) convertView.findViewById(R.id.iv4);
            iv5 = (ImageView) convertView.findViewById(R.id.iv5);
            this.mListener=listener;
            this.mLongClickListener=longClickListener;
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mLongClickListener != null){
                mLongClickListener.onItemLongClick(v,getPosition());
            }
            return true;
        }
    }
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.mItemLongClickListener = listener;
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface MyItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

}
