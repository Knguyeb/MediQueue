package com.khoinguyen.mediqueue.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khoinguyen.mediqueue.entity.TaiKhoan;
import com.khoinguyen.mediqueue.helper.PasswordHelper;
import com.khoinguyen.mediqueue.service.TaiKhoanService;

@Controller
public class TaiKhoanController {

    private final TaiKhoanService taiKhoanService;

    public TaiKhoanController(TaiKhoanService taiKhoanService) {
        this.taiKhoanService = taiKhoanService;
    }

    // =================================================================
    // 1. GIAO DIỆN QUẢN LÝ
    // =================================================================
    @GetMapping("/taikhoan/index")
    public String index(Model model) {
        List<TaiKhoan> danhSachTaiKhoan = taiKhoanService.findAll();
        model.addAttribute("danhSachTaiKhoan", danhSachTaiKhoan);
        return "taikhoan/index"; 
    }

    // =================================================================
    // 2. API RESTFUL (Dùng cho Popup / Nút bấm bằng AJAX)
    // =================================================================

    // Khóa / Mở khóa tài khoản (Toggle Status)
    @PutMapping("/api/taikhoan/{id}/toggle-status")
    @ResponseBody
    public ResponseEntity<?> toggleStatus(@PathVariable Integer id) {
        Optional<TaiKhoan> opt = taiKhoanService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        TaiKhoan taiKhoan = opt.get();
        // Đảo ngược trạng thái (true thành false, false thành true)
        taiKhoan.setTrangThai(!taiKhoan.getTrangThai());
        taiKhoanService.save(taiKhoan);
        
        return ResponseEntity.ok(taiKhoan.getTrangThai());
    }

    // Reset mật khẩu về mặc định "123"
    @PutMapping("/api/taikhoan/{id}/reset-password")
    @ResponseBody
    public ResponseEntity<?> resetPassword(@PathVariable Integer id) {
        Optional<TaiKhoan> opt = taiKhoanService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        TaiKhoan taiKhoan = opt.get();
        // Hash lại pass mặc định
        taiKhoan.setMatKhau(PasswordHelper.hashPassword("123"));
        taiKhoanService.save(taiKhoan);
        
        return ResponseEntity.ok("Đã đặt lại mật khẩu thành '123'");
    }
}