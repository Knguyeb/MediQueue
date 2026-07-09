package com.khoinguyen.mediqueue.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    // 2. XỬ LÝ NÚT BẤM (DÙNG REDIRECT ATTRIBUTES)
    // =================================================================

    // Khóa / Mở khóa tài khoản
    @PostMapping("/taikhoan/{id}/toggle-status")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes, jakarta.servlet.http.HttpServletRequest request) {
        Optional<TaiKhoan> opt = taiKhoanService.findById(id);
        if (opt.isPresent()) {
            TaiKhoan taiKhoan = opt.get();
            // Đảo ngược trạng thái
            boolean newStatus = !taiKhoan.getTrangThai();
            taiKhoan.setTrangThai(newStatus);
            taiKhoanService.save(taiKhoan);
            
            // Tạo câu thông báo linh hoạt theo trạng thái
            String hanhDong = newStatus ? "Mở khóa" : "Khóa";
            redirectAttributes.addFlashAttribute("successMessage", "Đã " + hanhDong + " tài khoản thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy tài khoản để xử lý!");
        }
        
        // Quay lại trang trước đó (Trang tổng hoặc Trang theo bệnh viện)
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/bacsi/index");
    }

    // Reset mật khẩu về mặc định "123"
    @PostMapping("/taikhoan/{id}/reset-password")
    public String resetPassword(@PathVariable Integer id, RedirectAttributes redirectAttributes, jakarta.servlet.http.HttpServletRequest request) {
        Optional<TaiKhoan> opt = taiKhoanService.findById(id);
        if (opt.isPresent()) {
            TaiKhoan taiKhoan = opt.get();
            taiKhoan.setMatKhau(PasswordHelper.hashPassword("123"));
            taiKhoanService.save(taiKhoan);
            
            redirectAttributes.addFlashAttribute("successMessage", "Đã đặt lại mật khẩu thành '123' thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy tài khoản để xử lý!");
        }
        
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/bacsi/index");
    }
}