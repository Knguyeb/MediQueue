package com.khoinguyen.mediqueue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.khoinguyen.mediqueue.entity.PhongKham;
import java.util.List;

public interface PhongKhamRepository extends JpaRepository<PhongKham, Integer> {
    List<PhongKham> findByBenhVien_MaBenhVien(Integer maBenhVien);
}