package com.phuc.datvekhachsan.network;

import com.google.gson.JsonObject;
import com.phuc.datvekhachsan.model.Booking;
import com.phuc.datvekhachsan.model.Hotel;
import com.phuc.datvekhachsan.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AdminApiService {

    @GET("api/admin/dashboard/stats")
    Call<JsonObject> getDashboardStats();

    // Hotels
    @GET("api/admin/hotels")
    Call<List<Hotel>> getAllHotels();

    @POST("api/admin/hotels")
    Call<Hotel> addHotel(@Body Hotel hotel);

    @PUT("api/admin/hotels/{id}")
    Call<Hotel> updateHotel(@Path("id") Long id, @Body Hotel hotel);

    @DELETE("api/admin/hotels/{id}")
    Call<JsonObject> deleteHotel(@Path("id") Long id);

    // Rooms
    @GET("api/admin/hotels/{hotelId}/rooms")
    Call<List<com.phuc.datvekhachsan.model.Room>> getRoomsByHotel(@Path("hotelId") Long hotelId);

    @POST("api/admin/hotels/{hotelId}/rooms")
    Call<com.phuc.datvekhachsan.model.Room> addRoom(@Path("hotelId") Long hotelId, @Body com.phuc.datvekhachsan.model.Room room);

    @PUT("api/admin/rooms/{id}")
    Call<com.phuc.datvekhachsan.model.Room> updateRoom(@Path("id") Long id, @Body com.phuc.datvekhachsan.model.Room room);

    @DELETE("api/admin/rooms/{id}")
    Call<JsonObject> deleteRoom(@Path("id") Long id);

    // Users
    @GET("api/admin/users")
    Call<List<User>> getAllUsers();

    @PUT("api/admin/users/{id}/role")
    Call<User> updateUserRole(@Path("id") Long id, @Body JsonObject roleRequest);

    @PUT("api/admin/users/{id}/status")
    Call<JsonObject> updateUserStatus(@Path("id") Long id, @Body JsonObject statusRequest);

    // Bookings
    @GET("api/admin/bookings")
    Call<List<com.phuc.datvekhachsan.model.Booking>> getAllBookings();

    @PUT("api/admin/bookings/{id}/status")
    Call<JsonObject> updateBookingStatus(@Path("id") Long id, @Body JsonObject request);

    // Upload
    @Multipart
    @POST("api/admin/upload")
    Call<JsonObject> uploadImage(@Part MultipartBody.Part file);
}
