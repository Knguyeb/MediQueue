package com.khoinguyen.mediqueue.service;

import com.khoinguyen.mediqueue.entity.PhongKham;
import com.khoinguyen.mediqueue.repository.PhongKhamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhongKhamService {
    private final PhongKhamRepository phongKhamRepository;

    public PhongKhamService(PhongKhamRepository phongKhamRepository) {
        this.phongKhamRepository = phongKhamRepository;
    }

    public List<PhongKham> findAll() { return phongKhamRepository.findAll(); }
    public Optional<PhongKham> findById(Long id) { return phongKhamRepository.findById(id); }
    public PhongKham save(PhongKham p) { return phongKhamRepository.save(p); }
    public void deleteById(Long id) { phongKhamRepository.deleteById(id); }
    public boolean existsById(Long id) { return phongKhamRepository.existsById(id); }
}
