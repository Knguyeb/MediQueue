package com.khoinguyen.mediqueue.repository;

import com.khoinguyen.mediqueue.entity.BacSi;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BacSiRepository extends JpaRepository<BacSi, Integer> {
    Optional<BacSi> findBySoDienThoai(String soDienThoai);
}
