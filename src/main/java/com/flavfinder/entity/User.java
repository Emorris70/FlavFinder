package com.flavfinder.entity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class to represent a user
 *
 * @author EmileM
 */
@Entity
@Table(name="user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private int id;

    @Column(name = "cognito_sub", unique = true, nullable = false)
    private String sub;

    @Column(name = "role", columnDefinition = "VARCHAR(50) DEFAULT 'user'")
    private String role = "user";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SavedLocation> savedLocations = new ArrayList<>();

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<SavedRestaurants> restaurants = new ArrayList<>();

    /**
     * Instantiates a new user
     */
    public User() {

    }

    /**
     * Creation of a new user entry.
     *
     * @param sub The user's cognito sub
     */
    public User(String sub) {
        this.sub = sub;
    }

//    /**
//     * Adds a new restaurant
//     *
//     * @param restaurant the restaurant to add
//     */
//    private void addRestaurant(SavedRestaurants restaurant) {
//        this.restaurants.add(restaurant);
//        restaurant.setUser(this);
//    }
//
//    /**
//     * Removes a restaurant
//     *
//     * @param restaurant the restaurant to remove
//     */
//    private void removeRestaurant(SavedRestaurants restaurant) {
//        this.savedLocations.remove(restaurant);
//        restaurant.setUser(null);
//    }
//
//    /**
//     * Gets a list of all saved restaurants
//     *
//     * @return list of restaurants
//     */
//    public List<SavedRestaurants> getRestaurants() {
//        return restaurants;
//    }
//
//    /**
//     * Sets the list of restaurants
//     *
//     * @param restaurants the restaurant to set
//     */
//    public void setRestaurants(List<SavedRestaurants> restaurants) {
//        this.restaurants = restaurants;
//    }

    /**
     * Adds a new savedLocation
     *
     * @param savedLocation the savedLocation to add
     */
    public void addLocation(SavedLocation savedLocation) {
        this.savedLocations.add(savedLocation);
        savedLocation.setUser(this);
    }

    /**
     * Removes a savedLocation
     *
     * @param savedLocation the savedLocation to remove
     */
    public void removeLocation(SavedLocation savedLocation) {
        this.savedLocations.remove(savedLocation);
        savedLocation.setUser(null);
    }

    /**
     * Get a list of saved savedLocations
     *
     * @return the list of savedLocations
     */
    public List<SavedLocation> getLocation() {
        return savedLocations;
    }

    /**
     * Sets the list of savedLocations
     *
     * @param savedLocation the savedLocation to set
     */
    public void setLocation(List<SavedLocation> savedLocation) {
        this.savedLocations = savedLocation;
    }

}
