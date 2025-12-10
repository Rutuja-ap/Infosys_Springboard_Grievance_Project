package com.Rutuja.Resolve.IT.Repositories;

import com.Rutuja.Resolve.IT.Model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long>
{ 
    List<Complaint> findByUserId(Long userId);
    List<Complaint> findByOfficerId(Long officerId);
    int countByOfficerId(Long officerId); 
    long countByStatus(String status);
    List<Complaint> findByDateOnlyBetween(LocalDate start, LocalDate end);
    List<Complaint> findAllByOrderByIdDesc();///////////////
    


}
