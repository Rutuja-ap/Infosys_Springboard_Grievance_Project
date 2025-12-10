package com.Rutuja.Resolve.IT.Repositories;

import com.Rutuja.Resolve.IT.Model.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
    List<StatusHistory> findByComplaintId(Long complaintId);
}
