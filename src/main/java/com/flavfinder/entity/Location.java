package com.flavfinder.entity;

import com.flavfinder.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * class to represent the users saved location
 *
 * @author EmileM
 */
@Entity(name = "Location")
@Table(name = "saved_locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private int id;
    // This will be the city name
    @Column(name = "city_name")
    private String cityName;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @Column(name = "is_default")
    private boolean isDefault;
    @ManyToOne
    private User user;

    /**
     * Instantiates a new saved location.
     */
    public Location() {

    }

    /**
     * Instantiates a new saved location. Constructor allows the new
     * entry of a location.
     *
     * @param cityName city name of the current location
     * @param zipCode the zipcode to be stored
     * @param lat the latitude value to be stored
     * @param lon the longitude value to be stored
     * @param isDefault boolean case to be stored
     * @param user the user corresponding to the new location
     */
    public Location(String cityName, String zipCode,
                    double lat, double lon, boolean isDefault, User user)
    {
        this.cityName = cityName;
        this.zipCode = zipCode;
        this.latitude = lat;
        this.longitude = lon;
        this.isDefault = isDefault;
        this.user = user;
    }

    /**
     * Gets the user id
     *
     * @return the user id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id the id to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the user
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user_id
     *
     * @param user sets the user_id
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the city name
     *
     * @return the city name
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Sets the city name
     *
     * @param cityName the city name to be set
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * Gets the zip code.
     *
     * @return gets the zip code.
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Gets the zipcode.
     *
     * @param zipCode the zipcode to be set.
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Gets the latitude
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude
     *
     * @param lat the latitude to be set
     */
    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    /**
     * Gets the longitude
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude
     *
     * @param lon longitude to be set
     */
    public void setLongitude(double lon) {
        this.longitude = lon;
    }

    /**
     * Gets the boolean case true or false.
     *
     * @return either true or false.
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * Sets the boolean case true or false.
     *
     * @param aDefault the boolean case true/false to be set.
     */
    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "SavedLocation{" +
                "id=" + id +
                ", user=" + user +
                ", cityName='" + cityName + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", lat=" + latitude +
                ", lon=" + latitude +
                ", isDefault=" + isDefault +
                '}';
    }
}
