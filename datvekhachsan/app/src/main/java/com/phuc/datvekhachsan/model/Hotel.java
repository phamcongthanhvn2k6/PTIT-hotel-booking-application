package com.phuc.datvekhachsan.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Hotel implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("location")
    private String location;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("rating")
    private double rating;

    @SerializedName("price")
    private double pricePerNight;

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("longitude")
    private Double longitude;

    // For MockData compatibility
    private int imageResId;
    private List<Integer> imageResIds;
    private transient List<String> amenities;

    @SerializedName("amenities")
    private List<Amenity> facilities;

    public Hotel() {}

    public Hotel(String name, String description, String location, int imageResId,
                 double rating, double pricePerNight, List<String> amenities, List<Amenity> facilities) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.imageResId = imageResId;
        this.rating = rating;
        this.pricePerNight = pricePerNight;
        this.amenities = amenities;
        this.facilities = facilities;
        this.imageResIds = java.util.Arrays.asList(
                imageResId,
                com.phuc.datvekhachsan.R.drawable.hotel_intro,
                imageResId,
                com.phuc.datvekhachsan.R.drawable.hotel_intro,
                imageResId
        );
    }

    public Hotel(String name, String description, String location, int imageResId, List<Integer> imageResIds,
                 double rating, double pricePerNight, List<String> amenities, List<Amenity> facilities) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.imageResId = imageResId;
        this.imageResIds = imageResIds;
        this.rating = rating;
        this.pricePerNight = pricePerNight;
        this.amenities = amenities;
        this.facilities = facilities;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public List<Amenity> getFacilities() { return facilities; }
    public void setFacilities(List<Amenity> facilities) { this.facilities = facilities; }

    public int getImageResId() { return imageResId; }
    public List<Integer> getImageResIds() { return imageResIds; }
    public List<String> getAmenities() {
        if (amenities != null) return amenities;
        if (facilities != null) {
            List<String> list = new java.util.ArrayList<>();
            for (Amenity a : facilities) {
                list.add(a.getName());
            }
            return list;
        }
        return new java.util.ArrayList<>();
    }
}
