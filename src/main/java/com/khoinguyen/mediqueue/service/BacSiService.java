package com.khoinguyen.mediqueue.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.khoinguyen.mediqueue.entity.BacSi;
import com.khoinguyen.mediqueue.entity.TaiKhoan;
import com.khoinguyen.mediqueue.entity.VaiTro;
import com.khoinguyen.mediqueue.repository.BacSiRepository;

@Service
public class BacSiService {

    private final BacSiRepository bacSiRepository;
    private final TaiKhoanService taiKhoanService;

    // Inject cả 2 dependency để sử dụng chéo
    public BacSiService(BacSiRepository bacSiRepository, TaiKhoanService taiKhoanService) {
        this.bacSiRepository = bacSiRepository;
        this.taiKhoanService = taiKhoanService;
    }

    public List<BacSi> findAll() {
        return bacSiRepository.findAll();
    }

    public Optional<BacSi> findById(Integer id) {
        return bacSiRepository.findById(id);
    }

    // =================================================================
    // HÀM THÊM MỚI BÁC SĨ (CÓ TỰ ĐỘNG TẠO TÀI KHOẢN)
    // =================================================================
    public BacSi themMoiBacSi(BacSi bacSi) {
        // 1. Gọi Service tạo tài khoản tự động với Vai trò là BAC_SI
        TaiKhoan taiKhoanMoi = taiKhoanService.taoTaiKhoanTuDong(bacSi.getHoTen(), VaiTro.BAC_SI);
        
        // 2. Gắn tài khoản vừa tạo vào hồ sơ Bác sĩ
        bacSi.setTaiKhoan(taiKhoanMoi);
        
        // 3. Lưu toàn bộ xuống Database
        return bacSiRepository.save(bacSi);
    }

    // Hàm cập nhật (Chỉ cập nhật thông tin, không đụng tới Tài khoản)
    public BacSi update(BacSi bacSi) {
        return bacSiRepository.save(bacSi);
    }

    public void deleteById(Integer id) {
        bacSiRepository.deleteById(id);
    }

    public Optional<BacSi> findBySoDienThoai(String soDienThoai) {
        return bacSiRepository.findBySoDienThoai(soDienThoai);
    }
}