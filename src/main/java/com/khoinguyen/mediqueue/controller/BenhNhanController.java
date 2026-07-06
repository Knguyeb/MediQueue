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

import com.khoinguyen.mediqueue.entity.BenhNhan;
import com.khoinguyen.mediqueue.service.BenhNhanService;

@Controller
@RequestMapping("/benhnhan")
public class BenhNhanController {

    private final BenhNhanService benhNhanService;

    public BenhNhanController(BenhNhanService benhNhanService) {
        this.benhNhanService = benhNhanService;
    }

    // =================================================================
    // 1. HIỂN THỊ DANH SÁCH BỆNH NHÂN
    // =================================================================
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("danhSachBenhNhan", benhNhanService.findAll());
        return "benhnhan/index"; 
    }

    // =================================================================
    // 2. THÊM MỚI BỆNH NHÂN
    // =================================================================
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("benhNhan", new BenhNhan());
        return "benhnhan/create"; 
    }

    @PostMapping("/create")
    public String saveBenhNhan(@ModelAttribute BenhNhan benhNhan, RedirectAttributes redirectAttributes) {
        try {
            benhNhanService.themMoiBenhNhan(benhNhan);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm mới Bệnh nhân và cấp tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu: " + e.getMessage());
        }
        return "redirect:/benhnhan/index"; 
    }

    // =================================================================
    // 3. CẬP NHẬT THÔNG TIN
    // =================================================================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<BenhNhan> opt = benhNhanService.findById(id);
        
        if (opt.isPresent()) {
            model.addAttribute("benhNhan", opt.get());
            return "benhnhan/edit"; 
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hồ sơ Bệnh nhân!");
            return "redirect:/benhnhan/index";
        }
    }

    @PostMapping("/edit")
    public String updateBenhNhan(@ModelAttribute BenhNhan benhNhan, RedirectAttributes redirectAttributes) {
        try {
            benhNhanService.update(benhNhan);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ Bệnh nhân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật: " + e.getMessage());
        }
        return "redirect:/benhnhan/index"; 
    }

    // =================================================================
    // 4. XÓA BỆNH NHÂN
    // =================================================================
    @PostMapping("/delete/{id}")
    public String deleteBenhNhan(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            benhNhanService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa Bệnh nhân và tài khoản liên kết khỏi hệ thống!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa do ràng buộc dữ liệu hệ thống!");
        }
        return "redirect:/benhnhan/index";
    }
}