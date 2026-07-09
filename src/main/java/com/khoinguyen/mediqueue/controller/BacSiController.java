package com.khoinguyen.mediqueue.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khoinguyen.mediqueue.entity.BacSi;
import com.khoinguyen.mediqueue.entity.BenhVien;
import com.khoinguyen.mediqueue.service.BacSiService;
import com.khoinguyen.mediqueue.service.BenhVienService;
import com.khoinguyen.mediqueue.service.DanhMucChuyenKhoaService;

@Controller
@RequestMapping("/bacsi")
public class BacSiController {

    private final BacSiService bacSiService;
    private final BenhVienService benhVienService;
    private final DanhMucChuyenKhoaService danhMucService;

    public BacSiController(BacSiService bacSiService, 
                           BenhVienService benhVienService, 
                           DanhMucChuyenKhoaService danhMucService) {
        this.bacSiService = bacSiService;
        this.benhVienService = benhVienService;
        this.danhMucService = danhMucService;
    }

    // =================================================================
    // 1. TRANG TỔNG: HIỂN THỊ TẤT CẢ BÁC SĨ
    // =================================================================
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("danhSachBacSi", bacSiService.findAll());
        return "bacsi/index"; 
    }

    // =================================================================
    // 2. TRANG RIÊNG: HIỂN THỊ BÁC SĨ THEO BỆNH VIỆN
    // =================================================================
    @GetMapping("/theobenhvien")
    public String theoBenhVien(@RequestParam(required = false) Integer maBenhVien, Model model, RedirectAttributes redirectAttributes) {
        if (maBenhVien == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn Bệnh viện để xem danh sách Bác sĩ!");
            return "redirect:/benhvien/index";
        }

        // Lọc bác sĩ theo bệnh viện
        List<BacSi> danhSachTheoBV = bacSiService.findAll().stream()
                .filter(bs -> bs.getBenhVien() != null && bs.getBenhVien().getMaBenhVien().equals(maBenhVien))
                .collect(Collectors.toList());
                
        model.addAttribute("danhSachBacSi", danhSachTheoBV);

        // Truyền tên bệnh viện và mã BV xuống View
        benhVienService.findById(maBenhVien).ifPresent(bv -> {
            model.addAttribute("benhVienChuQuan", bv.getTenBenhVien());
            model.addAttribute("maBenhVien", maBenhVien); 
        });

        return "bacsi/theobenhvien"; // Trỏ tới 1 file HTML riêng
    }

    // =================================================================
    // 3. THÊM MỚI BÁC SĨ (DÙNG CHUNG CHO CẢ 2 TRANG)
    // =================================================================
    @GetMapping("/create")
    public String create(@RequestParam(required = false) Integer maBenhVien, Model model) {
        BacSi bs = new BacSi();
        
        // Nếu có mã truyền vào (tức là bấm từ Trang riêng) -> Set sẵn bệnh viện để khóa <select>
        if (maBenhVien != null) {
            BenhVien bv = new BenhVien();
            bv.setMaBenhVien(maBenhVien);
            bs.setBenhVien(bv);
        }
        
        model.addAttribute("bacSi", bs);
        model.addAttribute("danhSachBenhVien", benhVienService.findAll());
        model.addAttribute("danhSachChuyenKhoa", danhMucService.findAll());
        
        return "bacsi/create"; 
    }

    @PostMapping("/create")
    public String saveBacSi(@ModelAttribute BacSi bacSi, RedirectAttributes redirectAttributes) {
        Integer maBv = bacSi.getBenhVien() != null ? bacSi.getBenhVien().getMaBenhVien() : null;
        try {
            bacSiService.themMoiBacSi(bacSi);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm mới Bác sĩ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
        }
        
        // Luôn điều hướng về xem danh sách bác sĩ của bệnh viện vừa thêm
        if (maBv != null) {
            return "redirect:/bacsi/theobenhvien?maBenhVien=" + maBv;
        }
        return "redirect:/bacsi/index"; 
    }

    // =================================================================
    // 4. CẬP NHẬT THÔNG TIN BÁC SĨ
    // =================================================================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<BacSi> opt = bacSiService.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("bacSi", opt.get());
            model.addAttribute("danhSachBenhVien", benhVienService.findAll());
            model.addAttribute("danhSachChuyenKhoa", danhMucService.findAll());
            return "bacsi/edit"; 
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hồ sơ Bác sĩ!");
            return "redirect:/bacsi/index";
        }
    }

    @PostMapping("/edit")
    public String updateBacSi(@ModelAttribute BacSi bacSi, RedirectAttributes redirectAttributes) {
        Integer maBv = bacSi.getBenhVien() != null ? bacSi.getBenhVien().getMaBenhVien() : null;
        try {
            // ================= THÊM ĐOẠN CODE NÀY =================
            // Tìm lại hồ sơ bác sĩ cũ trong database để lấy lại tài khoản
            Optional<BacSi> bacSiCu = bacSiService.findById(bacSi.getMaBacSi());
            if (bacSiCu.isPresent()) {
                // Gán lại tài khoản cũ vào object mới submit từ form lên
                bacSi.setTaiKhoan(bacSiCu.get().getTaiKhoan());
            }
            // ======================================================

            bacSiService.update(bacSi);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ Bác sĩ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
        }
        
        if (maBv != null) {
            return "redirect:/bacsi/theobenhvien?maBenhVien=" + maBv;
        }
        return "redirect:/bacsi/index"; 
    }

   // =================================================================
    // 5. XÓA BÁC SĨ
    // =================================================================
    @PostMapping("/delete/{id}")
    public String deleteBacSi(@PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String redirectUrl = "redirect:" + (referer != null ? referer : "/bacsi/index");

        // 1. Lấy thông tin bác sĩ lên trước để kiểm tra
        Optional<BacSi> opt = bacSiService.findById(id);
        
        if (opt.isPresent()) {
            BacSi bacSi = opt.get();
            
            // 2. Kiểm tra ràng buộc Phòng Khám
            if (bacSi.getPhongKham() != null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa: Bác sĩ này đang được phân công tại phòng khám, có dữ liệu liên quan!");
                return redirectUrl; // Dừng lại và quay về trang cũ ngay lập tức
            }

            // 3. Nếu không vướng phòng khám thì tiến hành xóa
            try {
                // Nhờ có cascade = CascadeType.ALL ở Entity, thao tác này sẽ xóa luôn TaiKhoan
                bacSiService.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage", "Đã xóa hồ sơ Bác sĩ và tài khoản liên kết thành công!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa do ràng buộc dữ liệu khác trong hệ thống!");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hồ sơ Bác sĩ để xóa!");
        }
        
        return redirectUrl;
    }
}