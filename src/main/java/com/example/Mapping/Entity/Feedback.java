package com.example.Mapping.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "idx_feedback_label", columnList = "feedback_label"))
public class Feedback {

    @Id
    @GeneratedValue
    private UUID feedback_id;

    @Column(name = "feedback_label", unique = true)
    private String feedback_label;

    private String feedback_content;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o)|| getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return feedback_id != null && feedback_id.equals(feedback.feedback_id);
    }

    @Override
    public int hashCode() {
        return feedback_id != null ? feedback_id.hashCode() : 0;
    }

}
