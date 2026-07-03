package com.khoinguyen.mediqueue.repository;

import com.khoinguyen.mediqueue.entity.BacSi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacSiRepository extends JpaRepository<BacSi, Long> {
}