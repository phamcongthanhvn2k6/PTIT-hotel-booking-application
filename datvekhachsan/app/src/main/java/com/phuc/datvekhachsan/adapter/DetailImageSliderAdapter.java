package com.phuc.datvekhachsan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phuc.datvekhachsan.R;

import java.util.List;

public class DetailImageSliderAdapter extends RecyclerView.Adapter<DetailImageSliderAdapter.ViewHolder> {
    private final List<Object> images;

    public DetailImageSliderAdapter(List<Object> images) {
        this.images = images;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImage);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_slider_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object image = images.get(position);
        if (image instanceof Integer) {
            holder.imageView.setImageResource((Integer) image);
        } else if (image instanceof String) {
            Glide.with(holder.itemView.getContext())
                    .load((String) image)
                    .placeholder(R.drawable.hotel_intro)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
