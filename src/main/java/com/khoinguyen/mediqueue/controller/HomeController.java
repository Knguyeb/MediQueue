package com.khoinguyen.mediqueue.controller;

import com.khoinguyen.mediqueue.entity.BenhVien;
import com.khoinguyen.mediqueue.service.BenhVienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final BenhVienService benhVienService;

    public HomeController(BenhVienService benhVienService) {
        this.benhVienService = benhVienService;
    }

    @GetMapping({"/", "/home"})
    public String index(
            // Bắt tham số từ URL (ví dụ: /home?search=HaNoi&page=2)
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Model model) {

        // Cài đặt 8 bệnh viện cho 1 trang
        int pageSize = 8; 
        
        // Tính toán trang cho Spring Data (trang đầu tiên bắt đầu từ số 0)
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        // Gọi hàm phân trang & tìm kiếm từ Service
        Page<BenhVien> pageBenhVien = benhVienService.timKiemVaPhanTrang(search, pageable);

        // Truyền các biến cần thiết ra giao diện (phải khớp với Thymeleaf)
        model.addAttribute("danhSachBenhVien", pageBenhVien.getContent()); // Lấy đúng 8 cái
        model.addAttribute("currentPage", page);                           // Trang số mấy
        model.addAttribute("totalPages", pageBenhVien.getTotalPages());    // Tổng bao nhiêu trang
        model.addAttribute("searchKeyword", search);                       // Từ khóa đang tìm

        return "home/index";
    }
}