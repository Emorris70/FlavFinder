package com.flavfinder.entity;

import com.flavfinder.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * class to represent the users saved location
 *
 * @author EmileM
 */
@Entity(name = "SavedLocation")
@Table(name = "saved_locations")
public class SavedLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private int id;
    @ManyToOne
    private User user;
    // This will be the city name
    @Column(name = "city_name")
    private String cityName;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "latitude")
    private double lat;

    @Column(name = "longitude")
    private double lon;

    @Column(name = "is_default")
    private boolean isDefault;

    /**
     * Instantiates a new saved location.
     */
    public SavedLocation() {

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
    public SavedLocation(String cityName, String zipCode,
                         double lat, double lon, boolean isDefault, User user)
    {
        this.cityName = cityName;
        this.zipCode = zipCode;
        this.lat = lat;
        this.lon = lon;
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
    public double getLat() {
        return lat;
    }

    /**
     * Sets the latitude
     *
     * @param lat the latitude to be set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Gets the longitude
     *
     * @return the longitude
     */
    public double getLon() {
        return lon;
    }

    /**
     * Sets the longitude
     *
     * @param lon longitude to be set
     */
    public void setLon(double lon) {
        this.lon = lon;
    }

    /**
     * Gets the boolean case true or false.
     *
     * @return either true or false.
     */
    public boolean getDefault() {
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
                ", lat=" + lat +
                ", lon=" + lon +
                ", isDefault=" + isDefault +
                '}';
    }
}
