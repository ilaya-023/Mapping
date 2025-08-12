package com.example.Mapping.ResponceBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchResponseDTO {
    private String productCode;
    private String ratingCode;
    private String feedbackLabel;
    private String tagCode;
}

