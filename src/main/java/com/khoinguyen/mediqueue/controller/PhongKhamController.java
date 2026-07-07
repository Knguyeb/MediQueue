package com.khoinguyen.mediqueue.controller;

import java.util.Optional;
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

import com.khoinguyen.mediqueue.entity.BenhVien;
import com.khoinguyen.mediqueue.entity.PhongKham;
import com.khoinguyen.mediqueue.service.BacSiService;
import com.khoinguyen.mediqueue.service.BenhVienService;
import com.khoinguyen.mediqueue.service.DanhMucChuyenKhoaService;
import com.khoinguyen.mediqueue.service.PhongKhamService;

@Controller
@RequestMapping("/phongkham")
public class PhongKhamController {

    private final PhongKhamService phongKhamService;
    private final BenhVienService benhVienService;
    private final BacSiService bacSiService;
    private final DanhMucChuyenKhoaService danhMucChuyenKhoaService;

    // Inject tất cả 4 service cần thiết
    public PhongKhamController(PhongKhamService phongKhamService, 
                               BenhVienService benhVienService, 
                               BacSiService bacSiService,
                               DanhMucChuyenKhoaService danhMucChuyenKhoaService) {
        this.phongKhamService = phongKhamService;
        this.benhVienService = benhVienService;
        this.bacSiService = bacSiService;
        this.danhMucChuyenKhoaService = danhMucChuyenKhoaService;
    }

    // =================================================================
    // 1. HIỂN THỊ DANH SÁCH (HỨNG PARAM TỪ NÚT BẤM)
    // =================================================================
    @GetMapping("/index")
    public String index(@RequestParam(required = false) Integer maBenhVien, Model model) {
        if (maBenhVien != null) {
            model.addAttribute("danhSachPhongKham", phongKhamService.layDanhSachTheoBenhVien(maBenhVien));
            benhVienService.findById(maBenhVien).ifPresent(bv -> {
                model.addAttribute("benhVienChuQuan", bv.getTenBenhVien());
            });
        } else {
            model.addAttribute("danhSachPhongKham", phongKhamService.findAll());
        }
        return "phongkham/index"; 
    }
    
    // =================================================================
    // 2. THÊM MỚI PHÒNG KHÁM CÓ GẮN BÁC SĨ & CHUYÊN KHOA
    // =================================================================
    @GetMapping("/create")
    public String create(@RequestParam(required = false) Integer maBenhVien, Model model) {
        PhongKham pk = new PhongKham();
        pk.setTrangThai(true); // Trạng thái mặc định luôn là True
        
        // Nếu bấm từ quản lý bệnh viện cụ thể, set sẵn bệnh viện đó
        if (maBenhVien != null) {
            BenhVien bv = new BenhVien();
            bv.setMaBenhVien(maBenhVien);
            pk.setBenhVien(bv);

            System.out.println("Đang tạo phòng cho Bệnh viện ID: " + maBenhVien);
        }
        
        model.addAttribute("phongKham", pk);
        
        // Truyền 3 danh sách xuống để View xử lý Select Box và Javascript lọc dữ liệu
        model.addAttribute("danhSachBenhVien", benhVienService.findAll());
        model.addAttribute("danhSachChuyenKhoa", danhMucChuyenKhoaService.findAll());
        model.addAttribute("danhSachBacSi", bacSiService.findAll());
        
        return "phongkham/create"; 
    }

    @PostMapping("/create")
    public String savePhongKham(@ModelAttribute PhongKham phongKham, 
                                @RequestParam(name = "maBacSi", required = false) Integer maBacSi, 
                                RedirectAttributes redirectAttributes) {
        try {
            // 1. Lưu Phòng khám trước để Database sinh ra mã ID (maPhongKham)
            PhongKham pkDaLuu = phongKhamService.save(phongKham);
            
            // 2. Nếu quản lý có chọn Bác sĩ phụ trách, tiến hành cập nhật Bác sĩ đó
            if (maBacSi != null) {
                bacSiService.findById(maBacSi).ifPresent(bs -> {
                    bs.setPhongKham(pkDaLuu); // Gắn phòng vừa tạo cho bác sĩ
                    bacSiService.update(bs);  // Lưu bác sĩ xuống Database
                });
            }

            redirectAttributes.addFlashAttribute("successMessage", "Thêm mới Phòng khám thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu: " + e.getMessage());
        }
        return "redirect:/phongkham/index"; 
    }

    // =================================================================
    // 3. CẬP NHẬT THÔNG TIN PHÒNG KHÁM
    // =================================================================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<PhongKham> opt = phongKhamService.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("phongKham", opt.get());
            // Cần truyền lại đủ 3 danh sách để Load dropdown
            model.addAttribute("danhSachBenhVien", benhVienService.findAll());
            model.addAttribute("danhSachChuyenKhoa", danhMucChuyenKhoaService.findAll());
            model.addAttribute("danhSachBacSi", bacSiService.findAll());
            return "phongkham/edit"; 
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hồ sơ Phòng khám!");
            return "redirect:/phongkham/index";
        }
    }

    @PostMapping("/edit")
    public String updatePhongKham(@ModelAttribute PhongKham phongKham, 
                                  @RequestParam(name = "maBacSi", required = false) Integer maBacSi,
                                  RedirectAttributes redirectAttributes) {
        try {
            PhongKham pkDaLuu = phongKhamService.save(phongKham);
            
            // Nếu có chọn Bác sĩ phụ trách thì set cho Bác sĩ
            if (maBacSi != null) {
                bacSiService.findById(maBacSi).ifPresent(bs -> {
                    bs.setPhongKham(pkDaLuu);
                    bacSiService.update(bs);
                });
            }
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ Phòng khám thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật: " + e.getMessage());
        }
        return "redirect:/phongkham/index"; 
    }

    // =================================================================
    // 4. XÓA PHÒNG KHÁM
    // =================================================================
    @PostMapping("/delete/{id}")
    public String deletePhongKham(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            phongKhamService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa Phòng khám khỏi hệ thống!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa do ràng buộc dữ liệu hệ thống!");
        }
        return "redirect:/phongkham/index";
    }

    // =================================================================
    // 5. THAY ĐỔI TRẠNG THÁI (BẬT / TẮT NHANH)
    // =================================================================
    @PostMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            Optional<PhongKham> opt = phongKhamService.findById(id);
            if (opt.isPresent()) {
                PhongKham pk = opt.get();
                // Đảo ngược trạng thái hiện tại (Đang bật -> Tắt, Đang tắt -> Bật)
                pk.setTrangThai(!pk.getTrangThai()); 
                phongKhamService.save(pk);
                
                String tb = pk.getTrangThai() ? "Đã MỞ lại phòng khám!" : "Đã TẠM ĐÓNG phòng khám!";
                redirectAttributes.addFlashAttribute("successMessage", tb);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
        }
        
        // Lấy lại đường dẫn URL trước khi bấm nút (có thể chứa đuôi ?maBenhVien=...)
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/phongkham/index");
    }
}