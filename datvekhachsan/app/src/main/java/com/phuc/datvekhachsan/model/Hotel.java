package com.phuc.datvekhachsan.model;

import java.io.Serializable;
import java.util.List;

public class Hotel implements Serializable {
    private String name;
    private String description;
    private String location;
    private int imageResId;
    private List<Integer> imageResIds;
    private double rating;
    private double pricePerNight;
    private List<String> amenities;
    private List<Amenity> facilities;

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

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public int getImageResId() { return imageResId; }
    public List<Integer> getImageResIds() { return imageResIds; }
    public double getRating() { return rating; }
    public double getPricePerNight() { return pricePerNight; }
    public List<String> getAmenities() { return amenities; }
    public List<Amenity> getFacilities() { return facilities; }
}
