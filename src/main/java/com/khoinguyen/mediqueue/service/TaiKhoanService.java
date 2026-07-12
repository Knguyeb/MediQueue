package com.khoinguyen.mediqueue.service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.khoinguyen.mediqueue.entity.TaiKhoan;
import com.khoinguyen.mediqueue.entity.VaiTro;
import com.khoinguyen.mediqueue.helper.PasswordHelper;
import com.khoinguyen.mediqueue.repository.TaiKhoanRepository;

@Service
public class TaiKhoanService {

    private final TaiKhoanRepository taiKhoanRepository;

    public TaiKhoanService(TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    public List<TaiKhoan> findAll() {
        return taiKhoanRepository.findAll();
    }

    public Optional<TaiKhoan> findById(Integer id) {
        return taiKhoanRepository.findById(id);
    }

    public TaiKhoan save(TaiKhoan taiKhoan) {
        return taiKhoanRepository.save(taiKhoan);
    }

    public Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap) {
        return taiKhoanRepository.findByTenDangNhap(tenDangNhap);
    }

    // =====================================================================
    // HÀM QUAN TRỌNG: TỰ ĐỘNG TẠO TÀI KHOẢN TỪ HỌ TÊN
    // =====================================================================
    public TaiKhoan taoTaiKhoanTuDong(String hoTen, VaiTro vaiTro) {
        TaiKhoan tkMoi = new TaiKhoan();
        
        // 1. Sinh tên đăng nhập duy nhất
        String tenDangNhap;
        do {
            tenDangNhap = taoTenDangNhap(hoTen);
        } while (taiKhoanRepository.existsByTenDangNhap(tenDangNhap)); // Lặp nếu vô tình trùng 5 số

        tkMoi.setTenDangNhap(tenDangNhap);

        // 2. Hash mật khẩu mặc định là "123" bằng PasswordHelper của bạn
        tkMoi.setMatKhau(PasswordHelper.hashPassword("123"));
        
        // 3. Set vai trò và trạng thái
        tkMoi.setVaiTro(vaiTro);
        tkMoi.setTrangThai(true);

        return tkMoi;
    }

    // Hàm phụ trợ: Chuyển "Nguyễn Văn A" -> "NVA12345"
    private String taoTenDangNhap(String fullName) {
        // Xóa dấu tiếng Việt (VD: Nguyễn -> Nguyen)
        String normalized = Normalizer.normalize(fullName, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("Đ", "D").replaceAll("đ", "d");
                
        // Cắt theo khoảng trắng và lấy chữ cái đầu
        String[] words = normalized.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
        }
        
        // Random 5 số (00000 -> 99999)
        Random random = new Random();
        String randomDigits = String.format("%05d", random.nextInt(100000));
        
        return initials.toString().toUpperCase() + randomDigits;
    }
}