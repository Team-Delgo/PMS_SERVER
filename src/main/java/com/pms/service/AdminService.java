package com.pms.service;


import com.pms.domain.Admin;
import com.pms.dto.AdminReqDTO;
import com.pms.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public Admin register(AdminReqDTO reqDTO) {
        return adminRepository.save(reqDTO.toEntity()
                .formatPhoneNo() // 전화번호 형식 지정
                .changePassword(passwordEncoder.encode(reqDTO.getPassword())) // 패스워드 암호화 및 적용
        );
    }

    // 회원탈퇴
    public void delete(int adminId) {
        adminRepository.deleteById(adminId);
    }

    // 이메일 존재 유무 확인
    public boolean isEmailExisting(String email) {
        return adminRepository.findByEmail(email).isPresent();
    }


    // 비밀번호 변경
    public void changePassword(AdminReqDTO reqDTO) {
        adminRepository.save(getAdminByEmail(reqDTO.getEmail())
                .changePassword(passwordEncoder.encode(reqDTO.getPassword())));
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("NOT FOUND ADMIN"));
    }

    public Admin getAdminById(int userId) {
        return adminRepository.findByAdminId(userId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND ADMIN"));
    }
}
