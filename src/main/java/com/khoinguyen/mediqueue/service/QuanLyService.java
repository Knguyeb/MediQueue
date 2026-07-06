package com.khoinguyen.mediqueue.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.khoinguyen.mediqueue.entity.QuanLy;
import com.khoinguyen.mediqueue.entity.TaiKhoan;
import com.khoinguyen.mediqueue.entity.VaiTro;
import com.khoinguyen.mediqueue.repository.QuanLyRepository;

@Service
public class QuanLyService {

    private final QuanLyRepository quanLyRepository;
    private final TaiKhoanService taiKhoanService;

    public QuanLyService(QuanLyRepository quanLyRepository, TaiKhoanService taiKhoanService) {
        this.quanLyRepository = quanLyRepository;
        this.taiKhoanService = taiKhoanService;
    }

    public List<QuanLy> findAll() {
        return quanLyRepository.findAll();
    }

    public Optional<QuanLy> findById(Integer id) {
        return quanLyRepository.findById(id);
    }

    // =================================================================
    // HÀM THÊM MỚI QUẢN LÝ (TỰ ĐỘNG TẠO TÀI KHOẢN)
    // =================================================================
    public QuanLy themMoiQuanLy(QuanLy quanLy) {
        // 1. Gọi Service tạo tài khoản tự động với Vai trò là QUAN_LY
        TaiKhoan taiKhoanMoi = taiKhoanService.taoTaiKhoanTuDong(quanLy.getHoTen(), VaiTro.QUAN_LY);
        
        // 2. Gắn tài khoản vừa tạo vào hồ sơ Quản lý
        quanLy.setTaiKhoan(taiKhoanMoi);
        
        // 3. Lưu toàn bộ xuống Database
        return quanLyRepository.save(quanLy);
    }

    public QuanLy update(QuanLy quanLy) {
        return quanLyRepository.save(quanLy);
    }

    public void deleteById(Integer id) {
        quanLyRepository.deleteById(id);
    }
}