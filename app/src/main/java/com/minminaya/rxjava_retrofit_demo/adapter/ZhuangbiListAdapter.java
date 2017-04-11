package com.minminaya.rxjava_retrofit_demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.minminaya.rxjava_retrofit_demo.R;
import com.minminaya.rxjava_retrofit_demo.model.ZhuanbiImage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NIWA on 2017/4/7.
 */

public class ZhuangbiListAdapter extends RecyclerView.Adapter<ZhuangbiListAdapter.ViewHolderA> {

    List<ZhuanbiImage> mImages;


    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolderA(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderA holder, int position) {
        ZhuanbiImage image = mImages.get(position);
        Glide.with(holder.itemView.getContext()).load(image.image_url).into(holder.mImageIv);
        holder.mDescriptionTv.setText(image.description);
    }

    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.size();
    }

    /**将数据传入adapter*/
    public void setImages(List<ZhuanbiImage> images){
        this.mImages = images;
        notifyDataSetChanged();
    }

    static class ViewHolderA extends RecyclerView.ViewHolder {
        @BindView(R.id.imageIv)
        ImageView mImageIv;
        @BindView(R.id.descriptionTv)
        TextView mDescriptionTv;

        public ViewHolderA(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
