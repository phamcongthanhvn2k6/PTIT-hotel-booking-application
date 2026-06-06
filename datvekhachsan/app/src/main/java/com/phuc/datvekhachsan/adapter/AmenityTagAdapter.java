package com.phuc.datvekhachsan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.R;

import java.util.List;

public class AmenityTagAdapter extends RecyclerView.Adapter<AmenityTagAdapter.ViewHolder> {
    private final List<String> items;

    public AmenityTagAdapter(List<String> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tagTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            tagTxt = itemView.findViewById(R.id.tagTxt);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_tag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tagTxt.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
