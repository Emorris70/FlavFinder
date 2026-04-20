package com.flavfinder.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

/**
 * class to represent the users saved location
 *
 * @author EmileM
 */
@Entity(name = "Location")
@Table(name = "saved_locations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private int id;
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

}
