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
    // 1. HIỂN THỊ DANH SÁCH (BẮT BUỘC PHẢI CÓ MÃ BỆNH VIỆN)
    // =================================================================
    @GetMapping("/index")
    public String index(@RequestParam(required = false) Integer maBenhVien, Model model, RedirectAttributes redirectAttributes) {
        if (maBenhVien == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn một Cơ sở y tế để quản lý phòng khám!");
            return "redirect:/benhvien/index";
        }
        model.addAttribute("danhSachPhongKham", phongKhamService.layDanhSachTheoBenhVien(maBenhVien));
        benhVienService.findById(maBenhVien).ifPresent(bv -> {
            model.addAttribute("benhVienChuQuan", bv.getTenBenhVien());
        });
        return "phongkham/index"; 
    }
    
    // =================================================================
    // 2. THÊM MỚI PHÒNG KHÁM
    // =================================================================
    @GetMapping("/create")
    public String create(@RequestParam(required = false) Integer maBenhVien, Model model) {
        PhongKham pk = new PhongKham();
        pk.setTrangThai(true);
        if (maBenhVien != null) {
            BenhVien bv = new BenhVien();
            bv.setMaBenhVien(maBenhVien);
            pk.setBenhVien(bv);
        }
        model.addAttribute("phongKham", pk);
        model.addAttribute("danhSachBenhVien", benhVienService.findAll());
        model.addAttribute("danhSachChuyenKhoa", danhMucChuyenKhoaService.findAll());
        
        List<com.khoinguyen.mediqueue.entity.BacSi> bacSiRanh = bacSiService.findAll().stream()
                .filter(bs -> bs.getPhongKham() == null)
                .collect(Collectors.toList());
        model.addAttribute("danhSachBacSi", bacSiRanh);
        return "phongkham/create"; 
    }

    @PostMapping("/create")
    public String savePhongKham(@ModelAttribute PhongKham phongKham, 
                                @RequestParam(name = "maBacSi", required = false) Integer maBacSi, 
                                RedirectAttributes redirectAttributes) {
        try {
            PhongKham pkDaLuu = phongKhamService.save(phongKham);
            if (maBacSi != null) {
                bacSiService.findById(maBacSi).ifPresent(bs -> {
                    bs.setPhongKham(pkDaLuu); 
                    bacSiService.update(bs);  
                });
            }
            redirectAttributes.addFlashAttribute("successMessage", "Thêm mới Phòng khám thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu: " + e.getMessage());
        }
        if (phongKham.getBenhVien() != null && phongKham.getBenhVien().getMaBenhVien() != null) {
            return "redirect:/phongkham/index?maBenhVien=" + phongKham.getBenhVien().getMaBenhVien();
        }
        return "redirect:/phongkham/index";
    }

    // =================================================================
    // 3. CẬP NHẬT THÔNG TIN PHÒNG KHÁM (BẢN CHUẨN XÁC)
    // =================================================================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<PhongKham> opt = phongKhamService.findById(id);
        if (opt.isPresent()) {
            PhongKham pk = opt.get();
            model.addAttribute("phongKham", pk);
            
            model.addAttribute("danhSachBenhVien", benhVienService.findAll());
            model.addAttribute("danhSachChuyenKhoa", danhMucChuyenKhoaService.findAll());
            
            // Tìm bác sĩ hiện tại đang giữ phòng này
            com.khoinguyen.mediqueue.entity.BacSi bacSiHienTai = bacSiService.findAll().stream()
                    .filter(bs -> bs.getPhongKham() != null && bs.getPhongKham().getMaPhongKham().equals(pk.getMaPhongKham()))
                    .findFirst().orElse(null);
            
            model.addAttribute("bacSiHienTai", bacSiHienTai);
            
            // Lọc danh sách bác sĩ rảnh + bác sĩ đang giữ phòng
            List<com.khoinguyen.mediqueue.entity.BacSi> bacSiHopLe = bacSiService.findAll().stream()
                    .filter(bs -> bs.getPhongKham() == null || 
                                 (bacSiHienTai != null && bs.getMaBacSi().equals(bacSiHienTai.getMaBacSi())))
                    .collect(Collectors.toList());
                    
            model.addAttribute("danhSachBacSi", bacSiHopLe);
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
            
            // Gỡ phòng của bác sĩ cũ nếu quản lý đổi người
            for (com.khoinguyen.mediqueue.entity.BacSi bs : bacSiService.findAll()) {
                if (bs.getPhongKham() != null && bs.getPhongKham().getMaPhongKham().equals(pkDaLuu.getMaPhongKham())) {
                    if (maBacSi == null || !bs.getMaBacSi().equals(maBacSi)) {
                        bs.setPhongKham(null);
                        bacSiService.update(bs);
                    }
                }
            }
            
            // Cập nhật người mới
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
        if (phongKham.getBenhVien() != null && phongKham.getBenhVien().getMaBenhVien() != null) {
            return "redirect:/phongkham/index?maBenhVien=" + phongKham.getBenhVien().getMaBenhVien();
        }
        return "redirect:/phongkham/index"; 
    }

    // =================================================================
    // 4. XÓA PHÒNG KHÁM
    // =================================================================
    @PostMapping("/delete/{id}")
    public String deletePhongKham(@PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            Optional<PhongKham> opt = phongKhamService.findById(id);
            if (opt.isPresent()) {
                PhongKham pk = opt.get();
                if (pk.getBacSi() != null) {
                    com.khoinguyen.mediqueue.entity.BacSi bs = pk.getBacSi();
                    bs.setPhongKham(null);
                    bacSiService.update(bs);
                }
                phongKhamService.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage", "Đã xóa Phòng khám thành công!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa: " + e.getMessage());
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/phongkham/index");
    }

    // =================================================================
    // 5. THAY ĐỔI TRẠNG THÁI
    // =================================================================
    @PostMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            Optional<PhongKham> opt = phongKhamService.findById(id);
            if (opt.isPresent()) {
                PhongKham pk = opt.get();
                pk.setTrangThai(!pk.getTrangThai()); 
                phongKhamService.save(pk);
                String tb = pk.getTrangThai() ? "Đã MỞ lại phòng khám!" : "Đã TẠM ĐÓNG phòng khám!";
                redirectAttributes.addFlashAttribute("successMessage", tb);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/phongkham/index");
    }
}