package com.khoinguyen.mediqueue.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khoinguyen.mediqueue.entity.BacSi;
import com.khoinguyen.mediqueue.service.BacSiService;
import com.khoinguyen.mediqueue.service.BenhVienService;
import com.khoinguyen.mediqueue.service.DanhMucChuyenKhoaService;

@Controller
@RequestMapping("/bacsi")
public class BacSiController {

    private final BacSiService bacSiService;
    private final BenhVienService benhVienService;
    private final DanhMucChuyenKhoaService danhMucService;

    // Inject cả 3 Service để lấy dữ liệu liên kết
    public BacSiController(BacSiService bacSiService, 
                           BenhVienService benhVienService, 
                           DanhMucChuyenKhoaService danhMucService) {
        this.bacSiService = bacSiService;
        this.benhVienService = benhVienService;
        this.danhMucService = danhMucService;
    }

    // =================================================================
    // 1. HIỂN THỊ DANH SÁCH BÁC SĨ
    // =================================================================
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("danhSachBacSi", bacSiService.findAll());
        return "bacsi/index"; 
    }

    // =================================================================
    // 2. THÊM MỚI BÁC SĨ
    // =================================================================
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("bacSi", new BacSi());
        
        // Truyền danh sách Bệnh viện và Chuyên khoa xuống giao diện để làm Dropdown (thẻ <select>)
        model.addAttribute("danhSachBenhVien", benhVienService.findAll());
        model.addAttribute("danhSachChuyenKhoa", danhMucService.findAll());
        
        return "bacsi/create"; 
    }

    @PostMapping("/create")
    public String saveBacSi(@ModelAttribute BacSi bacSi, RedirectAttributes redirectAttributes) {
        try {
            // Gọi hàm themMoiBacSi (Đã tích hợp tự tạo Account NVA12345 bên trong Service)
            bacSiService.themMoiBacSi(bacSi);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm mới Bác sĩ và cấp tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu: " + e.getMessage());
        }
        return "redirect:/bacsi/index"; 
    }

    // =================================================================
    // 3. CẬP NHẬT THÔNG TIN BÁC SĨ
    // =================================================================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<BacSi> opt = bacSiService.findById(id);
        
        if (opt.isPresent()) {
            model.addAttribute("bacSi", opt.get());
            // Vẫn phải truyền 2 danh sách này xuống để load lại Dropdown
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
        try {
            bacSiService.update(bacSi);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ Bác sĩ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật: " + e.getMessage());
        }
        return "redirect:/bacsi/index"; 
    }

    // =================================================================
    // 4. XÓA BÁC SĨ (Sẽ tự động xóa luôn Tài khoản nhờ CascadeType.ALL)
    // =================================================================
    @PostMapping("/delete/{id}")
    public String deleteBacSi(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            bacSiService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa Bác sĩ và tài khoản liên kết khỏi hệ thống!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa do ràng buộc dữ liệu hệ thống!");
        }
        return "redirect:/bacsi/index";
    }
}