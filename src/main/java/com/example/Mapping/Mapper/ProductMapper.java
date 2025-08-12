package com.example.Mapping.Mapper;

import com.example.Mapping.Entity.*;
import com.example.Mapping.RequestBean.*;
import com.example.Mapping.ResponceBean.*;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    // Convert ProductRequestDTO to Product entity (for saving)
    public static Product toEntity(ProductRequestDTO dto) {
        if (dto == null) return null;

        Product product = Product.builder()
                .productCode(dto.getProductCode())
                .productName(dto.getProductName())
                .product_brand(dto.getProductBrand())
                .build();

        // Map Rating (One-to-One)
        if (dto.getRating() != null) {
            product.setRating(RatingMapper.toEntity(dto.getRating(), product));
        }

        // Map Feedbacks (One-to-Many)
        if (dto.getFeedbacks() != null) {
            List<Feedback> feedbacks = dto.getFeedbacks().stream()
                    .map(f -> FeedbackMapper.toEntity(f, product))
                    .collect(Collectors.toList());
            product.setFeedbacks(feedbacks);
        }

        // Map Tags (Many-to-Many)
        if (dto.getTags() != null) {
            Set<Tag> tags = dto.getTags().stream()
                    .map(TagMapper::toEntity)
                    .collect(Collectors.toSet());
            product.setTags(tags);
        }

        return product;
    }

    // Convert Product entity to ProductResponseDTO (for output)
    public static ProductResponseDTO toDTO(Product product) {
        if (product == null) return null;

        return ProductResponseDTO.builder()
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .productBrand(product.getProduct_brand())
                .rating(RatingMapper.toDTO(product.getRating()))
                .feedbacks(product.getFeedbacks() != null
                        ? product.getFeedbacks().stream()
                        .map(FeedbackMapper::toDTO)
                        .collect(Collectors.toList())
                        : null)
                .tags(product.getTags() != null
                        ? product.getTags().stream()
                        .map(TagMapper::toDTO)
                        .collect(Collectors.toSet())
                        : null)
                .build();
    }
}
