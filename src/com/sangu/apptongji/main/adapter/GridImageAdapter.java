package com.sangu.apptongji.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.recycle.dragrecyclerview.helper.MyItemTouchCallback;
import com.sangu.apptongji.R;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017-06-30.
 */

public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.ViewHolder> implements MyItemTouchCallback.ItemTouchAdapter {
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private List<LocalMedia> list = new ArrayList<>();
    private int selectMax = 9;
    private int type = 0;

    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition==list.size() || toPosition==list.size()){
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public interface onAddPicClickListener {
        void onAddPicClick(int type, int position);
    }

    public GridImageAdapter(Context context, onAddPicClickListener mOnAddPicClickListener) {
        mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public void setList(List<LocalMedia> list) {
        this.list = list;
    }

    public void setType(int type) {
        this.type = type;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        ImageView iv_video;
        LinearLayout ll_del;

        public ViewHolder(View view) {
            super(view);
            mImg = (ImageView) view.findViewById(R.id.fiv);
            iv_video = (ImageView) view.findViewById(R.id.iv_video);
            ll_del = (LinearLayout) view.findViewById(R.id.ll_del);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.gv_filter_image,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        //itemView 的点击事件
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(viewHolder.getAdapterPosition(), v);
                }
            });
        }
        return viewHolder;
    }

    private boolean isShowAddItem(int position) {
        int size = list.size() == 0 ? 0 : list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //少于8张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            if (type==1) {
                viewHolder.mImg.setImageResource(R.drawable.fuwuqian);
            }else if (type==2){
                viewHolder.mImg.setImageResource(R.drawable.fuwuhou);
            }else {
                viewHolder.mImg.setImageResource(R.drawable.fx_icon_add);
            }
            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAddPicClickListener.onAddPicClick(0, viewHolder.getAdapterPosition());
                }
            });
            viewHolder.ll_del.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.ll_del.setVisibility(View.VISIBLE);
            viewHolder.ll_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnAddPicClickListener.onAddPicClick(1, viewHolder.getAdapterPosition());
                }
            });
            LocalMedia media = list.get(position);
            int type = media.getType();
            String path = "";
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图
                path = media.getPath();
            }

            switch (type) {
                case 1:
                    // 图片
                    if (media.isCompressed()) {
                        Log.i("compress image result", new File(media.getCompressPath()).length() / 1024 + "k");
                        Log.i("原图地址::", media.getPath());
                        Log.i("压缩地址::", media.getCompressPath());
                    }
                    viewHolder.iv_video.setVisibility(View.GONE);
                    Glide.with(viewHolder.itemView.getContext())
                            .load(path)
                            .centerCrop()
                            .placeholder(R.color.color_f6)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(viewHolder.mImg);
                    break;
                case 2:
                    // 视频
                    Log.i("时长:", media.getDuration() + "");
                    viewHolder.iv_video.setVisibility(View.VISIBLE);
                    Glide.with(viewHolder.itemView.getContext()).load(path).thumbnail(0.5f).into(viewHolder.mImg);
                    break;
                default:

                    break;
            }

        }
    }

    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
