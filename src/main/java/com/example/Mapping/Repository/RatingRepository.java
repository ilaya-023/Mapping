package com.example.Mapping.Repository;

import com.example.Mapping.Entity.Product;
import com.example.Mapping.Entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
}
