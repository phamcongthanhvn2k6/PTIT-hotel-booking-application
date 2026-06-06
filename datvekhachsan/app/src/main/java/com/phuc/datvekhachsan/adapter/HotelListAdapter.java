package com.phuc.datvekhachsan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuc.datvekhachsan.activity.HotelDetailActivity;
import com.phuc.datvekhachsan.R;
import com.phuc.datvekhachsan.model.Hotel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.ViewHolder> {
    private final List<Hotel> hotels;
    private final boolean isVerticalList;

    public HotelListAdapter(List<Hotel> hotels) {
        this(hotels, false);
    }

    public HotelListAdapter(List<Hotel> hotels, boolean isVerticalList) {
        this.hotels = hotels;
        this.isVerticalList = isVerticalList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hotelImage;
        TextView hotelNameTxt, locationTxt, ratingTxt, priceTxt, descSnippetTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            hotelImage = itemView.findViewById(R.id.hotelImage);
            hotelNameTxt = itemView.findViewById(R.id.hotelNameTxt);
            locationTxt = itemView.findViewById(R.id.locationTxt);
            ratingTxt = itemView.findViewById(R.id.ratingTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            descSnippetTxt = itemView.findViewById(R.id.descSnippetTxt);
        }

        void bind(Hotel hotel) {
            hotelImage.setImageResource(hotel.getImageResId());
            hotelNameTxt.setText(hotel.getName());
            locationTxt.setText(itemView.getContext().getString(R.string.location_with_pin, hotel.getLocation()));
            ratingTxt.setText(String.valueOf(hotel.getRating()));

            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            if (priceTxt != null) {
                priceTxt.setText(itemView.getContext().getString(R.string.price_per_night_format, formatter.format(hotel.getPricePerNight())));
            }

            if (descSnippetTxt != null) {
                descSnippetTxt.setText(hotel.getDescription());
            }

            itemView.setOnClickListener(v -> {
                Context context = itemView.getContext();
                Intent intent = new Intent(context, HotelDetailActivity.class);
                intent.putExtra("hotel", hotel);
                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = isVerticalList ? R.layout.viewholder_hotel_list : R.layout.viewholder_hotel;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(hotels.get(position));
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }
}
