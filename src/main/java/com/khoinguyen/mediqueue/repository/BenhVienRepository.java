package com.khoinguyen.mediqueue.repository;

import com.khoinguyen.mediqueue.entity.BenhVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenhVienRepository extends JpaRepository<BenhVien, Integer> {
    Page<BenhVien> findByTenBenhVienContainingIgnoreCaseOrDiaChiContainingIgnoreCase(String tenBenhVien, String diaChi, Pageable pageable);
}