package com.Rutuja.Resolve.IT.Repositories;

import com.Rutuja.Resolve.IT.Model.Feedback;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
     List<Feedback> findByComplaintId(Long complaintId);
    List<Feedback> findByUserId(Long userId);
}
