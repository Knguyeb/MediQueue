package com.khoinguyen.mediqueue.repository;

import com.khoinguyen.mediqueue.entity.PhieuKham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuKhamRepository extends JpaRepository<PhieuKham, Long> {
    
}
