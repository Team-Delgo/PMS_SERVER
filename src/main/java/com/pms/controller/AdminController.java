package com.pms.controller;


import com.pms.comm.CommController;
import com.pms.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController extends CommController {

    private final AdminService adminService;

    // 회원탈퇴
    @DeleteMapping("/{adminId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer adminId) {
        adminService.delete(adminId);
        return SuccessReturn();
    }
}
