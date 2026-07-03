package com.khoinguyen.mediqueue.repository;

import com.khoinguyen.mediqueue.entity.ChuyenKhoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChuyenKhoaRepository extends JpaRepository<ChuyenKhoa, Long> {
    
}
