package com.flavfinder.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

/**
 * Class represents a saved restaurant info card
 *
 * @author EmileM
 */
@Entity(name = "SavedRestaurants")
@Table(name = "saved_restaurants")
public class SavedRestaurants {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private int id;
    @Column(name = "api_restaurant_id")
    private String restaurantsId;
    @Column(name = "name")
    private String restaurantName;
    @Column(name = "category")
    private String category;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @ManyToOne
    private User user;

    /**
     * Instantiates a new savedRestaurant.
     */
    public SavedRestaurants() {

    }

    /**
     * Instantiates a new savedRestaurants. Constructor allows
     * for snapshot of the saved restaurant.
     *
     * @param restaurantsId restaurant's id
     * @param name restaurants name
     * @param category the category
     * @param imgUrl Unsplash image url matching that category
     * @param lat latitude of that restaurant
     * @param lon longitude of that restaurant
     */
    public SavedRestaurants(String restaurantsId, String name, String category,
                            String imgUrl, double lat, double lon, User user)
    {
        this.restaurantsId = restaurantsId;
        this.restaurantName = name;
        this.category = category;
        this.imageUrl = imgUrl;
        this.latitude = lat;
        this.longitude = lon;
        this.user = user;
    }

    /**
     * Gets the id
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the restaurant id
     *
     * @return the restaurant id
     */
    public String getRestaurantsId() {
        return restaurantsId;
    }

    /**
     * Sets the restaurants id
     *
     * @param restaurantsId the restaurant id to set
     */
    public void setRestaurantsId(String restaurantsId) {
        this.restaurantsId = restaurantsId;
    }

    /**
     * Gets the restaurants name
     *
     * @return the restaurants name
     */
    public String getRestaurantName() {
        return restaurantName;
    }

    /**
     * Sets the restaurants name
     *
     * @param restaurantName
     */
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    /**
     * Gets the category
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category
     *
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the image url
     * @return the image url
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image url
     * @param imageUrl the image url to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the latitude
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the user
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user
     * @param user user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "savedRestaurants{" +
                "id=" + id +
                ", restaurantsId='" + restaurantsId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", category='" + category + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SavedRestaurants that = (SavedRestaurants) o;
        return id == that.id && Double.compare(latitude, that.latitude) == 0
                && Double.compare(longitude, that.longitude) == 0
                && Objects.equals(restaurantsId, that.restaurantsId)
                && Objects.equals(restaurantName, that.restaurantName)
                && Objects.equals(category, that.category)
                && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurantsId, restaurantName, category, imageUrl, latitude, longitude, user);
    }
}