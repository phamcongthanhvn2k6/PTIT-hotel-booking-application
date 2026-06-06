package com.phuc.datvekhachsan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Amenity;

import java.util.List;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.ViewHolder> {
    private final List<Amenity> facilities;

    public FacilityAdapter(List<Amenity> facilities) {
        this.facilities = facilities;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView facilityIcon;
        TextView facilityNameTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            facilityIcon = itemView.findViewById(R.id.facilityIcon);
            facilityNameTxt = itemView.findViewById(R.id.facilityNameTxt);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_facility, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Amenity facility = facilities.get(position);
        holder.facilityIcon.setImageResource(facility.getIconResId());
        holder.facilityNameTxt.setText(facility.getName());
    }

    @Override
    public int getItemCount() {
        return facilities.size();
    }
}
