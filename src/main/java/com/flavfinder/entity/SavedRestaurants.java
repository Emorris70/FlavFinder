package com.flavfinder.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;
// TODO refactor this class
/**
 * Class represents a saved restaurant info card
 *
 * @author EmileM
 */
@Entity(name = "SavedRestaurants")
@Table(name = "restaurants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    // TODO should be a many-many
    @ManyToMany
    private User user;

}