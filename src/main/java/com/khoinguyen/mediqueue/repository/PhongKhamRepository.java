package com.khoinguyen.mediqueue.repository;

import com.khoinguyen.mediqueue.entity.PhongKham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PhongKhamRepository extends JpaRepository<PhongKham, Long> {
    
}
