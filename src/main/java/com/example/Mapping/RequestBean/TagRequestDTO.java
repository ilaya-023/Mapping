package com.example.Mapping.RequestBean;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagRequestDTO {

    @NotBlank(message = "Tag code is required")
    @Pattern(regexp = "^[A-Z0-9\\-]+$", message = "Tag code must be uppercase alphanumeric with optional dashes")
    private String tagCode;

    @NotBlank(message = "Tag name is required")
    @Size(min = 2, max = 30, message = "Tag name must be between 2 and 30 characters")
    private String tagName;
}