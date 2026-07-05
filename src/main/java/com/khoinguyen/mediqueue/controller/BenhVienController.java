package com.khoinguyen.mediqueue.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khoinguyen.mediqueue.entity.BenhVien;
import com.khoinguyen.mediqueue.service.BenhVienService;

@Controller 
@CrossOrigin
public class BenhVienController {

    private final BenhVienService benhVienService;

    // Dependency Injection qua Constructor (Chuẩn khuyến nghị của Spring)
    public BenhVienController(BenhVienService benhVienService) {
        this.benhVienService = benhVienService;
    }

    // =================================================================
    // 1. KHU VỰC GIAO DIỆN (WEB MVC) - Thymeleaf HTML
    // =================================================================
    
    @GetMapping("/benhvien/index")
    public String index(Model model) {
        List<BenhVien> danhSachCoSo = benhVienService.findAll();
        model.addAttribute("danhSachCoSo", danhSachCoSo);
        return "benhvien/index"; 
    }

    @GetMapping("/benhvien/create")
    public String create(Model model) {
        model.addAttribute("benhVien", new BenhVien());
        return "benhvien/create"; 
    }

    @PostMapping("/benhvien/create")
    public String saveBenhVien(@ModelAttribute BenhVien benhVien, 
                               @RequestParam(value = "HinhAnhFiles", required = false) List<MultipartFile> hinhAnhFiles,
                               RedirectAttributes redirectAttributes) {
        try {
            // Lưu ảnh và gán chuỗi đường dẫn vào entity
            String hinhAnhChuoi = saveImages(hinhAnhFiles);
            benhVien.setHinhAnh(hinhAnhChuoi);
            
            benhVienService.save(benhVien);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm mới bệnh viện thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi lưu: " + e.getMessage());
        }
        return "redirect:/benhvien/index"; 
    }


    @GetMapping("/benhvien/details/{id}")
    public String details(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<BenhVien> opt = benhVienService.findById(id);
        
        if (opt.isPresent()) {
            model.addAttribute("benhVien", opt.get());
            return "benhvien/details"; // Trả về file templates/benhvien/Details.html
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy dữ liệu bệnh viện!");
            return "redirect:/benhvien/index";
        }
    }

    @GetMapping("/benhvien/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<BenhVien> opt = benhVienService.findById(id);
        
        if (opt.isPresent()) {
            model.addAttribute("benhVien", opt.get());
            return "benhvien/edit"; 
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy dữ liệu bệnh viện cần sửa!");
            return "redirect:/benhvien/index";
        }
    }

    @PostMapping("/benhvien/edit")
    public String updateBenhVien(@ModelAttribute BenhVien benhVien, 
                                @RequestParam(value = "HinhAnhFiles", required = false) List<MultipartFile> hinhAnhFiles,
                                @RequestParam(value = "DeletedImageIds", required = false) String deletedImageIds, // 1. Nhận danh sách ảnh cần xóa
                                RedirectAttributes redirectAttributes) {
        try {
            // Lấy thông tin bệnh viện cũ từ Database để đối chiếu
            Optional<BenhVien> oldBenhVienOpt = benhVienService.findById(benhVien.getMaBenhVien());
            
            if (oldBenhVienOpt.isPresent()) {
                BenhVien oldBenhVien = oldBenhVienOpt.get();
                String currentImagesStr = oldBenhVien.getHinhAnh();
                
                // Khởi tạo danh sách chứa các ảnh hiện tại
                List<String> currentImagesList = new ArrayList<>();
                if (currentImagesStr != null && !currentImagesStr.trim().isEmpty()) {
                    currentImagesList = new ArrayList<>(Arrays.asList(currentImagesStr.split(",")));
                }

                // 2. XỬ LÝ XÓA ẢNH: Lọc bỏ các ảnh mà người dùng đã bấm xóa trên giao diện
                if (deletedImageIds != null && !deletedImageIds.isEmpty()) {
                   // Đổi từ: deletedImageIds.split(",")
                    List<String> imagesToDelete = Arrays.asList(deletedImageIds.split("[,;]"));
                    currentImagesList.removeAll(imagesToDelete);
                    
                    // (Tùy chọn) Tại đây bạn có thể viết thêm code xóa file vật lý trong thư mục /images/ 
                    // bằng java.nio.file.Files.deleteIfExists() để tiết kiệm dung lượng ổ cứng.
                }

                // 3. XỬ LÝ THÊM ẢNH MỚI: Nếu có upload thêm ảnh, lưu lại và nối vào danh sách hiện có
                if (hinhAnhFiles != null && !hinhAnhFiles.isEmpty() && !hinhAnhFiles.get(0).isEmpty()) {
                    String newImagesStr = saveImages(hinhAnhFiles);
                    if (!newImagesStr.isEmpty()) {
                        // Đổi từ: currentImagesStr.split(",")
                        currentImagesList = new ArrayList<>(Arrays.asList(currentImagesStr.split("[,;]")));
                    }
                }

                // 4. Gộp danh sách cuối cùng lại thành chuỗi phân cách bởi dấu phẩy và gán vào entity
                benhVien.setHinhAnh(String.join(",", currentImagesList));
            }
            
            benhVienService.save(benhVien);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin bệnh viện thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật: " + e.getMessage());
        }
        
        return "redirect:/benhvien/index"; 
    }

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

    // Hàm helper phục vụ lưu trữ file cục bộ
    private String saveImages(List<MultipartFile> files) throws Exception {
        if (files == null || files.isEmpty()) return "";
        
        List<String> fileNames = new ArrayList<>();
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
                
                fileNames.add("/images/" + fileName);
            }
        }
        return String.join(",", fileNames);
    }

    // =================================================================
    // 2. KHU VỰC API RESTFUL - Trả về dữ liệu JSON (cho Frontend/Mobile)
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
        
        // Cập nhật ảnh qua API nếu phía Client có truyền chuỗi hình ảnh mới
        if (benhVien.getHinhAnh() != null) {
            existing.setHinhAnh(benhVien.getHinhAnh());
        }

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
        // Tùy chỉnh phân trang mặc định lấy 5 phần tử đầu tiên khớp từ khóa
        Page<BenhVien> resultPage = benhVienService.timKiemVaPhanTrang(keyword, PageRequest.of(0, 5));
        return ResponseEntity.ok(resultPage.getContent());
    }
}