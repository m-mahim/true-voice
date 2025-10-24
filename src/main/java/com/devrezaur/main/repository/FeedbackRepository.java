package com.devrezaur.main.repository;

import com.devrezaur.main.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findByFeedbackForContainingIgnoreCase(String feedbackFor);
    List<Feedback> findByFeedbackBy(String feedbackBy);
}
