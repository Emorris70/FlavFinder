package com.flavfinder.APIdentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoSample {

    @JsonProperty("photo_url")
    private String photoUrl;

    @JsonProperty("photo_url_large")
    private String photoUrlLarge;

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getPhotoUrlLarge() { return photoUrlLarge; }
    public void setPhotoUrlLarge(String photoUrlLarge) { this.photoUrlLarge = photoUrlLarge; }
}