package com.khoinguyen.mediqueue.controller;

import java.time.LocalDate;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.khoinguyen.mediqueue.entity.BenhNhan;
import com.khoinguyen.mediqueue.entity.QuanLy;
import com.khoinguyen.mediqueue.entity.BacSi;
import com.khoinguyen.mediqueue.entity.TaiKhoan;
import com.khoinguyen.mediqueue.entity.VaiTro; 
import com.khoinguyen.mediqueue.helper.PasswordHelper;
import com.khoinguyen.mediqueue.service.BenhNhanService;
import com.khoinguyen.mediqueue.service.BacSiService;
import com.khoinguyen.mediqueue.service.QuanLyService;
import com.khoinguyen.mediqueue.service.TaiKhoanService;

@Controller
public class AuthController {

    private final TaiKhoanService taiKhoanService;
    private final BenhNhanService benhNhanService; 
    private final BacSiService bacSiService; 
    private final QuanLyService quanLyService; 

    public AuthController(TaiKhoanService taiKhoanService, BenhNhanService benhNhanService, BacSiService bacSiService, QuanLyService quanLyService) {
        this.taiKhoanService = taiKhoanService;
        this.benhNhanService = benhNhanService;
        this.bacSiService = bacSiService;
        this.quanLyService = quanLyService;
    }

    // =================================================================
    // 1. HIỂN THỊ GIAO DIỆN
    // =================================================================

    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/"; 
        }
        return "auth/login"; 
    }

    @GetMapping("/register")
    public String showRegisterForm(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/";
        }
        return "auth/register"; 
    }

    // =================================================================
    // 2. XỬ LÝ ĐĂNG NHẬP
    // =================================================================

   @PostMapping("/login")
    public String processLogin(@RequestParam("tenDangNhap") String loginInput, 
                            @RequestParam("matKhau") String matKhau,
                            HttpSession session,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        Optional<TaiKhoan> opt = Optional.empty();
        String tenBenhVien = null;

        // 1. Cố gắng tìm tài khoản bằng SĐT trước (phổ biến nhất)
        Optional<BenhNhan> optBenhNhan = benhNhanService.findBySoDienThoai(loginInput);
        Optional<BacSi> optBacSi = bacSiService.findBySoDienThoai(loginInput);
        Optional<QuanLy> optQuanLy = quanLyService.findBySoDienThoai(loginInput);

        if (optBenhNhan.isPresent()) {
            opt = Optional.ofNullable(optBenhNhan.get().getTaiKhoan());
        } else if (optBacSi.isPresent()) {
            opt = Optional.ofNullable(optBacSi.get().getTaiKhoan());
            if (optBacSi.get().getBenhVien() != null) tenBenhVien = optBacSi.get().getBenhVien().getTenBenhVien();
        } else if (optQuanLy.isPresent()) {
            opt = Optional.ofNullable(optQuanLy.get().getTaiKhoan());
            if (optQuanLy.get().getBenhVien() != null) tenBenhVien = optQuanLy.get().getBenhVien().getTenBenhVien();
        }

        // 2. Nếu không tìm thấy qua SĐT, thì thử tìm bằng Tên đăng nhập
        if (opt.isEmpty()) {
            opt = taiKhoanService.findByTenDangNhap(loginInput);
            
            // Nếu tìm thấy qua Tên đăng nhập, vẫn cố gắng lấy tên bệnh viện
            if (opt.isPresent()) {
                // Thử tìm lại trong bảng Bác Sĩ hoặc Quản lý dựa trên tên đăng nhập
                Optional<BacSi> bs = bacSiService.findByTaiKhoan_TenDangNhap(loginInput);
                Optional<QuanLy> ql = quanLyService.findByTaiKhoan_TenDangNhap(loginInput);
                
                if (bs.isPresent() && bs.get().getBenhVien() != null) tenBenhVien = bs.get().getBenhVien().getTenBenhVien();
                else if (ql.isPresent() && ql.get().getBenhVien() != null) tenBenhVien = ql.get().getBenhVien().getTenBenhVien();
            }
        }

        // 3. Xử lý logic đăng nhập như cũ
        if (opt.isPresent()) {
            TaiKhoan taiKhoan = opt.get();
            if (PasswordHelper.verifyPassword(matKhau, taiKhoan.getMatKhau())) {
                if (taiKhoan.getTrangThai() != null && !taiKhoan.getTrangThai()) {
                    model.addAttribute("errorMessage", "Tài khoản đã bị khóa!");
                    return "auth/login";
                }
                
                session.setAttribute("loggedInUser", taiKhoan);
                if (taiKhoan.getVaiTro() != null) session.setAttribute("QuyenHan", taiKhoan.getVaiTro().toString());
                if (tenBenhVien != null) session.setAttribute("tenBenhVien", tenBenhVien);
                
                redirectAttributes.addFlashAttribute("successMessage", "Đăng nhập thành công!");
                return "redirect:/home";
            }
        }
        
        model.addAttribute("errorMessage", "Tài khoản/SĐT hoặc mật khẩu không chính xác!");
        model.addAttribute("savedTenDangNhap", loginInput); 
        return "auth/login"; 
    }

    // =================================================================
    // 3. XỬ LÝ ĐĂNG KÝ (TỰ ĐỘNG TẠO TÀI KHOẢN)
    // =================================================================

    @PostMapping("/register")
    public String processRegister(
            @RequestParam("hoTen") String hoTen,
            @RequestParam("soDienThoai") String soDienThoai,
            @RequestParam("email") String email,
            @RequestParam("gioiTinh") Boolean gioiTinh,
            @RequestParam("ngaySinh") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngaySinh,
            RedirectAttributes redirectAttributes) {

        if (taiKhoanService.findByTenDangNhap(soDienThoai).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Số điện thoại này đã được đăng ký! Vui lòng đăng nhập.");
            return "redirect:/register";
        }

        try {
            TaiKhoan tk = new TaiKhoan();
            tk.setTenDangNhap(soDienThoai); 
            tk.setMatKhau(PasswordHelper.hashPassword("123")); 
            tk.setVaiTro(VaiTro.BENH_NHAN); 
            tk.setTrangThai(true);

            TaiKhoan savedTaiKhoan = taiKhoanService.save(tk);
            
            BenhNhan bn = new BenhNhan();
            bn.setHoTen(hoTen);
            bn.setSoDienThoai(soDienThoai);
            bn.setEmail(email);
            bn.setGioiTinh(gioiTinh);
            bn.setNgaySinh(ngaySinh);
            bn.setTaiKhoan(savedTaiKhoan); 
            
            benhNhanService.save(bn); 
             
            redirectAttributes.addFlashAttribute("successMessage", 
                "Tạo hồ sơ thành công! <br>Tài khoản: <b>" + soDienThoai + "</b> <br>Mật khẩu: <b>123</b>");
                
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi hệ thống, vui lòng thử lại sau!");
            return "redirect:/register";
        }
    }

    // =================================================================
    // 4. ĐĂNG XUẤT
    // =================================================================
    
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate(); 
        redirectAttributes.addFlashAttribute("successMessage", "Đăng xuất thành công!");
        return "redirect:/home"; 
    }
}