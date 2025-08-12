package com.example.Mapping.RequestBean;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Product code is required")
    @Pattern(regexp = "^[A-Z0-9\\-]+$", message = "Product code must be uppercase alphanumeric with optional dashes")
    private String productCode;

    @NotNull(message = "Product name is required")
    @Size(min = 2, max = 50, message = "Product name must be between 2 and 50 characters")
    private String productName;

    @NotNull(message = "Product brand is required")
    private String productBrand;

    private List<FeedbackRequestDTO> feedbacks;
    private RatingRequestDTO rating;
    private Set<TagRequestDTO> tags;
}
