package com.pms.repository;


import com.pms.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByPhoneNo(String phoneNo);
    Optional<Admin> findByAdminId(int userId);
}
