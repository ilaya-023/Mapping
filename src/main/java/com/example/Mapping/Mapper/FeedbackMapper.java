package com.example.Mapping.Mapper;

import com.example.Mapping.Entity.Feedback;
import com.example.Mapping.Entity.Product;
import com.example.Mapping.RequestBean.FeedbackRequestDTO;
import com.example.Mapping.ResponceBean.FeedbackResponseDTO;

public class FeedbackMapper {

    public static Feedback toEntity(FeedbackRequestDTO dto, Product product) {
        if (dto == null || product == null) return null;

        return Feedback.builder()
                .feedback_label(dto.getFeedbackLabel())
                .feedback_content(dto.getFeedbackContent())
                .product(product)
                .build();
    }

    public static FeedbackResponseDTO toDTO(Feedback feedback) {
        if (feedback == null) return null;

        return FeedbackResponseDTO.builder()
                .feedbackLabel(feedback.getFeedback_label())
                .feedbackContent(feedback.getFeedback_content())
                .build();
    }
}
