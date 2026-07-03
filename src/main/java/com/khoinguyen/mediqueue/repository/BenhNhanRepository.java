package com.khoinguyen.mediqueue.repository;

import com.khoinguyen.mediqueue.entity.BenhNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenhNhanRepository extends JpaRepository<BenhNhan, Long> {
    
}
