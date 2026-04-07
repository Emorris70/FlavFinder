package com.flavfinder.entity;
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
    private List<Location> locations = new ArrayList<>();

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
//        this.locations.remove(restaurant);
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
     * Adds a new location
     *
     * @param location the location to add
     */
    public void addLocation(Location location) {
        this.locations.add(location);
        location.setUser(this);
    }

    /**
     * Removes a location
     *
     * @param location the location to remove
     */
    public void removeLocation(Location location) {
        this.locations.remove(location);
        location.setUser(null);
    }

    /**
     * Get a list of saved locations
     *
     * @return the list of locations
     */
    public List<Location> getLocation() {
        return locations;
    }

    /**
     * Sets the list of locations
     *
     * @param location the location to set
     */
    public void setLocation(List<Location> location) {
        this.locations = location;
    }

    /**
     * Gets the user id
     *
     * @return The user id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user id
     *
     * @param id The id to be set for user
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the user sub
     * @return The user sub
     */
    public String getSub() {
        return sub;
    }

    /**
     * Sets the user sub
     * @param sub The sub to be set
     */
    public void setSub(String sub) {
        this.sub = sub;
    }

    /**
     * Gets the use role
     *
     * @return The role of the user
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user role
     *
     * @param role The user role to be set
     */
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(sub, user.sub)
                && Objects.equals(role, user.role)
                && Objects.equals(locations, user.locations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sub, role, locations);
    }
}
