package com.example.Mapping.Mapper;

import com.example.Mapping.Entity.Product;
import com.example.Mapping.Entity.Rating;
import com.example.Mapping.RequestBean.RatingRequestDTO;
import com.example.Mapping.ResponceBean.RatingResponseDTO;

public class RatingMapper {

    public static Rating toEntity(RatingRequestDTO dto, Product product) {
        if (dto == null || product == null) return null;

        return Rating.builder()
                .ratingCode(dto.getRatingCode())
                .overall_rating(dto.getOverallRating())
                .product(product)
                .build();
    }

    public static RatingResponseDTO toDTO(Rating rating) {
        if (rating == null) return null;

        return RatingResponseDTO.builder()
                .ratingCode(rating.getRatingCode())
                .overallRating(rating.getOverall_rating())
                .build();
    }
}
