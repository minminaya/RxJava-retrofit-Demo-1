package com.minminaya.rxjava_retrofit_demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.minminaya.rxjava_retrofit_demo.R;
import com.minminaya.rxjava_retrofit_demo.model.Item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Niwa on 2017/4/9.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolderB> {
    List<Item> images;

    @Override
    public ViewHolderB onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolderB(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderB holder, int position) {
        Item image = images.get(position);
        Glide.with(holder.itemView.getContext()).load(image.imageUrl).into(holder.imageIv);
        holder.descriptionTv.setText(image.description);
    }

    public void setImages(List<Item> images) {
        this.images = images;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    static class ViewHolderB extends RecyclerView.ViewHolder {
        @BindView(R.id.imageIv)
        ImageView imageIv;
        @BindView(R.id.descriptionTv)
        TextView descriptionTv;

        public ViewHolderB(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
