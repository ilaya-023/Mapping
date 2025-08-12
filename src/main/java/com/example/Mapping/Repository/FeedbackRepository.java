package com.example.Mapping.Repository;

import com.example.Mapping.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
}
