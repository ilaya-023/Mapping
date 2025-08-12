package com.example.Mapping.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "idx_rating_code", columnList = "rating_code"))
public class Rating {

    @Id
    @GeneratedValue
    private UUID rating_id;

    @Column(name = "rating_code", unique = true)
    private String ratingCode;

    private int overall_rating;

    @OneToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o) || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return rating_id != null && rating_id.equals(rating.rating_id);
    }

    @Override
    public int hashCode() {
        return rating_id != null ? rating_id.hashCode() : 0;
    }

}
