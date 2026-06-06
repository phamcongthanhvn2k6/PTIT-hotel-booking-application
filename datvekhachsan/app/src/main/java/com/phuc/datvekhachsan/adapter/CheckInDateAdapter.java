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

public class CheckInDateAdapter extends RecyclerView.Adapter<CheckInDateAdapter.ViewHolder> {
    private final List<String> dates;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;

    public CheckInDateAdapter(List<String> dates) {
        this.dates = dates;
    }

    public boolean hasSelection() {
        return selectedPosition != -1;
    }

    public String getSelectedDate() {
        if (selectedPosition >= 0 && selectedPosition < dates.size()) {
            return dates.get(selectedPosition);
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayTxt, dateMonthTxt;
        View dateLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            dayTxt = itemView.findViewById(R.id.dayTxt);
            dateMonthTxt = itemView.findViewById(R.id.dateMonthTxt);
            dateLayout = itemView.findViewById(R.id.dateLayout);
        }

        void bind(String date) {
            String[] parts = date.split("/");
            if (parts.length == 3) {
                dayTxt.setText(parts[0]);
                dateMonthTxt.setText(itemView.getContext().getString(R.string.date_month_format, parts[1], parts[2]));
            }

            if (selectedPosition == getAdapterPosition()) {
                dateLayout.setBackgroundResource(R.drawable.white_bg);
                dayTxt.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
                dateMonthTxt.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
            } else {
                dateLayout.setBackgroundResource(R.drawable.light_black_bg);
                dayTxt.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                dateMonthTxt.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(dates.get(position));
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
}
