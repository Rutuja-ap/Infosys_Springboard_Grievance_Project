package com.Rutuja.Resolve.IT.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Rutuja.Resolve.IT.Model.Officer;
@Repository
public interface OfficerRepository extends JpaRepository<Officer, Long> {
    Officer findByEmail(String email);
}
