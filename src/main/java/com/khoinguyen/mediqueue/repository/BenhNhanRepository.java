package com.khoinguyen.mediqueue.repository;

import com.khoinguyen.mediqueue.entity.BenhNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BenhNhanRepository extends JpaRepository<BenhNhan, Integer> {
    
    // Thêm hàm này để Spring Data JPA tự động viết câu SQL tìm theo SĐT
    Optional<BenhNhan> findBySoDienThoai(String soDienThoai);
}