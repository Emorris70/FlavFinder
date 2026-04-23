package com.flavfinder.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

/**
 * Represents a row in the saved_restaurants join table.
 * Links a User to a Restaurant with a timestamp of when it was saved.
 *
 * @author EmileM
 */
@Entity
@Table(name = "saved_restaurants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedRestaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt;

    @PrePersist
    private void prePersist() {
        if (savedAt == null) {
            savedAt = LocalDateTime.now();
        }
    }
}
