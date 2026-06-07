package com.phuc.datvekhachsan.network;

import com.google.gson.JsonObject;
import com.phuc.datvekhachsan.model.Booking;
import com.phuc.datvekhachsan.model.Hotel;
import com.phuc.datvekhachsan.model.Room;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
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

    @GET("api/rooms/hotel/{hotelId}")
    Call<List<Room>> getRoomsByHotelId(
        @Path("hotelId") Long hotelId,
        @Query("checkInDate") String checkInDate
    );

    @POST("api/bookings")
    Call<JsonObject> createBooking(@Body JsonObject request);

    @GET("api/bookings/my-history")
    Call<List<Booking>> getMyHistory();

    @GET("api/user/profile")
    Call<JsonObject> getProfile();

    @PUT("api/user/profile")
    Call<JsonObject> updateProfile(@Body JsonObject request);

    @GET("api/user/favorites")
    Call<List<Hotel>> getFavoriteHotels();

    @POST("api/user/favorites/{id}")
    Call<JsonObject> addFavoriteHotel(@Path("id") Long hotelId);

    @DELETE("api/user/favorites/{id}")
    Call<JsonObject> removeFavoriteHotel(@Path("id") Long hotelId);

    @POST("api/reviews")
    Call<JsonObject> createReview(@Body JsonObject request);

    @retrofit2.http.Multipart
    @POST("api/user/profile/avatar")
    Call<JsonObject> uploadAvatar(@retrofit2.http.Part okhttp3.MultipartBody.Part file);
}
