package com.flavfinder.APIdentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessItem {

    @JsonProperty("place_id")
    private String placeId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("rating")
    private double rating;

    @JsonProperty("review_count")
    private int reviewCount;

    @JsonProperty("opening_status")
    private String openingStatus;

    @JsonProperty("type")
    private String type;

    @JsonProperty("price_level")
    private String priceLevel;

    @JsonProperty("full_address")
    private String fullAddress;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("website")
    private String website;

    @JsonProperty("place_link")
    private String placeLink;

    @JsonProperty("booking_link")
    private String bookingLink;

    @JsonProperty("photos_sample")
    private List<PhotoSample> photosSample;

    @JsonProperty("working_hours")
    private Map<String, List<String>> workingHours;

    // Getters and setters
    public String getPlaceId() { return placeId; }
    public void setPlaceId(String placeId) { this.placeId = placeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public String getOpeningStatus() { return openingStatus; }
    public void setOpeningStatus(String openingStatus) { this.openingStatus = openingStatus; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPriceLevel() { return priceLevel; }
    public void setPriceLevel(String priceLevel) { this.priceLevel = priceLevel; }

    public String getFullAddress() { return fullAddress; }
    public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getPlaceLink() { return placeLink; }
    public void setPlaceLink(String placeLink) { this.placeLink = placeLink; }

    public String getBookingLink() { return bookingLink; }
    public void setBookingLink(String bookingLink) { this.bookingLink = bookingLink; }

    public List<PhotoSample> getPhotosSample() { return photosSample; }
    public void setPhotosSample(List<PhotoSample> photosSample) { this.photosSample = photosSample; }

    public Map<String, List<String>> getWorkingHours() { return workingHours; }
    public void setWorkingHours(Map<String, List<String>> workingHours) { this.workingHours = workingHours; }
}