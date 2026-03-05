package com.flavfinder.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Class represents a saved restaurant info card
 *
 * @author EmileM
 */
@Entity(name = "savedRestaurants")
@Table(name = "saved_restaurants")
public class savedRestaurants {
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

    public savedRestaurants() {

    }

    public savedRestaurants(String restaurantsId, String name, String category,
                            String imgUrl, double lat, double lon)
    {
        this.restaurantsId = restaurantsId;
        this.restaurantName = name;
        this.category = category;
        this.imageUrl = imgUrl;


    }
}
