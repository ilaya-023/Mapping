package com.example.Mapping.RequestBean;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequestDTO {

    @NotBlank(message = "Feedback label is required")
    @Size(min = 2, max = 30, message = "Feedback label must be between 2 and 30 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Feedback label must be alphanumeric with optional dashes")
    private String feedbackLabel;

    @NotBlank(message = "Feedback content is required")
    @Size(max = 500, message = "Feedback content must be less than 500 characters")
    private String feedbackContent;
}