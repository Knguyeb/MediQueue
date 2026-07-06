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

import com.khoinguyen.mediqueue.entity.QuanLy;
import com.khoinguyen.mediqueue.service.BenhVienService;
import com.khoinguyen.mediqueue.service.QuanLyService;

@Controller
@RequestMapping("/quanly")
public class QuanLyController {

    private final QuanLyService quanLyService;
    private final BenhVienService benhVienService;

    // Inject các Service cần thiết
    public QuanLyController(QuanLyService quanLyService, BenhVienService benhVienService) {
        this.quanLyService = quanLyService;
        this.benhVienService = benhVienService;
    }

    // =================================================================
    // 1. HIỂN THỊ DANH SÁCH
    // =================================================================
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("danhSachQuanLy", quanLyService.findAll());
        return "quanly/index"; 
    }

    // =================================================================
    // 2. THÊM MỚI QUẢN LÝ
    // =================================================================
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("quanLy", new QuanLy());
        // Truyền danh sách Bệnh viện xuống để làm Dropdown chọn nơi công tác
        model.addAttribute("danhSachBenhVien", benhVienService.findAll());
        return "quanly/create"; 
    }

    @PostMapping("/create")
    public String saveQuanLy(@ModelAttribute QuanLy quanLy, RedirectAttributes redirectAttributes) {
        try {
            quanLyService.themMoiQuanLy(quanLy);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm mới cán bộ Quản lý và cấp tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu: " + e.getMessage());
        }
        return "redirect:/quanly/index"; 
    }

    // =================================================================
    // 3. CẬP NHẬT THÔNG TIN
    // =================================================================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<QuanLy> opt = quanLyService.findById(id);
        
        if (opt.isPresent()) {
            model.addAttribute("quanLy", opt.get());
            // Load lại danh sách Bệnh viện cho Dropdown
            model.addAttribute("danhSachBenhVien", benhVienService.findAll());
            return "quanly/edit"; 
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hồ sơ Quản lý!");
            return "redirect:/quanly/index";
        }
    }

    @PostMapping("/edit")
    public String updateQuanLy(@ModelAttribute QuanLy quanLy, RedirectAttributes redirectAttributes) {
        try {
            quanLyService.update(quanLy);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ Quản lý thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật: " + e.getMessage());
        }
        return "redirect:/quanly/index"; 
    }

    // =================================================================
    // 4. XÓA QUẢN LÝ
    // =================================================================
    @PostMapping("/delete/{id}")
    public String deleteQuanLy(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            quanLyService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa cán bộ Quản lý và tài khoản liên kết khỏi hệ thống!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa do ràng buộc dữ liệu hệ thống!");
        }
        return "redirect:/quanly/index";
    }
}