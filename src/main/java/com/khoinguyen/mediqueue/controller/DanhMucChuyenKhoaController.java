package com.khoinguyen.mediqueue.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khoinguyen.mediqueue.entity.DanhMucChuyenKhoa;
import com.khoinguyen.mediqueue.service.DanhMucChuyenKhoaService;

@Controller 
@CrossOrigin
public class DanhMucChuyenKhoaController {

    private final DanhMucChuyenKhoaService danhMucService;

    // Dependency Injection qua Constructor (Chuẩn khuyến nghị của Spring)
    public DanhMucChuyenKhoaController(DanhMucChuyenKhoaService danhMucService) {
        this.danhMucService = danhMucService;
    }

    // =================================================================
    // 1. KHU VỰC GIAO DIỆN (WEB MVC) - Thymeleaf HTML
    // =================================================================
    
    @GetMapping("/danhmucchuyenkhoa/index")
    public String index(Model model) {
        List<DanhMucChuyenKhoa> danhSachDanhMuc = danhMucService.findAll();
        model.addAttribute("danhSachDanhMuc", danhSachDanhMuc);
        return "danhmucchuyenkhoa/index"; 
    }

    @GetMapping("/danhmucchuyenkhoa/create")
    public String create(Model model) {
        model.addAttribute("danhMuc", new DanhMucChuyenKhoa());
        return "danhmucchuyenkhoa/create"; 
    }

    @PostMapping("/danhmucchuyenkhoa/create")
    public String saveDanhMuc(@ModelAttribute DanhMucChuyenKhoa danhMuc, 
                              RedirectAttributes redirectAttributes) {
        try {
            danhMucService.save(danhMuc);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm mới danh mục chuyên khoa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu: " + e.getMessage());
        }
        return "redirect:/danhmucchuyenkhoa/index"; 
    }

    @GetMapping("/danhmucchuyenkhoa/details/{id}")
    public String details(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<DanhMucChuyenKhoa> opt = danhMucService.findById(id);
        
        if (opt.isPresent()) {
            model.addAttribute("danhMuc", opt.get());
            return "danhmucchuyenkhoa/details"; 
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy dữ liệu danh mục!");
            return "redirect:/danhmucchuyenkhoa/index";
        }
    }

    @GetMapping("/danhmucchuyenkhoa/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<DanhMucChuyenKhoa> opt = danhMucService.findById(id);
        
        if (opt.isPresent()) {
            model.addAttribute("danhMuc", opt.get());
            return "danhmucchuyenkhoa/edit"; 
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy dữ liệu danh mục cần sửa!");
            return "redirect:/danhmucchuyenkhoa/index";
        }
    }

    @PostMapping("/danhmucchuyenkhoa/edit")
    public String updateDanhMuc(@ModelAttribute DanhMucChuyenKhoa danhMuc, 
                                RedirectAttributes redirectAttributes) {
        try {
            // Không có xử lý hình ảnh phức tạp, chỉ cần save ghi đè thông tin chữ
            danhMucService.save(danhMuc);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật: " + e.getMessage());
        }
        
        return "redirect:/danhmucchuyenkhoa/index"; 
    }

    @PostMapping("/danhmucchuyenkhoa/delete/{id}")
    public String deleteDanhMucUI(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            if (danhMucService.existsById(id)) {
                danhMucService.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage", "Đã xóa danh mục khỏi hệ thống!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy danh mục này trên hệ thống.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa danh mục này vì đang được liên kết với các Bệnh viện khác!");
        }
        
        return "redirect:/danhmucchuyenkhoa/index";
    }

    // =================================================================
    // 2. KHU VỰC API RESTFUL - Trả về dữ liệu JSON (cho Frontend/Mobile)
    // =================================================================

    @GetMapping("/api/danhmucchuyenkhoa")
    @ResponseBody
    public List<DanhMucChuyenKhoa> getAll() {
        return danhMucService.findAll();
    }

    @GetMapping("/api/danhmucchuyenkhoa/{id}")
    @ResponseBody
    public ResponseEntity<DanhMucChuyenKhoa> getById(@PathVariable Integer id) {
        Optional<DanhMucChuyenKhoa> opt = danhMucService.findById(id);
        return opt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/api/danhmucchuyenkhoa")
    @ResponseBody
    public ResponseEntity<?> createApi(@RequestBody DanhMucChuyenKhoa danhMuc) { 
        // 1. Kiểm tra trùng tên trước khi thêm mới
        if (danhMucService.kiemTraTrungTen(danhMuc.getTenDanhMuc())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Tên chuyên khoa '" + danhMuc.getTenDanhMuc() + "' đã tồn tại trong hệ thống!");
        }

        DanhMucChuyenKhoa saved = danhMucService.save(danhMuc);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/api/danhmucchuyenkhoa/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody DanhMucChuyenKhoa danhMuc) {
        Optional<DanhMucChuyenKhoa> opt = danhMucService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        DanhMucChuyenKhoa existing = opt.get();

        // 2. Kiểm tra trùng tên khi sửa (Nếu đổi sang tên khác VÀ tên đó đã bị người khác dùng)
        if (!existing.getTenDanhMuc().equalsIgnoreCase(danhMuc.getTenDanhMuc()) && 
            danhMucService.kiemTraTrungTen(danhMuc.getTenDanhMuc())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Tên chuyên khoa '" + danhMuc.getTenDanhMuc() + "' đã được sử dụng!");
        }

        existing.setTenDanhMuc(danhMuc.getTenDanhMuc());

        DanhMucChuyenKhoa saved = danhMucService.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/api/danhmucchuyenkhoa/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!danhMucService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        danhMucService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/danhmucchuyenkhoa/search")
    @ResponseBody
    public ResponseEntity<List<DanhMucChuyenKhoa>> search(@RequestParam("keyword") String keyword) {
        Page<DanhMucChuyenKhoa> resultPage = danhMucService.timKiemVaPhanTrang(keyword, PageRequest.of(0, 5));
        return ResponseEntity.ok(resultPage.getContent());
    }
}