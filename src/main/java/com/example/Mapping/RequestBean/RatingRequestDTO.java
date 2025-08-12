package com.example.Mapping.RequestBean;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequestDTO {

    @NotBlank(message = "Rating code is required")
    @Pattern(regexp = "^[A-Z0-9\\-]+$", message = "Rating code must be uppercase alphanumeric with optional dashes")
    private String ratingCode;

    @Min(value = 1, message = "Overall rating must be at least 1")
    @Max(value = 5, message = "Overall rating must be at most 5")
    private int overallRating;
}
