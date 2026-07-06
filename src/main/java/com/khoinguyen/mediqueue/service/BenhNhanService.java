package com.khoinguyen.mediqueue.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.khoinguyen.mediqueue.entity.BenhNhan;
import com.khoinguyen.mediqueue.entity.TaiKhoan;
import com.khoinguyen.mediqueue.entity.VaiTro;
import com.khoinguyen.mediqueue.repository.BenhNhanRepository;

@Service
public class BenhNhanService {

    private final BenhNhanRepository benhNhanRepository;
    private final TaiKhoanService taiKhoanService;

    public BenhNhanService(BenhNhanRepository benhNhanRepository, TaiKhoanService taiKhoanService) {
        this.benhNhanRepository = benhNhanRepository;
        this.taiKhoanService = taiKhoanService;
    }

    public List<BenhNhan> findAll() {
        return benhNhanRepository.findAll();
    }

    public Optional<BenhNhan> findById(Integer id) {
        return benhNhanRepository.findById(id);
    }

    // =================================================================
    // HÀM THÊM MỚI BỆNH NHÂN (TỰ ĐỘNG TẠO TÀI KHOẢN)
    // =================================================================
    public BenhNhan themMoiBenhNhan(BenhNhan benhNhan) {
        // 1. Gọi Service tạo tài khoản tự động với Vai trò là BENH_NHAN
        TaiKhoan taiKhoanMoi = taiKhoanService.taoTaiKhoanTuDong(benhNhan.getHoTen(), VaiTro.BENH_NHAN);
        
        // 2. Gắn tài khoản vừa tạo vào hồ sơ Bệnh nhân
        benhNhan.setTaiKhoan(taiKhoanMoi);
        
        // 3. Lưu toàn bộ xuống Database
        return benhNhanRepository.save(benhNhan);
    }

    public BenhNhan update(BenhNhan benhNhan) {
        return benhNhanRepository.save(benhNhan);
    }

    public void deleteById(Integer id) {
        benhNhanRepository.deleteById(id);
    }
}