package com.sangu.apptongji.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.entity.MerDetail;

import java.util.List;

/**
 * Created by Administrator on 2016-10-25.
 */

public class MerDetailListAdapter extends RecyclerView.Adapter<MerDetailListAdapter.ViewHolder> {
    private List<MerDetail> data;
    private Context context;
    public MyItemClickListener mItemClickListener;
    public MyItemLongClickListener mItemLongClickListener;

    public MerDetailListAdapter(List<MerDetail> data, Context context) {
        super();
        this.data = data;
        this.context = context;
    }

    public void appendData(List<MerDetail> data, Context context) {//分页关键
        this.data.addAll(data);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_merdetail, parent, false);
        return new ViewHolder(v,mItemClickListener,mItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        MerDetail merDetail = data.get(position);
        if (merDetail == null || data.size() == 0) {
            data.remove(position);
            this.notifyDataSetChanged();
        }
        String title = merDetail.getTransaction_type();
        if (title!=null) {
            holder.tv_title.setText("正事多-" + title);
        }
        String create = merDetail.getTimestamp();
        if (create!=null&&create.length()>13){
            String time = create.substring(0, 4) + "-" + create.substring(4, 6) + "-" + create.substring(6, 8) + " "
                    + create.substring(8, 10) + ":" + create.substring(10, 12);
            holder.tv_time.setText(time);
        }
        String jinE = merDetail.getTransaction_amount();
        String type = merDetail.getAcc_type();
        if (type!=null&&"收入".equals(type)){
            double jine = Double.parseDouble(jinE);
            jinE = String.format("%.2f", jine);
            holder.tv_jine.setText("+"+jinE);
        }else {
            double jine = Double.parseDouble(jinE);
            jinE = String.format("%.2f", jine);
            holder.tv_jine.setText("-"+jinE);
            holder.tv_jine.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (data == null || data.size() == 0)?0:data.size();
    }

    public interface MyItemClickListener {
        void onItemClick(View view,int position);
    }
    public interface MyItemLongClickListener {
        void onItemLongClick(View view,int position);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.mItemLongClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView tv_title;
        TextView tv_time;
        TextView tv_jine;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public ViewHolder(View convertView,MyItemClickListener listener,MyItemLongClickListener longClickListener) {
            super(convertView);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_time = (TextView) convertView.findViewById(R.id.tv_from);
            tv_jine = (TextView) convertView.findViewById(R.id.tv_jine);
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

}
