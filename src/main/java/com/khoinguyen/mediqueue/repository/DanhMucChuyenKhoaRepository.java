package com.khoinguyen.mediqueue.repository;

import com.khoinguyen.mediqueue.entity.DanhMucChuyenKhoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DanhMucChuyenKhoaRepository extends JpaRepository<DanhMucChuyenKhoa, Integer> {
    Page<DanhMucChuyenKhoa> findByTenDanhMucContainingIgnoreCase(String keyword, Pageable pageable);
    boolean existsByTenDanhMucIgnoreCase(String tenDanhMuc);
}