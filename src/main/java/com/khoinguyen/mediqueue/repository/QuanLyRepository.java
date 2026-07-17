package com.khoinguyen.mediqueue.repository;

import com.khoinguyen.mediqueue.entity.QuanLy;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuanLyRepository extends JpaRepository<QuanLy, Integer> {
    Optional<QuanLy> findByTaiKhoan_TenDangNhap(String tenDangNhap);
    Optional<QuanLy> findBySoDienThoai(String soDienThoai);
}
