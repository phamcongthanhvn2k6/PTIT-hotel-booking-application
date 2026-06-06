package com.phuc.datvekhachsan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomGridAdapter extends RecyclerView.Adapter<RoomGridAdapter.ViewHolder> {
    private List<Room> rooms;
    private final ArrayList<String> selectedRoomNames = new ArrayList<>();
    private final OnRoomSelectedListener listener;

    public interface OnRoomSelectedListener {
        void onRoomSelected(String selectedNames, int count);
    }

    public RoomGridAdapter(List<Room> rooms, OnRoomSelectedListener listener) {
        this.rooms = rooms;
        this.listener = listener;
    }

    public void updateRooms(List<Room> newRooms) {
        this.rooms = newRooms;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomNameTxt;
        TextView roomStatusTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            roomNameTxt = itemView.findViewById(R.id.roomNameTxt);
            roomStatusTxt = itemView.findViewById(R.id.roomStatusTxt);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.roomNameTxt.setText(room.getName());

        switch (room.getStatus()) {
            case AVAILABLE:
                holder.roomStatusTxt.setBackgroundResource(R.drawable.room_available);
                holder.roomStatusTxt.setText(R.string.room_status_available);
                break;
            case SELECTED:
                holder.roomStatusTxt.setBackgroundResource(R.drawable.room_selected);
                holder.roomStatusTxt.setText(R.string.room_status_selected);
                break;
            case UNAVAILABLE:
                holder.roomStatusTxt.setBackgroundResource(R.drawable.room_unavailable);
                holder.roomStatusTxt.setText(R.string.room_status_booked);
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            Room r = rooms.get(pos);
            switch (r.getStatus()) {
                case AVAILABLE:
                    r.setStatus(Room.RoomStatus.SELECTED);
                    selectedRoomNames.add(r.getName());
                    notifyItemChanged(pos);
                    break;
                case SELECTED:
                    r.setStatus(Room.RoomStatus.AVAILABLE);
                    selectedRoomNames.remove(r.getName());
                    notifyItemChanged(pos);
                    break;
                default:
                    break;
            }
            String selected = String.join(", ", selectedRoomNames);
            listener.onRoomSelected(selected, selectedRoomNames.size());
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }
}
