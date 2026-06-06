package com.phuc.datvekhachsan.backend.dto;

public class ReviewRequest {
    private Long hotelId;
    private Integer rating;
    private String comment;

    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
