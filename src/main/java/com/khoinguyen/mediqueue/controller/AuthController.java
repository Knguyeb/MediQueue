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
import com.khoinguyen.mediqueue.entity.VaiTro; // Nhớ import đúng file Enum của bạn
import com.khoinguyen.mediqueue.helper.PasswordHelper;
import com.khoinguyen.mediqueue.service.BenhNhanService;
import com.khoinguyen.mediqueue.service.BacSiService;
import com.khoinguyen.mediqueue.service.QuanLyService;
import com.khoinguyen.mediqueue.service.TaiKhoanService;

@Controller
public class AuthController {

    private final TaiKhoanService taiKhoanService;
    private final BenhNhanService benhNhanService; // Cần thêm Service này để lưu hồ sơ
    private final BacSiService bacSiService; // Cần thêm Service này để lưu hồ sơ
    private final QuanLyService quanLyService; // Cần thêm Service này để lưu hồ sơ

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
            return "redirect:/"; // Đã đăng nhập thì đá về Trang chủ
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
    public String processLogin(@RequestParam("tenDangNhap") String loginInput, // Đổi tên biến sang loginInput cho rõ nghĩa
                            @RequestParam("matKhau") String matKhau,
                            HttpSession session,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        // Bước 1: Thử tìm kiếm theo Tên đăng nhập trong bảng Tài khoản
        Optional<TaiKhoan> opt = taiKhoanService.findByTenDangNhap(loginInput);

        // Bước 2: Nếu không thấy, thử tìm kiếm theo Số điện thoại trong bảng Bệnh nhân
        if (opt.isEmpty()) {
            // Bạn cần đảm bảo BenhNhanService của bạn đã viết hàm findBySoDienThoai
            Optional<BenhNhan> optBenhNhan = benhNhanService.findBySoDienThoai(loginInput);
            
            if (optBenhNhan.isPresent()) {
                // Nếu tìm thấy bệnh nhân sở hữu số điện thoại này, lấy ra tài khoản liên kết
                opt = Optional.ofNullable(optBenhNhan.get().getTaiKhoan());
            }
        }

        if (opt.isEmpty()) {
            // Cần đảm bảo BacSiService của bạn đã viết hàm findBySoDienThoai
            Optional<BacSi> optBacSi = bacSiService.findBySoDienThoai(loginInput);
            if (optBacSi.isPresent()) {
                opt = Optional.ofNullable(optBacSi.get().getTaiKhoan());
            }
        }

        if (opt.isEmpty()) {
            // Cần đảm bảo QuanLyService của bạn đã viết hàm findBySoDienThoai
            Optional<QuanLy> optQuanLy = quanLyService.findBySoDienThoai(loginInput);
            if (optQuanLy.isPresent()) {
                opt = Optional.ofNullable(optQuanLy.get().getTaiKhoan());
            }
        }


        // Bước 3: Kiểm tra tài khoản và mật khẩu (Giữ nguyên logic cũ)
        if (opt.isPresent()) {
            TaiKhoan taiKhoan = opt.get();
            
            // SỬA DÒNG NÀY: Dùng hàm verifyPassword thay vì .equals()
            if (PasswordHelper.verifyPassword(matKhau, taiKhoan.getMatKhau())) {
                
                if (taiKhoan.getTrangThai() != null && !taiKhoan.getTrangThai()) {
                    model.addAttribute("errorMessage", "Tài khoản của bạn đã bị khóa! Vui lòng liên hệ quản trị viên.");
                    model.addAttribute("savedTenDangNhap", loginInput);
                    return "auth/login";
                }
                
                session.setAttribute("loggedInUser", taiKhoan);
                if (taiKhoan.getVaiTro() != null) {
                    session.setAttribute("QuyenHan", taiKhoan.getVaiTro().toString());
                }
                
                redirectAttributes.addFlashAttribute("successMessage", "Đăng nhập thành công! Chào mừng trở lại.");
                return "redirect:/home";
            }
        }
        
        // Đăng nhập thất bại
        model.addAttribute("errorMessage", "Tài khoản hoặc mật khẩu không chính xác!");
        model.addAttribute("savedTenDangNhap", loginInput); // Trả lại chuỗi vừa gõ lên ô nhập liệu
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

        // 1. Kiểm tra SĐT đã tồn tại chưa
        if (taiKhoanService.findByTenDangNhap(soDienThoai).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Số điện thoại này đã được đăng ký! Vui lòng đăng nhập.");
            return "redirect:/register";
        }

        try {
            // 2. TẠO TÀI KHOẢN TRƯỚC (Để lấy ID liên kết)
            TaiKhoan tk = new TaiKhoan();
            tk.setTenDangNhap(soDienThoai); // Dùng SĐT làm Tên đăng nhập
            tk.setMatKhau(PasswordHelper.hashPassword("123")); // Mật khẩu mặc định là 123
            tk.setVaiTro(VaiTro.BENH_NHAN); 
            tk.setTrangThai(true);

            TaiKhoan savedTaiKhoan = taiKhoanService.save(tk);
            
            // 3. TẠO HỒ SƠ BỆNH NHÂN (Lưu thông tin cá nhân)
            BenhNhan bn = new BenhNhan();
            bn.setHoTen(hoTen);
            bn.setSoDienThoai(soDienThoai);
            bn.setEmail(email);
            bn.setGioiTinh(gioiTinh);
            bn.setNgaySinh(ngaySinh);
            bn.setTaiKhoan(savedTaiKhoan); // Khóa ngoại trỏ về tài khoản vừa tạo
            
            benhNhanService.save(bn); // Lưu xuống DB
             
            // 4. Thông báo thành công và chuyển về trang đăng nhập
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
        session.invalidate(); // Xóa sạch bộ nhớ Session
        redirectAttributes.addFlashAttribute("successMessage", "Đăng xuất thành công!");
        return "redirect:/home"; // Chuyển hướng về trang chủ
    }
}