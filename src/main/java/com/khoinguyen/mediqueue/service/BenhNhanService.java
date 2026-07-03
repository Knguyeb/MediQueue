package com.khoinguyen.mediqueue.service;

import com.khoinguyen.mediqueue.entity.BenhNhan;
import com.khoinguyen.mediqueue.repository.BenhNhanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BenhNhanService {
    private final BenhNhanRepository benhNhanRepository;

    public BenhNhanService(BenhNhanRepository benhNhanRepository) {
        this.benhNhanRepository = benhNhanRepository;
    }

    public List<BenhNhan> findAll() { return benhNhanRepository.findAll(); }
    public Optional<BenhNhan> findById(Long id) { return benhNhanRepository.findById(id); }
    public BenhNhan save(BenhNhan b) { return benhNhanRepository.save(b); }
    public void deleteById(Long id) { benhNhanRepository.deleteById(id); }
    public boolean existsById(Long id) { return benhNhanRepository.existsById(id); }
}
