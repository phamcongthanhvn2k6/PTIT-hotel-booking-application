package com.phuc.datvekhachsan.network;

import com.google.gson.JsonObject;
import com.phuc.datvekhachsan.model.Booking;
import com.phuc.datvekhachsan.model.Hotel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/auth/login")
    Call<JsonObject> login(@Body JsonObject request);

    @POST("api/auth/register")
    Call<JsonObject> register(@Body JsonObject request);

    @GET("api/hotels")
    Call<List<Hotel>> getHotels();

    @GET("api/hotels/search")
    Call<List<Hotel>> searchHotels(@Query("keyword") String keyword);

    @GET("api/hotels/{id}")
    Call<Hotel> getHotelById(@Path("id") Long id);

    @POST("api/bookings")
    Call<JsonObject> createBooking(@Body JsonObject request);

    @GET("api/bookings/my-history")
    Call<List<Booking>> getMyHistory();
    
    @POST("api/reviews")
    Call<JsonObject> createReview(@Body JsonObject request);
}
