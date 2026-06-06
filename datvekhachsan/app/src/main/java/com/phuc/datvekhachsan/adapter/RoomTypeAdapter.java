package com.phuc.datvekhachsan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.R;

import java.util.List;

public class RoomTypeAdapter extends RecyclerView.Adapter<RoomTypeAdapter.ViewHolder> {
    private final List<String> roomTypes;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;

    public RoomTypeAdapter(List<String> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public boolean hasSelection() {
        return selectedPosition != -1;
    }

    public String getSelectedRoomType() {
        if (selectedPosition >= 0 && selectedPosition < roomTypes.size()) {
            return roomTypes.get(selectedPosition);
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomTypeTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            roomTypeTxt = itemView.findViewById(R.id.roomTypeTxt);
        }

        void bind(String type) {
            roomTypeTxt.setText(type);

            if (selectedPosition == getAdapterPosition()) {
                roomTypeTxt.setBackgroundResource(R.drawable.white_bg);
                roomTypeTxt.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
            } else {
                roomTypeTxt.setBackgroundResource(R.drawable.light_black_bg);
                roomTypeTxt.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
            }

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    lastSelectedPosition = selectedPosition;
                    selectedPosition = pos;
                    notifyItemChanged(lastSelectedPosition);
                    notifyItemChanged(selectedPosition);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(roomTypes.get(position));
    }

    @Override
    public int getItemCount() {
        return roomTypes.size();
    }
}
