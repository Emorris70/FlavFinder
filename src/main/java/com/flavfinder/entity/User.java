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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SavedRestaurant> savedRestaurants = new ArrayList<>();

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

    /**
     * Adds a saved restaurant entry.
     *
     * @param savedRestaurant the join entry to add
     */
    public void addSavedRestaurant(SavedRestaurant savedRestaurant) {
        this.savedRestaurants.add(savedRestaurant);
        savedRestaurant.setUser(this);
    }

    /**
     * Removes a saved restaurant entry.
     *
     * @param savedRestaurant the join entry to remove
     */
    public void removeSavedRestaurant(SavedRestaurant savedRestaurant) {
        this.savedRestaurants.remove(savedRestaurant);
        savedRestaurant.setUser(null);
    }


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
}
