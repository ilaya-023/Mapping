package com.example.Mapping.ResponceBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private String productCode;
    private String productName;
    private String productBrand;

    private List<FeedbackResponseDTO> feedbacks;
    private RatingResponseDTO rating;
    private Set<TagResponseDTO> tags;
}
