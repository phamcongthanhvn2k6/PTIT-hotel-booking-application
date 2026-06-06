package com.phuc.datvekhachsan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.R;

import java.util.List;

public class DetailImageSliderAdapter extends RecyclerView.Adapter<DetailImageSliderAdapter.ViewHolder> {
    private final List<Integer> imageResIds;

    public DetailImageSliderAdapter(List<Integer> imageResIds) {
        this.imageResIds = imageResIds;
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
        holder.imageView.setImageResource(imageResIds.get(position));
    }

    @Override
    public int getItemCount() {
        return imageResIds.size();
    }
}
