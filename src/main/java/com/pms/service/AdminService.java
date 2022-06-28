package com.pms.service;


import com.pms.domain.Admin;
import com.pms.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입
    public Admin signup(Admin admin) {
        // Email 중복확인
        isEmailExisting(admin.getEmail());
        // 패스워드 암호화 및 적용
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        // Admin Data save
        Admin savedAdmin = adminRepository.save(admin);

        return savedAdmin;
    }

    // 이메일 존재 유무 확인
    public boolean isEmailExisting(String email) {
        Optional<Admin> findAdmin = adminRepository.findByEmail(email);
        return findAdmin.isPresent();
    }


    // 회원탈퇴
    public void deleteAdmin(int adminId) {
        Admin admin = adminRepository.findByAdminId(adminId).orElseThrow(() -> new NullPointerException("NOT EXIST ADMIN"));
        adminRepository.delete(admin);
    }

    // 비밀번호 변경
    public void changePassword(String checkedEmail, String newPassword) {
        Admin admin = adminRepository.findByEmail(checkedEmail).orElseThrow(() -> new NullPointerException("NOT EXIST ADMIN"));
        String encodedPassword = passwordEncoder.encode(newPassword);
        admin.setPassword(encodedPassword);
        adminRepository.save(admin);
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
    }

    public Admin getAdminByAdminId(int userId) {
        return adminRepository.findByAdminId(userId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
    }

    public Admin updateAdminData(Admin admin) {
        return adminRepository.save(admin);
    }

}
