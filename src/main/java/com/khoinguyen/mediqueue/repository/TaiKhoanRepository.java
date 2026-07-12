package com.khoinguyen.mediqueue.repository;

import com.khoinguyen.mediqueue.entity.TaiKhoan;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    boolean existsByTenDangNhap(String tenDangNhap);
    Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap);
}
