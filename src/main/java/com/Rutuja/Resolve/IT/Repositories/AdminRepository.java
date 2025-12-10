package com.Rutuja.Resolve.IT.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Rutuja.Resolve.IT.Model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmail(String email);
}
