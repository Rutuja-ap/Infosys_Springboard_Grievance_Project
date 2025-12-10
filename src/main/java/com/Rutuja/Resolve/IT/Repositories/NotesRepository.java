package com.Rutuja.Resolve.IT.Repositories;

import com.Rutuja.Resolve.IT.Model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotesRepository extends JpaRepository<Notes, Long> {
    List<Notes> findByComplaintIdOrderByCreatedAtDesc(Long complaintId);
}
