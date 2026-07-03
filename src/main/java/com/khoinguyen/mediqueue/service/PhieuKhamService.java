package com.khoinguyen.mediqueue.service;

import com.khoinguyen.mediqueue.entity.PhieuKham;
import com.khoinguyen.mediqueue.repository.PhieuKhamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhieuKhamService {
    private final PhieuKhamRepository phieuKhamRepository;

    public PhieuKhamService(PhieuKhamRepository phieuKhamRepository) {
        this.phieuKhamRepository = phieuKhamRepository;
    }

    public List<PhieuKham> findAll() { return phieuKhamRepository.findAll(); }
    public Optional<PhieuKham> findById(Long id) { return phieuKhamRepository.findById(id); }
    public PhieuKham save(PhieuKham p) { return phieuKhamRepository.save(p); }
    public void deleteById(Long id) { phieuKhamRepository.deleteById(id); }
    public boolean existsById(Long id) { return phieuKhamRepository.existsById(id); }
}
