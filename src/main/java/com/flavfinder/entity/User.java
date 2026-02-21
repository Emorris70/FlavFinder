package com.flavfinder.entity;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    // Ensure the default value is set to user.
    @Column(name = "role", columnDefinition = "VARCHAR(50) DEFAULT 'user'")
    private String role = "user";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Location> location = new ArrayList<>();

    /**
     * Instantiates a new user
     */
    public User() {

    }

    /**
     * Creation of a new user entry.
     *
     * @param firstName The user first name
     * @param email The user email address
     * @param password User hashed password
     */
    public User(String firstName, String email, String password) {
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

    /**
     * Adds a new location
     *
     * @param location the location to add
     */
    public void addLocation(Location location) {
        this.location.add(location);
        location.setUser(this);
    }

    /**
     * Removes a location
     *
     * @param location the location to remove
     */
    public void removeLocation(Location location) {
        this.location.remove(location);
        location.setUser(null);
    }

    /**
     * Get a list of saved locations
     *
     * @return the list of locations
     */
    public List<Location> getLocation() {
        return location;
    }

    /**
     * Sets the list of locations
     *
     * @param location the location to set
     */
    public void setLocation(List<Location> location) {
        this.location = location;
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
     * Gets the user first name
     *
     * @return First name of user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user first name
     *
     * @param firstName The first name of user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the users email
     *
     * @return The users email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user email
     *
     * @param email The email to be set for user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user password
     *
     * @return The user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user password
     *
     * @param password The password to be set
     */
    public void setPassword(String password) {
        this.password = password;
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

    /**
     * Concatenates the user information.
     *
     * @return User info
     */
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
