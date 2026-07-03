package com.khoinguyen.mediqueue.controller;

import com.khoinguyen.mediqueue.entity.BenhVien;
import com.khoinguyen.mediqueue.service.BenhVienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Import để truyền dữ liệu thông báo khi redirect

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller 
@CrossOrigin
public class BenhVienController {

    private final BenhVienService benhVienService;

    public BenhVienController(BenhVienService benhVienService) {
        this.benhVienService = benhVienService;
    }

    // =================================================================
    // 1. KHU VỰC GIAO DIỆN (WEB MVC) - Trả về file HTML (Thymeleaf)
    // =================================================================
    
    @GetMapping("/benhvien/index")
    public String index(Model model) {
        // Lấy danh sách bệnh viện từ database
        List<BenhVien> danhSachCoSo = benhVienService.findAll();
        
        // Đẩy dữ liệu ra view Thymeleaf
        model.addAttribute("danhSachCoSo", danhSachCoSo);
        
        // Trả về file HTML nằm ở: src/main/resources/templates/benhvien/index.html
        return "benhvien/index"; 
    }

    @GetMapping("/benhvien/create")
    public String create(Model model) {
        // Khởi tạo một đối tượng trống để form trong Thymeleaf có thể binding dữ liệu
        model.addAttribute("benhVien", new BenhVien());
        
        // Trả về file HTML nằm ở: src/main/resources/templates/benhvien/create.html
        return "benhvien/create"; 
    }

   @PostMapping("/benhvien/create")
    public String saveBenhVien(@ModelAttribute BenhVien benhVien, 
                               @RequestParam(value = "HinhAnhFiles", required = false) List<MultipartFile> hinhAnhFiles,
                               RedirectAttributes redirectAttributes) {
        try {
            // 1. Gọi hàm lưu file để lấy chuỗi đường dẫn (ví dụ: "/images/a.jpg,/images/b.jpg")
            String hinhAnhChuoi = saveImages(hinhAnhFiles);
            
            // 2. Gán chuỗi đó vào Entity trước khi lưu xuống DB
            benhVien.setHinhAnh(hinhAnhChuoi);
            
            // 3. Lưu bệnh viện
            benhVienService.save(benhVien);
            
            redirectAttributes.addFlashAttribute("successMessage", "Thêm mới bệnh viện thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu: " + e.getMessage());
        }
        return "redirect:/benhvien/index"; 
    }

    // Hàm này mở form và truyền dữ liệu cũ của Bệnh viện lên giao diện
    @GetMapping("/benhvien/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<BenhVien> opt = benhVienService.findById(id);
        
        if (opt.isPresent()) {
            // Tìm thấy dữ liệu -> đẩy ra view
            model.addAttribute("benhVien", opt.get());
            return "benhvien/edit"; 
        } else {
            // Không tìm thấy -> báo lỗi và quay về danh sách
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy dữ liệu bệnh viện cần sửa!");
            return "redirect:/benhvien/index";
        }
    }

    // Hàm này nhận dữ liệu từ form Sửa gửi lên và lưu vào Database
    @PostMapping("/benhvien/edit")
    public String updateBenhVien(@ModelAttribute BenhVien benhVien, 
                                 @RequestParam(value = "HinhAnhFiles", required = false) List<MultipartFile> hinhAnhFiles,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Với Spring Data JPA, hàm save() tự động hiểu là UPDATE nếu đối tượng đã có ID (Khóa chính)
            benhVienService.save(benhVien);
            
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin bệnh viện thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật: " + e.getMessage());
        }
        
        return "redirect:/benhvien/index"; 
    }

    // Đổi thành @PostMapping để khớp với method="post" của thẻ form ẩn
    @PostMapping("/benhvien/delete/{id}")
    public String deleteBenhVienUI(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            if (benhVienService.existsById(id)) {
                benhVienService.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage", "Đã xóa bệnh viện khỏi hệ thống vĩnh viễn!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bệnh viện này trên hệ thống.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa bệnh viện này vì dữ liệu đang được sử dụng ở chức năng khác!");
        }
        
        return "redirect:/benhvien/index";
    }

    private String saveImages(List<MultipartFile> files) throws Exception {
        if (files == null || files.isEmpty()) return "";
        
        List<String> fileNames = new ArrayList<>();
        // Lưu vào thư mục static/images theo yêu cầu của bạn
        String uploadDir = "src/main/resources/static/images/";
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                // Đường dẫn lưu vào DB (khi hiển thị trong HTML, Spring sẽ tự hiểu nó nằm trong /images/)
                fileNames.add("/images/" + fileName);
            }
        }
        return String.join(",", fileNames);
    }

    // =================================================================
    // 2. KHU VỰC API - Trả về dữ liệu JSON cho Frontend (AJAX/Fetch)
    // =================================================================

    @GetMapping("/api/benhvien")
    @ResponseBody
    public List<BenhVien> getAll() {
        return benhVienService.findAll();
    }

    @GetMapping("/api/benhvien/{id}")
    @ResponseBody
    public ResponseEntity<BenhVien> getById(@PathVariable Integer id) {
        Optional<BenhVien> opt = benhVienService.findById(id);
        return opt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/api/benhvien")
    @ResponseBody
    public ResponseEntity<BenhVien> createApi(@RequestBody BenhVien benhVien) { 
        BenhVien saved = benhVienService.save(benhVien);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/api/benhvien/{id}")
    @ResponseBody
    public ResponseEntity<BenhVien> update(@PathVariable Integer id, @RequestBody BenhVien benhVien) {
        Optional<BenhVien> opt = benhVienService.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        BenhVien existing = opt.get();
        existing.setTenBenhVien(benhVien.getTenBenhVien());
        existing.setDiaChi(benhVien.getDiaChi());
        existing.setSoDienThoai(benhVien.getSoDienThoai());
        existing.setEmail(benhVien.getEmail());
        existing.setViDo(benhVien.getViDo());
        existing.setKinhDo(benhVien.getKinhDo());
        BenhVien saved = benhVienService.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/api/benhvien/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!benhVienService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        benhVienService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/benhvien/search")
    @ResponseBody
    public ResponseEntity<List<BenhVien>> search(@RequestParam("keyword") String keyword) {
        Page<BenhVien> resultPage = benhVienService.timKiemVaPhanTrang(keyword, PageRequest.of(0, 5));
        return ResponseEntity.ok(resultPage.getContent());
    }
}